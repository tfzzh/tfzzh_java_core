/**
 * @author Weijie Xu
 * @dateTime 2015年4月24日 下午1:58:51
 */
package com.tfzzh.model.dao.tools;

import com.tfzzh.model.bean.BaseEntityBean;
import com.tfzzh.model.dao.BaseDAO;

/**
 * 在sql中表作用域
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月24日 下午1:58:51
 * @param <E> 目标相关数据对象
 * @param <D> 目标表相关DAO
 */
@Deprecated
public class TableScope<E extends BaseEntityBean, D extends BaseDAO<E>> {

	/**
	 * 域名称
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午2:10:53
	 */
	private final String name;

	/**
	 * 实际相关的DAO接口实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午2:12:09
	 */
	private final D dao;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午2:12:07
	 * @param name 域名称
	 * @param dao 实际相关的DAO接口实例
	 */
	public TableScope(final String name, final D dao) {
		this.name = name;
		this.dao = dao;
	}

	/**
	 * 得到域名称
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午2:12:53
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 得到实际相关的DAO接口实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午2:12:53
	 * @return the dao
	 */
	public D getDao() {
		return this.dao;
	}

	/**
	 * 得到目标字段作用域
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午1:14:36
	 * @param tfb 字段信息对象
	 * @return 字段作用域
	 */
	public FieldScope getFieldScope(final EntityInfoBean<E>.FieldInfoBean tfb) {
		return new FieldScope(tfb);
	}

	/**
	 * 在sql中字段作用域
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午4:10:07
	 */
	protected class FieldScope {

		/**
		 * 数据表中字段
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月24日 下午4:13:21
		 */
		private final EntityInfoBean<E>.FieldInfoBean tfb;

		/**
		 * @author Weijie Xu
		 * @dateTime 2015年4月24日 下午4:13:24
		 * @param tfb 数据表中字段
		 */
		protected FieldScope(final EntityInfoBean<E>.FieldInfoBean tfb) {
			this.tfb = tfb;
		}

		/**
		 * 得到相关的表中字段对象
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月24日 下午4:15:11
		 * @return 表中字段对象
		 */
		protected EntityInfoBean<E>.FieldInfoBean getTableField() {
			return this.tfb;
		}

		/**
		 * 得到所在的域
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月24日 下午4:15:12
		 * @return 所在的域对象
		 */
		protected TableScope<E, D> getTableScope() {
			return TableScope.this;
		}
	}
}
