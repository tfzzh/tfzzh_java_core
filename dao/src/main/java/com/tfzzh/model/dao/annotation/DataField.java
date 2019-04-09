/**
 * @author Weijie Xu
 * @dateTime 2015年4月20日 上午11:14:13
 */
package com.tfzzh.model.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bean中，属性与表字段对应关系，只作用在属性上
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月20日 上午11:14:13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataField {

	/**
	 * 所相关的数据表中字段名称
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午1:52:00
	 * @return 的数据表中字段名称
	 */
	String fieldName();

	/**
	 * 所相关的get方法的名称<br />
	 * 如果没有，则为自身名字的首字符大写，并增加get前缀<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午1:52:49
	 * @return 相关get方法的名称
	 */
	String getMethodName() default "";

	/**
	 * 所相关的set方法的名称<br />
	 * 如果没有，则为自身名字的首字符大写，并增加set前缀<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午1:52:49
	 * @return 相关set方法的名称
	 */
	String setMethodName() default "";

	/**
	 * 相关数据库字段类型<br />
	 * 需要去到目标FieldTypeEnum中去得到目标类型<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:21:48
	 * @return 数据库字段类型
	 */
	String fieldType();

	/**
	 * 字段的长度，针对字符串类数据<br />
	 * 0，表示无限制，或按照默认的来；<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:27:24
	 * @return 字串长度
	 */
	long length() default 0;

	/**
	 * 是否无符号，默认无符号
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午12:34:26
	 * @return true，无符号；<br />
	 *         false，有符号；<br />
	 */
	boolean unsigned() default true;

	/**
	 * 是否可以为null，默认不能为null
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午12:34:26
	 * @return true，可以为null；<br />
	 *         false，不可以为null；<br />
	 */
	boolean canNull() default false;

	/**
	 * 数据精度，针对浮点double，float的总长度<br />
	 * 0，表示无限制，或按照默认的来；<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:27:26
	 * @return 浮点总长度
	 */
	int precision() default 0;

	/**
	 * 数据精度，针对浮点double，float的小数点后精确位数<br />
	 * 0，表示无限制，或按照默认的来；<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午3:27:27
	 * @return 小数点后精确位数
	 */
	int scale() default 0;

	/**
	 * 默认值；<br />
	 * "-"，为不存在默认值；<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午12:40:34
	 * @return 默认值
	 */
	String defValue() default "-";

	/**
	 * 字段说明内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午7:39:08
	 * @return 字段说明内容
	 */
	String desc();

	/**
	 * 索引位，从1开始
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 下午4:36:39
	 * @return 索引位
	 */
	int index();
}
