/**
 * @author Xu Weijie
 * @dateTime 2012-7-20 下午2:42:20
 */
package com.tfzzh.view.web.el;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 表单控制区
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-20 下午2:42:20
 */
public class FormOtherLineTag extends TagSupport {

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 下午5:51:30
	 */
	private static final long serialVersionUID = 305136729946601854L;

	/**
	 * 元素对应style，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午3:19:26
	 */
	private String style;

	/**
	 * 标题对应class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午3:19:26
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
	 * 合并横向单元列数<br />
	 * 针对table类型结构<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午3:19:25
	 */
	private int colspan = -1;

	/**
	 * 表单类型
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午3:19:24
	 */
	private FormTypeEnum type = null;

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午3:19:23
	 * @return 类型值
	 * @throws JspException 抛
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		final FormTypeEnum type = (FormTypeEnum) super.pageContext.getAttribute(FormTypeEnum.CONTEXT_KEY_FORM_TYPE);
		if (null == type) {
			throw new JspTagException("Need The Tag 'type' Before!!");
		} else {
			this.type = type;
		}
		final StringBuilder sb = new StringBuilder();
		sb.append(this.type.getEleStart());
		if (null != this.style) {
			sb.append(" style='").append(this.style).append('\'');
		}
		if (null != this.className) {
			sb.append(" class='").append(this.className).append('\'');
		}
		if (this.colspan > 0) {
			sb.append(" colspan='").append(this.colspan).append('\'');
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
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午3:19:20
	 * @return 类型值
	 * @throws JspException 抛
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		try {
			super.pageContext.getOut().println(this.type.getEleEnd());
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return Tag.EVAL_PAGE;
	}

	/**
	 * 设置元素对应style，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午4:45:15
	 * @param style the style to set
	 */
	public void setStyle(final String style) {
		this.style = style;
	}

	/**
	 * 设置标题对应class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午4:45:15
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

	/**
	 * 设置合并横向单元列数<br />
	 * 针对table类型结构<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午4:45:15
	 * @param colspan the colspan to set
	 */
	public void setColspan(final int colspan) {
		this.colspan = colspan;
	}
}
