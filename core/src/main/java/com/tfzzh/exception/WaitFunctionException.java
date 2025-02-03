/**
 * @author tfzzh
 * @dateTime 2025年1月24日 15:26:25
 */
package com.tfzzh.exception;

/**
 * 待完善功能异常<br />
 * 仅应对应开发过程中状态<br />
 * 
 * @author tfzzh
 * @dateTime 2025年1月24日 15:26:25
 */
public class WaitFunctionException extends RuntimeException {

	/**
	 * @author tfzzh
	 * @dateTime 2025年1月24日 15:27:39
	 */
	private static final long serialVersionUID = 2430564188191770160L;

	/**
	 * @author tfzzh
	 * @dateTime 2025年1月24日 15:27:12
	 * @param msg 待完善功能消息内容
	 */
	public WaitFunctionException(String msg) {
		super(msg);
	}
}
