/**
 * @author Weijie Xu
 * @dateTime 2014年10月30日 下午5:10:07
 */
package com.tfzzh.tools;

import java.util.ArrayList;
import java.util.List;

import com.tfzzh.exception.ConfigurationException;

/**
 * 范围键对象
 * 
 * @author Weijie Xu
 * @dateTime 2014年10月30日 下午5:10:07
 */
public class RangeKey implements Comparable<RangeKey> {

	/**
	 * 目标值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月30日 下午5:10:36
	 */
	private final int key;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年10月30日 下午5:10:37
	 */
	private final int maxKey;

	/**
	 * 是否为键：<br />
	 * true，作为map的key<br />
	 * false，作为查询用key<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月30日 下午5:31:05
	 */
	private final boolean isKey;

	/**
	 * 所相关的子集信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月9日 下午12:39:05
	 */
	private final Subset ss;

	/**
	 * 所在父级对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月9日 下午3:14:52
	 */
	private RangeKey parent;

	/**
	 * 用于显示的字串信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月18日 下午7:25:35
	 */
	private transient String strInfo = null;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年10月30日 下午5:13:21
	 * @param key 唯一键
	 */
	public RangeKey(final int key) {
		this.key = key;
		this.maxKey = key;
		this.isKey = true;
		this.ss = new Subset();
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年10月30日 下午5:13:23
	 * @param key 范围小值
	 * @param maxKey 范围大值
	 */
	public RangeKey(final int key, final int maxKey) {
		if (key < maxKey) {
			this.key = key;
			this.maxKey = maxKey;
		} else {
			this.key = maxKey;
			this.maxKey = key;
		}
		this.isKey = true;
		this.ss = new Subset();
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年10月30日 下午5:31:36
	 * @param key 唯一键
	 * @param useless 实为无效字段，多态概念
	 */
	public RangeKey(final int key, final boolean useless) {
		this.key = key;
		this.maxKey = key;
		this.isKey = false;
		this.ss = null;
	}

	/**
	 * 得到键值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月30日 下午5:14:20
	 * @return 键值
	 */
	public int getKey() {
		return this.key;
	}

	/**
	 * 你懂的
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月30日 下午5:20:03
	 * @return 你懂的
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.key;
	}

	/**
	 * 得到所相关域的最外层对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月9日 下午3:19:16
	 * @return 所相关域的最外层对象
	 */
	private RangeKey getTop() {
		if (this.parent == null) {
			return this;
		} else {
			return this.parent.getTop();
		}
	}

	/**
	 * 进行范围比较
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月30日 下午5:27:58
	 * @param o 已经存在的key对象
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final RangeKey o) {
		if (this.isKey) {
			if (this == o) {
				// 是同一个对象
				return 0;
			} else if (this.key < o.key) { // 是Key，初期插入Map时用到，o.key与o.maxKey可能都有值
				// 在外，小
				if (this.maxKey < o.key) {
					// o>this
					return -1;
				} else if (this.maxKey > o.key) {
					// o在this内部
					this.ss.putKey(o);
					return -1;
				} else {
					throw new ConfigurationException("Overlap Proxy Key with: [" + this.key + ":" + this.maxKey + "] and [" + o.key + ":" + o.maxKey + "] >> Range can't cross...");
				}
			} else if (this.key == o.key) {
				// 是不被允许的情况
				throw new ConfigurationException("Overlap Proxy Key with: [" + this.key + ":" + this.maxKey + "] and [" + o.key + ":" + o.maxKey + "] >> First key equal...");
			} else // 概念为，this可能在o的内部或 o<this
			if (this.key < o.maxKey) { // 此处不可等
				// 认为this在o内部
				o.ss.putKey(this);
				return 1;
			} else if (this.key == o.maxKey) {
				// 不能有关键值的重叠
				throw new ConfigurationException("Overlap Proxy Key with: [" + this.key + ":" + this.maxKey + "] and [" + o.key + ":" + o.maxKey + "] >> Has key equal...");
			} else {
				// 得到所在最外层对象
				final RangeKey top = o.getTop();
				if (top != o) {
					if ((this.key < top.maxKey) && (this.maxKey > top.maxKey)) {
						throw new ConfigurationException("Overlap Proxy Key with: [" + this.key + ":" + this.maxKey + "] and [" + top.key + ":" + top.maxKey + "] >> Range can't cross...");
					} else if ((this.key == top.maxKey) || (this.maxKey == top.maxKey)) {
						throw new ConfigurationException("Overlap Proxy Key with: [" + this.key + ":" + this.maxKey + "] and [" + top.key + ":" + top.maxKey + "] >> Has key equal...");
					}
					if (top.maxKey > this.key) {
						// 在最外层对象内
						top.ss.putKey(this);
					}
				}
				// 在外，大
				return 1;
			}
		} else {
			// 不是Key，用来做查询是否存在目标值用，此时o.key只有一个值
			if (this.key < o.key) {
				return -1;
			} else if (this.key <= o.maxKey) {
				// 在范围内
				// 此时要判定内部的子集
				for (final RangeKey k : o.ss.subset) {
					if (k.key > this.key) {
						break;
					} else if (k.maxKey >= this.key) {
						// 因为在子集中，所以不在此范围中
						return 1;
					}
				}
				// 因为在内部，则直接返回
				return 0;
			} else {
				final RangeKey top = o.getTop();
				if (top.maxKey > this.key) {
					// 因为有上层的包含关系，然后需要进行前后定位
					// 首先判定父级
					RangeKey par = o.parent;
					while (true) {
						if (par.maxKey > this.key) {
							// 在当前，然后判定子集情况
							for (final RangeKey rk : par.ss.subset) {
								if (rk.key > this.key) {
									break;
								} else if (rk.maxKey >= this.key) {
									// 因为在子集中，所以不在此范围中，认为所在子集，一定是自身后面的部分
									return 1;
								}
							}
							// 因为在该父级，所以向前移动
							return -1;
						}
						par = par.parent;
					}
				}
				return 1;
			}
		}
	}

	/**
	 * 得到小范围边界值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年2月15日_下午3:42:36
	 * @return the minKey
	 */
	public int getMinKey() {
		return this.key;
	}

	/**
	 * 得到大范围边界值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年2月15日_下午3:42:36
	 * @return the maxKey
	 */
	public int getMaxKey() {
		return this.maxKey;
	}

	/**
	 * 你懂的
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月30日 下午6:51:11
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (null == this.strInfo) {
			final StringBuilder sb = new StringBuilder(10);
			sb.append("{key:");
			if (!this.isKey) {
				sb.append(this.key);
			} else {
				// 当前数据
				if (this.key != this.maxKey) {
					sb.append(this.key).append('-').append(this.maxKey);
				} else {
					sb.append(this.key);
				}
				// 父对象数据
				if (this.parent != null) {
					sb.append('<').append(this.parent.key).append('-').append(this.parent.maxKey).append('>');
				}
				// 子集数据
				sb.append(this.ss.toString());
			}
			sb.append('}');
			this.strInfo = sb.toString();
		}
		return this.strInfo;
	}

	/**
	 * 得到查询用key
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月16日_上午11:03:17
	 * @param value 目标查询值
	 * @return 得到的查询用key
	 */
	public static RangeKey getSearchKey(final int value) {
		return new RangeKey(value, false);
	}

	/**
	 * 包含的子集
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月9日 下午12:38:08
	 */
	private class Subset {

		/**
		 * 直接子集列表
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年3月9日 下午12:52:02
		 */
		private final List<RangeKey> subset = new ArrayList<>();

		/**
		 * 放入一个新Key
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年3月9日 下午12:51:40
		 * @param key 新key
		 */
		private void putKey(final RangeKey key) {
			if (null != key.parent) {
				final RangeKey self = RangeKey.this;
				if (self == key.parent) {
					return;
				} else
				// 判定从属情况
				if ((self.key < key.parent.key) && (self.maxKey > key.parent.maxKey)) {
					// 包含其父类，将父类放入到自身中
					self.ss.putKey(key.parent);
				} else if ((self.key > key.parent.key) && (self.maxKey < key.parent.maxKey)) {
					// 在目标父类之下，放入到其父类下
					key.parent.ss.putKey(self);
				} else {
					// 错误情况
					throw new ConfigurationException("Overlap Proxy Key with: [" + self.key + ":" + self.maxKey + "] and [" + key.parent.key + ":" + key.parent.maxKey + "] >> Range can't cross...");
				}
			}
			if (this.subset.size() == 0) {
				// 如果还没有数据直接放入
				this.subset.add(key);
				key.parent = RangeKey.this;
			} else {
				RangeKey tmp;
				for (int i = 0; i < this.subset.size(); i++) {
					if (key == (tmp = this.subset.get(i))) {
						// 因为已经存在，不做多余的事情，但当前来说，不确定该情况是否确实会出现
						return;
					} else if (key.key < tmp.key) {
						// 与当前项比较
						if (key.maxKey < tmp.key) {
							// 都是范围独立的，直接增加
							this.subset.add(i, key);
							key.parent = RangeKey.this;
							return;
						} else
						// 判定最大值
						if (tmp.maxKey >= key.maxKey) {
							// 这是一个错误情况，正确来说，只可能是key.maxKey>tmp.maxKey
							throw new ConfigurationException("Overlap Proxy Key with: [" + key.key + ":" + key.maxKey + "] and [" + tmp.key + ":" + tmp.maxKey + "] >> Range can't cross...");
						} else {
							// 这是一个key包含tmp的情况
							// 将tmp从当前对象中移除，并放入到key的子集中
							this.subset.remove(i);
							key.ss.putKey(tmp);
							// 在当前增加目标key
							if (key.parent != RangeKey.this) {
								this.subset.add(i, key);
								key.parent = RangeKey.this;
							}
							i--;
							// return; 这里不能停，要继续
						}
					} else if (key.key == tmp.key) {
						// 错误的情况
						throw new ConfigurationException("Overlap Proxy Key with: [" + key.key + ":" + key.maxKey + "] and [" + tmp.key + ":" + tmp.maxKey + "] >> First key equal...");
					} else if (key.key < tmp.maxKey) {
						// key，可能包含在tmp中情况
						if (key.maxKey >= tmp.maxKey) {
							// 这是一个错误情况，正确来说，只可能是key.maxKey<tmp.maxKey
							throw new ConfigurationException("Overlap Proxy Key with: [" + key.key + ":" + key.maxKey + "] and [" + tmp.key + ":" + tmp.maxKey + "] >> Range can't cross...");
						}
						// 将key放入到tmp的子集中
						tmp.ss.putKey(key);
						return;
					} else if (key.key == tmp.maxKey) {
						// 这是一个错误情况，正确来说，只可能是key.maxKey>tmp.maxKey
						throw new ConfigurationException("Overlap Proxy Key with: [" + key.key + ":" + key.maxKey + "] and [" + tmp.key + ":" + tmp.maxKey + "] >> Has key equal...");
					}
					// 已经是完全大于情况，去到下一个子集元素，再做判定
				}
				this.subset.add(key);
				key.parent = RangeKey.this;
			}
		}

		/**
		 * 你懂的
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年3月9日 下午2:10:58
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			if (this.subset.size() == 0) {
				return "";
			}
			final StringBuilder sb = new StringBuilder(this.subset.size() * 10);
			sb.append('[');
			boolean isFirst = true;
			for (final RangeKey k : this.subset) {
				if (isFirst) {
					isFirst = false;
				} else {
					sb.append(',');
				}
				if (k.key == k.maxKey) {
					sb.append(k.key);
				} else {
					sb.append(k.key).append('-').append(k.maxKey);
				}
			}
			sb.append(']');
			return sb.toString();
		}
	}
}
