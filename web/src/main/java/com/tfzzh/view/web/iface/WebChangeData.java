/**
 * @author Weijie Xu
 * @dateTime 2013-9-27 下午7:57:46
 */
package com.tfzzh.view.web.iface;

import com.tfzzh.core.control.iface.ChangeData;
import com.tfzzh.model.bean.BaseEntityBean;

/**
 * 基于Web的数据变更
 * 
 * @author Weijie Xu
 * @param <T> 基于BaseParamBean的Bean
 * @dateTime 2013-9-27 下午7:57:46
 */
public interface WebChangeData<T extends BaseEntityBean> extends ChangeData {

	/**
	 * @author Weijie Xu
	 * @dateTime 2013-9-27 下午7:59:02
	 * @param data 数据信息
	 */
	void changeData(T data);
}
