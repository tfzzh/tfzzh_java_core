/**
 * @author Xu Weijie
 * @dateTime 2012-7-19 下午8:46:34
 */
package com.tfzzh.view.web.el;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.tfzzh.core.control.bean.BaseSystemBean;
import com.tfzzh.view.web.bean.BaseParamBean;

/**
 * 表单元素标签
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-19 下午8:46:34
 */
public class FormElementShowTag extends TagSupport {

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 下午5:52:12
	 */
	private static final long serialVersionUID = -8861134638376957035L;

	/**
	 * 标题名
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:30:32
	 */
	private String title;

	/**
	 * 信息录入类型：<br />
	 * show|showDate<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午8:50:31
	 */
	private ElementShowTypeEnum type;

	/**
	 * 对应表单数据对象中的键值
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午8:50:32
	 */
	private String data;

	/**
	 * 对应表单数据对象中属性名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2012-7-20 上午2:29:01
	 */
	private String att;

	/**
	 * 默认值
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:36:05
	 */
	private String def;

	/**
	 * 格式化内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-1 下午4:12:39
	 */
	private String format;

	/**
	 * 元素对应style，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:49:44
	 */
	private String style;

	/**
	 * 标题对应class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午8:50:58
	 */
	private String className;

	/**
	 * 元素所需的其他属性内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 下午5:43:31
	 */
	private String other;

	/**
	 * 标题对应style，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:49:44
	 */
	private String titStyle;

	/**
	 * 元素对应class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午8:50:58
	 */
	private String titClassName;

	/**
	 * 标题所需的其他属性内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 下午5:44:11
	 */
	private String titOther;

	/**
	 * 值对应style，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:49:44
	 */
	private String valStyle;

	/**
	 * 值对应class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午8:50:58
	 */
	private String valClassName;

	/**
	 * 值所需的其他属性内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 下午5:44:10
	 */
	private String valOther;

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:33:01
	 * @return 类型值
	 * @throws JspException 抛
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		final FormTypeEnum type = (FormTypeEnum) super.pageContext.getAttribute(FormTypeEnum.CONTEXT_KEY_FORM_TYPE);
		if (null == type) {
			throw new JspTagException("Need The Tag 'type' Before!!");
		}
		// 对应方法
		Method m = null;
		// 数据对象
		final Object param;
		// 得到字段对象
		if ((null != this.data) && (null != this.att)) {
			// 属性值
			param = super.pageContext.getAttribute(this.data);
			if (null == param) {
				// 不存在目标属性，应该为不可能出现的情况
				throw new JspException("Not exists ParamBean: " + this.data + " with " + this.id + ":" + this.title);
			} else {
				if ((param instanceof BaseParamBean) || (param instanceof BaseSystemBean)) {
					final String mp = this.att.substring(0, 1).toUpperCase() + this.att.substring(1);
					try {
						m = param.getClass().getMethod("get" + mp);
					} catch (NoSuchMethodException | SecurityException e) {
						try {
							m = param.getClass().getMethod("is" + mp);
						} catch (NoSuchMethodException | SecurityException e1) {
							throw new JspException("Not exists Attribute:" + this.att + " in ParamBean:" + this.data + " with " + this.id + ":" + this.title);
						}
					}
				}
			}
		} else {
			throw new JspException("Need a exists Attribute in ParamBean! " + this.id + "(" + this.data + ":" + this.att + ")");
		}
		final StringBuilder sb = new StringBuilder();
		if (this.type.hasTag()) {
			sb.append(type.getEleStart());
			if (null != this.titStyle) {
				sb.append(" style='").append(this.titStyle).append('\'');
			}
			if (null != this.titClassName) {
				sb.append(" class='").append(this.titClassName).append('\'');
			}
			if (null != this.titOther) {
				sb.append(this.titOther);
			}
			sb.append('>');
			sb.append(this.title);
			sb.append(type.getEleSeparate());
			if (null != this.valStyle) {
				sb.append(" style='").append(this.valStyle).append('\'');
			}
			if (null != this.valClassName) {
				sb.append(" class='").append(this.valClassName).append('\'');
			}
			if (null != this.valOther) {
				sb.append(this.valOther);
			}
			sb.append('>');
		}
		sb.append(this.type.getShowStart());
		if (null != this.id) {
			sb.append(" id='").append(this.id).append('\'');
		}
		sb.append(" name='").append(this.att).append('\'');
		if (null != this.style) {
			sb.append(" style='").append(this.style).append('\'');
		}
		if (null != this.className) {
			sb.append(" class='").append(this.className).append('\'');
		}
		if (null != this.other) {
			sb.append(this.other);
		}
		// TODO 之后需要考虑一些条件类验证方式的增加
		if ((param instanceof BaseParamBean) || (param instanceof BaseSystemBean)) {
			// 需要得到值
			try {
				final Object obj = m.invoke(param);
				if (null == obj) {
					// 因不存在，改用默认值
					if (null == this.def) {
						// 该情况认为，可以不需要显示表现值
						sb.append(this.type.getValContent(null, this.format));
					} else {
						sb.append(this.type.getValContent(this.def, this.format));
					}
				} else {
					sb.append(this.type.getValContent(obj, this.format));
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new JspException("Error Data Attribute:" + this.att + " in ParamBean:" + this.data + " with " + this.id + ":" + this.title);
			}
		} else {
			// 该情况认为，可以不需要显示表现值
			sb.append(this.type.getValContent(null, this.format));
		}
		sb.append(this.type.getShowEnd());
		if (this.type.hasTag()) {
			sb.append(type.getEleEnd());
		}
		try {
			super.pageContext.getOut().println(sb.toString());
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return Tag.EVAL_PAGE;
	}

	/**
	 * 设置标题名
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:31:22
	 * @param title the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * 设置ID，页面用
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:15:23
	 * @param id the id to set
	 */
	@Override
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * 设置信息录入类型：<br />
	 * show|showDate<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:15:23
	 * @param type the type to set
	 */
	public void setType(final String type) {
		this.type = ElementShowTypeEnum.getType(type);
	}

	/**
	 * 设置对应表单数据对象中的键值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2012-7-20 上午2:29:47
	 * @param data the data to set
	 */
	public void setData(final String data) {
		this.data = data;
	}

	/**
	 * 设置对应表单数据对象中属性名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2012-7-20 上午2:29:47
	 * @param att the att to set
	 */
	public void setAtt(final String att) {
		this.att = att;
	}

	/**
	 * 设置默认值
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:39:02
	 * @param def the def to set
	 */
	public void setDefault(final String def) {
		this.def = def;
	}

	/**
	 * 设置格式化内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-1 下午4:13:12
	 * @param format the format to set
	 */
	public void setFormat(final String format) {
		this.format = format;
	}

	/**
	 * 设置元素对应style，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:50:05
	 * @param style the style to set
	 */
	public void setStyle(final String style) {
		this.style = style;
	}

	/**
	 * 设置元素对应class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:15:23
	 * @param className the className to set
	 */
	public void setClassName(final String className) {
		this.className = className;
	}

	/**
	 * 设置元素所需的其他属性内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 下午5:46:56
	 * @param other the other to set
	 */
	public void setOther(final String other) {
		this.other = other;
	}

	/**
	 * 设置标题对应style，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:33:22
	 * @param titStyle the titStyle to set
	 */
	public void setTitStyle(final String titStyle) {
		this.titStyle = titStyle;
	}

	/**
	 * 设置标题对应class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:33:22
	 * @param titClassName the titClassName to set
	 */
	public void setTitClassName(final String titClassName) {
		this.titClassName = titClassName;
	}

	/**
	 * 设置标题所需的其他属性内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 下午5:46:56
	 * @param titOther the titOther to set
	 */
	public void setTitOther(final String titOther) {
		this.titOther = titOther;
	}

	/**
	 * 设置值对应style，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:33:22
	 * @param valStyle the valStyle to set
	 */
	public void setValStyle(final String valStyle) {
		this.valStyle = valStyle;
	}

	/**
	 * 设置值对应class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:33:22
	 * @param valClassName the valClassName to set
	 */
	public void setValClassName(final String valClassName) {
		this.valClassName = valClassName;
	}

	/**
	 * 设置值所需的其他属性内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 下午5:46:56
	 * @param valOther the valOther to set
	 */
	public void setValOther(final String valOther) {
		this.valOther = valOther;
	}
}
