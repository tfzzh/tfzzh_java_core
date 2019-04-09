/**
 * @author Xu Weijie
 * @dateTime 2012-11-17 下午12:58:23
 */
package com.tfzzh.tools;

/**
 * 基础时间控制池
 * 
 * @author Xu Weijie
 * @dateTime 2012-11-17 下午12:58:23
 */
public abstract class BaseTimeoutPool implements Runnable {

	/**
	 * 是否运行中
	 * 
	 * @author xuweijie
	 * @dateTime 2012-11-17 下午2:35:35
	 */
	private boolean isRun = false;

	/**
	 * 是否停止状态
	 * 
	 * @author xuweijie
	 * @dateTime 2012-11-17 下午2:36:01
	 */
	private boolean isStop = true;

	/**
	 * 间隔时间15s
	 * 
	 * @author xuweijie
	 * @dateTime 2012-11-17 下午2:48:53
	 */
	protected int intervalTime;

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-11-17 下午12:52:48
	 */
	protected BaseTimeoutPool() {
		this(15000);
	}

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-11-17 下午12:52:07
	 * @param intervalTime 间隔时间
	 */
	protected BaseTimeoutPool(final int intervalTime) {
		this.intervalTime = intervalTime;
	}

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-11-17 下午12:50:09
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		this.beforeOperation();
		this.isRun = true;
		this.isStop = false;
		while (this.isRun) {
			this.operation();
		}
		this.isStop = true;
		this.afterOperation();
	}

	/**
	 * 进入循环操作前的控制
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-11-17 下午12:57:07
	 */
	protected void beforeOperation() {
	}

	/**
	 * 具体的操作内容<br />
	 * 循环操作<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-11-17 下午12:55:12
	 */
	protected abstract void operation();

	/**
	 * 跳出循环操作后的控制
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-11-17 下午12:57:09
	 */
	protected void afterOperation() {
	}

	/**
	 * 设置循环单次操作间的间隔时间
	 * 
	 * @author xuweijie
	 * @dateTime 2012-11-17 下午2:49:18
	 * @param intervalTime the intervalTime to set
	 */
	public void setIntervalTime(final int intervalTime) {
		this.intervalTime = intervalTime;
	}

	/**
	 * 停止该循环线程
	 * 
	 * @author xuweijie
	 * @dateTime 2012-11-17 下午2:36:46
	 */
	public void toStop() {
		this.isRun = false;
	}

	/**
	 * 该循环是否已经停止
	 * 
	 * @author xuweijie
	 * @dateTime 2012-11-17 下午2:37:13
	 * @return true，已经停止；<br />
	 *         false，还在运行中；<br />
	 */
	public boolean isStop() {
		return this.isStop;
	}
}
