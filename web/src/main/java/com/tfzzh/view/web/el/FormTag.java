/**
 * @author Xu Weijie
 * @dateTime 2012-7-19 下午3:03:09
 */
package com.tfzzh.view.web.el;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 表单用标签
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-19 下午3:03:09
 */
public class FormTag extends TagSupport {

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 下午5:55:59
	 */
	private static final long serialVersionUID = -2431821458678110833L;

	/**
	 * id的值
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午8:21:25
	 */
	private String id;

	/**
	 * 表单提交的目标，同html
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午4:05:25
	 */
	private String action;

	/**
	 * 表现所显示的目标，同html
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午4:05:26
	 */
	private String target;

	/**
	 * 提交表单的方式，同html
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午8:15:40
	 */
	private String method = "post";

	/**
	 * 在提交前需要进行的js操作，同html
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午8:15:41
	 */
	private String onSubmit;

	/**
	 * 元素所需的其他属性内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 下午5:55:01
	 */
	private String other;

	/**
	 * 在开始标签时
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午4:07:02
	 * @return 类型值
	 * @throws JspException 抛
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		final StringBuilder sb = new StringBuilder();
		sb.append("<form");
		if (null != this.id) {
			sb.append(" id='").append(this.id).append('\'');
		}
		if (null != this.action) {
			sb.append(" action='").append(this.action).append('\'');
		}
		if (null != this.target) {
			sb.append(" target='").append(this.target).append('\'');
		}
		if (null != this.method) {
			sb.append(" method='").append(this.method).append('\'');
		}
		if (null != this.onSubmit) {
			sb.append(" onSubmit='").append(this.onSubmit).append('\'');
		}
		if (null != this.other) {
			sb.append(this.other);
		}
		sb.append('>');
		try {
			super.pageContext.getOut().println(sb.toString());
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return Tag.EVAL_PAGE;
	}

	/**
	 * 在关闭标签时
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午4:14:39
	 * @return 类型值
	 * @throws JspException 抛
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		try {
			super.pageContext.getOut().println("</form>");
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return Tag.EVAL_BODY_INCLUDE;
	}

	/**
	 * 设置id的值
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午8:22:35
	 * @param id the id to set
	 */
	@Override
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * 设置表单提交的目标，同html
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午4:05:58
	 * @param action the action to set
	 */
	public void setAction(final String action) {
		this.action = action;
	}

	/**
	 * 设置所显示的目标，同html
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午4:05:58
	 * @param target the target to set
	 */
	public void setTarget(final String target) {
		this.target = target;
	}

	/**
	 * 设置提交表单的方式，同html
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午8:22:35
	 * @param method the method to set
	 */
	public void setMethod(final String method) {
		this.method = method;
	}

	/**
	 * 设置在提交前需要进行的js操作，同html
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午8:22:35
	 * @param onSubmit the onSubmit to set
	 */
	public void setOnSubmit(final String onSubmit) {
		this.onSubmit = onSubmit;
	}

	/**
	 * 设置元素所需的其他属性内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-1 下午4:26:35
	 * @param other the other to set
	 */
	public void setOther(final String other) {
		this.other = other;
	}
}
