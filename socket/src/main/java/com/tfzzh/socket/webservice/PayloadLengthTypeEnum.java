/**
 * @author Weijie Xu
 * @dateTime 2015年2月11日 上午10:02:12
 */
package com.tfzzh.socket.webservice;

import com.tfzzh.exception.UnknowRuntimeException;

/**
 * 负载长度类型
 * 
 * @author Weijie Xu
 * @dateTime 2015年2月11日 上午10:02:12
 */
public enum PayloadLengthTypeEnum {
	/**
	 * 普通的，实际来说是最简的方式：%x00-7D
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月11日 上午10:10:04
	 */
	Normal(0) {

		@Override
		public int getContentLength(final byte[] buf) {
			return buf[1] & 0x7f;
		}

		@Override
		public void completionByteArr(final byte[] buf, final int len) {
			buf[1] |= len;
		}
	},
	/**
	 * 短消息：%x0000-FFFF
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月11日 上午10:10:05
	 */
	Short(2) {

		@Override
		public int getContentLength(final byte[] buf) {
			return DataTypeChangeHelper.getShort(buf, 2);
		}

		@Override
		public void completionByteArr(final byte[] buf, final int len) {
			buf[1] |= 126;
			buf[3] = (byte) len;
			buf[2] = (byte) (len >> 8);
		}
	},
	/**
	 * 长消息：%x0000000000000000-7FFFFFFFFFFFFFFF
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月11日 上午10:10:06
	 */
	Long(8) {

		@Override
		public int getContentLength(final byte[] buf) {
			return (int) DataTypeChangeHelper.getLong(buf, 2);
		}

		@Override
		public void completionByteArr(final byte[] buf, final int len) {
			buf[1] |= 127;
			buf[9] = (byte) len;
			buf[8] = (byte) (len >> 8);
			buf[7] = (byte) (len >> 16);
			buf[6] = (byte) (len >> 24);
			buf[5] = (byte) (len >> 32);
			buf[4] = (byte) (len >> 40);
			buf[3] = (byte) (len >> 48);
			buf[2] = (byte) (len >> 56);
		}
	};

	/**
	 * 目标数组长度
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月11日 上午11:03:19
	 */
	private final int len;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年2月11日 上午11:03:11
	 * @param len 目标数组长度
	 */
	PayloadLengthTypeEnum(final int len) {
		this.len = len;
	}

	/**
	 * 得到目标数组长度
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月11日 上午11:03:42
	 * @return the len
	 */
	public int getArrLen() {
		return this.len;
	}

	/**
	 * 得到内容的长度
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月11日 上午11:05:25
	 * @param buf 完整的字节数组
	 * @return 所相关的内容的长度
	 */
	public abstract int getContentLength(byte[] buf);

	/**
	 * 得到长度字节位(第二位)，所相关byte值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月11日 下午12:42:46
	 * @param buf 目标字节数组
	 * @param len 目标长度，仅为了normal类型
	 */
	public abstract void completionByteArr(byte[] buf, int len);

	/**
	 * 得到目标负载长度类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月11日 上午11:10:50
	 * @param len 目标字节长度
	 * @return 负载长度类型
	 */
	public static PayloadLengthTypeEnum getType(final byte len) {
		switch (len) {
		case 126:
			return Short;
		case 127:
			return Long;
		default:
			return Normal;
		}
	}

	/**
	 * 得到目标负载长度类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月11日 上午11:22:38
	 * @param len 目标内容长度
	 * @return 负载长度类型
	 */
	public static PayloadLengthTypeEnum getType(final int len) {
		if (len < 0) {
			// 正常来说不可能出现的情况
			throw new UnknowRuntimeException("Cann't exists len[" + len + "]... ");
		} else if (len < 126) {
			return Normal;
		} else if (len <= 65535) {
			return Short;
		} else {
			return Long;
		}
	}
}
