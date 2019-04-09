/**
 * @author Weijie Xu
 * @dateTime 2015年4月25日 下午4:46:59
 */
package com.tfzzh.model.dao.tools;

import com.tfzzh.model.bean.BaseDataBean;

/**
 * sql排序数据集合
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月25日 下午4:46:59
 */
public class QLOrderBean extends LocalScope {

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午4:47:02
	 * @param <E> 数据对象
	 * @param qlb 所属条件组合对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 * @param sort 排序条件
	 */
	protected <E extends BaseDataBean> QLOrderBean(final QLBean qlb, final EntityInfoBean<E>.FieldInfoBean tfb, final SortEnum sort) {
		super(qlb, tfb, sort);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午4:47:02
	 * @param <E> 数据对象
	 * @param nlb 所属列表属性对象
	 * @param tfb 表字段对象，主符号左边相关字段
	 * @param sort 排序条件
	 */
	protected <E extends BaseDataBean> QLOrderBean(final NSListFieldBean nlb, final EntityInfoBean<E>.FieldInfoBean tfb, final SortEnum sort) {
		super(nlb, tfb, sort);
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
		return QLBean.TYPE_ORDER;
	}
}
