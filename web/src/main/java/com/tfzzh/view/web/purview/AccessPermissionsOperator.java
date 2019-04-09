/**
 * @author Xu Weijie
 * @dateTime 2012-7-10 下午4:20:13
 */
package com.tfzzh.view.web.purview;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfzzh.view.web.iface.LoginAccount;
import com.tfzzh.view.web.servlet.session.ClientSessionBean;

/**
 * 访问权限验证
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-10 下午4:20:13
 */
public interface AccessPermissionsOperator {

	/**
	 * 得到访问权限列表
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-11 下午3:37:56
	 * @return 连接权限列表
	 */
	List<AccessPermissionsInfo> getAccessPurviews();

	/**
	 * 创建一个访问权限数据
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-16 下午12:26:55
	 * @param info 访问权限信息
	 * @return 成功放入的系统信息；<br />
	 *         null，未放入成功；<br />
	 */
	AccessPermissionsInfo addAccessPurviewData(AccessPermissionsInfo info);

	/**
	 * 验证操作
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-13 下午8:07:49
	 * @param accessPermission 访问权限信息
	 * @param request 请求的信息
	 * @param response 返回数据
	 * @param la 登陆的用户信息，可能为null
	 * @return ture，验证成功；<br />
	 *         false，验证失败；<br />
	 */
	@Deprecated
	boolean volidate(AccessPermissionsInfo accessPermission, HttpServletRequest request, HttpServletResponse response, LoginAccount la);

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年4月12日 上午11:48:49
	 * @param accessPermission 访问权限信息
	 * @param request 请求的信息
	 * @param response 返回数据
	 * @param cs 客户端会话信息
	 * @return ture，验证成功；<br />
	 *         false，验证失败；<br />
	 */
	boolean volidate(AccessPermissionsInfo accessPermission, HttpServletRequest request, HttpServletResponse response, ClientSessionBean cs);
}
