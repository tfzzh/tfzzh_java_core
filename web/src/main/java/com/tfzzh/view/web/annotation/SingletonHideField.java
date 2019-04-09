/**
 * @author Xu Weijie
 * @dateTime 2012-7-10 下午9:10:09
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要初始化的单例
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-10 下午9:10:09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SingletonHideField {

	/**
	 * 对应的Servlet配置属性名
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-10 下午9:20:48
	 * @return 对应的Servlet配置属性名
	 */
	String value();
}
