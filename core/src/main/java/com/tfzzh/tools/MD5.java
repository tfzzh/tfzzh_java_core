/**
 * @author tfzzh
 * @dateTime 2010-8-1 下午08:16:34
 */
package com.tfzzh.tools;

import java.security.MessageDigest;

/**
 * MD5编码
 * 
 * @author tfzzh
 * @dateTime 2010-8-1 下午08:16:34
 * @model
 */
public final class MD5 {

	/**
	 * 使用的固定数字编码
	 * 
	 * @author tfzzh
	 * @dateTime 2010-8-1 下午08:19:55
	 */
	private final static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 获得指定字串的MD5编码
	 * 
	 * @author tfzzh
	 * @dateTime 2010-8-1 下午08:17:09
	 * @param target 目标字串
	 * @return MD5编码
	 */
	public static String getMD5(final String target) {
		try {
			// 使用MD5创建MessageDigest对象
			final MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			final byte[] md = mdTemp.digest(target.getBytes(Constants.SYSTEM_CODE));
			final int j = md.length;
			final char str[] = new char[j * 2];
			byte b;
			for (int i = 0, k = 0; i < j; i++) {
				b = md[i];
				str[k++] = MD5.hexDigits[(b >> 4) & 0xf];
				str[k++] = MD5.hexDigits[b & 0xf];
			}
			return new String(str);
		} catch (final Exception e) {
			return null;
		}
	}
}
