/**
 * @author tfzzh
 * @dateTime 2016年12月6日 下午7:01:02
 */
package com.tfzzh.nosql.model.mongodb.dao;

import java.util.List;

import com.tfzzh.model.bean.BaseMongoBean;
import com.tfzzh.model.dao.MongoDAO;
import com.tfzzh.model.dao.tools.EntityInfoBean;
import com.tfzzh.model.dao.tools.QLBean;

/**
 * 基于mongo的da基类
 * 
 * @author tfzzh
 * @dateTime 2016年12月6日 下午7:01:02
 * @param <E> 基于mongo的数据实例对象
 */
public interface BaseMongoDAO<E extends BaseMongoBean> extends MongoDAO {

	/**
	 * 增加一条新数据
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月6日 下午12:43:15
	 * @param ent 新数据
	 * @return 新增加的数据，如果存在自增ID，则自增ID值
	 */
	long insertData(E ent);

	/**
	 * 插入数量数据（未测试）
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月16日 下午4:58:00
	 * @param el 数据实例列表
	 * @return 被创建的数据自增字段的值列表
	 */
	List<Long> insertDatas(List<E> el);

	/**
	 * 向指定条件数据中指定列表字段中增加数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月2日 下午7:05:18
	 * @param qb 更新的数据条件
	 * @return 被更新的数据行数
	 */
	long insertFieldDatas(QLBean qb);

	/**
	 * 得到所有数据列表
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月7日 上午11:06:32
	 * @return 所有数据列表
	 */
	List<E> findAllData();

	/**
	 * 得到条件数据列表
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月7日 上午11:06:33
	 * @param qb 数据条件
	 * @return 条件数据列表
	 */
	List<E> findData(QLBean qb);

	/**
	 * 得到目标条件中列表字段内容数量
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月2日 下午1:41:20
	 * @param qb 数据条件
	 * @return 目标列表字段内容数量
	 */
	int aggregateFieldCount(QLBean qb);

	/**
	 * 得到条件的列表字段中数据列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月6日 下午4:10:32
	 * @param <N> 目标实例对象
	 * @param qb 数据条件
	 * @param eib 相关数据实体的类信息
	 * @return 条件数据列表
	 */
	<N extends BaseMongoBean> List<N> aggregateFieldFind(QLBean qb, EntityInfoBean<N> eib);

	/**
	 * 根据指定条件更新数据
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月6日 下午12:43:16
	 * @param qb 指定条件
	 * @return 被更新的数据量
	 */
	long updateData(QLBean qb);

	/**
	 * 删除数据
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月20日 下午12:06:56
	 * @param qb 数据条件
	 * @return 被删除的数据量
	 */
	long deleteData(QLBean qb);
}
