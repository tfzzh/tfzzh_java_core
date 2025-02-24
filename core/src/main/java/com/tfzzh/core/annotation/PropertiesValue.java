/**
 * @author tfzzh
 * @dateTime 2016年11月21日 下午1:38:21
 */
package com.tfzzh.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tfzzh.tools.PropertiesValueTypeEnum;

/**
 * 针对constants文件中，需要从properties文件中取值的字段</br>
 * 需要注意类型设置，默认为string类型</br>
 * 
 * @author tfzzh
 * @dateTime 2016年11月21日 下午1:38:21
 */
/**
 * @author tfzzh
 * @dateTime 2016年11月21日 下午3:00:07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PropertiesValue {

	/**
	 * 所对应properties文件中的key名，如果为默认值，则与字段名相同
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:40:05
	 * @return 对应properties文件中的key名
	 */
	public String value() default "";

	/**
	 * 对应值的表现类型，默认为String
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午1:45:19
	 * @return 对应值的表现类型
	 */
	public PropertiesValueTypeEnum type() default PropertiesValueTypeEnum.Auto;

	/**
	 * 特殊值，针对不同类型有不同作用</br>
	 * 当前默认是针对列表字段所需字串间隔附</br>
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午3:00:13
	 * @return 特殊值，部分类型需要
	 */
	public String specialValue() default "|";

	/**
	 * 默认值
	 * 
	 * @author tfzzh
	 * @dateTime 2020年10月27日 下午5:09:32
	 * @return 可选默认值
	 */
	public String defVal() default "";
}
