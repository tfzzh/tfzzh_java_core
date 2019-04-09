/**
 * @author Weijie Xu
 * @dateTime 2014年6月13日 下午7:08:00
 */
package com.tfzzh.socket.netty.tools;

import com.tfzzh.socket.http.netty.handler.HttpInitializer;
import com.tfzzh.socket.netty.handler.ClientInitializer;
import com.tfzzh.socket.netty.handler.ServerInitializer;
import com.tfzzh.socket.tools.SessionControl;
import com.tfzzh.socket.tools.ValidateControl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * 初始化类型
 * 
 * @author Weijie Xu
 * @dateTime 2014年6月13日 下午7:08:00
 */
public enum ServerTypeEnum {
	/**
	 * 服务器类型，创建监听
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月13日 下午7:09:06
	 */
	Server {

		@Override
		public ChannelInitializer<Channel> getInitializerInstance(final SessionControl sc, final ValidateControl validateControl, final String spaceName) {
			return new ServerInitializer(sc, validateControl, spaceName);
		}
	},
	/**
	 * Http服务，创建监听
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月28日_下午4:26:17
	 */
	HttpServer {

		@Override
		public ChannelInitializer<Channel> getInitializerInstance(final SessionControl sc, final ValidateControl validateControl, final String spaceName) {
			return new HttpInitializer(sc, validateControl, spaceName);
		}
	},
	/**
	 * 转发，当前还不确实是否需要独立出来，暂定如此，之后可以去除
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月13日 下午7:09:07
	 */
	Retransmission {

		@Override
		public ChannelInitializer<Channel> getInitializerInstance(final SessionControl sc, final ValidateControl validateControl, final String spaceName) {
			return new ServerInitializer(sc, validateControl, spaceName);
		}
	},
	/**
	 * 客户端类型，仅是与目标服务器进行连接，本身不创建多余的监听
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月13日 下午7:09:07
	 */
	Client {

		@Override
		public ChannelInitializer<Channel> getInitializerInstance(final SessionControl sc, final ValidateControl validateControl, final String spaceName) {
			return new ClientInitializer(sc, validateControl, spaceName);
		}
	};

	/**
	 * 得到初始化对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月16日 下午9:06:53
	 * @param sc Session控制
	 * @param validateControl 验证控制器
	 * @param spaceName 所属空间名
	 * @return 初始化对象
	 */
	public abstract ChannelInitializer<Channel> getInitializerInstance(SessionControl sc, ValidateControl validateControl, String spaceName);
}
