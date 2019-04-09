/**
 * @author Xu Weijie
 * @datetime 2017年9月26日_下午2:37:29
 */
package com.tfzzh.core.validate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作为可以让{@link FieldValidate}在目标重复多个而存在
 * 
 * @author Xu Weijie
 * @datetime 2017年9月26日_下午2:37:29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface FieldValidates {

	/**
	 * 多个FieldValidate对象
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午2:38:31
	 * @return 多个FieldValidate对象
	 */
	FieldValidate[] value() default {};
}
