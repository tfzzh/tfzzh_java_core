/**
 * @author Xu Weijie
 * @datetime 2017年9月26日_下午6:08:37
 */
package com.tfzzh.core.validate.element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tfzzh.core.validate.ValidateErrorInfo;
import com.tfzzh.core.validate.ValidateErrorTypeConstants;

/**
 * 正则表达式验证
 * 
 * @author Xu Weijie
 * @datetime 2017年9月26日_下午6:08:37
 */
public class RegularValidateElement extends ValidateElement {

	/**
	 * 规则名字
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午11:10:30
	 */
	private String name = null;

	/**
	 * 规则样式
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午6:12:25
	 */
	private Pattern pattern = null;

	/**
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午6:12:24
	 * @param name 规则名字
	 * @param reference 规则列表
	 */
	public RegularValidateElement(final String name, final String[] reference) {
		for (final String ref : reference) {
			if (ref.length() > 0) {
				this.pattern = Pattern.compile(ref);
				this.name = name;
				return;
			}
		}
	}

	/**
	 * 验证目标值是否在范围内
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午6:14:45
	 * @param val 目标值
	 * @return true，在范围内
	 * @see com.tfzzh.core.validate.element.ValidateElement#validate(java.lang.String, java.lang.Object)
	 */
	@Override
	public ValidateErrorInfo validate(final String name, final Object val) {
		if (null == this.pattern) {
			// 无规则，所以算是通过的
			return null;
		}
		if (!(val instanceof CharSequence)) {
			return new ValidateErrorInfo(name, ValidateErrorTypeConstants.ERR_TYPE, null, null);
		}
		final Matcher matcher = this.pattern.matcher((CharSequence) val);
		if (matcher.matches()) {
			// 通过的情况
			return null;
		} else {
			return new ValidateErrorInfo(name, ValidateErrorTypeConstants.REGULAR, this.name, null);
		}
	}
	// public static void main(String[] args) {
	// String[] s = { "^(\\w)+([-_.]*(\\w)+)*@(\\w)+([-_.]*(\\w)+)*((.\\w+)+)$" };
	// RegularValidateElement rv = new RegularValidateElement("email", s);
	// System.out.println(rv.validate("ha", "a_a.-b.a@a-ds.c_.a.a34343"));
	// }
}
