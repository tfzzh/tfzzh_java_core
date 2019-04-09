/**
 * @author Weijie Xu
 * @dateTime 2014-3-26 下午2:27:23
 */
package com.tfzzh.socket.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 行为消息
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-26 下午2:27:23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActionMessage {

	/**
	 * 定义的编号（名字）
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-26 下午2:28:00
	 * @return 被定义的编号（名字）
	 */
	int value();

	/**
	 * 是否代理主类（基础类）
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年7月4日 下午4:46:29
	 * @return true，是代理；<br />
	 *         false，不是代理；<br />
	 */
	boolean isProxy() default false;
}
