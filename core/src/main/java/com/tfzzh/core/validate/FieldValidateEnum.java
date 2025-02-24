/**
 * @author Xu Weijie
 * @datetime 2017年9月26日_下午12:00:43
 */
package com.tfzzh.core.validate;

import com.tfzzh.core.validate.annotation.FieldValidate;
import com.tfzzh.core.validate.element.CustomValidateElement;
import com.tfzzh.core.validate.element.DecimalRangeValidateElement;
import com.tfzzh.core.validate.element.IntegerRangeValidateElement;
import com.tfzzh.core.validate.element.LengthRangeValidateElement;
import com.tfzzh.core.validate.element.NonNullValidateElement;
import com.tfzzh.core.validate.element.RegularValidateElement;
import com.tfzzh.core.validate.element.ValidateElement;

/**
 * 常用字段验证类型
 * 
 * @author Xu Weijie
 * @datetime 2017年9月26日_下午12:00:43
 */
public enum FieldValidateEnum {

	/**
	 * 非空，必须存在内容，零字长也认为null
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午2:00:10
	 */
	NonNull {

		@Override
		public ValidateElement getValidateElement(final FieldValidate fv) {
			return new NonNullValidateElement();
		}
	},
	/**
	 * 数值范围：</br>
	 * <x：小于某个值；</br>
	 * <=x：小于等于（不可大于）某个值；</br>
	 * >x：大于某个值；</br>
	 * >=x：大于等于（不可小于）某个值；</br>
	 * 最多可设置两条，组合为一个范围；</br>
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午1:59:48
	 */
	IntegerRange {

		@Override
		public ValidateElement getValidateElement(final FieldValidate fv) {
			return new IntegerRangeValidateElement(fv.reference());
		}
	},
	/**
	 * 数值范围：</br>
	 * <x：小于某个值；</br>
	 * <=x：小于等于（不可大于）某个值；</br>
	 * >x：大于某个值；</br>
	 * >=x：大于等于（不可小于）某个值；</br>
	 * 最多可设置两条，组合为一个范围；</br>
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午1:59:48
	 */
	NumberRange {

		@Override
		public ValidateElement getValidateElement(final FieldValidate fv) {
			return new DecimalRangeValidateElement(fv.reference());
		}
	},
	/**
	 * 字符串长度范围：</br>
	 * <x：小于某个值；</br>
	 * <=x：小于等于（不可大于）某个值；</br>
	 * >x：大于某个值；</br>
	 * >=x：大于等于（不可小于）某个值；</br>
	 * 最多可设置两条，组合为一个范围；</br>
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午1:59:49
	 */
	LengthRange {

		@Override
		public ValidateElement getValidateElement(final FieldValidate fv) {
			return new LengthRangeValidateElement(fv.reference());
		}
	},
	/**
	 * 正则表达式模式，同java正则表达式模型
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午2:14:19
	 */
	Regular {

		@Override
		public ValidateElement getValidateElement(final FieldValidate fv) {
			return new RegularValidateElement(fv.name(), fv.reference());
		}
	},
	/**
	 * 自定义类型，根据对“name”属性的定义，去对应目标的操作方法，如果没有目标操作方法，认为是通过的
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午2:02:07
	 */
	Custom {

		@Override
		public ValidateElement getValidateElement(final FieldValidate fv) {
			return new CustomValidateElement(fv.name(), fv.reference());
		}
	};

	/**
	 * 得到验证用元素
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午5:03:57
	 * @param fv 字段验证信息
	 * @return 验证用元素
	 */
	public abstract ValidateElement getValidateElement(FieldValidate fv);
}
