/**
 * @author xuweijie
 * @dateTime 2012-2-2 下午2:44:58
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适配链接中的适配值与参对参数Bean的数据映射
 * 
 * @author xuweijie
 * @dateTime 2012-2-2 下午3:59:12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface LinkBean {

	/**
	 * 是否进行有效性校验<br />
	 * 默认需要进行校验动作<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2024年12月31日 18:12:18
	 * @return true，需要验证
	 */
	boolean needValid() default true;
}
