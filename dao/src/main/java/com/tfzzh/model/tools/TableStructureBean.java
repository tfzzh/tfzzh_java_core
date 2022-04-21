/**
 * @author tfzzh
 * @dateTime 2020年12月3日 下午6:04:19
 */
package com.tfzzh.model.tools;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 表结构对象
 * 
 * @author tfzzh
 * @dateTime 2020年12月3日 下午6:04:19
 */
public class TableStructureBean {

	/**
	 * 表名
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月3日 下午6:11:24
	 */
	private String name;

	/**
	 * 表说明
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月3日 下午6:11:24
	 */
	private String desc;

	/**
	 * 字段列表，有序
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月3日 下午6:11:25
	 */
	private Map<String, TableFieldStructureBean> fields = new LinkedHashMap<>();

	/**
	 * @author tfzzh
	 * @dateTime 2020年12月3日 下午6:18:47
	 * @param name 表名
	 * @param desc 表说明
	 */
	public TableStructureBean(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	/**
	 * 放入一个字段
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月3日 下午7:35:30
	 * @param field 字段
	 */
	public void putField(TableFieldStructureBean field) {
		this.fields.put(field.getName(), field);
	}

	/**
	 * 得到
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月7日 下午5:52:47
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 得到
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月7日 下午5:52:47
	 * @return the desc
	 */
	public String getDesc() {
		return this.desc;
	}

	/**
	 * 得到字段列表
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月7日 下午5:52:47
	 * @return the fields
	 */
	public Collection<TableFieldStructureBean> getFields() {
		return this.fields.values();
	}

	/**
	 * 得到指定字段信息
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月7日 下午5:53:25
	 * @param key 目标属性名
	 * @return 对应属性信息
	 */
	public TableFieldStructureBean getField(String key) {
		return this.fields.get(key);
	}
}
