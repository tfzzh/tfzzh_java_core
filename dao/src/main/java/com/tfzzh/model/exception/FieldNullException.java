/**
 * @author Weijie Xu
 * @dateTime 2015年5月6日 下午4:55:14
 */
package com.tfzzh.model.exception;

/**
 * 字段null异常，针对不可为null的字段，被设置的null值时
 * 
 * @author Weijie Xu
 * @dateTime 2015年5月6日 下午4:55:14
 */
public class FieldNullException extends DaoRuntimeException {

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午4:58:36
	 */
	private static final long serialVersionUID = 8726630884373528181L;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午4:55:17
	 * @param entityName 相关实体名
	 * @param fieldName 相关字段名
	 */
	public FieldNullException(final String entityName, final String fieldName) {
		super(new StringBuilder().append("The Field[").append(fieldName).append("] in Entity[").append(entityName).append("] Cannt be null...").toString());
	}
}
