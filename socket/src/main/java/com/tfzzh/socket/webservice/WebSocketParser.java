package com.tfzzh.socket.webservice;

import java.io.UnsupportedEncodingException;

import com.tfzzh.socket.tools.SocketConstants;
import com.tfzzh.tools.Constants;

import io.netty.buffer.ByteBuf;

/**
 * websocket信息解析器
 * 
 * @author Weijie Xu
 * @dateTime 2015年2月7日 上午10:30:26
 */
public class WebSocketParser {

	/**
	 * 头信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午6:02:57
	 */
	private WebSocketHeaderParser header;

	/**
	 * 掩码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午6:03:01
	 */
	private byte[] mask;

	/**
	 * 内容信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午6:03:13
	 */
	private byte[] content = new byte[0];

	/**
	 * 消息体实际的总长度
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 下午6:13:34
	 */
	private int actualLength = 0;

	/**
	 * 已经被发送出的数据长度
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月8日_上午11:08:13
	 */
	private int alreadySendLength = 0;

	/**
	 * 是否已经发送过
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月8日_上午11:20:03
	 */
	private boolean hasSend = false;

	// /**
	// * @author Weijie Xu
	// * @dateTime 2015年2月7日 上午10:38:35
	// * @param buffer 待处理的字节流
	// */
	// public WebSocketParser(final byte[] buffer) {
	// // 格式化帧头
	// this.header = new WebSocketHeaderParser(buffer);
	// int len = 2 + this.header.getLengthType().getArrLen();
	// if (buffer.length < len) {
	// // 如果这里不进行判定，之后有可能极低概率出现越界情况
	// return;
	// }
	// final int cl = this.header.getLengthType().getContentLength(buffer);
	// // 是否有掩码
	// if (this.header.hasMask()) {
	// // 有掩码情况，多4个判定位
	// if (buffer.length < ((len += 4) + cl)) {
	// // 目标长度不足
	// return;
	// }
	// this.mask = new byte[4];
	// System.arraycopy(buffer, 2 + this.header.getLengthType().getArrLen(), this.mask, 0, 4);
	// } else {
	// if (buffer.length < (len + cl)) {
	// // 目标长度不足
	// return;
	// }
	// this.mask = null;
	// }
	// this.content = new byte[cl];
	// System.arraycopy(buffer, len, this.content, 0, cl);
	// // 如果有掩码，则需要还原原始数据
	// if (this.header.hasMask()) {
	// this.toMask(this.content, 0);
	// }
	// this.actualLength = len;
	// }
	/**
	 * @author XuWeijie
	 * @datetime 2016年1月12日_下午8:54:53
	 * @param buf 字节流
	 */
	public WebSocketParser(final ByteBuf buf) {
		final byte[] buffer = new byte[buf.readableBytes()];
		buf.getBytes(buf.readerIndex(), buffer);
		// 格式化帧头
		this.header = new WebSocketHeaderParser(buffer);
		int len = 2 + this.header.getLengthType().getArrLen();
		if (buffer.length < len) {
			// 如果这里不进行判定，之后有可能极低概率出现越界情况
			return;
		}
		final int cl = this.header.getLengthType().getContentLength(buffer);
		// 是否有掩码
		if (this.header.hasMask()) {
			// 有掩码情况，多4个判定位
			if (buffer.length < ((len += 4) + cl)) {
				// 目标长度不足
				return;
			}
			this.mask = new byte[4];
			System.arraycopy(buffer, 2 + this.header.getLengthType().getArrLen(), this.mask, 0, 4);
		} else {
			if (buffer.length < (len + cl)) {
				// 目标长度不足
				return;
			}
			this.mask = null;
		}
		this.content = new byte[cl];
		System.arraycopy(buffer, len, this.content, 0, cl);
		// 如果有掩码，则需要还原原始数据
		if (this.header.hasMask()) {
			this.toMask(this.content, 0);
		}
		this.actualLength = len;
		buf.skipBytes(len + cl);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午6:04:48
	 * @param msg 消息内容
	 * @param isBinary 是否二进制消息类型
	 */
	public WebSocketParser(final byte[] msg, final boolean isBinary) {
		this.content = msg;
		this.header = new WebSocketHeaderParser(true, false, false, false, isBinary ? WebsocketOpCode.Binary : WebsocketOpCode.Text, false, this.content.length);
		this.actualLength = this.content.length + 2 + this.header.getLengthType().getArrLen();
		this.mask = new byte[0];
	}

	/**
	 * 是否解析完成
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 下午5:46:43
	 * @return the parsingComplete
	 */
	public boolean isParsingComplete() {
		return this.actualLength > 0;
	}

	/**
	 * 得到输出用字节数组
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午6:36:15
	 * @return 输出用字节数组
	 */
	public byte[] getOutBytes() {
		final byte[] buffer = new byte[this.actualLength];
		this.header.putHeaderBytes(buffer, this.content.length);
		this.alreadySendLength = this.content.length;
		this.hasSend = true;
		if (this.header.hasMask()) {
			System.arraycopy(this.mask, 0, buffer, 2 + this.header.getLengthType().getArrLen(), this.mask.length);
			System.arraycopy(this.content, 0, buffer, 2 + this.header.getLengthType().getArrLen() + this.mask.length, this.content.length);
			this.toMask(buffer, 2 + this.header.getLengthType().getArrLen() + this.mask.length);
		} else {
			System.arraycopy(this.content, 0, buffer, 2 + this.header.getLengthType().getArrLen(), this.content.length);
			// this.toMask(buffer, 2 + this.header.getLengthType().getArrLen());
		}
		return buffer;
	}

	/**
	 * 得到定长输出用字节数组
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月8日_上午11:07:06
	 * @param length 目标长度
	 * @param msgInd 消息索引号
	 * @param totleIndex 总消息数量
	 * @param ind 当前消息索引
	 * @return 输出用字节数组
	 */
	public byte[] getOutBytes(final int length, final int msgInd, final short totleIndex, final short ind) {
		if (this.content.length == 0) {
			if (this.hasSend) {
				// 已经被发送完成，不再处理
				return null;
			}
		}
		if (this.alreadySendLength >= this.content.length) {
			// 已经发送完成，不再处理
			return null;
		}
		// 得到预发送字节长度
		int tarLen;
		if (this.content.length > length) {
			tarLen = this.content.length - this.alreadySendLength;
			if (tarLen > length) {
				tarLen = length;
				// 是片段
			}
		} else {
			tarLen = this.content.length;
		}
		this.hasSend = true;
		// isFragment ? WebsocketOpCode.Fragment :
		final WebSocketHeaderParser header = new WebSocketHeaderParser(true, false, false, false, this.header.getOpCode(), false, tarLen);
		final int actualLength = tarLen + 2 + header.getLengthType().getArrLen() + 12; // 请求消息号4，消息索引号4，消息总条数2，当前消息条数2
		final byte[] buffer = new byte[actualLength];
		header.putHeaderBytes(buffer, tarLen + 12);
		// 消息码
		this.putIntToByte(SocketConstants.WEBSOCKET_SECTION_CODE, buffer, 2 + header.getLengthType().getArrLen());
		// 消息索引号
		this.putIntToByte(msgInd, buffer, 6 + header.getLengthType().getArrLen());
		// 消息总数量
		this.putShortToByte(totleIndex, buffer, 10 + header.getLengthType().getArrLen());
		// 当前消息索引位
		this.putShortToByte(ind, buffer, 12 + header.getLengthType().getArrLen());
		if (header.hasMask()) {
			System.arraycopy(this.mask, 0, buffer, 2 + 12 + header.getLengthType().getArrLen(), this.mask.length);
			System.arraycopy(this.content, this.alreadySendLength, buffer, 2 + 12 + header.getLengthType().getArrLen() + this.mask.length, tarLen);
			this.toMask(buffer, 2 + header.getLengthType().getArrLen() + this.mask.length);
		} else {
			System.arraycopy(this.content, this.alreadySendLength, buffer, 2 + 12 + header.getLengthType().getArrLen(), tarLen);
		}
		this.alreadySendLength += tarLen;
		return buffer;
	}

	/**
	 * 得到实际内容所占用的字节长度
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 下午5:29:21
	 * @return 实际内容所占用的字节长度
	 */
	public int getActualBytesLength() {
		return this.actualLength;
	}

	/**
	 * 得到有效内容的字节数组
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午6:01:30
	 * @return 内容信息（字节数组）
	 */
	public byte[] getContentBytes() {
		return this.content;
	}

	/**
	 * 得到有效内容的字节长度
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 下午5:30:28
	 * @return 有效内容的字节长度
	 */
	public int getContentBytesLength() {
		return this.content.length;
	}

	/**
	 * 得到文本信息<br />
	 * 仅有在为文本类型的时候，才会有返回<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午6:25:04
	 * @return 其中相关的文本信息
	 */
	public String getText() {
		if (this.header.getOpCode() == WebsocketOpCode.Text) {
			try {
				return new String(this.content, Constants.SYSTEM_CODE);
			} catch (final UnsupportedEncodingException e) {
				return null;
			}
		} else {
			return "";
		}
	}

	/**
	 * 进行掩码运算
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午6:06:39
	 * @param buf 需要进行处理的字节数组
	 * @param posInd 起始的字节
	 */
	private void toMask(final byte[] buf, final int posInd) {
		for (int i = buf.length - 1, l = this.mask.length, j = (i - posInd) % l; i >= posInd; i--, j = (j == 0 ? l - 1 : j - 1)) {
			buf[i] = (byte) (buf[i] ^ this.mask[j]);
		}
	}

	/**
	 * 将int写入到byte数组中
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月8日_下午3:09:31
	 * @param value 目标int值
	 * @param bytes 目标byte数组
	 * @param index 起始索引位
	 */
	private void putIntToByte(final int value, final byte[] bytes, final int index) {
		bytes[index] = (byte) ((value >>> 24) & 0xFF);
		bytes[index + 1] = (byte) ((value >>> 16) & 0xFF);
		bytes[index + 2] = (byte) ((value >>> 8) & 0xFF);
		bytes[index + 3] = (byte) ((value >>> 0) & 0xFF);
	}

	/**
	 * 将short写入到byte数组中
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月8日_下午3:11:50
	 * @param value 目标short值
	 * @param bytes 目标byte数组
	 * @param index 起始索引位
	 */
	private void putShortToByte(final short value, final byte[] bytes, final int index) {
		bytes[index] = (byte) ((value >>> 8) & 0xFF);
		bytes[index + 1] = (byte) ((value >>> 0) & 0xFF);
	}
}
