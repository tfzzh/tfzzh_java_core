/**
 * @author Weijie Xu
 * @dateTime 2017年7月21日 上午10:07:37
 */
package com.tfzzh.socket.netty;

import com.tfzzh.log.CoreLog;
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
 * http代理服务
 * 
 * @author Weijie Xu
 * @dateTime 2017年7月21日 上午10:07:37
 */
public class BaseHttpProxyService implements Runnable {

	/**
	 * 启动的IP
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年7月21日 上午10:07:37
	 */
	private String ip;

	/**
	 * 启动的端口
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年7月21日 上午10:07:37
	 */
	private Integer port;

	/**
	 * 是否启动完成
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年7月21日 上午10:07:37
	 */
	private boolean startOk = false;

	/**
	 * 服务类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年7月21日 上午10:07:37
	 */
	private ServerTypeEnum type;

	/**
	 * Session控制
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年7月21日 上午10:07:37
	 */
	private SessionControl sc;

	/**
	 * 验证控制器
	 * 
	 * @author XuWeijie
	 * @dateTime 2017年7月21日 上午10:07:37
	 */
	protected ValidateControl vc;

	/**
	 * 所属空间名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年7月21日 上午10:07:37
	 */
	private final String spaceName;

	/**
	 * 服务频道
	 * 
	 * @author XuWeijie
	 * @dateTime 2017年7月21日 上午10:07:37
	 */
	private Channel channel;

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年7月21日 上午10:07:37
	 * @param port 要开启的端口
	 * @param sc Session控制
	 * @param vc 验证控制器
	 * @param spaceName 所属空间名
	 */
	public BaseHttpProxyService(final int port, final SessionControl sc, final ValidateControl vc, final String spaceName) {
		this(null, port, ServerTypeEnum.HttpServer, sc, vc, spaceName);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年7月21日 上午10:07:37
	 * @param ip 服务对应的IP
	 * @param port 要开启的端口
	 * @param sc Session控制
	 * @param vc 验证控制器
	 * @param spaceName 所属空间名
	 */
	public BaseHttpProxyService(final String ip, final int port, final SessionControl sc, final ValidateControl vc, final String spaceName) {
		this(ip, port, ServerTypeEnum.HttpServer, sc, vc, spaceName);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年7月21日 上午10:07:37
	 * @param ip 服务对应的IP
	 * @param port 要开启的端口
	 * @param type 服务的类型
	 * @param sc Session控制
	 * @param vc 验证控制器
	 * @param spaceName 所属空间名
	 */
	public BaseHttpProxyService(final String ip, final int port, final ServerTypeEnum type, final SessionControl sc, final ValidateControl vc, final String spaceName) {
		this.ip = ((null == ip) || (ip.length() == 0)) ? null : ip;
		this.port = port;
		this.type = ServerTypeEnum.HttpServer;
		this.sc = sc;
		this.vc = vc;
		this.spaceName = spaceName;
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年7月21日 上午10:07:37
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
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), this.getClass().getSimpleName() + " Start ok... Listening<" + (this.ip == null ? "*:" + this.port : this.ip + ":" + this.port) + ">");
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
	 * @dateTime 2017年7月21日 上午10:07:37
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
	 * @dateTime 2017年7月21日 上午10:07:37
	 * @return the channel
	 */
	public Channel getServerChannel() {
		return this.channel;
	}
}
