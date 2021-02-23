/**
 * @author Weijie Xu
 * @dateTime 2015年4月22日 下午9:01:24
 */
package com.tfzzh.model.pools;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Dao相关监听池
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月22日 下午9:01:24
 */
public class DaoListenerPool {

	/**
	 * 连接监听线程
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 上午9:40:44
	 */
	private DaoConnectionListenerThread dclt = null;

	/**
	 * 线程相关的控制锁
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 上午10:08:12
	 */
	private final Lock threadLook = new ReentrantLock();

	/**
	 * 对象唯一实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 上午9:38:03
	 */
	private static final DaoListenerPool pool = new DaoListenerPool();

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 上午9:38:00
	 */
	private DaoListenerPool() {
	}

	/**
	 * 得到对象实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 上午9:37:57
	 * @return 对象唯一实例
	 */
	public static DaoListenerPool getInstance() {
		return DaoListenerPool.pool;
	}

	/**
	 * 放入一个连接信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 上午10:05:14
	 * @param info 连接信息
	 */
	public void putConnectionInfo(final ConnectionInfoBean info) {
		// 首先判定是否存在相关线程
		this.threadLook.lock();
		try {
			if (null == this.dclt) {
				// 因为不存在需要创建
				this.dclt = new DaoConnectionListenerThread();
				// 启动
				this.dclt.start();
			}
		} finally {
			this.threadLook.unlock();
		}
		// 将数据放入
		this.dclt.putInfo(info);
	}

	/**
	 * 移除一个连接信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 上午10:06:13
	 * @param info 连接信息
	 */
	public void removeConnectionInfo(final ConnectionInfoBean info) {
		this.threadLook.lock();
		// 移除目标
		this.dclt.removeInfo(info.getName());
		try {
			if (this.dclt.isEmpty()) {
				// 如果此时数据为null，则关闭该线程
				this.dclt.toStop();
				this.dclt = null;
			}
		} finally {
			this.threadLook.unlock();
		}
	}
}
