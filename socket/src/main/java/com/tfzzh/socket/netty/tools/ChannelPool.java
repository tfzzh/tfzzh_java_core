/**
 * @author Weijie Xu
 * @dateTime 2014-3-18 下午8:38:10
 */
package com.tfzzh.socket.netty.tools;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tfzzh.exception.InitializeException;
import com.tfzzh.socket.webservice.DefaultHandshake;

import io.netty.channel.Channel;

/**
 * 频道池<br />
 * 链接池<br />
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-18 下午8:38:10
 */
public class ChannelPool {

	/**
	 * 频道列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-19 下午1:36:27
	 */
	private final Map<Channel, RequestNettySession> channels;

	/**
	 * 对象唯一实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-19 下午1:36:25
	 */
	private static final ChannelPool pool = new ChannelPool();

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-19 下午1:36:21
	 */
	private ChannelPool() {
		this.channels = new ConcurrentHashMap<>();
		this.init();
	}

	/**
	 * 一些初始化操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-19 下午1:36:15
	 */
	private void init() {
	}

	/**
	 * 得到对象实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-19 下午1:36:03
	 * @return 对象唯一实例
	 */
	public static ChannelPool getInstance() {
		return ChannelPool.pool;
	}

	/**
	 * 放入一个频道
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-19 下午1:44:33
	 * @param channel 待放入的频道
	 * @return 被创建的请求会话<br />
	 *         null，目标频道已经存在于池中<br />
	 */
	public RequestNettySession putChannel(final Channel channel) {
		if (this.channels.containsKey(channel)) {
			// 已经存在目标频道信息
			return null;
		}
		try {
			final RequestNettySession rs = new RequestNettySession(channel);
			this.channels.put(channel, rs);
			return rs;
		} catch (final InitializeException e) {
			return null;
		}
	}

	/**
	 * 放入一个服务器间通信用频道
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月4日 下午3:57:33
	 * @param channel 待放入的频道
	 * @return 被创建的请求会话<br />
	 *         null，目标频道已经存在于池中<br />
	 */
	public RequestNettySession putClientChannel(final Channel channel) {
		if (this.channels.containsKey(channel)) {
			// 已经存在目标频道信息
			return null;
		}
		try {
			final RequestNettySession rs = new RequestNettySession(channel);
			rs.setHandshakeInfo(new DefaultHandshake());
			this.channels.put(channel, rs);
			return rs;
		} catch (final InitializeException e) {
			return null;
		}
	}

	/**
	 * 得到目标频道的会话信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午7:11:20
	 * @param channel 目标频道
	 * @return 对应的会话信息
	 */
	public RequestNettySession getChannelSession(final Channel channel) {
		return this.channels.get(channel);
	}

	/**
	 * 移除一个频道会话
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午7:09:11
	 * @param channel 频道信息
	 * @return ture，移除成功；<br />
	 *         false，移除失败；<br />
	 */
	public boolean clearChannelSession(final Channel channel) {
		return null != this.channels.remove(channel);
	}
}
