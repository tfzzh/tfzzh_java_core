/**
 * @author tfzzh
 * @dateTime 2023年11月24日 09:57:13
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适配内跳（forward）相关，request.attribute相关stream解密后内容
 * 
 * @author tfzzh
 * @dateTime 2023年11月24日 09:57:13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface LinkAttrDecrypt {
}
