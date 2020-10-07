/**
 * @author Weijie Xu
 * @dateTime 2014-3-21 下午3:11:41
 */
package com.tfzzh.core.control.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.tfzzh.core.control.annotation.ManagerImpl;
import com.tfzzh.core.control.annotation.ManagerIoc;
import com.tfzzh.exception.InitializeException;
import com.tfzzh.tools.ClassTool;
import com.tfzzh.tools.Constants;
import com.tfzzh.tools.InstanceFactory;
import com.tfzzh.tools.PropertiesTools;
import com.tfzzh.tools.StringTools;
import com.tfzzh.tools.XMLAnalyse;

/**
 * 控制层实体对象控制类数据初始化处理工具
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-21 下午3:11:41
 */
public class NewManagerMapTool {

	/**
	 * 文件入口标签
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:18:04
	 */
	protected final String xmlRoot = "controller";

	/**
	 * 对应配置文件
	 * 
	 * @author XuWeijie
	 * @dateTime 2012-6-25 下午1:53:58
	 */
	protected final String xmlPropertyTag = "property";

	/**
	 * 对应配置文件的调取ID
	 * 
	 * @author XuWeijie
	 * @dateTime 2012-6-25 下午1:54:03
	 */
	protected final String xmlPropertyIdAttlist = "id";

	/**
	 * 对应配置文件的文件相对或绝对路径
	 * 
	 * @author XuWeijie
	 * @dateTime 2012-6-25 下午1:54:05
	 */
	protected final String xmlPropertyFileAttlist = "file";

	/**
	 * 控制类相关
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:19:31
	 */
	protected final String xmlManagerListTag = "manager-list";

	/**
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:19:32
	 */
	protected final String xmlManagerTag = "manager";

	/**
	 * 控制类名称
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:19:33
	 */
	protected final String xmlManagerNameAttlist = "name";

	/**
	 * 控制类对应实现路径
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:19:35
	 */
	protected final String xmlManagerClassTag = "manager-class";

	/**
	 * 需要有参数注入的控制列表
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:19:37
	 */
	protected final String xmlManagerPropertyListTag = "manager-property-list";

	/**
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:19:39
	 */
	protected final String xmlManagerPropertyTag = "manager-property";

	/**
	 * 控制类代设置参数名称
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:19:41
	 */
	protected final String xmlManagerPropertyNameAttlist = "name";

	/**
	 * DAO注入
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:19:42
	 */
	protected final String xmlManagerPropertyDaoTag = "manager-property-dao";

	/**
	 * Manager注入
	 * 
	 * @author tfzzh
	 * @dateTime 2010-8-2 上午02:54:34
	 */
	protected final String xmlManagerPropertyManagerTag = "manager-property-manager";

	/**
	 * 字符型注入
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:19:44
	 */
	protected final String xmlManagerPropertyStringTag = "manager-property-string";

	/**
	 * 整型注入
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:19:45
	 */
	protected final String xmlManagerPropertyIntegerTag = "manager-property-integer";

	/**
	 * 短整形注入
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:19:47
	 */
	protected final String xmlManagerPropertyShortTag = "manager-property-short";

	/**
	 * 长整型注入
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:19:48
	 */
	protected final String xmlManagerPropertyLongTag = "manager-property-long";

	/**
	 * 长浮点型注入
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:19:49
	 */
	protected final String xmlManagerPropertyDoubleTag = "manager-property-double";

	/**
	 * 浮点型注入
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:19:51
	 */
	protected final String xmlManagerPropertyFloatTag = "manager-property-float";

	/**
	 * 布尔型注入
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:19:52
	 */
	protected final String xmlManagerPropertyBooleanTag = "manager-property-boolean";

	/**
	 * 接口相关
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:19:54
	 */
	protected final String xmlInterfaceListTag = "interface-list";

	/**
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:20:04
	 */
	protected final String xmlInterfaceTag = "interface";

	/**
	 * 接口名称
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:20:06
	 */
	protected final String xmlInterfaceNameAttlist = "name";

	/**
	 * 对应实现列表中ID
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:20:07
	 */
	protected final String xmlInterfaceImplementAttlist = "implement";

	/**
	 * 配置文件相关数据
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午5:37:07
	 */
	protected final Map<String, Properties> pps = new HashMap<>();

	/**
	 * 控制类属性
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午5:37:07
	 */
	protected final Map<Object, List<String>> paramManager = new HashMap<>();

	/**
	 * 用类路径对应控制类及Dao类对象
	 * 
	 * @author XuWeijie
	 * @datetime 2015年10月26日_下午4:42:53
	 */
	protected final Map<String, Set<Object>> claPathObj = new HashMap<>();

	/**
	 * 运行状态：1，大体数据处理完成；2，属性注入完成；3，数据表结构同步完成；
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月18日_下午12:44:59
	 */
	protected int runStatus = 0;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-21 下午3:40:37
	 */
	public NewManagerMapTool() {
		ManagerMap.getInstance();
	}

	/**
	 * 读取数量XML文件的方法<br />
	 * 请保证文件名称的正确性<br />
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 下午04:57:25
	 * @param basePath 基础路径
	 * @param dtdPath 相关DTD文件路径
	 * @param xmlPath 待分析文件集合，多文件时，使用“,”号隔开
	 */
	public void readerAmountXML(final String basePath, final String dtdPath, final String xmlPath) {
		// 存在设定好的DTD文件路径
		final XMLAnalyse xa = new XMLAnalyse(basePath + dtdPath, this.xmlRoot);
		xa.readXML(basePath, xmlPath.split("[,]"));
		this.analyseXML(xa.getXmlElements());
		this.runStatus = 1;
	}

	/**
	 * 读取数量XML文件的方法<br />
	 * 走本地限定DTD文件<br />
	 * 按照指定前缀的方式读取<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-6 下午6:17:5025
	 * @param basePath 基础路径
	 * @param dtdPath 相关DTD文件路径
	 * @param xmlPrefix xml文件前缀
	 */
	public void readerPrefixAmountXML(final String basePath, final String dtdPath, final String xmlPrefix) {
		final XMLAnalyse xa = new XMLAnalyse(basePath + dtdPath, this.xmlRoot);
		xa.readXML(basePath, xmlPrefix);
		this.analyseXML(xa.getXmlElements());
		this.runStatus = 1;
	}

	/**
	 * 读取数量XML文件的方法<br />
	 * 走网络限定DTD文件<br />
	 * 按照指定前缀的方式读取<br />
	 * 
	 * @author Xu Weijie
	 * @datetime 2018年2月1日_下午2:01:11
	 * @param basePath 基础路径
	 * @param dtdUrl DTD文件网络地址
	 * @param xmlPrefix xml文件前缀
	 */
	public void readerPrefixAmountXML(final String basePath, final URL dtdUrl, final String xmlPrefix) {
		final XMLAnalyse xa = new XMLAnalyse(dtdUrl, this.xmlRoot);
		xa.readXML(basePath, xmlPrefix);
		this.analyseXML(xa.getXmlElements());
		this.runStatus = 1;
	}

	/**
	 * 针对路径解析必要注解内容
	 * 
	 * @author 听风紫竹
	 * @datetime 2015年7月12日_下午11:24:07
	 * @param path 目标路径
	 */
	public void annotationAnalysis(final String path) {
		final Set<Class<?>> clzs = ClassTool.getClasses(path, true);
		ManagerImpl mi;
		for (final Class<?> clz : clzs) {
			if (null != (mi = clz.getAnnotation(ManagerImpl.class))) {
				// 是Manager接口的实现，单一接口实现
				this.analyseAnnotationManager(clz, mi);
			}
		}
		this.runStatus = 1;
	}

	/**
	 * 进行对XML文件的分析
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 下午04:37:51
	 * @param xmlElements xml元素消息
	 */
	@SuppressWarnings("unchecked")
	protected void analyseXML(final Map<String, Object> xmlElements) {
		if (xmlElements.size() == 0) {
			return;
		}
		final Map<String, Object> rootElem = (Map<String, Object>) ((Map<String, Object>) xmlElements.get(this.xmlRoot)).get(XMLAnalyse.XML_TAG_ELEMENT);
		// final Map<String, Properties> pps = new HashMap<String, Properties>();
		{ // 加载配置文件相关
			final List<Map<String, Map<String, String>>> list = (List<Map<String, Map<String, String>>>) rootElem.get(this.xmlPropertyTag);
			if (null != list) {
				for (final Map<String, Map<String, String>> map : list) {
					final String id = map.get(XMLAnalyse.XML_TAG_ATTRIBUTE).get(this.xmlPropertyIdAttlist);
					final String file = map.get(XMLAnalyse.XML_TAG_ATTRIBUTE).get(this.xmlPropertyFileAttlist);
					// final InputStream in = this.getClass().getResourceAsStream(file);
					try {
						File f = new File(Constants.INIT_CONFIG_PATH_BASE + file);
						if (!f.exists()) {
							f = new File(file);
							if (!f.exists()) {
								// 都不存在下一个
								continue;
							}
						}
						final InputStream in = new FileInputStream(f);
						final Properties pp = new Properties();
						pp.load(in);
						this.pps.put(id, pp);
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		{ // 主要的控制类
			final List<Map<String, Map<String, List<Map<String, Object>>>>> list = (List<Map<String, Map<String, List<Map<String, Object>>>>>) rootElem.get(this.xmlManagerListTag);
			if (null != list) {
				List<Map<String, Object>> managerList;
				for (final Map<String, Map<String, List<Map<String, Object>>>> map : list) {
					managerList = map.get(XMLAnalyse.XML_TAG_ELEMENT).get(this.xmlManagerTag);
					// 主要的控制类
					if ((managerList != null) && (managerList.size() != 0)) {
						for (final Map<String, Object> manager : managerList) {
							this.analyseXmlManager(manager);
						}
					}
				}
			}
		}
		{ // 相应的操作接口名对应的控制类
			final List<Map<String, Map<String, List<Map<String, Object>>>>> list = (List<Map<String, Map<String, List<Map<String, Object>>>>>) rootElem.get(this.xmlInterfaceListTag);
			if (null != list) {
				List<Map<String, Object>> interfaceList;
				for (final Map<String, Map<String, List<Map<String, Object>>>> map : list) {
					interfaceList = map.get(XMLAnalyse.XML_TAG_ELEMENT).get(this.xmlInterfaceTag);
					// 相应的操作接口名对应的控制类
					if ((interfaceList != null) && (interfaceList.size() != 0)) {
						for (final Map<String, Object> interFace : interfaceList) {
							// 前得到属性值interface在类中的使用名,后得到对应的控制类的名称
							ManagerMap.getInstance().putInterface(PropertiesTools.getPropertiesValue(((Map<String, String>) interFace.get(XMLAnalyse.XML_TAG_ATTRIBUTE)).get(this.xmlInterfaceNameAttlist), this.pps), PropertiesTools.getPropertiesValue(((Map<String, String>) interFace.get(XMLAnalyse.XML_TAG_ATTRIBUTE)).get(this.xmlInterfaceImplementAttlist), this.pps));
						}
					}
				}
			}
		}
	}

	/**
	 * 解析XML中Manager内容
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午7:06:33
	 * @param manager manager列表
	 */
	@SuppressWarnings("unchecked")
	protected void analyseXmlManager(final Map<String, Object> manager) {
		if (manager.size() == 0) {
			return;
		}
		try {
			// 前得到属性值MANAGE的名称,后得到CLASS文件的路径并生成实体
			final Object managerObject = InstanceFactory.classInstance(PropertiesTools.getPropertiesValue(((Map<String, List<Map<String, String>>>) manager.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlManagerClassTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), this.pps));
			// 得到
			if (((Map<String, Map<String, Object>>) manager.get(XMLAnalyse.XML_TAG_ELEMENT)).containsKey(this.xmlManagerPropertyListTag)) {
				final List<Map<String, Object>> propertyList = ((Map<String, List<Map<String, Map<String, List<Map<String, Object>>>>>>) manager.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlManagerPropertyListTag).get(0).get(XMLAnalyse.XML_TAG_ELEMENT).get(this.xmlManagerPropertyTag);
				if (null != propertyList) {
					for (final Map<String, Object> propertyMap : propertyList) {
						final Map<String, List<Map<String, String>>> propertyType = (Map<String, List<Map<String, String>>>) propertyMap.get(XMLAnalyse.XML_TAG_ELEMENT);
						try {
							String propertyMethod = PropertiesTools.getPropertiesValue(((Map<String, String>) propertyMap.get(XMLAnalyse.XML_TAG_ATTRIBUTE)).get(this.xmlManagerPropertyNameAttlist), this.pps);
							final String propertyMethodParameter = PropertiesTools.getPropertiesValue(propertyType.get(this.xmlManagerPropertyBooleanTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), this.pps);
							// if (propertyType.containsKey(this.xmlManagerPropertyDaoTag)) {
							// // DAO 类的控制
							// propertyMethod = "set" + propertyMethod.substring(0, 1).toUpperCase() + propertyMethod.substring(1);
							// List<String> params;
							// if ((params = this.paramDao.get(managerObject)) == null) {
							// params = new LinkedList<>();
							// this.paramDao.put(managerObject, params);
							// }
							// params.add(propertyMethod + "," + propertyMethodParameter);
							// // final Class[] cla = (this.DAO_MAP.get(propertyMethodParameter)).getClass()
							// // .getInterfaces();
							// // final Method method = managerObject.getClass().getMethod(propertyMethod, cla);
							// // method.invoke(managerObject, this.DAO_MAP.get(propertyMethodParameter));
							// } else
							if (propertyType.containsKey(this.xmlManagerPropertyManagerTag)) {
								// Manager 类的控制
								propertyMethod = "set" + propertyMethod.substring(0, 1).toUpperCase() + propertyMethod.substring(1);
								List<String> params;
								if ((params = this.paramManager.get(managerObject)) == null) {
									params = new LinkedList<>();
									this.paramManager.put(managerObject, params);
								}
								params.add(propertyMethod + "," + propertyMethodParameter);
							} else if (propertyType.containsKey(this.xmlManagerPropertyStringTag)) {
								// String 类的控制
								propertyMethod = "set" + propertyMethod.substring(0, 1).toUpperCase() + propertyMethod.substring(1);
								final Class<?>[] cla = { String.class };
								final Method method = managerObject.getClass().getMethod(propertyMethod, cla);
								method.invoke(managerObject, propertyMethodParameter);
							} else if (propertyType.containsKey(this.xmlManagerPropertyIntegerTag)) {
								// Integer 类的控制
								final int pro = Integer.parseInt(propertyMethodParameter);
								propertyMethod = "set" + propertyMethod.substring(0, 1).toUpperCase() + propertyMethod.substring(1);
								final Class<?>[] cla = { int.class };
								final Method method = managerObject.getClass().getMethod(propertyMethod, cla);
								method.invoke(managerObject, pro);
							} else if (propertyType.containsKey(this.xmlManagerPropertyShortTag)) {
								// Short 类的控制
								final short pro = Short.parseShort(propertyMethodParameter);
								propertyMethod = "set" + propertyMethod.substring(0, 1).toUpperCase() + propertyMethod.substring(1);
								final Class<?>[] cla = { short.class };
								final Method method = managerObject.getClass().getMethod(propertyMethod, cla);
								method.invoke(managerObject, pro);
							} else if (propertyType.containsKey(this.xmlManagerPropertyLongTag)) {
								// Long 类的控制
								final long pro = Long.parseLong(propertyMethodParameter);
								propertyMethod = "set" + propertyMethod.substring(0, 1).toUpperCase() + propertyMethod.substring(1);
								final Class<?>[] cla = { long.class };
								final Method method = managerObject.getClass().getMethod(propertyMethod, cla);
								method.invoke(managerObject, pro);
							} else if (propertyType.containsKey(this.xmlManagerPropertyDoubleTag)) {
								// Double 类的控制
								final double pro = Double.parseDouble(propertyMethodParameter);
								propertyMethod = "set" + propertyMethod.substring(0, 1).toUpperCase() + propertyMethod.substring(1);
								final Class<?>[] cla = { double.class };
								final Method method = managerObject.getClass().getMethod(propertyMethod, cla);
								method.invoke(managerObject, pro);
							} else if (propertyType.containsKey(this.xmlManagerPropertyFloatTag)) {
								// Float 类的控制
								final float pro = Float.parseFloat(propertyMethodParameter);
								propertyMethod = "set" + propertyMethod.substring(0, 1).toUpperCase() + propertyMethod.substring(1);
								final Class<?>[] cla = { float.class };
								final Method method = managerObject.getClass().getMethod(propertyMethod, cla);
								method.invoke(managerObject, pro);
							} else if (propertyType.containsKey(this.xmlManagerPropertyBooleanTag)) {
								// Boolean 类的控制
								final boolean pro = Boolean.parseBoolean(propertyMethodParameter);
								propertyMethod = "set" + propertyMethod.substring(0, 1).toUpperCase() + propertyMethod.substring(1);
								final Class<?>[] cla = { boolean.class };
								final Method method = managerObject.getClass().getMethod(propertyMethod, cla);
								method.invoke(managerObject, pro);
							} else {
								continue;
							}
						} catch (final Exception e) {
							throw new InitializeException(e.getMessage());
						}
					}
				}
			}
			{ // 进行控制类注解的对象注入
				// 得到自己
				Class<?> clz = managerObject.getClass();
				do {
					// 对应有继承的操作 add tfzzh by 2012-12-16
					this.setParam(managerObject, clz);
					clz = clz.getSuperclass();
				} while ((clz != Object.class) && Object.class.isAssignableFrom(clz.getSuperclass()));
			}
			ManagerMap.getInstance().putManager(((Map<String, String>) manager.get(XMLAnalyse.XML_TAG_ATTRIBUTE)).get(this.xmlManagerNameAttlist), managerObject);
			this.putClaPathObj(managerObject);
		} catch (final Exception e) {
			throw new InitializeException(e.getMessage());
		}
	}

	/**
	 * 解析注解
	 * 
	 * @author 听风紫竹
	 * @datetime 2015年7月12日_下午7:27:36
	 * @param clz 待解析的manager类
	 * @param mi 对应的注解
	 */
	protected void analyseAnnotationManager(final Class<?> clz, final ManagerImpl mi) {
		try {
			final Object managerObject = InstanceFactory.classInstance(clz);
			{ // 进行控制类注解类的对象注入
				// 得到自己
				Class<?> tmp = clz;
				do {
					// 对应有继承的操作
					this.setParam(managerObject, tmp);
					tmp = tmp.getSuperclass();
				} while ((tmp != Object.class) && Object.class.isAssignableFrom(tmp.getSuperclass()));
			}
			final String suf;
			// final String fmName;
			String managerName = clz.getSimpleName();
			if (managerName.indexOf("ManagerImpl") != -1) {
				// fmName = StringTools.cutTail(managerName, "ManagerImpl", false);
				managerName = StringTools.cutTail(managerName, "ManagerImpl") + "Manager";
				suf = "Manager";
			} else if (managerName.indexOf("ControlImpl") != -1) {
				// fmName = StringTools.cutTail(managerName, "ControlImpl", false);
				managerName = StringTools.cutTail(managerName, "ControlImpl") + "Control";
				suf = "Control";
			} else if (managerName.indexOf("ServiceImpl") != -1) {
				// fmName = StringTools.cutTail(managerName, "ServiceImpl", false);
				managerName = StringTools.cutTail(managerName, "ServiceImpl") + "Service";
				suf = "Service";
			} else {
				throw new InitializeException("The MVC-Control Name Not Conform To Rules: " + clz.getName());
			}
			// managerName = StringTools.cutTail(clz.getSimpleName(), "ManagerImpl") + "Manager";
			ManagerMap.getInstance().putManager(managerName, managerObject);
			this.putClaPathObj(managerObject);
			// 处理所实现接口列表，暂不管接口自己的继承关系
			StringBuilder fName;
			for (final Class<?> iface : clz.getInterfaces()) {
				fName = new StringBuilder();
				if (mi.value().length() == 0) {
					fName.append(StringTools.cutTail(iface.getSimpleName(), suf));
					// if (fmName.equalsIgnoreCase(fName.toString())) {
					// fName.append(suf);
					// } else {
					// fName.append(fmName).append(suf);
					// }
					fName.append(suf);
				} else {
					fName.append(mi.value());
				}
				ManagerMap.getInstance().putInterface(fName.toString(), managerName);
			}
		} catch (final IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author 听风紫竹
	 * @datetime 2015年7月12日_下午11:20:31
	 */
	public void dispose() {
		if (this.runStatus != 1) {
			throw new InitializeException("Need ready Data First (use: readerAmountXML, readerPrefixAmountXML, annotationAnalysis) ... ");
		}
		this.disposeManager();
		this.runStatus = 2;
	}

	/**
	 * @author 听风紫竹
	 * @datetime 2015年7月12日_下午11:21:20
	 */
	protected void disposeManager() {
		// 处理有加载其他Manager的Manager
		if (null != this.paramManager) {
			Object obj;
			Object mgr;
			Class<?> clz;
			Field field = null;
			String[] eles;
			Class<?>[] cla;
			Method method;
			for (final Entry<Object, List<String>> params : this.paramManager.entrySet()) {
				obj = params.getKey();
				// 得到自己
				clz = obj.getClass();
				// 分析相关加载的内容
				for (final String con : params.getValue()) {
					eles = con.split("[,]");
					mgr = ManagerMap.getInstance().getManagerDirect(eles[1]);
					if (null == mgr) {
						mgr = this.getClaPathObj(eles[2]);
					}
					if (null == mgr) {
						throw new InitializeException("Link Control Field has Error: " + clz.getName() + "-" + field.getName());
					}
					// 认为单一接口继承
					if (eles[0].startsWith("set")) {
						// 是方法
						cla = mgr.getClass().getInterfaces();
						try {
							// 对值的放入
							method = obj.getClass().getMethod(eles[0], cla);
							method.invoke(obj, ManagerMap.getInstance().getManagerDirect(eles[1]));
						} catch (final Exception e) {
							// 可能有名字问题，这个时候继续使用第三部分处理
							throw new InitializeException(e.getMessage());
						}
					} else {
						// 是属性
						// if (null != (mgr = ManagerMap.getInstance().getManagerDirect(eles[1]))) {
						try {
							field = clz.getDeclaredField(eles[0]);
							if (null != field) {
								field.setAccessible(true);
								field.set(obj, mgr);
							}
							continue;
						} catch (final IllegalArgumentException e) {
							throw new InitializeException("Link Control Field set value Error: " + clz.getName() + "-" + field.getName() + " > " + mgr.getClass().getName());
						} catch (final IllegalAccessException e) {
							throw new InitializeException("Link Control Field set value Error: " + clz.getName() + "-" + field.getName() + " > " + mgr.getClass().getName());
						} catch (final NoSuchFieldException e) {
							throw new InitializeException("Link Control Field set value Error: " + clz.getName() + "-" + field.getName() + " > " + mgr.getClass().getName());
						} catch (final SecurityException e) {
							throw new InitializeException("Link Control Field set value Error: " + clz.getName() + "-" + field.getName() + " > " + mgr.getClass().getName());
						} finally {
							if (null != field) {
								field.setAccessible(false);
							}
						}
					}
					// }
				}
			}
		}
	}

	/**
	 * 设置属性
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-12-16 上午1:50:06
	 * @param managerObject 目标数据对象
	 * @param clz 对应类结构，主要作用于父类对象数据
	 */
	protected void setParam(final Object managerObject, final Class<? extends Object> clz) {
		// 得到属性列表
		final Field[] fields = clz.getDeclaredFields();
		ManagerIoc mag;
		// DaoIoc dao;
		// MongoDaoIoc mdao;
		String name;
		List<String> params;
		for (final Field field : fields) {
			// 认为Link层只接受Manager对象的注入
			if (null != (mag = field.getAnnotation(ManagerIoc.class))) {
				// 存在注入
				if ((name = mag.value()).length() == 0) {
					name = field.getName();
				}
				if ((params = this.paramManager.get(managerObject)) == null) {
					params = new LinkedList<>();
					this.paramManager.put(managerObject, params);
				}
				params.add(field.getName() + "," + name + "," + field.getType().getName());
				// } else if (null != (dao = field.getAnnotation(DaoIoc.class))) {
				// // 存在注入
				// if ((name = dao.value()).length() == 0) {
				// name = field.getName();
				// }
				// if ((params = this.paramDao.get(managerObject)) == null) {
				// params = new LinkedList<>();
				// this.paramDao.put(managerObject, params);
				// }
				// params.add(field.getName() + "," + name + "," + field.getType().getName());
				// } else if (null != (mdao = field.getAnnotation(MongoDaoIoc.class))) {
				// // 存在注入
				// if ((name = mdao.value()).length() == 0) {
				// name = field.getName();
				// }
				// if ((params = this.paramDao.get(managerObject)) == null) {
				// params = new LinkedList<>();
				// this.paramDao.put(managerObject, params);
				// }
				// params.add(field.getName() + "," + name + "," + field.getType().getName());
			}
		}
	}

	/**
	 * 放入到Class路径对象池
	 * 
	 * @author XuWeijie
	 * @datetime 2015年10月26日_下午8:18:11
	 * @param obj 待处理对象
	 */
	protected void putClaPathObj(final Object obj) {
		// 得到所相关的接口列表
		Set<Object> tl;
		Class<?> clz = obj.getClass();
		{// 处理自身
			tl = this.claPathObj.get(clz.getName());
			if (null == tl) {
				tl = new LinkedHashSet<>();
				this.claPathObj.put(clz.getName(), tl);
			}
			tl.add(obj);
		}
		// 处理接口
		Class<?>[] ifs;
		do {
			ifs = clz.getInterfaces();
			for (final Class<?> ifc : ifs) {
				tl = this.claPathObj.get(ifc.getName());
				if (null == tl) {
					tl = new LinkedHashSet<>();
					this.claPathObj.put(ifc.getName(), tl);
				}
				tl.add(obj);
			}
		} while (((clz = clz.getSuperclass()) != null) && (clz != Object.class));
	}

	/**
	 * 得到指定路径的对象
	 * 
	 * @author XuWeijie
	 * @datetime 2015年10月26日_下午9:22:29
	 * @param path 目标路径
	 * @return 目标对象
	 */
	public Object getClaPathObj(final String path) {
		final Set<Object> tl = this.claPathObj.get(path);
		if (null == tl) {
			return null;
		}
		if (tl.size() == 1) {
			return tl.iterator().next();
		}
		return null;
	}

	/**
	 * 得到
	 * 
	 * @author tfzzh
	 * @dateTime 2017年2月23日 上午10:02:10
	 * @return the paramManager
	 */
	public Map<Object, List<String>> getParamManager() {
		return this.paramManager;
	}
}
