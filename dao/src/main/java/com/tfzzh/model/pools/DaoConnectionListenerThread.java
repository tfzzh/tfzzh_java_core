/**
 * @author Weijie Xu
 * @dateTime 2015年4月22日 下午8:59:56
 */
package com.tfzzh.model.pools;

import java.util.HashMap;
import java.util.Map;

import com.tfzzh.model.tools.DaoConstants;

/**
 * Dao连接监听线程
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月22日 下午8:59:56
 */
public class DaoConnectionListenerThread extends Thread {

	/**
	 * 连接信息池
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午9:02:35
	 */
	private final Map<String, ConnectionInfoBean> infos = new HashMap<>();

	/**
	 * 是否为去到关闭状态
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 上午10:47:30
	 */
	private boolean toStop = false;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 上午10:49:35
	 */
	protected DaoConnectionListenerThread() {
	}

	/**
	 * 放入一个连接信息对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午9:03:22
	 * @param info 连接信息对象
	 */
	protected void putInfo(final ConnectionInfoBean info) {
		this.infos.put(info.getName(), info);
	}

	/**
	 * 移除一个连接信息对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午9:03:23
	 * @param name 连接信息对象名称
	 * @return true，有移除成功；<br />
	 *         false，不存在该名称连接信息对象；<br />
	 */
	protected boolean removeInfo(final String name) {
		return null != this.infos.remove(name);
	}

	/**
	 * 是否已经null了
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 上午10:34:44
	 * @return true，已经空了；<br />
	 *         false，还有东西；<br />
	 */
	protected boolean isEmpty() {
		return this.infos.size() == 0;
	}

	/**
	 * 运行方法
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午9:00:44
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (!this.toStop) {
			// 遍历列表
			// 默认等待时间为5分钟
			long waitTime = DaoConstants.BASE_CONNECTION_IDLE_TIME_OUT;
			for (final ConnectionInfoBean cib : this.infos.values()) {
				cib.validateCons();
				if (waitTime > cib.getIdelTime()) {
					waitTime = cib.getIdelTime();
				}
			}
			try {
				Thread.sleep(waitTime);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 去到停止流程
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 上午10:47:50
	 */
	protected void toStop() {
		this.toStop = true;
	}
}
