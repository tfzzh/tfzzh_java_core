/**
 * @author Weijie Xu
 * @dateTime 2015年3月31日 下午3:29:07
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求参数对象中，嵌套了需要向内处理的对象
 * 
 * @author Weijie Xu
 * @dateTime 2015年3月31日 下午3:29:07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ParamBean {
}
