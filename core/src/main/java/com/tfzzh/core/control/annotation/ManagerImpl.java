/**
 * @author XuWeijie
 * @datetime 2015年7月10日_下午4:52:22
 */
package com.tfzzh.core.control.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author XuWeijie
 * @datetime 2015年7月10日_下午4:52:22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ManagerImpl {

	/**
	 * 目标名字，默认为""
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月30日_上午9:33:33
	 * @return 目标名字
	 */
	String value() default "";
}
