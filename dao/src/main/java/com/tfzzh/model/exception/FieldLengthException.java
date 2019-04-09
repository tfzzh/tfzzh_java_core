/**
 * @author Weijie Xu
 * @dateTime 2015年4月30日 下午1:38:30
 */
package com.tfzzh.model.exception;

/**
 * 字段长度范围异常
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月30日 下午1:38:30
 */
public class FieldLengthException extends DaoRuntimeException {

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月30日 下午1:43:33
	 */
	private static final long serialVersionUID = -4555775640518917981L;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月30日 下午1:38:43
	 * @param fieldName 字段名
	 * @param type 字段类型
	 * @param length 长度
	 */
	public FieldLengthException(final String fieldName, final String type, final long length) {
		super(new StringBuilder(35 + fieldName.length() + type.length()).append("With field ").append(fieldName).append('(').append(type).append(") length Cannt for ").append(length).toString());
	}
}
