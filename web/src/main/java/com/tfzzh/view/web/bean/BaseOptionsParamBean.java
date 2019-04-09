/**
 * @author Weijie Xu
 * @dateTime 2012-7-23 上午12:00:00
 */
package com.tfzzh.view.web.bean;

import com.tfzzh.tools.BaseBean;

/**
 * 基础选项参数
 * 
 * @author Weijie Xu
 * @dateTime 2012-7-23 上午12:00:00
 */
public abstract class BaseOptionsParamBean extends BaseBean {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-1-21 下午3:21:31
	 */
	private static final long serialVersionUID = 2097859545475050179L;

	/**
	 * 生成数据<br />
	 * 得到选项用json格式数据<br />
	 * {o1:{v:x1,n:y1,t:z1,a:{o11:{v:x11,n:y11,t:z11},o12:{v:x12,n:y12,t:z12},...}},o2:{v:x2,n:y2,t:z2},...}<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-23 下午7:22:03
	 * @return 得到json格式数据
	 */
	public abstract String getOptionsJson();
}
