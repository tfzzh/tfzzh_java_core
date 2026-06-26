/**
 * @author Weijie Xu
 * @dateTime 2017年3月23日 上午9:40:51
 */
package com.tfzzh.log;

/**
 * 基础log接口
 * 
 * @author Weijie Xu
 * @dateTime 2017年3月23日 上午9:40:51
 */
public interface LogControl {

	/**
	 * 写入fatal级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:28:58
	 * @param msgObj log内容
	 */
	void fatal(Object... msgObj);

	/**
	 * 写入fatal级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:37:06
	 * @param clz 所相关操作类对象
	 * @param msgObj 日志内容
	 */
	void fatal(Class<?> clz, Object... msgObj);

	/**
	 * fatal级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月5日 下午5:52:41
	 * @return true，存在该级别
	 */
	boolean fatalEnabled();

	/**
	 * fatal级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月5日 下午6:25:05
	 * @param clz 所相关操作类对象
	 * @return true，存在该级别
	 */
	boolean fatalEnabled(Class<?> clz);

	/**
	 * 写入error级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:28:58
	 * @param msgObj log内容
	 */
	void error(Object... msgObj);

	/**
	 * 写入error级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:37:06
	 * @param clz 所相关操作类对象
	 * @param msgObj 日志内容
	 */
	void error(Class<?> clz, Object... msgObj);

	/**
	 * error级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月5日 下午5:52:43
	 * @return true，存在该级别
	 */
	boolean errorEnabled();

	/**
	 * error级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月5日 下午6:25:17
	 * @param clz 所相关操作类对象
	 * @return true，存在该级别
	 */
	boolean errorEnabled(Class<?> clz);

	/**
	 * 写入warn级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:28:58
	 * @param msgObj log内容
	 */
	void warn(Object... msgObj);

	/**
	 * 写入warn级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:37:06
	 * @param clz 所相关操作类对象
	 * @param msgObj 日志内容
	 */
	void warn(Class<?> clz, Object... msgObj);

	/**
	 * warn级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月5日 下午5:52:45
	 * @return true，存在该级别
	 */
	boolean warnEnabled();

	/**
	 * warn级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月5日 下午6:25:20
	 * @param clz 所相关操作类对象
	 * @return true，存在该级别
	 */
	boolean warnEnabled(Class<?> clz);

	/**
	 * 写入info级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:28:58
	 * @param msgObj log内容
	 */
	void info(Object... msgObj);

	/**
	 * 写入info级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:37:06
	 * @param clz 所相关操作类对象
	 * @param msgObj 日志内容
	 */
	void info(Class<?> clz, Object... msgObj);

	/**
	 * info级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月5日 下午5:52:46
	 * @return true，存在该级别
	 */
	boolean infoEnabled();

	/**
	 * info级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月5日 下午6:25:24
	 * @param clz 所相关操作类对象
	 * @return true，存在该级别
	 */
	boolean infoEnabled(Class<?> clz);

	/**
	 * 写入debug级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:28:58
	 * @param msgObj log内容
	 */
	void debug(Object... msgObj);

	/**
	 * 写入debug级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:37:06
	 * @param clz 所相关操作类对象
	 * @param msgObj 日志内容
	 */
	void debug(Class<?> clz, Object... msgObj);

	/**
	 * debug级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月5日 下午5:52:47
	 * @return true，存在该级别
	 */
	boolean debugEnabled();

	/**
	 * debug级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月5日 下午6:25:27
	 * @param clz 所相关操作类对象
	 * @return true，存在该级别
	 */
	boolean debugEnabled(Class<?> clz);

	/**
	 * 写入trace级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:28:58
	 * @param msgObj log内容
	 */
	void trace(Object... msgObj);

	/**
	 * 写入trace级别的log
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 上午10:37:06
	 * @param clz 所相关操作类对象
	 * @param msgObj 日志内容
	 */
	void trace(Class<?> clz, Object... msgObj);

	/**
	 * trace级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月5日 下午5:52:52
	 * @return true，存在该级别
	 */
	boolean traceEnabled();

	/**
	 * trace级别日志，是否开启
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月5日 下午6:25:29
	 * @param clz 所相关操作类对象
	 * @return true，存在该级别
	 */
	boolean traceEnabled(Class<?> clz);
}
