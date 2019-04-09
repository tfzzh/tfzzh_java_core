/**
 * @author Weijie Xu
 * @dateTime 2015年2月7日 下午1:01:48
 */
package com.tfzzh.socket.webservice;

import com.tfzzh.socket.ClientTypeEnum;
import com.tfzzh.tools.Constants;

/**
 * 与Flash的安全沙箱反馈信息
 * 
 * @author Weijie Xu
 * @dateTime 2015年2月7日 下午1:16:23
 */
public class FlashHandshake extends Handshake {

	/**
	 * 被返回的信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午1:19:02
	 */
	private static transient String backMsg = null;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午1:16:23
	 */
	private FlashHandshake() {
		super(ClientTypeEnum.Flash);
	}

	/**
	 * 得到返回的信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午1:19:42
	 * @return 返回的信息
	 */
	private static String getBackMsg() {
		return "<?xml version=\"1.0\" encoding=\"" + Constants.SYSTEM_CODE + "\"?><cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy>" + '\0';
	}

	/**
	 * 是否握手OK
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午1:16:23
	 * @return true，握手成功；<br />
	 *         false，握手失败；<br />
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
	 * @dateTime 2015年2月7日 下午1:16:23
	 * @return 待返回的信息
	 * @see com.tfzzh.socket.webservice.Handshake#backMsg()
	 */
	@Override
	public String backMsg() {
		if (null == FlashHandshake.backMsg) {
			FlashHandshake.backMsg = FlashHandshake.getBackMsg();
		}
		return FlashHandshake.backMsg;
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
		return this;
	}

	/**
	 * 进行握手操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午3:51:56
	 * @param txt 验证的信息
	 * @return 成功的握手信息；<br />
	 *         null，未能成功；<br />
	 */
	public static FlashHandshake toHandshake(final String txt) {
		if (txt.startsWith("<policy-file-request")) {
			// 握手成功
			return new FlashHandshake();
		} else {
			return null;
		}
	}
}
