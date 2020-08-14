/**
 * @author tfzzh
 * @dateTime 2020年8月13日 上午1:23:11
 */
package com.tfzzh.view.web.bean;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.tfzzh.core.control.bean.BaseSessionBean;

/**
 * 基于Token的系统消息基础BEAN<br />
 * 该对象不做远端消息传递用，均为项目内调用<br />
 * 
 * @author tfzzh
 * @dateTime 2020年8月13日 上午1:23:11
 */
public abstract class BaseTokenBean extends BaseSessionBean {

	/**
	 * @author tfzzh
	 * @dateTime 2020年8月15日 上午2:51:29
	 */
	private static final long serialVersionUID = 7451161911572473307L;

	/**
	 * 端请求相关token<br />
	 * <端code, 端token><br />
	 * 
	 * @author tfzzh
	 * @dateTime 2020年8月13日 下午9:48:58
	 */
	private final Map<String, String> clientToken = new HashMap<>();

	/**
	 * 得到目标端对应token
	 * 
	 * @author tfzzh
	 * @dateTime 2020年8月13日 上午1:24:12
	 * @param clientCode 端code
	 * @return token 端对应token<br />
	 *         null，没有目标端token<br />
	 */
	public String getClientToken(final String clientCode) {
		return this.clientToken.get(clientCode);
	}

	/**
	 * 设置端的新token
	 * 
	 * @author tfzzh
	 * @dateTime 2020年8月13日 下午9:39:38
	 * @param clientCode 端code
	 * @param token 新的token
	 * @return 此端之前的token
	 */
	public String setToken(final String clientCode, final String token) {
		return this.clientToken.put(clientCode, token);
	}

	/**
	 * 清理指定端token
	 * 
	 * @author tfzzh
	 * @dateTime 2020年8月13日 下午9:39:16
	 * @param clientCode 端code
	 * @return 被移除的端的token
	 */
	public String clearToken(final String clientCode) {
		return this.clientToken.remove(clientCode);
	}

	/**
	 * 得到所有端相关token
	 * 
	 * @author tfzzh
	 * @dateTime 2020年8月15日 上午2:50:55
	 * @return 所有端相关token
	 */
	@JSONField(serialize = false)
	public Map<String, String> getAllToken() {
		return this.clientToken;
	}
}
