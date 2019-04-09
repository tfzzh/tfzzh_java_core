/**
 * @author Weijie Xu
 * @dateTime 2014年4月10日 下午8:04:19
 */
package com.tfzzh.socket.tools;

import java.util.HashMap;
import java.util.Map;

import com.tfzzh.socket.initiative.OperationAction;

/**
 * 主动操作行为池
 * 
 * @author Weijie Xu
 * @dateTime 2014年4月10日 下午8:05:20
 */
public class OperationPool {

	/**
	 * 行为对象列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月10日 下午7:11:43
	 */
	private final Map<String, OperationAction> operationMap;

	/**
	 * 唯一实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月10日 下午8:05:20
	 */
	private static OperationPool op = new OperationPool();

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年4月10日 下午8:05:20
	 */
	private OperationPool() {
		this.operationMap = new HashMap<>();
		this.init();
	}

	/**
	 * 初始化方法
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月10日 下午8:05:20
	 */
	private void init() {
	}

	/**
	 * 得到对象实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月10日 下午8:05:20
	 * @return 对象唯一实例
	 */
	public static OperationPool getInstance() {
		return OperationPool.op;
	}

	/**
	 * 放入一个行为对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月10日 下午8:05:20
	 * @param name 对象名称
	 * @param ra 对象实体
	 */
	public void putOperationAction(final String name, final OperationAction ra) {
		this.operationMap.put(name, ra);
	}

	/**
	 * 得到指定行为对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月10日 下午8:05:20
	 * @param name 对象名称
	 * @return 对象实体
	 */
	public OperationAction getOperationInfo(final String name) {
		return this.operationMap.get(name);
	}

	/**
	 * 得到指定行为对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月13日 下午1:55:58
	 * @param <O> 操作对象类
	 * @param oa 操作属性对象消息
	 * @return 目标操作对象
	 */
	@SuppressWarnings("unchecked")
	public <O extends OperationAction> O getOperationInfo(final OperationArrtibute<O> oa) {
		return (O) this.operationMap.get(oa.getKeyName());
	}
}
