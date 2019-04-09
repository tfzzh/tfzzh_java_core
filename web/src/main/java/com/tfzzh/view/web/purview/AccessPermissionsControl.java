/**
 * @author xuweijie
 * @dateTime 2012-2-15 下午1:15:11
 */
package com.tfzzh.view.web.purview;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfzzh.log.CoreLog;
import com.tfzzh.view.web.annotation.SingletonHideField;
import com.tfzzh.view.web.iface.LoginAccount;
import com.tfzzh.view.web.servlet.session.ClientSessionBean;

/**
 * url权限控制
 * 
 * @author xuweijie
 * @dateTime 2012-2-15 下午1:15:11
 */
public class AccessPermissionsControl {

	/**
	 * URL列表
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-15 下午1:17:28
	 */
	private final Map<String, AccessPermissionsInfo> links = new HashMap<>();

	/**
	 * 是否已经初始化数据
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-15 下午9:37:22
	 */
	private boolean isInit = false;

	/**
	 * 该对象唯一实例
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-9 下午4:54:02
	 */
	private final static AccessPermissionsControl control = new AccessPermissionsControl();

	/**
	 * 验证信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-10 下午9:05:28
	 */
	@SingletonHideField("accessPermissionOperator")
	private final AccessPermissionsOperator operator = null;

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-7-9 下午4:54:03
	 */
	private AccessPermissionsControl() {
		this.init();
	}

	/**
	 * 得到该对象实体
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-9 下午4:56:42
	 * @return 对象实体
	 */
	public static AccessPermissionsControl getInstance() {
		return AccessPermissionsControl.control;
	}

	/**
	 * 初始化方法
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-9 下午4:53:58
	 */
	private void init() {
	}

	/**
	 * 放入一个控制连接
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-9 下午4:57:52
	 * @param link 控制连接
	 */
	public void putAccessPermissions(final AccessPermissionsInfo link) {
		this.links.put(link.getTarget(), link);
	}

	/**
	 * 初始化数据 主要是与
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-15 下午9:36:47
	 */
	public void initData() {
		if ((null != this.operator) && !this.isInit) {
			// 得到数据库中权限列表
			final List<AccessPermissionsInfo> list = this.operator.getAccessPurviews();
			final Map<String, AccessPermissionsInfo> map = new HashMap<>(this.links);
			{
				AccessPermissionsInfo link;
				// 移除列表中存在的内容
				for (final AccessPermissionsInfo info : list) {
					if (null != (link = map.remove(info.getTarget()))) {
						// 存在目标信息
						link.setAccessPermissions(info);
					}
				}
			}
			{
				AccessPermissionsInfo info;
				Iterator<AccessPermissionsInfo> it;
				AccessPermissionsInfo in;
				int c = map.size();
				while (map.size() > 0) {
					it = map.values().iterator();
					while (it.hasNext()) {
						in = it.next();
						// 是主节点
						if (null != (info = this.operator.addAccessPurviewData(in))) {
							// 成功的放入
							in.setAccessPermissions(info);
							it.remove();
						}
					}
					if (c == map.size()) {
						// 有问题的情况
						if (CoreLog.getInstance().errorEnabled(this.getClass())) {
							CoreLog.getInstance().error(this.getClass(), "!!Error in init OperateLink with count： ", Integer.toString(map.size()));
						}
					} else {
						c = map.size();
					}
				}
			}
			this.isInit = true;
		}
	}

	/**
	 * 得到连接信息列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2012-7-9 下午10:22:38
	 * @return 连接信息列表
	 */
	public Map<String, AccessPermissionsInfo> getLinks() {
		return this.links;
	}

	/**
	 * 验证访问权限<br />
	 * 一个中介方法<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-14 下午5:06:08
	 * @param accessPermission 访问权限信息
	 * @param request 请求的信息
	 * @param response 返回数据
	 * @param la 登陆的用户信息，可能为null
	 * @return ture，验证成功；<br />
	 *         false，验证失败；<br />
	 */
	@Deprecated
	public boolean validateAccessPermission(final AccessPermissionsInfo accessPermission, final HttpServletRequest request, final HttpServletResponse response, final LoginAccount la) {
		if (null != this.operator) {
			return this.operator.volidate(accessPermission, request, response, la);
		} else {
			// 如果不存在操作实现，则一定是正确
			return true;
		}
	}

	/**
	 * 验证访问权限<br />
	 * 一个中介方法<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月12日 下午4:04:28
	 * @param accessPermission 访问权限信息
	 * @param request 请求的信息
	 * @param response 返回数据
	 * @param cs 客户端会话
	 * @return ture，验证成功；<br />
	 *         false，验证失败；<br />
	 */
	public boolean validateAccessPermission(final AccessPermissionsInfo accessPermission, final HttpServletRequest request, final HttpServletResponse response, final ClientSessionBean cs) {
		if (null != this.operator) {
			return this.operator.volidate(accessPermission, request, response, cs);
		} else {
			// 如果不存在操作实现，则一定是正确
			return true;
		}
	}
}
