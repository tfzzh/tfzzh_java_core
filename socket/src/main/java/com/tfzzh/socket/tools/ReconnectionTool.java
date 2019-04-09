/**
 * @author Weijie Xu
 * @dateTime 2015年3月6日 下午6:32:39
 */
package com.tfzzh.socket.tools;

import com.tfzzh.exception.TfzzhThreadGroup;
import com.tfzzh.exception.ThreadDispose;

/**
 * 断链重连工具<br />
 * 默认重试间隔时间为5s<br />
 * 
 * @author Weijie Xu
 * @dateTime 2015年3月6日 下午6:32:39
 */
public class ReconnectionTool implements ThreadDispose {

	/**
	 * 有线程相关的连接对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月6日 下午6:33:52
	 */
	private final Runnable rab;

	/**
	 * 线程组控制
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月6日 下午8:36:44
	 */
	private final TfzzhThreadGroup ttg;

	/**
	 * 开始的时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月6日 下午7:33:47
	 */
	private long startTime;

	/**
	 * 重试时间间隔，默认为5s
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月6日 下午7:34:40
	 */
	private int retryInterval = 5000;

	/**
	 * 当前重试次数
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月6日 下午7:38:04
	 */
	private int retryTimes = 0;

	/**
	 * 是否继续重试
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月7日 下午2:50:55
	 */
	private boolean isContinue = true;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年3月6日 下午6:40:32
	 * @param linkInfo 有线程相关的连接对象
	 */
	public ReconnectionTool(final Runnable linkInfo) {
		this.ttg = new TfzzhThreadGroup("ReconnectionTool", this);
		this.rab = linkInfo;
	}

	/**
	 * 得到重试时间间隔
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月6日 下午7:39:02
	 * @return the retryInterval
	 */
	public int getRetryInterval() {
		return this.retryInterval;
	}

	/**
	 * 设置重试时间间隔，默认为5s
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月6日 下午7:39:02
	 * @param retryInterval the retryInterval to set
	 */
	public void setRetryInterval(final int retryInterval) {
		this.retryInterval = retryInterval;
	}

	/**
	 * 得到开始的时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月6日 下午7:39:02
	 * @return the startTime
	 */
	public long getStartTime() {
		return this.startTime;
	}

	/**
	 * 得到当前重试次数
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月6日 下午7:39:02
	 * @return the retryTimes
	 */
	public int getRetryTimes() {
		return this.retryTimes;
	}

	/**
	 * 是否继续重试
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月7日 下午4:37:47
	 * @return the isContinue
	 */
	public boolean isContinue() {
		return this.isContinue;
	}

	/**
	 * 设置是否继续重试
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月7日 下午4:37:47
	 * @param isContinue the isContinue to set
	 */
	public void setContinue(final boolean isContinue) {
		this.isContinue = isContinue;
	}

	/**
	 * 是否已经结束了
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月7日 下午4:37:47
	 * @return the isOver
	 */
	public boolean isOver() {
		return this.ttg.isDestroyed();
	}

	/**
	 * 进行连接，一定是独立存在的部分
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月6日 下午6:39:27
	 */
	public void connect() {
		this.startTime = System.currentTimeMillis();
		new Thread(this.ttg, this.rab).start();
	}

	/**
	 * 针对异常的处理
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月6日 下午7:30:31
	 * @param t 目标线程
	 * @param e 目标异常
	 * @see com.tfzzh.exception.ThreadDispose#exceptionDispose(java.lang.Thread, java.lang.Throwable)
	 */
	@Override
	public void exceptionDispose(final Thread t, final Throwable e) {
		if (!this.isContinue) {
			// 如果被要求不需要继续了，就直接退出
			return;
		}
		// 此方法每次被调用时，
		try {
			// 进入时间等待
			Thread.sleep(this.retryInterval);
		} catch (final InterruptedException e1) {
			e1.printStackTrace();
		}
		// 增加计次
		this.retryTimes++;
		// 开始从连
		new Thread(this.ttg, this.rab).start();
	}
}
