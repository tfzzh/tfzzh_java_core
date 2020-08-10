/**
 * @author Weijie Xu
 * @dateTime 2013-9-27 下午8:34:53
 */
package com.tfzzh.view.web.bean;

import com.tfzzh.core.control.bean.BaseSessionBean;

/**
 * 基于Web的系统消息基础BEAN<br />
 * 该对象不做远端消息传递用，均为项目内调用<br />
 * 
 * @author Weijie Xu
 * @dateTime 2013-9-27 下午8:34:53
 */
public abstract class BaseWebSessionBean extends BaseSessionBean {

	/**
	 * @author Xu Weijie
	 * @datetime 2017年12月6日_上午11:37:49
	 */
	private static final long serialVersionUID = -1538535017612603086L;

	/**
	 * 得到客户端Token
	 * 
	 * @author Weijie Xu
	 * @datetime 2017年8月8日_下午3:30:30
	 * @return 客户端Token
	 */
	public abstract String getToken();

	/**
	 * 得到客户端Id，比如存在
	 * 
	 * @author Weijie Xu
	 * @datetime 2017年8月8日_下午3:30:30
	 * @return 客户端Id
	 */
	public abstract String getClientId();

	/**
	 * 得到客户端所相关作用域，可null
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年12月6日_上午11:36:24
	 * @return 客户端相关域
	 */
	public abstract String getClientScope();
}
