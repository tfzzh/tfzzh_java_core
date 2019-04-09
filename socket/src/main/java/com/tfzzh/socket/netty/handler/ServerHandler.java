/**
 * @author Weijie Xu
 * @dateTime 2013-8-30 下午1:35:23
 */
package com.tfzzh.socket.netty.handler;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tfzzh.socket.RequestSession;
import com.tfzzh.socket.bean.InputMessageBean;
import com.tfzzh.socket.netty.tools.ChannelPool;
import com.tfzzh.socket.netty.tools.RequestNettySession;
import com.tfzzh.socket.netty.tools.SocksServerUtils;
import com.tfzzh.socket.tools.SessionControl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 */
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

	/**
	 */
	private final Logger log = LogManager.getLogger(this.getClass());

	/**
	 * Session控制
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月26日 下午1:25:58
	 */
	private final SessionControl sc;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年3月16日 下午8:59:58
	 */
	public ServerHandler() {
		this.sc = null;
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年3月16日 下午8:59:45
	 * @param sc Session控制
	 */
	public ServerHandler(final SessionControl sc) {
		this.sc = sc;
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午7:14:42
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRegistered(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
		final RequestSession rs = ChannelPool.getInstance().putChannel(ctx.channel());
		ctx.fireChannelRegistered();
		if (null != this.sc) {
			this.sc.open(rs);
		}
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午7:16:12
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelUnregistered(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
		if (null != this.sc) {
			final RequestNettySession rs = ChannelPool.getInstance().getChannelSession(ctx.channel());
			this.sc.closed(rs);
		}
		ChannelPool.getInstance().clearChannelSession(ctx.channel());
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2013-8-30 下午2:58:50
	 * @see io.netty.channel.SimpleChannelInboundHandler#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
		if (msg instanceof InputMessageBean) {
			final InputMessageBean imsg = (InputMessageBean) msg;
			if (imsg.hasProblem()) {
				// 存在问题的情况，需要向请求端进行数据返回
				if (this.log.isDebugEnabled()) {
					if (!imsg.getRequestInfo().isKeep()) {
						this.log.debug(new StringBuilder(100).append("use[").append(imsg.getRunningTime()).append("] [").append(ctx.channel().localAddress()).append("] Receive [").append(ctx.channel().remoteAddress()).append("] ").append(imsg.showLinkInfo())
								.append(" error msg >> ").append(imsg));
					}
				}
			} else {
				// 正常情况
				this.channelRead0(ctx.channel(), imsg);
			}
		} else if (msg instanceof String) {
			// 认为String类型数据，直接发送
			ctx.writeAndFlush(msg);
		} else {
			if (this.log.isDebugEnabled()) {
				this.log.debug("Receive error msg >> " + msg);
			}
		}
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-19 下午4:15:31
	 * @param channel 频道信息
	 * @param bean 请求的消息
	 */
	private void channelRead0(final Channel channel, final InputMessageBean bean) {
		final RequestNettySession rs = ChannelPool.getInstance().getChannelSession(channel);
		bean.transpondExec(rs);
		if (this.log.isDebugEnabled()) {
			if (!bean.getRequestInfo().isKeep()) {
				this.log.debug(new StringBuilder(100).append("use[").append(bean.getRunningTime()).append("] [").append(channel.localAddress()).append("] Receive [").append(channel.remoteAddress()).append("] ").append(bean.showLinkInfo()).append(" ok msg(")
						.append(bean.getDataLen()).append(") >> ").append(bean.getMsg()));
			}
		}
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年6月26日 下午1:35:26
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
		if (null != this.sc) {
			final RequestNettySession rs = ChannelPool.getInstance().getChannelSession(ctx.channel());
			this.sc.idle(rs);
		}
		ctx.fireChannelInactive();
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2013-8-30 下午2:58:32
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#exceptionCaught(io.netty.channel.ChannelHandlerContext, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable throwable) throws Exception {
		if (this.log.isDebugEnabled()) {
			this.log.debug(new StringBuilder(40).append(ctx.channel().remoteAddress()).append(" >> ").append(throwable.getMessage()));
			if (!(throwable instanceof IOException)) {
				throwable.printStackTrace();
			}
		} else {
			if (!(throwable instanceof IOException)) {
				throwable.printStackTrace();
			}
		}
		if (null != this.sc) {
			final RequestNettySession rs = ChannelPool.getInstance().getChannelSession(ctx.channel());
			this.sc.exception(rs, throwable);
		}
		if (throwable instanceof IOException) {
			// 是IO相关异常，认为该异常都是掉线相关
			SocksServerUtils.closeOnFlush(ctx.channel());
		}
	}
}
