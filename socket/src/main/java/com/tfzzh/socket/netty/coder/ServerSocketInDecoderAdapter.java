/**
 * @author Weijie Xu
 * @dateTime 2013-8-30 下午2:08:09
 */
package com.tfzzh.socket.netty.coder;

import java.util.Arrays;

import com.tfzzh.exception.InstallException;
import com.tfzzh.socket.bean.InputMessageBean;
import com.tfzzh.socket.netty.tools.ChannelPool;
import com.tfzzh.socket.netty.tools.RequestNettySession;
import com.tfzzh.socket.tools.RequestPool;
import com.tfzzh.socket.tools.ValidateControl;
import com.tfzzh.socket.webservice.WebSocketParser;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * 针对AS的Socket请求消息传入解码适配器
 * 
 * @author Weijie Xu
 * @dateTime 2013-8-30 下午2:08:09
 */
public class ServerSocketInDecoderAdapter extends BaseNettyDecoderAdapter {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年10月31日 下午7:10:20
	 * @param spaceRequestPool 所在空间名
	 * @param validateControl 验证控制器
	 */
	public ServerSocketInDecoderAdapter(final RequestPool spaceRequestPool, final ValidateControl validateControl) {
		super(spaceRequestPool, validateControl);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2013-9-4 上午10:48:33
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
		// 对应的得到请求会话
		// 开始时间
		final long startTime = System.currentTimeMillis();
		final RequestNettySession rs = ChannelPool.getInstance().getChannelSession(ctx.channel());
		// 是否需要解析
		switch (super.isSecurityRequest(rs, (ByteBuf) msg)) {
		case 0: // 已经通过的
			break;
		case 1: // 验证头信息有问题的
		{
			ctx.fireChannelRead(msg);
			if (this.log.isErrorEnabled()) {
				this.log.error("[" + ctx.channel().localAddress() + "] Receive msg [" + ctx.channel().remoteAddress().toString() + "] has Problem ");
			}
			break;
		}
		case 2: // 认为是握手相关信息，放入消息，直接返回
			ctx.fireChannelRead(rs.getHandshakeBackMsg());
			return;
		}
		final ByteBuf bb = rs.putByteBuf((ByteBuf) msg);
		byte[] tmp = null;
		int len;
		while ((len = bb.readableBytes()) > 0) {
			int code;
			byte[] bs;
			switch (rs.getClientType()) {
			case Flash:
			case Normal: // 暂定为，与flash相同操作
			{
				if (len < 8) {
					// 长度不够，中断
					if (this.log.isDebugEnabled()) {
						this.log.debug("[" + ctx.channel().localAddress() + "] Receive msg [" + ctx.channel().remoteAddress().toString() + "] Decoder Insufficient11 length > " + len + "<" + 8);
					}
					return;
				}
				// 得到预读字节长度
				final int size = bb.getInt(bb.readerIndex());
				// 以下Size有个4的误差
				if ((size + 4) > len) {
					// 还未得到可操作长度，中断
					if (this.log.isDebugEnabled()) {
						this.log.debug("[" + ctx.channel().localAddress() + "] Receive msg [" + ctx.channel().remoteAddress().toString() + "] Decoder Insufficient12 length > " + len + "<" + (size + 8));
					}
					return;
				}
				// 跳过标示长度的4字节
				bb.skipBytes(4);
				// 得到请求码
				tmp = new byte[size];
				bb.getBytes(0, tmp);
				code = bb.readInt();
				// 得到目标长度的字节数组
				bs = new byte[size - 4];
				// 读取需要的内容
				bb.readBytes(bs, 0, size - 4);
				break;
			}
			case WebSocket: // 独立的操作方式
			{
				// websocket模式
				// 最小预期为10：2位为头，4位掩码，4位长度消息吗
				if (len < 10) {
					if (this.log.isDebugEnabled()) {
						this.log.debug("[" + ctx.channel().localAddress() + "] Receive msg [" + ctx.channel().remoteAddress().toString() + "] Decoder Insufficient11 length > " + len + "<" + 10);
					}
					return;
				}
				// byte[] bbs = new byte[bb.readableBytes()];
				// bb.readBytes(bbs, 0, bbs.length);
				final WebSocketParser df = new WebSocketParser(bb);
				if (!df.isParsingComplete()) {
					// 未正常解析（暂认为均为长度问题），跳过，继续等待
					if (this.log.isDebugEnabled()) {
						this.log.debug("[" + ctx.channel().localAddress() + "] Receive msg [" + ctx.channel().remoteAddress().toString() + "] Decoder Insufficient21 length > " + len + "<" + 8);
					}
					return;
				}
				tmp = df.getContentBytes();
				if (tmp.length < 4) {
					if (this.log.isDebugEnabled()) {
						this.log.debug("[" + ctx.channel().localAddress() + "] Receive msg [" + ctx.channel().remoteAddress().toString() + "] Decoder Insufficient12 length > " + tmp.length + ":" + Arrays.toString(tmp) + "<" + 4);
					}
					// 2016-04-07 抛弃异常的数据
					continue;
				}
				try {
					code = super.readInt(tmp, 0);
					// 去掉包括，字节长度表述4长，操作码4长
					bs = new byte[tmp.length - 4];
					System.arraycopy(tmp, 4, bs, 0, bs.length);
				} catch (final Exception e) {
					this.log.error("Error WebSocket msg : " + Arrays.toString(tmp) + " From : " + Arrays.toString(df.getContentBytes()) + "\n\n");
					e.printStackTrace();
					return;
				}
				break;
			}
			default: // 不可能出现的情况
				return;
			}
			try {
				// 解析前进行验证
				switch (super.validateBeforeParams(rs, this.spaceRequestPool.getRequestInfo(code))) {
				case Error: // 在这里中断
					continue;
				default: // 其他继续
				}
				final InputMessageBean imb = new InputMessageBean(this.spaceRequestPool, code, bs, startTime);
				ctx.fireChannelRead(imb);
				// return;
			} catch (final InstallException e) {
				e.printStackTrace();
				if (this.log.isErrorEnabled()) {
					// 针对一些页面，莫名的请求消息
					if ((bb.readableBytes() + bb.readerIndex()) == 10) {
						bb.readerIndex(0);
						final byte[] tm = new byte[10];
						// 读取需要的内容
						bb.readBytes(tm, 0, 10);
						this.log.error("\t>>>>[" + ctx.channel().localAddress() + "] Receive msg [" + ctx.channel().remoteAddress().toString() + "] has Problem Context >> " + Arrays.toString(tm));
						continue;
					} else {
						this.log.error("[" + ctx.channel().localAddress() + "] Receive msg [" + ctx.channel().remoteAddress().toString() + "] has Problem Context >> " + bb.readableBytes() + ":" + bb.readerIndex() + ">>" + Arrays.toString(tmp));
						continue;
					}
				}
				throw e;
			} catch (final Exception e) {
				e.printStackTrace();
				if (this.log.isErrorEnabled()) {
					this.log.error("[" + ctx.channel().localAddress() + "] Receive msg [" + ctx.channel().remoteAddress().toString() + "] has Problem ");
				}
			}
		}
		// bb.release();
	}
}
