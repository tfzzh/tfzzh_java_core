/**
 * @author XuWeijie
 * @dateTime Jan 4, 2011 10:29:41 AM
 */
package com.tfzzh.core.iface;

/**
 * 系统单例
 * 
 * @author XuWeijie
 * @dateTime Jan 4, 2011 10:29:41 AM
 * @param <T> 系统数据管理接口子接口或实现
 * @model
 */
public interface SystemSingleton<T extends SystemDataManager> {

	/**
	 * 进行数据初始化
	 * 
	 * @author XuWeijie
	 * @dateTime Jan 4, 2011 10:30:08 AM
	 * @param manager 数据管理
	 */
	void init(T manager);

	/**
	 * 对数据进行重新读取
	 * 
	 * @author XuWeijie
	 * @dateTime Jan 4, 2011 10:30:05 AM
	 */
	void reloadData();
}
