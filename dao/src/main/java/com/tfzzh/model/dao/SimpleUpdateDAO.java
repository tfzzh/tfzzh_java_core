/**
 * @author Xu Weijie
 * @dateTime 2012-11-20 下午12:53:30
 */
package com.tfzzh.model.dao;

import java.sql.SQLException;

import com.tfzzh.model.dao.tools.QLTermBean;
import com.tfzzh.model.dao.tools.QLUpdateBean;

/**
 * 基础简单更新DAO
 * 
 * @author Xu Weijie
 * @dateTime 2012-11-20 下午12:53:30
 */
public interface SimpleUpdateDAO {

	/**
	 * 条件更新数据行
	 * 
	 * @author XuWeijie
	 * @dateTime 2012-11-20 下午12:53:30
	 * @param updates 更新条件的集合
	 * @param terms 查询条件的集合
	 * @return 更新的数据行数量
	 * @throws SQLException 抛
	 */
	int updateData(QLUpdateBean updates, QLTermBean terms) throws SQLException;
}
