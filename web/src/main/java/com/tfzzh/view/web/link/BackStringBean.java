/**
 * @author Xu Weijie
 * @dateTime 2012-7-6 下午12:02:26
 */
package com.tfzzh.view.web.link;

import java.io.IOException;

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
	 * 数据对应的内容
	 * 
	 * @author tfzzh
	 * @dateTime 2020年8月18日 下午1:29:18
	 */
	private final String value;

	/**
	 * 默认数据所在键名为“data”<br />
	 * 默认数据类型为“json”<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2020年8月18日 下午1:32:37
	 * @param cont 目标内容
	 */
	public BackStringBean(String cont) {
		// 确实的不需要其他参数
		this("json", cont);
	}

	/**
	 * 默认数据类型为“json”
	 * 
	 * @author Xu Weijie
	 * @dateTime 2020年8月18日 下午1:32:37
	 * @param type 目标内容类型
	 * @param cont 目标内容
	 */
	public BackStringBean(final String type, String cont) {
		// 确实的不需要其他参数
		super(null, null);
		this.type = type;
		this.value = cont;
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
		return this.value;
	}
}
