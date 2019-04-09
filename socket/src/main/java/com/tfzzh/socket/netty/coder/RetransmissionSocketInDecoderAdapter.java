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

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * 针对AS的Socket请求消息传入解码适配器
 * 
 * @author Weijie Xu
 * @dateTime 2013-8-30 下午2:08:09
 */
public class RetransmissionSocketInDecoderAdapter extends BaseNettyDecoderAdapter {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年10月31日 下午7:10:20
	 * @param spaceRequestPool 所在空间名
	 * @param validateControl 验证控制器
	 */
	public RetransmissionSocketInDecoderAdapter(final RequestPool spaceRequestPool, final ValidateControl validateControl) {
		super(spaceRequestPool, validateControl);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2013-9-4 上午10:48:33
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
		// 对应的得到请求会话
		// 开始时间
		final long startTime = System.currentTimeMillis();
		final RequestNettySession rs = ChannelPool.getInstance().getChannelSession(ctx.channel());
		// 组合字节流
		final ByteBuf bb = rs.putByteBuf((ByteBuf) msg);
		// 得到可读字节长度
		int len;
		byte[] tmp = null;
		while ((len = bb.readableBytes()) > 0) {
			// 判定初级长度
			if (len < 8) {
				// 长度不够，中断
				if (this.log.isDebugEnabled()) {
					this.log.debug("[" + ctx.channel().remoteAddress().toString() + "] Receive msg [" + ctx.channel().localAddress() + "] Decoder Insufficient21 length > " + len + "<" + 8);
				}
				return;
			}
			// 得到预读字节长度
			final int size = bb.getInt(bb.readerIndex());
			// 以下Size有个4的误差
			if ((size + 4) > len) {
				// 还未得到可操作长度，中断
				if (this.log.isDebugEnabled()) {
					this.log.debug("[" + ctx.channel().remoteAddress().toString() + "] Receive msg [" + ctx.channel().localAddress() + "] Decoder Insufficient22 length > " + len + "<" + (size + 8));
				}
				return;
			}
			// 跳过标示长度的4字节
			bb.skipBytes(4);
			tmp = new byte[size];
			bb.getBytes(size, tmp);
			// 得到请求码
			final int code = bb.readInt();
			// 得到目标长度的字节数组
			final byte bs[] = new byte[size - 4];
			// 读取需要的内容
			bb.readBytes(bs, 0, size - 4);
			// 得到
			try {
				// 解析前进行验证
				switch (super.validateBeforeParams(rs, this.spaceRequestPool.getRequestInfo(code))) {
				case Error: // 在这里中断
					return;
				default: // 其他继续
				}
				final InputMessageBean imb = new InputMessageBean(this.spaceRequestPool, code, bs, startTime);
				// if (this.log.isDebugEnabled()) {
				// // 暂时去掉打印
				// this.log.debug("[" + ctx.channel().localAddress() + "] Receive msg [" + ctx.channel().remoteAddress().toString()
				// + "] <" + code + "> msg(" + len + ":" + bb.readableBytes() + ") >> " + imb.getMsg());
				// }
				// if (imb.hasProblem()) {
				// // 存在问题的情况，需要向请求端进行数据返回
				// System.err.println("Data has Error. >> " + imb.getProblemMsg());
				// ctx.fireChannelRead(imb);
				// } else {
				// // 不存在问题，进入到之后的逻辑
				// ctx.fireChannelRead(imb);
				// }
				ctx.fireChannelRead(imb);
			} catch (final InstallException e) {
				if (this.log.isErrorEnabled()) {
					this.log.error("[" + ctx.channel().remoteAddress().toString() + "] Receive msg [" + ctx.channel().localAddress() + "] has Problem Context >> " + Arrays.toString(tmp));
				}
				throw e;
			} catch (final Exception e) {
				e.printStackTrace();
			} finally {
				// 进行字节清理
				// 2014-06-28 暂不清理字节流
				// System.err.println("\t\tshengyu : " + bb.readableBytes());
				// rs.clearByteBuf();
			}
		}
	}
}
