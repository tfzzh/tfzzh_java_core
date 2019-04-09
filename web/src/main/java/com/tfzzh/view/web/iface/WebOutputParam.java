/**
 * @author Weijie Xu
 * @dateTime 2013-9-29 下午5:21:23
 */
package com.tfzzh.view.web.iface;

import com.tfzzh.view.web.bean.BaseParamBean;

/**
 * 基于Web的数据输出
 * 
 * @author Weijie Xu
 * @param <T> 提交数据Bean
 * @dateTime 2013-9-29 下午5:21:23
 */
public interface WebOutputParam<T extends BaseParamBean> {

	/**
	 * 得到显示用数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2013-9-29 下午5:22:18
	 * @return 页面显示用数据
	 */
	T outParam();
}
