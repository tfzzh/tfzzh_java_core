/**
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午3:16:05
 */
package com.tfzzh.model.mysql.tools;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;

import com.tfzzh.exception.ConfigurationException;
import com.tfzzh.exception.NotAvailableOperationModeException;
import com.tfzzh.exception.NotExistsTargetException;
import com.tfzzh.model.dao.annotation.DataField;
import com.tfzzh.model.dao.annotation.IdField;
import com.tfzzh.model.dao.tools.FieldType;
import com.tfzzh.model.dao.tools.IdFieldEnum;
import com.tfzzh.model.exception.FieldLengthException;
import com.tfzzh.tools.DateFormat;

/**
 * 数据字段类型
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午3:16:05
 */
public enum MySQLFieldTypeEnum implements FieldType {

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:38:55
	 */
	Char() {

		/**
		 * 特殊默认值1
		 * 
		 * @author tfzzh
		 * @dateTime 2021年12月2日 下午7:34:18
		 */
		private final String spDef1 = "''".intern();

		/**
		 * 特殊默认值对应替换值
		 * 
		 * @author tfzzh
		 * @dateTime 2021年12月2日 下午7:34:17
		 */
		private final String spVal1 = "".intern();

		@Override
		public Object getDefaultValue(final String dv) {
			if (null != dv) {
				if (this.spDef1.equals(dv)) {
					return spVal1;
				}
			}
			return dv;
		}

		@Override
		public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
			if ((df.length() < 1) || (df.length() > 255)) {
				throw new FieldLengthException(df.fieldName(), this.name(), df.length());
			}
			sb.append("CHAR(").append(df.length()).append(") ");
			super.isCanNull(sb, df);
			if (!df.defValue().equals("-")) {
				if (df.defValue().equalsIgnoreCase("NULL")) {
					sb.append("DEFAULT NULL ");
				} else {
					if ((df.defValue().length() > 1) && df.defValue().startsWith("'") && df.defValue().endsWith("'")) {
						sb.append("DEFAULT ").append(df.defValue()).append(' ');
					} else {
						sb.append("DEFAULT '").append(df.defValue()).append("' ");
					}
				}
			}
			sb.append("COMMENT '").append(df.desc()).append('\'');
			return sb;
		}

		@Override
		public boolean sameType(final String type) {
			return "CHAR".equals(type);
		}

		@Override
		public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final String defValue, final String desc) {
			if (!super.validateLength(df, length)) {
				return false;
			}
			if (!super.validateBaseData(df, canNull, desc)) {
				return false;
			}
			if (!super.validateDefault(df, defValue)) {
				return false;
			}
			return true;
		}

		@Override
		public boolean canUuid() {
			return true;
		}

		@Override
		public void putValueToPs(final PreparedStatement ps, final Object val, final int ind) {
			try {
				ps.setString(ind, (String) val);
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void setValueWithResult(final ResultSet rs, final Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
			setM.invoke(obj, rs.getString(ind));
		}
	},
	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:38:55
	 */
	VarChar() {

		/**
		 * 特殊默认值1
		 * 
		 * @author tfzzh
		 * @dateTime 2021年12月2日 下午7:34:18
		 */
		private final String spDef1 = "''".intern();

		/**
		 * 特殊默认值对应替换值
		 * 
		 * @author tfzzh
		 * @dateTime 2021年12月2日 下午7:34:17
		 */
		private final String spVal1 = "".intern();

		@Override
		public Object getDefaultValue(final String dv) {
			if (null != dv) {
				if (this.spDef1.equals(dv)) {
					return spVal1;
				}
			}
			return dv;
		}

		@Override
		public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
			if ((df.length() < 1) || (df.length() > 65532)) {
				throw new FieldLengthException(df.fieldName(), this.name(), df.length());
			}
			sb.append("VARCHAR(").append(df.length()).append(") ");
			super.isCanNull(sb, df);
			if (!df.defValue().equals("-")) {
				if (df.defValue().equalsIgnoreCase("NULL")) {
					sb.append("DEFAULT NULL ");
				} else {
					if ((df.defValue().length() > 1) && df.defValue().startsWith("'") && df.defValue().endsWith("'")) {
						sb.append("DEFAULT ").append(df.defValue()).append(' ');
					} else {
						sb.append("DEFAULT '").append(df.defValue()).append("' ");
					}
				}
			}
			sb.append("COMMENT '").append(df.desc()).append('\'');
			return sb;
		}

		@Override
		public boolean sameType(final String type) {
			return "VARCHAR".equals(type);
		}

		@Override
		public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final String defValue, final String desc) {
			if (!super.validateLength(df, length)) {
				return false;
			}
			if (!super.validateBaseData(df, canNull, desc)) {
				return false;
			}
			if (!super.validateDefault(df, defValue)) {
				return false;
			}
			return true;
		}

		@Override
		public void putValueToPs(final PreparedStatement ps, final Object val, final int ind) {
			try {
				ps.setString(ind, (String) val);
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void setValueWithResult(final ResultSet rs, final Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
			final String s = rs.getString(ind);
			if (null != s) {
				setM.invoke(obj, s);
			}
		}
	},
	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:38:56
	 */
	Int() {

		@Override
		public Object getDefaultValue(String dv) {
			final int ind = dv.indexOf(".");
			if (ind != -1) {
				dv = dv.substring(0, ind);
			}
			return Integer.parseInt(dv);
		}

		@Override
		public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
			sb.append("INT ");
			super.isUnsigned(sb, df);
			super.isCanNull(sb, df);
			super.isIncrement(sb, idf);
			if (!df.defValue().equals("-")) {
				sb.append("DEFAULT ").append(df.defValue()).append(' ');
			}
			sb.append("COMMENT '").append(df.desc()).append('\'');
			return sb;
		}

		@Override
		public boolean sameType(final String type) {
			if (type.length() < 3) {
				return false;
			} else if (type.length() == 3) {
				return "INT".equals(type);
			} else {
				return type.startsWith("INT");
			}
		}

		@Override
		public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final String defValue, final String desc) {
			if (!super.validateNumber(df, unsigned)) {
				return false;
			}
			if (!super.validateBaseData(df, canNull, desc)) {
				return false;
			}
			if (!super.validateDefault(df, defValue)) {
				return false;
			}
			if (!super.validateIncrement(idf, isIncrement)) {
				return false;
			}
			return true;
		}

		@Override
		public boolean canIncrement() {
			return true;
		}

		@Override
		public void putValueToPs(final PreparedStatement ps, final Object val, final int ind) {
			try {
				if (null == val) {
					ps.setNull(ind, Types.INTEGER);
				} else if (val instanceof Integer) {
					ps.setInt(ind, (Integer) val);
				} else if (val instanceof Number) {
					ps.setInt(ind, ((Number) val).intValue());
				} else {
					ps.setInt(ind, Integer.parseInt(val.toString()));
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void setValueWithResult(final ResultSet rs, final Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
			setM.invoke(obj, rs.getInt(ind));
		}
	},
	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:38:56
	 */
	BigInt() {

		@Override
		public Object getDefaultValue(String dv) {
			final int ind = dv.indexOf(".");
			if (ind != -1) {
				dv = dv.substring(0, ind);
			}
			return Long.parseLong(dv);
		}

		@Override
		public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
			sb.append("BIGINT ");
			super.isUnsigned(sb, df);
			super.isCanNull(sb, df);
			super.isIncrement(sb, idf);
			if (!df.defValue().equals("-")) {
				sb.append("DEFAULT ").append(df.defValue()).append(' ');
			}
			sb.append("COMMENT '").append(df.desc()).append('\'');
			return sb;
		}

		@Override
		public boolean sameType(final String type) {
			if (type.length() < 6) {
				return false;
			} else if (type.length() == 6) {
				return "BIGINT".equals(type);
			} else {
				return type.startsWith("BIGINT");
			}
		}

		@Override
		public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final String defValue, final String desc) {
			if (!super.validateNumber(df, unsigned)) {
				return false;
			}
			if (!super.validateBaseData(df, canNull, desc)) {
				return false;
			}
			if (!super.validateDefault(df, defValue)) {
				return false;
			}
			if (!super.validateIncrement(idf, isIncrement)) {
				return false;
			}
			return true;
		}

		@Override
		public boolean canIncrement() {
			return true;
		}

		@Override
		public void putValueToPs(final PreparedStatement ps, final Object val, final int ind) {
			try {
				if (null == val) {
					ps.setNull(ind, Types.BIGINT);
				} else if (val instanceof Long) {
					ps.setLong(ind, (Long) val);
				} else if (val instanceof Number) {
					ps.setLong(ind, ((Number) val).longValue());
				} else {
					ps.setLong(ind, Long.parseLong(val.toString()));
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void setValueWithResult(final ResultSet rs, final Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
			setM.invoke(obj, rs.getLong(ind));
		}
	},
	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:38:57
	 */
	TinyInt() {

		@Override
		public Object getDefaultValue(String dv) {
			final int ind = dv.indexOf(".");
			if (ind != -1) {
				dv = dv.substring(0, ind);
			}
			return Short.parseShort(dv);
		}

		@Override
		public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
			sb.append("TINYINT ");
			super.isUnsigned(sb, df);
			super.isCanNull(sb, df);
			if (!df.defValue().equals("-")) {
				sb.append("DEFAULT ").append(df.defValue()).append(' ');
			}
			sb.append("COMMENT '").append(df.desc()).append('\'');
			return sb;
		}

		@Override
		public boolean sameType(final String type) {
			if (type.length() < 7) {
				return false;
			} else if (type.length() == 7) {
				return "TINYINT".equals(type);
			} else {
				return type.startsWith("TINYINT");
			}
		}

		@Override
		public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final String defValue, final String desc) {
			if (!super.validateNumber(df, unsigned)) {
				return false;
			}
			if (!super.validateBaseData(df, canNull, desc)) {
				return false;
			}
			if (!super.validateDefault(df, defValue)) {
				return false;
			}
			if (!super.validateIncrement(idf, isIncrement)) {
				return false;
			}
			return true;
		}

		@Override
		public boolean canIncrement() {
			return true;
		}

		@Override
		public void putValueToPs(final PreparedStatement ps, final Object val, final int ind) {
			try {
				if (null == val) {
					ps.setNull(ind, Types.TINYINT);
				} else if (val instanceof Short) {
					ps.setShort(ind, (Short) val);
				} else if (val instanceof Number) {
					ps.setShort(ind, ((Number) val).shortValue());
				} else {
					ps.setShort(ind, Short.parseShort(val.toString()));
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void setValueWithResult(final ResultSet rs, final Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
			setM.invoke(obj, rs.getShort(ind));
		}
	},
	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:38:57
	 */
	Double() {

		@Override
		public Object getDefaultValue(final String dv) {
			return java.lang.Double.parseDouble(dv);
		}

		@Override
		public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
			sb.append("DOUBLE(").append(df.precision()).append(',').append(df.scale()).append(") ");
			super.isUnsigned(sb, df);
			super.isCanNull(sb, df);
			if (!df.defValue().equals("-")) {
				sb.append("DEFAULT ").append(df.defValue()).append(' ');
			}
			sb.append("COMMENT '").append(df.desc()).append('\'');
			return sb;
		}

		@Override
		public boolean sameType(final String type) {
			if (type.length() < 6) {
				return false;
			} else if (type.length() == 6) {
				return "DOUBLE".equals(type);
			} else {
				return type.startsWith("DOUBLE");
			}
		}

		@Override
		public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final String defValue, final String desc) {
			if (!super.validateFloat(df, length, scale)) {
				return false;
			}
			if (!super.validateNumber(df, unsigned)) {
				return false;
			}
			if (!super.validateBaseData(df, canNull, desc)) {
				return false;
			}
			if (!super.validateDefault(df, defValue)) {
				return false;
			}
			return true;
		}

		@Override
		public void putValueToPs(final PreparedStatement ps, final Object val, final int ind) {
			try {
				if (null == val) {
					ps.setNull(ind, Types.DOUBLE);
				} else if (val instanceof Double) {
					ps.setDouble(ind, (Double) val);
				} else if (val instanceof Number) {
					ps.setDouble(ind, ((Number) val).doubleValue());
				} else {
					ps.setDouble(ind, java.lang.Double.parseDouble(val.toString()));
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void setValueWithResult(final ResultSet rs, final Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
			setM.invoke(obj, rs.getDouble(ind));
		}
	},
	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:38:58
	 */
	Float() {

		@Override
		public Object getDefaultValue(final String dv) {
			return java.lang.Float.parseFloat(dv);
		}

		@Override
		public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
			sb.append("FLOAT(").append(df.precision()).append(',').append(df.scale()).append(") ");
			super.isUnsigned(sb, df);
			super.isCanNull(sb, df);
			if (!df.defValue().equals("-")) {
				sb.append("DEFAULT ").append(df.defValue()).append(' ');
			}
			sb.append("COMMENT '").append(df.desc()).append('\'');
			return sb;
		}

		@Override
		public boolean sameType(final String type) {
			if (type.length() < 5) {
				return false;
			} else if (type.length() == 5) {
				return "FLOAT".equals(type);
			} else {
				return type.startsWith("FLOAT");
			}
		}

		@Override
		public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final String defValue, final String desc) {
			if (!super.validateFloat(df, length, scale)) {
				return false;
			}
			if (!super.validateNumber(df, unsigned)) {
				return false;
			}
			if (!super.validateBaseData(df, canNull, desc)) {
				return false;
			}
			if (!super.validateDefault(df, defValue)) {
				return false;
			}
			return true;
		}

		@Override
		public void putValueToPs(final PreparedStatement ps, final Object val, final int ind) {
			try {
				if (null == val) {
					ps.setNull(ind, Types.FLOAT);
				} else if (val instanceof Float) {
					ps.setFloat(ind, (Float) val);
				} else if (val instanceof Number) {
					ps.setFloat(ind, ((Number) val).floatValue());
				} else {
					ps.setFloat(ind, java.lang.Float.parseFloat(val.toString()));
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void setValueWithResult(final ResultSet rs, final Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
			setM.invoke(obj, rs.getFloat(ind));
		}
	},
	/**
	 * 65,535(216–1)
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:38:58
	 */
	Text() {

		@Override
		public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
			sb.append("TEXT ");
			super.isCanNull(sb, df);
			sb.append("COMMENT '").append(df.desc()).append('\'');
			return sb;
		}

		@Override
		public boolean sameType(final String type) {
			return "TEXT".equals(type);
		}

		@Override
		public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final String defValue, final String desc) {
			return super.validateBaseData(df, canNull, desc);
		}

		@Override
		public void putValueToPs(final PreparedStatement ps, final Object val, final int ind) {
			try {
				ps.setString(ind, (String) val);
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void setValueWithResult(final ResultSet rs, final Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
			final String s = rs.getString(ind);
			if (null != s) {
				setM.invoke(obj, s);
			}
		}
	},
	/**
	 * 4,294,967,295或4GB(232–1)
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:38:59
	 */
	LongText() {

		@Override
		public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
			sb.append("LONGTEXT ");
			super.isCanNull(sb, df);
			sb.append("COMMENT '").append(df.desc()).append('\'');
			return sb;
		}

		@Override
		public boolean sameType(final String type) {
			return "LONGTEXT".equals(type);
		}

		@Override
		public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final String defValue, final String desc) {
			return super.validateBaseData(df, canNull, desc);
		}

		@Override
		public void putValueToPs(final PreparedStatement ps, final Object val, final int ind) {
			try {
				ps.setString(ind, (String) val);
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void setValueWithResult(final ResultSet rs, final Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
			final String s = rs.getString(ind);
			if (null != s) {
				setM.invoke(obj, s);
			}
		}
	},
	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:38:59
	 */
	TinyText() {

		@Override
		public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
			sb.append("TINYTEXT ");
			super.isCanNull(sb, df);
			sb.append("COMMENT '").append(df.desc()).append('\'');
			return sb;
		}

		@Override
		public boolean sameType(final String type) {
			return "TINYTEXT".equals(type);
		}

		@Override
		public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final String defValue, final String desc) {
			return super.validateBaseData(df, canNull, desc);
		}

		@Override
		public void putValueToPs(final PreparedStatement ps, final Object val, final int ind) {
			try {
				ps.setString(ind, (String) val);
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void setValueWithResult(final ResultSet rs, final Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
			final String s = rs.getString(ind);
			if (null != s) {
				setM.invoke(obj, s);
			}
		}
	},
	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:38:59
	 */
	Bit() {

		@Override
		public Object getDefaultValue(final String dv) {
			return Boolean.parseBoolean(dv) ? 1 : 0;
		}

		@Override
		public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
			sb.append("BIT ");
			super.isCanNull(sb, df);
			if (!df.defValue().equals("-")) {
				if (df.defValue().equalsIgnoreCase("NULL")) {
					sb.append("DEFAULT NULL ");
				} else {
					if ((df.defValue().length() > 1) && df.defValue().startsWith("'") && df.defValue().endsWith("'")) {
						sb.append("DEFAULT ").append(df.defValue()).append(' ');
					} else {
						sb.append("DEFAULT '").append(df.defValue()).append("' ");
					}
				}
			}
			sb.append("COMMENT '").append(df.desc()).append('\'');
			return sb;
		}

		@Override
		public boolean sameType(final String type) {
			return "BIT".equals(type);
		}

		@Override
		public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final String defValue, final String desc) {
			return super.validateBaseData(df, canNull, desc);
		}

		@Override
		public void putValueToPs(final PreparedStatement ps, final Object val, final int ind) {
			try {
				ps.setBoolean(ind, (Boolean) val);
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public Object toShow(final Object obj) {
			return obj == Boolean.TRUE ? 1 : 0;
		}

		@Override
		public void setValueWithResult(final ResultSet rs, final Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
			setM.invoke(obj, rs.getBoolean(ind));
		}
	},
	/**
	 * 65,535(216–1)
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:39:00
	 */
	Blob() {

		@Override
		public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
			// if (df.length() < 1 || df.length() > 65532) {
			// throw new FieldLengthException(df.fieldName(), this.name(), df.length());
			// }
			sb.append("BLOB ");
			super.isCanNull(sb, df);
			sb.append("COMMENT '").append(df.desc()).append('\'');
			return sb;
		}

		@Override
		public boolean sameType(final String type) {
			return "BLOB".equals(type);
		}

		@Override
		public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final String defValue, final String desc) {
			return super.validateBaseData(df, canNull, desc);
		}

		@Override
		public void putValueToPs(final PreparedStatement ps, final Object val, final int ind) {
			try {
				ps.setBytes(ind, (byte[]) val);
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public Object toShow(final Object obj) {
			// final InputStream is = (InputStream) obj;
			// try {
			// final byte[] bs = new byte[is.available()];
			// is.read(bs);
			// return Arrays.toString(bs);
			// } catch (final IOException e) {
			// e.printStackTrace();
			// return "";
			// }
			return Arrays.toString((byte[]) obj);
		}

		@Override
		public void setValueWithResult(final ResultSet rs, final Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
			final byte[] b = rs.getBytes(ind);
			if (null != b) {
				setM.invoke(obj, b);
			}
		}
	},
	/**
	 * 4,294,967,295或4GB(232–1)
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:39:00
	 */
	LongBlob() {

		@Override
		public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
			// if (df.length() < 1 || df.length() > 4294967295l) {
			// throw new FieldLengthException(df.fieldName(), this.name(), df.length());
			// }
			sb.append("LONGBLOB ");
			super.isCanNull(sb, df);
			sb.append("COMMENT '").append(df.desc()).append('\'');
			return sb;
		}

		@Override
		public boolean sameType(final String type) {
			return "LONGBLOB".equals(type);
		}

		@Override
		public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final String defValue, final String desc) {
			return super.validateBaseData(df, canNull, desc);
		}

		@Override
		public void putValueToPs(final PreparedStatement ps, final Object val, final int ind) {
			try {
				// ps.setBlob(ind, (InputStream) val);
				ps.setBytes(ind, (byte[]) val);
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public Object toShow(final Object obj) {
			// final InputStream is = (InputStream) obj;
			// try {
			// final byte[] bs = new byte[is.available()];
			// is.read(bs);
			// return Arrays.toString(bs);
			// } catch (final IOException e) {
			// e.printStackTrace();
			// return "";
			// }
			return Arrays.toString((byte[]) obj);
		}

		@Override
		public void setValueWithResult(final ResultSet rs, final Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
			// final java.sql.Blob b = rs.getBlob(ind);
			// if (null != b) {
			// setM.invoke(obj, b.getBinaryStream());
			// }
			final byte[] b = rs.getBytes(ind);
			if (null != b) {
				setM.invoke(obj, b);
			}
		}
	},
	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:39:01
	 */
	TinyBlob() {

		@Override
		public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
			// if (df.length() < 1 || df.length() > 255) {
			// throw new FieldLengthException(df.fieldName(), this.name(), df.length());
			// }
			sb.append("TINYBLOB ");
			super.isCanNull(sb, df);
			sb.append("COMMENT '").append(df.desc()).append('\'');
			return sb;
		}

		@Override
		public boolean sameType(final String type) {
			return "TINYBLOB".equals(type);
		}

		@Override
		public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final String defValue, final String desc) {
			return super.validateBaseData(df, canNull, desc);
		}

		@Override
		public void putValueToPs(final PreparedStatement ps, final Object val, final int ind) {
			try {
				// ps.setBlob(ind, (InputStream) val);
				ps.setBytes(ind, (byte[]) val);
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public Object toShow(final Object obj) {
			// final InputStream is = (InputStream) obj;
			// try {
			// final byte[] bs = new byte[is.available()];
			// is.read(bs);
			// return Arrays.toString(bs);
			// } catch (final IOException e) {
			// e.printStackTrace();
			// return "";
			// }
			return Arrays.toString((byte[]) obj);
		}

		@Override
		public void setValueWithResult(final ResultSet rs, final Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
			// final java.sql.Blob b = rs.getBlob(ind);
			// if (null != b) {
			// setM.invoke(obj, b.getBinaryStream());
			// }
			final byte[] b = rs.getBytes(ind);
			if (null != b) {
				setM.invoke(obj, b);
			}
		}
	},
	/**
	 * YYYY-MM-DD HH:MM:SS<br />
	 * 不能早于1970或晚于2037<br />
	 * 1.4个字节储存（Time stamp value is stored in 4 bytes）<br />
	 * 2.值以UTC格式保存（ it stores the number of milliseconds）<br />
	 * 3.时区转化 ，存储时对当前的时区进行转换，检索时再转换回当前的时区。<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:39:01
	 */
	TimeStamp() {

		@Override
		public Object getDefaultValue(final String dv) {
			return DateFormat.getLongDateShow(dv);
		}

		@Override
		public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
			sb.append("TIMESTAMP ");
			super.isCanNull(sb, df);
			if (!df.defValue().equals("-")) {
				if (df.defValue().equalsIgnoreCase("NULL")) {
					sb.append("DEFAULT NULL ");
				} else {
					if ((df.defValue().length() > 1) && df.defValue().startsWith("'") && df.defValue().endsWith("'")) {
						sb.append("DEFAULT ").append(df.defValue()).append(' ');
					} else {
						sb.append("DEFAULT '").append(df.defValue()).append("' ");
					}
				}
			}
			sb.append("COMMENT '").append(df.desc()).append('\'');
			return sb;
		}

		@Override
		public boolean sameType(final String type) {
			return "TIMESTAMP".equals(type);
		}

		@Override
		public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final String defValue, final String desc) {
			if (!super.validateBaseData(df, canNull, desc)) {
				return false;
			}
			if (!super.validateDefault(df, defValue)) {
				return false;
			}
			return true;
		}

		@Override
		public void putValueToPs(final PreparedStatement ps, final Object val, final int ind) {
			try {
				ps.setTimestamp(ind, new Timestamp(((java.util.Date) val).getTime()));
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public Object toShow(final Object obj) {
			return DateFormat.getLongDateShow((java.util.Date) obj);
		}

		@Override
		public void setValueWithResult(final ResultSet rs, final Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
			final java.sql.Timestamp t = rs.getTimestamp(ind);
			if (null != t) {
				setM.invoke(obj, t);
			}
		}
	},
	/**
	 * YYYY-MM-DD HH:MM:SS<br />
	 * '1000-01-01 00:00:00'到'9999-12-31 23:59:59'<br />
	 * 1.8个字节储存（8 bytes storage）<br />
	 * 2.实际格式储存（Just stores what you have stored and retrieves the same thing which you have stored.）<br />
	 * 3.与时区无关（It has nothing to deal with the TIMEZONE and Conversion.）<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:39:02
	 */
	DateTime() {

		@Override
		public Object getDefaultValue(final String dv) {
			return dv;
		}

		@Override
		public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
			sb.append("DATETIME ");
			super.isCanNull(sb, df);
			if (!df.defValue().equals("-")) {
				if (df.defValue().equalsIgnoreCase("NULL")) {
					sb.append("DEFAULT NULL ");
				} else {
					if ((df.defValue().length() > 1) && df.defValue().startsWith("'") && df.defValue().endsWith("'")) {
						sb.append("DEFAULT ").append(df.defValue()).append(' ');
					} else {
						sb.append("DEFAULT '").append(df.defValue()).append("' ");
					}
				}
			}
			sb.append("COMMENT '").append(df.desc()).append('\'');
			return sb;
		}

		@Override
		public boolean sameType(final String type) {
			return "DATETIME".equals(type);
		}

		@Override
		public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final String defValue, final String desc) {
			if (!super.validateBaseData(df, canNull, desc)) {
				return false;
			}
			if (!super.validateDefault(df, defValue)) {
				return false;
			}
			return true;
		}

		@Override
		public void putValueToPs(final PreparedStatement ps, final Object val, final int ind) {
			try {
				ps.setString(ind, (String) val);
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void setValueWithResult(final ResultSet rs, final Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
			final String s = rs.getString(ind);
			if (null != s) {
				setM.invoke(obj, s);
			}
		}
	},
	/**
	 * 在你仅需要日期值时，没有时间部分。MySQL检索并且以'YYYY-MM-DD'格式显示DATE值，支持的范围是'1000-01-01'到'9999-12-31'。
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:39:02
	 */
	Date() {

		@Override
		public Object getDefaultValue(final String dv) {
			return dv;
		}

		@Override
		public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
			sb.append("DATE ");
			super.isCanNull(sb, df);
			if (!df.defValue().equals("-")) {
				if (df.defValue().equalsIgnoreCase("NULL")) {
					sb.append("DEFAULT NULL ");
				} else {
					if ((df.defValue().length() > 1) && df.defValue().startsWith("'") && df.defValue().endsWith("'")) {
						sb.append("DEFAULT ").append(df.defValue()).append(' ');
					} else {
						sb.append("DEFAULT '").append(df.defValue()).append("' ");
					}
				}
			}
			sb.append("COMMENT '").append(df.desc()).append('\'');
			return sb;
		}

		@Override
		public boolean sameType(final String type) {
			return "DATE".equals(type);
		}

		@Override
		public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final String defValue, final String desc) {
			if (!super.validateBaseData(df, canNull, desc)) {
				return false;
			}
			if (!super.validateDefault(df, defValue)) {
				return false;
			}
			return true;
		}

		@Override
		public void putValueToPs(final PreparedStatement ps, final Object val, final int ind) {
			try {
				ps.setString(ind, (String) val);
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void setValueWithResult(final ResultSet rs, final Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
			final String s = rs.getString(ind);
			if (null != s) {
				setM.invoke(obj, s);
			}
		}
	},
	/**
	 * 表示一天中的时间。MySQL检索并且以"HH:MM:SS"格式显示TIME值。支持的范围是'00:00:00'到'23:59:59'。
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:39:03
	 */
	Time() {

		@Override
		public Object getDefaultValue(final String dv) {
			return dv;
		}

		@Override
		public StringBuilder makeAlter(final StringBuilder sb, final DataField df, final IdField idf) {
			sb.append("TIME ");
			super.isCanNull(sb, df);
			if (!df.defValue().equals("-")) {
				if (df.defValue().equalsIgnoreCase("NULL")) {
					sb.append("DEFAULT NULL ");
				} else {
					if ((df.defValue().length() > 1) && df.defValue().startsWith("'") && df.defValue().endsWith("'")) {
						sb.append("DEFAULT ").append(df.defValue()).append(' ');
					} else {
						sb.append("DEFAULT '").append(df.defValue()).append("' ");
					}
				}
			}
			sb.append("COMMENT '").append(df.desc()).append('\'');
			return sb;
		}

		@Override
		public boolean sameType(final String type) {
			return "TIME".equals(type);
		}

		@Override
		public boolean validateData(final DataField df, final IdField idf, final int length, final boolean canNull, final boolean unsigned, final boolean isIncrement, final int scale, final String defValue, final String desc) {
			if (!super.validateBaseData(df, canNull, desc)) {
				return false;
			}
			if (!super.validateDefault(df, defValue)) {
				return false;
			}
			return true;
		}

		@Override
		public void putValueToPs(final PreparedStatement ps, final Object val, final int ind) {
			try {
				ps.setString(ind, (String) val);
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void setValueWithResult(final ResultSet rs, final Object obj, final Method setM, final int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
			final String s = rs.getString(ind);
			if (null != s) {
				setM.invoke(obj, s);
			}
		}
	};

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:41:45
	 */
	private final String type;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:41:42
	 */
	MySQLFieldTypeEnum() {
		this.type = this.name().toLowerCase();
	}

	/**
	 * 是否为无符号
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午2:36:54
	 * @param sb 组合用字串，会直接将增加的内容，放入进去
	 * @param df 数据字段信息
	 */
	protected void isUnsigned(final StringBuilder sb, final DataField df) {
		if (df.unsigned()) {
			sb.append("UNSIGNED ");
		}
	}

	/**
	 * 是否可null
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午2:36:53
	 * @param sb 组合用字串，会直接将增加的内容，放入进去
	 * @param df 数据字段信息
	 */
	protected void isCanNull(final StringBuilder sb, final DataField df) {
		if (!df.canNull()) {
			sb.append("NOT NULL ");
		}
	}

	/**
	 * 是否自增，针对数值型字段
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午2:36:51
	 * @param sb 组合用字串，会直接将增加的内容，放入进去
	 * @param idf Id类字段信息
	 */
	protected void isIncrement(final StringBuilder sb, final IdField idf) {
		if ((null != idf) && (idf.value() == IdFieldEnum.Increment)) {
			sb.append("AUTO_INCREMENT ");
		}
	}

	/**
	 * 验证基础信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月5日 下午7:00:54
	 * @param df 数据字段信息
	 * @param canNull 是否可null
	 * @param desc 注释内容
	 * @return true，相同，通过；<br />
	 *         false，不同，未通过；<br />
	 */
	protected boolean validateBaseData(final DataField df, final boolean canNull, final String desc) {
		return df.canNull() != canNull ? false : (df.desc().equals(desc));
	}

	/**
	 * 验证字符串一类的长度
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月5日 下午7:01:01
	 * @param df 数据字段信息
	 * @param length 长度
	 * @return true，相同，通过；<br />
	 *         false，不同，未通过；<br />
	 */
	protected boolean validateLength(final DataField df, final int length) {
		return df.length() == length;
	}

	/**
	 * 验证数值相关
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月5日 下午7:05:09
	 * @param df 数据字段信息
	 * @param unsigned 是否无符号
	 * @return true，相同，通过；<br />
	 *         false，不同，未通过；<br />
	 */
	protected boolean validateNumber(final DataField df, final boolean unsigned) {
		return df.unsigned() == unsigned;
	}

	/**
	 * 验证浮点相关
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月5日 下午7:01:31
	 * @param df 数据字段信息
	 * @param length 数值长度
	 * @param scale 小数点位数
	 * @return true，相同，通过；<br />
	 *         false，不同，未通过；<br />
	 */
	protected boolean validateFloat(final DataField df, final int length, final int scale) {
		return df.precision() != length ? false : (df.scale() == scale);
	}

	/**
	 * 验证是否有自增
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月5日 下午7:05:06
	 * @param idf Id类字段信息
	 * @param isIncrement 是否为自增
	 * @return true，相同，通过；<br />
	 *         false，不同，未通过；<br />
	 */
	protected boolean validateIncrement(final IdField idf, final boolean isIncrement) {
		return null == idf ? !isIncrement : ((idf.value() == IdFieldEnum.Increment) == isIncrement);
	}

	/**
	 * 验证默认值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月5日 下午7:05:07
	 * @param df 数据字段信息
	 * @param defValue 默认值
	 * @return true，相同，通过；<br />
	 *         false，不同，未通过；<br />
	 */
	protected boolean validateDefault(final DataField df, final String defValue) {
		if ("-".equals(df.defValue())) {
			return (null == defValue) || (defValue.length() == 0);
		} else {
			return df.defValue().equals(defValue);
		}
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 上午11:13:27
	 * @param sb 组合用字串，会直接将增加的内容，放入进去
	 * @param df 数据字段信息
	 * @param idf Id类字段信息
	 * @return 同sb
	 * @see com.tfzzh.model.dao.tools.FieldType#makeAlter(java.lang.StringBuilder, com.tfzzh.model.dao.annotation.DataField, com.tfzzh.model.dao.annotation.IdField)
	 */
	@Override
	public abstract StringBuilder makeAlter(StringBuilder sb, DataField df, IdField idf);

	/**
	 * 得到真实的默认值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午8:20:31
	 * @param dv 默认值，字符串模型
	 * @return 真实的默认值
	 * @see com.tfzzh.model.dao.tools.FieldType#getDefaultValue(java.lang.String)
	 */
	@Override
	public Object getDefaultValue(final String dv) {
		throw new NotAvailableOperationModeException("This Type[" + this.name() + "] Cannt be set Default Value...");
	}

	/**
	 * 是否为可以设置自增的类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月5日 下午7:12:10
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
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午2:47:59
	 * @return true，可以为UUID；<br />
	 *         false，不可以为UUID；<br />
	 * @see com.tfzzh.model.dao.tools.FieldType#canUuid()
	 */
	@Override
	public boolean canUuid() {
		return false;
	}

	/**
	 * 将对象按照目标类型进行可明文显示处理
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月8日 下午7:14:37
	 * @param obj 目标对象
	 * @return 可被明文显示的内容
	 * @see com.tfzzh.model.dao.tools.FieldType#toShow(java.lang.Object)
	 */
	@Override
	public Object toShow(final Object obj) {
		return obj;
	}

	/**
	 * 放入内容到mongo条件
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月9日 下午4:51:50
	 * @param mongo mongo条件串
	 * @param val 目标值
	 * @see com.tfzzh.model.dao.tools.FieldType#putValueToDBObject(java.lang.StringBuilder, java.lang.Object)
	 */
	@Override
	public void putValueToDBObject(final StringBuilder mongo, final Object val) {
		throw new NotAvailableOperationModeException(this.getClass().getSimpleName() + " putValueToDBObject ...");
	}

	/**
	 * 根据类型值得到字段类型对应
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:46:22
	 * @param type 字段类型
	 * @return 字段类型对象
	 */
	public static MySQLFieldTypeEnum getFieldType(String type) {
		if ((null == type) || ((type = type.trim()).length() == 0)) {
			throw new ConfigurationException("Error DataField type Cannot Null: " + type);
		}
		type = type.toLowerCase();
		for (final MySQLFieldTypeEnum e : MySQLFieldTypeEnum.values()) {
			if (e.type.equals(type)) {
				return e;
			}
		}
		throw new ConfigurationException("Error DataField type: " + type);
	}

	/**
	 * 根据对象得到字段类型对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月8日 下午5:18:33
	 * @param obj 目标对象
	 * @return 对象相关数据库的类型对象
	 */
	public static MySQLFieldTypeEnum getFieldType(final Object obj) {
		return MySQLFieldTypeEnum.getFieldType(obj.getClass());
	}

	/**
	 * 根据对象类型得到字段类型对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月8日 下午5:17:14
	 * @param clz 对象类型
	 * @return 对象相关数据库的类型对象
	 */
	public static MySQLFieldTypeEnum getFieldType(final Class<?> clz) {
		if (String.class.isAssignableFrom(clz)) {
			return VarChar;
		} else if (Integer.class.isAssignableFrom(clz)) {
			return Int;
		} else if (Long.class.isAssignableFrom(clz)) {
			return BigInt;
		} else if (Short.class.isAssignableFrom(clz)) {
			return TinyInt;
		} else if (Double.class.isAssignableFrom(clz)) {
			return Double;
		} else if (Float.class.isAssignableFrom(clz)) {
			return Float;
		} else if (Boolean.class.isAssignableFrom(clz)) {
			return Bit;
		} else if (java.util.Date.class.isAssignableFrom(clz)) {
			return TimeStamp;
		} else if (InputStream.class.isAssignableFrom(clz)) {
			return Blob;
		} else {
			throw new NotExistsTargetException("The [" + clz.getName() + "] is Not Supported with Save to Database...");
		}
	}
}
