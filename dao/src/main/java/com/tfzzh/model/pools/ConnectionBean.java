/**
 * @author Weijie Xu
 * @dateTime 2015年4月22日 下午2:03:07
 */
package com.tfzzh.model.pools;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.tfzzh.log.CoreLog;
import com.tfzzh.model.exception.SqlBatchException;

/**
 * 连接对象
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月22日 下午2:03:07
 */
public class ConnectionBean implements AutoCloseable {

	/**
	 * 数据库连接对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:33:26
	 */
	private final Connection conn;

	/**
	 * 所相关的连接数据信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午8:44:47
	 */
	private final ConnectionInfoBean info;

	/**
	 * 预期超时时间，超时的时间点
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午8:31:59
	 */
	private long timeOut;

	/**
	 * 是否自动提交，与批量操作有关，缓存类属性
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午8:32:11
	 */
	private boolean isAutoCommit;

	/**
	 * 已经积累的批量数据数量<br />
	 * 自动提交时，此值为0<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午8:32:32
	 */
	private int batchCount = 0;

	/**
	 * 所在连接信息被实际创建连接对象的索引值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午8:30:13
	 */
	private final int id;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午8:45:13
	 * @param conn 数据相关连接信息
	 * @param info 连接数据信息
	 * @throws SQLException 抛
	 */
	protected ConnectionBean(final Connection conn, final ConnectionInfoBean info) throws SQLException {
		this.id = info.getConnectionIndex();
		this.info = info;
		this.conn = conn;
		this.isAutoCommit = conn.getAutoCommit();
		this.timeOut = System.currentTimeMillis() + info.getTimeOut();
		// if (ConnectionBean.log.isDebugEnabled()) {
		// ConnectionBean.log.debug(new StringBuilder().append("在连接信息：").append(info.getName()).append("-中创建新的连接: ").append(this.id).toString());
		// }
		CoreLog.getInstance().debug(ConnectionBean.class, "In connection info：", info.getName(), "-create new: ", Integer.toString(this.id));
	}

	/**
	 * 刷新超时时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午8:46:14
	 */
	protected void refreshUseTime() {
		this.timeOut = System.currentTimeMillis() + this.info.getTimeOut();
	}

	/**
	 * 得到表结构
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月29日 下午2:51:55
	 * @return 表结构
	 * @throws SQLException 抛
	 */
	public DatabaseMetaData getMetaData() throws SQLException {
		return this.conn.getMetaData();
	}

	/**
	 * 得到数据库操作用Ps对象，单条的及时数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:57:38
	 * @param sql 目标sql
	 * @return 编制的声明
	 * @throws SQLException 抛
	 */
	public PreparedStatement prepareStatement(final String sql) throws SQLException {
		if (this.batchCount != 0) {
			// 该种情况是不允许的
			throw new SqlBatchException(this.info.getName(), this.batchCount);
		}
		if (!this.isAutoCommit) {
			this.isAutoCommit = true;
			this.conn.setAutoCommit(true);
		}
		return this.conn.prepareStatement(sql);
	}

	/**
	 * 得到数据库操作用Ps对象，单条的及时数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:57:37
	 * @param sql 目标sql
	 * @param autoGeneratedKeys 是否返回生成的键0
	 * @return 编制的声明
	 * @throws SQLException 抛
	 */
	public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
		if (this.batchCount != 0) {
			// 该种情况是不允许的
			throw new SqlBatchException(this.info.getName(), this.batchCount);
		}
		if (!this.isAutoCommit) {
			this.isAutoCommit = true;
			this.conn.setAutoCommit(true);
		}
		return this.conn.prepareStatement(sql, autoGeneratedKeys);
	}

	/**
	 * 得到数据库操作用Ps对象，批量前提
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 下午8:37:24
	 * @param sql 目标sql
	 * @return 编制的声明
	 * @throws SQLException 抛
	 */
	public PreparedStatement prepareStatementBatch(final String sql) throws SQLException {
		if (this.isAutoCommit) {
			this.isAutoCommit = false;
			this.conn.setAutoCommit(false);
		}
		this.batchCount++;
		return this.conn.prepareStatement(sql);
	}

	/**
	 * 得到数据库操作用Ps对象，批量前提
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 下午8:37:23
	 * @param sql 目标sql
	 * @param autoGeneratedKeys 是否返回生成的键0
	 * @return 编制的声明
	 * @throws SQLException 抛
	 */
	public PreparedStatement prepareStatementBatch(final String sql, final int autoGeneratedKeys) throws SQLException {
		if (this.isAutoCommit) {
			// 关闭自动提交
			this.isAutoCommit = false;
			this.conn.setAutoCommit(false);
		}
		this.batchCount++;
		return this.conn.prepareStatement(sql, autoGeneratedKeys);
	}

	/**
	 * 对批量的操作，进行提交
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午8:22:43
	 * @throws SQLException 抛
	 */
	public void commit() throws SQLException {
		if (!this.isAutoCommit) {
			this.conn.commit();
			this.batchCount = 0;
		}
	}

	/**
	 * 释放连接
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月28日 下午1:02:58
	 */
	public void release() {
		this.info.releaseConn(this);
	}

	/**
	 * 关闭（释放）连接
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月28日 下午1:03:10
	 */
	@Override
	public void close() {
		this.info.releaseConn(this);
	}

	/**
	 * 是否为可用状态
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午8:24:02
	 * @return true，是可用的；<br />
	 *         false，已经不可用；<br />
	 */
	protected boolean canUse() {
		try {
			if (this.isClosed()) {
				// 因为已经关闭
				return false;
			} else if (this.timeOut < System.currentTimeMillis()) {
				// 因為自身时间超时，而进行主动的关闭
				this.conn.close();
				return false;
			} else {
				// 在可用时间
				return true;
			}
		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 验证连接是否可用<br />
	 * 如果是返回的可用，则已经进行了 一些相关处理<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 下午3:13:58
	 * @return true，是可用的；<br />
	 *         false，已经不可用了；<br />
	 */
	protected boolean validateConn() {
		// 自身是否已经关闭
		if (this.isClosed()) {
			// 因为已经关闭
			return false;
		}
		if (System.currentTimeMillis() > this.timeOut) {
			// 做为空闲过长的超时关闭
			this.toClose(CloseType.TimeOut);
			return false;
		}
		// 用线程发送一条消息
		new Thread() {

			/**
			 * 你懂的
			 * 
			 * @author Weijie Xu
			 * @dateTime 2015年4月23日 下午5:29:47
			 * @see java.lang.Thread#run()
			 */
			@Override
			public void run() {
				try {
					final PreparedStatement ps = ConnectionBean.this.conn.prepareStatement("select 1;");
					ps.execute();
					ps.close();
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}
		}.start();
		return true;
	}

	/**
	 * 进行关闭操作<br />
	 * 需要自行控制，将执行了此操作的连接对象从连接信息中移除<br />
	 * 因为还有一些调用概念没理清，不好做进一步自动的触发管理<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 下午7:12:08
	 * @param type 被关闭的类型
	 */
	private void toClose(final CloseType type) {
		final boolean isClosed = this.isClosed();
		if (!isClosed) {
			try {
				this.conn.close();
			} catch (final SQLException e) {
				e.printStackTrace();
			}
			// if (ConnectionBean.log.isDebugEnabled()) {
			// ConnectionBean.log.debug(new StringBuilder(">在连接信息：").append(this.info.getName()).append("-中因[").append(type.name()).append("]关闭连接: ").append(this.id).toString());
			// }
			CoreLog.getInstance().debug(ConnectionBean.class, ">connection info：", this.info.getName(), "- cause [", type.name(), "] close: ", Integer.toString(this.id));
		}
	}

	/**
	 * 连接是否已经被关闭<br />
	 * 如果有异常，为false<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午8:23:16
	 * @return true，已经关闭；<br />
	 *         false，还在可用状态；<br />
	 */
	private boolean isClosed() {
		try {
			return this.conn.isClosed();
		} catch (final SQLException e) {
			return false;
		}
	}

	/**
	 * 因为太多而关闭
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午8:24:06
	 */
	protected void closeWithTooMany() {
		this.toClose(CloseType.TooMany);
	}

	/**
	 * 因为连接所相关的信息，已经失去了所有池的调用关系
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 下午8:36:34
	 */
	protected void closeWithFall() {
		try {
			this.toClose(CloseType.InfoFall);
		} catch (final Exception e) {
			final boolean isClosed;
			isClosed = this.isClosed();
			if (!isClosed) {
				try {
					this.conn.close();
				} catch (final SQLException e1) {
				}
			}
		}
	}

	/**
	 * 因为系统关闭而结束连接
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午2:13:05
	 */
	protected void closeWithShutdown() {
		try {
			if (!this.conn.isClosed()) {
				// 如果未关闭， 进行关闭
				this.conn.close();
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		} finally {
			// if (ConnectionBean.log.isDebugEnabled()) {
			// ConnectionBean.log.debug(new StringBuilder(">在连接信息：").append(this.info.getName()).append("-中因[系统关闭]关闭连接: ").append(this.id).toString());
			// }
			CoreLog.getInstance().debug(ConnectionBean.class, ">connection info：", this.info.getName(), "- cause [System Close] close: ", Integer.toString(this.id));
		}
	}

	/**
	 * 你懂的
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午7:44:53
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{Connection:").append(this.info.getUrl()).append(">>").append(this.id).append('}');
		return sb.toString();
	}

	/**
	 * 被关闭的类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月18日 下午3:40:10
	 */
	private enum CloseType {
		/**
		 * 空闲超时
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月18日 下午3:40:15
		 */
		TimeOut,
		/**
		 * 已经存在过多的连接
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月18日 下午3:40:17
		 */
		TooMany,
		/**
		 * 连接信息已经失宠，没有被人任何池调用
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月18日 下午3:40:19
		 */
		InfoFall;
	}
}
