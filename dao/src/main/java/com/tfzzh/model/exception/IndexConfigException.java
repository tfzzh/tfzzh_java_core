/**
 * @author Weijie Xu
 * @dateTime 2015年4月27日 下午3:16:30
 */
package com.tfzzh.model.exception;

/**
 * 索引配置异常
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月27日 下午3:16:30
 */
public class IndexConfigException extends DaoRuntimeException {

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 下午3:39:21
	 */
	private static final long serialVersionUID = 4394158468590908526L;

	/**
	 * 表索引有重复的占位索引
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 下午3:16:43
	 * @param beanName 对象名
	 * @param fieldName1 字段1名
	 * @param fieldName2 字段2名
	 * @param indexName 索引名
	 * @param index 占位
	 */
	public IndexConfigException(final String beanName, final String fieldName1, final String fieldName2, final String indexName, final int index) {
		super(new StringBuilder(60).append("Table Index has Overlaying with [").append(beanName).append('[').append(fieldName1).append(':').append(fieldName2).append(']').append(indexName).append('-').append(index).append("]").toString());
	}

	/**
	 * 表索引有空占位
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 下午3:57:13
	 * @param beanName 对象名
	 * @param indexName 索引名
	 * @param nullIndex null位置索引
	 */
	public IndexConfigException(final String beanName, final String indexName, final int nullIndex) {
		super(new StringBuilder(60).append("Table Index has Empty Placeholder with [").append(beanName).append('.').append(indexName).append('-').append(nullIndex).append("]").toString());
	}

	/**
	 * 表字段有重复的占位索引
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月27日 下午8:59:32
	 * @param beanName 对象名
	 * @param fieldName1 字段1名
	 * @param fieldName2 字段2名
	 * @param index 占位
	 */
	public IndexConfigException(final String beanName, final String fieldName1, final String fieldName2, final int index) {
		super(new StringBuilder(60).append("Field has Overlaying with [").append(beanName).append('[').append(fieldName1).append(':').append(fieldName2).append("](").append(index).append(')').toString());
	}
}
