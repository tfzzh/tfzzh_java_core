/**
 * @author 许纬杰
 * @datetime 2016年7月22日_下午6:47:11
 */
package com.tfzzh.tools;

/**
 * 内存运算工具
 * 
 * @author 许纬杰
 * @datetime 2016年7月22日_下午6:47:11
 */
public class MemoryTools {

	/**
	 * @author 许纬杰
	 * @datetime 2016年7月22日_下午6:55:25
	 */
	private final static String[] names = { "", "K", "M", "G", "T", "P", "E", "Z", "Y", "B", "N", "D", "C" };

	/**
	 * 显示内存中容量
	 * 
	 * @author 许纬杰
	 * @datetime 2016年7月22日_下午6:55:24
	 * @param value 数字值
	 * @return 文字容量
	 */
	public static String getMemoryString(long value) {
		int y;
		int ind = 0;
		final StringBuilder sb = new StringBuilder();
		while (value > 0) {
			y = (int) (value % 1024l);
			value = value / 1024;
			sb.insert(0, MemoryTools.names[ind++]);
			sb.insert(0, y);
		}
		return sb.toString();
	}
}
