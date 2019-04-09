/**
 * @author Weijie Xu
 * @dateTime 2014-3-20 下午7:50:38
 */
package com.tfzzh.socket.bean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.tfzzh.socket.RequestSession;
import com.tfzzh.socket.action.BaseMessageBean;
import com.tfzzh.socket.action.RequestAction;

/**
 * 请求信息Bean
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-20 下午7:50:38
 */
public class RequestInfoBean {

	/**
	 * 请求消息的编码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:55:20
	 */
	private final int code;

	/**
	 * 进行操作的类的实体
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午7:51:22
	 */
	private final RequestAction ra;

	/**
	 * 方法类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月28日 上午9:45:49
	 */
	private final Object methodType;

	/**
	 * 验证规则ID列表
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月26日_下午1:34:02
	 */
	private final Set<Integer> validationRuleIds;

	/**
	 * 对应的参数集合Bean的类
	 *
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:42:21
	 */
	private final Class<? extends BaseMessageBean> msgClz;

	/**
	 * 是否守护方法
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月29日 下午7:18:14
	 */
	private final boolean isKeep;

	/**
	 * 对象文字信息<br />
	 * 弱对象模式<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月17日 下午8:36:43
	 */
	private transient String beanStrInfo = null;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午7:52:34
	 * @param code 请求的编码
	 * @param ra 进行操作的类的实体
	 * @param method 进行实际操作的方法
	 * @param validationRuleIdStr 验证规则字串
	 * @param msgClz 相关参数集合Bean
	 * @param isKeep 是否守护方法
	 */
	public RequestInfoBean(final int code, final RequestAction ra, final Method method, final String validationRuleIdStr, final Class<? extends BaseMessageBean> msgClz, final boolean isKeep) {
		this.code = code;
		this.ra = ra;
		if ((null != validationRuleIdStr) && (validationRuleIdStr.length() > 0)) {
			final String[] strs = validationRuleIdStr.split("[,]");
			final List<Integer> li = new ArrayList<>(strs.length);
			for (final String s : strs) {
				if (s.length() > 0) {
					try {
						li.add(Integer.valueOf(s));
					} catch (final Exception e) {
					}
				}
			}
			this.validationRuleIds = new LinkedHashSet<>(li.size());
			for (int i = li.size() - 1; i >= 0; i--) {
				this.validationRuleIds.add(li.get(i));
			}
		} else {
			this.validationRuleIds = null;
		}
		this.msgClz = msgClz;
		this.isKeep = isKeep;
		// 设置方法类型
		Object mt;
		try {
			mt = ra.getMethodType(code);
			if (null == mt) {
				mt = ra.getMethodType(method.getName());
			}
		} catch (final Exception e) {
			mt = ra.getMethodType(method.getName());
		}
		this.methodType = mt;
	}

	/**
	 * 得到请求消息的编码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:56:30
	 * @return the code
	 */
	public int getCode() {
		return this.code;
	}

	/**
	 * 得到请求行为对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年5月14日 下午8:36:31
	 * @return 请求行为对象
	 */
	public RequestAction getRequestAction() {
		return this.ra;
	}

	/**
	 * 得到方法类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月28日 上午9:49:20
	 * @return the methodType
	 */
	public Object getMethodType() {
		return this.methodType;
	}

	/**
	 * 得到验证规则ID列表
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月26日_下午2:15:03
	 * @return the validationRuleIds
	 */
	public Set<Integer> getValidationRuleIds() {
		return this.validationRuleIds;
	}

	/**
	 * 得到对应的参数集合Bean的编码<br />
	 * 之后需要测试下，是反射快，还是拷贝快，然后再做一些微调<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午8:43:53
	 * @return the beanCode
	 * @throws IllegalAccessException 抛
	 * @throws InstantiationException 抛
	 */
	public BaseMessageBean getBeanInstance() throws InstantiationException, IllegalAccessException {
		return this.msgClz == null ? null : this.msgClz.newInstance();
	}

	/**
	 * 执行相关的请求命令
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年3月21日 下午2:20:14
	 * @param session 目标会话信息
	 * @param msg 请求的参数对象
	 */
	public void exec(final RequestSession session, final BaseMessageBean msg) {
		this.ra.exec(this.methodType, session, msg);
	}

	/**
	 * 是否守护方法
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月29日 下午7:05:31
	 * @return true，是，不打印；<br />
	 *         false，不是，需要打印；<br />
	 */
	public boolean isKeep() {
		return this.isKeep;
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年6月20日 下午5:03:57
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (null == this.beanStrInfo) {
			// 处理数据
			final StringBuilder sb = new StringBuilder(60);
			sb.append('<');
			sb.append(this.code).append('-').append(this.ra.getClass().getSimpleName()).append(':').append(this.methodType.getClass().getSimpleName());
			if (null != this.msgClz) {
				sb.append('(').append(this.msgClz.getSimpleName()).append(')');
			}
			sb.append('>');
			this.beanStrInfo = sb.toString();
		}
		return this.beanStrInfo;
		// return this.ra.getClass().getSimpleName() + ":" + this.methodType.getClass().getSimpleName() + (this.msgClz == null ? "" : "(" + this.msgClz.getSimpleName() + ")");
	}
}
