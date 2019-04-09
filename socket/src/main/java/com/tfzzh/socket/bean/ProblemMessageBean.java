/**
 * @author Weijie Xu
 * @dateTime 2014年4月9日 下午6:46:01
 */
package com.tfzzh.socket.bean;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.tfzzh.socket.tools.TfzzhDataInputStream;
import com.tfzzh.socket.tools.TfzzhDataOutputStream;

/**
 * 问题消息
 * 
 * @author Weijie Xu
 * @dateTime 2014年4月9日 下午6:46:01
 */
public class ProblemMessageBean extends BaseSocketMessageBean {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年4月9日 下午6:54:54
	 */
	private static final long serialVersionUID = -2499174907845818157L;

	/**
	 * 消息列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月9日 下午6:47:41
	 */
	private final Map<String, String> msgMap;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年4月9日 下午6:52:32
	 */
	public ProblemMessageBean() {
		this.msgMap = new LinkedHashMap<>();
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年4月9日 下午6:47:40
	 * @param msgMap 消息列表
	 */
	public ProblemMessageBean(final Map<String, String> msgMap) {
		this.msgMap = msgMap;
	}

	/**
	 * 得到消息列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月9日 下午6:51:08
	 * @return the msgMap
	 */
	public Map<String, String> getMsgMap() {
		return this.msgMap;
	}

	/**
	 * 向字节流写入内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月9日 下午6:46:01
	 * @param buf 字节流
	 * @throws IOException 抛
	 * @see com.tfzzh.socket.bean.BaseSocketMessageBean#write(com.tfzzh.socket.tools.TfzzhDataOutputStream)
	 */
	@Override
	public void write(final TfzzhDataOutputStream buf) throws IOException {
		// 先写入存在的问题数量
		buf.writeInt(this.msgMap.size());
		// 逐个放入问题信息
		for (final Entry<String, String> e : this.msgMap.entrySet()) {
			buf.writeString(e.getKey());
			buf.writeString(e.getValue());
		}
	}

	/**
	 * 从字节流读取内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月9日 下午6:46:01
	 * @param buf 字节流
	 * @param problemMsg 问题集合
	 * @throws IOException 抛
	 * @see com.tfzzh.socket.bean.BaseSocketMessageBean#read(com.tfzzh.socket.tools.TfzzhDataInputStream, java.util.Map)
	 */
	@Override
	public void read(final TfzzhDataInputStream buf, final Map<String, String> problemMsg) throws IOException {
		// 先读取数量
		for (int s = buf.readInt(); s > 0; s--) {
			// 逐个放入消息
			this.msgMap.put(buf.readString(), buf.readString());
		}
	}
}
