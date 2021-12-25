/**
 * @author Xu Weijie
 * @dateTime 2012-10-10 下午3:12:03
 */
package com.tfzzh.view.web.tools;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.tfzzh.tools.Constants;
import com.tfzzh.tools.StringTools;

/**
 * web服务工具
 * 
 * @author Xu Weijie
 * @dateTime 2012-10-10 下午3:12:03
 */
public class WebTools {

	/**
	 * @author tfzzh
	 * @dateTime 2021年12月25日 下午12:15:03
	 */
	private final static String HEADER_FORWARDED_FOR = "x-forwarded-for".intern();

	/**
	 * @author tfzzh
	 * @dateTime 2021年12月25日 下午12:15:04
	 */
	private final static String HEADER_PROXY_IP = "Proxy-Client-IP".intern();

	/**
	 * @author tfzzh
	 * @dateTime 2021年12月25日 下午12:15:05
	 */
	private final static String HEADER_WL_PROXY_IP = "WL-Proxy-Client-IP".intern();

	/**
	 * @author tfzzh
	 * @dateTime 2021年12月25日 下午12:15:56
	 */
	private final static String UNKNOWN = "unknown".intern();

	/**
	 * @author tfzzh
	 * @dateTime 2021年12月25日 下午1:14:16
	 */
	private final static String CLIENT_WINDOWSWECHAT = "WindowsWechat".intern();

	/**
	 * @author tfzzh
	 * @dateTime 2021年12月25日 下午1:14:17
	 */
	private final static String CLIENT_MICROMESSENGER = "MicroMessenger/".intern();

	/**
	 * @author tfzzh
	 * @dateTime 2021年12月25日 下午1:14:18
	 */
	private final static String CLIENT_IPHONE = "iPhone".intern();

	/**
	 * @author tfzzh
	 * @dateTime 2021年12月25日 下午1:14:18
	 */
	private final static String CLIENT_ANDROID = "Android".intern();

	/**
	 * @author tfzzh
	 * @dateTime 2021年12月25日 下午1:14:19
	 */
	private final static String CLIENT_WINDOWS = "Windows".intern();

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
		if (ip.indexOf(Constants.COMMA) == -1) {
			return ip;
		}
		final List<String> ips = StringTools.splitToArray(ip, Constants.COMMA);
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
		// String ip = request.getHeader("x-forwarded-for");
		String ip = request.getHeader(WebTools.HEADER_FORWARDED_FOR);
		if ((null == ip) || (ip.length() == 0) || WebTools.UNKNOWN.equalsIgnoreCase(ip)) {
			// ip = request.getHeader("Proxy-Client-IP");
			ip = request.getHeader(WebTools.HEADER_PROXY_IP);
		} else {
			return ip;
		}
		if ((null == ip) || (ip.length() == 0) || WebTools.UNKNOWN.equalsIgnoreCase(ip)) {
			// ip = request.getHeader("WL-Proxy-Client-IP");
			ip = request.getHeader(WebTools.HEADER_WL_PROXY_IP);
		} else {
			return ip;
		}
		if ((null == ip) || (ip.length() == 0) || WebTools.UNKNOWN.equalsIgnoreCase(ip)) {
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
		final String txt = request.getHeader(WebConstants.PRIVATE_LINK_NAME_USER_AGENT);
		if (null == txt) {
			return ClientTypeEnum.PcWeb;
		}
		// if (txt.indexOf("WindowsWechat") != -1) {
		if (txt.indexOf(WebTools.CLIENT_WINDOWSWECHAT) != -1) {
			return ClientTypeEnum.Weixin;
		}
		// if (txt.indexOf("MicroMessenger/") != -1) {
		if (txt.indexOf(WebTools.CLIENT_MICROMESSENGER) != -1) {
			return ClientTypeEnum.Weixin;
		}
		// if (txt.indexOf("iPhone") != -1) {
		if (txt.indexOf(WebTools.CLIENT_IPHONE) != -1) {
			return ClientTypeEnum.Ios;
		}
		// if (txt.indexOf("Android") != -1) {
		if (txt.indexOf(WebTools.CLIENT_ANDROID) != -1) {
			return ClientTypeEnum.Android;
		}
		// if (txt.indexOf("Windows") != -1) {
		if (txt.indexOf(WebTools.CLIENT_WINDOWS) != -1) {
			return ClientTypeEnum.PcWeb;
		}
		return ClientTypeEnum.PcWeb;
	}
}
