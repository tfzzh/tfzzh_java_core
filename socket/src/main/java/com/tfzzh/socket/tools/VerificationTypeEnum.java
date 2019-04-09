/**
 * @author XuWeijie
 * @datetime 2015年12月26日_下午12:06:13
 */
package com.tfzzh.socket.tools;

/**
 * 验证结果类型
 * 
 * @author XuWeijie
 * @datetime 2015年12月26日_下午12:06:14
 */
public enum VerificationTypeEnum {
	/**
	 * 验证成功
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月26日_下午12:08:39
	 */
	Ok,
	/**
	 * 通过，一般指，没有验证或者不需要验证情况
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月28日_上午10:10:37
	 */
	Pass,
	/**
	 * 验证有问题
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月26日_下午12:08:40
	 */
	Error;
}
