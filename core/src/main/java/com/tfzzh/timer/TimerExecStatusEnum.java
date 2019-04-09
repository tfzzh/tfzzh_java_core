/**
 * @author Weijie Xu
 * @dateTime 2014年4月15日 下午7:01:46
 */
package com.tfzzh.timer;

/**
 * 定时器执行状态
 * 
 * @author Weijie Xu
 * @dateTime 2014年4月15日 下午7:01:46
 */
public enum TimerExecStatusEnum {
	/**
	 * 被停止
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月18日 下午2:12:43
	 */
	BeStop,
	/**
	 * 运行中
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午7:03:23
	 */
	Running,
	/**
	 * 可执行次数已满
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午7:03:24
	 */
	Full,
	/**
	 * 已经达到可运行到的最大时间（过期）
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午7:03:25
	 */
	Expired,
	/**
	 * 因为系统关闭
	 * 
	 * @author XuWeijie
	 * @datetime 2015年10月12日_下午5:10:30
	 */
	SysExit;
}
