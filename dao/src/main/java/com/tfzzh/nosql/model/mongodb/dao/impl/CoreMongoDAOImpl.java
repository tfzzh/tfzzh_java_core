/**
 * @author tfzzh
 * @dateTime 2017年1月4日 下午12:25:24
 */
package com.tfzzh.nosql.model.mongodb.dao.impl;

import com.tfzzh.nosql.model.mongodb.MongoDatabaseData;
import com.tfzzh.nosql.model.mongodb.pools.MongoPool;

/**
 * @author tfzzh
 * @dateTime 2017年1月4日 下午12:25:24
 */
public class CoreMongoDAOImpl {

	/**
	 * 相关连接池
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午12:25:35
	 */
	private MongoPool mongoPool;

	/**
	 * 得到连接
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午12:25:36
	 * @return 可用的连接
	 */
	protected MongoDatabaseData getPool() {
		return this.mongoPool.getDatabase();
	}

	/**
	 * 设置连接池
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午12:25:37
	 * @param mongoPool 新的连接池
	 */
	public void setMongoPool(final MongoPool mongoPool) {
		this.mongoPool = mongoPool;
	}
}
