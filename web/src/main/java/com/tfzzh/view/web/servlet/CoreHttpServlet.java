package com.tfzzh.view.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfzzh.core.control.tools.ManagerMap;
import com.tfzzh.exception.NestedRuntimeException;
import com.tfzzh.log.CoreLog;
import com.tfzzh.tools.Constants;
import com.tfzzh.tools.ReflectControl;
import com.tfzzh.tools.ReflectControl.ControlInfo;
import com.tfzzh.tools.RunThreadLocal;
import com.tfzzh.view.web.bean.BaseParamBean;
import com.tfzzh.view.web.iface.ParamValidateControl;
import com.tfzzh.view.web.link.BaseBackOperationBean;
import com.tfzzh.view.web.link.OperateLink;
import com.tfzzh.view.web.link.OperateLink.OperateLinkInfo;
import com.tfzzh.view.web.servlet.session.ClientSessionBean;
import com.tfzzh.view.web.tools.InfoControl;
import com.tfzzh.view.web.tools.WebBaseConstants;
import com.tfzzh.view.web.tools.WebConstants;
import com.tfzzh.view.web.tools.WebTools;

/**
 * 新的核心控制方法<br />
 * 主要进行请求内容的分流，以及第一次验证<br />
 * 
 * @author xuweijie
 * @dateTime 2012-1-31 下午4:38:45
 */
@WebServlet({ "*.shtml", "/upload/*", "/util/*" })
public class CoreHttpServlet extends BaseHttpServlet {

	/**
	 * @author Xu Weijie
	 * @datetime 2017年11月29日_下午3:26:01
	 */
	private static final long serialVersionUID = -2733916220662692971L;

	// /**
	// * @author TFZZH
	// * @dateTime 2011-2-16 下午01:43:58
	// */
	// private final Logger log = LogManager.getLogger(this.getClass());
	/**
	 * @author TFZZH
	 * @dateTime 2011-2-16 下午01:42:43
	 * @param request 请求数据
	 * @param response 返回数据
	 * @throws ServletException 抛
	 * @throws IOException 抛
	 */
	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		final long l = System.currentTimeMillis();
		if ("OPTIONS".equals(request.getMethod().toUpperCase())) {
			// System.out.println("\t>>\tin OPTIONS>>>");
			// final Enumeration<String> rh = request.getHeaderNames();
			// String hk;
			// while (rh.hasMoreElements()) {
			// hk = rh.nextElement();
			// System.out.println("\th >> " + hk + " >> " + request.getHeader(hk));
			// }
			// System.out.println("\t ==\\t=====");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "*");
			response.setHeader("Access-Control-Allow-Headers", "*");
			return;
		}
		final Map<String, List<String>> pathParas = new HashMap<>();
		// 得到目标路径，并进行路径分析
		final OperateLinkInfo link = OperateLink.getInstance().getOperateLink(request, pathParas);
		if (null != link) {
			// 请求的参数信息
			final Map<String, Object> requestParas = new LinkedHashMap<>();
			// 参数日志
			final StringBuilder paraLog = new StringBuilder();
			// 错误信息列表
			final Map<String, String> errorMap = new HashMap<>();
			final Object[] methodParas = link.getInitMethodParas();
			final TmpSessionBean tab = new TmpSessionBean();
			// 进行参数分析
			final short s = link.validateParas(pathParas, requestParas, request, response, tab, methodParas, paraLog, errorMap);
			// 放入clientsession到线程存储
			RunThreadLocal.getInstance().putObject(WebBaseConstants.TL_KEY, tab.csb);
			if (link.isCanCrossDomain()) {
				this.putAccessAllow(request, response);
			}
			switch (s) {
			default: // 其他参数类问题情况
			case 0: { // 成功情况
				if (errorMap.size() > 0) {
					final String ep = errorMap.remove(WebConstants.PRIVATE_PARAM_ERROR);
					if (null != ep) {
						// TODO 进行请求数据问题处理
						// 处理接口调用相关
						final ParamValidateControl pvc = (ParamValidateControl) ManagerMap.getInstance().getManager(WebBaseConstants.CUSTOM_VALIDATE_CONTROL_IMPL_NAME);
						if (null != pvc) {
							// 进入自定义逻辑判定
							final BaseParamBean bpb = (BaseParamBean) methodParas[Integer.parseInt(ep)];
							final BaseBackOperationBean bbob = pvc.paramValidate(link, bpb);
							if (null != bbob) {
								if (CoreLog.getInstance().debugEnabled(this.getClass())) {
									CoreLog.getInstance().debug(this.getClass(), new StringBuilder().append("run: ").append(System.currentTimeMillis() - l).append(" with [").append(WebTools.getClientIp(request)).append("]> error Parameter Data: ").append(link.getReflectControlKey()).append(">>").append(bpb.getErrorInfo()).append(" params {").append(requestParas).append("}..").toString());
								}
								bbob.linkTo(link, request, response);
								RunThreadLocal.getInstance().removeUserSession(WebBaseConstants.TL_KEY);
								break;
							}
						}
					}
					if (errorMap.size() > 0) {
						// 有错误数据情况，待完善
						this.backToReferer(request, response, errorMap);
						if (CoreLog.getInstance().debugEnabled(this.getClass())) {
							CoreLog.getInstance().debug(this.getClass(), new StringBuilder().append("run: ").append(System.currentTimeMillis() - l).append(" with [").append(WebTools.getClientIp(request)).append("]> error Parameter Data: ").append(link.getReflectControlKey()).append(">>").append(errorMap).append(" params {").append(requestParas).append("}..").toString());
						}
						// 进行消息的再处理
						InfoControl.getInstantce().putProblemRequest(request, response, link, requestParas, l);
						// 将线程中的clientsession信息移除，针对动态数据库连接池
						RunThreadLocal.getInstance().removeUserSession(WebBaseConstants.TL_KEY);
						return;
					}
				}
				boolean isOk = false;
				try {
					// 进行实际操作
					final ControlInfo ci = ReflectControl.getInstance().getReflectControl(link.getReflectControlKey());
					final Object backInfo = ci.reflect(methodParas);
					isOk = true;
					if (backInfo instanceof BaseBackOperationBean) {
						// 进行返回数据控制操作
						final BaseBackOperationBean operation = (BaseBackOperationBean) backInfo;
						operation.setPrefix(link.getPrefix());
						// if (link.isCanCrossDomain()) {
						// 是可以被跨域访问的
						// String host = request.getHeader("Referer");
						// if (null == host) {
						// host = "*";
						// } else {
						// final int eh = host.indexOf(Constants.DIAGONAL_LINE, 10);
						// if (eh != -1) {
						// host = host.substring(0, eh);
						// } else {
						// host = "*";
						// }
						// }
						// response.addHeader("Access-Control-Allow-Origin", host);
						// response.addHeader("Access-Control-Allow-Credentials", "true");
						{ // 放入连接token相关信息
							if (null != tab.csb) {
								response.addHeader(WebBaseConstants.CUSTOM_SESSION_TOKEN_KEY, tab.csb.getToken());
								if (null != tab.csb.getClientId()) {
									response.addHeader(WebBaseConstants.CUSTOM_SESSION_NAME_KEY, tab.csb.getClientId());
								}
							}
						}
						// 具体的操作方法
						operation.linkTo(link, request, response);
					}
				} catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					final Throwable cau = e.getCause();
					if (null == cau) {
						// 其他异常的处理
						if (CoreLog.getInstance().errorEnabled(this.getClass())) {
							CoreLog.getInstance().error(this.getClass(), new StringBuilder().append("\truntime error request").append(request.getServletPath()).append(((null != request.getPathInfo() ? Constants.DIAGONAL_LINE + request.getPathInfo() : ""))).append(" >>>> params : ").append(requestParas).toString());
						}
						// 进行消息的再处理
						InfoControl.getInstantce().putExceptionRequest(request, response, link, requestParas, e, l);
					} else if (NestedRuntimeException.class.isAssignableFrom(cau.getClass())) {
						// 其他异常的处理
						if (CoreLog.getInstance().errorEnabled(this.getClass())) {
							CoreLog.getInstance().error(this.getClass(), new StringBuilder().append("\truntime error request").append(request.getServletPath()).append(((null != request.getPathInfo() ? Constants.DIAGONAL_LINE + request.getPathInfo() : ""))).append(" >>>> params : ").append(requestParas).toString());
						}
						// 进行消息的再处理
						InfoControl.getInstantce().putRuntimeExceptionRequest(request, response, link, requestParas, (NestedRuntimeException) cau, l);
					} else {
						// 其他异常的处理
						if (CoreLog.getInstance().errorEnabled(this.getClass())) {
							CoreLog.getInstance().error(this.getClass(), new StringBuilder().append("\terror request").append(request.getServletPath()).append(((null != request.getPathInfo() ? Constants.DIAGONAL_LINE + request.getPathInfo() : ""))).append(" >>>> params : ").append(requestParas).toString());
						}
						// 进行消息的再处理
						InfoControl.getInstantce().putExceptionRequest(request, response, link, requestParas, cau, l);
					}
				} catch (final NestedRuntimeException e) {
					// 运行时异常的处理
					if (CoreLog.getInstance().errorEnabled(this.getClass())) {
						CoreLog.getInstance().error(this.getClass(), new StringBuilder().append("\truntime error request").append(request.getServletPath()).append(((null != request.getPathInfo() ? Constants.DIAGONAL_LINE + request.getPathInfo() : ""))).append(" >>>> params : ").append(requestParas).toString());
					}
					// 进行消息的再处理
					InfoControl.getInstantce().putRuntimeExceptionRequest(request, response, link, requestParas, e, l);
				} catch (final Exception e) {
					// 其他异常的处理
					if (CoreLog.getInstance().errorEnabled(this.getClass())) {
						CoreLog.getInstance().error(this.getClass(), new StringBuilder().append("\terror request").append(request.getServletPath()).append(((null != request.getPathInfo() ? Constants.DIAGONAL_LINE + request.getPathInfo() : ""))).append(" >>>> params : ").append(requestParas).toString());
					}
					// 进行消息的再处理
					InfoControl.getInstantce().putExceptionRequest(request, response, link, requestParas, e, l);
				} finally {
					// if (null == tab.la) {
					// }
					if (CoreLog.getInstance().debugEnabled(this.getClass())) {
						CoreLog.getInstance().debug(this.getClass(), new StringBuilder().append("run: ").append(System.currentTimeMillis() - l).append(" with [").append(WebTools.getClientIp(request)).append(':').append(request.getRemotePort()).append("]>").append(link.getReflectControlKey()).append('(').append(paraLog.toString()).append(')').append(isOk ? " ok ct[" : " err ct[").append(request.getContentType()).append("] > original > ").append(requestParas.toString()).toString());
						request.getHeader("User-Agent");
						CoreLog.getInstance().debug(this.getClass(), new StringBuilder().append("\t\t >> user-agent >> ").append(request.getHeader("User-Agent")).toString());
					}
					if (isOk) {
						// 成功的请求
						InfoControl.getInstantce().putOkRequest(request, response, link, requestParas, paraLog.toString(), l);
					} else {
						// 问题的请求
						InfoControl.getInstantce().putProblemRequest(request, response, link, requestParas, l);
					}
					// 将线程中的clientsession信息移除，针对动态数据库连接池
					RunThreadLocal.getInstance().removeUserSession(WebBaseConstants.TL_KEY);
				}
				break;
			}
			case 1: { // 权限问题
				// 是一个当前帐号不可访问的连接
				if (CoreLog.getInstance().debugEnabled(this.getClass())) {
					CoreLog.getInstance().debug(this.getClass(), new StringBuilder().append("run: ").append(System.currentTimeMillis() - l).append(" with [").append(WebTools.getClientIp(request)).append(':').append(request.getRemotePort()).append("]> error Access Permission: ").append(link.getReflectControlKey()).toString());
				}
				// 进行消息的再处理
				InfoControl.getInstantce().putErrorAccessRequest(request, response, link, l);
				// 将线程中的clientsession信息移除，针对动态数据库连接池
				RunThreadLocal.getInstance().removeUserSession(WebBaseConstants.TL_KEY);
				return;
			}
			case 2: { // IP限制问题
				if (CoreLog.getInstance().debugEnabled(this.getClass())) {
					CoreLog.getInstance().debug(this.getClass(), new StringBuilder().append("run: ").append(System.currentTimeMillis() - l).append(" with [").append(WebTools.getClientIp(request)).append(':').append(request.getRemotePort()).append("]> error Ip Restriction: ").append(link.getReflectControlKey()).toString());
				}
				// 进行消息的再处理
				InfoControl.getInstantce().putIpRestrictionRequest(request, response, link, l);
				// 将线程中的clientsession信息移除，针对动态数据库连接池
				RunThreadLocal.getInstance().removeUserSession(WebBaseConstants.TL_KEY);
				return;
			}
			}
		} else {
			// 因为页面不存在
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), new StringBuilder().append("run: ").append(System.currentTimeMillis() - l).append(" with [").append(WebTools.getClientIp(request)).append("]> error Address: ").append(request.getMethod()).append('>').append(request.getRequestURL()).append('(').append(this.printParamMap(request.getParameterMap())).append(')').toString());
			}
			// 记录请求错误日志
			InfoControl.getInstantce().putErrorRequest(request, response, link, l);
			// 将线程中的clientsession信息移除，针对动态数据库连接池
			RunThreadLocal.getInstance().removeUserSession(WebBaseConstants.TL_KEY);
		}
	}

	/**
	 * 放入跨域信息
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年11月29日_下午3:21:15
	 * @param request 请求的消息
	 * @param response 返回用消息
	 */
	private void putAccessAllow(final HttpServletRequest request, final HttpServletResponse response) {
		// 是可以被跨域访问的
		String host = request.getHeader("Referer");
		if (null == host) {
			host = "*";
		} else {
			final int eh = host.indexOf(Constants.DIAGONAL_LINE, 10);
			if (eh != -1) {
				host = host.substring(0, eh);
			} else {
				host = "*";
			}
		}
		response.addHeader("Access-Control-Allow-Origin", host);
		response.addHeader("Access-Control-Allow-Credentials", "true");
	}

	/**
	 * 回到请求的来源页面
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-10-10 下午5:57:33
	 * @param request 请求数据
	 * @param response 返回数据
	 * @param errorMap 错误数据集合
	 * @throws IOException 抛
	 */
	private void backToReferer(final HttpServletRequest request, final HttpServletResponse response, final Map<String, String> errorMap) throws IOException {
		final StringBuilder sb = new StringBuilder(WebBaseConstants.REQUEST_KEY_ERROR_BACK.length() + 6 + (errorMap.size() * 16));
		sb.append(WebBaseConstants.REQUEST_KEY_ERROR_BACK);
		sb.append("={");
		boolean isFirst = true;
		for (final Entry<String, String> ent : errorMap.entrySet()) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(',');
			}
			sb.append(ent.getKey()).append('=').append(ent.getValue());
		}
		sb.append('}');
		final String ref = request.getHeader("Referer");
		final StringBuilder formUrl = new StringBuilder((ref == null ? 0 : ref.length()) + sb.length());
		if (null != ref) {
			formUrl.append(ref);
			if (ref.indexOf("?") != -1) {
				formUrl.append('&');
			} else {
				formUrl.append('?');
			}
			formUrl.append(sb);
			response.sendRedirect(formUrl.toString());
		}
	}

	/**
	 * 打印（生成字串）请求参数
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-28 下午6:16:19
	 * @param map 参数集合
	 * @return 生成的请求参数串
	 */
	private String printParamMap(final Map<String, String[]> map) {
		final StringBuilder sb = new StringBuilder();
		int i = 0, j;
		for (final Entry<String, String[]> ent : map.entrySet()) {
			if (i++ != 0) {
				sb.append(',');
			}
			sb.append(ent.getKey()).append('=');
			switch (ent.getValue().length) {
			case 0:
				break;
			case 1:
				sb.append(ent.getValue()[0]);
				break;
			default:
				j = 0;
				sb.append('[');
				for (final String str : ent.getValue()) {
					if (j++ != 0) {
						sb.append(',');
					}
					sb.append(str);
				}
				sb.append(']');
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * 临时帐号对象
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-10-10 下午7:07:05
	 */
	public class TmpSessionBean {

		/**
		 * @author Weijie Xu
		 * @dateTime 2017年4月12日 上午11:40:07
		 */
		private ClientSessionBean csb = null;

		/**
		 * 设置客户端会话
		 * 
		 * @author Weijie Xu
		 * @dateTime 2017年4月12日 上午11:42:48
		 * @param csb 客户端会话
		 */
		public void setClientSession(final ClientSessionBean csb) {
			this.csb = csb;
		}

		/**
		 * 得到客户端会话
		 *
		 * @author Weijie Xu
		 * @dateTime 2017年4月12日 上午11:42:49
		 * @return 客户端会话
		 */
		public ClientSessionBean getClientSession() {
			return this.csb;
		}
	}
}
