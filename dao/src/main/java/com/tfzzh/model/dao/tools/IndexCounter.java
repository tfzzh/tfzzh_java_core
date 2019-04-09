/**
 * @author Weijie Xu
 * @dateTime 2015年5月7日 下午8:08:03
 */
package com.tfzzh.model.dao.tools;

/**
 * 索引计数器
 * 
 * @author Weijie Xu
 * @dateTime 2015年5月7日 下午8:08:03
 */
public class IndexCounter {

	/**
	 * 索引位
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午8:10:20
	 */
	private int countInd = 1;

	/**
	 * 得到当前索引值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午8:10:42
	 * @return 当前索引值
	 */
	protected int getIndex() {
		return this.countInd++;
	}
}
