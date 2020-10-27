/**
 * @author Xu Weijie
 * @dateTime 2012-7-19 下午10:37:43
 */
package com.tfzzh.view.web.el;

/**
 * 信息类型 msg
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-19 下午10:37:43
 */
enum MsgTypeEnum {

	/**
	 * 纯粹为了显示
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-21 下午4:58:05
	 */
	Msg {

		@Override
		protected String getShowStart() {
			return "<span";
		}

		@Override
		protected String getValContent(final Object val) {
			return ">" + val.toString();
		}

		@Override
		protected String getValContent() {
			return ">";
		}

		@Override
		protected String getShowEnd() {
			return "</span>";
		}
	};

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
	protected static MsgTypeEnum getType(final String type) {
		for (final MsgTypeEnum e : MsgTypeEnum.values()) {
			if (e.name().equalsIgnoreCase(type)) {
				return e;
			}
		}
		return Msg;
	}
}
