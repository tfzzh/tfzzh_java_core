/**
 * @author XuWeijie
 * @datetime 2015年7月10日_下午4:16:15
 */
package com.tfzzh.model.pools;

import com.tfzzh.model.pools.ConnectionPool.ChangeableConnectionPool;

/**
 * 连接池配置
 * 
 * @author XuWeijie
 * @datetime 2015年7月10日_下午4:16:15
 */
public interface ConnectionPoolConfig {

	/**
	 * 放入一个静态连接池信息
	 * 
	 * @author Weijie Xu
	 * @datetime 2015年7月10日_下午4:16:15
	 * @param poolName 池名字
	 * @param infoName 连接信息名字
	 */
	void putUniqueConnectionInfo(String poolName, String infoName);

	/**
	 * 得到动态连接池，只有唯一的
	 * 
	 * @author Weijie Xu
	 * @datetime 2015年7月10日_下午4:16:15
	 * @return 动态连接池
	 */
	ChangeableConnectionPool getChangeableConnection();

	/**
	 * 设置可变连接池的名字
	 * 
	 * @author Weijie Xu
	 * @datetime 2015年7月10日_下午4:16:15
	 * @param poolName 可变连接池的名字
	 */
	void setChangeablePoolName(String poolName);

	/**
	 * 得到目标名称的连接池
	 * 
	 * @author Weijie Xu
	 * @datetime 2015年7月10日_下午4:16:15
	 * @param poolName 目标连接池名称
	 * @return 目标连接池对象； null，不存在目标
	 */
	ConnectionPool getConnectionPool(String poolName);
}
