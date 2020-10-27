/**
 * @author Xu Weijie
 * @dateTime 2012-7-19 下午9:46:14
 */
package com.tfzzh.view.web.el;

/**
 * 表单类型
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-19 下午9:46:14
 */
enum FormTypeEnum {

	/**
	 * table模式表单
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:48:02
	 */
	Tabel {

		@Override
		protected String getTagStart() {
			return "<table";
		}

		@Override
		protected String getTagEnd() {
			return "</table>";
		}

		@Override
		protected String getEleStart() {
			return "<tr><td";
		}

		@Override
		protected String getEleSeparate() {
			return "</td><td";
		}

		@Override
		protected String getEleEnd() {
			return "</td></tr>";
		}
	},
	/**
	 * div模式表单
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:48:04
	 */
	Div {

		@Override
		protected String getTagStart() {
			return "<div";
		}

		@Override
		protected String getTagEnd() {
			return "</div>";
		}

		@Override
		protected String getEleStart() {
			return "<div";
		}

		@Override
		protected String getEleSeparate() {
			return "</div><div";
		}

		@Override
		protected String getEleEnd() {
			return "</div>";
		}
	};

	/**
	 * 上下文键值：表单结构类型
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:55:52
	 */
	protected final static String CONTEXT_KEY_FORM_TYPE = "ft";

	/**
	 * 得到表单标签开始内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:07:00
	 * @return 开始标签内容
	 */
	protected abstract String getTagStart();

	/**
	 * 得到表单标签结束内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:07:01
	 * @return 结束标签内容
	 */
	protected abstract String getTagEnd();

	/**
	 * 得到元素标签开始内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:11:55
	 * @return 元素标签开始内容
	 */
	protected abstract String getEleStart();

	/**
	 * 得到元素标签隔断内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:11:56
	 * @return 元素标签隔断内容
	 */
	protected abstract String getEleSeparate();

	/**
	 * 得到元素标签结束内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午10:11:57
	 * @return 元素标签结束内容
	 */
	protected abstract String getEleEnd();

	/**
	 * 得到表单类型信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-19 下午9:48:01
	 * @param type 类型名
	 * @return 表单类型
	 */
	protected static FormTypeEnum getType(final String type) {
		for (final FormTypeEnum e : FormTypeEnum.values()) {
			if (e.name().equalsIgnoreCase(type)) {
				return e;
			}
		}
		return Tabel;
	}
}
