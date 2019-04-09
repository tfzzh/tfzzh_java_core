/**
 * @author Weijie Xu
 * @dateTime 2014-3-20 下午8:34:29
 */
package com.tfzzh.socket.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.tfzzh.exception.InstallException;
import com.tfzzh.socket.bean.RequestInfoBean;
import com.tfzzh.tools.RangeKey;

/**
 * 请求消息池
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-20 下午8:34:29
 */
public class RequestPool {

	/**
	 * 请求消息编码池
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:36:39
	 */
	private final Map<Integer, RequestInfoBean> requestCodeInfoPool;

	/**
	 * 请求消息池
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月30日 下午7:20:54
	 */
	private final Map<RangeKey, RequestInfoBean> requestInfoPool;

	/**
	 * 唯一实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:36:46
	 */
	private static Map<String, RequestPool> rpMap = new HashMap<>();

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:36:47
	 */
	private RequestPool() {
		this.requestCodeInfoPool = new HashMap<>();
		// 此对象一定是TreeMap，或有排序概念的Map为子类
		this.requestInfoPool = new TreeMap<>();
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
	 * @param type 类型值
	 * @return 对象唯一实例
	 */
	public static RequestPool getInstance(final String type) {
		RequestPool rp = RequestPool.rpMap.get(type);
		if (null == rp) {
			rp = new RequestPool();
			RequestPool.rpMap.put(type, rp);
		}
		return rp;
	}

	/**
	 * 放入一个请求信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:41:50
	 * @param code 编码
	 * @param bean 请求信息对象
	 */
	public void putRequestInfo(final int code, final RequestInfoBean bean) {
		this.requestInfoPool.put(new RangeKey(code), bean);
	}

	/**
	 * 放入一个请求信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月30日 下午7:22:36
	 * @param rangeCode 区间编码小值
	 * @param rangeMaxCode 区间编码大值
	 * @param bean 请求信息对象
	 */
	public void putRequestInfo(final int rangeCode, final int rangeMaxCode, final RequestInfoBean bean) {
		this.requestInfoPool.put(new RangeKey(rangeCode, rangeMaxCode), bean);
	}

	/**
	 * 得到一个请求信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:45:16
	 * @param code 消息的编码
	 * @return 请求信息对象
	 */
	public RequestInfoBean getRequestInfo(final Integer code) {
		RequestInfoBean rib = this.requestCodeInfoPool.get(code);
		if (null == rib) {
			// 因为没有，则从列表中得到
			rib = this.requestInfoPool.get(new RangeKey(code, false));
			if (null == rib) {
				// 都没有抛错
				throw new InstallException("Unknow Requset code > " + code);
			}
			// 放入到详细数据池
			this.requestCodeInfoPool.put(code, rib);
			return rib;
		} else {
			return rib;
		}
	}
}
