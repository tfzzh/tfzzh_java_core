/**
 * @author 许纬杰
 * @datetime 2016年4月27日_下午10:10:41
 */
package com.tfzzh.socket.action;

import com.tfzzh.tools.BaseBean;

/**
 * @author 许纬杰
 * @datetime 2016年4月27日_下午10:10:41
 */
public abstract class OutputMessageBean extends BaseBean {

	/**
	 * @author 许纬杰
	 * @datetime 2016年4月27日_下午10:24:59
	 */
	private static final long serialVersionUID = 5371268525530924321L;

	/**
	 * 准备发送的消息码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-21 下午1:00:28
	 */
	private final int code;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年4月9日 下午9:02:57
	 * @param code 消息码
	 */
	public OutputMessageBean(final int code) {
		this.code = code;
	}

	/**
	 * 得到准备发送的消息码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-21 下午1:02:43
	 * @return the code
	 */
	public int getCode() {
		return this.code;
	}

	/**
	 * 得到准备发出的数据消息对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-21 下午1:02:43
	 * @return the msg
	 */
	public abstract BaseMessageBean getMsg();
}
