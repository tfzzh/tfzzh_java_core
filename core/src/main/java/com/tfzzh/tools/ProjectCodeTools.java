/**
 * @author Weijie Xu
 * @dateTime 2017年3月21日 下午2:04:18
 */
package com.tfzzh.tools;

/**
 * 项目用号码处理
 * 
 * @author Weijie Xu
 * @dateTime 2017年3月21日 下午2:04:18
 */
public class ProjectCodeTools {

	/**
	 * 得到指定数位code，长码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月21日 下午2:06:05
	 * @param len 编码长度位数
	 * @param timeInd 时间串所在开始位数
	 * @return 指定数位code
	 */
	public static String getLongCode(int len, int timeInd) {
		if (len < BaseToolsConstants.LONG_CODE_LIMIT_MIN) {
			len = BaseToolsConstants.LONG_CODE_LIMIT_MIN;
		} else if (len > BaseToolsConstants.LONG_CODE_LIMIT_MAX) {
			len = BaseToolsConstants.LONG_CODE_LIMIT_MAX;
		}
		if (timeInd > len) {
			timeInd = len - 1;
		} else if (timeInd < 18) {
			timeInd = 18;
		}
		long time = System.currentTimeMillis();
		final RandomCode rc = RandomCode.getInstance(RandomCode.CODE_TYPE_LOWERCASE_UPPERCASE);
		final StringBuilder sb = new StringBuilder(len);
		sb.append(rc.getRandomCode(len - timeInd));
		final int tl = BaseToolsConstants.LONG_CODE_TIME_RANDOM_CODE.length;
		do {
			sb.append(BaseToolsConstants.LONG_CODE_TIME_RANDOM_CODE[(int) (time % tl)]);
		} while ((time /= tl) > 0);
		sb.append(rc.getRandomCode(len - sb.length()));
		return sb.toString();
	}

	/**
	 * 得到指定数位的Code，短码
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月27日_下午4:13:02
	 * @param len 编码长度位数
	 * @param timeInd 时间串所在开始位数
	 * @return 指定数位code
	 */
	public static String getShortCode(int len, int timeInd) {
		if (len > BaseToolsConstants.LONG_CODE_LIMIT_MIN) {
			len = BaseToolsConstants.LONG_CODE_LIMIT_MIN;
		} else if (len < 3) {
			len = 3;
		}
		if (timeInd >= len) {
			timeInd = len / 2;
		} else if (timeInd < 0) {
			timeInd = 0;
		}
		final RandomCode rc = RandomCode.getInstance(RandomCode.CODE_TYPE_LOWERCASE_UPPERCASE);
		final StringBuilder sb = new StringBuilder(len);
		if (timeInd != 0) {
			sb.append(rc.getRandomCode(len - timeInd));
		}
		long time = System.currentTimeMillis();
		time += TfzzhRandom.getInstance().nextInt(1000000);
		final int tl = BaseToolsConstants.LONG_CODE_TIME_RANDOM_CODE.length;
		do {
			sb.append(BaseToolsConstants.LONG_CODE_TIME_RANDOM_CODE[(int) (time % tl)]);
		} while (((time /= tl) > 0) && (sb.length() < len));
		final int s = len - sb.length();
		if (s > 0) {
			sb.append(rc.getRandomCode(s));
		}
		return sb.toString();
	}
}
