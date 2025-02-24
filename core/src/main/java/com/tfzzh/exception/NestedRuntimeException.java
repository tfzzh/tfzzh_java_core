/**
 * @author XuWeijie
 * @dateTime May 19, 2010 10:24:52 AM
 */
package com.tfzzh.exception;

import com.tfzzh.tools.NestedExceptionUtils;

/**
 * 基础运行时异常控制</br>
 * 抄自——spring中相对应类</br>
 * 
 * @author XuWeijie
 * @dateTime May 19, 2010 10:24:52 AM
 * @model
 */
public abstract class NestedRuntimeException extends RuntimeException {

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-7-13 下午4:12:37
	 */
	private static final long serialVersionUID = 7692715573732270457L;

	/**
	 * @author XuWeijie
	 * @dateTime May 19, 2010 10:27:50 AM
	 * @param msg 消息
	 */
	public NestedRuntimeException(final String msg) {
		super(msg);
	}

	/**
	 * @author XuWeijie
	 * @dateTime May 19, 2010 10:27:57 AM
	 * @param msg 消息
	 * @param cause 抛出消息
	 */
	public NestedRuntimeException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	/**
	 * @author XuWeijie
	 * @dateTime May 19, 2010 10:27:56 AM (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return NestedExceptionUtils.buildMessage(super.getMessage(), this.getCause());
	}

	/**
	 * @author XuWeijie
	 * @dateTime May 19, 2010 10:27:55 AM
	 * @return 抛出消息
	 */
	public Throwable getRootCause() {
		Throwable rootCause = null;
		Throwable cause = this.getCause();
		while ((cause != null) && (cause != rootCause)) {
			rootCause = cause;
			cause = cause.getCause();
		}
		return rootCause;
	}

	/**
	 * @author XuWeijie
	 * @dateTime May 19, 2010 10:27:54 AM
	 * @return 抛出消息
	 */
	public Throwable getMostSpecificCause() {
		final Throwable rootCause = this.getRootCause();
		return (rootCause != null ? rootCause : this);
	}

	/**
	 * 是否包含
	 * 
	 * @author XuWeijie
	 * @dateTime May 19, 2010 10:27:53 AM
	 * @param exType 比较的类消息
	 * @return 判定结果
	 */
	public boolean contains(final Class<?> exType) {
		if (exType == null) {
			return false;
		}
		if (exType.isInstance(this)) {
			return true;
		}
		Throwable cause = this.getCause();
		if (cause == this) {
			return false;
		}
		if (cause instanceof NestedRuntimeException) {
			return ((NestedRuntimeException) cause).contains(exType);
		} else {
			while (cause != null) {
				if (exType.isInstance(cause)) {
					return true;
				}
				if (cause.getCause() == cause) {
					break;
				}
				cause = cause.getCause();
			}
			return false;
		}
	}
}
