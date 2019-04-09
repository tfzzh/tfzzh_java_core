/**
 * @author TFZZH
 * @dateTime 2011-1-7 上午10:17:35
 */
package com.tfzzh.socket.tools;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.tfzzh.tools.Constants;

/**
 * @author TFZZH
 * @dateTime 2011-1-7 上午10:17:35
 */
public class TfzzhDataOutputStream extends DataOutputStream {

	/**
	 * @author TFZZH
	 * @dateTime 2011-1-7 上午10:17:41
	 * @param out 输出流
	 */
	public TfzzhDataOutputStream(final OutputStream out) {
		super(out);
	}

	/**
	 * 按最小方式写入String
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月17日 下午2:33:40
	 * @param str 待写入字符串
	 * @throws IOException 抛
	 */
	public void writeStringOfMin(final String str) throws IOException {
		final byte[] b = str.getBytes(Constants.SYSTEM_CODE);
		this.writeUInt29(b.length);
		super.write(b);
	}

	/**
	 * 将字符串消息写入流
	 * 
	 * @author TFZZH
	 * @dateTime 2011-1-7 上午10:23:50
	 * @param str 待写入字符串
	 * @throws IOException 抛
	 */
	public void writeString(final String str) throws IOException {
		super.writeUTF(str);
	}

	/**
	 * 该模式不仅会比readUTF()要快，且会相对少一些字节占用量
	 * 
	 * @author TFZZH
	 * @dateTime 2011-1-7 上午10:26:23
	 * @param ref 待写入int
	 * @throws IOException 抛
	 */
	public void writeUInt29(final int ref) throws IOException {
		// Represent smaller integers with fewer bytes using the most
		// significant bit of each byte. The worst case uses 32-bits
		// to represent a 29-bit number, which is what we would have
		// done with no compression.
		// 0x00000000 - 0x0000007F : 0xxxxxxx
		// 0x00000080 - 0x00003FFF : 1xxxxxxx 0xxxxxxx
		// 0x00004000 - 0x001FFFFF : 1xxxxxxx 1xxxxxxx 0xxxxxxx
		// 0x00200000 - 0x3FFFFFFF : 1xxxxxxx 1xxxxxxx 1xxxxxxx xxxxxxxx
		// 0x40000000 - 0xFFFFFFFF : throw range exception
		if (ref < 0x80) {
			// 0x00000000 - 0x0000007F : 0xxxxxxx
			super.writeByte(ref);
		} else if (ref < 0x4000) {
			// 0x00000080 - 0x00003FFF : 1xxxxxxx 0xxxxxxx
			super.writeByte(((ref >> 7) & 0x7F) | 0x80);
			super.writeByte(ref & 0x7F);
		} else if (ref < 0x200000) {
			// 0x00004000 - 0x001FFFFF : 1xxxxxxx 1xxxxxxx 0xxxxxxx
			super.writeByte(((ref >> 14) & 0x7F) | 0x80);
			super.writeByte(((ref >> 7) & 0x7F) | 0x80);
			super.writeByte(ref & 0x7F);
		} else if (ref < 0x40000000) {
			// 0x00200000 - 0x3FFFFFFF : 1xxxxxxx 1xxxxxxx 1xxxxxxx xxxxxxxx
			super.writeByte(((ref >> 22) & 0x7F) | 0x80);
			super.writeByte(((ref >> 15) & 0x7F) | 0x80);
			super.writeByte(((ref >> 8) & 0x7F) | 0x80);
			super.writeByte(ref & 0xFF);
		} else {
			// 0x40000000 - 0xFFFFFFFF : throw range exception
		}
	}
}
