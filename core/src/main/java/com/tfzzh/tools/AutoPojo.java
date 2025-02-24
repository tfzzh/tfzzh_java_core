/**
 * @author xuweijie
 * @dateTime 2012-1-29 下午4:20:51
 */
package com.tfzzh.tools;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动注入用注解</br>
 * 也可写入具体名字</br>
 * 
 * @author xuweijie
 * @dateTime 2012-1-29 下午4:20:51
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface AutoPojo {

	/**
	 * 得到待注入内容对应名称</br>
	 * 如空，则为对应字段的字段名</br>
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-29 下午4:21:25
	 * @return 对应字段名称
	 */
	String name() default "";
}
