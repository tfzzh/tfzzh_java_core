/**
 * @author xuweijie
 * @dateTime 2012-2-24 下午3:19:20
 */
package com.tfzzh.view.web.iface;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfzzh.exception.NestedRuntimeException;
import com.tfzzh.view.web.link.OperateLink.OperateLinkInfo;

/**
 * 请求信息<br />
 * 主要用做请求的各种状态时的另外操作<br />
 * 
 * @author xuweijie
 * @dateTime 2012-2-24 下午3:19:20
 */
public interface RequestInfo {

	/**
	 * 错误的请求信息<br />
	 * 一般为请求地址错误，或分流参数不正确<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-24 下午3:27:01
	 * @param request 请求消息
	 * @param response 返回数据 2017-08-24
	 * @param link 连接信息 2017-08-24
	 * @param startTime 消息逻辑起始时间
	 */
	void errorRequest(HttpServletRequest request, HttpServletResponse response, OperateLinkInfo link, long startTime);

	/**
	 * 访问权限问题<br />
	 * 一般为请求了本不可访问的权限地址<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-15 下午7:50:58
	 * @param request 请求消息
	 * @param response 返回数据 2017-08-24
	 * @param link 连接信息 2017-08-24
	 * @param startTime 消息逻辑起始时间
	 */
	void errorAccessPermissions(HttpServletRequest request, HttpServletResponse response, OperateLinkInfo link, long startTime);

	/**
	 * 被限制的IP
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月24日_下午3:07:09
	 * @param request 请求消息
	 * @param response 返回数据
	 * @param link 连接信息
	 * @param startTime 消息逻辑起始时间
	 */
	void errorIpRestriction(HttpServletRequest request, HttpServletResponse response, OperateLinkInfo link, long startTime);

	/**
	 * 运行时异常捕获处理
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月25日_下午2:00:22
	 * @param request 请求消息
	 * @param response 返回数据
	 * @param link 连接信息
	 * @param params 参数集合
	 * @param rex 运行时异常信息
	 * @param startTime 消息逻辑起始时间
	 */
	void exceptionRuntime(HttpServletRequest request, HttpServletResponse response, OperateLinkInfo link, Map<String, Object> params, NestedRuntimeException rex, long startTime);

	/**
	 * 其他异常捕获处理
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月25日_下午2:02:28
	 * @param request 请求消息
	 * @param response 返回数据
	 * @param link 连接信息
	 * @param params 参数集合
	 * @param ex 异常信息
	 * @param startTime 消息逻辑起始时间
	 */
	void exception(HttpServletRequest request, HttpServletResponse response, OperateLinkInfo link, Map<String, Object> params, Throwable ex, long startTime);

	/**
	 * 有问题的请求信息<br />
	 * 一般为参数问题，或者服务运行问题<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-24 下午3:28:28
	 * @param request 请求消息
	 * @param response 返回数据 2017-08-24
	 * @param link 连接信息 2017-08-24
	 * @param params 参数集合
	 * @param startTime 消息逻辑起始时间
	 */
	void problemRequest(HttpServletRequest request, HttpServletResponse response, OperateLinkInfo link, Map<String, Object> params, long startTime);

	/**
	 * 请求成功的信息
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-24 下午3:27:03
	 * @param request 请求消息
	 * @param response 返回数据 2017-08-24
	 * @param link 连接信息 2017-08-24
	 * @param params 参数集合
	 * @param context 整理好的参数内容
	 * @param startTime 消息逻辑起始时间
	 */
	void okRequest(HttpServletRequest request, HttpServletResponse response, OperateLinkInfo link, Map<String, Object> params, String context, long startTime);
}
