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
 * 分流模式<br />
 * 
 * @author xuweijie
 * @dateTime 2012-1-29 下午2:06:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LinkBranch {

	/**
	 * 分流的操作路径，必要的存在，但只标识有效的路径部分，不包括后缀名以及“?”及之后的部分
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-29 下午2:18:00
	 * @return 访问路径
	 */
	String path();

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
	 * 分流对应的内容名<br />
	 * 如果是参数分流，对应参数名：key<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-30 下午3:25:04
	 * @return 参数名；<br />
	 *         路径标识名；<br />
	 */
	String breachKey();

	/**
	 * 访问权限
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-9 下午3:04:15
	 * @return 权限值；默认0；
	 */
	@Deprecated
	int accessPermissions() default 0;
}
