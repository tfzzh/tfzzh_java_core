/**
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午2:08:31
 */
package com.tfzzh.model.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tfzzh.model.dao.tools.KeyIndexEnum;
import com.tfzzh.model.dao.tools.SortEnum;

/**
 * 数据属性相关的字段索引
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午2:08:31
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface KeyIndex {

	/**
	 * 名字，默认零字长，如果是零字长表示，名字就是字段名+索引类型名<br />
	 * 如果count>1，则该字段一定要有对应内容<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 上午11:18:53
	 * @return 各种索引的名字，如果同名，会叠加内容
	 */
	String name() default "";

	/**
	 * 键索引类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午2:20:14
	 * @return 索引类型
	 */
	KeyIndexEnum value();

	/**
	 * 排序方式，仅相关asc与desc，当前辅助的两项会进行同类型合并
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月30日 下午2:45:49
	 * @return 排序方式
	 */
	SortEnum sort() default SortEnum.ASC;

	/**
	 * 索引相关字段数量
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午2:33:18
	 * @return 索引相关字段数量
	 */
	int count() default 1;

	/**
	 * 所在索引的顺位，从1开始<br />
	 * 所以按数组处理时候，需要-1<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午2:33:19
	 * @return 所在索引的顺位
	 */
	int index() default 1;
}
