/**
 * @author xuweijie
 * @dateTime 2012-2-24 下午4:34:32
 */
package com.tfzzh.view.web.tools;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfzzh.exception.NestedRuntimeException;
import com.tfzzh.view.web.annotation.SingletonHideField;
import com.tfzzh.view.web.iface.RequestInfo;
import com.tfzzh.view.web.link.OperateLink.OperateLinkInfo;

/**
 * 信息控制
 * 
 * @author xuweijie
 * @dateTime 2012-2-24 下午4:34:32
 */
public class InfoControl {

	/**
	 * 请求信息
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-24 下午4:38:35
	 */
	@SingletonHideField("requestInfo")
	private final RequestInfo requestInfo = null;

	/**
	 * 该对象唯一实例
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-24 下午4:35:24
	 */
	private static final InfoControl info = new InfoControl();

	/**
	 * @author xuweijie
	 * @dateTime 2012-2-24 下午4:35:09
	 */
	private InfoControl() {
		this.init();
	}

	/**
	 * 得到该对象实例
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-24 下午4:35:48
	 * @return 对象实例
	 */
	public static InfoControl getInstantce() {
		return InfoControl.info;
	}

	/**
	 * 初始化信息
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-24 下午4:35:21
	 */
	private void init() {
	}

	/**
	 * 放入错误的请求信息<br />
	 * 一般为请求地址错误，或分流参数不正确<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-24 下午4:47:29
	 * @param request 请求消息
	 * @param response 返回数据 2017-08-24
	 * @param link 连接信息 2017-08-24
	 * @param startTime 消息逻辑起始时间
	 */
	public void putErrorRequest(final HttpServletRequest request, final HttpServletResponse response, final OperateLinkInfo link, final long startTime) {
		if (null != this.requestInfo) {
			this.requestInfo.errorRequest(request, response, link, startTime);
		}
	}

	/**
	 * 帐户引起的访问权限问题<br />
	 * 一般为请求了本不可访问的权限地址<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-15 下午7:52:17
	 * @param request 请求消息
	 * @param response 返回数据 2017-08-24
	 * @param link 连接信息 2017-08-24
	 * @param startTime 消息逻辑起始时间
	 * @throws IOException 抛
	 */
	public void putErrorAccessRequest(final HttpServletRequest request, final HttpServletResponse response, final OperateLinkInfo link, final long startTime) throws IOException {
		if (null != this.requestInfo) {
			this.requestInfo.errorAccessPermissions(request, response, link, startTime);
		} else {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}

	/**
	 * 被Ip限制的访问权限问题
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月24日_下午3:12:47
	 * @param request 请求消息
	 * @param response 返回数据
	 * @param link 连接信息
	 * @param startTime 消息逻辑起始时间
	 * @throws IOException 抛
	 */
	public void putIpRestrictionRequest(final HttpServletRequest request, final HttpServletResponse response, final OperateLinkInfo link, final long startTime) throws IOException {
		if (null != this.requestInfo) {
			this.requestInfo.errorIpRestriction(request, response, link, startTime);
		} else {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}

	/**
	 * 运行时异常问题处理
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月25日_下午2:20:55
	 * @param request 请求消息
	 * @param response 返回数据
	 * @param link 连接信息
	 * @param params 参数集合
	 * @param rex 运行时异常信息
	 * @param startTime 消息逻辑起始时间
	 */
	public void putRuntimeExceptionRequest(final HttpServletRequest request, final HttpServletResponse response, final OperateLinkInfo link, final Map<String, Object> params, final NestedRuntimeException rex, final long startTime) {
		if (null != this.requestInfo) {
			this.requestInfo.exceptionRuntime(request, response, link, params, rex, startTime);
		} else {
			rex.printStackTrace();
		}
	}

	/**
	 * 其他异常问题处理
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月25日_下午2:21:03
	 * @param request 请求消息
	 * @param response 返回数据
	 * @param link 连接信息
	 * @param params 参数集合
	 * @param ex 异常信息
	 * @param startTime 消息逻辑起始时间
	 */
	public void putExceptionRequest(final HttpServletRequest request, final HttpServletResponse response, final OperateLinkInfo link, final Map<String, Object> params, final Throwable ex, final long startTime) {
		if (null != this.requestInfo) {
			this.requestInfo.exception(request, response, link, params, ex, startTime);
		} else {
			ex.printStackTrace();
		}
	}

	/**
	 * 放入有问题的请求信息<br />
	 * 一般为参数问题，或者服务运行问题<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-24 下午4:47:30
	 * @param request 请求消息
	 * @param response 返回数据 2017-08-24
	 * @param link 连接信息 2017-08-24
	 * @param params 参数集合
	 * @param startTime 消息逻辑起始时间
	 */
	public void putProblemRequest(final HttpServletRequest request, final HttpServletResponse response, final OperateLinkInfo link, final Map<String, Object> params, final long startTime) {
		if (null != this.requestInfo) {
			this.requestInfo.problemRequest(request, response, link, params, startTime);
		}
	}

	/**
	 * 放入请求成功的信息
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-24 下午4:47:32
	 * @param request 请求消息
	 * @param response 返回数据 2017-08-24
	 * @param link 连接信息 2017-08-24
	 * @param params 参数集合
	 * @param context 整理好的参数内容
	 * @param startTime 消息逻辑起始时间
	 */
	public void putOkRequest(final HttpServletRequest request, final HttpServletResponse response, final OperateLinkInfo link, final Map<String, Object> params, final String context, final long startTime) {
		if (null != this.requestInfo) {
			this.requestInfo.okRequest(request, response, link, params, context, startTime);
		}
	}
}
