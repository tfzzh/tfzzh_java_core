/**
 * @author Weijie Xu
 * @dateTime 2015年5月7日 下午4:30:59
 */
package com.tfzzh.model.dao.tools;

import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.tfzzh.exception.NotAvailableOperationModeException;
import com.tfzzh.model.bean.BaseDataBean;
import com.tfzzh.model.dao.tools.QLLocation.FieldLocation;
import com.tfzzh.model.dao.tools.QLLocation.SortLocation;
import com.tfzzh.model.dao.tools.QLLocation.ValueLocation;
import com.tfzzh.nosql.model.mongodb.MongoDatabaseData;

/**
 * 范围域，主要是更新条件，查询条件<br />
 * 这里不涉及排序以及分组<br />
 * 其下为各种占位内容<br />
 * 
 * @author Weijie Xu
 * @dateTime 2015年5月7日 下午4:30:59
 */
public abstract class LocalScope {

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午4:33:24
	 */
	protected final QLBean qlb;

	/**
	 * 所从属的父域，如果
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午2:01:32
	 */
	private final LocalScope parent;

	/**
	 * 目标占位，主符号左边相关字段
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:32:28
	 */
	private final FieldLocation<?> target;

	/**
	 * 主符号，针对修改，查询类条件
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:33:13
	 */
	private final SymbolEnum symbol;

	/**
	 * 排序条件，针对排序
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午4:51:46
	 */
	private final SortLocation sort;

	/**
	 * 条件占位列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午1:34:01
	 */
	protected final List<QLLocation> locs = new LinkedList<>();

	/**
	 * 所包含子域列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午9:01:18
	 */
	private List<LocalScope> childScopes = null;

	/**
	 * 主要用在条件子包中
	 * 
	 * @author Xu Weijie
	 * @datetime 2018年3月1日_上午9:18:50
	 * @param <E> 数据对象
	 * @param qlb 所属条件组合对象
	 */
	public <E extends BaseDataBean> LocalScope(final QLBean qlb) {
		this(qlb, (LocalScope) null);
	}

	/**
	 * 主要用在条件子包中
	 * 
	 * @author Xu Weijie
	 * @datetime 2018年2月28日_下午7:49:04
	 * @param <E> 数据对象
	 * @param qlb 所属条件组合对象
	 * @param parentLs 所相关的父域
	 */
	public <E extends BaseDataBean> LocalScope(final QLBean qlb, final LocalScope parentLs) {
		this.qlb = qlb;
		this.target = null;
		this.symbol = null;
		this.parent = parentLs;
		this.sort = null;
		if (null != this.parent) {
			this.parent.addChildScope(this);
		} else {
			// 放入到相关数据对象中
			this.qlb.addLocalScope(this);
		}
	}

	/**
	 * 主要用在更新的内容，以及常态查询
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午1:31:49
	 * @param <E> 数据对象
	 * @param qlb 所属条件组合对象
	 * @param fib 表字段对象，主符号左边相关字段
	 */
	public <E extends BaseDataBean> LocalScope(final QLBean qlb, final EntityInfoBean<E>.FieldInfoBean fib) {
		this(qlb, fib, SymbolEnum.Equal, null);
	}

	/**
	 * 主要用在常态查询，有父域时
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午2:27:35
	 * @param <E> 数据对象
	 * @param qlb 所属条件组合对象
	 * @param fib 表字段对象，主符号左边相关字段
	 * @param parentLs 所相关的父域
	 */
	public <E extends BaseDataBean> LocalScope(final QLBean qlb, final EntityInfoBean<E>.FieldInfoBean fib, final LocalScope parentLs) {
		this(qlb, fib, SymbolEnum.Equal, parentLs);
	}

	/**
	 * 主要用在各种条件处理上
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:33:47
	 * @param <E> 数据对象
	 * @param qlb 所属条件组合对象
	 * @param fib 表字段对象，主符号左边相关字段
	 * @param symbol 主符号
	 */
	public <E extends BaseDataBean> LocalScope(final QLBean qlb, final EntityInfoBean<E>.FieldInfoBean fib, final SymbolEnum symbol) {
		this(qlb, fib, symbol, null);
	}

	/**
	 * 用于更新及查询的条件
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午2:00:51
	 * @param <E> 数据对象
	 * @param qlb 所属条件组合对象
	 * @param fib 表字段对象，主符号左边相关字段
	 * @param symbol 主符号
	 * @param parentLs 所相关的父域
	 */
	public <E extends BaseDataBean> LocalScope(final QLBean qlb, final EntityInfoBean<E>.FieldInfoBean fib, final SymbolEnum symbol, final LocalScope parentLs) {
		this.qlb = qlb;
		this.target = fib.getFieldLocation();
		this.symbol = symbol;
		this.parent = parentLs;
		this.sort = null;
		if (null != this.parent) {
			this.parent.addChildScope(this);
		} else {
			// 放入到相关数据对象中
			this.qlb.addLocalScope(this);
		}
	}

	/**
	 * 用于排序的条件
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午4:56:37
	 * @param <E> 数据对象
	 * @param qlb 所属条件组合对象
	 * @param fib 表字段对象，主符号左边相关字段
	 * @param sort 排序条件规则
	 */
	public <E extends BaseDataBean> LocalScope(final QLBean qlb, final EntityInfoBean<E>.FieldInfoBean fib, final SortEnum sort) {
		this(qlb, fib, sort, null);
	}

	/**
	 * 用于排序的条件
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午4:56:37
	 * @param <E> 数据对象
	 * @param qlb 所属条件组合对象
	 * @param fib 表字段对象，主符号左边相关字段
	 * @param sort 排序条件规则
	 * @param parentLs 所相关的父域
	 */
	public <E extends BaseDataBean> LocalScope(final QLBean qlb, final EntityInfoBean<E>.FieldInfoBean fib, final SortEnum sort, final LocalScope parentLs) {
		this.qlb = qlb;
		this.target = fib.getFieldLocation();
		this.symbol = null;
		this.parent = parentLs;
		this.sort = sort.getSortLocation();
		if (null != this.parent) {
			this.parent.addChildScope(this);
		} else {
			// 放入到相关数据对象中
			this.qlb.addLocalScope(this);
		}
	}

	/**
	 * 主要用在更新的内容，以及常态查询
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月5日 下午2:16:41
	 * @param <E> 数据对象
	 * @param nlb 所属列表属性对象
	 * @param fib 表字段对象，主符号左边相关字段
	 */
	public <E extends BaseDataBean> LocalScope(final NSListFieldBean nlb, final EntityInfoBean<E>.FieldInfoBean fib) {
		this(nlb, fib, SymbolEnum.Equal, null);
	}

	/**
	 * 主要用在常态查询，有父域时
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月5日 下午2:16:41
	 * @param <E> 数据对象
	 * @param nlb 所属列表属性对象
	 * @param fib 表字段对象，主符号左边相关字段
	 * @param parentLs 所相关的父域
	 */
	public <E extends BaseDataBean> LocalScope(final NSListFieldBean nlb, final EntityInfoBean<E>.FieldInfoBean fib, final LocalScope parentLs) {
		this(nlb, fib, SymbolEnum.Equal, parentLs);
	}

	/**
	 * 主要用在各种条件处理上
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月5日 下午2:16:41
	 * @param <E> 数据对象
	 * @param nlb 所属列表属性对象
	 * @param fib 表字段对象，主符号左边相关字段
	 * @param symbol 主符号
	 */
	public <E extends BaseDataBean> LocalScope(final NSListFieldBean nlb, final EntityInfoBean<E>.FieldInfoBean fib, final SymbolEnum symbol) {
		this(nlb, fib, symbol, null);
	}

	/**
	 * 用于更新及查询的条件
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月5日 下午2:16:41
	 * @param <E> 数据对象
	 * @param nlb 所属列表属性对象
	 * @param fib 表字段对象，主符号左边相关字段
	 * @param symbol 主符号
	 * @param parentLs 所相关的父域
	 */
	public <E extends BaseDataBean> LocalScope(final NSListFieldBean nlb, final EntityInfoBean<E>.FieldInfoBean fib, final SymbolEnum symbol, final LocalScope parentLs) {
		this.qlb = nlb.qlb;
		this.target = fib.getFieldLocation().cloneWithParent(nlb.getTarget().getFieldPath());
		this.symbol = symbol;
		this.parent = parentLs;
		this.sort = null;
		if (null != this.parent) {
			this.parent.addChildScope(this);
		}
		// 放入到相关数据对象中
		nlb.addLocalScope(this);
	}

	/**
	 * 用于排序的条件
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午4:56:37
	 * @param <E> 数据对象
	 * @param nlb 所属列表属性对象
	 * @param fib 表字段对象，主符号左边相关字段
	 * @param sort 排序条件规则
	 */
	public <E extends BaseDataBean> LocalScope(final NSListFieldBean nlb, final EntityInfoBean<E>.FieldInfoBean fib, final SortEnum sort) {
		this(nlb, fib, sort, null);
	}

	/**
	 * 用于排序的条件
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月5日 下午2:16:43
	 * @param <E> 数据对象
	 * @param nlb 所属列表属性对象
	 * @param fib 表字段对象，主符号左边相关字段
	 * @param sort 排序条件规则
	 * @param parentLs 所相关的父域
	 */
	public <E extends BaseDataBean> LocalScope(final NSListFieldBean nlb, final EntityInfoBean<E>.FieldInfoBean fib, final SortEnum sort, final LocalScope parentLs) {
		this.qlb = nlb.qlb;
		this.target = fib.getFieldLocation().cloneWithParent(nlb.getTarget().getFieldPath());
		this.symbol = null;
		this.parent = parentLs;
		this.sort = sort.getSortLocation();
		if (null != this.parent) {
			this.parent.addChildScope(this);
		}
		// 放入到相关数据对象中
		nlb.addLocalScope(this);
	}

	/**
	 * 得到操作类型
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月23日 下午4:26:33
	 * @return 操作类型
	 */
	protected abstract int getType();

	/**
	 * 增加一个占位项
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午1:34:56
	 * @param loc 占位项
	 */
	protected void addLocation(final QLLocation loc) {
		this.locs.add(loc);
	}

	/**
	 * 组合连接内容，主要针对特殊的子类，需要提供必要的实现
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月9日 下午12:31:44
	 * @param sql sql语句
	 */
	public void assemblyRelationalSQL(final StringBuilder sql) {
		throw new NotAvailableOperationModeException("Cannt Use this Method...");
	}

	/**
	 * 增加子域
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午1:41:12
	 * @param ls 子域
	 */
	private void addChildScope(final LocalScope ls) {
		if (null == this.childScopes) {
			this.childScopes = new LinkedList<>();
		}
		this.childScopes.add(ls);
	}

	/**
	 * 得到所包含子域列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午1:40:22
	 * @return the childScopes
	 */
	public List<LocalScope> getChildScopes() {
		return this.childScopes;
	}

	/**
	 * 组合SQL
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午8:57:24
	 * @param sql sql语句
	 * @param ic 索引计数器
	 */
	protected void assemblySQL(final StringBuilder sql, final IndexCounter ic) {
		if (null == this.sort) {
			// 是条件或更新类型
			// this.target.assemblySQL(sql, ic);
			this.symbol.assemblyLocationSQL(this.target, sql, ic, this.locs);
		} else {
			// 是排序类型
			this.target.assemblySQL(sql, ic);
			this.sort.assemblySQL(sql, ic);
		}
	}

	/**
	 * 组合为实际的mongo用内容
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月22日 下午5:04:14
	 * @param bo 合并到对象
	 */
	public void assemblyRelationalMongo(final BasicDBObject bo) {
		if (null == this.sort) {
			this.symbol.assemblyLocationMongo(this.target, bo, this.locs);
		} else {
			// 是排序类型
			final StringBuilder ks = new StringBuilder();
			final StringBuilder vs = new StringBuilder();
			this.target.assemblyMongo(ks);
			this.sort.assemblyMongo(vs);
			bo.put(ks.toString(), vs.toString());
		}
	}

	/**
	 * 组合为实际的mongo用内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月2日 下午7:53:27
	 * @param bl 放入到列表
	 * @param mdd mongo数据库对象
	 */
	public void assemblyRelationalMongo(final BasicDBList bl, final MongoDatabaseData mdd) {
	}

	/**
	 * 组合为实际的mongo用的字串
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月9日 下午4:57:22
	 * @param sb 查询字串
	 */
	public void assemblyRelationalMongo(final StringBuilder sb) {
		if (null == this.sort) {
			this.symbol.assemblyLocationMongo(this.target, sb, this.locs);
		} else {
			// 是排序类型
			sb.append('"');
			this.target.assemblyMongo(sb);
			sb.append("\":");
			this.sort.assemblyMongo(sb);
		}
	}

	/**
	 * 得到所相关目标
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月6日 下午3:10:17
	 * @return 所相关目标
	 */
	protected FieldLocation<?> getTarget() {
		return this.target;
	}

	/**
	 * 只组合有目标对象的对象名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月1日 下午4:57:31
	 * @return 对象的对象名
	 */
	public String getTargetName() {
		if (null == this.target) {
			return "";
		} else {
			return this.target.getTargetName();
		}
	}

	/**
	 * 只组合有目标对象的对象路径名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月6日 下午7:46:18
	 * @return 对象的对象路径名
	 */
	public String getTargetPathName() {
		if (null == this.target) {
			return "";
		} else {
			return this.target.getTargetPathName();
		}
	}

	/**
	 * 增加一个字段
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午7:59:29
	 * @param <E> 实体数据
	 * @param fib 表字段
	 * @return 自身对象
	 */
	protected <E extends BaseDataBean> LocalScope addField(final EntityInfoBean<E>.FieldInfoBean fib) {
		this.locs.add(fib.getFieldLocation());
		this.qlb.validateEntityInfo(fib.getEntityInfo());
		return this;
	}

	/**
	 * 增加一个符号
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午7:59:32
	 * @param as 运算符号
	 * @return 自身对象
	 */
	protected LocalScope addArithmeticSymbol(final ArithmeticSymbolEnum as) {
		this.locs.add(as.getArithmeticSymbolLocation());
		return this;
	}

	/**
	 * 增加一个值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午7:59:49
	 * @param value 对应的值
	 * @return 自身对象
	 */
	protected LocalScope addValue(final Object value) {
		this.locs.add(new ValueLocation(this.qlb, value, this.target));
		return this;
	}
}
