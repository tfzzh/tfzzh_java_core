/**
 * @author Weijie Xu
 * @dateTime 2015年5月16日 下午2:35:40
 */
package com.tfzzh.model.exception;

/**
 * 表名相关异常，一般存在于sql组合时候
 * 
 * @author Weijie Xu
 * @dateTime 2015年5月16日 下午2:35:40
 */
public class TableNameException extends DaoRuntimeException {

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年5月16日 下午2:36:11
	 */
	private static final long serialVersionUID = -7375235163918550892L;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年5月16日 下午2:36:01
	 * @param msg 消息
	 */
	public TableNameException(final String msg) {
		super(msg);
	}
}
