/**
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午12:53:21
 */
package com.tfzzh.model.dao.tools;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.tfzzh.model.bean.BaseDataBean;
import com.tfzzh.model.dao.AutomaticDataTableStructureDAO;

/**
 * 数据实体工具<br />
 * 预期对象，暂时还不明实际作用<br />
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午12:53:21
 */
public class EntityTool {

	/**
	 * 数据实体列表，实体类对应
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午1:54:17
	 */
	private final Map<Class<? extends BaseDataBean>, EntityInfoBean<? extends BaseDataBean>> entMap = new HashMap<>();

	/**
	 * 数据实体列表，ID对应
	 * 
	 * @author tfzzh
	 * @dateTime 2016年9月1日 下午2:00:45
	 */
	private final Map<Long, EntityInfoBean<? extends BaseDataBean>> entIdMap = new HashMap<>();

	/**
	 * 锁，针对放入部分
	 *
	 * @author Weijie Xu
	 * @dateTime 2015年4月29日 下午3:15:11
	 */
	private final Lock lock = new ReentrantLock();

	/**
	 * 是否已经同步表结构
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月18日_下午12:37:07
	 */
	private boolean synStructure = false;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午1:55:18
	 */
	private static final EntityTool et = new EntityTool();

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午1:56:10
	 */
	private EntityTool() {
		this.init();
	}

	/**
	 * 初始化方法，不一定会存在业务逻辑
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午1:56:10
	 */
	private void init() {
	}

	/**
	 * 得到对象实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午1:55:46
	 * @return 对象唯一实例
	 */
	public static EntityTool getInstance() {
		return EntityTool.et;
	}

	/**
	 * 初始化一个实体对象信息<br />
	 * 应该仅出现在DAO的接口对象中，作为接口的属性存在<br />
	 * 理论上，该操作应该是在项目初始化时候，因为需要对所有DAO进行相关初始化，而必然会被触发一次，所以一定会被执行，也不需要有同步控制<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 上午10:24:19
	 * @param <E> 数据实体对象
	 * @param clz 实体对象信息
	 * @param adts 自动处理表结构所相关的DAO
	 * @return 刚被放入的实体对象信息
	 */
	public <E extends BaseDataBean> EntityInfoBean<E> initEntityInfo(final Class<E> clz, final AutomaticDataTableStructureDAO<E> adts) {
		this.lock.lock();
		try {
			EntityInfoBean<E> eib = new EntityInfoBean<>(clz, adts);
			// 放入新的
			@SuppressWarnings("unchecked")
			final EntityInfoBean<E> oldEib = (EntityInfoBean<E>) this.entMap.put(clz, eib);
			if (null != oldEib) {
				if (null != oldEib.getAutomaticStructureDao()) {
					// 概率上因并发出现的重复put，处理还原最开始的数据，因为在这个过程中，其他线程是进不来的
					eib = oldEib;
				}
				this.entMap.put(clz, eib);
			}
			this.entIdMap.put(eib.getClassId(), eib);
			return eib;
		} finally {
			this.lock.unlock();
		}
	}

	/**
	 * 得到目标实体对象信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 上午10:41:23
	 * @param <E> 数据实体对象
	 * @param clz 实体对象信息
	 * @return 实体对象信息
	 */
	@SuppressWarnings("unchecked")
	public <E extends BaseDataBean> EntityInfoBean<E> getEntityInfo(final Class<E> clz) {
		final EntityInfoBean<E> eib = (EntityInfoBean<E>) this.entMap.get(clz);
		if (null == eib) {
			return this.initEntityInfo(clz, null);
		}
		return eib;
	}

	/**
	 * 得到目标实体对象信息
	 * 
	 * @author tfzzh
	 * @dateTime 2016年9月1日 下午2:12:24
	 * @param <E> 数据实体对象
	 * @param id 实体对象ID
	 * @return 实体对象信息
	 */
	@SuppressWarnings("unchecked")
	public <E extends BaseDataBean> EntityInfoBean<E> getEntityInfo(final Long id) {
		return (EntityInfoBean<E>) this.entIdMap.get(id);
	}

	/**
	 * 得到所有实例的列表
	 * 
	 * @author tfzzh
	 * @dateTime 2020年12月3日 下午5:12:36
	 * @return 所有实例的列表
	 */
	public Collection<EntityInfoBean<? extends BaseDataBean>> getAllEntitys() {
		return this.entIdMap.values();
	}

	/**
	 * 进行同步表结构+数据操作，如果有调用，一定要在处理完dispose之后
	 *
	 * @author XuWeijie
	 * @datetime 2015年9月18日_下午12:44:08
	 */
	public void syncDataTableStructure() {
		if (this.synStructure) {
			return;
		}
		this.synStructure = true;
		AutomaticDataTableStructureDAO<?> adts;
		for (final EntityInfoBean<? extends BaseDataBean> eib : this.entMap.values()) {
			adts = eib.getAutomaticStructureDao();
			if (null != adts) {
				try {
					// 首先处理结构
					adts.createOrEditTable();
					// 然后同步基础数据
					adts.execInitData();
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	// /**
	// * 为了之前版本项目的冗余方法
	// *
	// * @author Xu Weijie
	// * @datetime 2017年8月2日_下午7:01:27
	// */
	// public void syncDataTableStructure() {
	// }

	/**
	 * 进行同步表数据操作
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月12日 上午12:57:59
	 */
	public void syncDataTableOnlyData() {
		if (this.synStructure) {
			return;
		}
		this.synStructure = true;
		AutomaticDataTableStructureDAO<?> adts;
		for (final EntityInfoBean<? extends BaseDataBean> eib : this.entMap.values()) {
			adts = eib.getAutomaticStructureDao();
			if (null != adts) {
				// 同步基础数据
				adts.execInitData();
			}
		}
	}
}
