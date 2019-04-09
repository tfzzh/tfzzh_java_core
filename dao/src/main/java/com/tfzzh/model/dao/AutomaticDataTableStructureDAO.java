/**
 * @author XuWeijie
 * @datetime 2015年9月18日_下午1:07:51
 */
package com.tfzzh.model.dao;

import com.tfzzh.model.bean.BaseDataBean;

/**
 * 自动处理表结构
 * 
 * @author XuWeijie
 * @datetime 2015年9月18日_下午1:07:51
 * @param <E> 数据实例
 */
public interface AutomaticDataTableStructureDAO<E extends BaseDataBean> extends BaseDAO<E> {

	// /**
	// * 创建新表或更新表结构
	// *
	// * @author Weijie Xu
	// * @datetime 2015年9月18日_下午1:07:51
	// * @return -1，未成功；<br />
	// * 0，没有任何变化；<br />
	// * 1，创建的新表；<br />
	// * 2，更新的内容；<br />
	// * @throws SQLException 抛
	// */
	// int createOrEditTable() throws SQLException;
	/**
	 * 执行初始化数据
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月12日_下午2:59:37
	 */
	void execInitData();
}
