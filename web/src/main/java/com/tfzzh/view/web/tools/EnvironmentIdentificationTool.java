/**
 * @author XuWeijie
 * @datetime 2015年11月19日_下午8:37:25
 */
package com.tfzzh.view.web.tools;

import javax.servlet.http.HttpServletRequest;

import com.tfzzh.log.CoreLog;

/**
 * 环境识别工具<br />
 * 用来识别客户端系统及浏览器<br />
 * 
 * @author XuWeijie
 * @datetime 2015年11月19日_下午8:37:25
 */
public class EnvironmentIdentificationTool {

	/**
	 * 识别用请求头信息
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午9:03:48
	 */
	private static final String HEAD_NAME_EI = "user-agent";

	/**
	 * 识别手机环境
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:58:27
	 * @param request 被请求来的客户端信息
	 * @return 环境信息
	 */
	public static EnvironmentInfo identificationMobileEnvironment(final HttpServletRequest request) {
		final String txt = request.getHeader(EnvironmentIdentificationTool.HEAD_NAME_EI);
		// if (EnvironmentIdentificationTool.log.isInfoEnabled()) {
		// final StringBuilder sb = new StringBuilder();
		// sb.append(request.getRemoteAddr()).append(':').append(request.getRemotePort()).append("--").append(txt);
		// EnvironmentIdentificationTool.log.info(sb.toString());
		// }
		if (CoreLog.getInstance().debugEnabled()) {
			CoreLog.getInstance().debug(EnvironmentIdentificationTool.class, request.getRemoteAddr(), ":", Integer.toString(request.getRemotePort()), "--", txt);
		}
		if (null == txt) {
			// 假请求
			return new EnvironmentInfo(SystemTypeEnum.Custom, BrowserTypeEnum.Custom);
		}
		// 桌面微信判定
		final int wwi = txt.indexOf("WindowsWechat");
		final int mmi = txt.indexOf("MicroMessenger/");
		BrowserTypeEnum bt;
		if ((wwi != -1) || (mmi != -1)) {
			// 是微信电脑客户端
			// 去到微信用地址
			// 去到微信地址
			bt = BrowserTypeEnum.Weixin;
		} else if (txt.indexOf(" MQQBrowser/") != -1) {
			if (txt.indexOf(" QQ/") != -1) {
				bt = BrowserTypeEnum.QQMobile;
			} else {
				bt = BrowserTypeEnum.MQQBrowser;
			}
		} else if (txt.indexOf(" QQBrowser/") != -1) {
			bt = BrowserTypeEnum.QQBrowser;
		} else if (txt.indexOf("MSIE") != -1) {
			bt = BrowserTypeEnum.InternetExplorer;
		} else if (txt.indexOf("Maxthon") != -1) {
			bt = BrowserTypeEnum.Maxthon;
		} else if (txt.indexOf("Chrome") != -1) {
			bt = BrowserTypeEnum.Chrome;
		} else if (txt.indexOf("Opera") != -1) {
			bt = BrowserTypeEnum.Opera;
		} else if (txt.indexOf("Firefox") != -1) {
			bt = BrowserTypeEnum.Firefox;
		} else if (txt.indexOf("Safari") != -1) {
			bt = BrowserTypeEnum.Safari;
		} else {
			bt = BrowserTypeEnum.Other;
		}
		SystemTypeEnum st;
		if (txt.indexOf("Windows Phone") != -1) {
			st = SystemTypeEnum.WindowsPhone;
		} else if (txt.indexOf("iPhone") != -1) {
			st = SystemTypeEnum.Ios;
		} else if (txt.indexOf("Android") != -1) {
			st = SystemTypeEnum.Android;
		} else if (txt.indexOf("Windows") != -1) {
			st = SystemTypeEnum.Windows;
		} else {
			st = SystemTypeEnum.Unknow;
		}
		return new EnvironmentInfo(st, bt);
	}
}
