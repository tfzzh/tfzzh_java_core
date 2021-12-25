/**
 * @author tfzzh
 * @dateTime 2021年12月25日 上午10:49:35
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适配链接中的适配值，表示来自当前请求的user-agent
 * 
 * @author tfzzh
 * @dateTime 2021年12月25日 上午10:49:35
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface LinkUserAgent {
}
