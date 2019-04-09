/**
 * @author Xu Weijie
 * @datetime 2017年9月27日_下午5:40:27
 */
package com.tfzzh.view.web.iface;

import com.tfzzh.view.web.bean.BaseParamBean;
import com.tfzzh.view.web.link.BaseBackOperationBean;
import com.tfzzh.view.web.link.OperateLink.OperateLinkInfo;

/**
 * 请求数据验证控制
 * 
 * @author Xu Weijie
 * @datetime 2017年9月27日_下午5:40:27
 */
public interface ParamValidateControl {

	/**
	 * 请求消息对象验证
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月28日_上午10:13:15
	 * @param link 连接信息
	 * @param param 消息对象信息
	 * @return null，表示继续正常工作；<br />
	 *         非null，表示有问题，需要直接去到给出的位置；<br />
	 */
	BaseBackOperationBean paramValidate(OperateLinkInfo link, BaseParamBean param);
}
