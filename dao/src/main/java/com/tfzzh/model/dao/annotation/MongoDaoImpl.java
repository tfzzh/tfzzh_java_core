/**
 * @author XuWeijie
 * @dateTime 2017年1月4日 下午2:16:02
 */
package com.tfzzh.model.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author tfzzh
 * @dateTime 2017年1月4日 下午2:16:02
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MongoDaoImpl {

	/**
	 * mongo的dao名
	 * 
	 * @author XuWeijie
	 * @dateTime 2017年1月4日 下午2:16:02
	 * @return dao名
	 */
	String name() default "";

	/**
	 * 所需要的连接名
	 * 
	 * @author XuWeijie
	 * @dateTime 2017年1月4日 下午2:16:02
	 * @return 连接名
	 */
	String connName() default "";

	/**
	 * 获得连接用的方法
	 * 
	 * @author 听风紫竹
	 * @dateTime 2017年1月4日 下午2:16:02
	 * @return 连接用的方法
	 */
	String connMethod() default "setMongoPool";
}
