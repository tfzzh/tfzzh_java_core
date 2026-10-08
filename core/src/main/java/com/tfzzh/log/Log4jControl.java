/**
 * @author Weijie Xu
 * @dateTime 2017年3月23日 上午10:17:13
 */
package com.tfzzh.log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 基于Log4j的控制实现
 * 
 * @author Weijie Xu
 * @dateTime 2017年3月23日 上午10:17:13
 */
public class Log4jControl implements LogControl {

	/**
	 * log列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:21:04
	 */
	private final Map<Class<?>, Logger> logs = new ConcurrentHashMap<>();

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:25:21
	 */
	public Log4jControl() {
		this.init();
	}

	/**
	 * 一些初始化操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:27:41
	 */
	private void init() {
		// 暂无内容
	}

	/**
	 * 得到所相关日志模型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月8日 下午3:17:48
	 * @param clz 所相关类对象
	 * @return 所相关日志模型
	 */
	private Logger getLog(final Class<?> clz) {
		Logger log = this.logs.get(clz);
		if (null == log) {
			synchronized (this.logs) {
				if (null == log) {
					log = LogManager.getLogger(clz);
					this.logs.put(clz, log);
				}
			}
		}
		return log;
	}

	/**
	 * 写入fatal级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:28:58
	 * @param msgObj log内容
	 */
	@Override
	public void fatal(final Object... msgObj) {
		final StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts.length > 2) {
			try {
				this.fatal(Class.forName(sts[2].getClassName()), msgObj);
			} catch (final ClassNotFoundException e) {
				this.fatal(this.getClass(), msgObj);
			}
		} else {
			this.fatal(this.getClass(), msgObj);
		}
	}

	/**
	 * 写入fatal级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:37:06
	 * @param clz 所相关操作类对象
	 * @param msgObj 日志内容
	 */
	@Override
	public void fatal(final Class<?> clz, final Object... msgObj) {
		final Logger log = this.getLog(clz);
		if (log.isFatalEnabled()) {
			log.fatal(this.compositeMsg(msgObj));
		}
	}

	/**
	 * 写入error级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:28:58
	 * @param msgObj log内容
	 */
	@Override
	public void error(final Object... msgObj) {
		final StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts.length > 2) {
			try {
				this.error(Class.forName(sts[2].getClassName()), msgObj);
			} catch (final ClassNotFoundException e) {
				this.error(this.getClass(), msgObj);
			}
		} else {
			this.error(this.getClass(), msgObj);
		}
	}

	/**
	 * 写入error级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:37:06
	 * @param clz 所相关操作类对象
	 * @param msgObj 日志内容
	 */
	@Override
	public void error(final Class<?> clz, final Object... msgObj) {
		final Logger log = this.getLog(clz);
		if (log.isErrorEnabled()) {
			log.error(this.compositeMsg(msgObj));
		}
	}

	/**
	 * 写入warn级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:28:58
	 * @param msgObj log内容
	 */
	@Override
	public void warn(final Object... msgObj) {
		final StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts.length > 2) {
			try {
				this.warn(Class.forName(sts[2].getClassName()), msgObj);
			} catch (final ClassNotFoundException e) {
				this.warn(this.getClass(), msgObj);
			}
		} else {
			this.warn(this.getClass(), msgObj);
		}
	}

	/**
	 * 写入warn级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:37:06
	 * @param clz 所相关操作类对象
	 * @param msgObj 日志内容
	 */
	@Override
	public void warn(final Class<?> clz, final Object... msgObj) {
		final Logger log = this.getLog(clz);
		if (log.isWarnEnabled()) {
			log.warn(this.compositeMsg(msgObj));
		}
	}

	/**
	 * 写入info级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:28:58
	 * @param msgObj log内容
	 */
	@Override
	public void info(final Object... msgObj) {
		final StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts.length > 2) {
			try {
				this.info(Class.forName(sts[2].getClassName()), msgObj);
			} catch (final ClassNotFoundException e) {
				this.info(this.getClass(), msgObj);
			}
		} else {
			this.info(this.getClass(), msgObj);
		}
	}

	/**
	 * 写入info级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:37:06
	 * @param clz 所相关操作类对象
	 * @param msgObj 日志内容
	 */
	@Override
	public void info(final Class<?> clz, final Object... msgObj) {
		final Logger log = this.getLog(clz);
		if (log.isInfoEnabled()) {
			log.info(this.compositeMsg(msgObj));
		}
	}

	/**
	 * 写入debug级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:28:58
	 * @param msgObj log内容
	 */
	@Override
	public void debug(final Object... msgObj) {
		final StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts.length > 2) {
			try {
				this.debug(Class.forName(sts[2].getClassName()), msgObj);
			} catch (final ClassNotFoundException e) {
				this.debug(this.getClass(), msgObj);
			}
		} else {
			this.debug(this.getClass(), msgObj);
		}
	}

	/**
	 * 写入debug级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:37:06
	 * @param clz 所相关操作类对象
	 * @param msgObj 日志内容
	 */
	@Override
	public void debug(final Class<?> clz, final Object... msgObj) {
		final Logger log = this.getLog(clz);
		if (log.isDebugEnabled()) {
			log.debug(this.compositeMsg(msgObj));
		}
	}

	/**
	 * 写入trace级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:28:58
	 * @param msgObj log内容
	 */
	@Override
	public void trace(final Object... msgObj) {
		final StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts.length > 2) {
			try {
				this.trace(Class.forName(sts[2].getClassName()), msgObj);
			} catch (final ClassNotFoundException e) {
				this.trace(this.getClass(), msgObj);
			}
		} else {
			this.trace(this.getClass(), msgObj);
		}
	}

	/**
	 * 写入trace级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:37:06
	 * @param clz 所相关操作类对象
	 * @param msgObj 日志内容
	 */
	@Override
	public void trace(final Class<?> clz, final Object... msgObj) {
		final Logger log = this.getLog(clz);
		if (log.isTraceEnabled()) {
			log.trace(this.compositeMsg(msgObj));
		}
	}

	/**
	 * 组合内容消息
	 * 内容数量超过1时：如果第0位内容中包含“{}”，且“{}”的数量大于等于其他位内容数量（msgObj长度-1），
	 * 则以占位替换方式组合，即将第1位起的内容依次替换入第0位内容的“{}”占位中；
	 * 否则按原有顺序拼接方式组合。
	 *
	 * @author Weijie Xu
	 * @dateTime 2026年9月23日 下午4:05:00
	 * @param msgObj 消息集合
	 * @return 组合后的消息
	 */
	private StringBuilder compositeMsg(final Object... msgObj) {
		if (msgObj.length > 1) {
			final String first = String.valueOf(msgObj[0]);
			// 统计第0位内容中“{}”占位数量
			int point = 0;
			int count = 0;
			while ((point = first.indexOf("{}", point)) != -1) {
				count++;
				point += 2;
			}
			if (count >= (msgObj.length - 1)) {
				// 占位替换方式：依次将第1位起的内容替换入对应“{}”占位
				final StringBuilder sb = new StringBuilder(first);
				point = 0;
				for (int i = 1; i < msgObj.length; i++) {
					final String val = String.valueOf(msgObj[i]);
					final int p = sb.indexOf("{}", point);
					if (p == -1) {
						// 理论由前置数量校验保证不会进入，此处防御处理
						sb.append(' ').append(val);
					} else {
						sb.replace(p, p + 2, val);
						point = p + val.length();
					}
				}
				return sb;
			}
		}
		final StringBuilder sb = new StringBuilder(msgObj.length * 16);
		for (final Object element : msgObj) {
			sb.append(element);
			if (sb.lastIndexOf(" ") != (sb.length() - 1)) {
				sb.append(' ');
			}
		}
		return sb;
	}

	/**
	 * fatal级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月5日 下午6:04:13
	 * @return true，有开启该级别日志
	 * @see com.tfzzh.log.LogControl#fatalEnabled()
	 */
	@Override
	public boolean fatalEnabled() {
		return this.fatalEnabled(this.getClass());
	}

	/**
	 * fatal级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午2:19:36
	 * @param clz 所相关操作类对象
	 * @return true，存在该级别
	 * @see com.tfzzh.log.LogControl#fatalEnabled(java.lang.Class)
	 */
	@Override
	public boolean fatalEnabled(final Class<?> clz) {
		final Logger log = this.getLog(clz);
		return log.isFatalEnabled();
	}

	/**
	 * error级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月5日 下午6:04:13
	 * @return true，有开启该级别日志
	 * @see com.tfzzh.log.LogControl#errorEnabled()
	 */
	@Override
	public boolean errorEnabled() {
		return this.errorEnabled(this.getClass());
	}

	/**
	 * error级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午2:21:57
	 * @param clz 所相关操作类对象
	 * @return true，存在该级别
	 * @see com.tfzzh.log.LogControl#errorEnabled(java.lang.Class)
	 */
	@Override
	public boolean errorEnabled(final Class<?> clz) {
		final Logger log = this.getLog(clz);
		return log.isErrorEnabled();
	}

	/**
	 * error级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月5日 下午6:04:13
	 * @return true，存在该级别
	 * @see com.tfzzh.log.LogControl#warnEnabled()
	 */
	@Override
	public boolean warnEnabled() {
		return this.warnEnabled(this.getClass());
	}

	/**
	 * error级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午2:21:57
	 * @param clz 所相关操作类对象
	 * @return true，存在该级别
	 * @see com.tfzzh.log.LogControl#warnEnabled(java.lang.Class)
	 */
	@Override
	public boolean warnEnabled(final Class<?> clz) {
		final Logger log = this.getLog(clz);
		return log.isWarnEnabled();
	}

	/**
	 * info级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月5日 下午6:04:13
	 * @return true，有开启该级别日志
	 * @see com.tfzzh.log.LogControl#infoEnabled()
	 */
	@Override
	public boolean infoEnabled() {
		return this.infoEnabled(this.getClass());
	}

	/**
	 * info级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午2:21:57
	 * @param clz 所相关操作类对象
	 * @return true，存在该级别
	 * @see com.tfzzh.log.LogControl#infoEnabled(java.lang.Class)
	 */
	@Override
	public boolean infoEnabled(final Class<?> clz) {
		final Logger log = this.getLog(clz);
		return log.isInfoEnabled();
	}

	/**
	 * debug级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月5日 下午6:04:13
	 * @return true，有开启该级别日志
	 * @see com.tfzzh.log.LogControl#debugEnabled()
	 */
	@Override
	public boolean debugEnabled() {
		return this.debugEnabled(this.getClass());
	}

	/**
	 * debug级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午2:21:57
	 * @param clz 所相关操作类对象
	 * @return true，存在该级别
	 * @see com.tfzzh.log.LogControl#debugEnabled(java.lang.Class)
	 */
	@Override
	public boolean debugEnabled(final Class<?> clz) {
		final Logger log = this.getLog(clz);
		return log.isDebugEnabled();
	}

	/**
	 * trace级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月5日 下午6:04:13
	 * @return true，有开启该级别日志
	 * @see com.tfzzh.log.LogControl#traceEnabled()
	 */
	@Override
	public boolean traceEnabled() {
		return this.traceEnabled(this.getClass());
	}

	/**
	 * trace级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午2:21:57
	 * @param clz 所相关操作类对象
	 * @return true，存在该级别
	 * @see com.tfzzh.log.LogControl#traceEnabled(java.lang.Class)
	 */
	@Override
	public boolean traceEnabled(final Class<?> clz) {
		final Logger log = this.getLog(clz);
		return log.isTraceEnabled();
	}
}
