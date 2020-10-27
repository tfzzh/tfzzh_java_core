/**
 * @author Xu Weijie
 * @dateTime 2012-7-20 下午5:30:51
 */
package com.tfzzh.view.web.el;

/**
 * 操作按钮类型<br />
 * submit|image|reset|link<br />
 * 
 * @author Xu Weijie
 * @dateTime 2012-7-20 下午5:30:51
 */
public enum OperateButtonTypeEnum {

	/**
	 * 按钮提交
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午6:12:35
	 */
	Submit {

		@Override
		protected String getShowStart() {
			return "<input type='submit'";
		}

		@Override
		protected String getValContent(final String str) {
			return " value='" + str + "'";
		}

		@Override
		protected String getShowEnd() {
			return "/>";
		}
	},
	/**
	 * 图片提交
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午6:12:35
	 */
	Image {

		@Override
		protected String getShowStart() {
			return "<input type='image'";
		}

		@Override
		protected String getValContent(final String str) {
			return " src='" + str + "'";
		}

		@Override
		protected String getShowEnd() {
			return "/>";
		}
	},
	/**
	 * 重置
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午6:12:36
	 */
	Reset {

		@Override
		protected String getShowStart() {
			return "<input type='reset'";
		}

		@Override
		protected String getValContent(final String str) {
			return " value='" + str + "'";
		}

		@Override
		protected String getShowEnd() {
			return "/>";
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
	 * 得到按钮类型信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-20 下午7:46:10
	 * @param type 类型名
	 * @return 按钮类型
	 */
	protected static OperateButtonTypeEnum getType(final String type) {
		for (final OperateButtonTypeEnum e : OperateButtonTypeEnum.values()) {
			if (e.name().equalsIgnoreCase(type)) {
				return e;
			}
		}
		return null;
	}
}
