/**
 * @author Weijie Xu
 * @dateTime 2015年4月17日 下午6:28:32
 */
package com.tfzzh.model.bean;

import java.lang.reflect.Field;

import org.bson.Document;

import com.tfzzh.exception.ConfigurationException;
import com.tfzzh.model.dao.tools.EntityInfoBean;

/**
 * 实体数据基础类
 *
 * @author Weijie Xu
 * @dateTime 2015年4月17日 下午6:28:32
 */
public abstract class BaseEntityBean extends BaseDataBean {

	/**
	 * @author tfzzh
	 * @dateTime 2017年1月16日 下午4:53:28
	 */
	private static final long serialVersionUID = 8691290842513323265L;

	/**
	 * 相关的表名称
	 *
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午6:28:56
	 */
	protected String tableName;

	/**
	 * 得到表名称，该方法，在某些情况下需要被重写
	 *
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午6:40:30
	 * @return 表名称
	 */
	public String getTableName() {
		return this.tableName;
	}

	/**
	 * 设置表名称
	 *
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午6:41:14
	 * @param tableName 表名称
	 */
	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 得到自增字段的类型
	 *
	 * @author XuWeijie
	 * @datetime 2015年9月22日_下午2:59:35
	 * @return 默认为Object，默认时，不进行判定
	 */
	@Override
	public Class<?> getIncrementKeyType() {
		return Object.class;
	}

	/**
	 * 得到目标字段的值
	 *
	 * @author tfzzh
	 * @dateTime 2016年9月6日 下午4:11:21
	 * @param fib 字段信息对象
	 * @return 目标字段的值
	 */
	public Object getFieldValue(final EntityInfoBean<?>.FieldInfoBean fib) {
		final Field f = fib.getObjField();
		final Class<?> clz = f.getType();
		f.setAccessible(true);
		try {
			if (Object.class.isAssignableFrom(clz)) {
				return f.get(this);
			} else if (clz == int.class) {
				return f.getInt(this);
			} else if (clz == long.class) {
				return f.getLong(this);
			} else if (clz == short.class) {
				return f.getShort(this);
			} else if (clz == boolean.class) {
				return f.getBoolean(this);
			} else if (clz == float.class) {
				return f.getFloat(this);
			} else if (clz == double.class) {
				return f.getDouble(this);
			} else if (clz == char.class) {
				return f.getChar(this);
			} else if (clz == byte.class) {
				return f.getByte(this);
			} else {
				return f.get(this);
			}
		} catch (final IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} finally {
			f.setAccessible(false);
		}
	}

	/**
	 * 将Mongo库中数据放入到对象<br />
	 * 针对一般MongoDb库用方法<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月7日 下午2:41:16
	 * @param doc mongo库结果
	 */
	@Override
	public void putDocumentData(final Document doc) {
		throw new ConfigurationException(" This impl not putDocumentData Method ... ");
	}
}
