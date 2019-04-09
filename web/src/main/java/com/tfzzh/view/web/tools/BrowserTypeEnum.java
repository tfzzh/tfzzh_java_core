/**
 * @author XuWeijie
 * @datetime 2015年11月19日_下午8:37:49
 */
package com.tfzzh.view.web.tools;

/**
 * 浏览器类型
 * 
 * @author XuWeijie
 * @datetime 2015年11月19日_下午8:37:49
 */
public enum BrowserTypeEnum {
	/**
	 * IE系列浏览器
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:50:12
	 */
	InternetExplorer(1),
	/**
	 * 遨游浏览器
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:50:13
	 */
	Maxthon(2),
	/**
	 * 火狐浏览器
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:50:14
	 */
	Firefox(3),
	/**
	 * 谷歌浏览器
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:50:15
	 */
	Chrome(4),
	/**
	 * 欧鹏浏览器
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:50:15
	 */
	Opera(5),
	/**
	 * 苹果浏览器
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午9:22:24
	 */
	Safari(7),
	/**
	 * qq浏览器
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:50:16
	 */
	QQBrowser(21),
	/**
	 * 手机qq浏览器
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月12日_下午3:23:36
	 */
	MQQBrowser(22),
	/**
	 * 微信浏览器
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:50:16
	 */
	Weixin(61),
	/**
	 * qq移动版，附带是qq移动浏览器
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月14日_上午10:53:20
	 */
	QQMobile(62),
	/**
	 * 自定义
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月22日_下午9:14:19
	 */
	Custom(98),
	/**
	 * 其他浏览器
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:50:17
	 */
	Other(99);

	/**
	 * 类型值
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:53:33
	 */
	private final int val;

	/**
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:53:32
	 * @param val 类型值
	 */
	BrowserTypeEnum(final int val) {
		this.val = val;
	}

	/**
	 * 得到类型值
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:53:31
	 * @return the val
	 */
	public int getTypeValue() {
		return this.val;
	}

	/**
	 * 得到默认的浏览器类型
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午9:17:37
	 * @return 其他浏览器
	 */
	public static BrowserTypeEnum getDefaultType() {
		return Other;
	}

	/**
	 * 得到类型
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:53:30
	 * @param type 类型值
	 * @return 对应的浏览器类型
	 */
	public static BrowserTypeEnum getType(final int type) {
		for (final BrowserTypeEnum e : BrowserTypeEnum.values()) {
			if (e.val == type) {
				return e;
			}
		}
		return Other;
	}
}
