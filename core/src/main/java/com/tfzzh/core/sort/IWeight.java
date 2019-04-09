/**
 * @author Weijie Xu
 * @dateTime 2014年6月14日 上午11:37:07
 */
package com.tfzzh.core.sort;

/**
 * 权重状态接口
 * 
 * @author Weijie Xu
 * @dateTime 2014年6月14日 上午11:37:07
 */
public interface IWeight {

	/**
	 * 得到对应键值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月16日 上午9:34:01
	 * @return 对应键值
	 */
	int getKey();

	/**
	 * 得到参考值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月14日 上午11:37:54
	 * @return 参考值
	 */
	int getReferenceValue();
}
