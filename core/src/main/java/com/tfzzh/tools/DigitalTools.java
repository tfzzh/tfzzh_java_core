/**
 * @author XuWeijie
 * @datetime 2015年9月22日_下午3:16:12
 */
package com.tfzzh.tools;

/**
 * 数字工具
 * 
 * @author XuWeijie
 * @datetime 2015年9月22日_下午3:16:12
 */
public class DigitalTools {

	/**
	 * 将long转为int，主要是为了保证正负的属性
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月22日_下午3:16:37
	 * @param l long值
	 * @return 被转换为的int值
	 */
	public static int longToint(final long l) {
		final boolean isPositive = l >= 0;
		final int i = (int) l;
		if (i >= 0) {
			if (isPositive) {
				return i;
			} else {
				return -i;
			}
		} else {
			if (isPositive) {
				return -i;
			} else {
				return i;
			}
		}
	}
}
