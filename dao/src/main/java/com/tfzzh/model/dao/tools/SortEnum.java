/**
 * @author Weijie Xu
 * @dateTime 2015年4月25日 下午4:14:49
 */
package com.tfzzh.model.dao.tools;

import com.tfzzh.model.dao.tools.QLLocation.SortLocation;

/**
 * @author Weijie Xu
 * @dateTime 2015年4月25日 下午4:14:49
 */
public enum SortEnum {

	/**
	 * 升序排列
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午4:23:43
	 */
	ASC {

		@Override
		public String getIndexNameSuf() {
			return "_a";
		}

		@Override
		public String getSQLText() {
			return "";
		}

		@Override
		public String getMongoValue() {
			return "1";
		}
	},
	/**
	 * 升序排列，按数值模型的排序
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午4:23:44
	 */
	ASC_ABS {

		@Override
		public String getIndexNameSuf() {
			return "_a";
		}

		@Override
		public String getSQLText() {
			return "";
		}

		@Override
		public String getMongoValue() {
			return "1";
		}
	},
	/**
	 * 降序排列
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午4:23:45
	 */
	DESC {

		@Override
		public String getIndexNameSuf() {
			return "_d";
		}

		@Override
		public String getSQLText() {
			return " DESC";
		}

		@Override
		public String getMongoValue() {
			return "-1";
		}
	},
	/**
	 * 降序排列，按数值模型的排序
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午4:23:45
	 */
	DESC_ABS {

		@Override
		public String getIndexNameSuf() {
			return "_d";
		}

		@Override
		public String getSQLText() {
			return " DESC";
		}

		@Override
		public String getMongoValue() {
			return "-1";
		}
	};

	/**
	 * 排序占位
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月9日 上午10:30:30
	 */
	private final SortLocation loc = new SortLocation(this);

	/**
	 * 得到排序占位
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月9日 上午10:30:47
	 * @return the loc
	 */
	public SortLocation getSortLocation() {
		return this.loc;
	}

	/**
	 * 得到索引用名后缀
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月30日 下午4:50:28
	 * @return 索引用名后缀
	 */
	public abstract String getIndexNameSuf();

	/**
	 * 得到SQL用索引字段后缀
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月30日 下午4:52:14
	 * @return 索引字段后缀
	 */
	public abstract String getSQLText();

	/**
	 * 得到Mongo用索引值
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月9日 下午4:49:18
	 * @return 索引值
	 */
	public abstract String getMongoValue();
}
