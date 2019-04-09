/**
 * @author Weijie Xu
 * @dateTime 2015年4月20日 上午11:01:34
 */
package com.tfzzh.model.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 说明为数据实体
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月20日 上午11:01:34
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataTable {

	/**
	 * 对应数据库中表名称
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 上午11:06:32
	 * @return 数据库中表名称
	 */
	String tableName();

	/**
	 * 表说明内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午7:39:34
	 * @return 表说明内容
	 */
	String desc();

	/**
	 * 是否需要进行初始化处理，功能还未处理<br />
	 * 就是启动时候与目标库比较，看是否有该表，如果没有则创建，如果有则匹配表结构，如果不同则修改<br />
	 * 默认为true<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 上午9:57:33
	 * @return 是否需要进行初始化处理
	 */
	boolean needInit() default true;
}
