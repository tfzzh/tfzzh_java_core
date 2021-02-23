/**
 * @author tfzzh
 * @dateTime 2021年1月19日 上午11:33:18
 */
package com.tfzzh.tools;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 配置数据相关基础对象
 * 
 * @author tfzzh
 * @dateTime 2020年11月17日 下午4:54:25
 */
public abstract class ObjectInfo implements Serializable {

	/**
	 * @author tfzzh
	 * @dateTime 2021年1月19日 下午12:34:31
	 */
	private static final long serialVersionUID = 7916854170568283525L;

	/**
	 * @author tfzzh
	 * @dateTime 2020年11月17日 下午4:54:26
	 * @param jo 数据json
	 */
	protected ObjectInfo(final JSONObject jo) {
	}

	/**
	 * @author tfzzh
	 * @dateTime 2020年11月17日 下午7:17:20
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
