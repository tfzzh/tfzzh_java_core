/**
 * @author Weijie Xu
 * @dateTime 2015年4月17日 下午8:38:19
 */
package com.tfzzh.model.tools.iface;

/**
 * 有设置值相关操作的
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月17日 下午8:38:19
 */
public interface PutDataFieldOperation {

	/**
	 * 针对自身数据对象放入值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午8:58:41
	 * @param data 数据实体对象
	 * @param value 目标值
	 */
	void putValue(DataFieldOperationBean data, Object value);
}
