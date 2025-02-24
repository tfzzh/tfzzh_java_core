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
	 * 目标方法的方法名，默认为“asse”加属性转换名</br>
	 * 同StringTools.assemblyStringWhitInterval中方法，首字符大写，中间字符转小写的数据模型</br>
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:40:05
	 * @return 目标方法的方法名
	 */
	public String value() default "";

	/**
	 * 对应的配置文件属性名</br>
	 * 可空，空时同属性名</br>
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月21日 下午2:34:54
	 * @return 对应的配置文件属性名
	 */
	public String name() default "";

	/**
	 * 可能被用到的对象类
	 * 
	 * @author tfzzh
	 * @dateTime 2020年11月17日 下午4:56:07
	 * @return 可能被用到的对象类
	 */
	public Class<?> clz() default Object.class;

	/**
	 * 目标用到的类属性对象</br>
	 * 对应clz存在时</br>
	 * 
	 * @author tfzzh
	 * @dateTime 2020年11月17日 下午6:58:38
	 * @return 目标用到的类属性对象
	 */
	public Class<?> tarclz() default Object.class;

	/**
	 * 当作为一个map对象时，如果接收到为list数据，则该属性对应内容为key
	 * 
	 * @author tfzzh
	 * @dateTime 2021年5月28日 下午9:19:36
	 * @return 内容为key的属性
	 */
	public String mapKey() default "";
}
