/**
 * @author Xu Weijie
 * @datetime 2017年9月26日_下午3:04:23
 */
package com.tfzzh.core.validate;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证信息反馈<br />
 * 默认对象，可被继承，建议继承处理<br />
 * 
 * @author Xu Weijie
 * @datetime 2017年9月26日_下午3:04:23
 */
public class ValidateInfoBack {

	/**
	 * 问题消息列表<br />
	 * <目标名称,验证问题类型><br />
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午3:06:33
	 */
	private final Map<String, FieldValidateEnum> problemMsg = new HashMap<>();

	/**
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午3:07:55
	 */
	public ValidateInfoBack() {
	}

	/**
	 * 放入问题信息
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午3:09:00
	 * @param problemName 问题内容相关名称
	 * @param valiType 问题类型
	 */
	public void putProblemMsg(final String problemName, final FieldValidateEnum valiType) {
		this.problemMsg.put(problemName, valiType);
	}

	/**
	 * 是否存在问题
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午3:14:04
	 * @return true，存在问题
	 */
	public boolean hasProblem() {
		return this.problemMsg.size() > 0;
	}

	/**
	 * 得到问题消息列表<br />
	 * <目标名称,验证问题类型><br />
	 *
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午3:15:38
	 * @return the problemMsg
	 */
	public Map<String, FieldValidateEnum> getProblemMsg() {
		return this.problemMsg;
	}
}
