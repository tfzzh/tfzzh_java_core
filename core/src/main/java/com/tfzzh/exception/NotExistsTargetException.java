/**
 * @author XuWeijie
 * @datetime 2015年7月10日_下午1:43:16
 */
package com.tfzzh.exception;

/**
 * 预期目标不存在
 * 
 * @author XuWeijie
 * @datetime 2015年7月10日_下午1:43:16
 */
public class NotExistsTargetException extends UnknowRuntimeException {

	/**
	 * @author XuWeijie
	 * @datetime 2015年7月10日_下午1:43:49
	 */
	private static final long serialVersionUID = 1866725470443991045L;

	/**
	 * @author XuWeijie
	 * @datetime 2015年7月10日_下午1:43:28
	 * @param msg 消息
	 */
	public NotExistsTargetException(final String msg) {
		super(msg);
	}
}
