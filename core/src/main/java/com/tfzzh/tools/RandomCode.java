/**
 * @author Weijie Xu
 * @dateTime 2014年6月24日 下午5:24:57
 */
package com.tfzzh.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 随机码计算器
 * 
 * @author Weijie Xu
 * @dateTime 2014年6月24日 下午5:24:57
 */
public class RandomCode {

	/**
	 * 小写字符
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月24日 下午7:59:01
	 */
	public final static int CODE_TYPE_LOWERCASE = 1;

	/**
	 * 大写字符
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月24日 下午7:59:02
	 */
	public final static int CODE_TYPE_UPPERCASE = 2;

	/**
	 * 数值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月24日 下午7:59:03
	 */
	public final static int CODE_TYPE_DIGITAL = 4;

	/**
	 * 小写字符，大写字符
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月24日 下午7:59:04
	 */
	public final static int CODE_TYPE_LOWERCASE_UPPERCASE = RandomCode.CODE_TYPE_LOWERCASE | RandomCode.CODE_TYPE_UPPERCASE;

	/**
	 * 小写字符，数值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月24日 下午7:59:05
	 */
	public final static int CODE_TYPE_LOWERCASE_DIGITAL = RandomCode.CODE_TYPE_LOWERCASE | RandomCode.CODE_TYPE_DIGITAL;

	/**
	 * 大写字符，数值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月24日 下午7:59:06
	 */
	public final static int CODE_TYPE_UPPERCASE_DIGITAL = RandomCode.CODE_TYPE_UPPERCASE | RandomCode.CODE_TYPE_DIGITAL;

	/**
	 * 小写字符，大写字符，数值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月24日 下午7:59:08
	 */
	public final static int CODE_TYPE_LOWERCASE_UPPERCASE_DIGITAL = RandomCode.CODE_TYPE_LOWERCASE | RandomCode.CODE_TYPE_UPPERCASE | RandomCode.CODE_TYPE_DIGITAL;

	/**
	 * 对象列表
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月9日_下午12:48:20
	 */
	private final static RandomCode[] rca = new RandomCode[RandomCode.CODE_TYPE_LOWERCASE_UPPERCASE_DIGITAL + 1];

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年6月24日 下午5:50:52
	 */
	private char[] basicCode;

	/**
	 * 当前的类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月24日 下午7:42:36
	 */
	private final int type;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年6月24日 下午5:57:23
	 */
	private final char[][] basicString = { { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' }, { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' }, { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' } };

	/**
	 * 随机数控制器
	 * 
	 * @author Weijie Xu
	 * @dateTime Sep 13, 2014 2:54:27 PM
	 */
	private final TfzzhRandom ran = TfzzhRandom.getInstance();

	// /**
	// * @author Weijie Xu
	// * @dateTime 2014年6月24日 下午5:50:54
	// */
	// public RandomCode() {
	// this.type = 1;
	// this.refreshBasicCode();
	// }
	/**
	 * @author Weijie Xu
	 * @dateTime 2014年6月24日 下午7:51:15
	 * @param type 类型值：1，字母小写；2，字母大写；4，数字；<br />
	 *           与运算方式<br />
	 */
	private RandomCode(final int type) {
		this.type = type;
		this.refreshBasicCode();
	}

	/**
	 * 得到默认的随机码生成器实例<br />
	 * 仅有小写字符的随机码<br />
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月9日_下午12:54:27
	 * @return 随机码生成器实例
	 */
	public static RandomCode getInstance() {
		return RandomCode.getInstance(RandomCode.CODE_TYPE_LOWERCASE);
	}

	/**
	 * 得到指定的随机码生成器实例
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月9日_下午12:53:49
	 * @param type 随机码类型
	 * @return 随机码生成器实例
	 */
	public static RandomCode getInstance(final int type) {
		if (type > RandomCode.CODE_TYPE_LOWERCASE_UPPERCASE_DIGITAL) {
			return null;
		}
		RandomCode rc = RandomCode.rca[type];
		if (null == rc) {
			rc = new RandomCode(type);
			RandomCode.rca[type] = rc;
		}
		return rc;
	}

	/**
	 * 得到随机码 默认为6位长度
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月24日 下午5:50:09
	 * @return 得到6位长度随机码
	 */
	public String getRandomCode() {
		return this.getRandomCode(6);
	}

	/**
	 * 得到指定长度随机码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月24日 下午5:50:11
	 * @param length 目标长度，不可小于1，不可大于128
	 * @return 得到定长随机码
	 */
	public String getRandomCode(int length) {
		final StringBuilder sb = new StringBuilder(length);
		while (--length >= 0) {
			sb.append(this.basicCode[this.ran.nextInt(this.basicCode.length)]);
		}
		return sb.toString();
	}

	/**
	 * 得到基础码类型<br />
	 * 类型，1，字母小写；2，字母大写；4，数字；<br />
	 * 与运算方式<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月24日 下午7:44:59
	 * @return 当前类型值
	 */
	public int getBasicCodeType() {
		return this.type;
	}

	/**
	 * 刷新基础码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月24日 下午5:57:43
	 */
	private void refreshBasicCode() {
		final List<Character> li = new ArrayList<>();
		if ((this.type & 1) == 1) {
			for (final char s : this.basicString[0]) {
				li.add(s);
			}
		}
		if ((this.type & 2) == 2) {
			for (final char s : this.basicString[1]) {
				li.add(s);
			}
		}
		if ((this.type & 4) == 4) {
			for (final char s : this.basicString[2]) {
				li.add(s);
			}
		}
		final char[] r = new char[li.size()];
		int i = 0;
		// final TfzzhRandom ran = TfzzhRandom.getInstance();
		while (li.size() > 0) {
			r[i++] = li.remove(this.ran.nextInt(li.size()));
		}
		this.basicCode = r;
	}
}
