/**
 * @author tfzzh
 * @dateTime 2023年10月9日 11:55:04
 */
package com.tfzzh.view.web.link;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.tfzzh.tools.Constants;
import com.tfzzh.view.web.link.OperateLink.OperateLinkInfo;

/**
 * 返回简单加密字节流信息
 * 
 * @author tfzzh
 * @dateTime 2023年10月9日 11:55:04
 */
public class BackSimpleEncryptionBean extends BaseBackOperationBean {

	/**
	 * 待返回数据
	 * 
	 * @author tfzzh
	 * @dateTime 2023年10月15日 13:53:19
	 */
	private final Object data;

	/**
	 * sessionKey
	 *
	 * @author tfzzh
	 * @dateTime 2023年10月9日 15:47:15
	 */
	private final String sessKey;

	/**
	 * 端请求时间
	 *
	 * @author tfzzh
	 * @dateTime 2023年10月9日 15:47:15
	 */
	private final long reqTime;

	/**
	 * 输出的内容
	 * 
	 * @author tfzzh
	 * @dateTime 2023年10月9日 15:47:06
	 */
	private byte[] out = null;

	/**
	 * @author tfzzh
	 * @dateTime 2024年7月10日 21:57:45
	 * @param sessKey 会话秘钥
	 * @param reqTime 端请求时间
	 */
	public BackSimpleEncryptionBean(final String sessKey, final long reqTime) {
		this(null, sessKey, reqTime);
	}

	/**
	 * @author tfzzh
	 * @dateTime 2023年10月9日 13:33:05
	 * @param data 待返回数据
	 * @param sessKey 会话秘钥
	 * @param reqTime 端请求时间
	 */
	public BackSimpleEncryptionBean(final Object data, final String sessKey, final long reqTime) {
		super(null, null);
		this.data = data;
		this.sessKey = sessKey;
		this.reqTime = reqTime;
	}

	/**
	 * 得到简单加密后的字节串
	 * 
	 * @author tfzzh
	 * @dateTime 2023年10月15日 01:35:59
	 * @param contBA 回传内容字节串集合
	 * @param skBA 秘钥字节串集合
	 * @return 简单加密后的字节串
	 */
	private byte[] encryptionByte(final byte[] contBA, final byte[] skBA) {
		final byte[] bak = new byte[contBA.length];
		int ski = 0;
		for (int i = 0; i < contBA.length; i++) {
			int v = contBA[i];
			final int sv = skBA[ski];
			if ((i % 2) == 0) {
				v += sv;
				if (v > 127) {
					// v = -128 + (v - 127);
					v -= 256;
				}
			} else {
				v -= sv;
				if (v < -128) {
					// v = 127 - (-128 - v);
					v += 256;
				}
			}
			if (++ski >= skBA.length) {
				ski = 0;
			}
			bak[i] = (byte) v;
		}
		return bak;
	}

	/**
	 * 对象转String之-强硬转换
	 * 
	 * @author tfzzh
	 * @dateTime 2023年10月9日 17:25:42
	 * @param obj 目标对象
	 * @return 转为的str
	 */
	private String objToString(final Object obj) {
		if (null == obj) {
			return Constants.EMPTY;
		} else if (obj instanceof final String s) {
			return s;
		} else if (obj instanceof final Number n) {
			return String.valueOf(n);
		} else if (obj instanceof final Map m) {
			final JSONObject jo = new JSONObject(m);
			return jo.toJSONString();
		} else if (obj instanceof final List l) {
			final JSONArray ja = new JSONArray(l);
			return ja.toJSONString();
		} else {
			return JSON.toJSONString(obj);
		}
	}

	/**
	 * 得到本次消息秘钥
	 * 
	 * @author tfzzh
	 * @dateTime 2023年10月9日 16:13:05
	 * @param sessKey 会话秘钥
	 * @param reqTime 端请求时间
	 * @return 本次消息秘钥
	 */
	private String getSecretKey(final String sessKey, final long reqTime) {
		int i1 = (int) (reqTime % 10);
		final int i2 = (int) ((reqTime % 100) / 10);
		final int i3 = (int) ((reqTime % 1000) / 100);
		// System.out.println(MessageFormat.format(" reqTime s1: [{0}]\ts2: [{1}]\ts3: [{2}]", i1, i2, i3));
		String rts = String.valueOf(reqTime);
		rts += rts;
		final String rtsSegm = rts.substring(i2, i2 + i3);
		while (i1 >= sessKey.length()) {
			i1 -= sessKey.length();
		}
		final String sk;
		if (i1 == 0) {
			sk = rtsSegm + sessKey;
		} else {
			sk = sessKey.substring(0, i1) + rtsSegm + sessKey.substring(i1);
		}
		return sk;
	}

	/**
	 * 得到要输出的字节串
	 * 
	 * @author tfzzh
	 * @dateTime 2023年10月15日 13:54:49
	 * @return 输出的字节串
	 */
	public byte[] getOutBytes() {
		if (null == this.out) {
			if (null == this.data) {
				this.out = new byte[0];
				return this.out;
			}
			final String cont = this.objToString(this.data);
			final String sk = this.getSecretKey(this.sessKey, this.reqTime);
			// System.out.println(" sk -> [" + this.sessKey + "][" + this.reqTime + "] -> [" + sk + "]");
			final byte[] skBA = sk.getBytes();
			// System.out.println(MessageFormat.format(" sessKey -> [{0}] : [{1}] ... ", sk, Arrays.toString(skBA)));
			// ------------------------------------------------------
			final byte[] contBA = cont.getBytes();
			// System.out.println(MessageFormat.format(" \n\ncont ->\n [{0}] :\n [{1}] ... ", cont, Arrays.toString(contBA)));
			final byte[] bakBA = this.encryptionByte(contBA, skBA);
			// System.out.println(MessageFormat.format("\n bak ({1})[{0}]\n\n-> [{2}] \n... ", Arrays.toString(skBA), bakBA.length, Arrays.toString(bakBA)));
			this.out = bakBA;
		}
		return this.out;
	}

	/**
	 * @author tfzzh
	 * @dateTime 2023年10月9日 18:43:40
	 * @see com.tfzzh.view.web.link.BaseBackOperationBean#linkTo(com.tfzzh.view.web.link.OperateLink.OperateLinkInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void linkTo(final OperateLinkInfo link, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		link.executeResult(this, request, response);
	}
}
