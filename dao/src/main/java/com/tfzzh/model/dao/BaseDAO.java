/**
 * @author Weijie Xu
 * @dateTime 2015年4月17日 下午7:10:56
 */
package com.tfzzh.model.dao;

import java.sql.SQLException;
import java.util.List;

import com.tfzzh.model.bean.BaseDataBean;
import com.tfzzh.model.dao.tools.QLBean;

/**
 * 基础DAO处理
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月17日 下午7:10:56
 * @param <E> 实体数据
 */
public interface BaseDAO<E extends BaseDataBean> extends SqlDAO {

	/**
	 * 创建新表或更新表结构
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午4:46:03
	 * @return -1，未成功；<br />
	 *         0，没有任何变化；<br />
	 *         1，创建的新表；<br />
	 *         2，更新的内容；<br />
	 * @throws SQLException 抛
	 */
	int createOrEditTable() throws SQLException;

	/**
	 * 创建或更新表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月28日 上午11:01:35
	 * @param tableName 表名称
	 * @return 0，创建表成功；<br />
	 *         >0，被更新的字段数量；<br />
	 *         <0，未创建或更新内容成功；<br />
	 * @throws SQLException 抛
	 */
	int createOrEditTable(String tableName) throws SQLException;

	/**
	 * 清空表数据
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月21日_下午2:15:59
	 * @throws SQLException 抛
	 */
	void truncateData() throws SQLException;

	/**
	 * 插入一条数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 上午10:41:45
	 * @param ent 目标数据
	 * @return -1，插入失败；<br />
	 *         0，未知问题，也许是没有自增ID的返回；<br />
	 *         >0，成功的值，并返回了自增ID<br />
	 * @throws SQLException 抛
	 */
	int insertData(E ent) throws SQLException;

	/**
	 * 一次性插入数量的数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午7:29:03
	 * @param entList 实体数据列表
	 * @return 针对列表的结果
	 * @throws SQLException 抛
	 */
	int[] insertDatas(List<E> entList) throws SQLException;

	/**
	 * 一次性插入数量的数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午7:29:03
	 * @param entList 实体数据列表
	 * @param tableName 目标表名
	 * @return 针对列表的结果
	 * @throws SQLException 抛
	 */
	int[] insertDatas(List<E> entList, String tableName) throws SQLException;

	/**
	 * 得到目标表下所有数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月16日 下午12:55:09
	 * @return 目标表下所有数据
	 * @throws SQLException 抛
	 */
	List<E> selectAllData() throws SQLException;

	/**
	 * 得到目标表下所有数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月16日 下午12:55:45
	 * @param tableName 目标表名
	 * @return 目标表下所有数据
	 * @throws SQLException 抛
	 */
	List<E> selectAllData(String tableName) throws SQLException;

	/**
	 * 按条件查询得到目标数据<br />
	 * 结果为符合目标的数据列表<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午8:44:35
	 * @param qlb 数据查询条件
	 * @return 符合目标条件的数据列表
	 * @throws SQLException 抛
	 */
	List<E> selectData(QLBean qlb) throws SQLException;

	/**
	 * 查询得到多条件数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月16日 下午1:44:22
	 * @param qlbs 条件列表
	 * @return 得到的数据集合
	 * @throws SQLException 抛
	 */
	List<E> selectMultiData(List<QLBean> qlbs) throws SQLException;

	/**
	 * 得到目标数据的总量
	 *
	 * @author tfzzh
	 * @dateTime 2016年10月11日 下午1:36:02
	 * @return 目标数据的总量
	 * @throws SQLException 抛
	 */
	int selectDataCount() throws SQLException;

	/**
	 * 按条件查询得到目标数据的总量
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午4:49:28
	 * @param qlb 数据查询条件
	 * @return 目标数据的总量
	 * @throws SQLException 抛
	 */
	int selectDataCount(QLBean qlb) throws SQLException;

	// /**
	// * 按条件查询得到目标数据的总量
	// *
	// * @author Weijie Xu
	// * @dateTime 2015年5月7日 下午4:49:29
	// * @param qlb 数据查询条件
	// * @param tableName 目标表名称
	// * @return 目标数据的总量
	// * @throws SQLException 抛
	// */
	// int selectDataCount(QLBean qlb, String tableName) throws SQLException;
	/**
	 * 按条件更新目标数据内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 上午10:31:20
	 * @param qlb 数据查询条件
	 * @return 目标数据的总量
	 * @throws SQLException 抛
	 */
	int updateData(QLBean qlb) throws SQLException;

	/**
	 * 以整个数据实体信息为参考进行数据更新<br />
	 * 不能针对没有主键的表操作<br />
	 * 以主键字段作为条件，其他字段均为被更新项<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午1:13:11
	 * @param ent 数据实体
	 * @return 目标数据的总量，应该只有一条
	 * @throws SQLException 抛
	 */
	int updateData(E ent) throws SQLException;

	/**
	 * 以数量的整个数据实体信息为参考进行数据更新<br />
	 * 不能针对没有主键的表操作<br />
	 * 以主键字段作为条件，其他字段均为被更新项<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午7:43:26
	 * @param ents 数据实体列表
	 * @return 被更新的数据情况列表；<br />
	 *         null，操作失败；<br />
	 * @throws SQLException 抛
	 */
	int[] updateDatas(List<E> ents) throws SQLException;

	// /**
	// * 按条件更新目标数据内容
	// *
	// * @author Weijie Xu
	// * @dateTime 2015年5月11日 上午10:31:19
	// * @param qlb 数据查询条件
	// * @param tableName 目标表名称
	// * @return 目标数据的总量
	// * @throws SQLException 抛
	// */
	// int updateData(QLBean qlb, String tableName) throws SQLException;
	/**
	 * 以整个数据实体信息为参考进行数据更新<br />
	 * 不能针对没有主键的表操作<br />
	 * 以主键字段作为条件，其他字段均为被更新项<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午1:13:11
	 * @param ent 数据实体
	 * @param tableName 目标表名称
	 * @return 目标数据的总量，应该只有一条
	 * @throws SQLException 抛
	 */
	int updateData(E ent, String tableName) throws SQLException;

	/**
	 * 以数量的整个数据实体信息为参考进行数据更新<br />
	 * 不能针对没有主键的表操作<br />
	 * 以主键字段作为条件，其他字段均为被更新项<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午7:43:26
	 * @param ents 数据实体列表
	 * @param tableName 目标表名称
	 * @return 被更新的数据情况列表；<br />
	 *         null，操作失败；<br />
	 * @throws SQLException 抛
	 */
	int[] updateDatas(List<E> ents, String tableName) throws SQLException;

	/**
	 * 插入及更新，先进行插入操作，如果失败，进行更新操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午1:09:55
	 * @param entity 目标实体数据
	 * @return 1，数据插入成功；<br />
	 *         2，数据更新成功；<br />
	 *         -1，插入与更新均失败；<br />
	 * @throws SQLException 抛
	 */
	int insertOrUpdate(E entity) throws SQLException;

	/**
	 * 批量插入及更新整个对象的数据，先进行批量更新操作，如果失败，进行批量插入操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午8:35:12
	 * @param ents 目标实体数据列表
	 * @return 批量处理的结果：<br />
	 *         负数，表示更新成功，并被更新的数据数量，一般为1；<br />
	 *         整数，表示插入成功，为插入后自增列的值；<br />
	 *         0，一般是插入成功，但该数据没有自增列；<br />
	 * @throws SQLException 抛
	 */
	int[] insertOrUpdates(List<E> ents) throws SQLException;

	/**
	 * 按条件删除目标数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 上午9:43:05
	 * @param qlb 数据查询条件
	 * @return 目标数据的总量
	 * @throws SQLException 抛
	 */
	int deleteData(QLBean qlb) throws SQLException;

	// /**
	// * 按条件删除目标数据
	// *
	// * @author Weijie Xu
	// * @dateTime 2015年5月11日 上午9:43:04
	// * @param qlb 数据查询条件
	// * @param tableName 目标表名称
	// * @return 目标数据的总量
	// * @throws SQLException 抛
	// */
	// int deleteData(QLBean qlb, String tableName) throws SQLException;
	/**
	 * 清除表数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午8:51:37
	 * @return 结果
	 * @throws SQLException 抛
	 */
	boolean clearData() throws SQLException;

	/**
	 * 清除表数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月11日 下午8:51:39
	 * @param tableName 目标表名
	 * @return 结果
	 * @throws SQLException 抛
	 */
	boolean clearData(String tableName) throws SQLException;
}
