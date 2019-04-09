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
}
