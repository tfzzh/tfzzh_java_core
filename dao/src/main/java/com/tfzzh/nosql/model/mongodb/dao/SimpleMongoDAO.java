/**
 * @author Xu Weijie
 * @datetime 2017年10月18日_上午11:32:35
 */
package com.tfzzh.nosql.model.mongodb.dao;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.UpdateResult;
import com.tfzzh.model.dao.MongoDAO;

/**
 * 简单MongoDAO
 * 
 * @author Xu Weijie
 * @datetime 2017年10月18日_上午11:32:35
 */
public interface SimpleMongoDAO extends MongoDAO {

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
	 */
	MongoIterable<Document> find(String tableName, String find, String sort, Integer start, Integer size);

	/**
	 * 查找目标数据
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月18日_下午2:04:39
	 * @param tableName 目标表名
	 * @param find 所相关条件数据对象
	 * @param sort 排序数据对象
	 * @param start 开始索引位，可空
	 * @param size 数据数量，可空
	 * @return 未被处理的结果数据集合
	 */
	MongoIterable<Document> find(String tableName, Bson find, Bson sort, Integer start, Integer size);

	/**
	 * json条件更新指定数据
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月18日_下午7:04:15
	 * @param tableName 目标表名
	 * @param find 所相关条件json数据
	 * @param update 待更新json数据
	 * @return 得到的结果集
	 */
	UpdateResult update(String tableName, String find, String update);

	/**
	 * 更新指定数据
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月18日_下午2:04:39
	 * @param tableName 目标表名
	 * @param find 所相关条件数据对象
	 * @param update 待更新数据对象
	 * @return 得到的结果集
	 */
	UpdateResult update(String tableName, Bson find, Bson update);

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
	 */
	UpdateResult update(String tableName, Bson find, Bson update, boolean isUpsert);

	/**
	 * 根据指定json条件，查询列表字段中内容
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月18日_下午7:27:26
	 * @param tableName 目标表名
	 * @param aggr 条件json集合，可包含sort信息
	 * @return 未被处理的结果数据集合
	 */
	MongoIterable<Document> aggregateFieldFind(String tableName, String... aggr);

	/**
	 * 根据指定条件，查询列表字段中内容
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月18日_下午2:04:37
	 * @param tableName 目标表名
	 * @param aggr 条件集合，可包含sort信息
	 * @return 未被处理的结果数据集合
	 */
	MongoIterable<Document> aggregateFieldFind(String tableName, List<Bson> aggr);
}
