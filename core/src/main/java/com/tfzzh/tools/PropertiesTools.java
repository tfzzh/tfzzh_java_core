/**
 * @author Xu Weijie
 * @dateTime 2012-6-26 上午10:29:09
 */
package com.tfzzh.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Properties相关的控制操作
 * 
 * @author Xu Weijie
 * @dateTime 2012-6-26 上午10:29:09
 */
public class PropertiesTools {

	/**
	 * 完善字串中存在Constants内容的最终目标值
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-10-9 上午11:32:03
	 * @param value 目标字符串
	 * @return 替换后的最终目标值
	 */
	public static String getConstantsValue(String value) {
		if ((null != value) && ((value = value.trim()).length() > 0)) {
			int si = 0, ei = 0, e = 0;
			final StringBuilder c = new StringBuilder();
			String k;
			String classPath, v;
			Object val;
			int ind;
			while (true) {
				// 得到起点
				if ((si = value.indexOf("${", ei)) == -1) {
					// 不存在更多的跳出
					break;
				} else {
					// 得到末节点
					if ((ei = value.indexOf("}", si)) == -1) {
						// 不存在更多的跳出
						break;
					}
				}
				// 可以进行延续的操作
				c.append(value.substring(e, si));
				k = value.substring(si + 2, ei);
				e = ei + 1;
				ind = k.lastIndexOf(".");
				if (ind <= 0) {
					// 认为是直接使用的属性
					switch (k.toLowerCase()) {
					case "date": // 仅有日期：（yyyyMMdd）
						c.append(DateFormat.getDayShortStr());
						continue;
					case "datef": // 仅有日期，有格式：（yyyy-MM-dd）
						c.append(DateFormat.getDayShortShowStr());
						continue;
					case "datetime": // 时间与时期：（yyyyMMddHHmmss）
						c.append(DateFormat.getLongDate());
						continue;
					case "datetimef": // 时间与时期，有格式：（yyyy-MM-dd HH:mm:ss）
						c.append(DateFormat.getLongDateShow());
						continue;
					case "datetimeFull": // 时间与时期，完整的：（yyyyMMddHHmmssSSS）
						c.append(DateFormat.getFullDate());
						continue;
					case "datetimeFullf": // 时间与时期，完整的，有格式：（yyyy-MM-dd HH:mm:ss SSS）
						c.append(DateFormat.getFullDateShow());
						continue;
					case "time": // 仅是有时间：（HHmmss）
						c.append(DateFormat.getTimeShortStr());
						continue;
					case "timef": // 仅是有时间，有格式：（HH:mm:ss）
						c.append(DateFormat.getTimeShortStrShow());
						continue;
					case "timeFull": // 仅是有时间，完整的：（HHmmssSSS）
						c.append(DateFormat.getTimeLongStr());
						continue;
					case "timeFullf": // 仅是有时间，完整的，有格式：（HH:mm:ss SSS）
						c.append(DateFormat.getTimeLongStrShow());
						continue;
					}
				}
				// 得到最后一个点位置，认为是类名与属性的分界点
				try {
					classPath = k.substring(0, ind);
				} catch (final Exception ex) {
					classPath = "";
					ex.printStackTrace();
				}
				// 得到类信息
				Class<?> clz;
				try {
					clz = Class.forName(classPath);
					v = k.substring(ind + 1, k.length());
					final Field field = clz.getField(v);
					val = field.get(null);
					c.append(val.toString());
				} catch (final IllegalAccessException | ClassNotFoundException | NoSuchFieldException | SecurityException e1) {
					c.append(k);
				}
			}
			// 没有头或尾
			if (e != -1) {
				// 有被转换情况
				c.append(value.substring(e));
			} else {
				// 完全没有转换的情况
				return value;
			}
			return c.toString();
		} else {
			return value;
		}
	}

	/**
	 * 得到Properties文件中的对应值
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-6-26 上午10:33:50
	 * @param value 目标内容
	 * @param pps 目标对象集合
	 * @return 处理过的内容
	 */
	public static String getPropertiesValue(String value, final Map<String, Properties> pps) {
		if (pps.size() == 0) {
			return value;
		} else if ((null != value) && ((value = value.trim()).length() > 0)) {
			int si = 0, ei = 0, e = 0;
			String c = "";
			String k;
			String id, v;
			String val;
			int ind;
			Properties pp;
			while (true) {
				// 得到起点
				if ((si = value.indexOf("${", ei)) == -1) {
					// 不存在更多的跳出
					break;
				} else {
					// 得到末节点
					if ((ei = value.indexOf("}", si)) == -1) {
						// 不存在更多的跳出
						break;
					}
				}
				// 可以进行延续的操作
				c += value.substring(e, si);
				k = value.substring(si + 2, ei);
				e = ei + 1;
				ind = k.indexOf(".");
				try {
					id = k.substring(0, ind);
				} catch (final Exception ex) {
					id = "";
					ex.printStackTrace();
				}
				v = k.substring(ind + 1, k.length());
				if (null != (pp = pps.get(id))) {
					// 存在对应文件对象
					if (null != (val = (String) pp.get(v))) {
						// 存在值
						c += val;
						continue;
					}
				}
				// 不存在有效值
				c += k;
			}
			// 没有头或尾
			if (e != -1) {
				// 有被转换情况
				c += value.substring(e);
			} else {
				// 完全没有转换的情况
				c = value;
			}
			return c;
		} else {
			return value;
		}
	}

	/**
	 * 更新指定Properties文件中指定字段内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月15日 下午4:15:52
	 * @param fileName 文件名
	 * @param datas 数据内容
	 * @return true，更新成功；</br>
	 *         false，更新失败；</br>
	 */
	public static boolean updateProperties(final String fileName, final Map<String, String> datas) {
		// 确认文件是否存在
		final File f = new File(fileName);
		if (!f.exists()) {
			// 因为不存在
			return false;
		}
		// 读取文件内容到pps对象
		final Properties pps = new Properties();
		try {
			final FileInputStream fis = new FileInputStream(f);
			pps.load(fis);
			fis.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		// 更新pps内容
		for (final Entry<String, String> ent : datas.entrySet()) {
			pps.setProperty(ent.getKey(), ent.getValue());
		}
		try {
			// 保存内容到文件
			final FileOutputStream fos = new FileOutputStream(fileName);
			pps.store(fos, new StringBuilder().append("save time ").append(DateFormat.getFullDateShow(new Date())).toString());
			fos.close();
			return true;
		} catch (final IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
