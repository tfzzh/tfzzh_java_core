/**
 * @author Weijie Xu
 * @dateTime 2014年6月14日 上午11:35:15
 */
package com.tfzzh.core.sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * 权重计算，配合TreeMap一类，按照Key排序的Map结构类数据有效
 * 
 * @author Weijie Xu
 * @dateTime 2014年6月14日 上午11:35:15
 */
public class WeightCalculation {

	/**
	 * 是否为权重与区间数量的对调<br />
	 * 最大替换最小，然后次大替换次小，以此循环<br />
	 * 默认是对调<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月14日 上午11:38:59
	 */
	private final boolean swap;

	/**
	 * 总量，用以进行随机数的范围
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月14日 下午2:36:24
	 */
	private int sum;

	/**
	 * 权重序列
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月14日 下午2:46:06
	 */
	private List<WeightBean> weightRangeSequence = null;

	/**
	 * 锁对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月14日 下午5:47:43
	 */
	private final Object lock = new Object();

	/**
	 * 随机值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月14日 下午6:03:56
	 */
	private final Random ran = new Random();

	/**
	 * 比较的对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月16日 上午11:17:31
	 */
	private static final Comparator<WeightBean> cp = new Comparator<>() {

		/**
		 * 结果大值在前，小值在后，能保证0，范围的不出现性
		 * 
		 * @author Weijie Xu
		 * @dateTime 2014年6月14日 下午5:44:57
		 * @param wb1 前值
		 * @param wb2 后值
		 * @return -1，在前；1，在后；
		 */
		@Override
		public int compare(final WeightBean wb1, final WeightBean wb2) {
			return wb1.weight > wb2.weight ? -1 : 1;
		}
	};

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年6月14日 下午12:48:38
	 */
	public WeightCalculation() {
		this(true);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年6月14日 下午6:33:16
	 * @param swap 是否为权重与区间数量的对调
	 */
	public WeightCalculation(final boolean swap) {
		this.swap = swap;
	}

	/**
	 * 放入一套新的权重，此方法会将该对象之前的权重清除
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月14日 下午12:50:17
	 * @param rangeSequence 新的权重列表
	 */
	public void putNewWeight(final Collection<? extends IWeight> rangeSequence) {
		final List<WeightBean> wbl = new ArrayList<>(rangeSequence.size());
		for (final IWeight iw : rangeSequence) {
			wbl.add(new WeightBean(iw));
		}
		// 进行排序
		Collections.sort(wbl, WeightCalculation.cp);
		// System.out.println("------------------");
		// for (final WeightBean wb : wbl) {
		// System.out.println("\t" + wb.toString());
		// }
		// System.out.println("-------------");
		if (this.swap) {
			// 进行大小对调操作
			int min = 0, max = wbl.size() - 1;
			WeightBean minWb = wbl.get(min);
			WeightBean maxWb = wbl.get(max);
			int tmpMinVal;
			int tmpMaxVal;
			o: while (min < max) {
				tmpMinVal = minWb.weight;
				tmpMaxVal = maxWb.weight;
				minWb.weight = tmpMaxVal;
				maxWb.weight = tmpMinVal;
				// 小值控制
				while (true) {
					if (++min >= max) {
						break o;
					}
					minWb = wbl.get(min);
					if (minWb.weight == tmpMinVal) {
						minWb.weight = tmpMaxVal;
					} else {
						break;
					}
				}
				// 大值控制
				while (true) {
					if (--max <= min) {
						break o;
					}
					maxWb = wbl.get(max);
					if (maxWb.weight == tmpMaxVal) {
						maxWb.weight = tmpMinVal;
					} else {
						break;
					}
				}
			}
			// 进行排序
			Collections.sort(wbl, WeightCalculation.cp);
		}
		// 设置范围值
		int sum = 0;
		for (final WeightBean wb : wbl) {
			wb.limitValue = (sum += wb.weight);
		}
		synchronized (this.lock) {
			this.weightRangeSequence = wbl;
			this.sum = sum;
		}
		// System.out.println("------------------");
		// System.out.println("sum : " + this.sum);
		// for (final WeightBean wb : wbl) {
		// System.out.println("\t" + wb.toString());
		// }
		// System.out.println("-------------");
	}

	/**
	 * 得到一个随机选出的ID
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月23日 下午8:24:14
	 * @return 随机选出的ID
	 */
	public int getRandomId() {
		return this.getRandomId(20);
	}

	/**
	 * 得到一个随机选出的ID
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月14日 下午6:01:09
	 * @param radix 开始走算法的基数
	 * @return 随机选出的ID
	 */
	public int getRandomId(final int radix) {
		synchronized (this.lock) {
			if (this.sum == 0) {
				// 此时一定是随机取
				return this.weightRangeSequence.get(this.ran.nextInt(this.weightRangeSequence.size())).id;
			} else if (this.sum < (this.weightRangeSequence.size() * radix)) {
				// 此时随机取一个
				return this.weightRangeSequence.get(this.ran.nextInt(this.weightRangeSequence.size())).id;
			} else {
				final int wei = this.ran.nextInt(this.sum);
				// System.out.print(" > " + wei + " > ");
				for (final WeightBean wb : this.weightRangeSequence) {
					if (wei < wb.limitValue) {
						return wb.id;
					}
				}
			}
		}
		// 正常来说不可能存在的情况
		return -1;
	}

	/**
	 * 权重数据对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月14日 下午2:37:14
	 */
	private class WeightBean {

		/**
		 * key
		 * 
		 * @author Weijie Xu
		 * @dateTime 2014年6月14日 下午2:37:46
		 */
		private final int id;

		/**
		 * 权重值
		 * 
		 * @author Weijie Xu
		 * @dateTime 2014年6月14日 下午2:37:47
		 */
		private int weight;

		/**
		 * 上限值
		 * 
		 * @author Weijie Xu
		 * @dateTime 2014年6月14日 下午2:46:31
		 */
		private int limitValue;

		/**
		 * @author Weijie Xu
		 * @dateTime 2014年6月14日 下午2:50:10
		 * @param iw 权重状态
		 */
		private WeightBean(final IWeight iw) {
			this.id = iw.getKey();
			this.weight = iw.getReferenceValue();
		}

		/**
		 * @author Weijie Xu
		 * @dateTime 2014年6月16日 上午11:21:49
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append('{');
			sb.append("id:").append(this.id).append(",weight:").append(this.weight).append(",limitValue:").append(this.limitValue);
			sb.append('}');
			return sb.toString();
		}
	}
	// public static void main(final String[] args) {
	// List<WeightCal> list = new LinkedList<WeightCal>();
	// list.add(new WeightCal(14, 1000));
	// list.add(new WeightCal(1, 1000));
	// list.add(new WeightCal(2, 500));
	// list.add(new WeightCal(7, 20));
	// list.add(new WeightCal(4, 100));
	// list.add(new WeightCal(12, 0));
	// list.add(new WeightCal(5, 80));
	// list.add(new WeightCal(8, 40));
	// list.add(new WeightCal(13, 0));
	// list.add(new WeightCal(6, 60));
	// list.add(new WeightCal(9, 20));
	// list.add(new WeightCal(3, 300));
	// list.add(new WeightCal(10, 100));
	// list.add(new WeightCal(11, 0));
	// WeightCalculation wc = new WeightCalculation();
	// wc.putNewWeight(list);
	// Map<Integer, Integer> statistics = new TreeMap<Integer, Integer>();
	// Integer val;
	// for (int i = 1000000, id = -1; i > 0; i--) {
	// // System.out.print(i + " >> ");
	// id = wc.getRandomId();
	// // System.out.println(" >> " + id);
	// val = statistics.get(id);
	// if (null == val) {
	// statistics.put(id, 1);
	// } else {
	// statistics.put(id, ++val);
	// }
	// }
	// System.out.println("--------------------------------------");
	// for (Map.Entry<Integer, Integer> v : statistics.entrySet()) {
	// System.out.println(v.getKey() + " > " + v.getValue());
	// }
	// }
	//
	// private static class WeightCal implements IWeight {
	//
	// /**
	// * @author Weijie Xu
	// * @dateTime 2014年6月16日 上午10:13:52
	// */
	// private final int key;
	//
	// /**
	// * @author Weijie Xu
	// * @dateTime 2014年6月16日 上午10:13:56
	// */
	// private final int value;
	//
	// /**
	// * @author Weijie Xu
	// * @dateTime 2014年6月16日 上午10:14:55
	// * @param key
	// * @param value
	// */
	// private WeightCal(final int key, final int value) {
	// this.key = key;
	// this.value = value;
	// }
	//
	// /**
	// * @author Weijie Xu
	// * @dateTime 2014年6月16日 上午10:13:23
	// * @see com.tfzzh.core.sort.IWeight#getKey()
	// */
	// @Override
	// public int getKey() {
	// return this.key;
	// }
	//
	// /**
	// * @author Weijie Xu
	// * @dateTime 2014年6月16日 上午10:13:23
	// * @see com.tfzzh.core.sort.IWeight#getReferenceValue()
	// */
	// @Override
	// public int getReferenceValue() {
	// return this.value;
	// }
	// }
}
