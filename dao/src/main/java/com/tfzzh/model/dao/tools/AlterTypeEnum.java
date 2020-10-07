/**
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午8:45:14
 */
package com.tfzzh.model.dao.tools;

import com.tfzzh.exception.NotAvailableOperationModeException;

/**
 * SQL用变更类型
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午8:45:14
 */
public enum AlterTypeEnum {

	/**
	 * 新增
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午8:47:28
	 */
	Add {

		@Override
		public StringBuilder getAlterField(final StringBuilder sb, final String fieldName, final String baseAlter, final String beforeFieldName) {
			sb.append("ADD COLUMN `").append(fieldName).append("` ").append(baseAlter);
			if (null == beforeFieldName) {
				sb.append(" FIRST");
			} else {
				sb.append(" AFTER `").append(beforeFieldName).append('`');
			}
			return sb;
		}

		@Override
		public StringBuilder getAlterIndex(final StringBuilder sb, final String indexName, final String alter) {
			return sb.append("ADD ").append(alter);
		}
	},
	/**
	 * 变更
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午8:47:29
	 */
	Change {

		@Override
		public StringBuilder getAlterField(final StringBuilder sb, final String fieldName, final String baseAlter, final String beforeFieldName) {
			sb.append("CHANGE COLUMN `").append(fieldName).append("` `").append(fieldName).append("` ").append(baseAlter);
			if (null == beforeFieldName) {
				sb.append(" FIRST");
			} else {
				sb.append(" AFTER `").append(beforeFieldName).append('`');
			}
			return sb;
		}

		@Override
		public StringBuilder getAlterIndex(final StringBuilder sb, final String indexName, final String alter) {
			throw new NotAvailableOperationModeException("Change Index is Not available...");
		}
	},
	/**
	 * 去除
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午8:47:29
	 */
	Drop {

		@Override
		public StringBuilder getAlterField(final StringBuilder sb, final String fieldName, final String baseAlter, final String beforeFieldName) {
			return sb.append("DROP COLUMN `").append(fieldName).append('`');
		}

		@Override
		public StringBuilder getAlterIndex(final StringBuilder sb, final String indexName, final String alter) {
			if (indexName.equalsIgnoreCase("PRIMARY")) {
				// 主键
				return sb.append("DROP PRIMARY KEY");
			} else {
				// 其他
				return sb.append("DROP INDEX `").append(indexName).append('`');
			}
		}
	};

	/**
	 * 得到字段类内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午8:56:04
	 * @param sb 组合用字串，会直接将增加的内容，放入进去
	 * @param fieldName 字段名
	 * @param baseAlter 基础的内容
	 * @param beforeFieldName 前一个字段的名字
	 * @return 同sb
	 */
	public abstract StringBuilder getAlterField(StringBuilder sb, String fieldName, String baseAlter, String beforeFieldName);

	/**
	 * 得到索引类内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月30日 下午5:35:14
	 * @param sb 组合用字串，会直接将增加的内容，放入进去
	 * @param indexName 索引名字
	 * @param alter 内容
	 * @return 同sb
	 */
	public abstract StringBuilder getAlterIndex(StringBuilder sb, String indexName, String alter);
}
