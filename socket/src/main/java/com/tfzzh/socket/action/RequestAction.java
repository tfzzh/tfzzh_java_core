/**
 * @author Weijie Xu
 * @dateTime 2014-3-18 下午7:53:21
 */
package com.tfzzh.socket.action;

import com.tfzzh.socket.RequestSession;

/**
 * 请求的行为
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-18 下午7:53:21
 */
public interface RequestAction {

	/**
	 * 消息执行，新方式
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月28日 上午9:55:03
	 * @param methodType 方法类型对象
	 * @param session 请求的会话
	 * @param msg 消息信息
	 */
	void exec(Object methodType, RequestSession session, BaseMessageBean msg);

	/**
	 * 得到方法类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年7月18日 下午3:57:29
	 * @param code 方法code
	 * @return 方法类型
	 */
	default Object getMethodType(final int code) {
		return null;
	}

	/**
	 * 得到方法类型，该方法不能被直接调用，只是在项目核心部分使用
	 *
	 * @author Weijie Xu
	 * @dateTime 2014年10月27日 下午3:52:04
	 * @param methodName 方法名
	 * @return 方法类型，实际应该是个枚举对象
	 */
	default Object getMethodType(final String methodName) {
		return null;
	}
}
