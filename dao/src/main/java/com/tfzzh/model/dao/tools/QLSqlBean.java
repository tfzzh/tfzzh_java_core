/**
 * @author Weijie Xu
 * @dateTime 2015年5月16日 下午6:07:07
 */
package com.tfzzh.model.dao.tools;

import java.util.LinkedHashSet;
import java.util.Set;

import com.tfzzh.model.dao.tools.QLLocation.ValueLocation;

/**
 * sql排序数据集合
 * 
 * @author Weijie Xu
 * @dateTime 2015年5月16日 下午6:07:07
 */
public class QLSqlBean {

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午4:33:24
	 */
	protected final QLBean qlb;

	/**
	 * 拼装sql用字串
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月16日 下午6:19:50
	 */
	private final StringBuilder sql;

	/**
	 * 索引计数器
	 * 
	 * @author tfzzh
	 * @dateTime 2016年9月21日 下午5:25:46
	 */
	private final IndexCounter ic = new IndexCounter();

	/**
	 * 条件占位列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午1:34:01
	 */
	private final Set<QLLocation> locs = new LinkedHashSet<>();

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年5月16日 下午6:07:07
	 * @param qlb 所属条件组合对象
	 */
	protected QLSqlBean(final QLBean qlb) {
		this.qlb = qlb;
		this.sql = new StringBuilder(50);
	}

	/**
	 * 得到拼装sql用字串
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月16日 下午6:20:24
	 * @return the sql
	 */
	public StringBuilder getSql() {
		return this.sql;
	}

	/**
	 * 增加一个值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午7:59:49
	 * @param value 对应的值
	 * @return 自身对象
	 */
	public QLSqlBean addValue(final Object value) {
		this.locs.add(new ValueLocation(this.qlb, value, null, this.ic));
		return this;
	}

	/**
	 * 得到条件占位列表
	 * 
	 * @author tfzzh
	 * @dateTime 2016年9月21日 下午4:43:01
	 * @return the locs
	 */
	protected Set<QLLocation> getLocations() {
		return this.locs;
	}
}
