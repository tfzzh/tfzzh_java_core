/**
 * @author Xu Weijie
 * @datetime 2017年10月18日_上午11:32:17
 */
package com.tfzzh.nosql.model.mongodb.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.UpdateResult;
import com.tfzzh.model.dao.annotation.MongoDaoImpl;
import com.tfzzh.nosql.model.mongodb.dao.SimpleMongoDAO;

/**
 * 简单MongoDAO实现
 * 
 * @author Xu Weijie
 * @datetime 2017年10月18日_上午11:32:17
 */
@MongoDaoImpl
public class SimpleMongoDAOImpl extends CoreMongoDAOImpl implements SimpleMongoDAO {

	/**
	 * json条件查找目标数据
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月18日_下午6:44:00
	 * @param tableName 目标表名
	 * @param find 所相关条件json数据
	 * @param sort 排序json数据
	 * @param start 开始索引位，可空
	 * @param size 数据数量，可空
	 * @return 未被处理的结果数据集合
	 * @see com.tfzzh.nosql.model.mongodb.dao.SimpleMongoDAO#find(java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public MongoIterable<Document> find(final String tableName, final String find, final String sort, final Integer start, final Integer size) {
		final Bson findb = new BasicDBObject(JSON.parseObject(find, JSONReader.Feature.AllowUnQuotedFieldNames));
		final Bson sortb = new BasicDBObject(JSON.parseObject(sort, JSONReader.Feature.AllowUnQuotedFieldNames));
		return this.find(tableName, findb, sortb, start, size);
	}

	/**
	 * 对象条件查找目标数据
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月18日_下午2:04:39
	 * @param tableName 目标表名
	 * @param find 所相关条件数据对象
	 * @param sort 排序数据对象
	 * @param start 开始索引位，可空
	 * @param size 数据数量，可空
	 * @return 未被处理的结果数据集合
	 * @see com.tfzzh.nosql.model.mongodb.dao.SimpleMongoDAO#find(java.lang.String, org.bson.conversions.Bson,
	 *      org.bson.conversions.Bson, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public MongoIterable<Document> find(final String tableName, final Bson find, final Bson sort, final Integer start, final Integer size) {
		return super.getPool().find(tableName, find, sort, start, size);
	}

	/**
	 * json条件更新指定数据
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月18日_下午7:04:15
	 * @param tableName 目标表名
	 * @param find 所相关条件json数据
	 * @param update 待更新json数据
	 * @return 得到的结果集
	 * @see com.tfzzh.nosql.model.mongodb.dao.SimpleMongoDAO#update(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public UpdateResult update(final String tableName, final String find, final String update) {
		final Bson findb = new BasicDBObject(JSON.parseObject(find, JSONReader.Feature.AllowUnQuotedFieldNames));
		final Bson updateb = new BasicDBObject(JSON.parseObject(update, JSONReader.Feature.AllowUnQuotedFieldNames));
		return this.update(tableName, findb, updateb);
	}

	/**
	 * 对象条件更新指定数据<br />
	 * 默认为，如果目标对象不存在，则不更新<br />
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月18日_下午2:04:39
	 * @param tableName 目标表名
	 * @param find 所相关条件数据对象
	 * @param update 待更新数据对象
	 * @return 得到的结果集
	 * @see com.tfzzh.nosql.model.mongodb.dao.SimpleMongoDAO#update(java.lang.String, org.bson.conversions.Bson,
	 *      org.bson.conversions.Bson)
	 */
	@Override
	public UpdateResult update(final String tableName, final Bson find, final Bson update) {
		return this.update(tableName, find, update, false);
	}

	/**
	 * 对象条件更新指定数据
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月19日_下午1:30:29
	 * @param tableName 目标表名
	 * @param find 所相关条件数据对象
	 * @param update 待更新数据对象
	 * @param isUpsert 是否如果不存在则新增
	 * @return 得到的结果集
	 * @see com.tfzzh.nosql.model.mongodb.dao.SimpleMongoDAO#update(java.lang.String, org.bson.conversions.Bson,
	 *      org.bson.conversions.Bson, boolean)
	 */
	@Override
	public UpdateResult update(final String tableName, final Bson find, final Bson update, final boolean isUpsert) {
		return super.getPool().update(tableName, find, update, isUpsert);
	}

	/**
	 * 根据指定json条件，查询列表字段中内容
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月18日_下午7:27:26
	 * @param tableName 目标表名
	 * @param aggr 条件json集合，可包含sort信息
	 * @return 未被处理的结果数据集合
	 * @see com.tfzzh.nosql.model.mongodb.dao.SimpleMongoDAO#aggregateFieldFind(java.lang.String, java.lang.String[])
	 */
	@Override
	public MongoIterable<Document> aggregateFieldFind(final String tableName, final String... aggr) {
		if (null == aggr) {
			return super.getPool().aggregateFieldFind(tableName, null);
		}
		final List<Bson> bl = new ArrayList<>();
		for (final String s : aggr) {
			if (null != s) {
				bl.add(new BasicDBObject(JSON.parseObject(s, JSONReader.Feature.AllowUnQuotedFieldNames)));
			}
		}
		return super.getPool().aggregateFieldFind(tableName, bl);
	}

	/**
	 * 根据指定对象条件，查询列表字段中内容
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月18日_下午2:04:37
	 * @param tableName 目标表名
	 * @param aggr 条件集合，可包含sort信息
	 * @return 未被处理的结果数据集合
	 * @see com.tfzzh.nosql.model.mongodb.dao.SimpleMongoDAO#aggregateFieldFind(java.lang.String, java.util.List)
	 */
	@Override
	public MongoIterable<Document> aggregateFieldFind(final String tableName, final List<Bson> aggr) {
		return super.getPool().aggregateFieldFind(tableName, aggr);
	}
}
