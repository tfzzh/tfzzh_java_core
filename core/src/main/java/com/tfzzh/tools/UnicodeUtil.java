/**
 * @author Xu Weijie
 * @datetime 2018年1月26日_下午2:46:08
 */
package com.tfzzh.tools;

/**
 * Unicode转码工具，来源网络</br>
 * 
 * @author Xu Weijie
 * @datetime 2018年1月26日_下午2:46:08
 */
public class UnicodeUtil {

	/**
	 * 进行解码
	 * 
	 * @author Xu Weijie
	 * @datetime 2018年1月26日_下午2:46:14
	 * @param unicode 目标unicode
	 * @return 解出的字串
	 */
	public static String unicodeToString(final String unicode) {
		if ((unicode == null) || (unicode.length() == 0)) {
			return null;
		}
		final StringBuilder sb = new StringBuilder(unicode.length());
		int i = -1;
		int pos = 0;
		while ((i = unicode.indexOf("\\u", pos)) != -1) {
			if (i > pos) {
				sb.append(unicode.substring(pos, i));
			}
			if ((i + 5) < unicode.length()) {
				pos = i + 6;
				sb.append((char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16));
			}
		}
		return sb.toString();
	}

	/**
	 * 进行编码
	 * 
	 * @author Xu Weijie
	 * @datetime 2018年1月26日_下午2:46:15
	 * @param string 目标字串
	 * @return 编出的unicode
	 */
	public static String stringToUnicode(final String string) {
		if ((string == null) || (string.length() == 0)) {
			return null;
		}
		final StringBuilder unicode = new StringBuilder();
		char c;
		for (int i = 0; i < string.length(); i++) {
			c = string.charAt(i);
			if (c > Byte.MAX_VALUE) {
				unicode.append("\\u" + Integer.toHexString(c));
			} else {
				unicode.append(c);
			}
		}
		return unicode.toString();
	}
}
