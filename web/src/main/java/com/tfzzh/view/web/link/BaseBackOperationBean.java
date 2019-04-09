/**
 * @author Xu Weijie
 * @dateTime 2012-7-6 上午11:57:25
 */
package com.tfzzh.view.web.link;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfzzh.view.web.link.OperateLink.OperateLinkInfo;

/**
 * 数据返回操作基础控制
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-6 上午11:57:25
 */
public abstract class BaseBackOperationBean {

	/**
	 * 目标
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-5 上午11:42:29
	 */
	private final String target;

	/**
	 * 前缀信息，针对服务器转向
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-5 上午11:42:30
	 */
	private String prefix;

	/**
	 * 回传的属性消息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-5 上午11:42:32
	 */
	private final Map<String, ? extends Object> attributes;

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-7-5 上午11:45:14
	 * @param target 目标
	 * @param attributes 可用于操作的数据集合
	 */
	protected BaseBackOperationBean(final String target, final Map<String, ? extends Object> attributes) {
		this.target = target;
		this.attributes = attributes;
	}

	/**
	 * 得到目标
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-5 上午11:46:06
	 * @return the target
	 */
	public String getTarget() {
		return this.target;
	}

	/**
	 * 设置前缀信息，针对服务器转向
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-5 上午11:43:21
	 * @param prefix the prefix to set
	 */
	public void setPrefix(final String prefix) {
		this.prefix = prefix;
	}

	/**
	 * 得到前缀信息，针对服务器转向
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-5 上午11:46:06
	 * @return the prefix
	 */
	public String getPrefix() {
		return this.prefix;
	}

	/**
	 * 得到回传的属性消息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-5 上午11:46:06
	 * @return the attributes
	 */
	public Map<String, ? extends Object> getAttributes() {
		return this.attributes;
	}

	/**
	 * 将属性信息放入到request中
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-5 上午11:43:35
	 * @param request 请求信息
	 */
	public void putAttributeToRequest(final HttpServletRequest request) {
		if (null != this.attributes) {
			for (final Entry<String, ? extends Object> ent : this.attributes.entrySet()) {
				request.setAttribute(ent.getKey(), ent.getValue());
			}
		}
		// 加入前缀
		request.setAttribute("prefix", this.prefix);
	}

	/**
	 * 连接到
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-6 下午12:53:36
	 * @param link 控制连接
	 * @param request 请求数据
	 * @param response 返回数据
	 * @throws IOException 抛
	 * @throws ServletException 抛
	 */
	public abstract void linkTo(OperateLinkInfo link, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}
