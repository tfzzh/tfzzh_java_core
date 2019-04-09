/**
 * @author Weijie Xu
 * @dateTime 2015年4月21日 下午7:45:05
 */
package com.tfzzh.model.exception;

/**
 * sql相关批量提交异常
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月21日 下午7:45:05
 */
public class SqlBatchException extends DaoRuntimeException {

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:56:50
	 */
	private static final long serialVersionUID = 4527890015409813413L;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:45:23
	 * @param poolName 池名称
	 * @param count 等待提交的数据量
	 */
	public SqlBatchException(final String poolName, final int count) {
		super(new StringBuilder(40).append("in ConnectionPool>[").append(poolName).append("] has [").append(count).append("] datas wait to Batch").toString());
	}
}
