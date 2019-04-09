/**
 * @author Weijie Xu
 * @dateTime 2015年4月24日 下午8:26:39
 */
package com.tfzzh.model.dao.tools;

import com.tfzzh.model.dao.tools.QLLocation.ArithmeticSymbolLocation;

/**
 * 运算用符号
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月24日 下午8:26:39
 */
public enum ArithmeticSymbolEnum {
	/**
	 * 加法符号（针对数值）
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:05:49
	 */
	Addition {

		@Override
		public String getSQLText() {
			return "+";
		}

		@Override
		public String getMongoText() {
			return "+";
		}
	},
	/**
	 * 减法符号
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:05:50
	 */
	Subtraction {

		@Override
		public String getSQLText() {
			return "-";
		}

		@Override
		public String getMongoText() {
			return "-";
		}
	},
	/**
	 * 乘法符号
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:05:50
	 */
	Multiplication {

		@Override
		public String getSQLText() {
			return "*";
		}

		@Override
		public String getMongoText() {
			return "*";
		}
	},
	/**
	 * 除法符号
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:05:51
	 */
	Division {

		@Override
		public String getSQLText() {
			return "/";
		}

		@Override
		public String getMongoText() {
			return "/";
		}
	},
	/**
	 * 或运算
	 * 
	 * @author tfzzh
	 * @dateTime 2016年10月27日 下午3:16:14
	 */
	Or {

		@Override
		public String getSQLText() {
			return "|";
		}

		@Override
		public String getMongoText() {
			return "|";
		}
	},
	/**
	 * 与运算
	 * 
	 * @author tfzzh
	 * @dateTime 2016年10月27日 下午3:16:22
	 */
	And {

		@Override
		public String getSQLText() {
			return "&";
		}

		@Override
		public String getMongoText() {
			return "&";
		}
	},
	/**
	 * 异或运算
	 * 
	 * @author tfzzh
	 * @dateTime 2016年10月27日 下午3:16:27
	 */
	Xor {

		@Override
		public String getSQLText() {
			return "^";
		}

		@Override
		public String getMongoText() {
			return "^";
		}
	};

	/**
	 * 符号占位
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月9日 上午10:26:22
	 */
	private final ArithmeticSymbolLocation loc = new ArithmeticSymbolLocation(this);

	/**
	 * 得到符号占位
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月9日 上午10:26:40
	 * @return the loc
	 */
	public ArithmeticSymbolLocation getArithmeticSymbolLocation() {
		return this.loc;
	}

	/**
	 * 得到sql用文本
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午8:51:58
	 * @return sql用文本内容
	 */
	public abstract String getSQLText();

	/**
	 * 得到Mongo用文本
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月9日 下午4:51:46
	 * @return Mongo用文本内容
	 */
	public abstract String getMongoText();
}
