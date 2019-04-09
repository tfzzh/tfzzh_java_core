/**
 * @author Xu Weijie
 * @datetime 2017年9月26日_下午6:29:59
 */
package com.tfzzh.core.validate.element;

import com.tfzzh.core.control.tools.ManagerMap;
import com.tfzzh.core.validate.CustomValidate;
import com.tfzzh.core.validate.ValidateErrorInfo;

/**
 * @author Xu Weijie
 * @datetime 2017年9月26日_下午6:29:59
 */
public class CustomValidateElement extends ValidateElement {

	/**
	 * 规则处理名
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午6:30:35
	 */
	private final String name;

	/**
	 * 验证用参考值集合
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午6:30:37
	 */
	private final String[] reference;

	/**
	 * 自定验证的逻辑实现
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午6:33:23
	 */
	private static CustomValidate cv = null;

	/**
	 * 寻找的次数
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午6:33:54
	 */
	private static int findTimes = 0;

	/**
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午6:31:01
	 * @param name 规则处理名
	 * @param reference 验证用参考值集合
	 */
	public CustomValidateElement(final String name, final String[] reference) {
		this.name = name;
		this.reference = reference;
	}

	/**
	 * 验证目标值是否在范围内
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午6:29:59
	 * @param val 目标值
	 * @return true，在范围内
	 * @see com.tfzzh.core.validate.element.ValidateElement#validate(java.lang.String, java.lang.Object)
	 */
	@Override
	public ValidateErrorInfo validate(final String name, final Object val) {
		if (null == CustomValidateElement.cv) {
			if (CustomValidateElement.findTimes++ < 10) {
				CustomValidateElement.cv = (CustomValidate) ManagerMap.getInstance().getManager("customValidate");
				if (null != CustomValidateElement.cv) {
					return CustomValidateElement.cv.validate(this.name, this.reference, val);
				}
			}
			return null;
		}
		return CustomValidateElement.cv.validate(this.name, this.reference, val);
	}
}
