/**
 * @author Weijie Xu
 * @dateTime 2015年2月4日 上午10:26:35
 */
package com.tfzzh.tools;

/**
 * 系统语言工具
 * 
 * @author Weijie Xu
 * @dateTime 2015年2月4日 上午10:26:35
 */
public class SystemCode {

	/**
	 * 得到win系统的编码<br />
	 * 该方法还需要进一步处理验证有效性<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月4日 上午10:31:33
	 * @return 编码
	 */
	@Deprecated
	public static String getWinCode() {
		final String wl = System.getProperty("user.country");
		if (wl.indexOf("CN") >= 0) {
			return "GBK";
		} else if (wl.indexOf("TW") >= 0) {
			return "BIG5";
		} else if (wl.indexOf("US") >= 0) {
			return "US-ASCII";
		} else {
			// 实际win似乎并不支持直接的系统级默认UTF-8
			return "UTF-8";
		}
	}
}
