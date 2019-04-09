/**
 * @author XuWeijie
 * @datetime 2015年11月19日_下午8:59:03
 */
package com.tfzzh.view.web.tools;

/**
 * 环境信息
 * 
 * @author XuWeijie
 * @datetime 2015年11月19日_下午8:59:03
 */
public class EnvironmentInfo {

	/**
	 * 系统类型
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:59:53
	 */
	private final SystemTypeEnum system;

	/**
	 * 浏览器类型
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:59:54
	 */
	private final BrowserTypeEnum browser;

	/**
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午9:00:24
	 * @param system 系统类型
	 * @param browser 浏览器类型
	 */
	public EnvironmentInfo(final SystemTypeEnum system, final BrowserTypeEnum browser) {
		this.system = system;
		this.browser = browser;
	}

	/**
	 * 得到系统类型
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午9:00:43
	 * @return the system
	 */
	public SystemTypeEnum getSystem() {
		return this.system;
	}

	/**
	 * 得到浏览器类型
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午9:00:43
	 * @return the browser
	 */
	public BrowserTypeEnum getBrowser() {
		return this.browser;
	}
}
