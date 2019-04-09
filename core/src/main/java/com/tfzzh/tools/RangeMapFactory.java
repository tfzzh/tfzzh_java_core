/**
 * @author XuWeijie
 * @datetime 2015年10月10日_下午4:36:53
 */
package com.tfzzh.tools;

import java.util.Map;
import java.util.TreeMap;

/**
 * 范围Key的Map工厂
 * 
 * @author XuWeijie
 * @datetime 2015年10月10日_下午4:36:53
 */
public class RangeMapFactory {

	/**
	 * 根据文本内容得到Integer为值的范围Key
	 * 
	 * @author XuWeijie
	 * @datetime 2015年10月10日_下午4:38:29
	 * @param txt 数据内容：“|”定义数据分段，“:”定义数据中内容的分段；
	 * @return 被处理完的范围Key的Map
	 */
	public static Map<RangeKey, Integer> getIntegerRangeMap(final String txt) {
		final String[] ls = txt.split("[|]");
		String[] ds;
		RangeKey rk;
		final Map<RangeKey, Integer> bak = new TreeMap<>();
		for (String l : ls) {
			if ((l = l.trim()).length() > 0) {
				ds = l.split("[:]");
				if (ds.length >= 3) {
					try {
						rk = new RangeKey(Integer.parseInt(ds[0]), Integer.parseInt(ds[1]));
						bak.put(rk, Integer.parseInt(ds[2]));
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return bak;
	}

	/**
	 * 根据文本内容得到String为值的范围Key
	 * 
	 * @author XuWeijie
	 * @datetime 2015年10月10日_下午4:40:25
	 * @param txt 数据内容：“|”定义数据分段，“:”定义数据中内容的分段；
	 * @return 被处理完的范围Key的Map
	 */
	public static Map<RangeKey, String> getStringRangeMap(final String txt) {
		final String[] ls = txt.split("[|]");
		String[] ds;
		RangeKey rk;
		final Map<RangeKey, String> bak = new TreeMap<>();
		for (String l : ls) {
			if ((l = l.trim()).length() > 0) {
				ds = l.split("[:]");
				if (ds.length >= 3) {
					try {
						rk = new RangeKey(Integer.parseInt(ds[0]), Integer.parseInt(ds[1]));
						bak.put(rk, ds[2]);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return bak;
	}
}
