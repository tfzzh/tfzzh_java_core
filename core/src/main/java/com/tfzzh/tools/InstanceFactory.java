/**
 * @createDate Sep 3, 2008 5:57:09 PM
 */
package com.tfzzh.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 实体工厂
 * 
 * @createDate Sep 3, 2008 5:57:09 PM
 * @author tfzzh
 * @model
 */
public final class InstanceFactory {

	/**
	 * 创建一个指定类的实体
	 * 
	 * @createDate 2008-9-27 下午04:37:32
	 * @param path 类路径
	 * @return 类实体
	 * @throws InstantiationException 抛
	 * @throws IllegalAccessException 抛
	 * @throws ClassNotFoundException 抛
	 */
	public static Object classInstance(final String path) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return InstanceFactory.classInstance(Class.forName(path));
	}

	/**
	 * 创建一个指定类的实体
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-10-9 下午12:02:14
	 * @param <T> 非指定对象
	 * @param clz 类对象
	 * @return 类实体
	 * @throws IllegalAccessException 抛
	 * @throws InstantiationException 抛
	 */
	public static <T> T classInstance(final Class<T> clz) throws InstantiationException, IllegalAccessException {
		return clz.newInstance();
	}

	/**
	 * 创建一个指定类的实体，使用构造函数
	 * 
	 * @author xuweijie
	 * @dateTime 2011-12-28 下午1:19:16
	 * @param path 类路径
	 * @param objs 构造函数对象组合，注意有序
	 * @return 类实体
	 * @throws NoSuchMethodException 抛
	 * @throws SecurityException 抛
	 * @throws ClassNotFoundException 抛
	 * @throws InstantiationException 抛
	 * @throws IllegalAccessException 抛
	 * @throws IllegalArgumentException 抛
	 * @throws InvocationTargetException 抛
	 */
	public static Object classInstance(final String path, final Object... objs) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Class<?>[] clzs = new Class[objs.length];
		int i = 0;
		for (final Object o : objs) {
			clzs[i++] = o.getClass();
		}
		return InstanceFactory.classInstance(Class.forName(path), clzs, objs);
	}

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-10-9 下午12:13:57
	 * @param <T> 非指定对象
	 * @param clz 类对象
	 * @param clzs 构造函数参数类集合
	 * @param objs 构造函数对象组合，注意有序
	 * @return 类实体
	 * @throws NoSuchMethodException 抛
	 * @throws SecurityException 抛
	 * @throws ClassNotFoundException 抛
	 * @throws InstantiationException 抛
	 * @throws IllegalAccessException 抛
	 * @throws IllegalArgumentException 抛
	 * @throws InvocationTargetException 抛
	 */
	public static <T> T classInstance(final Class<T> clz, final Class<?>[] clzs, final Object[] objs) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Constructor<T> constructor = clz.getConstructor(clzs);
		return constructor.newInstance(objs);
	}
}
