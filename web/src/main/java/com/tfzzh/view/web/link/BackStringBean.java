/**
 * @author Xu Weijie
 * @dateTime 2012-7-6 下午12:02:26
 */
package com.tfzzh.view.web.link;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfzzh.view.web.link.OperateLink.OperateLinkInfo;

/**
 * 返回json结构的字串信息
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-6 下午12:02:26
 */
public class BackStringBean extends BaseBackOperationBean {

	/**
	 * 页面数据类型
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-12-24 下午6:35:44
	 */
	private final String type;

	/**
	 * 数据所在键名
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-12-24 下午6:35:45
	 */
	private final String key;

	/**
	 * 默认数据所在键名为“data”<br />
	 * 默认数据类型为“json”<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-6 下午12:38:46
	 * @param attributes 可用于操作的数据集合
	 */
	public BackStringBean(final Map<String, ? extends Object> attributes) {
		// 确实的不需要其他参数
		this(attributes, "data", "json");
	}

	/**
	 * 默认数据类型为“json”
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-12-24 下午6:38:01
	 * @param attributes 可用于操作的数据集合
	 * @param key 对应数据键名
	 */
	public BackStringBean(final Map<String, ? extends Object> attributes, final String key) {
		// 确实的不需要其他参数
		this(attributes, key, "json");
	}

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-12-24 下午6:38:06
	 * @param attributes 可用于操作的数据集合
	 * @param key 对应数据键名
	 * @param type 返回数据的类型
	 */
	public BackStringBean(final Map<String, ? extends Object> attributes, final String key, final String type) {
		// 确实的不需要其他参数
		super(null, attributes);
		this.key = key;
		this.type = type;
	}

	/**
	 * 连接到
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月25日_上午10:00:10
	 * @param link 控制连接
	 * @param request 请求数据
	 * @param response 返回数据
	 * @throws IOException 抛
	 * @throws ServletException 抛
	 * @see com.tfzzh.view.web.link.BaseBackOperationBean#linkTo(com.tfzzh.view.web.link.OperateLink.OperateLinkInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void linkTo(final OperateLinkInfo link, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		link.executeResult(this, request, response);
	}

	/**
	 * 得到页面数据类型
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-12-24 下午6:39:53
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * 得到数据内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-12-24 下午6:39:53
	 * @return the key
	 */
	public String getValue() {
		final Object obj = super.getAttributes().get(this.key);
		if (null == obj) {
			return null;
		} else {
			return obj.toString();
		}
	}
}
