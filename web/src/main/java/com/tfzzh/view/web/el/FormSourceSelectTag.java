/**
 * @author Weijie Xu
 * @dateTime 2012-7-23 上午2:51:37
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
import com.tfzzh.tools.Constants;
import com.tfzzh.view.web.bean.BaseOptionsParamBean;
import com.tfzzh.view.web.bean.BaseParamBean;

/**
 * 表单元数据选择类标签
 * 
 * @author Weijie Xu
 * @dateTime 2012-7-23 上午2:51:37
 */
public class FormSourceSelectTag extends TagSupport {

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 下午5:51:21
	 */
	private static final long serialVersionUID = -189987542521890079L;

	/**
	 * 标题名
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:30:32
	 */
	private String title;

	/**
	 * ID，页面用
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午8:50:28
	 */
	private String id;

	/**
	 * 源数据选择类型<br />
	 * select|checkbox|radio<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-23 上午11:21:18
	 */
	private SourceSelectTypeEnum type;

	/**
	 * 多选数据源
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-23 上午11:01:48
	 */
	private BaseOptionsParamBean source;

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
	 * 对应class名字，不解释
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
	 * 壳层次信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 上午11:00:52
	 */
	private String shell;

	/**
	 * 对应壳层次class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-2 下午3:41:34
	 */
	private String shellClassName;

	/**
	 * 对应壳层次所需的其他属性内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-2 下午3:41:37
	 */
	private String shellOther;

	/**
	 * 标题对应style，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:49:44
	 */
	private String titStyle;

	/**
	 * 标题对应class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午8:50:58
	 */
	private String titClassName;

	/**
	 * 元素所需的其他属性内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-1 下午4:25:58
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
	 * 元素所需的其他属性内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-1 下午4:25:58
	 */
	private String valOther;

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-7-23 上午11:07:32
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
		final String baseAtt;
		// 得到字段对象
		if ((null != this.data) && (null != this.att)) {
			// 属性值
			param = super.pageContext.getAttribute(this.data);
			if (null == param) {
				baseAtt = "";
				// 不存在目标属性，应该为不可能出现的情况
				// throw new JspException("Not exists ParamBean: " + this.data + " with " + this.id + ":" + this.title);
			} else {
				if ((param instanceof BaseParamBean) || (param instanceof BaseSystemBean)) {
					baseAtt = this.att.substring(0, 1).toUpperCase() + this.att.substring(1);
					try {
						m = param.getClass().getMethod("get" + baseAtt);
					} catch (NoSuchMethodException | SecurityException e) {
						throw new JspException("Not exists Attribute:" + this.att + " in ParamBean:" + this.data + " with " + this.id + ":" + this.title);
					}
				} else {
					baseAtt = "";
				}
			}
		} else {
			throw new JspException("Need a exists Attribute in ParamBean! " + this.id + "(" + this.data + ":" + this.att + ")");
		}
		final StringBuilder sb = new StringBuilder();
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
		// 控制用ID
		sb.append(" id='").append(this.att).append("Show'");
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
		sb.append(type.getEleEnd());
		if (null != m) {
			// 设置隐藏数据
			try {
				final Object obj = m.invoke(param);
				if (null == obj) {
					// 因不存在，改用默认值
					if (null == this.def) {
						// 该情况认为，可以不需要显示表现值
						sb.append(this.type.showHiddenData(this.att, baseAtt, ""));
					} else {
						sb.append(this.type.showHiddenData(this.att, baseAtt, this.def));
					}
				} else {
					sb.append(this.type.showHiddenData(this.att, baseAtt, obj.toString()));
				}
			} catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new JspException("Error Data Attribute:" + this.att + " in ParamBean:" + this.data + " with " + this.id + ":" + this.title);
			}
		}
		// 插入js脚本
		sb.append("<script type='text/javascript' def>");
		// 设置数据源
		sb.append("var ").append(this.att).append("Data=").append(this.source.getOptionsJson()).append(Constants.SEMICOLON);
		// 调用js
		sb.append(this.type.showJsFunction()).append("('").append(this.att).append("',");
		if (null != this.def) {
			sb.append('\'').append(this.def).append("\',");
		} else {
			sb.append("undefined,");
		}
		sb.append(this.att).append("Data,");
		if (null == this.className) {
			sb.append("undefined,");
		} else {
			sb.append('\'').append(this.className).append("\',");
		}
		if (null == this.other) {
			sb.append("undefined,");
		} else {
			sb.append('\'').append(this.other).append("\',");
		}
		if (null == this.shell) {
			sb.append("undefined,");
		} else {
			sb.append('\'').append(this.shell).append("\',");
		}
		if (null == this.shellClassName) {
			sb.append("undefined,");
		} else {
			sb.append('\'').append(this.shellClassName).append("\',");
		}
		if (null == this.shellOther) {
			sb.append("undefined);");
		} else {
			sb.append('\'').append(this.shellOther).append("\');");
		}
		sb.append("</script>");
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
	 * @dateTime 2012-7-23 下午4:58:41
	 * @param title the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * 设置ID，页面用
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-23 下午4:58:41
	 * @param id the id to set
	 */
	@Override
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * 设置源数据选择类型<br />
	 * select|checkbox|radio<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-23 下午4:58:41
	 * @param type the type to set
	 */
	public void setType(final String type) {
		this.type = SourceSelectTypeEnum.getType(type);
	}

	/**
	 * 设置多选数据源
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-23 下午4:58:41
	 * @param source the source to set
	 */
	public void setSource(final BaseOptionsParamBean source) {
		this.source = source;
	}

	/**
	 * 设置对应表单数据对象中的键值
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-23 下午4:58:41
	 * @param data the data to set
	 */
	public void setData(final String data) {
		this.data = data;
	}

	/**
	 * 设置对应表单数据对象中属性名
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-23 下午4:58:41
	 * @param att the att to set
	 */
	public void setAtt(final String att) {
		this.att = att;
	}

	/**
	 * 设置默认值
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-23 下午4:58:41
	 * @param def the def to set
	 */
	public void setDef(final String def) {
		this.def = def;
	}

	/**
	 * 设置对应class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-23 下午4:58:41
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
	 * 设置壳层次信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 下午4:39:02
	 * @param shell the shell to set
	 */
	public void setShell(final String shell) {
		this.shell = shell;
	}

	/**
	 * 设置对应壳层次class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 下午4:39:02
	 * @param shellClassName the shellClassName to set
	 */
	public void setShellClassName(final String shellClassName) {
		this.shellClassName = shellClassName;
	}

	/**
	 * 设置对应壳层次所需的其他属性内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-3 下午4:39:02
	 * @param shellOther the shellOther to set
	 */
	public void setShellOther(final String shellOther) {
		this.shellOther = shellOther;
	}

	/**
	 * 设置标题对应style，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-23 下午4:58:41
	 * @param titStyle the titStyle to set
	 */
	public void setTitStyle(final String titStyle) {
		this.titStyle = titStyle;
	}

	/**
	 * 设置标题对应class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-23 下午4:58:41
	 * @param titClassName the titClassName to set
	 */
	public void setTitClassName(final String titClassName) {
		this.titClassName = titClassName;
	}

	/**
	 * 设置元素所需的其他属性内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-1 下午4:26:35
	 * @param titOther the titOther to set
	 */
	public void setTitOther(final String titOther) {
		this.titOther = titOther;
	}

	/**
	 * 设置值对应style，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-23 下午4:58:41
	 * @param valStyle the valStyle to set
	 */
	public void setValStyle(final String valStyle) {
		this.valStyle = valStyle;
	}

	/**
	 * 设置值对应class名字，不解释
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-23 下午4:58:41
	 * @param valClassName the valClassName to set
	 */
	public void setValClassName(final String valClassName) {
		this.valClassName = valClassName;
	}

	/**
	 * 设置元素所需的其他属性内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-1 下午4:26:35
	 * @param valOther the valOther to set
	 */
	public void setValOther(final String valOther) {
		this.valOther = valOther;
	}
}
