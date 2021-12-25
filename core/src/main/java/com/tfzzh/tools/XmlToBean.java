/**
 * @author Xu Weijie
 * @datetime 2018年1月18日_下午1:53:57
 */
package com.tfzzh.tools;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 将xml内容，直接转入到结构Bean
 * 
 * @author Xu Weijie
 * @datetime 2018年1月18日_下午1:53:57
 */
public class XmlToBean {

	/**
	 * 读取XML内容到Bean
	 * 
	 * @author Xu Weijie
	 * @datetime 2018年1月18日_下午2:09:31
	 * @param <T> 任意对象
	 * @param path 目标xml路径
	 * @param clz xml对应的主类
	 * @return 解析后得到的数据列表
	 */
	public static <T extends Object> List<T> readXmlToBean(final String path, final Class<T> clz) {// , final Map<String, Class<?>> fieldWithBean
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// 创建DocumentBuilderFactory实例,指定DocumentBuilder
		DocumentBuilder db = null;
		final List<String> sl = StringTools.splitToArray(path, Constants.COMMA);
		final List<T> tl = new ArrayList<>();
		for (String s : sl) {
			// 创建 文本编辑实例
			try {
				db = dbf.newDocumentBuilder();
			} catch (final ParserConfigurationException pce) {
				pce.printStackTrace();
			}
			Document doc = null;
			// 得到待读取文件
			try {
				if (Constants.OS_WIN) {
					if ((-1 == s.indexOf(":/")) && (-1 == s.indexOf(":\\\\"))) {
						// 认为是非windows环境下完整路径
						s = Constants.INIT_CONFIG_PATH_BASE + s;
					}
				} else { // 非win环境
					if (!s.startsWith("/")) {
						// 认为是非linux系统下完整路径
						s = Constants.INIT_CONFIG_PATH_BASE + s;
					}
				}
				doc = db.parse(s);
			} catch (final SAXException saxe) {
				saxe.printStackTrace();
			} catch (final IOException ioe) {
				ioe.printStackTrace();
			}
			// 读取文件过程
			try {
				final Element root = doc.getDocumentElement();
				root.getNodeName();
				final NodeList nl = root.getChildNodes();
				Node n;
				for (int i = 0; i < nl.getLength(); i++) {
					n = nl.item(i);
					// System.out.println("node[" + n.getNodeType() + "] >> " + n.getNodeName() + "::" + n.getNamespaceURI());
					if (!n.getNodeName().startsWith("#")) {
						// 是有效数据
						final T t = clz.getDeclaredConstructor().newInstance();
						// tl.add(XmlToBean.nodeElement(n, t, fieldWithBean));
						tl.add(XmlToBean.nodeElement(n, t));
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return tl;
	}

	/**
	 * 将元素值放入到对象
	 * 
	 * @author Xu Weijie
	 * @datetime 2018年1月18日_下午5:53:33
	 * @param <T> 任意对象
	 * @param pn 目标节点元素
	 * @param t 目标数据对象
	 * @return 处理后的数据对象
	 */
	private static <T extends Object> T nodeElement(final Node pn, final T t) { // , final Map<String, Class<?>> fieldWithBean
		final NamedNodeMap nm = pn.getAttributes();
		Node n;
		final Method[] ma = t.getClass().getMethods();
		final Map<String, Method> mm = new HashMap<>();
		String name;
		for (final Method m : ma) {
			name = m.getName();
			if (m.getParameterTypes().length == 1) {
				if ((name.length() > 4) && (name.startsWith("set") || name.startsWith("add"))) {
					mm.put(name.substring(3, 4).toLowerCase() + name.substring(4), m);
				}
			}
		}
		// System.out.println(mm);
		Method m;
		if ((null != nm) && (nm.getLength() > 0)) {
			// 存在属性
			for (int i = 0; i < nm.getLength(); i++) {
				n = nm.item(i);
				m = mm.get(n.getNodeName());
				// System.out.println("\t" + pn.getNodeName() + " >> attr >> " + n.getNodeName() + " >>> " + m);
				if (null != m) {
					final Class<?>[] pc = m.getParameterTypes(); // 参数列表
					try {
						// Object val = XmlToBean.fieldValue(pc[0], n.getNodeValue());
						final Object val = XmlToBean.fieldValue(pc[0], n);
						// System.out.println("\t\t" + n.getNodeName() + " >> attr >> " + val);
						if (null != val) {
							m.invoke(t, val);
						}
						continue;
					} catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException | DOMException e) {
						e.printStackTrace();
					}
				}
			}
		}
		final NodeList nl = pn.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			n = nl.item(i);
			if (!n.getNodeName().startsWith("#")) {
				// System.out.println(pn.getNodeName() + " >> node >> " + n.getNodeName() + " >>>> " + n.getTextContent());
				m = mm.get(n.getNodeName());
				if (null != m) {
					final Class<?>[] pc = m.getParameterTypes(); // 参数列表
					// Object val = XmlToBean.fieldValue(pc[0], n.getTextContent());
					final Object val = XmlToBean.fieldValue(pc[0], n);
					if (null == val) {
						// 不存在目标值
						// 看是否对象属性
						// final Class<?> clz = fieldWithBean.get(n.getNodeName());
						final Class<?> clz = pc[0];
						// if (null != clz) {
						// System.out.println(" clz >> " + clz);
						if (m.getName().startsWith("add")) {
							// 是列表
							final NodeList cnl = n.getChildNodes();
							if (cnl.getLength() > 0) {
								// 是子列表
								Node cn;
								Object cnt;
								for (int j = 0; j < cnl.getLength(); j++) {
									cn = cnl.item(j);
									if (!cn.getNodeName().startsWith("#")) {
										try {
											final Constructor<?> ct = clz.getConstructor(t.getClass());
											cnt = ct.newInstance(t);
											// XmlToBean.nodeElement(cn, cnt, fieldWithBean);
											XmlToBean.nodeElement(cn, cnt);
											m.invoke(t, cnt);
										} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
											e.printStackTrace();
										}
									}
								}
							} else {
								// 认为是属性情况，则一定是String
								if (pc[0] != String.class) {
									// 非String内容
									try {
										final Constructor<?> ct = clz.getConstructor(t.getClass());
										final Object cnt = ct.newInstance(t);
										// XmlToBean.nodeElement(n, cnt, fieldWithBean);
										XmlToBean.nodeElement(n, cnt);
										m.invoke(t, cnt);
									} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
										e.printStackTrace();
									}
								} else {
									final Object tv = XmlToBean.fieldValue(String.class, n);
									if (null != tv) {
										try {
											m.invoke(t, tv);
										} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
											e.printStackTrace();
										}
									}
								}
							}
							continue;
						} else {
							// 是单一对象
							try {
								final Constructor<?> ct = clz.getConstructor(t.getClass());
								final Object cnt = ct.newInstance(t);
								// System.out.println(" xml>bean>>>" + n + "::" + cnt);
								// XmlToBean.nodeElement(n, cnt, fieldWithBean);
								XmlToBean.nodeElement(n, cnt);
								m.invoke(t, cnt);
								continue;
							} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
								e.printStackTrace();
							}
						}
					}
					// }
					try {
						// System.out.println("\t\t" + n.getNodeName() + " >> node >> " + val);
						m.invoke(t, val);
						continue;
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | DOMException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return t;
	}

	/**
	 * 处理字段的值
	 * 
	 * @author Xu Weijie
	 * @datetime 2018年1月18日_下午3:14:53
	 * @param clz 值类型
	 * @param n 所相关节点
	 * @return 处理后的使用值
	 */
	public static Object fieldValue(final Class<?> clz, Node n) {
		// System.out.println("\t fieldValue type >> " + n.getNodeType());
		String v;
		switch (n.getNodeType()) {
		case Node.ELEMENT_NODE: // 是元素
			v = n.getTextContent();
			if ((null == v) || (v.length() == 0)) {
				// 没有目标值，查看默认属性
				final NamedNodeMap nnm = n.getAttributes();
				if (null != nnm) {
					n = nnm.getNamedItem("val");
				} else {
					return null;
				}
			} else {// 成功后直接跳出
				break;
			}
		case Node.ATTRIBUTE_NODE: // 是属性
			// 不进行更多处理
			v = n.getNodeValue();
			break;
		default:
			return null;
		}
		if (clz == int.class) {
			return Integer.parseInt(v);
		} else if (clz == String.class) {
			return v;
		} else if (clz == Integer.class) {
			return Integer.valueOf(v);
		} else if (clz == long.class) {
			return Long.parseLong(v);
		} else if (clz == Long.class) {
			return Long.valueOf(v);
		} else if (clz == boolean.class) {
			return Boolean.parseBoolean(v);
		} else if (clz == Boolean.class) {
			return Boolean.valueOf(v);
		} else if (clz == short.class) {
			return Short.parseShort(v);
		} else if (clz == Short.class) {
			return Short.valueOf(v);
		} else if (clz == float.class) {
			return Float.parseFloat(v);
		} else if (clz == Float.class) {
			return Float.valueOf(v);
		} else if (clz == double.class) {
			return Double.parseDouble(v);
		} else if (clz == Double.class) {
			return Double.valueOf(v);
		}
		return null;
	}
}
