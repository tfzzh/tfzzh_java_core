/**
 * @author Weijie Xu
 * @dateTime 2014-3-20 下午5:13:35
 */
package com.tfzzh.exception;

/**
 * 配置文件配置错误
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-20 下午5:13:35
 */
public class ConfigurationException extends NestedRuntimeException {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:07:12
	 */
	private static final long serialVersionUID = -5620511554110468965L;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午5:13:39
	 * @param msg 消息信息
	 */
	public ConfigurationException(final String msg) {
		super(msg);
	}
}
