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
	 * @param msg log内容
	 */
	@Override
	public void fatal(final String... msg) {
		this.fatal(this.getClass(), msg);
	}

	/**
	 * 写入fatal级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:37:06
	 * @param clz 所相关操作类对象
	 * @param msg 日志内容
	 */
	@Override
	public void fatal(final Class<?> clz, final String... msg) {
		final Logger log = this.getLog(clz);
		if (log.isFatalEnabled()) {
			log.fatal(this.compositeMsg(msg));
		}
	}

	/**
	 * 写入error级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:28:58
	 * @param msg log内容
	 */
	@Override
	public void error(final String... msg) {
		this.error(this.getClass(), msg);
	}

	/**
	 * 写入error级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:37:06
	 * @param clz 所相关操作类对象
	 * @param msg 日志内容
	 */
	@Override
	public void error(final Class<?> clz, final String... msg) {
		final Logger log = this.getLog(clz);
		if (log.isErrorEnabled()) {
			log.error(this.compositeMsg(msg));
		}
	}

	/**
	 * 写入warn级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:28:58
	 * @param msg log内容
	 */
	@Override
	public void warn(final String... msg) {
		this.warn(this.getClass(), msg);
	}

	/**
	 * 写入warn级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:37:06
	 * @param clz 所相关操作类对象
	 * @param msg 日志内容
	 */
	@Override
	public void warn(final Class<?> clz, final String... msg) {
		final Logger log = this.getLog(clz);
		if (log.isWarnEnabled()) {
			log.warn(this.compositeMsg(msg));
		}
	}

	/**
	 * 写入info级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:28:58
	 * @param msg log内容
	 */
	@Override
	public void info(final String... msg) {
		this.info(this.getClass(), msg);
	}

	/**
	 * 写入info级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:37:06
	 * @param clz 所相关操作类对象
	 * @param msg 日志内容
	 */
	@Override
	public void info(final Class<?> clz, final String... msg) {
		final Logger log = this.getLog(clz);
		if (log.isInfoEnabled()) {
			log.info(this.compositeMsg(msg));
		}
	}

	/**
	 * 写入debug级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:28:58
	 * @param msg log内容
	 */
	@Override
	public void debug(final String... msg) {
		this.debug(this.getClass(), msg);
	}

	/**
	 * 写入debug级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:37:06
	 * @param clz 所相关操作类对象
	 * @param msg 日志内容
	 */
	@Override
	public void debug(final Class<?> clz, final String... msg) {
		final Logger log = this.getLog(clz);
		if (log.isDebugEnabled()) {
			log.debug(this.compositeMsg(msg));
		}
	}

	/**
	 * 写入trace级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:28:58
	 * @param msg log内容
	 */
	@Override
	public void trace(final String... msg) {
		this.trace(this.getClass(), msg);
	}

	/**
	 * 写入trace级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:37:06
	 * @param clz 所相关操作类对象
	 * @param msg 日志内容
	 */
	@Override
	public void trace(final Class<?> clz, final String... msg) {
		final Logger log = this.getLog(clz);
		if (log.isTraceEnabled()) {
			log.trace(this.compositeMsg(msg));
		}
	}

	/**
	 * 组合内容消息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年7月11日 下午3:27:14
	 * @param msg 消息集合
	 * @return 组合后的消息
	 */
	private StringBuilder compositeMsg(final String... msg) {
		final StringBuilder sb = new StringBuilder(msg.length * 16);
		for (final String element : msg) {
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
