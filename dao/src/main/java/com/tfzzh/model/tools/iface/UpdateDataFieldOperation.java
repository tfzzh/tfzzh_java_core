/**
 * @author Weijie Xu
 * @dateTime 2015年4月17日 下午8:30:40
 */
package com.tfzzh.model.tools.iface;

import com.tfzzh.model.bean.BaseEntityBean;
import com.tfzzh.model.dao.tools.QLUpdateBean;

/**
 * 有更新相关的操作
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月17日 下午8:30:40
 */
public interface UpdateDataFieldOperation {

	/**
	 * 设置对应的条件
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午8:36:48
	 * @param updates 更新的条件队列
	 * @param entity 数据对象
	 */
	void setUpdate(QLUpdateBean updates, BaseEntityBean entity);
}
