/**
 * @author xuweijie
 * @dateTime 2012-2-15 下午2:31:19
 */
package com.tfzzh.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串工具
 * 
 * @author xuweijie
 * @dateTime 2012-2-15 下午2:31:19
 */
public class StringTools {

	/**
	 * 空对象
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月21日 上午11:38:09
	 */
	private final static String empty = "";

	/**
	 * 根据字符串中的分隔符“_”对字母进行从新组合，将存在的该符号去掉，并将其之后的字符大写<br />
	 * 例如：“aa_a_BbbCc_c” -> “aaABbbCcC”<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午3:35:47
	 * @param target 目标字串
	 * @return 新字串
	 */
	public static String assemblyStringWhitInterval(final String target) {
		return StringTools.assemblyStringWhitInterval(target, false, false);
	}

	/**
	 * 根据字符串中的分隔符“_”对字母进行从新组合，将存在的该符号去掉，并将其之后的字符大写<br />
	 * 例如：“aa_a_BbbCc_c” -> “aaABbbCcC”<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午3:38:34
	 * @param target 目标字串
	 * @param firstUpper 是否将首字符大写
	 * @return 新字串
	 */
	public static String assemblyStringWhitInterval(final String target, final boolean firstUpper) {
		return StringTools.assemblyStringWhitInterval(target, firstUpper, false);
	}

	/**
	 * 根据字符串中的分隔符“_”对字母进行从新组合，将存在的该符号去掉，并将其之后的字符大写<br />
	 * 例如：“aa_a_BbbCc_c” -> “aaABbbCcC”<br />
	 * 
	 * @author xuweijie
	 * @datetime 2015年11月17日_下午3:11:13
	 * @param target 目标字串
	 * @param firstUpper 是否将首字符大写
	 * @param otherLower 是否其他字符转为小写
	 * @return 新字串
	 */
	public static String assemblyStringWhitInterval(final String target, final boolean firstUpper, final boolean otherLower) {
		final StringBuilder sb = new StringBuilder(target.length());
		short s;
		for (int i = 0, n = target.length(); i < n; i++) {
			s = (short) target.charAt(i);
			if (i == 0) {
				if (firstUpper) {
					//
					if ((s >= 97) && (s <= 122)) {
						sb.append((char) (s - 32));
					} else {
						sb.append((char) s);
					}
					continue;
				}
			}
			if (s == '_') {
				// 是符号，则针对下一个内容直接操作
				s = (short) target.charAt(++i);
				if ((s >= 97) && (s <= 122)) {
					// sb.append((char) (s - 32));
					// continue;
					s -= 32;
				}
			} else if (otherLower) {
				if ((s >= 65) && (s <= 90)) {
					s += 32;
				}
			}
			sb.append((char) s);
		}
		return sb.toString();
	}

	/**
	 * 根据字符串中的大写字母进行从新组合<br />
	 * 例如：“AaaBbbCcc” -> “aaa_bbb_ccc”<br />
	 * 
	 * @author XuWeijie
	 * @dateTime 2012-2-15 下午2:32:25
	 * @param target 目标字串
	 * @return 处理过的字符串
	 */
	public static String splitStringWhitUpper(final String target) {
		final StringBuilder sb = new StringBuilder((int) (target.length() * 1.4));
		short s;
		for (int i = 0, n = target.length(); i < n; i++) {
			s = (short) target.charAt(i);
			if ((s >= 65) && (s <= 90)) {
				if (sb.length() != 0) {
					sb.append('_');
				}
				sb.append((char) (s + 32));
			} else {
				sb.append((char) s);
			}
		}
		return sb.toString();
	}

	/**
	 * 最小化消耗来替代String.split的方法<br />
	 * 会将零字长字串在结果中过滤<br />
	 * 如果也考虑不过滤的方法，请考虑增加参数，并修改一部分代码，并增加一些对应的多态方法<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime Aug 8, 2014 10:14:08 AM
	 * @param str 准备做分割处理的字串
	 * @return 当前认为此方法出来的一定是String相关
	 */
	public static String[] split(final String str) {
		// 默认的分隔符
		return StringTools.split(str, "#");
	}

	/**
	 * 替换一段字串中的内容为目标内容
	 * 
	 * @author tfzzh
	 * @dateTime 2016年8月30日 下午5:28:26
	 * @param str 目标字串
	 * @param find 查找目标
	 * @param replace 替换为的内容
	 * @param startIndex 变更开始位
	 * @return 结果内容串
	 */
	public static String replace(final String str, final String find, final String replace, final int startIndex) {
		final StringBuilder sb = new StringBuilder(str);
		int index = startIndex;
		while ((index = sb.indexOf(find, index)) != -1) {
			sb.replace(index, index + find.length(), replace);
		}
		return sb.toString();
	}

	// public static void main(String[] args) {
	// long l1 = System.currentTimeMillis();
	// String str = "http://///www.aaa.com//cc//////addfjakjf_/////sjfasj.html";
	// System.out.println(StringTools.replace(str, "//", "/", 6));
	// for (int i = 10000000; i > 0; i--) {
	// StringTools.replace(str, "//", "/", 7);
	// }
	// System.out.println("over run >> " + (System.currentTimeMillis() - l1));
	// }
	/**
	 * 最小化消耗来替代String.split的方法<br />
	 * 未存在null判定<br />
	 * 会将零字长字串在结果中过滤<br />
	 * 如果也考虑不过滤的方法，请考虑增加参数，并修改一部分代码，并增加一些对应的多态方法<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime Aug 8, 2014 9:54:55 AM
	 * @param str 准备做分割处理的字串
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return 当前认为此方法出来的一定是String相关
	 */
	public static String[] split(final String str, final String splitFlag) {
		// final List<String> arr = new ArrayList<String>();
		// // start index
		// int si = 0;
		// // end index
		// int ei = 0;
		// while (true) {
		// ei = str.indexOf(splitFlag, si);
		// if (ei == -1) {
		// // 已经结束
		// break;
		// }
		// if (ei != si) {
		// arr.add(str.substring(si, ei));
		// }
		// si = ei + splitFlag.length();
		// }
		// // 增加最后一段
		// arr.add(str.substring(si));
		final List<String> arr = StringTools.splitToArray(str, splitFlag);
		return arr.toArray(new String[arr.size()]);
	}

	/**
	 * 最小化消耗来替代String.split的方法<br />
	 * 未存在null判定<br />
	 * 会将零字长字串在结果中过滤<br />
	 * 如果也考虑不过滤的方法，请考虑增加参数，并修改一部分代码，并增加一些对应的多态方法<br />
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月24日_下午4:00:45
	 * @param str 准备做分割处理的字串
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return string列表
	 */
	public static List<String> splitToArray(final String str, final String splitFlag) {
		final List<String> arr = new ArrayList<>();
		// start index
		int si = 0;
		// end index
		int ei = 0;
		while (true) {
			ei = str.indexOf(splitFlag, si);
			if (ei == -1) {
				// 已经结束
				break;
			}
			if (ei != si) {
				arr.add(str.substring(si, ei));
			}
			si = ei + splitFlag.length();
		}
		if (si < str.length()) {
			// 增加最后一段
			arr.add(str.substring(si));
		}
		return arr;
	}

	/**
	 * 将字符串转为int数组
	 * 
	 * @author XuWeijie
	 * @datetime 2015年10月29日_上午9:52:13
	 * @param txt 文本内容
	 * @param separator 分隔符
	 * @return int数组
	 */
	public static int[] toIntArray(final String txt, final String separator) {
		final List<Integer> li = StringTools.splitToIntArray(txt, separator);
		final int[] bak = new int[li.size()];
		for (int i = li.size() - 1; i >= 0; i--) {
			bak[i] = li.get(i).intValue();
		}
		return bak;
	}

	/**
	 * 将字符串转为Integer数组
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 下午1:48:13
	 * @param txt 文本内容
	 * @param separator 分隔符
	 * @return Integer数组
	 */
	public static Integer[] toIntObjArray(final String txt, final String separator) {
		final List<Integer> li = StringTools.splitToIntArray(txt, separator);
		return li.toArray(new Integer[li.size()]);
	}

	/**
	 * 最小化消耗来替代String.split的方法<br />
	 * 直接获取到Int列表<br />
	 * 
	 * @author XuWeijie
	 * @datetime 2016年1月8日_下午3:06:58
	 * @param str 准备做分割处理的字串
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return Integer列表
	 */
	public static List<Integer> splitToIntArray(final String str, final String splitFlag) {
		final List<Integer> arr = new ArrayList<>();
		if ((null == str) || (str.length() == 0)) {
			return arr;
		}
		// start index
		int si = 0;
		// end index
		int ei = 0;
		while (true) {
			ei = str.indexOf(splitFlag, si);
			if (ei == -1) {
				// 已经结束
				break;
			}
			if (ei != si) {
				try {
					arr.add(Double.valueOf(str.substring(si, ei)).intValue());
				} catch (final Exception e) {
				}
			}
			si = ei + splitFlag.length();
		}
		// 增加最后一段
		try {
			arr.add(Double.valueOf(str.substring(si)).intValue());
		} catch (final Exception e) {
		}
		return arr;
	}

	/**
	 * 将字符串转为short数组
	 * 
	 * @author XuWeijie
	 * @datetime 2015年10月29日_上午9:40:23
	 * @param txt 文本内容
	 * @param separator 分隔符
	 * @return short数组
	 */
	public static short[] toShortArray(final String txt, final String separator) {
		final List<Short> tl = StringTools.splitToShortArray(txt, separator);
		final short[] bak = new short[tl.size()];
		for (int i = tl.size() - 1; i >= 0; i--) {
			bak[i] = tl.get(i).shortValue();
		}
		return bak;
	}

	/**
	 * 将字符串转为Short数组
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 下午1:56:34
	 * @param txt 文本内容
	 * @param separator 分隔符
	 * @return Short数组
	 */
	public static Short[] toShortObjArray(final String txt, final String separator) {
		final List<Short> ls = StringTools.splitToShortArray(txt, separator);
		return ls.toArray(new Short[ls.size()]);
	}

	/**
	 * 最小化消耗来替代String.split的方法<br />
	 * 直接获取到Short列表<br />
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月30日_下午6:01:42
	 * @param str 准备做分割处理的字串
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return Short列表
	 */
	public static List<Short> splitToShortArray(final String str, final String splitFlag) {
		final List<Short> arr = new ArrayList<>();
		// start index
		int si = 0;
		// end index
		int ei = 0;
		while (true) {
			ei = str.indexOf(splitFlag, si);
			if (ei == -1) {
				// 已经结束
				break;
			}
			if (ei != si) {
				try {
					arr.add(Double.valueOf(str.substring(si, ei)).shortValue());
				} catch (final Exception e) {
				}
			}
			si = ei + splitFlag.length();
		}
		// 增加最后一段
		try {
			arr.add(Double.valueOf(str.substring(si)).shortValue());
		} catch (final Exception e) {
		}
		return arr;
	}

	/**
	 * 将字符串转为long数组
	 * 
	 * @author XuWeijie
	 * @datetime 2015年10月29日_上午9:40:23
	 * @param txt 文本内容
	 * @param separator 分隔符
	 * @return long数组
	 */
	public static long[] toLongArray(final String txt, final String separator) {
		final List<Long> tl = StringTools.splitToLongArray(txt, separator);
		final long[] bak = new long[tl.size()];
		for (int i = tl.size() - 1; i >= 0; i--) {
			bak[i] = tl.get(i).longValue();
		}
		return bak;
	}

	/**
	 * 将字符串转为Long数组
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 下午1:58:17
	 * @param txt 文本内容
	 * @param separator 分隔符
	 * @return Long数组
	 */
	public static Long[] toLongObjArray(final String txt, final String separator) {
		final List<Long> tl = StringTools.splitToLongArray(txt, separator);
		return tl.toArray(new Long[tl.size()]);
	}

	/**
	 * 最小化消耗来替代String.split的方法<br />
	 * 直接获取到Long列表<br />
	 * 
	 * @author XuWeijie
	 * @datetime 2016年1月8日_下午3:06:55
	 * @param str 准备做分割处理的字串
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return Long列表
	 */
	public static List<Long> splitToLongArray(final String str, final String splitFlag) {
		final List<Long> arr = new ArrayList<>();
		// start index
		int si = 0;
		// end index
		int ei = 0;
		while (true) {
			ei = str.indexOf(splitFlag, si);
			if (ei == -1) {
				// 已经结束
				break;
			}
			if (ei != si) {
				try {
					arr.add(Double.valueOf(str.substring(si, ei)).longValue());
				} catch (final Exception e) {
				}
			}
			si = ei + splitFlag.length();
		}
		// 增加最后一段
		try {
			arr.add(Double.valueOf(str.substring(si)).longValue());
		} catch (final Exception e) {
		}
		return arr;
	}

	/**
	 * 将字符串转为Double数组
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月21日 下午2:36:53
	 * @param txt 文本内容
	 * @param separator 分隔符
	 * @return Double数组
	 */
	public static Double[] toDoubleObjArray(final String txt, final String separator) {
		final List<Double> tl = StringTools.splitToDoubleArray(txt, separator);
		return tl.toArray(new Double[tl.size()]);
	}

	/**
	 * 最小化消耗来替代String.split的方法<br />
	 * 直接获取到Double列表<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月21日 下午2:36:53
	 * @param str 准备做分割处理的字串
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return Double列表
	 */
	public static List<Double> splitToDoubleArray(final String str, final String splitFlag) {
		final List<Double> arr = new ArrayList<>();
		// start index
		int si = 0;
		// end index
		int ei = 0;
		while (true) {
			ei = str.indexOf(splitFlag, si);
			if (ei == -1) {
				// 已经结束
				break;
			}
			if (ei != si) {
				try {
					arr.add(Double.valueOf(str.substring(si, ei)).doubleValue());
				} catch (final Exception e) {
				}
			}
			si = ei + splitFlag.length();
		}
		// 增加最后一段
		try {
			arr.add(Double.valueOf(str.substring(si)).doubleValue());
		} catch (final Exception e) {
		}
		return arr;
	}

	/**
	 * 将字符串转为Float数组
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月21日 下午2:36:53
	 * @param txt 文本内容
	 * @param separator 分隔符
	 * @return Float数组
	 */
	public static Float[] toFloatObjArray(final String txt, final String separator) {
		final List<Float> tl = StringTools.splitToFloatArray(txt, separator);
		return tl.toArray(new Float[tl.size()]);
	}

	/**
	 * 最小化消耗来替代String.split的方法<br />
	 * 直接获取到Float列表<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月21日 下午2:36:53
	 * @param str 准备做分割处理的字串
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return Float列表
	 */
	public static List<Float> splitToFloatArray(final String str, final String splitFlag) {
		final List<Float> arr = new ArrayList<>();
		// start index
		int si = 0;
		// end index
		int ei = 0;
		while (true) {
			ei = str.indexOf(splitFlag, si);
			if (ei == -1) {
				// 已经结束
				break;
			}
			if (ei != si) {
				try {
					arr.add(Double.valueOf(str.substring(si, ei)).floatValue());
				} catch (final Exception e) {
				}
			}
			si = ei + splitFlag.length();
		}
		// 增加最后一段
		try {
			arr.add(Double.valueOf(str.substring(si)).floatValue());
		} catch (final Exception e) {
		}
		return arr;
	}

	/**
	 * 直接获取，通过分隔符处理后的部分的，目标位置的内容<br />
	 * 这里不会将零字长字串过滤掉，因为占位是不能被跳过的<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime Aug 8, 2014 10:15:57 AM
	 * @param str 准备做分割处理的字串
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @param index 目标索引位置，从0开始计算
	 * @return 目标索引位置的字串内容
	 */
	public static String splitInd(final String str, final String splitFlag, final int index) {
		// start index
		int si = 0;
		// end index
		int ei = 0;
		// 当前索引位置
		int ind = 0;
		while (true) {
			ei = str.indexOf(splitFlag, si);
			if (ei == -1) {
				// 已经结束
				break;
			}
			if (ind == index) {
				return str.substring(si, ei);
			}
			si = ei + splitFlag.length();
			ind++;
		}
		if (ind == index) {
			// 最后一个的情况
			return str.substring(si);
		}
		// 不存在目标位置的字串
		return null;
	}

	/**
	 * 将list组合为String
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月24日_下午4:05:56
	 * @param ls 字符串列表
	 * @param splitFlag 间隔附
	 * @return 组合的字串
	 */
	public static String listToString(final List<String> ls, final String splitFlag) {
		final StringBuilder sb = new StringBuilder();
		final boolean has = !((null == splitFlag) || (splitFlag.length() == 0));
		boolean isFirst = true;
		for (final String s : ls) {
			if (isFirst) {
				isFirst = false;
			} else {
				if (has) {
					sb.append(splitFlag);
				}
			}
			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * 对string进行指定单char补位
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月7日 下午8:14:25
	 * @param targetValue 目标值
	 * @param len 目标补到长度
	 * @param c 补位用char
	 * @return 被补位后的字串
	 */
	public static String paddingString(final String targetValue, final int len, final char c) {
		if (targetValue.length() >= len) {
			// 已经达到目标长度，直接返回
			return targetValue;
		}
		final char[] pd = new char[len - targetValue.length()];
		for (int i = pd.length - 1; i >= 0; i--) {
			pd[i] = c;
		}
		final StringBuilder sb = new StringBuilder(len);
		sb.append(pd).append(targetValue);
		return sb.toString();
	}

	/**
	 * 将int转为String，根据目标长度，不足则补零
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月7日 下午6:30:52
	 * @param targetValue 目标数值
	 * @param targetLength 目标长度，不能超过11，如果超过11则按照11计算
	 * @return 返回后的值
	 */
	public static String intToString(final int targetValue, int targetLength) {
		final String str = Integer.toString(targetValue);
		if (targetLength <= 0) {
			// 不做补零操作，直接返回
			return str;
		}
		if (targetLength > 11) {
			targetLength = 11;
		}
		if (targetLength <= str.length()) {
			// 不做补零操作，直接返回
			return str;
		}
		final StringBuilder sb = new StringBuilder(targetLength);
		switch (targetLength - str.length()) {
		case 1:
			sb.append('0');
			break;
		case 2:
			sb.append("00");
			break;
		case 3:
			sb.append("000");
			break;
		case 4:
			sb.append("0000");
			break;
		case 5:
			sb.append("00000");
			break;
		case 6:
			sb.append("000000");
			break;
		case 7:
			sb.append("0000000");
			break;
		case 8:
			sb.append("00000000");
			break;
		case 9:
			sb.append("000000000");
			break;
		case 10:
			sb.append("0000000000");
			break;
		case 11:
			sb.append("00000000000");
			break;
		}
		sb.append(str);
		return sb.toString();
	}

	/**
	 * 如果字段尾部为目标内容，则裁掉，并将首字符小写
	 * 
	 * @author 听风紫竹
	 * @datetime 2015年7月12日_下午8:00:02
	 * @param tarStr 目标字符串
	 * @param suffix 后缀
	 * @return 被处理后的字串
	 */
	public static String cutTail(final String tarStr, final String suffix) {
		return StringTools.cutTail(tarStr, suffix, true);
	}

	/**
	 * 如果字段尾部为目标内容，则裁掉，并判定是否需要将首字符小写
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午7:21:48
	 * @param tarStr 目标字符串
	 * @param suffix 后缀
	 * @param lowerFirstCharacter 是否首字符小写
	 * @return 被处理后的字串
	 */
	public static String cutTail(String tarStr, final String suffix, final boolean lowerFirstCharacter) {
		final boolean sameEnd = tarStr.endsWith(suffix);
		if (sameEnd) {
			// 需要先切割再返回
			tarStr = tarStr.substring(0, tarStr.lastIndexOf(suffix));
		}
		if (lowerFirstCharacter) {
			return StringTools.toLowerFirstCharacter(tarStr);
		} else {
			return tarStr;
		}
	}

	/**
	 * 进行首字符转小写操作
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月18日_下午12:02:46
	 * @param tarStr 目标字串
	 * @return 首字符小写后的字串
	 */
	private static String toLowerFirstCharacter(final String tarStr) {
		final char hc = tarStr.charAt(0);
		if ((hc >= 65) && (hc <= 90)) {
			// 需要首字符小写
			final StringBuilder sb = new StringBuilder(tarStr.length());
			sb.append((char) (hc + 32));
			sb.append(tarStr.substring(1));
			return sb.toString();
		} else {
			// 直接返回
			return tarStr;
		}
	}

	/**
	 * 替换四字节的字符
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月9日_下午4:37:53
	 * @param content 文本内容
	 * @return 被替换后的内容，null直接被返回；
	 */
	public static String replaceFourChar(final String content) {
		return StringTools.replaceFourChar(content, "");
	}

	/**
	 * 替换四字节的字符
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月9日_下午4:36:45
	 * @param txt 文本内容
	 * @param tar 目标被替换的内容
	 * @return 被替换后的内容，null直接被返回；
	 */
	public static String replaceFourChar(String txt, final String tar) {
		if (null == txt) {
			return null;
		}
		final byte[] conbyte = txt.getBytes();
		for (int i = 0; i < conbyte.length; i++) {
			if ((conbyte[i] & 0xF8) == 0xF0) {
				conbyte[i++] = 0x30;
				conbyte[i++] = 0x30;
				conbyte[i++] = 0x30;
				conbyte[i] = 0x30;
			}
		}
		txt = new String(conbyte);
		return txt.replaceAll("0000", tar);
	}

	/**
	 * 替换四字节的字符（新未验证）
	 * 
	 * @author tfzzh
	 * @dateTime 2016年8月25日 下午2:49:26
	 * @param txt 文本内容
	 * @param tar 目标被替换的内容
	 * @return 被替换后的内容，null直接被返回；
	 */
	public static String replaceFourCharNew(final String txt, final String tar) {
		if (null == txt) {
			return null;
		}
		final byte[] conbyte = txt.getBytes();
		int j = 0;
		for (int i = 0; i < conbyte.length; i++) {
			if ((conbyte[i] & 0xF8) == 0xF0) {
				i += 3;
				// conbyte[i] = 0x30;
				// conbyte[i + 1] = 0x30;
				// conbyte[i + 2] = 0x30;
				// conbyte[i + 3] = 0x30;
			} else {
				conbyte[j++] = conbyte[i];
			}
		}
		if (j == 0) {
			return "";
		}
		if (j == txt.length()) {
			return txt;
		} else {
			final byte[] nc = new byte[j];
			System.arraycopy(nc, 0, conbyte, 0, conbyte.length);
			return new String(conbyte);
		}
	}

	/**
	 * 得到int值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月14日_下午6:31:02
	 * @param val 目标内容
	 * @return int值
	 */
	public static Integer getInteger(final String val) {
		if ((null == val) || (val.length() == 0)) {
			return null;
		}
		return Double.valueOf(val).intValue();
	}

	/**
	 * 得到short值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月14日_下午6:31:02
	 * @param val 目标内容
	 * @return short值
	 */
	public static Short getShort(final String val) {
		if ((null == val) || (val.length() == 0)) {
			return null;
		}
		return Double.valueOf(val).shortValue();
	}

	/**
	 * 得到long值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月14日_下午6:31:02
	 * @param val 目标内容
	 * @return long值
	 */
	public static Long getLong(final String val) {
		if ((null == val) || (val.length() == 0)) {
			return null;
		}
		try {
			return Double.valueOf(val).longValue();
		} catch (Exception e) {
			return DateFormat.formatStringToTime(val);
		}
	}

	/**
	 * 得到float值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月14日_下午6:31:02
	 * @param val 目标内容
	 * @return float值
	 */
	public static Float getFloat(final String val) {
		if ((null == val) || (val.length() == 0)) {
			return null;
		}
		return Double.valueOf(val).floatValue();
	}

	/**
	 * 得到double值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月14日_下午6:31:02
	 * @param val 目标内容
	 * @return double值
	 */
	public static Double getDouble(final String val) {
		if ((null == val) || (val.length() == 0)) {
			return null;
		}
		return Double.valueOf(val);
	}

	/**
	 * 得到boolean值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月14日_下午6:31:02
	 * @param val 目标内容
	 * @return boolean值
	 */
	public static Boolean getBoolean(final String val) {
		if ((null == val) || (val.length() == 0)) {
			return null;
		}
		return Boolean.valueOf(val);
	}

	/**
	 * 得到boolean值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月14日_下午6:31:02
	 * @param val 目标内容
	 * @return boolean值
	 */
	public static Character getChar(final String val) {
		if ((null == val) || (val.length() == 0)) {
			return null;
		}
		return val.charAt(0);
	}

	/**
	 * 得到boolean值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月14日_下午6:31:02
	 * @param val 目标内容
	 * @return boolean值
	 */
	public static Byte getByte(final String val) {
		if ((null == val) || (val.length() == 0)) {
			return null;
		}
		return Double.valueOf(val).byteValue();
	}

	/**
	 * 判定string是否为null或""
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月21日 上午11:38:37
	 * @param str 目标字串
	 * @return true，是null或者""
	 */
	public static boolean isNullOrEmpty(final String str) {
		return (null == str) || (str.length() == 0);
	}

	/**
	 * 得到一个非null的string
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月21日 上午11:38:36
	 * @param str 目标字串
	 * @return 如果是null，则返回""，其他返回自身
	 */
	public static String getStringNoNull(final String str) {
		return null == str ? StringTools.empty : str;
	}

	/**
	 * 得到目标string的字符位长度<br />
	 * 一个中文为两个字符位<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2020年8月14日 下午12:38:57
	 * @param str 目标string串
	 * @return 得到的字符位长度
	 */
	public static int getStringCharLenght(final String str) {
		int len = str.length();
		for (int i = 0; i < str.length(); i++) {
			final int ascii = str.charAt(i);
			if ((ascii >= 0) && (ascii <= 255)) {
				continue;
			} else {
				len++;
			}
		}
		return len;
	}
}
