/**
 * @author XuWeijie
 * @datetime 2015年7月10日_下午4:55:01
 */
package com.tfzzh.model.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author XuWeijie
 * @datetime 2015年7月10日_下午4:55:01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DaoImpl {

	/**
	 * dao名
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午7:17:18
	 * @return dao名
	 */
	String name() default "";

	/**
	 * 是否唯一的
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午7:34:58
	 * @return true，是唯一的；<br />
	 *         false，是可变的；<br />
	 */
	boolean unique() default true;

	/**
	 * 所需要的连接名
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月11日_下午7:37:09
	 * @return 连接名
	 */
	String connName() default "";

	/**
	 * 获得连接用的方法
	 * 
	 * @author 听风紫竹
	 * @datetime 2015年7月12日_下午7:06:11
	 * @return 连接用的方法
	 */
	String connMethod() default "setConnectionPool";
}
