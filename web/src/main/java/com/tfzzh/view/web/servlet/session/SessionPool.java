/**
 * @author Weijie Xu
 * @dateTime 2017年3月21日 上午11:39:13
 */
package com.tfzzh.view.web.servlet.session;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfzzh.log.CoreLog;
import com.tfzzh.timer.TimerTaskControl;
import com.tfzzh.tools.ProjectCodeTools;
import com.tfzzh.view.web.tools.ClientTypeEnum;
import com.tfzzh.view.web.tools.WebBaseConstants;

/**
 * 会话池<br />
 * 与数据库无关<br />
 * 生成对应token，暂定64位<br />
 * 对于多端的处理，当前还未有处理<br />
 * 
 * @author Weijie Xu
 * @dateTime 2017年3月21日 上午11:39:13
 */
public class SessionPool {

	/**
	 * 客户端Token会话池
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月21日 上午11:50:55
	 */
	private final Map<String, ClientSessionBean> tsm = new ConcurrentHashMap<>();

	/**
	 * 客户端ID会话池
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午4:05:41
	 */
	private final Map<String, Map<ClientTypeEnum, ClientSessionBean>> csm = new ConcurrentHashMap<>();

	/**
	 * 对象唯一实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月21日 下午12:57:18
	 */
	private static final SessionPool sp = new SessionPool();

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年4月7日 上午10:19:46
	 */
	private final TimerTaskControl ttc = new TimerTaskControl(WebBaseConstants.SESSION_POOL_TASK_TIME);

	/**
	 * 用户锁
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月21日 下午1:44:09
	 */
	private final Lock uLock = new ReentrantLock();

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年3月21日 下午12:57:16
	 */
	private SessionPool() {
		this.init();
	}

	/**
	 * 一些初始化操作<br />
	 * 将自身放入到任务队列中<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月7日 上午10:14:03
	 */
	private void init() {
		new Thread(this.ttc).start();
		this.ttc.putTask(new SessionTaskBean());
	}

	/**
	 * 得到对象实例
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月21日 下午12:57:13
	 * @return 得到对象唯一实例
	 */
	public static SessionPool getInstance() {
		return SessionPool.sp;
	}

	/**
	 * 得到Token码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月27日 下午7:14:14
	 * @return Token码
	 */
	public static String createToken() {
		return ProjectCodeTools.getLongCode(WebBaseConstants.SESSION_TOKEN_LENGTH, WebBaseConstants.SESSION_TOKEN_TIME_INDEX);
	}

	/**
	 * 创建一个全新客户端会话<br />
	 * 生成全新Token<br />
	 * null，表示过程中，有出现不知道什么问题<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月21日 下午1:48:11
	 * @param request 请求消息
	 * @param response 返回用消息
	 * @param type 客户端类型
	 * @return 被创建的客户端会话
	 */
	public ClientSessionBean createSession(final HttpServletRequest request, final HttpServletResponse response, final ClientTypeEnum type) {
		// System.out.println("\t\t\t >>> create >>> new token >> ");
		return this.createSession(request, response, SessionPool.createToken(), type);
	}

	/**
	 * 创建一个全新客户端会话<br />
	 * 使用指定Token<br />
	 * null，表示过程中，有出现不知道什么问题<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月11日 下午6:42:41
	 * @param request 请求消息
	 * @param response 返回用消息
	 * @param token 被指定的token
	 * @param type 客户端类型
	 * @return 被创建的客户端会话
	 */
	public ClientSessionBean createSession(final HttpServletRequest request, final HttpServletResponse response, final String token, final ClientTypeEnum type) {
		try {
			this.uLock.lock();
			final ClientSessionBean us = new ClientSessionBean(request, response, token, type);
			// System.out.println("\t\t\t >>> new token >> " + us.getToken());
			this.tsm.put(us.getToken(), us);
			return us;
		} catch (final Exception e) {
			CoreLog.getInstance().warn(SessionPool.class, e.getMessage());
			return null;
		} finally {
			this.uLock.unlock();
		}
	}

	/**
	 * 根据token得到会话<br />
	 * null，表示没有目标token的会话记录<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月21日 下午5:32:08
	 * @param token 目标token
	 * @return 目标会话信息
	 */
	public ClientSessionBean getSessionByToken(final String token) {
		final ClientSessionBean cs = this.tsm.get(token);
		// System.out.println("\t\t\t >>> get token >> " + token + ">>" + (cs != null));
		if (null != cs) {
			cs.refreshUseTime();
		}
		return cs;
	}

	/**
	 * 根据客户端ID得到会话<br />
	 * null，表示没有目标客户端ID的会话记录<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月28日 下午4:59:23
	 * @param clientId 客户端ID
	 * @param type 客户端类型
	 * @return 目标会话信息
	 */
	public ClientSessionBean getSessionByClient(final String clientId, final ClientTypeEnum type) {
		final Map<ClientTypeEnum, ClientSessionBean> tm = this.csm.get(clientId);
		if (null == tm) {
			return null;
		}
		final ClientSessionBean cs = tm.get(type);
		if (null != cs) {
			cs.refreshUseTime();
		}
		return cs;
	}

	/**
	 * 放入一个ClientId相关会话
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月11日 下午5:12:44
	 * @param cs 所相关客户端会话
	 */
	protected void putClientSession(final ClientSessionBean cs) {
		try {
			this.uLock.lock();
			Map<ClientTypeEnum, ClientSessionBean> ocm = this.csm.get(cs.getClientId());
			if (null == ocm) {
				ocm = new HashMap<>(ClientTypeEnum.values().length);
				this.csm.put(cs.getClientId(), ocm);
				ocm.put(cs.getType(), cs);
			} else {
				final ClientSessionBean ocs = ocm.remove(cs.getType());
				if (null != ocs) {
					// 将之前的用户进行登出
					ocs.innerLogout();
				}
				ocm.put(cs.getType(), cs);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			this.uLock.unlock();
		}
	}

	/**
	 * 移除指定ClientId相关会话
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月11日 下午3:16:41
	 * @param cs 所相关客户端会话
	 */
	protected void removeClientSession(final ClientSessionBean cs) {
		try {
			this.uLock.lock();
			final Map<ClientTypeEnum, ClientSessionBean> ocm = this.csm.get(cs.getClientId());
			if (null == ocm) {
				return;
			}
			ocm.remove(cs.getType());
			if (ocm.size() == 0) {
				this.csm.remove(cs.getClientId());
			}
		} finally {
			this.uLock.unlock();
		}
	}

	/**
	 * 过期会话
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月28日 下午5:03:26
	 * @param cs 所相关客户端会话
	 */
	protected void failSession(final ClientSessionBean cs) {
		try {
			this.uLock.lock();
			this.tsm.remove(cs.getToken());
			this.removeClientSession(cs);
		} catch (final Exception e) {
			CoreLog.getInstance().warn(e.getMessage());
			// 此时直接结束
			return;
		} finally {
			this.uLock.unlock();
		}
	}

	/**
	 * 验证session会话过期情况
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月28日 下午5:02:46
	 */
	public void validate() {
		final Map<String, ClientSessionBean> tm;
		try {
			this.uLock.lock();
			tm = new HashMap<>(this.tsm);
		} catch (final Exception e) {
			CoreLog.getInstance().warn(e.getMessage());
			// 此时直接结束
			return;
		} finally {
			this.uLock.unlock();
		}
		final long now = System.currentTimeMillis();
		for (final ClientSessionBean cs : tm.values()) {
			if (cs.validateOverdue(now)) {
				// 失效的会话，进行移除
				this.failSession(cs);
			}
		}
	}
}
