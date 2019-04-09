/**
 * @author Weijie Xu
 * @dateTime 2014年6月26日 下午3:10:42
 */
package com.tfzzh.socket;

/**
 * 会话类型
 * 
 * @author Weijie Xu
 * @dateTime 2014年6月26日 下午3:10:42
 */
public interface SessionType {

	/**
	 * 该类型对应的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月26日 下午3:11:11
	 * @return 类型值
	 */
	int getValue();

	/**
	 * 得到相关的属性键名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月26日 下午3:11:12
	 * @return 属性键名
	 */
	String getAttributeKey();

	/**
	 * 你懂
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月26日 下午8:26:19
	 * @return showContent
	 */
	@Override
	String toString();
}
