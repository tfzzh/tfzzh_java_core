/**
 * @author Weijie Xu
 * @dateTime 2014年6月26日 下午1:21:37
 */
package com.tfzzh.socket.tools;

import com.tfzzh.socket.RequestSession;

/**
 * Session控制
 * 
 * @author Weijie Xu
 * @dateTime 2014年6月26日 下午1:21:37
 * @model
 */
public interface SessionControl {

	/**
	 * 创建一个新Session时的操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月26日 下午1:21:37
	 * @param session 会话消息
	 */
	void create(RequestSession session);

	/**
	 * 开启一个新Session时的操作<br />
	 * 认为该过程在创建之后<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月26日 下午1:21:37
	 * @param session 会话消息
	 */
	void open(RequestSession session);

	/**
	 * Session空闲到一个阶段的操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月26日 下午1:21:37
	 * @param session 会话消息
	 */
	void idle(RequestSession session);

	/**
	 * 关闭一个Session时的操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月26日 下午1:21:37
	 * @param session 会话消息
	 */
	void closed(RequestSession session);

	/**
	 * Session接收到异常消息时
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月26日 下午1:21:37
	 * @param session 会话消息
	 * @param cause 异常消息
	 */
	void exception(RequestSession session, Throwable cause);

	/**
	 * 与目标服务异常断开连接情况
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月6日 上午11:01:15
	 * @param session 被异常的连接会话
	 */
	void exceptionClose(RequestSession session);

	/**
	 * 关闭服务器操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月26日 下午1:21:37
	 * @param session 会话消息
	 * @param operate 操作控制类型
	 */
	void serverOperate(RequestSession session, RequestSession operate);
}
