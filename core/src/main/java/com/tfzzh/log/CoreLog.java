/**
 * @author Weijie Xu
 * @dateTime 2017年3月23日 上午9:40:25
 */
package com.tfzzh.log;

/**
 * 核心log控制对象，单例模型
 * 
 * @author Weijie Xu
 * @dateTime 2017年3月23日 上午9:40:25
 */
public class CoreLog {

	/**
	 * log控制实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午9:47:54
	 */
	private LogControl log = null;

	/**
	 * 对象唯一实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午9:48:29
	 */
	private static final CoreLog lp = new CoreLog();

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午9:48:27
	 */
	public CoreLog() {
		this.init();
	}

	/**
	 * 一些初始化操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午9:48:31
	 */
	private void init() {
	}

	/**
	 * 得到对象实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午9:49:51
	 * @return 对象唯一实例
	 */
	public static CoreLog getInstance() {
		return CoreLog.lp;
	}

	/**
	 * 放入Log控制实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:09:08
	 * @param log Log控制实例
	 */
	public void putLog(final LogControl log) {
		this.log = log;
	}

	/**
	 * 放入log信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:15:22
	 * @param lvl log级别
	 * @param msgObj log内容
	 */
	public void putLogMsg(final LogLevel lvl, final Object... msgObj) {
		final StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		Class<?> clz = null;
		if (sts.length > 2) {
			try {
				clz = Class.forName(sts[2].getClassName());
			} catch (final ClassNotFoundException e) {
			}
		}
		switch (lvl) {
		case FATAL:
			if (null != clz) {
				this.fatal(clz, msgObj);
			} else {
				this.fatal(msgObj);
			}
			break;
		case ERROR:
			if (null != clz) {
				this.error(clz, msgObj);
			} else {
				this.error(msgObj);
			}
			break;
		case WARN:
			if (null != clz) {
				this.warn(clz, msgObj);
			} else {
				this.warn(msgObj);
			}
			break;
		case INFO:
			if (null != clz) {
				this.info(clz, msgObj);
			} else {
				this.info(msgObj);
			}
			break;
		case DEBUG:
			if (null != clz) {
				this.debug(clz, msgObj);
			} else {
				this.debug(msgObj);
			}
			break;
		case TRACE:
			if (null != clz) {
				this.trace(clz, msgObj);
			} else {
				this.trace(msgObj);
			}
			break;
		}
	}

	/**
	 * 放入log信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:15:23
	 * @param lvl log级别
	 * @param clz 所相关操作类对象
	 * @param msgObj log内容
	 */
	public void putLogMsg(final LogLevel lvl, final Class<?> clz, final Object... msgObj) {
		switch (lvl) {
		case FATAL:
			this.fatal(clz, msgObj);
			break;
		case ERROR:
			this.error(clz, msgObj);
			break;
		case WARN:
			this.warn(clz, msgObj);
			break;
		case INFO:
			this.info(clz, msgObj);
			break;
		case DEBUG:
			this.debug(clz, msgObj);
			break;
		case TRACE:
			this.trace(clz, msgObj);
			break;
		}
	}

	/**
	 * 写入fatal级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param msgObj log内容
	 */
	public void fatal(final Object... msgObj) {
		if (null != this.log) {
			final StackTraceElement[] sts = Thread.currentThread().getStackTrace();
			if (sts.length > 2) {
				try {
					this.log.fatal(Class.forName(sts[2].getClassName()), msgObj);
				} catch (final ClassNotFoundException e) {
					this.log.fatal(msgObj);
				}
			} else {
				this.log.fatal(msgObj);
			}
		}
	}

	/**
	 * 写入fatal级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param clz 所相关操作类对象
	 * @param msgObj 日志内容
	 */
	public void fatal(final Class<?> clz, final Object... msgObj) {
		if (null != this.log) {
			this.log.fatal(clz, msgObj);
		}
	}

	/**
	 * 写入error级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param msgObj log内容
	 */
	public void error(final Object... msgObj) {
		if (null != this.log) {
			final StackTraceElement[] sts = Thread.currentThread().getStackTrace();
			if (sts.length > 2) {
				try {
					this.log.error(Class.forName(sts[2].getClassName()), msgObj);
				} catch (final ClassNotFoundException e) {
					this.log.error(msgObj);
				}
			} else {
				this.log.error(msgObj);
			}
		}
	}

	/**
	 * 写入error级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param clz 所相关操作类对象
	 * @param msgObj 日志内容
	 */
	public void error(final Class<?> clz, final Object... msgObj) {
		if (null != this.log) {
			this.log.error(clz, msgObj);
		}
	}

	/**
	 * 写入warn级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param msgObj log内容
	 */
	public void warn(final Object... msgObj) {
		if (null != this.log) {
			final StackTraceElement[] sts = Thread.currentThread().getStackTrace();
			if (sts.length > 2) {
				try {
					this.log.warn(Class.forName(sts[2].getClassName()), msgObj);
				} catch (final ClassNotFoundException e) {
					this.log.warn(msgObj);
				}
			} else {
				this.log.warn(msgObj);
			}
		}
	}

	/**
	 * 写入warn级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param clz 所相关操作类对象
	 * @param msgObj 日志内容
	 */
	public void warn(final Class<?> clz, final Object... msgObj) {
		if (null != this.log) {
			this.log.warn(clz, msgObj);
		}
	}

	/**
	 * 写入info级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param msgObj log内容
	 */
	public void info(final Object... msgObj) {
		if (null != this.log) {
			final StackTraceElement[] sts = Thread.currentThread().getStackTrace();
			if (sts.length > 2) {
				try {
					this.log.info(Class.forName(sts[2].getClassName()), msgObj);
				} catch (final ClassNotFoundException e) {
					this.log.info(msgObj);
				}
			} else {
				this.log.info(msgObj);
			}
		}
	}

	/**
	 * 写入info级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param clz 所相关操作类对象
	 * @param msgObj 日志内容
	 */
	public void info(final Class<?> clz, final Object... msgObj) {
		if (null != this.log) {
			this.log.info(clz, msgObj);
		}
	}

	/**
	 * 写入debug级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param msgObj log内容
	 */
	public void debug(final Object... msgObj) {
		if (null != this.log) {
			final StackTraceElement[] sts = Thread.currentThread().getStackTrace();
			if (sts.length > 2) {
				try {
					this.log.debug(Class.forName(sts[2].getClassName()), msgObj);
				} catch (final ClassNotFoundException e) {
					this.log.debug(msgObj);
				}
			} else {
				this.log.debug(msgObj);
			}
		}
	}

	/**
	 * 写入debug级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param clz 所相关操作类对象
	 * @param msgObj 日志内容
	 */
	public void debug(final Class<?> clz, final Object... msgObj) {
		if (null != this.log) {
			this.log.debug(clz, msgObj);
		}
	}

	/**
	 * 写入trace级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param msgObj log内容
	 */
	public void trace(final Object... msgObj) {
		if (null != this.log) {
			final StackTraceElement[] sts = Thread.currentThread().getStackTrace();
			if (sts.length > 2) {
				try {
					this.log.trace(Class.forName(sts[2].getClassName()), msgObj);
				} catch (final ClassNotFoundException e) {
					this.log.trace(msgObj);
				}
			} else {
				this.log.trace(msgObj);
			}
		}
	}

	/**
	 * 写入trace级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param clz 所相关操作类对象
	 * @param msgObj 日志内容
	 */
	public void trace(final Class<?> clz, final Object... msgObj) {
		if (null != this.log) {
			this.log.trace(clz, msgObj);
		}
	}

	/**
	 * fatal级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午4:28:59
	 * @return true，存在该级别
	 */
	public boolean fatalEnabled() {
		if (null == this.log) {
			return false;
		}
		return this.log.fatalEnabled();
	}

	/**
	 * fatal级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午4:28:58
	 * @param clz 所相关操作类对象
	 * @return true，存在该级别
	 */
	public boolean fatalEnabled(final Class<?> clz) {
		if (null == this.log) {
			return false;
		}
		return this.log.fatalEnabled(clz);
	}

	/**
	 * error级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午4:28:56
	 * @return true，存在该级别
	 */
	public boolean errorEnabled() {
		if (null == this.log) {
			return false;
		}
		return this.log.errorEnabled();
	}

	/**
	 * error级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午4:28:55
	 * @param clz 所相关操作类对象
	 * @return true，存在该级别
	 */
	public boolean errorEnabled(final Class<?> clz) {
		if (null == this.log) {
			return false;
		}
		return this.log.errorEnabled(clz);
	}

	/**
	 * warn级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午4:28:54
	 * @return true，存在该级别
	 */
	public boolean warnEnabled() {
		if (null == this.log) {
			return false;
		}
		return this.log.warnEnabled();
	}

	/**
	 * warn级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午4:28:54
	 * @param clz 所相关操作类对象
	 * @return true，存在该级别
	 */
	public boolean warnEnabled(final Class<?> clz) {
		if (null == this.log) {
			return false;
		}
		return this.log.warnEnabled(clz);
	}

	/**
	 * info级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午4:28:51
	 * @return true，存在该级别
	 */
	public boolean infoEnabled() {
		if (null == this.log) {
			return false;
		}
		return this.log.infoEnabled();
	}

	/**
	 * info级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午4:28:50
	 * @param clz 所相关操作类对象
	 * @return true，存在该级别
	 */
	public boolean infoEnabled(final Class<?> clz) {
		if (null == this.log) {
			return false;
		}
		return this.log.infoEnabled(clz);
	}

	/**
	 * debug级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午4:28:49
	 * @return true，存在该级别
	 */
	public boolean debugEnabled() {
		if (null == this.log) {
			return false;
		}
		return this.log.debugEnabled();
	}

	/**
	 * debug级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午4:28:48
	 * @param clz 所相关操作类对象
	 * @return true，存在该级别
	 */
	public boolean debugEnabled(final Class<?> clz) {
		if (null == this.log) {
			return false;
		}
		return this.log.debugEnabled(clz);
	}

	/**
	 * trace级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午4:28:48
	 * @return true，存在该级别
	 */
	public boolean traceEnabled() {
		if (null == this.log) {
			return false;
		}
		return this.log.traceEnabled();
	}

	/**
	 * trace级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月6日 下午4:28:46
	 * @param clz 所相关操作类对象
	 * @return true，存在该级别
	 */
	public boolean traceEnabled(final Class<?> clz) {
		if (null == this.log) {
			return false;
		}
		return this.log.traceEnabled(clz);
	}
}
