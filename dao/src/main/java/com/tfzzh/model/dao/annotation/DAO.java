/**
 * @author Weijie Xu
 * @dateTime 2015年4月20日 上午10:29:22
 */
package com.tfzzh.model.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 为DAO接口文件的实现类，会通过直接的接口继承进行对应的注入
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月20日 上午10:29:22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DAO {

	/**
	 * bean用名，用于进行匹配设置，默认为类名称去掉DAO后缀，并首字母小写
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 上午11:05:03
	 * @return bean用名
	 */
	String name() default "";

	/**
	 * 是否唯一（系统）链接<br />
	 * true，唯一（系统）链接<br />
	 * false，可变（用户）链接<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 上午10:29:40
	 * @return 是否唯一
	 */
	boolean unique() default true;
}
