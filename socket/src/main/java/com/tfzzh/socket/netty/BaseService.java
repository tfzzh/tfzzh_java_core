/**
 * @author Weijie Xu
 * @dateTime 2013-8-30 下午1:29:47
 */
package com.tfzzh.socket.netty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tfzzh.socket.netty.tools.ServerTypeEnum;
import com.tfzzh.socket.tools.SessionControl;
import com.tfzzh.socket.tools.ValidateControl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * socket服务
 * 
 * @author Weijie Xu
 * @dateTime 2013-8-30 下午1:29:47
 */
public class BaseService implements Runnable {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年12月5日 下午8:55:47
	 */
	private final Logger log = LogManager.getLogger(this.getClass());

	/**
	 * 启动的IP
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月13日 下午6:45:15
	 */
	private String ip;

	/**
	 * 启动的端口
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月12日 下午7:13:38
	 */
	private Integer port;

	/**
	 * 是否启动完成
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月13日 下午8:48:00
	 */
	private boolean startOk = false;

	/**
	 * 服务类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月13日 下午7:13:25
	 */
	private ServerTypeEnum type;

	/**
	 * Session控制
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月26日 下午2:44:24
	 */
	private SessionControl sc;

	/**
	 * 验证控制器
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月26日_下午12:10:28
	 */
	protected ValidateControl vc;

	/**
	 * 所属空间名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月31日 下午7:30:45
	 */
	private final String spaceName;

	/**
	 * 服务频道
	 * 
	 * @author XuWeijie
	 * @datetime 2015年8月21日_上午11:31:37
	 */
	private Channel channel;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年3月16日 下午9:08:09
	 * @param port 要开启的端口
	 * @param sc Session控制
	 * @param vc 验证控制器
	 * @param spaceName 所属空间名
	 */
	public BaseService(final int port, final SessionControl sc, final ValidateControl vc, final String spaceName) {
		this(null, port, ServerTypeEnum.Server, sc, vc, spaceName);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年3月16日 下午9:08:09
	 * @param ip 服务对应的IP
	 * @param port 要开启的端口
	 * @param sc Session控制
	 * @param vc 验证控制器
	 * @param spaceName 所属空间名
	 */
	public BaseService(final String ip, final int port, final SessionControl sc, final ValidateControl vc, final String spaceName) {
		this(ip, port, ServerTypeEnum.Server, sc, vc, spaceName);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年6月12日 下午7:14:13
	 * @param ip 服务对应的IP
	 * @param port 要开启的端口
	 * @param type 服务的类型
	 * @param sc Session控制
	 * @param vc 验证控制器
	 * @param spaceName 所属空间名
	 */
	public BaseService(final String ip, final int port, final ServerTypeEnum type, final SessionControl sc, final ValidateControl vc, final String spaceName) {
		this.ip = ((null == ip) || (ip.length() == 0)) ? null : ip;
		this.port = port;
		this.type = ServerTypeEnum.Server;
		this.sc = sc;
		this.vc = vc;
		this.spaceName = spaceName;
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2013-9-4 上午10:47:29
	 */
	@Override
	public void run() {
		final EventLoopGroup bossGroup = new NioEventLoopGroup();
		final EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			final ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(this.type.getInitializerInstance(this.sc, this.vc, this.spaceName));
			final ChannelFuture cf;
			if (null == this.ip) {
				cf = b.bind(this.port).sync();
			} else {
				cf = b.bind(this.ip, this.port).sync();
			}
			this.startOk = true;
			if (this.log.isDebugEnabled()) {
				this.log.debug(this.getClass().getSimpleName() + " Start ok... Listening<" + (this.ip == null ? "*:" + this.port : this.ip + ":" + this.port) + ">");
			}
			this.channel = cf.channel();
			// 清除没什么意义的数据
			this.ip = null;
			this.port = null;
			this.type = null;
			this.sc = null;
			this.channel.closeFuture().sync();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	/**
	 * 是否启动成功
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月13日 下午8:47:57
	 * @return true，已经启动成功；<br />
	 *         false，还没启动完全；<br />
	 */
	public boolean isStartOk() {
		return this.startOk;
	}

	/**
	 * 得到服务频道
	 * 
	 * @author XuWeijie
	 * @datetime 2015年8月21日_上午11:32:58
	 * @return the channel
	 */
	public Channel getServerChannel() {
		return this.channel;
	}
}
