/**
 * @author Xu Weijie
 * @dateTime 2012-10-10 下午3:12:03
 */
package com.tfzzh.view.web.tools;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.tfzzh.tools.StringTools;

/**
 * web服务工具
 * 
 * @author Xu Weijie
 * @dateTime 2012-10-10 下午3:12:03
 */
public class WebTools {

	/**
	 * 得到第一客户端IP
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月17日_下午3:21:35
	 * @param request 请求信息
	 * @return 目标客户端第一位置Ip
	 */
	public static String getFirstClientIp(final HttpServletRequest request) {
		final String ip = WebTools.getClientIp(request);
		if (ip.indexOf(",") == -1) {
			return ip;
		}
		final List<String> ips = StringTools.splitToArray(ip, ",");
		return ips.get(0);
	}

	/**
	 * 得到客户端IP
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-10-10 下午3:12:52
	 * @param request 请求信息
	 * @return 目标客户端Ip
	 */
	public static String getClientIp(final HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if ((null == ip) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		} else {
			return ip;
		}
		if ((null == ip) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		} else {
			return ip;
		}
		if ((null == ip) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 得到客户端Web类型
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月23日_下午5:58:58
	 * @param request 请求的消息
	 * @return 客户端类型
	 */
	public static ClientTypeEnum getClientWebType(final HttpServletRequest request) {
		final String txt = request.getHeader("user-agent");
		if (null == txt) {
			return ClientTypeEnum.PcWeb;
		}
		if (txt.indexOf("WindowsWechat") != -1) {
			return ClientTypeEnum.Weixin;
		}
		if (txt.indexOf("MicroMessenger/") != -1) {
			return ClientTypeEnum.Weixin;
		}
		if (txt.indexOf("iPhone") != -1) {
			return ClientTypeEnum.Ios;
		}
		if (txt.indexOf("Android") != -1) {
			return ClientTypeEnum.Android;
		}
		if (txt.indexOf("Windows") != -1) {
			return ClientTypeEnum.PcWeb;
		}
		return ClientTypeEnum.PcWeb;
	}
}
