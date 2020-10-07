/**
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午2:09:06
 */
package com.tfzzh.model.dao.tools;

/**
 * 字段相关Key索引类型
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午2:09:06
 */
public enum KeyIndexEnum {

	/**
	 * 主键
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午2:12:11
	 */
	Primary(500) {

		// @Override
		// public String getTablePrefixd() {
		// return "PRIMARY KEY ";
		// }
		@Override
		public String getAlterPrefixd() {
			return "PRIMARY KEY ";
		}

		@Override
		public String getNamePrefixd() {
			return null;
		}

		@Override
		public SortEnum getSortType(final SortEnum type) {
			return type;
		}
	},
	/**
	 * 唯一索引
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午2:12:25
	 */
	Unique(50) {

		// @Override
		// public String getTablePrefixd() {
		// return "UNIQUE KEY ";
		// }
		@Override
		public String getAlterPrefixd() {
			return "UNIQUE INDEX ";
		}

		@Override
		public String getNamePrefixd() {
			return "uni_";
		}

		@Override
		public SortEnum getSortType(final SortEnum type) {
			return type;
		}
	},
	/**
	 * 一般的索引
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午2:13:00
	 */
	Normal(10) {

		// @Override
		// public String getTablePrefixd() {
		// return "KEY ";
		// }
		@Override
		public String getAlterPrefixd() {
			return "INDEX ";
		}

		@Override
		public String getNamePrefixd() {
			return "ind_";
		}

		@Override
		public SortEnum getSortType(final SortEnum type) {
			return type;
		}
	},
	/**
	 * 全文索引，在like时有效
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午2:28:16
	 */
	FullText(5) {

		// @Override
		// public String getTablePrefixd() {
		// return "FULLTEXT KEY ";
		// }
		@Override
		public String getAlterPrefixd() {
			return "FULLTEXT INDEX ";
		}

		@Override
		public String getNamePrefixd() {
			return "ft_";
		}

		@Override
		public SortEnum getSortType(final SortEnum type) {
			return null;
		}
	};

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午2:29:42
	 */
	private final int weight;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午2:29:43
	 * @param weight 条件排序用权重值
	 */
	KeyIndexEnum(final int weight) {
		this.weight = weight;
	}

	/**
	 * 得到权重
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午2:29:52
	 * @return the weight
	 */
	public int getWeight() {
		return this.weight;
	}

	// /**
	// * 得到建表时用前缀
	// *
	// * @author Weijie Xu
	// * @dateTime 2015年5月4日 上午11:20:54
	// * @return 建表时用前缀
	// */
	// public abstract String getTablePrefixd();
	/**
	 * 得到Alter用前缀
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 下午7:47:06
	 * @return Alter用前缀
	 */
	public abstract String getAlterPrefixd();

	/**
	 * 得到名字前缀<br />
	 * 如果为null，表示不需要名称，暂且如此设定<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 下午7:41:16
	 * @return 名字前缀
	 */
	public abstract String getNamePrefixd();

	/**
	 * 得到排序类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月30日 下午3:43:29
	 * @param type 当前排序类型
	 * @return 实际排序类型
	 */
	public abstract SortEnum getSortType(SortEnum type);
}
