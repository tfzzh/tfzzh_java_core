/**
 * @author tfzzh
 * @dateTime 2016年11月21日 下午2:27:06
 */
package com.tfzzh.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 针对constants文件，对应properties文件中通用名前缀<br />
 * 默认为与类名相同做前缀<br />
 * 
 * @author tfzzh
 * @dateTime 2016年11月21日 下午2:27:06
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PropertiesFile {

	/**
	 * 所相关properties文件中字段属性的前缀名<br />
	 * 默认值为类名<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午2:28:51
	 * @return 对应properties文件属性前缀名
	 */
	public String value() default "";
}
