/**
 * @author tfzzh
 * @dateTime 2016年11月21日 下午1:38:21
 */
package com.tfzzh.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 针对constants文件中，不能直接通过properties获取值，是通过内置方法通过一定运算得到的内容
 * 
 * @author tfzzh
 * @dateTime 2016年11月21日 下午1:38:21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PropertiesMethod {

	/**
	 * 目标方法的方法名，默认为“asse”加属性转换名<br />
	 * 同StringTools.assemblyStringWhitInterval中方法，首字符大写，中间字符转小写的数据模型<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:40:05
	 * @return 目标方法的方法名
	 */
	public String value() default "";
}
