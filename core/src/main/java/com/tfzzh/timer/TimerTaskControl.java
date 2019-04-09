/**
 * @author Weijie Xu
 * @dateTime 2014年4月15日 下午6:30:41
 */
package com.tfzzh.timer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.tfzzh.log.CoreLog;

/**
 * 时间控制器
 * 
 * @author Weijie Xu
 * @dateTime 2014年4月15日 下午6:30:41
 */
public class TimerTaskControl implements Runnable {

	/**
	 * 定时器列表 <时间段, 该时段需要执行的定时器任务列表>
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午7:15:37
	 */
	private final NavigableMap<Long, List<BaseTimerTaskBean>> tasks = new TreeMap<>();

	/**
	 * 任务所相关执行时间key
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月18日_下午1:53:48
	 */
	private final Map<BaseTimerTaskBean, Long> taskTimeList = new HashMap<>();

	/**
	 * 运行中的线程
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月18日 下午6:40:27
	 */
	private Thread runThread = null;

	/**
	 * 时间间隔
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午7:19:35
	 */
	private final long timeInterval;

	/**
	 * 下次运行时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午7:15:30
	 */
	private long nextRunTime;

	/**
	 * 是否在停止状态
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午7:16:00
	 */
	private boolean isStop = false;

	/**
	 * 是否已经结束
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午7:16:01
	 */
	private boolean isOver = false;

	/**
	 * 锁
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午7:17:19
	 */
	private final Object lock = new Object();

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午7:20:28
	 * @param timeInterval 时间间隔
	 */
	public TimerTaskControl(final long timeInterval) {
		this.timeInterval = timeInterval;
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午6:31:07
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		this.runThread = Thread.currentThread();
		this.isStop = false;
		while (!this.isStop) {
			if (this.tasks.size() > 0) {
				synchronized (this.lock) {
					final long l = (System.currentTimeMillis() + 1) / this.timeInterval;
					long f = this.tasks.firstKey();
					// 定时器执行状态
					while (l >= f) {
						// 开始执行其中的任务
						final List<BaseTimerTaskBean> tasks = this.tasks.remove(f);
						for (final BaseTimerTaskBean t : tasks) {
							// 这里使用线程方式处理，以便放置有独立任务被卡死时，会连带其他任务不可被继续执行的问题
							// 移除时间点中任务
							this.taskTimeList.remove(t);
							new Thread(new Runnable() {

								@Override
								public void run() {
									try {
										t.exec();
									} catch (final Exception e) {
										e.printStackTrace();
									}
									final TimerExecStatusEnum ts = t.toNext();
									switch (ts) {
									case Running:
										TimerTaskControl.this.putTask(t, true);
										break;
									default:
										t.inOver(ts);
										break;
									}
								}
							}).start();
						}
						if (this.tasks.size() == 0) {
							break;
						} else {
							f = this.tasks.firstKey();
						}
					}
					if (this.tasks.size() > 0) {
						this.nextRunTime = this.tasks.firstKey() * this.timeInterval;
					} else {
						this.nextRunTime = 0;
					}
				}
			}
			try {
				// 进行时间等待
				final long waitTime = this.nextRunTime - System.currentTimeMillis();
				if (waitTime < 0) {
					this.nextRunTime = 0;
					Thread.sleep(Long.MAX_VALUE);
					if (CoreLog.getInstance().debugEnabled(TimerTaskControl.class)) {
						CoreLog.getInstance().debug(TimerTaskControl.class, "waitTime >> ", Long.toString(Long.MAX_VALUE));
					}
				} else {
					Thread.sleep(waitTime);
					if (CoreLog.getInstance().debugEnabled(TimerTaskControl.class)) {
						CoreLog.getInstance().debug(TimerTaskControl.class, "waitTime >> ", Long.toString(waitTime));
					}
				}
			} catch (final InterruptedException e) {
			}
		}
		// 对当前存在的任务，一个个进行系统关闭的方法调用
		for (final List<BaseTimerTaskBean> tasks : this.tasks.values()) {
			for (final BaseTimerTaskBean t : tasks) {
				t.inSysExit();
			}
		}
		this.isOver = true;
	}

	/**
	 * 放入一个时间任务
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月18日 下午7:56:55
	 * @param task 时间任务
	 */
	public void putTask(final BaseTimerTaskBean task) {
		this.putTask(task, true);
	}

	/**
	 * 放入一个时间任务
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午7:55:46
	 * @param task 时间任务
	 * @param canWeekup 是否可被唤醒
	 */
	private void putTask(final BaseTimerTaskBean task, final boolean canWeekup) {
		synchronized (this.lock) {
			if (task.getNextRunTime() <= 0) {
				return;
			}
			final Long oldKey = this.taskTimeList.get(task);
			final long runTime = (task.getNextRunTime() + 1) / this.timeInterval;
			if (null != oldKey) {
				// 需要考虑是否移除
				if (oldKey.longValue() == runTime) {
					// 因为存在，且执行时间点相同，不用继续了
					return;
				} else {
					// 因为执行时间点不同，需要从之前的队列中进行移除
					final List<BaseTimerTaskBean> list = this.tasks.get(oldKey);
					if (null != list) {
						list.remove(task);
					}
				}
			}
			// 放入新的任务
			List<BaseTimerTaskBean> list = this.tasks.get(runTime);
			if (null == list) {
				list = new LinkedList<>();
				this.tasks.put(runTime, list);
			}
			list.add(task);
			this.taskTimeList.put(task, runTime);
			if (CoreLog.getInstance().debugEnabled(TimerTaskControl.class)) {
				CoreLog.getInstance().debug(TimerTaskControl.class, "add Task >> ", Long.toString(runTime));
			}
			if (canWeekup && ((this.nextRunTime == 0) || ((task.getNextRunTime() + 1) < this.nextRunTime))) {
				// 需要唤醒run的相关线程，并进行从新休眠
				// this.runThread.notify();
				if (null != this.runThread) {
					this.runThread.interrupt();
					if (CoreLog.getInstance().debugEnabled(TimerTaskControl.class)) {
						CoreLog.getInstance().debug(TimerTaskControl.class, "weekup");
					}
				}
			}
		}
	}

	/**
	 * 得到定时器列表
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月14日_上午10:49:32
	 * @return the tasks
	 */
	public NavigableMap<Long, List<BaseTimerTaskBean>> getTasks() {
		return this.tasks;
	}

	/**
	 * 得到时间间隔
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月14日_上午10:50:29
	 * @return the timeInterval
	 */
	public long getTimeInterval() {
		return this.timeInterval;
	}

	/**
	 * 得到下次运行时间
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月14日_上午10:50:29
	 * @return the nextRunTime
	 */
	public long getNextRunTime() {
		return this.nextRunTime;
	}

	/**
	 * 进行停止操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午7:24:03
	 */
	public void toStop() {
		synchronized (this.lock) {
			this.isStop = true;
		}
	}

	/**
	 * 是否已经结束
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月15日 下午7:24:28
	 * @return true，已经结束；<br />
	 *         false，还未结束；<br />
	 */
	public boolean isOver() {
		return this.isOver;
	}
}
