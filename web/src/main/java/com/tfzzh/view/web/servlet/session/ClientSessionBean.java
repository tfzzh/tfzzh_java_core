/**
 * @author Weijie Xu
 * @dateTime 2017年3月21日 上午11:20:26
 */
package com.tfzzh.view.web.servlet.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfzzh.core.control.tools.ManagerMap;
import com.tfzzh.tools.BaseBean;
import com.tfzzh.tools.StringTools;
import com.tfzzh.view.web.bean.LoginSessionBean;
import com.tfzzh.view.web.iface.TokenControl;
import com.tfzzh.view.web.tools.ClientTypeEnum;
import com.tfzzh.view.web.tools.WebBaseConstants;

/**
 * 自建客户端会话数据，以替代服务容器的session
 * 
 * @author Weijie Xu
 * @dateTime 2017年3月21日 上午11:20:26
 */
public class ClientSessionBean extends BaseBean {

	/**
	 * @author Xu Weijie
	 * @datetime 2017年12月6日_下午9:12:59
	 */
	private static final long serialVersionUID = 1395826399787829889L;

	/**
	 * 客户端token
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月21日 上午11:37:20
	 */
	private final String token;

	/**
	 * 最后使用时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月28日 上午9:56:24
	 */
	private long lastUseTime;

	/**
	 * 客户端类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月28日 下午5:59:48
	 */
	private final ClientTypeEnum type;

	/**
	 * 客户登录信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月11日 上午10:25:48
	 */
	private LoginSessionBean loginInfo;

	/**
	 * 参数等列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月28日 下午7:27:31
	 */
	private final Map<String, Object> params = new ConcurrentHashMap<>();

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 上午9:56:59
	 * @param request 请求消息
	 * @param response 返回用消息
	 * @param token 客户端token
	 * @param type 客户端类型
	 */
	public ClientSessionBean(final HttpServletRequest request, final HttpServletResponse response, final String token, final ClientTypeEnum type) {
		this.token = token;
		this.lastUseTime = System.currentTimeMillis();
		this.type = type;
		// 更新cookie中的token相关信息
		this.saveDataToCookie(request, response, WebBaseConstants.CUSTOM_SESSION_TOKEN_KEY, token, WebBaseConstants.COOKIE_TIME);
	}

	/**
	 * 得到客户端token
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月27日 下午7:16:51
	 * @return the token
	 */
	public String getToken() {
		return this.token;
	}

	/**
	 * 得到客户端ID
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月27日 下午7:16:51
	 * @return the clientId
	 */
	public String getClientId() {
		if (null == this.loginInfo) {
			return null;
		}
		return this.loginInfo.getClientId();
	}

	/**
	 * 得到客户端所相关作用域
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年12月6日_下午2:49:39
	 * @return 客户端所相关作用域
	 */
	public String getClientScope() {
		if (null == this.loginInfo) {
			return null;
		}
		return this.loginInfo.getClientScope();
	}

	/**
	 * 得到登录信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月11日 下午7:25:30
	 * @return the loginInfo
	 */
	public LoginSessionBean getLoginInfo() {
		return this.loginInfo;
	}

	/**
	 * 得到客户端类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月28日 下午6:00:55
	 * @return the type
	 */
	public ClientTypeEnum getType() {
		return this.type;
	}

	/**
	 * 刷新最后使用时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月28日 上午9:57:19
	 */
	protected void refreshUseTime() {
		this.lastUseTime = System.currentTimeMillis();
	}

	/**
	 * 验证是否过期
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月28日 上午10:01:49
	 * @param referenceTime 参考时间
	 * @return true，已经过期；<br />
	 *         false，还未过期；<br />
	 */
	public boolean validateOverdue(final long referenceTime) {
		return (referenceTime - this.lastUseTime) > WebBaseConstants.SESSION_EXPIRATION_TIME;
	}

	/**
	 * 放入一个参数数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月28日 下午7:28:56
	 * @param key 键名
	 * @param val 目标值
	 * @return 对应该键名，之前所存的值
	 */
	public Object putParam(final String key, final Object val) {
		return this.params.put(key, val);
	}

	/**
	 * 得到一个参数数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月28日 下午7:28:57
	 * @param key 键名
	 * @return 对应的值
	 */
	public Object getParam(final String key) {
		return this.params.get(key);
	}

	/**
	 * 移除一个参数数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月11日 下午6:20:04
	 * @param key 键名
	 * @return 删除前的目标参数值
	 */
	public Object removeParam(final String key) {
		return this.params.remove(key);
	}

	/**
	 * 进行帐号登录操作<br />
	 * 同时进行对池中对应数据的更新操作<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月11日 上午11:42:52
	 * @param request 请求消息
	 * @param response 返回用消息
	 * @param loginInfo 所相关用户登录信息
	 */
	public void login(final HttpServletRequest request, final HttpServletResponse response, final LoginSessionBean loginInfo) {
		if (null != this.loginInfo) {
			SessionPool.getInstance().removeClientSession(this);
		}
		this.loginInfo = loginInfo;
		this.loginInfo.setSession(this);
		SessionPool.getInstance().putClientSession(this);
		final TokenControl tc = (TokenControl) ManagerMap.getInstance().getManager(WebBaseConstants.CUSTOM_SESSION_TOKEN_CONTROL_IMPL_NAME);
		if (null != tc) {
			// 可能的记录进库的操作
			tc.saveLoginSession(this.loginInfo);
		}
		// 刷新cookie中的客户相关信息
		this.saveDataToCookie(request, response, WebBaseConstants.CUSTOM_SESSION_NAME_KEY, loginInfo.getClientId(), WebBaseConstants.COOKIE_TIME);
		final String scope = loginInfo.getClientScope();
		if (!StringTools.isNullOrEmpty(scope)) {
			this.saveDataToCookie(request, response, WebBaseConstants.CUSTOM_SESSION_SCOPE_KEY, scope, WebBaseConstants.COOKIE_TIME);
		}
	}

	/**
	 * 进行帐号登出操作，用于外部操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月14日 上午12:40:46
	 */
	public void logout() {
		this.logout(null, null);
	}

	/**
	 * 进行帐号登出操作，用于外部操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月11日 下午4:20:41
	 * @param request 请求消息
	 * @param response 返回用消息
	 */
	public void logout(final HttpServletRequest request, final HttpServletResponse response) {
		SessionPool.getInstance().removeClientSession(this);
		if (null != this.loginInfo) {
			if ((null != request) && (null != response)) {
				this.saveDataToCookie(request, response, WebBaseConstants.CUSTOM_SESSION_NAME_KEY, "", -1);
				this.saveDataToCookie(request, response, WebBaseConstants.CUSTOM_SESSION_SCOPE_KEY, "", -1);
			}
			final TokenControl tc = (TokenControl) ManagerMap.getInstance().getManager(WebBaseConstants.CUSTOM_SESSION_TOKEN_CONTROL_IMPL_NAME);
			if (null != tc) {
				// 可能的记录进库的操作
				tc.saveLogoutSession(this.loginInfo);
			}
			this.loginInfo.setSession(null);
			this.innerLogout();
		}
	}

	/**
	 * 进行帐号登出操作，内部结算用操作<br />
	 * 一般为针对SessionPool中的调用<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月11日 上午10:19:02
	 */
	protected void innerLogout() {
		this.loginInfo = null;
	}

	/**
	 * 进行失效操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月11日 下午5:40:03
	 */
	protected void fail() {
		this.params.clear();
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年4月13日 下午11:03:25
	 * @param request 请求消息
	 * @param response 返回用消息
	 * @param key cookie的key
	 * @param value cookie的值
	 * @param lifecycle 生命周期
	 */
	private void saveDataToCookie(final HttpServletRequest request, final HttpServletResponse response, final String key, final String value, final int lifecycle) {
		// final String s = request.getServerName();
		if (null == WebBaseConstants.CUSTOM_SESSION_TOKEN_HOST_NAME) {
			return;
		}
		final Cookie ck = new Cookie(key, value);
		// if (s.indexOf(WebBaseConstants.CUSTOM_SESSION_TOKEN_HOST_NAME) == -1) {
		//// 不存在目标域名的，一般认为是测试环境
		// ck.setDomain(s);
		// } else {
		ck.setDomain(WebBaseConstants.CUSTOM_SESSION_TOKEN_HOST_NAME);
		// }
		ck.setPath("/");
		ck.setMaxAge(lifecycle);
		// if (CoreLog.getInstance().debugEnabled(this.getClass())) {
		// CoreLog.getInstance().debug(this.getClass(), "add [", ck.getDomain(), "] cookie[", ck.getName(), ":", ck.getValue(), "]");
		// }
		response.addCookie(ck);
	}

	/**
	 * 你知道你懂得
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月16日 上午11:44:14
	 * @return 你懂得
	 * @see com.tfzzh.tools.BaseBean#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(128);
		sb.append("{'type':'").append(this.type.name());
		sb.append("','token':'").append(this.token);
		if (null != this.loginInfo) {
			sb.append("','clientId':").append(this.loginInfo.getClientId());
		}
		sb.append("','lastUseTime':").append(this.lastUseTime).append("}");
		return sb.toString();
	}
}
