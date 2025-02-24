/**
 * @author Xu Weijie
 * @datetime 2017年9月26日_下午6:27:19
 */
package com.tfzzh.core.validate;

/**
 * 针对自定义验证的接口定义
 * 
 * @author Xu Weijie
 * @datetime 2017年9月26日_下午6:27:19
 */
public interface CustomValidate {

	/**
	 * 进行验证操作
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午6:57:43
	 * @param name 规则处理名
	 * @param ref 验证用参考值集合
	 * @param target 目标值
	 * @return null，表示验证成功；</br>
	 *         否则为问题信息</br>
	 */
	ValidateErrorInfo validate(String name, String[] ref, Object target);
}
