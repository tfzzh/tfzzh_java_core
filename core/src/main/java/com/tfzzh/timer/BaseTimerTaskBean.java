/**
 * @author Weijie Xu
 * @dateTime 2014年4月15日 下午6:34:50
 */
package com.tfzzh.timer;

/**
 * 定时器基础类<br />
 * 可以设置起始运行时间点，每隔多久运行一次<br />
 * 
 * @author Weijie Xu
 * @dateTime 2014年4月15日 下午6:34:50
 */
public abstract class BaseTimerTaskBean {

	/**
	 * 最终可运行到的时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午6:49:31
	 */
	private final long finalRunTime;

	/**
	 * 计划运行次数
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午6:43:50
	 */
	private final int runTimes;

	/**
	 * 当前已经运行的次数
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午6:45:30
	 */
	private int times = 0;

	/**
	 * 下次运行时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午6:45:57
	 */
	private long nextRunTime;

	/**
	 * 去到停止
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月18日 下午2:10:34
	 */
	private boolean toStop = false;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午6:53:20
	 * @param firstRunTime 首次执行时间
	 * @param finalRunTime 最终可运行到的时间
	 * @param runTimes 可被执行的次数
	 */
	public BaseTimerTaskBean(final long firstRunTime, final long finalRunTime, final int runTimes) {
		this.nextRunTime = firstRunTime;
		this.finalRunTime = finalRunTime;
		this.runTimes = runTimes;
	}

	/**
	 * 设置下次运行时间<br />
	 * 该方法在每次被执行后，被调用<br />
	 * 中间需要通过调用{@Method setNextRunTime}来设置计算得到的下次运行时间<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午6:54:32
	 */
	protected abstract void resetNextRunTime();

	/**
	 * 设置下次运行时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午7:11:13
	 * @param time 时间
	 */
	protected void setNextRunTime(final long time) {
		this.nextRunTime = time;
	}

	/**
	 * 去到下一次
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午7:05:58
	 * @return 执行状态
	 */
	protected TimerExecStatusEnum toNext() {
		if (this.toStop) {
			return TimerExecStatusEnum.BeStop;
		}
		// 先判定次数限制
		if (++this.times >= this.runTimes) {
			// 当前已经达到需求的最大执行次数
			return TimerExecStatusEnum.Full;
		}
		// 运算下次执行时间
		this.resetNextRunTime();
		if (this.nextRunTime > this.finalRunTime) {
			// 因为当前时间
			return TimerExecStatusEnum.Expired;
		}
		return TimerExecStatusEnum.Running;
	}

	/**
	 * 得到下次运行时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午6:55:03
	 * @return 运行时间
	 */
	public long getNextRunTime() {
		return this.nextRunTime;
	}

	/**
	 * 到时需要被执行的方法
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午6:57:27
	 */
	protected abstract void exec();

	/**
	 * 去到停止
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月18日 下午2:11:00
	 */
	public void toStop() {
		this.toStop = true;
	}

	/**
	 * 在结束时的操作<br />
	 * 为了抽象方法<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月18日 下午2:14:45
	 * @param status 结束时的状态
	 */
	protected void inOver(final TimerExecStatusEnum status) {
	}

	/**
	 * 因为系统被关闭
	 * 
	 * @author XuWeijie
	 * @datetime 2015年10月12日_下午5:11:22
	 */
	protected void inSysExit() {
	}
}
