/**
 * @author xuweijie
 * @dateTime 2012-1-20 下午4:22:09
 */
package com.tfzzh.socket.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tfzzh.core.annotation.BaseAnnotationTool;
import com.tfzzh.core.control.annotation.ManagerIoc;
import com.tfzzh.core.control.tools.ManagerMap;
import com.tfzzh.core.control.tools.NewManagerMapTool;
import com.tfzzh.exception.InitializeException;
import com.tfzzh.socket.action.BaseMessageBean;
import com.tfzzh.socket.action.RequestAction;
import com.tfzzh.socket.annotation.ActionMessage;
import com.tfzzh.socket.annotation.ActionOperation;
import com.tfzzh.socket.annotation.ActionSpace;
import com.tfzzh.socket.annotation.ProxyActionOperation;
import com.tfzzh.socket.bean.RequestInfoBean;
import com.tfzzh.tools.ClassTool;

/**
 * 请求行为注解解析
 * 
 * @author xuweijie
 * @dateTime 2012-1-20 下午4:22:09
 */
public class RequestAnnotationTool extends BaseAnnotationTool {

	/**
	 * 用类路径对应控制类及Dao类对象
	 * 
	 * @author XuWeijie
	 * @datetime 2015年10月27日_下午1:28:06
	 */
	private final NewManagerMapTool nmmt;

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年7月14日 下午12:10:31
	 */
	public RequestAnnotationTool() {
		this.nmmt = null;
	}

	/**
	 * @author XuWeijie
	 * @datetime 2015年12月26日_下午6:23:22
	 * @param nmmt 类路径对应控制类及Dao类对象
	 */
	public RequestAnnotationTool(final NewManagerMapTool nmmt) {
		this.nmmt = nmmt;
	}

	/**
	 * 消息对象列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月4日 下午4:38:13
	 */
	private final Map<Integer, Class<? extends BaseMessageBean>> msgMap = new HashMap<>();

	/**
	 * 读取请求消息注解路径
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年7月3日 下午7:24:40
	 * @param analysePath 解析路径
	 */
	public void readerRequestAnnotationPath(final String analysePath) {
		super.readerAnnotationPath(analysePath);
	}

	/**
	 * 读取路径下所有文件
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-29 上午11:48:48
	 * @param path 目标路径
	 * @see com.tfzzh.core.annotation.BaseAnnotationTool#readerPathFiles(java.lang.String)
	 */
	@Override
	protected void readerPathFiles(final String path) {
		final Set<Class<?>> clzs = ClassTool.getClasses(path);
		ActionSpace as = null;
		ActionOperation ao;
		ProxyActionOperation pao;
		Class<?>[] ifs;
		Method[] meths;
		RequestAction ra = null;
		RequestPool rp;
		for (final Class<?> clz : clzs) {
			ifs = clz.getInterfaces();
			for (final Class<?> cl : ifs) {
				if (cl == RequestAction.class) {
					// 是目标类型
					try {
						if (null != (as = clz.getAnnotation(ActionSpace.class))) {
							ra = (RequestAction) clz.newInstance();
							// 将对象放入到池中
							rp = RequestPool.getInstance(as.spaceName());
							// rp.putRequestAction(as.value(), ra);
							// 是有效的
							meths = clz.getMethods();
							for (final Method me : meths) {
								if (null != (ao = me.getAnnotation(ActionOperation.class))) {
									// 可以直接处理的方法
									rp.putRequestInfo(ao.id(), new RequestInfoBean(ao.id(), ra, me, ao.validationRule(), this.msgMap.get(ao.msgCode()), ao.isKeep()));
								} else if (null != (pao = me.getAnnotation(ProxyActionOperation.class))) {
									// 是代理方法，更多仅是进行转发工作
									final Class<? extends BaseMessageBean> clzM = this.msgMap.get(pao.msgCode());
									if (pao.id().length > 0) {
										// 是目标方法
										for (final int id : pao.id()) {
											rp.putRequestInfo(id, new RequestInfoBean(id, ra, me, null, clzM, false));
										}
									} else if (pao.rangeId().length == 2) {
										rp.putRequestInfo(pao.rangeId()[0], pao.rangeId()[1], new RequestInfoBean(pao.rangeId()[0], ra, me, null, clzM, false));
									} else {
										throw new InitializeException("Link Control Id Error: " + clz.getName() + "-" + me.getName());
									}
								}
							}
							// 得到属性列表
							final Field[] fields = clz.getDeclaredFields();
							ManagerIoc manager;
							String name;
							Object mgr;
							for (final Field field : fields) {
								// 认为Link层只接受Manager对象的注入
								if (null != (manager = field.getAnnotation(ManagerIoc.class))) {
									// 存在注入
									if ((name = manager.value()).length() == 0) {
										name = field.getName();
									}
									mgr = ManagerMap.getInstance().getManager(name);
									if (null == mgr) {
										if (null != this.nmmt) {
											mgr = this.nmmt.getClaPathObj(field.getType().getName());
										}
									}
									if (null == mgr) {
										throw new InitializeException("Link Control Field has Error: " + clz.getName() + "-" + field.getName());
									}
									// if (null != (mgr = ManagerMap.getInstance().getManager(name))) {
									field.setAccessible(true);
									try {
										field.set(ra, mgr);
										continue;
									} catch (final IllegalArgumentException e) {
										throw new InitializeException("Link Control Field set value Error: " + clz.getName() + "-" + field.getName() + " > " + mgr.getClass().getName());
									} catch (final IllegalAccessException e) {
										throw new InitializeException("Link Control Field set value Error: " + clz.getName() + "-" + field.getName() + " > " + mgr.getClass().getName());
									} catch (final Exception e) {
										e.printStackTrace();
									} finally {
										field.setAccessible(false);
									}
								}
							}
						}
					} catch (final InstantiationException e) {
						e.printStackTrace();
					} catch (final IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 读取消息参数对象注解路径
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-29 上午10:37:59
	 * @param analysePath 解析路径
	 */
	public void readerMessageAnnotationPath(final String analysePath) {
		final String[] paths = analysePath.split("[,]");
		for (String path : paths) {
			if ((path = path.trim()).length() > 0) {
				path = path.replaceAll("[/]", ".");
				this.readerMessagePathFiles(path);
			}
		}
	}

	/**
	 * 读取路径下所有文件
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-29 上午11:48:48
	 * @param path 目标路径
	 */
	private void readerMessagePathFiles(final String path) {
		final Set<Class<?>> clzs = ClassTool.getClasses(path);
		ActionMessage am;
		Class<? extends BaseMessageBean> ifs;
		for (final Class<?> clz : clzs) {
			if (BaseMessageBean.class.isAssignableFrom(clz)) {
				ifs = clz.asSubclass(BaseMessageBean.class);
			} else {
				continue;
			}
			// 是目标类型
			am = clz.getAnnotation(ActionMessage.class);
			if (null != am) {
				// 是有效的
				// 放入到池中
				this.msgMap.put(am.value(), ifs);
			}
		}
	}
}
