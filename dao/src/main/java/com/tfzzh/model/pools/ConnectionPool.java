/**
 * @author Weijie Xu
 * @dateTime 2015年4月21日 下午5:06:56
 */
package com.tfzzh.model.pools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.tfzzh.exception.UnknowRuntimeException;
import com.tfzzh.log.CoreLog;
import com.tfzzh.model.exception.UnknowDaoException;
import com.tfzzh.model.tools.DaoConstants;
import com.tfzzh.tools.RunThreadLocal;

/**
 * 连接池
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月21日 下午5:06:56
 */
public abstract class ConnectionPool {

	/**
	 * 池名字
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午5:17:02
	 */
	private final String name;

	// /**
	// * 针对该池的控制锁
	// *
	// * @author Weijie Xu
	// * @dateTime 2015年4月21日 下午8:55:14
	// */
	// private final Object lock = new Object();
	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午6:33:27
	 */
	public ConnectionPool() {
		this(null);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午6:33:26
	 * @param name 池名字
	 */
	public ConnectionPool(final String name) {
		if (null != name) {
			this.name = name;
		} else {
			this.name = Integer.toString(this.hashCode());
		}
	}

	/**
	 * 得到名字
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午6:33:18
	 * @return 池名字
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 得到连接信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 上午11:16:06
	 * @return 连接信息
	 */
	public abstract ConnectionInfoBean getConnectionInfo();

	/**
	 * 设置连接信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 上午11:16:07
	 * @param info 连接信息
	 */
	public abstract void setConnectionInfo(ConnectionInfoBean info);

	/**
	 * 创建一个新连接
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午8:52:33
	 * @return 创建一个连接信息
	 */
	protected abstract ConnectionBean createConnection();

	/**
	 * 得到对应的数据库连接对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 上午10:47:14
	 * @param isMust 是否必须给出；<br />
	 *           true，必须给出，如果队列使用已满，则创建新的连接信息；<br />
	 *           false，非必须，如果队列已满则等待有空闲的出现，直到等待超时；<br />
	 * @return 连接对象<br />
	 *         null，表示在一定时限内，也未能得到需要的连接对象，表示繁忙，针对false情况；<br />
	 */
	public ConnectionBean getConnection(final boolean isMust) {
		final ConnectionInfoBean cib = this.getConnectionInfo();
		ConnectionBean cb = null;
		int i = 0;
		do {
			if (i++ > 0) {
				try {
					Thread.sleep(DaoConstants.CONNECTION_GET_BUSY_WAIT_TIME);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (i > DaoConstants.CONNECTION_GET_BUSY_MAX_TRY_TIMES) {
				// 认为连接超时，抛出null，让外面去处理
				return null;
			}
			cb = cib.getConnection();
			if ((null == cb) && isMust) {
				// 创建一个新的连接
				cb = this.createConnection();
			}
		} while (null == cb);
		return cb;
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:09:42
	 */
	public static class UniqueConnectionPool extends ConnectionPool {

		/**
		 * 连接信息
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月21日 下午7:16:13
		 */
		private final ConnectionInfoBean info;

		/**
		 * 临时存储
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月22日 下午7:46:49
		 */
		private final Properties pps;

		/**
		 * @author Weijie Xu
		 * @dateTime 2015年4月21日 下午7:16:12
		 * @param info 连接信息
		 */
		public UniqueConnectionPool(final ConnectionInfoBean info) {
			this(null, info);
		}

		/**
		 * @author Weijie Xu
		 * @dateTime 2015年4月21日 下午7:17:02
		 * @param name 池名字
		 * @param info 连接信息
		 */
		public UniqueConnectionPool(final String name, final ConnectionInfoBean info) {
			super(name);
			this.info = info;
			{
				final Properties pps = new Properties();
				pps.put("user", this.info.getUser());
				pps.put("password", this.info.getPass());
				// 是否unicode格式
				pps.put("useUnicode", String.valueOf(this.info.isUseUnicode()));
				// 传输字符编码格式
				pps.put("characterEncoding", this.info.getCharacterEncoding());
				if (this.info.getIdelTime() > 0) {
					pps.put("maxIdleTime", Long.toString(this.info.getIdelTime()));
				}
				// 放入其他
				pps.putAll(this.info.getOthers());
				this.pps = pps;
			}
			// 初始化驱动信息
			ConnectionPoolManager.getInstance().initDriver(this.info.getDriver());
			// 在连接信息中，放入自己
			this.info.reguestPool(this);
		}

		/**
		 * 得到连接信息
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月22日 下午3:06:59
		 * @see com.tfzzh.model.pools.ConnectionPool#getConnectionInfo()
		 */
		@Override
		public ConnectionInfoBean getConnectionInfo() {
			return this.info;
		}

		/**
		 * 设置连接信息
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月22日 下午3:07:24
		 * @see com.tfzzh.model.pools.ConnectionPool#setConnectionInfo(com.tfzzh.model.pools.ConnectionInfoBean)
		 */
		@Override
		@Deprecated
		public void setConnectionInfo(final ConnectionInfoBean info) {
			throw new UnknowDaoException("Cannot call this Method...");
		}

		/**
		 * 创建一个连接
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月21日 下午8:52:18
		 * @see com.tfzzh.model.pools.ConnectionPool#createConnection()
		 */
		@Override
		protected ConnectionBean createConnection() {
			try {
				final long l1 = System.currentTimeMillis();
				// 创建连接
				final Connection conn = DriverManager.getConnection(this.info.getUrl(), this.pps);
				// 设置只读属性
				conn.setReadOnly(this.info.isReadOnly());
				final ConnectionBean cb = new ConnectionBean(conn, this.info);
				// synchronized (super.lock) {
				// 被加入的都是使用的连接
				this.info.addUseCons(cb);
				// }
				if (CoreLog.getInstance().debugEnabled(this.getClass())) {
					CoreLog.getInstance().debug(this.getClass(), "Created[", Long.toString(System.currentTimeMillis() - l1), "] Unique Connection[", cb.toString(), "] is...");
				}
				return cb;
			} catch (final SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * 清理，将自身从主池中移除后的一些销毁操作
		 *
		 * @author Weijie Xu
		 * @dateTime 2015年4月22日 下午4:10:08
		 */
		protected void clear() {
			// 在连接信息中移除自身
			this.info.unreguestPool(this);
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), "clear Unique Connection[", super.name, "]...");
			}
		}

		/**
		 * 因系统关闭而销毁连接池
		 * 
		 * @author tfzzh
		 * @dateTime 2016年11月18日 下午2:16:11
		 */
		protected void sysShutdown() {
			// 在连接信息中移除自身
			this.info.shutdownPool(this);
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), "shutdown Unique Connection[", super.name, "]...");
			}
		}
	}

	/**
	 * 可变连接池<br />
	 * 概念，根据每用户请求过程中必定仅占用唯一服务器线程，并请求完成后会及时归还线程<br />
	 * 同时每操作，不可能同时多用户共享一线程，虽然单一用户根据不同请求所占线程可能不同<br />
	 * 前提，需要在请求的逻辑操作前后，增加对本地线程对象的放入与移除操作<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午2:28:05
	 */
	public static class ChangeableConnectionPool extends ConnectionPool {

		/**
		 * @author Weijie Xu
		 * @dateTime 2015年4月22日 下午2:35:26
		 */
		private final Map<Object, ConnectionInfoBean> userConnectionInfo = new HashMap<>();

		/**
		 * 设置连接信息（针对当前线程相关用户）
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月22日 下午3:16:29
		 * @see com.tfzzh.model.pools.ConnectionPool#setConnectionInfo(com.tfzzh.model.pools.ConnectionInfoBean)
		 */
		@Override
		public void setConnectionInfo(final ConnectionInfoBean info) {
			final Object tmpObj = RunThreadLocal.getInstance().getObject();
			if (null != tmpObj) {
				// 放入的一定是Copy版本的
				final ConnectionInfoBean oldInfo = this.userConnectionInfo.put(tmpObj, info);
				if (null != oldInfo) {
					// 在之前的连接信息中，移除当前池
					oldInfo.unreguestPool(this);
				}
				// 在当前的连接信息中，增加当前池
				info.reguestPool(this);
			} else {
				throw new UnknowRuntimeException("Not Exists UserSesion in ThreadLocal...");
			}
		}

		/**
		 * @author Weijie Xu
		 * @dateTime 2015年4月22日 下午3:04:52
		 * @see com.tfzzh.model.pools.ConnectionPool#getConnectionInfo()
		 */
		@Override
		public ConnectionInfoBean getConnectionInfo() {
			final Object tmpObj = RunThreadLocal.getInstance().getObject();
			if (null != tmpObj) {
				final ConnectionInfoBean cib = this.userConnectionInfo.get(tmpObj);
				if (null != cib) {
					return cib;
				} else {
					throw new UnknowRuntimeException("Not Exists UserSesion corresponding ConnectionInfoBean...");
				}
			} else {
				throw new UnknowRuntimeException("Not Exists UserSesion in ThreadLocal...");
			}
		}

		/**
		 * @author Weijie Xu
		 * @dateTime 2015年4月23日 下午9:04:36
		 * @see com.tfzzh.model.pools.ConnectionPool#createConnection()
		 */
		@Override
		protected ConnectionBean createConnection() {
			final ConnectionInfoBean info = this.getConnectionInfo();
			final Properties pps = new Properties();
			pps.put("user", info.getUser());
			pps.put("password", info.getPass());
			// 是否unicode格式
			pps.put("useUnicode", String.valueOf(info.isUseUnicode()));
			// 传输字符编码格式
			pps.put("characterEncoding", info.getCharacterEncoding());
			if (info.getIdelTime() > 0) {
				pps.put("maxIdleTime", Long.toString(info.getIdelTime()));
			}
			// 放入其他
			pps.putAll(info.getOthers());
			try {
				final long l1 = System.currentTimeMillis();
				// 创建连接
				final Connection conn = DriverManager.getConnection(info.getUrl(), pps);
				// 设置只读属性
				conn.setReadOnly(info.isReadOnly());
				final ConnectionBean cb = new ConnectionBean(conn, info);
				// 被加入的都是使用的连接
				info.addUseCons(cb);
				if (CoreLog.getInstance().debugEnabled(this.getClass())) {
					CoreLog.getInstance().debug(this.getClass(), "Created[", Long.toString(System.currentTimeMillis() - l1), "] Changeable Connection[", cb.toString(), "] is...");
				}
				return cb;
			} catch (final SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * 清理，将自身从主池中移除后的一些销毁操作
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月4日 下午7:13:31
		 */
		protected void clear() {
			final Iterator<ConnectionInfoBean> it = this.userConnectionInfo.values().iterator();
			while (it.hasNext()) {
				it.next().unreguestPool(this);
				it.remove();
			}
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), "clear Changeable Connection[", super.name, "]...");
			}
		}

		/**
		 * 因系统关闭而销毁连接池
		 * 
		 * @author tfzzh
		 * @dateTime 2016年11月18日 下午2:18:14
		 */
		protected void sysShutdown() {
			// 在连接信息中移除自身
			final Iterator<ConnectionInfoBean> it = this.userConnectionInfo.values().iterator();
			while (it.hasNext()) {
				it.next().shutdownPool(this);
				it.remove();
			}
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), "shutdown Changeable Connection[", super.name, "]...");
			}
		}
	}
}
