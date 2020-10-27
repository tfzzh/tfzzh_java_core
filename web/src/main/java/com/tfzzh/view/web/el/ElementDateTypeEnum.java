/**
 * @author Weijie Xu
 * @dateTime 2013-12-24 下午2:12:24
 */
package com.tfzzh.view.web.el;

/**
 * 元素时间类型<br />
 * ymd：显示>年-月-日<br />
 * 
 * @author Weijie Xu
 * @dateTime 2013-12-24 下午2:12:24
 */
public enum ElementDateTypeEnum {

	/**
	 * 针对
	 * 
	 * @author Weijie Xu
	 * @dateTime 2013-12-24 下午2:21:00
	 */
	YMD {

		@Override
		protected boolean hasTag() {
			return true;
		}

		@Override
		protected String getShowStart(final String name) {
			return "<div y=\"" + name + "Year\" m=\"" + name + "Month\" d=\"" + name + "Day\" i=\"" + name + "\" ";
		}

		@Override
		protected String getValContent(final Object content) {
			if (null == content) {
				return "";
			} else {
				final String[] s = content.toString().split("[-_/]");
				if (s.length == 3) {
					return " yv=\"" + s[0] + "\" mv=\"" + s[1] + "\" dv=\"" + s[2] + "\"";
				}
			}
			return "";
		}

		@Override
		protected String getShowEnd() {
			return "></div>";
		}

		@Override
		protected String getClassName() {
			return "date_ymd_select";
		}
	};

	/**
	 * 是否存在包围的标签
	 * 
	 * @author Weijie Xu
	 * @dateTime 2013-12-24 下午2:27:22
	 * @return true，存在；<br />
	 *         false，不存在；<br />
	 */
	protected abstract boolean hasTag();

	/**
	 * 得到显示标签开始内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2013-12-24 下午2:27:23
	 * @param name 特定内容
	 * @return 显示标签开始内容
	 */
	protected abstract String getShowStart(String name);

	/**
	 * 得到值内容页面信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2013-12-24 下午2:27:24
	 * @param content 值内容
	 * @return 值标签内容
	 */
	protected abstract String getValContent(Object content);

	/**
	 * 得到显示标签结束内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2013-12-24 下午2:27:25
	 * @return 显示标签结束内容
	 */
	protected abstract String getShowEnd();

	/**
	 * 得到Class用名称
	 * 
	 * @author Weijie Xu
	 * @dateTime 2013-12-24 下午3:26:13
	 * @return Class用名
	 */
	protected abstract String getClassName();

	/**
	 * 得到元素类型信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2013-12-24 下午2:27:28
	 * @param type 类型名
	 * @return 元素类型
	 */
	public static ElementDateTypeEnum getType(final String type) {
		for (final ElementDateTypeEnum e : ElementDateTypeEnum.values()) {
			if (e.name().equalsIgnoreCase(type)) {
				return e;
			}
		}
		return YMD;
	}
}
