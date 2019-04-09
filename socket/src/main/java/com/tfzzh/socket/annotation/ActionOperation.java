/**
 * @author Weijie Xu
 * @dateTime 2014-3-22 下午4:32:39
 */
package com.tfzzh.socket.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 行为的操作
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-22 下午4:32:39
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionOperation {

	/**
	 * 请求ID
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月4日 下午6:25:56
	 * @return 请求ID
	 */
	int id();

	/**
	 * 消息编码<br />
	 * 0，表示不存在相应的消息<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-26 下午5:21:14
	 * @return 相关的消息编码
	 */
	int msgCode() default 0;

	/**
	 * 验证规则id集合：<br />
	 * 使用","分割规则id<br />
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月26日_下午1:31:58
	 * @return 验证规则串
	 */
	String validationRule() default "";

	/**
	 * 是否守护线程，默认为不是
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月29日 下午7:07:29
	 * @return true，是；<br />
	 *         false，不是；<br />
	 */
	boolean isKeep() default false;
}
