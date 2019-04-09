/**
 * @author 许纬杰
 * @datetime 2016年5月17日_下午9:36:43
 */
package com.tfzzh.tools;

/**
 * 根据时间，以及固定字母排列，生成所谓的随机码
 * 
 * @author 许纬杰
 * @datetime 2016年5月17日_下午9:36:43
 */
public class TimeRandomCode {

	/**
	 * 定长的消息
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月17日_下午9:38:19
	 */
	private final char[] cs;

	/**
	 * 辅助参数
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月17日_下午9:38:49
	 */
	private int index;

	/**
	 * @author 许纬杰
	 * @datetime 2016年5月17日_下午9:38:21
	 * @param cs 随机字符串
	 */
	public TimeRandomCode(final char[] cs) {
		this.cs = cs;
	}

	/**
	 * 得到随机值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月18日_上午10:00:38
	 * @param length 目标字串长度
	 * @return 得到的随机串
	 */
	public String getRandom(final int length) {
		final long l1 = System.currentTimeMillis() + (this.index += TfzzhRandom.getInstance().nextInt(10) + 1);
		final String s1 = String.valueOf(l1);
		final String s2 = new StringBuilder(s1.length() + 4).append(s1.substring(s1.length() - 4)).append(s1).toString();
		long l2 = Long.parseLong(s2);
		final StringBuilder sb = new StringBuilder();
		int y;
		do {
			y = (int) (l2 % this.cs.length);
			l2 = l2 / this.cs.length;
			sb.append(this.cs[y]);
			if (sb.length() == length) {
				return sb.toString();
			}
		} while (l2 > 0);
		while (sb.length() < length) {
			sb.insert(0, this.cs[TfzzhRandom.getInstance().nextInt(this.cs.length)]);
		}
		return sb.toString();
	}
}
