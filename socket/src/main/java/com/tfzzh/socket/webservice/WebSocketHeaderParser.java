package com.tfzzh.socket.webservice;

import io.netty.buffer.ByteBuf;

/**
 * Websocket头信息解析器
 * 
 * @author Weijie Xu
 * @dateTime 2015年2月7日 上午10:31:57
 */
public class WebSocketHeaderParser {

	/**
	 * 不明
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午6:15:18
	 */
	private final boolean fin;

	/**
	 * 还无实际效果
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午9:40:04
	 */
	private final boolean rsv1;

	/**
	 * 还无实际效果
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午9:40:06
	 */
	private final boolean rsv2;

	/**
	 * 还无实际效果
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午9:40:08
	 */
	private final boolean rsv3;

	/**
	 * 字节类型：<br />
	 * 00，消息片段；<br />
	 * 01，文本；<br />
	 * 08，关闭；<br />
	 * 02，字节；<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午9:40:49
	 */
	private final byte opcode;

	/**
	 * 是否存在掩码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午10:07:24
	 */
	private final boolean maskcode;

	/**
	 * 预期文本长度
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午10:07:52
	 */
	private final PayloadLengthTypeEnum pl;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 上午10:36:35
	 * @param buffer 待处理的字节流信息
	 */
	public WebSocketHeaderParser(final byte[] buffer) {
		// 第一个字节
		this.fin = (buffer[0] & 0x80) == 0x80; // 128
		this.rsv1 = (buffer[0] & 0x40) == 0x40; // 64
		this.rsv2 = (buffer[0] & 0x20) == 0x20; // 32
		this.rsv3 = (buffer[0] & 0x10) == 0x10; // 16
		this.opcode = (byte) (buffer[0] & 0x0f); // 1111
		// 第二个字节
		this.maskcode = (buffer[1] & 0x80) == 0x80; // 128
		this.pl = PayloadLengthTypeEnum.getType((byte) (buffer[1] & 0x7f)); // 最大到127
	}

	/**
	 * @author XuWeijie
	 * @datetime 2016年1月12日_下午8:55:37
	 * @param bb 字节流
	 */
	public WebSocketHeaderParser(final ByteBuf bb) {
		final byte b0 = bb.getByte(0);
		// 第一个字节
		this.fin = (b0 & 0x80) == 0x80; // 128
		this.rsv1 = (b0 & 0x40) == 0x40; // 64
		this.rsv2 = (b0 & 0x20) == 0x20; // 32
		this.rsv3 = (b0 & 0x10) == 0x10; // 16
		this.opcode = (byte) (b0 & 0x0f); // 1111
		final byte b1 = bb.getByte(1);
		// 第二个字节
		this.maskcode = (b1 & 0x80) == 0x80; // 128
		this.pl = PayloadLengthTypeEnum.getType((byte) (b1 & 0x7f)); // 最大到127
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午10:31:08
	 * @param fin 不明
	 * @param rsv1 还无实际效果
	 * @param rsv2 还无实际效果
	 * @param rsv3 还无实际效果
	 * @param opcode 字节类型： 01，文本； 08，关闭； 02，字节；
	 * @param hasmask 是否存在掩码
	 * @param length 预期文本长度
	 */
	public WebSocketHeaderParser(final boolean fin, final boolean rsv1, final boolean rsv2, final boolean rsv3, final byte opcode, final boolean hasmask, final int length) {
		this.fin = fin;
		this.rsv1 = rsv1;
		this.rsv2 = rsv2;
		this.rsv3 = rsv3;
		this.opcode = opcode;
		// 第二个字节
		this.maskcode = hasmask;
		this.pl = PayloadLengthTypeEnum.getType(length);
	}

	/**
	 * 不明
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午10:09:50
	 * @return 不明
	 */
	public boolean getFin() {
		return this.fin;
	}

	/**
	 * 还无实际效果
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午10:10:39
	 * @return 还无实际效果
	 */
	public boolean getRsv1() {
		return this.rsv1;
	}

	/**
	 * 还无实际效果
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午10:10:40
	 * @return 还无实际效果
	 */
	public boolean getRsv2() {
		return this.rsv2;
	}

	/**
	 * 还无实际效果
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午10:10:42
	 * @return 还无实际效果
	 */
	public boolean getRsv3() {
		return this.rsv3;
	}

	/**
	 * 得到字节类型： 01，文本； 08，关闭； 02，字节；
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午10:31:59
	 * @return 字节类型： 01，文本； 08，关闭； 02，字节；
	 */
	public byte getOpCode() {
		return this.opcode;
	}

	/**
	 * 是否存在掩码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午10:32:10
	 * @return 是否存在掩码
	 */
	public boolean hasMask() {
		return this.maskcode;
	}

	/**
	 * 得到预期文本长度
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午10:32:18
	 * @return 预期文本长度
	 */
	public PayloadLengthTypeEnum getLengthType() {
		return this.pl;
	}

	/**
	 * 放入头字节信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 上午10:25:46
	 * @param buffer 字节消息数字
	 * @param length 内容文本长度
	 */
	public void putHeaderBytes(final byte[] buffer, final int length) {
		if (this.fin) {
			buffer[0] ^= 0x80;
		}
		if (this.rsv1) {
			buffer[0] ^= 0x40;
		}
		if (this.rsv2) {
			buffer[0] ^= 0x20;
		}
		if (this.rsv3) {
			buffer[0] ^= 0x10;
		}
		buffer[0] ^= this.opcode;
		if (this.maskcode) {
			buffer[1] ^= 0x80;
		}
		this.pl.completionByteArr(buffer, length);
	}
	// /**
	// * 放入头字节信息，可分片处理的
	// *
	// * @author 许纬杰
	// * @datetime 2016年3月8日_上午11:15:14
	// * @param buffer 字节消息数字
	// * @param length 内容文本长度
	// * @param isFragment 是否为片段
	// */
	// public void putHeaderBytes(final byte[] buffer, final int length, boolean isFragment) {
	// if (this.fin) {
	// buffer[0] ^= 0x80;
	// }
	// if (this.rsv1) {
	// buffer[0] ^= 0x40;
	// }
	// if (this.rsv2) {
	// buffer[0] ^= 0x20;
	// }
	// if (this.rsv3) {
	// buffer[0] ^= 0x10;
	// }
	// if (isFragment) {
	// buffer[0] ^= 0;
	// } else {
	// buffer[0] ^= this.opcode;
	// }
	// if (this.maskcode) {
	// buffer[1] ^= 0x80;
	// }
	// this.pl.completionByteArr(buffer, length);
	// }
}
