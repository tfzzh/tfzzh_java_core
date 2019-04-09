/**
 * @author Weijie Xu
 * @dateTime 2014年6月13日 下午1:52:08
 */
package com.tfzzh.socket.tools;

import com.tfzzh.socket.initiative.OperationAction;

/**
 * 控制属性
 * 
 * @author Weijie Xu
 * @dateTime 2014年6月13日 下午1:52:08
 * @param <O> 目标控制类
 */
public final class OperationArrtibute<O extends OperationAction> {

	/**
	 * 目标键名称
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月13日 下午1:54:00
	 */
	private final String keyName;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年6月13日 下午1:53:50
	 * @param name 目标键名称
	 */
	public OperationArrtibute(final String name) {
		this.keyName = name;
	}

	/**
	 * 得到目标键名称
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月13日 下午1:54:39
	 * @return the keyName
	 */
	public String getKeyName() {
		return this.keyName;
	}
}
