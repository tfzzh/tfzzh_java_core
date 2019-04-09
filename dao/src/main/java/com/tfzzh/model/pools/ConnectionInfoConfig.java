/**
 * @author XuWeijie
 * @datetime 2015年7月10日_下午4:15:40
 */
package com.tfzzh.model.pools;

/**
 * 连接信息配置
 * 
 * @author XuWeijie
 * @datetime 2015年7月10日_下午4:15:40
 */
public interface ConnectionInfoConfig {

	/**
	 * 放入一个连接数据信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午3:23:10
	 * @param name 池名称
	 * @param driver 连接用驱动名
	 * @param url URL连接
	 * @param user 用户名
	 * @param pass 密码
	 * @param description 说明
	 * @param useUnicode 是否使用unicode格式
	 * @param characterEncoding 连接用字符编码类型
	 * @param readOnly 是否只读
	 * @param timeOut 连接后空闲超时时间
	 * @param min 最小连接存在数
	 * @param max 最大连接存在数
	 * @param others 其他属性对应键值对，需为键与值成对出现，1为键名，2为键值，以此类推
	 */
	void putConnectionInfo(String name, String driver, String url, String user, String pass, String description, boolean useUnicode, String characterEncoding, boolean readOnly, long timeOut, int min, int max, String... others);
}
