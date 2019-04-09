/**
 * @author Xu Weijie
 * @dateTime 2012-7-6 下午4:05:42
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适配链接中的适配值，表示当前客户端IP值
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-6 下午4:05:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface LinkIp {
}
