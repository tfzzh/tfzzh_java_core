/**
 * @author Weijie Xu
 * @dateTime 2014-3-21 下午3:11:41
 */
package com.tfzzh.model.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import com.tfzzh.core.control.tools.ManagerMap;
import com.tfzzh.core.control.tools.NewManagerMapTool;
import com.tfzzh.exception.InitializeException;
import com.tfzzh.exception.NestedRuntimeException;
import com.tfzzh.exception.UnknowRuntimeException;
import com.tfzzh.model.dao.MongoDAO;
import com.tfzzh.model.dao.annotation.DaoImpl;
import com.tfzzh.model.dao.annotation.DaoIoc;
import com.tfzzh.model.dao.annotation.MongoDaoImpl;
import com.tfzzh.model.dao.annotation.MongoDaoIoc;
import com.tfzzh.model.pools.ConnectionInfoConfig;
import com.tfzzh.model.pools.ConnectionPoolConfig;
import com.tfzzh.model.pools.MongoPoolConfig;
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
public class ModelMapTool extends NewManagerMapTool {

	/**
	 * 数据库连接信息列表
	 * 
	 * @author XuWeijie
	 * @dateTime May 5, 2010 12:37:35 PM
	 */
	protected final String xmlConnectionListTag = "connection-list";

	/**
	 * 数据连接线的控制类路径
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午5:23:12
	 */
	protected final String xmlConnectionListPathAttlist = "path";

	/**
	 * 连接信息
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午1:27:47
	 */
	protected final String xmlConnectionInfoTag = "connection-info";

	/**
	 * 数据库连接名称
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:20:06
	 */
	protected final String xmlConnectionNameAttlist = "name";

	/**
	 * 数据库连接是否使用Unicode
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:18:30
	 */
	protected final String xmlConnectionUseUnicodeAttlist = "useUnicode";

	/**
	 * 数据库连接字符格式
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:18:30
	 */
	protected final String xmlConnectionCharacterEncodingAttlist = "characterEncoding";

	/**
	 * 数据库连接是否只读连接。1，只读；other，非只读
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:18:30
	 */
	protected final String xmlConnectionReadOnlyAttlist = "readOnly";

	/**
	 * 数据库连接驱动类路径
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:18:30
	 */
	protected final String xmlConnectionDriverTag = "connection-driver";

	/**
	 * 数据库连接地址
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:18:30
	 */
	protected final String xmlConnectionUrlTag = "connection-url";

	/**
	 * 数据库连接用户名
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:18:30
	 */
	protected final String xmlConnectionUserTag = "connection-user";

	/**
	 * 数据库连接用户密码
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:18:30
	 */
	protected final String xmlConnectionPasswordTag = "connection-password";

	/**
	 * 数据库连接说明
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-28 上午10:40:59
	 */
	protected final String xmlConnectionDescriptionTag = "connection-description";

	/**
	 * 数据库连接通道超时断开时间，毫秒数
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:18:30
	 */
	protected final String xmlConnectionTimeOutTag = "connection-time-out";

	/**
	 * 创建连接时超时时间，毫秒数
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:18:30
	 */
	protected final String xmlConnectionSocketTimeOutTag = "connection-socket-time-out";

	/**
	 * 数据库连接最小线程数量
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:18:30
	 */
	protected final String xmlConnectionMinCountTag = "connection-min-count";

	/**
	 * 数据库连接最大线程数量
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:18:30
	 */
	protected final String xmlConnectionMaxCountTag = "connection-max-count";

	/**
	 * 连接池信息
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午1:31:25
	 */
	protected final String xmlConnectionPoolsTag = "connection-pools";

	/**
	 * 连接迟控制类路径
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午5:29:42
	 */
	protected final String xmlConnectionPoolsPathAttlist = "path";

	/**
	 * 默认的静态连接池名
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午5:30:15
	 */
	protected final String xmlConnectionPoolsDefaultAttlist = "default";

	/**
	 * 静态连接池，可多个
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午1:34:43
	 */
	protected final String xmlStaticPoolsTag = "static-pool";

	/**
	 * 动态连接池，仅能一个或没有
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午1:34:44
	 */
	protected final String xmlDynamicPoolsTag = "dynamic-pool";

	/**
	 * 连接池名字
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午1:47:12
	 */
	protected final String xmlStaticPoolsNameAttlist = "name";

	/**
	 * 连接池对应的连接信息的名字
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午1:47:13
	 */
	protected final String xmlStaticPoolsInfoNameAttlist = "infoName";

	/**
	 * 动态连接池名字
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午5:32:37
	 */
	protected final String xmlDynamicPoolsNameAttlist = "name";

	/**
	 * 与mongo连接信息列表
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午2:10:00
	 */
	protected final String xmlMongoListTag = "mongo-list";

	/**
	 * mongo连接控制类路径
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午4:36:34
	 */
	protected final String xmlMongoListPathAttlist = "path";

	/**
	 * mongo的默认连接
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午5:17:20
	 */
	protected final String xmlMongoListDefaultAttlist = "default";

	/**
	 * 与mongo连接信息
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午2:10:00
	 */
	protected final String xmlMongoInfoTag = "mongo-info";

	/**
	 * 与mongo连接的名字
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午2:10:00
	 */
	protected final String xmlMongoInfoNameAttlist = "name";

	/**
	 * 与mongo连接字符格式
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午2:10:01
	 */
	protected final String xmlMongoInfoCharacterEncodingAttlist = "characterEncoding";

	/**
	 * 与mongo连接的地址
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午2:10:01
	 */
	protected final String xmlMongoUrlTag = "mongo-url";

	/**
	 * 与mongo连接的目标数据库
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午4:50:24
	 */
	protected final String xmlMongoDBTag = "mongo-db";

	/**
	 * 与mongo连接的用户，可null
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午2:10:02
	 */
	protected final String xmlMongoUserTag = "mongo-user";

	/**
	 * 与mongo连接的密码，如果user为null则同时为null
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午2:10:05
	 */
	protected final String xmlMongoPasswordTag = "mongo-password";

	/**
	 * 与mongo连接的说明
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午2:10:06
	 */
	protected final String xmlMongoDescriptionTag = "mongo-description";

	/**
	 * DAO相关
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:18:26
	 */
	protected final String xmlDAOListTag = "dao-list";

	/**
	 * DAO单独元素
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:18:28
	 */
	protected final String xmlDAOTag = "dao";

	/**
	 * DAO名称
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:18:29
	 */
	protected final String xmlDAONameAttlist = "name";

	/**
	 * DAO的实际相关类
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:18:30
	 */
	protected final String xmlDAOClassTag = "dao-class";

	/**
	 * DAO相关的连接池名称
	 * 
	 * @author XuWeijie
	 * @dateTime May 5, 2010 1:27:59 PM
	 */
	protected final String xmlDAOConnectionTag = "dao-connection";

	/**
	 * DAO相关的连接池设置参数名称
	 * 
	 * @author XuWeijie
	 * @dateTime May 5, 2010 1:38:14 PM
	 */
	protected final String xmlDAOConnectionNameAttlist = "name";

	/**
	 * DAO连接类型，有unique（唯一/系统）；changeable（可变/用户）两种模式
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-28 上午10:18:17
	 */
	protected final String xmlDAOConnectionTypeAttlist = "type";

	/**
	 * dao属性
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午5:37:06
	 */
	protected final Map<Object, List<String>> paramDao = new HashMap<>();

	/**
	 * 使用的连接池
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午5:37:05
	 */
	private ConnectionPoolConfig poolC = null;

	/**
	 * 默认的连接池
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午5:37:05
	 */
	private Object defaultConPool = null;

	/**
	 * 使用的mongo连接
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午5:07:30
	 */
	private MongoPoolConfig mongoC = null;

	/**
	 * 默认的mongo连接
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午5:07:31
	 */
	private Object defaultMongoPool = null;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-21 下午3:40:37
	 */
	public ModelMapTool() {
		super();
		// ManagerMap.getInstance();
	}

	/**
	 * 针对路径解析必要注解内容
	 * 
	 * @author 听风紫竹
	 * @datetime 2015年7月12日_下午11:24:07
	 * @param path 目标路径
	 */
	@Override
	public void annotationAnalysis(final String path) {
		final Set<Class<?>> clzs = ClassTool.getClasses(path, true);
		ManagerImpl mi;
		DaoImpl di;
		MongoDaoImpl mdi;
		for (final Class<?> clz : clzs) {
			if (null != (mi = clz.getAnnotation(ManagerImpl.class))) {
				// 是Manager接口的实现，单一接口实现
				super.analyseAnnotationManager(clz, mi);
			} else if (null != (di = clz.getAnnotation(DaoImpl.class))) {
				// 是DAO
				this.analyseAnnotationDao(clz, di);
			} else if (null != (mdi = clz.getAnnotation(MongoDaoImpl.class))) {
				// 是Mongo的DAO
				this.analyseAnnotationMongo(clz, mdi);
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
	@Override
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
						final InputStream in = new FileInputStream(new File(Constants.INIT_CONFIG_PATH_BASE + file));
						final Properties pp = new Properties();
						pp.load(in);
						this.pps.put(id, pp);
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		String path = null;
		ConnectionInfoConfig infoC = null;
		{ // 连接控制
			final List<Map<String, Map<String, Object>>> list = (List<Map<String, Map<String, Object>>>) rootElem.get(this.xmlConnectionListTag);
			if (null != list) {
				List<Map<String, Object>> connectionList;
				for (final Map<String, Map<String, Object>> map : list) {
					final String tmpPath = (String) (map.get(XMLAnalyse.XML_TAG_ATTRIBUTE).get(this.xmlConnectionListPathAttlist));
					if (!tmpPath.equals(path)) {
						path = tmpPath;
						try {
							infoC = (ConnectionInfoConfig) InstanceFactory.classInstance(path);
						} catch (final InstantiationException e) {
							throw new UnknowRuntimeException(e);
						} catch (final IllegalAccessException e) {
							throw new UnknowRuntimeException(e);
						} catch (final ClassNotFoundException e) {
							throw new UnknowRuntimeException(e);
						}
					}
					connectionList = (List<Map<String, Object>>) map.get(XMLAnalyse.XML_TAG_ELEMENT).get(this.xmlConnectionInfoTag);
					// 连接控制
					if ((connectionList != null) && (connectionList.size() > 0)) {
						// 连接池名称，驱动名，连接，用户名，密码，连接字符编码
						String name, driver, url, user, pass, description, characterEncoding;
						// 是否Unicode格式，是否只读连接
						boolean useUnicode, readOnly;
						// 空闲最大时间，创建连接时最大时间
						long timeOut, socketTimeOut;
						// 允许存在的最小连接数/最大连接数
						int min, max;
						for (final Map<String, Object> connection : connectionList) {
							name = PropertiesTools.getPropertiesValue(((Map<String, String>) connection.get(XMLAnalyse.XML_TAG_ATTRIBUTE)).get(this.xmlConnectionNameAttlist), this.pps);
							driver = PropertiesTools.getPropertiesValue(((Map<String, List<Map<String, String>>>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlConnectionDriverTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), this.pps);
							url = PropertiesTools.getPropertiesValue(((Map<String, List<Map<String, String>>>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlConnectionUrlTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), this.pps);
							user = PropertiesTools.getPropertiesValue(((Map<String, List<Map<String, String>>>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlConnectionUserTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), this.pps);
							pass = PropertiesTools.getPropertiesValue(((Map<String, List<Map<String, String>>>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlConnectionPasswordTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), this.pps);
							if (null != ((Map<String, List<Map<String, String>>>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlConnectionDescriptionTag)) {
								description = PropertiesTools.getPropertiesValue(((Map<String, List<Map<String, String>>>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlConnectionDescriptionTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), this.pps);
							} else {
								description = url + "(" + user + ")>" + driver;
							}
							characterEncoding = PropertiesTools.getPropertiesValue(((Map<String, String>) connection.get(XMLAnalyse.XML_TAG_ATTRIBUTE)).get(this.xmlConnectionCharacterEncodingAttlist), this.pps);
							useUnicode = Boolean.parseBoolean(PropertiesTools.getPropertiesValue(((Map<String, String>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlConnectionUseUnicodeAttlist), this.pps));
							readOnly = Boolean.parseBoolean(PropertiesTools.getPropertiesValue(((Map<String, String>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlConnectionReadOnlyAttlist), this.pps));
							timeOut = Long.parseLong(PropertiesTools.getPropertiesValue(((Map<String, List<Map<String, String>>>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlConnectionTimeOutTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), this.pps));
							socketTimeOut = Long.parseLong(PropertiesTools.getPropertiesValue(((Map<String, List<Map<String, String>>>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlConnectionSocketTimeOutTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), this.pps));
							min = Integer.parseInt(PropertiesTools.getPropertiesValue(((Map<String, List<Map<String, String>>>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlConnectionMinCountTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), this.pps));
							max = Integer.parseInt(PropertiesTools.getPropertiesValue(((Map<String, List<Map<String, String>>>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlConnectionMaxCountTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), this.pps));
							// 进行连接池消息放入
							infoC.putConnectionInfo(name, driver, url, user, pass, description, useUnicode, characterEncoding, readOnly, socketTimeOut, min, max, "socketTimeOut", String.valueOf(socketTimeOut), "timeOut", String.valueOf(timeOut));
						}
					}
				}
			}
		}
		{ // 连接池数据库
			final List<Map<String, Map<String, Object>>> list = (List<Map<String, Map<String, Object>>>) rootElem.get(this.xmlConnectionPoolsTag);
			if (null != list) {
				List<Map<String, Object>> connectionPoolList;
				for (final Map<String, Map<String, Object>> map : list) {
					if (null == this.poolC) {
						final String tmpPath = (String) (map.get(XMLAnalyse.XML_TAG_ATTRIBUTE).get(this.xmlConnectionPoolsPathAttlist));
						if (!tmpPath.equals(path)) {
							path = tmpPath;
							try {
								this.poolC = (ConnectionPoolConfig) InstanceFactory.classInstance(path);
							} catch (final InstantiationException e) {
								throw new UnknowRuntimeException(e);
							} catch (final IllegalAccessException e) {
								throw new UnknowRuntimeException(e);
							} catch (final ClassNotFoundException e) {
								throw new UnknowRuntimeException(e);
							}
						} else {
							this.poolC = (ConnectionPoolConfig) infoC;
						}
					}
					final String defName = (String) (map.get(XMLAnalyse.XML_TAG_ATTRIBUTE).get(this.xmlConnectionPoolsDefaultAttlist));
					connectionPoolList = (List<Map<String, Object>>) map.get(XMLAnalyse.XML_TAG_ELEMENT).get(this.xmlStaticPoolsTag);
					// 连接控制
					if ((connectionPoolList != null) && (connectionPoolList.size() > 0)) {
						// 连接池名称，相关连接信息名称
						String name, infoName;
						for (final Map<String, Object> connection : connectionPoolList) {
							name = PropertiesTools.getConstantsValue(((Map<String, String>) connection.get(XMLAnalyse.XML_TAG_ATTRIBUTE)).get(this.xmlStaticPoolsNameAttlist));
							infoName = PropertiesTools.getConstantsValue(((Map<String, String>) connection.get(XMLAnalyse.XML_TAG_ATTRIBUTE)).get(this.xmlStaticPoolsInfoNameAttlist));
							// 进行连接池消息放入
							this.poolC.putUniqueConnectionInfo(name, infoName);
							if (name.equals(defName)) {
								this.defaultConPool = this.poolC.getConnectionPool(name);
							}
						}
					}
					connectionPoolList = (List<Map<String, Object>>) map.get(XMLAnalyse.XML_TAG_ELEMENT).get(this.xmlDynamicPoolsTag);
					// 连接控制
					if ((connectionPoolList != null) && (connectionPoolList.size() > 0)) {
						// 连接池名称，相关连接信息名称
						String name;
						for (final Map<String, Object> connection : connectionPoolList) {
							name = PropertiesTools.getPropertiesValue(((Map<String, String>) connection.get(XMLAnalyse.XML_TAG_ATTRIBUTE)).get(this.xmlDynamicPoolsNameAttlist), this.pps);
							// 进行连接池消息放入
							this.poolC.setChangeablePoolName(name);
							if (name.equals(defName)) {
								this.defaultConPool = this.poolC.getChangeableConnection();
							}
						}
					}
				}
			}
		}
		MongoPoolConfig mongoC = null;
		{ // Mongo连接
			final List<Map<String, Map<String, Object>>> list = (List<Map<String, Map<String, Object>>>) rootElem.get(this.xmlMongoListTag);
			if (null != list) {
				List<Map<String, Object>> mongoList;
				String defName;
				for (final Map<String, Map<String, Object>> map : list) {
					final String tmpPath = (String) (map.get(XMLAnalyse.XML_TAG_ATTRIBUTE).get(this.xmlMongoListPathAttlist));
					defName = (String) (map.get(XMLAnalyse.XML_TAG_ATTRIBUTE).get(this.xmlMongoListDefaultAttlist));
					if (!tmpPath.equals(path)) {
						path = tmpPath;
						try {
							mongoC = (MongoPoolConfig) InstanceFactory.classInstance(path);
						} catch (final InstantiationException e) {
							throw new UnknowRuntimeException(e);
						} catch (final IllegalAccessException e) {
							throw new UnknowRuntimeException(e);
						} catch (final ClassNotFoundException e) {
							throw new UnknowRuntimeException(e);
						}
					}
					mongoList = (List<Map<String, Object>>) map.get(XMLAnalyse.XML_TAG_ELEMENT).get(this.xmlMongoInfoTag);
					// 连接控制
					if ((mongoList != null) && (mongoList.size() > 0)) {
						// 连接池名称，驱动名，连接，用户名，密码，连接字符编码
						String name, url, dbName, user, pass;
						for (final Map<String, Object> connection : mongoList) {
							name = PropertiesTools.getPropertiesValue(((Map<String, String>) connection.get(XMLAnalyse.XML_TAG_ATTRIBUTE)).get(this.xmlMongoInfoNameAttlist), this.pps);
							url = PropertiesTools.getPropertiesValue(((Map<String, List<Map<String, String>>>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlMongoUrlTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), this.pps);
							dbName = PropertiesTools.getPropertiesValue(((Map<String, List<Map<String, String>>>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlMongoDBTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), this.pps);
							if (null != ((Map<String, List<Map<String, String>>>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlMongoUserTag)) {
								user = PropertiesTools.getPropertiesValue(((Map<String, List<Map<String, String>>>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlMongoUserTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), this.pps);
								if (null != ((Map<String, List<Map<String, String>>>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlMongoPasswordTag)) {
									pass = PropertiesTools.getPropertiesValue(((Map<String, List<Map<String, String>>>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlMongoPasswordTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), this.pps);
								} else {
									pass = null;
								}
							} else {
								user = null;
								pass = null;
							}
							if (null != ((Map<String, List<Map<String, String>>>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlMongoDescriptionTag)) {
								PropertiesTools.getPropertiesValue(((Map<String, List<Map<String, String>>>) connection.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlMongoDescriptionTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), this.pps);
							} else {
							}
							PropertiesTools.getPropertiesValue(((Map<String, String>) connection.get(XMLAnalyse.XML_TAG_ATTRIBUTE)).get(this.xmlMongoInfoCharacterEncodingAttlist), this.pps);
							// 进行连接池消息放入
							mongoC.putMongoConnectionInfo(name, url, dbName, user, pass);
							if ((null != defName) && defName.equalsIgnoreCase(name)) {
								this.defaultMongoPool = mongoC.getPool(name);
							}
						}
					}
				}
				this.mongoC = mongoC;
			}
		}
		{ // DAO类
			final List<Map<String, Map<String, List<Map<String, Object>>>>> list = (List<Map<String, Map<String, List<Map<String, Object>>>>>) rootElem.get(this.xmlDAOListTag);
			if (null != list) {
				List<Map<String, Object>> daoList;
				for (final Map<String, Map<String, List<Map<String, Object>>>> map : list) {
					daoList = map.get(XMLAnalyse.XML_TAG_ELEMENT).get(this.xmlDAOTag);
					// DAO类
					if ((daoList != null) && (daoList.size() != 0)) {
						for (final Map<String, Object> dao : daoList) {
							this.analyseXmlDao(dao);
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
	@Override
	@SuppressWarnings("unchecked")
	protected void analyseXmlManager(final Map<String, Object> manager) {
		if (manager.size() == 0) {
			return;
		}
		try {
			// 前得到属性值MANAGE的名称,后得到CLASS文件的路径并生成实体
			final Object managerObject = InstanceFactory.classInstance(PropertiesTools.getPropertiesValue(((Map<String, List<Map<String, String>>>) manager.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlManagerClassTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), super.pps));
			// 得到
			if (((Map<String, Map<String, Object>>) manager.get(XMLAnalyse.XML_TAG_ELEMENT)).containsKey(this.xmlManagerPropertyListTag)) {
				final List<Map<String, Object>> propertyList = ((Map<String, List<Map<String, Map<String, List<Map<String, Object>>>>>>) manager.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlManagerPropertyListTag).get(0).get(XMLAnalyse.XML_TAG_ELEMENT).get(super.xmlManagerPropertyTag);
				if (null != propertyList) {
					for (final Map<String, Object> propertyMap : propertyList) {
						final Map<String, List<Map<String, String>>> propertyType = (Map<String, List<Map<String, String>>>) propertyMap.get(XMLAnalyse.XML_TAG_ELEMENT);
						try {
							String propertyMethod = PropertiesTools.getPropertiesValue(((Map<String, String>) propertyMap.get(XMLAnalyse.XML_TAG_ATTRIBUTE)).get(this.xmlManagerPropertyNameAttlist), this.pps);
							final String propertyMethodParameter = PropertiesTools.getPropertiesValue(propertyType.get(this.xmlManagerPropertyBooleanTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), this.pps);
							if (propertyType.containsKey(super.xmlManagerPropertyDaoTag)) {
								// DAO 类的控制
								propertyMethod = "set" + propertyMethod.substring(0, 1).toUpperCase() + propertyMethod.substring(1);
								List<String> params;
								if ((params = this.paramDao.get(managerObject)) == null) {
									params = new LinkedList<>();
									this.paramDao.put(managerObject, params);
								}
								params.add(propertyMethod + "," + propertyMethodParameter);
								// final Class[] cla = (this.DAO_MAP.get(propertyMethodParameter)).getClass()
								// .getInterfaces();
								// final Method method = managerObject.getClass().getMethod(propertyMethod, cla);
								// method.invoke(managerObject, this.DAO_MAP.get(propertyMethodParameter));
							} else if (propertyType.containsKey(super.xmlManagerPropertyManagerTag)) {
								// Manager 类的控制
								propertyMethod = "set" + propertyMethod.substring(0, 1).toUpperCase() + propertyMethod.substring(1);
								List<String> params;
								if ((params = super.paramManager.get(managerObject)) == null) {
									params = new LinkedList<>();
									super.paramManager.put(managerObject, params);
								}
								params.add(propertyMethod + "," + propertyMethodParameter);
							} else if (propertyType.containsKey(super.xmlManagerPropertyStringTag)) {
								// String 类的控制
								propertyMethod = "set" + propertyMethod.substring(0, 1).toUpperCase() + propertyMethod.substring(1);
								final Class<?>[] cla = { String.class };
								final Method method = managerObject.getClass().getMethod(propertyMethod, cla);
								method.invoke(managerObject, propertyMethodParameter);
							} else if (propertyType.containsKey(super.xmlManagerPropertyIntegerTag)) {
								// Integer 类的控制
								final int pro = Integer.parseInt(propertyMethodParameter);
								propertyMethod = "set" + propertyMethod.substring(0, 1).toUpperCase() + propertyMethod.substring(1);
								final Class<?>[] cla = { int.class };
								final Method method = managerObject.getClass().getMethod(propertyMethod, cla);
								method.invoke(managerObject, pro);
							} else if (propertyType.containsKey(super.xmlManagerPropertyShortTag)) {
								// Short 类的控制
								final short pro = Short.parseShort(propertyMethodParameter);
								propertyMethod = "set" + propertyMethod.substring(0, 1).toUpperCase() + propertyMethod.substring(1);
								final Class<?>[] cla = { short.class };
								final Method method = managerObject.getClass().getMethod(propertyMethod, cla);
								method.invoke(managerObject, pro);
							} else if (propertyType.containsKey(super.xmlManagerPropertyLongTag)) {
								// Long 类的控制
								final long pro = Long.parseLong(propertyMethodParameter);
								propertyMethod = "set" + propertyMethod.substring(0, 1).toUpperCase() + propertyMethod.substring(1);
								final Class<?>[] cla = { long.class };
								final Method method = managerObject.getClass().getMethod(propertyMethod, cla);
								method.invoke(managerObject, pro);
							} else if (propertyType.containsKey(super.xmlManagerPropertyDoubleTag)) {
								// Double 类的控制
								final double pro = Double.parseDouble(propertyMethodParameter);
								propertyMethod = "set" + propertyMethod.substring(0, 1).toUpperCase() + propertyMethod.substring(1);
								final Class<?>[] cla = { double.class };
								final Method method = managerObject.getClass().getMethod(propertyMethod, cla);
								method.invoke(managerObject, pro);
							} else if (propertyType.containsKey(super.xmlManagerPropertyFloatTag)) {
								// Float 类的控制
								final float pro = Float.parseFloat(propertyMethodParameter);
								propertyMethod = "set" + propertyMethod.substring(0, 1).toUpperCase() + propertyMethod.substring(1);
								final Class<?>[] cla = { float.class };
								final Method method = managerObject.getClass().getMethod(propertyMethod, cla);
								method.invoke(managerObject, pro);
							} else if (propertyType.containsKey(super.xmlManagerPropertyBooleanTag)) {
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
	 * 解析XML中DAO内容
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午6:47:30
	 * @param dao dao列表
	 */
	@SuppressWarnings("unchecked")
	protected void analyseXmlDao(final Map<String, Object> dao) {
		try {
			final Object daoObject = InstanceFactory.classInstance(PropertiesTools.getPropertiesValue(((Map<String, List<Map<String, String>>>) dao.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlDAOClassTag).get(0).get(XMLAnalyse.XML_TAG_CONTENT), this.pps));
			// 前得到属性值DAO的名称,后得到CLASS文件的路径并生成实体
			ManagerMap.getInstance().putDao(PropertiesTools.getPropertiesValue(((Map<String, String>) dao.get(XMLAnalyse.XML_TAG_ATTRIBUTE)).get(this.xmlDAONameAttlist), this.pps), daoObject);
			this.putClaPathObj(daoObject);
			final boolean isMongo = daoObject instanceof MongoDAO;
			{
				final Map<String, Object> propertyMap = ((Map<String, List<Map<String, Object>>>) dao.get(XMLAnalyse.XML_TAG_ELEMENT)).get(this.xmlDAOConnectionTag).get(0);
				final Object pc;
				String propertyMethod;
				if (null != propertyMap) {
					// 数据连接的控制
					propertyMethod = PropertiesTools.getPropertiesValue(((Map<String, String>) propertyMap.get(XMLAnalyse.XML_TAG_ATTRIBUTE)).get(this.xmlDAOConnectionNameAttlist), this.pps);
					final String propertyType = PropertiesTools.getPropertiesValue(((Map<String, String>) propertyMap.get(XMLAnalyse.XML_TAG_ATTRIBUTE)).get(this.xmlDAOConnectionTypeAttlist), this.pps);
					final boolean isUnique;
					if ((null == propertyType) || !propertyType.equalsIgnoreCase("changeable")) {
						isUnique = true;
					} else {
						isUnique = false;
					}
					final String propertyMethodParameter = (String) propertyMap.get(XMLAnalyse.XML_TAG_CONTENT);
					propertyMethod = "set" + propertyMethod.substring(0, 1).toUpperCase() + propertyMethod.substring(1);
					if (isMongo) {
						pc = this.mongoC.getPool(propertyMethodParameter);
					} else {
						if (isUnique) {
							pc = this.poolC.getConnectionPool(propertyMethodParameter);
						} else {
							pc = this.poolC.getChangeableConnection();
						}
					}
				} else {
					if (isMongo) {
						propertyMethod = "setMongoPool";
						pc = this.defaultMongoPool;
					} else {
						propertyMethod = "setConnectionPool";
						pc = this.defaultConPool;
					}
				}
				Class<?>[] cla = new Class[] { pc.getClass() };
				Method method;
				while (true) {
					try {
						method = daoObject.getClass().getMethod(propertyMethod, cla);
						// 成功而跳出
						break;
					} catch (final NoSuchMethodException e) {
					} catch (final SecurityException e) {
					}
					cla = new Class[] { cla[0].getSuperclass() };
				}
				try {
					// 对指定类的指定方法注入相应的对象消息
					method.invoke(daoObject, pc);
				} catch (final IllegalAccessException e) {
					throw new InitializeException(e.getMessage());
				} catch (final IllegalArgumentException e) {
					throw new InitializeException(e.getMessage());
				} catch (final InvocationTargetException e) {
					throw new InitializeException(e.getMessage());
				}
			}
		} catch (final InstantiationException e) {
			throw new InitializeException(e.getMessage());
		} catch (final IllegalAccessException e) {
			throw new InitializeException(e.getMessage());
		} catch (final ClassNotFoundException e) {
			throw new InitializeException(e.getMessage());
		}
	}

	/**
	 * 解析注解相关DAO内容
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午7:13:46
	 * @param clz 待解析的dao类
	 * @param di 对应的注解
	 */
	protected void analyseAnnotationDao(final Class<?> clz, final DaoImpl di) {
		Object daoObject;
		try {
			daoObject = InstanceFactory.classInstance(clz);
		} catch (final InstantiationException e) {
			e.printStackTrace();
			throw new UnknowRuntimeException(e);
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
			throw new UnknowRuntimeException(e);
		}
		// 前得到属性DAO的名称，后得到CLASS文件的路径并生成实体
		final String daoName;
		if (di.name().length() > 0) {
			daoName = di.name();
		} else {
			daoName = StringTools.cutTail(clz.getSimpleName(), "DAOImpl") + "Dao";
		}
		ManagerMap.getInstance().putDao(daoName, daoObject);
		this.putClaPathObj(daoObject);
		Object pc;
		if (di.unique()) {
			// 唯一的
			try {
				pc = this.poolC.getConnectionPool(di.connName());
				if (null == pc) {
					pc = this.defaultConPool;
				}
			} catch (final NestedRuntimeException e) {
				pc = this.defaultConPool;
			}
		} else {
			// 可变的
			pc = this.poolC.getChangeableConnection();
		}
		Class<?>[] cla = new Class[] { pc.getClass() };
		Method method;
		while (true) {
			// 针对父类适应
			try {
				method = clz.getMethod(di.connMethod(), cla);
				// 因为成功而跳出
				break;
			} catch (final NoSuchMethodException e) {
			} catch (final SecurityException e) {
			}
			cla = new Class[] { cla[0].getSuperclass() };
		}
		try {
			method.invoke(daoObject, pc);
		} catch (final IllegalAccessException e) {
			throw new InitializeException(e.getMessage());
		} catch (final IllegalArgumentException e) {
			throw new InitializeException(e.getMessage());
		} catch (final InvocationTargetException e) {
			throw new InitializeException(e.getMessage());
		}
	}

	/**
	 * 解析注解相关MONGO-DAO内容
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午7:13:46
	 * @param clz 待解析的mongo-dao类
	 * @param di 对应的注解
	 */
	protected void analyseAnnotationMongo(final Class<?> clz, final MongoDaoImpl di) {
		Object daoObject;
		try {
			daoObject = InstanceFactory.classInstance(clz);
		} catch (final InstantiationException e) {
			e.printStackTrace();
			throw new UnknowRuntimeException(e);
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
			throw new UnknowRuntimeException(e);
		}
		// 前得到属性DAO的名称，后得到CLASS文件的路径并生成实体
		final String daoName;
		if (di.name().length() > 0) {
			daoName = di.name();
		} else {
			daoName = StringTools.cutTail(clz.getSimpleName(), "DAOImpl") + "Dao";
		}
		ManagerMap.getInstance().putDao(daoName, daoObject);
		this.putClaPathObj(daoObject);
		Object pc = this.mongoC.getPool(di.connName());
		if (null == pc) {
			pc = this.defaultMongoPool;
		}
		Class<?>[] cla = new Class[] { pc.getClass() };
		Method method;
		while (true) {
			// 针对父类适应
			try {
				method = clz.getMethod(di.connMethod(), cla);
				// 因为成功而跳出
				break;
			} catch (final NoSuchMethodException e) {
				e.printStackTrace();
			} catch (final SecurityException e) {
				e.printStackTrace();
			}
			cla = new Class[] { cla[0].getSuperclass() };
		}
		try {
			method.invoke(daoObject, pc);
		} catch (final IllegalAccessException e) {
			throw new InitializeException(e.getMessage());
		} catch (final IllegalArgumentException e) {
			throw new InitializeException(e.getMessage());
		} catch (final InvocationTargetException e) {
			throw new InitializeException(e.getMessage());
		}
	}

	/**
	 * @author 听风紫竹
	 * @datetime 2015年7月12日_下午11:20:31
	 */
	@Override
	public void dispose() {
		if (this.runStatus != 1) {
			throw new InitializeException("Need ready Data First (use: readerAmountXML, readerPrefixAmountXML, annotationAnalysis) ... ");
		}
		this.disposeDao();
		this.disposeManager();
		this.runStatus = 2;
	}

	/**
	 * @author 听风紫竹
	 * @datetime 2015年7月12日_下午11:20:30
	 */
	protected void disposeDao() {
		// 处理有加载其他DAO的Manager
		if (null != this.paramDao) {
			Object obj;
			Object dr;
			Class<?> clz;
			Field field = null;
			String[] eles;
			Class<?>[] cla;
			Method method;
			for (final Entry<Object, List<String>> params : this.paramDao.entrySet()) {
				obj = params.getKey();
				// 得到自己
				clz = obj.getClass();
				// 分析相关加载的内容
				for (final String con : params.getValue()) {
					eles = con.split("[,]");
					dr = ManagerMap.getInstance().getDao(eles[1]);
					if (null == null) {
						dr = this.getClaPathObj(eles[2]);
					}
					if (null == dr) {
						throw new InitializeException("Link Control Field has Error: " + clz.getName() + "-" + eles[1] + ":" + eles[2]);
					}
					// 认为单一接口继承
					if (eles[0].startsWith("set")) {
						// 是方法
						cla = dr.getClass().getInterfaces();
						try {
							// 对值的放入
							method = obj.getClass().getMethod(eles[0], cla);
							method.invoke(obj, ManagerMap.getInstance().getDao(eles[1]));
						} catch (final Exception e) {
							throw new InitializeException(e.getMessage());
						}
					} else {
						// 是属性
						try {
							while (true) {
								try {
									field = clz.getDeclaredField(eles[0]);
									break;
								} catch (final NoSuchFieldException e) {
									// 对应有继承的操作 add tfzzh by 2012-12-16
									clz = clz.getSuperclass();
								}
							}
							if (null != field) {
								field.setAccessible(true);
								field.set(obj, dr);
							}
							continue;
						} catch (final IllegalArgumentException e) {
							throw new InitializeException("Link Control Field set value Error: " + clz.getName() + "-" + field.getName() + " > " + dr.getClass().getName());
						} catch (final IllegalAccessException e) {
							throw new InitializeException("Link Control Field set value Error: " + clz.getName() + "-" + field.getName() + " > " + dr.getClass().getName());
						} catch (final SecurityException e) {
							throw new InitializeException("Link Control Field set value Error: " + clz.getName() + "-" + field.getName() + " > " + dr.getClass().getName());
						} finally {
							if (null != field) {
								field.setAccessible(false);
							}
						}
						// throw new InitializeException("Link Control Field has Error: " + clz.getName() + "-" + field.getName());
					}
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
	@Override
	protected void setParam(final Object managerObject, final Class<? extends Object> clz) {
		// 得到属性列表
		final Field[] fields = clz.getDeclaredFields();
		ManagerIoc mag;
		DaoIoc dao;
		MongoDaoIoc mdao;
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
			} else if (null != (dao = field.getAnnotation(DaoIoc.class))) {
				// 存在注入
				if ((name = dao.value()).length() == 0) {
					name = field.getName();
				}
				if ((params = this.paramDao.get(managerObject)) == null) {
					params = new LinkedList<>();
					this.paramDao.put(managerObject, params);
				}
				params.add(field.getName() + "," + name + "," + field.getType().getName());
			} else if (null != (mdao = field.getAnnotation(MongoDaoIoc.class))) {
				// 存在注入
				if ((name = mdao.value()).length() == 0) {
					name = field.getName();
				}
				if ((params = this.paramDao.get(managerObject)) == null) {
					params = new LinkedList<>();
					this.paramDao.put(managerObject, params);
				}
				params.add(field.getName() + "," + name + "," + field.getType().getName());
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
	@Override
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
	@Override
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
}
