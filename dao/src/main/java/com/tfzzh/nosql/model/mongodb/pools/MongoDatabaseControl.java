/**
 * @author tfzzh
 * @dateTime 2016年11月18日 下午6:19:51
 */
package com.tfzzh.nosql.model.mongodb.pools;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tfzzh.model.bean.BaseDataBean;

/**
 * mongo库控制
 * 
 * @author tfzzh
 * @dateTime 2016年11月18日 下午6:19:51
 */
public class MongoDatabaseControl {

	/**
	 * mongo数据库信息
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午6:21:05
	 */
	private final MongoDatabase md;

	/**
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午6:21:02
	 * @param md 数据库信息
	 */
	private MongoDatabaseControl(final MongoDatabase md) {
		this.md = md;
	}

	/**
	 * 得到目标表数据
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午6:27:32
	 * @param tableName 目标表名称
	 * @return 目标表数据
	 */
	public MongoCollection<Document> getTable(final String tableName) {
		return this.md.getCollection(tableName);
	}

	/**
	 * 得到目标表数据
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午6:45:40
	 * @param <D> 数据实例
	 * @param tableName 表名称
	 * @param clz 对应的数据实际类对象
	 * @return 目标数据列表
	 */
	public <D extends BaseDataBean> MongoCollection<D> getTableData(final String tableName, final Class<D> clz) {
		final MongoCollection<D> mc = this.md.getCollection(tableName, clz);
		return mc;
	}
}
