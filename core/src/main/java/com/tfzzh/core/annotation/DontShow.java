/**
 * @author Weijie Xu
 * @dateTime Sep 10, 2014 9:40:39 PM
 */
package com.tfzzh.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 不要在打印中显示的注解，针对Get，Is方法
 * 
 * @author Weijie Xu
 * @dateTime Sep 10, 2014 9:40:39 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DontShow {
}
