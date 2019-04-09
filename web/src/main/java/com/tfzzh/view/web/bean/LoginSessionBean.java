/**
 * @author Weijie Xu
 * @dateTime 2017年4月11日 上午10:26:38
 */
package com.tfzzh.view.web.bean;

import com.tfzzh.view.web.iface.LoginAccount;
import com.tfzzh.view.web.servlet.session.ClientSessionBean;

/**
 * 基于Web的登录会话
 * 
 * @author Weijie Xu
 * @dateTime 2017年4月11日 上午10:26:38
 */
public abstract class LoginSessionBean extends BaseWebSessionBean implements LoginAccount {

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年4月14日 上午2:34:43
	 */
	private static final long serialVersionUID = -1932474060676787337L;

	/**
	 * 设置客户端会话
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月14日 上午6:52:11
	 * @param session 客户端会话
	 */
	public abstract void setSession(ClientSessionBean session);
}
