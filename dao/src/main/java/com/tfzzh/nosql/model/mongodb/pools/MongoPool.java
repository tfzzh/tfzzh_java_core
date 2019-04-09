/**
 * @author tfzzh
 * @dateTime 2016年11月18日 下午1:45:47
 */
package com.tfzzh.nosql.model.mongodb.pools;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.tfzzh.log.CoreLog;
import com.tfzzh.nosql.model.mongodb.MongoDatabaseData;

/**
 * mongo连接池
 * 
 * @author tfzzh
 * @dateTime 2016年11月18日 下午1:45:47
 */
public class MongoPool {

	/**
	 * 连接池的名字
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午2:52:45
	 */
	private final MongoConnectionInfoBean connectionInfo;

	/**
	 * 连接信息
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午5:33:30
	 */
	private MongoClient mc = null;

	/**
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午2:53:29
	 * @param ci 连接信息
	 */
	public MongoPool(final MongoConnectionInfoBean ci) {
		this.connectionInfo = ci;
	}

	/**
	 * 得到连接池名称
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午5:31:51
	 * @return 连接池名称
	 */
	public String getName() {
		return this.connectionInfo.getName();
	}

	/**
	 * 得到连接所相关数据库信息
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午6:11:27
	 * @return 所相关数据库信息
	 */
	// public MongoDatabase getDatabase() {
	// if (null == this.mc) {
	// synchronized (this) {
	// this.mc = this.connectionInfo.getClient();
	// if (MongoPool.log.isDebugEnabled()) {
	// MongoPool.log.debug(new StringBuilder().append("new Mongo Client[" + this.connectionInfo.getName() + ":" + this.connectionInfo.getDbName() + "] Create ... "));
	// }
	// }
	// }
	// MongoDatabase md = this.mc.getDatabase(this.connectionInfo.getDbName());
	//
	// return md;
	// }
	public MongoDatabaseData getDatabase() {
		if (null == this.mc) {
			synchronized (this) {
				this.mc = this.connectionInfo.getClient();
				// if (MongoPool.log.isDebugEnabled()) {
				// MongoPool.log.debug(new StringBuilder().append("new Mongo Client[" + this.connectionInfo.getName() + ":" + this.connectionInfo.getDbName() + "] Create ... "));
				// }
				CoreLog.getInstance().debug(MongoPool.class, "new Mongo Client[", this.connectionInfo.getName(), ":", this.connectionInfo.getDbName(), "] Create ... ");
			}
		}
		final MongoDatabase md = this.mc.getDatabase(this.connectionInfo.getDbName());
		final MongoDatabaseData mdd = new MongoDatabaseData(md);
		return mdd;
	}
}
