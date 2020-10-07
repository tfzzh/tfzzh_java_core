/**
 * @author Weijie Xu
 * @dateTime 2014-3-20 下午4:55:05
 */
package com.tfzzh.socket.bean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.tfzzh.socket.RequestSession;
import com.tfzzh.socket.tools.RequestPool;
import com.tfzzh.socket.tools.TfzzhDataInputStream;
import com.tfzzh.tools.BaseBean;

/**
 * 被请求进入的消息
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-20 下午4:55:05
 */
public class InputMessageBean extends BaseBean {

	/**
	 * @author 许纬杰
	 * @datetime 2016年4月27日_上午11:26:13
	 */
	private static final long serialVersionUID = 8765136254915079605L;

	/**
	 * 请求码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年5月9日 下午7:14:08
	 */
	private final int code;

	/**
	 * 请求的消息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午5:20:57
	 */
	private final RequestInfoBean requestInfo;

	/**
	 * 消息对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午5:21:05
	 */
	private final BaseSocketMessageBean msg;

	/**
	 * 进入时的时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午3:22:35
	 */
	private final long inTime;

	/**
	 * 有效的数据长度
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年11月1日 下午3:12:06
	 */
	private final int dataLen;

	/**
	 * 问题消息列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月9日 下午6:39:54
	 */
	private final Map<String, String> problemMsg = new LinkedHashMap<>();

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午5:21:25
	 * @param spaceRequestPool 所属空间请求池
	 * @param code 请求码
	 * @param bytes 预从中读取消息的字节流对象
	 * @param startTime 开始时间
	 * @throws IOException 抛
	 * @throws IllegalAccessException 抛
	 * @throws InstantiationException 抛
	 * @throws SecurityException 抛
	 * @throws NoSuchMethodException 抛
	 * @throws InvocationTargetException 抛
	 * @throws IllegalArgumentException 抛
	 */
	public InputMessageBean(final RequestPool spaceRequestPool, final int code, final byte[] bytes, final long startTime) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		this.code = code;
		this.requestInfo = spaceRequestPool.getRequestInfo(code);
		// 得到数据操作对象
		this.msg = (BaseSocketMessageBean) this.requestInfo.getBeanInstance();
		this.inTime = startTime;
		this.dataLen = bytes.length;
		if (null != this.msg) {
			// 封装
			final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			final TfzzhDataInputStream tdis = new TfzzhDataInputStream(bais);
			// 进行读取操作
			this.msg.read(tdis, this.problemMsg);
			try {
				// 关闭无效了的东西
				tdis.close();
				bais.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 得到请求码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年5月9日 下午7:14:49
	 * @return the code
	 */
	public int getCode() {
		return this.code;
	}

	/**
	 * 得到解析请求方法用的消息码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月31日 下午8:19:43
	 * @return 解析请求方法用的消息码
	 */
	public int getRequestCode() {
		return this.requestInfo.getCode();
	}

	/**
	 * 执行转发工作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月21日 下午2:13:06
	 * @param session 相关的会话消息
	 */
	public void transpondExec(final RequestSession session) {
		this.requestInfo.exec(session, this.msg);
	}

	/**
	 * 得到请求的消息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午5:24:07
	 * @return the code
	 */
	public RequestInfoBean getRequestInfo() {
		return this.requestInfo;
	}

	/**
	 * 得到消息对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午5:24:07
	 * @return the msg
	 */
	public BaseSocketMessageBean getMsg() {
		return this.msg;
	}

	/**
	 * 得到字节流长度
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年11月1日 下午3:15:55
	 * @return 字节流长度
	 */
	public int getDataLen() {
		return this.dataLen;
	}

	/**
	 * 是否存在问题
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月9日 下午6:43:26
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
	 * @dateTime 2014年4月9日 下午6:41:05
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
	 * @dateTime 2014年4月15日 下午3:24:56
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
	 * @dateTime 2014年6月20日 下午5:06:59
	 * @return 连接信息
	 */
	public String showLinkInfo() {
		return this.requestInfo.toString();
		// return "<" + this.code + "-" + this.requestInfo.toString() + ">";
	}
}
