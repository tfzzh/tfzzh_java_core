/**
 * @author XuWeijie
 * @datetime 2015年8月25日_下午8:36:17
 */
package com.tfzzh.tools;

/**
 * @author XuWeijie
 * @datetime 2015年8月25日_下午8:36:17
 */
public class Sha1 {

	/**
	 * @author XuWeijie
	 * @datetime 2015年8月25日_下午8:38:53
	 */
	private final int[] abcde = { 0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476, 0xc3d2e1f0 };

	/**
	 * 摘要数据存储数组
	 * 
	 * @author XuWeijie
	 * @datetime 2015年8月25日_下午8:38:52
	 */
	private final int[] digestInt = new int[5];

	/**
	 * 计算过程中的临时数据存储数组
	 * 
	 * @author XuWeijie
	 * @datetime 2015年8月25日_下午8:38:52
	 */
	private final int[] tmpData = new int[80];

	/**
	 * 计算sha-1摘要
	 * 
	 * @author XuWeijie
	 * @datetime 2015年8月25日_下午8:38:51
	 * @param bytedata 字节数据
	 * @return 固定内容
	 */
	private int process_input_bytes(final byte[] bytedata) {
		// 初试化常量
		System.arraycopy(this.abcde, 0, this.digestInt, 0, this.abcde.length);
		// 格式化输入字节数组，补10及长度数据
		final byte[] newbyte = this.byteArrayFormatData(bytedata);
		// 获取数据摘要计算的数据单元个数
		final int MCount = newbyte.length / 64;
		// 循环对每个数据单元进行摘要计算
		for (int pos = 0; pos < MCount; pos++) {
			// 将每个单元的数据转换成16个整型数据，并保存到tmpData的前16个数组元素中
			for (int j = 0; j < 16; j++) {
				this.tmpData[j] = this.byteArrayToInt(newbyte, (pos * 64) + (j * 4));
			}
			// 摘要计算函数
			this.encrypt();
		}
		return 20;
	}

	/**
	 * 格式化输入字节数组格式
	 * 
	 * @author XuWeijie
	 * @datetime 2015年8月25日_下午8:38:49
	 * @param bytedata 字节数据
	 * @return 新字节数据
	 */
	private byte[] byteArrayFormatData(final byte[] bytedata) {
		// 补0数量
		int zeros = 0;
		// 补位后总位数
		int size = 0;
		// 原始数据长度
		final int n = bytedata.length;
		// 模64后的剩余位数
		final int m = n % 64;
		// 计算添加0的个数以及添加10后的总长度
		if (m < 56) {
			zeros = 55 - m;
			size = (n - m) + 64;
		} else if (m == 56) {
			zeros = 63;
			size = n + 8 + 64;
		} else {
			zeros = (63 - m) + 56;
			size = ((n + 64) - m) + 64;
		}
		// 补位后生成的新数组内容
		final byte[] newbyte = new byte[size];
		// 复制数组的前面部分
		System.arraycopy(bytedata, 0, newbyte, 0, n);
		// 获得数组Append数据元素的位置
		int l = n;
		// 补1操作
		newbyte[l++] = (byte) 0x80;
		// 补0操作
		for (int i = 0; i < zeros; i++) {
			newbyte[l++] = (byte) 0x00;
		}
		// 计算数据长度，补数据长度位共8字节，长整型
		final long N = (long) n * 8;
		final byte h8 = (byte) (N & 0xFF);
		final byte h7 = (byte) ((N >> 8) & 0xFF);
		final byte h6 = (byte) ((N >> 16) & 0xFF);
		final byte h5 = (byte) ((N >> 24) & 0xFF);
		final byte h4 = (byte) ((N >> 32) & 0xFF);
		final byte h3 = (byte) ((N >> 40) & 0xFF);
		final byte h2 = (byte) ((N >> 48) & 0xFF);
		final byte h1 = (byte) (N >> 56);
		newbyte[l++] = h1;
		newbyte[l++] = h2;
		newbyte[l++] = h3;
		newbyte[l++] = h4;
		newbyte[l++] = h5;
		newbyte[l++] = h6;
		newbyte[l++] = h7;
		newbyte[l++] = h8;
		return newbyte;
	}

	/**
	 * 处理方式1
	 * 
	 * @author XuWeijie
	 * @datetime 2015年8月25日_下午8:38:11
	 * @param x 1
	 * @param y 2
	 * @param z 3
	 * @return 结果
	 */
	private int f1(final int x, final int y, final int z) {
		return (x & y) | (~x & z);
	}

	/**
	 * 处理方式2
	 * 
	 * @author XuWeijie
	 * @datetime 2015年8月25日_下午8:38:11
	 * @param x 1
	 * @param y 2
	 * @param z 3
	 * @return 结果
	 */
	private int f2(final int x, final int y, final int z) {
		return x ^ y ^ z;
	}

	/**
	 * 处理方式3
	 * 
	 * @author XuWeijie
	 * @datetime 2015年8月25日_下午8:38:09
	 * @param x 1
	 * @param y 2
	 * @param z 3
	 * @return 结果
	 */
	private int f3(final int x, final int y, final int z) {
		return (x & y) | (x & z) | (y & z);
	}

	/**
	 * 处理方式4
	 * 
	 * @author XuWeijie
	 * @datetime 2015年8月25日_下午8:38:08
	 * @param x 1
	 * @param y 2
	 * @return 结果
	 */
	private int f4(final int x, final int y) {
		return (x << y) | (x >>> (32 - y));
	}

	/**
	 * 单元摘要计算函数
	 * 
	 * @author XuWeijie
	 * @datetime 2015年8月25日_下午8:38:08
	 */
	private void encrypt() {
		for (int i = 16; i <= 79; i++) {
			this.tmpData[i] = this.f4(this.tmpData[i - 3] ^ this.tmpData[i - 8] ^ this.tmpData[i - 14] ^ this.tmpData[i - 16], 1);
		}
		final int[] tmpabcde = new int[5];
		for (int i1 = 0; i1 < tmpabcde.length; i1++) {
			tmpabcde[i1] = this.digestInt[i1];
		}
		for (int j = 0; j <= 19; j++) {
			final int tmp = this.f4(tmpabcde[0], 5) + this.f1(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + this.tmpData[j] + 0x5a827999;
			tmpabcde[4] = tmpabcde[3];
			tmpabcde[3] = tmpabcde[2];
			tmpabcde[2] = this.f4(tmpabcde[1], 30);
			tmpabcde[1] = tmpabcde[0];
			tmpabcde[0] = tmp;
		}
		for (int k = 20; k <= 39; k++) {
			final int tmp = this.f4(tmpabcde[0], 5) + this.f2(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + this.tmpData[k] + 0x6ed9eba1;
			tmpabcde[4] = tmpabcde[3];
			tmpabcde[3] = tmpabcde[2];
			tmpabcde[2] = this.f4(tmpabcde[1], 30);
			tmpabcde[1] = tmpabcde[0];
			tmpabcde[0] = tmp;
		}
		for (int l = 40; l <= 59; l++) {
			final int tmp = this.f4(tmpabcde[0], 5) + this.f3(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + this.tmpData[l] + 0x8f1bbcdc;
			tmpabcde[4] = tmpabcde[3];
			tmpabcde[3] = tmpabcde[2];
			tmpabcde[2] = this.f4(tmpabcde[1], 30);
			tmpabcde[1] = tmpabcde[0];
			tmpabcde[0] = tmp;
		}
		for (int m = 60; m <= 79; m++) {
			final int tmp = this.f4(tmpabcde[0], 5) + this.f2(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + this.tmpData[m] + 0xca62c1d6;
			tmpabcde[4] = tmpabcde[3];
			tmpabcde[3] = tmpabcde[2];
			tmpabcde[2] = this.f4(tmpabcde[1], 30);
			tmpabcde[1] = tmpabcde[0];
			tmpabcde[0] = tmp;
		}
		for (int i2 = 0; i2 < tmpabcde.length; i2++) {
			this.digestInt[i2] = this.digestInt[i2] + tmpabcde[i2];
		}
		for (int n = 0; n < this.tmpData.length; n++) {
			this.tmpData[n] = 0;
		}
	}

	/**
	 * 4字节数组转换为整数
	 * 
	 * @author XuWeijie
	 * @datetime 2015年8月25日_下午8:38:05
	 * @param bytedata 字节数据
	 * @param i 起始索引位
	 * @return 结果int
	 */
	private int byteArrayToInt(final byte[] bytedata, final int i) {
		return ((bytedata[i] & 0xff) << 24) | ((bytedata[i + 1] & 0xff) << 16) | ((bytedata[i + 2] & 0xff) << 8) | (bytedata[i + 3] & 0xff);
	}

	/**
	 * 整数转换为4字节数组
	 * 
	 * @author XuWeijie
	 * @datetime 2015年8月25日_下午8:38:04
	 * @param intValue 目标int
	 * @param byteData 字节数据
	 * @param i 起始索引位
	 */
	private void intToByteArray(final int intValue, final byte[] byteData, final int i) {
		byteData[i] = (byte) (intValue >>> 24);
		byteData[i + 1] = (byte) (intValue >>> 16);
		byteData[i + 2] = (byte) (intValue >>> 8);
		byteData[i + 3] = (byte) intValue;
	}

	/**
	 * 将字节转换为十六进制字符串
	 * 
	 * @author XuWeijie
	 * @datetime 2015年8月25日_下午8:38:03
	 * @param ib 目标字节
	 * @return 结果内容
	 */
	private static String byteToHexString(final byte ib) {
		final char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		final char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0F];
		ob[1] = Digit[ib & 0X0F];
		final String s = new String(ob);
		return s;
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @author XuWeijie
	 * @datetime 2015年8月25日_下午8:38:01
	 * @param bytearray 字节数据
	 * @return 结果内容
	 */
	private static String byteArrayToHexString(final byte[] bytearray) {
		String strDigest = "";
		for (final byte element : bytearray) {
			strDigest += Sha1.byteToHexString(element);
		}
		return strDigest;
	}

	/**
	 * 计算sha-1摘要，返回相应的字节数组
	 * 
	 * @author XuWeijie
	 * @datetime 2015年8月25日_下午8:37:59
	 * @param byteData 字节数据
	 * @return 处理后的摘要
	 */
	public byte[] getDigestOfBytes(final byte[] byteData) {
		this.process_input_bytes(byteData);
		final byte[] digest = new byte[20];
		for (int i = 0; i < this.digestInt.length; i++) {
			this.intToByteArray(this.digestInt[i], digest, i * 4);
		}
		return digest;
	}

	/**
	 * 计算sha-1摘要，返回相应的十六进制字符串
	 * 
	 * @author XuWeijie
	 * @datetime 2015年8月25日_下午8:37:58
	 * @param byteData 字节数据
	 * @return 结果
	 */
	public String getDigestOfString(final byte[] byteData) {
		return Sha1.byteArrayToHexString(this.getDigestOfBytes(byteData));
	}

	/**
	 * @author 许纬杰
	 * @datetime 2016年4月26日_下午6:16:26
	 * @param args 无效果
	 */
	public static void main(final String[] args) {
		final String data = "jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg&noncestr=Wm3WZYTPz0wzccnW&timestamp=1414587457&url=http://mp.weixin.qq.com?params=value";
		System.out.println(data);
		final String digest = new Sha1().getDigestOfString(data.getBytes());
		System.out.println(digest);
	}
}
