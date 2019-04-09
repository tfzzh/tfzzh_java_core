/**
 * @author Weijie Xu
 * @dateTime 2012-7-17 上午1:17:34
 */
package com.tfzzh.view.web.iface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfzzh.core.iface.UserSession;
import com.tfzzh.view.web.servlet.session.ClientSessionBean;

/**
 * 登陆中的帐号信息
 * 
 * @author Weijie Xu
 * @dateTime 2012-7-17 上午1:17:34
 */
public interface LoginAccount extends UserSession {

	/**
	 * 进行登录操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月11日 上午10:57:44
	 * @param request 请求消息
	 * @param response 返回用消息
	 * @param session 客户端会话
	 * @param ip 登录时客户端所相关IP
	 * @return true，登录成功
	 */
	boolean login(HttpServletRequest request, HttpServletResponse response, ClientSessionBean session, String ip);

	/**
	 * 移除与session关系<br />
	 * 表示登出操作<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2012-7-17 上午1:18:19
	 * @param request 请求消息
	 * @param response 返回用消息
	 */
	void logout(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 是否有对目标地址/接口的访问权限
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-15 下午1:15:27
	 * @param accessPermission 访问权限值
	 * @return true，可以访问；<br />
	 *         false，不可以访问；<br />
	 */
	boolean canAccess(int accessPermission);
}
