/**
 * @author Weijie Xu
 * @dateTime 2013-8-30 下午1:39:16
 */
package com.tfzzh.socket.netty.tools;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

/**
 * 接口服务工具
 * 
 * @author Weijie Xu
 * @dateTime 2013-8-30 下午1:39:16
 */
public final class SocksServerUtils {

	/**
	 * @author Weijie Xu
	 * @dateTime 2013-9-4 上午10:48:02
	 * @param ch 控制的频道
	 */
	public static void closeOnFlush(final Channel ch) {
		if (ch.isActive()) {
			ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}
	}
}
