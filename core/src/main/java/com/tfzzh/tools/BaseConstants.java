/**
 * @author tfzzh
 * @dateTime 2016年11月21日 上午11:10:24
 */
package com.tfzzh.tools;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tfzzh.core.annotation.PropertiesFile;
import com.tfzzh.core.annotation.PropertiesMethod;
import com.tfzzh.core.annotation.PropertiesValue;
import com.tfzzh.exception.ConfigurationException;

/**
 * 基础常量消息<br />
 * 暂时不允许同对象多实例<br />
 * 
 * @author tfzzh
 * @dateTime 2016年11月21日 上午11:10:24
 */
public abstract class BaseConstants {

	/**
	 * 消息池
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 上午11:45:13
	 */
	private final static Map<String, BaseConstants> messagePool = new HashMap<>();

	/**
	 * 类加载对象池
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午12:08:52
	 */
	private final static Map<ClassLoader, List<String>> clPool = new HashMap<>();

	/**
	 * 文件信息类型
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 */
	private final static int FILE_TYPE = 2;

	/**
	 * properties
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月7日 下午3:58:17
	 */
	private String ppn;

	/**
	 * 所相关资源名
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 上午11:39:25
	 */
	private String bundleName;

	/**
	 * 目标资源文件信息
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 */
	private File bundleFile;

	/**
	 * 读取类型：<br />
	 * 0，文件路径读取；<br />
	 * 1，文件对象读取；<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 */
	private int readType = 0;

	/**
	 * 相关资源包
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 上午11:25:04
	 */
	private ResourceBundle resourceBundle;

	/**
	 * 针对类名称后缀为“Constants”“Message”的对象，进行自动名称处理，将后缀自动去掉，变更为“_message”
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月17日_下午2:28:38
	 */
	public BaseConstants() {
		final String cn = this.getClass().getSimpleName();
		String ccn = StringTools.cutTail(cn, "Constants");
		if (cn.equalsIgnoreCase(ccn)) {
			ccn = StringTools.cutTail(cn, "Message");
			if (!cn.equalsIgnoreCase(ccn)) {
				ccn += "_message";
			}
		} else {
			ccn += "_message";
		}
		this.setBundleName(ccn);
		// 因为初始化加入到池
		CoreConstantsPool.getInstance().putConstants(this);
	}

	/**
	 * @author tfzzh
	 * @dateTime 2016年11月21日 上午11:38:54
	 * @param bundleName 所相关资源名
	 */
	public BaseConstants(final String bundleName) {
		this.setBundleName(bundleName);
		// 因为初始化加入到池
		CoreConstantsPool.getInstance().putConstants(this);
	}

	/**
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 * @param bundleFile 所相关资源文件信息
	 */
	public BaseConstants(final File bundleFile) {
		this.setBundleFile(bundleFile);
		// 因为初始化加入到池
		CoreConstantsPool.getInstance().putConstants(this);
	}

	/**
	 * 设置目标文件路径
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 上午11:36:35
	 * @param bundleName 所相关资源名
	 */
	protected void setBundleName(final String bundleName) {
		if (null == this.bundleName) {
			this.bundleName = bundleName;
		} else if (this.bundleName.equals(bundleName)) {
			// 此情况，不进行处理
		} else {
			// 需要对之前的存在进行清理
			this.clearCache();
			this.bundleName = bundleName;
		}
		// 在这里刷新值
		this.refreshValue();
	}

	/**
	 * 设置目标资源文件
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 * @param bundleFile 所相关资源文件
	 */
	protected void setBundleFile(final File bundleFile) {
		if (this.readType != BaseConstants.FILE_TYPE) {
			this.readType = BaseConstants.FILE_TYPE;
		}
		if (null == this.bundleFile) {
			this.bundleFile = bundleFile;
		} else if (this.bundleFile.equals(bundleFile)) {
			// 此情况，不进行处理
		} else {
			// 需要对之前的存在进行清理
			this.clearCache();
			this.bundleFile = bundleFile;
		}
		// 在这里刷新值
		this.refreshValue();
	}

	/**
	 * 得到所相关资源数据
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 上午11:31:13
	 * @return 所相关资源数据
	 */
	protected ResourceBundle getResourceBundle() {
		return this.resourceBundle;
	}

	// /**
	// * 刷新数据<br />
	// * 当前就是清理缓存<br />
	// *
	// * @author tfzzh
	// * @dateTime 2016年11月21日 上午11:31:04
	// */
	// public void refresh() {
	// this.clearCache();
	// // 在这里刷新值
	// this.refreshValue();
	// }
	/**
	 * 清理缓存
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午12:03:54
	 */
	private void clearCache() {
		if (null != this.resourceBundle) {
			synchronized (BaseConstants.messagePool) {
				final List<String> sl = BaseConstants.clPool.remove(this.getClass().getClassLoader());
				BaseConstants bm;
				for (final String s : sl) {
					bm = BaseConstants.messagePool.remove(s);
					if (null != bm) {
						bm.clearResource();
					}
				}
			}
			ResourceBundle.clearCache(this.getClass().getClassLoader());
		}
	}

	/**
	 * 清理资源文件
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午12:18:39
	 */
	private void clearResource() {
		this.resourceBundle = null;
	}

	/**
	 * 清理所有相关缓存
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 上午11:46:58
	 */
	public static void clearAllCache() {
		synchronized (BaseConstants.messagePool) {
			final Iterator<List<String>> sli = BaseConstants.clPool.values().iterator();
			List<String> sl;
			BaseConstants bm;
			while (sli.hasNext()) {
				sl = sli.next();
				sli.remove();
				for (final String s : sl) {
					bm = BaseConstants.messagePool.remove(s);
					if (null != bm) {
						bm.clearResource();
					}
				}
			}
		}
	}

	/**
	 * 刷新字段的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:48:29
	 */
	private void refreshValue() {
		if (this.readType == BaseConstants.FILE_TYPE) {
			this.resourceBundle = FileTools.getResourceBundle(this.bundleFile);
		} else {
			this.resourceBundle = FileTools.getResourceBundle(this.bundleName, this.getClass());
		}
		synchronized (BaseConstants.messagePool) {
			final ClassLoader cl = this.getClass().getClassLoader();
			List<String> sl = BaseConstants.clPool.get(cl);
			if (null == sl) {
				sl = new ArrayList<>();
				BaseConstants.clPool.put(cl, sl);
			}
			if (this.readType == BaseConstants.FILE_TYPE) {
				final String bundlePath = this.bundleFile.getPath();
				sl.add(bundlePath);
				BaseConstants.messagePool.put(bundlePath, this);
			} else {
				sl.add(this.bundleName);
				BaseConstants.messagePool.put(this.bundleName, this);
			}
		}
		// 得到所有字段
		Class<?> clz = this.getClass();
		do {
			this.setParameters();
			clz = clz.getSuperclass();
		} while (BaseConstants.class.isAssignableFrom(clz.getSuperclass()));
	}

	/**
	 * 设置属性的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午3:03:19
	 */
	private void setParameters() {
		final PropertiesFile pf = this.getClass().getAnnotation(PropertiesFile.class);
		if (null == this.ppn) {
			if (null == pf) {
				this.ppn = this.getClass().getSimpleName() + ".";
			} else if (pf.value().length() == 0) {
				this.ppn = this.getClass().getSimpleName() + ".";
			} else {
				this.ppn = pf.value() + ".";
			}
		}
		final Map<String, String> pps = new HashMap<>();
		final Field[] fields = this.getClass().getDeclaredFields();
		for (final Field field : fields) {
			this.valToField(field, pps);
		}
	}

	/**
	 * 将值放入到字段
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 * @param field 目标字段
	 * @param pps 配置表内容
	 */
	private void valToField(final Field field, final Map<String, String> pps) {
		PropertiesValue fv = field.getAnnotation(PropertiesValue.class);
		if (null != fv) {
			// 需要进行的操作
			String fn = fv.value();
			if (fn.length() == 0) {
				fn = this.ppn + field.getName();
			} else {
				fn = this.ppn + fn;
			}
			PropertiesValueTypeEnum pvt = fv.type();
			// 进行转译
			final Class<?> fc = field.getType();
			try {
				if (fc == String.class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getString(fn));
					return;
				} else if (fc == int.class) {
					field.setInt((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getInt(fn));
					return;
				} else if (fc == Integer.class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getIntObj(fn));
					return;
				} else if (fc == long.class) {
					field.setLong((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getLong(fn));
					return;
				} else if (fc == Long.class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getLongObj(fn));
					return;
				} else if (fc == short.class) {
					field.setShort((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getShort(fn));
					return;
				} else if (fc == Short.class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getShortObj(fn));
					return;
				} else if (fc == double.class) {
					field.setDouble((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getDouble(fn));
					return;
				} else if (fc == Double.class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getDoubleObj(fn));
					return;
				} else if (fc == float.class) {
					field.setFloat((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getFloat(fn));
					return;
				} else if (fc == Float.class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getFloatObj(fn));
					return;
				} else if (fc == boolean.class) {
					field.setBoolean((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getBoolean(fn));
					return;
				} else if (fc == Boolean.class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getBooleanObj(fn));
					return;
				} else if (fc == int[].class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getIntegerArray(fn, fv.specialValue()));
					return;
				} else if (fc == String[].class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getStringArray(fn, fv.specialValue()));
					return;
				} else if (fc == long[].class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getLongArray(fn, fv.specialValue()));
					return;
				} else if (fc == short[].class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getShortArray(fn, fv.specialValue()));
					return;
				} else if (fc == Integer[].class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getIntegerObjArray(fn, fv.specialValue()));
					return;
				} else if (fc == Long[].class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getLongObjArray(fn, fv.specialValue()));
					return;
				} else if (fc == Short[].class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getShortObjArray(fn, fv.specialValue()));
					return;
				} else if (List.class.isAssignableFrom(fc)) {
					final Type ft = field.getGenericType();
					if ((null != ft) && ParameterizedType.class.isAssignableFrom(ft.getClass())) {
						// 自动处理
						final Type lt = ((ParameterizedType) ft).getActualTypeArguments()[0];
						switch (lt.getTypeName()) {
						case "java.lang.String":
							field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getStringList(fn, fv.specialValue()));
							return;
						case "java.lang.Integer":
							field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getIntegerList(fn, fv.specialValue()));
							return;
						case "java.lang.Long":
							field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getLongList(fn, fv.specialValue()));
							return;
						case "java.lang.Short":
							field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getShortList(fn, fv.specialValue()));
							return;
						case "java.lang.Double":
							field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getDoubleList(fn, fv.specialValue()));
							return;
						case "java.lang.Float":
							field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getFloatList(fn, fv.specialValue()));
							return;
						}
					} else if (JSONArray.class.getName().equals(ft.getTypeName())) {
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getJsonArrList(fn));
						return;
					}
					// 注解处理
					switch (pvt) {
					case String:
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getStringList(fn, fv.specialValue()));
						return;
					case Integer:
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getIntegerList(fn, fv.specialValue()));
						return;
					case Long:
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getLongList(fn, fv.specialValue()));
						return;
					case Short:
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getShortList(fn, fv.specialValue()));
						return;
					case Double:
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getDoubleList(fn, fv.specialValue()));
						return;
					case Float:
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getFloatList(fn, fv.specialValue()));
						return;
					default:
						throw new ConfigurationException("Error Annotation type in [" + this.getClass().getName() + "::" + field.getName() + "]--" + pvt.name());
					}
				} else if (JSONObject.class.isAssignableFrom(fc)) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getJsonObjList(fn));
					return;
				} else if (JSONArray.class.isAssignableFrom(fc)) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getJsonArrList(fn));
					return;
				}
			} catch (final IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			PropertiesMethod fm = field.getAnnotation(PropertiesMethod.class);
			if (null != fm) {
				String mn = fm.value();
				String fn = fm.name();
				if (fn.length() == 0) {
					fn = this.ppn + field.getName();
				} else {
					fn = this.ppn + fn;
				}
				if (fm.value().length() == 0) {
					mn = "asse" + StringTools.assemblyStringWhitInterval(field.getName(), true, true);
				} else {
					mn = fm.value();
				}
				Method m;
				try {
					m = this.getClass().getDeclaredMethod(mn);
					m.setAccessible(true);
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), m.invoke(this));
					m.setAccessible(false);
					return;
				} catch (final NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e11) {
					try {
						m = this.getClass().getDeclaredMethod(mn, String.class);
						m.setAccessible(true);
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), m.invoke(this, this.getString(fn)));
						m.setAccessible(false);
						return;
					} catch (final NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e12) {
						// 增加对assemble全称前缀的支持
						if (fm.value().length() == 0) {
							mn = "assemble" + StringTools.assemblyStringWhitInterval(field.getName(), true, true);
							try {
								m = this.getClass().getDeclaredMethod(mn);
								m.setAccessible(true);
								field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), m.invoke(this));
								m.setAccessible(false);
								return;
							} catch (final NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e21) {
								try {
									m = this.getClass().getDeclaredMethod(mn, String.class);
									m.setAccessible(true);
									field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), m.invoke(this, this.getString(fn)));
									m.setAccessible(false);
									return;
								} catch (final NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e22) {
									// 结束
									e22.printStackTrace();
								}
							}
						} else {
							// 结束
							e12.printStackTrace();
						}
					}
				}
			} else {
				// 不是值也不是方法的，直接跳出，无之后的判定处理
				return;
			}
		}
		System.err.println("in properties[" + (null == this.bundleName ? this.bundleFile.getPath() : this.bundleName) + "] not find[" + (null == field ? "null" : field.getName()) + "] ... ");
	}

	/**
	 * 得到String类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:00:18
	 * @param key 目标键
	 * @return 对应的String值
	 */
	private String getString(final String key) {
		// 因为不存在而需要取值
		final String sv = this.getValue(key);
		return sv;
	}

	/**
	 * 得到int类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 下午5:26:09
	 * @param key 目标键
	 * @return 对应的int值
	 */
	private int getInt(final String key) {
		// 因为不存在而需要取值
		final String sv = this.getValue(key);
		try {
			return Integer.parseInt(sv);
		} catch (final Exception e) {
			throw new ConfigurationException("Constants Class [" + this.getClass().getSimpleName() + "] key [" + key + "] Must int type...");
		}
	}

	/**
	 * 得到Integer类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:00:20
	 * @param key 目标键
	 * @return 对应的Integer值
	 */
	private Integer getIntObj(final String key) {
		// 因为不存在而需要取值
		final String sv = this.getValue(key);
		if (null == sv) {
			return null;
		}
		try {
			return Integer.valueOf(sv);
		} catch (final Exception e) {
			throw new ConfigurationException("Constants Class [" + this.getClass().getSimpleName() + "] key [" + key + "] Must Integer type...");
		}
	}

	/**
	 * 得到long类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 下午5:26:09
	 * @param key 目标键
	 * @return 对应的long值
	 */
	private long getLong(final String key) {
		// 因为不存在而需要取值
		final String sv = this.getValue(key);
		try {
			return Long.parseLong(sv);
		} catch (final Exception e) {
			throw new ConfigurationException("Constants Class [" + this.getClass().getSimpleName() + "] key [" + key + "] Must long type...");
		}
	}

	/**
	 * 得到Long类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:08:07
	 * @param key 目标键
	 * @return 对应的Long值
	 */
	private Long getLongObj(final String key) {
		// 因为不存在而需要取值
		final String sv = this.getValue(key);
		if (null == sv) {
			return null;
		}
		try {
			return Long.valueOf(sv);
		} catch (final Exception e) {
			throw new ConfigurationException("Constants Class [" + this.getClass().getSimpleName() + "] key [" + key + "] Must Long type...");
		}
	}

	/**
	 * 得到float类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 下午5:26:09
	 * @param key 目标键
	 * @return 对应的float值
	 */
	private float getFloat(final String key) {
		// 因为不存在而需要取值
		final String sv = this.getValue(key);
		try {
			return Float.parseFloat(sv);
		} catch (final Exception e) {
			throw new ConfigurationException("Constants Class [" + this.getClass().getSimpleName() + "] key [" + key + "] Must float type...");
		}
	}

	/**
	 * 得到Float类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:08:05
	 * @param key 目标键
	 * @return 对应的Float值
	 */
	private Float getFloatObj(final String key) {
		// 因为不存在而需要取值
		final String sv = this.getValue(key);
		if (null == sv) {
			return null;
		}
		try {
			return Float.valueOf(sv);
		} catch (final Exception e) {
			throw new ConfigurationException("Constants Class [" + this.getClass().getSimpleName() + "] key [" + key + "] Must Float type...");
		}
	}

	/**
	 * 得到double类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 下午5:26:09
	 * @param key 目标键
	 * @return 对应的double值
	 */
	private double getDouble(final String key) {
		// 因为不存在而需要取值
		final String sv = this.getValue(key);
		try {
			return Double.parseDouble(sv);
		} catch (final Exception e) {
			throw new ConfigurationException("Constants Class [" + this.getClass().getSimpleName() + "] key [" + key + "] Must double type...");
		}
	}

	/**
	 * 得到Double类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:08:03
	 * @param key 目标键
	 * @return 对应的Double值
	 */
	private Double getDoubleObj(final String key) {
		// 因为不存在而需要取值
		final String sv = this.getValue(key);
		if (null == sv) {
			return null;
		}
		try {
			return Double.valueOf(sv);
		} catch (final Exception e) {
			throw new ConfigurationException("Constants Class [" + this.getClass().getSimpleName() + "] key [" + key + "] Must Double type...");
		}
	}

	/**
	 * 得到short类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 下午5:26:09
	 * @param key 目标键
	 * @return 对应的short值
	 */
	private short getShort(final String key) {
		final String sv = this.getValue(key);
		try {
			return Short.parseShort(sv);
		} catch (final Exception e) {
			throw new ConfigurationException("Constants Class [" + this.getClass().getSimpleName() + "] key [" + key + "] Must short type...");
		}
	}

	/**
	 * 得到Short类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:08:02
	 * @param key 目标键
	 * @return 对应的Short值
	 */
	private Short getShortObj(final String key) {
		final String sv = this.getValue(key);
		if (null == sv) {
			return null;
		}
		try {
			return Short.valueOf(sv);
		} catch (final Exception e) {
			throw new ConfigurationException("Constants Class [" + this.getClass().getSimpleName() + "] key [" + key + "] Must Short type...");
		}
	}

	/**
	 * 得到boolean类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 下午5:26:09
	 * @param key 目标键
	 * @return 对应的boolean值
	 */
	private boolean getBoolean(final String key) {
		final String sv = this.getValue(key);
		try {
			return Boolean.parseBoolean(sv);
		} catch (final Exception e) {
			throw new ConfigurationException("Constants Class [" + this.getClass().getSimpleName() + "] key [" + key + "] Must boolean type...");
		}
	}

	/**
	 * 得到Boolean类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 下午5:26:09
	 * @param key 目标键
	 * @return 对应的Boolean值
	 */
	private Boolean getBooleanObj(final String key) {
		final String sv = this.getValue(key);
		if (null == sv) {
			return null;
		}
		try {
			return Boolean.valueOf(sv);
		} catch (final Exception e) {
			throw new ConfigurationException("Constants Class [" + this.getClass().getSimpleName() + "] key [" + key + "] Must Boolean type...");
		}
	}

	/**
	 * 得到String[]类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 上午11:27:32
	 * @param key 目标键
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return 对应的String[]值
	 */
	private String[] getStringArray(final String key, final String splitFlag) {
		final String sv = this.getValue(key);
		final String[] sa = StringTools.split(sv, splitFlag);
		return sa;
	}

	/**
	 * 得到List<String>类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:08:00
	 * @param key 目标键
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return 对应的List<String>值
	 */
	private List<String> getStringList(final String key, final String splitFlag) {
		final String sv = this.getValue(key);
		final List<String> sl = StringTools.splitToArray(sv, splitFlag);
		return sl;
	}

	/**
	 * 得到int[]类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 下午3:28:49
	 * @param key 目标键
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return 对应的int[]值
	 */
	private int[] getIntegerArray(final String key, final String splitFlag) {
		final String sv = this.getValue(key);
		return StringTools.toIntArray(sv, splitFlag);
	}

	/**
	 * 得到Integer[]类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 上午11:34:32
	 * @param key 目标键
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return 对应的Integer[]值
	 */
	private Integer[] getIntegerObjArray(final String key, final String splitFlag) {
		final String sv = this.getValue(key);
		return StringTools.toIntObjArray(sv, splitFlag);
	}

	/**
	 * 得到List<Integer>类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:07:59
	 * @param key 目标键
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return 对应的List<Integer>值
	 */
	private List<Integer> getIntegerList(final String key, final String splitFlag) {
		final String sv = this.getValue(key);
		final List<Integer> il = StringTools.splitToIntArray(sv, splitFlag);
		return il;
	}

	/**
	 * 得到long[]类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 下午3:33:35
	 * @param key 目标键
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return 对应的long[]值
	 */
	private long[] getLongArray(final String key, final String splitFlag) {
		final String sv = this.getValue(key);
		return StringTools.toLongArray(sv, splitFlag);
	}

	/**
	 * 得到Long[]类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 上午11:34:34
	 * @param key 目标键
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return 对应的Long[]值
	 */
	private Long[] getLongObjArray(final String key, final String splitFlag) {
		final String sv = this.getValue(key);
		return StringTools.toLongObjArray(sv, splitFlag);
	}

	/**
	 * 得到List<Long>类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:07:57
	 * @param key 目标键
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return 对应的List<Long>值
	 */
	private List<Long> getLongList(final String key, final String splitFlag) {
		final String sv = this.getValue(key);
		return StringTools.splitToLongArray(sv, splitFlag);
	}

	/**
	 * 得到short[]类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 下午3:31:33
	 * @param key 目标键
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return 对应的short[]值
	 */
	private short[] getShortArray(final String key, final String splitFlag) {
		final String sv = this.getValue(key);
		return StringTools.toShortArray(sv, splitFlag);
	}

	/**
	 * 得到Short[]类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 上午11:34:37
	 * @param key 目标键
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return 对应的Short[]值
	 */
	private Short[] getShortObjArray(final String key, final String splitFlag) {
		final String sv = this.getValue(key);
		return StringTools.toShortObjArray(sv, splitFlag);
	}

	/**
	 * 得到List<Short>类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:07:56
	 * @param key 目标键
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return 对应的List<Short>值
	 */
	private List<Short> getShortList(final String key, final String splitFlag) {
		final String sv = this.getValue(key);
		final List<Short> ll = StringTools.splitToShortArray(sv, splitFlag);
		return ll;
	}

	/**
	 * 得到List<Double>类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 * @param key 目标键
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return 对应的List<Double>值
	 */
	private List<Double> getDoubleList(final String key, final String splitFlag) {
		final String sv = this.getValue(key);
		final List<Double> ll = StringTools.splitToDoubleArray(sv, splitFlag);
		return ll;
	}

	/**
	 * 得到List<Float>类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 * @param key 目标键
	 * @param splitFlag 分隔符，一般认为是单字节字符串
	 * @return 对应的List<Float>值
	 */
	private List<Float> getFloatList(final String key, final String splitFlag) {
		final String sv = this.getValue(key);
		final List<Float> ll = StringTools.splitToFloatArray(sv, splitFlag);
		return ll;
	}

	/**
	 * 得到JSONObject类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 * @param key 目标键
	 * @return 对应的JSONObject值
	 */
	private JSONObject getJsonObjList(final String key) {
		final String sv = this.getValue(key);
		return JSONObject.parseObject(sv);
	}

	/**
	 * 得到JSONArray类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 * @param key 目标键
	 * @return 对应的JSONArray值
	 */
	private JSONArray getJsonArrList(final String key) {
		final String sv = this.getValue(key);
		return JSONArray.parseArray(sv);
	}

	/**
	 * 得到目标键的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 上午11:26:25
	 * @param key 目标键
	 * @return 目标键的值
	 */
	private String getValue(final String key) {
		try {
			// 直接获取值
			String val = this.getResourceBundle().getString(key);
			// 处理可能存在的编码问题
			val = new String(val.getBytes("ISO-8859-1"), null == Constants.SYSTEM_CODE ? "UTF-8" : Constants.SYSTEM_CODE);
			// 进行可能的内容替换操作
			return this.rep(val);
		} catch (final MissingResourceException e) {
			return null;
		} catch (final UnsupportedEncodingException e) {
			throw new ConfigurationException("Constants Class [" + this.getClass().getSimpleName() + "] key [" + key + "] is Not Exists");
		}
	}

	/**
	 * 进行嵌套值替换
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 * @param cont 目标内容
	 * @return 最后的值
	 */
	private String rep(String cont) {
		if (null == cont) {
			return cont;
		}
		final String ss = "{p:", es = "}";
		int ei = 0;
		int si = cont.indexOf(ss, ei);
		while (si != -1) {
			ei = cont.indexOf(es, si);
			if (ei == -1) {
				return cont;
			}
			String tk = cont.substring(si + ss.length(), ei);
			if (tk.length() > 0) {
				String tv = this.getValue(this.ppn + tk);
				if (null == tv) {
					tv = "";
				}
				cont = cont.replace(ss + tk + es, tv);
				si = cont.indexOf(ss, si);
			} else {
				break;
			}
		}
		return cont;
	}

	/**
	 * 得到自身相关常量类
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月22日 上午9:01:40
	 * @return 自身相关常量类
	 */
	protected BaseConstants getConstants() {
		if (this.readType == BaseConstants.FILE_TYPE) {
			final String bundlePath = this.bundleFile.getPath();
			return BaseConstants.getConstants(bundlePath);
		} else {
			return BaseConstants.getConstants(this.bundleName);
		}
	}

	/**
	 * 得到指定资源名相关常量类
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月22日 上午9:01:43
	 * @param bundleName 指定资源名
	 * @return 指定资源名相关常量类
	 */
	public static BaseConstants getConstants(final String bundleName) {
		return BaseConstants.messagePool.get(bundleName);
	}

	/**
	 * 得到指定key的内容
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月8日 下午12:23:21
	 * @param key 目标key名
	 * @return 对应的内容
	 */
	public String getTargetValue(final String key) {
		return this.getValue(this.ppn + key);
	}

	/**
	 * 打印所有可见属性信息<br />
	 * 测试用方法<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 */
	public void printAllAttr() {
		final Field[] fields = this.getClass().getFields();
		Object val;
		for (final Field field : fields) {
			try {
				val = field.get(this);
				if (null != val) {
					if (field.getGenericType() == int.class) {
						if ((int) val == 0) {
							System.err.println("\terr >>> " + field.getName() + "<may be int Err>");
							continue;
						}
					}
					if (field.getGenericType() == long.class) {
						if ((long) val == 0) {
							System.err.println("\terr >>> " + field.getName() + "<may be long Err>");
							continue;
						}
					}
					if (field.getGenericType() == short.class) {
						if ((short) val == 0) {
							System.err.println("\terr >>> " + field.getName() + "<may be short Err>");
							continue;
						}
					}
					if (field.getGenericType() == double.class) {
						if ((double) val == 0) {
							System.err.println("\terr >>> " + field.getName() + "<may be double Err>");
							continue;
						}
					}
					if (field.getGenericType() == float.class) {
						if ((float) val == 0) {
							System.err.println("\terr >>> " + field.getName() + "<may be float Err>");
							continue;
						}
					}
					System.out.println(field.getName() + "=><" + field.get(this) + ">");
					continue;
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			System.err.println("\terr >>> " + field.getName() + "<err>");
		}
	}
}
