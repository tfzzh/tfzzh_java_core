/**
 * @author Weijie Xu
 * @dateTime 2014-3-24 上午11:08:54
 */
package com.tfzzh.socket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tfzzh.socket.action.OutputMessageBean;
import com.tfzzh.socket.tools.ConnectionStatusEnum;
import com.tfzzh.socket.webservice.Handshake;
import com.tfzzh.tools.BaseBean;

/**
 * 请求来的会话
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-24 上午11:08:54
 */
public abstract class RequestSession extends BaseBean {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年4月10日 下午2:15:15
	 */
	private static final long serialVersionUID = 9142388982203345887L;

	/**
	 * 创建的时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月13日 下午1:42:22
	 */
	private final long createTime = System.currentTimeMillis();

	/**
	 * 属性集合
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-19 上午11:36:48
	 */
	private final Map<String, Object> attr;

	/**
	 * 上次保持连接的请求相关的毫秒数
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月11日 下午7:54:11
	 */
	private long lastKeepTime = 0;

	/**
	 * 类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月26日 下午3:08:11
	 */
	private SessionType type = null;

	/**
	 * 客户端连接类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月6日 下午5:57:32
	 */
	private ClientTypeEnum clientType = null;

	/**
	 * 相关的握手信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 下午4:23:02
	 */
	private Handshake hs = null;

	/**
	 * 自身消息打印缓存
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月19日 上午10:07:36
	 */
	private transient String strInfo = null;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-24 上午11:10:25
	 */
	protected RequestSession() {
		this.attr = new ConcurrentHashMap<>();
		this.refreshKeepTime();
	}

	/**
	 * 得到创建的时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月13日 下午1:43:03
	 * @return the createTime
	 */
	public long getCreateTime() {
		return this.createTime;
	}

	/**
	 * 放入一个属性
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-19 下午12:55:40
	 * @param key 对应的键
	 * @param value 目标值
	 */
	public void putAttr(final String key, final Object value) {
		this.attr.put(key, value);
	}

	/**
	 * 得到一个属性
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-19 下午12:55:37
	 * @param key 目标键
	 * @return 属性值
	 */
	public Object getAttr(final String key) {
		return this.attr.get(key);
	}

	/**
	 * 移除一个属性
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-19 下午12:55:33
	 * @param key 目标键
	 * @return 移除对应的值
	 */
	public Object removeAttr(final String key) {
		return this.attr.remove(key);
	}

	/**
	 * 得到类型所对应的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月26日 下午3:08:48
	 * @return the type
	 */
	public Object getTypeValue() {
		if (null == this.type) {
			return null;
		} else {
			return this.attr.get(this.type.getAttributeKey());
		}
	}

	/**
	 * 设置类型，及对应的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月26日 下午3:08:48
	 * @param type the type to set
	 * @param obj 类型对应的值
	 */
	public void setType(final SessionType type, final Object obj) {
		this.type = type;
		if (null != obj) {
			this.attr.put(this.type.getAttributeKey(), obj);
		}
	}

	/**
	 * 得到客户端连接类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月6日 下午6:36:58
	 * @return the clientType
	 */
	public ClientTypeEnum getClientType() {
		return this.clientType;
	}

	/**
	 * 是否通过了验证
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月27日 下午5:12:55
	 * @return 已经通过的前端类型；<br />
	 *         null，未通过验证；<br />
	 */
	public boolean isPass() {
		return null != this.clientType;
	}

	/**
	 * 设置握手信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月6日 下午6:27:42
	 * @param hs 握手信息
	 */
	public void setHandshakeInfo(final Handshake hs) {
		this.hs = hs.getBackHandshake();
		this.clientType = hs.getClientType();
	}

	/**
	 * 得到握手返回消息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月10日 下午4:25:24
	 * @return 握手返回消息
	 */
	public String getHandshakeBackMsg() {
		if (null == this.hs) {
			return null;
		}
		final String bak = this.hs.backMsg();
		this.hs = null;
		return bak;
	}

	/**
	 * 得到会话目标IP
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月10日 下午6:40:35
	 * @return 会话目标IP
	 */
	public abstract String getIp();

	/**
	 * 得到会话目标端口
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月10日 下午6:40:37
	 * @return 会话目标端口
	 */
	public abstract int getPort();

	/**
	 * 刷新最后保持连接状态请求的时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月11日 下午7:55:51
	 */
	public void refreshKeepTime() {
		this.lastKeepTime = System.currentTimeMillis();
	}

	/**
	 * 得到连接状态
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月11日 下午8:12:25
	 * @return 当前连接状态
	 */
	public ConnectionStatusEnum getConnectionStatus() {
		return ConnectionStatusEnum.getConnectionStatus(System.currentTimeMillis() - this.lastKeepTime);
	}

	/**
	 * 向目标发送消息，及时的
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月4日 下午7:42:43
	 * @param msg 准备发送的消息
	 * @return true，消息发送成功；<br />
	 *         false，消息发送失败；<br />
	 */
	public abstract boolean sendMessage(OutputMessageBean msg);

	/**
	 * 向目标发送消息，会在一个小时段内被集合为一个队列后，然后统一在一起发送<br />
	 * 优化时效性不高的传输时用<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年11月19日 下午2:53:16
	 * @param msg 准备发送的消息
	 * @return true，消息发送成功；<br />
	 *         false，消息发送失败；<br />
	 */
	public abstract boolean sendMessageForTask(OutputMessageBean msg);

	/**
	 * 进行关闭，以及进行一些清理操作<br />
	 * 且应带有关闭连接的操作，需要在子类的实现中进行<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-24 上午11:11:34
	 */
	public void close() {
		this.attr.clear();
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年6月26日 下午8:33:54
	 * @see com.tfzzh.tools.BaseBean#toString()
	 */
	@Override
	public String toString() {
		if (null == this.strInfo) {
			final StringBuilder sb = new StringBuilder();
			sb.append('{');
			if (null != this.type) {
				sb.append("type:").append(this.type.toString()).append(',');
			}
			sb.append("add:").append(this.getIp()).append(':').append(this.getPort());
			sb.append('}');
			this.strInfo = sb.toString();
		}
		return this.strInfo;
	}
}
