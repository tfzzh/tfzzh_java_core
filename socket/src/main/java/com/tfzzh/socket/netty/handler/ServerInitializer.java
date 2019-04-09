/**
 * @author Weijie Xu
 * @dateTime 2013-8-30 下午1:33:33
 */
package com.tfzzh.socket.netty.handler;

import com.tfzzh.exception.ConfigurationOrInstallException;
import com.tfzzh.socket.netty.coder.ServerSocketInDecoderAdapter;
import com.tfzzh.socket.netty.coder.ServerSocketOutEncoderAdapter;
import com.tfzzh.socket.tools.RequestPool;
import com.tfzzh.socket.tools.SessionControl;
import com.tfzzh.socket.tools.ValidateControl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;

/**
 * 服务器初始化
 * 
 * @author Weijie Xu
 * @dateTime 2013-8-30 下午1:33:33
 */
public final class ServerInitializer extends ChannelInitializer<Channel> {

	/**
	 * 消息输出编码适配器
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-13 下午8:35:43
	 */
	private final ChannelOutboundHandler encoder;

	/**
	 * 处理器
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-19 下午6:45:09
	 */
	private final ChannelHandler handler;

	/**
	 * 所在空间名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月31日 下午7:10:10
	 */
	private final RequestPool spaceRequestPool;

	/**
	 * 验证控制器
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月26日_下午12:10:28
	 */
	protected final ValidateControl validateControl;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年3月16日 下午9:04:48
	 * @param sc Session控制
	 * @param validateControl 验证控制器
	 * @param spaceName 所属空间名
	 */
	public ServerInitializer(final SessionControl sc, final ValidateControl validateControl, final String spaceName) {
		this.handler = new ServerHandler(sc);
		this.spaceRequestPool = RequestPool.getInstance(spaceName);
		this.validateControl = validateControl;
		this.encoder = new ServerSocketOutEncoderAdapter(this.spaceRequestPool);
		if (null == this.spaceRequestPool) {
			throw new ConfigurationOrInstallException("Unknow Action NameSpace: " + spaceName);
		}
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-13 下午8:35:35
	 * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
	 */
	@Override
	public void initChannel(final Channel socketChannel) throws Exception {
		final ChannelPipeline channelPipeline = socketChannel.pipeline();
		// 消息传入解码适配器
		channelPipeline.addLast("decoder", new ServerSocketInDecoderAdapter(this.spaceRequestPool, this.validateControl));
		channelPipeline.addLast("encoder", this.encoder);
		channelPipeline.addLast("handler", this.handler);
	}
}
