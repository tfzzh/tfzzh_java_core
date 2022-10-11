/**
 * @author TFZZH
 * @dateTime 2011-12-6 下午10:01:31
 */
package com.tfzzh.view.web.bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import com.tfzzh.tools.InstanceFactory;
import com.tfzzh.tools.PropertiesTools;
import com.tfzzh.view.web.annotation.ParamAlias;
import com.tfzzh.view.web.annotation.UploadFileInfo;
import com.tfzzh.view.web.tools.UploadFileNameTypeEnum;

/**
 * 用于有文件上传的请求信息
 * 
 * @author TFZZH
 * @dateTime 2011-12-6 下午10:01:31
 */
public abstract class UploadParamBean extends BaseParamBean {

	/**
	 * @author Xu Weijie
	 * @datetime 2017年9月28日_下午2:05:40
	 */
	private static final long serialVersionUID = 2318380718403625918L;

	/**
	 * 对所上传文件的限制信息<br />
	 * 列表中内容：0，文件最大字节数限制；1，后缀名限制；<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-12 下午2:37:48
	 */
	private static final Map<String, List<String>> FILE_INFO = new HashMap<>();

	/**
	 * 上传文件最大字节数量限制
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-11 上午1:58:10
	 */
	private final int maxSize;

	/**
	 * 上传文件后缀名限制：<br />
	 * 格式：|后缀名|后缀名|后缀名|...<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-12 下午2:43:14
	 */
	private final String suffixLimit;
	/**
	 * 得到文件最大字节数量限制，与后缀名限制<br />
	 * maxSize:最大字节数限制<br />
	 * suffixLimit:后缀名限制<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-11 上午2:21:22
	 */
	{
		// 先判定缓存中数据
		List<String> info = UploadParamBean.FILE_INFO.get(this.getClass().getSimpleName());
		if (null == info) {
			// 不存在，需要创建
			UploadFileInfo ufi;
			int m = 0;
			final StringBuilder sb = new StringBuilder();
			sb.append('|');
			// 遍历对象中属性
			for (final Field f : this.getClass().getDeclaredFields()) {
				if (null != (ufi = f.getAnnotation(UploadFileInfo.class))) {
					// 是目标字段
					if (m < ufi.maxLength()) {
						m = ufi.maxLength();
					}
					sb.append(ufi.suffix()).append('|');
				}
			}
			info = new LinkedList<>();
			UploadParamBean.FILE_INFO.put(this.getClass().getSimpleName(), info);
			this.maxSize = m;
			this.suffixLimit = sb.toString();
			info.add(String.valueOf(this.maxSize));
			info.add(this.suffixLimit);
			// 将数据放入到缓存中
		} else {
			this.maxSize = Integer.parseInt(info.get(0));
			this.suffixLimit = info.get(1);
		}
	}

	/**
	 * 得到文件最大字节数量限制
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-13 上午11:37:53
	 * @return the maxSize
	 */
	public int getMaxSize() {
		return this.maxSize;
	}

	/**
	 * 得到
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-13 上午11:37:53
	 * @return the suffixLimit
	 */
	public String getSuffixLimit() {
		return this.suffixLimit;
	}

	/**
	 * 设置参数，无文件上传操作
	 * 
	 * @author TFZZH
	 * @dateTime 2011-3-7 下午04:02:33
	 * @param paraMap 参数集合，包括文件部分；<br />
	 *           List<String>：正常的页面传值；<br />
	 *           FileItem：上传的文件；<br />
	 * @see com.tfzzh.view.web.bean.BaseParamBean#setParameters(java.util.Map)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void setParameters(final Map<String, Object> paraMap) {
		Class<?> clz = this.getClass();
		do {
			this.setParameters((Class<? extends UploadParamBean>) clz, paraMap);
			clz = clz.getSuperclass();
		} while (UploadParamBean.class.isAssignableFrom(clz.getSuperclass()));
		this.validate();
	}

	/**
	 * 按类层级设置参数
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-11-21 下午7:22:00
	 * @param pClz 类信息
	 * @param paraMap 参数集合，包括文件部分；<br />
	 *           List<String>：正常的页面传值；<br />
	 *           FileItem：上传的文件；<br />
	 */
	@SuppressWarnings("unchecked")
	private void setParameters(final Class<? extends UploadParamBean> pClz, final Map<String, Object> paraMap) {
		final Field[] fields = pClz.getDeclaredFields();
		Class<?> clz;
		String ps;
		Object fo;
		for (final Field field : fields) {
			// 开启字段可写权限
			field.setAccessible(true);
			try {
				clz = field.getType();
				if (clz == String.class) {
					if (null != (ps = this.getParamStringValue(field, paraMap))) {
						field.set(this, ps);
					}
				} else if ((clz == Integer.class) || (clz == int.class)) {
					if (null != (ps = this.getParamStringValue(field, paraMap))) {
						field.set(this, Integer.parseInt(ps));
					}
				} else if ((clz == Short.class) || (clz == short.class)) {
					if (null != (ps = this.getParamStringValue(field, paraMap))) {
						field.set(this, Short.parseShort(ps));
					}
				} else if ((clz == Long.class) || (clz == long.class)) {
					if (null != (ps = this.getParamStringValue(field, paraMap))) {
						field.set(this, Long.parseLong(ps));
					}
				} else if ((clz == Boolean.class) || (clz == boolean.class)) {
					if (null != (ps = this.getParamStringValue(field, paraMap))) {
						field.set(this, Boolean.parseBoolean(ps));
					}
				} else if ((clz == Double.class) || (clz == double.class)) {
					if (null != (ps = this.getParamStringValue(field, paraMap))) {
						field.set(this, Double.parseDouble(ps));
					}
				} else if ((clz == Float.class) || (clz == float.class)) {
					if (null != (ps = this.getParamStringValue(field, paraMap))) {
						field.set(this, Float.parseFloat(ps));
					}
				} else if (clz == UploadFileBean.class) {
					if (null != (fo = this.getParamValue(field, paraMap))) {
						final UploadFileInfo info = field.getAnnotation(UploadFileInfo.class);
						try {
							final Object[] objs = { fo, info.addTimestamp(), PropertiesTools.getConstantsValue(info.folderPath()), info.nameType(), PropertiesTools.getConstantsValue(info.fileName()), PropertiesTools.getConstantsValue(info.suffix()), paraMap };
							final Class<?>[] clzs = { FileItem.class, boolean.class, String.class, UploadFileNameTypeEnum.class, String.class, String.class, Map.class };
							// 创建一个文件对象
							final UploadFileBean bean = InstanceFactory.classInstance(UploadFileBean.class, clzs, objs);
							field.set(this, bean);
						} catch (InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				} else if (clz == UploadByteFileBean.class) {
					final String[] fileInfo = this.getByteFileParamValue(field, paraMap);
					if (null != fileInfo) {
						final UploadFileInfo info = field.getAnnotation(UploadFileInfo.class);
						try {
							final Object[] objs = { fileInfo[0], fileInfo[1], info.addTimestamp(), PropertiesTools.getConstantsValue(info.folderPath()), info.nameType(), PropertiesTools.getConstantsValue(info.fileName()), PropertiesTools.getConstantsValue(info.suffix()), paraMap };
							final Class<?>[] clzs = { String.class, String.class, boolean.class, String.class, UploadFileNameTypeEnum.class, String.class, String.class, Map.class };
							// 创建一个文件对象
							final UploadByteFileBean bean = InstanceFactory.classInstance(UploadByteFileBean.class, clzs, objs);
							field.set(this, bean);
						} catch (InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				} else if (clz == UploadFileBean[].class) {
					// 数组
					if (null != (fo = this.getParamValue(field, paraMap))) {
						final UploadFileInfo info = field.getAnnotation(UploadFileInfo.class);
						if (fo instanceof List) {
							// 是列表
							final List<FileItem> fil = (List<FileItem>) fo;
							final List<UploadFileBean> ufl = new ArrayList<>(fil.size());
							for (final FileItem fi : fil) {
								try {
									final Object[] objs = { fi, info.addTimestamp(), PropertiesTools.getConstantsValue(info.folderPath()), info.nameType(), PropertiesTools.getConstantsValue(info.fileName()), PropertiesTools.getConstantsValue(info.suffix()), paraMap };
									final Class<?>[] clzs = { FileItem.class, boolean.class, String.class, UploadFileNameTypeEnum.class, String.class, String.class, Map.class };
									// 创建一个文件对象
									ufl.add(InstanceFactory.classInstance(UploadFileBean.class, clzs, objs));
								} catch (InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
									e.printStackTrace();
								}
							}
							if (ufl.size() == 0) {
								// 不操作了
								continue;
							} else {
								final UploadFileBean[] beans = ufl.toArray(new UploadFileBean[ufl.size()]);
								field.set(this, beans);
							}
						} else {
							// 是个体
							try {
								final Object[] objs = { fo, info.addTimestamp(), PropertiesTools.getConstantsValue(info.folderPath()), info.nameType(), PropertiesTools.getConstantsValue(info.fileName()), PropertiesTools.getConstantsValue(info.suffix()), paraMap };
								final Class<?>[] clzs = { FileItem.class, boolean.class, String.class, UploadFileNameTypeEnum.class, String.class, String.class, Map.class };
								// 创建一个文件对象
								final UploadFileBean[] beans = new UploadFileBean[] { InstanceFactory.classInstance(UploadFileBean.class, clzs, objs) };
								field.set(this, beans);
							} catch (InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
								e.printStackTrace();
								continue;
							}
						}
					}
				} else if (clz == UploadByteFileBean[].class) {
					// 数组
					final String[] fileInfo = this.getByteFileParamValue(field, paraMap);
					if (null != fileInfo) {
						final UploadFileInfo info = field.getAnnotation(UploadFileInfo.class);
						final List<UploadByteFileBean> ubfl = new ArrayList<>(fileInfo.length / 2);
						for (int i = 0; i < fileInfo.length; i += 2) {
							try {
								final Object[] objs = { fileInfo[i], fileInfo[i + 1], info.addTimestamp(), PropertiesTools.getConstantsValue(info.folderPath()), info.nameType(), PropertiesTools.getConstantsValue(info.fileName()), PropertiesTools.getConstantsValue(info.suffix()), paraMap };
								final Class<?>[] clzs = { String.class, String.class, boolean.class, String.class, UploadFileNameTypeEnum.class, String.class, String.class, Map.class };
								// 创建一个文件对象
								ubfl.add(InstanceFactory.classInstance(UploadByteFileBean.class, clzs, objs));
							} catch (InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
						if (ubfl.size() == 0) {
							continue;
						} else {
							field.set(this, ubfl.toArray(new UploadByteFileBean[ubfl.size()]));
						}
					}
				}
			} catch (final IllegalArgumentException | IllegalAccessException e) {
			}
			// 关闭字段可写权限
			field.setAccessible(false);
		}
	}

	/**
	 * 得到文件字节内容，针对字节文件
	 * 
	 * @author tfzzh
	 * @dateTime 2016年10月14日 下午2:21:07
	 * @param field 目标属性
	 * @param paraMap 参数列表
	 * @return 字节流文件内容，位置0是文件内容，位置1是文件在客户端名；<br />
	 *         null，没有目标属性所对应的值；<br />
	 */
	@SuppressWarnings("unchecked")
	private String[] getByteFileParamValue(final Field field, final Map<String, Object> paraMap) {
		Object pd = paraMap.get(field.getName() + "Data");
		Object pn = null;
		if (null == pd) {
			// 判定是否存在别名
			final ParamAlias pa = field.getAnnotation(ParamAlias.class);
			if (null != pa) {
				for (final String al : pa.value()) {
					if (null != (pd = paraMap.get(al + "Data"))) {
						pn = paraMap.get(al + "Name");
						if (null == pn) {
							return null;
						}
					}
				}
				if (null == pd) {
					return null;
				}
			} else {
				return null;
			}
		} else {
			pn = paraMap.get(field.getName() + "Name");
			if (null == pn) {
				return null;
			}
		}
		String fd;
		String fn;
		final String[] bsa;
		if ((pd instanceof List) && (pn instanceof List)) {
			final List<String> psl = (List<String>) pd;
			final List<String> pnl = (List<String>) pn;
			final int l = Math.min(psl.size(), pnl.size());
			final List<String> ls = new ArrayList<>(l * 2);
			for (int i = 0; i < l; i++) {
				fd = psl.get(i);
				fn = pnl.get(i);
				if ((fd.length() != 0) || (fn.length() != 0)) {
					ls.add(fd);
					ls.add(fn);
				}
			}
			bsa = ls.toArray(new String[ls.size()]);
		} else {
			if (pd instanceof List) {
				fd = ((List<String>) pd).get(0);
			} else {
				fd = pd.toString();
			}
			if (pn instanceof List) {
				fn = ((List<String>) pn).get(0);
			} else {
				fn = pn.toString();
			}
			if ((fd.length() == 0) && (fn.length() == 0)) {
				bsa = null;
			} else {
				bsa = new String[] { fd, fn };
			}
		}
		return bsa;
	}
}
