/**
 * @author tfzzh
 * @dateTime 2016年11月21日 上午11:10:24
 */
package com.tfzzh.tools;

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
	 * 得到所相关资源数据
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 上午11:31:13
	 * @return 所相关资源数据
	 */
	protected ResourceBundle getResourceBundle() {
		return this.resourceBundle;
	}

	/**
	 * 刷新数据<br />
	 * 当前就是清理缓存<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 上午11:31:04
	 */
	public void refresh() {
		this.clearCache();
		// 在这里刷新值
		this.refreshValue();
	}

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
		// synchronized (this.valMap) {
		// this.valMap.clear();
		// }
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
	@SuppressWarnings("unchecked")
	private void refreshValue() {
		this.resourceBundle = FileTools.getResourceBundle(this.bundleName, this.getClass());
		synchronized (BaseConstants.messagePool) {
			final ClassLoader cl = this.getClass().getClassLoader();
			List<String> sl = BaseConstants.clPool.get(cl);
			if (null == sl) {
				sl = new ArrayList<>();
				BaseConstants.clPool.put(cl, sl);
			}
			sl.add(this.bundleName);
			BaseConstants.messagePool.put(this.bundleName, this);
		}
		// 得到所有字段
		Class<?> clz = this.getClass();
		do {
			this.setParameters((Class<? extends BaseConstants>) clz);
			clz = clz.getSuperclass();
		} while (BaseConstants.class.isAssignableFrom(clz.getSuperclass()));
	}

	/**
	 * 设置属性的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午3:03:19
	 * @param pClz 所相关的类对象
	 */
	private void setParameters(final Class<? extends BaseConstants> pClz) {
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
		final Field[] fields = pClz.getDeclaredFields();
		PropertiesValue fv;
		PropertiesMethod fm;
		Method m;
		String fn, mn;
		PropertiesValueTypeEnum pvt;
		for (final Field field : fields) {
			fv = field.getAnnotation(PropertiesValue.class);
			if (null != fv) {
				// 需要进行的操作
				fn = fv.value();
				if (fn.length() == 0) {
					fn = this.ppn + field.getName();
				} else {
					fn = this.ppn + fn;
				}
				pvt = fv.type();
				// if (pvt == PropertiesValueTypeEnum.Auto) {
				// 进行转译
				final Class<?> fc = field.getType();
				try {
					if (fc == String.class) {
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getString(fn));
					} else if (fc == int.class) {
						field.setInt((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getInt(fn));
					} else if (fc == Integer.class) {
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getIntObj(fn));
					} else if (fc == long.class) {
						field.setLong((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getLong(fn));
					} else if (fc == Long.class) {
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getLongObj(fn));
					} else if (fc == short.class) {
						field.setShort((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getShort(fn));
					} else if (fc == Short.class) {
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getShortObj(fn));
					} else if (fc == double.class) {
						field.setDouble((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getDouble(fn));
					} else if (fc == Double.class) {
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getDoubleObj(fn));
					} else if (fc == float.class) {
						field.setFloat((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getFloat(fn));
					} else if (fc == Float.class) {
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getFloatObj(fn));
					} else if (fc == boolean.class) {
						field.setBoolean((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getBoolean(fn));
					} else if (fc == Boolean.class) {
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getBooleanObj(fn));
					} else if (fc == int[].class) {
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getIntegerArray(fn, fv.specialValue()));
					} else if (fc == String[].class) {
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getStringArray(fn, fv.specialValue()));
					} else if (fc == long[].class) {
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getLongArray(fn, fv.specialValue()));
					} else if (fc == short[].class) {
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getShortArray(fn, fv.specialValue()));
					} else if (fc == Integer[].class) {
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getIntegerObjArray(fn, fv.specialValue()));
					} else if (fc == Long[].class) {
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getLongObjArray(fn, fv.specialValue()));
					} else if (fc == Short[].class) {
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getShortObjArray(fn, fv.specialValue()));
					} else if (List.class.isAssignableFrom(fc)) {
						final Type ft = field.getGenericType();
						if ((null != ft) && ParameterizedType.class.isAssignableFrom(ft.getClass())) {
							// 自动处理
							final Type lt = ((ParameterizedType) ft).getActualTypeArguments()[0];
							switch (lt.getTypeName()) {
							case "java.lang.String":
								field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getStringList(fn, fv.specialValue()));
								continue;
							case "java.lang.Integer":
								field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getIntegerList(fn, fv.specialValue()));
								continue;
							case "java.lang.Long":
								field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getLongList(fn, fv.specialValue()));
								continue;
							case "java.lang.Short":
								field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getShortList(fn, fv.specialValue()));
								continue;
							}
						}
						// 注解处理
						switch (pvt) {
						case String:
							field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getStringList(fn, fv.specialValue()));
							break;
						case Integer:
							field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getIntegerList(fn, fv.specialValue()));
							break;
						case Long:
							field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getLongList(fn, fv.specialValue()));
							break;
						case Short:
							field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getShortList(fn, fv.specialValue()));
							break;
						default:
							throw new ConfigurationException("Error Annotation type in [" + pClz.getName() + "::" + field.getName() + "]--" + pvt.name());
						}
					}
				} catch (final IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			} else {
				fm = field.getAnnotation(PropertiesMethod.class);
				if (null != fm) {
					mn = fm.value();
					if (fm.value().length() == 0) {
						mn = "asse" + StringTools.assemblyStringWhitInterval(field.getName(), true, true);
					} else {
						mn = fm.value();
					}
					try {
						m = this.getClass().getDeclaredMethod(mn);
						m.setAccessible(true);
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), m.invoke(this));
						m.setAccessible(false);
					} catch (final NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
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
	 * 得到目标键的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 上午11:26:25
	 * @param key 目标键
	 * @return 目标键的值
	 */
	private String getValue(final String key) {
		try {
			final String val = this.getResourceBundle().getString(key);
			return new String(val.getBytes("ISO-8859-1"), null == Constants.SYSTEM_CODE ? "UTF-8" : Constants.SYSTEM_CODE);
		} catch (final MissingResourceException e) {
			return null;
		} catch (final UnsupportedEncodingException e) {
			throw new ConfigurationException("Constants Class [" + this.getClass().getSimpleName() + "] key [" + key + "] is Not Exists");
		}
	}

	/**
	 * 得到自身相关常量类
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月22日 上午9:01:40
	 * @return 自身相关常量类
	 */
	protected BaseConstants getConstants() {
		return BaseConstants.getConstants(this.bundleName);
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
}
