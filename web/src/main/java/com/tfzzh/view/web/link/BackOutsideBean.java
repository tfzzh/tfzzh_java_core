/**
 * @author Xu Weijie
 * @dateTime 2012-7-6 上午11:47:22
 */
package com.tfzzh.view.web.link;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfzzh.view.web.link.OperateLink.OperateLinkInfo;

/**
 * 去到对外链的跳转<br />
 * 一般为代理外链<br />
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-6 上午11:47:22
 */
public class BackOutsideBean extends BaseBackOperationBean {

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-7-6 下午12:35:47
	 * @param target 目标
	 */
	public BackOutsideBean(final String target) {
		super(target, null);
	}

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-7-6 下午12:35:47
	 * @param target 目标
	 * @param attributes 可用于操作的数据集合
	 */
	public BackOutsideBean(final String target, final Map<String, ? extends Object> attributes) {
		super(target, attributes);
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
}
