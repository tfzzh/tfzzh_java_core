/**
 * @author XuWeijie
 * @dateTime 2016年9月21日 上午10:43:17
 */
package com.tfzzh.model.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.tfzzh.model.bean.BaseEntityBean;
import com.tfzzh.model.dao.tools.QLBean;

/**
 * 基础简单查询DAO
 * 
 * @author XuWeijie
 * @dateTime 2016年9月21日 上午10:43:17
 * @model
 */
public interface SimpleSelectDAO {

	/**
	 * 查询得到指定表中所有信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-4 下午3:50:30
	 * @param <B> 数据对象
	 * @param clz 数据对象类
	 * @param qb 条件的集合
	 * @return 得到的数据列表，不可能为null
	 * @throws SQLException 抛
	 * @throws IllegalAccessException 抛
	 * @throws InstantiationException 抛
	 */
	<B extends BaseEntityBean> List<B> getDataList(Class<B> clz, QLBean qb) throws SQLException, InstantiationException, IllegalAccessException;

	/**
	 * 得到单一数据列表
	 * 
	 * @author tfzzh
	 * @dateTime 2024年1月25日 18:56:33
	 * @param <O> 单一数据对象
	 * @param clz 单一数据对象类
	 * @param qb 条件的集合
	 * @return 得到的数据列表，不可能为null
	 * @throws SQLException 抛
	 * @throws IllegalAccessException 抛
	 * @throws InstantiationException 抛
	 */
	<O extends Object> List<O> getOnlyDataList(Class<O> clz, QLBean qb) throws SQLException, InstantiationException, IllegalAccessException;

	/**
	 * 查询得到指定表中指定信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2016年9月21日 上午10:43:17
	 * @param qb 条件的集合
	 * @return 得到的数据列表，不可能为null
	 * @throws SQLException 抛
	 * @throws IllegalAccessException 抛
	 * @throws InstantiationException 抛
	 */
	List<Map<String, Object>> getDataList(QLBean qb) throws SQLException, InstantiationException, IllegalAccessException;

	/**
	 * 得到目标sql得到的数量结果<br />
	 * sql语句从from开始写，会固定在前面增加内容“select count(0) ”<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2016年10月11日 下午12:44:19
	 * @param qb 条件的集合
	 * @return 数量结果 >=0，正常返回；<br />
	 *         -1，条件不正确；<br />
	 *         -2，与数据库连接错误；<br />
	 *         -3，查询错误；<br />
	 * @throws SQLException 抛
	 * @throws IllegalAccessException 抛
	 * @throws InstantiationException 抛
	 */
	int getDataCount(QLBean qb) throws SQLException, InstantiationException, IllegalAccessException;

	/**
	 * 单纯的执行sql，一般为更新或删除等，仅会返回一个结果数量
	 * 
	 * @author tfzzh
	 * @dateTime 2016年10月27日 下午1:35:34
	 * @param qb 条件的集合
	 * @return 数量结果 >=0，正常返回；<br />
	 *         -1，条件不正确；<br />
	 *         -2，与数据库连接错误；<br />
	 *         -3，查询错误；<br />
	 * @throws SQLException 抛
	 * @throws IllegalAccessException 抛
	 * @throws InstantiationException 抛
	 */
	int execData(QLBean qb) throws SQLException, InstantiationException, IllegalAccessException;
}
