/**
 * @author Weijie Xu
 * @dateTime 2015年4月27日 下午5:15:00
 */
package com.tfzzh.model.exception;

/**
 * 注解问题
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月27日 下午5:15:00
 */
public class AnnotationException extends DaoRuntimeException {

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 下午5:31:18
	 */
	private static final long serialVersionUID = -1059849086518585541L;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 下午5:20:09
	 * @param beanName 对象名称
	 * @param annoName 需要的注解名称
	 */
	public AnnotationException(final String beanName, final String annoName) {
		super(new StringBuilder().append("The Bean[").append(beanName).append("] must exists Annotation[").append(annoName).append("]...").toString());
	}
}
