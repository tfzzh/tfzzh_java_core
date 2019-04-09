/**
 * @author tfzzh
 * @dateTime 2016年11月18日 下午1:48:18
 */
package com.tfzzh.nosql.model.mongodb.pools;

import java.util.HashMap;
import java.util.Map;

import com.tfzzh.exception.NotAvailableOperationModeException;
import com.tfzzh.model.pools.MongoPoolConfig;

/**
 * @author tfzzh
 * @dateTime 2016年11月18日 下午1:48:18
 */
public class MongoPoolManager implements MongoPoolConfig {

	/**
	 * 连接池列表
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午1:50:47
	 */
	private final Map<String, MongoPool> infos = new HashMap<>();

	/**
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午2:50:48
	 */
	private static MongoPoolManager mpm;

	/**
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午2:50:47
	 */
	public MongoPoolManager() {
		if (null == MongoPoolManager.mpm) {
			MongoPoolManager.mpm = this;
		} else {
			throw new NotAvailableOperationModeException("The MongoPoolManager Cannt be Created More than Twice...");
		}
	}

	/**
	 * 得到对象实例
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午2:51:02
	 * @return 对象唯一实力
	 */
	public static MongoPoolManager getInstance() {
		return MongoPoolManager.mpm;
	}

	/**
	 * 放入mongo用连接
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月23日 下午2:17:41
	 * @param name 连接信息名
	 * @param url 地址
	 * @param dbName 目标库
	 * @param userName 用户名
	 * @param password 密码
	 * @return 得到新的mongo用连接
	 * @see com.tfzzh.model.pools.MongoPoolConfig#putMongoConnectionInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public MongoPool putMongoConnectionInfo(final String name, final String url, final String dbName, final String userName, final String password) {
		final MongoConnectionInfoBean mc = new MongoConnectionInfoBean(name, url, dbName, userName, password);
		final MongoPool mp = new MongoPool(mc);
		this.infos.put(name, mp);
		return mp;
	}

	/**
	 * 得到目标名称的连接池
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月23日 下午2:15:01
	 * @param name 目标名称
	 * @return 目标连接池
	 * @see com.tfzzh.model.pools.MongoPoolConfig#getPool(java.lang.String)
	 */
	@Override
	public MongoPool getPool(final String name) {
		return this.infos.get(name);
	}
}
