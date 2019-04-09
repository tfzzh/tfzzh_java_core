/**
 * @author Weijie Xu
 * @dateTime 2017年3月22日 下午5:49:09
 */
package com.tfzzh.tools;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 核心常量池<br />
 * 当前，一个常量对象，仅支持一个实例的记录<br />
 * 
 * @author Weijie Xu
 * @dateTime 2017年3月22日 下午5:49:09
 */
public class CoreConstantsPool {

	/**
	 * 常量对象池
	 * 暂时只能
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午6:21:55
	 */
	private final Map<Class<?>, BaseConstants> cp = new ConcurrentHashMap<>();

	/**
	 * 对象唯一实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午6:23:01
	 */
	private final static CoreConstantsPool ccp = new CoreConstantsPool();

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午6:23:19
	 */
	private CoreConstantsPool() {
		this.init();
	}

	/**
	 * 默认初始化操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午7:06:58
	 */
	private void init() {
		// 暂无实际逻辑
	}

	/**
	 * 得到对象实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午6:23:39
	 * @return 对象唯一实例
	 */
	public static CoreConstantsPool getInstance() {
		return CoreConstantsPool.ccp;
	}

	/**
	 * 重置常量池数据
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午6:33:58
	 */
	public void reloadConstants() {
		synchronized (this.cp) {
			for (final BaseConstants bc : this.cp.values()) {
				bc.refresh();
			}
		}
	}

	/**
	 * 放入常量实例到常量池<br />
	 * 暂时不允许同对象多实例<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午6:31:22
	 * @param bc 常量对象
	 */
	public void putConstants(final BaseConstants bc) {
		this.cp.put(bc.getClass(), bc);
	}

	/**
	 * 得到目标常量
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午6:03:46
	 * @param <C> 常量子类
	 * @param clz 常量类对象
	 * @return 目标常量实例
	 */
	@SuppressWarnings("unchecked")
	public <C extends BaseConstants> C getConstants(final Class<C> clz) {
		C c = (C) this.cp.get(clz);
		if (null == c) {
			synchronized (this.cp) {
				if (null == (c = (C) this.cp.get(clz))) {
					try {
						c = clz.newInstance();
						this.cp.put(clz, c);
					} catch (final InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return c;
	}

	/**
	 * 移除一个常量实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午6:57:07
	 * @param <C> 常量子类
	 * @param clz 常量类对象
	 * @return 被移除的常量实例
	 */
	@SuppressWarnings("unchecked")
	public <C extends BaseConstants> C removeConstants(final Class<C> clz) {
		return (C) this.cp.remove(clz);
	}
}
