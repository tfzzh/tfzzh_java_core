/**
 * @author Xu Weijie
 * @dateTime 2012-9-11 上午1:15:59
 */
package com.tfzzh.view.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tfzzh.view.web.tools.UploadFileNameTypeEnum;

/**
 * 上传文件所相关必要信息
 * 
 * @author Xu Weijie
 * @dateTime 2012-9-11 上午1:15:59
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UploadFileInfo {

	/**
	 * 被允许的后缀名限制列表<br />
	 * 规则：后缀后|后缀名|后缀名...<br />
	 * 无内容为可以任何文件类型<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-11 上午1:18:45
	 * @return 被允许的文件后缀名列表<br />
	 *         无内容为可以任何文件类型<br />
	 */
	String suffix() default "*";

	/**
	 * 上传文件的名称类型，<br />
	 * 存在时间戳字符串替换符{time}；<br />
	 * 到服务端本地存储文件名定义方式：<br />
	 * Same，与上传文件名相同；<br />
	 * SpellingAll，如果是中文则转为拼音，为单字拼音全文，仅针对简体文件名；<br />
	 * SpellingHead，如果是中文则转为拼音，为单字拼音首字母，仅针对简体文件名；<br />
	 * Designation，指定为fileName中内容；<br />
	 * Field，根据指定的提交指定名称字段fileName内容定义；<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-16 上午11:56:54
	 * @return 上传文件的名称类型
	 */
	UploadFileNameTypeEnum nameType() default UploadFileNameTypeEnum.Same;

	/**
	 * 主要针对上传文件名称类型的定义
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-18 上午11:44:49
	 * @return the otherValue
	 */
	String fileName() default "";

	/**
	 * 是否在文件名中增加时间戳
	 * 
	 * @author Aiyi Ji
	 * @dateTime 2014-2-25 下午1:48:21
	 * @return true 使用使用戳；<br />
	 *         不使用时间戳；<br />
	 */
	boolean addTimestamp() default false;

	/**
	 * 文件存储路径设置；<br />
	 * 首位为“-”：接续当前应用所在硬盘路径；<br />
	 * 首位无“-”：全新的路径；<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-10-8 下午4:47:31
	 * @return 文件存储路径
	 */
	String folderPath() default "-";

	/**
	 * 对上传文件的最大字节数限制
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-11 上午1:21:23
	 * @return 被限制的最大字节数
	 */
	int maxLength();
}
