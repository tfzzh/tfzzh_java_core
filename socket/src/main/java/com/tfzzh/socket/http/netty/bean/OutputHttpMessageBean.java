/**
 * @author Weijie Xu
 * @datetime 2016年4月27日_下午10:23:00
 */
package com.tfzzh.socket.http.netty.bean;

import com.tfzzh.socket.action.OutputMessageBean;
import com.tfzzh.socket.http.netty.tools.BaseHttpMessageBean;

/**
 * 准备传出的消息
 * 
 * @author Weijie Xu
 * @datetime 2016年4月27日_下午10:23:00
 */
public class OutputHttpMessageBean extends OutputMessageBean {

	/**
	 * @author 许纬杰
	 * @datetime 2016年5月9日_上午10:50:19
	 */
	private static final long serialVersionUID = 2208762030919284013L;

	/**
	 * 准备发出的数据消息对象
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_下午10:23:00
	 */
	private final BaseHttpMessageBean msg;

	/**
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_下午10:23:00
	 * @param code 消息码
	 */
	public OutputHttpMessageBean(final int code) {
		this(code, null);
	}

	/**
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_下午10:23:00
	 * @param code 消息码
	 * @param msg 数据消息对象
	 */
	public OutputHttpMessageBean(final int code, final BaseHttpMessageBean msg) {
		super(code);
		this.msg = msg;
	}

	/**
	 * 得到准备发出的数据消息对象
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_下午10:23:00
	 * @return the msg
	 * @see com.tfzzh.socket.action.OutputMessageBean#getMsg()
	 */
	@Override
	public BaseHttpMessageBean getMsg() {
		return this.msg;
	}

	/**
	 * 写入页面返回内容
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月27日_下午10:23:00
	 * @param sb 内容对象
	 */
	public void writeContent(final StringBuilder sb) {
		if (null != this.msg) {
			this.msg.writeContent(sb);
		}
	}

	/**
	 * 写入请求消息内容
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_下午10:23:00
	 * @param sb 内容对象
	 */
	public void writeParam(final StringBuilder sb) {
		if (null != this.msg) {
			this.msg.writeParam(sb);
		}
	}
}
