/**
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午4:39:15
 */
package com.tfzzh.model.tools;

import com.tfzzh.exception.ConfigurationException;
import com.tfzzh.model.dao.tools.FieldType;
import com.tfzzh.model.mongo.tools.MongoFieldTypeEnum;
import com.tfzzh.model.mysql.tools.MySQLFieldTypeEnum;

/**
 * 数据库工厂<br />
 * 会根据不同数据库，得到一些不同的配置<br />
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午4:39:15
 */
public abstract class DatabaseFactroy {

	/**
	 * 数据库为mysql，当前仅针对着一种
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午5:07:40
	 */
	private final static String DATABASE_MYSQL = "mysql";

	/**
	 * 根据字段类型名得到字段数据库相关类型对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午4:54:08
	 * @param type 数据库类型名
	 * @return 字段数据库相关类型对象
	 */
	public static FieldType getSqlFieldType(final String type) {
		if (DatabaseFactroy.DATABASE_MYSQL.equalsIgnoreCase(DaoConstants.DATABASE_TYPE)) {
			return MySQLFieldTypeEnum.getFieldType(type);
		} else {
			throw new ConfigurationException("Error Database type[dao_messages.properties:DATABASE_TYPE]: " + DaoConstants.DATABASE_TYPE);
		}
	}

	/**
	 * 根据对象得到字段数据库相关类型对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月8日 下午5:20:51
	 * @param obj 目标对象
	 * @return 数据库相关类型对象
	 */
	public static FieldType getSqlFieldType(final Object obj) {
		if (DatabaseFactroy.DATABASE_MYSQL.equalsIgnoreCase(DaoConstants.DATABASE_TYPE)) {
			return MySQLFieldTypeEnum.getFieldType(obj);
		} else {
			throw new ConfigurationException("Error Database type[dao_messages.properties:DATABASE_TYPE]: " + DaoConstants.DATABASE_TYPE);
		}
	}

	/**
	 * 根据对象类型得到字段数据库相关类型对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月8日 下午5:20:51
	 * @param clz 对象类型
	 * @return 数据库相关类型对象
	 */
	public static FieldType getSqlFieldType(final Class<?> clz) {
		if (DatabaseFactroy.DATABASE_MYSQL.equalsIgnoreCase(DaoConstants.DATABASE_TYPE)) {
			return MySQLFieldTypeEnum.getFieldType(clz);
		} else {
			throw new ConfigurationException("Error Database type[dao_messages.properties:DATABASE_TYPE]: " + DaoConstants.DATABASE_TYPE);
		}
	}

	/**
	 * 根据字段类型名得到字段Mongo相关类型对象
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月13日 下午4:11:08
	 * @param type 数据库类型名
	 * @return 字段Mongo相关类型对象
	 */
	public static FieldType getMongoFieldType(final String type) {
		return MongoFieldTypeEnum.getFieldType(type);
	}

	/**
	 * 根据字段类型名得到字段Mongo相关类型对象
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月13日 下午4:11:10
	 * @param obj 目标对象
	 * @return Mongo相关类型对象
	 */
	public static FieldType getMongoFieldType(final Object obj) {
		return MongoFieldTypeEnum.getFieldType(obj);
	}

	/**
	 * 根据字段类型名得到字段Mongo相关类型对象
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月13日 下午4:11:12
	 * @param clz 对象类型
	 * @return Mongo相关类型对象
	 */
	public static FieldType getMongoFieldType(final Class<?> clz) {
		return MongoFieldTypeEnum.getFieldType(clz);
	}
}
