/**
 * @author tfzzh
 * @dateTime 2016年12月5日 下午2:32:22
 */
package com.tfzzh.model.bean;

import java.sql.ResultSet;
import java.util.List;

import org.bson.Document;

import com.tfzzh.exception.ConfigurationException;
import com.tfzzh.model.dao.tools.EntityInfoBean;

/**
 * 实体Mongo数据基础类
 * 
 * @author tfzzh
 * @dateTime 2016年12月5日 下午2:32:22
 */
public abstract class BaseMongoBean extends BaseDataBean {

	/**
	 * @author tfzzh
	 * @dateTime 2017年1月16日 下午4:53:28
	 */
	private static final long serialVersionUID = 8038570590925637227L;

	/**
	 * 集合名
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月5日 下午2:33:21
	 */
	protected String collectionName;

	/**
	 * 得到所有对象属性的值的集合
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月24日_下午3:38:36
	 * @return 所有对象属性的值的集合
	 */
	public BaseMongoBean[] getBeanFields() {
		return null;
	}

	/**
	 * 得到所有为列表属性的值的集合
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月24日_下午3:01:41
	 * @return 所有为列表属性的值的集合；<br />
	 *         null，不存在列表属性；<br />
	 */
	public List<? extends BaseMongoBean>[] getArrayFields() {
		return null;
	}

	/**
	 * 得到对应的实例信息
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月23日_下午7:08:48
	 * @return 所对应的实例信息
	 */
	public abstract EntityInfoBean<?> getEntityInfo();

	/**
	 * 放入相关自增字段的值
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月23日_下午7:46:32
	 * @param incId 相关自增字段的值
	 */
	public abstract void putIncrement(long incId);

	/**
	 * 将数据库中数据放入到对象<br />
	 * 针对一般数据库用方法<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月7日 下午2:30:26
	 * @param rs 数据库结果
	 * @param index 字段对应索引位
	 */
	@Override
	public void putResultData(final ResultSet rs, final int[] index) {
		throw new ConfigurationException(" This impl not putResultData Method ... ");
	}

	/**
	 * 得到保存用数据
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月1日 下午1:38:55
	 * @return 保存用数据
	 */
	public abstract Document getInsertData();
}
