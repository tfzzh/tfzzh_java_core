/**
 * @author Weijie Xu
 * @dateTime 2012-7-17 上午1:15:19
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适配链接中的适配值，表示当前登陆中的用户信息
 * 
 * @author Weijie Xu
 * @dateTime 2012-7-17 上午1:15:19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface LinkLogin {
}
