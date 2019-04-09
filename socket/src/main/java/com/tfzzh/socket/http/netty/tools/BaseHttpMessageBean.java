/**
 * @author Weijie Xu
 * @datetime 2016年4月27日_上午11:18:00
 */
package com.tfzzh.socket.http.netty.tools;

import java.util.Map;

import com.tfzzh.socket.action.BaseMessageBean;

/**
 * 基于http的消息Bean
 * 
 * @author Weijie Xu
 * @datetime 2016年4月27日_上午11:18:00
 */
public abstract class BaseHttpMessageBean extends BaseMessageBean {

	/**
	 * @author 许纬杰
	 * @datetime 2016年4月27日_上午11:18:21
	 */
	private static final long serialVersionUID = -8422027381572377622L;

	/**
	 * 写入页面返回内容
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月27日_上午11:15:11
	 * @param sb 内容对象
	 * @return 结果内容
	 */
	public abstract StringBuilder writeContent(StringBuilder sb);

	/**
	 * 写入请求消息内容
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月27日_上午11:15:11
	 * @param sb 内容对象
	 * @return 结果内容
	 */
	public abstract StringBuilder writeParam(StringBuilder sb);

	/**
	 * 读取请求消息
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月27日_上午11:15:13
	 * @param problemMsg 问题消息列表
	 * @param list 请求消息列表
	 */
	public abstract void read(Map<String, String> list, Map<String, String> problemMsg);

	/**
	 * 设置请求的编码
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_上午11:18:00
	 * @param code 请求的编码
	 */
	public void putProxyCode(final int code) {
	}
}
