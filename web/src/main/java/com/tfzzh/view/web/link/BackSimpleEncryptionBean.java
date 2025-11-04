/**
 * @author tfzzh
 * @dateTime 2023年10月9日 11:55:04
 */
package com.tfzzh.view.web.link;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.tfzzh.exception.NonPassableMethodException;
import com.tfzzh.log.CoreLog;
import com.tfzzh.tools.Constants;
import com.tfzzh.tools.StringTools;
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
	// /**
	// * 端请求时间
	// *
	// * @author tfzzh
	// * @dateTime 2023年10月9日 15:47:15
	// */
	// private final long reqTime;

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
	 *           // * @param reqTime 端请求时间
	 */
	// public BackSimpleEncryptionBean(final String sessKey, final long reqTime) {
	public BackSimpleEncryptionBean(final String sessKey) {
		// this(null, sessKey, reqTime);
		this(null, sessKey);
	}

	/**
	 * @author tfzzh
	 * @dateTime 2023年10月9日 13:33:05
	 * @param data 待返回数据
	 * @param sessKey 会话秘钥
	 *           // * @param reqTime 端请求时间
	 */
	// public BackSimpleEncryptionBean(final Object data, final String sessKey, final long reqTime) {
	public BackSimpleEncryptionBean(final Object data, final String sessKey) {
		super(null, null);
		this.data = data;
		this.sessKey = sessKey;
		// this.reqTime = reqTime;
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
		return switch (obj) {
		case null -> Constants.EMPTY;
		case String s -> s;
		case Number n -> String.valueOf(n);
		case Map<?, ?> m -> {
			final JSONObject jo = new JSONObject(m);
			yield jo.toJSONString();
		}
		case List<?> l -> {
			final JSONArray ja = new JSONArray(l);
			yield ja.toJSONString();
		}
		default -> JSON.toJSONString(obj);
		};
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
	 * @param rt 端请求时间
	 * @return 输出的字节串
	 */
	public byte[] getOutBytes(final long rt) {
		if (null == this.out) {
			if (null == this.data) {
				this.out = new byte[0];
				return this.out;
			}
			final String cont = this.objToString(this.data);
			// final String sk = this.getSecretKey(this.sessKey, this.reqTime);
			final String sk = this.getSecretKey(this.sessKey, rt);
			// System.out.println(" sk -> [" + this.sessKey + "][" + this.reqTime + "] -> [" + sk + "] -->> [" + cont + "] ... ");
			final byte[] skBA = sk.getBytes();
			// System.out.println(MessageFormat.format(" sessKey -> [{0}] : [{1}] ... ", sk, Arrays.toString(skBA)));
			// ------------------------------------------------------
			final byte[] contBA = cont.getBytes();
			// System.out.println(MessageFormat.format(" sk:rt -> [{0}]:[{1}] -> [{2}] ... \ncont ->\n [{3}] :\n [{4}] ... ", this.sessKey, this.reqTime, sk, cont, Arrays.toString(contBA)));
			// System.out.println(MessageFormat.format("\tsk:rt -> [{0}]:[{1}] -> [{2}] ... \ncont ->\n\t[{3}]\n\t[{4}]\n... ", this.sessKey, rt, sk, cont, Arrays.toString(contBA)));
			final byte[] bakBA = this.encryptionByte(contBA, skBA);
			// CoreLog.getInstance().debug(this.getClass(), "\tsk:rt -> [%s]:[%d] -> [%s] ... \ncont ->\n\t[%s]\ncontbyte ->\t[%s]\ncontencry ->\t[%s]\n... ".formatted(this.sessKey, rt, sk, cont, Arrays.toString(contBA), Arrays.toString(bakBA)));
			CoreLog.getInstance().debug(this.getClass(), "\tsk:rt -> [%s]:[%d] -> [%s] ... \ncont ->\n\t[%s]\n... ".formatted(this.sessKey, rt, sk, StringTools.sliceLenString(cont, 128)));
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
	@Deprecated
	public void linkTo(final OperateLinkInfo link, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		throw new NonPassableMethodException(" Simple Encryption don't invoke linkTo ... ");
	}
}
