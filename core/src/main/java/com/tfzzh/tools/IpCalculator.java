/**
 * @author XuWeijie
 * @dateTime Oct 30, 2010 7:15:48 PM
 */
package com.tfzzh.tools;

import java.util.Map;
import java.util.TreeMap;

/**
 * Ip计算器
 * 
 * @author XuWeijie
 * @dateTime Oct 30, 2010 7:15:48 PM
 * @model
 */
public class IpCalculator {

	/**
	 * 将字符串的IP转换为long型数值<br />
	 * 暂时只支持IPv4<br />
	 * 进入前保证该字串内容为标准ip结构<br />
	 * 
	 * @author XuWeijie
	 * @dateTime Oct 30, 2010 7:16:57 PM
	 * @param ip Ip字符串
	 * @return 对应的整型数
	 */
	public static long ipv4StringToLong(final String ip) {
		final String[] parts = ip.split("[.]");
		return (Long.parseLong(parts[0]) * 16777216l) + (Long.parseLong(parts[1]) * 65536l) + (Long.parseLong(parts[2]) * 256l) + Long.parseLong(parts[3]);
	}

	/**
	 * 得到ip范围集合<br />
	 * 此类中未做排序工作<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月18日 下午7:16:44
	 * @param rangeString IP范围字串，依靠“|”进行大分割；“:”表示范围区间，前面为小值，后面是大值；
	 * @return p范围数组类集合
	 */
	public static Map<Long, Long> analyzeIpRange(final String rangeString) {
		final String[] lines = rangeString.split("[|]");
		String[] ds;
		// 正常认为，此类数据是配置出来的，不会存在冗余的概念
		final Map<Long, Long> ir = new TreeMap<>();
		for (String l : lines) {
			if ((l = l.trim()).length() != 0) {
				ds = l.split("[:]");
				switch (ds.length) {
				case 1: // 为等于关系
					ir.put(IpCalculator.ipv4StringToLong(ds[0]), IpCalculator.ipv4StringToLong(ds[0]));
					break;
				case 2:
					ir.put(IpCalculator.ipv4StringToLong(ds[0]), IpCalculator.ipv4StringToLong(ds[1]));
					break;
				}
			}
		}
		return ir;
	}

	/**
	 * 目标IP是否在目标IP范围内
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月18日 下午7:28:08
	 * @param ip 目标IP
	 * @param ipRange 目标IP范围内
	 * @return true，在范围内；<br />
	 *         false，不在范围内；<br />
	 */
	public static boolean ipInRange(final String ip, final Map<Long, Long> ipRange) {
		final long ipL = IpCalculator.ipv4StringToLong(ip);
		for (final Map.Entry<Long, Long> ent : ipRange.entrySet()) {
			if ((ipL >= ent.getKey()) && (ipL <= ent.getValue())) {
				return true;
			} else if (ipL < ent.getKey()) {
				// 提前结束
				return false;
			}
		}
		return false;
	}
}
