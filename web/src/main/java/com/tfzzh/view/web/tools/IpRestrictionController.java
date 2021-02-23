/**
 * @author Xu Weijie
 * @datetime 2017年8月23日_下午7:19:39
 */
package com.tfzzh.view.web.tools;

import java.util.HashMap;
import java.util.Map;

import com.tfzzh.tools.IpCalculator;

/**
 * IP限制控制器
 * 
 * @author Xu Weijie
 * @datetime 2017年8月23日_下午7:19:39
 */
public class IpRestrictionController {

	/**
	 * @author Xu Weijie
	 * @datetime 2017年8月24日_下午5:14:42
	 */
	private String defName = null;

	/**
	 * 对象唯一实例
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月24日_下午5:04:13
	 */
	private static final IpRestrictionController irc = new IpRestrictionController();

	/**
	 * IP限制列表<br />
	 * <目标名,限制列表<ip起始位,ip结束位>><br />
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月24日_下午5:00:53
	 */
	private final Map<String, Map<Long, Long>> ipRes = new HashMap<>();

	/**
	 * @author Xu Weijie
	 * @datetime 2017年8月24日_下午5:06:10
	 */
	private IpRestrictionController() {
	}

	/**
	 * 得到对象实例
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月24日_下午5:06:41
	 * @return 对象唯一实例
	 */
	public static IpRestrictionController getInstance() {
		return IpRestrictionController.irc;
	}

	/**
	 * 验证IP是否为有效ip
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月23日_下午7:21:29
	 * @param name 目标名字
	 * @param ip 目标IP
	 * @return true，通过验证；<br />
	 *         false，未通过验证，失败的；<br />
	 */
	public boolean validateIp(final String name, final String ip) {
		final Map<Long, Long> ir = this.ipRes.get(name);
		if (null == ir) {
			return true;
		}
		return IpCalculator.ipInRange(ip, ir);
	}

	/**
	 * 放入IP限制数据
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月23日_下午9:25:12
	 * @param name 目标名字
	 * @param ipRange IP范围字串，依靠“|”进行大分割；“:”表示范围区间，前面为小值，后面是大值；
	 * @return 被放入的Ip段列表
	 */
	public Map<Long, Long> putIpRestriction(final String name, final String ipRange) {
		return this.putIpRestriction(name, ipRange, false);
	}

	/**
	 * 放入IP限制数据
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月24日_上午9:14:51
	 * @param name 目标名字
	 * @param ipRange IP范围字串，依靠“|”进行大分割；“:”表示范围区间，前面为小值，后面是大值；
	 * @param isDef 是否默认
	 * @return 被放入的IP段列表
	 */
	public Map<Long, Long> putIpRestriction(final String name, final String ipRange, final boolean isDef) {
		if (null == name) {
			return null;
		}
		final Map<Long, Long> ir = IpCalculator.analyzeIpRange(ipRange);
		if (ir.size() > 0) {
			this.ipRes.put(name, ir);
			if (isDef) {
				this.defName = name;
			}
			return ir;
		} else {
			return null;
		}
	}

	/**
	 * 设置为默认
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月24日_上午9:15:53
	 * @param name 目标名字
	 * @return 被设置为默认的IP段列表
	 */
	public boolean setDefault(final String name) {
		if (this.ipRes.containsKey(name)) {
			this.defName = name;
			return true;
		}
		return false;
	}

	/**
	 * 移除IP限制数据
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月23日_下午9:27:34
	 * @param name 目标名字
	 * @return 被移除的IP段列表<br />
	 *         null，为不存在该名称IP段<br />
	 */
	public Map<Long, Long> removeIpRestriction(final String name) {
		final Map<Long, Long> ir = this.ipRes.remove(name);
		if (null != ir) {
			// 此情况name一定不为null
			if (name.equals(this.defName)) {
				this.defName = null;
			}
		}
		return ir;
	}

	/**
	 * 显示指定名称的IP限制数据
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月25日_上午9:15:33
	 * @param name 指定名称
	 * @return IP限制数据
	 */
	public Map<Long, Long> getIpRestriction(final String name) {
		return this.ipRes.get(name);
	}

	/**
	 * 显示所有IP限制数据
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月25日_上午9:15:34
	 * @return IP限制数据
	 */
	public Map<String, Map<Long, Long>> getAllIpRestriction() {
		return this.ipRes;
	}
}
