/**
 * @author Weijie Xu
 * @dateTime 2014年4月11日 下午7:59:35
 */
package com.tfzzh.socket.tools;

/**
 * 接口的常量类
 * 
 * @author Weijie Xu
 * @dateTime 2014年4月11日 下午7:59:35
 */
public interface SocketConstants {

	/**
	 * 安全范围界限
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月11日 下午8:05:55
	 */
	int KEEP_STATUS_SECURITY = Integer.parseInt(Messages.getString("SocketConstants.KEEP_STATUS_SECURITY"));

	/**
	 * 警告的范围界限
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月11日 下午8:05:57
	 */
	int KEEP_STATUS_WARNING = Integer.parseInt(Messages.getString("SocketConstants.KEEP_STATUS_WARNING"));

	/**
	 * 超时的范围界限
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月11日 下午8:06:44
	 */
	int KEEP_STATUS_TIMEOUT = Integer.parseInt(Messages.getString("SocketConstants.KEEP_STATUS_TIMEOUT"));

	/**
	 * websocket长度限制，0为无限制
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月8日_下午2:09:34
	 */
	int WEBSOKECT_CONTENT_LENGTH_MAX = Integer.parseInt(Messages.getString("SocketConstants.WEBSOKECT_CONTENT_LENGTH_MAX"));

	/**
	 * websocket拆分消息的消息码<br />
	 * 固定参数：消息索引号、消息总数量、当前消息索引位、字节流<br />
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月8日_下午2:47:57
	 */
	int WEBSOCKET_SECTION_CODE = Integer.parseInt(Messages.getString("SocketConstants.WEBSOCKET_SECTION_CODE"));
}
