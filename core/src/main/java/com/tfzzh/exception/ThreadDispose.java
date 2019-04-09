/**
 * @author Weijie Xu
 * @dateTime 2015年3月6日 下午7:25:09
 */
package com.tfzzh.exception;

/**
 * 线程的特殊处理
 * 
 * @author Weijie Xu
 * @dateTime 2015年3月6日 下午7:25:09
 */
public interface ThreadDispose {

	/**
	 * 针对异常的处理
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月6日 下午7:29:27
	 * @param t 目标线程
	 * @param e 目标异常
	 */
	void exceptionDispose(Thread t, Throwable e);
}
