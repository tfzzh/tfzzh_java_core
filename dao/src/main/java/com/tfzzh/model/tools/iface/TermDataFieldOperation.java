/**
 * @author Weijie Xu
 * @dateTime 2015年4月17日 下午8:36:27
 */
package com.tfzzh.model.tools.iface;

import com.tfzzh.model.dao.tools.QLTermBean;

/**
 * 有设置查询条件相关操作的
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月17日 下午8:36:27
 */
public interface TermDataFieldOperation {

	/**
	 * 设置对应的条件
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午8:37:25
	 * @param sql 组合的SQL
	 * @param terms 条件队列
	 * @param value 对应的值
	 */
	void setTerm(StringBuilder sql, QLTermBean terms, Object value);
}
