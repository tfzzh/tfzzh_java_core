/**
 * @author Xu Weijie
 * @datetime 2017年9月26日_下午3:49:26
 */
package com.tfzzh.core.validate.element;

import com.tfzzh.core.validate.ValidateErrorInfo;

/**
 * 基础验证元素
 * 
 * @author Xu Weijie
 * @datetime 2017年9月26日_下午3:49:26
 */
public abstract class ValidateElement {

	/**
	 * 对目标进行验证
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午3:50:01
	 * @param name 目标名字，更多为了返回消息而存在
	 * @param val 目标值
	 * @return null，验证通过；<br />
	 *         否则为问题信息<br />
	 */
	public abstract ValidateErrorInfo validate(String name, Object val);
}
