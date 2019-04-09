/**
 * @author Xu Weijie
 * @dateTime 2012-11-17 下午12:48:31
 */
package com.tfzzh.tools;

import java.util.HashMap;
import java.util.Map;

import com.tfzzh.core.control.iface.RefreshData;

/**
 * 基础数据池
 * 
 * @author Xu Weijie
 * @dateTime 2012-11-17 下午12:48:31
 * @param <T> 池数据对象
 */
public abstract class BasePool<T extends RefreshData> {

	/**
	 * 池中数据信息列表<br />
	 * <对应关系内容,T><br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-3-9 上午11:58:15
	 */
	private final Map<String, BasePoolData> datas = new HashMap<>();

	/**
	 * 放入新数据
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-11-17 下午5:12:26
	 * @param key 键值
	 * @param data 数据对象
	 */
	protected void putData(final String key, final BasePoolData data) {
		this.datas.put(key, data);
	}

	/**
	 * 根据目标key得到数据对象
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-11-17 下午5:20:14
	 * @param key 目标key
	 * @return 数据对象；<br />
	 *         null，无目标key所对应的数据对象；<br />
	 */
	protected BasePoolData getData(final String key) {
		return this.datas.get(key);
	}

	/**
	 * 移除一个数据
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-11-17 下午5:16:16
	 * @param key 移除一个数据
	 * @return 被移除的数据信息；<br />
	 *         null，不存在目标key的数据对象；<br />
	 */
	protected BasePoolData removeData(final String key) {
		return this.datas.remove(key);
	}

	/**
	 * 得到数据集合
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-11-17 下午6:38:54
	 * @return 数据集合
	 */
	protected Map<String, BasePoolData> getDatas() {
		return this.datas;
	}

	/**
	 * 基础池中数据
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-11-17 下午1:08:47
	 */
	public abstract class BasePoolData {

		/**
		 * 数据对象
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-11-17 下午1:10:33
		 */
		private final T data;

		/**
		 * 最后使用时间
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-11-17 下午1:10:30
		 */
		private long lastUseTime;

		/**
		 * @author Xu Weijie
		 * @dateTime 2012-11-17 下午1:11:39
		 * @param key 键名
		 * @param data 数据对象
		 */
		protected BasePoolData(final String key, final T data) {
			this.data = data;
			BasePool.this.datas.put(key, this);
		}

		/**
		 * 得到当前数据对象
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-11-17 下午1:14:18
		 * @return 数据对象
		 */
		public T getData() {
			// 更新被使用时间
			this.use();
			return this.data;
		}

		/**
		 * 使用对象方法
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-11-17 下午1:12:11
		 */
		private void use() {
			this.lastUseTime = System.currentTimeMillis();
		}

		/**
		 * 判定池对象是否过期
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-11-17 下午1:12:51
		 * @param intervalTime 过期间隔时间
		 * @return true，已经过期；<br />
		 *         false，还未过期；<br />
		 */
		public boolean isPastDue(final long intervalTime) {
			return (System.currentTimeMillis() - this.lastUseTime) > intervalTime;
		}
	}
}
