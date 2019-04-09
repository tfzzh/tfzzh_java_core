/**
 * @author XuWeijie
 * @datetime 2015年7月10日_下午1:36:10
 */
package com.tfzzh.model.exception;

/**
 * 未知的Dao异常
 * 
 * @author XuWeijie
 * @datetime 2015年7月10日_下午1:36:10
 */
public class UnknowDaoException extends DaoRuntimeException {

	/**
	 * @author XuWeijie
	 * @datetime 2015年7月10日_下午4:24:38
	 */
	private static final long serialVersionUID = 1725194595066752061L;

	/**
	 * @author XuWeijie
	 * @datetime 2015年7月10日_下午1:36:27
	 * @param msg 消息
	 */
	public UnknowDaoException(final String msg) {
		super(msg);
	}
}
