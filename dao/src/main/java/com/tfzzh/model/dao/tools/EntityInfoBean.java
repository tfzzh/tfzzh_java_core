/**
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午12:53:48
 */
package com.tfzzh.model.dao.tools;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoIterable;
import com.tfzzh.exception.ConfigurationException;
import com.tfzzh.exception.NotAvailableOperationModeException;
import com.tfzzh.model.bean.BaseDataBean;
import com.tfzzh.model.bean.BaseEntityBean;
import com.tfzzh.model.bean.BaseMongoBean;
import com.tfzzh.model.bean.BaseSystemEntityBean;
import com.tfzzh.model.bean.BaseUserEntityBean;
import com.tfzzh.model.dao.AutomaticDataTableStructureDAO;
import com.tfzzh.model.dao.annotation.DataField;
import com.tfzzh.model.dao.annotation.DataTable;
import com.tfzzh.model.dao.annotation.IdField;
import com.tfzzh.model.dao.annotation.KeyIndex;
import com.tfzzh.model.dao.annotation.KeyIndexs;
import com.tfzzh.model.dao.tools.QLLocation.FieldLocation;
import com.tfzzh.model.exception.AnnotationException;
import com.tfzzh.model.exception.FieldNullException;
import com.tfzzh.model.exception.IndexConfigException;
import com.tfzzh.model.tools.DaoConstants;
import com.tfzzh.model.tools.DatabaseFactroy;
import com.tfzzh.tools.DigitalTools;
import com.tfzzh.tools.TfzzhUUID;

/**
 * 实体信息对象
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午12:53:48
 * @param <E> 数据相关实体
 */
public class EntityInfoBean<E extends BaseDataBean> {

	/**
	 * 实体对象类
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午1:14:17
	 */
	private final Class<E> entClz;

	/**
	 * 类对象ID
	 * 
	 * @author tfzzh
	 * @dateTime 2016年9月1日 下午2:03:09
	 */
	private Long classId;

	/**
	 * 所相关的DAO实现
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月18日_下午1:06:28
	 */
	private final AutomaticDataTableStructureDAO<E> adts;

	/**
	 * 所相关的父类对象<br />
	 * null，不存在相关父类对象<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午1:15:13
	 */
	private final EntityInfoBean<? extends BaseDataBean> parent;

	/**
	 * 所相关属性对象列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午1:14:45
	 */
	private final Map<String, FieldInfoBean> fields;

	/**
	 * 表字段名为key的属性对象列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月30日 下午2:24:39
	 */
	private final Map<String, FieldInfoBean> tpKeyFileds;

	/**
	 * 字段顺位列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月29日 下午7:50:03
	 */
	private final FieldInfoBean[] fibs;

	/**
	 * 自增字段
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月5日 下午7:07:46
	 */
	private FieldInfoBean incrementField = null;

	/**
	 * UUID字段
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午1:39:00
	 */
	private FieldInfoBean uuidField = null;

	/**
	 * 相关索引信息列表<br />
	 * <数据库用索引名,索引对象><br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 下午12:54:23
	 */
	private final Map<String, IndexInfoBean> indexs;

	/**
	 * 主键信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月4日 下午2:22:01
	 */
	private IndexInfoBean primary = null;

	/**
	 * 表名称
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 下午5:11:35
	 */
	private final String tableName;

	/**
	 * 表说明
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 下午5:09:01
	 */
	private final String desc;

	/**
	 * 创建表用SQL语句，后部分，需要补表名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 上午10:51:56
	 */
	private String createSQL = null;

	/**
	 * 插入数据用SQL语句，后部分，需要补表名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 上午10:56:05
	 */
	private String insertSQL = null;

	/**
	 * 通用更新数据用SQL语句，后部分，需要补表名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午2:35:19
	 */
	private String updateSQL = null;

	/**
	 * 查询数据用SQL语句，前部分，需要补表名及条件
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午8:53:31
	 */
	private final String selectSQL;

	/**
	 * 所相关数据库类型
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月23日 下午5:03:18
	 */
	private final DatabaseTypeEnum databaseType;

	/**
	 * @author Xu Weijie
	 * @datetime 2017年8月2日_下午6:06:46
	 */
	private final static Map<String, Object> VALIDATED_TABLE = new HashMap<>();

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午1:14:39
	 * @param clz 目标实体对象类
	 * @param adts 自动处理表结构所相关的DAO
	 */
	public EntityInfoBean(final Class<E> clz, final AutomaticDataTableStructureDAO<E> adts) {
		final DataTable dt = clz.getAnnotation(DataTable.class);
		this.databaseType = this.analyseDatabaseType(clz);
		if (null == dt) {
			this.classId = null;
			this.tableName = null;
			this.entClz = null;
			this.parent = null;
			this.fields = null;
			this.indexs = null;
			this.desc = null;
			throw new AnnotationException(clz.getSimpleName(), "DataTable");
		}
		try {
			final Field tf = clz.getField("OBJECT_ID");
			this.classId = (Long) tf.get(clz);
		} catch (final NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		this.tableName = dt.tableName();
		this.entClz = clz;
		this.adts = adts;
		this.parent = this.initParent(clz, adts);
		this.fields = this.analysisFields(clz);
		// 进行排序
		this.fibs = this.fieldSort();
		// 一定在排序之后
		this.tpKeyFileds = this.getTpFields();
		this.indexs = this.analysisIndexs(clz);
		this.desc = dt.desc();
		this.selectSQL = this.initSelectSQL();
	}

	/**
	 * 解析数据库类型
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月23日 下午5:05:01
	 * @param clz 目标实体对象类
	 * @return 数据库类型
	 */
	private DatabaseTypeEnum analyseDatabaseType(final Class<E> clz) {
		if (BaseEntityBean.class.isAssignableFrom(clz)) {
			return DatabaseTypeEnum.SQL;
		} else if (BaseMongoBean.class.isAssignableFrom(clz)) {
			return DatabaseTypeEnum.Mongo;
		} else {
			return DatabaseTypeEnum.SQL;
		}
	}

	/**
	 * 得到所从属的父类对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午1:29:07
	 * @param clz 目标clz对象
	 * @param adts 自动处理表结构所相关的DAO
	 * @return clz所从属的父类，自建对象级别，不相关
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private EntityInfoBean<? extends BaseDataBean> initParent(final Class<E> clz, final AutomaticDataTableStructureDAO adts) {
		final Class<?> supClz = clz.getSuperclass();
		if (null == supClz) {
			return null;
		}
		final boolean isAbs = Modifier.isAbstract(supClz.getModifiers());
		if (isAbs) {
			// 如果是抽象类，不做处理
			return null;
		}
		if (supClz.isAssignableFrom(BaseEntityBean.class) || supClz.isAssignableFrom(BaseMongoBean.class) || supClz.isAssignableFrom(BaseSystemEntityBean.class) || supClz.isAssignableFrom(BaseUserEntityBean.class)) {
			// 不需要再向上处理
			return null;
		}
		return new EntityInfoBean(supClz, adts);
	}

	/**
	 * 解析字段数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午1:43:32
	 * @param clz 目标clz对象
	 * @return 被处理过的字段信息对象列表
	 */
	private Map<String, FieldInfoBean> analysisFields(final Class<E> clz) {
		final Field[] fs = clz.getDeclaredFields();
		final Map<String, FieldInfoBean> fm = new HashMap<>(fs.length / 2);
		DataField df;
		for (final Field f : fs) {
			if (null != (df = f.getAnnotation(DataField.class))) {
				// 存在相关注解，是需要的字段
				fm.put(f.getName(), new FieldInfoBean(this.entClz, f, df));
			}
		}
		return fm;
	}

	/**
	 * 设置表字段名称为Key的属性对象列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月30日 下午2:32:42
	 * @return 表字段名称为Key的属性对象列表
	 */
	private Map<String, FieldInfoBean> getTpFields() {
		final Map<String, FieldInfoBean> tfm = new LinkedHashMap<>(this.fields.size());
		for (final FieldInfoBean fib : this.fibs) {
			tfm.put(fib.df.fieldName(), fib);
		}
		return tfm;
	}

	/**
	 * 解析字段相关索引数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 下午12:55:22
	 * @param clz 目标clz对象
	 * @return 被处理过的索引信息对象列表
	 */
	private Map<String, IndexInfoBean> analysisIndexs(final Class<E> clz) {
		final Field[] fs = clz.getDeclaredFields();
		final Map<String, IndexInfoBean> im = new HashMap<>(fs.length / 2);
		KeyIndexs kis;
		KeyIndex ki;
		// 将字段逐一放入到索引
		for (final Field f : fs) {
			if (null != (kis = f.getAnnotation(KeyIndexs.class))) {
				for (final KeyIndex k : kis.value()) {
					this.putIndexTerm(f, k, im);
				}
			} else if (null != (ki = f.getAnnotation(KeyIndex.class))) {
				this.putIndexTerm(f, ki, im);
			}
		}
		if (im.size() == 0) {
			return null;
		}
		// 设置最终用来对外调用的列表
		final Map<String, IndexInfoBean> kiim = new LinkedHashMap<>(fs.length / 2);
		// 验证是否有索引，存在空位置
		for (final IndexInfoBean i : im.values()) {
			i.validate();
			kiim.put(i.indexName, i);
		}
		return kiim;
	}

	/**
	 * 放入索引项
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月29日 下午3:54:46
	 * @param f 所相关字段
	 * @param ki 索引的数据属性
	 * @param im 索引集合列表
	 */
	private void putIndexTerm(final Field f, final KeyIndex ki, final Map<String, IndexInfoBean> im) {
		// 存在相关注解，是需要的字段
		// 首先处理名字
		String keyName;
		if (ki.value() == KeyIndexEnum.Primary) {
			// 是主键
			keyName = KeyIndexEnum.Primary.name();
			// 变更字段的是否主键状态
			this.fields.get(f.getName()).isPrimary = true;
		} else {
			if (ki.name().length() == 0) {
				// 因为不存在，而得到字段名
				keyName = f.getName();
			} else {
				keyName = ki.name();
			}
		}
		// 得到索引信息
		IndexInfoBean iib = im.get(keyName);
		if (null == iib) {
			iib = new IndexInfoBean(keyName, this.newArrayByClass(ki.count()), ki.value(), ki.sort());
			im.put(keyName, iib);
		}
		iib.putField(this.getField(f.getName()), ki.sort(), ki.index() - 1);
	}

	/**
	 * 得到类对象ID
	 * 
	 * @author tfzzh
	 * @dateTime 2016年9月1日 下午2:10:05
	 * @return the classId
	 */
	public Long getClassId() {
		return this.classId;
	}

	/**
	 * 得到用于自动处理表结构的DAO
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月18日_下午1:13:45
	 * @return 自动处理表结构的DAO
	 */
	public AutomaticDataTableStructureDAO<E> getAutomaticStructureDao() {
		return this.adts;
	}

	/**
	 * 得到默认的表名称
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午6:39:12
	 * @return 表名称
	 */
	public String getDefaultTableName() {
		return this.tableName;
	}

	/**
	 * 得到默认的sql用表名，带有符号“`”
	 * 
	 * @author tfzzh
	 * @dateTime 2016年10月27日 下午1:29:25
	 * @return 表名称，带有符号“`”
	 */
	public String getDefaultSQLTableName() {
		return '`' + this.tableName + '`';
	}

	/**
	 * 得到所相关的父类对象。null，不存在相关父类对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午9:00:13
	 * @return the parent
	 */
	public EntityInfoBean<? extends BaseDataBean> getParent() {
		return this.parent;
	}

	/**
	 * 得到目标字段对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午12:49:42
	 * @param fieldName 字段名
	 * @return 字段对象
	 */
	public FieldInfoBean getField(final String fieldName) {
		return this.fields.get(fieldName);
	}

	/**
	 * 得到所相关属性对象列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午9:00:13
	 * @return the fileds
	 */
	public Map<String, FieldInfoBean> getFields() {
		return this.fields;
	}

	/**
	 * 得到所相关属性对象列表的拷贝
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月29日 下午5:31:10
	 * @return 属性对象列表的拷贝
	 */
	public Map<String, FieldInfoBean> getTpFiledsCopy() {
		return new LinkedHashMap<>(this.tpKeyFileds);
	}

	/**
	 * 得到所相关索引对象列表的拷贝
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月29日 下午8:56:47
	 * @return 索引对象列表的拷贝
	 */
	public Map<String, IndexInfoBean> getIndexsCopy() {
		if (null == this.indexs) {
			return new LinkedHashMap<>(0);
		} else {
			return new LinkedHashMap<>(this.indexs);
		}
	}

	/**
	 * 得到主键的Copy对象
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月11日_上午11:35:49
	 * @return 主键的Copy对象
	 */
	public FieldInfoBean[] getPrimaryFieldsCopy() {
		return null == this.primary ? null : this.primary.fs.clone();
	}

	/**
	 * 得到所相关数据库类型
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月28日 下午1:22:58
	 * @return the databaseType
	 */
	public DatabaseTypeEnum getDatabaseType() {
		return this.databaseType;
	}

	/**
	 * 得到创建表用sql语句
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 下午7:31:18
	 * @return 创建表用sql语句
	 */
	public String getCreateSQL() {
		return this.getCreateSQL(this.tableName);
	}

	/**
	 * 得到创建表用sql语句
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 下午7:36:52
	 * @param tableName 目标表名
	 * @return 创建表用sql语句
	 */
	public String getCreateSQL(final String tableName) {
		if (null == this.createSQL) {
			final StringBuilder sb = new StringBuilder();
			sb.append('(');
			boolean isFirst = true;
			// 首先组合字段
			for (final FieldInfoBean fib : this.fibs) {
				if (isFirst) {
					isFirst = false;
				} else {
					sb.append(',');
				}
				sb.append(fib.sqlField).append(' ').append(fib.baseAlter);
			}
			if (null != this.indexs) {
				sb.append(',');
				isFirst = true;
				// 然后组合索引
				for (final IndexInfoBean iib : this.indexs.values()) {
					if (isFirst) {
						isFirst = false;
					} else {
						sb.append(',');
					}
					sb.append(iib.alter);
				}
			}
			// 最后的组合
			sb.append(")ENGINE='MyISAM' AUTO_INCREMENT=0 DEFAULT CHARSET='").append(DaoConstants.DATABASE_CODE.replace("-", "").replace("_", "")).append("' COMMENT='").append(this.desc).append("';");
			this.createSQL = sb.toString();
		}
		final StringBuilder sb = new StringBuilder(this.createSQL.length() + 16 + tableName.length());
		sb.append("CREATE TABLE `").append(tableName).append("` ");
		sb.append(this.createSQL);
		return sb.toString();
	}

	/**
	 * 得到插入数据用SQL，使用默认表名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 上午11:01:47
	 * @return 插入数据用sql语句
	 */
	public String getInsertSQL() {
		return this.getInsertSQL(this.tableName);
	}

	/**
	 * 得到插入数据用SQL
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 上午11:02:20
	 * @param tableName 目标表名
	 * @return 插入数据用sql语句
	 */
	public String getInsertSQL(final String tableName) {
		this.validateTabel(tableName);
		if (null == this.insertSQL) {
			final StringBuilder sb = new StringBuilder((this.fibs.length * 10) + 13);
			sb.append('(');
			boolean isFirst = true;
			// 首先组合字段
			for (final FieldInfoBean fib : this.fibs) {
				if (this.incrementField != fib) {
					if (isFirst) {
						isFirst = false;
					} else {
						sb.append(',');
					}
					sb.append(fib.sqlField);
				}
			}
			sb.append(") VALUES (");
			isFirst = true;
			for (int i = null == this.incrementField ? this.fibs.length : this.fibs.length - 1; i > 0; i--) {
				if (isFirst) {
					isFirst = false;
				} else {
					sb.append(',');
				}
				sb.append('?');
			}
			sb.append(");");
			this.insertSQL = sb.toString();
		}
		final StringBuilder sb = new StringBuilder(this.insertSQL.length() + 15 + tableName.length());
		sb.append("INSERT INTO `").append(tableName).append("` ");
		sb.append(this.insertSQL);
		return sb.toString();
	}

	/**
	 * 得到根据完整对象更新数据用SQL
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午4:04:04
	 * @return 更新数据用sql语句
	 */
	public String getUpdateSQL() {
		return this.getUpdateSQL(this.tableName);
	}

	/**
	 * 得到根据完整对象更新数据用SQL
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午4:03:53
	 * @param tableName 表名称
	 * @return 更新数据用sql语句
	 */
	public String getUpdateSQL(final String tableName) {
		this.validateTabel(tableName);
		if (null == this.updateSQL) {
			if (this.primary == null) {
				this.updateSQL = "";
			} else {
				final StringBuilder sb = new StringBuilder((this.fibs.length * 10) + 13);
				sb.append(" SET ");
				boolean isFirst = true;
				// 首先组合字段
				for (final FieldInfoBean fib : this.fibs) {
					if (this.incrementField != fib) {
						if (!fib.isPrimary) {
							// 不是自增，也不是主键
							if (isFirst) {
								isFirst = false;
							} else {
								sb.append(',');
							}
							sb.append(fib.sqlField).append("=?");
						}
					}
				}
				sb.append(" WHERE ");
				isFirst = true;
				for (final FieldInfoBean fib : this.primary.fs) {
					if (isFirst) {
						isFirst = false;
					} else {
						sb.append(" AND ");
					}
					sb.append(fib.sqlField).append("=?");
				}
				sb.append(';');
				this.updateSQL = sb.toString();
			}
		}
		if (this.updateSQL.length() == 0) {
			throw new NotAvailableOperationModeException("The Entity[" + this.entClz.getName() + "] havnnt Primary Key, Cannt use Entity Update...");
		}
		final StringBuilder sb = new StringBuilder(this.updateSQL.length() + 10 + tableName.length());
		sb.append("UPDATE `").append(tableName).append("` ");
		sb.append(this.updateSQL);
		return sb.toString();
	}

	/**
	 * 得到更新数据用SQL头部分
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月8日_下午1:52:39
	 * @param tableName 目标表名
	 * @return 更新数据用sql语句
	 */
	public StringBuilder getUpdateSQLHead(final String tableName) {
		this.validateTabel(tableName);
		return new StringBuilder().append("UPDATE `").append(tableName).append('`');
	}

	/**
	 * 得到查询用SQL，前部分
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午8:55:25
	 * @return 查询用SQL，前部分
	 */
	private String initSelectSQL() {
		final StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		boolean isFirst = true;
		for (final FieldInfoBean fib : this.fibs) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(',');
			}
			sb.append(fib.sqlField);
		}
		sb.append(" FROM ");
		return sb.toString();
	}

	/**
	 * 得到查询数据用SQL头部分
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午9:02:27
	 * @return 查询数据用sql语句
	 */
	public StringBuilder getSelectSQLHead() {
		return this.getSelectSQLHead(this.tableName);
	}

	/**
	 * 得到查询数据用SQL头部分
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午9:02:25
	 * @param tableName 目标表名
	 * @return 查询数据用sql语句
	 */
	public StringBuilder getSelectSQLHead(final String tableName) {
		this.validateTabel(tableName);
		return new StringBuilder(this.selectSQL.length() + tableName.length() + 2).append(this.selectSQL).append('`').append(tableName).append('`');
	}

	/**
	 * 得到查询数据数量用SQL头部分
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月8日_下午1:21:10
	 * @return 查询数据数量用SQL头部分
	 */
	public StringBuilder getCountSQLHead() {
		return this.getCountSQLHead(this.tableName);
	}

	/**
	 * 得到查询数据数量用SQL头部分
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月8日_下午1:21:11
	 * @param tableName 目标表名
	 * @return 查询数据用sql语句
	 */
	public StringBuilder getCountSQLHead(final String tableName) {
		this.validateTabel(tableName);
		return new StringBuilder().append("SELECT COUNT(0) FROM `").append(tableName).append('`');
	}

	/**
	 * 新增数据时设置目标值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午12:48:53
	 * @param ps 声明对象
	 * @param ent 数据实体
	 * @param log 日志记录
	 */
	public void putInsertValue(final PreparedStatement ps, final E ent, final StringBuilder log) {
		int ind = 1;
		boolean isFirst = true;
		for (final FieldInfoBean fib : this.fibs) {
			if (this.incrementField != fib) {
				if (isFirst) {
					isFirst = false;
				} else {
					if (null != log) {
						log.append(',');
					}
				}
				if (this.uuidField == fib) {
					fib.putUuid(ps, ent, ind++, log);
				} else {
					fib.putValue(ps, ent, ind++, log);
				}
			}
		}
	}

	/**
	 * 更新数据时，设置目标值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午4:24:55
	 * @param ps 声明对象
	 * @param ent 数据实体
	 * @param log 日志记录
	 */
	public void putUpdateValue(final PreparedStatement ps, final E ent, final StringBuilder log) {
		int ind = 1;
		boolean isFirst = true;
		// 更新内容部分
		for (final FieldInfoBean fib : this.fibs) {
			if (this.incrementField != fib) {
				if (!fib.isPrimary) {
					if (null != log) {
						if (isFirst) {
							isFirst = false;
						} else {
							log.append(',');
						}
					}
					fib.putValue(ps, ent, ind++, log);
				}
			}
		}
		// 主键条件部分
		for (final FieldInfoBean fib : this.primary.fs) {
			if (null != log) {
				if (!isFirst) {
					log.append(',');
				}
			}
			fib.putValue(ps, ent, ind++, log);
		}
	}

	/**
	 * 是否存在自增字段
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午7:59:35
	 * @return true，存在自增字段；<br />
	 *         false，不存在自增字段；<br />
	 */
	public boolean hasIncrement() {
		return null != this.incrementField;
	}

	/**
	 * 得到自增字段的数据库用名
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月5日 下午4:55:08
	 * @return 自增字段的数据库用名
	 */
	public String getIncrementDataFieldName() {
		if (this.hasIncrement()) {
			return this.incrementField.getDataFieldName();
		} else {
			return null;
		}
	}

	/**
	 * 返回自增数据数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午6:53:07
	 * @param ent 目标数据对象
	 * @param rs 之前反馈的数据结果
	 * @return 自增的值，如果是long也是int，不过保证了方向
	 */
	public int backIncrement(final E ent, final ResultSet rs) {
		if (null == this.incrementField) {
			throw new NotAvailableOperationModeException("The Entity[" + this.entClz.getName() + "] havnt Increment Field...");
		}
		try {
			if (ent.getIncrementKeyType() == Integer.class) {
				final Integer i = rs.getInt(1);
				this.incrementField.setM.invoke(ent, i);
				return i;
			} else if (ent.getIncrementKeyType() == Long.class) {
				final Long l = rs.getLong(1);
				this.incrementField.setM.invoke(ent, l);
				return DigitalTools.longToint(l);
			}
			return 0;
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return -3;
	}

	/**
	 * 放入自增字段，更多针对mongo的设定
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月7日 上午10:17:01
	 * @param ent 数据实例
	 * @param incId 数据的自增字段值
	 */
	public void putIncrement(final E ent, final long incId) {
		try {
			if (ent.getIncrementKeyType() == Integer.class) {
				this.incrementField.setM.invoke(ent, Integer.valueOf((int) incId));
			} else if (ent.getIncrementKeyType() == int.class) {
				this.incrementField.setM.invoke(ent, (int) incId);
			} else if (ent.getIncrementKeyType() == Long.class) {
				this.incrementField.setM.invoke(ent, Long.valueOf(incId));
			} else if (ent.getIncrementKeyType() == long.class) {
				this.incrementField.setM.invoke(ent, incId);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建字段信息数组
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 下午2:55:21
	 * @param length 目标数组长度
	 * @return 被创建的字段信息数组
	 */
	@SuppressWarnings("unchecked")
	public FieldInfoBean[] newArrayByClass(final int length) {
		return (FieldInfoBean[]) Array.newInstance(FieldInfoBean.class, length);
	}

	/**
	 * 得到实体对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月9日 下午3:22:29
	 * @return 实体对象
	 */
	public E getEntityInstance() {
		try {
			return this.entClz.getDeclaredConstructor().newInstance();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到实体并包含其中的数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月9日 下午3:31:35
	 * @param rs 数据查询的结果集
	 * @return 分析出来的数据结果集
	 */
	public List<E> getEntityAndWithData(final ResultSet rs) {
		try {
			// 第一次next
			if (rs.next()) {
				// 字段在当前rs中的顺位映射关系
				final int[] fi = (int[]) Array.newInstance(int.class, this.fields.size());
				// 数据列表
				final List<E> le = new ArrayList<>();
				int ind = 0;
				// 处理rs的顺位映射
				for (final FieldInfoBean fib : this.fibs) {
					fi[ind++] = rs.findColumn(fib.getDataFieldName());
				}
				try {
					E e;
					do {
						e = this.getEntityInstance();
						le.add(e);
						e.putResultData(rs, fi);
					} while (rs.next());
				} catch (final IllegalArgumentException e1) {
					e1.printStackTrace();
				}
				return le;
			} else {
				// 因为不存在数据
				return new ArrayList<>(0);
			}
		} catch (final SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 放入数据到对象
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月7日 上午10:47:01
	 * @param dm 数据列表
	 * @return 数据实例列表
	 */
	public List<E> getEntityAndWithData(final MongoIterable<Document> dm) {
		final List<E> le = new ArrayList<>();
		final Iterator<Document> di = dm.iterator();
		Document d;
		E e;
		while (di.hasNext()) {
			d = di.next();
			e = this.getEntityInstance();
			e.putDocumentData(d);
			le.add(e);
		}
		return le;
	}

	/**
	 * 放入数据到对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月8日 下午5:04:39
	 * @param dl 数据列表
	 * @return 数据实例列表
	 */
	public List<E> getEntityAndWithData(final List<Document> dl) {
		final List<E> le = new ArrayList<>();
		E e;
		for (final Document d : dl) {
			e = this.getEntityInstance();
			e.putDocumentData(d);
			le.add(e);
		}
		return le;
	}

	/**
	 * 字段排序
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月29日 下午7:51:32
	 * @return 被排序的结果
	 */
	private FieldInfoBean[] fieldSort() {
		final FieldInfoBean[] fibs = this.newArrayByClass(this.fields.size());
		for (final FieldInfoBean fib : this.fields.values()) {
			try {
				if (null != fibs[fib.df.index() - 1]) {
					throw new IndexConfigException(EntityInfoBean.this.entClz.getSimpleName(), fib.field.getName(), fibs[fib.df.index() - 1].field.getName(), fib.df.index());
				}
			} catch (final IndexOutOfBoundsException e) {
				// 仅过滤掉越界异常
				e.printStackTrace();
			}
			fibs[fib.df.index() - 1] = fib;
		}
		FieldInfoBean before = null;
		for (final FieldInfoBean fib : fibs) {
			try {
				fib.before = before;
				before = fib;
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return fibs;
	}

	/**
	 * 验证表是否存在<br />
	 * 如果不存在则创建<br />
	 * 如果存在则更新结构<br />
	 * 如果已经验证过则不处理<br />
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月2日_下午6:36:18
	 * @param tableName 待验证表名
	 */
	protected void validateTabel(final String tableName) {
		Object obj = EntityInfoBean.VALIDATED_TABLE.get(tableName);
		if (null != obj) {
			// 因为存在不再处理
			return;
		}
		synchronized (EntityInfoBean.VALIDATED_TABLE) {
			obj = EntityInfoBean.VALIDATED_TABLE.get(tableName);
			if (null != obj) {
				// 被同步过程中，已经存在
				return;
			}
			if (null != this.adts) {
				// 进行表处理
				try {
					this.adts.createOrEditTable(tableName);
					// 然后同步基础数据
					EntityInfoBean.VALIDATED_TABLE.put(tableName, new Object());
					this.adts.execInitData();
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 实体中字段信息对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午1:14:09
	 */
	public class FieldInfoBean {

		/**
		 * 字段对象
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月20日 下午1:44:57
		 */
		private final Field field;

		/**
		 * sql用字段名，带有符号“`”
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月8日 上午10:15:50
		 */
		private final String sqlField;

		/**
		 * 字段数据信息
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月27日 下午8:05:03
		 */
		private final DataField df;

		/**
		 * 前一个字段信息
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月29日 下午8:07:48
		 */
		private FieldInfoBean before = null;

		/**
		 * Id类字段信息
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月5日 下午8:02:40
		 */
		private final IdField idf;

		/**
		 * 是否主键
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月11日 下午2:54:27
		 */
		private boolean isPrimary = false;

		/**
		 * 相关的get方法
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月20日 下午1:44:58
		 */
		private final Method getM;

		/**
		 * 相关的set方法
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月20日 下午1:44:59
		 */
		private final Method setM;

		/**
		 * 相关字段类型
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月20日 下午3:51:18
		 */
		private final FieldType type;

		/**
		 * 字段默认值
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月6日 下午8:18:33
		 */
		private final Object defValue;

		/**
		 * 针对字段的内容信息
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月21日 上午10:17:59
		 */
		private final String baseAlter;

		/**
		 * 对应的属性占位
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月8日 下午8:30:40
		 */
		private final FieldLocation<E> tl;

		/**
		 * @author Weijie Xu
		 * @dateTime 2015年4月20日 下午1:48:51
		 * @param clz 所在对象类
		 * @param f 目标字段
		 * @param df 字段注解，这里传过来，仅是为了减少一次在获取的过程
		 */
		public FieldInfoBean(final Class<E> clz, final Field f, final DataField df) {
			this.field = f;
			this.df = df;
			this.sqlField = "`" + this.df.fieldName() + "`";
			switch (EntityInfoBean.this.databaseType) {
			case Mongo:
				this.type = DatabaseFactroy.getMongoFieldType(df.fieldType());
				break;
			default:
				this.type = DatabaseFactroy.getSqlFieldType(df.fieldType());
				break;
			}
			this.idf = f.getAnnotation(IdField.class);
			if (null != this.idf) {
				if (this.idf.value() == IdFieldEnum.Increment) {
					// 是自增字段
					if (this.type.canIncrement()) {
						// 判定是否已经存在了自增列
						if (null != EntityInfoBean.this.incrementField) {
							// 因为重复了所以要报错
							throw new NotAvailableOperationModeException("The Fields[" + f.getName() + ":" + EntityInfoBean.this.incrementField.field.getName() + "] in " + EntityInfoBean.this.entClz.getName() + " Both Increment key Error ...");
						} else {
							EntityInfoBean.this.incrementField = this;
						}
					} else {
						// 因为不能是自增，而需要报错
						throw new NotAvailableOperationModeException("The Field[" + f.getName() + "] in " + EntityInfoBean.this.entClz.getName() + " Type is [" + this.type.name() + "] cannt Increment...");
					}
				} else if (this.idf.value() == IdFieldEnum.UUID) {
					if (this.type.canUuid()) {
						// 判定是否已经存在了UUID列
						if (null != EntityInfoBean.this.uuidField) {
							// 因为重复了所以要报错
							throw new NotAvailableOperationModeException("The Fields[" + f.getName() + ":" + EntityInfoBean.this.uuidField.field.getName() + "] in " + EntityInfoBean.this.entClz.getName() + " Both Uuid key Error ...");
						} else {
							EntityInfoBean.this.uuidField = this;
						}
					} else {
						// 因为不能是UUID，而需要报错
						throw new NotAvailableOperationModeException("The Field[" + f.getName() + "] in " + EntityInfoBean.this.entClz.getName() + " Type is [" + this.type.name() + "] cannt UUID...");
					}
				}
			}
			{ // 分析相关的get、set方法
				// 得到相关名字
				String gmS = df.getMethodName().trim();
				String smS = df.setMethodName().trim();
				if ((gmS.length() == 0) || (smS.length() == 0)) {
					final String fn = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
					if (gmS.length() == 0) {
						gmS = "get" + fn;
					}
					if (smS.length() == 0) {
						smS = "set" + fn;
					}
				}
				try {
					this.getM = clz.getMethod(gmS);
				} catch (final NoSuchMethodException e) {
					e.printStackTrace();
					throw new ConfigurationException("Error getMethod with " + EntityInfoBean.this.entClz.getName() + " (" + f.getName() + ")> " + gmS);
				} catch (final SecurityException e) {
					e.printStackTrace();
					throw new ConfigurationException("Error getMethod with " + EntityInfoBean.this.entClz.getName() + " (" + f.getName() + ")> " + gmS);
				}
				try {
					this.setM = clz.getMethod(smS, f.getType());
				} catch (final NoSuchMethodException e) {
					e.printStackTrace();
					throw new ConfigurationException("Error setMethod with " + EntityInfoBean.this.entClz.getName() + " (" + f.getName() + ")> " + smS);
				} catch (final SecurityException e) {
					e.printStackTrace();
					throw new ConfigurationException("Error setMethod with " + EntityInfoBean.this.entClz.getName() + " (" + f.getName() + ")> " + smS);
				}
			}
			if (!this.df.defValue().equals("-")) {
				if (this.df.canNull() && this.df.defValue().equals("NULL")) {
					// 因为可以null，而且默认也是null，所以不处理，有点废话的概念...
					this.defValue = null;
				} else {
					this.defValue = this.type.getDefaultValue(this.df.defValue());
				}
			} else {
				this.defValue = null;
			}
			switch (EntityInfoBean.this.databaseType) {
			case Mongo:
				this.baseAlter = null;
				break;
			default:
				this.baseAlter = this.makeBaseAlter(df, this.idf);
				break;
			}
			this.tl = new FieldLocation<>(this);
		}

		/**
		 * 制作基础内容信息
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月21日 上午10:17:42
		 * @param df 数据字段信息
		 * @param idf Id类字段信息
		 * @return alter用内容
		 */
		private String makeBaseAlter(final DataField df, final IdField idf) {
			final StringBuilder sb = new StringBuilder();
			this.type.makeAlter(sb, df, idf);
			return sb.toString();
		}

		/**
		 * 得到对象字段信息
		 * 
		 * @author tfzzh
		 * @dateTime 2016年9月6日 下午4:12:35
		 * @return 对象字段信息
		 */
		public Field getObjField() {
			return this.field;
		}

		/**
		 * 得到所相关对象字段的名称<br />
		 * 是与数据库字段无关的<br />
		 * 
		 * @author tfzzh
		 * @dateTime 2016年9月6日 上午11:21:32
		 * @return 所相关对象字段的名称
		 */
		public String getObjFieldName() {
			return this.field.getName();
		}

		/**
		 * 得到所相关的字段的名称<br />
		 * 对应数据库列的名<br />
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月4日 下午6:54:34
		 * @return 所相关的字段的名称
		 */
		public String getDataFieldName() {
			return this.df.fieldName();
		}

		/**
		 * 得到sql用字段名，带有符号“`”
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月8日 上午10:50:32
		 * @return sql用字段名，带有符号“`”
		 */
		public String getSQLFieldName() {
			return this.sqlField;
		}

		/**
		 * 得到mongo用字段名
		 * 
		 * @author tfzzh
		 * @dateTime 2016年12月14日 下午1:29:55
		 * @return mongo用字段名
		 */
		public String getMongoFieldName() {
			return this.df.fieldName();
		}

		/**
		 * 得到相关的get方法
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月24日 下午8:52:20
		 * @return the getM
		 */
		public Method getGetMethod() {
			return this.getM;
		}

		/**
		 * 得到相关的set方法
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月24日 下午8:52:20
		 * @return the setM
		 */
		public Method getSetMethod() {
			return this.setM;
		}

		/**
		 * 放入要作为条件的值
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月6日 下午1:20:49
		 * @param ps 声明对象
		 * @param ent 数据实体
		 * @param ind 目标位置，在ps中
		 * @param log 日志记录
		 */
		private void putValue(final PreparedStatement ps, final E ent, final int ind, final StringBuilder log) {
			try {
				// 得到值
				final Object val = this.getM.invoke(ent);
				if (null == val) {
					if (null != this.defValue) {
						// 有默认值的情况
						this.type.putValueToPs(ps, this.defValue, ind);
						if (null != log) {
							log.append(this.field.getName()).append('=').append(this.defValue.toString());
						}
					} else {
						if (!this.df.canNull()) {
							// 不可为null的字段null了
							throw new FieldNullException(EntityInfoBean.this.entClz.getName(), this.field.getName());
						}
						ps.setNull(ind, Types.NULL);
						if (null != log) {
							log.append(this.field.getName()).append('=').append("null");
						}
					}
				} else {
					this.type.putValueToPs(ps, val, ind);
					if (null != log) {
						log.append(this.field.getName()).append('=').append(val.toString());
					}
				}
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
			} catch (final IllegalArgumentException e) {
				e.printStackTrace();
			} catch (final InvocationTargetException e) {
				e.printStackTrace();
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 放入UUID属性的值
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月6日 下午4:13:31
		 * @param ps 声明对象
		 * @param ent 数据实体
		 * @param ind 目标位置，在ps中
		 * @param log 日志记录
		 */
		private void putUuid(final PreparedStatement ps, final E ent, final int ind, final StringBuilder log) {
			try {
				final Object val = this.getM.invoke(ent);
				if (null == val) {
					final String uuid = TfzzhUUID.randomUUID().toString();
					ps.setString(ind, uuid);
					// 设置完了，放入
					this.setM.invoke(ent, uuid);
					if (null != log) {
						log.append(this.field.getName()).append('=').append(uuid);
					}
				} else {
					// 仅是强转，认为该情况尽可能是string型
					ps.setString(ind, (String) val);
					if (null != log) {
						log.append(this.field.getName()).append('=').append(val.toString());
					}
				}
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
			} catch (final IllegalArgumentException e) {
				e.printStackTrace();
			} catch (final InvocationTargetException e) {
				e.printStackTrace();
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 得到相关字段类型
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月24日 下午8:52:20
		 * @return the type
		 */
		public FieldType getType() {
			return this.type;
		}

		/**
		 * 得到前一个字段的字段名
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月5日 下午8:44:54
		 * @return 前一个字段的字段名<br />
		 *         null，表示为表中第一个字段<br />
		 */
		public String getBeforeFieldName() {
			return null == this.before ? null : this.before.df.fieldName();
		}

		/**
		 * 得到对应的属性占位
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月8日 下午8:31:32
		 * @return the tl
		 */
		public FieldLocation<E> getFieldLocation() {
			return this.tl;
		}

		/**
		 * 得到针对字段的内容信息
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月24日 下午8:52:20
		 * @return the baseAlter
		 */
		public String getBaseAlter() {
			return this.baseAlter;
		}

		/**
		 * 得到增加字段用的内容信息
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月29日 下午6:57:20
		 * @param sb 组合用字串，会直接将增加的内容，放入进去
		 * @return 同sb
		 */
		public StringBuilder getAddAlter(final StringBuilder sb) {
			return AlterTypeEnum.Add.getAlterField(sb, this.df.fieldName(), this.baseAlter, this.before == null ? null : this.before.df.fieldName());
		}

		/**
		 * 得到修改字段用的内容信息
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月29日 下午6:57:20
		 * @param sb 组合用字串，会直接将增加的内容，放入进去
		 * @return 同sb
		 */
		public StringBuilder getChangeAlter(final StringBuilder sb) {
			return AlterTypeEnum.Change.getAlterField(sb, this.df.fieldName(), this.baseAlter, this.before == null ? null : this.before.df.fieldName());
		}

		/**
		 * 比较类型，长度，非空，无符号，自增，默认值，注释
		 *
		 * @author Weijie Xu
		 * @dateTime 2015年5月5日 上午10:17:03
		 * @param type 新的类型，请保证内容均为大写（String）
		 * @param length 长度（int）
		 * @param canNull 是否可null（boolean）
		 * @param isIncrement 是否自增（boolean）
		 * @param scale 小数点位数，针对浮点（int）
		 * @param defValue 默认值？（String）
		 * @param desc 说明（String）
		 * @param behind 在什么字段后面（String）
		 * @return true，是相同的；<br />
		 *         false，是不同的；<br />
		 */
		public boolean validate(final String type, final int length, final boolean canNull, final boolean isIncrement, final int scale, final String defValue, final String desc, final String behind) {
			// 首先判定类型
			if (this.type.sameType(type)) {
				// 因为类型相同，需要继续下一步动作
				// 判定位置
				if (null == this.before) {
					if (null != behind) {
						// 位置不同了
						return false;
					}
				} else {
					if (!this.before.df.fieldName().equals(behind)) {
						// 认为位置不同了
						return false;
					}
				}
				return this.type.validateData(this.df, this.idf, length, canNull, type.indexOf("UNSIGNED") != -1, isIncrement, scale, defValue, desc);
			}
			return false;
		}

		/**
		 * 得到所属数据实体信息
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月16日 下午2:00:16
		 * @return 所属数据实体信息
		 */
		public EntityInfoBean<E> getEntityInfo() {
			return EntityInfoBean.this;
		}

		/**
		 * 得到所相关的对象信息
		 * 
		 * @author Weijie Xu
		 * @dateTime 2017年5月16日 下午5:42:08
		 * @param clz 目标类对象
		 * @param <B> 所相关类对象
		 * @return 属性所相关对象的实例
		 */
		public <B extends BaseDataBean> EntityInfoBean<B> getRelativeEntitiyInfo(final Class<B> clz) {
			if (clz.isAssignableFrom(this.field.getType())) {
				return EntityTool.getInstance().getEntityInfo(clz);
			} else {
				return null;
			}
		}
	}

	// DROP INDEX `uni_name` ,
	// ADD UNIQUE INDEX `uni_name` (`name` DESC),
	// DROP INDEX `ind_group_group_level` ,
	// ADD INDEX `ind_group_group_level` (`group` ASC, `group_level` DESC),
	// ADD UNIQUE INDEX `index5` (`name` DESC, `age` ASC),
	// DROP INDEX `ind_age` ,
	// DROP INDEX `uni_uuid` ;
	// DROP PRIMARY KEY,
	// ADD PRIMARY KEY (`uuid`, `name`);
	/**
	 * 索引信息对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午2:34:31
	 */
	public class IndexInfoBean {

		/**
		 * 索引的名字，当前仅是为了日志输入内容而存在
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月27日 下午3:40:48
		 */
		private final String name;

		/**
		 * 所相关的字段列表
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月27日 下午1:54:13
		 */
		private final EntityInfoBean<E>.FieldInfoBean[] fs;

		/**
		 * 索引的类型
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月27日 下午1:54:58
		 */
		private final KeyIndexEnum type;

		/**
		 * 排序的类型，升序（asc）或降序（desc）
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月30日 下午2:47:54
		 */
		private final SortEnum[] sortTypes;

		/**
		 * 索引名字
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月30日 上午9:57:31
		 */
		private String indexName = null;

		/**
		 * 内容信息
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月27日 下午8:13:15
		 */
		private String alter = null;

		/**
		 * @author Weijie Xu
		 * @dateTime 2015年4月27日 下午2:01:05
		 * @param name 索引的名字
		 * @param fs 相关实体中字段对象列表，定长数组
		 * @param type 索引类型
		 * @param sortType 排序的类型
		 */
		private IndexInfoBean(final String name, final EntityInfoBean<E>.FieldInfoBean[] fs, final KeyIndexEnum type, final SortEnum sortType) {
			this.name = name;
			this.fs = fs;
			this.type = type;
			this.sortTypes = new SortEnum[fs.length];
		}

		/**
		 * 放入一个元素
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月27日 下午3:08:15
		 * @param fi 目标属性
		 * @param sort 排序类型
		 * @param ind 目标位置
		 */
		private void putField(final FieldInfoBean fi, final SortEnum sort, final int ind) {
			if (null != this.fs[ind]) {
				throw new IndexConfigException(EntityInfoBean.this.entClz.getSimpleName(), fi.field.getName(), this.fs[ind].field.getName(), this.name, ind + 1);
			}
			this.fs[ind] = fi;
			this.sortTypes[ind] = this.type.getSortType(sort);
		}

		/**
		 * 验证是否存在空占位
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月27日 下午4:02:35
		 */
		private void validate() {
			for (int i = 0; i < this.fs.length; i++) {
				if (null == (this.fs[i])) {
					throw new IndexConfigException(EntityInfoBean.this.entClz.getSimpleName(), this.name, i + 1);
				}
			}
			// 因为OK，进行内容组合
			this.alter = this.makeAlter();
		}

		/**
		 * 制作内容信息
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月27日 下午8:15:14
		 * @return 内容信息
		 */
		private String makeAlter() {
			final StringBuilder sb = new StringBuilder();
			sb.append(this.type.getAlterPrefixd());
			if (null != this.type.getNamePrefixd()) {
				// 名字处理
				final StringBuilder sbName = new StringBuilder();
				sbName.append(this.type.getNamePrefixd());
				if (this.fs.length == 1) {
					// 只有一个内容
					sbName.append(this.fs[0].df.fieldName());
					if (null != this.sortTypes[0]) {
						sbName.append(this.sortTypes[0].getIndexNameSuf());
					}
				} else {
					// 多个内容
					boolean isFirst = true;
					FieldInfoBean fi;
					SortEnum se;
					for (int i = 0; i < this.fs.length; i++) {
						fi = this.fs[i];
						if (isFirst) {
							isFirst = false;
						} else {
							sbName.append('_');
						}
						sbName.append(fi.df.fieldName());
						if (null != (se = this.sortTypes[i])) {
							sbName.append(se.getIndexNameSuf());
						}
					}
				}
				this.indexName = sbName.toString().toUpperCase();
				sb.append('`').append(sbName).append("` ");
			} else {
				this.indexName = "PRIMARY";
				EntityInfoBean.this.primary = this;
			}
			if (this.fs.length == 1) {
				// 只有一个内容
				sb.append("(").append(this.fs[0].sqlField);
				if (null != this.sortTypes[0]) {
					sb.append(this.sortTypes[0].getSQLText()).append(')');
				} else {
					sb.append(")");
				}
			} else {
				// 多个内容
				boolean isFirst = true;
				sb.append('(');
				FieldInfoBean fi;
				SortEnum se;
				for (int i = 0; i < this.fs.length; i++) {
					fi = this.fs[i];
					if (isFirst) {
						isFirst = false;
					} else {
						sb.append(',');
					}
					sb.append('`').append(fi.df.fieldName()).append('`');
					if (null != (se = this.sortTypes[i])) {
						sb.append(se.getSQLText());
					}
				}
				sb.append(')');
			}
			return sb.toString();
		}

		/**
		 * 得到索引的名字
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月30日 上午10:29:11
		 * @return 索引的名字
		 */
		public String getName() {
			return this.indexName;
		}

		/**
		 * 得到增加字段用的内容信息
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月4日 上午11:08:25
		 * @param sb 组合用字串，会直接将增加的内容，放入进去
		 * @return 同sb
		 */
		public StringBuilder getAddAlter(final StringBuilder sb) {
			return AlterTypeEnum.Add.getAlterIndex(sb, this.indexName, this.alter);
		}
		// /**得到内容信息
		// * @author Weijie Xu
		// * @dateTime 2015年4月27日 下午8:15:00
		// *
		// * @return 内容信息
		// */
		// public String getAlter () {
		// return this.alter;
		// }
	}
}
