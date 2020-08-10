/**
 * @author XuWeijie
 * @dateTime 2010-3-25 下午05:43:36
 */
package com.tfzzh.core.control.tools;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制层实体对象控制类
 * 
 * @author XuWeijie
 * @dateTime 2010-3-25 下午05:43:36
 * @model
 */
public class ManagerMap {

	/**
	 * 所有已知的DAO类的实体(名称,实体)
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 下午08:00:48
	 */
	private final Map<String, Object> DAO_MAP = new HashMap<>();

	/**
	 * 所有已知的控制类实体(名称,实体)
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 下午08:00:44
	 */
	private final Map<String, Object> MANAGER_MAP = new HashMap<>();

	/**
	 * 实现对应名称操作接口的操作类实体名称(操作接口名称,操作实体名称)
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 下午08:00:38
	 */
	private final Map<String, String> INTERFACE_IMPL = new HashMap<>();

	/**
	 * @author XuWeijie
	 * @dateTime 2010-3-26 下午03:26:51
	 */
	private static ManagerMap managerMap = new ManagerMap();

	/**
	 * @author tfzzh
	 * @createDate 2009-1-12 上午12:34:02
	 */
	private ManagerMap() {
		this.init();
	}

	/**
	 * 得到唯一实体
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-12 下午03:22:56
	 * @return 该类唯一实体
	 */
	public static ManagerMap getInstance() {
		return ManagerMap.managerMap;
	}

	/**
	 * 初始化方法，暂无实际操作
	 * 
	 * @author XuWeijie
	 * @dateTime 2010-3-26 下午01:59:48
	 */
	public void init() {
	}

	/**
	 * 根据接口对象类型得到实现其的实体
	 * 
	 * @author tfzzh
	 * @dateTime 2020年8月10日 下午4:17:28
	 * @param <M> 目标对象
	 * @param clz 目标对象类型
	 * @return 接口实体
	 */
	@SuppressWarnings("unchecked")
	public <M> M getManager(final Class<M> clz) {
		String mn = clz.getSimpleName();
		mn = mn.substring(0, 1).toLowerCase() + mn.substring(1);
		return (M) this.MANAGER_MAP.get(this.INTERFACE_IMPL.get(mn));
	}

	/**
	 * 根据接口名称得到实现其的实体
	 * 
	 * @author tfzzh
	 * @createDate 2008-11-12 下午03:21:39
	 * @param managerName 接口定名
	 * @return 接口实体
	 */
	public Object getManager(final String managerName) {
		return this.MANAGER_MAP.get(this.INTERFACE_IMPL.get(managerName));
	}

	/**
	 * 放入DAO
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-21 下午3:25:16
	 * @param key 键
	 * @param dao 值
	 */
	public void putDao(final String key, final Object dao) {
		this.DAO_MAP.put(key, dao);
	}

	/**
	 * 得到DAO
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-21 下午3:26:11
	 * @param key 键
	 * @return 目标DAO
	 */
	public Object getDao(final String key) {
		return this.DAO_MAP.get(key);
	}

	/**
	 * 得到当前所有Dao的列表
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月18日_下午12:50:50
	 * @return 所有Dao的列表
	 */
	protected Collection<Object> getAllDao() {
		return this.DAO_MAP.values();
	}

	/**
	 * 放入接口
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-21 下午3:26:33
	 * @param key 键
	 * @param value 值
	 */
	public void putInterface(final String key, final String value) {
		this.INTERFACE_IMPL.put(key, value);
	}

	/**
	 * 放入实现
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-21 下午3:26:34
	 * @param key 键
	 * @param obj 值
	 */
	public void putManager(final String key, final Object obj) {
		this.MANAGER_MAP.put(key, obj);
	}

	/**
	 * 直接得到实现<br />
	 * 不通过接口，为工具对其直接的调用<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-21 下午3:36:45
	 * @param key 键
	 * @return 目标实现
	 */
	public Object getManagerDirect(final String key) {
		return this.MANAGER_MAP.get(key);
	}
}
