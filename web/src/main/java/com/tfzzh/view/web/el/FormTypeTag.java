/**
 * @author Xu Weijie
 * @dateTime 2012-7-19 下午9:44:59
 */
package com.tfzzh.view.web.el;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Xu Weijie
 * @dateTime 2012-7-19 下午9:44:59
 */
public class FormTypeTag extends TagSupport {

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 下午5:57:33
	 */
	private static final long serialVersionUID = 669906496321516005L;

	/**
	 * 表单类型
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:53:10
	 */
	private FormTypeEnum type;

	/**
	 * ID，页面用
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午8:50:28
	 */
	private String id;

	/**
	 * 元素对应style，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:49:44
	 */
	private String style;

	/**
	 * 元素对应class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午8:50:58
	 */
	private String className;

	/**
	 * 元素所需的其他属性内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-1 下午4:25:58
	 */
	private String other;

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:56:52
	 * @return 类型值
	 * @throws JspException 抛
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		super.pageContext.setAttribute(FormTypeEnum.CONTEXT_KEY_FORM_TYPE, this.type);
		final StringBuilder sb = new StringBuilder();
		sb.append(this.type.getTagStart());
		if (null != this.id) {
			sb.append(" id='").append(this.id).append('\'');
		}
		if (null != this.style) {
			sb.append(" style='").append(this.style).append('\'');
		}
		if (null != this.className) {
			sb.append(" class='").append(this.className).append('\'');
		}
		if (null != this.other) {
			sb.append(this.other);
		}
		sb.append(">");
		try {
			super.pageContext.getOut().println(sb.toString());
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return Tag.EVAL_PAGE;
	}

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:05:45
	 * @return 类型值
	 * @throws JspException 抛
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		try {
			super.pageContext.getOut().println(this.type.getTagEnd());
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return Tag.EVAL_PAGE;
	}

	/**
	 * 设置表单类型
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:53:18
	 * @param type the type to set
	 */
	public void setType(final String type) {
		this.type = FormTypeEnum.getType(type);
	}

	/**
	 * 设置ID，页面用
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:04:54
	 * @param id the id to set
	 */
	@Override
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * 设置元素对应style，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:04:54
	 * @param style the style to set
	 */
	public void setStyle(final String style) {
		this.style = style;
	}

	/**
	 * 设置元素对应class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:04:54
	 * @param className the className to set
	 */
	public void setClassName(final String className) {
		this.className = className;
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
