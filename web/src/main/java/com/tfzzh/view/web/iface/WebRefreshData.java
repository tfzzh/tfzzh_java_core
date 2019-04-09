/**
 * @author Weijie Xu
 * @dateTime 2013-9-27 下午8:16:02
 */
package com.tfzzh.view.web.iface;

import com.tfzzh.core.control.iface.RefreshData;
import com.tfzzh.view.web.bean.BaseWebSessionBean;

/**
 * 基于Web的数据刷新
 * 
 * @author Weijie Xu
 * @param <T> 基于BaseParamBean的Bean
 * @dateTime 2013-9-27 下午8:16:02
 */
public interface WebRefreshData<T extends BaseWebSessionBean> extends RefreshData {

	/**
	 * 刷新自身数据
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-11-17 下午10:02:57
	 * @param data 新的数据
	 */
	void refreshData(T data);
}
