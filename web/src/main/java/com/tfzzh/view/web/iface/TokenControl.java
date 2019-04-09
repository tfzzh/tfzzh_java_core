/**
 * @author Weijie Xu
 * @dateTime 2017年4月13日 下午2:54:56
 */
package com.tfzzh.view.web.iface;

import com.tfzzh.view.web.bean.LoginSessionBean;

/**
 * token操作的控制
 * 
 * @author Weijie Xu
 * @dateTime 2017年4月13日 下午2:54:56
 */
public interface TokenControl {

	/**
	 * 根据token以及clientId，得到对应的登录会话信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月13日 下午3:04:18
	 * @param token 用户token
	 * @param clientId 用户clientId
	 * @param clientScope 用户相关作用域
	 * @return 登录会话
	 */
	LoginSessionBean getLoginSessionByToken(String token, String clientId, String clientScope);

	/**
	 * 记录用户登录时token信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月13日 下午3:05:21
	 * @param ls 登录会话信息
	 * @return true，记录登录信息成功
	 */
	boolean saveLoginSession(LoginSessionBean ls);

	/**
	 * 记录用户登出时token信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月14日 上午2:52:13
	 * @param ls 登录会话信息
	 * @return true，登出数据记录成功
	 */
	boolean saveLogoutSession(LoginSessionBean ls);
}
