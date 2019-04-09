/**
 * @author 许纬杰
 * @datetime 2016年6月2日_上午11:31:49
 */
package com.tfzzh.tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 排行控制<br />
 * 可以根据KEY得到排行信息<br />
 * 可以根据top值得到排行信息<br />
 * 在触发TopDataInfo.refresh()时，可以更新排行数据<br />
 * 
 * @author 许纬杰
 * @datetime 2016年6月2日_上午11:31:49
 * @param <K> key的类型
 * @param <B> 实际数据对象的类型
 */
public class TopControl<K, B extends TopDataInfo<K>> {

	/**
	 * 排行数据列表，key为键
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_上午11:48:33
	 */
	private final Map<K, TopNodeInfo> keys;

	/**
	 * 首位置位置
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午1:41:05
	 */
	private TopNode<K, B> first;

	/**
	 * 末位置
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午1:41:06
	 */
	private TopNode<K, B> last;

	/**
	 * 当前所指向节点
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午2:52:28
	 */
	private TopNode<K, B> currNode;

	/**
	 * 是否升序排列<br />
	 * true，是升序排列，0在最前；<br />
	 * false，是降序排列，0在最后；<br />
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午2:15:56
	 */
	private final OrderEnum order;

	/**
	 * 锁
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_上午11:48:30
	 */
	protected final Lock kLock = new ReentrantLock();

	/**
	 * 默认为降序排列<br />
	 * 大值在前面<br />
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午2:20:02
	 */
	public TopControl() {
		this(false);
	}

	/**
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午1:21:51
	 * @param isAsc 是否升序排列<br />
	 *           true，是升序排列，0在最前；<br />
	 *           false，是降序排列，0在最后；<br />
	 */
	public TopControl(final boolean isAsc) {
		this(isAsc, 1000);
	}

	/**
	 * @author 许纬杰
	 * @datetime 2016年6月3日_下午3:23:17
	 * @param isAsc 是否升序排列<br />
	 *           true，是升序排列，0在最前；<br />
	 *           false，是降序排列，0在最后；<br />
	 * @param initSize 初始化空间大小
	 */
	public TopControl(final boolean isAsc, final int initSize) {
		this.order = OrderEnum.getOrder(isAsc);
		this.keys = new HashMap<>(initSize);
	}

	/**
	 * 放入一个新的排行数据，不会覆盖
	 * 
	 * @author 许纬杰
	 * @datetime 2016年7月27日_下午10:05:15
	 * @param td 排行数据
	 * @return 放入后所在当前名次
	 */
	public int putNode(final B td) {
		return this.putNode(td, false);
	}

	/**
	 * 放入一个新的排行数据
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_上午11:56:51
	 * @param td 排行数据
	 * @param isCover 是否覆盖，true，是要覆盖的
	 * @return 放入后所在当前名次
	 */
	public int putNode(final B td, final boolean isCover) {
		try {
			this.kLock.lock();
			final TopNodeInfo otd = this.keys.get(td.getKey());
			if (null != otd) {
				if (isCover) {
					otd.changeTopData(td, this);
				}
				return otd.ind.get();
			}
			// 首先找到目标位置，因为是新的，所以需要先进行初始化数据
			// final long ref = td.initReferenceValue();
			final TopNode<K, B> tn = this.first;
			if (null == tn) {
				// 第一个数据
				final TopNodeInfo newTn = new TopNodeInfo(new InnerIndex(1), td, this);
				this.last = newTn;
				this.first = newTn;
				this.currNode = newTn;
				this.keys.put(td.getKey(), newTn);
				td.refresh();
				return newTn.ind.get();
			}
			td.initReferenceValue(false);
			final TopNode<K, B> newTn = this.order.putNewNode(this, td);
			// 创建节点数据
			// 具体放入
			this.keys.put(td.getKey(), (TopNodeInfo) newTn);
			// this.print();
			td.refresh();
			return newTn.getIndex().get();
		} finally {
			this.kLock.unlock();
		}
	}

	/**
	 * 放入到第一个位置
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午9:51:21
	 * @param td 新的节点
	 * @return 被创建的节点
	 */
	private TopNode<K, B> putFirst(final TopDataInfo<K> td) {
		final TopNode<K, B> of = this.first;
		final TopNode<K, B> newTn = new TopNodeInfo(new InnerIndex(1), td, this);
		this.first = newTn;
		newTn.setBehind(of);
		of.setBefore(newTn);
		// 向下增量
		of.indexAddTo(-1);
		return newTn;
	}

	/**
	 * 放入到最后一个位置
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午9:51:21
	 * @param td 新的节点
	 * @return 被创建的节点
	 */
	private TopNode<K, B> putLast(final TopDataInfo<K> td) {
		final TopNode<K, B> ol = this.last;
		final TopNode<K, B> newTn = new TopNodeInfo(new InnerIndex(this.size() + 1), td, this);
		this.last = newTn;
		newTn.setBefore(ol);
		ol.setBehind(newTn);
		return newTn;
	}

	//
	// /**
	// * 放入到一个节点后面
	// *
	// * @author 许纬杰
	// * @datetime 2016年6月2日_下午9:51:21
	// * @param tc 排行控制对象
	// * @param target 目标节点
	// * @param tdi 新的节点
	// * @param ind 排行位置
	// * @return 被创建的节点
	// */
	// private TopNode<K, B> putNodeBehind(final TopControl<K, B> tc, final TopNode<K, B> target, final TopDataInfo<K> tdi, final int ind) {
	// final TopNode<K, B> bf = target.getBefore();
	// final TopNode<K, B> newTn = tc.createNewNode(ind, tdi);
	// target.setBefore(newTn);
	// newTn.setBefore(bf);
	// bf.setBehind(newTn);
	// newTn.setBehind(target);
	// // 向下增量
	// target.indexAddTo(-1);
	// return newTn;
	// }
	/**
	 * 得到目标节点原始对象
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午6:30:26
	 * @param key 目标键
	 * @return 目标节点信息
	 */
	public B getNode(final K key) {
		final TopNodeInfo tni = this.keys.get(key);
		if (null == tni) {
			return null;
		} else {
			return tni.getTopData();
		}
	}

	/**
	 * 得到目标节点原始对象
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月3日_下午6:05:10
	 * @param ind 目标排名位置
	 * @return 目标节点对象信息
	 */
	public B getNode(final int ind) {
		final TopNode<K, B> tn = this.getTopNode(ind);
		return null == tn ? null : tn.getTopData();
	}

	/**
	 * 得到目标节点
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午6:30:28
	 * @param ind 目标排名位置
	 * @return 目标节点信息
	 */
	private TopNode<K, B> getTopNode(final int ind) {
		try {
			this.kLock.lock();
			if (ind < 1) {
				// 取舍范围
				return null;
			} else if (ind == 1) {
				return this.first;
			} else if (ind > this.size()) {
				// 取舍范围
				return null;
			} else if (ind == this.size()) {
				return this.last;
			} else if (ind == this.index()) {
				return this.currNode;
			}
			// 以上都不是
			// 进行差值查询
			final int d2 = Math.abs(ind - this.index());
			final int d3 = this.size() - ind;
			if (ind <= d2) {
				if (ind <= d3) {
					// ind最小
					// 从最开始数
					return this.findNodeFromFirst(ind);
				} else if (d3 < d2) {
					// d3最小
					// 从最后向前数
					return this.findNodeFromLast(d3);
				} else {
					// d2最小
					// 从节点开始
					return this.findNodeForNode(d2, ind > this.index());
				}
			} else {
				if (d2 < d3) {
					// d2是最小的
					// 从中间开始数
					return this.findNodeForNode(d2, ind > this.index());
				} else {
					// d3最小
					// 从最后向前数
					return this.findNodeFromLast(d3);
				}
			}
		} finally {
			this.kLock.unlock();
		}
	}

	/**
	 * 找到节点从开始
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午8:03:50
	 * @param len 位置差值
	 * @return 目标节点信息
	 */
	private TopNode<K, B> findNodeFromFirst(final int len) {
		TopNode<K, B> tn = this.first;
		int ind = 1;
		while (ind < len) {
			tn = tn.getBehind();
			ind++;
		}
		this.currNode = tn;
		return tn;
	}

	/**
	 * 找到节点从最后
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午8:03:51
	 * @param len 位置差值
	 * @return 目标节点信息
	 */
	private TopNode<K, B> findNodeFromLast(int len) {
		TopNode<K, B> tn = this.last;
		while (--len >= 0) {
			tn = tn.getBefore();
		}
		this.currNode = tn;
		return tn;
	}

	/**
	 * 找到节点从中间
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午8:03:48
	 * @param len 位置差值
	 * @param forward 是否向前：true，向前behind；false，向后before；
	 * @return 目标节点信息
	 */
	private TopNode<K, B> findNodeForNode(int len, final boolean forward) {
		TopNode<K, B> tn = this.currNode;
		if (forward) {
			while (--len >= 0) {
				tn = tn.getBehind();
			}
		} else {
			while (--len >= 0) {
				tn = tn.getBefore();
			}
		}
		this.currNode = tn;
		return tn;
	}

	/**
	 * 进行数据刷新<br />
	 * 是在调用方法中存在的锁<br />
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午1:20:06
	 * @param tdi 目标数据
	 * @param isBigger 是否变大了：true，是变大了；
	 * @return true，变更成功；
	 */
	protected boolean changeTop(final TopDataInfo<K> tdi, final boolean isBigger) {
		final TopNodeInfo tn = this.keys.get(tdi.getKey());
		if (null == tn) {
			// 因为已经不存在了
			return false;
		}
		this.order.toTargetNode(this, tn, isBigger);
		return true;
	}

	/**
	 * 按照key进行节点移除
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午2:05:00
	 * @param key key值
	 */
	protected void remove(final K key) {
		try {
			this.kLock.lock();
			final TopNodeInfo tn = this.keys.remove(key);
			if (null != tn) {
				tn.disconnect();
				if (this.currNode == tn) {
					this.currNode = this.first;
				}
			}
		} finally {
			this.kLock.unlock();
		}
	}

	/**
	 * 按照排名进行节点移除
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午2:05:01
	 * @param ind 排名值
	 */
	protected void remove(final int ind) {
		try {
			this.kLock.lock();
			final TopNode<K, B> tn = this.getTopNode(ind);
			if (null != tn) {
				final TopNodeInfo tni = (TopNodeInfo) tn;
				tni.disconnect();
				if (this.currNode == tn) {
					this.currNode = this.first;
				}
				this.keys.remove(tni.tdi.getKey());
			}
		} finally {
			this.kLock.unlock();
		}
	}

	/**
	 * 得到当前指针位置
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月3日_下午2:11:12
	 * @return 当前指针位置
	 */
	private int index() {
		if (null == this.currNode) {
			return 0;
		}
		return this.currNode.getIndex().get();
	}

	/**
	 * 得到数据长度
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午9:12:16
	 * @return 数据长度
	 */
	public int size() {
		return this.keys.size();
	}

	/**
	 * 得到一个迭代器
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月7日_下午6:14:19
	 * @return 一个新的迭代器
	 */
	public Iterator<B> iterator() {
		return new Itr();
	}

	/**
	 * 迭代器
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月7日_下午6:16:14
	 */
	public class Itr implements Iterator<B> {

		/**
		 * 当前节点
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月7日_下午6:15:33
		 */
		private TopNode<K, B> currNode;

		/**
		 * @author 许纬杰
		 * @datetime 2016年6月7日_下午6:16:07
		 */
		private Itr() {
		}

		/**
		 * 是否存在下一个
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月7日_下午6:11:52
		 * @return true，是存在下一个的
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			if (null == this.currNode) {
				return TopControl.this.first != null;
			}
			return null != this.currNode.getBehind();
		}

		/**
		 * 得到下一个
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月7日_下午6:11:52
		 * @return 下一个
		 * @see java.util.Iterator#next()
		 */
		@Override
		public B next() {
			if (null == this.currNode) {
				if (null == TopControl.this.first) {
					return null;
				}
				this.currNode = TopControl.this.first;
				return this.currNode.getTopData();
			} else {
				if (null == this.currNode.getBehind()) {
					return null;
				}
				return (this.currNode = this.currNode.getBehind()).getTopData();
			}
		}

		/**
		 * @author 许纬杰
		 * @datetime 2016年6月7日_下午6:11:52
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			if (null == this.currNode) {
				// 移除第一个
				if (null == TopControl.this.first) {
					// 此时无操作
					return;
				}
				((TopNodeInfo) TopControl.this.first).disconnect();
			} else {
				((TopNodeInfo) this.currNode).disconnect();
			}
		}
	}

	// /**
	// * 打印
	// *
	// * @author 许纬杰
	// * @datetime 2016年6月2日_下午8:38:44
	// */
	// public void print() {
	// final StringBuilder sb = new StringBuilder();
	// TopNode<K, B> tn = this.first;
	// sb.append("[");
	// boolean isFirst = true;
	// do {
	// if (isFirst) {
	// isFirst = false;
	// } else {
	// sb.append('\n');
	// }
	// sb.append(tn.toString());
	// } while (null != (tn = tn.getBehind()));
	// sb.append("]");
	// System.out.println("first:" + this.first.toString());
	// System.out.println(sb.toString());
	// System.out.println("last:" + this.last.toString());
	// System.out.println(this.size());
	// System.out.println(" ============== ");
	// }
	/**
	 * 排行节点
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午4:19:22
	 * @param <K> key的类型
	 * @param <B> 实际数据对象的类型
	 */
	private interface TopNode<K, B extends TopDataInfo<K>> {

		/**
		 * 得到索引对象
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午5:18:25
		 * @return 索引对象
		 */
		InnerIndex getIndex();

		/**
		 * 得到排行数据
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午6:16:00
		 * @return 排行数据
		 */
		B getTopData();

		/**
		 * 索引位向后增量
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午5:53:32
		 * @param len 增量的长度，-1，为到头位置
		 */
		void indexAddTo(int len);

		/**
		 * 索引位向后减量
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午5:53:33
		 * @param len 减量长度，-1，为到头位置
		 */
		void indexDecTo(int len);

		/**
		 * 得到参考值
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午4:22:00
		 * @return 参考值
		 */
		long getRef();

		/**
		 * 得到前一个节点
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午4:23:02
		 * @return 前一个节点
		 */
		TopNode<K, B> getBefore();

		/**
		 * 设置前一个节点
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午4:45:28
		 * @param tn 新的节点
		 */
		void setBefore(TopNode<K, B> tn);

		/**
		 * 得到后一个节点
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午4:23:02
		 * @return 后一个节点
		 */
		TopNode<K, B> getBehind();

		/**
		 * 设置后一个节点
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午4:45:26
		 * @param tn 新的节点
		 */
		void setBehind(TopNode<K, B> tn);
	}

	/**
	 * 排行节点信息
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午1:40:19
	 */
	public class TopNodeInfo implements TopNode<K, B> {

		/**
		 * 索引
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午1:41:55
		 */
		private final InnerIndex ind;

		/**
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午1:46:51
		 */
		private TopDataInfo<K> tdi;

		/**
		 * 前一个数据<br />
		 * null，表示对第一个数据<br />
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午2:24:39
		 */
		private TopNodeInfo before;

		/**
		 * 后一个数据<br />
		 * null，表示为最后一个数据<br />
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午2:24:40
		 */
		private TopNodeInfo behind;

		/**
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午1:57:38
		 * @param ind 排位索引
		 * @param tdi 数据对象
		 * @param tc 排行控制
		 */
		public TopNodeInfo(final InnerIndex ind, final TopDataInfo<K> tdi, final TopControl<K, ? extends TopDataInfo<K>> tc) {
			this.ind = ind;
			this.tdi = tdi;
			this.tdi.setTop(ind);
			this.tdi.setTopControl(tc);
		}

		/**
		 * 得到索引对象
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午5:18:42
		 * @return 索引对象
		 * @see com.tfzzh.tools.TopControl.TopNode#getIndex()
		 */
		@Override
		public InnerIndex getIndex() {
			return this.ind;
		}

		/**
		 * 得到排行数据
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午6:16:25
		 * @return 排行数据
		 * @see com.tfzzh.tools.TopControl.TopNode#getTopData()
		 */
		@SuppressWarnings("unchecked")
		@Override
		public B getTopData() {
			return (B) this.tdi;
		}

		/**
		 * 变更元素
		 * 
		 * @author 许纬杰
		 * @datetime 2016年7月27日_下午10:20:08
		 * @param td 数据对象
		 * @param tc 排行控制
		 */
		private void changeTopData(final B td, final TopControl<K, ? extends TopDataInfo<K>> tc) {
			this.tdi = td;
			this.tdi.setTop(this.ind);
			this.tdi.setTopControl(tc);
		}

		/**
		 * 索引位向后增量
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午5:55:39
		 * @param len 增量的长度，-1，为到头位置
		 * @see com.tfzzh.tools.TopControl.TopNode#indexAddTo(int)
		 */
		@Override
		public void indexAddTo(int len) {
			if ((len < 0) || (--len > 0)) {
				this.ind.add();
				if (null != this.behind) {
					this.behind.indexAddTo(len);
				}
			}
		}

		/**
		 * 索引位向后减量
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午5:55:39
		 * @param len 减量长度，-1，为到头位置
		 * @see com.tfzzh.tools.TopControl.TopNode#indexDecTo(int)
		 */
		@Override
		public void indexDecTo(int len) {
			if ((len < 0) || (--len > 0)) {
				this.ind.dec();
				if (null != this.behind) {
					this.behind.indexDecTo(len);
				}
			}
		}

		/**
		 * 得到参考值
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午4:23:43
		 * @return 参考值
		 * @see com.tfzzh.tools.TopControl.TopNode#getRef()
		 */
		@Override
		public long getRef() {
			return this.tdi.getReferenceValue();
		}

		/**
		 * 得到前一个节点
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午4:23:43
		 * @return 前一个节点
		 * @see com.tfzzh.tools.TopControl.TopNode#getBefore()
		 */
		@Override
		public TopNode<K, B> getBefore() {
			return this.before;
		}

		/**
		 * 设置前一个节点
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午4:45:51
		 * @param tn 新的节点
		 * @see com.tfzzh.tools.TopControl.TopNode#setBefore(com.tfzzh.tools.TopControl.TopNode)
		 */
		@Override
		public void setBefore(final TopNode<K, B> tn) {
			this.before = (TopNodeInfo) tn;
		}

		/**
		 * 得到后一个节点
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午4:23:43
		 * @return 后一个节点
		 * @see com.tfzzh.tools.TopControl.TopNode#getBehind()
		 */
		@Override
		public TopNode<K, B> getBehind() {
			return this.behind;
		}

		/**
		 * 设置后一个节点
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午4:45:51
		 * @param tn 新的节点
		 * @see com.tfzzh.tools.TopControl.TopNode#setBehind(com.tfzzh.tools.TopControl.TopNode)
		 */
		@Override
		public void setBehind(final TopNode<K, B> tn) {
			this.behind = (TopNodeInfo) tn;
		}

		// /**
		// * 连接在目标之后
		// *
		// * @author 许纬杰
		// * @datetime 2016年6月2日_下午2:41:22
		// * @param target 前面的目标
		// */
		// private void connectBefore(final TopNodeInfo target) {
		// final TopNodeInfo bh = target.behind;
		// target.behind = this;
		// if (null != bh) {
		// bh.before = this;
		// this.behind = bh;
		// }
		// }
		// /**
		// * 连接在目标之前
		// *
		// * @author 许纬杰
		// * @datetime 2016年6月2日_下午2:41:25
		// * @param target 后面的目标
		// */
		// private void connectBehind(final TopNodeInfo target) {
		// final TopNodeInfo bf = target.before;
		// target.before = this;
		// if (null != bf) {
		// bf.behind = this;
		// this.before = bf;
		// }
		// }
		/**
		 * 断开
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午2:42:49
		 */
		private void disconnect() {
			final TopNodeInfo bf = this.before;
			final TopNodeInfo bh = this.behind;
			if (null != bf) {
				bf.behind = bh;
			} else {
				// 是第一个
				TopControl.this.first = this.behind;
			}
			if (null != bh) {
				bh.before = bf;
				bh.indexDecTo(-1);
			} else {
				// 是最后一个
				TopControl.this.last = this.before;
			}
			this.behind = null;
			this.before = null;
		}

		/**
		 * 你懂的
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午8:43:10
		 * @return 你懂的
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("{i:").append(this.ind.get());
			sb.append(",tdi:").append(this.tdi);
			sb.append(",before:").append(null == this.before ? null : this.before.tdi.getKey());
			sb.append(",behind:").append(null == this.behind ? null : this.behind.tdi.getKey());
			sb.append("}");
			return sb.toString();
		}
	}

	/**
	 * 排序规则
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午4:07:01
	 */
	private enum OrderEnum {
		/**
		 * 从小到大
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午4:06:58
		 */
		Asc {

			@Override
			protected <K, B extends TopDataInfo<K>> void toTargetNode(final TopControl<K, B> tc, final TopNode<K, B> tn, final boolean isBigger) {
				if (isBigger) {
					// 向后移动判定
					TopNode<K, B> bh = tn.getBehind();
					if (null == bh) {
						// 因为已经到头
						return;
					}
					TopNode<K, B> bf;
					while (tn.getRef() >= bh.getRef()) {
						// 需要向后移动
						bf = tn.getBefore();
						if (null != bf) {
							bf.setBehind(bh);
						}
						if (null != bh.getBehind()) {
							bh.getBehind().setBefore(tn);
						}
						tn.setBehind(bh.getBehind());
						bh.setBefore(bf);
						bh.setBehind(tn);
						tn.setBefore(bh);
						bh.getIndex().dec();
						// c++;
						if (null == bh.getBefore()) {
							tc.first = bh;
						}
						if (null == (bh = tn.getBehind())) {
							tc.last = tn;
							break;
						}
					}
					// 进行位置移动
					// sbh.indexDecTo(c);
					if (null == tn.getBefore()) {
						tn.getIndex().reset(tn.getBehind().getIndex().get() - 1);
					} else {
						tn.getIndex().reset(tn.getBefore().getIndex().get() + 1);
					}
					return;
				} else {
					// 向前移动判定
					TopNode<K, B> bf = tn.getBefore();
					if (null == bf) {
						// 因为已经到头
						return;
					}
					TopNode<K, B> bh = null;
					while (tn.getRef() < bf.getRef()) {
						bh = tn.getBehind();
						if (null != bh) {
							bh.setBefore(bf);
						}
						if (null != bf.getBefore()) {
							bf.getBefore().setBehind(tn);
						}
						tn.setBefore(bf.getBefore());
						bf.setBehind(bh);
						bf.setBefore(tn);
						tn.setBehind(bf);
						bf.getIndex().add();
						// c++;
						if (null == bf.getBehind()) {
							tc.last = bf;
						}
						if (null == (bf = tn.getBefore())) {
							tc.first = tn;
							break;
						}
					}
					// tn.getBehind().indexAddTo(c);
					if (null != tn.getBehind()) {
						tn.getIndex().reset(tn.getBehind().getIndex().get() - 1);
					} else {
						tn.getIndex().reset(tn.getBefore().getIndex().get() + 1);
					}
				}
			}

			@Override
			protected <K, B extends TopDataInfo<K>> TopNode<K, B> putNewNode(final TopControl<K, B> tc, final TopDataInfo<K> td) {
				return tc.putFirst(td);
			}
		},
		/**
		 * 从大到小
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午4:06:59
		 */
		Desc {

			@Override
			protected <K, B extends TopDataInfo<K>> void toTargetNode(final TopControl<K, B> tc, final TopNode<K, B> tn, final boolean isBigger) {
				if (!isBigger) {
					// 向后移动判定
					TopNode<K, B> bh = tn.getBehind();
					if (null == bh) {
						// 因为已经到头
						return;
					}
					// if (tc.first == tn) {
					// tc.first = bh;
					// }
					TopNode<K, B> bf;
					while (tn.getRef() < bh.getRef()) {
						// 需要向后移动
						bf = tn.getBefore();
						if (null != bf) {
							bf.setBehind(bh);
						}
						if (null != bh.getBehind()) {
							bh.getBehind().setBefore(tn);
						}
						tn.setBehind(bh.getBehind());
						bh.setBefore(bf);
						bh.setBehind(tn);
						tn.setBefore(bh);
						bh.getIndex().dec();
						// c++;
						if (null == bh.getBefore()) {
							tc.first = bh;
						}
						if (null == (bh = tn.getBehind())) {
							tc.last = tn;
							break;
						}
					}
					// 进行位置移动
					if (null == tn.getBefore()) {
						tn.getIndex().reset(tn.getBehind().getIndex().get() - 1);
					} else {
						tn.getIndex().reset(tn.getBefore().getIndex().get() + 1);
					}
					return;
				} else {
					// 向前移动判定
					TopNode<K, B> bf = tn.getBefore();
					if (null == bf) {
						// 因为已经到头
						return;
					}
					// if (tc.last == tn) {
					// tc.last = bf;
					// }
					// int c = 1;
					TopNode<K, B> bh = null;
					while (tn.getRef() >= bf.getRef()) {
						bh = tn.getBehind();
						if (null != bh) {
							bh.setBefore(bf);
						}
						if (null != bf.getBefore()) {
							bf.getBefore().setBehind(tn);
						}
						tn.setBefore(bf.getBefore());
						bf.setBehind(bh);
						bf.setBefore(tn);
						tn.setBehind(bf);
						bf.getIndex().add();
						// c++;
						if (null == bf.getBehind()) {
							tc.last = bf;
						}
						if (null == (bf = tn.getBefore())) {
							tc.first = tn;
							break;
						}
					}
					if (null != tn.getBehind()) {
						tn.getIndex().reset(tn.getBehind().getIndex().get() - 1);
					} else {
						tn.getIndex().reset(tn.getBefore().getIndex().get() + 1);
					}
				}
			}

			@Override
			protected <K, B extends TopDataInfo<K>> TopNode<K, B> putNewNode(final TopControl<K, B> tc, final TopDataInfo<K> td) {
				return tc.putLast(td);
			}
		};

		/**
		 * 微笑比较，以之前位置为基础，向上或下顺次比较
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月3日_上午10:43:34
		 * @param <K> key的类型
		 * @param <B> 实际数据对象的类型
		 * @param tc 排行控制
		 * @param tn 当前节点
		 * @param isBigger 数值是否比之前的大
		 */
		protected abstract <K, B extends TopDataInfo<K>> void toTargetNode(TopControl<K, B> tc, TopNode<K, B> tn, boolean isBigger);

		/**
		 * 放入一个新节点
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月23日_下午9:43:40
		 * @param <K> key的类型
		 * @param <B> 实际数据对象的类型
		 * @param tc 排行控制
		 * @param td 排行数据
		 * @return 被放入成功后的节点
		 */
		protected abstract <K, B extends TopDataInfo<K>> TopNode<K, B> putNewNode(TopControl<K, B> tc, TopDataInfo<K> td);

		/**
		 * 得到排序规则
		 * 
		 * @author 许纬杰
		 * @datetime 2016年6月2日_下午4:07:59
		 * @param isAsc 是否从小到大
		 * @return 对应的排序规则
		 */
		private static OrderEnum getOrder(final boolean isAsc) {
			return isAsc ? Asc : Desc;
		}
	}
	// // TODO 一下，测试用部分
	//
	// public static class TestTopData extends TopDataInfo<String> {
	//
	// /**
	// * @author 许纬杰
	// * @datetime 2016年6月23日_下午6:44:54
	// */
	// private long val = 0;
	//
	// /**
	// * @author 许纬杰
	// * @datetime 2016年6月2日_下午8:54:14
	// * @param key
	// */
	// protected TestTopData(String key) {
	// super(key);
	// }
	//
	// /**
	// * @author 许纬杰
	// * @datetime 2016年6月2日_下午8:54:29
	// * @return
	// * @see com.tfzzh.tools.TopDataInfo#refreshReferenceScore()
	// */
	// @Override
	// protected long refreshReferenceScore() {
	// // if (this.val == 0) {
	// return this.val = TfzzhRandom.getInstance().nextInt(1000000);
	// // } else {
	// // return this.val += TfzzhRandom.getInstance().nextInt(100000);
	// // }
	// }
	// }
	//
	// public static class TestTopData2 extends TopDataInfo<Integer> {
	//
	// /**
	// * @author 许纬杰
	// * @datetime 2016年6月23日_下午6:44:54
	// */
	// private long val = 0;
	//
	// /**
	// * @author 许纬杰
	// * @datetime 2016年6月2日_下午8:54:14
	// * @param key
	// */
	// protected TestTopData2(Integer key) {
	// super(key);
	// }
	//
	// /**
	// * @author 许纬杰
	// * @datetime 2016年6月2日_下午8:54:29
	// * @return
	// * @see com.tfzzh.tools.TopDataInfo#refreshReferenceScore()
	// */
	// @Override
	// protected long refreshReferenceScore() {
	// if (this.val == 0) {
	// return this.val = TfzzhRandom.getInstance().nextInt(1000000);
	// } else {
	// return this.val += TfzzhRandom.getInstance().nextInt(100000);
	// }
	// }
	// }
	//
	// /**
	// * @author 许纬杰
	// * @datetime 2016年6月2日_下午8:55:34
	// * @param args
	// */
	// public static void main(String[] args) {
	// test1();
	// }
	//
	// private static void test2() {
	// long l1 = System.currentTimeMillis();
	// TopControl<Integer, TestTopData2> tc = new TopControl<Integer, TestTopData2>(TfzzhRandom.getInstance().nextBoolean());
	// TestTopData2 ttd;
	// for (int i = 1; i < 20000; i++) {
	// ttd = new TestTopData2(i);
	// tc.putNode(ttd);
	// }
	// // tc.print();
	// long l2 = System.currentTimeMillis();
	// System.out.println("init use[" + (l2 - l1) + "]");
	// int n = 50000;
	// TestTopData2 tn;
	// for (int i = 0; i < n; i++) {
	// tn = tc.getNode(TfzzhRandom.getInstance().nextInt(tc.size()) + 1);
	// tn.refresh();
	// }
	// long l3 = System.currentTimeMillis();
	// System.out.println("run get[" + n + "] use[" + (l3 - l2) + "]");
	// tc.print();
	// }
	//
	// private static void test1() {
	// TopControl<String, TestTopData> tc = new TopControl<String, TestTopData>(true);
	// TestTopData ttd1 = new TestTopData("a");
	// // System.out.println(ttd1.toString());
	// TestTopData ttd2 = new TestTopData("b");
	// // System.out.println(ttd2.toString());
	// TestTopData ttd3 = new TestTopData("c");
	// // System.out.println(ttd3.toString());
	// TestTopData ttd4 = new TestTopData("d");
	// // System.out.println(ttd4.toString());
	// TestTopData ttd5 = new TestTopData("e");
	// // System.out.println(ttd5.toString());
	// TestTopData ttd6 = new TestTopData("f");
	// // System.out.println(ttd6.toString());
	// TestTopData ttd7 = new TestTopData("g");
	// // System.out.println(ttd7.toString());
	// TestTopData ttd8 = new TestTopData("h");
	// // System.out.println(ttd8.toString());
	// TestTopData ttd9 = new TestTopData("i");
	// // System.out.println(ttd9.toString());
	// TestTopData ttd10 = new TestTopData("j");
	// // System.out.println(ttd10.toString());
	// TestTopData ttd11 = new TestTopData("k");
	// // System.out.println(ttd11.toString());
	// // System.out.println("=======");
	// tc.putNode(ttd1);
	// tc.print();
	// System.out.println("=======" + ttd1.toString() + "=======");
	// tc.putNode(ttd2);
	// tc.print();
	// System.out.println("=======" + ttd2.toString() + "=======");
	// tc.putNode(ttd3);
	// tc.print();
	// System.out.println("=======" + ttd3.toString() + "=======");
	// tc.putNode(ttd4);
	// tc.print();
	// System.out.println("=======" + ttd4.toString() + "=======");
	// tc.putNode(ttd5);
	// tc.print();
	// System.out.println("=======" + ttd5.toString() + "=======");
	// tc.putNode(ttd6);
	// tc.print();
	// System.out.println("=======" + ttd6.toString() + "=======");
	// tc.putNode(ttd7);
	// tc.print();
	// System.out.println("=======" + ttd7.toString() + "=======");
	// tc.putNode(ttd8);
	// tc.print();
	// System.out.println("=======" + ttd8.toString() + "=======");
	// tc.putNode(ttd9);
	// tc.print();
	// System.out.println("=======" + ttd9.toString() + "=======");
	// tc.putNode(ttd10);
	// tc.print();
	// System.out.println("=======" + ttd10.toString() + "=======");
	// tc.putNode(ttd11);
	// tc.print();
	// System.out.println("=======" + ttd11.toString() + "=======");
	// // tc.print();
	// // ttd3.refresh();
	// // tc.print();
	// // System.out.println(ttd3.toString());
	// // ttd4.refresh();
	// // tc.print();
	// // System.out.println(ttd4.toString());
	// // ttd5.refresh();
	// // tc.print();
	// // System.out.println(ttd5.toString());
	// // ttd6.refresh();
	// // tc.print();
	// // System.out.println(ttd6.toString());
	// System.out.println("====----------------------------===");
	// for (int i = 0; i < 20; i++) {
	// ttd6.refresh();
	// tc.print();
	// System.out.println(ttd6.toString());
	// }
	// // tc.remove("e");
	// // tc.print();
	// // tc.remove(4);
	// // tc.print();
	// // System.out.println("=======");
	// // int ind = 4;
	// // System.out.println(ind + " >> " + tc.getNode(ind) + " : " + tc.ind + " >> " + tc.currNode);
	// // System.out.println("=======");
	// // ind = 2;
	// // System.out.println(ind + " >> " + tc.getNode(ind) + " : " + tc.ind + " >> " + tc.currNode);
	// // System.out.println("=======");
	// // ind = 6;
	// // System.out.println(ind + " >> " + tc.getNode(ind) + " : " + tc.ind + " >> " + tc.currNode);
	// // System.out.println("=======");
	// // ind = 1;
	// // System.out.println(ind + " >> " + tc.getNode(ind) + " : " + tc.ind + " >> " + tc.currNode);
	// // System.out.println("=======");
	// // ind = 3;
	// // System.out.println(ind + " >> " + tc.getNode(ind) + " : " + tc.ind + " >> " + tc.currNode);
	// // System.out.println("=======");
	// // ind = 5;
	// // System.out.println(ind + " >> " + tc.getNode(ind) + " : " + tc.ind + " >> " + tc.currNode);
	// // System.out.println("=======");
	// // ind = 6;
	// // System.out.println(ind + " >> " + tc.getNode(ind) + " : " + tc.ind + " >> " + tc.currNode);
	// // System.out.println("=======");
	// // ind = 7;
	// // System.out.println(ind + " >> " + tc.getNode(ind) + " : " + tc.ind + " >> " + tc.currNode);
	// // System.out.println("=======");
	// }
}
