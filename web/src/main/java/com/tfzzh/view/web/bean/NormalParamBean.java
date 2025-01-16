/**
 * @author Xu Weijie
 * @dateTime 2012-9-7 下午4:30:16
 */
package com.tfzzh.view.web.bean;

import java.lang.reflect.Field;
import java.util.Map;

import com.tfzzh.view.web.annotation.ParamBean;

/**
 * 用于正常请求信息
 * 
 * @author Xu Weijie
 * @dateTime 2012-9-7 下午4:30:16
 */
public abstract class NormalParamBean extends BaseParamBean {

	/**
	 * @author Xu Weijie
	 * @datetime 2018年2月5日_上午10:26:24
	 */
	private static final long serialVersionUID = 1605674518535845679L;

	/**
	 * 设置参数
	 * 
	 * @author TFZZH
	 * @dateTime 2011-3-7 下午04:02:33
	 * @param paraMap 参数集合，包括文件部分；<br />
	 *           List<String>：正常的页面传值；<br />
	 *           FileItem：上传的文件；<br />
	 * @param needValid 是否需要校验：<br />
	 *           true，需要校验；<br />
	 * @see com.tfzzh.view.web.bean.BaseParamBean#setParameters(java.util.Map, boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void setParameters(final Map<String, Object> paraMap, boolean needValid) {
		Class<?> clz = this.getClass();
		do {
			this.setParameters((Class<? extends NormalParamBean>) clz, paraMap);
			clz = clz.getSuperclass();
		} while (NormalParamBean.class.isAssignableFrom(clz.getSuperclass()));
		if (needValid) {
			this.validate();
		}
	}

	/**
	 * 按类层级设置参数
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-11-21 下午7:04:02
	 * @param pClz 类信息
	 * @param paraMap 参数集合，包括文件部分；<br />
	 *           List<String>：正常的页面传值；<br />
	 *           FileItem：上传的文件；<br />
	 */
	private void setParameters(final Class<? extends NormalParamBean> pClz, final Map<String, Object> paraMap) {
		final Field[] fields = pClz.getDeclaredFields();
		for (final Field field : fields) {
			this.setField(this, field, paraMap);
		}
	}

	/**
	 * 对内置对象设置参数
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月31日 下午4:10:25
	 * @param obj 内置对象信息
	 * @param paraMap 参数集合，包括文件部分；<br />
	 *           List<String>：正常的页面传值；<br />
	 *           FileItem：上传的文件；<br />
	 */
	private void setInnerParameters(final Object obj, final Map<String, Object> paraMap) {
		final Field[] fields = obj.getClass().getDeclaredFields();
		for (final Field field : fields) {
			this.setField(obj, field, paraMap);
		}
	}

	/**
	 * 进行对字段的设置
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月31日 下午4:12:31
	 * @param obj 属性所属对象
	 * @param field 当前属性对象
	 * @param paraMap 参数集合，包括文件部分；<br />
	 *           List<String>：正常的页面传值；<br />
	 *           FileItem：上传的文件；<br />
	 */
	private void setField(final Object obj, final Field field, final Map<String, Object> paraMap) {
		final Class<?> clz = field.getType();
		String ps;
		try {
			if (clz == String.class) {
				if (null != (ps = this.getParamStringValue(field, paraMap))) {
					// 开启字段可写权限
					field.setAccessible(true);
					field.set(obj, ps);
					// 关闭字段可写权限
					field.setAccessible(false);
				}
			} else if ((clz == Integer.class) || (clz == int.class)) {
				if (null != (ps = this.getParamStringValue(field, paraMap))) {
					field.setAccessible(true);
					if (clz == Integer.class) {
						field.set(obj, Integer.valueOf(ps));
					} else {
						field.setInt(obj, Integer.parseInt(ps));
					}
					field.setAccessible(false);
				}
			} else if ((clz == Short.class) || (clz == short.class)) {
				if (null != (ps = this.getParamStringValue(field, paraMap))) {
					field.setAccessible(true);
					if (clz == Short.class) {
						field.set(obj, Short.valueOf(ps));
					} else {
						field.setShort(obj, Short.parseShort(ps));
					}
					// field.setShort(obj, Short.parseShort(((List<String>) ps).get(0)));
					field.setAccessible(false);
				}
			} else if ((clz == Long.class) || (clz == long.class)) {
				if (null != (ps = this.getParamStringValue(field, paraMap))) {
					field.setAccessible(true);
					if (clz == Long.class) {
						field.set(obj, Long.valueOf(ps));
					} else {
						field.setLong(obj, Long.parseLong(ps));
					}
					// field.setLong(obj, Long.parseLong(((List<String>) ps).get(0)));
					field.setAccessible(false);
				}
			} else if ((clz == Boolean.class) || (clz == boolean.class)) {
				if (null != (ps = this.getParamStringValue(field, paraMap))) {
					field.setAccessible(true);
					if (clz == Boolean.class) {
						field.set(obj, Boolean.valueOf(ps));
					} else {
						field.setBoolean(obj, Boolean.parseBoolean(ps));
					}
					// field.setBoolean(obj, Boolean.parseBoolean(((List<String>) ps).get(0)));
					field.setAccessible(false);
				}
			} else if ((clz == Double.class) || (clz == double.class)) {
				if (null != (ps = this.getParamStringValue(field, paraMap))) {
					field.setAccessible(true);
					if (clz == Double.class) {
						field.set(obj, Double.valueOf(ps));
					} else {
						field.setDouble(obj, Double.parseDouble(ps));
					}
					// field.setDouble(obj, Double.parseDouble(((List<String>) ps).get(0)));
					field.setAccessible(false);
				}
			} else if ((clz == Float.class) || (clz == float.class)) {
				if (null != (ps = this.getParamStringValue(field, paraMap))) {
					field.setAccessible(true);
					if (clz == Float.class) {
						field.set(obj, Float.valueOf(ps));
					} else {
						field.setFloat(obj, Float.parseFloat(ps));
					}
					// field.setFloat(obj, Float.parseFloat(((List<String>) ps).get(0)));
					field.setAccessible(false);
				}
			} else {
				// 不成功走到这里
				final ParamBean pb = field.getAnnotation(ParamBean.class);
				if (null != pb) {
					// 是个对象
					// 初始化这个属性所相关对象
					try {
						final Object innerObj = clz.getDeclaredConstructor().newInstance();
						field.setAccessible(true);
						field.set(obj, innerObj);
						field.setAccessible(false);
						this.setInnerParameters(innerObj, paraMap);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (final IllegalArgumentException e) {
		} catch (final IllegalAccessException e) {
		}
	}
}
