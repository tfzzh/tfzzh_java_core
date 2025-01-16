/**
 * @author xuweijie
 * @dateTime 2012-1-20 下午4:22:09
 */
package com.tfzzh.view.web.tools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfzzh.core.control.annotation.ManagerIoc;
import com.tfzzh.core.control.tools.ManagerMap;
import com.tfzzh.core.control.tools.NewManagerMapTool;
import com.tfzzh.exception.InitializeException;
import com.tfzzh.tools.ClassTool;
import com.tfzzh.tools.Constants;
import com.tfzzh.tools.ReflectControl;
import com.tfzzh.tools.StringTools;
import com.tfzzh.view.web.annotation.IpRestriction;
import com.tfzzh.view.web.annotation.LinkAllIp;
import com.tfzzh.view.web.annotation.LinkAllParam;
import com.tfzzh.view.web.annotation.LinkAttr;
import com.tfzzh.view.web.annotation.LinkAttrDecrypt;
import com.tfzzh.view.web.annotation.LinkBean;
import com.tfzzh.view.web.annotation.LinkBranch;
import com.tfzzh.view.web.annotation.LinkBranchInfo;
import com.tfzzh.view.web.annotation.LinkDeploy;
import com.tfzzh.view.web.annotation.LinkIp;
import com.tfzzh.view.web.annotation.LinkLogin;
import com.tfzzh.view.web.annotation.LinkMain;
import com.tfzzh.view.web.annotation.LinkNormal;
import com.tfzzh.view.web.annotation.LinkParam;
import com.tfzzh.view.web.annotation.LinkSuffix;
import com.tfzzh.view.web.annotation.LinkUrl;
import com.tfzzh.view.web.annotation.LinkUserAgent;
import com.tfzzh.view.web.bean.BaseParamBean;
import com.tfzzh.view.web.iface.ReflectLinkOperate;
import com.tfzzh.view.web.link.LinkType;
import com.tfzzh.view.web.link.OperateLink;
import com.tfzzh.view.web.link.OperateLink.OperateLinkNodeInfo;
import com.tfzzh.view.web.servlet.session.ClientSessionBean;

/**
 * 页面连接注解解析
 * 
 * @author xuweijie
 * @dateTime 2012-1-20 下午4:22:09
 */
public class LinkPageAnnotationAnalysis {

	/**
	 * 用类路径对应控制类及Dao类对象
	 * 
	 * @author XuWeijie
	 * @datetime 2015年10月27日_下午1:28:06
	 */
	private final NewManagerMapTool nmmt;
	// /**
	// * 读取request.attribute，控制key
	// *
	// * @author tfzzh
	// * @dateTime 2023年11月24日 14:46:54
	// */
	// private final String KEY_READ_REQ_ATTR = "r_r_a".intern();

	/**
	 * @author XuWeijie
	 * @datetime 2015年10月27日_下午1:28:10
	 */
	public LinkPageAnnotationAnalysis() {
		this.nmmt = null;
	}

	/**
	 * @author XuWeijie
	 * @datetime 2015年10月27日_下午1:29:01
	 * @param nmmt 类路径对应控制类及Dao类对象
	 */
	public LinkPageAnnotationAnalysis(final NewManagerMapTool nmmt) {
		this.nmmt = nmmt;
	}

	/**
	 * 读取注解路径
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-22 下午3:30:06
	 * @param analysePath 解析路径
	 */
	public void readerAnnotationPath(final String analysePath) {
		this.readerAnnotationPath(null, analysePath);
	}

	/**
	 * 读取注解路径
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-29 上午10:37:59
	 * @param basePath 基础路径
	 * @param analysePath 解析路径
	 */
	public void readerAnnotationPath(final String basePath, final String analysePath) {
		final String[] paths = StringTools.split(analysePath, Constants.DIAGONAL_LINE);
		for (String path : paths) {
			if ((path = path.trim()).length() > 0) {
				path = path.replaceAll("[/]", ".");
				this.readerPathFiles(path);
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
	@SuppressWarnings("unchecked")
	private void readerPathFiles(final String path) {
		final Set<Class<?>> clzs = ClassTool.getClasses(path, true);
		for (final Class<?> clz : clzs) {
			// System.out.println("\t\t" + clz.getName());
			final Class<?>[] ifs = clz.getInterfaces();
			for (final Class<?> cl : ifs) {
				if (cl == ReflectLinkOperate.class) {
					// 是目标类型
					this.analyseClassAnnotation((Class<? extends ReflectLinkOperate>) clz);
					break;
				}
			}
		}
	}

	/**
	 * 解析类中注解
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-29 上午10:38:15
	 * @param clz 需解析注解的类信息
	 */
	private void analyseClassAnnotation(final Class<? extends ReflectLinkOperate> clz) {
		final LinkMain main = clz.getAnnotation(LinkMain.class);
		if (null != main) {
			// 存在注解操作
			// 判断是否分流内容
			final LinkBranch branch = clz.getAnnotation(LinkBranch.class);
			final Method[] methods = clz.getMethods();
			final OperateLink operate = OperateLink.getInstance();
			final ReflectControl reflect = ReflectControl.getInstance();
			final OperateLinkNodeInfo mainNode = operate.addNewLinkNodeInfo(main, null);
			// add by xwj 2017-08-23
			final IpRestriction bir = clz.getAnnotation(IpRestriction.class);
			String mainPath = main.mainPath();
			// 变更符号
			mainPath = mainPath.replaceAll("\\\\", Constants.DIAGONAL_LINE);
			if (!mainPath.startsWith(Constants.DIAGONAL_LINE)) {
				mainPath = Constants.DIAGONAL_LINE + mainPath;
			}
			if (!mainPath.endsWith(Constants.DIAGONAL_LINE)) {
				mainPath += Constants.DIAGONAL_LINE;
			}
			RequestMethod[] rms;
			// 临时
			Map<String, Class<?>> map = null;
			String reflectControlKey;
			String prefix;
			// int accessPermissions;
			if (null == branch) {
				LinkNormal normal;
				LinkDeploy deploy;
				String name;
				String jspName;
				String[] result;
				// 非分流操作
				for (final Method meth : methods) {
					if (null != (normal = meth.getAnnotation(LinkNormal.class))) {
						// 正常的
						name = normal.id().trim();
						boolean tryReplace = false;
						if (name.length() == 0) {
							name = meth.getName();
							if (name.endsWith("Form")) {
								// 应是默认型post请求
								rms = new RequestMethod[] { RequestMethod.Post };
								name = name.substring(0, name.length() - 4);
								if (normal.result().length == 0) {
									jspName = StringTools.splitStringWhitUpper(name);
									result = new String[] { "r:t:" + mainPath.substring(1) + jspName };
								} else {
									result = normal.result();
									tryReplace = true;
								}
							} else {
								rms = normal.method();
								if (normal.result().length == 0) {
									jspName = StringTools.splitStringWhitUpper(name);
									result = new String[] { "d:t:" + mainPath.substring(1) + jspName };
								} else {
									result = normal.result();
									tryReplace = true;
								}
							}
						} else {
							rms = normal.method();
							if (normal.result().length == 0) {
								jspName = StringTools.splitStringWhitUpper(name);
								result = new String[] { "d:t:" + mainPath.substring(1) + jspName };
							} else {
								result = normal.result();
								tryReplace = true;
							}
						}
						if (tryReplace) {
							for (int i = result.length - 1; i >= 0; i--) {
								// 先替换关键字
								result[i] = result[i].replaceAll("#mainpath#", mainPath);
								result[i] = result[i].replaceAll("#mp#", mainPath);
								// 再替换符号
								result[i] = result[i].replaceAll("//", Constants.DIAGONAL_LINE);
								if (result[i].startsWith(Constants.DIAGONAL_LINE)) {
									result[i] = result[i].substring(1);
								}
							}
						}
						// 得到方法
						map = this.getParamTypeMap(meth, clz);
						{ // 判断上层路径层级
							int tier = 1;
							final String[] paths = StringTools.split(mainPath, Constants.DIAGONAL_LINE);
							for (final String path : paths) {
								if (path.length() != 0) {
									tier++;
								}
							}
							prefix = "";
							while (tier-- > 1) {
								prefix += "../";
							}
						}
						// accessPermissions = normal.accessPermissions();
						// add by xwj 2017-08-23
						IpRestriction ir = meth.getAnnotation(IpRestriction.class);
						if (null == ir) {
							ir = bir;
						}
						final boolean readStreamJSON = main.readStreamJSON();
						final boolean needToken = main.needToken();
						for (final RequestMethod rm : rms) {
							reflectControlKey = rm.name() + mainPath + name;
							reflect.addControlInfo(reflectControlKey, clz, meth, true);
							operate.addNewLinkInfo(mainPath, name, name, rm, LinkType.Normal, reflectControlKey, prefix, mainNode, result, map, readStreamJSON ? normal.readStreamJSON() : false, normal.canCrossDomain(), needToken ? normal.needToken() : false, normal.description(), null == ir ? null : ir.value());
						}
					} else if (null != (deploy = meth.getAnnotation(LinkDeploy.class))) {
						// 适配的
						// 得到方法
						map = this.getParamTypeMap(meth, clz);
						rms = deploy.method();
						final StringBuilder prefixPath = new StringBuilder();
						{ // 判断上层路径层级
							prefix = "";
							final String[] paths = StringTools.split(deploy.path(), Constants.DIAGONAL_LINE);
							for (final String path : paths) {
								final int ind = path.indexOf("{");
								if ((ind != -1) && (path.indexOf("}", ind) != -1)) {
									// 是参数项
									prefixPath.append('@');
								} else {
									// 固定项
									prefixPath.append(path);
								}
								prefixPath.append('/');
								prefix += "../";
							}
						}
						// accessPermissions = deploy.accessPermissions();
						// add by xwj 2017-08-23
						IpRestriction ir = meth.getAnnotation(IpRestriction.class);
						if (null == ir) {
							ir = bir;
						}
						final boolean readStreamJSON = main.readStreamJSON();
						final boolean needToken = main.needToken();
						for (final RequestMethod rm : rms) {
							reflectControlKey = rm.name() + mainPath + prefixPath;
							reflect.addControlInfo(reflectControlKey, clz, meth, true);
							operate.addNewLinkInfo(mainPath, deploy.path(), prefixPath.toString(), rm, LinkType.Deploy, reflectControlKey, prefix, mainNode, deploy.result(), map, readStreamJSON ? deploy.readStreamJSON() : false, deploy.canCrossDomain(), needToken ? deploy.needToken() : false, deploy.description(), null == ir ? null : ir.value());
						}
					}
				}
			} else {
				// final OperateLinkNodeInfo branchNode = operate.addNewLinkNodeInfo(branch, main, mainNode);
				final OperateLinkNodeInfo branchNode = operate.addNewLinkNodeInfo(main);
				LinkBranchInfo branchInfo;
				// 分流操作
				for (final Method meth : methods) {
					if (null != (branchInfo = meth.getAnnotation(LinkBranchInfo.class))) {
						// 得到方法
						map = this.getParamTypeMap(meth, clz);
						rms = branch.method();
						{ // 判断上层路径层级
							final String[] paths = StringTools.split(mainPath, Constants.DIAGONAL_LINE);
							int tier = 1 + paths.length;
							prefix = "";
							while (tier-- > 1) {
								prefix += "../";
							}
						}
						// accessPermissions = branch.accessPermissions();
						// add by xwj 2017-08-23
						IpRestriction ir = meth.getAnnotation(IpRestriction.class);
						if (null == ir) {
							ir = bir;
						}
						final boolean readStreamJSON = main.readStreamJSON();
						final boolean needToken = main.needToken();
						for (final RequestMethod rm : rms) {
							reflectControlKey = rm.name() + mainPath + branch.path() + "?" + branchInfo.value();
							reflect.addControlInfo(reflectControlKey, clz, meth, true);
							operate.addNewLinkInfo(mainPath + branch.path(), branchInfo.value(), mainPath + branch.path() + "?" + branchInfo.value(), rm, LinkType.Branch, branch.breachKey(), reflectControlKey, prefix, branchNode, branchInfo.result(), map, readStreamJSON ? branchInfo.readStreamJSON() : false, branchInfo.canCrossDomain(), needToken ? branchInfo.needToken() : false, branch.description(), null == ir ? null : ir.value());
						}
					}
				}
			}
			{ // 进行控制类注解的对象注入
				// 得到自己
				final Object self = ReflectControl.getInstance().getReflectImpl(clz);
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
						field.setAccessible(true);
						try {
							field.set(self, mgr);
							continue;
						} catch (final IllegalArgumentException | IllegalAccessException e) {
							throw new InitializeException("Link Control Field set value Error: " + clz.getName() + "-" + field.getName() + " > " + mgr.getClass().getName());
						} catch (final Exception e) {
							e.printStackTrace();
						} finally {
							field.setAccessible(false);
						}
					}
				}
			}
		}
	}

	/**
	 * 得到参数类型集合
	 * 
	 * @author Weijie Xu
	 * @dateTime 2012-7-17 上午1:47:30
	 * @param meth 方法信息
	 * @param clz 对象类信息
	 * @return 参数类型集合
	 */
	private Map<String, Class<?>> getParamTypeMap(final Method meth, final Class<? extends ReflectLinkOperate> clz) {
		final Class<?>[] parasClz = meth.getParameterTypes();
		final Annotation[][] parasAnn = meth.getParameterAnnotations();
		final int sum = parasClz.length;
		final Map<String, Class<?>> map = new LinkedHashMap<>(sum);
		for (int i = 0; i < sum; i++) {
			if (parasAnn[i].length > 0) {
				// TODO 之后考虑整体优化方案 2023-11-24
				if (parasAnn[i][0] instanceof LinkParam) {
					map.put(((LinkParam) parasAnn[i][0]).value(), parasClz[i]);
				} else if (parasAnn[i][0] instanceof LinkAttr) {
					map.put(WebConstants.KEY_REQUEST_ATTRIBUTE + ((LinkAttr) parasAnn[i][0]).value(), parasClz[i]);
					// map.put(this.KEY_READ_REQ_ATTR, parasClz[i]);
				} else if (parasAnn[i][0] instanceof LinkAttrDecrypt) {
					map.put(WebConstants.KEY_REQUEST_DECRYPT_STREAM_ATTRIBUTE, parasClz[i]);
					// map.put(this.KEY_READ_REQ_ATTR, parasClz[i]);
				} else if (parasAnn[i][0] instanceof LinkLogin) {
					map.put(WebConstants.PRIVATE_LINK_NAME_ACCOUNT, parasClz[i]);
				} else if (parasAnn[i][0] instanceof LinkBean) {
					LinkBean lb = (LinkBean) parasAnn[i][0];
					String key = WebConstants.PRIVATE_LINK_NAME_BEAN + Constants.COLON + parasClz[i].getName();
					if (lb.needValid()) {
						key += ":1";
					}
					map.put(key, parasClz[i]);
				} else if (parasAnn[i][0] instanceof LinkAllIp) { // add xwj 2017-10-17
					map.put(WebConstants.PRIVATE_LINK_NAME_IP, parasClz[i]);
				} else if (parasAnn[i][0] instanceof LinkIp) {
					map.put(WebConstants.PRIVATE_LINK_NAME_FIRST_IP, parasClz[i]);
				} else if (parasAnn[i][0] instanceof LinkUserAgent) { // add xwj 2021-12-25
					map.put(WebConstants.PRIVATE_LINK_NAME_USER_AGENT, parasClz[i]);
				} else if (parasAnn[i][0] instanceof LinkUrl) {
					map.put(WebConstants.PRIVATE_LINK_NAME_URL, parasClz[i]);
				} else if (parasAnn[i][0] instanceof LinkAllParam) {
					map.put(WebConstants.PRIVATE_LINK_NAME_ALL_PARAM, parasClz[i]);
				} else if (parasAnn[i][0] instanceof LinkSuffix) {
					map.put(WebConstants.PRIVATE_LINK_NAME_SUFFIX, parasClz[i]);
				} else {
					throw new InitializeException("Link Control Method has Error Param: " + clz.getName() + "-" + meth.getName() + "(" + i + ")");
				}
			} else {
				// 该情况认为不是request就是response
				if (parasClz[i] == HttpServletRequest.class) {
					// 请求
					map.put(WebConstants.PRIVATE_LINK_NAME_REQUEST, parasClz[i]);
				} else if (parasClz[i] == HttpServletResponse.class) {
					// 返回
					map.put(WebConstants.PRIVATE_LINK_NAME_RESPONSE, parasClz[i]);
					// } else if (HttpSession.class.isAssignableFrom(parasClz[i])) {
					// // session
					// map.put(WebConstants.PRIVATE_LINK_NAME_SESSION, parasClz[i]);
				} else if (ClientSessionBean.class.isAssignableFrom(parasClz[i])) {
					// custom session 2017-04-12
					map.put(WebConstants.PRIVATE_LINK_NAME_CUSTOM_SESSION, parasClz[i]);
				} else if (BaseParamBean.class.isAssignableFrom(parasClz[i])) {
					// 是参数bean
					map.put(WebConstants.PRIVATE_LINK_NAME_BEAN, parasClz[i]);
				} else {
					throw new InitializeException("Link Control Method has Error Param: " + clz.getName() + "-" + meth.getName() + "(" + i + ")");
				}
			}
		}
		return map;
	}
}
