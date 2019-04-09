/**
 * @author Xu Weijie
 * @dateTime 2012-7-19 下午10:37:43
 */
package com.tfzzh.view.web.el;

import java.util.Calendar;
import java.util.Date;

import com.tfzzh.tools.DateFormat;

/**
 * 元素类型 show|showDate
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-19 下午10:37:43
 */
enum ElementShowTypeEnum {
	/**
	 * 纯粹为了显示
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-21 下午4:58:05
	 */
	Show {

		@Override
		protected boolean hasTag() {
			return true;
		}

		@Override
		protected String getShowStart() {
			return "<span";
		}

		@Override
		protected String getValContent(final Object val, final String format) {
			return ">" + (null == val ? "" : val.toString());
		}

		@Override
		protected String getShowEnd() {
			return "</span>";
		}
	},
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
		protected String getValContent(final Object val, final String format) {
			return ">" + (null == val ? "" : (val instanceof Date ? DateFormat.customDateFormat((Date) val, format) : (val instanceof Calendar) ? DateFormat.customDateFormat(((Calendar) val).getTime(), format) : val.toString()));
		}

		@Override
		protected String getShowEnd() {
			return "</span>";
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
	 * 得到显示标签开始内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2012-7-20 上午12:51:28
	 * @return 显示标签开始内容
	 */
	protected abstract String getShowStart();

	/**
	 * 得到值内容页面信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 上午11:33:33
	 * @param val 值内容
	 * @return 值标签内容
	 */
	protected String getValContent(final Object val) {
		return this.getValContent(val, null);
	}

	/**
	 * 得到值内容页面信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-8-1 下午3:34:35
	 * @param val 值内容
	 * @param format 格式化方式
	 * @return 值标签内容
	 */
	protected abstract String getValContent(Object val, String format);

	/**
	 * 得到显示标签结束内容
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
	protected static ElementShowTypeEnum getType(final String type) {
		for (final ElementShowTypeEnum e : ElementShowTypeEnum.values()) {
			if (e.name().equalsIgnoreCase(type)) {
				return e;
			}
		}
		return Show;
	}
}
