/**
 * @author Weijie Xu
 * @dateTime 2015年5月8日 下午8:47:53
 */
package com.tfzzh.model.dao.tools;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tfzzh.exception.NotAvailableOperationModeException;
import com.tfzzh.model.bean.BaseDataBean;
import com.tfzzh.model.dao.tools.EntityInfoBean.FieldInfoBean;
import com.tfzzh.model.tools.DatabaseFactroy;

/**
 * 基础占位内容
 * 
 * @author Weijie Xu
 * @dateTime 2015年5月8日 下午8:47:53
 */
public abstract class QLLocation {

	/**
	 * 组合sql用字串
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月7日 下午8:05:35
	 * @param sql sql语句
	 * @param ic 索引计数器
	 */
	protected abstract void assemblySQL(StringBuilder sql, IndexCounter ic);

	/**
	 * 组合mongo用字串
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月9日 上午11:50:52
	 * @param mongo mongo条件字串
	 */
	protected abstract void assemblyMongo(StringBuilder mongo);

	/**
	 * 得到目标名字
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月1日 下午5:58:58
	 * @return 目标名字
	 */
	protected abstract String getTargetName();

	/**
	 * 得到目标路径名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年6月6日 下午7:44:45
	 * @return 目标路径名
	 */
	protected String getTargetPathName() {
		return this.getTargetName();
	}

	/**
	 * 设置对应声明变量的值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月8日 下午4:55:47
	 * @param ps 声明变量
	 * @param log log记录集
	 */
	protected void setPSValue(final PreparedStatement ps, final StringBuilder log) {
		throw new NotAvailableOperationModeException("The Error with SQL Location[" + this.getClass().getSimpleName() + "]...");
	}

	/**
	 * 符号占位，各种符号，加减号
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 上午10:51:08
	 */
	public static class ArithmeticSymbolLocation extends QLLocation {

		/**
		 * 运算符号
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月25日 下午3:35:19
		 */
		private final ArithmeticSymbolEnum as;

		/**
		 * @author Weijie Xu
		 * @dateTime 2015年4月24日 下午9:02:10
		 * @param as 运算符号
		 */
		protected ArithmeticSymbolLocation(final ArithmeticSymbolEnum as) {
			this.as = as;
		}

		/**
		 * 得到运算符号
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月25日 下午3:36:52
		 * @return the as
		 */
		public ArithmeticSymbolEnum getArithmeticSymbol() {
			return this.as;
		}

		/**
		 * 组合sql
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月9日 上午10:56:36
		 * @see com.tfzzh.model.dao.tools.QLLocation#assemblySQL(java.lang.StringBuilder, com.tfzzh.model.dao.tools.IndexCounter)
		 */
		@Override
		protected void assemblySQL(final StringBuilder sql, final IndexCounter ic) {
			// 因为是运算符号，所以只需要加入符号内容
			sql.append(this.as.getSQLText());
		}

		/**
		 * 组合mongo用字串
		 * 
		 * @author tfzzh
		 * @dateTime 2016年12月14日 上午11:26:35
		 * @param mongo mongo条件字串
		 * @see com.tfzzh.model.dao.tools.QLLocation#assemblyMongo(java.lang.StringBuilder)
		 */
		@Override
		protected void assemblyMongo(final StringBuilder mongo) {
			mongo.append(this.as.getMongoText());
		}

		/**
		 * 得到目标名字
		 * 
		 * @author Weijie Xu
		 * @dateTime 2017年6月1日 下午6:08:22
		 * @return 目标名字
		 * @see com.tfzzh.model.dao.tools.QLLocation#getTargetName()
		 */
		@Override
		protected String getTargetName() {
			return this.as.name();
		}
	}

	/**
	 * 值占位，针对具体的数值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 上午10:52:21
	 */
	public static class ValueLocation extends QLLocation {

		/**
		 * 所相关的SQL组合相关数据对象
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月8日 下午8:51:52
		 */
		private final QLBean qlb;

		/**
		 * 相关索引位
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月8日 下午5:00:37
		 */
		private int ind = -1;

		/**
		 * 对应的值
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月25日 下午3:36:14
		 */
		private final Object value;

		/**
		 * 值对应数据库类型
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月8日 下午5:19:39
		 */
		private final FieldType valueType;

		/**
		 * @author Weijie Xu
		 * @dateTime 2015年4月25日 下午3:36:11
		 * @param qlb 所相关的SQL组合相关数据对象
		 * @param value 对应的值
		 * @param target 所相关字段
		 */
		protected ValueLocation(final QLBean qlb, final Object value, final FieldLocation<?> target) {
			this.qlb = qlb;
			this.value = value;
			switch (qlb.getDatabaseType()) {
			case Mongo:
				if (null != target) {
					this.valueType = target.fib.getType();
				} else {
					this.valueType = DatabaseFactroy.getMongoFieldType(value);
				}
				break;
			default:
				if (null != target) {
					this.valueType = target.fib.getType();
				} else {
					this.valueType = DatabaseFactroy.getSqlFieldType(value);
				}
				break;
			}
		}

		/**
		 * @author tfzzh
		 * @dateTime 2016年9月21日 下午5:31:00
		 * @param qlb 所相关的SQL组合相关数据对象
		 * @param value 对应的值
		 * @param target 所相关字段
		 * @param ic 计数控制器
		 */
		protected ValueLocation(final QLBean qlb, final Object value, final FieldLocation<?> target, final IndexCounter ic) {
			this.qlb = qlb;
			this.value = value;
			switch (qlb.getDatabaseType()) {
			case Mongo:
				if (null != target) {
					this.valueType = target.fib.getType();
				} else {
					this.valueType = DatabaseFactroy.getMongoFieldType(value);
				}
				break;
			default:
				if (null != target) {
					this.valueType = target.fib.getType();
				} else {
					this.valueType = DatabaseFactroy.getSqlFieldType(value);
				}
				break;
			}
			this.ind = ic.getIndex();
		}

		/**
		 * 得到对应的值
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月25日 下午3:37:15
		 * @return the value
		 */
		public Object getValue() {
			return this.value;
		}

		/**
		 * 组合sql
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月9日 上午10:56:36
		 * @see com.tfzzh.model.dao.tools.QLLocation#assemblySQL(java.lang.StringBuilder, com.tfzzh.model.dao.tools.IndexCounter)
		 */
		@Override
		protected void assemblySQL(final StringBuilder sql, final IndexCounter ic) {
			sql.append('?');
			this.ind = ic.getIndex();
			this.qlb.addPsLocation(this);
		}

		/**
		 * 设置对应声明变量的值
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月8日 下午5:22:45
		 * @see com.tfzzh.model.dao.tools.QLLocation#setPSValue(java.sql.PreparedStatement, java.lang.StringBuilder)
		 */
		@Override
		protected void setPSValue(final PreparedStatement ps, final StringBuilder log) {
			this.valueType.putValueToPs(ps, this.value, this.ind);
			if (null != log) {
				log.append(this.value);
			}
		}

		/**
		 * 组合mongo用字串
		 * 
		 * @author tfzzh
		 * @dateTime 2016年12月14日 上午11:26:35
		 * @param mongo mongo条件字串
		 * @see com.tfzzh.model.dao.tools.QLLocation#assemblyMongo(java.lang.StringBuilder)
		 */
		@Override
		protected void assemblyMongo(final StringBuilder mongo) {
			this.valueType.putValueToDBObject(mongo, this.value);
		}

		/**
		 * 得到目标名字
		 * 
		 * @author Weijie Xu
		 * @dateTime 2017年6月1日 下午6:08:22
		 * @return 目标名字
		 * @see com.tfzzh.model.dao.tools.QLLocation#getTargetName()
		 */
		@Override
		protected String getTargetName() {
			return "value";
		}
	}

	/**
	 * 排序规则占位，asc，desc
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月25日 下午4:26:06
	 */
	public static class SortLocation extends QLLocation {

		/**
		 * 排序规则
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月25日 下午4:26:06
		 */
		private final SortEnum sort;

		/**
		 * @author Weijie Xu
		 * @dateTime 2015年4月25日 下午4:26:06
		 * @param sort 排序规则
		 */
		protected SortLocation(final SortEnum sort) {
			this.sort = sort;
		}

		/**
		 * 得到排序规则
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月25日 下午4:26:06
		 * @return the as
		 */
		public SortEnum getSort() {
			return this.sort;
		}

		/**
		 * 组合sql
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月9日 上午10:56:36
		 * @see com.tfzzh.model.dao.tools.QLLocation#assemblySQL(java.lang.StringBuilder, com.tfzzh.model.dao.tools.IndexCounter)
		 */
		@Override
		protected void assemblySQL(final StringBuilder sql, final IndexCounter ic) {
			// 因为是排序规则名，所以只需要加入内容
			sql.append(this.sort.getSQLText());
		}

		/**
		 * 组合mongo用字串
		 * 
		 * @author tfzzh
		 * @dateTime 2016年12月14日 上午11:26:35
		 * @param mongo mongo条件字串
		 * @see com.tfzzh.model.dao.tools.QLLocation#assemblyMongo(java.lang.StringBuilder)
		 */
		@Override
		protected void assemblyMongo(final StringBuilder mongo) {
			mongo.append(this.sort.getMongoValue());
		}

		/**
		 * 得到目标名字
		 * 
		 * @author Weijie Xu
		 * @dateTime 2017年6月1日 下午6:08:22
		 * @return 目标名字
		 * @see com.tfzzh.model.dao.tools.QLLocation#getTargetName()
		 */
		@Override
		protected String getTargetName() {
			return this.sort.name();
		}
	}

	/**
	 * 属性占位，表示一个具体的表字段
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 上午10:53:09
	 * @param <E> 实体数据
	 */
	public static class FieldLocation<E extends BaseDataBean> extends QLLocation {

		/**
		 * 所相关的表字段
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月24日 下午3:36:13
		 */
		private final EntityInfoBean<E>.FieldInfoBean fib;

		/**
		 * 所相关父级关系列表
		 * 
		 * @author Weijie Xu
		 * @dateTime 2017年6月6日 上午11:08:32
		 */
		@SuppressWarnings("rawtypes")
		private final List<FieldInfoBean> parent;

		/**
		 * @author Weijie Xu
		 * @dateTime 2017年6月6日 上午11:16:00
		 * @param fib 表字段
		 */
		protected FieldLocation(final EntityInfoBean<E>.FieldInfoBean fib) {
			this.fib = fib;
			this.parent = null;
			// super.getLocalScope().addLocation(this);
		}

		/**
		 * @author Weijie Xu
		 * @dateTime 2015年4月24日 下午3:36:15
		 * @param fib 表字段
		 * @param parent 所相关父级关系列表
		 */
		private FieldLocation(final EntityInfoBean<E>.FieldInfoBean fib, @SuppressWarnings("rawtypes") final List<FieldInfoBean> parent) {
			this.fib = fib;
			this.parent = parent;
			// super.getLocalScope().addLocation(this);
		}

		/**
		 * 因存在父级而创建新对象
		 * 
		 * @author Weijie Xu
		 * @dateTime 2017年6月6日 上午11:14:12
		 * @param parent 所相关父级对象
		 * @return 新创建的表字段
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		protected FieldLocation cloneWithParent(final List<FieldInfoBean> parent) {
			return new FieldLocation(this.fib, parent);
		}

		/**
		 * 得到相关的属性对象
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年4月24日 下午6:58:20
		 * @return 属性对象
		 */
		protected EntityInfoBean<E>.FieldInfoBean getFieldInfo() {
			return this.fib;
		}

		/**
		 * 得到相关的属性对象及向上路径
		 * 
		 * @author Weijie Xu
		 * @dateTime 2017年6月6日 下午3:34:04
		 * @return 相关的属性对象及向上路径
		 */
		@SuppressWarnings("rawtypes")
		protected List<FieldInfoBean> getFieldPath() {
			if (null == this.parent) {
				return Arrays.asList(this.fib);
			} else {
				final List<FieldInfoBean> fl = new ArrayList<>(this.parent.size() + 1);
				fl.addAll(this.parent);
				fl.add(this.fib);
				return fl;
			}
		}

		/**
		 * 组合sql
		 * 
		 * @author Weijie Xu
		 * @dateTime 2015年5月9日 上午10:56:36
		 * @see com.tfzzh.model.dao.tools.QLLocation#assemblySQL(java.lang.StringBuilder, com.tfzzh.model.dao.tools.IndexCounter)
		 */
		@Override
		protected void assemblySQL(final StringBuilder sql, final IndexCounter ic) {
			// 增加为字段表现
			sql.append(this.fib.getSQLFieldName());
		}

		/**
		 * 组合mongo用字串
		 * 
		 * @author tfzzh
		 * @dateTime 2016年12月14日 上午11:26:35
		 * @param mongo mongo条件字串
		 * @see com.tfzzh.model.dao.tools.QLLocation#assemblyMongo(java.lang.StringBuilder)
		 */
		@SuppressWarnings("rawtypes")
		@Override
		protected void assemblyMongo(final StringBuilder mongo) {
			// 增加为字段表现
			if (null != this.parent) {
				for (final FieldInfoBean f : this.parent) {
					mongo.append(f.getMongoFieldName()).append('.');
				}
			}
			mongo.append(this.fib.getMongoFieldName());
		}

		/**
		 * 得到目标名字
		 * 
		 * @author Weijie Xu
		 * @dateTime 2017年6月1日 下午6:08:22
		 * @return 目标名字
		 * @see com.tfzzh.model.dao.tools.QLLocation#getTargetName()
		 */
		@Override
		protected String getTargetName() {
			return this.fib.getMongoFieldName();
		}

		/**
		 * 得到目标路径名
		 * 
		 * @author Weijie Xu
		 * @dateTime 2017年6月6日 下午7:45:05
		 * @return 目标路径名
		 * @see com.tfzzh.model.dao.tools.QLLocation#getTargetPathName()
		 */
		@SuppressWarnings("rawtypes")
		@Override
		protected String getTargetPathName() {
			if (null == this.parent) {
				return this.fib.getMongoFieldName();
			} else {
				final StringBuilder sb = new StringBuilder();
				for (final FieldInfoBean f : this.parent) {
					sb.append(f.getMongoFieldName()).append('.');
				}
				sb.append(this.fib.getMongoFieldName());
				return sb.toString();
			}
		}
	}
}
