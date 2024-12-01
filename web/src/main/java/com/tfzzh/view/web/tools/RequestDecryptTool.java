/**
 * @author tfzzh
 * @dateTime 2023年11月23日 16:05:53
 */
package com.tfzzh.view.web.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求解码工具
 * 
 * @author tfzzh
 * @dateTime 2023年11月23日 16:05:53
 */
public class RequestDecryptTool {

	/**
	 * 基础字符集合
	 * 
	 * @author tfzzh
	 * @dateTime 2023年11月23日 16:07:26
	 */
	private final String baseLetters;

	/**
	 * @author tfzzh
	 * @dateTime 2023年11月23日 16:07:38
	 * @param baseLetters 基础字符集合
	 */
	protected RequestDecryptTool(final String baseLetters) {
		this.baseLetters = baseLetters;
	}

	/**
	 * 解密字符串内容
	 * 
	 * @author tfzzh
	 * @dateTime 2023年11月24日 17:42:18
	 * @param eCont 加密的字符串
	 * @param secrKey 加密key
	 * @param rt 相关时间
	 * @return 解密后的内容
	 */
	public String decryptCont(final String eCont, final String secrKey, final long rt) {
		final byte[] ba = this.eContToByte(eCont, rt);
		return this.decryptContByte(ba, secrKey, rt);
	}

	/**
	 * 解密无秘钥的串
	 * 
	 * @author tfzzh
	 * @dateTime 2023年11月25日 16:20:53
	 * @param eStr 无秘钥加密的字串
	 * @param rt 相关时间
	 * @return 解密后的token
	 */
	public String decryptNoSecrKey(final String eStr, final long rt) {
		final String rts = String.valueOf(rt);
		final StringBuilder sb = new StringBuilder();
		final int rtLen = rts.length() - 1;
		for (int i = 0; i <= rtLen; i++) {
			if (i <= (rtLen - 4)) {
				// 逆序
				sb.append(rts.charAt(rtLen - i));
			} else {
				// 正序
				sb.append(rts.charAt(i));
			}
		}
		final String str = this.decryptCont(eStr, sb.toString(), rt);
		// System.out.println(" str [" + str + "] ... ");
		return str;
	}

	/**
	 * 解密字节串内容
	 * 
	 * @author tfzzh
	 * @dateTime 2023年11月23日 16:29:16
	 * @param eContBA 加密的字节串
	 * @param secrKey 加密key
	 * @param rt 相关时间
	 * @return 解密后的内容
	 */
	public String decryptContByte(final byte[] eContBA, final String secrKey, final long rt) {
		// System.out.println(" baseLetters [" + this.baseLetters + "] ... ");
		final String sKey = this.getSecretKey(secrKey, rt);
		final byte[] sContBA = this.decryptByte(eContBA, sKey.getBytes());
		// String tstCont = new String(ba);
		// System.out.println(" ready sKey[" + sKey + "] eContBA [" + Arrays.toString(eContBA) + "]ba[" + Arrays.toString(sContBA) + "] ... ");
		String sCont = new String(sContBA);
		// System.out.println(" np[" + sCont + "] ... ");
		String fHx = sCont.substring(sCont.length() - 2);
		if (fHx.charAt(0) == '}') {
			fHx = sCont.substring(sCont.length() - 1);
			sCont = sCont.substring(0, sCont.length() - 1);
		} else {
			sCont = sCont.substring(0, sCont.length() - 2);
		}
		int fInd = -2;
		try {
			fInd = Integer.valueOf(fHx, 16);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		// System.out.println(" sCont:[" + sCont + "] sk:[" + secrKey + "] fHx:[" + fHx + "] fInd:[" + fInd + "] ... ");
		// int l1 = (int) ((rt % 1000) / 10);
		final int l2 = (int) (((rt % 100) / 10) + ((rt % 1000) / 100));
		// System.out.println(" l1:[" + l1 + "] l2:[" + l2 + "] ... ");
		final StringBuilder sb = new StringBuilder(sCont);
		for (int i = 0; i < secrKey.length(); i++) {
			try {
				// String sv = String.valueOf(sb.charAt(fInd));
				// System.out.println(" run[" + i + "] s[" + sv + "] fi:[" + fInd + "] sb:[" + sb.toString() + "]");
				fInd -= l2;
				while (fInd < 0) {
					fInd += sb.length();
				}
				sb.deleteCharAt(fInd);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		// System.out.println(" re over [" + sb.toString() + "] ... ");
		return sb.toString();
	}
	// /**
	// * 解密字节串文件<br />
	// * 验证过类型：<br />
	// * 图片：2024-09<br />
	// *
	// * @author tfzzh
	// * @dateTime 2024年9月26日 00:14:28
	// * @param eFileBA 加密的文件字节流
	// * @param secrKey 加密key
	// * @param rt 相关时间
	// * @param filePath 文件目标路径
	// * @return 文件字节流
	// */
	// public byte[] decryptFileByte(final byte[] eFileBA, final String secrKey, final long rt, final String filePath) {
	// final String sKey = this.getSecretKey(secrKey, rt);
	// final byte[] sFileBA = this.decryptByte(eFileBA, sKey.getBytes());
	// try (FileOutputStream fos = new FileOutputStream(filePath)) {
	// fos.write(sFileBA);
	// fos.flush();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return sFileBA;
	// }

	/**
	 * 解密字节串文件<br />
	 * 验证过类型：<br />
	 * 图片：2024-09<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2024年9月26日 00:14:28
	 * @param eFileBA 加密的文件字节流
	 * @param secrKey 加密key
	 * @param rt 相关时间
	 * @return 文件字节流
	 */
	public byte[] decryptFileByte(final byte[] eFileBA, final String secrKey, final long rt) {
		final String sKey = this.getSecretKey(secrKey, rt);
		final byte[] sFileBA = this.decryptByte(eFileBA, sKey.getBytes());
		return sFileBA;
	}

	/**
	 * 将加密字符串转为字节串
	 * 
	 * @author tfzzh
	 * @dateTime 2023年11月24日 15:26:53
	 * @param eCont 被加密的内容
	 * @param reqTime 时间戳
	 * @return 转换后的字节串
	 */
	private byte[] eContToByte(final String eCont, final long reqTime) {
		final int baseLen = this.baseLetters.length() / 2;
		final int ind = (int) ((reqTime % 1000) % baseLen);
		final String gStr = this.baseLetters.substring(ind, ind + 6);
		final String bStr = this.baseLetters.substring(ind + 6, ind + baseLen);
		int sInd = 0;
		final List<Integer> bl = new ArrayList<>((eCont.length() / 2) + (eCont.length() / 10));
		while (sInd < eCont.length()) {
			String s = String.valueOf(eCont.charAt(sInd));
			int gInd = gStr.indexOf(s);
			int bInd = -1;
			if (gInd == -1) {
				gInd = 2;
				bInd = bStr.indexOf(s);
			} else {
				sInd++;
				s = String.valueOf(eCont.charAt(sInd));
				bInd = bStr.indexOf(s);
			}
			final int val = ((gInd * bStr.length()) + bInd) - 128;
			// System.out.println(" eContToByte si[" + sInd + ":" + s + "] g[" + gInd + "] b[" + bInd + "] val[" + val + "] ... ");
			bl.add(val);
			sInd++;
		}
		final byte[] bak = new byte[bl.size()];
		int bInd = 0;
		for (final Integer b : bl) {
			bak[bInd++] = b.byteValue();
		}
		return bak;
	}

	/**
	 * 得到秘钥key
	 * 
	 * @author tfzzh
	 * @dateTime 2023年11月24日 15:26:55
	 * @param sessKey 基础加密key
	 * @param reqTime 时间戳
	 * @return 加密用key
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
	 * 解密
	 * 
	 * @author tfzzh
	 * @dateTime 2023年11月24日 15:28:32
	 * @param contBA 被加密的字节流
	 * @param skBA 秘钥key的字节流
	 * @return 被解密后的字节流
	 */
	private byte[] decryptByte(final byte[] contBA, final byte[] skBA) {
		final int cbLen = contBA.length;
		final int sbLen = skBA.length;
		final byte[] bak = new byte[cbLen];
		int ski = 0;
		for (int i = 0; i < cbLen; i++) {
			byte v = contBA[i];
			final byte sv = skBA[ski];
			if ((i % 2) == 0) {
				v -= sv;
				if (v < -128) {
					// v = (byte) (128 - (-127 - v));
					v += 256;
				}
				// if (v != 48) {
				// v -= 1;
				// }
			} else {
				v += sv;
				if (v > 127) {
					// v = (byte) (-127 + (v - 128));
					v -= 256;
				}
			}
			if (++ski >= sbLen) {
				ski = 0;
			}
			bak[i] = v;
		}
		return bak;
	}
}
