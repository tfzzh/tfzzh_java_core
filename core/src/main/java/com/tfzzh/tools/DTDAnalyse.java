/**
 * @author XuWeijie
 * @dateTime 2010-3-26 上午10:22:24
 */
package com.tfzzh.tools;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.wutka.dtd.DTD;
import com.wutka.dtd.DTDAttribute;
import com.wutka.dtd.DTDChoice;
import com.wutka.dtd.DTDElement;
import com.wutka.dtd.DTDEmpty;
import com.wutka.dtd.DTDItem;
import com.wutka.dtd.DTDMixed;
import com.wutka.dtd.DTDName;
import com.wutka.dtd.DTDParser;
import com.wutka.dtd.DTDSequence;

/**
 * DTD分析
 * 
 * @author XuWeijie
 * @dateTime 2010-3-26 上午10:22:24
 * @model
 */
public class DTDAnalyse {

	/**
	 * 内元素设置
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:13:15
	 */
	private Map<String, Object> map;

	/**
	 * 主要入口标签名
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:12:55
	 */
	private final String root;

	/**
	 * 文件名称
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:12:54
	 */
	private final String fileName;

	/**
	 * 文件网络地址
	 * 
	 * @author Xu Weijie
	 * @datetime 2018年2月1日_下午1:34:10
	 */
	private final URL url;

	/**
	 * 读本地DTD文件
	 * 
	 * @author tfzzh
	 * @createDate 2009-1-9 下午05:55:28
	 * @param fileName 文件名称
	 * @param root 入口标签名
	 */
	public DTDAnalyse(final String fileName, final String root) {
		this.root = root;
		this.fileName = fileName;
		this.url = null;
		this.initialize();
	}

	/**
	 * 读网络DTD文件
	 * 
	 * @author Xu Weijie
	 * @datetime 2018年2月1日_下午1:34:32
	 * @param url 网络连接地址
	 * @param root 入口标签名
	 */
	public DTDAnalyse(final URL url, final String root) {
		this.root = root;
		this.fileName = null;
		this.url = url;
		this.initialize();
	}

	/**
	 * 初始化信息
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-4 下午01:47:02
	 */
	private void initialize() {
		final DTD dtd;
		if (null != this.fileName) {
			// 是文件方式
			final File file = new File(this.fileName);
			try {
				final DTDParser dp = new DTDParser(file);
				dtd = dp.parse(false);
			} catch (final IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			// 是网络方式
			try {
				final DTDParser dp = new DTDParser(this.url);
				dtd = dp.parse(false);
			} catch (final IOException e) {
				e.printStackTrace();
				return;
			}
		}
// final File file = new File(this.fileName);
// try {
// final DTDParser dp = new DTDParser(file);
// final DTD dtd = dp.parse(false);
// 得到DTD入口TAG名
		final DTDElement elem = (DTDElement) dtd.elements.get(this.root);
		if (elem != null) {
			// 存在入口则继续做向下分析
			this.map = new HashMap<>();
			this.map.put(elem.getName(), this.analyseElement(elem, dtd));
		}
// } catch (final IOException e) {
// e.printStackTrace();
// }
	}

	/**
	 * 分析单个元素内容
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-4 下午01:56:57
	 * @param elem 元素信息
	 * @param dtd DTD信息
	 * @return 元素分析后的MAP消息
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, Map> analyseElement(final DTDElement elem, final DTD dtd) {
		final Map<String, Map> innMap = new HashMap<>();
		// 分析得到元素属性,只分析得到元素名.不分析值的状态
		if ((elem.attributes != null) && !elem.attributes.isEmpty()) {
			DTDAttribute attr;
			final Enumeration<?> attrs = elem.attributes.elements();
			final Map<String, String> attrMap = new HashMap<>();
			while (attrs.hasMoreElements()) {
				attr = (DTDAttribute) attrs.nextElement();
				// 保存元素属性
				attrMap.put(attr.getName(), null);
			}
			innMap.put(XMLAnalyse.XML_TAG_ATTRIBUTE, attrMap);
		}
		// 分析得到内嵌元素
		if (elem.getContent() != null) {
			final DTDItem item = elem.getContent();
			final Map<String, Map> elemMap = this.dumpDTDItem(item, dtd);
			if ((elemMap != null) && (elemMap.size() > 0)) {
				// 有内嵌元素的部分
				innMap.put(XMLAnalyse.XML_TAG_ELEMENT, elemMap);
			}
		}
		return innMap;
	}

	/**
	 * @author XuWeijie
	 * @dateTime 2010-3-26 上午11:21:02
	 * @param item 元素消息
	 * @param dtd DTD信息
	 * @return 元素分析后的MAP消息
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, Map> dumpDTDItem(final DTDItem item, final DTD dtd) {
		if (item == null) {
			return null;
		} else {
			// 用于选择的tag组
			if (item instanceof DTDChoice) {
				final Map<String, Map> map = new HashMap<>();
				final DTDItem[] items = ((DTDChoice) item).getItems();
				String k;
				// 逐个分析内部元素
				for (final DTDItem it : items) {
					k = this.getDTDName(it);
					// 是否为tag名称
					if (k != null) {
						// 如果是tag名称则继续向下分析
						map.put(k, this.analyseElement((DTDElement) dtd.elements.get(k), dtd));
					} else {
						final Map<String, Map> tmpMap;
						if ((tmpMap = this.dumpDTDItem(it, dtd)) != null) {
							map.putAll(tmpMap);
						}
					}
				}
				return map;
			} else if (item instanceof DTDSequence) { // 顺序的tag组
				final Map<String, Map> map = new HashMap<>();
				final DTDItem[] items = ((DTDSequence) item).getItems();
				String k;
				// 逐个分析内部元素
				for (final DTDItem it : items) {
					k = this.getDTDName(it);
					// 是否为tag名称
					if (k != null) {
						// 如果是tag名称则继续向下分析
						final DTDElement elem = (DTDElement) dtd.elements.get(k);
						map.put(k, this.analyseElement(elem, dtd));
					} else {
						final Map<String, Map> tmpMap;
						if ((tmpMap = this.dumpDTDItem(it, dtd)) != null) {
							map.putAll(tmpMap);
						}
					}
				}
				return map;
			} else if (item instanceof DTDMixed) { // 混合的tag组
				final Map<String, Map> map = new HashMap<>();
				final DTDItem[] items = ((DTDMixed) item).getItems();
				String k;
				// 逐个分析内部元素
				for (final DTDItem it : items) {
					k = this.getDTDName(it);
					// 是否为tag名称
					if (k != null) {
						// 如果是tag名称则继续向下分析
						final DTDElement elem = (DTDElement) dtd.elements.get(k);
						map.put(k, this.analyseElement(elem, dtd));
					} else {
						final Map<String, Map> tmpMap;
						if ((tmpMap = this.dumpDTDItem(it, dtd)) != null) {
							map.putAll(tmpMap);
						}
					}
				}
				return map;
			} else if (item instanceof DTDEmpty) { // 空消息
				final Map<String, Map> map = new HashMap<>();
				final String k;
				// 是否为tag名称
				if ((k = this.getDTDName(item)) != null) {
					// 如果是tag名称则继续向下分析
					final DTDElement elem = (DTDElement) dtd.elements.get(k);
					map.put(k, this.analyseElement(elem, dtd));
				}
				return map;
			} else {
				// 其他情况
				return null;
			}
		}
	}

	/**
	 * 得到元素名称
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-4 上午11:29:46
	 * @param item 元素信息
	 * @return 对应名称
	 */
	private String getDTDName(final DTDItem item) {
		// 是否元素名称
		if (item instanceof DTDName) {
			return ((DTDName) item).value;
		} else {
			return null;
		}
	}

	/**
	 * 得到对DTD分析后的内容消息
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-4 上午11:30:43
	 * @return the map
	 */
	public Map<String, Object> getDTDElement() {
		return this.map;
	}
}
