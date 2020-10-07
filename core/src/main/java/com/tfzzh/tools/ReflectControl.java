/**
 * @author tfzzh
 * @createDate 2008-11-23 下午05:19:27
 */
package com.tfzzh.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.tfzzh.exception.InitializeException;

/**
 * 反射控制器<br />
 * 主要用于视图层<br />
 * 
 * @author xuweijie
 * @dateTime 2012-1-30 上午11:48:18
 */
public class ReflectControl {

	/**
	 * 操作控制信息
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-30 上午11:58:26
	 */
	private final Map<String, ControlInfo> controls = new HashMap<>(10);

	/**
	 * 对象缓存信息<br />
	 * <对象路径,对象实例><br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-8 下午1:10:28
	 */
	private final Map<String, BaseReflectOperate> objs = new HashMap<>();

	/**
	 * 对象唯一实例
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-7 下午6:12:24
	 */
	private final static ReflectControl control = new ReflectControl();

	/**
	 * @author xuweijie
	 * @dateTime 2012-2-7 下午6:12:26
	 */
	private ReflectControl() {
	}

	/**
	 * 得到该对象唯一实例
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-7 下午6:12:54
	 * @return 对象实例
	 */
	public static ReflectControl getInstance() {
		return ReflectControl.control;
	}

	/**
	 * 增加一个反射控制信息<br />
	 * 不对所增加的信息进行有效验证<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-8 下午1:25:04
	 * @param name 信息名称
	 * @param clz 操作对象类信息
	 * @param method 方法对象
	 * @return 反射控制对象
	 */
	public ControlInfo addControlInfo(final String name, final Class<? extends BaseReflectOperate> clz, final Method method) {
		return this.addControlInfo(name, clz, method, false);
	}

	/**
	 * 增加一个反射控制信息
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-8 下午1:25:05
	 * @param name 信息名称
	 * @param clz 操作对象类信息
	 * @param method 方法对象
	 * @param validate 是否进行验证：true，验证；false，不验证；
	 * @return 反射控制对象
	 */
	public ControlInfo addControlInfo(final String name, final Class<? extends BaseReflectOperate> clz, final Method method, final boolean validate) {
		// 匹配对象
		final String clzName = clz.getName();
		BaseReflectOperate reflect;
		if (null == (reflect = this.objs.get(clzName))) {
			try {
				reflect = InstanceFactory.classInstance(clz);
			} catch (final IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException e) {
				throw new InitializeException("Cannt instance BaseReflectOperate with: " + clzName);
			}
			this.objs.put(clzName, reflect);
		}
		return this.addControlInfo(name, reflect, method, validate);
	}

	/**
	 * 增加一个反射控制信息<br />
	 * 不对所增加的信息进行有效验证<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-31 下午6:10:08
	 * @param name 信息名称
	 * @param obj 操作对象信息
	 * @param method 方法对象
	 * @return 反射控制对象
	 */
	public ControlInfo addControlInfo(final String name, final BaseReflectOperate obj, final Method method) {
		return this.addControlInfo(name, obj, method, false);
	}

	/**
	 * 增加一个反射控制信息
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-30 下午1:30:36
	 * @param name 信息名称
	 * @param obj 操作对象信息
	 * @param method 方法对象
	 * @param validate 是否进行验证：true，验证；false，不验证；
	 * @return 反射控制对象<br />
	 *         null，仅验证情况下出现，认为目标反射信息不正确<br />
	 */
	public ControlInfo addControlInfo(final String name, final BaseReflectOperate obj, final Method method, final boolean validate) {
		final String clzName = obj.getClass().getName();
		final BaseReflectOperate existsObj = this.objs.get(clzName);
		if (null == existsObj) {
			// 还不存在
			this.objs.put(clzName, obj);
		} else if (existsObj != obj) {
			// 认为是错误情况
			throw new InitializeException("Ready exists BaseReflectOperate with: " + clzName);
		}
		final ControlInfo info = new ControlInfo(obj, method);
		if (validate && !info.validate()) {
			// 因为错误， 不放入到队列
			throw new InitializeException("Validate BaseReflectOperate fail: " + clzName + " - " + method.getName());
		} else {
			this.controls.put(name, info);
			return info;
		}
	}

	/**
	 * 执行反射方法操作
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-30 下午1:49:03
	 * @param name 信息名称
	 * @param params 参数内容信息
	 * @throws IllegalAccessException 抛
	 * @throws IllegalArgumentException 抛
	 * @throws InvocationTargetException 抛
	 */
	public void execute(final String name, final Object... params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final ControlInfo con = this.controls.get(name);
		if (null != con) {
			con.reflect(params);
		}
	}

	/**
	 * 得到反射控制方法
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-9 上午11:02:31
	 * @param name 目标名称
	 * @return 反射控制方法
	 */
	public ControlInfo getReflectControl(final String name) {
		return this.controls.get(name);
	}

	/**
	 * 得到目标类已经存在的实体
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-16 下午5:11:37
	 * @param clz 类信息
	 * @return 实体信息；<br />
	 *         null，还不存在目标类实体<br />
	 */
	public BaseReflectOperate getReflectImpl(final Class<?> clz) {
		return this.getReflectImpl(clz.getName());
	}

	/**
	 * 得到目标类名已经存在的实体
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-16 下午5:11:39
	 * @param className 类全名
	 * @return 实体信息；<br />
	 *         null，还不存在目标类实体<br />
	 */
	public BaseReflectOperate getReflectImpl(final String className) {
		return this.objs.get(className);
	}

	/**
	 * 控制信息
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-30 上午11:55:00
	 */
	public class ControlInfo {

		/**
		 * 操作对象
		 * 
		 * @author xuweijie
		 * @dateTime 2012-1-30 上午11:57:15
		 */
		private final BaseReflectOperate obj;

		/**
		 * 目标方法
		 * 
		 * @author xuweijie
		 * @dateTime 2012-1-30 上午11:57:16
		 */
		private final Method method;

		/**
		 * @author xuweijie
		 * @dateTime 2012-1-30 上午11:57:10
		 * @param obj 操作对象
		 * @param method 目标方法
		 */
		public ControlInfo(final BaseReflectOperate obj, final Method method) {
			this.obj = obj;
			this.method = method;
		}

		/**
		 * 执行方法
		 * 
		 * @author xuweijie
		 * @dateTime 2012-1-30 下午1:17:08
		 * @param params 参数信息
		 * @return 执行方法后被返回的数据结果
		 * @throws IllegalAccessException 抛
		 * @throws IllegalArgumentException 抛
		 * @throws InvocationTargetException 抛
		 */
		public Object reflect(final Object... params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			if ((null != params) && (params.length > 0)) {
				final Object[] obj = new Object[params.length];
				for (int i = params.length - 1; i >= 0; i--) {
					obj[i] = params[i];
				}
				return this.method.invoke(this.obj, obj);
			} else {
				return this.method.invoke(this.obj);
			}
		}

		/**
		 * 验证该反射信息是否有效
		 * 
		 * @author xuweijie
		 * @dateTime 2012-1-31 下午6:14:28
		 * @return true，有效；<br />
		 *         false，无效；<br />
		 */
		private boolean validate() {
			final Class<? extends BaseReflectOperate> clz = this.obj.getClass();
			try {
				if (null != (clz.getMethod(this.method.getName(), this.method.getParameterTypes()))) {
					return true;
				}
			} catch (final NoSuchMethodException e) {
				e.printStackTrace();
			} catch (final SecurityException e) {
				e.printStackTrace();
			}
			return false;
		}
	}
}
