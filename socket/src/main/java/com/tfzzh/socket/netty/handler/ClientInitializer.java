/**
 * @author Weijie Xu
 * @dateTime 2014-3-19 下午8:08:38
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
 * 请求服务初始化
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-19 下午8:08:38
 */
public class ClientInitializer extends ChannelInitializer<Channel> {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-21 上午9:59:40
	 */
	private final ChannelOutboundHandler encoder;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-21 上午9:59:38
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
	 * @dateTime 2015年3月16日 下午9:04:15
	 * @param sc Session控制
	 * @param validateControl 验证控制器
	 * @param spaceName 所属空间名
	 */
	public ClientInitializer(final SessionControl sc, final ValidateControl validateControl, final String spaceName) {
		this.handler = new ClientHandler(sc);
		this.spaceRequestPool = RequestPool.getInstance(spaceName);
		this.validateControl = validateControl;
		this.encoder = new ServerSocketOutEncoderAdapter(this.spaceRequestPool);
		if (null == this.spaceRequestPool) {
			throw new ConfigurationOrInstallException("Unknow Action NameSpace: " + spaceName);
		}
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-19 下午8:09:49
	 * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
	 */
	@Override
	protected void initChannel(final Channel ch) throws Exception {
		final ChannelPipeline pipeline = ch.pipeline();
		// Add the text line codec combination first,
		pipeline.addLast("decoder", new ServerSocketInDecoderAdapter(this.spaceRequestPool, this.validateControl));
		pipeline.addLast("encoder", this.encoder);
		// and then business logic.
		pipeline.addLast("handler", this.handler);
	}
}
