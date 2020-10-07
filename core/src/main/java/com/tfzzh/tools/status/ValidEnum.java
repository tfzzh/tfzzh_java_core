/**
 * @author TFZZH
 * @dateTime 2011-10-16 下午12:09:08
 */
package com.tfzzh.tools.status;

/**
 * 有效性状态<br />
 * 值状态：1，有效；0，无效；<br />
 * 
 * @author TFZZH
 * @dateTime 2011-10-16 下午12:09:08
 */
public enum ValidEnum {

	/**
	 * 有效的<br />
	 * value：1<br />
	 * 
	 * @author TFZZH
	 * @dateTime 2011-10-16 下午12:12:04
	 */
	VALID {

		@Override
		public short getValue() {
			return 1;
		}
	},
	/**
	 * 无效的<br />
	 * value：2<br />
	 * 
	 * @author TFZZH
	 * @dateTime 2011-10-16 下午12:12:05
	 */
	INVALID {

		@Override
		public short getValue() {
			return 0;
		}
	};

	/**
	 * 得到状态值
	 * 
	 * @author TFZZH
	 * @dateTime 2011-10-21 上午10:29:03
	 * @return get value
	 */
	public abstract short getValue();
}
