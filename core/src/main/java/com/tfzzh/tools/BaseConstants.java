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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
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
	 * @author tfzzh
	 * @dateTime 2020年7月29日 下午8:36:39
	 */
	protected final Logger log = LogManager.getLogger(this.getClass());

	/**
	 * 消息池
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 上午11:45:13
	 */
	private static final Map<String, BaseConstants> messagePool = new HashMap<>();

	/**
	 * 类加载对象池
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午12:08:52
	 */
	private static final Map<ClassLoader, List<String>> clPool = new HashMap<>();

	/**
	 * 文件信息类型
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 */
	private static final int FILE_TYPE = 2;

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
	protected BaseConstants() {
		ResourceBundle.clearCache(this.getClass().getClassLoader());
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
	protected BaseConstants(final String bundleName) {
		ResourceBundle.clearCache(this.getClass().getClassLoader());
		this.setBundleName(bundleName);
		// 因为初始化加入到池
		CoreConstantsPool.getInstance().putConstants(this);
	}

	/**
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 * @param bundleFile 所相关资源文件信息
	 */
	protected BaseConstants(final File bundleFile) {
		ResourceBundle.clearCache(this.getClass().getClassLoader());
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
				if (null != sl) {
					BaseConstants bm;
					for (final String s : sl) {
						bm = BaseConstants.messagePool.remove(s);
						if (null != bm) {
							bm.clearResource();
						}
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
						bm.clearCache();
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
		// final Map<String, String> pps = new HashMap<>();
		final Field[] fields = this.getClass().getDeclaredFields();
		for (final Field field : fields) {
			this.valToField(field);
		}
	}

	/**
	 * 将值放入到字段
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 * @param field 目标字段
	 */
	private void valToField(final Field field) {
		final PropertiesValue fv = field.getAnnotation(PropertiesValue.class);
		if (null != fv) {
			// 需要进行的操作
			String fn = fv.value();
			if (fn.length() == 0) {
				fn = this.ppn + field.getName();
			} else {
				fn = this.ppn + fn;
			}
			final PropertiesValueTypeEnum pvt = fv.type();
			// 进行转译
			final Class<?> fc = field.getType();
			try {
				if (fc == String.class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getString(fn, fv));
					return;
				} else if (fc == int.class) {
					field.setInt((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getInt(fn, fv));
					return;
				} else if (fc == Integer.class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getIntObj(fn, fv));
					return;
				} else if (fc == long.class) {
					field.setLong((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getLong(fn, fv));
					return;
				} else if (fc == Long.class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getLongObj(fn, fv));
					return;
				} else if (fc == short.class) {
					field.setShort((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getShort(fn, fv));
					return;
				} else if (fc == Short.class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getShortObj(fn, fv));
					return;
				} else if (fc == double.class) {
					field.setDouble((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getDouble(fn, fv));
					return;
				} else if (fc == Double.class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getDoubleObj(fn, fv));
					return;
				} else if (fc == float.class) {
					field.setFloat((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getFloat(fn, fv));
					return;
				} else if (fc == Float.class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getFloatObj(fn, fv));
					return;
				} else if (fc == boolean.class) {
					field.setBoolean((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getBoolean(fn, fv));
					return;
				} else if (fc == Boolean.class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getBooleanObj(fn, fv));
					return;
				} else if (fc == int[].class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getIntegerArray(fn, fv));
					return;
				} else if (fc == String[].class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getStringArray(fn, fv));
					return;
				} else if (fc == long[].class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getLongArray(fn, fv));
					return;
				} else if (fc == short[].class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getShortArray(fn, fv));
					return;
				} else if (fc == Integer[].class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getIntegerObjArray(fn, fv));
					return;
				} else if (fc == Long[].class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getLongObjArray(fn, fv));
					return;
				} else if (fc == Short[].class) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getShortObjArray(fn, fv));
					return;
				} else if (List.class.isAssignableFrom(fc)) {
					final Type ft = field.getGenericType();
					if ((null != ft) && ParameterizedType.class.isAssignableFrom(ft.getClass())) {
						// 自动处理
						final Type lt = ((ParameterizedType) ft).getActualTypeArguments()[0];
						switch (lt.getTypeName()) {
						case "java.lang.String":
							field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getStringList(fn, fv));
							return;
						case "java.lang.Integer":
							field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getIntegerList(fn, fv));
							return;
						case "java.lang.Long":
							field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getLongList(fn, fv));
							return;
						case "java.lang.Short":
							field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getShortList(fn, fv));
							return;
						case "java.lang.Double":
							field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getDoubleList(fn, fv));
							return;
						case "java.lang.Float":
							field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getFloatList(fn, fv));
							return;
						}
					} else if (JSONArray.class.getName().equals(ft.getTypeName())) {
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getJsonArrList(fn, fv));
						return;
					}
					// 注解处理
					switch (pvt) {
					case String:
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getStringList(fn, fv));
						return;
					case Integer:
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getIntegerList(fn, fv));
						return;
					case Long:
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getLongList(fn, fv));
						return;
					case Short:
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getShortList(fn, fv));
						return;
					case Double:
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getDoubleList(fn, fv));
						return;
					case Float:
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getFloatList(fn, fv));
						return;
					default:
						throw new ConfigurationException("Error Annotation type in [" + this.getClass().getName() + "::" + field.getName() + "]--" + pvt.name());
					}
				} else if (JSONObject.class.isAssignableFrom(fc)) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getJsonObjList(fn, fv));
					return;
				} else if (JSONArray.class.isAssignableFrom(fc)) {
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), this.getJsonArrList(fn, fv));
					return;
				}
			} catch (final IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			final PropertiesMethod fm = field.getAnnotation(PropertiesMethod.class);
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
					// m = this.getClass().getDeclaredMethod(mn);
					m = this.classMethod(this.getClass(), mn);
					if (!m.trySetAccessible()) {
						m.setAccessible(true);
					}
					// m.setAccessible(true);
					field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), m.invoke(this));
					// m.setAccessible(false);
					return;
				} catch (final NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e11) {
					try {
						// m = this.getClass().getDeclaredMethod(mn, String.class);
						m = this.classMethod(this.getClass(), mn, String.class);
						if (!m.trySetAccessible()) {
							m.setAccessible(true);
						}
						// m.setAccessible(true);
						field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), m.invoke(this, this.getString(fn, fv)));
						// m.setAccessible(false);
						return;
					} catch (final NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e12) {
						if (fm.clz() != Object.class) {
							try {
								// m = this.getClass().getDeclaredMethod(mn, String.class, Class.class);
								m = this.classMethod(this.getClass(), mn, String.class, Class.class, String.class);
								if (!m.trySetAccessible()) {
									m.setAccessible(true);
								}
								// m.setAccessible(true);
								field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), m.invoke(this, this.getString(fn, fv), fm.clz(), fm.mapKey()));
								// m.setAccessible(false);
								return;
							} catch (final NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e13) {
								// 结束
								e13.printStackTrace();
							}
						} else {
							// 增加对assemble全称前缀的支持
							if (fm.value().length() == 0) {
								mn = "assemble" + StringTools.assemblyStringWhitInterval(field.getName(), true, true);
								try {
									// m = this.getClass().getDeclaredMethod(mn);
									m = this.classMethod(this.getClass(), mn);
									if (!m.trySetAccessible()) {
										m.setAccessible(true);
									}
									// m.setAccessible(true);
									field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), m.invoke(this));
									// m.setAccessible(false);
									return;
								} catch (final NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e21) {
									try {
										// m = this.getClass().getDeclaredMethod(mn, String.class);
										m = this.classMethod(this.getClass(), mn, String.class);
										if (!m.trySetAccessible()) {
											m.setAccessible(true);
										}
										// m.setAccessible(true);
										field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), m.invoke(this, this.getString(fn, fv)));
										// m.setAccessible(false);
										return;
									} catch (final NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e22) {
										// 结束
										if (fm.clz() != Object.class) {
											try {
												// m = this.getClass().getDeclaredMethod(mn, String.class, Class.class);
												m = this.classMethod(this.getClass(), mn, String.class, Class.class, String.class);
												if (!m.trySetAccessible()) {
													m.setAccessible(true);
												}
												// m.setAccessible(true);
												field.set((((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ? null : this), m.invoke(this, this.getString(fn, fv), fm.clz(), fm.mapKey()));
												// m.setAccessible(false);
												return;
											} catch (final NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e23) {
												// 结束
												e23.printStackTrace();
											}
										} else {
											e22.printStackTrace();
										}
									}
								}
							} else {
								// 结束
								e12.printStackTrace();
							}
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
	 * 得到类方法
	 * 
	 * @author tfzzh
	 * @dateTime 2021年1月19日 下午3:58:38
	 * @param clz 目标类
	 * @param methodName 目标方法名
	 * @param clzs 请求参数
	 * @return 目标 方法
	 * @throws NoSuchMethodException 抛
	 */
	private Method classMethod(Class<?> clz, final String methodName, final Class<?>... clzs) throws NoSuchMethodException {
		while (clz != Object.class) {
			try {
				final Method m = clz.getDeclaredMethod(methodName, clzs);
				return m;
			} catch (final NoSuchMethodException | SecurityException e) {
			}
			clz = clz.getSuperclass();
		}
		throw new NoSuchMethodException();
	}

	/**
	 * 得到String类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:00:18
	 * @param key 目标键
	 * @param pv 属性值控制注解
	 * @return 对应的String值
	 */
	private String getString(final String key, final PropertiesValue pv) {
		// 因为不存在而需要取值
		String sv = this.getValue(key, pv);
		if (!StringTools.isNullOrEmpty(sv)) {
			sv = sv.intern();
		}
		return sv;
	}

	/**
	 * 得到int类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 下午5:26:09
	 * @param key 目标键
	 * @param pv 属性值控制注解
	 * @return 对应的int值
	 */
	private int getInt(final String key, final PropertiesValue pv) {
		// 因为不存在而需要取值
		final String sv = this.getValue(key, pv);
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
	 * @param pv 属性值控制注解
	 * @return 对应的Integer值
	 */
	private Integer getIntObj(final String key, final PropertiesValue pv) {
		// 因为不存在而需要取值
		final String sv = this.getValue(key, pv);
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
	 * @param pv 属性值控制注解
	 * @return 对应的long值
	 */
	private long getLong(final String key, final PropertiesValue pv) {
		// 因为不存在而需要取值
		final String sv = this.getValue(key, pv);
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
	 * @param pv 属性值控制注解
	 * @return 对应的Long值
	 */
	private Long getLongObj(final String key, final PropertiesValue pv) {
		// 因为不存在而需要取值
		final String sv = this.getValue(key, pv);
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
	 * @param pv 属性值控制注解
	 * @return 对应的float值
	 */
	private float getFloat(final String key, final PropertiesValue pv) {
		// 因为不存在而需要取值
		final String sv = this.getValue(key, pv);
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
	 * @param pv 属性值控制注解
	 * @return 对应的Float值
	 */
	private Float getFloatObj(final String key, final PropertiesValue pv) {
		// 因为不存在而需要取值
		final String sv = this.getValue(key, pv);
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
	 * @param pv 属性值控制注解
	 * @return 对应的double值
	 */
	private double getDouble(final String key, final PropertiesValue pv) {
		// 因为不存在而需要取值
		final String sv = this.getValue(key, pv);
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
	 * @param pv 属性值控制注解
	 * @return 对应的Double值
	 */
	private Double getDoubleObj(final String key, final PropertiesValue pv) {
		// 因为不存在而需要取值
		final String sv = this.getValue(key, pv);
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
	 * @param pv 属性值控制注解
	 * @return 对应的short值
	 */
	private short getShort(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
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
	 * @param pv 属性值控制注解
	 * @return 对应的Short值
	 */
	private Short getShortObj(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
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
	 * @param pv 属性值控制注解
	 * @return 对应的boolean值
	 */
	private boolean getBoolean(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
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
	 * @param pv 属性值控制注解
	 * @return 对应的Boolean值
	 */
	private Boolean getBooleanObj(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
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
	 * @param pv 属性值控制注解
	 * @return 对应的String[]值
	 */
	private String[] getStringArray(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
		final String[] sa = StringTools.split(sv, pv.specialValue());
		return sa;
	}

	/**
	 * 得到List<String>类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:08:00
	 * @param key 目标键
	 * @param pv 属性值控制注解
	 * @return 对应的List<String>值
	 */
	private List<String> getStringList(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
		final List<String> sl = StringTools.splitToArray(sv, pv.specialValue());
		return sl;
	}

	/**
	 * 得到int[]类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 下午3:28:49
	 * @param key 目标键
	 * @param pv 属性值控制注解
	 * @return 对应的int[]值
	 */
	private int[] getIntegerArray(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
		return StringTools.toIntArray(sv, pv.specialValue());
	}

	/**
	 * 得到Integer[]类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 上午11:34:32
	 * @param key 目标键
	 * @param pv 属性值控制注解
	 * @return 对应的Integer[]值
	 */
	private Integer[] getIntegerObjArray(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
		return StringTools.toIntObjArray(sv, pv.specialValue());
	}

	/**
	 * 得到List<Integer>类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:07:59
	 * @param key 目标键
	 * @param pv 属性值控制注解
	 * @return 对应的List<Integer>值
	 */
	private List<Integer> getIntegerList(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
		final List<Integer> il = StringTools.splitToIntArray(sv, pv.specialValue());
		return il;
	}

	/**
	 * 得到long[]类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 下午3:33:35
	 * @param key 目标键
	 * @param pv 属性值控制注解
	 * @return 对应的long[]值
	 */
	private long[] getLongArray(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
		return StringTools.toLongArray(sv, pv.specialValue());
	}

	/**
	 * 得到Long[]类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 上午11:34:34
	 * @param key 目标键
	 * @param pv 属性值控制注解
	 * @return 对应的Long[]值
	 */
	private Long[] getLongObjArray(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
		return StringTools.toLongObjArray(sv, pv.specialValue());
	}

	/**
	 * 得到List<Long>类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:07:57
	 * @param key 目标键
	 * @param pv 属性值控制注解
	 * @return 对应的List<Long>值
	 */
	private List<Long> getLongList(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
		return StringTools.splitToLongArray(sv, pv.specialValue());
	}

	/**
	 * 得到short[]类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 下午3:31:33
	 * @param key 目标键
	 * @param pv 属性值控制注解
	 * @return 对应的short[]值
	 */
	private short[] getShortArray(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
		return StringTools.toShortArray(sv, pv.specialValue());
	}

	/**
	 * 得到Short[]类型的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月24日 上午11:34:37
	 * @param key 目标键
	 * @param pv 属性值控制注解
	 * @return 对应的Short[]值
	 */
	private Short[] getShortObjArray(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
		return StringTools.toShortObjArray(sv, pv.specialValue());
	}

	/**
	 * 得到List<Short>类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:07:56
	 * @param key 目标键
	 * @param pv 属性值控制注解
	 * @return 对应的List<Short>值
	 */
	private List<Short> getShortList(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
		final List<Short> ll = StringTools.splitToShortArray(sv, pv.specialValue());
		return ll;
	}

	/**
	 * 得到List<Double>类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 * @param key 目标键
	 * @param pv 属性值控制注解
	 * @return 对应的List<Double>值
	 */
	private List<Double> getDoubleList(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
		final List<Double> ll = StringTools.splitToDoubleArray(sv, pv.specialValue());
		return ll;
	}

	/**
	 * 得到List<Float>类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 * @param key 目标键
	 * @param pv 属性值控制注解
	 * @return 对应的List<Float>值
	 */
	private List<Float> getFloatList(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
		final List<Float> ll = StringTools.splitToFloatArray(sv, pv.specialValue());
		return ll;
	}

	/**
	 * 得到JSONObject类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 * @param key 目标键
	 * @param pv 属性值控制注解
	 * @return 对应的JSONObject值
	 */
	private JSONObject getJsonObjList(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
		return JSON.parseObject(sv, JSONReader.Feature.AllowUnQuotedFieldNames);
	}

	/**
	 * 得到JSONArray类型的值
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 * @param key 目标键
	 * @param pv 属性值控制注解
	 * @return 对应的JSONArray值
	 */
	private JSONArray getJsonArrList(final String key, final PropertiesValue pv) {
		final String sv = this.getValue(key, pv);
		return JSON.parseArray(sv, JSONReader.Feature.AllowUnQuotedFieldNames);
	}

	/**
	 * 得到目标键的值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 上午11:26:25
	 * @param key 目标键
	 * @param pv 属性值控制注解
	 * @return 目标键的值
	 */
	private String getValue(final String key, final PropertiesValue pv) {
		try {
			// 直接获取值
			String val = this.getResourceBundle().getString(key);
			if (null == val) {
				final String dv = pv.defVal();
				if (!StringTools.isNullOrEmpty(dv)) {
					return dv;
				}
				return null;
			} else {
				// 处理可能存在的编码问题
				int couLm = 0;
				String tVal = val;
				char c;
				for (int i = 0; i < tVal.length(); i++) {
					c = tVal.charAt(i);
					if (c == 63) {
						if (++couLm >= 2) {
							break;
						}
					} else {
						if (couLm > 0) {
							couLm = 0;
						}
					}
				}
				if (couLm >= 2) {
					try {
						tVal = new String(tVal.getBytes("ISO-8859-1"), null == Constants.SYSTEM_CODE ? "UTF-8" : Constants.SYSTEM_CODE);
						for (int i = 0; i < tVal.length(); i++) {
							c = tVal.charAt(i);
							if (c == 63) {
								if (++couLm >= 2) {
									break;
								}
							} else {
								if (couLm > 0) {
									couLm = 0;
								}
							}
						}
						if (couLm >= 2) {
							tVal = val;
						}
					} catch (final UnsupportedEncodingException e) {
						throw new ConfigurationException("Constants Class [" + this.getClass().getSimpleName() + "] key [" + key + "] is Not Exists");
					}
				}
				val = tVal;
			}
			// 进行可能的内容替换操作
			return this.rep(val, pv);
		} catch (final MissingResourceException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 进行嵌套值替换
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 * @param cont 目标内容
	 * @param pv 属性值控制注解
	 * @return 最后的值
	 */
	private String rep(String cont, final PropertiesValue pv) {
		if (null == cont) {
			return cont;
		}
		final String ss = "{p:";
		final String es = "}";
		int ei = 0;
		int si = cont.indexOf(ss, ei);
		while (si != -1) {
			ei = cont.indexOf(es, si);
			if (ei == -1) {
				return cont;
			}
			final String tk = cont.substring(si + ss.length(), ei);
			if (tk.length() > 0) {
				String tv = this.getValue(this.ppn + tk, pv);
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
	 * @param pv 属性值控制注解
	 * @return 对应的内容
	 */
	public String getTargetValue(final String key, final PropertiesValue pv) {
		return this.getValue(this.ppn + key, pv);
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

	/**
	 * 进行对应数据解析
	 * 
	 * @author tfzzh
	 * @dateTime 2020年9月20日 下午9:34:17
	 * @param cont 配置文件内容
	 * @return 解析后的内容
	 */
	protected Set<String> strToSet(final String cont) {
		final List<String> sl = StringTools.splitToArray(cont, "|");
		return new HashSet<>(sl);
	}

	/**
	 * 进行对应数据解析
	 * 
	 * @author tfzzh
	 * @dateTime 2020年11月18日 下午12:54:09
	 * @param cont 配置文件内容
	 * @return 解析后的内容
	 */
	protected Map<String, Set<String>> strToStrSet(final String cont) {
		// this.log.debug("in strToStrSet ... ");
		if ((null == cont) || (cont.length() == 0)) {
			return new HashMap<>();
		}
		final JSONObject jo = JSON.parseObject(cont, JSONReader.Feature.AllowUnQuotedFieldNames);
		final Map<String, Set<String>> bak = new HashMap<>();
		Set<String> ss;
		for (final String key : jo.keySet()) {
			final List<String> sl = StringTools.splitToArray(jo.getString(key), "|");
			ss = new HashSet<>(sl);
			bak.put(key, ss);
		}
		// if (bak.size() == 0) {
		// this.log.error("no conf strToStrSet ... ");
		// }
		return bak;
	}

	/**
	 * 进行对应数据解析
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月8日 下午3:35:08
	 * @param cont 配置文件内容
	 * @return 解析后的内容
	 */
	protected Map<String, Set<String>> strToStrLinkedSet(final String cont) {
		// this.log.debug("in strToStrLinkedSet ... ");
		if ((null == cont) || (cont.length() == 0)) {
			return new HashMap<>();
		}
		final JSONObject jo = JSON.parseObject(cont, JSONReader.Feature.AllowUnQuotedFieldNames);
		final Map<String, Set<String>> bak = new HashMap<>();
		Set<String> ss;
		for (final String key : jo.keySet()) {
			final List<String> sl = StringTools.splitToArray(jo.getString(key), "|");
			ss = new LinkedHashSet<>(sl);
			bak.put(key, ss);
		}
		// if (bak.size() == 0) {
		// this.log.error("no conf strToStrLinkedSet ... ");
		// }
		return bak;
	}

	/**
	 * 进行对应数据解析
	 * 
	 * @author tfzzh
	 * @dateTime 2021年7月2日 下午9:11:03
	 * @param cont 配置文件内容
	 * @return 解析后的内容
	 */
	protected Map<String, Map<String, String>> strToStrMap(final String cont) {
		// this.log.debug("in strToStrLinkedSet ... ");
		if ((null == cont) || (cont.length() == 0)) {
			return new HashMap<>();
		}
		final JSONObject jo = JSON.parseObject(cont, JSONReader.Feature.AllowUnQuotedFieldNames);
		final Map<String, Map<String, String>> bak = new HashMap<>();
		Map<String, String> sm;
		JSONObject tjo;
		for (final String key : jo.keySet()) {
			tjo = jo.getJSONObject(key);
			sm = new HashMap<>();
			bak.put(key, sm);
			for (final String tKey : tjo.keySet()) {
				sm.put(tKey, tjo.getString(tKey));
			}
		}
		// if (bak.size() == 0) {
		// this.log.error("no conf strToStrLinkedSet ... ");
		// }
		return bak;
	}

	/**
	 * 进行对应数据解析
	 * 
	 * @author tfzzh
	 * @dateTime 2020年8月17日 下午3:39:09
	 * @param cont 配置文件内容
	 * @return 解析后的内容
	 */
	protected Map<String, Integer> strToStrInt(final String cont) {
		// this.log.debug("in strToStrInt ... ");
		if ((null == cont) || (cont.length() == 0)) {
			return new HashMap<>();
		}
		final JSONObject jo = JSON.parseObject(cont, JSONReader.Feature.AllowUnQuotedFieldNames);
		final Map<String, Integer> bak = new HashMap<>();
		for (final String key : jo.keySet()) {
			bak.put(key, jo.getInteger(key));
		}
		// if (bak.size() == 0) {
		// this.log.error("no conf strToStrInt ... ");
		// }
		return bak;
	}

	/**
	 * 进行对应数据解析
	 * 
	 * @author tfzzh
	 * @dateTime 2020年10月19日 下午12:42:24
	 * @param cont 配置文件内容
	 * @return 解析后的内容
	 */
	protected Map<String, String> strToStrStr(final String cont) {
		// this.log.debug("in strToStrStr ... ");
		if ((null == cont) || (cont.length() == 0)) {
			return new HashMap<>();
		}
		final JSONObject jo = JSON.parseObject(cont, JSONReader.Feature.AllowUnQuotedFieldNames);
		final Map<String, String> bak = new HashMap<>();
		for (final String key : jo.keySet()) {
			bak.put(key, jo.getString(key));
		}
		// if (bak.size() == 0) {
		// this.log.error("no conf strToStrStr ... ");
		// }
		return bak;
	}

	/**
	 * 进行对应数据解析<br />
	 * string转为string-key，jsonString为value情况<br />
	 * 过程存在将内容转为json对象再转回的操作<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2021年3月4日 下午4:26:16
	 * @param cont 配置文件内容
	 * @return 解析后的内容
	 */
	protected Map<String, String> strToStrJson(final String cont) {
		// this.log.debug("in strToStrJson ... ");
		if ((null == cont) || (cont.length() == 0)) {
			return new HashMap<>();
		}
		final JSONObject jo = JSON.parseObject(cont, JSONReader.Feature.AllowUnQuotedFieldNames);
		final Map<String, String> bak = new HashMap<>();
		String js;
		Object tjo;
		for (final String key : jo.keySet()) {
			js = jo.getString(key);
			try {
				tjo = JSON.parse(js, JSONReader.Feature.AllowUnQuotedFieldNames);
				bak.put(key, JSON.toJSONString(tjo));
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		// if (bak.size() == 0) {
		// this.log.error("no conf strToStrJson ... ");
		// }
		return bak;
	}

	/**
	 * 进行对应数据解析
	 * 
	 * @author tfzzh
	 * @dateTime 2020年11月17日 下午5:48:34
	 * @param <O> 目标数据对象
	 * @param cont 配置文件内容
	 * @param clz 目标数据对象类
	 * @param mapKey 如果是列表数据，则为key对应属性
	 * @return 解析后的内容
	 */
	protected <O extends ObjectInfo> Map<String, List<O>> strToListObject(final String cont, final Class<O> clz, final String mapKey) {
		// this.log.debug("in strToListObject ... ");
		if ((null == cont) || (cont.length() == 0)) {
			return new HashMap<>();
		}
		final Map<String, List<O>> bak = new HashMap<>();
		if (cont.startsWith("{")) {
			final JSONObject jo = JSON.parseObject(cont, JSONReader.Feature.AllowUnQuotedFieldNames);
			List<O> sl;
			JSONArray ja;
			JSONObject ijo;
			O o;
			final Class<?>[] clza = new Class[] { JSONObject.class };
			Object[] obja;
			for (final String key : jo.keySet()) {
				ja = jo.getJSONArray(key);
				sl = new ArrayList<>();
				for (int i = 0; i < ja.size(); i++) {
					ijo = ja.getJSONObject(i);
					obja = new Object[] { ijo };
					try {
						o = InstanceFactory.classInstance(clz, clza, obja);
						sl.add(o);
					} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				bak.put(key, sl);
			}
		} else if (cont.startsWith("[") || !StringTools.isNullOrEmpty(mapKey)) {
			final JSONArray ja = JSON.parseArray(cont, JSONReader.Feature.AllowUnQuotedFieldNames);
			List<O> sl;
			JSONObject jo;
			O o;
			final Class<?>[] clza = new Class[] { JSONObject.class };
			Object[] obja;
			String key;
			for (int i = 0; i < ja.size(); i++) {
				jo = ja.getJSONObject(i);
				key = jo.getString(mapKey);
				sl = bak.get(key);
				if (null == sl) {
					sl = new ArrayList<>();
					bak.put(key, sl);
				}
				obja = new Object[] { jo };
				try {
					o = InstanceFactory.classInstance(clz, clza, obja);
					sl.add(o);
				} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		} // 其他问题数据不过多处理
			// if (bak.size() == 0) {
			// this.log.error("no conf strToListObject ... ");
			// }
		return bak;
	}

	/**
	 * 进行对应数据解析
	 * 
	 * @author tfzzh
	 * @dateTime 2021年4月8日 下午5:20:34
	 * @param <O> 目标数据对象
	 * @param cont 配置文件内容
	 * @param clz 目标数据对象类
	 * @param mapKey 通用属性，这里无效
	 * @return 解析后的内容
	 */
	protected <O extends ObjectInfo> List<O> onlyListObject(final String cont, final Class<O> clz, final String mapKey) {
		// this.log.debug("in onlyListObject ... ");
		if ((null == cont) || (cont.length() == 0)) {
			return new ArrayList<>();
		}
		final JSONArray ja = JSON.parseArray(cont, JSONReader.Feature.AllowUnQuotedFieldNames);
		final List<O> bak = new ArrayList<>();
		JSONObject ijo;
		O o;
		final Class<?>[] clza = new Class[] { JSONObject.class };
		Object[] obja;
		for (int i = 0; i < ja.size(); i++) {
			ijo = ja.getJSONObject(i);
			obja = new Object[] { ijo };
			try {
				o = InstanceFactory.classInstance(clz, clza, obja);
				bak.add(o);
			} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		// if (bak.size() == 0) {
		// this.log.error("no conf onlyListObject ... ");
		// }
		return bak;
	}

	/**
	 * 进行对应数据解析
	 * 
	 * @author tfzzh
	 * @dateTime 2020年11月20日 下午8:17:26
	 * @param <O> 目标数据对象
	 * @param cont 配置文件内容
	 * @param clz 目标数据对象类
	 * @param mapKey 如果是列表数据，则为key对应属性
	 * @return 解析后的内容
	 */
	protected <O extends ObjectInfo> Map<String, O> strToObject(final String cont, final Class<O> clz, final String mapKey) {
		// this.log.debug("in strToListObject ... ");
		if ((null == cont) || (cont.length() == 0)) {
			return new HashMap<>();
		}
		final Map<String, O> bak = new HashMap<>();
		if (cont.startsWith("{")) {
			// 正常对象
			final JSONObject jo = JSON.parseObject(cont, JSONReader.Feature.AllowUnQuotedFieldNames);
			JSONObject ijo;
			O o;
			final Class<?>[] clza = new Class[] { JSONObject.class };
			Object[] obja;
			for (final String key : jo.keySet()) {
				ijo = jo.getJSONObject(key);
				obja = new Object[] { ijo };
				try {
					o = InstanceFactory.classInstance(clz, clza, obja);
					bak.put(key, o);
				} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		} else if (cont.startsWith("[") || !StringTools.isNullOrEmpty(mapKey)) {
			// 列表模式 2021-05-28
			final JSONArray ja = JSON.parseArray(cont, JSONReader.Feature.AllowUnQuotedFieldNames);
			JSONObject ijo;
			O o;
			final Class<?>[] clza = new Class[] { JSONObject.class };
			Object[] obja;
			String key;
			for (int i = 0; i < ja.size(); i++) {
				ijo = ja.getJSONObject(i);
				key = ijo.getString(mapKey);
				obja = new Object[] { ijo };
				try {
					o = InstanceFactory.classInstance(clz, clza, obja);
					bak.put(key, o);
				} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		} // 其他问题数据不过多处理
			// if (bak.size() == 0) {
			// this.log.error("no conf strToListObject ... ");
			// }
		return bak;
	}

	/**
	 * 进行对应数据解析
	 * 
	 * @author tfzzh
	 * @dateTime 2021年4月8日 下午5:28:28
	 * @param <O> 目标数据对象
	 * @param cont 配置文件内容
	 * @param clz 目标数据对象类
	 * @param mapKey 通用属性，这里无效
	 * @return 解析后的内容
	 */
	protected <O extends ObjectInfo> O onlyObject(final String cont, final Class<O> clz, final String mapKey) {
		// this.log.debug("in onlyObject ... ");
		if ((null == cont) || (cont.length() == 0)) {
			return null;
		}
		final JSONObject jo = JSON.parseObject(cont, JSONReader.Feature.AllowUnQuotedFieldNames);
		O bak = null;
		final Class<?>[] clza = new Class[] { JSONObject.class };
		Object[] obja;
		obja = new Object[] { jo };
		try {
			bak = InstanceFactory.classInstance(clz, clza, obja);
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		// if (bak.size() == 0) {
		// this.log.error("no conf onlyObject ... ");
		// }
		return bak;
	}
	// /**
	// * 过段时间删除该方法 2021-12-14<br />
	// * 进行对应数据解析<br />
	// *
	// * @author tfzzh
	// * @dateTime 2020年9月12日 下午3:23:10
	// * @param cont 配置文件内容
	// * @return 解析后的内容
	// */
	// @Deprecated
	// protected Map<Integer, List<String>> strToMilTask(final String cont) {
	// // this.log.debug("in strToMilTask ... ");
	// if ((null == cont) || (cont.length() == 0)) {
	// return new HashMap<>();
	// }
	// final JSONObject jo = JSON.parseObject(cont, JSONReader.Feature.AllowUnQuotedFieldNames);
	// final Map<Integer, List<String>> bak = new HashMap<>();
	// List<String> sl;
	// JSONArray ja;
	// for (final String key : jo.keySet()) {
	// sl = new ArrayList<>();
	// ja = jo.getJSONArray(key);
	// for (int i = 0; i < ja.size(); i++) {
	// sl.add(ja.getString(i));
	// }
	// bak.put(Integer.parseInt(key), sl);
	// }
	// // if (bak.size() == 0) {
	// // this.log.error("no conf ORUN_MILEAGE_TASK ... ");
	// // }
	// return bak;
	// }
	// /**
	// * 过段时间删除该方法 2021-12-14<br />
	// * 进行对应数据解析<br />
	// *
	// * @author tfzzh
	// * @dateTime 2020年9月19日 下午5:49:23
	// * @param cont 配置文件内容
	// * @return 解析后的内容
	// */
	// @Deprecated
	// protected Map<Integer, String> strToFTTask(final String cont) {
	// // this.log.debug("in strToFTTask ... ");
	// if ((null == cont) || (cont.length() == 0)) {
	// return new HashMap<>();
	// }
	// final JSONObject jo = JSON.parseObject(cont, JSONReader.Feature.AllowUnQuotedFieldNames);
	// final Map<Integer, String> bak = new HashMap<>();
	// for (final String key : jo.keySet()) {
	// bak.put(Integer.parseInt(key), jo.getString(key));
	// }
	// // if (bak.size() == 0) {
	// // this.log.error("no conf TASK_FILETYPE_CODE ... ");
	// // }
	// return bak;
	// }
}
