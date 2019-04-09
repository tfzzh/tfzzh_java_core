/**
 * @author Xu Weijie
 * @datetime 2015年8月25日_下午8:16:10
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适配链接中的适配值，表示来自当前请求的完整url
 * 
 * @author Xu Weijie
 * @datetime 2015年8月25日_下午8:16:10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface LinkUrl {
}
