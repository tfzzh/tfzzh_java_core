/**
 * @author Weijie Xu
 * @dateTime 2015年2月7日 下午1:01:48
 */
package com.tfzzh.socket.webservice;

import com.tfzzh.socket.ClientTypeEnum;

/**
 * 默认的握手反馈
 * 
 * @author Weijie Xu
 * @dateTime 2015年2月7日 下午1:01:48
 */
public class DefaultHandshake extends Handshake {

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午1:14:33
	 */
	public DefaultHandshake() {
		super(ClientTypeEnum.Normal);
	}

	/**
	 * 是否握手OK
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午1:01:48
	 * @see com.tfzzh.socket.webservice.Handshake#handshakeOk()
	 */
	@Override
	public boolean handshakeOk() {
		return true;
	}

	/**
	 * 需要返回的信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午1:01:48
	 * @return 待返回的信息
	 * @see com.tfzzh.socket.webservice.Handshake#backMsg()
	 */
	@Override
	public String backMsg() {
		return null;
	}

	/**
	 * 得到需要返回的握手用信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月4日 下午3:54:49
	 * @return 需要返回的握手用信息
	 * @see com.tfzzh.socket.webservice.Handshake#getBackHandshake()
	 */
	@Override
	public Handshake getBackHandshake() {
		return null;
	}
}
