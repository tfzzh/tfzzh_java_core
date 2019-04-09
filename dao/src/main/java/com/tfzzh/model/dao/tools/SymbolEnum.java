/**
 * @author Weijie Xu
 * @dateTime 2015年4月24日 下午8:03:07
 */
package com.tfzzh.model.dao.tools;

import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.tfzzh.exception.NotAvailableOperationModeException;
import com.tfzzh.model.dao.tools.QLLocation.FieldLocation;
import com.tfzzh.model.dao.tools.QLLocation.ValueLocation;

/**
 * 操作符号<br />
 * mongo，暂时还支持对应字段值的比较<br />
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月24日 下午8:03:07
 */
public enum SymbolEnum {
	/**
	 * 等于
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:05:46
	 */
	Equal {

		@Override
		public void assemblyLocationSQL(final FieldLocation<?> fl, final StringBuilder sql, final IndexCounter ic, final List<QLLocation> locs) {
			fl.assemblySQL(sql, ic);
			sql.append('=');
			for (final QLLocation l : locs) {
				l.assemblySQL(sql, ic);
			}
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final BasicDBObject bo, final List<QLLocation> locs) {
			final StringBuilder ks = new StringBuilder();
			fl.assemblyMongo(ks);
			if (locs.size() == 1) {
				final QLLocation ql = locs.get(0);
				if (ql instanceof ValueLocation) {
					final ValueLocation vl = (ValueLocation) ql;
					bo.append(ks.toString(), vl.getValue());
					return;
				}
			}
			final StringBuilder vs = new StringBuilder();
			for (final QLLocation l : locs) {
				l.assemblyMongo(vs);
			}
			bo.append(ks.toString(), vs.toString());
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final StringBuilder sb, final List<QLLocation> locs) {
			// if (sb.length() > 4) {
			// sb.append(',');
			// }
			// 放入字段
			// sb.append("{\"");
			sb.append("\"");
			fl.assemblyMongo(sb);
			sb.append("\":");
			// 放入值
			if (locs.size() == 1) {
				locs.get(0).assemblyMongo(sb);
			} else {
				for (final QLLocation ql : locs) {
					ql.assemblyMongo(sb);
				}
			}
			// sb.append('}');
		}

		@Override
		protected String getMongoSymbol(final boolean alone) {
			throw new NotAvailableOperationModeException(SymbolEnum.class.getSimpleName() + "." + this.name() + " not method getMongoSymbol...");
		}
	},
	/**
	 * 不等
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:05:47
	 */
	UnEqual {

		@Override
		public void assemblyLocationSQL(final FieldLocation<?> fl, final StringBuilder sql, final IndexCounter ic, final List<QLLocation> locs) {
			fl.assemblySQL(sql, ic);
			sql.append("!=");
			for (final QLLocation l : locs) {
				l.assemblySQL(sql, ic);
			}
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final BasicDBObject bo, final List<QLLocation> locs) {
			final StringBuilder ks = new StringBuilder();
			fl.assemblyMongo(ks);
			if (locs.size() == 1) {
				final QLLocation ql = locs.get(0);
				if (ql instanceof ValueLocation) {
					final ValueLocation vl = (ValueLocation) ql;
					bo.append(ks.toString(), new BasicDBObject(this.getMongoSymbol(), vl.getValue()));
					return;
				}
			}
			final StringBuilder vs = new StringBuilder();
			for (final QLLocation l : locs) {
				l.assemblyMongo(vs);
			}
			bo.append(ks.toString(), new BasicDBObject(this.getMongoSymbol(), vs.toString()));
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final StringBuilder sb, final List<QLLocation> locs) {
			// if (sb.length() > 4) {
			// sb.append(',');
			// }
			// 放入字段
			// sb.append("{\"");
			sb.append("\"");
			fl.assemblyMongo(sb);
			sb.append("\":{");
			// 放入值
			if (locs.size() == 1) {
				sb.append(this.getMongoSymbol(false));
				locs.get(0).assemblyMongo(sb);
			} else {
				boolean isFirst = true;
				for (final QLLocation ql : locs) {
					if (isFirst) {
						isFirst = false;
					} else {
						sb.append(",");
					}
					sb.append(this.getMongoSymbol(false));
					ql.assemblyMongo(sb);
				}
			}
			sb.append("}");
			// sb.append("}}");
		}

		@Override
		protected String getMongoSymbol(final boolean alone) {
			if (alone) {
				return "$ne";
			} else {
				return "$ne:";
			}
		}
	},
	/**
	 * 大于
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:05:47
	 */
	MoreThan {

		@Override
		public void assemblyLocationSQL(final FieldLocation<?> fl, final StringBuilder sql, final IndexCounter ic, final List<QLLocation> locs) {
			fl.assemblySQL(sql, ic);
			sql.append('>');
			for (final QLLocation l : locs) {
				l.assemblySQL(sql, ic);
			}
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final BasicDBObject bo, final List<QLLocation> locs) {
			final StringBuilder ks = new StringBuilder();
			fl.assemblyMongo(ks);
			if (locs.size() == 1) {
				final QLLocation ql = locs.get(0);
				if (ql instanceof ValueLocation) {
					final ValueLocation vl = (ValueLocation) ql;
					bo.append(ks.toString(), new BasicDBObject(this.getMongoSymbol(), vl.getValue()));
					return;
				}
			}
			final StringBuilder vs = new StringBuilder();
			for (final QLLocation l : locs) {
				l.assemblyMongo(vs);
			}
			bo.append(ks.toString(), new BasicDBObject(this.getMongoSymbol(), vs.toString()));
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final StringBuilder sb, final List<QLLocation> locs) {
			// if (sb.length() > 4) {
			// sb.append(',');
			// }
			// 放入字段
			// sb.append("{\"");
			sb.append("\"");
			fl.assemblyMongo(sb);
			sb.append("\":{");
			// 放入值
			if (locs.size() == 1) {
				sb.append(this.getMongoSymbol(false));
				locs.get(0).assemblyMongo(sb);
			} else {
				boolean isFirst = true;
				for (final QLLocation ql : locs) {
					if (isFirst) {
						isFirst = false;
					} else {
						sb.append(",");
					}
					sb.append(this.getMongoSymbol(false));
					ql.assemblyMongo(sb);
				}
			}
			sb.append("}");
			// sb.append("}}");
		}

		@Override
		protected String getMongoSymbol(final boolean alone) {
			if (alone) {
				return "$gt";
			} else {
				return "$gt:";
			}
		}
	},
	/**
	 * 大于等于
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:05:48
	 */
	MoreEqual {

		@Override
		public void assemblyLocationSQL(final FieldLocation<?> fl, final StringBuilder sql, final IndexCounter ic, final List<QLLocation> locs) {
			fl.assemblySQL(sql, ic);
			sql.append(">=");
			for (final QLLocation l : locs) {
				l.assemblySQL(sql, ic);
			}
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final BasicDBObject bo, final List<QLLocation> locs) {
			final StringBuilder ks = new StringBuilder();
			fl.assemblyMongo(ks);
			if (locs.size() == 1) {
				final QLLocation ql = locs.get(0);
				if (ql instanceof ValueLocation) {
					final ValueLocation vl = (ValueLocation) ql;
					bo.append(ks.toString(), new BasicDBObject(this.getMongoSymbol(), vl.getValue()));
					return;
				}
			}
			final StringBuilder vs = new StringBuilder();
			for (final QLLocation l : locs) {
				l.assemblyMongo(vs);
			}
			bo.append(ks.toString(), new BasicDBObject(this.getMongoSymbol(), vs.toString()));
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final StringBuilder sb, final List<QLLocation> locs) {
			// if (sb.length() > 4) {
			// sb.append(',');
			// }
			// 放入字段
			// sb.append("{\"");
			sb.append("\"");
			fl.assemblyMongo(sb);
			sb.append("\":{");
			// 放入值
			if (locs.size() == 1) {
				sb.append(this.getMongoSymbol(false));
				locs.get(0).assemblyMongo(sb);
			} else {
				boolean isFirst = true;
				for (final QLLocation ql : locs) {
					if (isFirst) {
						isFirst = false;
					} else {
						sb.append(",");
					}
					sb.append(this.getMongoSymbol(false));
					ql.assemblyMongo(sb);
				}
			}
			sb.append("}");
			// sb.append("}}");
		}

		@Override
		protected String getMongoSymbol(final boolean alone) {
			if (alone) {
				return "$gte";
			} else {
				return "$gte:";
			}
		}
	},
	/**
	 * 小于
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:05:48
	 */
	LessThan {

		@Override
		public void assemblyLocationSQL(final FieldLocation<?> fl, final StringBuilder sql, final IndexCounter ic, final List<QLLocation> locs) {
			fl.assemblySQL(sql, ic);
			sql.append('<');
			for (final QLLocation l : locs) {
				l.assemblySQL(sql, ic);
			}
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final BasicDBObject bo, final List<QLLocation> locs) {
			final StringBuilder ks = new StringBuilder();
			fl.assemblyMongo(ks);
			if (locs.size() == 1) {
				final QLLocation ql = locs.get(0);
				if (ql instanceof ValueLocation) {
					final ValueLocation vl = (ValueLocation) ql;
					bo.append(ks.toString(), new BasicDBObject(this.getMongoSymbol(), vl.getValue()));
					return;
				}
			}
			final StringBuilder vs = new StringBuilder();
			for (final QLLocation l : locs) {
				l.assemblyMongo(vs);
			}
			bo.append(ks.toString(), new BasicDBObject(this.getMongoSymbol(), vs.toString()));
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final StringBuilder sb, final List<QLLocation> locs) {
			// if (sb.length() > 4) {
			// sb.append(',');
			// }
			// 放入字段
			// sb.append("{\"");
			sb.append("\"");
			fl.assemblyMongo(sb);
			sb.append("\":{");
			// 放入值
			if (locs.size() == 1) {
				sb.append(this.getMongoSymbol(false));
				locs.get(0).assemblyMongo(sb);
			} else {
				boolean isFirst = true;
				for (final QLLocation ql : locs) {
					if (isFirst) {
						isFirst = false;
					} else {
						sb.append(",");
					}
					sb.append(this.getMongoSymbol(false));
					ql.assemblyMongo(sb);
				}
			}
			sb.append("}");
			// sb.append("}}");
		}

		@Override
		protected String getMongoSymbol(final boolean alone) {
			if (alone) {
				return "$lt";
			} else {
				return "$lt:";
			}
		}
	},
	/**
	 * 小于等于
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:05:49
	 */
	LessEqual {

		@Override
		public void assemblyLocationSQL(final FieldLocation<?> fl, final StringBuilder sql, final IndexCounter ic, final List<QLLocation> locs) {
			fl.assemblySQL(sql, ic);
			sql.append("<=");
			for (final QLLocation l : locs) {
				l.assemblySQL(sql, ic);
			}
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final BasicDBObject bo, final List<QLLocation> locs) {
			final StringBuilder ks = new StringBuilder();
			fl.assemblyMongo(ks);
			if (locs.size() == 1) {
				final QLLocation ql = locs.get(0);
				if (ql instanceof ValueLocation) {
					final ValueLocation vl = (ValueLocation) ql;
					bo.append(ks.toString(), new BasicDBObject(this.getMongoSymbol(), vl.getValue()));
					return;
				}
			}
			final StringBuilder vs = new StringBuilder();
			for (final QLLocation l : locs) {
				l.assemblyMongo(vs);
			}
			bo.append(ks.toString(), new BasicDBObject(this.getMongoSymbol(), vs.toString()));
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final StringBuilder sb, final List<QLLocation> locs) {
			// if (sb.length() > 4) {
			// sb.append(',');
			// }
			// 放入字段
			// sb.append("{\"");
			sb.append("\"");
			fl.assemblyMongo(sb);
			sb.append("\":{");
			// 放入值
			if (locs.size() == 1) {
				sb.append(this.getMongoSymbol(false));
				locs.get(0).assemblyMongo(sb);
			} else {
				boolean isFirst = true;
				for (final QLLocation ql : locs) {
					if (isFirst) {
						isFirst = false;
					} else {
						sb.append(",");
					}
					sb.append(this.getMongoSymbol(false));
					ql.assemblyMongo(sb);
				}
			}
			sb.append("}");
			// sb.append("}}");
		}

		@Override
		protected String getMongoSymbol(final boolean alone) {
			if (alone) {
				return "$lte";
			} else {
				return "$lte:";
			}
		}
	},
	/**
	 * 针对字符串的叠加，其后应该都是各种value与field<br />
	 * 主要用在更新部分<br />
	 * 条件部分也可以用，但实际情况，应该使用的甚少<br />
	 * CONCAT(region_name,store_name)<br />
	 * region_name || ' ' || store_name<br />
	 * region_name + ' ' + store_name<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午9:06:54
	 */
	Concat {

		@Override
		public void assemblyLocationSQL(final FieldLocation<?> fl, final StringBuilder sql, final IndexCounter ic, final List<QLLocation> locs) {
			fl.assemblySQL(sql, ic);
			sql.append("=CONCAT(");
			boolean isFirst = true;
			for (final QLLocation l : locs) {
				if (isFirst) {
					isFirst = false;
				} else {
					sql.append(',');
				}
				l.assemblySQL(sql, ic);
			}
			sql.append(")");
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final BasicDBObject bo, final List<QLLocation> locs) {
			throw new NotAvailableOperationModeException(SymbolEnum.class.getSimpleName() + "." + this.name() + " not method assemblyLocationMongo...");
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final StringBuilder sb, final List<QLLocation> locs) {
			throw new NotAvailableOperationModeException(SymbolEnum.class.getSimpleName() + "." + this.name() + " not method assemblyLocationMongo...");
		}

		@Override
		protected String getMongoSymbol(final boolean alone) {
			throw new NotAvailableOperationModeException(SymbolEnum.class.getSimpleName() + "." + this.name() + " not method getMongoSymbol...");
		}
	},
	/**
	 * 是否为null，其后没有任何内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:05:51
	 */
	IsNull {

		@Override
		public void assemblyLocationSQL(final FieldLocation<?> fl, final StringBuilder sql, final IndexCounter ic, final List<QLLocation> locs) {
			fl.assemblySQL(sql, ic);
			sql.append(" IS NULL");
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final BasicDBObject bo, final List<QLLocation> locs) {
			final StringBuilder ks = new StringBuilder();
			fl.assemblyMongo(ks);
			bo.append(ks.toString(), null);
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final StringBuilder sb, final List<QLLocation> locs) {
			// if (sb.length() > 4) {
			// sb.append(',');
			// }
			// sb.append('{');
			sb.append("\"");
			fl.assemblyMongo(sb);
			sb.append("\":null");
			// sb.append(":null}");
		}

		@Override
		protected String getMongoSymbol(final boolean alone) {
			throw new NotAvailableOperationModeException(SymbolEnum.class.getSimpleName() + "." + this.name() + " not method getMongoSymbol...");
		}
	},
	/**
	 * 是否不为null，其后没有任何内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:05:51
	 */
	IsNotNull {

		@Override
		public void assemblyLocationSQL(final FieldLocation<?> fl, final StringBuilder sql, final IndexCounter ic, final List<QLLocation> locs) {
			fl.assemblySQL(sql, ic);
			sql.append(" IS NOT NULL ");
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final BasicDBObject bo, final List<QLLocation> locs) {
			final StringBuilder ks = new StringBuilder();
			fl.assemblyMongo(ks);
			bo.append(ks.toString(), "{$ne:null}");
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final StringBuilder sb, final List<QLLocation> locs) {
			// if (sb.length() > 4) {
			// sb.append(',');
			// }
			// sb.append('{');
			sb.append("\"");
			fl.assemblyMongo(sb);
			sb.append("\":{$ne:null}");
			// sb.append(":{$ne:null}}");
		}

		@Override
		protected String getMongoSymbol(final boolean alone) {
			throw new NotAvailableOperationModeException(SymbolEnum.class.getSimpleName() + "." + this.name() + " not method getMongoSymbol...");
		}
	},
	/**
	 * 需要存在目标字段，针对mongo<br />
	 * 对应sql，同isNotNull<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月9日 下午12:00:22
	 */
	Exists {

		@Override
		public void assemblyLocationSQL(final FieldLocation<?> fl, final StringBuilder sql, final IndexCounter ic, final List<QLLocation> locs) {
			fl.assemblySQL(sql, ic);
			sql.append(" IS NOT NULL ");
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final BasicDBObject bo, final List<QLLocation> locs) {
			final StringBuilder ks = new StringBuilder();
			fl.assemblyMongo(ks);
			bo.append(ks.toString(), "{$exists:true}");
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final StringBuilder sb, final List<QLLocation> locs) {
			// if (sb.length() > 4) {
			// sb.append(',');
			// }
			// sb.append('{');
			sb.append("\"");
			fl.assemblyMongo(sb);
			sb.append("\":{$exists:true}");
			// sb.append(":{$exists:true}}");
		}

		@Override
		protected String getMongoSymbol(final boolean alone) {
			throw new NotAvailableOperationModeException(SymbolEnum.class.getSimpleName() + "." + this.name() + " not method getMongoSymbol...");
		}
	},
	/**
	 * 不能存在目标字段，针对mongo<br />
	 * 对应sql为是否为null<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月9日 下午12:00:23
	 */
	NotExists {

		@Override
		public void assemblyLocationSQL(final FieldLocation<?> fl, final StringBuilder sql, final IndexCounter ic, final List<QLLocation> locs) {
			fl.assemblySQL(sql, ic);
			sql.append(" IS NULL");
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final BasicDBObject bo, final List<QLLocation> locs) {
			final StringBuilder ks = new StringBuilder();
			fl.assemblyMongo(ks);
			bo.append(ks.toString(), "{$exists:false}");
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final StringBuilder sb, final List<QLLocation> locs) {
			// if (sb.length() > 4) {
			// sb.append(',');
			// }
			// sb.append('{');
			sb.append("\"");
			fl.assemblyMongo(sb);
			sb.append("\":{$exists:false}");
			// sb.append(":{$exists:false}}");
		}

		@Override
		protected String getMongoSymbol(final boolean alone) {
			throw new NotAvailableOperationModeException(SymbolEnum.class.getSimpleName() + "." + this.name() + " not method getMongoSymbol...");
		}
	},
	/**
	 * 在两者之间，其后应该是两个value
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:05:52
	 */
	Between {

		@Override
		public void assemblyLocationSQL(final FieldLocation<?> fl, final StringBuilder sql, final IndexCounter ic, final List<QLLocation> locs) {
			fl.assemblySQL(sql, ic);
			sql.append(" BETWEEN ");
			locs.get(0).assemblySQL(sql, ic);
			sql.append(" AND ");
			locs.get(1).assemblySQL(sql, ic);
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final BasicDBObject bo, final List<QLLocation> locs) {
			final StringBuilder ks = new StringBuilder();
			fl.assemblyMongo(ks);
			final StringBuilder vs = new StringBuilder();
			vs.append("{$gte:");
			locs.get(0).assemblyMongo(vs);
			vs.append(",$lte:");
			locs.get(1).assemblyMongo(vs);
			vs.append("}");
			bo.append(ks.toString(), vs.toString());
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final StringBuilder sb, final List<QLLocation> locs) {
			// if (sb.length() > 4) {
			// sb.append(',');
			// }
			// sb.append('{');
			sb.append("\"");
			fl.assemblyMongo(sb);
			sb.append("\":{$gte:");
			locs.get(0).assemblyMongo(sb);
			sb.append(",$lte:");
			locs.get(1).assemblyMongo(sb);
			sb.append("}");
			// sb.append("}}");
		}

		@Override
		protected String getMongoSymbol(final boolean alone) {
			throw new NotAvailableOperationModeException(SymbolEnum.class.getSimpleName() + "." + this.name() + " not method getMongoSymbol...");
		}
	},
	/**
	 * 不在两者间的，其后应该是两个value
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:21:35
	 */
	NotBetween {

		@Override
		public void assemblyLocationSQL(final FieldLocation<?> fl, final StringBuilder sql, final IndexCounter ic, final List<QLLocation> locs) {
			fl.assemblySQL(sql, ic);
			sql.append(" NOT BETWEEN ");
			locs.get(0).assemblySQL(sql, ic);
			sql.append(" AND ");
			locs.get(1).assemblySQL(sql, ic);
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final BasicDBObject bo, final List<QLLocation> locs) {
			final BasicDBList bdl = new BasicDBList();
			final StringBuilder ks = new StringBuilder();
			fl.assemblyMongo(ks);
			final StringBuilder vs1 = new StringBuilder();
			vs1.append("{$lt:");
			locs.get(0).assemblyMongo(vs1);
			vs1.append('}');
			bdl.add(new BasicDBObject(ks.toString(), vs1.toString()));
			final StringBuilder vs2 = new StringBuilder();
			vs2.append("{$gt:");
			locs.get(0).assemblyMongo(vs2);
			vs2.append('}');
			bdl.add(new BasicDBObject(ks.toString(), vs2.toString()));
			bo.append("$or", bdl);
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final StringBuilder sb, final List<QLLocation> locs) {
			// if (sb.length() > 4) {
			// sb.append(',');
			// }
			// sb.append("{$or:[{");
			sb.append("$or:[{\"");
			fl.assemblyMongo(sb);
			sb.append("\":{$lt:");
			locs.get(0).assemblyMongo(sb);
			sb.append("}},{\"");
			fl.assemblyMongo(sb);
			sb.append("\":{$gt:");
			locs.get(1).assemblyMongo(sb);
			sb.append("}}]");
			// sb.append("}}]}");
		}

		@Override
		protected String getMongoSymbol(final boolean alone) {
			throw new NotAvailableOperationModeException(SymbolEnum.class.getSimpleName() + "." + this.name() + " not method getMongoSymbol...");
		}
	},
	/**
	 * 包括之后的所有值，其后应该都是各种value
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:08:15
	 */
	In {

		@Override
		public void assemblyLocationSQL(final FieldLocation<?> fl, final StringBuilder sql, final IndexCounter ic, final List<QLLocation> locs) {
			fl.assemblySQL(sql, ic);
			sql.append(" IN (");
			boolean isFirst = true;
			for (final QLLocation l : locs) {
				if (isFirst) {
					isFirst = false;
				} else {
					sql.append(',');
				}
				l.assemblySQL(sql, ic);
			}
			sql.append(")");
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final BasicDBObject bo, final List<QLLocation> locs) {
			final StringBuilder ks = new StringBuilder();
			fl.assemblyMongo(ks);
			final StringBuilder vs = new StringBuilder();
			vs.append("{").append(this.getMongoSymbol()).append(":[");
			boolean isFirst = true;
			for (final QLLocation l : locs) {
				if (isFirst) {
					isFirst = false;
				} else {
					vs.append(',');
				}
				l.assemblyMongo(vs);
			}
			vs.append("]}");
			bo.append(ks.toString(), vs.toString());
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final StringBuilder sb, final List<QLLocation> locs) {
			// if (sb.length() > 4) {
			// sb.append(',');
			// }
			// 放入字段
			// sb.append("{\"");
			sb.append("\"");
			fl.assemblyMongo(sb);
			sb.append("\":{").append(this.getMongoSymbol()).append(":[");
			// 放入值
			boolean isFirst = true;
			for (final QLLocation ql : locs) {
				if (isFirst) {
					isFirst = false;
				} else {
					sb.append(",");
				}
				ql.assemblyMongo(sb);
			}
			sb.append("]}");
			// sb.append("]}}");
		}

		@Override
		protected String getMongoSymbol(final boolean alone) {
			if (alone) {
				return "$in";
			} else {
				return "$in:";
			}
		}
	},
	/**
	 * 不包括之后的所有值，其后应该都是各种value
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:20:52
	 */
	NotIn {

		@Override
		public void assemblyLocationSQL(final FieldLocation<?> fl, final StringBuilder sql, final IndexCounter ic, final List<QLLocation> locs) {
			fl.assemblySQL(sql, ic);
			sql.append(" NOT IN (");
			boolean isFirst = true;
			for (final QLLocation l : locs) {
				if (isFirst) {
					isFirst = false;
				} else {
					sql.append(',');
				}
				l.assemblySQL(sql, ic);
			}
			sql.append(")");
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final BasicDBObject bo, final List<QLLocation> locs) {
			final StringBuilder ks = new StringBuilder();
			fl.assemblyMongo(ks);
			final StringBuilder vs = new StringBuilder();
			vs.append("{").append(this.getMongoSymbol()).append(":[");
			boolean isFirst = true;
			for (final QLLocation l : locs) {
				if (isFirst) {
					isFirst = false;
				} else {
					vs.append(',');
				}
				l.assemblyMongo(vs);
			}
			vs.append("]}");
			bo.append(ks.toString(), vs.toString());
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final StringBuilder sb, final List<QLLocation> locs) {
			// if (sb.length() > 4) {
			// sb.append(',');
			// }
			// 放入字段
			// sb.append("{\"");
			sb.append("\"");
			fl.assemblyMongo(sb);
			sb.append("\":{").append(this.getMongoSymbol()).append(":[");
			// 放入值
			boolean isFirst = true;
			for (final QLLocation ql : locs) {
				if (isFirst) {
					isFirst = false;
				} else {
					sb.append(",");
				}
				ql.assemblyMongo(sb);
			}
			sb.append("]}");
			// sb.append("]}}");
		}

		@Override
		protected String getMongoSymbol(final boolean alone) {
			if (alone) {
				return "$nin";
			} else {
				return "$nin:";
			}
		}
	},
	/**
	 * 
	 */
	All {

		@Override
		public void assemblyLocationSQL(final FieldLocation<?> fl, final StringBuilder sql, final IndexCounter ic, final List<QLLocation> locs) {
			sql.append('(');
			boolean isFirst = true;
			for (final QLLocation l : locs) {
				if (isFirst) {
					isFirst = false;
				} else {
					sql.append(" and ");
				}
				fl.assemblySQL(sql, ic);
				sql.append('=');
				l.assemblySQL(sql, ic);
			}
			sql.append(')');
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final BasicDBObject bo, final List<QLLocation> locs) {
			final StringBuilder ks = new StringBuilder();
			fl.assemblyMongo(ks);
			final StringBuilder vs = new StringBuilder();
			vs.append("{").append(this.getMongoSymbol()).append(":[");
			boolean isFirst = true;
			for (final QLLocation l : locs) {
				if (isFirst) {
					isFirst = false;
				} else {
					vs.append(',');
				}
				l.assemblyMongo(vs);
			}
			vs.append("]}");
			bo.append(ks.toString(), vs.toString());
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final StringBuilder sb, final List<QLLocation> locs) {
			// if (sb.length() > 4) {
			// sb.append(',');
			// }
			// 放入字段
			// sb.append("{\"");
			sb.append("\"");
			fl.assemblyMongo(sb);
			sb.append("\":{").append(this.getMongoSymbol()).append(":[");
			// 放入值
			boolean isFirst = true;
			for (final QLLocation ql : locs) {
				if (isFirst) {
					isFirst = false;
				} else {
					sb.append(",");
				}
				ql.assemblyMongo(sb);
			}
			sb.append("]}");
			// sb.append("]}}");
		}

		@Override
		protected String getMongoSymbol(final boolean alone) {
			if (alone) {
				return "$all";
			} else {
				return "$all:";
			}
		}
	},
	/**
	 * 是否部分匹配，其后仅有一个value
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:05:52
	 */
	Like {

		@Override
		public void assemblyLocationSQL(final FieldLocation<?> fl, final StringBuilder sql, final IndexCounter ic, final List<QLLocation> locs) {
			fl.assemblySQL(sql, ic);
			sql.append(" LIKE ");
			locs.get(0).assemblySQL(sql, ic);
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final BasicDBObject bo, final List<QLLocation> locs) {
			final StringBuilder ks = new StringBuilder();
			fl.assemblyMongo(ks);
			final StringBuilder vs = new StringBuilder();
			locs.get(0).assemblyMongo(vs);
			vs.deleteCharAt(vs.length() - 1).deleteCharAt(0);
			if (vs.charAt(0) == '%') {
				if (vs.charAt(vs.length() - 1) == '%') {
					// 模糊匹配
					vs.replace(0, 1, "^.*").replace(vs.length() - 1, vs.length(), ".*$");
					bo.append(ks.toString(), new BasicDBObject(this.getMongoSymbol(), vs));
				} else {
					// 左匹配
					vs.replace(0, 1, "^.*").append("$");
					bo.append(ks.toString(), new BasicDBObject(this.getMongoSymbol(), vs));
				}
			} else if (vs.charAt(vs.length() - 1) == '%') {
				// 右匹配
				vs.insert(0, "^").replace(vs.length() - 1, vs.length(), ".*$");
				bo.append(ks.toString(), new BasicDBObject(this.getMongoSymbol(), vs));
			} else {
				// 无匹配
				bo.append(ks.toString(), vs.toString());
			}
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final StringBuilder sb, final List<QLLocation> locs) {
			// if (sb.length() > 4) {
			// sb.append(',');
			// }
			// 放入字段
			// sb.append("{\"");
			sb.append("\"");
			fl.assemblyMongo(sb);
			sb.append("\":");
			final StringBuilder vs = new StringBuilder();
			locs.get(0).assemblyMongo(vs);
			vs.deleteCharAt(vs.length() - 1).deleteCharAt(0);
			if (vs.charAt(0) == '%') {
				if (vs.charAt(vs.length() - 1) == '%') {
					// 模糊匹配
					vs.replace(0, 1, "\"^.*").replace(vs.length() - 1, vs.length(), ".*$\"");
					sb.append('{').append(this.getMongoSymbol(false)).append(vs).append('}');
				} else {
					// 左匹配
					vs.replace(0, 1, "\"^.*").append("$\"");
					sb.append('{').append(this.getMongoSymbol(false)).append(vs).append('}');
				}
			} else if (vs.charAt(vs.length() - 1) == '%') {
				// 右匹配
				vs.insert(0, "\"^").replace(vs.length() - 1, vs.length(), ".*$\"");
				sb.append('{').append(this.getMongoSymbol(false)).append(vs).append('}');
			} else {
				// 无匹配
				sb.append('"').append(vs).append('"');
			}
			// sb.append('}');
		}

		@Override
		protected String getMongoSymbol(final boolean alone) {
			if (alone) {
				return "$regex";
			} else {
				return "$regex:";
			}
		}
	},
	/**
	 * 非相关部分匹配，其后仅有一个value
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月24日 下午8:20:51
	 */
	NotLike {

		@Override
		public void assemblyLocationSQL(final FieldLocation<?> fl, final StringBuilder sql, final IndexCounter ic, final List<QLLocation> locs) {
			fl.assemblySQL(sql, ic);
			sql.append(" NOT LIKE ");
			locs.get(0).assemblySQL(sql, ic);
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final BasicDBObject bo, final List<QLLocation> locs) {
			final StringBuilder ks = new StringBuilder();
			fl.assemblyMongo(ks);
			final StringBuilder vs = new StringBuilder();
			locs.get(0).assemblyMongo(vs);
			vs.deleteCharAt(vs.length() - 1).deleteCharAt(0);
			if (vs.charAt(0) == '%') {
				if (vs.charAt(vs.length() - 1) == '%') {
					// 模糊匹配
					vs.replace(0, 1, "^.*").replace(vs.length() - 1, vs.length(), ".*$");
					bo.append(ks.toString(), new BasicDBObject(this.getMongoSymbol(), vs));
				} else {
					// 左匹配
					vs.replace(0, 1, "^.*").append("$");
					bo.append(ks.toString(), new BasicDBObject(this.getMongoSymbol(), vs));
				}
			} else if (vs.charAt(vs.length() - 1) == '%') {
				// 右匹配
				vs.insert(0, "^").replace(vs.length() - 1, vs.length(), ".*$");
				bo.append(ks.toString(), new BasicDBObject(this.getMongoSymbol(), vs));
			} else {
				// 无匹配
				vs.insert(0, "^").append("$");
				bo.append(ks.toString(), new BasicDBObject(this.getMongoSymbol(), vs));
			}
		}

		@Override
		public void assemblyLocationMongo(final FieldLocation<?> fl, final StringBuilder sb, final List<QLLocation> locs) {
			// if (sb.length() > 4) {
			// sb.append(',');
			// }
			// 放入字段
			// sb.append("{\"");
			sb.append("\"");
			fl.assemblyMongo(sb);
			sb.append("\":{$not:");
			final StringBuilder vs = new StringBuilder();
			locs.get(0).assemblyMongo(vs);
			vs.deleteCharAt(vs.length() - 1).deleteCharAt(0);
			if (vs.charAt(0) == '%') {
				if (vs.charAt(vs.length() - 1) == '%') {
					// 模糊匹配
					vs.replace(0, 1, "\"^.*").replace(vs.length() - 1, vs.length(), ".*$\"");
					sb.append('{').append(this.getMongoSymbol(false)).append(vs).append('}');
				} else {
					// 左匹配
					vs.replace(0, 1, "\"^.*").append("$\"");
					sb.append('{').append(this.getMongoSymbol(false)).append(vs).append('}');
				}
			} else if (vs.charAt(vs.length() - 1) == '%') {
				// 右匹配
				vs.insert(0, "\"^").replace(vs.length() - 1, vs.length(), ".*$\"");
				sb.append('{').append(this.getMongoSymbol(false)).append(vs).append('}');
			} else {
				// 无匹配
				sb.append('"').append(vs).append('"');
			}
			sb.append("}");
			// sb.append("}}");
			// if (sb.length() > 4) {
			// sb.append(',');
			// }
			// // 放入字段
			// sb.append("{\"");
			// fl.assemblyMongo(sb);
			// sb.append("\":{");
			// final StringBuilder vs = new StringBuilder();
			// locs.get(0).assemblyMongo(vs);
			// vs.deleteCharAt(vs.length() - 1).deleteCharAt(0);
			// if (vs.charAt(0) == '%') {
			// if (vs.charAt(vs.length() - 1) == '%') {
			// // 模糊匹配
			// vs.replace(0, 1, "\"^.*").replace(vs.length() - 1, vs.length(), ".*$\"");
			// // sb.append('{').append(this.getMongoSymbol(false)).append(vs).append('}');
			// } else {
			// // 左匹配
			// vs.replace(0, 1, "\"^.*").append("$\"");
			// // sb.append('{').append(this.getMongoSymbol(false)).append(vs).append('}');
			// }
			// } else if (vs.charAt(vs.length() - 1) == '%') {
			// // 右匹配
			// vs.insert(0, "\"^").replace(vs.length() - 1, vs.length(), ".*$\"");
			// // sb.append('{').append(this.getMongoSymbol(false)).append(vs).append('}');
			// } else {
			// // 无匹配
			// vs.insert(0, "\"^").append("$\"");
			// }
			// sb.append(this.getMongoSymbol(false)).append(vs).append("}}");
		}

		@Override
		protected String getMongoSymbol(final boolean alone) {
			if (alone) {
				return "$regex";
			} else {
				return "$regex:";
			}
		}
	};

	/**
	 * 得到Mongo用标志符号
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月12日 下午4:48:32
	 * @return Mongo用标志符号
	 */
	protected String getMongoSymbol() {
		return this.getMongoSymbol(true);
	}

	/**
	 * 得到Mongo用标志符号
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月13日 上午11:28:33
	 * @param alone 是否只有自身内容
	 * @return Mongo用标志符号
	 */
	protected abstract String getMongoSymbol(boolean alone);

	/**
	 * 组合占位sql语句
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月8日 下午2:28:36
	 * @param fl 目标占位，主符号左边相关字段
	 * @param sql sql语句
	 * @param ic 索引计数器
	 * @param locs 条件占位列表
	 */
	public abstract void assemblyLocationSQL(FieldLocation<?> fl, StringBuilder sql, IndexCounter ic, List<QLLocation> locs);

	/**
	 * 组合占位mongo语句
	 * 
	 * @author tfzzh
	 * @dateTime 2016年12月9日 上午11:57:47
	 * @param fl 目标占位，主符号左边相关字段
	 * @param bo 组合消息用对象
	 * @param locs 条件占位列表
	 */
	public abstract void assemblyLocationMongo(FieldLocation<?> fl, BasicDBObject bo, List<QLLocation> locs);

	/**
	 * 组合占位mongo语句
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月9日 下午4:40:00
	 * @param fl 目标占位，主符号左边相关字段
	 * @param sb 组合用字串
	 * @param locs 条件占位列表
	 */
	public abstract void assemblyLocationMongo(FieldLocation<?> fl, StringBuilder sb, List<QLLocation> locs);
}
