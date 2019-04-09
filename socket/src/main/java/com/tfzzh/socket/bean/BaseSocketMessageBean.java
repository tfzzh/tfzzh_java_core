/**
 * @author Weijie Xu
 * @dateTime 2014-3-18 下午7:47:12
 */
package com.tfzzh.socket.bean;

import java.io.IOException;
import java.util.Map;

import com.tfzzh.socket.action.BaseMessageBean;
import com.tfzzh.socket.tools.TfzzhDataInputStream;
import com.tfzzh.socket.tools.TfzzhDataOutputStream;

/**
 * 基于Sokcet的消息Bean
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-18 下午7:47:12
 */
public abstract class BaseSocketMessageBean extends BaseMessageBean {

	/**
	 * @author 许纬杰
	 * @datetime 2016年4月27日_上午11:10:39
	 */
	private static final long serialVersionUID = 1184049559793169263L;

	/**
	 * 将自身属性对象写入到字节流
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午4:50:27
	 * @param buf 预向其写入消息的字节流对象
	 * @throws IOException 抛
	 */
	public abstract void write(TfzzhDataOutputStream buf) throws IOException;

	/**
	 * 从字节流中读取内容到对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午4:50:26
	 * @param buf 预从中读取消息的字节流对象
	 * @param problemMsg 问题消息列表
	 * @throws IOException 抛
	 */
	public abstract void read(TfzzhDataInputStream buf, Map<String, String> problemMsg) throws IOException;

	/**
	 * 设置请求的编码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年7月4日 下午5:12:24
	 * @param code 请求的编码
	 */
	public void putProxyCode(final int code) {
	}
}
