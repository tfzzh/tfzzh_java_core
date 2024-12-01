/**
 * @author xuweijie
 * @dateTime 2012-1-30 下午2:50:41
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分流连接的具体操作
 * 
 * @author xuweijie
 * @dateTime 2012-1-30 下午2:50:41
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LinkBranchInfo {

	/**
	 * 对应的操作值
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-30 下午4:25:28
	 * @return 操作值
	 */
	String value();

	/**
	 * 结果部分：<br />
	 * d:name:target_jsp；d开头，对应目标JSP页面进行合成；<br />
	 * r:name:target_request_get；r开头，对应目标Id的Get请求；<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-29 下午2:47:01
	 * @return 结果集
	 */
	String[] result() default {};

	/**
	 * 是否读取data信息<br />
	 * 默认读取<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2023年11月24日 14:37:09
	 * @return true，读取data信息
	 */
	boolean readStreamJSON() default true;

	/**
	 * 是否可以被跨域访问<br />
	 * 默认为true<br />
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年11月29日_下午1:50:53
	 * @return true，可以被跨域访问
	 */
	boolean canCrossDomain() default true;

	/**
	 * 是否需要进行token验证<br />
	 * 默认需要<br />
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年11月6日_下午3:33:57
	 * @return true，需要验证；
	 */
	boolean needToken() default true;

	/**
	 * 访问权限
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-9 下午3:04:15
	 * @return 权限值；默认0；
	 */
	@Deprecated
	int accessPermission() default 0;
}
