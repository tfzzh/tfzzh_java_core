/**
 * @author Weijie Xu
 * @dateTime 2015年5月8日 下午7:58:26
 */
package com.tfzzh.model.dao.tools;

/**
 * 关系符号
 * 
 * @author Weijie Xu
 * @dateTime 2015年5月8日 下午7:58:26
 */
public enum RelationalSymbolEnum {

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年5月8日 下午8:06:56
	 */
	And {

		@Override
		public String getSQLText() {
			return " AND ";
		}
	},
	/**
	 * @author Weijie Xu
	 * @dateTime 2015年5月8日 下午8:06:58
	 */
	Or {

		@Override
		public String getSQLText() {
			return " OR ";
		}
	};

	/**
	 * 得到SQL用内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月8日 下午8:00:38
	 * @return SQL用内容
	 */
	public abstract String getSQLText();
}
