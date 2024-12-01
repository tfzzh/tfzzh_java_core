/**
 * @author Xu Weijie
 * @dateTime 2016年9月21日 下午5:43:10
 */
package com.tfzzh.model.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.tfzzh.log.CoreLog;
import com.tfzzh.model.bean.BaseEntityBean;
import com.tfzzh.model.dao.SimpleSelectDAO;
import com.tfzzh.model.dao.annotation.DaoImpl;
import com.tfzzh.model.dao.tools.EntityInfoBean;
import com.tfzzh.model.dao.tools.EntityTool;
import com.tfzzh.model.dao.tools.QLBean;
import com.tfzzh.model.dao.tools.QLSqlBean;
import com.tfzzh.model.pools.ConnectionBean;
import com.tfzzh.model.tools.DaoBaseConstants;

/**
 * 简单查询DAO<br />
 * 针对返回ID或UUID等单数据结果的列表<br />
 * 
 * @author Xu Weijie
 * @dateTime 2016年9月21日 下午5:43:10
 */
@DaoImpl
public class SimpleSelectDAOImpl extends CoreDAOImpl implements SimpleSelectDAO {

	/**
	 * 查询得到指定表中指定信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2016年9月21日 下午5:43:10
	 * @param <B> 数据对象
	 * @param clz 数据对象类
	 * @param qb 条件的集合
	 * @return 得到的数据列表，不可能为null
	 * @throws SQLException 抛
	 * @throws IllegalAccessException 抛
	 * @throws InstantiationException 抛
	 * @see com.tfzzh.model.dao.SimpleSelectDAO#getDataList(java.lang.Class, com.tfzzh.model.dao.tools.QLBean)
	 */
	@Override
	public <B extends BaseEntityBean> List<B> getDataList(final Class<B> clz, final QLBean qb) throws SQLException, InstantiationException, IllegalAccessException {
		final QLSqlBean qs = qb.getSQL();
		if (null == qs) {
			return null;
		}
		final long l = System.currentTimeMillis();
		// 进行所相关表名验证 add xwj 2017-11-27
		qb.volidateTableName();
		// 得到sql基本语句
		final StringBuilder sql = qs.getSql();
		// 这里只处理分页
		qb.assemblyPagerankSQL(sql);
		final StringBuilder log;
		if (CoreLog.getInstance().debugEnabled(this.getClass())) {
			log = new StringBuilder();
			log.append('{');
		} else {
			log = null;
		}
		try (ConnectionBean conn = this.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
			if (null != qb) {
				qb.setPSValue(ps, log);
			}
			try (ResultSet rs = ps.executeQuery();) {
				final EntityInfoBean<B> eib = EntityTool.getInstance().getEntityInfo(clz);
				return eib.getEntityAndWithData(rs);
			} catch (final Exception e) {
				e.printStackTrace();
				return null;
			}
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				log.append('}');
				CoreLog.getInstance().debug(this.getClass(), "JDBCThread[", Thread.currentThread().getName(), "] run: ", Long.toString(System.currentTimeMillis() - l), " select data: ", sql.toString(), " >", (log.length() > DaoBaseConstants.SQL_MAX_LENGTH ? log.substring(0, DaoBaseConstants.SQL_MAX_LENGTH) : log.toString()));
			}
		}
	}

	/**
	 * 得到单一数据列表
	 * 
	 * @author tfzzh
	 * @dateTime 2024年1月25日 18:56:33
	 * @param <O> 单一数据对象
	 * @param clz 单一数据对象类
	 * @param qb 条件的集合
	 * @return 得到的数据列表，不可能为null
	 * @throws SQLException 抛
	 * @throws IllegalAccessException 抛
	 * @throws InstantiationException 抛
	 * @see com.tfzzh.model.dao.SimpleSelectDAO#getOnlyDataList(java.lang.Class, com.tfzzh.model.dao.tools.QLBean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <O extends Object> List<O> getOnlyDataList(final Class<O> clz, final QLBean qb) throws SQLException, InstantiationException, IllegalAccessException {
		final QLSqlBean qs = qb.getSQL();
		if (null == qs) {
			return null;
		}
		final long l = System.currentTimeMillis();
		// 进行所相关表名验证 add xwj 2017-11-27
		qb.volidateTableName();
		// 得到sql基本语句
		final StringBuilder sql = qs.getSql();
		// 这里只处理分页
		qb.assemblyPagerankSQL(sql);
		final StringBuilder log;
		if (CoreLog.getInstance().debugEnabled(this.getClass())) {
			log = new StringBuilder();
			log.append('{');
		} else {
			log = null;
		}
		try (ConnectionBean conn = this.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
			if (null != qb) {
				qb.setPSValue(ps, log);
			}
			try (ResultSet rs = ps.executeQuery();) {
				return (List<O>) this.resultToList(clz, rs);
			} catch (final Exception e) {
				e.printStackTrace();
				return null;
			}
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				log.append('}');
				CoreLog.getInstance().debug(this.getClass(), "JDBCThread[", Thread.currentThread().getName(), "] run: ", Long.toString(System.currentTimeMillis() - l), " select data: ", sql.toString(), " >", (log.length() > DaoBaseConstants.SQL_MAX_LENGTH ? log.substring(0, DaoBaseConstants.SQL_MAX_LENGTH) : log.toString()));
			}
		}
	}

	/**
	 * 将数据库返回的结果集处理为目标数据结果集
	 * 
	 * @author tfzzh
	 * @dateTime 2024年1月25日 18:56:25
	 * @param clz 单一数据对象类
	 * @param rs 数据库结果
	 * @return 目标数据结果集
	 */
	private List<?> resultToList(final Class<?> clz, final ResultSet rs) {
		try {
			if (clz == String.class) {
				final List<String> bak = new ArrayList<>();
				while (rs.next()) {
					bak.add(rs.getString(1));
				}
				return bak;
			} else if ((clz == Integer.class) || (clz == int.class)) {
				final List<Integer> bak = new ArrayList<>();
				while (rs.next()) {
					bak.add(rs.getInt(1));
				}
				return bak;
			} else if ((clz == Long.class) || (clz == long.class)) {
				final List<Long> bak = new ArrayList<>();
				while (rs.next()) {
					bak.add(rs.getLong(1));
				}
				return bak;
			} else if ((clz == Float.class) || (clz == float.class)) {
				final List<Float> bak = new ArrayList<>();
				while (rs.next()) {
					bak.add(rs.getFloat(1));
				}
				return bak;
			} else if ((clz == Short.class) || (clz == short.class)) {
				final List<Short> bak = new ArrayList<>();
				while (rs.next()) {
					bak.add(rs.getShort(1));
				}
				return bak;
			} else if ((clz == Double.class) || (clz == double.class)) {
				final List<Double> bak = new ArrayList<>();
				while (rs.next()) {
					bak.add(rs.getDouble(1));
				}
				return bak;
			} else if ((clz == Boolean.class) || (clz == boolean.class)) {
				final List<Boolean> bak = new ArrayList<>();
				while (rs.next()) {
					bak.add(rs.getBoolean(1));
				}
				return bak;
			} else if ((clz == Date.class) || Date.class.isAssignableFrom(clz)) {
				final List<Date> bak = new ArrayList<>();
				while (rs.next()) {
					bak.add(rs.getDate(1));
				}
				return bak;
			} else {
				// 正常不可能出现的情况
				final List<Object> bak = new ArrayList<>();
				while (rs.next()) {
					bak.add(rs.getObject(1));
				}
				return bak;
			}
		} catch (final SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 查询得到指定表中指定信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2016年9月21日 下午5:43:10
	 * @param qb 条件的集合
	 * @return 得到的数据列表，不可能为null
	 * @throws SQLException 抛
	 * @throws IllegalAccessException 抛
	 * @throws InstantiationException 抛
	 * @see com.tfzzh.model.dao.SimpleSelectDAO#getDataList(com.tfzzh.model.dao.tools.QLBean)
	 */
	@Override
	public List<Map<String, Object>> getDataList(final QLBean qb) throws SQLException, InstantiationException, IllegalAccessException {
		final QLSqlBean qs = qb.getSQL();
		if (null == qs) {
			return null;
		}
		final long l = System.currentTimeMillis();
		// 进行所相关表名验证 add xwj 2017-11-27
		qb.volidateTableName();
		// 得到sql基本语句
		final StringBuilder sql = qs.getSql();
		// 这里只处理分页
		qb.assemblyPagerankSQL(sql);
		final StringBuilder log;
		if (CoreLog.getInstance().debugEnabled(this.getClass())) {
			log = new StringBuilder();
			log.append('{');
		} else {
			log = null;
		}
		try (ConnectionBean conn = this.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
			if (null != qb) {
				qb.setPSValue(ps, log);
			}
			try (ResultSet rs = ps.executeQuery();) {
				return this.getEntityMap(rs);
			} catch (final Exception e) {
				e.printStackTrace();
				return null;
			}
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				log.append('}');
				CoreLog.getInstance().debug(this.getClass(), "JDBCThread[", Thread.currentThread().getName(), "] run: ", Long.toString(System.currentTimeMillis() - l), " select data: ", sql.toString(), " >", (log.length() > DaoBaseConstants.SQL_MAX_LENGTH ? log.substring(0, DaoBaseConstants.SQL_MAX_LENGTH) : log.toString()));
			}
		}
	}

	/**
	 * 得到目标sql得到的数量结果<br />
	 * sql语句从from开始写，会固定在前面增加内容“select count(0) ”<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2016年10月11日 下午12:44:19
	 * @param qb 条件的集合
	 * @return 数量结果 >=0，正常返回；<br />
	 *         -1，条件不正确；<br />
	 *         -2，与数据库连接错误；<br />
	 *         -3，查询错误；<br />
	 * @throws SQLException 抛
	 * @throws IllegalAccessException 抛
	 * @throws InstantiationException 抛
	 * @see com.tfzzh.model.dao.SimpleSelectDAO#getDataCount(com.tfzzh.model.dao.tools.QLBean)
	 */
	@Override
	public int getDataCount(final QLBean qb) throws SQLException, InstantiationException, IllegalAccessException {
		final QLSqlBean qs = qb.getSQL();
		if (null == qs) {
			return -1;
		}
		// 进行所相关表名验证 add xwj 2017-11-27
		qb.volidateTableName();
		// 得到sql基本语句
		final StringBuilder sql = qs.getSql();
		if (sql.indexOf("count(") == -1) {
			sql.insert(0, "select count(0) ");
		}
		return this.execData(qb);
	}

	/**
	 * 单纯的执行sql，一般为更新或删除等，仅会返回一个结果数量
	 * 
	 * @author tfzzh
	 * @dateTime 2016年10月27日 下午1:35:34
	 * @param qb 条件的集合
	 * @return 数量结果 >=0，正常返回；<br />
	 *         -1，条件不正确；<br />
	 *         -2，与数据库连接错误；<br />
	 *         -3，查询错误；<br />
	 * @throws SQLException 抛
	 * @throws IllegalAccessException 抛
	 * @throws InstantiationException 抛
	 * @see com.tfzzh.model.dao.SimpleSelectDAO#execData(com.tfzzh.model.dao.tools.QLBean)
	 */
	@Override
	public int execData(final QLBean qb) throws SQLException, InstantiationException, IllegalAccessException {
		final QLSqlBean qs = qb.getSQL();
		if (null == qs) {
			return -1;
		}
		final long l = System.currentTimeMillis();
		// 进行所相关表名验证 add xwj 2017-11-27
		qb.volidateTableName();
		// 这里只处理分页
		final StringBuilder sql = qs.getSql();
		qb.assemblyPagerankSQL(sql);
		final StringBuilder log;
		if (CoreLog.getInstance().debugEnabled(this.getClass())) {
			log = new StringBuilder();
			log.append('{');
		} else {
			log = null;
		}
		try (ConnectionBean conn = this.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
			if (null != qb) {
				qb.setPSValue(ps, log);
			}
			final String sw = sql.substring(0, 6).toLowerCase();
			if ("select".equals(sw)) {
				// 查询类
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						return rs.getInt(1);
					}
					return 0;
				} catch (final Exception e) {
					e.printStackTrace();
					return -3;
				}
			} else {
				// 执行类
				return ps.executeUpdate();
			}
		} catch (final Exception e) {
			e.printStackTrace();
			return -2;
		} finally {
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				log.append('}');
				CoreLog.getInstance().debug(this.getClass(), "JDBCThread[", Thread.currentThread().getName(), "] run: ", Long.toString(System.currentTimeMillis() - l), " select data: ", sql.toString(), " >", (log.length() > DaoBaseConstants.SQL_MAX_LENGTH ? log.substring(0, DaoBaseConstants.SQL_MAX_LENGTH) : log.toString()));
			}
		}
	}

	/**
	 * 得到数量实体的内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2016年9月21日 下午5:43:10
	 * @param rs 未经处理的数据消息
	 * @return 解析后的数据对象列表
	 * @throws SQLException 抛
	 */
	private List<Map<String, Object>> getEntityMap(final ResultSet rs) throws SQLException {
		final List<Map<String, Object>> list = new LinkedList<>();
		final ResultSetMetaData rsmd = rs.getMetaData();
		final int cou = rsmd.getColumnCount();
		final String[] names = new String[cou];
		final Map<String, Class<?>> mc = new HashMap<>();
		String name;
		for (int i = 0; i < cou;) {
			name = names[i] = rsmd.getColumnLabel(++i);
			switch (rsmd.getColumnType(i)) {
			case java.sql.Types.BIT:
			case java.sql.Types.TINYINT:
			case java.sql.Types.SMALLINT:
				mc.put(name, Short.class);
				break;
			case java.sql.Types.INTEGER:
				mc.put(name, Integer.class);
				break;
			case java.sql.Types.BIGINT:
				mc.put(name, Long.class);
				break;
			case java.sql.Types.FLOAT:
				mc.put(name, Float.class);
				break;
			case java.sql.Types.DOUBLE:
			case java.sql.Types.NUMERIC:
			case java.sql.Types.DECIMAL:
				mc.put(name, Double.class);
				break;
			case java.sql.Types.CHAR:
			case java.sql.Types.VARCHAR:
			case java.sql.Types.LONGVARCHAR:
				mc.put(name, String.class);
				break;
			case java.sql.Types.DATE:
			case java.sql.Types.TIME:
			case java.sql.Types.TIMESTAMP:
				mc.put(name, Date.class);
				break;
			case java.sql.Types.BOOLEAN:
				mc.put(name, Boolean.class);
				break;
			default:
			}
		}
		Map<String, Object> tmpMap;
		Class<?> clz;
		while (rs.next()) {
			tmpMap = new LinkedHashMap<>();
			for (int i = 0; i < cou;) {
				if (null == (clz = mc.get(names[i]))) {
					tmpMap.put(names[i], rs.getObject(++i));
				} else {
					if (clz == Integer.class) {
						tmpMap.put(names[i], rs.getInt(++i));
					} else if (clz == String.class) {
						tmpMap.put(names[i], rs.getString(++i));
					} else if (clz == Long.class) {
						tmpMap.put(names[i], rs.getLong(++i));
					} else if (clz == Short.class) {
						tmpMap.put(names[i], rs.getShort(++i));
					} else if (clz == Float.class) {
						tmpMap.put(names[i], rs.getFloat(++i));
					} else if (clz == Double.class) {
						tmpMap.put(names[i], rs.getDouble(++i));
					} else if (clz == Date.class) {
						try {
							tmpMap.put(names[i], rs.getDate(i));
						} catch (final Exception e) {
							tmpMap.put(names[i], rs.getString(i));
						}
						i++;
					} else if (clz == Boolean.class) {
						tmpMap.put(names[i], rs.getBoolean(++i));
					} else {
						tmpMap.put(names[i], rs.getObject(++i));
					}
				}
			}
			list.add(tmpMap);
		}
		return list;
	}
}
