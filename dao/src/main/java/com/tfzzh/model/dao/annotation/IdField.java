/**
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午2:42:14
 */
package com.tfzzh.model.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tfzzh.model.dao.tools.IdFieldEnum;

/**
 * Id类字段，针对自增ID，与UUID
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午2:42:14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IdField {

	/**
	 * 相关的ID类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午2:43:53
	 * @return 相关的ID类型
	 */
	IdFieldEnum value();
}
