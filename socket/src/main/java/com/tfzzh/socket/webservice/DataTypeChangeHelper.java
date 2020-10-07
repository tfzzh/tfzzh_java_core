package com.tfzzh.socket.webservice;

import java.nio.charset.Charset;

/**
 * 数据类型变化控制器
 * 
 * @author Weijie Xu
 * @dateTime 2015年1月28日 下午12:02:56
 */
public class DataTypeChangeHelper {

	/**
	 * 将一个单字节的byte转换成32位的int
	 * 
	 * @param b byte
	 * @return convert result
	 */
	public static int unsignedByteToInt(final byte b) {
		return b & 0xFF;
	}

	/**
	 * 将一个单字节的Byte转换成十六进制的数
	 * 
	 * @param b byte
	 * @return convert result
	 */
	public static String byteToHex(final byte b) {
		return Integer.toHexString(b & 0xFF);
	}

	/**
	 * 将一个4byte的数组转换成32位的int
	 * 
	 * @param buf bytes buffer
	 * @param pos byte[]中开始转换的位置
	 * @return convert result
	 */
	public static long unsigned4BytesToInt(final byte[] buf, final int pos) {
		// int firstByte = (0x000000FF & ((int) buf[pos]));
		// int secondByte = (0x000000FF & ((int) buf[pos + 1]));
		// int thirdByte = (0x000000FF & ((int) buf[pos + 2]));
		// int fourthByte = (0x000000FF & ((int) buf[pos + 3]));
		// return ((long) ((firstByte) << 24 | (secondByte) << 16 | (thirdByte) << 8 | (fourthByte))) & 0xFFFFFFFFL;
		return (((buf[pos]) << 24) | ((buf[pos + 1]) << 16) | ((buf[pos + 2]) << 8) | (buf[pos + 3])) & 0xFFFFFFFFL;
	}

	/**
	 * 将16位的short转换成byte数组
	 * 
	 * @param s short
	 * @return byte[] 长度为2
	 */
	public static byte[] shortToByteArray(final short s) {
		// final byte[] targets = new byte[2];
		// for (int i = 0; i < 2; i++) {
		// // int offset = (targets.length - 1 - i) * 8;
		// int offset = (1 - i) * 8;
		// targets[i] = (byte) ((s >>> offset) & 0xff);
		// }
		// return targets;
		return new byte[] { (byte) ((s >>> 8) & 0xff), (byte) (s & 0xff) };
	}

	/**
	 * 将32位整数转换成长度为4的byte数组
	 * 
	 * @param s int
	 * @return byte[]
	 */
	public static byte[] intToByteArray(final int s) {
		// byte[] targets = new byte[4];
		// for (int i = 0; i < 4; i++) {
		// int offset = (targets.length - 1 - i) * 8;
		// targets[i] = (byte) ((s >>> offset) & 0xff);
		// }
		// return targets;
		return new byte[] { (byte) ((s >>> 24) & 0xff), (byte) ((s >>> 16) & 0xff), (byte) ((s >>> 8) & 0xff), (byte) (s & 0xff) };
	}

	/**
	 * long to byte[]
	 * 
	 * @param s long
	 * @return byte[]
	 */
	public static byte[] longToByteArray(final long s) {
		// byte[] targets = new byte[8];
		// for (int i = 0; i < 8; i++) {
		// int offset = (targets.length - 1 - i) * 8;
		// targets[i] = (byte) ((s >>> offset) & 0xff);
		// }
		// return targets;
		return new byte[] { (byte) ((s >>> 56) & 0xff), (byte) ((s >>> 48) & 0xff), (byte) ((s >>> 40) & 0xff), (byte) ((s >>> 32) & 0xff), (byte) ((s >>> 24) & 0xff), (byte) ((s >>> 16) & 0xff), (byte) ((s >>> 8) & 0xff), (byte) (s & 0xff) };
	}

	// /**
	// * 32位int转byte[]
	// *
	// * @author Weijie Xu
	// * @dateTime 2015年1月28日 下午12:03:13
	// * @param res int
	// * @return byte[]
	// */
	// public static byte[] int2byte(int res) {
	// byte[] targets = new byte[4];
	// targets[0] = (byte) (res & 0xff);// 最低位
	// targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
	// targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
	// targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
	// return targets;
	// // return new byte[] { (byte) res, (byte) (res >> 8), (byte) (res >> 16), (byte) (res >> 32) };
	// }
	// /**
	// * 将长度为2的byte数组转换为16位int
	// *
	// * @param res byte[]
	// * @return int
	// */
	// public static int byte2int(byte[] res) {
	// int firstByte = (0x00FF & ((int) res[0]));
	// int secondByte = (0x00FF & ((int) res[1]));
	// int rs = (firstByte << 8 | secondByte) & 0xFFFF;
	// return rs;
	// }
	/**
	 * 得到short的字节数组
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午11:08:25
	 * @param data 目标值
	 * @return 字节数组
	 */
	public static byte[] getBytes(final short data) {
		// byte[] bytes = new byte[2];
		// bytes[0] = (byte) (data & 0xff);
		// bytes[1] = (byte) ((data & 0xff00) >> 8);
		// return bytes;
		return new byte[] { (byte) (data & 0xff), (byte) ((data >> 8) & 0xff) };
	}

	/**
	 * 得到char的字节数组
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午11:08:25
	 * @param data 目标值
	 * @return 字节数组
	 */
	public static byte[] getBytes(final char data) {
		// byte[] bytes = new byte[2];
		// bytes[0] = (byte) (data);
		// bytes[1] = (byte) (data >> 8);
		// return bytes;
		return new byte[] { (byte) (data & 0xff), (byte) ((data >> 8) & 0xff) };
	}

	/**
	 * 得到int的字节数组
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午11:08:25
	 * @param data 目标值
	 * @return 字节数组
	 */
	public static byte[] getBytes(final int data) {
		return new byte[] { (byte) (data & 0xff), (byte) ((data >> 8) & 0xff), (byte) ((data >> 16) & 0xff), (byte) ((data >> 24) & 0xff) };
	}

	/**
	 * 得到long的字节数组
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午11:08:25
	 * @param data 目标值
	 * @return 字节数组
	 */
	public static byte[] getBytes(final long data) {
		return new byte[] { (byte) (data & 0xff), (byte) ((data >> 8) & 0xff), (byte) ((data >> 16) & 0xff), (byte) ((data >> 24) & 0xff), (byte) ((data >> 32) & 0xff), (byte) ((data >> 40) & 0xff), (byte) ((data >> 48) & 0xff), (byte) ((data >> 56) & 0xff) };
	}

	/**
	 * 得到float的字节数组
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午11:08:25
	 * @param data 目标值
	 * @return 字节数组
	 */
	public static byte[] getBytes(final float data) {
		return DataTypeChangeHelper.getBytes(Float.floatToIntBits(data));
	}

	/**
	 * 得到double的字节数组
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午11:08:25
	 * @param data 目标值
	 * @return 字节数组
	 */
	public static byte[] getBytes(final double data) {
		return DataTypeChangeHelper.getBytes(Double.doubleToLongBits(data));
	}

	/**
	 * 得到指定字符串对应字符编码的字节数组
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午11:08:44
	 * @param data 目标值
	 * @param charsetName 字符编码
	 * @return 字节数组
	 */
	public static byte[] getBytes(final String data, final String charsetName) {
		return data.getBytes(Charset.forName(charsetName));
	}

	/**
	 * 根据字节数组得到short值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午11:08:47
	 * @param bytes 目标字节数组
	 * @param posInd 目标起始索引
	 * @return shrot值
	 */
	public static int getShort(final byte[] bytes, final int posInd) {
		// return (bytes[posInd] & 0xff) | ((bytes[posInd + 1] & 0xff) << 8);
		return (bytes[posInd + 1] & 0xff) | ((bytes[posInd] & 0xff) << 8);
	}

	/**
	 * 根据字节数组得到char值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午11:08:47
	 * @param bytes 目标字节数组
	 * @param posInd 目标起始索引
	 * @return char值
	 */
	public static char getChar(final byte[] bytes, final int posInd) {
		// return (char) ((bytes[posInd] & 0xff) | ((bytes[posInd + 1] & 0xff) << 8));
		return (char) ((bytes[posInd + 1] & 0xff) | ((bytes[posInd] & 0xff) << 8));
	}

	/**
	 * 根据字节数组得到int值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午11:08:47
	 * @param bytes 目标字节数组
	 * @param posInd 目标起始索引
	 * @return int值
	 */
	public static int getInt(final byte[] bytes, final int posInd) {
		// return (bytes[posInd] & 0xff) | ((bytes[posInd + 1] & 0xff) << 8) | ((bytes[posInd + 2] & 0xff) << 16) | ((bytes[posInd + 3] & 0xff) << 24);
		return ((bytes[posInd] + 3) & 0xff) | ((bytes[posInd + 3] & 0xff) << 8) | ((bytes[posInd + 1] & 0xff) << 16) | ((bytes[posInd] & 0xff) << 24);
	}

	/**
	 * 根据字节数组得到long值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午11:08:47
	 * @param bytes 目标字节数组
	 * @param posInd 目标起始索引
	 * @return long值
	 */
	public static long getLong(final byte[] bytes, final int posInd) {
		// return (bytes[posInd] & 0xff) | ((long) (bytes[posInd + 1] & 0xff) << 8) | ((long) (bytes[posInd + 2] & 0xff) << 16) | ((long) (bytes[posInd + 3] & 0xff) << 24) | ((long) (bytes[posInd + 4] & 0xff) << 32) | ((long) (bytes[posInd + 5] & 0xff) << 40)
		// | ((long) (bytes[posInd + 6] & 0xff) << 48) | ((long) (bytes[posInd + 7] & 0xff) << 56);
		return (bytes[posInd + 7] & 0xff) | ((long) (bytes[posInd + 6] & 0xff) << 8) | ((long) (bytes[posInd + 5] & 0xff) << 16) | ((long) (bytes[posInd + 4] & 0xff) << 24) | ((long) (bytes[posInd + 3] & 0xff) << 32) | ((long) (bytes[posInd + 2] & 0xff) << 40) | ((long) (bytes[posInd + 1] & 0xff) << 48) | ((long) (bytes[posInd] & 0xff) << 56);
	}

	// public static void main(String[] args) {
	// int type = 9;
	// switch (type) {
	// case 0:
	// byte[] buf = new byte[] { 21, 21, 21, 21 };
	// int pos = 0;
	// System.out.println(DataTypeChangeHelper.unsigned4BytesToInt(buf, pos));
	// return;
	// case 1:
	// short s = 23423;
	// System.out.println(Arrays.toString(DataTypeChangeHelper.shortToByteArray(s)));
	// return;
	// case 2:
	// int i = 2142323423;
	// System.out.println(Arrays.toString(DataTypeChangeHelper.intToByteArray(i)));
	// return;
	// case 3:
	// long l = 2342322342323423432l;
	// System.out.println(Arrays.toString(DataTypeChangeHelper.longToByteArray(l)));
	// return;
	// case 5:
	// long l1 = 2342322342323423432l;
	// System.out.println(Arrays.toString(DataTypeChangeHelper.longToByteArray(l1)));
	// return;
	// case 6:
	// byte[] bs1 = { 12, 12 };
	// System.out.println(DataTypeChangeHelper.getShort(bs1, 0));
	// return;
	// case 7:
	// byte[] bs2 = { 12, 12 };
	// System.out.println(DataTypeChangeHelper.getChar(bs2, 0));
	// return;
	// case 8:
	// byte[] bs3 = { 12, 12, 12, 12 };
	// System.out.println(DataTypeChangeHelper.getInt(bs3, 0));
	// return;
	// case 9:
	// byte[] bs4 = { 12, 12, 12, 12, 12, 12, 12, 12 };
	// System.out.println(DataTypeChangeHelper.getLong(bs4, 0));
	// return;
	// }
	// }
	/**
	 * 根据字节数组得到float值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午11:08:47
	 * @param bytes 目标字节数组
	 * @param posInd 目标起始索引
	 * @return float值
	 */
	public static float getFloat(final byte[] bytes, final int posInd) {
		return Float.intBitsToFloat(DataTypeChangeHelper.getInt(bytes, posInd));
	}

	/**
	 * 根据字节数组得到double值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午11:08:47
	 * @param bytes 目标字节数组
	 * @param posInd 目标起始索引
	 * @return double值
	 */
	public static double getDouble(final byte[] bytes, final int posInd) {
		return Double.longBitsToDouble(DataTypeChangeHelper.getLong(bytes, posInd));
	}

	/**
	 * 根据字节数组得到String值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午11:08:47
	 * @param bytes 目标字节数组
	 * @param charsetName 字符编码名
	 * @return String值
	 */
	public static String getString(final byte[] bytes, final String charsetName) {
		return new String(bytes, Charset.forName(charsetName));
	}

	/**
	 * 根据字节数组得到String值，编码为“UTF-8”
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午11:08:47
	 * @param bytes 目标字节数组
	 * @return String值
	 */
	public static String getString(final byte[] bytes) {
		return DataTypeChangeHelper.getString(bytes, "UTF-8");
	}
}
