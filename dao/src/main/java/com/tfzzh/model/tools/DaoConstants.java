/**
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午4:04:30
 */
package com.tfzzh.model.tools;

/**
 * DAO配置项
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午4:04:30
 */
public interface DaoConstants {

	/**
	 * 数据库编码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午4:08:17
	 */
	String DATABASE_CODE = Messages.getString("DaoBaseConstants.DATABASE_CODE");

	/**
	 * 数据库类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午4:08:15
	 */
	String DATABASE_TYPE = Messages.getString("DaoBaseConstants.DATABASE_TYPE");

	/**
	 * 基础的连接空闲断开时限
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 下午5:01:19
	 */
	Long BASE_CONNECTION_IDLE_TIME_OUT = Long.parseLong(Messages.getString("DaoBaseConstants.BASE_CONNECTION_IDLE_TIME_OUT"));

	/**
	 * 连接获得繁忙等待时间
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午2:32:41
	 */
	Long CONNECTION_GET_BUSY_WAIT_TIME = Long.parseLong(Messages.getString("DaoBaseConstants.CONNECTION_GET_BUSY_WAIT_TIME"));

	/**
	 * 连接获得繁忙重试最大次数
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午2:32:43
	 */
	Integer CONNECTION_GET_BUSY_MAX_TRY_TIMES = Integer.parseInt(Messages.getString("DaoBaseConstants.CONNECTION_GET_BUSY_MAX_TRY_TIMES"));

	/**
	 * dao管理配置文件路径及命名
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月4日 下午12:01:29
	 */
	String MG_CONF_PATH = Messages.getString("DaoBaseConstants.MG_CONF_PATH");
}
