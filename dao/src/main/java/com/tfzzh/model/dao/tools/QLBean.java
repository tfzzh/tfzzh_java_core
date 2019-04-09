/**
 * @author Weijie Xu
 * @dateTime 2015年4月24日 下午3:05:15
 */
package com.tfzzh.model.dao.tools;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.util.JSON;
import com.tfzzh.exception.ConfigurationException;
import com.tfzzh.exception.NotAvailableOperationModeException;
import com.tfzzh.log.CoreLog;
import com.tfzzh.model.bean.BaseDataBean;
import com.tfzzh.model.exception.TableNameException;
import com.tfzzh.model.exception.UnknowDaoException;
import com.tfzzh.model.tools.PagerankBean;
import com.tfzzh.nosql.model.mongodb.MongoDatabaseData;

/**
 * SQL组合相关数据对象，该对象仅能存在于单一线程中
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月24日 下午3:05:15
 */
public class QLBean {

	// /**
	// * @author tfzzh
	// * @dateTime 2017年1月7日 下午10:14:24
	// */
	// protected static final Logger log = LogManager.getLogger(QLBean.class.getName());
	/**
	 * TODO 之后需要慎密处理多表相关逻辑，同时需要注意效率问题，当前先不做太多此方面问题处理<br />
	 * 表命名空间说明集合<br />
	 * 多表制作时，可以考虑将value的String换成一个对象，包括目标表名以及另名...<br />
	 * 另名建议为在getTableName时候设置进去的<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午3:06:06
	 */
	// private Map<String, TableScope<? extends BaseEntityBean, ? extends BaseDAO<?>>> tsm = null;
	private final Map<EntityInfoBean<? extends BaseDataBean>, String> tsm = new HashMap<>(1);

	/**
	 * 所相关主对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月16日 下午2:01:49
	 */
	private EntityInfoBean<? extends BaseDataBean> mainEib = null;

	/**
	 * 条件用范围域集合，主域集合，不直接包含任何子域
	 *
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午3:33:31
	 */
	private List<LocalScope> termScopes = null;

	/**
	 * 更新用范围域集合
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午6:41:06
	 */
	private List<LocalScope> updateScopes = null;

	/**
	 * 排序用范围域集合
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午6:41:07
	 */
	private List<LocalScope> orderScopes = null;

	/**
	 * 列表字段
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年5月17日 下午7:57:20
	 */
	private NSListFieldBean listFieldScopes = null;

	/**
	 * 针对ps，用的占位控制
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午9:02:57
	 */
	private Set<QLLocation> psLocation = null;

	/**
	 * 分页信息
	 * 
	 * @author 许纬杰
	 * @datetime 2016年7月22日_下午5:34:31
	 */
	private PagerankBean prb = null;

	/**
	 * 动态sql
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月16日 下午6:04:13
	 */
	private QLSqlBean sql = null;

	/**
	 * 条件类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午5:07:07
	 */
	protected final static int TYPE_TERM = 0;

	/**
	 * 更新类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午5:07:08
	 */
	protected final static int TYPE_UPDATE = 1;

	/**
	 * 排序类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午5:07:09
	 */
	protected final static int TYPE_ORDER = 2;

	/**
	 * 数组字段模式
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月2日 下午2:09:35
	 */
	protected final static int TYPE_LIST_FIELD = 11;

	/**
	 * 主动放入主实例对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月9日 下午12:57:16
	 * @param mainEnt 目标实例对象
	 */
	public void putMainEntity(final EntityInfoBean<? extends BaseDataBean> mainEnt) {
		this.mainEib = mainEnt;
	}

	/**
	 * 放入表命名空间，针对有非默认表名或者没有条件时<br />
	 * 此时为默认表名<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月16日 下午2:30:00
	 * @param eib 目标实例对象
	 */
	public void putTableScope(final EntityInfoBean<? extends BaseDataBean> eib) {
		this.putTableScope(eib, null);
	}

	/**
	 * 放入表命名空间，针对有非默认表名或者没有条件时
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午3:18:22
	 * @param eib 目标实例对象
	 * @param tableName 目标表名
	 */
	public void putTableScope(final EntityInfoBean<? extends BaseDataBean> eib, final String tableName) {
		// if (null == this.tsm) {
		// this.tsm = new HashMap<String, TableScope<? extends BaseEntityBean, ? extends BaseDAO<?>>>();
		// }
		// this.tsm.put(ts.getName(), ts);
		if (null == this.mainEib) {
			this.mainEib = eib;
		}
		this.tsm.put(eib, tableName);
	}

	/**
	 * 增加一个域，此方式建立在，该对象仅可能存在于单一线程中
	 *
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午4:05:02
	 * @param ls 域
	 */
	protected void addLocalScope(final LocalScope ls) {
		switch (ls.getType()) {
		case TYPE_TERM:
			if (null == this.termScopes) {
				this.termScopes = new ArrayList<>(3);
			}
			this.termScopes.add(ls);
			break;
		case TYPE_UPDATE:
			if (null == this.updateScopes) {
				this.updateScopes = new ArrayList<>(2);
			}
			this.updateScopes.add(ls);
			break;
		case TYPE_ORDER:
			if (null == this.orderScopes) {
				this.orderScopes = new ArrayList<>(1);
			}
			this.orderScopes.add(ls);
			break;
		case TYPE_LIST_FIELD:
			if (null == this.listFieldScopes) {
				this.listFieldScopes = (NSListFieldBean) ls;
			} else {
				throw new ConfigurationException(" Can't two List Field in once operation ... ");
			}
		}
	}

	/**
	 * 增加一个ps用的占位值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午9:06:04
	 * @param loc 目标占位值
	 */
	protected void addPsLocation(final QLLocation loc) {
		if (null == this.psLocation) {
			this.psLocation = new LinkedHashSet<>(5);
		}
		this.psLocation.add(loc);
	}

	/**
	 * 清除占位值<br />
	 * 用在sql处理完成后，其他过程请慎用<br />
	 * 
	 * @author Xu Weijie
	 * @datetime 2018年3月1日_上午10:07:29
	 */
	public void clearPsLocation() {
		this.psLocation = null;
	}

	// /**
	// * 设置类型
	// *
	// * @author Weijie Xu
	// * @dateTime 2015年5月7日 下午6:39:06
	// * @param type 目标类型
	// */
	// private void setType(final int type) {
	// if (this.type != type) {
	// this.type = type;
	// }
	// }
	/**
	 * 得到所相关数据库类型
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月23日 下午5:17:20
	 * @return 所相关数据库类型
	 */
	public DatabaseTypeEnum getDatabaseType() {
		if (null == this.mainEib) {
			// 默认为sql
			return DatabaseTypeEnum.SQL;
		}
		return this.mainEib.getDatabaseType();
	}

	/**
	 * 验证实例信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月16日 下午2:13:15
	 * @param eib 数据对象实例信息
	 */
	protected void validateEntityInfo(final EntityInfoBean<? extends BaseDataBean> eib) {
		if (null == this.mainEib) {
			this.mainEib = eib;
			final String back = this.tsm.put(eib, null);
			if (null != back) {
				this.tsm.put(eib, back);
			}
		} else if (eib != this.mainEib) {
			final String back = this.tsm.put(eib, null);
			if (null != back) {
				this.tsm.put(eib, back);
			}
		}
	}

	/**
	 * 得到条件用对象
	 *
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午3:10:00
	 * @param <E> 数据对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 * @return 条件用对象
	 */
	public <E extends BaseDataBean> QLTermBean getNewTerm(final EntityInfoBean<E>.FieldInfoBean tfb) {
		// this.setType(QLBean.TYPE_TERM);
		this.validateEntityInfo(tfb.getEntityInfo());
		return new QLTermBean(this, tfb);
	}

	/**
	 * 得到条件用对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午6:36:55
	 * @param <E> 数据对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 * @param parentLs 所相关的父域
	 * @return 条件用对象
	 */
	public <E extends BaseDataBean> QLTermBean getNewTerm(final EntityInfoBean<E>.FieldInfoBean tfb, final LocalScope parentLs) {
		// this.setType(QLBean.TYPE_TERM);
		this.validateEntityInfo(tfb.getEntityInfo());
		return new QLTermBean(this, tfb, parentLs);
	}

	/**
	 * 得到条件用对象
	 *
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午4:06:22
	 * @param <E> 数据对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 * @param symbol 主符号
	 * @return 条件用对象
	 */
	public <E extends BaseDataBean> QLTermBean getNewTerm(final EntityInfoBean<E>.FieldInfoBean tfb, final SymbolEnum symbol) {
		// this.setType(QLBean.TYPE_TERM);
		this.validateEntityInfo(tfb.getEntityInfo());
		return new QLTermBean(this, tfb, symbol);
	}

	/**
	 * 得到条件用对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午6:36:35
	 * @param <E> 数据对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 * @param symbol 主符号
	 * @param parentLs 所相关的父域
	 * @return 条件用对象
	 */
	public <E extends BaseDataBean> QLTermBean getNewTerm(final EntityInfoBean<E>.FieldInfoBean tfb, final SymbolEnum symbol, final LocalScope parentLs) {
		// this.setType(QLBean.TYPE_TERM);
		this.validateEntityInfo(tfb.getEntityInfo());
		return new QLTermBean(this, tfb, symbol, parentLs);
	}

	/**
	 * 得到一个条件集合对象，为一组“()”内容
	 * 
	 * @author Xu Weijie
	 * @datetime 2018年2月28日_下午7:22:56
	 * @return 条件包用对象，需要向内增加自元素
	 */
	public <E extends BaseDataBean> QLPackTermBean getPackTerm() {
		return new QLPackTermBean(this);
	}

	/**
	 * 得到一个条件集合对象，为一组“()”内容
	 * 
	 * @author Xu Weijie
	 * @datetime 2018年3月1日_上午9:23:24
	 * @param parentLs 所相关的父域
	 * @return 条件包用对象，需要向内增加自元素
	 */
	public <E extends BaseDataBean> QLPackTermBean getPackTerm(final LocalScope parentLs) {
		return new QLPackTermBean(this, parentLs);
	}

	/**
	 * 得到更新用对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午3:10:50
	 * @param <E> 数据对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 * @return 更新用对象
	 */
	public <E extends BaseDataBean> QLUpdateBean getNewUpdate(final EntityInfoBean<E>.FieldInfoBean tfb) {
		// this.setType(QLBean.TYPE_UPDATE);
		this.validateEntityInfo(tfb.getEntityInfo());
		return new QLUpdateBean(this, tfb);
	}

	/**
	 * 得到更新用对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午12:50:19
	 * @param <E> 数据对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 * @param symbol 主符号，一般仅针对更新时，字符串拼接
	 * @return 更新用对象
	 */
	public <E extends BaseDataBean> QLUpdateBean getNewUpdate(final EntityInfoBean<E>.FieldInfoBean tfb, final SymbolEnum symbol) {
		// this.setType(QLBean.TYPE_UPDATE);
		this.validateEntityInfo(tfb.getEntityInfo());
		return new QLUpdateBean(this, tfb, symbol);
	}

	/**
	 * 得到排序用对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午3:10:49
	 * @param <E> 数据对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 * @param sort 排序条件
	 * @return 排序用对象
	 */
	public <E extends BaseDataBean> QLOrderBean getNewOrder(final EntityInfoBean<E>.FieldInfoBean tfb, final SortEnum sort) {
		// this.setType(QLBean.TYPE_ORDER);
		this.validateEntityInfo(tfb.getEntityInfo());
		return new QLOrderBean(this, tfb, sort);
	}

	/**
	 * 放入数组字段，针对mongodb<br />
	 * 主要处理，得到数组字段的长度<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年5月17日 下午7:58:22
	 * @param <E> 数据对象
	 * @param tfb 表字段对象
	 * @return 条件用对象
	 */
	public <E extends BaseDataBean> NSListFieldBean putArrayField(final EntityInfoBean<E>.FieldInfoBean tfb) {
		return new NSListFieldBean(this, tfb);
	}

	/**
	 * 放入分页消息
	 * 
	 * @author 许纬杰
	 * @datetime 2016年7月22日_下午5:39:12
	 * @param page 当前页数
	 * @param size 页面数据量
	 * @return 分页数据对象
	 */
	public PagerankBean putPagerank(final int page, final int size) {
		this.prb = new PagerankBean(page, size);
		return this.prb;
	}

	/**
	 * 得到分页信息
	 * 
	 * @author 许纬杰
	 * @datetime 2016年7月22日_下午5:42:18
	 * @return the prb
	 */
	public PagerankBean getPagerank() {
		return this.prb;
	}

	/**
	 * 得到拼装sql用对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月16日 下午6:06:10
	 * @return 拼装sql用对象
	 */
	public QLSqlBean getSQL() {
		if (null == this.sql) {
			this.sql = new QLSqlBean(this);
		}
		return this.sql;
	}

	/**
	 * 得到表名，可能是多表（之后考虑的）
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月16日 下午3:43:53
	 * @return 表名
	 */
	public String getTableName() {
		if (null == this.mainEib) {
			// 没有表，错误情况
			// 无条件还未放入表信息
			throw new TableNameException("Havent TableName in this QL...");
		}
		if ((this.tsm.size() == 0) && (null != this.mainEib)) {
			return this.mainEib.getDefaultSQLTableName();
		} else if (this.tsm.size() == 1) {
			// 单表情况
			final String tableName = this.tsm.get(this.mainEib);
			if (null == tableName) {
				return this.mainEib.getDefaultTableName();
			} else {
				return tableName;
			}
		} else if (this.tsm.size() > 1) {
			// TODO 多表情况，需要处理
			final StringBuilder sb = new StringBuilder(this.tsm.size() * 12);
			boolean isFirst = true;
			for (final Entry<EntityInfoBean<? extends BaseDataBean>, String> ent : this.tsm.entrySet()) {
				// TODO 需要增加对应逻辑，主要是最名称符号`的控制，之后考虑，暂时掠过
				if (isFirst) {
					isFirst = false;
				} else {
					sb.append(',');
				}
				if (null == ent.getValue()) {
					sb.append(ent.getKey().getDefaultTableName());
				} else {
					sb.append(ent.getValue());
				}
			}
			throw new NotAvailableOperationModeException("The Multi Table Has not Opened...");
		} else {
			throw new UnknowDaoException("Cannt to reach here...");
		}
	}

	/**
	 * 验证所相关数据表名<br />
	 * 如果不存在则创建<br />
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年11月27日_下午2:32:41
	 */
	public void volidateTableName() {
		for (final Entry<EntityInfoBean<? extends BaseDataBean>, String> ent : this.tsm.entrySet()) {
			ent.getKey().validateTabel(ent.getValue());
		}
	}

	/**
	 * 组合条件
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 上午10:47:59
	 * @param sql sql语句
	 * @param ic 索引计数器
	 */
	public void assemblyTermSQL(final StringBuilder sql, final IndexCounter ic) {
		if (null != this.termScopes) {
			sql.append(" WHERE ");
			this.assemblyTermSQL(this.termScopes, sql, ic);
		}
	}

	/**
	 * 组合条件
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午6:44:41
	 * @param lsList 范围域列表
	 * @param sql sql语句
	 * @param ic 索引计数器
	 */
	private void assemblyTermSQL(final List<LocalScope> lsList, final StringBuilder sql, final IndexCounter ic) {
		boolean isFirst = true;
		for (final LocalScope ls : lsList) {
			if (isFirst) {
				isFirst = false;
			} else {
				ls.assemblyRelationalSQL(sql);
			}
			if (null != ls.getChildScopes()) {
				sql.append('(');
				this.assemblyTermSQL(ls.getChildScopes(), sql, ic);
				sql.append(')');
// if (isFirst) {
// isFirst = false;
// }
			} else {
				ls.assemblySQL(sql, ic);
			}
		}
	}

	/**
	 * 组合更新内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 上午10:39:41
	 * @param sql sql语句
	 * @param ic 索引计数器
	 */
	public void assemblyUpdateSQL(final StringBuilder sql, final IndexCounter ic) {
		if (null != this.updateScopes) {
			sql.append(" SET ");
			boolean isFirst = true;
			for (final LocalScope ls : this.updateScopes) {
				if (isFirst) {
					isFirst = false;
				} else {
					sql.append(',');
				}
				ls.assemblySQL(sql, ic);
			}
		}
	}

	/**
	 * 组合排序
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月9日 下午6:17:05
	 * @param sql sql语句
	 */
	public void assemblySortSQL(final StringBuilder sql) {
		if (null != this.orderScopes) {
			sql.append(" ORDER BY ");
			boolean isFirst = true;
			for (final LocalScope ls : this.orderScopes) {
				if (isFirst) {
					isFirst = false;
				} else {
					sql.append(',');
				}
				ls.assemblySQL(sql, null);
			}
		}
	}

	/**
	 * 组合分页<br />
	 * 当前仅针对mysql<br />
	 * 
	 * @author 许纬杰
	 * @datetime 2016年7月22日_下午5:47:04
	 * @param sql sql语句
	 */
	public void assemblyPagerankSQL(final StringBuilder sql) {
		if (null != this.prb) {
			sql.append(" LIMIT ").append(this.prb.getDataStartIndex()).append(',').append(this.prb.getSize());
		}
	}

	/**
	 * 组合直接的sql
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月16日 下午6:26:39
	 * @param sql sql语句
	 */
	public void assemblySQL(final StringBuilder sql) {
		sql.append(this.sql.getSql());
	}

	/**
	 * 设置预备声明中内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月8日 下午4:50:07
	 * @param ps 预备声明
	 * @param log log记录集
	 */
	public void setPSValue(final PreparedStatement ps, final StringBuilder log) {
		Set<QLLocation> lql = this.psLocation;
		if (null == lql) {
			if (null != this.sql) {
				lql = this.sql.getLocations();
			}
		}
		if (null != lql) {
			if (null == log) {
				for (final QLLocation l : lql) {
					l.setPSValue(ps, log);
				}
			} else {
				boolean isFirst = true;
				for (final QLLocation l : lql) {
					if (isFirst) {
						isFirst = false;
					} else {
						log.append(',');
					}
					l.setPSValue(ps, log);
				}
			}
		}
	}

	/**
	 * 组合mongo查询
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月22日 下午7:58:48
	 * @param mCol mongo库
	 * @return 得到的结果
	 */
	public MongoIterable<Document> assemblyMongoFind(final MongoCollection<Document> mCol) {
		FindIterable<Document> fi;
		final Bson fb = this.assemblyMongoFind();
		if (null != fb) {
			fi = mCol.find(fb);
		} else {
			fi = mCol.find();
		}
		final Bson sb = this.assemblyMongoSort();
		if (null != sb) {
			fi.sort(sb);
		}
		if (null != this.prb) {
			this.prb.getDataStartIndex();
			fi.skip(this.prb.getDataStartIndex()).limit(this.prb.getSize());
		}
		return fi;
	}

	/**
	 * 组合mongo向列表字段中插入
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月2日 下午4:30:34
	 * @param mCol mongo库
	 * @param mdd mongo数据库对象
	 * @return 更新的结果
	 */
	public UpdateResult assemblyMongoAggregateFieldInsert(final MongoCollection<Document> mCol, final MongoDatabaseData mdd) {
		// db.to_like.update(
		// {"function":2,"function_id":2},
		// {$addToSet:{users:{$each:[{user_id:3,create_time:"2017-05-17 11:21:18",time:1494991278356},{user_id:4,create_time:"2017-05-17 12:21:18",time:1494992278356}]}}})
		final Bson fb = this.assemblyMongoFind();
		final Bson ub = this.assemblyMongoFieldInsert(mdd);
		if ((null != fb) && (ub != null)) {
			return mCol.updateMany(fb, ub);
		}
		return null;
	}

	/**
	 * 组合mongo查询指定条件数据中数组字段的数量
	 * 集合中必然存在字段“size”为该内容数量
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年5月17日 下午7:52:39
	 * @param mCol mongo库
	 * @return 得到的结果
	 */
	public MongoIterable<Document> assemblyMongoAggregateFieldCount(final MongoCollection<Document> mCol) {
		// aggregate([
		// {$match:{function:2,function_id:2}},
		// {$unwind:"$users"},
		// {$group:{_id:{"function":"$function",function_id:"$function_id"},users:{$push:"$users"},size:{$sum:1}}}
		// ])
		// // 得到条件
		// final Bson mb = this.assemblyMongoAggregateMatch();
		// // 得到分组部分信息
		// final Bson ub = this.assemblyMongoAggregateUnwind();
		// 得到查询所相关条件集合
		final List<Bson> mub = this.assemblyMongoAggregateMatch();
		// 在分组中判定信息
		final List<Bson> gcb = this.assemblyMongoAggregateGroup(true);
		mub.addAll(gcb);
		return mCol.aggregate(mub);
	}

	/**
	 * 组合mongo中以列表字段为主要结果的查询
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月5日 下午4:09:38
	 * @param mCol mongo库
	 * @return 得到的结果
	 */
	public MongoIterable<Document> assemblyMongoAggregateFieldFind(final MongoCollection<Document> mCol) {
		// // 得到条件
		// final Bson mb = this.assemblyMongoAggregateMatch();
		// // 得到分组部分信息
		// final Bson ub = this.assemblyMongoAggregateUnwind();
		// 得到查询所相关条件集合
		final List<Bson> mub = this.assemblyMongoAggregateMatch();
		// 在分组中判定信息
		final List<Bson> gcb = this.assemblyMongoAggregateGroup(false);
		mub.addAll(gcb);
		return mCol.aggregate(mub);
	}

	/**
	 * 进行mongo数据更新
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月22日 下午9:15:21
	 * @param mCol mongo库
	 * @return 更新的结果
	 */
	public UpdateResult assemblyMongoUpdate(final MongoCollection<Document> mCol) {
		final Bson fb = this.assemblyMongoFind();
		final Bson ub = this.assemblyMongoUpdate();
		if ((null != fb) && (ub != null)) {
			return mCol.updateMany(fb, ub);
		}
		return null;
	}

	/**
	 * 进行mongo数据删除
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月20日 下午12:03:19
	 * @param mCol mongo库
	 * @return 删除的结果
	 */
	public DeleteResult assemblyMongoDelete(final MongoCollection<Document> mCol) {
		final Bson fb = this.assemblyMongoFind();
		if (null != fb) {
			return mCol.deleteMany(fb);
		}
		return null;
	}

	/**
	 * 组合mongo查询部分代码
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月20日 上午10:38:34
	 * @return 组合的对象
	 */
	private Bson assemblyMongoFind() {
		if (null != this.termScopes) {
			// final BasicDBObject bo = new BasicDBObject();
			final StringBuilder sb = new StringBuilder(16 + (16 * this.termScopes.size()));
			sb.append('{');
			boolean isFirst = true;
			for (final LocalScope ls : this.termScopes) {
				if (isFirst) {
					isFirst = false;
				} else {
					sb.append(',');
				}
				// ls.assemblyRelationalMongo(bo);
				ls.assemblyRelationalMongo(sb);
			}
			sb.append('}');
			if (CoreLog.getInstance().debugEnabled(QLBean.class)) {
				CoreLog.getInstance().debug(QLBean.class, "MongoThread[", Thread.currentThread().getName(), "] find > ", sb.toString());
			}
			return (BasicDBObject) JSON.parse(sb.toString());
		}
		return null;
	}

	/**
	 * 组合mongo集合查询所相关条件集合
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月12日 上午11:09:11
	 * @return 查询所相关条件集合
	 */
	@SuppressWarnings({ "unchecked" })
	private List<Bson> assemblyMongoAggregateMatch() {
		// match
		// {$match:{function:2,function_id:2}}
		if ((null != this.termScopes) && (null != this.listFieldScopes)) {
			final StringBuilder sb = new StringBuilder(16 + (16 * this.termScopes.size()));
			sb.append("[{$match:{");
			boolean isFirst = true;
			for (final LocalScope ls : this.termScopes) {
				if (isFirst) {
					isFirst = false;
				} else {
					sb.append(',');
				}
				ls.assemblyRelationalMongo(sb);
			}
			sb.append("}},{$unwind:\"$");
			sb.append(this.listFieldScopes.getTargetName());
			if (null != this.listFieldScopes.getTermScopes()) {
				sb.append("\"},{$match:{");
				// 加入子条件内容
				isFirst = true;
				for (final LocalScope ls : this.listFieldScopes.getTermScopes()) {
					if (isFirst) {
						isFirst = false;
					} else {
						sb.append(',');
					}
					ls.assemblyRelationalMongo(sb);
				}
				sb.append("}}]");
			} else {
				sb.append("\"}]");
			}
			if (CoreLog.getInstance().debugEnabled(QLBean.class)) {
				CoreLog.getInstance().debug(QLBean.class, "MongoThread[", Thread.currentThread().getName(), "] Aggregate Match > ", sb.toString());
			}
			return (List<Bson>) JSON.parse(sb.toString());
		}
		return null;
	}

	// /**
	// * 组合mongo中总量查询所需集合条件
	// *
	// * @author Weijie Xu
	// * @dateTime 2017年6月1日 下午7:35:28
	// * @return 总量查询所需集合条件
	// */
	// private Bson assemblyMongoAggregateMatch() {
	// // match
	// // {$match:{function:2,function_id:2}}
	// if ((null != this.termScopes) && (null != this.listFieldScopes)) {
	// final StringBuilder sb = new StringBuilder(16 + (16 * this.termScopes.size()));
	// sb.append("{$match:{");
	// boolean isFirst = true;
	// for (final LocalScope ls : this.termScopes) {
	// if (isFirst) {
	// isFirst = false;
	// } else {
	// sb.append(',');
	// }
	// ls.assemblyRelationalMongo(sb);
	// }
	// if (null != this.listFieldScopes.getTermScopes()) {
	// // 加入子条件内容
	// for (final LocalScope ls : this.listFieldScopes.getTermScopes()) {
	// if (isFirst) {
	// isFirst = false;
	// } else {
	// sb.append(',');
	// }
	// ls.assemblyRelationalMongo(sb);
	// }
	// }
	// sb.append("}}");
	// if (CoreLog.getInstance().debugEnabled(QLBean.class)) {
	// CoreLog.getInstance().debug(QLBean.class, "MongoThread[", Thread.currentThread().getName(), "] Aggregate Match > ", sb.toString());
	// }
	// return (BasicDBObject) JSON.parse(sb.toString());
	// }
	// return null;
	// }
	//
	// /**
	// * 组合mongo中总量查询所相关列表字段条件
	// *
	// * @author Weijie Xu
	// * @dateTime 2017年6月1日 下午7:35:26
	// * @return 总量查询所相关列表字段条件
	// */
	// private Bson assemblyMongoAggregateUnwind() {
	// // unwind
	// // {$unwind:"$users"}
	// if (null != this.listFieldScopes) {
	// final StringBuilder sb = new StringBuilder(32);
	// sb.append("{$unwind:\"$");
	// sb.append(this.listFieldScopes.getTargetName());
	// sb.append("\"}");
	// if (CoreLog.getInstance().debugEnabled(QLBean.class)) {
	// CoreLog.getInstance().debug(QLBean.class, "MongoThread[", Thread.currentThread().getName(), "] Aggregate Unwind > ", sb.toString());
	// }
	// return (BasicDBObject) JSON.parse(sb.toString());
	// }
	// return null;
	// }
	/**
	 * 组合mongo中总量查询所相关查询结果输出内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月2日 上午11:22:30
	 * @param isCount 是否查询数量
	 * @return 总量查询所相关查询结果输出内容
	 */
	@SuppressWarnings({ "unchecked" })
	private List<Bson> assemblyMongoAggregateGroup(final boolean isCount) {
		// group
		// {$group:{_id:{"function":"$function",function_id:"$function_id"},users:{$push:"$users"},size:{$sum:1}}}
		if ((null != this.termScopes) && (null != this.listFieldScopes)) {
			final StringBuilder sb = new StringBuilder(16 + (16 * this.termScopes.size()));
			sb.append("[");
			boolean isFirst = true;
			if (!isCount) {
				if ((null != this.orderScopes) || ((null != this.listFieldScopes) && (null != this.listFieldScopes.getOrderScopes()))) {
					// 排序
					// {$sort:{"users.user_id":1}},
					sb.append("{$sort:{");
					isFirst = true;
					if (null != this.orderScopes) {
						for (final LocalScope s : this.orderScopes) {
							if (isFirst) {
								isFirst = false;
							} else {
								sb.append(',');
							}
							s.assemblyRelationalMongo(sb);
						}
					}
					if ((null != this.listFieldScopes) && (null != this.listFieldScopes.getOrderScopes())) {
						for (final LocalScope s : this.listFieldScopes.getOrderScopes()) {
							if (isFirst) {
								isFirst = false;
							} else {
								sb.append(',');
							}
							s.assemblyRelationalMongo(sb);
						}
					}
					sb.append("}},");
				}
				if (null != this.prb) {
					// 分页
					// {$skip:1},{$limit:2}
					sb.append("{$skip:").append(this.prb.getDataStartIndex()).append("},{$limit:").append(this.prb.getSize()).append("},");
				}
			}
			// 条件部分
			// _id:{"function":"$function",function_id:"$function_id"}
			sb.append("{$group:{_id:{");
			String tn;
			isFirst = true;
			for (final LocalScope ls : this.termScopes) {
				if (isFirst) {
					isFirst = false;
				} else {
					sb.append(',');
				}
				// 得到名称
				tn = ls.getTargetName();
				sb.append(tn);
				sb.append(":\"$");
				sb.append(tn);
				sb.append('"');
			}
			if (null != this.listFieldScopes.getTermScopes()) {
				// 加入子条件内容
				for (final LocalScope ls : this.listFieldScopes.getTermScopes()) {
					if (isFirst) {
						isFirst = false;
					} else {
						sb.append(',');
					}
					// 得到名称
					tn = ls.getTargetName();
					sb.append(ls.getTargetName());
					sb.append(":\"$");
					sb.append(ls.getTargetPathName());
					sb.append('"');
				}
			}
			sb.append("},");
			if (isCount) {
				// 显示数量
				// size:{$sum:1}
				sb.append("size:{$sum:1}}}");
			} else {
				// 显示分组字段
				// users:{$push:"$users"}
				final String afn = this.listFieldScopes.getTargetName();
				sb.append("list:{$push:\"$").append(afn).append("\"}}}");
			}
			if (CoreLog.getInstance().debugEnabled(QLBean.class)) {
				CoreLog.getInstance().debug(QLBean.class, "MongoThread[", Thread.currentThread().getName(), "] Aggregate Group > ", sb.toString());
			}
			sb.append("]");
			return (List<Bson>) JSON.parse(sb.toString());
		}
		return null;
	}

	/**
	 * 组合更新用的内容
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月22日 下午9:12:49
	 * @return 组合的对象
	 */
	private Bson assemblyMongoUpdate() {
		if (null != this.updateScopes) {
			final BasicDBObject bo = new BasicDBObject();
			for (final LocalScope ls : this.updateScopes) {
				ls.assemblyRelationalMongo(bo);
			}
			final BasicDBObject bak = new BasicDBObject();
			bak.append("$set", bo);
			if (CoreLog.getInstance().debugEnabled(QLBean.class)) {
				CoreLog.getInstance().debug(QLBean.class, "MongoThread[", Thread.currentThread().getName(), "] update > ", bak.toJson());
			}
			// if (QLBean.log.isDebugEnabled()) {
			// QLBean.log.debug("MongoThread[" + Thread.currentThread().getName() + "] update > " + bak.toJson());
			// }
			return bak;
		}
		return null;
	}

	/**
	 * 组合列表字段数据增加内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月2日 下午8:04:45
	 * @param mdd mongo数据库对象
	 * @return 组合的对象
	 */
	private Bson assemblyMongoFieldInsert(final MongoDatabaseData mdd) {
		// {$addToSet:{users:{$each:[{user_id:3,create_time:"2017-05-17 11:21:18",time:1494991278356},{user_id:4,create_time:"2017-05-17 12:21:18",time:1494992278356}]}}})
		if (null != this.listFieldScopes) {
			final StringBuilder sb = new StringBuilder();
			final String lfn = this.listFieldScopes.getTargetName();
			sb.append("{$addToSet:{").append(lfn).append(":{$each:[]}}}");
			final BasicDBObject bak = (BasicDBObject) JSON.parse(sb.toString());
			// this.listFieldScopes.assemblyRelationalMongo(sb);
			final BasicDBObject abo = (BasicDBObject) bak.get("$addToSet");
			final BasicDBObject fbo = (BasicDBObject) abo.get(lfn);
			final BasicDBList ebo = (BasicDBList) fbo.get("$each");
			this.listFieldScopes.assemblyRelationalMongo(ebo, mdd);
			if (CoreLog.getInstance().debugEnabled(QLBean.class)) {
				CoreLog.getInstance().debug(QLBean.class, "MongoThread[", Thread.currentThread().getName(), "] Field Insert > ", bak.toJson());
			}
			return bak;
		}
		return null;
	}

	/**
	 * 组合排序用内容
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月20日 上午10:53:00
	 * @return 组合的对象
	 */
	private Bson assemblyMongoSort() {
		if (null != this.orderScopes) {
			final BasicDBObject bo = new BasicDBObject();
			for (final LocalScope ls : this.orderScopes) {
				ls.assemblyRelationalMongo(bo);
			}
			return bo;
		}
		return null;
	}
}
