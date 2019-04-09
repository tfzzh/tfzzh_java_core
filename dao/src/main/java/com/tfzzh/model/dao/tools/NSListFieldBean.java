/**
 * @author Weijie Xu
 * @dateTime 2017年6月2日 下午2:23:13
 */
package com.tfzzh.model.dao.tools;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.tfzzh.exception.ConfigurationException;
import com.tfzzh.model.bean.BaseDataBean;
import com.tfzzh.model.bean.BaseMongoBean;
import com.tfzzh.nosql.model.mongodb.MongoDatabaseData;

/**
 * nosql相关列表字段
 * 
 * @author Weijie Xu
 * @dateTime 2017年6月2日 下午2:23:13
 */
public class NSListFieldBean extends LocalScope {

	/**
	 * 需要被新放入到列表字段的数据列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月2日 下午4:20:07
	 */
	private List<Object> insertList = null;

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
	private LocalScope listFieldScopes = null;

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年6月2日 下午2:23:29
	 * @param <E> 数据对象
	 * @param qlb 所属条件组合对象
	 * @param fib 表字段对象，主符号左边相关字段
	 */
	public <E extends BaseDataBean> NSListFieldBean(final QLBean qlb, final EntityInfoBean<E>.FieldInfoBean fib) {
		super(qlb, fib);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年6月2日 下午2:23:29
	 * @param <E> 数据对象
	 * @param nlb 所属列表属性对象
	 * @param fib 表字段对象，主符号左边相关字段
	 */
	public <E extends BaseDataBean> NSListFieldBean(final NSListFieldBean nlb, final EntityInfoBean<E>.FieldInfoBean fib) {
		super(nlb, fib);
	}

	/**
	 * 得到操作类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月2日 下午2:23:35
	 * @return 操作类型
	 * @see com.tfzzh.model.dao.tools.LocalScope#getType()
	 */
	@Override
	protected int getType() {
		return QLBean.TYPE_LIST_FIELD;
	}

	/**
	 * 增加一个域，此方式建立在，该对象仅可能存在于单一线程中
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月5日 下午1:45:14
	 * @param ls 域
	 */
	protected void addLocalScope(final LocalScope ls) {
		switch (ls.getType()) {
		case QLBean.TYPE_TERM:
			if (null == this.termScopes) {
				this.termScopes = new ArrayList<>(3);
			}
			this.termScopes.add(ls);
			break;
		case QLBean.TYPE_UPDATE:
			if (null == this.updateScopes) {
				this.updateScopes = new ArrayList<>(2);
			}
			this.updateScopes.add(ls);
			break;
		case QLBean.TYPE_ORDER:
			if (null == this.orderScopes) {
				this.orderScopes = new ArrayList<>(1);
			}
			this.orderScopes.add(ls);
			break;
		case QLBean.TYPE_LIST_FIELD:
			if (null == this.listFieldScopes) {
				this.listFieldScopes = ls;
			} else {
				throw new ConfigurationException(" Can't two List Field in once operation ... ");
			}
		}
	}

	/**
	 * 增加一个字段
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月5日 下午2:16:11
	 * @param <E> 实体数据
	 * @param fib 表字段
	 * @return 自身对象
	 * @see com.tfzzh.model.dao.tools.LocalScope#addField(com.tfzzh.model.dao.tools.EntityInfoBean.FieldInfoBean)
	 */
	@Override
	protected <E extends BaseDataBean> LocalScope addField(final EntityInfoBean<E>.FieldInfoBean fib) {
		this.locs.add(fib.getFieldLocation());
		return this;
	}

	/**
	 * 增加需要被插入的值对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月2日 下午4:25:51
	 * @param value 目标值对象
	 * @return 自己
	 * @see com.tfzzh.model.dao.tools.LocalScope#addValue(java.lang.Object)
	 */
	@Override
	public LocalScope addValue(final Object value) {
		if (null == this.insertList) {
			this.insertList = new LinkedList<>();
		}
		this.insertList.add(value);
		return this;
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
		return new QLTermBean(this, tfb, symbol, parentLs);
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
	 * 组合为实际的mongo用内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月2日 下午7:54:36
	 * @param bl 放入到列表
	 * @param mdd mongo数据库对象
	 * @see com.tfzzh.model.dao.tools.LocalScope#assemblyRelationalMongo(com.mongodb.BasicDBList, com.tfzzh.nosql.model.mongodb.MongoDatabaseData)
	 */
	@Override
	public void assemblyRelationalMongo(final BasicDBList bl, final MongoDatabaseData mdd) {
		if (null == this.insertList) {
			return;
		}
		for (final Object o : this.insertList) {
			if (o instanceof BaseMongoBean) {
				final BaseMongoBean bmb = (BaseMongoBean) o;
				mdd.putIncrementValue(bmb);
				// TODO 标注，待删除
				bl.add(bmb.getInsertData());
			} else {
				bl.add(o);
			}
		}
	}

	/**
	 * 得到条件用范围域集合，主域集合，不直接包含任何子域
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月5日 下午7:55:17
	 * @return the termScopes
	 */
	protected List<LocalScope> getTermScopes() {
		return this.termScopes;
	}

	/**
	 * 得到更新用范围域集合
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月5日 下午7:55:17
	 * @return the updateScopes
	 */
	protected List<LocalScope> getUpdateScopes() {
		return this.updateScopes;
	}

	/**
	 * 得到排序用范围域集合
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月5日 下午7:55:17
	 * @return the orderScopes
	 */
	protected List<LocalScope> getOrderScopes() {
		return this.orderScopes;
	}
}
