/**
 * @author xuweijie
 * @dateTime 2012-2-16 下午4:48:38
 */
package com.tfzzh.core.control.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性目标为Manager的注入
 * 
 * @author xuweijie
 * @dateTime 2012-2-16 下午4:48:38
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManagerIoc {

	/**
	 * 对应对象名<br />
	 * 如果零长，则同参数名<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-16 下午4:49:51
	 * @return 对象名
	 */
	public String value() default "";
}
