/**
 * @author 许纬杰
 * @datetime 2016年4月22日_上午10:28:15
 */
package com.tfzzh.socket.http.netty.handler;

import com.tfzzh.socket.tools.RequestPool;
import com.tfzzh.socket.tools.SessionControl;
import com.tfzzh.socket.tools.ValidateControl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * 接收各种Gm工具请求消息的Socket服务
 * 
 * @author 许纬杰
 * @datetime 2016年4月22日_上午10:28:15
 */
public class HttpInitializer extends ChannelInitializer<Channel> {

	/**
	 * Session控制器
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月9日_上午10:34:50
	 */
	private final SessionControl sc;

	/**
	 * 请求消息池
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月9日_上午10:34:51
	 */
	private final RequestPool rp;

	/**
	 * 验证控制器
	 * 
	 * @author XuWeijie
	 * @datetime 2016年4月28日_下午4:27:38
	 */
	protected final ValidateControl validateControl;

	/**
	 * @author 许纬杰
	 * @datetime 2016年4月28日_下午4:29:46
	 * @param sc Session控制器
	 * @param validateControl 验证控制器
	 * @param spaceName 空间名字
	 */
	public HttpInitializer(final SessionControl sc, final ValidateControl validateControl, final String spaceName) {
		this.sc = sc;
		this.rp = RequestPool.getInstance(spaceName);
		this.validateControl = validateControl;
	}

	/**
	 * @author 许纬杰
	 * @datetime 2016年4月28日_下午4:36:04
	 * @param socketChannel 接口频道
	 * @throws Exception 抛
	 * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
	 */
	@Override
	public void initChannel(final Channel socketChannel) throws Exception {
		final ChannelPipeline channelPipeline = socketChannel.pipeline();
		// 消息传入解码适配器
		channelPipeline.addLast(new HttpRequestDecoder());
		channelPipeline.addLast(new HttpResponseEncoder());
		channelPipeline.addLast(new HttpContentCompressor());
		channelPipeline.addLast(new HttpServerHandler(this.sc, this.rp));
	}
}
