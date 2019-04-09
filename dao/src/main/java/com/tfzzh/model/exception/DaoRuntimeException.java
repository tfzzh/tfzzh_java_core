/**
 * @author Weijie Xu
 * @dateTime 2015年4月21日 下午7:43:27
 */
package com.tfzzh.model.exception;

import com.tfzzh.exception.NestedRuntimeException;

/**
 * DAO相关的运行时异常
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月21日 下午7:43:27
 */
public class DaoRuntimeException extends NestedRuntimeException {

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:44:18
	 */
	private static final long serialVersionUID = 1065880361171437700L;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:43:48
	 * @param msg 消息
	 */
	public DaoRuntimeException(final String msg) {
		super(msg);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:43:38
	 * @param msg 消息
	 * @param cause 抛出消息
	 */
	public DaoRuntimeException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
