/**
 * @author Xu Weijie
 * @dateTime 2012-11-6 下午5:53:02
 */
package com.tfzzh.tools;

/**
 * 可对内容进行重新加载的常量配置
 * 
 * @author Xu Weijie
 * @dateTime 2012-11-6 下午5:53:02
 */
public abstract class ChangeConstants {

	/**
	 * 进行数据更新
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-11-6 下午5:54:52
	 */
	public void changeData() {
		this.releaseData();
	}

	/**
	 * 刷新数据
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-11-6 下午5:54:25
	 */
	protected abstract void releaseData();
}
