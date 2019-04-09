/**
 * @author Weijie Xu
 * @dateTime 2014-3-22 下午4:29:58
 */
package com.tfzzh.socket.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主动的操作行为
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-22 下午4:29:58
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InitiativeOperation {

	/**
	 * 定义的名字
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-22 下午4:31:26
	 * @return 被定义的名字
	 */
	String value();
}
