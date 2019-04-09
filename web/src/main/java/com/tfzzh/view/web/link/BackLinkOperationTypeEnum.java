/**
 * @author xuweijie
 * @dateTime 2012-1-31 下午4:29:59
 */
package com.tfzzh.view.web.link;

/**
 * 返回的操作
 * 
 * @author xuweijie
 * @dateTime 2012-1-31 下午4:29:59
 */
public enum BackLinkOperationTypeEnum {
	/**
	 * 进行服务器端连接跳转
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-31 下午4:30:47
	 */
	Redirect,
	/**
	 * 进行JSP页面合成
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-31 下午4:30:48
	 */
	Dispatcher,
	/**
	 * 回传一个Json格式数据
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-2 上午11:42:14
	 */
	Json,
	/**
	 * 对外链的跳转<br />
	 * 一般为代理外链<br />
	 * 
	 * @author Zhina Qian
	 * @dateTime 2012-5-13 下午5:18:40
	 */
	ToOutsideLink,
	/**
	 * 无操作
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-31 下午4:31:35
	 */
	Non;
}
