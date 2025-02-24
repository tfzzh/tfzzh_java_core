/**
 * @author Weijie Xu
 * @dateTime 2014-3-20 下午5:13:35
 */
package com.tfzzh.exception;

/**
 * 游戏中设置错误或游戏中设置错误</br>
 * 不好明确辨别具体是哪里错误的情况</br>
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-20 下午5:13:35
 */
public class ConfigurationOrInstallException extends NestedRuntimeException {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:06:57
	 */
	private static final long serialVersionUID = 661220185961788880L;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午5:13:39
	 * @param msg 消息信息
	 */
	public ConfigurationOrInstallException(final String msg) {
		super(msg);
	}
}
