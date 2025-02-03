/**
 * @author Weijie Xu
 * @dateTime 2015年4月28日 上午11:00:58
 */
package com.tfzzh.model.dao.impl;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tfzzh.log.CoreLog;
import com.tfzzh.model.bean.BaseEntityBean;
import com.tfzzh.model.dao.tools.AlterTypeEnum;
import com.tfzzh.model.dao.tools.EntityInfoBean;
import com.tfzzh.model.dao.tools.IndexCounter;
import com.tfzzh.model.dao.tools.QLBean;
import com.tfzzh.model.pools.ConnectionBean;
import com.tfzzh.model.tools.DaoBaseConstants;

/**
 * 基础DAO处理实例
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月28日 上午11:00:58
 * @param <E> 数据实例对象insertData
 */
public abstract class BaseDAOImpl<E extends BaseEntityBean> extends CoreDAOImpl {

	/**
	 * 相关数据实体的类信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午8:43:37
	 */
	private final EntityInfoBean<E> eib = this.getEntityInfo();

	/**
	 * 得到数据实体的类信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午8:43:38
	 * @return 数据实体的类信息
	 */
	protected abstract EntityInfoBean<E> getEntityInfo();

	/**
	 * 创建新表或更新表结构
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午4:46:03
	 * @return -1，未成功；<br />
	 *         0，没有任何变化；<br />
	 *         1，创建的新表；<br />
	 *         2，更新的内容；<br />
	 * @throws SQLException 抛
	 */
	public int createOrEditTable() throws SQLException {
		return this.createOrEditTable(this.eib.getDefaultTableName());
	}

	/**
	 * 创建新表或更新表结构
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月28日 下午12:30:54
	 * @param tableName 表名称
	 * @return -1，未成功；<br />
	 *         0，没有任何变化；<br />
	 *         1，创建的新表；<br />
	 *         2，更新的内容；<br />
	 * @throws SQLException 抛
	 */
	public int createOrEditTable(String tableName) throws SQLException {
		final long l = System.currentTimeMillis();
		ConnectionBean conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int back = -1;
		final String backS = null;
		String sql = null;
		try {
			// final long l1 = System.currentTimeMillis();
			conn = this.getConnection();
			final DatabaseMetaData dmd = conn.getMetaData();
			// System.out.println("link use >> " + (System.currentTimeMillis() - l1));
			if (null == tableName) {
				tableName = this.eib.getDefaultTableName();
			}
			final String dbName = conn.getDatabaseName();
			rs = dmd.getTables(dbName, null, tableName, new String[] { "TABLE" });
			// 对字段的处理，先增加，再修改，再移除
			// 对索引的处理，先删除，再增加
			// 整体，字段增加，字段修改，索引删除，字段移除，索引增加
			if (rs.next()) {
				// 关闭之前的关联
				rs.close();
				final StringBuilder sb = new StringBuilder(200);
				sb.append("ALTER TABLE `").append(tableName).append("` ");
				// 需要被移除的字段
				final List<String> dropFields = new LinkedList<>();
				boolean isFirst = true;
				{ // 字段的控制
					// 从新关联字段
					rs = dmd.getColumns(dbName, null, tableName, null);
					// 一定是copy的
					final Map<String, EntityInfoBean<E>.FieldInfoBean> fis = this.eib.getTpFiledsCopy();
					final Map<String, EntityInfoBean<E>.FieldInfoBean> efs = new LinkedHashMap<>(fis.size());
					EntityInfoBean<E>.FieldInfoBean fi;
					String fieldName;
					String behindField = null;
					while (rs.next()) {
						// 认为此处的字段列表是有序的，正常就是有序的
						// 有表，进入对表字段及索引的逐个比较
						fieldName = rs.getString("COLUMN_NAME");
						// 1.TABLE_CAT String => table catalog (may be null)
						// 2.TABLE_SCHEM String => table schema (may be null)
						// 3.TABLE_NAME String => table name
						// 4.COLUMN_NAME String => column name
						// 5.DATA_TYPE int => SQL type from java.sql.Types
						// 6.TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
						// 7.COLUMN_SIZE int => column size.
						// 8.BUFFER_LENGTH is not used.
						// 9.DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
						// 10.NUM_PREC_RADIX int => Radix (typically either 10 or 2)
						// 11.NULLABLE int => is NULL allowed. ◦ columnNoNulls - might not allow NULL values
						// ◦ columnNullable - definitely allows NULL values
						// ◦ columnNullableUnknown - nullability unknown
						//
						// 12.REMARKS String => comment describing column (may be null)
						// 13.COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
						// 14.SQL_DATA_TYPE int => unused
						// 15.SQL_DATETIME_SUB int => unused
						// 16.CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
						// 17.ORDINAL_POSITION int => index of column in table (starting at 1)
						// 18.IS_NULLABLE String => ISO rules are used to determine the nullability for a column. ◦ YES --- if the column can include NULLs
						// ◦ NO --- if the column cannot include NULLs
						// ◦ empty string --- if the nullability for the column is unknown
						//
						// 19.SCOPE_CATALOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
						// 20.SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
						// 21.SCOPE_TABLE String => table name that this the scope of a reference attribute (null if the DATA_TYPE isn't REF)
						// 22.SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
						// 23.GENERATED COLUMNIS_AUTOINCREMENT String => Indicates whether this column is auto incremented ◦ YES --- if the column is auto incremented
						// ◦ NO --- if the column is not auto incremented
						// ◦ empty string --- if it cannot be determined whether the column is auto incremented
						//
						// 24.IS_GENERATEDCOLUMN String => Indicates whether this is a generated column ◦ YES --- if this a generated column
						// ◦ NO --- if this not a generated column
						// ◦ empty string --- if it cannot be determined whether this is a generated column
						// rs.getInt("DATA_TYPE") 有点乱，不可用
						// rs.getInt("NUM_PREC_RADIX") 进制10或2
						// rs.getString("IS_GENERATEDCOLUMN") 是否生成的列
						// rs.getString("IS_NULLABLE")同rs.getInt("NULLABLE")：Yes-1；No-0；
						// rs.getInt("CHAR_OCTET_LENGTH")
						// System.out.println(fieldName + " -- " + rs.getString("TYPE_NAME") + " -- " + rs.getInt("COLUMN_SIZE") + " -- " + rs.getInt("DECIMAL_DIGITS") + " -- " + rs.getInt("NULLABLE") + " -- " + rs.getString("COLUMN_DEF") + " -- " +
						// rs.getString("IS_AUTOINCREMENT") + " -- " + rs.getString("REMARKS") + " -- " + rs.getInt("ORDINAL_POSITION"));
						// 需要比较的，名字为主，类型，长度，非空，无符号，自增，默认值，注释
						if (null != (fi = fis.remove(fieldName))) {
							// 因为已经存在，需要进行数据交验，是否与之前相同
							if (!fi.validate(rs.getString("TYPE_NAME").toUpperCase(), rs.getInt("COLUMN_SIZE"), rs.getInt("NULLABLE") == 1, "YES".equals(rs.getString("IS_AUTOINCREMENT")), rs.getInt("DECIMAL_DIGITS"), rs.getString("COLUMN_DEF"), rs.getString("REMARKS"), behindField)) {
								efs.put(fieldName, fi);
							}
							behindField = fieldName;
						} else {
							// 因为已经不存在，而需要移除，记录进列表中
							dropFields.add(fieldName);
						}
					}
					rs.close();
					if (fis.size() > 0) {
						// 此列表一定是有序的，不会有前置字段相关问题
						// 需要再被增加的字段
						for (final EntityInfoBean<E>.FieldInfoBean f : fis.values()) {
							isFirst = this.changeTrace(sb, f.getBeforeFieldName(), efs, isFirst);
							if (isFirst) {
								isFirst = false;
							} else {
								sb.append(',');
							}
							f.getAddAlter(sb);
						}
					}
					if (efs.size() > 0) {
						Iterator<EntityInfoBean<E>.FieldInfoBean> it = efs.values().iterator();
						EntityInfoBean<E>.FieldInfoBean f;
						int size = 0;
						// 有修改的字段
						while (it.hasNext()) {
							f = it.next();
							// 得到就移除
							it.remove();
							size = efs.size();
							isFirst = this.changeTrace(sb, f.getBeforeFieldName(), efs, isFirst);
							if (isFirst) {
								isFirst = false;
							} else {
								sb.append(',');
							}
							f.getChangeAlter(sb);
							if (size != efs.size()) {
								it = efs.values().iterator();
							}
						}
					}
				}
				final Map<String, EntityInfoBean<E>.IndexInfoBean> iis = this.eib.getIndexsCopy();
				EntityInfoBean<E>.IndexInfoBean priKey = null;
				{ // 索引的设置
					rs = dmd.getIndexInfo(dbName, null, tableName, false, false);
					// 直接从索引队列中移除，如果存在，并增加进“安全队列”
					// 如果不存在于队列，判定是否存在于“安全队列”，如果存在不管，如果不存在加入到要“移除队列（附带去重功能的）”
					// 如果是主键，如果不存在于索引队列，则直接进入“移除队列”
					// 如果是主键，存在于索引队列（第一次），比较字段数量（省略说明），比较同位置字段名，如果相同，设定位置为null，如果不同，直接加入到“移除队列”
					// 如果是主键，不存在于索引队列，判定，如果存在“移除队列”跳过，是否存在“安全队列”，存在，进行与以上相同判断，及相同操作，不存在于“安全队列”，增加进“移除队列”
					EntityInfoBean<E>.IndexInfoBean ii;
					// 安全队列
					final Set<String> sas = new HashSet<>();
					// 移除队列
					final Set<String> rms = new HashSet<>();
					String indexName;
					final String pkn = "PRIMARY";
					final EntityInfoBean<E>.FieldInfoBean[] pfs = this.eib.getPrimaryFieldsCopy();
					while (rs.next()) {
						// 仅比较名字
						// System.out.println(rs.getString("INDEX_NAME") + " -- " + rs.getString("COLUMN_NAME") + " -- " + rs.getBoolean("NON_UNIQUE") + " -- " + rs.getString("ASC_OR_DESC") + " -- " + rs.getShort("TYPE") + " -- " + rs.getInt("CARDINALITY") + "
						// -- " + rs.getShort("PAGES") + " -- " + rs.getShort("ORDINAL_POSITION"));
						indexName = rs.getString("INDEX_NAME").toUpperCase();
						if (null == (ii = iis.remove(indexName))) {
							if (!sas.contains(indexName)) {
								if (rms.add(indexName)) {
									// 需要被移除的
									if (isFirst) {
										isFirst = false;
									} else {
										sb.append(',');
									}
									AlterTypeEnum.Drop.getAlterIndex(sb, indexName, null);
								}
							} else if (indexName.equals(pkn)) {
								// 主键处理逻辑，与下重复
								final int ind = rs.getShort("ORDINAL_POSITION");
								if (ind > pfs.length) {
									if (rms.add(indexName)) {
										// 因为长度一定不同，直接移除
										if (isFirst) {
											isFirst = false;
										} else {
											sb.append(',');
										}
										AlterTypeEnum.Drop.getAlterIndex(sb, indexName, null);
									}
								} else {
									final String fn = rs.getString("COLUMN_NAME");
									if (fn.equals(pfs[ind - 1].getDataFieldName())) {
										// 与目标位置的
										pfs[ind - 1] = null;
									} else {
										if (rms.add(indexName)) {
											if (isFirst) {
												isFirst = false;
											} else {
												sb.append(',');
											}
											AlterTypeEnum.Drop.getAlterIndex(sb, indexName, null);
										}
									}
								}
							}
						} else if (indexName.equalsIgnoreCase(pkn)) {
							priKey = ii;
							if (rms.contains(pkn)) {
								// 已经是需要被移除状态，直接下一个
								continue;
							}
							if (!sas.contains(pkn)) {
								// 还未存在于安全中，则增加
								sas.add(pkn);
							}
							if (null == pfs) {
								// 因为新版本不存在主键了
								if (rms.add(pkn)) {
									if (isFirst) {
										isFirst = false;
									} else {
										sb.append(',');
									}
									AlterTypeEnum.Drop.getAlterIndex(sb, pkn, null);
								}
								continue;
							}
							// 设置注解目标位置为null
							final int ind = rs.getShort("ORDINAL_POSITION");
							if (ind > pfs.length) {
								// 因为长度一定不同，直接移除
								if (rms.add(indexName)) {
									if (isFirst) {
										isFirst = false;
									} else {
										sb.append(',');
									}
									AlterTypeEnum.Drop.getAlterIndex(sb, indexName, null);
								}
							} else {
								final String fn = rs.getString("COLUMN_NAME");
								if (fn.equals(pfs[ind - 1].getDataFieldName())) {
									// 与目标位置的
									pfs[ind - 1] = null;
								} else {
									if (rms.add(indexName)) {
										if (isFirst) {
											isFirst = false;
										} else {
											sb.append(',');
										}
										AlterTypeEnum.Drop.getAlterIndex(sb, indexName, null);
									}
								}
							}
						} else {
							sas.add(indexName);
						}
					}
					if (sas.contains(pkn)) {
						// 如果不存在主键相关，则进一步确认
						if (!rms.contains(pkn)) {
							for (final EntityInfoBean<E>.FieldInfoBean fi : pfs) {
								if (null != fi) {
									if (rms.add(pkn)) {
										// 主键不同，增加
										if (isFirst) {
											isFirst = false;
										} else {
											sb.append(',');
										}
										AlterTypeEnum.Drop.getAlterIndex(sb, pkn, null);
									}
									break;
								}
							}
							// 以为没有被移除，所以不需要增加
							priKey = null;
						}
					}
				}
				{ // 处理需要被移除的字段
					for (final String fn : dropFields) {
						if (isFirst) {
							isFirst = false;
						} else {
							sb.append(',');
						}
						AlterTypeEnum.Drop.getAlterField(sb, fn, null, null);
					}
				}
				{ // 处理需要被增加的索引
					for (final EntityInfoBean<E>.IndexInfoBean ii : iis.values()) {
						if (isFirst) {
							isFirst = false;
						} else {
							sb.append(',');
						}
						ii.getAddAlter(sb);
					}
					if (null != priKey) {
						if (isFirst) {
							isFirst = false;
						} else {
							sb.append(',');
						}
						priKey.getAddAlter(sb);
					}
				}
				if (isFirst) {
					// 因为没有任何数据修改
					back = 0;
				} else {
					sb.append(';');
					sql = sb.toString();
					// System.out.println("sql >> " + sql);
					ps = conn.prepareStatement(sql);
					ps.execute();
					back = 2;
				}
			} else {
				// 没有目标表，直接创建
				sql = this.eib.getCreateSQL(tableName);
				// System.out.println("sql >> " + sql);
				ps = conn.prepareStatement(sql);
				ps.execute();
				back = 1;
			}
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (null != rs) {
				rs.close();
			}
			if (null != ps) {
				ps.close();
			}
			if (null != conn) {
				conn.release();
			}
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				if (null == sql) {
					CoreLog.getInstance().debug(this.getClass(), "JDBCThread[", Thread.currentThread().getName(), "] run: ", Long.toString(System.currentTimeMillis() - l), "  table [", tableName, "] exist and nochange ... ");
				} else {
					CoreLog.getInstance().debug(this.getClass(), "JDBCThread[", Thread.currentThread().getName(), "] run: ", Long.toString(System.currentTimeMillis() - l), " create table: ", sql, " >", backS);
				}
			}
		}
		return back;
	}

	/**
	 * 修改追溯
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月5日 下午8:54:42
	 * @param sb 组合用字串，会直接将增加的内容，放入进去
	 * @param beforeName 前一个字段的字段名
	 * @param efs 修改字段列表
	 * @param isFirst 是否第一个
	 * @return isFirst-是否第一个，用来向下传递
	 */
	private boolean changeTrace(final StringBuilder sb, final String beforeName, final Map<String, EntityInfoBean<E>.FieldInfoBean> efs, boolean isFirst) {
		EntityInfoBean<E>.FieldInfoBean f;
		if (null != (f = efs.remove(beforeName))) {
			isFirst = this.changeTrace(sb, f.getBeforeFieldName(), efs, isFirst);
		} else {
			return isFirst;
		}
		// 如果存在，优先处理
		if (isFirst) {
			isFirst = false;
		} else {
			sb.append(',');
		}
		f.getChangeAlter(sb);
		return isFirst;
	}

	/**
	 * 清空默认表数据
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月21日_下午2:15:59
	 * @throws SQLException 抛
	 */
	public void truncateData() throws SQLException {
		this.truncateData(this.eib.getDefaultTableName());
	}

	/**
	 * 清空指定表数据
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月19日_上午9:33:49
	 * @param tableName 目标表名
	 * @throws SQLException 抛
	 */
	public void truncateData(final String tableName) throws SQLException {
		final long l = System.currentTimeMillis();
		ConnectionBean conn = null;
		PreparedStatement ps = null;
		final String sql = new StringBuilder(11 + this.eib.getDefaultTableName().length()).append("TRUNCATE `").append(tableName).append('`').toString();
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.execute();
			return;
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (null != ps) {
				ps.close();
			}
			if (null != conn) {
				conn.release();
			}
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), "JDBCThread[", Thread.currentThread().getName(), "] run: ", Long.toString(System.currentTimeMillis() - l), " truncate table: ", sql);
			}
		}
	}

	/**
	 * 插入一条数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 上午10:41:45
	 * @param ent 目标实体数据
	 * @return -1，插入失败；<br />
	 *         -3，在自增字段处理时出现问题<br />
	 *         0，未知问题，也许是没有自增ID的返回；<br />
	 *         >0，成功的值，并返回了自增ID<br />
	 * @throws SQLException 抛
	 */
	public int insertData(final E ent) throws SQLException {
		final long l = System.currentTimeMillis();
		ConnectionBean conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int back = -1;
		final String sql = (null == ent.getTableName() ? this.eib.getInsertSQL() : this.eib.getInsertSQL(ent.getTableName()));
		final StringBuilder log;
		if (CoreLog.getInstance().debugEnabled(this.getClass())) {
			log = new StringBuilder();
		} else {
			log = null;
		}
		try {
			conn = this.getConnection();
			// 需要有返回新增自增ID的值
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			if (null != log) {
				log.append('{');
			}
			this.eib.putInsertValue(ps, ent, log);
			if (null != log) {
				log.append('}');
			}
			ps.executeUpdate();
			if (this.eib.hasIncrement()) {
				// 存在自增字段，才继续
				rs = ps.getGeneratedKeys();
				if (rs.next()) {
					// 因为得到，所以反馈
					back = this.eib.backIncrement(ent, rs);
				} else {
					back = 0;
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (null != rs) {
				rs.close();
			}
			if (null != ps) {
				ps.close();
			}
			if (null != conn) {
				conn.release();
			}
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), "JDBCThread[", Thread.currentThread().getName(), "] run: ", Long.toString(System.currentTimeMillis() - l), " insert data: ", sql, " >", (log.length() > DaoBaseConstants.SQL_MAX_LENGTH ? log.substring(0, DaoBaseConstants.SQL_MAX_LENGTH) : log.toString()));
			}
		}
		return back;
	}

	/**
	 * 一次性插入数量的数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午7:29:03
	 * @param entList 实体数据列表
	 * @return 针对列表的结果
	 * @throws SQLException 抛
	 */
	public int[] insertDatas(final List<E> entList) throws SQLException {
		if ((null == entList) || (entList.size() == 0)) {
			return null;
		}
		return this.insertDatas(entList, entList.get(0).getTableName());
	}

	/**
	 * 一次性插入数量的数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午7:29:03
	 * @param entList 实体数据列表
	 * @param tableName 目标表名
	 * @return 针对列表的结果
	 * @throws SQLException 抛
	 */
	public int[] insertDatas(final List<E> entList, final String tableName) throws SQLException {
		final long l = System.currentTimeMillis();
		ConnectionBean conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int[] back = null;
		final String sql = (null == tableName ? this.eib.getInsertSQL() : this.eib.getInsertSQL(tableName));
		final StringBuilder log;
		if (CoreLog.getInstance().debugEnabled(this.getClass())) {
			log = new StringBuilder();
			log.append("[{");
		} else {
			log = null;
		}
		try {
			conn = this.getConnection();
			// 需要有返回新增自增ID的值
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			boolean isFirst = true;
			for (final E ent : entList) {
				if (isFirst) {
					isFirst = false;
				} else {
					if (null != log) {
						log.append("},{");
					}
				}
				this.eib.putInsertValue(ps, ent, log);
				ps.addBatch();
			}
			back = ps.executeBatch();
			if (this.eib.hasIncrement()) {
				rs = ps.getGeneratedKeys();
				int ind = 0;
				while (rs.next()) {
					// 因为得到，所以反馈
					if (back[ind] > 0) {
						back[ind] = this.eib.backIncrement(entList.get(ind), rs);
					}
					ind++;
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (null != rs) {
				rs.close();
			}
			if (null != ps) {
				ps.close();
			}
			if (null != conn) {
				conn.release();
			}
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				log.append("}]");
				CoreLog.getInstance().debug(this.getClass(), "JDBCThread[", Thread.currentThread().getName(), "] run: ", Long.toString(System.currentTimeMillis() - l), " inserts data: ", sql, " >", (log.length() > DaoBaseConstants.SQL_MAX_LENGTH ? log.substring(0, DaoBaseConstants.SQL_MAX_LENGTH) : log.toString()));
			}
		}
		return back;
	}

	/**
	 * 得到目标表下所有数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月16日 下午12:55:09
	 * @return 目标表下所有数据
	 * @throws SQLException 抛
	 */
	public List<E> selectAllData() throws SQLException {
		return this.selectData(null, this.eib.getDefaultTableName());
	}

	/**
	 * 得到目标表下所有数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月16日 下午12:55:45
	 * @param tableName 目标表名
	 * @return 目标表下所有数据
	 * @throws SQLException 抛
	 */
	public List<E> selectAllData(final String tableName) throws SQLException {
		return this.selectData(null, tableName);
	}

	/**
	 * 按条件查询得到目标数据<br />
	 * 结果为符合目标的数据列表<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午8:44:35
	 * @param qlb 数据查询条件
	 * @return 符合目标条件的数据列表
	 * @throws SQLException 抛
	 */
	public List<E> selectData(final QLBean qlb) throws SQLException {
		return this.selectData(qlb, qlb.getTableName());
	}

	/**
	 * 按条件查询得到目标数据<br />
	 * 结果为符合目标的数据列表<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午8:44:35
	 * @param qlb 数据查询条件
	 * @param tableName 目标表名称
	 * @return 符合目标条件的数据列表
	 * @throws SQLException 抛
	 */
	private List<E> selectData(final QLBean qlb, final String tableName) throws SQLException {
		final long l = System.currentTimeMillis();
		ConnectionBean conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// 准备设置相关查询条件
		final StringBuilder sql = this.eib.getSelectSQLHead(tableName);
		if (null != qlb) {
			final IndexCounter ic = new IndexCounter();
			qlb.assemblyTermSQL(sql, ic);
			qlb.assemblySortSQL(sql);
			qlb.assemblyPagerankSQL(sql);
		}
		final StringBuilder log;
		if (CoreLog.getInstance().debugEnabled(this.getClass())) {
			log = new StringBuilder();
			log.append('{');
		} else {
			log = null;
		}
		// System.out.println("sql >> " + sql.toString());
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(sql.toString());
			if (null != qlb) {
				qlb.setPSValue(ps, log);
			}
			rs = ps.executeQuery();
			return this.eib.getEntityAndWithData(rs);
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (null != rs) {
				rs.close();
			}
			if (null != ps) {
				ps.close();
			}
			if (null != conn) {
				conn.release();
			}
			if (null != qlb) {
				qlb.clearPsLocation();
			}
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				log.append('}');
				CoreLog.getInstance().debug(this.getClass(), "JDBCThread[", Thread.currentThread().getName(), "] run: ", Long.toString(System.currentTimeMillis() - l), " select data: ", sql.toString(), " >", (log.length() > DaoBaseConstants.SQL_MAX_LENGTH ? log.substring(0, DaoBaseConstants.SQL_MAX_LENGTH) : log.toString()));
			}
		}
	}

	/**
	 * 查询得到多条件数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月16日 下午1:44:22
	 * @param qlbs 条件列表
	 * @return 得到的数据集合
	 * @throws SQLException 抛
	 */
	public List<E> selectMultiData(final List<QLBean> qlbs) throws SQLException {
		final long l = System.currentTimeMillis();
		ConnectionBean conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		final List<E> result = new ArrayList<>(qlbs.size() * 10);
		int ts = 0;
		for (final QLBean qlb : qlbs) {
			ts++;
			// 准备设置相关查询条件
			final StringBuilder sql = this.eib.getSelectSQLHead(qlb.getTableName());
			if (null != qlb) {
				final IndexCounter ic = new IndexCounter();
				qlb.assemblyTermSQL(sql, ic);
				qlb.assemblySortSQL(sql);
				qlb.assemblyPagerankSQL(sql);
			}
			final StringBuilder log;
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				log = new StringBuilder();
				log.append('{');
			} else {
				log = null;
			}
			// System.out.println("sql >> " + sql.toString());
			try {
				conn = this.getConnection();
				ps = conn.prepareStatement(sql.toString());
				if (null != qlb) {
					qlb.setPSValue(ps, log);
				}
				rs = ps.executeQuery();
				result.addAll(this.eib.getEntityAndWithData(rs));
			} catch (final Exception e) {
				e.printStackTrace();
			} finally {
				if (null != rs) {
					rs.close();
				}
				if (null != ps) {
					ps.close();
				}
				if (null != conn) {
					conn.release();
				}
				if (null != qlb) {
					qlb.clearPsLocation();
				}
				if (CoreLog.getInstance().debugEnabled(this.getClass())) {
					log.append('}');
					CoreLog.getInstance().debug(this.getClass(), "JDBCThread[", Thread.currentThread().getName(), "] run(", Integer.toString(ts), "): ", Long.toString(System.currentTimeMillis() - l), " select data: ", sql.toString(), " >", (log.length() > DaoBaseConstants.SQL_MAX_LENGTH ? log.substring(0, DaoBaseConstants.SQL_MAX_LENGTH) : log.toString()));
				}
			}
		}
		return result;
	}

	/**
	 * 得到目标数据的总量
	 *
	 * @author tfzzh
	 * @dateTime 2016年10月11日 下午1:36:02
	 * @return 目标数据的总量
	 * @throws SQLException 抛
	 */
	public int selectDataCount() throws SQLException {
		return this.selectDataCount(null);
	}

	/**
	 * 按条件查询得到目标数据的总量
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午4:49:29
	 * @param qlb 数据查询条件
	 * @return 目标数据的总量
	 * @throws SQLException 抛
	 */
	public int selectDataCount(final QLBean qlb) throws SQLException {
		final long l = System.currentTimeMillis();
		ConnectionBean conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int back = -1;
		final StringBuilder sql;
		if (null != qlb) {
			sql = this.eib.getCountSQLHead(qlb.getTableName());
		} else {
			sql = this.eib.getCountSQLHead();
		}
		if (null != qlb) {
			final IndexCounter ic = new IndexCounter();
			qlb.assemblyTermSQL(sql, ic);
		}
		final StringBuilder log;
		if (CoreLog.getInstance().debugEnabled(this.getClass())) {
			log = new StringBuilder();
			log.append('{');
		} else {
			log = null;
		}
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(sql.toString());
			if (null != qlb) {
				qlb.setPSValue(ps, log);
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				back = rs.getInt(1);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (null != rs) {
				rs.close();
			}
			if (null != ps) {
				ps.close();
			}
			if (null != conn) {
				conn.release();
			}
			if (null != qlb) {
				qlb.clearPsLocation();
			}
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				log.append('}');
				CoreLog.getInstance().debug(this.getClass(), "JDBCThread[", Thread.currentThread().getName(), "] run: ", Long.toString(System.currentTimeMillis() - l), " select data: ", sql.toString(), " >", (log.length() > DaoBaseConstants.SQL_MAX_LENGTH ? log.substring(0, DaoBaseConstants.SQL_MAX_LENGTH) : log.toString()));
			}
		}
		// 准备设置相关查询条件
		// 首先组合查询用sql，
		return back;
	}

	/**
	 * 以整个数据实体信息为参考进行数据更新<br />
	 * 不能针对没有主键的表操作<br />
	 * 以主键字段作为条件，其他字段均为被更新项<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午1:13:11
	 * @param ent 数据实体
	 * @return 目标数据的总量，应该只有一条
	 * @throws SQLException 抛
	 */
	public int updateData(final E ent) throws SQLException {
		if (null == ent.getKeyValue()) {
			// 存在主键问题
			return -1;
		}
		return this.updateData(ent, ent.getTableName());
	}

	/**
	 * 以数量的整个数据实体信息为参考进行数据更新<br />
	 * 不能针对没有主键的表操作<br />
	 * 以主键字段作为条件，其他字段均为被更新项<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午7:43:26
	 * @param ents 数据实体列表
	 * @return 被更新的数据情况列表；<br />
	 *         null，操作失败；<br />
	 * @throws SQLException 抛
	 */
	public int[] updateDatas(final List<E> ents) throws SQLException {
		if ((null == ents) || (ents.size() == 0)) {
			return new int[0];
		}
		return this.updateDatas(ents, ents.get(0).getTableName());
	}

	/**
	 * 按条件更新目标数据内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 上午10:31:19
	 * @param qlb 数据查询条件
	 * @return 目标数据的总量
	 * @throws SQLException 抛
	 */
	public int updateData(final QLBean qlb) throws SQLException {
		final long l = System.currentTimeMillis();
		ConnectionBean conn = null;
		PreparedStatement ps = null;
		int back = -1;
		// 准备设置相关查询条件
		final StringBuilder sql = this.eib.getUpdateSQLHead(qlb.getTableName());
		final IndexCounter ic = new IndexCounter();
		qlb.assemblyUpdateSQL(sql, ic);
		qlb.assemblyTermSQL(sql, ic);
		final StringBuilder log;
		if (CoreLog.getInstance().debugEnabled(this.getClass())) {
			log = new StringBuilder();
			log.append('{');
		} else {
			log = null;
		}
		// System.out.println("sql >> " + sql.toString());
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(sql.toString());
			qlb.setPSValue(ps, log);
			back = ps.executeUpdate();
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (null != ps) {
				ps.close();
			}
			if (null != conn) {
				conn.release();
			}
			if (null != qlb) {
				qlb.clearPsLocation();
			}
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				log.append('}');
				CoreLog.getInstance().debug(this.getClass(), "JDBCThread[", Thread.currentThread().getName(), "] run: ", Long.toString(System.currentTimeMillis() - l), " update data: ", sql.toString(), " >", (log.length() > DaoBaseConstants.SQL_MAX_LENGTH ? log.substring(0, DaoBaseConstants.SQL_MAX_LENGTH) : log.toString()));
			}
		}
		return back;
	}

	/**
	 * 以整个数据实体信息为参考进行数据更新<br />
	 * 不能针对没有主键的表操作<br />
	 * 以主键字段作为条件，其他字段均为被更新项<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午1:13:11
	 * @param ent 数据实体
	 * @param tableName 目标表名称
	 * @return 目标数据的总量，应该只有一条
	 * @throws SQLException 抛
	 */
	public int updateData(final E ent, String tableName) throws SQLException {
		final long l = System.currentTimeMillis();
		ConnectionBean conn = null;
		PreparedStatement ps = null;
		int back = -1;
		if (null == tableName) {
			tableName = ent.getTableName();
		}
		final StringBuilder sql = new StringBuilder();
		// 准备设置相关查询条件
		sql.append(this.eib.getUpdateSQL(tableName));
		final StringBuilder log;
		if (CoreLog.getInstance().debugEnabled(this.getClass())) {
			log = new StringBuilder();
			log.append('{');
		} else {
			log = null;
		}
		// System.out.println("sql >> " + sql.toString());
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(sql.toString());
			this.eib.putUpdateValue(ps, ent, log);
			back = ps.executeUpdate();
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (null != ps) {
				ps.close();
			}
			if (null != conn) {
				conn.release();
			}
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				log.append('}');
				CoreLog.getInstance().debug(this.getClass(), "JDBCThread[", Thread.currentThread().getName(), "] run: ", Long.toString(System.currentTimeMillis() - l), " update data: ", sql.toString(), " >", (log.length() > DaoBaseConstants.SQL_MAX_LENGTH ? log.substring(0, DaoBaseConstants.SQL_MAX_LENGTH) : log.toString()));
			}
		}
		return back;
	}

	/**
	 * 以数量的整个数据实体信息为参考进行数据更新<br />
	 * 不能针对没有主键的表操作<br />
	 * 以主键字段作为条件，其他字段均为被更新项<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午7:43:26
	 * @param ents 数据实体列表
	 * @param tableName 目标表名称
	 * @return 被更新的数据情况列表；<br />
	 *         null，操作失败；<br />
	 * @throws SQLException 抛
	 */
	public int[] updateDatas(final List<E> ents, String tableName) throws SQLException {
		if ((null == ents) || (ents.size() == 0)) {
			return new int[0];
		}
		final long l = System.currentTimeMillis();
		ConnectionBean conn = null;
		PreparedStatement ps = null;
		int[] back = null;
		final StringBuilder sql = new StringBuilder();
		if (null == tableName) {
			tableName = ents.get(0).getTableName();
		}
		// 准备设置相关查询条件
		sql.append(this.eib.getUpdateSQL(tableName));
		final StringBuilder log;
		if (CoreLog.getInstance().debugEnabled(this.getClass())) {
			log = new StringBuilder();
			log.append("[{");
		} else {
			log = null;
		}
		// System.out.println("sql >> " + sql.toString());
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(sql.toString());
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				boolean isFirst = true;
				for (final E e : ents) {
					if (null != log) {
						if (isFirst) {
							isFirst = false;
						} else {
							log.append("},{");
						}
					}
					this.eib.putUpdateValue(ps, e, log);
					ps.addBatch();
				}
			} else {
				for (final E e : ents) {
					this.eib.putUpdateValue(ps, e, log);
					ps.addBatch();
				}
			}
			back = ps.executeBatch();
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (null != ps) {
				ps.close();
			}
			if (null != conn) {
				conn.release();
			}
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				log.append("}]");
				CoreLog.getInstance().debug(this.getClass(), "JDBCThread[", Thread.currentThread().getName(), "] run: ", Long.toString(System.currentTimeMillis() - l), " update data: ", sql.toString(), " >", (log.length() > DaoBaseConstants.SQL_MAX_LENGTH ? log.substring(0, DaoBaseConstants.SQL_MAX_LENGTH) : log.toString()));
			}
		}
		return back;
	}

	/**
	 * 按条件删除目标数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 上午9:43:04
	 * @param qlb 数据查询条件
	 * @return 被删除数据的总量
	 * @throws SQLException 抛
	 */
	public int deleteData(final QLBean qlb) throws SQLException {
		final long l = System.currentTimeMillis();
		ConnectionBean conn = null;
		PreparedStatement ps = null;
		int back = -1;
		final StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM `").append(qlb.getTableName()).append('`');
		final IndexCounter ic = new IndexCounter();
		qlb.assemblyTermSQL(sql, ic);
		final StringBuilder log;
		if (CoreLog.getInstance().debugEnabled(this.getClass())) {
			log = new StringBuilder();
			log.append('{');
		} else {
			log = null;
		}
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(sql.toString());
			qlb.setPSValue(ps, log);
			back = ps.executeUpdate();
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (null != ps) {
				ps.close();
			}
			if (null != conn) {
				conn.release();
			}
			if (null != qlb) {
				qlb.clearPsLocation();
			}
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				log.append('}');
				CoreLog.getInstance().debug(this.getClass(), "JDBCThread[", Thread.currentThread().getName(), "] run: ", Long.toString(System.currentTimeMillis() - l), " delete data: ", sql.toString(), " >", (log.length() > DaoBaseConstants.SQL_MAX_LENGTH ? log.substring(0, DaoBaseConstants.SQL_MAX_LENGTH) : log.toString()));
			}
		}
		// 准备设置相关查询条件
		// 首先组合查询用sql，
		return back;
	}

	/**
	 * 插入及更新，先进行更新操作，如果失败，进行插入操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午1:09:55
	 * @param ent 目标实体数据
	 * @return 1，数据插入成功；<br />
	 *         2，数据更新成功；<br />
	 *         -1，插入与更新均失败；<br />
	 * @throws SQLException 抛
	 */
	public int insertOrUpdate(final E ent) throws SQLException {
		if (this.updateData(ent) > 0) {
			// 进行更新操作
			return 2;
		} else if (this.insertData(ent) > 0) {
			// 因更新失败，而需要插入数据
			return 1;
		} else {
			// 因为以上都失败
			return -1;
		}
	}

	/**
	 * 批量插入及更新整个对象的数据，先进行批量更新操作，如果失败，进行批量插入操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午8:35:12
	 * @param ents 目标实体数据列表
	 * @return 批量处理的结果：<br />
	 *         负数，表示更新成功，并被更新的数据数量，一般为1；<br />
	 *         整数，表示插入成功，为插入后自增列的值；<br />
	 *         0，一般是插入成功，但该数据没有自增列；<br />
	 * @throws SQLException 抛
	 */
	public int[] insertOrUpdates(final List<E> ents) throws SQLException {
		final int[] backs = this.updateDatas(ents);
		final List<E> ints = new ArrayList<>(ents.size() / 2);
		for (int i = 0; i < backs.length; i++) {
			if (backs[i] == 0) {
				// 更新未成功的
				ints.add(ents.get(i));
			}
		}
		final int[] backInts = this.insertDatas(ints);
		int ind = 0;
		for (int i = 0; i < backs.length; i++) {
			if (backs[i] == 0) {
				backs[i] = backInts[ind++];
			} else {
				backs[i] = -backs[i];
			}
		}
		return backs;
	}

	/**
	 * 清除表数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午8:51:37
	 * @return 结果
	 * @throws SQLException 抛
	 */
	public boolean clearData() throws SQLException {
		return this.clearData(this.eib.getDefaultTableName());
	}

	/**
	 * 清除表数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午8:51:39
	 * @param tableName 目标表名
	 * @return 结果
	 * @throws SQLException 抛
	 */
	public boolean clearData(final String tableName) throws SQLException {
		final long l = System.currentTimeMillis();
		final StringBuilder sql = new StringBuilder("truncate table ").append(tableName);
		boolean result = false;
		ConnectionBean conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(sql.toString());
			result = ps.execute();
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), "JDBCThread[", Thread.currentThread().getName(), "] run: ", Long.toString(System.currentTimeMillis() - l), " truncate: ", sql.toString(), " >", Boolean.toString(result));
			}
		}
		return result;
	}
}
