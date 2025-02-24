/**
 * @author xuweijie
 * @dateTime 2012-2-1 上午10:30:54
 */
package com.tfzzh.exception;

/**
 * 初始化异常</br>
 * 用于项目启动过程中，所遇到的问题</br>
 * 
 * @author xuweijie
 * @dateTime 2012-2-1 上午10:30:54
 */
public class InitializeException extends NestedRuntimeException {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年4月11日 上午10:44:46
	 */
	private static final long serialVersionUID = -3757039108265896818L;

	/**
	 * @author xuweijie
	 * @dateTime 2012-2-1 上午10:32:48
	 * @param msg 消息信息
	 */
	public InitializeException(final String msg) {
		super(msg);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年4月11日 上午10:44:22
	 * @param msg 消息
	 * @param cause 抛出消息
	 */
	public InitializeException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
