/**
 * @author Xu Weijie
 * @datetime 2017年9月26日_下午4:07:21
 */
package com.tfzzh.core.validate.element;

import com.tfzzh.core.validate.ValidateErrorInfo;
import com.tfzzh.core.validate.ValidateErrorTypeConstants;

/**
 * 验证数值范围
 * 
 * @author Xu Weijie
 * @datetime 2017年9月26日_下午4:07:21
 */
public class DecimalRangeValidateElement extends ValidateElement {

	/**
	 * 最小值，如果null，则表示没有最小值
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午5:50:14
	 */
	private Double min;

	/**
	 * 是否需要等于最小值
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午5:50:14
	 */
	private boolean eqMin = false;

	/**
	 * 最大值，如果null，则表示没有最大值
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午5:50:14
	 */
	private Double max;

	/**
	 * 是否需要等于最大值
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午5:50:14
	 */
	private boolean eqMax = false;

	/**
	 * 错误消息内容
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午11:22:30
	 */
	private final String errVal;

	/**
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午5:50:14
	 * @param reference 规则列表
	 */
	public DecimalRangeValidateElement(final String[] reference) {
		for (final String ref : reference) {
			this.analysis(ref);
		}
		if (null != this.max) {
			if (null != this.min) {
				this.errVal = new StringBuilder().append(this.eqMin ? "[" : "(").append(this.min).append(',').append(this.max).append(this.eqMax ? "]" : ")").toString();
			} else {
				this.errVal = this.eqMax ? "<=" : "<" + this.max.toString();
			}
		} else {
			if (null != this.min) {
				this.errVal = this.eqMin ? ">=" : ">" + this.min.toString();
			} else {
				this.errVal = null;
			}
		}
	}

	/**
	 * 进行内容解析
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午5:50:14
	 * @param ref 待解析内容
	 */
	private void analysis(final String ref) {
		if (ref.charAt(0) == 62) {
			// 是大于>，相关小值
			if (ref.charAt(1) == 61) {
				this.eqMin = true;
				this.min = Double.valueOf(ref.substring(2));
			} else {
				this.min = Double.valueOf(ref.substring(1));
			}
		} else if (ref.charAt(0) == 60) {
			// 是小于<，相关大值
			if (ref.charAt(1) == 61) {
				this.eqMax = true;
				this.max = Double.valueOf(ref.substring(2));
			} else {
				this.max = Double.valueOf(ref.substring(1));
			}
		}
	}

	/**
	 * 验证目标值是否在范围内
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午5:50:14
	 * @param val 目标值
	 * @return true，在范围内
	 * @see com.tfzzh.core.validate.element.ValidateElement#validate(java.lang.String, java.lang.Object)
	 */
	@Override
	public ValidateErrorInfo validate(final String name, final Object val) {
		if (null == val) {
			// 空认为是通过的
			return null;
		}
		if (!(val instanceof Number)) {
			return new ValidateErrorInfo(name, ValidateErrorTypeConstants.NO_NUMBER, null, null);
		}
		final double l = ((Number) val).doubleValue();
		if (null != this.max) {
			if (this.eqMax) {
				if (l > this.max) {
					return new ValidateErrorInfo(name, ValidateErrorTypeConstants.ABOVE_RANGE, this.errVal, null);
				}
			} else {
				if (l >= this.max) {
					return new ValidateErrorInfo(name, ValidateErrorTypeConstants.ABOVE_RANGE, this.errVal, null);
				}
			}
		}
		if (null != this.min) {
			if (this.eqMin) {
				if (l < this.min) {
					return new ValidateErrorInfo(name, ValidateErrorTypeConstants.BELOW_RANGE, this.errVal, null);
				}
			} else {
				if (l <= this.min) {
					return new ValidateErrorInfo(name, ValidateErrorTypeConstants.BELOW_RANGE, this.errVal, null);
				}
			}
		}
		return null;
	}
}
