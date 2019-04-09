/**
 * @author Weijie Xu
 * @dateTime 2014年4月11日 下午7:57:04
 */
package com.tfzzh.socket.tools;

/**
 * 连接状态
 * 
 * @author Weijie Xu
 * @dateTime 2014年4月11日 下午7:57:04
 */
public enum ConnectionStatusEnum {
	/**
	 * 顺畅，最好
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月11日 下午8:16:34
	 */
	Smooth,
	/**
	 * 安全，次级
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月11日 下午7:57:57
	 */
	Security,
	/**
	 * 警告，危险
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月11日 下午7:57:59
	 */
	Warning,
	/**
	 * 超时
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月11日 下午7:58:00
	 */
	Timeout;

	/**
	 * 得到连接状态
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月11日 下午8:17:15
	 * @param time 差值时间
	 * @return 当前的状态
	 */
	public static ConnectionStatusEnum getConnectionStatus(final long time) {
		if (time < SocketConstants.KEEP_STATUS_SECURITY) {
			return ConnectionStatusEnum.Smooth;
		} else if (time < SocketConstants.KEEP_STATUS_WARNING) {
			return ConnectionStatusEnum.Security;
		} else if (time < SocketConstants.KEEP_STATUS_TIMEOUT) {
			return ConnectionStatusEnum.Warning;
		} else {
			return ConnectionStatusEnum.Timeout;
		}
	}
}
