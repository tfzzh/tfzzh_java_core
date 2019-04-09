/**
 * @author XuWeijie
 * @dateTime Nov 11, 2010 6:45:56 PM
 */
package com.tfzzh.socket.tools;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.tfzzh.tools.Constants;

/**
 * @author XuWeijie
 * @dateTime Nov 11, 2010 6:45:56 PM
 * @model
 */
public class TfzzhDataInputStream extends DataInputStream {

	/**
	 * @author XuWeijie
	 * @dateTime Nov 11, 2010 6:46:34 PM
	 * @param in 进入的数据
	 */
	public TfzzhDataInputStream(final InputStream in) {
		super(in);
	}

	/**
	 * 按最小方式读取String
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月17日 下午2:29:49
	 * @return 字符串消息
	 * @throws IOException 抛
	 */
	public String readStringOfMin() throws IOException {
		final int len = this.readUInt29();
		if (len == 0) {
			return null;
		} else {
			final byte[] b = new byte[len];
			super.read(b, 0, len);
			return new String(b, Constants.SYSTEM_CODE);
		}
	}

	/**
	 * 得到字符串消息
	 * 
	 * @author XuWeijie
	 * @dateTime Nov 11, 2010 6:53:23 PM
	 * @return 字符串消息
	 * @throws IOException 抛
	 */
	public String readString() throws IOException {
		return super.readUTF();
	}

	/**
	 * 得到压缩后的字节串中数值<br />
	 * 该模式不仅会比readUTF()要快，且会相对少一些字节占用量<br />
	 *
	 * @author XuWeijie
	 * @dateTime Nov 11, 2010 7:52:57 PM
	 * @return 数值
	 * @throws IOException 抛
	 */
	public int readUInt29() throws IOException {
		// represents smaller integers with fewer bytes using the most
		// significant bit of each byte. The worst case uses 32-bits
		// to represent a 29-bit number, which is what we would have
		// done with no compression.
		// <pre>
		// 0x00000000 - 0x0000007F : 0xxxxxxx
		// 0x00000080 - 0x00003FFF : 1xxxxxxx 0xxxxxxx
		// 0x00004000 - 0x001FFFFF : 1xxxxxxx 1xxxxxxx 0xxxxxxx
		// 0x00200000 - 0x3FFFFFFF : 1xxxxxxx 1xxxxxxx 1xxxxxxx xxxxxxxx
		// 0x40000000 - 0xFFFFFFFF : throw range exception
		// </pre>
		int value;
		// Each byte must be treated as unsigned
		int b = super.readByte() & 0xFF;
		if (b < 128) {
			return b;
		}
		value = (b & 0x7F) << 7;
		b = super.readByte() & 0xFF;
		if (b < 128) {
			return (value | b);
		}
		value = (value | (b & 0x7F)) << 7;
		b = super.readByte() & 0xFF;
		if (b < 128) {
			return (value | b);
		}
		value = (value | (b & 0x7F)) << 8;
		b = super.readByte() & 0xFF;
		return (value | b);
	}
}
