/**
 * @author tfzzh
 * @dateTime 2016年11月18日 上午11:01:51
 */
package com.tfzzh.model.mongo.tools;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.tfzzh.exception.ConfigurationException;
import com.tfzzh.exception.NotAvailableOperationModeException;
import com.tfzzh.exception.NotExistsTargetException;
import com.tfzzh.model.dao.annotation.DataField;
import com.tfzzh.model.dao.annotation.IdField;
import com.tfzzh.model.dao.tools.FieldType;
import com.tfzzh.tools.DateFormat;

/**
 * mongo字段类型
 * 
 * @author tfzzh
 * @dateTime 2016年11月18日 上午11:01:51
 */
public enum MongoFieldTypeEnum implements FieldType {

	/**
	 * 这是最常用的数据类型来存储数据在MongoDB中的字符串必须是有效的UTF-8
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月23日 下午5:45:15
	 */
	String {

		@Override
		public Object getDefaultValue(final String dv) {
			return dv;
		}

		@Override
		public boolean canUuid() {
			return true;
		}

		@Override
		public void putValueToDBObject(final StringBuilder mongo, final java.lang.Object val) {
			mongo.append('"').append(val.toString().replaceAll("\"", "\\\\\"")).append('"');
		}
	},
	/**
	 * 这种类型是用来存储一个数值整数可以是32位或64位，这取决于您的服务器
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月23日 下午5:45:27
	 */
	Integer {

		@Override
		public Object getDefaultValue(final String dv) {
			return java.lang.Integer.valueOf(dv);
		}

		@Override
		public boolean canIncrement() {
			return true;
		}
	},
	/**
	 * 64位整型数
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月23日 下午5:48:59
	 */
	Long {

		@Override
		public Object getDefaultValue(final String dv) {
			return java.lang.Long.valueOf(dv);
		}

		@Override
		public boolean canIncrement() {
			return true;
		}
	},
	/**
	 * 此类型用于存储一个布尔值 (true/ false)
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月23日 下午5:49:06
	 */
	Boolean {

		@Override
		public Object getDefaultValue(final String dv) {
			return java.lang.Boolean.valueOf(dv);
		}
	},
	/**
	 * 这种类型是用来存储浮点值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月23日 下午5:49:11
	 */
	Double {

		@Override
		public Object getDefaultValue(final String dv) {
			return java.lang.Double.parseDouble(dv);
		}
	},
	/**
	 * 使用此类型的数组或列表或多个值存储到一个键
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月23日 下午5:49:17
	 */
	Arrays {

		@Override
		public void putValueToDBObject(final StringBuilder mongo, final java.lang.Object val) {
			mongo.append('{');
			boolean isFirst = true;
			if (val instanceof List) {
				// 是List
				final List<?> l = (List<?>) val;
				for (final Object o : l) {
					if (isFirst) {
						isFirst = false;
						mongo.append(',');
					}
					mongo.append(o);
				}
			}
			mongo.append('}');
		}
	},
	/**
	 * 时间戳这可以方便记录时的文件已被修改或添加
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月23日 下午5:49:23
	 */
	Timestamp {

		@Override
		public Object getDefaultValue(final String dv) {
			return DateFormat.getLongDateShow(dv);
		}
	},
	/**
	 * 此数据类型用于嵌入式的文件
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月23日 下午5:49:36
	 */
	Object,
	/**
	 * 此数据类型用于存储当前日期或时间的UNIX时间格式可以指定自己的日期和时间，日期和年，月，日到创建对象
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月23日 下午5:49:49
	 */
	Date {

		@Override
		public Object getDefaultValue(final String dv) {
			return DateFormat.getLongDateShow(dv);
		}
	},
	/**
	 * 此数据类型用于存储二进制数据
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月23日 下午5:50:06
	 */
	BinaryData {

		@Override
		public Object toShow(final Object obj) {
			return java.util.Arrays.toString((byte[]) obj);
		}
	},
	/**
	 * 此数据类型用于存储到文档中的JavaScript代码
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月23日 下午5:50:12
	 */
	Code {

		@Override
		public Object getDefaultValue(final String dv) {
			return dv;
		}

		@Override
		public void putValueToDBObject(final StringBuilder mongo, final java.lang.Object val) {
			mongo.append('"').append(val.toString().replaceAll("\"", "\\\\\"")).append('"');
		}
	},
	/**
	 * 此数据类型用于存储正则表达式
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月23日 下午5:50:24
	 */
	RegularExpression {

		@Override
		public Object getDefaultValue(final String dv) {
			return dv;
		}

		@Override
		public void putValueToDBObject(final StringBuilder mongo, final java.lang.Object val) {
			mongo.append('"').append(val.toString().replaceAll("\"", "\\\\\"")).append('"');
		}
	};

	/**
	 * 类型值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月26日 上午11:05:14
	 */
	private final String type;

	/**
	 * @author tfzzh
	 * @dateTime 2016年12月26日 上午11:05:15
	 */
	MongoFieldTypeEnum() {
		this.type = this.name().toLowerCase();
	}

	/**
	 * 根据类型值得到字段类型对应
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:46:22
	 * @param type 字段类型
	 * @return 字段类型对象
	 */
	public static MongoFieldTypeEnum getFieldType(String type) {
		if ((null == type) || ((type = type.trim()).length() == 0)) {
			throw new ConfigurationException("Error MongoField type Cannot Null: " + type);
		}
		type = type.toLowerCase();
		for (final MongoFieldTypeEnum e : MongoFieldTypeEnum.values()) {
			if (e.type.equals(type)) {
				return e;
			}
		}
		throw new ConfigurationException("Error MongoField type: " + type);
	}

	/**
	 * 根据对象得到字段类型对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月8日 下午5:18:33
	 * @param obj 目标对象
	 * @return 对象相关数据库的类型对象
	 */
	public static MongoFieldTypeEnum getFieldType(final Object obj) {
		return MongoFieldTypeEnum.getFieldType(obj.getClass());
	}

	/**
	 * 根据对象类型得到字段类型对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月8日 下午5:17:14
	 * @param clz 对象类型
	 * @return 对象相关数据库的类型对象
	 */
	public static MongoFieldTypeEnum getFieldType(final Class<?> clz) {
		if (String.class.isAssignableFrom(clz)) {
			return String;
		} else if (Integer.class.isAssignableFrom(clz)) {
			return Integer;
		} else if (Long.class.isAssignableFrom(clz)) {
			return Long;
		} else if (Short.class.isAssignableFrom(clz)) {
			return Integer;
		} else if (Double.class.isAssignableFrom(clz)) {
			return Double;
		} else if (Float.class.isAssignableFrom(clz)) {
			return Double;
		} else if (Boolean.class.isAssignableFrom(clz)) {
			return Boolean;
		} else if (java.util.Date.class.isAssignableFrom(clz)) {
			return Timestamp;
		} else if (InputStream.class.isAssignableFrom(clz)) {
			return BinaryData;
		} else {
			throw new NotExistsTargetException("The [" + clz.getName() + "] is Not Supported with Save to MongoDB...");
		}
	}

	/**
	 * 得到真实的默认值
	 *
	 * @author tfzzh
	 * @dateTime 2016年12月26日 上午11:15:36
	 * @param dv 默认值，字符串模型
	 * @return 真实的默认值
	 * @see com.tfzzh.model.dao.tools.FieldType#getDefaultValue(java.lang.String)
	 */
	@Override
	public java.lang.Object getDefaultValue(final java.lang.String dv) {
		throw new NotAvailableOperationModeException("This Type[" + this.name() + "] Cannt be set Default Value...");
	}

	/**
	 * 是否为可以设置自增的类型
	 *
	 * @author tfzzh
	 * @dateTime 2016年12月26日 上午11:15:36
	 * @return true，可以为自增；<br />
	 *         false，不可以为自增；<br />
	 * @see com.tfzzh.model.dao.tools.FieldType#canIncrement()
	 */
	@Override
	public boolean canIncrement() {
		return false;
	}

	/**
	 * 是否可以设置为UUID的类型
	 *
	 * @author tfzzh
	 * @dateTime 2016年12月26日 上午11:15:36
	 * @return true，可以为UUID；<br />
	 *         false，不可以为UUID；<br />
	 * @see com.tfzzh.model.dao.tools.FieldType#canUuid()
	 */
	@Override
	public boolean canUuid() {
		return false;
	}

	/**
	 * 制作alter内容
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月26日 上午11:15:36
	 * @param sb 组合用字串，会直接将增加的内容，放入进去
	 * @param df 数据字段信息
	 * @param idf Id类字段信息
	 * @return 同sb
	 * @see com.tfzzh.model.dao.tools.FieldType#makeAlter(java.lang.StringBuilder, com.tfzzh.model.dao.annotation.DataField, com.tfzzh.model.dao.annotation.IdField)
	 */
	@Override
	public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
		throw new NotAvailableOperationModeException(this.getClass().getSimpleName() + " makeAlter ...");
	}

	/**
	 * 是否相同的类型<br />
	 * 针对更新数据库结构时，当前主要结构型数据库<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月26日 上午11:15:36
	 * @param type 类型名，请保证内容均为大写
	 * @return true，是相同的；<br />
	 *         false，是不同的；<br />
	 * @see com.tfzzh.model.dao.tools.FieldType#sameType(java.lang.String)
	 */
	@Override
	public boolean sameType(final java.lang.String type) {
		throw new NotAvailableOperationModeException(this.getClass().getSimpleName() + " sameType ...");
	}

	/**
	 * 验证字段数据是否相同<br />
	 * 针对更新数据库结构时，当前主要结构型数据库<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月26日 上午11:15:36
	 * @param df 数据字段信息
	 * @param idf Id类字段信息
	 * @param length 字段长度
	 * @param canNull 是否可null
	 * @param unsigned 是否无符号，针对数值
	 * @param isIncrement 是否为自增
	 * @param scale 小数点位数
	 * @param defValue 默认值
	 * @param desc 说明内容
	 * @return true，是相同的；<br />
	 *         false，是不同的；<br />
	 * @see com.tfzzh.model.dao.tools.FieldType#validateData(com.tfzzh.model.dao.annotation.DataField, com.tfzzh.model.dao.annotation.IdField, int, boolean, boolean, boolean, int, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final java.lang.String defValue, final java.lang.String desc) {
		throw new NotAvailableOperationModeException(this.getClass().getSimpleName() + " validateData ...");
	}

	/**
	 * 放入值到声明对象
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月26日 上午11:15:36
	 * @param ps 声明对象
	 * @param val 目标值
	 * @param ind 目标位置，在ps中
	 * @see com.tfzzh.model.dao.tools.FieldType#putValueToPs(java.sql.PreparedStatement, java.lang.Object, int)
	 */
	@Override
	public void putValueToPs(final PreparedStatement ps, final java.lang.Object val, final int ind) {
		throw new NotAvailableOperationModeException(this.getClass().getSimpleName() + " putValueToPs ...");
	}

	/**
	 * 向对象中设置结果集中的对应值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月26日 上午11:15:36
	 * @param rs jdbc反馈的结果集
	 * @param obj 目标对象
	 * @param setM 对应的目标方法
	 * @param ind 在rs的索引位
	 * @throws IllegalAccessException 抛
	 * @throws IllegalArgumentException 抛
	 * @throws InvocationTargetException 抛
	 * @throws SQLException 抛
	 * @see com.tfzzh.model.dao.tools.FieldType#setValueWithResult(java.sql.ResultSet, java.lang.Object, java.lang.reflect.Method, int)
	 */
	@Override
	public void setValueWithResult(final ResultSet rs, final java.lang.Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
		throw new NotAvailableOperationModeException(this.getClass().getSimpleName() + " setValueWithResult ...");
	}

	/**
	 * 将对象按照目标类型进行可明文显示处理
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月26日 上午11:15:36
	 * @param obj 目标对象
	 * @return 可被明文显示的内容
	 * @see com.tfzzh.model.dao.tools.FieldType#toShow(java.lang.Object)
	 */
	@Override
	public java.lang.Object toShow(final java.lang.Object obj) {
		return obj;
	}

	/**
	 * 放入内容到mongo条件
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月9日 下午4:50:16
	 * @param mongo mongo条件串
	 * @param val 目标值
	 * @see com.tfzzh.model.dao.tools.FieldType#putValueToDBObject(java.lang.StringBuilder, java.lang.Object)
	 */
	@Override
	public void putValueToDBObject(final StringBuilder mongo, final java.lang.Object val) {
		mongo.append(val);
	}
}
