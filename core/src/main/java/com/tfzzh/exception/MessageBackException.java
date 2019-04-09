/**
 * @author XuWeijie
 * @dateTime May 19, 2010 10:35:10 AM
 */
package com.tfzzh.exception;

import com.tfzzh.tools.NestedExceptionUtils;

/**
 * 用于返回消息的异常控制
 * 
 * @author XuWeijie
 * @dateTime May 19, 2010 10:35:10 AM
 * @model
 */
public class MessageBackException extends NestedRuntimeException {

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-7-13 下午4:12:45
	 */
	private static final long serialVersionUID = -7452769987323808199L;

	/**
	 * @author XuWeijie
	 * @dateTime May 19, 2010 10:35:13 AM
	 * @param msg 消息内容集合
	 */
	public MessageBackException(final String... msg) {
		super(NestedExceptionUtils.messageCombination(msg));
	}
}
