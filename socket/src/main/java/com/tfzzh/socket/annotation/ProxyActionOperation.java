/**
 * @author Weijie Xu
 * @dateTime 2014年7月3日 下午4:34:13
 */
package com.tfzzh.socket.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 代理的行为操作
 * 
 * @author Weijie Xu
 * @dateTime 2014年7月3日 下午4:34:13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProxyActionOperation {

	/**
	 * 请求ID集合，与范围ID，二选一的存在
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年7月3日 下午4:34:13
	 * @return 请求ID
	 */
	int[] id() default {};

	/**
	 * 请求的ID范围，与请求ID集合，二选一的存在<br />
	 * 只有两个值，最大与最小<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月30日 下午7:36:34
	 * @return 范围ID
	 */
	int[] rangeId() default {};

	/**
	 * 消息编码<br />
	 * 0，表示不存在相应的消息<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年7月3日 下午7:33:58
	 * @return 相关的消息编码
	 */
	int msgCode() default 0;
}
