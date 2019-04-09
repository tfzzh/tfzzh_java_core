/**
 * @author Xu Weijie
 * @dateTime 2012-7-19 下午10:37:43
 */
package com.tfzzh.view.web.el;

import java.util.Date;

import com.tfzzh.tools.DateFormat;

/**
 * 元素类型 show|text|hidden|password|textarea|file
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-19 下午10:37:43
 */
enum ElementTypeEnum {
	/**
	 * 显示时间
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-1 下午2:41:36
	 */
	ShowDate {

		@Override
		protected boolean hasTag() {
			return true;
		}

		@Override
		protected String getShowStart() {
			return "<span";
		}

		@Override
		protected boolean needValue() {
			return true;
		}

		@Override
		protected String getValContent(final Object val) {
			return ">" + (val instanceof Date ? DateFormat.getShortDateShow((Date) val) : val.toString());
		}

		@Override
		protected String getValContent() {
			return ">";
		}

		@Override
		protected String getShowEnd() {
			return "</span>";
		}
	},
	/**
	 * 文本类型
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:39:01
	 */
	Text {

		@Override
		protected boolean hasTag() {
			return true;
		}

		@Override
		protected String getShowStart() {
			return "<input type='text'";
		}

		@Override
		protected String getShowEnd() {
			return "/>";
		}

		@Override
		protected String getValContent(final Object val) {
			return " value='" + val + "'";
		}

		@Override
		protected String getValContent() {
			return "";
		}

		@Override
		protected boolean needValue() {
			return true;
		}
	},
	/**
	 * 颜色值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-1-23 下午2:59:46
	 */
	ColorVal {

		@Override
		protected boolean hasTag() {
			return true;
		}

		@Override
		protected String getShowStart() {
			return "<input type='text'";
		}

		@Override
		protected String getShowEnd() {
			return "/>";
		}

		@Override
		protected String getValContent(final Object val) {
			return " value='" + val + "' " + this.getValContent() + " style='color:#" + val + "'";
		}

		@Override
		protected String getValContent() {
			return " onChange='changeEleColorBySelf(this);' maxlength='6' size='8' style='font-weight:bold;'";
		}

		@Override
		protected boolean needValue() {
			return true;
		}
	},
	/**
	 * 隐藏类型
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:39:02
	 */
	Hidden {

		@Override
		protected boolean hasTag() {
			return false;
		}

		@Override
		protected String getShowStart() {
			return "<input type='hidden'";
		}

		@Override
		protected String getShowEnd() {
			return "/>";
		}

		@Override
		protected String getValContent(final Object val) {
			return " value='" + val + "'";
		}

		@Override
		protected String getValContent() {
			return "";
		}

		@Override
		protected boolean needValue() {
			return true;
		}
	},
	/**
	 * 密码类型
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:39:02
	 */
	Password {

		@Override
		protected boolean hasTag() {
			return true;
		}

		@Override
		protected String getShowStart() {
			return "<input type='password'";
		}

		@Override
		protected String getShowEnd() {
			return "/>";
		}

		@Override
		protected String getValContent(final Object val) {
			return "";
		}

		@Override
		protected String getValContent() {
			return "";
		}

		@Override
		protected boolean needValue() {
			return false;
		}
	},
	/**
	 * 大文本类型
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:39:03
	 */
	TextArea {

		@Override
		protected boolean hasTag() {
			return true;
		}

		@Override
		protected String getShowStart() {
			return "<textarea";
		}

		@Override
		protected String getShowEnd() {
			return "</textarea>";
		}

		@Override
		protected String getValContent(final Object val) {
			return ">" + val;
		}

		@Override
		protected String getValContent() {
			return ">";
		}

		@Override
		protected boolean needValue() {
			return true;
		}
	},
	/**
	 * 文件
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:39:04
	 */
	File {

		@Override
		protected boolean hasTag() {
			return true;
		}

		@Override
		protected String getShowStart() {
			return "<input type='file'";
		}

		@Override
		protected String getShowEnd() {
			return "/>";
		}

		@Override
		protected String getValContent(final Object val) {
			return "";
		}

		@Override
		protected String getValContent() {
			return "";
		}

		@Override
		protected boolean needValue() {
			return false;
		}
	},
	/**
	 * 单纯的显示
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-12-25 下午6:16:55
	 */
	Show {

		@Override
		protected boolean hasTag() {
			return true;
		}

		@Override
		protected String getShowStart() {
			return null;
		}

		@Override
		protected boolean needValue() {
			return true;
		}

		@Override
		protected String getValContent(final Object val) {
			return val.toString();
		}

		@Override
		protected String getValContent() {
			return "";
		}

		@Override
		protected String getShowEnd() {
			return null;
		}
	};

	/**
	 * 是否存在包围的标签
	 * 
	 * @author Weijie Xu
	 * @dateTime 2012-7-20 上午12:44:43
	 * @return true，存在；<br />
	 *         false，不存在；<br />
	 */
	protected abstract boolean hasTag();

	/**
	 * 得到显示标签开始内容<br />
	 * 如果为null，则表示没有标签，一般用于Show情况<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2012-7-20 上午12:51:28
	 * @return 显示标签开始内容
	 */
	protected abstract String getShowStart();

	/**
	 * 需要具体值
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午12:04:05
	 * @return true，需要具体值；<br />
	 *         false，不需要具体值；<br />
	 */
	protected abstract boolean needValue();

	/**
	 * 得到值内容页面信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 上午11:33:33
	 * @param val 值内容
	 * @return 值标签内容
	 */
	protected abstract String getValContent(Object val);

	/**
	 * 得到值内容页面信息，不需要值的情况
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-21 下午12:06:47
	 * @return 值标签内容
	 */
	protected abstract String getValContent();

	/**
	 * 得到显示标签结束内容<br />
	 * 如果为null，则表示没有标签，一般用于Show情况<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2012-7-20 上午12:51:30
	 * @return 显示标签结束内容
	 */
	protected abstract String getShowEnd();

	/**
	 * 得到元素类型信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:41:09
	 * @param type 类型名
	 * @return 元素类型
	 */
	protected static ElementTypeEnum getType(final String type) {
		for (final ElementTypeEnum e : ElementTypeEnum.values()) {
			if (e.name().equalsIgnoreCase(type)) {
				return e;
			}
		}
		return Text;
	}
}
