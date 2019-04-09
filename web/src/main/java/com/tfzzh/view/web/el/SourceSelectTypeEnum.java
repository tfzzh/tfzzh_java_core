/**
 * @author Weijie Xu
 * @dateTime 2012-7-23 上午2:52:25
 */
package com.tfzzh.view.web.el;

/**
 * 源数据选择类型<br />
 * select|checkbox|radio<br />
 * 
 * @author Weijie Xu
 * @dateTime 2012-7-23 上午2:52:25
 */
public enum SourceSelectTypeEnum {
	/**
	 * 单选下拉框
	 * 
	 * @author Weijie Xu
	 * @dateTime 2012-7-23 上午2:56:52
	 */
	Select {

		@Override
		protected String showHiddenData(final String att, final String baseAtt, final String value) {
			// 一个隐藏数据
			return "<input type='hidden' id='" + att + "' value='" + value + "'>";
		}

		@Override
		protected String showJsFunction() {
			return "selectShow";
		}
	},
	/**
	 * 多选盒子
	 * 
	 * @author Weijie Xu
	 * @dateTime 2012-7-23 上午2:56:52
	 */
	Checkbox {

		@Override
		protected String showHiddenData(final String att, final String baseAtt, final String value) {
			// 多个隐藏数据
			final StringBuilder sb = new StringBuilder();
			// 主数据
			sb.append("<input type='hidden' id='").append(att).append("' value=',").append(value).append(",'>");
			// 增数据
			sb.append("<input type='hidden' id='add").append(att).append("' name='add").append(baseAtt).append("' value=','>");
			// 减数据
			sb.append("<input type='hidden' id='dec").append(att).append("' name='dec").append(baseAtt).append("' value=','>");
			return sb.toString();
		}

		@Override
		protected String showJsFunction() {
			return "checkShow";
		}
	},
	/**
	 * 单选盒子
	 * 
	 * @author Weijie Xu
	 * @dateTime 2012-7-23 上午2:56:53
	 */
	Raido {

		@Override
		protected String showHiddenData(final String att, final String baseAtt, final String value) {
			// 一个隐藏数据
			return "<input type='hidden' id='" + att + "' value='" + value + "'>";
		}

		@Override
		protected String showJsFunction() {
			return "radioShow";
		}
	},
	/**
	 * @author Xu Weijie
	 * @dateTime 2012-7-26 下午12:42:30
	 */
	Show {

		@Override
		protected String showHiddenData(final String att, final String baseAtt, final String value) {
			// 一个隐藏数据
			return "<input type='hidden' id='" + att + "' value='" + value + "'>";
		}

		@Override
		protected String showJsFunction() {
			return null;
		}
	};

	/**
	 * 显示隐藏数据
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-23 上午11:22:28
	 * @param att 名字
	 * @param baseAtt 组合用基础名字
	 * @param value 值
	 * @return 显示用内容
	 */
	protected abstract String showHiddenData(String att, String baseAtt, String value);

	/**
	 * 显示控制js
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-23 下午1:32:32
	 * @return 显示用内容
	 */
	protected abstract String showJsFunction();

	/**
	 * 得到源数据选择类型信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2012-7-23 上午2:58:17
	 * @param type 源数据选择类型名
	 * @return 源数据选择类型
	 */
	public static SourceSelectTypeEnum getType(final String type) {
		for (final SourceSelectTypeEnum e : SourceSelectTypeEnum.values()) {
			if (e.name().equalsIgnoreCase(type)) {
				return e;
			}
		}
		return null;
	}
}
