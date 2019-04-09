/**
 * @author Weijie Xu
 * @datetime 2016年4月27日_上午10:26:10
 */
package com.tfzzh.socket.http.netty.tools;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.tfzzh.socket.RequestSession;
import com.tfzzh.socket.bean.RequestInfoBean;
import com.tfzzh.socket.tools.RequestPool;
import com.tfzzh.tools.BaseBean;

/**
 * 被请求进入的消息
 * 
 * @author 许纬杰
 * @datetime 2016年4月27日_上午10:26:10
 */
public class InputHttpMessageBean extends BaseBean {

	/**
	 * @author 许纬杰
	 * @datetime 2016年4月27日_上午11:34:32
	 */
	private static final long serialVersionUID = -3678680177652381316L;

	/**
	 * 请求码
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_上午10:26:10
	 */
	private final int code;

	/**
	 * 请求的消息
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_上午10:26:10
	 */
	private final RequestInfoBean requestInfo;

	/**
	 * 消息对象
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_上午10:26:10
	 */
	private final BaseHttpMessageBean msg;

	/**
	 * 进入时的时间
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_上午10:26:10
	 */
	private final long inTime;

	/**
	 * 问题消息列表
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_上午10:26:10
	 */
	private final Map<String, String> problemMsg = new LinkedHashMap<>();

	/**
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_上午10:26:10
	 * @param spaceRequestPool 所属空间请求池
	 * @param code 请求码
	 * @param params 得到的请求参数列表
	 * @param startTime 开始时间
	 * @throws IOException 抛
	 * @throws IllegalAccessException 抛
	 * @throws InstantiationException 抛
	 */
	public InputHttpMessageBean(final RequestPool spaceRequestPool, final int code, final Map<String, String> params, final long startTime) throws IOException, InstantiationException, IllegalAccessException {
		this.code = code;
		this.requestInfo = spaceRequestPool.getRequestInfo(code);
		// 得到数据操作对象
		this.msg = (BaseHttpMessageBean) this.requestInfo.getBeanInstance();
		this.inTime = startTime;
		if (null != this.msg) {
			// 进行读取操作
			this.msg.read(params, this.problemMsg);
		}
	}

	/**
	 * 得到请求码
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_上午10:26:10
	 * @return the code
	 */
	public int getCode() {
		return this.code;
	}

	/**
	 * 得到解析请求方法用的消息码
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_上午10:26:10
	 * @return 解析请求方法用的消息码
	 */
	public int getRequestCode() {
		return this.requestInfo.getCode();
	}

	/**
	 * 执行转发工作
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_上午10:26:10
	 * @param session 相关的会话消息
	 */
	public void transpondExec(final RequestSession session) {
		this.requestInfo.exec(session, this.msg);
	}

	/**
	 * 得到请求的消息
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_上午10:26:10
	 * @return the code
	 */
	public RequestInfoBean getRequestInfo() {
		return this.requestInfo;
	}

	/**
	 * 得到消息对象
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_上午10:26:10
	 * @return the msg
	 */
	public BaseHttpMessageBean getMsg() {
		return this.msg;
	}

	/**
	 * 是否存在问题
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_上午10:26:10
	 * @return true，存在问题；<br />
	 *         false，不存在问题；<br />
	 */
	public boolean hasProblem() {
		return this.problemMsg.size() != 0;
	}

	/**
	 * 得到问题消息
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_上午10:26:10
	 * @return the problemMsg
	 */
	public Map<String, String> getProblemMsg() {
		return this.problemMsg;
	}

	/**
	 * 得到运行时间<br />
	 * 从该请求进入到相关顺序方法执行完成，所用的逻辑执行时间<br />
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_上午10:26:10
	 * @return 从该请求进入到相关顺序方法执行完成，所用的逻辑执行时间
	 */
	public long getRunningTime() {
		return System.currentTimeMillis() - this.inTime;
	}

	/**
	 * 显示连接信息<br />
	 * <请求码-接收的类:操作的方法(可能存在的参数存储对象)><br />
	 * 
	 * @author Weijie Xu
	 * @datetime 2016年4月27日_上午10:26:10
	 * @return 连接信息
	 */
	public String showLinkInfo() {
		return this.requestInfo.toString();
		// return "<" + this.code + "-" + this.requestInfo.toString() + ">";
	}
}
