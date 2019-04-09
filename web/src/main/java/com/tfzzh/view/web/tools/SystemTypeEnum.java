/**
 * @author XuWeijie
 * @datetime 2015年11月19日_下午8:39:18
 */
package com.tfzzh.view.web.tools;

/**
 * 系统类型
 * 
 * @author XuWeijie
 * @datetime 2015年11月19日_下午8:39:18
 */
public enum SystemTypeEnum {
	/**
	 * windows桌面系统
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:40:12
	 */
	Windows(11) {

		@Override
		public boolean isMoblie() {
			return false;
		}
	},
	/**
	 * android系统
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:40:12
	 */
	Android(21) {

		@Override
		public boolean isMoblie() {
			return true;
		}
	},
	/**
	 * 苹果手机系统
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:40:13
	 */
	Ios(32) {

		@Override
		public boolean isMoblie() {
			return true;
		}
	},
	/**
	 * windows手机
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:40:14
	 */
	WindowsPhone(12) {

		@Override
		public boolean isMoblie() {
			return true;
		}
	},
	/**
	 * 苹果桌面系统
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:40:24
	 */
	Os(31) {

		@Override
		public boolean isMoblie() {
			return false;
		}
	},
	/**
	 * 自定义
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月22日_下午9:14:19
	 */
	Custom(98) {

		@Override
		public boolean isMoblie() {
			return false;
		}
	},
	/**
	 * 未知的系统
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:45:32
	 */
	Unknow(99) {

		@Override
		public boolean isMoblie() {
			return false;
		}
	};

	/**
	 * 类型值
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:42:14
	 */
	private final int val;

	/**
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:43:19
	 * @param val 类型值
	 */
	SystemTypeEnum(final int val) {
		this.val = val;
	}

	/**
	 * 得到类型值
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:43:03
	 * @return the val
	 */
	public int getTypeValue() {
		return this.val;
	}

	/**
	 * 是否移动系统
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月26日_下午12:29:30
	 * @return true，是移动系统；
	 */
	public abstract boolean isMoblie();

	/**
	 * 得到默认的系统类型
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午9:18:21
	 * @return 未知的系统
	 */
	public static SystemTypeEnum getDefaultType() {
		return Unknow;
	}

	/**
	 * 得到类型
	 * 
	 * @author XuWeijie
	 * @datetime 2015年11月19日_下午8:46:03
	 * @param type 类型值
	 * @return 对应的系统类型
	 */
	public static SystemTypeEnum getType(final int type) {
		for (final SystemTypeEnum e : SystemTypeEnum.values()) {
			if (e.val == type) {
				return e;
			}
		}
		return Unknow;
	}
}
