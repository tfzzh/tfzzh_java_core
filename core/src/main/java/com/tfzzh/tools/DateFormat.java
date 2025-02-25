/**
 * @author tfzzh
 * @createDate 2008-11-25 下午03:55:47
 */
package com.tfzzh.tools;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 字符串与日期间转换,字符转日期有可能返回null
 * 
 * @author tfzzh
 * @createDate 2008-11-25 下午03:55:47
 */
public class DateFormat {

	/**
	 * 格式化时间，显示用，完整，日期+时间+毫秒
	 */
	private static final String DATE_FORMAT_SHOW_FULL = "yyyy-MM-dd HH:mm:ss SSS";

	/**
	 * 格式化时间，完整，日期+时间+毫秒
	 */
	private static final String DATE_FORMAT_FULL = "yyyyMMddHHmmssSSS";

	/**
	 * 格式化时间，显示用，日期+时间
	 */
	private static final String DATE_FORMAT_SHOW_LONG = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 格式化时间，日期+时间
	 */
	private static final String DATE_FORMAT_LONG = "yyyyMMddHHmmss";

	/**
	 * 格式化时间，显示用，日期
	 */
	private static final String DATE_FORMAT_SHOW_SHORT = "yyyy-MM-dd";

	/**
	 * 格式化时间，显示用，日期+小时数
	 * 
	 * @author XuWeijie
	 * @datetime 2015年6月2日_下午3:07:06
	 */
	private static final String DATE_FORMAT_SHOW_SHORT_HOUR = "yyyy-MM-dd HH";

	/**
	 * 格式化时间，日期+小时数
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月12日 下午11:02:12
	 */
	private static final String DATE_FORMAT_SHORT_HOUR = "yyyyMMddHH";

	/**
	 * 格式化时间，显示用，日期+小时数:分钟数
	 * 
	 * @author tfzzh
	 * @dateTime 2020年10月25日 下午12:34:09
	 */
	private static final String DATE_FORMAT_SHOW_SHORT_HOUR_MINUTE = "yyyy-MM-dd HH:mm";

	/**
	 * 格式化时间，日期
	 */
	private static final String DATE_FORMAT_SHORT = "yyyyMMdd";

	/**
	 * 格式化时间，年月
	 * 
	 * @author xuweijie
	 * @dateTime 2012-3-30 下午4:38:50
	 */
	private static final String DATE_FORMAT_YEAR_MONTH = "yyyyMM";

	/**
	 * 格式化时间，年月，显示用
	 * 
	 * @author tfzzh
	 * @dateTime 2020年8月20日 下午7:33:21
	 */
	private static final String DATE_FORMAT_YEAR_MONTH_SHOW = "yyyy-MM";

	/**
	 * 格式化时间：时分秒
	 * 
	 * @author Weijie Xu
	 * @dateTime Aug 19, 2014 8:17:49 PM
	 */
	private static final String DATE_FORMAT_SHOW_TIME = "HH:mm:ss";

	/**
	 * 格式化时间：时分秒
	 * 
	 * @author Weijie Xu
	 * @dateTime Aug 19, 2014 8:17:50 PM
	 */
	private static final String DATE_FORMAT_TIME = "HHmmss";

	/**
	 * 格式化时间：带有时区相关显示
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月10日 下午3:21:59
	 */
	private static final String DATE_FORMAT_EXPIRE = "yyyy-MM-dd HH:mm:ss";

	/**
	 * RFC3339格式
	 * 
	 * @author tfzzh
	 * @dateTime 2022年10月23日 下午7:51:00
	 */
	private static final String DATE_RFC3339 = "yyyy-MM-dd'T'HH:mm:ssZ";

	/**
	 * 将日期转换为字符串:完整(yyyyMMddHHmmssSSS)
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-25 下午04:06:02
	 * @param date 日期对象
	 * @return yyyyMMddHHmmssSSS格式字符串
	 */
	public static String getFullDate(final Date date) {
		if (null == date) {
			return null;
		} else {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return DateFormat.getFullDate(cal);
		}
	}

	/**
	 * 将毫秒串转换为字符串:完整(yyyyMMddHHmmssSSS)
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:48:58
	 * @param time 时间毫秒串
	 * @return yyyyMMddHHmmssSSS格式字符串
	 */
	public static String getFullDate(final long time) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return DateFormat.getFullDate(cal);
	}

	/**
	 * 将日历内容转换为字符串:长型(yyyy-MM-dd hh:mm:ss)
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:48:57
	 * @param cal 日历对象
	 * @return yyyyMMddHHmmssSSS格式字符串
	 */
	public static String getFullDate(final Calendar cal) {
		final StringBuilder sb = new StringBuilder(17);
		sb.append(cal.get(Calendar.YEAR)).append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2)).append(StringTools.intToString(cal.get(Calendar.DAY_OF_MONTH), 2)).append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2)).append(StringTools.intToString(cal.get(Calendar.MINUTE), 2)).append(StringTools.intToString(cal.get(Calendar.SECOND), 2)).append(StringTools.intToString(cal.get(Calendar.MILLISECOND), 3));
		return sb.toString().intern();
	}

	/**
	 * 将完整日期(yyyyMMddHHmmssSSS)字符转换为日期,如果有异常,返回null
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-25 下午04:40:22
	 * @param date yyyyMMddHHmmssSSS格式字符串
	 * @return 日期对象
	 */
	public static Date getFullDate(final String date) {
		if ((null == date) || (date.length() == 0)) {
			return null;
		} else {
			try {
				final SimpleDateFormat sdff = new SimpleDateFormat(DateFormat.DATE_FORMAT_FULL);
				return sdff.parse(date);
			} catch (final ParseException e) {
				return null;
			}
		}
	}

	/**
	 * 将日期转换为字符串:完整(yyyy-MM-dd HH:mm:ss SSS)
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-25 下午04:06:02
	 * @param date 日期对象
	 * @return yyyy-MM-dd HH:mm:ss SSS格式字符串
	 */
	public static String getFullDateShow(final Date date) {
		if (null == date) {
			return null;
		} else {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return DateFormat.getFullDateShow(cal);
		}
	}

	/**
	 * 将毫秒串转换为字符串:完整(yyyy-MM-dd HH:mm:ss SSS)
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:51:53
	 * @param time 时间毫秒串
	 * @return yyyy-MM-dd HH:mm:ss SSS格式字符串
	 */
	public static String getFullDateShow(final long time) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return DateFormat.getFullDateShow(cal);
	}

	/**
	 * 将日历内容转换为字符串:长型(yyyy-MM-dd hh:mm:ss)
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:51:54
	 * @param cal 日历对象
	 * @return yyyy-MM-dd HH:mm:ss SSS格式字符串
	 */
	public static String getFullDateShow(final Calendar cal) {
		final StringBuilder sb = new StringBuilder(23);
		sb.append(cal.get(Calendar.YEAR)).append('-').append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2)).append('-').append(StringTools.intToString(cal.get(Calendar.DAY_OF_MONTH), 2)).append(' ').append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2)).append(':').append(StringTools.intToString(cal.get(Calendar.MINUTE), 2)).append(':').append(StringTools.intToString(cal.get(Calendar.SECOND), 2)).append(':').append(StringTools.intToString(cal.get(Calendar.MILLISECOND), 3));
		return sb.toString().intern();
	}

	/**
	 * 将完整日期(yyyy-MM-dd HH:mm:ss SSS)字符转换为日期,如果有异常,返回null
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-25 下午04:40:22
	 * @param date yyyy-MM-dd HH:mm:ss SSS格式字符串
	 * @return 日期对象
	 */
	public static Date getFullDateShow(final String date) {
		if ((null == date) || (date.length() == 0)) {
			return null;
		} else {
			try {
				final SimpleDateFormat sdfsf = new SimpleDateFormat(DateFormat.DATE_FORMAT_SHOW_FULL);
				return sdfsf.parse(date);
			} catch (final ParseException e) {
				return null;
			}
		}
	}

	/**
	 * 将日期转换为字符串：短型(yyyyMMdd)
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-25 下午04:06:02
	 * @param date 日期对象
	 * @return yyyyMMdd格式字符串
	 */
	public static String getShortDate(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return DateFormat.getShortDate(cal);
	}

	/**
	 * 将毫秒串转换为字符串：短型(yyyyMMdd)
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:58:14
	 * @param time 时间毫秒串
	 * @return yyyyMMdd格式字符串
	 */
	public static String getShortDate(final long time) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return DateFormat.getShortDate(cal);
	}

	/**
	 * 将当天日历转换为字符串：短型(yyyyMMdd)
	 * 
	 * @author tfzzh
	 * @dateTime 2020年8月21日 下午2:54:27
	 * @return yyyyMMdd格式字符串
	 */
	public static String getShortDate() {
		return DateFormat.getShortDate(Calendar.getInstance());
	}

	/**
	 * 将日历内容转换为字符串：短型(yyyyMMdd)
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:59:01
	 * @param cal 日历对象
	 * @return yyyyMMdd格式字符串
	 */
	public static String getShortDate(final Calendar cal) {
		final StringBuilder sb = new StringBuilder(8);
		sb.append(cal.get(Calendar.YEAR)).append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2)).append(StringTools.intToString(cal.get(Calendar.DAY_OF_MONTH), 2));
		return sb.toString().intern();
	}

	/**
	 * 将短型日期(yyyyMMdd)字符转换为日期,如果有异常,返回null
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-25 下午04:40:22
	 * @param date yyyyMMdd格式字符串
	 * @return 日期对象
	 */
	public static Date getShortDate(final String date) {
		try {
			final SimpleDateFormat sdfs = new SimpleDateFormat(DateFormat.DATE_FORMAT_SHORT);
			return sdfs.parse(date);
		} catch (final ParseException e) {
			return null;
		}
	}

	/**
	 * 将日期转换为字符串：短型(yyyy-MM-dd)
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-25 下午04:06:02
	 * @param date 日期对象
	 * @return yyyy-MM-dd格式字符串
	 */
	public static String getShortDateShow(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return DateFormat.getShortDateShow(cal);
	}

	/**
	 * 将毫秒串转换为字符串：短型(yyyy-MM-dd)
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:58:21
	 * @param time 时间毫秒串
	 * @return yyyy-MM-dd格式字符串
	 */
	public static String getShortDateShow(final long time) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return DateFormat.getShortDateShow(cal);
	}

	/**
	 * 将日历内容转换为字符串：短型(yyyy-MM-dd)
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:59:05
	 * @param cal 日历对象
	 * @return yyyy-MM-dd格式字符串
	 */
	public static String getShortDateShow(final Calendar cal) {
		final StringBuilder sb = new StringBuilder(10);
		sb.append(cal.get(Calendar.YEAR)).append('-').append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2)).append('-').append(StringTools.intToString(cal.get(Calendar.DAY_OF_MONTH), 2));
		return sb.toString().intern();
	}

	/**
	 * 将短型日期(yyyy-MM-dd)字符转换为日期,如果有异常,返回null
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-25 下午04:40:22
	 * @param date yyyy-MM-dd格式字符串
	 * @return 日期对象
	 */
	public static Date getShortDateShow(final String date) {
		try {
			final SimpleDateFormat sdfss = new SimpleDateFormat(DateFormat.DATE_FORMAT_SHOW_SHORT);
			return sdfss.parse(date);
		} catch (final ParseException e) {
			return null;
		}
	}

	/**
	 * 将日期转换为字符串：短型(yyyy-MM-dd HH)
	 * 
	 * @author XuWeijie
	 * @datetime 2015年6月2日_下午3:12:19
	 * @param date 日期对象
	 * @return yyyy-MM-dd HH格式字符串
	 */
	public static String getShortHourDateShow(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return DateFormat.getShortHourDateShow(cal);
	}

	/**
	 * 将毫秒串转换为字符串：短型(yyyy-MM-dd HH)
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:58:25
	 * @param time 时间毫秒串
	 * @return yyyy-MM-dd HH格式字符串
	 */
	public static String getShortHourDateShow(final long time) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return DateFormat.getShortHourDateShow(cal);
	}

	/**
	 * 将日历内容转换为字符串：短型(yyyy-MM-dd HH)
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:59:09
	 * @param cal 日历对象
	 * @return yyyy-MM-dd HH格式字符串
	 */
	public static String getShortHourDateShow(final Calendar cal) {
		final StringBuilder sb = new StringBuilder(13);
		sb.append(cal.get(Calendar.YEAR)).append('-').append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2)).append('-').append(StringTools.intToString(cal.get(Calendar.DAY_OF_MONTH), 2)).append(' ').append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2));
		return sb.toString().intern();
	}

	/**
	 * 将短型日期(yyyy-MM-dd HH)字符转换为日期,如果有异常,返回null
	 * 
	 * @author XuWeijie
	 * @datetime 2015年6月2日_下午3:12:19
	 * @param date yyyy-MM-dd HH 格式字符串
	 * @return 日期对象
	 */
	public static Date getShortHourDateShow(final String date) {
		try {
			final SimpleDateFormat sdfss = new SimpleDateFormat(DateFormat.DATE_FORMAT_SHOW_SHORT_HOUR);
			return sdfss.parse(date);
		} catch (final ParseException e) {
			return null;
		}
	}

	/**
	 * 将日期转换为字符串：短型(yyyyMMddHH)
	 * 
	 * @author XuWeijie
	 * @dateTime 2020年12月12日 下午11:03:10
	 * @param date 日期对象
	 * @return yyyyMMddHH格式字符串
	 */
	public static String getShortHourDate(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return DateFormat.getShortHourDate(cal);
	}

	/**
	 * 将毫秒串转换为字符串：短型(yyyyMMddHH)
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月12日 下午11:03:10
	 * @param time 时间毫秒串
	 * @return yyyyMMddHH格式字符串
	 */
	public static String getShortHourDate(final long time) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return DateFormat.getShortHourDate(cal);
	}

	/**
	 * 将日历内容转换为字符串：短型(yyyyMMddHH)
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月12日 下午11:03:10
	 * @param cal 日历对象
	 * @return yyyyMMddHH格式字符串
	 */
	public static String getShortHourDate(final Calendar cal) {
		final StringBuilder sb = new StringBuilder(13);
		sb.append(cal.get(Calendar.YEAR)).append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2)).append(StringTools.intToString(cal.get(Calendar.DAY_OF_MONTH), 2)).append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2));
		return sb.toString().intern();
	}

	/**
	 * 将短型日期(yyyyMMddHH)字符转换为日期,如果有异常,返回null
	 * 
	 * @author XuWeijie
	 * @datetime 2015年6月2日_下午3:12:19
	 * @param date yyyyMMddHH 格式字符串
	 * @return 日期对象
	 */
	public static Date getShortHourDate(final String date) {
		try {
			final SimpleDateFormat sdfss = new SimpleDateFormat(DateFormat.DATE_FORMAT_SHORT_HOUR);
			return sdfss.parse(date);
		} catch (final ParseException e) {
			return null;
		}
	}

	/**
	 * 得到短日期小时(yyyyMMddHH)字串
	 * 
	 * @author tfzzh
	 * @dateTime 2025年1月10日 11:38:08
	 * @return 日期小时字串
	 */
	public static String getShortHourDate() {
		Calendar cal = Calendar.getInstance();
		final StringBuilder sb = new StringBuilder(10);
		sb.append(cal.get(Calendar.YEAR)).append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2)).append(StringTools.intToString(cal.get(Calendar.DAY_OF_MONTH), 2)).append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2));
		return sb.toString().intern();
	}

	/**
	 * 将短型日期(yyyy-MM-dd HH:mm)字符转换为日期,如果有异常,返回null
	 * 
	 * @author tfzzh
	 * @dateTime 2020年10月25日 下午12:35:03
	 * @param date yyyy-MM-dd HH:mm 格式字符串
	 * @return 日期对象
	 */
	public static Date getShortHourMinuteDateShow(final String date) {
		try {
			final SimpleDateFormat sdfss = new SimpleDateFormat(DateFormat.DATE_FORMAT_SHOW_SHORT_HOUR_MINUTE);
			return sdfss.parse(date);
		} catch (final ParseException e) {
			return null;
		}
	}

	/**
	 * 将日期转换为字符串：长型(yyyyMMddHHmmss)
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-25 下午04:06:41
	 * @param date 日期对象
	 * @return yyyyMMddHHmmss 格式字符串
	 */
	public static String getLongDate(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return DateFormat.getLongDate(cal);
	}

	/**
	 * 将毫秒串转换为字符串：长型(yyyyMMddHHmmss)
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:58:29
	 * @param time 时间毫秒串
	 * @return yyyyMMddHHmmss格式字符串
	 */
	public static String getLongDate(final long time) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return DateFormat.getLongDate(cal);
	}

	/**
	 * 将日历内容转换为字符串：长型(yyyyMMddHHmmss)
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:59:15
	 * @param cal 日历对象
	 * @return yyyyMMddHHmmss格式字符串
	 */
	public static String getLongDate(final Calendar cal) {
		final StringBuilder sb = new StringBuilder(14);
		sb.append(cal.get(Calendar.YEAR)).append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2)).append(StringTools.intToString(cal.get(Calendar.DAY_OF_MONTH), 2)).append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2)).append(StringTools.intToString(cal.get(Calendar.MINUTE), 2)).append(StringTools.intToString(cal.get(Calendar.SECOND), 2));
		return sb.toString().intern();
	}

	/**
	 * 将长型日期(yyyyMMddHHmmss)为日期,如果有异常,返回null
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-25 下午04:41:03
	 * @param date yyyyMMddHHmmss格式字符串
	 * @return 日期对象
	 */
	public static Date getLongDate(final String date) {
		try {
			final SimpleDateFormat sdfl = new SimpleDateFormat(DateFormat.DATE_FORMAT_LONG);
			return sdfl.parse(date);
		} catch (final ParseException e) {
			return null;
		}
	}

	/**
	 * 将日期转换为字符串:长型(yyyy-MM-dd hh:mm:ss)
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-25 下午04:06:41
	 * @param date 日期对象
	 * @return yyyy-MM-dd hh:mm:ss格式字符串
	 */
	public static String getLongDateShow(final Date date) {
		if (null == date) {
			return null;
		} else {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return DateFormat.getLongDateShow(cal);
		}
	}

	/**
	 * 将毫秒串转换为字符串:长型(yyyy-MM-dd hh:mm:ss)
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:45:08
	 * @param time 时间毫秒串
	 * @return yyyy-MM-dd hh:mm:ss格式字符串
	 */
	public static String getLongDateShow(final long time) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return DateFormat.getLongDateShow(cal);
	}

	/**
	 * 将日历内容转换为字符串:长型(yyyy-MM-dd hh:mm:ss)
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:45:57
	 * @param cal 日历对象
	 * @return yyyy-MM-dd hh:mm:ss格式字符串
	 */
	public static String getLongDateShow(final Calendar cal) {
		final StringBuilder sb = new StringBuilder(19);
		sb.append(cal.get(Calendar.YEAR)).append('-').append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2)).append('-').append(StringTools.intToString(cal.get(Calendar.DAY_OF_MONTH), 2)).append(' ').append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2)).append(':').append(StringTools.intToString(cal.get(Calendar.MINUTE), 2)).append(':').append(StringTools.intToString(cal.get(Calendar.SECOND), 2));
		return sb.toString().intern();
	}

	/**
	 * 将长型日期(yyyy-MM-dd hh:mm:ss)字符转换为日期,如果有异常,返回null
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-25 下午04:41:03
	 * @param date yyyy-MM-dd hh:mm:ss格式字符串
	 * @return 日期对象
	 */
	public static Date getLongDateShow(final String date) {
		try {
			final SimpleDateFormat sdfsl = new SimpleDateFormat(DateFormat.DATE_FORMAT_SHOW_LONG);
			return sdfsl.parse(date);
		} catch (final ParseException e) {
			return null;
		}
	}

	/**
	 * 得到Rfc3339格式时间字串
	 * 
	 * @author tfzzh
	 * @dateTime 2022年10月23日 上午2:17:28
	 * @param date 目标时间
	 * @return Rfc3339格式时间字串
	 */
	public static String getRfc3339(final Date date) {
		// final Format f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		final Format f = new SimpleDateFormat(DateFormat.DATE_RFC3339);
		String str = f.format(new Date());
		if (!str.endsWith(":00")) {
			str = str.substring(0, str.length() - 2) + ":00";
		}
		return str;
	}

	/**
	 * 根据Rfc3339格式时间字串得到时间对象
	 * 
	 * @author tfzzh
	 * @dateTime 2022年10月23日 下午7:49:38
	 * @param date Rfc3339格式时间字串
	 * @return 时间对象
	 */
	public static Date getRfc3339(String date) {
		if ((null == date) || (date.length() < 20)) {
			return null;
		}
		// final Format f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		final Format f = new SimpleDateFormat(DateFormat.DATE_RFC3339);
		if (date.endsWith(":00")) {
			date = date.substring(0, date.length() - 3) + "00";
		}
		try {
			final Date d = (Date) f.parseObject(date);
			return d;
		} catch (final ParseException e) {
			// e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将日期转换为字符串：长型(yyyyMM)
	 * 
	 * @author xuweijie
	 * @dateTime 2012-3-30 下午4:55:49
	 * @param date 日期对象
	 * @return yyyyMM格式字符串
	 */
	public static String getYearMonthDate(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return DateFormat.getYearMonthDate(cal);
	}

	/**
	 * 将毫秒串转换为字符串：长型(yyyyMM)
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:58:34
	 * @param time 时间毫秒串
	 * @return yyyyMM格式字符串
	 */
	public static String getYearMonthDate(final long time) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return DateFormat.getYearMonthDate(cal);
	}

	/**
	 * 将日历内容转换为字符串：长型(yyyyMM)
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:59:20
	 * @param cal 日历对象
	 * @return yyyyMM格式字符串
	 */
	public static String getYearMonthDate(final Calendar cal) {
		final StringBuilder sb = new StringBuilder(6);
		sb.append(cal.get(Calendar.YEAR)).append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2));
		return sb.toString().intern();
	}

	/**
	 * 将长型日期(yyyyMM)为日期,如果有异常,返回null
	 * 
	 * @author xuweijie
	 * @dateTime 2012-3-30 下午4:55:48
	 * @param date yyyyMM格式字符串
	 * @return 日期对象
	 */
	public static Date getYearMonthDate(final String date) {
		try {
			final SimpleDateFormat sdfl = new SimpleDateFormat(DateFormat.DATE_FORMAT_YEAR_MONTH);
			return sdfl.parse(date);
		} catch (final ParseException e) {
			return null;
		}
	}

	/**
	 * 将日期(yyyy-MM)转为日期,如果有异常,返回null
	 * 
	 * @author tfzzh
	 * @dateTime 2020年8月20日 下午7:34:17
	 * @param date yyyy-MM格式字符串
	 * @return 日期对象
	 */
	public static Date getYearMonthDateShow(final String date) {
		try {
			final SimpleDateFormat sdfl = new SimpleDateFormat(DateFormat.DATE_FORMAT_YEAR_MONTH_SHOW);
			return sdfl.parse(date);
		} catch (final ParseException e) {
			return null;
		}
	}

	/**
	 * 得到时间：年月（yyyy-MM）
	 * 
	 * @author Weijie Xu
	 * @dateTime Aug 19, 2014 8:21:49 PM
	 * @param date 当前时间
	 * @return yyyy-MM格式字符串
	 */
	public static String getYearMonthDateShow(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		final StringBuilder sb = new StringBuilder(7);
		sb.append(cal.get(Calendar.YEAR)).append('-').append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2));
		return sb.toString().intern();
	}

	/**
	 * 得到时间：年月（yyyy-MM）
	 * 
	 * @author tfzzh
	 * @dateTime 2020年8月20日 下午7:38:18
	 * @return yyyy-MM格式字符串
	 */
	public static String getYearMonthDateShow() {
		final Calendar cal = Calendar.getInstance();
		return DateFormat.getYearMonthDateShow(cal);
	}

	/**
	 * 得到时间：年月（yyyy-MM）
	 * 
	 * @author tfzzh
	 * @dateTime 2020年8月20日 下午7:38:20
	 * @param cal 目标日历对象
	 * @return yyyy-MM格式字符串
	 */
	public static String getYearMonthDateShow(final Calendar cal) {
		final StringBuilder sb = new StringBuilder(7);
		sb.append(cal.get(Calendar.YEAR)).append('-').append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2));
		return sb.toString().intern();
	}

	/**
	 * 将日期转换为字符串:rfc3339标准(yyyy-MM-ddTHH:mm:ssSZ)
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月10日 下午3:26:15
	 * @param date 日期对象
	 * @return yyyy-MM-ddTHH:mm:ssSZ格式字符串
	 */
	public static String getExpireDate(final Date date) {
		if (null == date) {
			return null;
		} else {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return DateFormat.getExpireDate(cal);
		}
	}

	/**
	 * 将毫秒串转换为字符串:rfc3339标准(yyyy-MM-ddTHH:mm:ssSZ)
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月10日 下午3:26:15
	 * @param time 时间毫秒串
	 * @return yyyy-MM-ddTHH:mm:ssSZ格式字符串
	 */
	public static String getExpireDate(final long time) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return DateFormat.getExpireDate(cal);
	}

	/**
	 * 将日历内容转换为字符串:rfc3339标准(yyyy-MM-ddTHH:mm:ssSZ)
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月10日 下午3:26:15
	 * @param cal 日历对象
	 * @return yyyy-MM-ddTHH:mm:ssSZ格式字符串
	 */
	public static String getExpireDate(final Calendar cal) {
		final StringBuilder sb = new StringBuilder(17);
		sb.append(cal.get(Calendar.YEAR)).append('-').append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2)).append('-').append(StringTools.intToString(cal.get(Calendar.DAY_OF_MONTH), 2)).append('T').append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2)).append(':').append(StringTools.intToString(cal.get(Calendar.MINUTE), 2)).append(':').append(StringTools.intToString(cal.get(Calendar.SECOND), 2)).append("+08:00");
		return sb.toString().intern();
	}

	/**
	 * 将rfc3339标准(yyyy-MM-ddTHH:mm:ssSZ)字符转换为日期,如果有异常,返回null
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月10日 下午3:26:15
	 * @param date yyyy-MM-ddTHH:mm:ssSZ格式字符串
	 * @return 日期对象
	 */
	public static Date getExpireDate(String date) {
		if ((null == date) || (date.length() == 0)) {
			return null;
		} else {
			try {
				date = date.replaceAll("T", " ");
				date = date.replaceAll("\\+08:00T", "");
				final SimpleDateFormat sdff = new SimpleDateFormat(DateFormat.DATE_FORMAT_EXPIRE);
				return sdff.parse(date);
			} catch (final ParseException e) {
				return null;
			}
		}
	}

	/**
	 * 得到时间：时分秒（HHmmss）
	 * 
	 * @author Weijie Xu
	 * @dateTime Aug 19, 2014 8:21:49 PM
	 * @param date 当前时间
	 * @return HHmmss格式字符串
	 */
	public static String getTime(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return DateFormat.getTime(cal);
	}

	/**
	 * 将毫秒串转换为字符串：时分秒（HHmmss）
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:58:39
	 * @param time 时间毫秒串
	 * @return HHmmss格式字符串
	 */
	public static String getTime(final long time) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return DateFormat.getTime(cal);
	}

	/**
	 * 将日历内容转换为字符串：时分秒（HHmmss）
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:59:28
	 * @param cal 日历对象
	 * @return HHmmss格式字符串
	 */
	public static String getTime(final Calendar cal) {
		final StringBuilder sb = new StringBuilder(6);
		sb.append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2)).append(StringTools.intToString(cal.get(Calendar.MINUTE), 2)).append(StringTools.intToString(cal.get(Calendar.SECOND), 2));
		return sb.toString().intern();
	}

	/**
	 * 将字串（HHmmss）转换为时间
	 * 
	 * @author Weijie Xu
	 * @dateTime Sep 4, 2014 8:30:59 PM
	 * @param date HHmmss格式字符串
	 * @return 时间对象
	 */
	public static Date getTime(final String date) {
		final SimpleDateFormat sdft = new SimpleDateFormat(DateFormat.DATE_FORMAT_TIME);
		try {
			return sdft.parse(date);
		} catch (final ParseException e) {
			return null;
		}
	}

	/**
	 * 得到时间：时分秒（HH:mm:ss）
	 * 
	 * @author Weijie Xu
	 * @dateTime Aug 19, 2014 8:21:48 PM
	 * @param date 当前时间
	 * @return HH:mm:ss格式字符串
	 */
	public static String getTimeShow(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return DateFormat.getTimeShow(cal);
	}

	/**
	 * 将毫秒串转换为字符串：时分秒（HH:mm:ss）
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:58:43
	 * @param time 时间毫秒串
	 * @return HH:mm:ss格式字符串
	 */
	public static String getTimeShow(final long time) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return DateFormat.getTimeShow(cal);
	}

	/**
	 * 将日历内容转换为字符串：时分秒（HH:mm:ss）
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月13日 下午3:59:35
	 * @param cal 日历对象
	 * @return HH:mm:ss格式字符串
	 */
	public static String getTimeShow(final Calendar cal) {
		final StringBuilder sb = new StringBuilder(8);
		sb.append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2)).append(':').append(StringTools.intToString(cal.get(Calendar.MINUTE), 2)).append(':').append(StringTools.intToString(cal.get(Calendar.SECOND), 2));
		return sb.toString().intern();
	}

	/**
	 * 将字串（HH:mm:ss）转换为时间
	 * 
	 * @author Weijie Xu
	 * @dateTime Sep 4, 2014 8:32:14 PM
	 * @param date HH:mm:ss格式字符串
	 * @return 时间对象
	 */
	public static Date getTimeShow(final String date) {
		final SimpleDateFormat sdfst = new SimpleDateFormat(DateFormat.DATE_FORMAT_SHOW_TIME);
		try {
			return sdfst.parse(date);
		} catch (final ParseException e) {
			return null;
		}
	}

	/**
	 * 自定日期转换格式，将日期转为指定结构字符串
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-1 下午2:58:52
	 * @param date 日期对象
	 * @param format 目标结构字串
	 * @return 指定格式日期字串
	 */
	public static String customDateFormat(final Date date, final String format) {
		if (null == date) {
			return null;
		} else {
			final SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		}
	}

	/**
	 * 得到当前日期的字符串（yyyyMMdd）
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月7日 下午5:22:33
	 * @return 当前日期的字符串（yyyyMMdd）
	 */
	public static String getDayShortStr() {
		final Calendar cal = Calendar.getInstance();
		final StringBuilder sb = new StringBuilder(8);
		sb.append(cal.get(Calendar.YEAR)).append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2)).append(StringTools.intToString(cal.get(Calendar.DAY_OF_MONTH), 2));
		return sb.toString().intern();
	}

	/**
	 * 得到当前日期的显示用字符串（yyyy-MM-dd）
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月7日 下午5:22:33
	 * @return 当前日期的显示用字符串（yyyy-MM-dd）
	 */
	public static String getDayShortShowStr() {
		final Calendar cal = Calendar.getInstance();
		final StringBuilder sb = new StringBuilder(10);
		sb.append(cal.get(Calendar.YEAR)).append('-').append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2)).append('-').append(StringTools.intToString(cal.get(Calendar.DAY_OF_MONTH), 2));
		return sb.toString().intern();
	}

	/**
	 * 得到当前日期时间长型的字符串（yyyyMMddHHmmss）
	 * 
	 * @author tfzzh
	 * @dateTime 2016年8月29日 下午1:36:29
	 * @return 当前日期时间长型的字符串（yyyyMMddHHmmss）
	 */
	public static String getLongDate() {
		final Calendar cal = Calendar.getInstance();
		final StringBuilder sb = new StringBuilder(14);
		sb.append(cal.get(Calendar.YEAR)).append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2)).append(StringTools.intToString(cal.get(Calendar.DAY_OF_MONTH), 2)).append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2)).append(StringTools.intToString(cal.get(Calendar.MINUTE), 2)).append(StringTools.intToString(cal.get(Calendar.SECOND), 2));
		return sb.toString().intern();
	}

	/**
	 * 得到当前时间显示用长型的字符串:长型(yyyy-MM-dd hh:mm:ss)
	 * 
	 * @author tfzzh
	 * @dateTime 2016年8月7日 下午1:18:06
	 * @return yyyy-MM-dd hh:mm:ss格式字符串
	 */
	public static String getLongDateShow() {
		final Calendar cal = Calendar.getInstance();
		final StringBuilder sb = new StringBuilder(19);
		sb.append(cal.get(Calendar.YEAR)).append('-').append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2)).append('-').append(StringTools.intToString(cal.get(Calendar.DAY_OF_MONTH), 2)).append(' ').append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2)).append(':').append(StringTools.intToString(cal.get(Calendar.MINUTE), 2)).append(':').append(StringTools.intToString(cal.get(Calendar.SECOND), 2));
		return sb.toString().intern();
	}

	/**
	 * 得到当前日期时间长型的字符串（yyyyMMddHHmmssSSS）
	 * 
	 * @author tfzzh
	 * @dateTime 2016年8月29日 下午1:40:45
	 * @return 当前日期时间长型的字符串（yyyyMMddHHmmssSSS）
	 */
	public static String getFullDate() {
		final Calendar cal = Calendar.getInstance();
		final StringBuilder sb = new StringBuilder(17);
		sb.append(cal.get(Calendar.YEAR)).append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2)).append(StringTools.intToString(cal.get(Calendar.DAY_OF_MONTH), 2)).append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2)).append(StringTools.intToString(cal.get(Calendar.MINUTE), 2)).append(StringTools.intToString(cal.get(Calendar.SECOND), 2)).append(StringTools.intToString(cal.get(Calendar.MILLISECOND), 3));
		return sb.toString().intern();
	}

	/**
	 * 得到当前时间显示用长型的字符串:长型(yyyy-MM-dd hh:mm:ss SSS)
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-25 下午04:06:02
	 * @return 当前时间显示用长型的字符串:长型(yyyy-MM-dd hh:mm:ss SSS)
	 */
	public static String getFullDateShow() {
		final Calendar cal = Calendar.getInstance();
		final StringBuilder sb = new StringBuilder(23);
		sb.append(cal.get(Calendar.YEAR)).append('-').append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2)).append('-').append(StringTools.intToString(cal.get(Calendar.DAY_OF_MONTH), 2)).append(' ').append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2)).append(':').append(StringTools.intToString(cal.get(Calendar.MINUTE), 2)).append(':').append(StringTools.intToString(cal.get(Calendar.SECOND), 2)).append(':').append(StringTools.intToString(cal.get(Calendar.MILLISECOND), 3));
		return sb.toString().intern();
	}

	/**
	 * 得到当前日期的显示用字符串（yyyy-MM-dd HH）
	 * 
	 * @author XuWeijie
	 * @datetime 2015年6月2日_下午3:14:36
	 * @return 当前日期的显示用字符串（yyyy-MM-dd HH）
	 */
	public static String getShortHourDateShow() {
		final Calendar cal = Calendar.getInstance();
		final StringBuilder sb = new StringBuilder(13);
		sb.append(cal.get(Calendar.YEAR)).append('-').append(StringTools.intToString(cal.get(Calendar.MONTH) + 1, 2)).append('-').append(StringTools.intToString(cal.get(Calendar.DAY_OF_MONTH), 2)).append(' ').append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2));
		return sb.toString().intern();
	}

	/**
	 * 得到当前时间的字符串（HHmmss）
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月7日 下午5:22:35
	 * @return 当前时间的字符串（HHmmss）
	 */
	public static String getTimeShortStr() {
		final Calendar cal = Calendar.getInstance();
		final StringBuilder sb = new StringBuilder(6);
		sb.append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2)).append(StringTools.intToString(cal.get(Calendar.MINUTE), 2)).append(StringTools.intToString(cal.get(Calendar.SECOND), 2));
		return sb.toString().intern();
	}

	/**
	 * 得到当前时间的显示用字符串（HHmmss）
	 * 
	 * @author tfzzh
	 * @dateTime 2016年8月29日 下午1:48:59
	 * @return 当前时间的显示用字符串（HHmmss）
	 */
	public static String getTimeShortStrShow() {
		final Calendar cal = Calendar.getInstance();
		final StringBuilder sb = new StringBuilder(8);
		sb.append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2)).append(':').append(StringTools.intToString(cal.get(Calendar.MINUTE), 2)).append(':').append(StringTools.intToString(cal.get(Calendar.SECOND), 2));
		return sb.toString().intern();
	}

	/**
	 * 得到当前完整时间的字符串（HHmmssSSS）
	 * 
	 * @author tfzzh
	 * @dateTime 2016年8月29日 下午1:50:24
	 * @return 当前完整时间的字符串（HHmmss）
	 */
	public static String getTimeLongStr() {
		final Calendar cal = Calendar.getInstance();
		final StringBuilder sb = new StringBuilder(6);
		sb.append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2)).append(StringTools.intToString(cal.get(Calendar.MINUTE), 2)).append(StringTools.intToString(cal.get(Calendar.SECOND), 2)).append(StringTools.intToString(cal.get(Calendar.MILLISECOND), 3));
		return sb.toString().intern();
	}

	/**
	 * 得到当前完整时间的显示用字符串（HH:mm:ss SSS）
	 * 
	 * @author tfzzh
	 * @dateTime 2016年8月29日 下午1:50:23
	 * @return 当前完整时间的显示用字符串（HH:mm:ss SSS）
	 */
	public static String getTimeLongStrShow() {
		final Calendar cal = Calendar.getInstance();
		final StringBuilder sb = new StringBuilder(8);
		sb.append(StringTools.intToString(cal.get(Calendar.HOUR_OF_DAY), 2)).append(':').append(StringTools.intToString(cal.get(Calendar.MINUTE), 2)).append(':').append(StringTools.intToString(cal.get(Calendar.SECOND), 2)).append(' ').append(StringTools.intToString(cal.get(Calendar.MILLISECOND), 3));
		return sb.toString().intern();
	}

	/**
	 * 将时间从显示格式变为紧凑模式
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月25日_下午4:53:19
	 * @param day 显示模式时间
	 * @return 紧凑模式时间
	 */
	public static String getTimeShowToNone(String day) {
		day = day.replaceAll("-", "");
		day = day.replaceAll("/", "");
		day = day.replaceAll(":", "");
		day = day.replaceAll(" ", "");
		return day;
	}

	/**
	 * 将日期字符串转为long
	 * 
	 * @author tfzzh
	 * @dateTime 2021年4月12日 下午1:23:02
	 * @param day 日期字符串
	 * @return long值
	 */
	public static long getTimeStrToLong(String day) {
		day = DateFormat.getTimeShowToNone(day);
		return Long.parseLong(day);
	}

	/**
	 * 仅针对excel模版数据导入时，时间类型字段直接转为时间戳模型
	 * 
	 * @author tfzzh
	 * @dateTime 2020年10月25日 下午12:26:37
	 * @param str 目标内容
	 * @return 被处理完成的时间戳；</br>
	 *         null，表示不是时间格式内容；</br>
	 */
	public static Long formatStringToTime(final String str) {
		if (StringTools.isNullOrEmpty(str)) {
			return null;
		}
		switch (str.length()) {
		case 7: { // yyyy-MM
			final Date d = DateFormat.getYearMonthDateShow(str);
			if (null == d) {
				return null;
			}
			return d.getTime();
		}
		case 10: { // yyyy-MM-dd
			final Date d = DateFormat.getShortDateShow(str);
			if (null == d) {
				return null;
			}
			return d.getTime();
		}
		case 13: { // yyyy-MM-dd HH
			final Date d = DateFormat.getShortHourDateShow(str);
			if (null == d) {
				return null;
			}
			return d.getTime();
		}
		case 16: { // yyyy-MM-dd HH:mm
			final Date d = DateFormat.getShortHourMinuteDateShow(str);
			if (null == d) {
				return null;
			}
			return d.getTime();
		}
		case 19: { // yyyy-MM-dd HH:mm:ss
			final Date d = DateFormat.getLongDateShow(str);
			if (null == d) {
				return null;
			}
			return d.getTime();
		}
		}
		return null;
	}
}
