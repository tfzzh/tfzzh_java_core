/**
 * @author Xu Weijie
 * @dateTime 2012-7-19 下午3:03:09
 */
package com.tfzzh.view.web.el;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.tfzzh.core.control.bean.BaseSystemBean;

/**
 * 表单对象标签
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-19 下午3:03:09
 */
public class FormSystemTag extends TagSupport {

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-7-22 上午2:57:00
	 */
	private static final long serialVersionUID = 3143586247657176579L;

	/**
	 * 页面参数数据Bean
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午5:58:06
	 */
	private BaseSystemBean value;

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
			// 返回错误信息
			throw new JspException("Not exists SystemBean: " + this.key);
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
	public void setValue(final BaseSystemBean value) {
		this.value = value;
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
