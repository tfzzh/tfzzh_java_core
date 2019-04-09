/**
 * @author Weijie Xu
 * @dateTime 2014-3-20 下午8:34:29
 */
package com.tfzzh.socket.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.tfzzh.exception.InstallException;
import com.tfzzh.socket.action.RequestAction;
import com.tfzzh.socket.bean.RequestInfoBean;
import com.tfzzh.tools.RangeKey;

/**
 * 请求消息池
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-20 下午8:34:29
 */
public class RequestPoolType {

	/**
	 * 行为对象列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月10日 下午7:11:43
	 */
	private final Map<Integer, Map<String, RequestAction>> actionMap;

	/**
	 * 代理行为对象列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年7月3日 下午5:30:45
	 */
	private final Map<Integer, Map<String, RequestAction>> proxyActionMap;

	/**
	 * 请求消息编码池
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:36:39
	 */
	private final Map<Integer, Map<Integer, RequestInfoBean>> requestCodeInfoPool;

	/**
	 * 请求消息池
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月30日 下午7:20:54
	 */
	private final Map<Integer, Map<RangeKey, RequestInfoBean>> requestInfoPool;

	/**
	 * 唯一实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:36:46
	 */
	private static RequestPoolType rp = new RequestPoolType();

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:36:47
	 */
	private RequestPoolType() {
		this.requestCodeInfoPool = new HashMap<>();
		// 此对象一定是TreeMap，或有排序概念的Map为子类
		this.requestInfoPool = new HashMap<>();
		this.actionMap = new HashMap<>();
		this.proxyActionMap = new HashMap<>();
		this.init();
	}

	/**
	 * 初始化方法
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:36:49
	 */
	private void init() {
	}

	/**
	 * 得到对象实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:36:51
	 * @return 对象唯一实例
	 */
	public static RequestPoolType getInstance() {
		return RequestPoolType.rp;
	}

	/**
	 * 放入一个行为对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月10日 下午7:14:09
	 * @param type 类型
	 * @param name 对象名称
	 * @param ra 对象实体
	 */
	public void putRequestAction(final Integer type, final String name, final RequestAction ra) {
		Map<String, RequestAction> map = this.actionMap.get(type);
		if (null == map) {
			map = new HashMap<>();
			this.actionMap.put(type, map);
		}
		map.put(name, ra);
	}

	/**
	 * 放入一个代理行为对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年7月3日 下午5:32:17
	 * @param type 类型
	 * @param name 对象名称
	 * @param ra 对象实体
	 */
	public void putProxyRequestAction(final Integer type, final String name, final RequestAction ra) {
		Map<String, RequestAction> map = this.proxyActionMap.get(type);
		if (null == map) {
			map = new HashMap<>();
			this.proxyActionMap.put(type, map);
		}
		map.put(name, ra);
	}

	/**
	 * 放入一个请求信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:41:50
	 * @param type 类型
	 * @param code 编码
	 * @param bean 请求信息对象
	 */
	public void putRequestInfo(final Integer type, final int code, final RequestInfoBean bean) {
		Map<RangeKey, RequestInfoBean> map = this.requestInfoPool.get(type);
		if (null == map) {
			map = new TreeMap<>();
			this.requestInfoPool.put(type, map);
			final Map<Integer, RequestInfoBean> tmpMap = this.requestCodeInfoPool.get(type);
			this.requestCodeInfoPool.put(type, tmpMap);
		}
		map.put(new RangeKey(code), bean);
	}

	/**
	 * 放入一个请求信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月30日 下午7:22:36
	 * @param type 类型
	 * @param rangeCode 区间编码小值
	 * @param rangeMaxCode 区间编码大值
	 * @param bean 请求信息对象
	 */
	public void putRequestInfo(final Integer type, final int rangeCode, final int rangeMaxCode, final RequestInfoBean bean) {
		Map<RangeKey, RequestInfoBean> map = this.requestInfoPool.get(type);
		if (null == map) {
			map = new TreeMap<>();
			this.requestInfoPool.put(type, map);
		}
		map.put(new RangeKey(rangeCode, rangeMaxCode), bean);
	}

	/**
	 * 得到指定行为对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月10日 下午7:15:11
	 * @param type 类型
	 * @param name 对象名称
	 * @return 对象实体
	 */
	public RequestAction getRequestInfo(final Integer type, final String name) {
		final Map<String, RequestAction> map = this.actionMap.get(type);
		if (null == map) {
			return null;
		}
		return map.get(name);
	}

	/**
	 * 得到一个请求信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:45:16
	 * @param type 类型
	 * @param code 消息的编码
	 * @return 请求信息对象
	 */
	public RequestInfoBean getRequestInfo(final Integer type, final Integer code) {
		final Map<Integer, RequestInfoBean> map = this.requestCodeInfoPool.get(type);
		if (null == map) {
			return null;
		}
		RequestInfoBean rib = map.get(code);
		if (null == rib) {
			final Map<RangeKey, RequestInfoBean> tmpMap = this.requestInfoPool.get(type);
			// 因为没有，则从列表中得到
			rib = tmpMap.get(new RangeKey(code, false));
			if (null == rib) {
				// 都没有抛错
				throw new InstallException("Unknow Requset code > " + code);
			}
			// 放入到详细数据池
			map.put(code, rib);
			return rib;
		} else {
			return rib;
		}
	}
}
