/**
 * @author Xu Weijie
 * @datetime 2017年10月17日_下午3:31:50
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适配链接中的适配值，表示当前客户端首位IP值<br />
 * 针对部分页面请求，会存在以“,”分割多个IP的情况<br />
 * 
 * @author Xu Weijie
 * @datetime 2017年10月17日_下午3:31:50
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface LinkAllIp {
}
