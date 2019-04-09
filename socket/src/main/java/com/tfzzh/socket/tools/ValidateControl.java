/**
 * @author Weijie Xu
 * @datetime 2015年12月26日_下午12:04:26
 */
package com.tfzzh.socket.tools;

import com.tfzzh.socket.RequestSession;
import com.tfzzh.socket.bean.RequestInfoBean;

/**
 * 验证控制
 * 
 * @author Weijie Xu
 * @datetime 2015年12月26日_下午12:04:26
 */
public interface ValidateControl {

	/**
	 * 验证在解析参数前
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月26日_下午12:07:02
	 * @param rs 请求会话
	 * @param requestInfo 被请求的消息信息
	 * @return 结果类型
	 */
	VerificationTypeEnum validateBeforeParams(RequestSession rs, RequestInfoBean requestInfo);
}
