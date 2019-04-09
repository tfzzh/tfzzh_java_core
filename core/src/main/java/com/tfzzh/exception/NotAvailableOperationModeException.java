/**
 * @author XuWeijie
 * @datetime 2015年7月10日_下午1:34:56
 */
package com.tfzzh.exception;

/**
 * 没有可用的操作模式
 * 
 * @author XuWeijie
 * @datetime 2015年7月10日_下午1:34:56
 */
public class NotAvailableOperationModeException extends UnknowRuntimeException {

	/**
	 * @author XuWeijie
	 * @datetime 2015年7月10日_下午1:35:46
	 */
	private static final long serialVersionUID = -4674992559784596727L;

	/**
	 * @author XuWeijie
	 * @datetime 2015年7月10日_下午1:35:12
	 * @param msg 消息
	 */
	public NotAvailableOperationModeException(final String msg) {
		super(msg);
	}
}
