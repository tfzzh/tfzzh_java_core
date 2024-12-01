/**
 * @author xuweijie
 * @dateTime 2012-1-29 下午2:06:00
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tfzzh.view.web.tools.RequestMethod;

/**
 * 连接页面信息注解<br />
 * 正常模式<br />
 * 
 * @author xuweijie
 * @dateTime 2012-1-29 下午2:06:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LinkNormal {

	/**
	 * 连接ID，唯一<br />
	 * 如果为零字长，则认为于所在方法名相同<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-29 下午2:18:00
	 * @return 连接ID，访问路径
	 */
	String id() default "";

	/**
	 * 请求的方式，默认为GET<br />
	 * 暂时只支持{@link RequestMethod}所支持部分<br />
	 * 多个更多只是为了同时支持Post与Get的情况<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-29 下午2:18:27
	 * @return 请求的方式
	 */
	RequestMethod[] method() default RequestMethod.Get;

	/**
	 * 该连接的说明<br />
	 * 多语言情况再说<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-29 下午2:29:13
	 * @return 文字说明
	 */
	String description() default "";

	/**
	 * 结果部分：<br />
	 * 可以用“#mainpath#”、“mp”替换为主路径内容；<br />
	 * d:name:target_jsp；d开头，对应目标JSP页面进行合成；<br />
	 * r:name:target_request_get；r开头，对应目标Id的Get请求；<br />
	 * 通常默认为“d:t:{MainPath+MethodName}”对应路径及方法名（主路径+"_"+方法名）的jsp文件<br />
	 * 或如果方法名称为“*Form”则默认为“r:t:{MainPath+MethodName(nonexist:Form)}”对应路径为（主路径+"/"+方法名（无Form））的服务器转向，并同时认定为Post请求类型<br />
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
	 * Deprecated add 2023-11-24<br />
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年11月6日_下午3:33:57
	 * @return true，需要验证；
	 */
	boolean needToken() default true;

	/**
	 * 访问权限<br />
	 * Deprecated add 2023-11-24<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-9 下午3:04:15
	 * @return 权限值；默认0；
	 */
	@Deprecated
	int accessPermissions() default 0;
}
