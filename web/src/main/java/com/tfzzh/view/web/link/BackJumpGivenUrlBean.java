/**
 * @author tfzzh
 * @dateTime 2016年9月4日 下午5:29:30
 */
package com.tfzzh.view.web.link;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfzzh.view.web.link.OperateLink.OperateLinkInfo;

/**
 * 进行服务器端连接跳转
 * 
 * @author tfzzh
 * @dateTime 2016年9月4日 下午5:29:30
 */
public class BackJumpGivenUrlBean extends BaseBackOperationBean {

	/**
	 * 是否附带自身的token参数
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月28日_下午2:27:29
	 */
	private boolean takeToken;

	/**
	 * 无拼接参数，会附带系统默认token
	 * 
	 * @author Xu Weijie
	 * @dateTime 2016年9月4日 下午5:29:30
	 * @param target 目标
	 */
	public BackJumpGivenUrlBean(final String target) {
		super(target, null);
	}

	/**
	 * @author Xu Weijie
	 * @datetime 2017年8月28日_下午2:29:03
	 * @param target 目标
	 * @param takeToken 是否附带token
	 */
	public BackJumpGivenUrlBean(final String target, final boolean takeToken) {
		this(target, null, takeToken);
	}

	/**
	 * 会附带系统默认token
	 * 
	 * @author Xu Weijie
	 * @dateTime 2016年9月4日 下午5:29:30
	 * @param target 目标
	 * @param attributes 可用于操作的数据集合
	 */
	public BackJumpGivenUrlBean(final String target, final Map<String, ? extends Object> attributes) {
		this(target, attributes, true);
	}

	/**
	 * @author Xu Weijie
	 * @datetime 2017年8月28日_下午2:28:30
	 * @param target 目标
	 * @param attributes 可用于操作的数据集合
	 * @param takeToken 是否附带token
	 */
	public BackJumpGivenUrlBean(final String target, final Map<String, ? extends Object> attributes, final boolean takeToken) {
		super(target, attributes);
		this.takeToken = takeToken;
	}

	/**
	 * 是否附带自身的token参数
	 *
	 * @author Xu Weijie
	 * @datetime 2017年8月28日_下午2:30:04
	 * @return the takeToken
	 */
	public boolean isTakeToken() {
		return this.takeToken;
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
