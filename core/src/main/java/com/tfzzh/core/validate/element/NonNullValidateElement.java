/**
 * @author Xu Weijie
 * @datetime 2017年9月26日_下午3:49:07
 */
package com.tfzzh.core.validate.element;

import com.tfzzh.core.validate.ValidateErrorInfo;
import com.tfzzh.core.validate.ValidateErrorTypeConstants;

/**
 * 非空验证
 * 
 * @author Xu Weijie
 * @datetime 2017年9月26日_下午3:49:07
 */
public class NonNullValidateElement extends ValidateElement {

	/**
	 * 对目标进行验证
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午3:51:11
	 * @param val 目标值
	 * @return true，验证通过
	 * @see com.tfzzh.core.validate.element.ValidateElement#validate(java.lang.String, java.lang.Object)
	 */
	@Override
	public ValidateErrorInfo validate(final String name, final Object val) {
		if (null != val) {
			// 是非空
			if (val instanceof CharSequence) {
				// 是字串类型，还需要判定长度
				if (((CharSequence) val).length() == 0) {
					return new ValidateErrorInfo(name, ValidateErrorTypeConstants.IS_EMPTY, null, null);
				}
			}
			return null;
		} else {
			return new ValidateErrorInfo(name, ValidateErrorTypeConstants.IS_EMPTY, null, null);
		}
	}
}
