/**
 * @author XuWeijie
 * @datetime 2015年7月13日_下午3:01:47
 */
package com.tfzzh.model.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 关联数据
 * 
 * @author XuWeijie
 * @datetime 2015年7月13日_下午3:01:47
 */
public abstract class CorrelationDataBean extends BaseDataBean {

	/**
	 * @author tfzzh
	 * @dateTime 2016年12月5日 下午2:42:45
	 */
	private static final long serialVersionUID = -8001938786557356924L;

	/**
	 * 实体对象列表
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月13日_下午4:22:09
	 */
	private final Map<String, BaseDataBean> entitys = new HashMap<>();

	/**
	 * 放入一个实体对象
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月13日_下午4:31:58
	 * @param name 实体名
	 * @param beb 实体对象
	 */
	public void setEntity(final String name, final BaseDataBean beb) {
		this.entitys.put(name, beb);
	}

	/**
	 * 得到目标实体对象
	 * 
	 * @author XuWeijie
	 * @datetime 2015年7月13日_下午4:32:11
	 * @param name 实体名
	 * @return 实体对象
	 */
	public BaseDataBean getTargetEntity(final String name) {
		return this.entitys.get(name);
	}
}
