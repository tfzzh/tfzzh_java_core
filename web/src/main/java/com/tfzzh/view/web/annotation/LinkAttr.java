/**
 * @author tfzzh
 * @dateTime 2023年11月24日 09:57:13
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适配内跳（forward）相关，request.attribute传值
 * 
 * @author tfzzh
 * @dateTime 2023年11月24日 09:57:13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface LinkAttr {

	/**
	 * 对应适配Key，在每一独立方法中唯一<br />
	 * 如果为零字长，则认为于所在参数名相同<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2023年11月24日 09:57:13
	 * @return 对应配置Key
	 */
	String value();
}
