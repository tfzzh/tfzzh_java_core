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
 * 适配链接中的适配值与参数的对应
 * 
 * @author xuweijie
 * @dateTime 2012-2-2 下午2:44:58
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface LinkParam {

	/**
	 * 对应适配Key，在每一独立方法中唯一<br />
	 * 如果为零字长，则认为于所在参数名相同<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-2 下午2:47:02
	 * @return 对应配置Key
	 */
	String value();
}
