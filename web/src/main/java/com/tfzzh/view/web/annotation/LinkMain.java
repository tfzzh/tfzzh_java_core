/**
 * @author xuweijie
 * @dateTime 2012-1-29 下午1:59:06
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 连接主信息注解<br />
 * 主要记录主路径，一般理解为功能分类的范畴<br />
 * 
 * @author xuweijie
 * @dateTime 2012-1-29 下午1:59:06
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LinkMain {

	/**
	 * 主路径
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-29 下午2:04:21
	 * @return 主路径
	 */
	String mainPath();

	/**
	 * 该连接的说明<br />
	 * 多语言情况再说<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2013-12-21 下午4:58:34
	 * @return 文字说明
	 */
	String description() default "";

	/**
	 * 访问权限
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-17 下午7:57:23
	 * @return 权限值；默认0；
	 */
	int accessPermissions() default 0;

	/**
	 * 用于返回的URL映射表表<br />
	 * 可以用“#mainpath#”替换为主路径内容；<br />
	 * d:name:target_jsp；d开头，对应目标JSP页面进行合成；<br />
	 * r:name:target_request_get；r开头，对应目标Id的Get请求；<br />
	 * 通常默认为“d:t:{MainPath+MethodName}”对应路径及方法名（主路径+"_"+方法名）的jsp文件<br />
	 * 或如果方法名称为“*Form”则默认为“r:t:{MainPath+MethodName(nonexist:Form)}”对应路径为（主路径+"/"+方法名（无Form））的服务器转向，并同时认定为Post请求类型<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2013-12-21 上午11:22:40
	 * @return 结果集
	 */
	String[] resultList() default {};
}
