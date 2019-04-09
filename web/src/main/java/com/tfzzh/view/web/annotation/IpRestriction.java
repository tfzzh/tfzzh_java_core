/**
 * @author Xu Weijie
 * @datetime 2017年8月23日_下午6:46:58
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ip访问限制
 * 
 * @author Xu Weijie
 * @datetime 2017年8月23日_下午6:46:58
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface IpRestriction {

	/**
	 * 限制标识
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月23日_下午6:48:27
	 * @return 限制标识
	 */
	String value() default "";
}
