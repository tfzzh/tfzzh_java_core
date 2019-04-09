/**
 * @author Xu Weijie
 * @dateTime 2012-7-19 下午3:03:09
 */
package com.tfzzh.view.web.el;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.tfzzh.view.web.bean.BaseParamBean;

/**
 * 表单对象标签
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-19 下午3:03:09
 */
public class FormParamTag extends TagSupport {

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-7-22 上午2:55:01
	 */
	private static final long serialVersionUID = 7627482557508000620L;

	/**
	 * 页面参数数据Bean
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午5:58:06
	 */
	private BaseParamBean value;

	/**
	 * 是否可以不存在<br />
	 * 如果可以，则用Object替代<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-21 下午12:09:29
	 */
	private boolean canNon = false;

	/**
	 * 该数据Bean对应的Key
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:27:52
	 */
	private String key;

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:29:30
	 * @return 类型值
	 * @throws JspException 抛
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		if (this.value == null) {
			// 不存在目标值
			if (this.canNon) {
				// 可空情况，放入一个替代obj
				super.pageContext.setAttribute(this.key, new Object());
			} else {
				// 返回错误信息
				throw new JspException("Not exists ParamBean: " + this.key);
			}
		} else {
			super.pageContext.setAttribute(this.key, this.value);
		}
		return super.doStartTag();
	}

	/**
	 * 设置页面参数信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午5:57:41
	 * @param value the value to set
	 */
	public void setValue(final BaseParamBean value) {
		this.value = value;
	}

	/**
	 * 设置是否可以不存在<br />
	 * 如果可以，则用Object替代<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-21 下午12:10:05
	 * @param canNon the canNon to set
	 */
	public void setCanNon(final boolean canNon) {
		this.canNon = canNon;
	}

	/**
	 * 设置该数据Bean对应的Key
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:28:40
	 * @param key the key to set
	 */
	public void setKey(final String key) {
		this.key = key;
	}
}
