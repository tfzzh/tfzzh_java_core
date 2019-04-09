/**
 * @author tfzzh
 * @dateTime 2016年8月29日 下午6:30:32
 */
package com.tfzzh.view.web.link;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfzzh.view.web.link.OperateLink.OperateLinkInfo;

/**
 * 进行JSP页面合成
 * 
 * @author tfzzh
 * @dateTime 2016年8月29日 下午6:30:32
 */
public class BackToJspBean extends BaseBackOperationBean {

	/**
	 * 进入到默认的目标<br />
	 * 默认目标值：“t”<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2016年8月29日 下午6:53:10
	 */
	public BackToJspBean() {
		super("t", null);
	}

	/**
	 * @author Xu Weijie
	 * @dateTime 2016年8月29日 下午6:53:10
	 * @param target 目标
	 */
	public BackToJspBean(final String target) {
		super(target, null);
	}

	/**
	 * 进入到默认的目标<br />
	 * 默认目标值：“t”<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2016年8月29日 下午6:53:10
	 * @param attributes 可用于操作的数据集合
	 */
	public BackToJspBean(final Map<String, ? extends Object> attributes) {
		super("t", attributes);
	}

	/**
	 * @author Xu Weijie
	 * @dateTime 2016年8月29日 下午6:53:10
	 * @param formats 针对需要进行url替换的操作，替换列表
	 */
	public BackToJspBean(final Object[] formats) {
		super("t", null);
	}

	/**
	 * @author Xu Weijie
	 * @dateTime 2016年8月29日 下午6:53:10
	 * @param target 目标
	 * @param attributes 可用于操作的数据集合
	 */
	public BackToJspBean(final String target, final Map<String, ? extends Object> attributes) {
		super(target, attributes);
	}

	/**
	 * @author Xu Weijie
	 * @dateTime 2016年8月29日 下午6:53:10
	 * @param target 目标
	 * @param formats 针对需要进行url替换的操作，替换列表
	 */
	public BackToJspBean(final String target, final Object[] formats) {
		super(target, null);
	}

	/**
	 * @author Xu Weijie
	 * @dateTime 2016年8月29日 下午6:53:10
	 * @param target 目标
	 * @param attributes 可用于操作的数据集合
	 * @param formats 针对需要进行url替换的操作，替换列表
	 */
	public BackToJspBean(final String target, final Map<String, ? extends Object> attributes, final Object[] formats) {
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
