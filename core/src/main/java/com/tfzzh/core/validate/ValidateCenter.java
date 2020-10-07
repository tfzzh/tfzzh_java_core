/**
 * @author Xu Weijie
 * @datetime 2017年9月26日_上午11:47:19
 */
package com.tfzzh.core.validate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.tfzzh.core.validate.annotation.FieldValidate;
import com.tfzzh.core.validate.annotation.FieldValidates;
import com.tfzzh.core.validate.element.ValidateElement;

/**
 * 验证控制
 * 
 * @author Xu Weijie
 * @datetime 2017年9月26日_上午11:47:19
 */
public class ValidateCenter {

	/**
	 * 有验证操作的Bean对象，以及对应的相关方法及值验证规则<br />
	 * <目标类对象,需验证相关的属性与其值验证规则><br />
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午4:14:28
	 */
	private final Map<Class<?>, Map<Field, List<ValidateElement>>> vbm = new HashMap<>();

	/**
	 * 对象唯一实例
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_下午5:31:39
	 */
	private final static ValidateCenter vc = new ValidateCenter();

	/**
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_下午5:31:37
	 */
	private ValidateCenter() {
	}

	/**
	 * 得到对象实例
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_下午5:33:14
	 * @return 对象唯一实例
	 */
	public static ValidateCenter getInstance() {
		return ValidateCenter.vc;
	}

	/**
	 * 对对象进行验证
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午4:16:24
	 * @param tar 目标对象
	 * @return 验证问题消息集合，如果没有问题，则返回null
	 */
	public Map<Field, ValidateErrorInfo> validate(final Object tar) {
		final Class<?> clz = tar.getClass();
		// 得到验证相关的属性与其值验证规则
		Map<Field, List<ValidateElement>> tm = this.vbm.get(clz);
		if (null == tm) {
			// 是一个新对象，需要创建新的验证模型组件
			tm = this.processingRules(clz);
			this.vbm.put(clz, tm);
		}
		// 逐一进行验证操作
		final Map<Field, ValidateErrorInfo> vm = new LinkedHashMap<>();
		Object to;
		ValidateErrorInfo vei;
		Field f;
		f: for (final Entry<Field, List<ValidateElement>> ent : tm.entrySet()) {
			try {
				f = ent.getKey();
				f.setAccessible(true);
				to = ent.getKey().get(tar);
				f.setAccessible(false);
				for (final ValidateElement ve : ent.getValue()) {
					if (null != (vei = ve.validate(ent.getKey().getName(), to))) {
						vm.put(ent.getKey(), vei);
						continue f;
					}
				}
			} catch (final IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if (vm.size() == 0) {
			return null;
		} else {
			return vm;
		}
	}

	/**
	 * 整理验证规则
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午4:21:45
	 * @param clz 目标类对象
	 * @return 需验证相关的属性与其值验证规则
	 */
	private Map<Field, List<ValidateElement>> processingRules(Class<?> clz) {
		final Map<Field, List<ValidateElement>> tm = new LinkedHashMap<>();
		do {
			this.processingFieldRules(clz, tm);
			clz = clz.getSuperclass();
		} while (Object.class != clz);
		return tm;
	}

	/**
	 * 进行属性规则处理
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月26日_下午4:46:53
	 * @param pClz 目标对象
	 * @param tm 相关验证消息列表
	 */
	private void processingFieldRules(final Class<?> pClz, final Map<Field, List<ValidateElement>> tm) {
		final Field[] fields = pClz.getDeclaredFields();
		FieldValidates afvs;
		FieldValidate afv;
		List<ValidateElement> vl;
		for (final Field field : fields) {
			afvs = field.getAnnotation(FieldValidates.class);
			if ((null == afvs) || (afvs.value().length == 0)) {
				afv = field.getAnnotation(FieldValidate.class);
				if (null != afv) {
					vl = new ArrayList<>();
					vl.add(afv.value().getValidateElement(afv));
					tm.put(field, vl);
				}
				continue;
			}
			vl = new ArrayList<>();
			for (final FieldValidate fv : afvs.value()) {
				vl.add(fv.value().getValidateElement(fv));
			}
			tm.put(field, vl);
		}
	}
	// public static void main(String[] args) {
	// ValidateControl vc = new ValidateControl();
	// vc.processingClassRules(BaseSystemBean.class, null);
	// }
}
