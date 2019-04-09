/**
 * @author Weijie Xu
 * @datetime 2016年4月27日_下午10:23:00
 */
package com.tfzzh.socket.bean;

import java.io.IOException;

import com.tfzzh.socket.action.OutputMessageBean;
import com.tfzzh.socket.tools.TfzzhDataOutputStream;

/**
 * 准备传出的消息
 * 
 * @author Weijie Xu
 * @datetime 2016年4月27日_下午10:23:00
 */
public class OutputSocketMessageBean extends OutputMessageBean {

	/**
	 * @author 许纬杰
	 * @datetime 2016年4月27日_下午10:24:47
	 */
	private static final long serialVersionUID = 1017955870371403892L;

	/**
	 * 准备发出的数据消息对象
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_下午10:23:00
	 */
	private final BaseSocketMessageBean msg;

	/**
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_下午10:23:00
	 * @param code 消息码
	 */
	public OutputSocketMessageBean(final int code) {
		this(code, null);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-21 下午1:02:20
	 * @param code 消息码
	 * @param msg 数据消息对象
	 */
	public OutputSocketMessageBean(final int code, final BaseSocketMessageBean msg) {
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
	public BaseSocketMessageBean getMsg() {
		return this.msg;
	}

	/**
	 * 写入数据
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_下午10:23:00
	 * @param buf 输出数据流
	 * @throws IOException 抛
	 */
	public void write(final TfzzhDataOutputStream buf) throws IOException {
		// 首先写入数据对象
		if (null != this.msg) {
			this.msg.write(buf);
		}
	}
}
