/**
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午4:04:30
 */
package com.tfzzh.model.tools;

import com.tfzzh.core.annotation.PropertiesValue;
import com.tfzzh.tools.BaseConstants;
import com.tfzzh.tools.CoreConstantsPool;

/**
 * DAO配置项
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午4:04:30
 */
public class DaoBaseConstants extends BaseConstants {

	static {
		CoreConstantsPool.getInstance().getConstants(DaoBaseConstants.class);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午6:02:38
	 */
	public DaoBaseConstants() {
		super("dao_messages");
	}

	/**
	 * 数据库编码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午4:08:17
	 */
	@PropertiesValue
	public static String DATABASE_CODE;

	/**
	 * 数据库类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午4:08:15
	 */
	@PropertiesValue
	public static String DATABASE_TYPE;

	/**
	 * 基础的连接空闲断开时限
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 下午5:01:19
	 */
	@PropertiesValue
	public static long BASE_CONNECTION_IDLE_TIME_OUT;

	/**
	 * 连接获得繁忙等待时间
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午2:32:41
	 */
	@PropertiesValue
	public static long CONNECTION_GET_BUSY_WAIT_TIME;

	/**
	 * 连接获得繁忙重试最大次数
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午2:32:43
	 */
	@PropertiesValue
	public static int CONNECTION_GET_BUSY_MAX_TRY_TIMES;

	/**
	 * 输出sql时，显示的最大语句长度
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午6:26:14
	 */
	@PropertiesValue
	public static int SQL_MAX_LENGTH;

	/**
	 * dao连接线程池中用key
	 * 
	 * @author tfzzh
	 * @dateTime 2020年10月27日 下午4:55:30
	 */
	@PropertiesValue(defVal = "DAO_CON")
	public static String TL_KEY;
}
