/**
 * @author Weijie Xu
 * @dateTime 2015年4月25日 下午4:09:44
 */
package com.tfzzh.model.dao.tools;

import com.tfzzh.model.bean.BaseDataBean;

/**
 * sql更新数据集合
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月25日 下午4:09:44
 */
public class QLUpdateBean extends LocalScope {

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午4:09:51
	 * @param <E> 数据对象
	 * @param qlb 所属条件组合对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 */
	protected <E extends BaseDataBean> QLUpdateBean(final QLBean qlb, final EntityInfoBean<E>.FieldInfoBean tfb) {
		super(qlb, tfb);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 上午11:24:56
	 * @param <E> 数据对象
	 * @param qlb 所属条件组合对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 * @param symbol 主符号，一般仅针对更新时，字符串拼接
	 */
	protected <E extends BaseDataBean> QLUpdateBean(final QLBean qlb, final EntityInfoBean<E>.FieldInfoBean tfb, final SymbolEnum symbol) {
		super(qlb, tfb, symbol);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年6月6日 上午11:50:35
	 * @param <E> 数据对象
	 * @param nlb 所属列表属性对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 */
	protected <E extends BaseDataBean> QLUpdateBean(final NSListFieldBean nlb, final EntityInfoBean<E>.FieldInfoBean tfb) {
		super(nlb, tfb);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年6月6日 上午11:50:35
	 * @param <E> 数据对象
	 * @param nlb 所属列表属性对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 * @param symbol 主符号，一般仅针对更新时，字符串拼接
	 */
	protected <E extends BaseDataBean> QLUpdateBean(final NSListFieldBean nlb, final EntityInfoBean<E>.FieldInfoBean tfb, final SymbolEnum symbol) {
		super(nlb, tfb, symbol);
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
	public <E extends BaseDataBean> QLUpdateBean addField(final EntityInfoBean<E>.FieldInfoBean fib) {
		super.addField(fib);
		return this;
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
		return QLBean.TYPE_UPDATE;
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
	public QLUpdateBean addArithmeticSymbol(final ArithmeticSymbolEnum as) {
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
	public QLUpdateBean addValue(final Object value) {
		super.addValue(value);
		return this;
	}
}
