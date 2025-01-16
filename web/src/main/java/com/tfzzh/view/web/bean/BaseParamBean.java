/**
 * @author TFZZH
 * @dateTime 2011-3-7 下午04:01:24
 */
package com.tfzzh.view.web.bean;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.tfzzh.core.validate.ValidateCenter;
import com.tfzzh.core.validate.ValidateErrorInfo;
import com.tfzzh.tools.BaseBean;
import com.tfzzh.view.web.annotation.ParamAlias;

/**
 * 提交数据基础FormBean
 * 
 * @author TFZZH
 * @dateTime 2011-3-7 下午04:01:24
 */
public abstract class BaseParamBean extends BaseBean {

	/**
	 * @author Xu Weijie
	 * @datetime 2017年9月29日_下午1:51:40
	 */
	private static final long serialVersionUID = -8741054166898985585L;

	/**
	 * 字段问题列表
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月27日_下午5:37:45
	 */
	private Map<String, ValidateErrorInfo> vm = null;

	/**
	 * 设置参数
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-7 下午4:32:02
	 * @param paraMap 参数集合，包括文件部分；<br />
	 *           List<String>：正常的页面传值；<br />
	 *           FileItem：上传的文件；<br />
	 * @param needValid 是否需要校验：<br />
	 *           true，需要校验；<br />
	 */
	public abstract void setParameters(Map<String, Object> paraMap, boolean needValid);

	/**
	 * 消息验证
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-6-18 下午2:41:38
	 */
	protected void validate() {
		final Map<Field, ValidateErrorInfo> fvm = ValidateCenter.getInstance().validate(this);
		if (null != fvm) {
			final Map<String, ValidateErrorInfo> svm = new LinkedHashMap<>(fvm.size());
			for (final Entry<Field, ValidateErrorInfo> ent : fvm.entrySet()) {
				svm.put(ent.getKey().getName(), ent.getValue());
			}
			this.vm = svm;
		}
	}

	/**
	 * 得到字段对应参数值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月10日 下午6:51:49
	 * @param field 目标属性
	 * @param paraMap 参数列表
	 * @return 对应值；<br />
	 *         null，没有目标属性所对应的值；<br />
	 */
	protected Object getParamValue(final Field field, final Map<String, Object> paraMap) {
		final Object ps = this.getParamObj(field, paraMap);
		if (null == ps) {
			return null;
		}
		if (ps instanceof List) {
			final List<?> pl = (List<?>) ps;
			if (pl.size() == 1) {
				// 单独的直接返回
				return pl.get(0);
			} else {
				return ps;
			}
		} else {
			return ps;
		}
	}

	/**
	 * 得到字段对应参数String值
	 * 
	 * @author Xu Weijie
	 * @datetime 2018年2月5日_上午10:18:16
	 * @param field 目标属性
	 * @param paraMap 参数列表
	 * @return 对应String值；<br />
	 *         null，没有目标属性所对应的值；<br />
	 */
	protected String getParamStringValue(final Field field, final Map<String, Object> paraMap) {
		final Object ps = this.getParamObj(field, paraMap);
		if (null == ps) {
			return null;
		}
		if (ps instanceof List) {
			final List<?> pl = (List<?>) ps;
			if (pl.size() > 0) {
				// 单独的直接返回
				return pl.get(0).toString();
			} else {
				return "";
			}
		} else {
			return ps.toString();
		}
	}

	/**
	 * 得到目标参数对象
	 * 
	 * @author Xu Weijie
	 * @datetime 2018年2月5日_上午10:20:10
	 * @param field 目标属性
	 * @param paraMap 参数列表
	 * @return 目标参数对象
	 */
	private Object getParamObj(final Field field, final Map<String, Object> paraMap) {
		Object ps = paraMap.get(field.getName());
		if (null == ps) {
			// 判定是否存在别名
			final ParamAlias pa = field.getAnnotation(ParamAlias.class);
			if (null != pa) {
				for (final String al : pa.value()) {
					if (null != (ps = paraMap.get(al))) {
						break;
					}
				}
			}
		}
		return ps;
	}

	/**
	 * 是否存在问题
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月28日_下午3:56:19
	 * @return true，存在问题
	 */
	public boolean hasError() {
		return this.vm == null ? false : this.vm.size() > 0;
	}

	/**
	 * 得到验证结果信息
	 *
	 * @author Xu Weijie
	 * @datetime 2017年9月28日_上午10:24:03
	 * @return the vm 验证结果信息
	 */
	public Map<String, ValidateErrorInfo> getErrorInfo() {
		return this.vm;
	}
}
