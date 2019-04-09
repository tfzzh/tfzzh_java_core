/**
 * @author Weijie Xu
 * @dateTime 2015年4月28日 下午12:34:02
 */
package com.tfzzh.model.dao.impl;

import com.tfzzh.model.pools.ConnectionBean;
import com.tfzzh.model.pools.ConnectionPool;

/**
 * 核心的SQL处理
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月28日 下午12:34:02
 */
public abstract class CoreDAOImpl {

	/**
	 * 相关连接池
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月28日 下午12:37:42
	 */
	private ConnectionPool connectionPool;

	/**
	 * 得到连接
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月28日 下午12:37:43
	 * @return 可用的连接
	 */
	protected ConnectionBean getConnection() {
		return this.connectionPool.getConnection(true);
	}

	/**
	 * 设置连接池
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月28日 下午12:37:45
	 * @param connectionPool 新的连接池
	 */
	public void setConnectionPool(final ConnectionPool connectionPool) {
		this.connectionPool = connectionPool;
	}
}
