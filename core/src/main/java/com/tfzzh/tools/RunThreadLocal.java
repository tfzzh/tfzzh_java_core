/**
 * @author xuweijie
 * @dateTime 2012-2-28 上午11:59:40
 */
package com.tfzzh.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * 运行中的线程控<br />
 * 当前主要针对数据库的根据用户的，动态连接池变换<br />
 * 
 * @author xuweijie
 * @dateTime 2012-2-28 上午11:59:40
 */
public class RunThreadLocal {

	/**
	 * 针对一些需要通过线程获取对象的用例
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-28 下午1:16:33
	 */
	private final Map<String, ThreadLocal<Object>> objMap = new HashMap<>();

	/**
	 * @author xuweijie
	 * @dateTime 2012-2-28 下午1:18:16
	 */
	private static RunThreadLocal run = new RunThreadLocal();

	/**
	 * @author xuweijie
	 * @dateTime 2012-2-28 下午1:18:18
	 */
	private RunThreadLocal() {
		this.init();
	}

	/**
	 * @author xuweijie
	 * @dateTime 2012-2-28 下午1:18:24
	 */
	private void init() {
	}

	/**
	 * 得到对象实例
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-28 下午1:18:29
	 * @return 对象实例
	 */
	public static RunThreadLocal getInstance() {
		return RunThreadLocal.run;
	}

	/**
	 * 放入一个用户会话信息消息到当前线程
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-28 下午1:20:44
	 * @param key 数据类型key
	 * @param obj 目标对象
	 */
	public void putObject(final String key, final Object obj) {
		final String sk = ("rtl_" + key).intern();
		synchronized (sk) {
			ThreadLocal<Object> otl = this.objMap.get(key);
			if (null == otl) {
				otl = new ThreadLocal<>();
				this.objMap.put(key, otl);
			}
			otl.set(obj);
		}
	}

	/**
	 * 得到存放在当前线程中的目标对象
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-28 下午1:21:53
	 * @param key 数据类型key
	 * @return 目标对象
	 */
	public Object getObject(final String key) {
		final ThreadLocal<Object> otl = this.objMap.get(key);
		if (null == otl) {
			return null;
		}
		return otl.get();
	}

	/**
	 * 移除当前线程中的目标对象
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-28 下午1:22:44
	 * @param key 数据类型key
	 */
	public void removeUserSession(final String key) {
		final ThreadLocal<Object> otl = this.objMap.get(key);
		if (null == otl) {
			return;
		}
		otl.remove();
	}
}
