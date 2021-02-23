/**
 * @author XuWeijie
 * @dateTime 2010-3-26 上午10:04:51
 */
package com.tfzzh.tools;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author XuWeijie
 * @dateTime 2010-3-26 上午10:04:51
 * @model
 */
public class XMLAnalyse {

	/**
	 * XML中TAG属性名
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:17:12
	 */
	public static final String XML_TAG_ATTRIBUTE = "attribute";

	/**
	 * XML中TAG名
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:17:14
	 */
	public static final String XML_TAG_ELEMENT = "element";

	/**
	 * XML中TAG中内容
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:17:13
	 */
	public static final String XML_TAG_CONTENT = "content";

	/**
	 * xml内容
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:44:14
	 */
	private final Map<String, Object> xmlElements = new HashMap<>();

	/**
	 * 对应的DTD信息
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:44:18
	 */
	private final Map<String, Object> dtdElements;

	/**
	 * XML分析
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-4 下午01:48:39
	 * @param dtdPath 对应DTD文件路径
	 * @param dtdRoot 对应分析用入口
	 */
	public XMLAnalyse(final String dtdPath, final String dtdRoot) {
		this.dtdElements = new DTDAnalyse(dtdPath, dtdRoot).getDTDElement();
	}

	/**
	 * @author Xu Weijie
	 * @datetime 2018年2月1日_下午1:58:10
	 * @param dtdUrl 对应DTD文件网络地址
	 * @param dtdRoot 对应分析用入口
	 */
	public XMLAnalyse(final URL dtdUrl, final String dtdRoot) {
		this.dtdElements = new DTDAnalyse(dtdUrl, dtdRoot).getDTDElement();
	}

	/**
	 * 解析text为xml到root层
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月15日 下午3:17:04
	 * @param xmlTxt xml相关文本内容
	 * @return xml的root层
	 */
	public static Element extractXmlRoot(final String xmlTxt) {
		try {
			final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			final DocumentBuilder db = dbf.newDocumentBuilder();
			final StringReader sr = new StringReader(xmlTxt);
			final InputSource is = new InputSource(sr);
			final Document document = db.parse(is);
			final Element root = document.getDocumentElement();
			sr.close();
			return root;
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取数量XML文件内容
	 * 
	 * @author XuWeijie
	 * @dateTime Aug 11, 2010 2:35:51 PM
	 * @param basePath xml文件基础路径
	 * @param xmlPaths xml文件路径列表
	 */
	public void readXML(final String basePath, final String[] xmlPaths) {
		for (String xmlPath : xmlPaths) {
			if ((xmlPath = xmlPath.trim()).length() > 0) {
				this.readXML(basePath + xmlPath);
			}
		}
	}

	/**
	 * 读取指定前缀XML文件内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-6 下午6:15:23
	 * @param basePath xml文件基础路径
	 * @param xmlPrefix xml文件前缀
	 */
	public void readXML(final String basePath, final String xmlPrefix) {
		final File fd = new File(basePath);
		String fn;
		for (final File f : fd.listFiles()) {
			if ((fn = f.getName()).startsWith(xmlPrefix) && fn.endsWith(".xml")) {
				this.readXML(f);
			}
		}
	}

	/**
	 * 读取数量XML文件内容
	 * 
	 * @author TFZZH
	 * @dateTime 2011-3-4 下午08:14:58
	 * @param xmlPaths xml文件路径列表
	 */
	public void readXML(final String[] xmlPaths) {
		for (String xmlPath : xmlPaths) {
			if ((xmlPath = xmlPath.trim()).length() > 0) {
				this.readXML(xmlPath);
			}
		}
	}

	/**
	 * 读取XML文件内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-6 下午6:13:11
	 * @param xmlPath xml文件路径
	 */
	private void readXML(final String xmlPath) {
		this.readXML(new File(xmlPath));
	}

	/**
	 * 读取XML文件内容
	 * 
	 * @author tfzzh
	 * @createDate 2009-1-9 下午06:04:44
	 * @param file xml文件信息
	 */
	@SuppressWarnings("unchecked")
	private void readXML(final File file) {
		if (this.dtdElements == null) {
			return;
		} else {
			// 为解析XML作准备，
			// 创建DocumentBuilderFactory实例,指定DocumentBuilder
			final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = null;
			// 创建 文本编辑实例
			try {
				db = dbf.newDocumentBuilder();
			} catch (final ParserConfigurationException pce) {
				pce.printStackTrace();
			}
			Document doc = null;
			// 得到待读取文件
			try {
				doc = db.parse(file);
			} catch (final SAXException saxe) {
				saxe.printStackTrace();
			} catch (final IOException ioe) {
				ioe.printStackTrace();
			}
			// 读取文件过程
			try {
				final Element root = doc.getDocumentElement();
				final String rootTag = this.dtdElements.entrySet().iterator().next().getKey();
				if (root.getTagName().equals(rootTag)) {
					Map<String, Object> map;
					if ((map = (Map<String, Object>) this.xmlElements.get(rootTag)) == null) {
						this.xmlElements.put(rootTag, (map = new HashMap<>()));
					}
					this.readXMLElement(root, this.dtdElements, map);
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取XML标签中内容
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 下午12:42:23
	 * @param elem 标签消息
	 * @param ppts 内容消息
	 * @param eleMap 元素中内容消息
	 */
	@SuppressWarnings("unchecked")
	private void readXMLElement(final Element elem, final Map<String, Object> ppts, final Map<String, Object> eleMap) {
		// 得到当前标签序列
		final Iterator<Entry<String, Object>> it = ppts.entrySet().iterator();
		Entry<String, Object> ent;
		Map<String, Object> value;
		Map<Object, Object> tmpMap;
		List<Object> tmpList;
		// 逐个分析标签内容
		while (it.hasNext()) {
			ent = it.next();
			value = (Map<String, Object>) ent.getValue();
			// 是否存在属性
			if (value.containsKey(XMLAnalyse.XML_TAG_ATTRIBUTE)) {
				// 分析属性
				final Map<String, String> attr = (Map<String, String>) value.get(XMLAnalyse.XML_TAG_ATTRIBUTE);
				if ((attr != null) && !attr.isEmpty()) {
					// 属性Map<attribute,Map<"attrName", "attrValue">>
					// Map<String, Map<String, String>> attrMap = new HashMap<>();
					// 得到属性名称列表
					final Iterator<Entry<String, String>> itA = attr.entrySet().iterator();
					Entry<String, String> entA;
					String keyA, valA;
					if ((tmpMap = (Map<Object, Object>) eleMap.get(XMLAnalyse.XML_TAG_ATTRIBUTE)) == null) {
						eleMap.put(XMLAnalyse.XML_TAG_ATTRIBUTE, (tmpMap = new HashMap<>()));
					}
					while (itA.hasNext()) {
						entA = itA.next();
						keyA = entA.getKey();
						valA = elem.getAttribute(keyA);
						if ((valA != null) && (valA.length() > 0)) {
							tmpMap.put(keyA, elem.getAttribute(keyA));
						}
					}
				}
			}
			// 是否存在内容
			{
				final Text text = (Text) elem.getFirstChild();
				if ((text != null) && (text.getNodeValue().trim().length() != 0)) {
					eleMap.put(XMLAnalyse.XML_TAG_CONTENT, text.getNodeValue().trim());
				}
			}
			// 是否存在内嵌元素
			if (value.containsKey(XMLAnalyse.XML_TAG_ELEMENT)) {
				final Map<String, Object> innerElem = (Map<String, Object>) value.get(XMLAnalyse.XML_TAG_ELEMENT);
				if (innerElem != null) {
					Map<String, Object> map;
					final Iterator<Entry<String, Object>> itE = innerElem.entrySet().iterator();
					Entry<String, Object> entE;
					String keyE;
					Map<Object, Object> valueE;
					List<Map<String, Object>> innerTag;
					if ((tmpMap = (Map<Object, Object>) eleMap.get(XMLAnalyse.XML_TAG_ELEMENT)) == null) {
						eleMap.put(XMLAnalyse.XML_TAG_ELEMENT, (tmpMap = new HashMap<>()));
					}
					while (itE.hasNext()) {
						entE = itE.next();
						keyE = entE.getKey();
						valueE = (Map<Object, Object>) entE.getValue();
						final NodeList nl = elem.getElementsByTagName(keyE);
						if (nl != null) {
							final int size = nl.getLength();
							if (size != 0) {
								innerTag = new ArrayList<>(size);
								for (int i = 0; i < size; i++) {
									if (valueE != null) {
										final Map<String, Object> temp = new HashMap<>();
										temp.put(keyE, valueE);
										innerTag.add((map = new HashMap<>()));
										this.readXMLElement((Element) nl.item(i), temp, map);
									}
								}
								if (innerTag.size() != 0) {
									if ((tmpList = (List<Object>) tmpMap.get(keyE)) == null) {
										tmpMap.put(keyE, (tmpList = new ArrayList<>()));
									}
									tmpList.addAll(innerTag);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 得到对XML文件分析后的结果数据
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-4 下午02:34:07
	 * @return the xmlElements
	 */
	public Map<String, Object> getXmlElements() {
		return this.xmlElements;
	}
}
