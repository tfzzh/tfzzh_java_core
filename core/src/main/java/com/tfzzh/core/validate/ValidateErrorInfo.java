/**
 * @author Xu Weijie
 * @datetime 2017年9月27日_上午9:50:29
 */
package com.tfzzh.core.validate;

/**
 * 验证错误信息
 * 
 * @author Xu Weijie
 * @datetime 2017年9月27日_上午9:50:29
 */
public class ValidateErrorInfo {

	/**
	 * 错误信息的名字</br>
	 * 一般用于匹配或说明位置</br>
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午9:59:40
	 */
	private final String name;

	/**
	 * 错误信息内容类型名
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午10:36:27
	 */
	private final String type;

	/**
	 * 错误信息内容类型对应的值
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午10:36:27
	 */
	private final String value;

	/**
	 * 错误信息的内容</br>
	 * 一般用于标明内容重点</br>
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午9:59:41
	 */
	private final String info;

	/**
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午9:59:48
	 * @param name 错误信息的名字
	 * @param type 错误信息内容类型名
	 * @param value 错误信息内容类型对应的值
	 * @param info 错误信息的内容
	 */
	public ValidateErrorInfo(final String name, final String type, final String value, final String info) {
		this.name = name;
		this.type = type;
		this.value = value;
		this.info = info;
	}

	/**
	 * 得到错误信息的名字</br>
	 * 一般用于匹配或说明位置</br>
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午9:51:13
	 * @return 错误信息的名字
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 得到错误信息内容类型名
	 *
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午10:38:23
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * 得到错误信息内容类型对应的值
	 *
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午10:38:23
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * 得到错误信息的内容</br>
	 * 一般用于标明内容重点</br>
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_上午9:51:13
	 * @return 错误信息的内容
	 */
	public String getInfo() {
		return this.info;
	}
}
