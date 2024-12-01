/**
 * @author Xu Weijie
 * @dateTime 2012-7-11 上午11:19:49
 */
package com.tfzzh.view.web.purview;

/**
 * 访问权限信息
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-11 上午11:19:49
 */
public interface AccessPermissionsInfo {

	/**
	 * 得到目标
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-15 下午8:53:20
	 * @return 目标值
	 */
	String getTarget();

	/**
	 * 是否节点
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-18 下午8:12:37
	 * @return true，是节点；<br />
	 *         false，不是节点；<br />
	 */
	boolean isNode();

	/**
	 * 得到目标节点连接
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-17 下午9:22:00
	 * @return 目标节点连接
	 */
	AccessPermissionsNodeInfo getTargetNode();
	// /**
	// * 得到访问权限值
	// *
	// * @author Xu Weijie
	// * @dateTime 2012-7-11 上午11:30:03
	// * @return 访问权限值
	// */
	// int getAccessPermissionsValue();

	/**
	 * 得到说明
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-16 下午1:18:02
	 * @return 说明内容
	 */
	String getDescription();

	/**
	 * 设置访问权限对象
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-17 下午9:08:37
	 * @param accessPermissions 访问权限对象
	 */
	void setAccessPermissions(AccessPermissionsInfo accessPermissions);

	/**
	 * 访问权限节点信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-18 下午10:03:43
	 */
	public interface AccessPermissionsNodeInfo extends AccessPermissionsInfo {
	}
}
