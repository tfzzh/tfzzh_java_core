/**
 * @author Weijie Xu
 * @dateTime 2013-8-30 下午2:11:49
 */
package com.tfzzh.socket.netty.coder;

import java.io.ByteArrayOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tfzzh.socket.bean.OutputSocketMessageBean;
import com.tfzzh.socket.tools.RequestPool;
import com.tfzzh.socket.tools.TfzzhDataOutputStream;

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
public class RetransmissionSocketOutEncoderAdapter extends ChannelOutboundHandlerAdapter {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年12月5日 下午8:55:47
	 */
	private final Logger log = LogManager.getLogger(this.getClass());

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年11月11日 上午9:53:04
	 * @param spaceRequestPool 所在空间名
	 */
	public RetransmissionSocketOutEncoderAdapter(final RequestPool spaceRequestPool) {
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
			final int l = stream.size() + 4;
			// 先放入长度
			buf.writeInt(l);
			// 放入请求码
			buf.writeInt(omb.getCode());
			// 放入消息字节流
			buf.writeBytes(stream.toByteArray());
			// 进行消息发送
			ctx.writeAndFlush(buf, promise);
			tdos.close();
			stream.close();
			if (this.log.isDebugEnabled()) {
				this.log.debug("use[" + (System.currentTimeMillis() - l1) + "] [" + ctx.channel().remoteAddress().toString() + "] Send MsgBean [" + ctx.channel().localAddress().toString() + "] <" + omb.getCode() + "> msg(" + l + ") >> " + omb.getMsg());
			}
		} else {
			final ByteBuf buf = Unpooled.buffer();
			buf.writeBytes(msg.toString().getBytes());
			ctx.writeAndFlush(buf, promise);
			if (this.log.isDebugEnabled()) {
				this.log.debug("use[" + (System.currentTimeMillis() - l1) + "] Send Msg [" + ctx.channel().remoteAddress().toString() + "] <" + msg.getClass().getName() + "> msg >> " + msg.toString());
			}
		}
	}
}
