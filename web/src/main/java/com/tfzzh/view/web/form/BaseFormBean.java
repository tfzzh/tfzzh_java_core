/**
 * @author TFZZH
 * @dateTime 2011-3-7 下午04:01:24
 */
package com.tfzzh.view.web.form;

import java.util.Map;

import com.tfzzh.model.dao.tools.QLOrderBean;
import com.tfzzh.model.dao.tools.QLTermBean;
import com.tfzzh.tools.BaseBean;

/**
 * 提交数据基础FormBean
 * 
 * @author TFZZH
 * @dateTime 2011-3-7 下午04:01:24
 */
public abstract class BaseFormBean extends BaseBean {

	/**
	 * @author tfzzh
	 * @dateTime 2017年2月23日 下午3:35:04
	 */
	private static final long serialVersionUID = 4979581249520493255L;

	/**
	 * @author TFZZH
	 * @dateTime 2011-3-7 下午04:46:35
	 * @param paras 参数集合
	 */
	public BaseFormBean(final Map<String, String[]> paras) {
		this.setParameters(paras);
	}

	/**
	 * 设置参数
	 * 
	 * @author TFZZH
	 * @dateTime 2011-3-7 下午04:02:33
	 * @param paras 参数集合
	 */
	protected abstract void setParameters(Map<String, String[]> paras);

	/**
	 * 得到查询数据用条件
	 * 
	 * @author TFZZH
	 * @dateTime 2011-3-7 下午04:02:33
	 * @return 查询用条件
	 */
	public abstract QLTermBean getTerms();

	/**
	 * 得到查询数据用排序
	 * 
	 * @author TFZZH
	 * @dateTime 2011-3-8 下午01:31:27
	 * @return 排序用条件
	 */
	public abstract QLOrderBean getOrders();

	/**
	 * 得到目标页数
	 * 
	 * @author TFZZH
	 * @dateTime 2011-3-8 下午01:23:02
	 * @return 页数
	 */
	public abstract int getPage();

	/**
	 * 得到页显示数据行数
	 * 
	 * @author TFZZH
	 * @dateTime 2011-3-8 下午01:22:59
	 * @return 数据行数
	 */
	public abstract int getSize();
}
