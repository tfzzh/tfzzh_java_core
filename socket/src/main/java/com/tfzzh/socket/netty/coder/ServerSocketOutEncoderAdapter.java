/**
 * @author Weijie Xu
 * @dateTime 2013-8-30 下午2:11:49
 */
package com.tfzzh.socket.netty.coder;

import java.io.ByteArrayOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tfzzh.socket.bean.OutputSocketMessageBean;
import com.tfzzh.socket.netty.tools.ChannelPool;
import com.tfzzh.socket.netty.tools.RequestNettySession;
import com.tfzzh.socket.tools.RequestPool;
import com.tfzzh.socket.tools.SocketConstants;
import com.tfzzh.socket.tools.TfzzhDataOutputStream;
import com.tfzzh.socket.webservice.WebSocketParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * 针对AS的Socket消息输出编码适配器
 * 
 * @author Weijie Xu
 * @dateTime 2013-8-30 下午2:11:49
 */
@ChannelHandler.Sharable
public class ServerSocketOutEncoderAdapter extends ChannelOutboundHandlerAdapter {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年12月5日 下午8:55:47
	 */
	private final Logger log = LogManager.getLogger(this.getClass());

	/**
	 * 分段索引号
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月8日_下午3:00:22
	 */
	private int sectionIndex = 0;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年11月11日 上午9:53:04
	 * @param spaceRequestPool 所在空间名
	 */
	public ServerSocketOutEncoderAdapter(final RequestPool spaceRequestPool) {
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2013-8-30 下午4:34:31
	 * @see io.netty.channel.ChannelOutboundHandlerAdapter#write(io.netty.channel.ChannelHandlerContext, java.lang.Object, io.netty.channel.ChannelPromise)
	 */
	@Override
	public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
		final long l1 = System.currentTimeMillis();
		if (msg instanceof OutputSocketMessageBean) {
			// 进行对消息的组合与发送
			final ByteArrayOutputStream stream = new ByteArrayOutputStream();
			final TfzzhDataOutputStream tdos = new TfzzhDataOutputStream(stream);
			final OutputSocketMessageBean omb = (OutputSocketMessageBean) msg;
			omb.write(tdos);
			final ByteBuf buf = Unpooled.buffer();
			final RequestNettySession rs = ChannelPool.getInstance().getChannelSession(ctx.channel());
			switch (rs.getClientType()) {
			case Flash:
			case Normal:
				// 一般接口，需要增加长度信息，websocket不存在长度部分
				final int l = stream.size() + 4;
				// 先放入长度
				buf.writeInt(l);
				break;
			case WebSocket:
				// 无操作
			}
			// 放入请求码
			buf.writeInt(omb.getCode());
			// 放入消息字节流
			buf.writeBytes(stream.toByteArray());
			int l = 0;
			switch (rs.getClientType()) {
			case WebSocket:
				final byte[] bbs = new byte[buf.readableBytes()];
				buf.readBytes(bbs);
				if (SocketConstants.WEBSOKECT_CONTENT_LENGTH_MAX == 0) {
					final byte[] wsBak = this.makeWebScoketMessage(bbs);
					l = wsBak.length;
					try {
						final ByteBuf newBuf = Unpooled.wrappedBuffer(wsBak);
						ctx.writeAndFlush(newBuf, promise);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				} else {
					final short totleIndex = (short) ((bbs.length / SocketConstants.WEBSOKECT_CONTENT_LENGTH_MAX) + 1);
					if (totleIndex < 2) {
						final byte[] wsBak = this.makeWebScoketMessage(bbs);
						l = wsBak.length;
						try {
							final ByteBuf newBuf = Unpooled.wrappedBuffer(wsBak);
							ctx.writeAndFlush(newBuf, promise);
						} catch (final Exception e) {
							e.printStackTrace();
						}
					} else {
						byte[] wsBak;
						final int id = this.sectionIndex++;
						short ind = 1;
						final WebSocketParser df = new WebSocketParser(bbs, true);
						while (null != (wsBak = df.getOutBytes(SocketConstants.WEBSOKECT_CONTENT_LENGTH_MAX, id, totleIndex, ind++))) {
							l += wsBak.length;
							try {
								final ByteBuf newBuf = Unpooled.wrappedBuffer(wsBak);
								// ctx.write(newBuf);
								ctx.writeAndFlush(newBuf);
							} catch (final Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				break;
			case Flash:
			case Normal:
				ctx.writeAndFlush(buf, promise);
				break;
			}
			// 进行消息发送
			tdos.close();
			stream.close();
			if (this.log.isDebugEnabled()) {
				final String sendStr;
				if (null != omb.getMsg()) {
					sendStr = omb.getMsg().toString();
				} else {
					sendStr = "";
				}
				this.log.debug(new StringBuilder(100).append("use[").append(System.currentTimeMillis() - l1).append("] [").append(ctx.channel().remoteAddress().toString()).append("] Send MsgBean [").append(ctx.channel().localAddress().toString()).append("] <").append(omb.getCode()).append("> msg(").append(l).append(") >> ").append(sendStr.length() > 500 ? sendStr.substring(0, 500) : sendStr));
			}
		} else {
			// 针对各种string消息，头信息验证消息的回传
			final ByteBuf buf = Unpooled.buffer();
			buf.writeBytes(msg.toString().getBytes());
			ctx.writeAndFlush(buf, promise);
			if (this.log.isDebugEnabled()) {
				this.log.debug(new StringBuilder(50).append("use[").append(System.currentTimeMillis() - l1).append("] Send Msg [").append(ctx.channel().remoteAddress().toString()).append("] <").append(msg.getClass().getName()).append("> msg >> ").append(msg.toString()));
			}
		}
	}

	/**
	 * 进行针对webSocket的消息处理
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月11日 下午4:50:13
	 * @param msg 待处理的消息
	 * @return 处理后的消息
	 */
	private byte[] makeWebScoketMessage(final byte[] msg) {
		final WebSocketParser df = new WebSocketParser(msg, true);
		return df.getOutBytes();
	}
}
