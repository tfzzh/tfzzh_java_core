/**
 * @author Weijie Xu
 * @dateTime 2014年6月23日 下午4:46:15
 */
package com.tfzzh.socket.tools;

/**
 * 请求消息属性
 * 
 * @author Weijie Xu
 * @dateTime 2014年6月23日 下午4:46:15
 */
public abstract class RequestMessageAttribute {

	/**
	 * 请求编码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月23日 下午5:45:57
	 */
	private final int code;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年6月23日 下午7:18:55
	 * @param requestCode 请求编码
	 */
	public RequestMessageAttribute(final int requestCode) {
		this.code = requestCode;
	}

	/**
	 * 得到请求编码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月23日 下午4:47:19
	 * @return 请求编码
	 */
	public int getRequestCode() {
		return this.code;
	}
}
