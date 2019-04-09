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
public class FormMsgTag extends TagSupport {

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 下午5:51:40
	 */
	private static final long serialVersionUID = -2471522967016612306L;

	/**
	 * 默认值
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午5:18:57
	 */
	private String def;

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
	 * 操作连接类型：link
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午7:59:54
	 */
	private final MsgTypeEnum type = MsgTypeEnum.Msg;

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
		if (null != this.other) {
			sb.append(this.other);
		}
		if (null != this.value) {
			sb.append(this.type.getValContent(this.value));
		} else if (null != this.def) {
			sb.append(this.type.getValContent(this.def));
		} else {
			sb.append(this.type.getValContent());
		}
		sb.append(this.type.getShowEnd());
		try {
			super.pageContext.getOut().println(sb.toString());
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return super.doStartTag();
	}

	/**
	 * 默认值
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-21 上午12:21:58
	 * @param def the def to set
	 */
	public void setDef(final String def) {
		this.def = def;
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
