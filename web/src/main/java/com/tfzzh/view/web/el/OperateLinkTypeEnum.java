/**
 * @author Xu Weijie
 * @dateTime 2012-7-20 下午6:24:30
 */
package com.tfzzh.view.web.el;

/**
 * 操作连接类型
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-20 下午6:24:30
 */
public enum OperateLinkTypeEnum {
	/**
	 * 普通连接
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午7:00:54
	 */
	Link {

		@Override
		protected String getShowStart() {
			return "<a";
		}

		@Override
		protected String getUrlContent(final String url) {
			return " href='" + url + "'";
		}

		@Override
		protected String getValContent(final String val) {
			return ">" + val;
		}

		@Override
		protected String getShowEnd() {
			return "</a>";
		}
	};

	/**
	 * 得到显示标签开始内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午6:14:31
	 * @return 显示标签开始内容
	 */
	protected abstract String getShowStart();

	/**
	 * 得到地址内容页面信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午6:14:32
	 * @param url 地址内容
	 * @return 地址标签内容
	 */
	protected abstract String getUrlContent(String url);

	/**
	 * 得到值内容页面信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午6:14:32
	 * @param val 值内容
	 * @return 值标签内容
	 */
	protected abstract String getValContent(String val);

	/**
	 * 得到显示标签结束内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午6:14:34
	 * @return 显示标签结束内容
	 */
	protected abstract String getShowEnd();

	/**
	 * 得到连接类型信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午7:46:10
	 * @param type 类型名
	 * @return 连接类型
	 */
	protected static OperateLinkTypeEnum getType(final String type) {
		for (final OperateLinkTypeEnum e : OperateLinkTypeEnum.values()) {
			if (e.name().equalsIgnoreCase(type)) {
				return e;
			}
		}
		return null;
	}
}
