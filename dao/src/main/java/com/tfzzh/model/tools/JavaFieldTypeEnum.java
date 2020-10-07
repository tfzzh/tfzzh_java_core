/**
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午7:55:25
 */
package com.tfzzh.model.tools;

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * java属性类型
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午7:55:25
 */
public enum JavaFieldTypeEnum {

	/**
	 * 字符串
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午7:57:21
	 */
	String {

		@Override
		public void setPreparedStatementValue(final PreparedStatement ps, final int ind, final Object value) throws SQLException {
			ps.setString(ind, (String) value);
		}
	},
	/**
	 * 整形
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午7:57:20
	 */
	Integer {

		@Override
		public void setPreparedStatementValue(final PreparedStatement ps, final int ind, final Object value) throws SQLException {
			ps.setInt(ind, (Integer) value);
		}
	},
	/**
	 * 短整形
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午7:57:20
	 */
	Short {

		@Override
		public void setPreparedStatementValue(final PreparedStatement ps, final int ind, final Object value) throws SQLException {
			ps.setShort(ind, (Short) value);
		}
	},
	/**
	 * 长整型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午7:57:19
	 */
	Long {

		@Override
		public void setPreparedStatementValue(final PreparedStatement ps, final int ind, final Object value) throws SQLException {
			ps.setLong(ind, (Long) value);
		}
	},
	/**
	 * 大浮点型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午7:57:19
	 */
	Double {

		@Override
		public void setPreparedStatementValue(final PreparedStatement ps, final int ind, final Object value) throws SQLException {
			ps.setDouble(ind, (Double) value);
		}
	},
	/**
	 * 浮点型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午7:57:18
	 */
	Float {

		@Override
		public void setPreparedStatementValue(final PreparedStatement ps, final int ind, final Object value) throws SQLException {
			ps.setFloat(ind, (Float) value);
		}
	},
	/**
	 * 字节型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午7:57:18
	 */
	Byte {

		@Override
		public void setPreparedStatementValue(final PreparedStatement ps, final int ind, final Object value) throws SQLException {
			ps.setByte(ind, (Byte) value);
		}
	},
	/**
	 * 布尔型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午7:57:17
	 */
	Boolean {

		@Override
		public void setPreparedStatementValue(final PreparedStatement ps, final int ind, final Object value) throws SQLException {
			ps.setBoolean(ind, (Boolean) value);
		}
	},
	/**
	 * 时间日期型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午7:57:17
	 */
	Date {

		@Override
		public void setPreparedStatementValue(final PreparedStatement ps, final int ind, final Object value) throws SQLException {
			ps.setTimestamp(ind, new java.sql.Timestamp(((java.util.Date) value).getTime()));
		}
	},
	/**
	 * 输入流，针对Blob，为公司项目而特别增加的处理
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午7:57:16
	 */
	InputStream {

		@Override
		public void setPreparedStatementValue(final PreparedStatement ps, final int ind, final Object value) throws SQLException {
			if (null != value) {
				ps.setBlob(ind, new ByteArrayInputStream((byte[]) value));
			} else {
				ps.setNull(ind, Types.BLOB);
			}
		}
	},
	/**
	 * 无内容，不会记录"?"信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午7:57:14
	 */
	Non {

		@Override
		public void setPreparedStatementValue(final PreparedStatement ps, final int ind, final Object value) throws SQLException {
			throw new SQLException("Non Type Cannt to set Value!");
		}
	};

	/**
	 * 放入值到SQL预处理条件
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午8:02:35
	 * @param ps 预处理对象
	 * @param ind 目标位置
	 * @param value 目标值
	 * @throws SQLException 抛
	 */
	public abstract void setPreparedStatementValue(PreparedStatement ps, int ind, Object value) throws SQLException;

	/**
	 * 根据类得到字段类型对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午8:02:30
	 * @param clz 目标类对象
	 * @return java字段类型对象
	 */
	public static JavaFieldTypeEnum getType(final Class<?> clz) {
		if (clz.equals(java.lang.String.class)) {
			return String;
		} else if (clz.equals(java.lang.Integer.class)) {
			return Integer;
		} else if (clz.equals(java.lang.Short.class)) {
			return Short;
		} else if (clz.equals(java.lang.Long.class)) {
			return Long;
		} else if (clz.equals(java.lang.Double.class)) {
			return Double;
		} else if (clz.equals(java.lang.Float.class)) {
			return Float;
		} else if (clz.equals(java.lang.Byte.class)) {
			return Byte;
		} else if (clz.equals(java.lang.Boolean.class)) {
			return Boolean;
		} else if (clz.equals(java.util.Date.class)) {
			return Date;
		} else if (clz.equals(byte[].class)) {
			return InputStream;
		} else {
			return Non;
		}
	}
}
