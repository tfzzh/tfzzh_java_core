/**
 * @author xuweijie
 * @dateTime 2012-2-2 下午5:45:19
 */
package com.tfzzh.view.web.link;

/**
 * 链接类型
 * 
 * @author xuweijie
 * @dateTime 2012-2-2 下午5:45:19
 */
public enum LinkType {
	/**
	 * 正常模式
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-2 下午5:45:48
	 */
	Normal,
	/**
	 * 适配模式，仿WebService
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-2 下午5:45:48
	 */
	Deploy,
	/**
	 * 分流，适配的再扩展，也同样支持正常模式中的参数
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-2 下午5:45:46
	 */
	Branch;
}
