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
 * @author Weijie Xu
 * @dateTime 2013-8-30 下午1:35:23
 */
@ChannelHandler.Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年12月5日 下午8:55:47
	 */
	private final Logger log = LogManager.getLogger(this.getClass());

	/**
	 * Session控制
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月26日 下午1:44:31
	 */
	private final SessionControl sc;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年3月16日 下午9:00:23
	 */
	public ClientHandler() {
		this.sc = null;
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年3月16日 下午9:00:24
	 * @param sc Session控制
	 */
	public ClientHandler(final SessionControl sc) {
		this.sc = sc;
	}

	/**
	 * 在此处进行对channel的注册，因为此时才开始存在地址等消息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月11日 上午11:05:24
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		final RequestSession rs = ChannelPool.getInstance().putClientChannel(ctx.channel());
		ctx.fireChannelActive();
		if (null != this.sc) {
			this.sc.open(rs);
		}
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年4月18日 上午10:58:14
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
			final Channel channel = ctx.channel();
			final InputMessageBean bean = (InputMessageBean) msg;
			final RequestNettySession rs = ChannelPool.getInstance().getChannelSession(channel);
			bean.transpondExec(rs);
			if (this.log.isDebugEnabled()) {
				if (!bean.getRequestInfo().isKeep()) {
					this.log.debug(new StringBuilder(100).append("use[").append(bean.getRunningTime()).append("] [").append(ctx.channel().localAddress()).append("] Receive [").append(ctx.channel().remoteAddress().toString()).append("] ").append(bean.showLinkInfo()).append(" ok msg(").append(bean.getDataLen()).append(") >> ").append(bean.getMsg()));
				}
			}
		} else {
			if (this.log.isDebugEnabled()) {
				this.log.debug("Receive error msg >> " + msg);
			}
		}
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年6月26日 下午1:45:04
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
		} else {
			throwable.printStackTrace();
		}
		if (null != this.sc) {
			final RequestNettySession rs = ChannelPool.getInstance().getChannelSession(ctx.channel());
			if (throwable instanceof IOException) {
				// 认为是连接被断开情况
				this.sc.exceptionClose(rs);
				// 断开连接
				SocksServerUtils.closeOnFlush(ctx.channel());
			} else {
				this.sc.exception(rs, throwable);
			}
		} else if (throwable instanceof IOException) {
			// 是IO相关异常，认为该异常都是掉线相关
			SocksServerUtils.closeOnFlush(ctx.channel());
		}
		// 非有操作，或非有目标接口相关不处理
	}
}
