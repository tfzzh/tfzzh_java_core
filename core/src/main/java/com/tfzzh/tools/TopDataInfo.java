/**
 * @author 许纬杰
 * @datetime 2016年6月2日_上午11:33:05
 */
package com.tfzzh.tools;

/**
 * 排行的基础信息<br />
 * 数据刷新方式为refresh()方法；该方法请尽量在子类中触发调用<br />
 * 
 * @author 许纬杰
 * @datetime 2016年6月2日_上午11:33:05
 * @param <K> key类型
 */
public abstract class TopDataInfo<K> {

	/**
	 * 排行控制
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_上午11:52:13
	 */
	private TopControl<K, ? extends TopDataInfo<K>> tc;

	/**
	 * 对应的key
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_上午11:44:42
	 */
	private final K key;

	/**
	 * 参考值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午2:01:48
	 */
	private long ref;

	/**
	 * 排行信息
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月3日_下午5:45:37
	 */
	private InnerIndex top = null;

	/**
	 * @author 许纬杰
	 * @datetime 2016年6月2日_上午11:45:15
	 * @param key 数据的Key
	 */
	protected TopDataInfo(final K key) {
		this.key = key;
	}

	/**
	 * 得到对应的key
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_上午11:54:06
	 * @return the key
	 */
	public K getKey() {
		return this.key;
	}

	/**
	 * 设置排行控制
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_上午11:52:37
	 * @param tc 排行控制
	 */
	protected void setTopControl(final TopControl<K, ? extends TopDataInfo<K>> tc) {
		this.tc = tc;
	}

	/**
	 * 设置排行信息
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月3日_下午5:45:56
	 * @param top the top to set
	 */
	protected void setTop(final InnerIndex top) {
		this.top = top;
	}

	/**
	 * 得到排行信息
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月3日_下午5:45:56
	 * @return 排行的名次；<br />
	 *         -1，未在排行中；<br />
	 */
	public int getTop() {
		if (null == this.top) {
			return -1;
		}
		return this.top.get();
	}

	/**
	 * 初始化参考值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午3:11:52
	 * @param asc 是否正序排列
	 */
	protected void initReferenceValue(final boolean asc) {
		if (asc) {
			// 正序，小在上，初始化为最大值
			this.ref = Long.MAX_VALUE;
		} else {
			// 倒序，大在上，初始化为最小值
			this.ref = 0;
		}
	}

	/**
	 * 得到参考值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午4:01:06
	 * @return 参考值
	 */
	public long getReferenceValue() {
		return this.ref;
	}

	/**
	 * 得到排行用值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_上午11:58:16
	 * @return 排行用的值
	 */
	protected abstract long refreshReferenceScore();

	/**
	 * 进行当前排行的刷新操作<br />
	 * 该方法进行在子类对象中进行触发调用<br />
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_上午11:37:03
	 */
	public void refresh() {
		final long newRef = this.refreshReferenceScore();
		if (newRef == 0) {
			return;
		}
		if (newRef == this.ref) {
			return;
		}
		try {
			this.tc.kLock.lock();
			final boolean isBigger = newRef > this.ref;
			this.ref = newRef;
			this.tc.changeTop(this, isBigger);
		} finally {
			this.tc.kLock.unlock();
		}
	}

	/**
	 * 你懂的
	 * 
	 * @author 许纬杰
	 * @datetime 2016年6月2日_下午8:44:55
	 * @return 你懂的
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{key:").append(this.key);
		sb.append(",ref:").append(this.ref);
		sb.append("}");
		return sb.toString();
	}
}
