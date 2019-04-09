/**
 * @author XuWeijie
 * @datetime 2015年10月19日_下午4:50:22
 */
package com.tfzzh.tools;

/**
 * 对象返回
 * 
 * @author XuWeijie
 * @datetime 2015年10月19日_下午4:50:22
 * @param <O> 目标对象的类型
 * @param <S> 反馈状态的类型
 */
public class ObjectBack<O, S> {

	/**
	 * 目标对象
	 * 
	 * @author XuWeijie
	 * @datetime 2015年10月19日_下午4:51:02
	 */
	private final O obj;

	/**
	 * 反馈的状态
	 * 
	 * @author XuWeijie
	 * @datetime 2015年10月19日_下午4:51:02
	 */
	private final S status;

	/**
	 * 仅针对错误情况
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月18日_上午9:42:46
	 * @param status 反馈的状态
	 */
	public ObjectBack(final S status) {
		this.status = status;
		this.obj = null;
	}

	/**
	 * @author XuWeijie
	 * @datetime 2015年10月19日_下午4:51:39
	 * @param obj 目标对象
	 * @param status 反馈的状态
	 */
	public ObjectBack(final O obj, final S status) {
		this.obj = obj;
		this.status = status;
	}

	/**
	 * 得到目标对象
	 * 
	 * @author XuWeijie
	 * @datetime 2015年10月19日_下午4:52:02
	 * @return the obj
	 */
	public O getObj() {
		return this.obj;
	}

	/**
	 * 得到反馈的状态
	 * 
	 * @author XuWeijie
	 * @datetime 2015年10月19日_下午4:52:02
	 * @return the status
	 */
	public S getStatus() {
		return this.status;
	}
}
