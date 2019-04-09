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
	private final static CoreLog lp = new CoreLog();

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
	 * @param msg log内容
	 */
	public void putLogMsg(final LogLevel lvl, final String... msg) {
		switch (lvl) {
		case FATAL:
			this.fatal(msg);
			break;
		case ERROR:
			this.error(msg);
			break;
		case WARN:
			this.warn(msg);
			break;
		case INFO:
			this.info(msg);
			break;
		case DEBUG:
			this.debug(msg);
			break;
		case TRACE:
			this.trace(msg);
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
	 * @param msg log内容
	 */
	public void putLogMsg(final LogLevel lvl, final Class<?> clz, final String... msg) {
		switch (lvl) {
		case FATAL:
			this.fatal(clz, msg);
			break;
		case ERROR:
			this.error(clz, msg);
			break;
		case WARN:
			this.warn(clz, msg);
			break;
		case INFO:
			this.info(clz, msg);
			break;
		case DEBUG:
			this.debug(clz, msg);
			break;
		case TRACE:
			this.trace(clz, msg);
			break;
		}
	}

	/**
	 * 写入fatal级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param msg log内容
	 */
	public void fatal(final String... msg) {
		if (null != this.log) {
			this.log.fatal(msg);
		}
	}

	/**
	 * 写入fatal级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param clz 所相关操作类对象
	 * @param msg 日志内容
	 */
	public void fatal(final Class<?> clz, final String... msg) {
		if (null != this.log) {
			this.log.fatal(clz, msg);
		}
	}

	/**
	 * 写入error级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param msg log内容
	 */
	public void error(final String... msg) {
		if (null != this.log) {
			this.log.error(msg);
		}
	}

	/**
	 * 写入error级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param clz 所相关操作类对象
	 * @param msg 日志内容
	 */
	public void error(final Class<?> clz, final String... msg) {
		if (null != this.log) {
			this.log.error(clz, msg);
		}
	}

	/**
	 * 写入warn级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param msg log内容
	 */
	public void warn(final String... msg) {
		if (null != this.log) {
			this.log.warn(msg);
		}
	}

	/**
	 * 写入warn级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param clz 所相关操作类对象
	 * @param msg 日志内容
	 */
	public void warn(final Class<?> clz, final String... msg) {
		if (null != this.log) {
			this.log.warn(clz, msg);
		}
	}

	/**
	 * 写入info级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param msg log内容
	 */
	public void info(final String... msg) {
		if (null != this.log) {
			this.log.info(msg);
		}
	}

	/**
	 * 写入info级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param clz 所相关操作类对象
	 * @param msg 日志内容
	 */
	public void info(final Class<?> clz, final String... msg) {
		if (null != this.log) {
			this.log.info(clz, msg);
		}
	}

	/**
	 * 写入debug级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param msg log内容
	 */
	public void debug(final String... msg) {
		if (null != this.log) {
			this.log.debug(msg);
		}
	}

	/**
	 * 写入debug级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param clz 所相关操作类对象
	 * @param msg 日志内容
	 */
	public void debug(final Class<?> clz, final String... msg) {
		if (null != this.log) {
			this.log.debug(clz, msg);
		}
	}

	/**
	 * 写入trace级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param msg log内容
	 */
	public void trace(final String... msg) {
		if (null != this.log) {
			this.log.trace(msg);
		}
	}

	/**
	 * 写入trace级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午11:02:11
	 * @param clz 所相关操作类对象
	 * @param msg 日志内容
	 */
	public void trace(final Class<?> clz, final String... msg) {
		if (null != this.log) {
			this.log.trace(clz, msg);
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
