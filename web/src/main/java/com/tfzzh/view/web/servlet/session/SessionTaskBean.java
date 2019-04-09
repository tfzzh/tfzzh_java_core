/**
 * @author Weijie Xu
 * @dateTime 2017年4月7日 上午10:42:19
 */
package com.tfzzh.view.web.servlet.session;

import com.tfzzh.timer.BaseTimerTaskBean;
import com.tfzzh.view.web.tools.WebBaseConstants;

/**
 * 会话任务
 * 
 * @author Weijie Xu
 * @dateTime 2017年4月7日 上午10:42:19
 */
public class SessionTaskBean extends BaseTimerTaskBean {

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年4月7日 上午11:10:39
	 */
	public SessionTaskBean() {
		super(WebBaseConstants.SESSION_POOL_VALIDATE_TIME, Long.MAX_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年4月7日 上午10:42:19
	 * @see com.tfzzh.timer.BaseTimerTaskBean#resetNextRunTime()
	 */
	@Override
	protected void resetNextRunTime() {
		super.setNextRunTime(System.currentTimeMillis() + WebBaseConstants.SESSION_POOL_VALIDATE_TIME);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年4月7日 上午10:42:19
	 * @see com.tfzzh.timer.BaseTimerTaskBean#exec()
	 */
	@Override
	protected void exec() {
		SessionPool.getInstance().validate();
	}
}
