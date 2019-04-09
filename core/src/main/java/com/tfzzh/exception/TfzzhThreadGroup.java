/**
 * @author Weijie Xu
 * @dateTime 2015年3月6日 下午6:59:40
 */
package com.tfzzh.exception;

/**
 * 线程组控制
 * 
 * @author Weijie Xu
 * @dateTime 2015年3月6日 下午6:59:40
 */
public class TfzzhThreadGroup extends ThreadGroup {

	/**
	 * 线程控制
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月6日 下午8:13:20
	 */
	private final ThreadDispose td;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年3月6日 下午6:59:47
	 * @param name 线程组名称
	 * @param td 线程控制实现
	 */
	public TfzzhThreadGroup(final String name, final ThreadDispose td) {
		super(name);
		this.td = td;
	}

	/**
	 * 被抛出的异常的捕获
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月6日 下午7:14:38
	 * @param t 目标线程
	 * @param e 发生的异常
	 * @see java.lang.ThreadGroup#uncaughtException(java.lang.Thread, java.lang.Throwable)
	 */
	@Override
	public void uncaughtException(final Thread t, final Throwable e) {
		if (null != this.td) {
			// 进行操作转发
			this.td.exceptionDispose(t, e);
		}
	}
}
