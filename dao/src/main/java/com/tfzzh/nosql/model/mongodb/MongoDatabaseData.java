/**
 * @author tfzzh
 * @dateTime 2016年12月5日 下午1:47:11
 */
package com.tfzzh.nosql.model.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.tfzzh.model.bean.BaseMongoBean;
import com.tfzzh.model.dao.tools.EntityInfoBean;
import com.tfzzh.model.dao.tools.QLBean;

/**
 * mongo数据库对象包装类
 * 
 * @author tfzzh
 * @dateTime 2016年12月5日 下午1:47:11
 */
public class MongoDatabaseData {

	/**
	 * 所相关mongo的数据库数据对象
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月5日 下午1:47:48
	 */
	private final MongoDatabase md;

	/**
	 * @author tfzzh
	 * @dateTime 2016年12月5日 下午1:48:38
	 * @param md 所相关mongo的数据库数据对象
	 */
	public MongoDatabaseData(final MongoDatabase md) {
		this.md = md;
	}

	// /**
	// * 得到指定表中所有数据
	// *
	// * @author tfzzh
	// * @dateTime 2016年12月12日 上午11:01:37
	// * @param <E> 目标数据对象
	// * @param tableName 数据表名称
	// * @param clz 数据类对象
	// * @return 数据列表
	// */
	// public <E extends BaseMongoBean> List<E> getDataList(final String tableName, final Class<E> clz) {
	// final MongoCollection<Document> mCol = this.md.getCollection(tableName);
	// final List<E> el = new ArrayList<>();
	// E e;
	// for (final Document doc : mCol.find()) {
	// e = this.assemData(doc, clz);
	// if (null != e) {
	// el.add(e);
	// }
	// }
	// return el;
	// }
	/**
	 * 得到条件数据
	 *
	 * @author tfzzh
	 * @dateTime 2016年12月12日 上午11:01:36
	 * @param <E> 目标数据对象
	 * @param qb 数据条件
	 * @param clz 数据类对象
	 * @return 数据列表
	 */
	public <E extends BaseMongoBean> List<E> getDataList(final QLBean qb, final Class<E> clz) {
		final MongoCollection<Document> mCol = this.md.getCollection(qb.getTableName());
		final MongoIterable<Document> fi = qb.assemblyMongoFind(mCol);
		final List<E> el = new ArrayList<>();
		if (null != fi) {
			E e;
			for (final Document doc : mCol.find()) {
				e = this.assemData(doc, clz);
				if (null != e) {
					el.add(e);
				}
			}
		}
		return el;
	}

	/**
	 * 放入一条数据
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月4日 下午12:38:16
	 * @param <E> 目标数据对象
	 * @param ent 需要被放入的数据对象
	 * @return 被创建后，所相关的自增字段的值
	 */
	public <E extends BaseMongoBean> long insert(final E ent) {
		final MongoCollection<Document> mCol = this.md.getCollection(ent.getEntityInfo().getDefaultTableName());
		// final Class<?> iClz = ent.getIncrementKeyType();
		// long id = 0;
		// if (null != iClz) {
		// // 得到通用自增ID表
		// final MongoCollection<Document> imc = this.md.getCollection("increment_key");
		// final String k = eib.getDefaultTableName() + "_" + eib.getIncrementDataFieldName();
		// if (0 == imc.count()) {
		// // 还没有数据
		// // 创建数据
		// final BasicDBObject bof = new BasicDBObject();
		// bof.put("n", "u");
		// final Document nd = new Document();
		// nd.put("n", "u");
		// imc.findOneAndReplace(bof, nd);
		// }
		// final BasicDBObject bou = new BasicDBObject();
		// bou.put("$inc", new BasicDBObject(k, 1));
		// final BasicDBObject bof = new BasicDBObject();
		// bof.put("n", "u");
		// final FindOneAndUpdateOptions fo = new FindOneAndUpdateOptions();
		// fo.upsert(true);
		// fo.returnDocument(ReturnDocument.AFTER);
		// final Document ibd = imc.findOneAndUpdate(bof, bou, fo);
		// if (null == ibd) {
		// id = 1;
		// } else {
		// final Object ido = ibd.get(k);
		// if (null == ido) {
		// id = 1;
		// } else if (ido instanceof Integer) {
		// id = ((Integer) ido).longValue();
		// } else if (ido instanceof Long) {
		// id = ((Long) ido).longValue();
		// }
		// }
		// eib.putIncrement(ent, id);
		// }
		final long id = this.putIncrementValue(ent);
		final Document doc = ent.getInsertData();
		mCol.insertOne(doc);
		return id;
	}

	/**
	 * 放入自增字段的值
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月23日_下午5:46:23
	 * @param <E> 目标数据对象
	 * @param ent 需要被放入的数据对象
	 * @return 所相关的自增字段的值
	 */
	public <E extends BaseMongoBean> long putIncrementValue(final E ent) {
		final Class<?> iClz = ent.getIncrementKeyType();
		long id = 0;
		if (null != iClz) {
			// 得到通用自增ID表
			final MongoCollection<Document> imc = this.md.getCollection("increment_key");
			final String k = ent.getEntityInfo().getDefaultTableName() + "_" + ent.getEntityInfo().getIncrementDataFieldName();
			// if (0 == imc.count()) {
			if (0 == imc.countDocuments()) {
				// 还没有数据
				// 创建数据
				final BasicDBObject bof = new BasicDBObject();
				bof.put("n", "u");
				final Document nd = new Document();
				nd.put("n", "u");
				imc.findOneAndReplace(bof, nd);
			}
			final BasicDBObject bou = new BasicDBObject();
			bou.put("$inc", new BasicDBObject(k, 1));
			final BasicDBObject bof = new BasicDBObject();
			bof.put("n", "u");
			final FindOneAndUpdateOptions fo = new FindOneAndUpdateOptions();
			fo.upsert(true);
			fo.returnDocument(ReturnDocument.AFTER);
			final Document ibd = imc.findOneAndUpdate(bof, bou, fo);
			if (null == ibd) {
				id = 1;
			} else {
				final Object ido = ibd.get(k);
				if (null == ido) {
					id = 1;
				} else if (ido instanceof Integer) {
					id = ((Integer) ido).longValue();
				} else if (ido instanceof Long) {
					id = ((Long) ido).longValue();
				}
			}
			// ent.getEntityInfo().putIncrement(ent, id);
			// eib.putIncrement(ent, id);
			ent.putIncrement(id);
		}
		final BaseMongoBean[] fa = ent.getBeanFields();
		if (null != fa) {
			// 因为存在对象属性，所以逐一取出，进行可能的自增字段处理
			for (final BaseMongoBean l : fa) {
				if (null != l) {
					this.putIncrementValue(l);
				}
			}
		}
		final List<? extends BaseMongoBean>[] fla = ent.getArrayFields();
		if (null != fla) {
			// 因为存在列表属性，所以逐一取出，进行可能的自增字段处理
			for (final List<? extends BaseMongoBean> fl : fla) {
				if (null != fl) {
					for (final BaseMongoBean f : fl) {
						if (null != f) {
							this.putIncrementValue(f);
						}
					}
				}
			}
		}
		return id;
	}

	/**
	 * 向指定条件的指定列表字段中，增加数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月2日 下午4:02:49
	 * @param qb 更新的数据条件
	 * @return 被更新的数据行数
	 */
	public long insertField(final QLBean qb) {
		final MongoCollection<Document> mCol = this.md.getCollection(qb.getTableName());
		final UpdateResult ur = qb.assemblyMongoAggregateFieldInsert(mCol, this);
		return ur.getModifiedCount();
	}

	/**
	 * 更新指定数据
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月6日 下午12:37:20
	 * @param qb 更新的数据条件
	 * @return 被更新的数据行数
	 */
	public long update(final QLBean qb) {
		final MongoCollection<Document> mCol = this.md.getCollection(qb.getTableName());
		final UpdateResult ur = qb.assemblyMongoUpdate(mCol);
		return ur.getModifiedCount();
	}

	/**
	 * 更新指定数据
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月18日_下午1:53:49
	 * @param tableName 目标表名
	 * @param find 所相关条件数据对象
	 * @param update 待更新数据对象
	 * @param isUpsert 是否如果不存在则新增
	 * @return 得到的结果集
	 */
	public UpdateResult update(final String tableName, final Bson find, final Bson update, final boolean isUpsert) {
		final MongoCollection<Document> mCol = this.md.getCollection(tableName);
		if (isUpsert) {
			final UpdateOptions uo = new UpdateOptions();
			return mCol.updateMany(find, update, uo);
		} else {
			return mCol.updateMany(find, update);
		}
	}

	/**
	 * 删除指定数据
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月16日 下午10:11:38
	 * @param qb 删除数据对象
	 * @return 被删除的数据量
	 */
	public long delete(final QLBean qb) {
		final MongoCollection<Document> mCol = this.md.getCollection(qb.getTableName());
		final DeleteResult dr = qb.assemblyMongoDelete(mCol);
		return dr.getDeletedCount();
	}

	/**
	 * 根据条件得到数据列表
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月7日 上午10:59:47
	 * @param <E> 目标数据对象
	 * @param qb 数据条件
	 * @param eib 数据对象工具
	 * @return 得到的数据列表
	 */
	public <E extends BaseMongoBean> List<E> find(final QLBean qb, final EntityInfoBean<E> eib) {
		final MongoCollection<Document> mCol = this.md.getCollection(qb.getTableName());
		return eib.getEntityAndWithData(qb.assemblyMongoFind(mCol));
	}

	/**
	 * 查找目标数据
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月18日_上午11:50:31
	 * @param tableName 目标表名
	 * @param find 所相关条件数据对象
	 * @param sort 排序数据对象
	 * @param start 开始索引位，可空
	 * @param size 数据数量，可空
	 * @return 未被处理的结果数据集合
	 */
	public MongoIterable<Document> find(final String tableName, final Bson find, final Bson sort, final Integer start,
			final Integer size) {
		FindIterable<Document> fi;
		final MongoCollection<Document> dm = this.md.getCollection(tableName);
		if (null != find) {
			fi = dm.find(find);
		} else {
			fi = dm.find();
		}
		if (null != sort) {
			fi.sort(sort);
		}
		if (null != start) {
			fi.skip(start);
		}
		if (null != size) {
			fi.limit(size);
		}
		return fi;
	}

	/**
	 * 根据指定条件得到目标数据，以及在目标数据列表字段中，查找到指定条件数据，并返回数据列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月8日 下午4:54:23
	 * @param <E> 目标数据对象
	 * @param qb 数据条件
	 * @param eib 数据对象工具
	 * @return 得到的数据列表
	 */
	public <E extends BaseMongoBean> List<E> aggregateFieldFind(final QLBean qb, final EntityInfoBean<E> eib) {
		final MongoCollection<Document> mCol = this.md.getCollection(qb.getTableName());
		// db.getCollection('to_like').
		// aggregate([
		// {$match:{function:2,"users.user_id":3}},
		// {$unwind:"$users"},
		// {$group:{_id:{"function":"$function",function_id:"$function_id",user_id:"$users.user_id"},us:{$push:"$users"}}}])
		final MongoIterable<Document> dm = qb.assemblyMongoAggregateFieldFind(mCol);
		final List<E> back = new ArrayList<>();
		Document d;
		Object ol;
		// qb.get
		final MongoCursor<Document> it = dm.iterator();
		while (it.hasNext()) {
			d = it.next();
			ol = d.get("list");
			if (null != ol) {
				if (ol instanceof List) {
					@SuppressWarnings("unchecked")
					final List<Document> dl = (List<Document>) ol;
					back.addAll(eib.getEntityAndWithData(dl));
				}
			}
		}
		return back;
	}

	/**
	 * 根据指定条件，查询列表字段中内容
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月18日_上午11:56:40
	 * @param tableName 目标表名
	 * @param aggr 条件集合，可包含sort信息
	 * @return 未被处理的结果数据集合
	 */
	public MongoIterable<Document> aggregateFieldFind(final String tableName, final List<Bson> aggr) {
		MongoIterable<Document> fi;
		final MongoCollection<Document> dm = this.md.getCollection(tableName);
		if (null != aggr) {
			fi = dm.aggregate(aggr);
		} else {
			// 无条件返回所有数据
			fi = dm.find();
		}
		return fi;
	}

	/**
	 * 得到目标数据中目标数组字段的长度
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年5月17日 下午6:42:16
	 * @param qb 数据条件
	 * @return 目标数组字段的长度
	 */
	public int aggregateFieldCount(final QLBean qb) {
		final MongoCollection<Document> mCol = this.md.getCollection(qb.getTableName());
		// aggregate([
		// {$match:{function:2,function_id:2}},
		// {$unwind:"$users"},
		// {$group:{_id:{"function":"$function",function_id:"$function_id"},users:{$push:"$users"},size:{$sum:1}}}
		// ])
		final MongoIterable<Document> dm = qb.assemblyMongoAggregateFieldCount(mCol);
		for (final Document d : dm) {
			return d.getInteger("size", 0);
		}
		return -1;
	}

	/**
	 * 得到目标表数据
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月7日 上午11:33:59
	 * @param collectionName 目标表名称
	 * @return 目标表数据
	 */
	public MongoCollection<Document> getCollection(final String collectionName) {
		return this.md.getCollection(collectionName);
	}

	/**
	 * 组合数据到对象
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月12日 上午11:02:43
	 * @param <E> 目标数据对象
	 * @param doc mongo的数据对象
	 * @param clz 数据类对象
	 * @return 处理过的数据对象
	 */
	private <E extends BaseMongoBean> E assemData(final Document doc, final Class<E> clz) {
		try {
			final E e = clz.newInstance();
			e.putDocumentData(doc);
			return e;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
