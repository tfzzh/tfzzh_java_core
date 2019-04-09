/**
 * @author Weijie Xu
 * @dateTime 2015年2月7日 下午12:55:39
 */
package com.tfzzh.socket.webservice;

import com.tfzzh.socket.ClientTypeEnum;

/**
 * 握手的结果
 * 
 * @author Weijie Xu
 * @dateTime 2015年2月7日 下午12:55:39
 */
public abstract class Handshake {

	/**
	 * 客户端连接类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月6日 下午5:57:32
	 */
	private final ClientTypeEnum clientType;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午1:13:29
	 * @param clientType 客户端连接类型
	 */
	protected Handshake(final ClientTypeEnum clientType) {
		this.clientType = clientType;
	}

	/**
	 * 得到客户端连接类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午1:14:07
	 * @return 客户端连接类型
	 */
	public ClientTypeEnum getClientType() {
		return this.clientType;
	}

	/**
	 * 是否握手OK
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午12:56:47
	 * @return true，握手成功；<br />
	 *         false，握手失败；<br />
	 */
	public abstract boolean handshakeOk();

	/**
	 * 需要返回的信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午12:56:48
	 * @return 待返回的信息
	 */
	public abstract String backMsg();

	/**
	 * 得到需要返回的握手用信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月4日 下午3:54:21
	 * @return 需要返回的握手用信息
	 */
	public abstract Handshake getBackHandshake();
}
