/**
 * @author tfzzh
 * @dateTime 2016年12月6日 下午7:01:48
 */
package com.tfzzh.nosql.model.mongodb.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.tfzzh.model.bean.BaseMongoBean;
import com.tfzzh.model.dao.tools.EntityInfoBean;
import com.tfzzh.model.dao.tools.QLBean;
import com.tfzzh.nosql.model.mongodb.MongoDatabaseData;

/**
 * 基于mongo的dao实现基类
 * 
 * @author tfzzh
 * @dateTime 2016年12月6日 下午7:01:48
 * @param <E> 基于mongo的数据实例对象
 */
public abstract class BaseMongoDAOImpl<E extends BaseMongoBean> extends CoreMongoDAOImpl {

	/**
	 * 相关数据实体的类信息
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午12:54:42
	 */
	private final EntityInfoBean<E> eib = this.getEntityInfo();

	/**
	 * 得到数据实体的类信息
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 上午11:26:42
	 * @return 数据实体的类信息
	 */
	protected abstract EntityInfoBean<E> getEntityInfo();

	/**
	 * 插入数据
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月16日 下午4:57:59
	 * @param ent 数据实例
	 * @return 被创建的数据自增字段的值
	 */
	public long insertData(final E ent) {
		final MongoDatabaseData mdd = super.getPool();
		return mdd.insert(ent);
	}

	/**
	 * 插入数量数据（未测试）
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月16日 下午4:58:00
	 * @param el 数据实例列表
	 * @return 被创建的数据自增字段的值列表
	 */
	public List<Long> insertDatas(final List<E> el) {
		final MongoDatabaseData mdd = super.getPool();
		final List<Long> ll = new ArrayList<>(el.size());
		for (final E e : el) {
			ll.add(mdd.insert(e));
		}
		return ll;
	}

	/**
	 * 向指定条件数据中指定列表字段中增加数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月2日 下午7:05:18
	 * @param qb 更新的数据条件
	 * @return 被更新的数据行数
	 */
	public long insertFieldDatas(final QLBean qb) {
		final MongoDatabaseData mdd = super.getPool();
		return mdd.insertField(qb);
	}

	/**
	 * 得到所有数据列表
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月7日 上午11:06:32
	 * @return 所有数据列表
	 */
	public List<E> findAllData() {
		return this.findData(null);
	}

	/**
	 * 得到条件数据列表
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月7日 上午11:06:33
	 * @param qb 数据条件
	 * @return 条件数据列表
	 */
	public List<E> findData(final QLBean qb) {
		final MongoDatabaseData mdd = super.getPool();
		return mdd.find(qb, this.eib);
	}

	/**
	 * 得到条件数据的子列表数据数量
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月2日 上午11:52:10
	 * @param qb 数据条件
	 * @return 条件数据列表
	 */
	public int aggregateFieldCount(final QLBean qb) {
		final MongoDatabaseData mdd = super.getPool();
		return mdd.aggregateFieldCount(qb);
	}

	/**
	 * 得到条件的列表字段中数据列表<br />
	 * 根据指定条件得到目标数据，以及在目标数据列表字段中，查找到指定条件数据，并返回数据列表<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月6日 下午4:10:32
	 * @param <N> 目标实例对象
	 * @param qb 数据条件
	 * @param eib 结束相关数据实体的类信息
	 * @return 条件数据列表
	 */
	public <N extends BaseMongoBean> List<N> aggregateFieldFind(final QLBean qb, final EntityInfoBean<N> eib) {
		final MongoDatabaseData mdd = super.getPool();
		return mdd.aggregateFieldFind(qb, eib);
	}

	/**
	 * 更新数据
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月16日 下午4:58:03
	 * @param qb 数据条件
	 * @return 被更新的数据数量
	 */
	public long updateData(final QLBean qb) {
		final MongoDatabaseData mdd = super.getPool();
		return mdd.update(qb);
	}

	/**
	 * 删除数据
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月20日 下午12:06:56
	 * @param qb 数据条件
	 * @return 被删除的数据量
	 */
	public long deleteData(final QLBean qb) {
		final MongoDatabaseData mdd = super.getPool();
		return mdd.delete(qb);
	}
}
