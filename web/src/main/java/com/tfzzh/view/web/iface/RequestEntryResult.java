/**
 * @author tfzzh
 * @dateTime 2023年11月12日 15:24:54
 */
package com.tfzzh.view.web.iface;

import com.alibaba.fastjson2.JSONObject;

/**
 * 请求入口返回内容
 * 
 * @author tfzzh
 * @dateTime 2023年11月12日 15:24:54
 * @param status 1，进行直接返回控制；
 * @param cont 直接返回字串
 * @param objCont 返回json内容
 */
public record RequestEntryResult(Integer status, String cont, JSONObject objCont) {

	/**
	 * 你懂得
	 * 
	 * @author tfzzh
	 * @dateTime 2023年11月12日 16:14:21
	 * @see java.lang.Record#toString()
	 */
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
