/**
 * @author xuweijie
 * @dateTime 2017年1月4日 下午2:17:33
 */
package com.tfzzh.model.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性目标为Mongo相关Dao的注入
 * 
 * @author tfzzh
 * @dateTime 2017年1月4日 下午2:17:33
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MongoDaoIoc {

	/**
	 * 对应对象名<br />
	 * 如果零长，则同参数名<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2017年1月4日 下午2:17:33
	 * @return 对象名
	 */
	public String value() default "";
}
