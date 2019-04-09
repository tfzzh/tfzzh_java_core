/**
 * @author Weijie Xu
 * @dateTime 2015年4月25日 下午5:53:30
 */
package com.tfzzh.model.dao.impl;

import com.tfzzh.model.bean.BaseEntityBean;
import com.tfzzh.model.dao.tools.EntityInfoBean;
import com.tfzzh.model.dao.tools.EntityTool;

/**
 * DAO处理工厂
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月25日 下午5:53:30
 */
public class DAOProcessFactory {

	/**
	 * 对象唯一实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午6:11:27
	 */
	private final static DAOProcessFactory factory = new DAOProcessFactory();

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午6:11:28
	 */
	private DAOProcessFactory() {
	}

	/**
	 * 得到对象实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午6:11:29
	 * @return 对象唯一实例
	 */
	public static DAOProcessFactory getInstance() {
		return DAOProcessFactory.factory;
	}

	/**
	 * 得到创建表用的SQL语句
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 下午4:08:32
	 * @param <E> 数据相关实体
	 * @param clz 数据实体对象
	 * @param tableName 表名称
	 * @return 创建表用SQL
	 */
	public <E extends BaseEntityBean> String getCreateTableSQL(final Class<E> clz, final String tableName) {
		final EntityInfoBean<E> eib = EntityTool.getInstance().getEntityInfo(clz);
		return eib.getCreateSQL(tableName);
	}
}
