/**
 * @author Weijie Xu
 * @dateTime 2014-3-20 下午5:13:35
 */
package com.tfzzh.exception;

/**
 * 游戏运行中的未知错误
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-20 下午5:13:35
 */
public class UnknowRuntimeException extends NestedRuntimeException {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年6月27日 下午5:37:34
	 */
	private static final long serialVersionUID = 6284048412142932466L;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年6月27日 下午5:37:19
	 * @param msg 消息信息
	 */
	public UnknowRuntimeException(final String msg) {
		super(msg);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-22 下午1:04:02
	 * @param cause 异常的内容
	 */
	public UnknowRuntimeException(final Throwable cause) {
		super(cause.getMessage(), cause);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午5:16:19
	 * @param msg 消息信息
	 * @param cause 异常的内容
	 */
	public UnknowRuntimeException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
