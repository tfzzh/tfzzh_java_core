/**
 * @author TFZZH
 * @dateTime 2011-11-3 下午4:21:00
 */
package com.tfzzh.view.web.tools;

/**
 * @author TFZZH
 * @dateTime 2011-11-3 下午4:21:00
 */
public enum UploadBackCodeEnum {
	/**
	 * 文件写入成功
	 * 
	 * @author TFZZH
	 * @dateTime 2011-11-3 下午5:51:10
	 */
	OK((short) 0),
	/**
	 * 文件大小超过限定
	 * 
	 * @author TFZZH
	 * @dateTime 2011-11-3 下午5:37:17
	 */
	SizeLimit((short) 1),
	/**
	 * 没有文件后缀名
	 * 
	 * @author TFZZH
	 * @dateTime 2011-11-3 下午5:38:20
	 */
	PosfixInexist((short) 2),
	/**
	 * 文件后缀错误
	 * 
	 * @author TFZZH
	 * @dateTime 2011-11-3 下午5:49:31
	 */
	PosfixError((short) 3),
	/**
	 * 文件写入异常
	 * 
	 * @author TFZZH
	 * @dateTime 2011-11-3 下午5:51:16
	 */
	IOException((short) 4),
	/**
	 * 未知的错误
	 * 
	 * @author TFZZH
	 * @dateTime 2011-11-3 下午5:51:14
	 */
	UnknowExceptioin((short) 5);

	/**
	 * 编码
	 * 
	 * @author TFZZH
	 * @dateTime 2011-11-3 下午5:53:32
	 */
	private final short code;

	/**
	 * @author TFZZH
	 * @dateTime 2011-11-3 下午5:54:08
	 * @param code 编码
	 */
	UploadBackCodeEnum(final short code) {
		this.code = code;
	}

	/**
	 * 得到编码
	 * 
	 * @author TFZZH
	 * @dateTime 2011-11-3 下午5:53:31
	 * @return the code
	 */
	public short getCode() {
		return this.code;
	}
}
