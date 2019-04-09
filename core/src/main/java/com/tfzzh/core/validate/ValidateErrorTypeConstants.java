/**
 * @author Xu Weijie
 * @datetime 2017年9月27日_上午10:49:30
 */
package com.tfzzh.core.validate;

/**
 * 验证问题类型常量集
 * 
 * @author Xu Weijie
 * @datetime 2017年9月27日_上午10:49:30
 */
public interface ValidateErrorTypeConstants {

	/**
	 * 空问题
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午10:56:10
	 */
	String IS_EMPTY = "EMPTY";

	/**
	 * 错误的字段类型
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_下午4:36:23
	 */
	String ERR_TYPE = "ERR_TYPE";

	/**
	 * 非整型数值
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午10:56:10
	 */
	String NO_INTEGER = "INTEGER";

	/**
	 * 非非负整型数值
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午10:56:11
	 */
	String NO_NON_NEGA_INTEGER = "NON_NEGA_INTEGER";

	/**
	 * 非浮点数值
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午10:56:11
	 */
	String NO_NUMBER = "NUMBER";

	/**
	 * 非非负浮点数值
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午10:56:11
	 */
	String NO_NON_NEGA_NUMBER = "NON_NEGA_NUMBER";

	/**
	 * 超过目标范围区间
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午10:56:12
	 */
	String ABOVE_RANGE = "ABOVE_RANGE";

	/**
	 * 低于目标范围区间
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午11:00:06
	 */
	String BELOW_RANGE = "BELOW_RANGE";

	/**
	 * 超过目标长度限制
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午10:56:12
	 */
	String ABOVE_LENGTH = "ABOVE_LENGTH";

	/**
	 * 低于目标长度限制
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午11:02:05
	 */
	String BELOW_LENGTH = "BELOW_LENGTH";

	/**
	 * 正则验证未通过
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午11:06:38
	 */
	String REGULAR = "REGULAR";
}
