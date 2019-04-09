/**
 * @author Xu Weijie
 * @datetime 2017年10月10日_下午2:44:57
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适配链接中的适配头部信息与对应的值
 * 
 * @author Xu Weijie
 * @datetime 2017年10月10日_下午2:44:57
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
public @interface LinkHeader {

	/**
	 * 对应适配Header的key，在每一独立方法中唯一
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月10日_下午2:45:45
	 * @return 对应适配Header的key
	 */
	String value();
}
