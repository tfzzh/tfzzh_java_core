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

import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;
import com.tfzzh.view.web.link.OperateLink.OperateLinkInfo;

/**
 * 返回json结构的字串信息
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-6 下午12:02:26
 */
public class BackJsonBean extends BaseBackOperationBean {

	/**
	 * 过滤器
	 * 
	 * @author Xu Weijie
	 * @datetime 2018年2月2日_上午9:16:11
	 */
	private final SimplePropertyPreFilter pf;

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-7-6 下午12:38:46
	 * @param attributes 可用于操作的数据集合
	 */
	public BackJsonBean(final Map<String, ? extends Object> attributes) {
		// 确实的不需要其他参数
		this(attributes, null);
	}

	/**
	 * @author Xu Weijie
	 * @datetime 2018年2月2日_上午9:18:27
	 * @param attributes 可用于操作的数据集合
	 * @param pf 过滤器
	 */
	public BackJsonBean(final Map<String, ? extends Object> attributes, final SimplePropertyPreFilter pf) {
		// 确实的不需要其他参数
		super(null, attributes);
		this.pf = pf;
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
	 * 得到过滤器
	 *
	 * @author Xu Weijie
	 * @datetime 2018年2月2日_上午9:18:56
	 * @return the pf
	 */
	public SimplePropertyPreFilter getSerializeFilter() {
		return this.pf;
	}
}
