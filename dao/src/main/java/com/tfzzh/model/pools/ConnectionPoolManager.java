/**
 * @author Weijie Xu
 * @dateTime 2015年4月21日 下午4:59:49
 */
package com.tfzzh.model.pools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tfzzh.exception.NotAvailableOperationModeException;
import com.tfzzh.exception.NotExistsTargetException;
import com.tfzzh.model.pools.ConnectionPool.ChangeableConnectionPool;
import com.tfzzh.model.pools.ConnectionPool.UniqueConnectionPool;

/**
 * 数据连接池控制，简版<br />
 * 暂定已经存在的连接池不能被移除，只能被统一关闭<br />
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月21日 下午4:59:49
 */
public final class ConnectionPoolManager implements ConnectionInfoConfig, ConnectionPoolConfig {

	/**
	 * 已经启动的数据库连接驱动
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 下午9:01:15
	 */
	private final Set<String> dbDrivers = new HashSet<>();

	/**
	 * 连接用数据信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午2:08:30
	 */
	private final Map<String, ConnectionInfoBean> infos = new HashMap<>();

	/**
	 * 静态连接池信息列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午2:08:29
	 */
	private final Map<String, UniqueConnectionPool> conUniPools = new HashMap<>();

	/**
	 * 可变池的名字
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月29日 下午12:42:19
	 */
	private String changeablePoolName = "changePool";

	/**
	 * 可变连接池信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午2:20:40
	 */
	private final ChangeableConnectionPool conChgPool = new ChangeableConnectionPool();

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午2:09:52
	 */
	private static ConnectionPoolManager cpm = null;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午2:09:47
	 */
	public ConnectionPoolManager() {
		if (null == ConnectionPoolManager.cpm) {
			ConnectionPoolManager.cpm = this;
		} else {
			throw new NotAvailableOperationModeException("The ConnectionPoolManager Cannt be Created More than Twice...");
		}
	}

	/**
	 * 得到对象实例
	 *
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午2:12:20
	 * @return 对象唯一实力
	 */
	public static ConnectionPoolManager getInstance() {
		return ConnectionPoolManager.cpm;
	}

	/**
	 * 开启关闭系统的监听事件
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月4日 下午7:19:59
	 */
	public void openShutDownEvent() {
		Runtime.getRuntime().addShutdownHook(new Thread(new ConnectionShutDownListener()));
	}

	/**
	 * 关闭连接池
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月26日_下午7:57:11
	 */
	public void shutdownPool() {
		new Thread(new ConnectionShutDownListener()).start();
	}

	/**
	 * 进行直接的池关闭
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月29日_下午5:52:43
	 */
	public void close() {
		for (final UniqueConnectionPool ucp : ConnectionPoolManager.cpm.conUniPools.values()) {
			ucp.clear();
		}
		ConnectionPoolManager.cpm.conChgPool.clear();
	}

	/**
	 * 初始化驱动
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 下午9:02:13
	 * @param dbDriver 驱动完整路径
	 */
	protected void initDriver(final String dbDriver) {
		if (!this.dbDrivers.contains(dbDriver)) {
			try {
				// 设置主驱动
				Class.forName(dbDriver);
				this.dbDrivers.add(dbDriver);
			} catch (final ClassNotFoundException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	/**
	 * 得到目标名称的连接数据信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午3:21:11
	 * @param infoName 连接信息名
	 * @return 连接数据信息
	 */
	public ConnectionInfoBean getConnectionInfo(final String infoName) {
		return this.infos.get(infoName);
	}

	/**
	 * 放入一个连接数据信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午3:23:10
	 * @param name 池名称
	 * @param driver 连接用驱动名
	 * @param url URL连接
	 * @param user 用户名
	 * @param pass 密码
	 * @param description 说明
	 * @param useUnicode 是否使用unicode格式
	 * @param characterEncoding 连接用字符编码类型
	 * @param readOnly 是否只读
	 * @param timeOut 连接后空闲超时时间
	 * @param min 最小连接存在数
	 * @param max 最大连接存在数
	 * @param others 其他属性对应键值对，需为键与值成对出现，1为键名，2为键值，以此类推
	 * @see com.tfzzh.model.pools.ConnectionInfoConfig#putConnectionInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String, boolean, long, int, int, java.lang.String[])
	 */
	@Override
	public void putConnectionInfo(final String name, final String driver, final String url, final String user, final String pass, final String description, final boolean useUnicode, final String characterEncoding, final boolean readOnly, final long timeOut, final int min, final int max, final String... others) {
		this.infos.put(name, new ConnectionInfoBean(name, driver, url, user, pass, description, useUnicode, characterEncoding, readOnly, timeOut, min, max, others));
	}

	/**
	 * 放入一个静态连接池信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月28日 下午4:26:06
	 * @param poolName 池名字
	 * @param infoName 连接信息名字
	 * @see com.tfzzh.model.pools.ConnectionPoolConfig#putUniqueConnectionInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public void putUniqueConnectionInfo(final String poolName, final String infoName) {
		this.createUniqueConnection(poolName, infoName);
	}

	/**
	 * 创建一个静态连接池
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午3:40:14
	 * @param poolName 池名字
	 * @param infoName 连接信息名字
	 * @return 被创建的连接池
	 */
	public UniqueConnectionPool createUniqueConnection(final String poolName, final String infoName) {
		final ConnectionInfoBean info;
		if (null != (info = this.infos.get(infoName))) {
			final UniqueConnectionPool ucp = new UniqueConnectionPool(info);
			this.conUniPools.put(poolName, ucp);
			return ucp;
		} else {
			throw new NotExistsTargetException("Not exists ConnectionInfoData name with [" + infoName + "]...");
		}
	}

	/**
	 * 移除一个静态连接池
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午8:02:39
	 * @param poolName 目标连接池名字
	 * @return 被移除的连接池
	 */
	public UniqueConnectionPool removeUniqueConnection(final String poolName) {
		final UniqueConnectionPool ucp = this.conUniPools.remove(poolName);
		if (null == ucp) {
			throw new NotExistsTargetException("Not exists UniqueConnectionPool name with [" + poolName + "]...");
		} else {
			ucp.clear();
		}
		return ucp;
	}

	/**
	 * 得到目标静态连接池
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午8:14:07
	 * @param poolName 目标连接池名字
	 * @return 目标静态连接池
	 */
	public UniqueConnectionPool getUniqueConnection(final String poolName) {
		final UniqueConnectionPool ucp = this.conUniPools.get(poolName);
		if (null == ucp) {
			throw new NotExistsTargetException("Not exists UniqueConnectionPool name with [" + poolName + "]...");
		}
		return ucp;
	}

	/**
	 * 得到动态连接池，只有唯一的
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午3:43:22
	 * @return 动态连接池
	 * @see com.tfzzh.model.pools.ConnectionPoolConfig#getChangeableConnection()
	 */
	@Override
	public ChangeableConnectionPool getChangeableConnection() {
		return this.conChgPool;
	}

	/**
	 * 设置可变连接池的名字
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月29日 下午12:43:50
	 * @param poolName 可变连接池的名字
	 * @see com.tfzzh.model.pools.ConnectionPoolConfig#setChangeablePoolName(java.lang.String)
	 */
	@Override
	public void setChangeablePoolName(final String poolName) {
		this.changeablePoolName = poolName;
	}

	/**
	 * 得到目标名称的连接池
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月29日 下午12:46:30
	 * @param poolName 目标连接池名称
	 * @return 目标连接池对象； null，不存在目标
	 * @see com.tfzzh.model.pools.ConnectionPoolConfig#getConnectionPool(java.lang.String)
	 */
	@Override
	public ConnectionPool getConnectionPool(final String poolName) {
		// 优先判定是否存在同名静态连接池
		final UniqueConnectionPool ucp = this.conUniPools.get(poolName);
		if (null == ucp) {
			// 查看是否与可变连接池同名
			if (this.changeablePoolName.equals(poolName)) {
				return this.conChgPool;
			} else {
				throw new NotExistsTargetException("Not Exists ConnectionPool name with [" + poolName + "]...");
			}
		}
		return ucp;
	}

	/**
	 * 系统注销时，需要一些连接操作的监听
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月4日 下午7:04:53
	 */
	private class ConnectionShutDownListener implements Runnable {

		/**
		 * @author Weijie Xu
		 * @dateTime 2015年5月4日 下午7:10:41
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				for (final UniqueConnectionPool ucp : ConnectionPoolManager.cpm.conUniPools.values()) {
					try {
						ucp.sysShutdown();
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
				try {
					ConnectionPoolManager.cpm.conChgPool.sysShutdown();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			} catch (final Exception e) {
				// 先简版处理
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
}
