/**
 * @author Weijie Xu
 * @dateTime 2014-3-20 下午5:13:35
 */
package com.tfzzh.exception;

/**
 * 游戏中设置错误
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-20 下午5:13:35
 */
public class InstallException extends NestedRuntimeException {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:05:25
	 */
	private static final long serialVersionUID = -1873958471006066053L;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午5:13:39
	 * @param msg 消息信息
	 */
	public InstallException(final String msg) {
		super(msg);
	}
}
