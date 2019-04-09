/**
 * @author TFZZH
 * @dateTime 2011-2-16 下午05:45:31
 */
package com.tfzzh.view.web.tools;

/**
 * 请求方法
 * 
 * @author TFZZH
 * @dateTime 2011-2-16 下午05:45:31
 */
public enum RequestMethod {
	/**
	 * Get类型
	 * 
	 * @author TFZZH
	 * @dateTime 2011-2-16 下午05:45:58
	 */
	Get,
	/**
	 * Post类型
	 * 
	 * @author TFZZH
	 * @dateTime 2011-2-16 下午05:46:00
	 */
	Post,
	/**
	 * Delete类型
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-1 下午6:12:55
	 */
	Delete,
	/**
	 * Head类型
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-1 下午6:12:56
	 */
	Head,
	/**
	 * Put类型
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-1 下午6:12:57
	 */
	Put,
	/**
	 * Trace类型
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-3 下午2:08:11
	 */
	Trace,
	/**
	 * 通用类型
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-1 下午6:12:57
	 */
	Options,
	/**
	 * 不存在的请求类型
	 * 
	 * @author TFZZH
	 * @dateTime 2011-2-21 下午07:09:21
	 */
	Non;

	/**
	 * 根据方法名称得到请求类型
	 * 
	 * @author TFZZH
	 * @dateTime 2011-2-21 下午07:14:45
	 * @param method 方法名称
	 * @return 请求的类型
	 */
	public static RequestMethod getMethod(final String method) {
		for (final RequestMethod rm : RequestMethod.values()) {
			if (rm.name().equalsIgnoreCase(method)) {
				return rm;
			}
		}
		return Non;
	}
}
