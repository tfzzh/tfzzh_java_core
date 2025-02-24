/**
 * @author XuWeijie
 * @dateTime Apr 24, 2010 5:56:24 PM
 */
package com.tfzzh.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tfzzh.core.annotation.DontShow;
import com.tfzzh.exception.NestedRuntimeException;

/**
 * 基础BEAN
 * 
 * @author XuWeijie
 * @dateTime Apr 24, 2010 5:56:24 PM
 * @model
 */
public class BaseBean implements Serializable {

	/**
	 * @author tfzzh
	 * @dateTime 2020年7月30日 下午1:24:07
	 */
	private static final long serialVersionUID = 7232821655810501700L;

	/**
	 * @author tfzzh
	 * @dateTime 2020年7月30日 下午1:23:47
	 */
	protected final Logger log = LogManager.getLogger(this.getClass());

	/**
	 * 当前线程，专为打印控制
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-9 下午3:35:19
	 */
	private transient Thread thread = null;

	/**
	 * 防重工具
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-9 下午3:42:54
	 */
	private static final Map<Thread, Map<BaseBean, Object>> THREAD_MAP = new HashMap<>();

	/**
	 * 把类1中与类2中同名的值复制到类2中
	 * 
	 * @author XuWeijie
	 * @dateTime Apr 24, 2010 6:11:35 PM
	 * @param copy 待复制数据的类
	 * @param target 目标复制到的类
	 */
	public static void attributeCopy(final Object copy, final Object target) {
		// 得到类一的所有公开方法
		final Method[] m1 = copy.getClass().getMethods();
		// 得到类二的所有公开方法
		final Method[] m2 = target.getClass().getMethods();
		String mName, lName;
		// 分析得到类一中通过GET,IS取值的方法
		final Map<String, Integer> map1 = new HashMap<>();
		for (int i = 0, n = m1.length; i < n; i++) {
			mName = m1[i].getName();
			if (mName.startsWith("get")) {
				lName = mName.substring(3);
				if (lName.equals("Class")) {
					continue;
				}
			} else if (mName.startsWith("is")) {
				lName = mName.substring(2);
			} else {
				continue;
			}
			// lName：方法名 i:在m1数组(里面包含对象引用所能调用的所有方法)中的索引
			map1.put(lName, i);
		}
		// 分析得到类一中通过SET赋值的方法
		final Map<String, Integer> map2 = new HashMap<>();
		for (int i = 0, n = m2.length; i < n; i++) {
			mName = m2[i].getName();
			if (mName.startsWith("set")) {
				lName = mName.substring(3);
			} else {
				continue;
			}
			map2.put(lName, i);
		}
		{
			// 将类二中需要设置的方法，根据类一中已经存在的取值做对应，并赋值
			Integer v1;
			for (final Entry<String, Integer> ent : map2.entrySet()) {
				if (null != (v1 = map1.get(ent.getKey()))) {
					// v1 = map1.get(k);
					try {
						m2[ent.getValue()].invoke(target, m1[v1].invoke(copy));
					} catch (final IllegalArgumentException e) {
						// e.printStackTrace();
					} catch (final IllegalAccessException e) {
						// e.printStackTrace();
					} catch (final InvocationTargetException e) {
						// e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 将指定结构的Map<String, Object>中值放入到对象中
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月18日 下午5:15:41
	 * @param map Map对象
	 * @param obj 目标数据对象
	 */
	public static void attributeMapToObject(final Map<String, Object> map, final Object obj) {
		// 得到目标对象的所有公开方法
		final Method[] ms = obj.getClass().getMethods();
		String mName, lName;
		Method m;
		// 分析得到类一中通过SET赋值的方法
		final Map<String, Integer> mm = new HashMap<>();
		for (int i = 0, n = ms.length; i < n; i++) {
			m = ms[i];
			mName = m.getName();
			if (mName.startsWith("set")) {
				lName = mName.substring(3);
			} else {
				continue;
			}
			mm.put(lName, i);
		}
		if (map.size() > mm.size()) {
			// 对象Set方法较少
			Object v1;
			for (final Entry<String, Integer> ent : mm.entrySet()) {
				if (null != (v1 = map.get(ent.getKey()))) {
					// v1 = map.get(k);
					try {
						ms[ent.getValue()].invoke(obj, v1);
					} catch (final IllegalArgumentException e) {
						// e.printStackTrace();
					} catch (final IllegalAccessException e) {
						// e.printStackTrace();
					} catch (final InvocationTargetException e) {
						// e.printStackTrace();
					}
				}
			}
		} else {
			// 对象Set方法较多
			Integer v1;
			for (final Entry<String, Object> ent : map.entrySet()) {
				if (null != (v1 = mm.get(ent.getKey()))) {
					try {
						ms[v1].invoke(obj, ent.getValue());
					} catch (final IllegalArgumentException e) {
						// e.printStackTrace();
					} catch (final IllegalAccessException e) {
						// e.printStackTrace();
					} catch (final InvocationTargetException e) {
						// e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 返回字符串消息</br>
	 * 仅适合单线程调试</br>
	 * 
	 * @author XuWeijie
	 * @dateTime Apr 24, 2010 6:11:29 PM (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		boolean initial = false;
		if (null == this.thread) {
			this.thread = Thread.currentThread();
		}
		Map<BaseBean, Object> set = BaseBean.THREAD_MAP.get(this.thread);
		if (null == set) {
			set = new HashMap<>();
			BaseBean.THREAD_MAP.put(this.thread, set);
			initial = true;
		}
		final String str = this.backString(set);
		// 是初始的，可以进行还原操作
		if (initial) {
			// 清除元素
			set.clear();
			BaseBean.THREAD_MAP.remove(this.thread);
		}
		return str;
	}

	/**
	 * 返回原始的对象字串消息
	 * 
	 * @author TFZZH
	 * @dateTime 2011-6-24 上午01:28:28
	 * @return 原始对象字串消息
	 */
	private String toOldString() {
		return super.toString();
	}

	/**
	 * 得到一般格式数据内容
	 * 
	 * @author XuWeijie
	 * @dateTime Apr 24, 2010 6:11:09 PM
	 * @param dispose 处理过的内容
	 * @return 内容的字符串格式
	 */
	private String backString(final Map<BaseBean, Object> dispose) {
		// 检查对象是否已经存在于
		if (!dispose.containsKey(this)) {
			// 加入当前对象
			dispose.put(this, null);
			// 认为是第一次读取该对象数据消息
			final StringBuilder sb = new StringBuilder();
			final Method[] mets = this.getClass().getMethods();
			sb.append("{");
			String mName, lName;
			DontShow dts;
			for (final Method m : mets) {
				mName = m.getName();
				if (mName.startsWith("get")) {
					// 名为GET方法
					lName = mName.substring(3);
					if (lName.equals("Class")) {
						continue;
					} else if (lName.equals("Instance")) {
						continue;
					}
				} else if (mName.startsWith("is")) {
					// 名为IS方法，针对BOOLEAN型数据
					lName = mName.substring(2);
				} else {
					continue;
				}
				dts = m.getAnnotation(DontShow.class);
				if (null != dts) {
					// 是不需要在打印中被调用的方法
					continue;
				}
				if (m.getParameterTypes().length == 0) {
					final Object obj;
					try {
						// 参数长度为0
						obj = m.invoke(this);
					} catch (final IllegalArgumentException e) {
						e.printStackTrace();
						continue;
					} catch (final IllegalAccessException e) {
						e.printStackTrace();
						continue;
					} catch (final InvocationTargetException e) {
						e.printStackTrace();
						continue;
					} catch (final NestedRuntimeException e) {
						// 此处不做任何处理
						continue;
					}
					{ // 在通过反射，顺利得到数据后进行
						if (sb.length() > 1) {
							sb.append(Constants.COMMA);
						}
						sb.append(lName.toLowerCase());
						sb.append("=");
					}
					if (obj instanceof BaseBean) {
						final BaseBean base = (BaseBean) obj;
						if (dispose.containsKey(base)) {
							// 已经存在该对象，该情况可能性应仅出现在程序调试的情况
							// 放入说明该对象已经存在的消息
							sb.append("Existed " + base.toOldString());
						} else {
							// 不存在，则进行消息放入
							sb.append(base);
						}
					} else {
						if (null == obj) {
							sb.append(obj);
						} else {
							final String cn = obj.getClass().getName();
							if ('[' == cn.charAt(0)) {
								switch (cn.charAt(1)) {
								case 'B':
									sb.append(Arrays.toString((byte[]) obj));
									break;
								case 'D':
									sb.append(Arrays.toString((double[]) obj));
									break;
								case 'F':
									sb.append(Arrays.toString((float[]) obj));
									break;
								case 'I':
									sb.append(Arrays.toString((int[]) obj));
									break;
								case 'J':
									sb.append(Arrays.toString((long[]) obj));
									break;
								case 'L':
									sb.append(Arrays.toString((Object[]) obj));
									break;
								case 'S':
									sb.append(Arrays.toString((short[]) obj));
									break;
								case 'Z':
									sb.append(Arrays.toString((boolean[]) obj));
									break;
								}
							} else {
								sb.append(obj);
							}
						}
					}
				}
			}
			sb.append("}");
			return sb.toString();
		} else {
			// 已经存在该对象的数据消息
			return "Existed " + this.toOldString();
		}
	}

	/**
	 * 克隆本对象</br>
	 * 字节型克隆（深度）</br>
	 * 
	 * @author XuWeijie
	 * @dateTime Apr 24, 2010 6:11:15 PM (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		try {
			final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			final ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);
			final ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			final ObjectInputStream in = new ObjectInputStream(byteIn);
			return in.readObject();
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 对string类型变量进行去掉空格的操作
	 * 
	 * @author XuWeijie
	 * @dateTime Apr 24, 2010 6:11:20 PM
	 * @param s 待去空格的字符串
	 * @return 去除前后空格的新字符串
	 */
	protected String trim(final String s) {
		if (s != null) {
			return s.trim();
		} else {
			return null;
		}
	}
}
