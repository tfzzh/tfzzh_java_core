/**
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午2:49:09
 */
package com.tfzzh.model.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据属性相关的字段索引数据集合
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午2:49:09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface KeyIndexs {

	/**
	 * 相关索引集合
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午2:49:36
	 * @return 相关索引集合
	 */
	KeyIndex[] value();
}
