/**
 * @author xuweijie
 * @dateTime 2012-3-22 上午11:44:02
 */
package com.tfzzh.model.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性目标为Dao的注入
 * 
 * @author xuweijie
 * @dateTime 2012-3-22 上午11:44:02
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DaoIoc {

	/**
	 * 对应对象名<br />
	 * 如果零长，则同参数名<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-3-22 上午11:44:52
	 * @return 对象名
	 */
	public String value() default "";
}
