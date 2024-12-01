/**
 * @author Weijie Xu
 * @dateTime 2015年2月7日 下午1:01:48
 */
package com.tfzzh.socket.webservice;

import java.security.MessageDigest;

import com.tfzzh.socket.ClientTypeEnum;

/**
 * 与Flash的安全沙箱反馈信息
 * 
 * @author Weijie Xu
 * @dateTime 2015年2月7日 下午1:16:23
 */
public class WebSocketHandshake extends Handshake {

	/**
	 * 安全码，此码不可能为null
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午5:01:55
	 */
	private final String secKeyAccept;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午1:16:23
	 * @param secKeyAccept 安全码
	 */
	public WebSocketHandshake(final String secKeyAccept) {
		super(ClientTypeEnum.WebSocket);
		this.secKeyAccept = secKeyAccept;
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
		if (null == this.secKeyAccept) {
			return "HTTP/1.1 101 WebSocket Protocol Handshake\r\nUpgrade: WebSocket\r\nConnection: Upgrade\r\n\r\n";
		} else {
			return "HTTP/1.1 101 Switching Protocols\r\nUpgrade: websocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept: " + this.secKeyAccept + "\r\n\r\n";
		}
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
	 * @dateTime 2015年2月7日 下午5:30:53
	 * @param txt 验证的信息
	 * @return 成功的握手信息；<br />
	 *         null，未能成功；<br />
	 */
	public static WebSocketHandshake toHandshake(final String txt) {
		{ // 一些基础验证
			// get host....
			if ((txt.indexOf("Host:") == -1) || (txt.indexOf("GET") == -1) || (txt.indexOf("HTTP") == -1)) {
				return null;
			}
		}
		final String tSecKey = WebSocketHandshake.getContent(txt, "Sec-WebSocket-Key:", "\r\n");
		if (null != tSecKey) {
			final String tSecKeyAccept = WebSocketHandshake.calcSecKeyAccept(tSecKey);
			if (null != tSecKeyAccept) {
				return new WebSocketHandshake(tSecKeyAccept);
			} else {
				// 错误情况
				return null;
			}
		} else {
			// 不需要验证情况
			return new WebSocketHandshake(null);
		}
	}

	/**
	 * 得到指定内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年1月27日 上午11:21:39
	 * @param txt 实际文本
	 * @param head 头内容
	 * @param tail 尾内容
	 * @return 得到的内容串
	 */
	private static String getContent(final String txt, final String head, final String tail) {
		// 首位索引
		int strInd = txt.indexOf(head);
		if (strInd < 0) {
			// 不存在
			return null;
		}
		strInd += head.length();
		// 末位索引
		final int endInd = txt.indexOf(tail, strInd);
		if (endInd < 0) {
			return null;
		}
		final String con = txt.substring(strInd, endInd);
		// 去掉前后空格
		return con.trim();
	}

	/**
	 * 针对WebSocket的握手验证码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年1月27日 下午2:02:03
	 * @param lSecKey 来自于请求端的安全码
	 * @return 处理后的返回码
	 */
	private static String calcSecKeyAccept(final String lSecKey) {
		if (null == lSecKey) {
			return null;
		}
		try {
			final MessageDigest md = MessageDigest.getInstance("SHA-1");
			final byte[] lBufSource = (lSecKey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8");
			final byte[] lBufTarget = md.digest(lBufSource);
			return WebSocketHandshake.base64Encode(lBufTarget);
		} catch (final Exception lEx) {
		}
		return null;
	}

	/**
	 * base64底数数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年1月27日 下午2:11:20
	 */
	private static transient char[] b64d = null;

	/**
	 * 初始化base64底数数据<br />
	 * 照搬他人无修改<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年1月27日 下午2:11:21
	 * @return base64底数数据
	 */
	private static char[] initBase64DataMap() {
		final char[] bs = new char[64];
		int i = 0;
		for (char c = 'A'; c <= 'Z'; c++) {
			bs[i++] = c;
		}
		for (char c = 'a'; c <= 'z'; c++) {
			bs[i++] = c;
		}
		for (char c = '0'; c <= '9'; c++) {
			bs[i++] = c;
		}
		bs[i++] = '+';
		bs[i++] = '/';
		return bs;
	}

	/**
	 * 进行Base64处理<br />
	 * 照搬他人无修改<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年1月27日 下午2:08:08
	 * @param in 目标字节数组
	 * @return 编码后的数组
	 */
	private static String base64Encode(final byte[] in) {
		final int iLen = in.length;
		final int oDataLen = ((iLen * 4) + 2) / 3;
		final char[] out = new char[((iLen + 2) / 3) * 4];
		int ip = 0;
		int op = 0;
		int i0;
		int i1;
		int i2;
		int o0;
		int o1;
		int o2;
		int o3;
		if (null == WebSocketHandshake.b64d) {
			WebSocketHandshake.b64d = WebSocketHandshake.initBase64DataMap();
		}
		while (ip < iLen) {
			i0 = in[ip++] & 0xff;
			i1 = ip < iLen ? in[ip++] & 0xff : 0;
			i2 = ip < iLen ? in[ip++] & 0xff : 0;
			o0 = i0 >>> 2;
			o1 = ((i0 & 3) << 4) | (i1 >>> 4);
			o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
			o3 = i2 & 0x3F;
			out[op++] = WebSocketHandshake.b64d[o0];
			out[op++] = WebSocketHandshake.b64d[o1];
			out[op] = op++ < oDataLen ? WebSocketHandshake.b64d[o2] : '=';
			// op++;
			out[op] = op++ < oDataLen ? WebSocketHandshake.b64d[o3] : '=';
			// op++;
		}
		return new String(out);
	}
}
