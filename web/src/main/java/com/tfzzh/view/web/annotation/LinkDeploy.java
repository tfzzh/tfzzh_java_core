/**
 * @author xuweijie
 * @dateTime 2012-2-2 下午2:08:02
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tfzzh.view.web.tools.RequestMethod;

/**
 * 连接页面信息注解<br />
 * 适配模式<br />
 * 
 * @author xuweijie
 * @dateTime 2012-2-2 下午2:08:02
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LinkDeploy {

	/**
	 * 路径适配信息，必要的存在，此路径不包含后缀名
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-2 下午2:27:53
	 * @return 路径适配描述
	 */
	String path();

	/**
	 * 请求的方式，默认为GET<br />
	 * 暂时只支持{@link RequestMethod}所支持部分<br />
	 * 多个更多只是为了同时支持Post与Get的情况<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-2 下午2:33:49
	 * @return 请求的方式
	 */
	RequestMethod[] method() default RequestMethod.Get;

	/**
	 * 该连接的说明<br />
	 * 多语言情况再说<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-2 下午2:34:03
	 * @return 文字说明
	 */
	String description() default "";

	/**
	 * 结果部分：<br />
	 * d:name:target_jsp；d开头，对应目标JSP页面进行合成；<br />
	 * r:name:target_request_get；r开头，对应目标Id的Get请求；<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-2 下午2:40:02
	 * @return 结果集
	 */
	String[] result() default {};

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
	int accessPermissions() default 0;
}
