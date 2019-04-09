/**
 * @author 许纬杰
 * @datetime 2016年3月10日_下午1:55:32
 */
package com.tfzzh.tools;

/**
 * 内部索引
 * 
 * @author 许纬杰
 * @datetime 2016年3月10日_下午1:55:32
 */
public class InnerIndex {

	/**
	 * 索引值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月10日_下午1:55:46
	 */
	private int ind;

	/**
	 * @author 许纬杰
	 * @datetime 2016年3月10日_下午1:56:04
	 */
	public InnerIndex() {
		// 为了第一次可以在0位置
		this.ind = -1;
	}

	/**
	 * @author 许纬杰
	 * @datetime 2016年3月10日_下午1:56:26
	 * @param ind 初始索引
	 */
	public InnerIndex(final int ind) {
		this.ind = ind;
	}

	/**
	 * 增加索引值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月10日_下午1:57:02
	 * @return 被增加后的值
	 */
	public int add() {
		return ++this.ind;
	}

	/**
	 * 增加数量索引
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月20日_下午5:02:07
	 * @param add 待增加的值
	 * @return 被增加后的值
	 */
	public int addMore(final int add) {
		return this.ind += add;
	}

	/**
	 * 增加索引值，附带如果超过目标长度则重置数值功能
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月10日_下午2:00:31
	 * @param tarLength 目标长度
	 * @return 被增加后的值
	 */
	public int add(final int tarLength) {
		this.ind++;
		while (this.ind >= tarLength) {
			this.ind -= tarLength;
		}
		return this.ind;
	}

	/**
	 * 降低索引值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月16日_下午8:08:11
	 * @return 被降低后的值
	 */
	public int dec() {
		return --this.ind;
	}

	/**
	 * 降低数量索引
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月20日_下午5:02:38
	 * @param dec 待降低的值
	 * @return 被降低后的值
	 */
	public int decMore(final int dec) {
		return this.ind -= dec;
	}

	/**
	 * 重置索引值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月10日_下午1:57:31
	 */
	public void reset() {
		this.ind = 0;
	}

	/**
	 * 重置索引值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月16日_下午8:12:06
	 * @param newInd 新的索引值
	 */
	public void reset(final int newInd) {
		this.ind = newInd;
	}

	/**
	 * 得到索引值
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月10日_下午1:57:55
	 * @return the ind
	 */
	public int get() {
		return this.ind;
	}
}
