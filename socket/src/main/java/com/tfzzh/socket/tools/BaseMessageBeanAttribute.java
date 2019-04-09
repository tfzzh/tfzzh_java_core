/**
 * @author Weijie Xu
 * @dateTime 2014年6月16日 下午6:31:59
 */
package com.tfzzh.socket.tools;

/**
 * 基础消息Bean属性
 * 
 * @author Weijie Xu
 * @dateTime 2014年6月16日 下午6:31:59
 */
public abstract class BaseMessageBeanAttribute {

	/**
	 * Bean编号
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月16日 下午6:37:31
	 */
	private final int code;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年6月16日 下午6:37:28
	 * @param code Bean编号
	 */
	public BaseMessageBeanAttribute(final int code) {
		this.code = code;
	}

	/**
	 * 得到Bean编号
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月16日 下午6:37:39
	 * @return the code
	 */
	public int getCode() {
		return this.code;
	}
}
