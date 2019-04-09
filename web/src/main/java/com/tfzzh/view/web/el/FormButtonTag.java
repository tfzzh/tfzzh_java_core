/**
 * @author Xu Weijie
 * @dateTime 2012-7-20 下午1:50:37
 */
package com.tfzzh.view.web.el;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 表单提交用按钮标签
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-20 下午1:50:37
 */
public class FormButtonTag extends TagSupport {

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 下午5:52:22
	 */
	private static final long serialVersionUID = -8016393815640798771L;

	/**
	 * 显示的内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午4:46:37
	 */
	private String value;

	/**
	 * 元素对应style，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午4:46:38
	 */
	private String style;

	/**
	 * 元素对应class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午4:46:38
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
	 * 在按下按钮时进行的js操作，同html
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午4:46:39
	 */
	private String onClick;

	/**
	 * 操作按钮类型
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午7:39:50
	 */
	private OperateButtonTypeEnum type;

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午7:49:16
	 * @return 类型值
	 * @throws JspException 抛
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.type.getShowStart());
		if (null != this.style) {
			sb.append(" style='").append(this.style).append('\'');
		}
		if (null != this.className) {
			sb.append(" class='").append(this.className).append('\'');
		}
		if (null != this.other) {
			sb.append(this.other);
		}
		if (null != this.onClick) {
			sb.append(" onClick='").append(this.onClick).append('\'');
		}
		if (null != this.value) {
			sb.append(this.type.getValContent(this.value));
		}
		sb.append(this.type.getShowEnd());
		try {
			super.pageContext.getOut().println(sb.toString());
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return Tag.EVAL_PAGE;
	}

	/**
	 * 设置
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午7:44:50
	 * @param value the value to set
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	/**
	 * 设置
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午7:44:50
	 * @param style the style to set
	 */
	public void setStyle(final String style) {
		this.style = style;
	}

	/**
	 * 设置
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午7:44:50
	 * @param className the className to set
	 */
	public void setClassName(final String className) {
		this.className = className;
	}

	/**
	 * 设置
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午7:44:50
	 * @param onClick the onClick to set
	 */
	public void setOnClick(final String onClick) {
		this.onClick = onClick;
	}

	/**
	 * 设置操作按钮类型
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午7:44:50
	 * @param type the type to set
	 */
	public void setType(final String type) {
		this.type = OperateButtonTypeEnum.getType(type);
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
