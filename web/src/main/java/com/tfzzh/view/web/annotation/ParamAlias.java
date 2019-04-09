/**
 * @author Weijie Xu
 * @dateTime 2015年4月10日 下午6:33:07
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数别名，可能会因为不同请求，会以多种名字出现的同概念参数所用
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月10日 下午6:33:07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ParamAlias {

	/**
	 * 具体的别名列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月10日 下午6:33:29
	 * @return 可能的别名列表
	 */
	String[] value();
}
