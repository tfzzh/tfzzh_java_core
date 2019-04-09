/**
 * @author Weijie Xu
 * @dateTime 2015年4月17日 下午6:27:42
 */
package com.tfzzh.model.bean;

import java.sql.ResultSet;

import org.bson.Document;

import com.tfzzh.tools.BaseBean;

/**
 * 基础数据相关bean
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月17日 下午6:27:42
 */
public abstract class BaseDataBean extends BaseBean {

	/**
	 * @author tfzzh
	 * @dateTime 2016年12月23日 下午4:47:21
	 */
	private static final long serialVersionUID = -5419838471282315218L;

	/**
	 * 得到对象ID
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月5日 下午2:35:40
	 * @return 对象Id
	 */
	public abstract Long getObjectId();

	/**
	 * 得到Key的值
	 *
	 * @author XuWeijie
	 * @dateTime 2016年12月5日 下午2:35:40
	 * @return key的值
	 */
	public abstract Object getKeyValue();

	/**
	 * 得到自增字段的类型
	 *
	 * @author tfzzh
	 * @dateTime 2016年12月6日 下午7:12:59
	 * @return 默认为Object，默认时，不进行判定
	 */
	public abstract Class<?> getIncrementKeyType();

	/**
	 * 将数据库中数据放入到对象<br />
	 * 针对一般数据库用方法<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月5日 下午2:41:21
	 * @param rs 数据库结果
	 * @param index 字段对应索引位
	 */
	public abstract void putResultData(ResultSet rs, int[] index);

	/**
	 * 将Mongo库中数据放入到对象<br />
	 * 针对一般MongoDb库用方法<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月6日 下午5:26:08
	 * @param doc mongo库结果
	 */
	public abstract void putDocumentData(Document doc);
}
