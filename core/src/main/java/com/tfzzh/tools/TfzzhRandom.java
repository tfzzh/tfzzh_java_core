/**
 * @author xuweijie
 * @dateTime 2011-12-30 下午12:36:31
 */
package com.tfzzh.tools;

import java.util.Random;

/**
 * 单例随机数对象
 * 
 * @author xuweijie
 * @dateTime 2011-12-30 下午12:36:31
 */
public class TfzzhRandom extends Random {

	/**
	 * @author xuweijie
	 * @dateTime 2012-1-4 上午10:12:38
	 */
	private static final long serialVersionUID = 6885317947719981614L;

	/**
	 * @author xuweijie
	 * @dateTime 2011-12-30 下午12:39:37
	 */
	private final static TfzzhRandom ran = new TfzzhRandom();

	/**
	 * @author xuweijie
	 * @dateTime 2011-12-30 下午12:39:34
	 */
	private TfzzhRandom() {
	}

	/**
	 * 得到对象唯一实例
	 * 
	 * @author xuweijie
	 * @dateTime 2011-12-30 下午12:39:32
	 * @return 唯一实例
	 */
	public static TfzzhRandom getInstance() {
		return TfzzhRandom.ran;
	}

	/**
	 * 得到随机百分随机数
	 * 
	 * @author xuweijie
	 * @dateTime 2011-12-31 下午1:19:38
	 * @return 100以内随机数
	 */
	public int next100() {
		return this.nextInt(100);
	}

	/**
	 * 得到随机千分随机数
	 * 
	 * @author xuweijie
	 * @dateTime 2011-12-31 下午1:19:39
	 * @return 1000以内随机数
	 */
	public int next1000() {
		return this.nextInt(1000);
	}

	/**
	 * 得到随机万分随机数
	 * 
	 * @author xuweijie
	 * @dateTime 2011-12-31 下午1:19:40
	 * @return 10000以内随机数
	 */
	public int next10000() {
		return this.nextInt(10000);
	}

	/**
	 * 进行二次随机
	 * 
	 * @author XuWeijie
	 * @datetime 2015年6月18日_下午2:48:09
	 * @param num 目标值
	 * @return 得到的值
	 */
	public int nextIntTwice(final int num) {
		final int r = this.nextInt(num) + 1;
		return this.nextInt(r);
	}

	/**
	 * 二次随机得到中值<br />
	 * 浮动范围：40，概率100000次170极致，上下限<br />
	 * 浮动范围：50，概率100000次110极致，上下限<br />
	 * 浮动范围：60，概率100000次80极致，上下限<br />
	 * 浮动范围：70，概率100000次60极致，上下限<br />
	 * 浮动范围：80，概率100000次43极致，上下限<br />
	 * 浮动范围：90，概率100000次35极致，上下限<br />
	 * 浮动范围：100，概率100000次27极致，上下限<br />
	 * 浮动范围：150，概率100000次13极致，上下限<br />
	 * 浮动范围：200，概率100000次6极致，上下限<br />
	 * 浮动范围：300，概率100000次3极致，上下限<br />
	 * 浮动范围：400，概率100000次2极致，上下限<br />
	 * 浮动范围：500，概率100000次1极致，上下限<br />
	 * 
	 * @author XuWeijie
	 * @datetime 2015年6月18日_下午4:19:24
	 * @param num 目标范围
	 * @return 得到的值
	 */
	public int nextIntTwiceMidValue(final int num) {
		int r = this.nextInt(num + 1);
		r = this.nextInt(r + 1);
		return this.getMidValue(num, r);
	}

	/**
	 * 根据范围，得到中值
	 * 
	 * @author XuWeijie
	 * @datetime 2015年6月18日_下午4:45:19
	 * @param num 目标范围
	 * @param r 中值计算参数
	 * @return 目标中值
	 */
	private int getMidValue(final int num, final int r) {
		if ((r & 1) == 1) {
			return (num / 2) + 1 + (r / 2);
		} else {
			return (num / 2) - (r / 2);
		}
	}
}
