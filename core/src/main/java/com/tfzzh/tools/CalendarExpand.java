/**
 * @author xuweijie
 * @dateTime 2012-3-26 下午2:47:43
 */
package com.tfzzh.tools;

import java.util.Calendar;
import java.util.Date;

/**
 * 日历的扩展
 * 
 * @author xuweijie
 * @dateTime 2012-3-26 下午2:47:43
 */
public class CalendarExpand {

	/**
	 * 得到时区所相关毫秒数
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月16日_上午10:50:54
	 */
	public final static long TIMEZONE_CORRECTED_VALUE = Calendar.getInstance().getTimeZone().getRawOffset();

	/**
	 * 一小时所相关的毫秒数
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月16日_上午11:02:45
	 */
	public final static long ONE_HOUR_TIME = 1000l * 60l * 60l;

	/**
	 * 一天所相关的毫秒数
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月16日_上午10:54:10
	 */
	public final static long ONE_DAY_TIME = CalendarExpand.ONE_HOUR_TIME * 24l;

	/**
	 * 根据日期字串得到当时日历对象
	 * 
	 * @author xuweijie
	 * @dateTime 2012-11-19 下午3:47:12
	 * @param day 日期字串，保持：“年，月，日”的顺序
	 * @param intervalSymbols 日期字串的间隔符号
	 * @return 对应日历对象
	 */
	public static Calendar getCalendarInDay(final String day, final String intervalSymbols) {
		final String[] str = day.split(intervalSymbols);
		if (str.length == 3) {
			final Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(str[0]), Integer.parseInt(str[1]) - 1, Integer.parseInt(str[2]));
			return cal;
		} else {
			return null;
		}
	}

	/**
	 * 得到当天的开始时间
	 * 
	 * @author XuWeijie
	 * @datetime 2015年6月10日_下午8:58:58
	 * @return 日历对象
	 */
	public static Calendar timeToDayStart() {
		return CalendarExpand.timeToDayStart(Calendar.getInstance());
	}

	/**
	 * 时间到一天的开始
	 * 
	 * @author xuweijie
	 * @dateTime 2012-3-26 下午2:48:24
	 * @param cal 日历对象
	 * @return 日历对象
	 */
	public static Calendar timeToDayStart(final Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	/**
	 * 得到当天的结束时间
	 * 
	 * @author XuWeijie
	 * @datetime 2015年6月10日_下午8:58:58
	 * @return 日历对象
	 */
	public static Calendar timeToDayEnd() {
		return CalendarExpand.timeToDayEnd(Calendar.getInstance());
	}

	/**
	 * 得到当前小时的节点时间
	 * 
	 * @author XuWeijie
	 * @datetime 2015年6月27日_下午2:12:18
	 * @return 当前小时的节点时间
	 */
	public static Calendar timeToThisHourEnd() {
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal;
	}

	/**
	 * 时间到一天的结束
	 * 
	 * @author xuweijie
	 * @dateTime 2012-3-26 下午2:48:25
	 * @param cal 日历对象
	 * @return 日历对象
	 */
	public static Calendar timeToDayEnd(final Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal;
	}

	/**
	 * 目标所在时间当前天的结束时刻
	 * 
	 * @author Xu Weijie
	 * @datetime 2018年3月14日_下午6:12:31
	 * @param time 目标时间
	 * @return 日历对象
	 */
	public static Calendar timeToDayEnd(final long time) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return CalendarExpand.timeToDayEnd(cal);
	}

	/**
	 * 得到下一次运算时间
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月28日_上午11:08:28
	 * @param tarTimeStr 目标时间串，需求格式：HH:mm:ss SSS
	 * @return 得到下一次的执行时间
	 */
	public static long getNextTime(String tarTimeStr) {
		// 得到在今天的时间
		// 得到目标所需要的时间串
		tarTimeStr = tarTimeStr.trim();
		final StringBuilder sb = new StringBuilder(19).append(DateFormat.getDayShortShowStr()).append(' ');
		switch (tarTimeStr.length()) {
		case 2:
			sb.append(tarTimeStr).append(":00:00 000");
			break;
		case 5:
			sb.append(tarTimeStr).append(":00 000");
			break;
		case 8:
			sb.append(tarTimeStr).append(" 000");
			break;
		case 12:
			sb.append(tarTimeStr);
			break;
		default: // 问题情况
			return Long.MAX_VALUE;
		}
		final Date d = DateFormat.getFullDateShow(sb.toString());
		long tarTime = d.getTime();
		final long now = System.currentTimeMillis();
		while (now > tarTime) {
			tarTime += Constants.DATA_ONE_DAY_SECOND;
		}
		return tarTime;
	}

	/**
	 * 是否在同一天
	 * 
	 * @author xuweijie
	 * @dateTime 2012-3-26 下午3:21:32
	 * @param before 前一个时间
	 * @param after 后一个时间
	 * @return true，同一天；<br />
	 *         false，非同一天；<br />
	 */
	public static boolean inSameDay(final Calendar before, final Calendar after) {
		if (before.get(Calendar.DAY_OF_YEAR) == after.get(Calendar.DAY_OF_YEAR)) {
			if (before.get(Calendar.YEAR) == after.get(Calendar.YEAR)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 前一个日期与后一个日期所相差的天数<br />
	 * 前大于后为正数，前小于后为负数<br />
	 * 
	 * @author XuWeijie
	 * @datetime 2015年6月1日_下午4:45:50
	 * @param beforeDay 前一个日期
	 * @param behindDay 后一个日期
	 * @return 所相差的天数，有正负
	 */
	public static int timeDifference(final String beforeDay, final String behindDay) {
		if (beforeDay.equals(behindDay)) {
			// 因为日期相同
			return 0;
		}
		final Date bd1 = DateFormat.getShortDateShow(beforeDay);
		final Date bd2 = DateFormat.getShortDateShow(behindDay);
		return CalendarExpand.timeDifference(bd1.getTime(), bd2.getTime());
	}

	/**
	 * 前一个时间点与后一个时间点所相差的天数<br />
	 * 前大于后为正数，前小于后为负数<br />
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月16日_上午10:55:58
	 * @param beforeTime 前一个时间点
	 * @param behindTime 后一个时间点
	 * @return 所相差的天数
	 */
	public static int timeDifference(final long beforeTime, final long behindTime) {
		return (int) (((beforeTime + CalendarExpand.TIMEZONE_CORRECTED_VALUE) / CalendarExpand.ONE_DAY_TIME) - ((behindTime + CalendarExpand.TIMEZONE_CORRECTED_VALUE) / CalendarExpand.ONE_DAY_TIME));
	}

	/**
	 * 前一个日期与后一个日期所相差的小时数<br />
	 * 前大于后为正数，前小于后为负数<br />
	 * 
	 * @author XuWeijie
	 * @datetime 2015年6月2日_下午3:15:41
	 * @param beforeTime 前一个时间
	 * @param behindTime 后一个时间
	 * @return 所相差的小时数，有正负
	 */
	public static int timeDifferenceHour(final String beforeTime, final String behindTime) {
		if (beforeTime.equals(behindTime)) {
			// 因为日期小时相同
			return 0;
		}
		final Date bd1 = DateFormat.getShortHourDateShow(beforeTime);
		final Date bd2 = DateFormat.getShortHourDateShow(behindTime);
		// return cal1.compareTo(cal2);
		// if (bd1.getTime() > bd2.getTime()) {
		// return (int) ((bd1.getTime() - bd2.getTime() + 1l) / CalendarExpand.ONE_HOUR_TIME);
		// } else {
		// return (int) ((bd1.getTime() - bd2.getTime() - 1l) / CalendarExpand.ONE_HOUR_TIME);
		// }
		return CalendarExpand.timeDifferenceHour(bd1.getTime(), bd2.getTime());
	}

	/**
	 * 前一个时间点与后一个时间点所相差的小时数<br />
	 * 前大于后为正数，前小于后为负数<br />
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月16日_上午11:06:54
	 * @param beforeTime 前一个时间点
	 * @param behindTime 后一个时间点
	 * @return 所相差的小时数，有正负
	 */
	public static int timeDifferenceHour(final long beforeTime, final long behindTime) {
		return (int) ((beforeTime / CalendarExpand.ONE_HOUR_TIME) - (behindTime / CalendarExpand.ONE_HOUR_TIME));
	}
}