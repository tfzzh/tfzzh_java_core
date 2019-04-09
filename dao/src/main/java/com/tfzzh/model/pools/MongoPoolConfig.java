/**
 * @author tfzzh
 * @dateTime 2017年1月4日 下午1:56:42
 */
package com.tfzzh.model.pools;

/**
 * Mongo连接配置
 * 
 * @author tfzzh
 * @dateTime 2017年1月4日 下午1:56:42
 */
public interface MongoPoolConfig {

	/**
	 * 放入mongo用连接
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午1:56:42
	 * @param name 连接信息名
	 * @param url 地址
	 * @param dbName 目标库
	 * @param userName 用户名
	 * @param password 密码
	 * @return 得到新的mongo用连接
	 */
	Object putMongoConnectionInfo(String name, String url, String dbName, String userName, String password);

	/**
	 * 得到目标名称的连接池
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午1:56:42
	 * @param name 目标名称
	 * @return 目标连接池
	 */
	Object getPool(String name);
}
