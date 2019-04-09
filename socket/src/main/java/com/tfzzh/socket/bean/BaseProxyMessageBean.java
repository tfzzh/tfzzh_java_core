/**
 * @author Weijie Xu
 * @dateTime 2014年6月29日 下午4:05:52
 */
package com.tfzzh.socket.bean;

import com.tfzzh.tools.BaseBean;

/**
 * 基础代理消息Bean
 * 
 * @author Weijie Xu
 * @dateTime 2014年6月29日 下午4:05:52
 */
public class BaseProxyMessageBean extends BaseBean {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年7月4日 下午8:08:19
	 */
	private static final long serialVersionUID = 1203567266699346887L;

	/**
	 * 请求码，仅通过设置而存在
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月29日 下午4:40:29
	 */
	private int requestCode;

	/**
	 * 被代理的消息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月29日 下午4:07:08
	 */
	private transient byte[] proxyMsg;

	/**
	 * 是否是存在的
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年7月5日 下午7:17:06
	 */
	private boolean isExist;

	/**
	 * 得到请求码，仅通过设置而存在
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月29日 下午4:40:55
	 * @return the requestCode
	 */
	public int getRequestCode() {
		return this.requestCode;
	}

	/**
	 * 设置请求码，仅通过设置而存在
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月29日 下午4:40:55
	 * @param requestCode the requestCode to set
	 */
	public void setRequestCode(final int requestCode) {
		this.requestCode = requestCode;
	}

	/**
	 * 得到被代理的消息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月29日 下午4:36:32
	 * @return the proxyMsg
	 */
	public byte[] getProxyMsg() {
		return this.proxyMsg;
	}

	/**
	 * 设置被代理的消息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月29日 下午4:36:32
	 * @param proxyMsg the proxyMsg to set
	 */
	public void setProxyMsg(final byte[] proxyMsg) {
		this.proxyMsg = proxyMsg;
		this.isExist = true;
	}

	/**
	 * 是否是存在的
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年7月5日 下午7:18:27
	 * @return the isExist
	 */
	public boolean isExist() {
		return this.isExist;
	}
}
