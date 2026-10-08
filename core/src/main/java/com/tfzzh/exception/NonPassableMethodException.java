/**
 * @author tfzzh
 * @dateTime 2025年9月4日 16:40:33
 */
package com.tfzzh.exception;

/**
 * 非可通行（调用）方法
 * 
 * @author tfzzh
 * @dateTime 2025年9月4日 16:40:33
 */
public class NonPassableMethodException extends NestedRuntimeException {

	/**
	 * @author tfzzh
	 * @dateTime 2025年9月4日 16:41:55
	 */
	private static final long serialVersionUID = 7883269341641989444L;

	/**
	 * @author tfzzh
	 * @dateTime 2025年9月4日 16:41:40
	 * @param msg 消息信息
	 */
	public NonPassableMethodException(final String msg) {
		super(msg);
	}
}
