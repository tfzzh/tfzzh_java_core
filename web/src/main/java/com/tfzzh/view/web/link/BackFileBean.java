/**
 * @author Xu Weijie
 * @dateTime 2012-9-5 下午9:33:50
 */
package com.tfzzh.view.web.link;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfzzh.view.web.link.OperateLink.OperateLinkInfo;

/**
 * 进行对指定文件内容的读取
 * 
 * @author Xu Weijie
 * @dateTime 2012-9-5 下午9:33:50
 */
public class BackFileBean extends BaseBackOperationBean {

	/**
	 * 针对需要进行url替换的操作，替换列表<br />
	 * 针对极特殊操作<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-5 下午9:06:24
	 */
	private final Object[] formats;

	/**
	 * 进入到默认的目标<br />
	 * 默认目标值：“t”<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-5 下午9:34:37
	 */
	public BackFileBean() {
		super("t", null);
		this.formats = null;
	}

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-9-5 下午9:34:37
	 * @param target 目标
	 */
	public BackFileBean(final String target) {
		super(target, null);
		this.formats = null;
	}

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-9-5 下午9:34:37
	 * @param formats 针对需要进行url替换的操作，替换列表
	 */
	public BackFileBean(final Object[] formats) {
		super("t", null);
		this.formats = formats;
	}

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-9-5 下午9:34:37
	 * @param target 目标
	 * @param formats 针对需要进行url替换的操作，替换列表
	 */
	protected BackFileBean(final String target, final Object[] formats) {
		super(target, null);
		this.formats = formats;
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
	 * 得到针对需要进行url替换的操作，替换列表<br />
	 * 针对极特殊操作<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-5 下午9:08:48
	 * @return the formats
	 */
	public Object[] getFormats() {
		return this.formats;
	}
}
