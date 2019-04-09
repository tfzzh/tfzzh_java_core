/**
 * @author Weijie Xu
 * @dateTime 2015年4月24日 上午10:03:06
 */
package com.tfzzh.model.dao.tools;

import com.tfzzh.model.bean.BaseDataBean;

/**
 * sql条件数据集合
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月24日 上午10:03:06
 */
public class QLTermBean extends LocalScope {

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
	 * @param tfb 表字段对象，主符号左边相关字段
	 */
	public <E extends BaseDataBean> QLTermBean(final QLBean qlb, final EntityInfoBean<E>.FieldInfoBean tfb) {
		super(qlb, tfb);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午4:43:29
	 * @param <E> 数据对象
	 * @param qlb 所属条件组合对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 * @param parentLs 所相关的父域
	 */
	public <E extends BaseDataBean> QLTermBean(final QLBean qlb, final EntityInfoBean<E>.FieldInfoBean tfb, final LocalScope parentLs) {
		super(qlb, tfb, parentLs);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午3:07:13
	 * @param <E> 数据对象
	 * @param qlb 所属条件组合对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 * @param symbol 主符号
	 */
	public <E extends BaseDataBean> QLTermBean(final QLBean qlb, final EntityInfoBean<E>.FieldInfoBean tfb, final SymbolEnum symbol) {
		super(qlb, tfb, symbol);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午4:43:30
	 * @param <E> 数据对象
	 * @param qlb 所属条件组合对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 * @param symbol 主符号
	 * @param parentLs 所相关的父域
	 */
	public <E extends BaseDataBean> QLTermBean(final QLBean qlb, final EntityInfoBean<E>.FieldInfoBean tfb, final SymbolEnum symbol, final LocalScope parentLs) {
		super(qlb, tfb, symbol, parentLs);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午3:07:14
	 * @param <E> 数据对象
	 * @param nlb 所属列表属性对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 */
	public <E extends BaseDataBean> QLTermBean(final NSListFieldBean nlb, final EntityInfoBean<E>.FieldInfoBean tfb) {
		super(nlb, tfb);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午4:43:29
	 * @param <E> 数据对象
	 * @param nlb 所属列表属性对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 * @param parentLs 所相关的父域
	 */
	public <E extends BaseDataBean> QLTermBean(final NSListFieldBean nlb, final EntityInfoBean<E>.FieldInfoBean tfb, final LocalScope parentLs) {
		super(nlb, tfb, parentLs);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午3:07:13
	 * @param <E> 数据对象
	 * @param nlb 所属列表属性对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 * @param symbol 主符号
	 */
	public <E extends BaseDataBean> QLTermBean(final NSListFieldBean nlb, final EntityInfoBean<E>.FieldInfoBean tfb, final SymbolEnum symbol) {
		super(nlb, tfb, symbol);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午4:43:30
	 * @param <E> 数据对象
	 * @param nlb 所属列表属性对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 * @param symbol 主符号
	 * @param parentLs 所相关的父域
	 */
	public <E extends BaseDataBean> QLTermBean(final NSListFieldBean nlb, final EntityInfoBean<E>.FieldInfoBean tfb, final SymbolEnum symbol, final LocalScope parentLs) {
		super(nlb, tfb, symbol, parentLs);
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

	/**
	 * 增加一个字段
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午7:59:29
	 * @param <E> 实体数据
	 * @param fib 表字段
	 * @return 自身对象
	 * @see com.tfzzh.model.dao.tools.LocalScope#addField(com.tfzzh.model.dao.tools.EntityInfoBean.FieldInfoBean)
	 */
	@Override
	public <E extends BaseDataBean> QLTermBean addField(final EntityInfoBean<E>.FieldInfoBean fib) {
		super.addField(fib);
		return this;
	}

	/**
	 * 增加一个符号
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午7:59:32
	 * @param as 运算符号
	 * @return 自身对象
	 * @see com.tfzzh.model.dao.tools.LocalScope#addArithmeticSymbol(com.tfzzh.model.dao.tools.ArithmeticSymbolEnum)
	 */
	@Override
	public QLTermBean addArithmeticSymbol(final ArithmeticSymbolEnum as) {
		super.addArithmeticSymbol(as);
		return this;
	}

	/**
	 * 增加一个值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午7:59:49
	 * @param value 对应的值
	 * @return 自身对象
	 * @see com.tfzzh.model.dao.tools.LocalScope#addValue(java.lang.Object)
	 */
	@Override
	public QLTermBean addValue(final Object value) {
		super.addValue(value);
		return this;
	}
}
