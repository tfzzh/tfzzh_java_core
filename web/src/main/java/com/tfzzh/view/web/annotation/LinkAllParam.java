/**
 * @author tfzzh
 * @dateTime 2016年10月10日 下午10:33:55
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适配链接中的适配值，表示得到所有参数的Map，后面仅跟类型“Map<String, Object>”
 * 
 * @author tfzzh
 * @dateTime 2016年10月10日 下午10:33:55
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface LinkAllParam {
}
