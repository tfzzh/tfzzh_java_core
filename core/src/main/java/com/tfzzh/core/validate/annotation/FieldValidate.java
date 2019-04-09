/**
 * @author Xu Weijie
 * @datetime 2017年9月26日_上午11:54:36
 */
package com.tfzzh.core.validate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tfzzh.core.validate.FieldValidateEnum;

/**
 * 作用于字段验证相关
 * 
 * @author Xu Weijie
 * @datetime 2017年9月26日_上午11:54:36
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Repeatable(FieldValidates.class)
public @interface FieldValidate {

	/**
	 * 验证类型
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午12:02:21
	 * @return 验证类型
	 */
	public FieldValidateEnum value();

	/**
	 * 验证名，针对自定义验证中，对应的操作方式名称
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午2:06:30
	 * @return 验证名
	 */
	public String name() default "";

	/**
	 * 得到验证用参考值集合
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午2:06:28
	 * @return 验证用参考值集合
	 */
	public String[] reference() default {};
}
