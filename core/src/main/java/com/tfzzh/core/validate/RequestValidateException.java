/**
 * @author Weijie Xu
 * @dateTime 2014年4月9日 下午1:19:20
 */
package com.tfzzh.core.validate;

import com.tfzzh.exception.NestedRuntimeException;

/**
 * 请求验证异常
 * 
 * @author Weijie Xu
 * @dateTime 2014年4月9日 下午1:19:20
 */
public class RequestValidateException extends NestedRuntimeException {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年4月9日 下午1:25:49
	 */
	private static final long serialVersionUID = -3120775024554516455L;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年4月9日 下午1:20:11
	 * @param msg 消息内容
	 */
	public RequestValidateException(final String msg) {
		super(msg);
	}
}
