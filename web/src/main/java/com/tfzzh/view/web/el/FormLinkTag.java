/**
 * @author Xu Weijie
 * @dateTime 2012-7-20 下午5:17:44
 */
package com.tfzzh.view.web.el;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 表单提交用图片按钮标签
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-20 下午5:17:44
 */
public class FormLinkTag extends TagSupport {

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 下午5:51:51
	 */
	private static final long serialVersionUID = -7573744558062940267L;

	/**
	 * 目标地址
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午5:18:57
	 */
	private String href;

	/**
	 * 打开目标
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-21 上午12:19:26
	 */
	private String target;

	/**
	 * 显示内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-21 上午12:17:48
	 */
	private String value;

	/**
	 * 连接对应style，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午5:18:58
	 */
	private String style;

	/**
	 * 连接对应class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午5:18:58
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
	 * @dateTime 2012-7-20 下午5:18:59
	 */
	private String onClick;

	/**
	 * 操作连接类型：link
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午7:59:54
	 */
	private OperateLinkTypeEnum type;

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-7-21 上午12:21:16
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
		sb.append(this.type.getUrlContent(this.href));
		if (null != this.target) {
			sb.append(" target='").append(this.target).append('\'');
		}
		if (null != this.onClick) {
			sb.append(" onClick='").append(this.onClick).append('\'');
		}
		if (null != this.other) {
			sb.append(this.other);
		}
		sb.append(this.type.getValContent(this.value));
		sb.append(this.type.getShowEnd());
		try {
			super.pageContext.getOut().println(sb.toString());
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return super.doStartTag();
	}

	/**
	 * 设置目标地址
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-21 上午12:21:58
	 * @param href the href to set
	 */
	public void setHref(final String href) {
		this.href = href;
	}

	/**
	 * 设置打开目标
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-21 上午12:21:58
	 * @param target the target to set
	 */
	public void setTarget(final String target) {
		this.target = target;
	}

	/**
	 * 设置显示内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-21 上午12:21:58
	 * @param value the value to set
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	/**
	 * 设置连接对应style，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-21 上午12:21:58
	 * @param style the style to set
	 */
	public void setStyle(final String style) {
		this.style = style;
	}

	/**
	 * 设置连接对应class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-21 上午12:21:58
	 * @param className the className to set
	 */
	public void setClassName(final String className) {
		this.className = className;
	}

	/**
	 * 设置在按下按钮时进行的js操作，同html
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-21 上午12:21:58
	 * @param onClick the onClick to set
	 */
	public void setOnClick(final String onClick) {
		this.onClick = onClick;
	}

	/**
	 * 设置操作连接类型
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-21 上午12:21:58
	 * @param type the type to set
	 */
	public void setType(final String type) {
		this.type = OperateLinkTypeEnum.getType(type);
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
