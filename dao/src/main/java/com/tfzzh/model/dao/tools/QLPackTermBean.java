/**
 * @author Weijie Xu
 * @dateTime 2015年4月24日 上午10:03:06
 */
package com.tfzzh.model.dao.tools;

import com.tfzzh.model.bean.BaseDataBean;

/**
 * sql条件数据集合
 * 
 * @author Xu Weijie
 * @datetime 2018年3月1日_上午9:21:45
 */
public class QLPackTermBean extends LocalScope {

	/**
	 * 关系符号
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月8日 下午8:07:24
	 */
	private RelationalSymbolEnum rs = RelationalSymbolEnum.And;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午3:07:14
	 * @param <E> 数据对象
	 * @param qlb 所属条件组合对象
	 */
	public <E extends BaseDataBean> QLPackTermBean(final QLBean qlb) {
		super(qlb);
	}

	/**
	 * @author Xu Weijie
	 * @datetime 2018年3月1日_上午9:24:34
	 * @param <E> 数据对象
	 * @param qlb 所属条件组合对象
	 * @param parentLs 所相关的父域
	 */
	public <E extends BaseDataBean> QLPackTermBean(final QLBean qlb, final LocalScope parentLs) {
		super(qlb, (LocalScope) null);
	}

	/**
	 * 得到操作类型
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月23日 下午4:27:06
	 * @return 操作类型
	 * @see com.tfzzh.model.dao.tools.LocalScope#getType()
	 */
	@Override
	protected int getType() {
		return QLBean.TYPE_TERM;
	}

	/**
	 * 设置关系符号
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月8日 下午8:07:52
	 * @param rs the rs to set
	 */
	public void setRelationalSymbol(final RelationalSymbolEnum rs) {
		this.rs = rs;
	}

	/**
	 * 组合连接内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月9日 下午12:32:54
	 * @see com.tfzzh.model.dao.tools.LocalScope#assemblyRelationalSQL(java.lang.StringBuilder)
	 */
	@Override
	public void assemblyRelationalSQL(final StringBuilder sql) {
		sql.append(this.rs.getSQLText());
	}
}
