/**
 * @author Weijie Xu
 * @dateTime 2015年4月17日 下午8:05:29
 */
package com.tfzzh.model.tools;

/**
 * 数据分页信息
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月17日 下午8:05:29
 */
public class PagerankBean {

	/**
	 * 当前页数，从1开始<br />
	 * 默认为1<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午8:06:20
	 */
	private int page;

	/**
	 * 页面数据量，一定大于0<br />
	 * 默认为20<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午8:06:26
	 */
	private int pageSize;

	/**
	 * 最大页数
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午8:06:27
	 */
	private int maxPage;

	/**
	 * 总数据条数
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午8:06:28
	 */
	private int sum;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午8:08:00
	 */
	public PagerankBean() {
		this(1, 20);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午8:08:04
	 * @param page 当前页数
	 */
	protected PagerankBean(final int page) {
		this(page, 20);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午8:08:02
	 * @param page 当前页数
	 * @param pageSize 页数据量
	 */
	public PagerankBean(final int page, final int pageSize) {
		if (page < 1) {
			this.page = 1;
		} else {
			this.page = page;
		}
		if (pageSize < 1) {
			this.pageSize = 20;
		} else {
			this.pageSize = pageSize;
		}
	}

	/**
	 * 得到当前页数，从1开始<br />
	 * 默认为1<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午8:12:32
	 * @return 当前页数
	 */
	public int getPage() {
		return this.page;
	}

	/**
	 * 得到页面数据量，一定大于0<br />
	 * 默认为20<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午8:11:24
	 * @return 当前页面数据量
	 */
	public int getSize() {
		return this.pageSize;
	}

	/**
	 * 得到最大页数
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午8:13:01
	 * @return 最大页数
	 */
	public int getMaxPage() {
		return this.maxPage;
	}

	/**
	 * 得到总数据量
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午8:13:02
	 * @return 总数据量
	 */
	public int getSum() {
		return this.sum;
	}

	/**
	 * 设置总数据量
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午8:22:31
	 * @param sum 总数据量
	 */
	public void setSum(final int sum) {
		if (sum <= 0) {
			// 特殊情况
			this.sum = 0;
			this.page = 1;
			this.maxPage = 1;
		} else {
			// 正常情况
			this.sum = sum;
			this.maxPage = ((this.sum - 1) / this.pageSize) + 1;
			if (this.page > this.maxPage) {
				this.page = this.maxPage;
			}
		}
	}

	/**
	 * 得到数据开始坐标位
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午8:18:23
	 * @return 数据开始坐标位
	 */
	public int getDataStartIndex() {
		return (this.page - 1) * this.getSize();
	}

	/**
	 * 得到数据结束坐标位
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月17日 下午8:21:39
	 * @return 数据结束坐标位
	 */
	public int getDataEndIndex() {
		final int end = this.getDataStartIndex() + this.getSize();
		if (end >= this.sum) {
			return this.sum;
		} else {
			return end;
		}
	}
}
