/**
 * @author xuweijie
 * @dateTime 2012-1-31 下午3:13:56
 */
package com.tfzzh.view.web.link;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.util.ParameterMap;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.tfzzh.core.control.tools.ManagerMap;
import com.tfzzh.exception.InitializeException;
import com.tfzzh.log.CoreLog;
import com.tfzzh.tools.Constants;
import com.tfzzh.tools.InnerIndex;
import com.tfzzh.tools.RunThreadLocal;
import com.tfzzh.tools.StringTools;
import com.tfzzh.view.web.annotation.LinkMain;
import com.tfzzh.view.web.bean.BaseParamBean;
import com.tfzzh.view.web.bean.LoginSessionBean;
import com.tfzzh.view.web.bean.UploadParamBean;
import com.tfzzh.view.web.iface.TokenControl;
import com.tfzzh.view.web.servlet.CoreHttpServlet.TmpSessionBean;
import com.tfzzh.view.web.servlet.session.ClientSessionBean;
import com.tfzzh.view.web.servlet.session.SessionPool;
import com.tfzzh.view.web.tools.IpRestrictionController;
import com.tfzzh.view.web.tools.RequestMethod;
import com.tfzzh.view.web.tools.WebBaseConstants;
import com.tfzzh.view.web.tools.WebConstants;
import com.tfzzh.view.web.tools.WebTools;

/**
 * 连接操作
 * 
 * @author xuweijie
 * @dateTime 2012-1-31 下午3:13:56
 */
public class OperateLink {

	/**
	 * @author tfzzh
	 * @dateTime 2021年7月5日 下午12:41:15
	 */
	private static final String S_JSON = "/json";

	/**
	 * @author tfzzh
	 * @dateTime 2021年7月5日 下午12:39:15
	 */
	private static final String S_BOUNDARY = "boundary=";

	/**
	 * @author tfzzh
	 * @dateTime 2021年7月5日 下午12:40:09
	 */
	private static final String S_X_WWW_FORM_URLENCODED = "/x-www-form-urlencoded";

	/**
	 * @author tfzzh
	 * @dateTime 2021年7月5日 下午2:20:20
	 */
	private static final String S_CONTENT_DISPOSITION = "content-disposition: form-data; name=\"";

	/**
	 * www-form传参值的开始部分
	 * 
	 * @author tfzzh
	 * @dateTime 2021年7月5日 下午2:30:59
	 */
	private static final String S_X_WWW_VAL_START = "\r\n\r\n";

	/**
	 * www-form传参值的结束部分
	 * 
	 * @author tfzzh
	 * @dateTime 2021年7月5日 下午2:31:00
	 */
	private static final String S_X_WWW_VAL_END = "\r\n";

	/**
	 * @author tfzzh
	 * @dateTime 2022年5月27日 下午12:14:29
	 */
	private static final String MMP = "m_m_p";

	/**
	 * 链接通配符
	 * 
	 * @author tfzzh
	 * @dateTime 2022年5月27日 下午12:17:08
	 */
	private static final String LINK_WILDCARD = "*";

	/**
	 * 路径层级列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月8日 下午6:38:40
	 */
	private final Map<String, Object> pathLvls = new ConcurrentHashMap<>();

	/**
	 * 对象实例
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-1 下午2:09:43
	 */
	private static final OperateLink operate = new OperateLink();

	/**
	 * @author xuweijie
	 * @dateTime 2012-2-1 下午2:09:41
	 */
	private OperateLink() {
	}

	/**
	 * 得到唯一实例
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-1 下午2:09:38
	 * @return 对象实例
	 */
	public static OperateLink getInstance() {
		return OperateLink.operate;
	}

	/**
	 * 增加一个新的常规连接信息<br />
	 * 该方法不能用于增加分流模式信息<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-7 下午5:03:35
	 * @param mainPath 主路径
	 * @param path 子路径/分流ID
	 * @param matchingPath 匹配用路径
	 * @param method 请求的方法
	 * @param type 链接类型
	 * @param reflectControlKey 反射控制键
	 * @param prefix 路径前缀
	 * @param targetNode 目标节点连接
	 * @param result 结果控制集合
	 * @param params 参数集合，有序的<br />
	 *           <参数对应key,参数的对象类型><br />
	 * @param readStreamJSON 是否读取stream相关json信息 add 2023-11-24
	 * @param canCrossDomain 是否可以跨域，true，可以跨域
	 * @param needToken 是否需要token验证
	 * @param description 说明
	 * @param ipRestr ip限制标识
	 * @return 控制连接
	 */
	public OperateLinkInfo addNewLinkInfo(final String mainPath, final String path, final String matchingPath, final RequestMethod method, final LinkType type, final String reflectControlKey, final String prefix, final OperateLinkNodeInfo targetNode, final String[] result, final Map<String, Class<?>> params, final boolean readStreamJSON, final boolean canCrossDomain, final boolean needToken, final String description, final String ipRestr) {
		// * @param accessPermissions 访问权限值 del 2023-11-24
		return this.addNewLinkInfo(mainPath, path, matchingPath, method, type, null, reflectControlKey, prefix, targetNode, result, params, readStreamJSON, canCrossDomain, needToken, description, ipRestr);
	}

	/**
	 * 增加一个新的常规连接信息
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-31 下午4:50:53
	 * @param mainPath 主路径
	 * @param path 子路径/分流ID
	 * @param matchingPath 匹配用路径
	 * @param method 请求的方法
	 * @param type 链接类型
	 * @param branchKey 分流KEY，针对分流模式
	 * @param reflectControlKey 反射控制键
	 * @param prefix 路径前缀
	 * @param targetNode 目标节点连接
	 * @param result 结果控制集合
	 * @param params 参数集合，有序的<br />
	 *           <参数对应key,参数的对象类型><br />
	 * @param readStreamJSON 是否读取stream相关json信息 add 2023-11-24
	 * @param canCrossDomain 是否可以跨域，true，可以跨域
	 * @param needToken 是否需要token验证
	 * @param description 说明
	 * @param ipRestr ip限制标识
	 * @return 控制连接
	 */
	@SuppressWarnings("unchecked")
	public OperateLinkInfo addNewLinkInfo(String mainPath, final String path, final String matchingPath, final RequestMethod method, final LinkType type, final String branchKey, final String reflectControlKey, final String prefix, final OperateLinkNodeInfo targetNode, final String[] result, final Map<String, Class<?>> params, final boolean readStreamJSON, final boolean canCrossDomain, final boolean needToken, final String description, final String ipRestr) {
		// * @param accessPermissions 访问权限值 del 2023-11-24
		if ((null == mainPath) || ((mainPath = mainPath.trim()).length() == 0) || mainPath.equals(Constants.DIAGONAL_LINE)) {
			mainPath = "";
			// lvl = 0;
		} else {
			if (mainPath.startsWith(Constants.DIAGONAL_LINE)) {
				mainPath = mainPath.substring(1);
			}
			if (mainPath.endsWith(Constants.DIAGONAL_LINE)) {
				mainPath = mainPath.substring(0, mainPath.length() - 1);
			}
		}
		{ // 新增的处理方式 2017-04-08
			final List<String> sl = StringTools.splitToArray(mainPath + Constants.DIAGONAL_LINE + matchingPath, Constants.DIAGONAL_LINE);
			Object toj;
			Map<String, Object> tm = this.pathLvls;
			Map<String, Object> ctm;
			for (final String s : sl) {
				toj = tm.get(s);
				if (null == toj) {
					// 因没有而创建
					ctm = new ConcurrentHashMap<>();
					tm.put(s, ctm);
				} else {
					ctm = (Map<String, Object>) toj;
				}
				tm = ctm;
			}
			// 创建信息
			final OperateLinkInfo link = this.createOperateLinkInfo(type, path, reflectControlKey, prefix, targetNode, result, params, readStreamJSON, canCrossDomain, needToken, description, ipRestr);
			Map<RequestMethod, OperateLinkList<? extends OperateLinkInfo>> methodMap;
			// methodMap = (Map<RequestMethod, OperateLinkList<? extends OperateLinkInfo>>) tm.get("m_m_p");
			methodMap = (Map<RequestMethod, OperateLinkList<? extends OperateLinkInfo>>) tm.get(OperateLink.MMP);
			if (null == methodMap) {
				methodMap = new HashMap<>();
				// tm.put("m_m_p", methodMap);
				tm.put(OperateLink.MMP, methodMap);
			}
			final OperateLinkList<? extends OperateLinkInfo> linkList = this.createOperateLinkList(type, branchKey);
			methodMap.put(method, linkList);
			// 放入消息到队列
			if (!linkList.addLink(matchingPath, link)) {
				// 因为已经存在，抛出异常信息，仅有最后一个else时，才可能出现该情况
				throw new InitializeException("Exist same LinkInfo: " + mainPath + " - " + path);
			}
			return link;
		}
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2013-12-21 下午4:59:54
	 * @param linkMain 链接主信息
	 * @param targetNode 目标节点连接
	 * @return 控制连接
	 */
	public OperateLinkNodeInfo addNewLinkNodeInfo(final LinkMain linkMain, final OperateLinkNodeInfo targetNode) {
		// public OperateLinkNodeInfo addNewLinkNodeInfo(final LinkMain linkMain, final OperateLinkNodeInfo targetNode) {
		final String mainPath = linkMain.mainPath();
		final String[] result = linkMain.resultList();
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
		// return new OperateLinkNodeInfo(mainPath, linkMain.accessPermissions(), linkMain.description(), targetNode, result);
		return new OperateLinkNodeInfo(result);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2013-12-21 下午5:25:59
	 * @param linkMain 链接主信息
	 * @return 控制连接
	 */
	public OperateLinkNodeInfo addNewLinkNodeInfo(final LinkMain linkMain) {
		return new OperateLinkNodeInfo(linkMain.resultList());
	}
	// * @param linkBranch 分流连接信息
	// * @param linkMain 链接主信息
	// * @param targetNode 目标节点连接
	// * @return 控制连接
	// public OperateLinkNodeInfo addNewLinkNodeInfo(final LinkBranch linkBranch, final LinkMain linkMain, final OperateLinkNodeInfo targetNode) {
	// return new OperateLinkNodeInfo(linkBranch.path(), linkBranch.accessPermissions(), linkBranch.description(), targetNode, linkMain.resultList());
	// }

	/**
	 * 创建一个控制连接信息
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-6 下午3:19:48
	 * @param type 连接类型
	 * @param deployPath 适配路径，针对适配模式
	 * @param reflectControlKey 反射控制键
	 * @param prefix 路径前缀
	 * @param targetNode 目标节点连接
	 * @param result 结果控制集合
	 * @param params 参数集合，有序的<br />
	 *           <参数对应key,参数的对象类型><br />
	 * @param readStreamJSON 是否读取stream相关json信息 add 2023-11-24
	 * @param canCrossDomain 是否可以跨域，true，可以跨域
	 * @param needToken 是否需要token验证
	 * @param description 说明
	 * @param ipRestr ip限制标识
	 * @return 控制连接
	 */
	private OperateLinkInfo createOperateLinkInfo(final LinkType type, final String deployPath, final String reflectControlKey, final String prefix, final OperateLinkNodeInfo targetNode, final String[] result, final Map<String, Class<?>> params, final boolean readStreamJSON, final boolean canCrossDomain, final boolean needToken, final String description, final String ipRestr) {
		// * @param accessPermissions 访问权限值 del 2023-11-24
		switch (type) {
		case Normal:
			return new NormalOperateLinkInfo(reflectControlKey, prefix, targetNode, result, params, readStreamJSON, canCrossDomain, needToken, description, ipRestr);
		case Deploy:
			return new DeployOperateLinkInfo(deployPath, reflectControlKey, prefix, targetNode, result, params, readStreamJSON, canCrossDomain, needToken, description, ipRestr);
		case Branch:
			return new BranchOperateLinkInfo(reflectControlKey, prefix, targetNode, result, params, readStreamJSON, canCrossDomain, needToken, description, ipRestr);
		default: // 不可能出现的情况
			throw new InitializeException("Exist not type: " + type.name());
		}
	}

	/**
	 * 创建一个控制链接列表
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-3 下午4:37:22
	 * @param type 连接类型
	 * @param branchKey 针对创建分流模式
	 * @return 新的控制链接列表
	 */
	private OperateLinkList<? extends OperateLinkInfo> createOperateLinkList(final LinkType type, final String branchKey) {
		switch (type) {
		case Normal:
			return new NormalOperateLinkList();
		case Deploy:
			return new DeployOperateLinkList();
		case Branch:
			return new BranchOperateLinkList(branchKey);
		default: // 不可能出现的情况
			throw new InitializeException("Exist not type: " + type.name());
		}
	}

	/**
	 * 根据链接条件得到链接信息
	 * 
	 * @author tfzzh
	 * @dateTime 2024年7月20日 05:31:46
	 * @param request 请求信息
	 * @param pathParas 路径参数，针对适配情况
	 * @return 控制连接
	 */
	public OperateLinkInfo getOperateLink(final HttpServletRequest request, final Map<String, List<String>> pathParas) {
		final String pathInfo = request.getPathInfo();
		final String path;
		String sPath = request.getServletPath();
		if (sPath.length() > 0) {
			sPath = sPath.substring(1);
		}
		if (null == pathInfo) {
			path = sPath;
		} else {
			if (sPath.length() > 0) {
				path = Constants.DIAGONAL_LINE + sPath + pathInfo;
			} else {
				path = pathInfo;
			}
		}
		return this.getOperateLink(path, request, pathParas);
	}

	/**
	 * 根据链接条件得到链接信息
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-2 上午11:36:27
	 * @param path 目标路径
	 * @param request 请求信息
	 * @param pathParas 路径参数，针对适配情况
	 * @return 控制连接
	 */
	@SuppressWarnings("unchecked")
	public OperateLinkInfo getOperateLink(final String path, final HttpServletRequest request, final Map<String, List<String>> pathParas) {
		// final String pathInfo = request.getPathInfo();
		// final String path;
		// String sPath = request.getServletPath();
		// if (sPath.length() > 0) {
		// sPath = sPath.substring(1);
		// }
		// if (null == pathInfo) {
		// path = sPath;
		// } else {
		// if (sPath.length() > 0) {
		// path = Constants.DIAGONAL_LINE + sPath + pathInfo;
		// } else {
		// path = pathInfo;
		// }
		// }
		final RequestMethod method = RequestMethod.getMethod(request.getMethod());
		if (method != RequestMethod.Non) {
			final String[] str = StringTools.split(path, Constants.DIAGONAL_LINE);
			{
				String last = str[str.length - 1];
				int i;
				if ((i = last.lastIndexOf(Constants.SPOT)) != -1) {
					// 放入后缀信息
					final List<String> cL = new LinkedList<>();
					pathParas.put(WebConstants.PRIVATE_LINK_NAME_SUFFIX, cL);
					cL.add(last.substring(i + 1));
					last = last.substring(0, i);
				}
				str[str.length - 1] = last;
			}
			Object toj = null;
			int lvl = 0;
			boolean effAdd = false;
			Map<String, Object> tm = this.pathLvls;
			for (final String s : str) {
				toj = tm.get(s);
				if (null == toj) {
					// 不直接存在，判定是否存在通配符
					toj = tm.get("@");
					if (null == toj) {
						// 是最终节点，取到内容后跳出
						break;
					}
					if (!effAdd) {
						effAdd = true;
						lvl++; // 多加一层
					}
				}
				tm = (Map<String, Object>) toj;
				if (!effAdd) {
					lvl++;
				}
			}
			if (lvl > 0) {
				lvl--;
			}
			// toj = tm.get("m_m_p");
			toj = tm.get(OperateLink.MMP);
			if (null == toj) {
				tm = (Map<String, Object>) tm.get(OperateLink.LINK_WILDCARD);
				if (null == tm) {
					return null;
				}
				toj = tm.get(OperateLink.MMP);
				if (null == toj) {
					return null;
				}
			}
			final Map<RequestMethod, OperateLinkList<? extends OperateLinkInfo>> methodMap = (Map<RequestMethod, OperateLinkList<? extends OperateLinkInfo>>) toj;
			final OperateLinkList<? extends OperateLinkInfo> linkList = methodMap.get(method);
			if (null == linkList) {
				return null;
			}
			OperateLinkInfo info;
			final StringBuffer sb = new StringBuffer();
			for (int i = lvl; i < str.length; i++) {
				if (i != lvl) {
					sb.append('/');
				}
				sb.append(str[i]);
			}
			// 进行获取判定
			if (null != (info = linkList.getLink(sb.toString(), request))) {
				final Map<String, List<String>> map;
				if (null != (map = info.getKeyBack(str))) {
					// 放入信息
					pathParas.putAll(map);
				}
			} else if (null != (info = linkList.getLink(OperateLink.LINK_WILDCARD, request))) {
				final Map<String, List<String>> map;
				if (null != (map = info.getKeyBack(str))) {
					// 放入信息
					pathParas.putAll(map);
				}
				final List<String> sl = new ArrayList<>(1);
				sl.add(path);
				pathParas.put("lwPath", sl);
			}
			return info;
		} // 一定错误的情况
			// // 因为失败，可以考虑记录该错误消息
		return null;
	}

	/**
	 * 控制链接列表
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-2 下午1:12:46
	 * @param <T> 控制连接子类
	 */
	public abstract class OperateLinkList<T extends OperateLinkInfo> {

		/**
		 * 验证连接类型
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-3 下午4:41:05
		 * @param type 待比较的类型
		 * @return true，相同类型；<br />
		 *         false，不同类型；<br />
		 */
		protected abstract boolean validateLinkType(LinkType type);

		/**
		 * 为了转换而特别增加
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-6 下午2:51:58
		 * @param key 子路径/适配信息/分流ID
		 * @param info 控制连接
		 * @return true，放入成功；<br />
		 *         false，放入失败，因为已经存在一个相同的key内容，不能覆盖；<br />
		 */
		@SuppressWarnings("unchecked")
		private boolean addLink(final String key, final OperateLinkInfo info) {
			return this.addLink((T) info, key);
		}

		/**
		 * 增加一个链接信息
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-2 下午1:19:35
		 * @param info 控制连接
		 * @param key 子路径/适配信息/分流ID
		 * @return true，放入成功；<br />
		 *         false，放入失败，因为已经存在一个相同的key内容，不能覆盖；<br />
		 */
		protected abstract boolean addLink(final T info, final String key);

		/**
		 * 得到连接信息
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-2 下午6:24:47
		 * @param path 剩余的地址部分
		 * @param request 请求消息
		 * @return 连接信息
		 */
		protected abstract T getLink(final String path, final HttpServletRequest request);
	}

	/**
	 * 正常模式的控制链接列表
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-3 下午3:09:44
	 */
	public class NormalOperateLinkList extends OperateLinkList<NormalOperateLinkInfo> {

		/**
		 * 连接数据
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-3 下午4:35:35
		 */
		private final Map<String, NormalOperateLinkInfo> datas = new TreeMap<>();

		/**
		 * 增加一个连接信息<br />
		 * 一个路径名称对应一种操作<br />
		 * 该路径没有层级，即没有符号“/”，且不存在后缀名部分<br />
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-3 下午5:18:46
		 * @see com.tfzzh.view.web.link.OperateLink.OperateLinkList#addLink(com.tfzzh.view.web.link.OperateLink.OperateLinkInfo, java.lang.String)
		 */
		@Override
		protected boolean addLink(final NormalOperateLinkInfo info, String key) {
			if (key.startsWith(Constants.DIAGONAL_LINE)) {
				key = key.substring(1);
			}
			if (this.datas.containsKey(key)) {
				return false;
			} else {
				this.datas.put(key, info);
				return true;
			}
		}

		/**
		 * 得到连接信息
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-3 下午5:01:28
		 * @see com.tfzzh.view.web.link.OperateLink.OperateLinkList#getLink(java.lang.String, javax.servlet.http.HttpServletRequest)
		 */
		@Override
		protected NormalOperateLinkInfo getLink(final String path, final HttpServletRequest request) {
			return this.datas.get(path);
		}

		/**
		 * @author xuweijie
		 * @dateTime 2012-2-3 下午4:42:13
		 * @see com.tfzzh.view.web.link.OperateLink.OperateLinkList#validateLinkType(com.tfzzh.view.web.link.LinkType)
		 */
		@Override
		protected boolean validateLinkType(final LinkType type) {
			return type == LinkType.Normal;
		}
	}

	/**
	 * 适配模式的控制链接列表<br />
	 * 主要根据剩余层级进行一次判定<br />
	 * 然后是层级中的优先级及关键位置判定<br />
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-3 下午3:10:14
	 */
	public class DeployOperateLinkList extends OperateLinkList<DeployOperateLinkInfo> {

		/**
		 * 连接数据
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-3 下午6:13:57
		 */
		private final Map<Integer, List<DeployOperateLinkInfo>> datas = new TreeMap<>();

		/**
		 * @author xuweijie
		 * @dateTime 2012-2-6 下午12:03:27
		 * @see com.tfzzh.view.web.link.OperateLink.OperateLinkList#addLink(com.tfzzh.view.web.link.OperateLink.OperateLinkInfo, java.lang.String)
		 */
		@Override
		protected boolean addLink(final DeployOperateLinkInfo info, final String path) {
			// 分解path
			final List<String> paths = StringTools.splitToArray(path, Constants.DIAGONAL_LINE);
			// 层级
			int tier = 0;
			boolean save = false;
			for (final String p : paths) {
				if ("@".equals(p)) {
					save = true;
				}
				if (save) {
					tier++;
				}
			}
			if (tier == 0) {
				// 配置的链接内容，不可不存在层级
				throw new InitializeException("Error Deploy Link: no tier!");
			}
			List<DeployOperateLinkInfo> list;
			if (null == (list = this.datas.get(tier))) {
				// 新增的部分
				list = new LinkedList<>();
				this.datas.put(tier, list);
				list.add(info);
				return true;
			} else {
				// 优先进行比较操作
				for (final DeployOperateLinkInfo old : list) {
					if (old.equals(info)) {
						return false;
					}
				}
				list.add(info);
				return true;
			}
		}

		/**
		 * @author xuweijie
		 * @dateTime 2012-2-6 下午12:01:29
		 * @see com.tfzzh.view.web.link.OperateLink.OperateLinkList#getLink(java.lang.String, javax.servlet.http.HttpServletRequest)
		 */
		@Override
		protected DeployOperateLinkInfo getLink(final String path, final HttpServletRequest request) {
			// 进行可用层级判定
			// 分解path
			final String[] paths = path.split("[/]");
			// 层级
			final int tier = paths.length;
			final List<DeployOperateLinkInfo> list;
			if (null == (list = this.datas.get(tier))) {
				// 不存在目标层级的数据
				return null;
			} else {
				for (final DeployOperateLinkInfo info : list) {
					if (info.validatePath(path)) {
						return info;
					}
				}
				return null;
			}
		}

		/**
		 * 验证连接类型
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-3 下午4:42:13
		 * @see com.tfzzh.view.web.link.OperateLink.OperateLinkList#validateLinkType(com.tfzzh.view.web.link.LinkType)
		 */
		@Override
		protected boolean validateLinkType(final LinkType type) {
			return type == LinkType.Deploy;
		}
	}

	/**
	 * 分流模式的控制链接列表
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-3 下午3:10:22
	 */
	public class BranchOperateLinkList extends OperateLinkList<BranchOperateLinkInfo> {

		/**
		 * 主要参数key
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-7 下午5:07:50
		 */
		private final String key;

		/**
		 * 参数模式下的连接数据
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-7 下午2:59:11
		 */
		private final Map<String, BranchOperateLinkInfo> datas = new TreeMap<>();

		/**
		 * @author xuweijie
		 * @dateTime 2012-2-7 下午5:06:31
		 * @param key 分流用参数key
		 */
		public BranchOperateLinkList(final String key) {
			this.key = key;
		}

		/**
		 * @author xuweijie
		 * @dateTime 2012-2-6 下午12:03:37
		 * @see com.tfzzh.view.web.link.OperateLink.OperateLinkList#addLink(com.tfzzh.view.web.link.OperateLink.OperateLinkInfo, java.lang.String)
		 */
		@Override
		protected boolean addLink(final BranchOperateLinkInfo info, String key) {
			if (key.startsWith(Constants.DIAGONAL_LINE)) {
				key = key.substring(1);
			}
			// 是参数类型
			if (this.datas.containsKey(key)) {
				return false;
			} else {
				this.datas.put(key, info);
				return true;
			}
		}

		/**
		 * @author xuweijie
		 * @dateTime 2012-2-6 下午12:01:19
		 * @see com.tfzzh.view.web.link.OperateLink.OperateLinkList#getLink(java.lang.String, javax.servlet.http.HttpServletRequest)
		 */
		@Override
		protected BranchOperateLinkInfo getLink(final String path, final HttpServletRequest request) {
			final String con = request.getParameter(this.key);
			if (null == con) {
				return null;
			} else {
				return this.datas.get(con);
			}
		}

		/**
		 * 验证连接类型
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-3 下午4:42:13
		 * @see com.tfzzh.view.web.link.OperateLink.OperateLinkList#validateLinkType(com.tfzzh.view.web.link.LinkType)
		 */
		@Override
		protected boolean validateLinkType(final LinkType type) {
			return type == LinkType.Branch;
		}
	}

	/**
	 * 控制连接
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-31 下午3:20:01
	 */
	public abstract class OperateLinkInfo {

		/**
		 * 反射控制键
		 * 
		 * @author xuweijie
		 * @dateTime 2012-1-31 下午3:15:59
		 */
		private final String reflectControlKey;

		/**
		 * 路径前缀
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-15 下午3:30:55
		 */
		private final String prefix;
		// /**
		// * 访问权限值
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-9 下午3:05:50
		// */
		// private final int accessPermissionsValue;
		// /**
		// * 访问权限对象
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-11 下午8:05:08
		// */
		// private AccessPermissionsInfo accessPermissions = null;

		/**
		 * 目标节点连接
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-7-17 下午9:23:32
		 */
		private final OperateLinkNodeInfo targetNode;

		/**
		 * 目标操作
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-7-5 下午1:40:36
		 */
		private final Map<String, BackLinkOperationInfo> targets;

		/**
		 * 参数集合，有序的<br />
		 * <参数对应key,参数的对象类型><br />
		 * 
		 * @author xuweijie
		 * @dateTime 2012-1-31 下午3:15:43
		 */
		private final Map<String, Class<?>> params;

		/**
		 * 是否读取stream相关json信息
		 * 
		 * @author tfzzh
		 * @dateTime 2023年11月24日 11:52:31
		 */
		private final boolean readStreamJSON;

		/**
		 * 是否可以跨域
		 * 
		 * @author Xu Weijie
		 * @datetime 2017年11月29日_下午3:11:06
		 */
		private final boolean canCrossDomain;

		/**
		 * 是否需要token验证
		 * 
		 * @author Xu Weijie
		 * @datetime 2017年11月6日_下午3:36:31
		 */
		private final boolean needToken;

		/**
		 * 可上传文件最大字节数限制
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-9-12 下午1:16:46
		 */
		private final int fileMaxSize;

		/**
		 * 目标文件后缀名列表
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-9-12 下午1:29:59
		 */
		private final String fileSuffixLimit;
		// /**
		// * 说明
		// *
		// * @author xuweijie
		// * @dateTime 2012-2-7 下午6:16:32
		// */
		// private final String description;

		/**
		 * ip限制标识
		 * 
		 * @author Xu Weijie
		 * @datetime 2017年8月23日_下午6:57:21
		 */
		private final String ipRestr;

		/**
		 * @author xuweijie
		 * @dateTime 2012-1-31 下午6:07:29
		 * @param reflectControlKey 反射控制键
		 * @param prefix 路径前缀
		 * @param targetNode 目标节点连接
		 * @param result 结果控制集合
		 * @param params 参数集合，有序的<br />
		 *           <参数对应key,参数的对象类型><br />
		 * @param readStreamJSON 是否读取stream相关json信息 add 2023-11-24
		 * @param canCrossDomain 是否可以跨域，true，可以跨域
		 * @param needToken 是否需要token验证
		 * @param description 说明
		 * @param ipRestr ip限制标识
		 */
		public OperateLinkInfo(final String reflectControlKey, final String prefix, final OperateLinkNodeInfo targetNode, final String[] result, final Map<String, Class<?>> params, final boolean readStreamJSON, final boolean canCrossDomain, final boolean needToken, final String description, final String ipRestr) {
			// * @param accessPermissions 访问权限值 del 2023-11-24
			this.reflectControlKey = reflectControlKey;
			// this.accessPermissionsValue = accessPermissions;
			this.prefix = prefix;
			this.targetNode = targetNode;
			this.targets = new TmpTool().getResultMap(result);
			this.params = params;
			this.readStreamJSON = readStreamJSON; // add 2023-11-24
			this.canCrossDomain = canCrossDomain;
			this.needToken = needToken;
			{ // 进行是否存在上传文件，以及可上传文件大小限制设置
				int maxSize = 0;
				String suffixLimit = "";
				for (final Class<?> clz : this.params.values()) {
					if (UploadParamBean.class.isAssignableFrom(clz)) {
						// 属于有文件上传的请求
						try {
							// 纯临时对象
							final UploadParamBean tmp = (UploadParamBean) clz.getDeclaredConstructor().newInstance();
							if (maxSize < tmp.getMaxSize()) {
								maxSize = tmp.getMaxSize();
							}
							suffixLimit += tmp.getSuffixLimit();
						} catch (final Exception e) {
							// 无信息
						}
					}
				}
				this.fileMaxSize = maxSize;
				this.fileSuffixLimit = suffixLimit;
			}
			// this.description = description;
			this.ipRestr = ipRestr;
			// AccessPermissionsControl.getInstance().putAccessPermissions(this);
		}

		/**
		 * 得到反射控制键
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-1 下午1:34:47
		 * @return the reflectControlKey
		 */
		public String getReflectControlKey() {
			return this.reflectControlKey;
		}

		/**
		 * 得到路径前缀
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-15 下午3:31:13
		 * @return the prefix
		 */
		public String getPrefix() {
			return this.prefix;
		}

		/**
		 * 验证传递的参数信息；<br />
		 * 权限的验证；<br />
		 * 参数有效性验证；<br />
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-9-13 下午12:55:15
		 * @param pathParas 路径相关参数
		 * @param requestParas 被请求的所有可以被收集到的参数的集合，用于接续的操作
		 * @param request 请求信息
		 * @param response 返回信息
		 * @param tab 临时帐号对象
		 * @param methodParas 方法用参数对象集合
		 * @param paraLog 参数日志信息，用于记录
		 * @param errorMap 错误信息集合
		 * @return 0，正确情况；<br />
		 *         1，权限错误情况；<br />
		 *         2，上传文件超过预期限制，针对文件上传部分；<br />
		 *         3，文件类型非限定范围内；<br />
		 *         4，文件上传时发生位置错误；<br />
		 *         7，对应Param对象处理问题；<br />
		 *         8，有不可为null的字段为null；<br />
		 *         9，Param对象必须为BaseParamBean子类；<br />
		 */
		public short validateParas(final Map<String, List<String>> pathParas, final Map<String, Object> requestParas, final HttpServletRequest request, final HttpServletResponse response, final TmpSessionBean tab, final Object[] methodParas, final StringBuilder paraLog, final Map<String, String> errorMap) {
			// 首先验证IP add by xwj 2017-08-23
			if (null != this.ipRestr) {
				// 是存在ip验证需求的
				final IpRestrictionController irc = IpRestrictionController.getInstance();
				if (null != irc) {
					// 确实存在验证实现操作，仅针对第一IP做验证
					// final String ip = WebTools.getClientIp(request); change xwj 2017-10-17
					final String ip = WebTools.getFirstClientIp(request);
					if (!irc.validateIp(this.ipRestr, ip)) {
						// 验证未通过
						return 2;
					}
				}
			}
			// 分析出提交的参数信息
			final short back = this.handleRequestContant(request, requestParas, pathParas, errorMap);
			switch (back) {
			case 0: // ok的情况
				break;
			default: // 有问题的情况
				return back;
			}
			// 用户登陆信息
			ClientSessionBean cs = null;
			// if (WebBaseConstants.CUSTOM_SESSION) {
			final List<String> plns = pathParas.get(WebConstants.PRIVATE_LINK_NAME_SUFFIX);
			if (this.isNeedToken() && (request.getDispatcherType() != DispatcherType.ERROR) && ((null == plns) || ((WebBaseConstants.URL_POS != null) && WebBaseConstants.URL_POS.endsWith(plns.get(0))))) {
				// 是自定义session
				for (boolean b = true; b; b = false) { // 仅仅为了跳出的设置
					Object tokeno = request.getHeader(WebBaseConstants.CUSTOM_SESSION_TOKEN_KEY);
					if (null == tokeno) {
						tokeno = requestParas.get(WebBaseConstants.CUSTOM_SESSION_TOKEN_KEY);
					}
					String token = null, clientId = null, clientScope = null;
					if ((null != tokeno)) {
						// 得到token
						if (tokeno instanceof CharSequence) {
							token = ((CharSequence) tokeno).toString();
						} else if (tokeno instanceof List) {
							final List<?> tl = (List<?>) tokeno;
							if (tl.size() > 0) {
								final Object ot = tl.get(0);
								if (null != ot) {
									token = ot.toString();
								}
							}
						} else {
							token = tokeno.toString();
						}
						// 是需要验证的情况
						Object clientIdo = request.getHeader(WebBaseConstants.CUSTOM_SESSION_NAME_KEY);
						if (null == clientIdo) {
							clientIdo = requestParas.get(WebBaseConstants.CUSTOM_SESSION_NAME_KEY);
						}
						if (null != clientIdo) {
							if (clientIdo instanceof List) {
								final List<?> cl = (List<?>) clientIdo;
								if (cl.size() > 0) {
									final Object ot = cl.get(0);
									if (null != ot) {
										clientId = ot.toString();
									}
								}
							} else {
								clientId = clientIdo.toString();
							}
							// add 2017-12-06
							if (null != clientId) {
								Object clientSdo = request.getHeader(WebBaseConstants.CUSTOM_SESSION_SCOPE_KEY);
								if (null == clientSdo) {
									clientSdo = requestParas.get(WebBaseConstants.CUSTOM_SESSION_SCOPE_KEY);
								}
								if (null != clientSdo) {
									if (clientSdo instanceof List) {
										final List<?> cl = (List<?>) clientSdo;
										if (cl.size() > 0) {
											final Object ot = cl.get(0);
											if (null != ot) {
												clientScope = ot.toString();
											}
										}
									} else {
										clientScope = clientSdo.toString();
									}
								}
							}
						}
						if (token.length() > 0) {
							cs = SessionPool.getInstance().getSessionByToken(token);
							if (null != cs) {
								// 因为存在，验证ClientId
								if (null != cs.getClientId()) {
									// 是需要验证的情况
									// final Object clientIdo = requestParas.get(WebBaseConstants.CUSTOM_SESSION_NAME_KEY);
									// if (null != clientIdo) {
									// if (clientIdo instanceof List) {
									// List<?> cl = (List<?>) clientIdo;
									// if (cl.size() > 0) {
									// Object ot = cl.get(0);
									// if (null != ot) {
									// clientId = ot.toString();
									// }
									// }
									// } else {
									// clientId = clientIdo.toString();
									// }
									if ((clientId != null) && (clientId.length() > 0)) {
										if (cs.getClientId().equals(clientId)) {
											// 是正确情况，可以跳出的
											break;
										} // 错误情况
									} // 错误情况
										// }// 错误情况
									cs = null;
									// token = null;
								} else {
									// 没有登录信息的，可以跳出的
									break;
								}
							} else if ((null != clientId) && (clientId.length() > 0)) {
								// try {
								// Integer.parseInt(clientId);
								// } catch (final Exception e) {
								// // 认为是无效数据
								// cs = SessionPool.getInstance().createSession(request, response, WebTools.getClientWebType(request));
								// token = null;
								// clientId = null;
								// break;
								// }
								// 从数据库得到对应数据
								final TokenControl tc = (TokenControl) ManagerMap.getInstance().getManager(WebBaseConstants.CUSTOM_SESSION_TOKEN_CONTROL_IMPL_NAME);
								if (null != tc) {
									// System.out.println("\tin TokenControl params >>[" + token + "][" + clientId + "]");
									// 得到目标对象
									final LoginSessionBean ls = tc.getLoginSessionByToken(token, clientId, clientScope);
									if (null != ls) {
										// 存在对应的用户数据
										// 创建新的clientsession
										cs = SessionPool.getInstance().createSession(request, response, token, WebTools.getClientWebType(request));
										// 直接进行登录
										cs.login(request, response, ls);
										break;
									}
								}
								// token = null;
								clientId = null;
								clientScope = null;
							} else {
								// token = null;
							}
						}
					}
					if (null == cs) {
						// 从cookie中获取token
						final Cookie[] cka = request.getCookies();
						String cToken = null;
						if (null != cka) {
							// String sn = request.getRemoteHost();
							// 便利cookie得到目标内容
							// System.out.println("\t>> cookie token >> start ");
							for (final Cookie ck : cka) {
								// System.out.println("cookie sn > " + sn + ":" + ck.getDomain() + Constants.DIAGONAL_LINE + ck.getPath() + "[" + ck.getName() + "=" + ck.getValue() + "]");
								// if (!sn.equals(ck.getDomain())) {
								// continue;
								// }
								if (WebBaseConstants.CUSTOM_SESSION_TOKEN_KEY.equals(ck.getName())) {
									cToken = ck.getValue();
									continue;
								} else if (WebBaseConstants.CUSTOM_SESSION_NAME_KEY.equals(ck.getName())) {
									clientId = ck.getValue();
									continue;
								} else if (WebBaseConstants.CUSTOM_SESSION_SCOPE_KEY.equals(ck.getName())) {
									clientScope = ck.getValue();
									continue;
								}
							}
							// System.out.println("\t\t>> cookie token >> over ");
						}
						if ((null != cToken) && (cToken.length() > 0)) {
							token = cToken;
							cs = SessionPool.getInstance().getSessionByToken(token);
							if (null != cs) {
								break;
							}
							if (null != clientId) {
								// try {
								// Integer.parseInt(clientId);
								// } catch (final Exception e) {
								// // 认为是无效数据
								// cs = SessionPool.getInstance().createSession(request, response, WebTools.getClientWebType(request));
								// token = null;
								// clientId = null;
								// break;
								// }
								// 从数据库得到对应数据
								final TokenControl tc = (TokenControl) ManagerMap.getInstance().getManager(WebBaseConstants.CUSTOM_SESSION_TOKEN_CONTROL_IMPL_NAME);
								if (null != tc) {
									// System.out.println("\tin TokenControl cookie >>[" + token + "][" + clientId + "]");
									// 得到目标对象
									final LoginSessionBean ls = tc.getLoginSessionByToken(token, clientId, clientScope);
									if (null != ls) {
										// 存在对应的用户数据
										// 创建新的clientsession
										cs = SessionPool.getInstance().createSession(request, response, token, WebTools.getClientWebType(request));
										// 直接进行登录
										cs.login(request, response, ls);
										break;
									}
								}
							}
						}
						// 创建全新
						if (null == token) {
							cs = SessionPool.getInstance().createSession(request, response, WebTools.getClientWebType(request));
						} else {
							cs = SessionPool.getInstance().createSession(request, response, token, WebTools.getClientWebType(request));
						}
					}
				}
			}
			tab.setClientSession(cs);
			// if (!this.validateAccessPermission(request, response, cs)) {
			// // 未通过权限验证
			// return 1;
			// }
			// 进行对象信息放入
			return this.handleParam(request, response, cs, requestParas, methodParas, paraLog, errorMap);
		}

		/**
		 * 分析请求的参数内容
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-9-15 上午2:30:31
		 * @param request 请求信息
		 * @param requestParas 被请求的所有可以被收集到的参数的集合，用于接续的操作
		 * @param pathParas 路径相关参数
		 * @param errorMap 错误信息集合
		 * @return 0，正确情况；<br />
		 *         2，上传文件超过预期限制，针对文件上传部分；<br />
		 *         3，文件类型非限定范围内；<br />
		 *         4，文件上传时发生位置错误；<br />
		 */
		@SuppressWarnings("unchecked")
		private short handleRequestContant(final HttpServletRequest request, final Map<String, Object> requestParas, final Map<String, List<String>> pathParas, final Map<String, String> errorMap) {
			String qs = request.getQueryString();
			if ((null != qs) && (qs.length() > 0)) {
				try {
					qs = URLDecoder.decode(qs, Constants.SYSTEM_CODE);
				} catch (final UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				final List<String> ls = StringTools.splitToArray(qs, "&");
				// System.out.println(" >> log 2021-07-05 >> request.getQueryString() >> " + ls);
				List<String> lss;
				for (final String s : ls) {
					lss = StringTools.splitToArray(s, Constants.EQUAL);
					switch (lss.size()) {
					case 0:
						continue;
					case 1:
						requestParas.put(lss.get(0), "");
						continue;
					default:
						requestParas.put(lss.get(0), lss.get(1));
						continue;
					}
				}
			}
			String ct = request.getContentType();
			if (null != ct) {
				ct = ct.toLowerCase();
			}
			// System.out.println(" >> log 2021-07-05 >> request.getContentType [" + ct + "]");
			if ("post".equals(request.getMethod().toLowerCase()) && (null != ct) && ct.startsWith(FileUploadBase.MULTIPART)) {
				// 是有上传文件
				// 继续参数控制操作
				final List<FileItem> fileList;
				final DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
				diskFileItemFactory.setSizeThreshold(WebBaseConstants.UPLOAD_FILE_SIZE_THRESHOLD);
				{ // 采用系统临时文件目录作为上传的临时目录
					final File tempfile = new File(System.getProperty("java.io.tmpdir"));
					diskFileItemFactory.setRepository(tempfile);
				}
				final ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);
				if (this.fileMaxSize > 0) {
					fileUpload.setSizeMax(this.fileMaxSize);
				}
				fileUpload.setHeaderEncoding(Constants.SYSTEM_CODE);
				try {
					fileList = fileUpload.parseRequest(request);
				} catch (final FileUploadException e) {
					e.printStackTrace();
					if (e instanceof SizeLimitExceededException) {
						// 是文件大小超过限制
						errorMap.put("sizeLimit", "文件不可超过1：" + this.fileMaxSize + "字节");
						return 2;
					} else {
						// 存在信息读取问题，返回错误信息
						errorMap.put("otherError", "错误信息是：" + e.getMessage());
						return 4;
					}
				}
				// 进行request中参数导入
				String fieldName;
				String str;
				StringBuilder sb;
				List<String> param;
				InputStreamReader isr;
				BufferedReader br;
				final Map<String, Object> map = new HashMap<>();
				for (final FileItem file : fileList) {
					if (null == file.getName()) {
						fieldName = file.getFieldName();
						// 该情况，是字段非文件
						try {
							// 分别创建为了关闭
							isr = new InputStreamReader(file.getInputStream());
							br = new BufferedReader(isr);
							if (null == (param = (List<String>) map.get(fieldName))) {
								// 因为不存在而创建新的
								map.put(fieldName, (param = new LinkedList<>()));
							}
							sb = new StringBuilder();
							// 读数据消息，组合字串
							while (null != (str = br.readLine())) {
								sb.append(str).append('\n');
							}
							// 防止因无内容而索引错误
							if (sb.length() > 0) {
								sb.delete(sb.length() - 1, sb.length());
							}
							// 放组合好的字串到消息列表中
							param.add(sb.toString());
							br.close();
							isr.close();
						} catch (final IOException e) {
							e.printStackTrace();
						}
					} else {
						if ((this.fileMaxSize == 0) || (file.getSize() <= this.fileMaxSize)) {
							// 文件大小ok
							if (this.fileSuffixLimit.indexOf("|*|") == -1) {
								// 需要验证文件名
								String fn = file.getFieldName();
								final int lastPoint = fn.lastIndexOf(Constants.SPOT);
								if (lastPoint >= 0) {
									fn = fn.substring(lastPoint) + 1;
									// 此时fn是文件后缀名
									if (this.fileSuffixLimit.indexOf("|" + fn + "|") == -1) {
										// 不存在目标后缀名，错误情况
										errorMap.put("suffixLimit", "文件类型限定为：" + this.fileSuffixLimit);
										return 3;
									}
								}
							} // 不需要验证文件名
							final Object ov = map.get(file.getFieldName());
							if (null == ov) {
								// 单个文件直接处理为个体
								map.put(file.getFieldName(), file);
							} else {
								// 数量处理为列表
								final List<FileItem> fil;
								if (ov instanceof List) {
									// 已经是列表，直接增加
									fil = (List<FileItem>) ov;
									fil.add(file);
								} else {
									// 新为列表，则变更map中值对象
									final FileItem fi = (FileItem) ov;
									fil = new ArrayList<>(2);
									fil.add(fi);
									map.put(file.getFieldName(), fil);
								}
								fil.add(file);
							}
						} else {
							errorMap.put("sizeLimit", "文件不可超过2：" + this.fileMaxSize + "字节");
							return 2;
						}
					}
				}
				// 放入路径参数信息
				requestParas.putAll(pathParas);
				// 放入参数
				requestParas.putAll(map);
				// System.out.println(" >> log 2021-07-05 >> multipart/ " + map);
			} else if ((null != ct) && (ct.indexOf(OperateLink.S_X_WWW_FORM_URLENCODED) != -1) && (ct.indexOf(OperateLink.S_BOUNDARY) != -1)) { // 2021-07-05 增加对x-www-form-urlencoded结构数据处理
				final int bdys = ct.indexOf(OperateLink.S_BOUNDARY);
				final int bdye = ct.indexOf(Constants.SEMICOLON, bdys);
				final String bdy;
				if (bdye == -1) {
					bdy = ct.substring(bdys + OperateLink.S_BOUNDARY.length());
				} else {
					bdy = ct.substring(bdys + OperateLink.S_BOUNDARY.length(), bdye);
				}
				// 是 form data 结构数据
				try (InputStreamReader reader = new InputStreamReader(request.getInputStream(), Constants.SYSTEM_CODE)) {
					final char[] buff = new char[1024];
					int len = 0;
					final StringBuilder sb = new StringBuilder();
					while ((len = reader.read(buff)) != -1) {
						sb.append(buff, 0, len);
					}
					// System.out.println(" >>> " + sb.toString());
					this.analysisFormUrlEncodeData(requestParas, sb.toString(), bdy);
				} catch (final UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (final IOException e) {
					e.printStackTrace();
				} catch (final JSONException e) {
					e.printStackTrace();
				}
				// System.out.println(" >> log 2021-07-05 >> read x-www-form-urlencoded boundary[" + ct + "]");
			} else {
				if ((null != ct) && (ct.indexOf(OperateLink.S_JSON) != -1)) {
					// TODO 是 json结构数据
					if (this.readStreamJSON) {
						try (InputStreamReader reader = new InputStreamReader(request.getInputStream(), Constants.SYSTEM_CODE)) {
							final char[] buff = new char[1024];
							int len = 0;
							final StringBuilder sb = new StringBuilder();
							while ((len = reader.read(buff)) != -1) {
								sb.append(buff, 0, len);
							}
							// JSON
							// System.out.println(" >>> " + sb.toString());
							final JSONObject jo = JSON.parseObject(sb.toString());
							requestParas.putAll(pathParas);
							if (null != jo) {
								this.analysisJsonData(requestParas, jo, null);
							}
						} catch (final UnsupportedEncodingException e) {
							e.printStackTrace();
						} catch (final IOException e) {
							e.printStackTrace();
						} catch (final JSONException e) {
							e.printStackTrace();
						}
					}
					// 各种异常相关，都不再继续向下处理
				} else {
					// 正常情况
					requestParas.putAll(pathParas);
					List<String> param;
					// 请求中的参数信息
					final Map<String, String[]> map = request.getParameterMap();
					final String[] er;
					if (map instanceof ParameterMap) {
						final ParameterMap<String, String[]> pMap = (ParameterMap<String, String[]>) map;
						if (pMap.isLocked()) {
							pMap.setLocked(false);
							er = pMap.remove(WebBaseConstants.REQUEST_KEY_ERROR_BACK);
							pMap.setLocked(true);
						} else {
							er = pMap.remove(WebBaseConstants.REQUEST_KEY_ERROR_BACK);
						}
					} else {
						er = map.remove(WebBaseConstants.REQUEST_KEY_ERROR_BACK);
					}
					if ((null != er) && (er.length > 0)) {
						requestParas.put(WebBaseConstants.REQUEST_KEY_ERROR_BACK, er[0]);
					}
					Entry<String, String[]> ent;
					final Iterator<Entry<String, String[]>> it = request.getParameterMap().entrySet().iterator();
					while (it.hasNext()) {
						ent = it.next();
						if ((null != requestParas.get(ent.getKey())) || (ent.getValue().length == 0)) {
							continue;
						}
						if (ent.getValue().length == 1) {
							requestParas.put(ent.getKey(), ent.getValue()[0]);
						} else {
							// 放入新元素
							param = new LinkedList<>();
							for (final String ss : ent.getValue()) {
								if (null != ss) {
									// 放入信息
									param.add(ss);
								}
							}
							requestParas.put(ent.getKey(), param);
						}
					}
				}
				// request.getParameterMap().entrySet();
			}
			return 0;
		}

		/**
		 * 解析formUrlEncode的数据
		 * 
		 * @author tfzzh
		 * @dateTime 2021年7月5日 下午1:21:09
		 * @param requestParas 请求参数集合
		 * @param cont 文本内容
		 * @param boundary 边界内容
		 */
		private void analysisFormUrlEncodeData(final Map<String, Object> requestParas, final String cont, final String boundary) {
			final List<String> fields = StringTools.splitToArray(cont, boundary);
			int fns, fne, fvs, fve;
			String key, val;
			for (String fc : fields) {
				if (StringTools.isNullOrEmpty(fc)) {
					continue;
				}
				fc = fc.toLowerCase();
				if ((fns = fc.indexOf(OperateLink.S_CONTENT_DISPOSITION)) == -1) {
					// 非有效数据
					continue;
				}
				fns += OperateLink.S_CONTENT_DISPOSITION.length();
				fne = fc.indexOf(Constants.DOUBLE_QUOTATION, fns);
				if (fne == -1) {
					// 非有效数据
					continue;
				}
				fvs = fc.indexOf(OperateLink.S_X_WWW_VAL_START, fne);
				if (fvs == -1) {
					// 非有效数据
					continue;
				}
				fvs += OperateLink.S_X_WWW_VAL_START.length();
				fve = fc.indexOf(OperateLink.S_X_WWW_VAL_END, fvs);
				if (fve == -1) {
					// 非有效数据
					continue;
				}
				key = fc.substring(fns, fne);
				val = fc.substring(fvs, fve);
				requestParas.put(key, val);
			}
			// System.out.println(" >> log 2021-07-05 >> over 2 analysisFormUrlEncodeData >>> " + JSON.toJSONString(requestParas));
		}

		/**
		 * 解析json结构数据<br />
		 * 认为json中，只有字符串与数字(也是字符串)<br />
		 * 
		 * @author tfzzh
		 * @dateTime 2019年4月3日 下午6:58:02
		 * @param requestParas 请求参数集合
		 * @param jo 请求的json数据被一次解析出的json数据结构对象
		 * @param path 上层路径，针对多层json结构数据
		 */
		private void analysisJsonData(final Map<String, Object> requestParas, final JSONObject jo, final String path) {
			String key;
			for (final Entry<String, Object> ent : jo.entrySet()) {
				key = null == path ? ent.getKey() : (path + Constants.SPOT + ent.getKey());
				if (null != requestParas.get(key)) {
					continue;
				}
				if (ent.getValue() instanceof JSONObject) {
					// 对象子元素
					this.analysisJsonData(requestParas, (JSONObject) ent.getValue(), key);
				} else if (ent.getValue() instanceof JSONArray) {
					// 列表子元素
					final JSONArray ja = (JSONArray) ent.getValue();
					JSONObject jao;
					for (int i = 0, s = ja.size(); i < s; i++) {
						jao = ja.getJSONObject(i);
						this.analysisJsonData(requestParas, jao, key + Constants.SPOT + i);
					}
				} else {
					// 其他
					requestParas.put(key, ent.getValue());
				}
			}
			// System.out.println(" >> log 2021-07-05 >> over 1 analysisJsonData >>> " + JSON.toJSONString(requestParas));
		}

		/**
		 * 分析参数
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-9-15 上午2:31:40
		 * @param request 请求信息
		 * @param response 返回信息
		 * @param cs 自定义session
		 * @param requestParas 被请求的所有可以被收集到的参数的集合，用于接续的操作
		 * @param paras 预期对请求方法的参数集合
		 * @param paraLog 参数日志信息，用于记录
		 * @param errorMap 错误信息集合
		 * @return 0，正常情况；<br />
		 *         7，对应Param对象处理问题；<br />
		 *         8，有不可为null的字段为null；<br />
		 *         9，Param对象必须为BaseParamBean子类；<br />
		 */
		@SuppressWarnings("unchecked")
		private short handleParam(final HttpServletRequest request, final HttpServletResponse response, final ClientSessionBean cs, final Map<String, Object> requestParas, final Object[] paras, final StringBuilder paraLog, final Map<String, String> errorMap) {
			String clzName;
			// 通用部分
			Object os;
			// 非文件内容
			List<String> ls;
			String s;
			int i = -1;
			paraLog.append('{');
			int cou;
			for (final Entry<String, Class<?>> ent : this.params.entrySet()) {
				if (i++ != -1) {
					paraLog.append(',');
				}
				// TODO 之后考虑整体优化方案 2023-11-24
				paraLog.append(ent.getKey()).append('=');
				clzName = ent.getValue().getSimpleName();
				if (ent.getValue().isArray()) {
					// 是数组情况
					os = requestParas.get(ent.getKey());
					ls = (List<String>) os;
					// 放入KEY
					paraLog.append('[');
					cou = 0;
					switch (clzName) {
					case "Integer[]":
					case "int[]":
						if (null == ls) {
							paras[i] = null;
						} else {
							final int[] p = new int[ls.size()];
							for (int j = ls.size() - 1; j >= 0; j--) {
								if (cou++ != 0) {
									paraLog.append(',');
								}
								p[j] = Integer.parseInt(ls.get(j));
								paraLog.append(p[j]);
							}
							paras[i] = p;
						}
						break;
					case "Long[]":
					case "long[]":
						if (null == ls) {
							paras[i] = null;
						} else {
							final long[] p = new long[ls.size()];
							for (int j = ls.size() - 1; j >= 0; j--) {
								if (cou++ != 0) {
									paraLog.append(',');
								}
								p[j] = Long.parseLong(ls.get(j));
								paraLog.append(p[j]);
							}
							paras[i] = p;
						}
						break;
					case "Short[]":
					case "short[]":
						if (null == ls) {
							paras[i] = null;
						} else {
							final short[] p = new short[ls.size()];
							for (int j = ls.size() - 1; j >= 0; j--) {
								if (cou++ != 0) {
									paraLog.append(',');
								}
								p[j] = Short.parseShort(ls.get(j));
								paraLog.append(p[j]);
							}
							paras[i] = p;
						}
						break;
					case "Float[]":
					case "float[]":
						if (null == ls) {
							paras[i] = null;
						} else {
							final float[] p = new float[ls.size()];
							for (int j = ls.size() - 1; j >= 0; j--) {
								if (cou++ != 0) {
									paraLog.append(',');
								}
								p[j] = Float.parseFloat(ls.get(j));
								paraLog.append(p[j]);
							}
							paras[i] = p;
						}
						break;
					case "Double[]":
					case "double[]":
						if (null == ls) {
							paras[i] = null;
						} else {
							final double[] p = new double[ls.size()];
							for (int j = ls.size() - 1; j >= 0; j--) {
								if (cou++ != 0) {
									paraLog.append(',');
								}
								p[j] = Double.parseDouble(ls.get(j));
								paraLog.append(p[j]);
							}
							paras[i] = p;
						}
						break;
					case "Boolean[]":
					case "boolean[]":
						if (null == ls) {
							paras[i] = null;
						} else {
							final boolean[] p = new boolean[ls.size()];
							for (int j = ls.size() - 1; j >= 0; j--) {
								if (cou++ != 0) {
									paraLog.append(',');
								}
								p[j] = Boolean.parseBoolean(ls.get(j));
								paraLog.append(p[j]);
							}
							paras[i] = p;
						}
						break;
					case "String[]":
						if (null == ls) {
							paras[i] = null;
						} else {
							final String[] p = new String[ls.size()];
							for (int j = ls.size() - 1; j >= 0; j--) {
								if (cou++ != 0) {
									paraLog.append(',');
								}
								try {
									p[j] = URLDecoder.decode(ls.get(j).replaceAll("%", "%25"), Constants.SYSTEM_CODE);
									paraLog.append(p[j]);
								} catch (final UnsupportedEncodingException e) {
									paraLog.append(ls.get(j));
								}
							}
							paras[i] = p;
						}
						break;
					}
					paraLog.append(']');
				} else {
					// TODO 之后考虑整体优化方案 2023-11-24
					// 非数组
					String[] keys = ent.getKey().split(Constants.COLON);
					nas: switch (keys[0]) {
					case WebConstants.PRIVATE_LINK_NAME_REQUEST: { // 请求
						// 是request
						paras[i] = request;
						break;
					}
					case WebConstants.PRIVATE_LINK_NAME_BEAN: { // 对象
						// 是参数对象Bean，先初始化，再赋值
						// 得到对象，该对象必须为BaseParamBean子类
						if (ent.getValue().asSubclass(BaseParamBean.class) != null) {
							try {
								final BaseParamBean param;
								// if (WebBaseConstants.CUSTOM_SESSION) {
								if (null != cs) {
									final Object obj = cs.removeParam(this.reflectControlKey + WebBaseConstants.REDIRECT_SESSION_PARAM_BEAN_POS);
									if (null != obj) {
										// 存在先移除
										if (obj instanceof BaseParamBean) {
											param = (BaseParamBean) obj;
											paras[i] = param;
											paraLog.append(param);
											break;
										}
									}
								}
								// } else {
								// final HttpSession session = request.getSession();
								// if (null != session) {
								// final Object obj = session.getAttribute(this.reflectControlKey + WebBaseConstants.REDIRECT_SESSION_PARAM_BEAN_POS);
								// if (null != obj) {
								// // 存在先移除
								// session.removeAttribute(this.reflectControlKey + WebBaseConstants.REDIRECT_SESSION_PARAM_BEAN_POS);
								// if (obj instanceof BaseParamBean) {
								// param = (BaseParamBean) obj;
								// paras[i] = param;
								// paraLog.append(param);
								// break;
								// }
								// }
								// }
								// }
								param = (BaseParamBean) ent.getValue().getDeclaredConstructor().newInstance();
								param.setParameters(requestParas, keys.length > 2);
								// if (errorMap.size() > 0) {
								// return 7;
								// }
								if (param.hasError()) {
									errorMap.put(WebConstants.PRIVATE_PARAM_ERROR, Integer.toString(i));
								}
								paras[i] = param;
								paraLog.append(param);
								break;
							} catch (final Exception e) {
								// 错误情况
								e.printStackTrace();
								errorMap.put(ent.getValue().getSimpleName(), ent.getValue().getSimpleName() + " Data Error!");
								return 7;
							}
						} else {
							// 错误情况
							errorMap.put(ent.getValue().getSimpleName(), ent.getValue().getSimpleName() + " must extends BaseParamBean.");
							return 9;
						}
					}
					case WebConstants.PRIVATE_LINK_NAME_ACCOUNT: { // 登陆帐号信息
						// 验证基础权限情况
						if ((null == cs.getLoginInfo()) || !ent.getValue().isAssignableFrom(cs.getLoginInfo().getClass())) {
							return 1;
						}
						paras[i] = cs.getLoginInfo();
						break;
					}
					// del 2017-04-12
					// case WebConstants.PRIVATE_LINK_NAME_SESSION: { // session
					// paras[i] = request.getSession();
					// break;
					// }
					// 自定义session 2017-04-12
					case WebConstants.PRIVATE_LINK_NAME_CUSTOM_SESSION: { // custom session
						// if (WebBaseConstants.CUSTOM_SESSION) {
						paras[i] = cs;
						// } else {
						// paras[i] = null;
						// }
						break;
					}
					case WebConstants.PRIVATE_LINK_NAME_FIRST_IP: { // 请求方的首位IP add xwj 2017-10-17
						paras[i] = WebTools.getFirstClientIp(request);
						break;
					}
					case WebConstants.PRIVATE_LINK_NAME_IP: { // 请求方的IP
						paras[i] = WebTools.getClientIp(request);
						break;
					}
					case WebConstants.PRIVATE_LINK_NAME_USER_AGENT: { // 来自当前请求的user-agent
						paras[i] = request.getHeader(WebConstants.PRIVATE_LINK_NAME_USER_AGENT);
						break;
					}
					case WebConstants.PRIVATE_LINK_NAME_URL: { // 来自当前请求的完整url
						paras[i] = request.getHeader("Referer");
						break;
					}
					case WebConstants.PRIVATE_LINK_NAME_ALL_PARAM: { // 来自当前请求的完整url
						paras[i] = requestParas;
						break;
					}
					case WebConstants.PRIVATE_LINK_NAME_RESPONSE: { // 返回
						// 是response
						paras[i] = response;
						break;
					}
					case WebConstants.KEY_REQUEST_DECRYPT_STREAM_ATTRIBUTE: { // KEY，加密POST stream 请求，解密后数据到request.attribute
						paras[i] = request.getAttribute(WebConstants.KEY_REQUEST_DECRYPT_STREAM_ATTRIBUTE);
						break;
					}
					case WebConstants.PRIVATE_LINK_NAME_SUFFIX: { // 后缀名
						paras[i] = ((List<String>) requestParas.get(WebConstants.PRIVATE_LINK_NAME_SUFFIX)).get(0);
						break;
					}
					default: { // 其他
						final String key = ent.getKey();
						if (key.startsWith(WebConstants.KEY_REQUEST_ATTRIBUTE)) { // 判定是否对应request.attribute
							final String raKey = key.substring(WebConstants.KEY_REQUEST_ATTRIBUTE.length());
							paras[i] = request.getAttribute(raKey);
							break;
						}
						os = requestParas.get(key);
						// 非数组情况
						if (null == os) {
							s = null;
							switch (clzName) {
							case "int":
							case "long":
							case "short":
							case "float":
							case "double":
							case "boolean":
								errorMap.put(key, "cannt be null!");
								paraLog.append("null");
								return 8;
							}
							paras[i] = null;
							paraLog.append("null");
							break;
						} else if (os instanceof String) {
							s = (String) os;
							if ((null == s) || ((s = s.trim()).length() == 0)) {
								switch (clzName) {
								case "Integer":
								case "Long":
								case "Short":
								case "Float":
								case "Double":
								case "Boolean":
									paras[i] = null;
									paraLog.append("null");
									break nas;
								}
							} else {
								paraLog.append(s);
							}
						} else {
							// 非数组，单内容
							ls = (List<String>) os;
							s = ls.get(0);
							if ((null == s) || ((s = s.trim()).length() == 0)) {
								switch (clzName) {
								case "Integer":
								case "Long":
								case "Short":
								case "Float":
								case "Double":
								case "Boolean":
									paras[i] = null;
									paraLog.append("null");
									break nas;
								}
							} else {
								paraLog.append(s);
							}
						}
						switch (clzName) {
						case "int":
							paras[i] = Integer.parseInt(s);
							break;
						case "Integer":
							if (null != s) {
								paras[i] = Integer.valueOf(s);
							}
							break;
						case "long":
							paras[i] = Long.parseLong(s);
							break;
						case "Long":
							if (null != s) {
								paras[i] = Long.valueOf(s);
							}
							break;
						case "short":
							paras[i] = Short.parseShort(s);
							break;
						case "Short":
							if (null != s) {
								paras[i] = Short.valueOf(s);
							}
							break;
						case "float":
							paras[i] = Float.parseFloat(s);
							break;
						case "Float":
							if (null != s) {
								paras[i] = Float.valueOf(s);
							}
							break;
						case "double":
							paras[i] = Double.parseDouble(s);
							break;
						case "Double":
							if (null != s) {
								paras[i] = Double.valueOf(s);
							}
							break;
						case "boolean":
							paras[i] = Boolean.parseBoolean(s);
							break;
						case "Boolean":
							if (null != s) {
								paras[i] = Boolean.valueOf(s);
							}
							break;
						case "String":
							if (null != s) {
								try {
									paras[i] = URLDecoder.decode(s.replaceAll("%", "%25"), Constants.SYSTEM_CODE);
								} catch (final UnsupportedEncodingException e) {
									paras[i] = s;
								}
							}
							break;
						}
					}
					}
				}
			}
			paraLog.append('}');
			return 0;
		}

		/**
		 * 得到初始化的方法用参数集合
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-9-15 上午10:04:19
		 * @return 方法用参数集合
		 */
		public Object[] getInitMethodParas() {
			return new Object[this.params.size()];
		}

		/**
		 * 是否可以跨域，true，可以跨域
		 *
		 * @author Xu Weijie
		 * @datetime 2017年11月29日_下午3:12:07
		 * @return the canCrossDomain
		 */
		public boolean isCanCrossDomain() {
			return this.canCrossDomain;
		}

		/**
		 * 是否需要token验证
		 *
		 * @author Xu Weijie
		 * @datetime 2017年11月6日_下午3:37:47
		 * @return the needToken
		 */
		private boolean isNeedToken() {
			return this.needToken;
		}
		// /**
		// * 得到说明
		// *
		// * @author xuweijie
		// * @dateTime 2012-2-7 下午6:17:23
		// * @return the description
		// * @see com.tfzzh.view.web.purview.AccessPermissionsInfo#getDescription()
		// */
		// @Override
		// public String getDescription() {
		// return this.description;
		// }
		// /**
		// * 设置访问权限对象
		// *
		// * @author Weijie Xu
		// * @dateTime 2012-7-9 下午11:16:26
		// * @param accessPermissions the accessPermissions to set
		// */
		// @Override
		// public void setAccessPermissions(final AccessPermissionsInfo accessPermissions) {
		// if (null == this.accessPermissions) {
		// this.accessPermissions = accessPermissions;
		// }
		// }
		// /**
		// * 得到访问权限对象
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-12 上午10:58:46
		// * @return the accessPermissions
		// */
		// public AccessPermissionsInfo getAccessPermissions() {
		// return this.accessPermissions;
		// }
		// /**
		// * 得到访问权限值
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-9 下午4:36:52
		// * @return the accessPermission
		// * @see com.tfzzh.view.web.purview.AccessPermissionsInfo#getAccessPermissionsValue()
		// */
		// @Override
		// public int getAccessPermissionsValue() {
		// if (null == this.accessPermissions) {
		// return this.accessPermissionsValue;
		// } else {
		// return this.accessPermissions.getAccessPermissionsValue();
		// }
		// }

		/**
		 * 组合参数
		 * 
		 * @author tfzzh
		 * @dateTime 2016年9月4日 下午6:39:45
		 * @param sb 组合字串
		 * @param ii 计数器
		 * @param ent 参数内容
		 * @throws UnsupportedEncodingException 抛
		 */
		private void assembleParam(final StringBuilder sb, final InnerIndex ii, final Entry<String, ? extends Object> ent) throws UnsupportedEncodingException {
			this.assembleParam(sb, ii, ent.getKey(), ent.getValue());
		}

		/**
		 * 组合参数
		 * 
		 * @author Xu Weijie
		 * @datetime 2017年9月12日_下午12:19:45
		 * @param sb 组合字串
		 * @param ii 计数器
		 * @param key 参数key
		 * @param val 参数值
		 * @throws UnsupportedEncodingException 抛
		 */
		private void assembleParam(final StringBuilder sb, final InnerIndex ii, final String key, final Object val) throws UnsupportedEncodingException {
			if (ii.add() > 0) {
				sb.append('&');
			} else {
				sb.append('?');
			}
			sb.append(key).append('=');
			if (val instanceof String) {
				// 字符串才转
				try {
					sb.append(URLEncoder.encode(URLEncoder.encode(val.toString(), Constants.SYSTEM_CODE), Constants.SYSTEM_CODE));
				} catch (final Exception e) {
					// 某些系统在获取系统编码时，可能存在一些问题，具体还未深入去看，先做个简单处理
					sb.append(URLEncoder.encode(URLEncoder.encode(val.toString(), "UTF-8"), "UTF-8"));
				}
			} else {
				sb.append(val);
			}
		}

		/**
		 * 针对自控项目的服务的直接url跳转，非指代性跳转
		 * 
		 * @author tfzzh
		 * @dateTime 2016年9月4日 下午5:37:50
		 * @param back 返回的数据
		 * @param request 请求
		 * @param response 返回
		 * @throws IOException 抛
		 * @throws ServletException 抛
		 */
		public void executeResult(final BackJumpGivenUrlBean back, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
			final String target = back.getTarget();
			final int bs = target.indexOf("#");
			final StringBuilder sb;
			final String bf;
			if (bs != -1) {
				bf = target.substring(bs);
				sb = new StringBuilder(target.substring(0, bs));
			} else {
				sb = new StringBuilder(target);
				bf = null;
			}
			final InnerIndex ii = new InnerIndex(sb.indexOf("?"));
			final Object oj = RunThreadLocal.getInstance().getObject(WebBaseConstants.TL_KEY);
			if (back.isTakeToken()) {
				if ((null != oj) && (oj instanceof ClientSessionBean)) {
					// 得到当前客户端信息
					final ClientSessionBean cs = (ClientSessionBean) oj;
					// sb.append('?').append(WebBaseConstants.CUSTOM_SESSION_TOKEN_KEY).append('=').append(cs.getToken());
					this.assembleParam(sb, ii, WebBaseConstants.CUSTOM_SESSION_TOKEN_KEY, cs.getToken());
					// 放入内容到header
					response.addHeader(WebBaseConstants.CUSTOM_SESSION_TOKEN_KEY, cs.getToken());
					if (null != cs.getClientId()) {
						// sb.append('&').append(WebBaseConstants.CUSTOM_SESSION_NAME_KEY).append('=').append(cs.getClientId());
						this.assembleParam(sb, ii, WebBaseConstants.CUSTOM_SESSION_NAME_KEY, cs.getClientId());
						response.addHeader(WebBaseConstants.CUSTOM_SESSION_TOKEN_KEY, cs.getToken());
					}
				}
			} else {
				// 放入内容到header
				if ((null != oj) && (oj instanceof ClientSessionBean)) {
					final ClientSessionBean cs = (ClientSessionBean) oj;
					response.addHeader(WebBaseConstants.CUSTOM_SESSION_TOKEN_KEY, cs.getToken());
					if (null != cs.getClientId()) {
						response.addHeader(WebBaseConstants.CUSTOM_SESSION_TOKEN_KEY, cs.getToken());
					}
				}
			}
			final Map<String, ? extends Object> param = back.getAttributes();
			// 跳转URL
			if ((null != param) && (param.size() > 0)) {
				// 存在get参数
				// 进行参数组合
				for (final Entry<String, ? extends Object> ent : param.entrySet()) {
					this.assembleParam(sb, ii, ent);
				}
			}
			if (null != bf) {
				sb.append(bf);
			}
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), new StringBuilder().append("To Redirect >> ").append(sb.toString()).toString());
			}
			response.sendRedirect(sb.toString());
		}

		/**
		 * 针对服务器连接跳转
		 * 
		 * @author tfzzh
		 * @dateTime 2016年8月29日 下午6:46:32
		 * @param back 返回的数据
		 * @param request 请求
		 * @param response 返回
		 * @throws IOException 抛
		 * @throws ServletException 抛
		 */
		public void executeResult(final BackJumpTargetBean back, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
			final BackLinkOperationInfo info;
			if (null != (info = this.getBackResult(back.getTarget()))) {
				// 组合成目标URL
				final String target;
				// if (!back.isOutside()) {
				if (null != back.getFormats()) {
					target = MessageFormat.format(back.getPrefix() + info.getTarget() + WebBaseConstants.URL_POS, back.getFormats());
				} else {
					target = back.getPrefix() + info.getTarget() + WebBaseConstants.URL_POS;
				}
				target.replaceAll(Constants.SEMICOLON, ":");
				final StringBuilder sb = new StringBuilder(target);
				final Map<String, ? extends Object> param = back.getAttributes();
				// 跳转URL
				if (null != param) {
					// 存在get参数
					// 进行参数组合
					// int i = 0;
					final InnerIndex ii = new InnerIndex();
					for (final Entry<String, ? extends Object> ent : param.entrySet()) {
						if (ent.getValue() instanceof BaseParamBean) {
							// 如果是个参数信息Bean
							final HttpSession session = request.getSession();
							if (null != session) {
								session.setAttribute("Get/" + info.getTarget() + WebBaseConstants.REDIRECT_SESSION_PARAM_BEAN_POS, ent.getValue());
							}
						} else {
							this.assembleParam(sb, ii, ent);
						}
					}
				}
				if (CoreLog.getInstance().debugEnabled(this.getClass())) {
					CoreLog.getInstance().debug(this.getClass(), new StringBuilder().append("To Redirect >> ").append(sb.toString()).toString());
				}
				response.sendRedirect(sb.toString());
			}
		}

		/**
		 * 针对jsp页面合成
		 * 
		 * @author tfzzh
		 * @dateTime 2016年8月29日 下午6:46:08
		 * @param back 返回的数据
		 * @param request 请求
		 * @param response 返回
		 * @throws IOException 抛
		 * @throws ServletException 抛
		 */
		public void executeResult(final BackToJspBean back, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
			final BackLinkOperationInfo info;
			if (null != (info = this.getBackResult(back.getTarget()))) {
				// 将参数信息放入到request中
				back.putAttributeToRequest(request);
				final String target = info.getJspTarget();
				if (CoreLog.getInstance().debugEnabled(this.getClass())) {
					CoreLog.getInstance().debug(this.getClass(), new StringBuilder().append("To Dispatcher >> ").append(target).toString());
				}
				request.setAttribute(WebBaseConstants.SESSION_KEY_USER, RunThreadLocal.getInstance().getObject(WebBaseConstants.TL_KEY));
				request.getRequestDispatcher(target).forward(request, response);
			}
		}

		/**
		 * 针对去到对外部服务的跳转<br />
		 * 一般为代理外链<br />
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-7-6 下午12:32:07
		 * @param back 返回的数据
		 * @param request 请求
		 * @param response 返回
		 * @throws IOException 抛
		 * @throws ServletException 抛
		 */
		public void executeResult(final BackOutsideBean back, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
			final StringBuilder sb = new StringBuilder(back.getTarget());
			final Map<String, ? extends Object> param = back.getAttributes();
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				final Object oj = RunThreadLocal.getInstance().getObject(WebBaseConstants.TL_KEY);
				CoreLog.getInstance().debug(this.getClass(), "BackOutsideBean executeResult .. [", null == oj ? "null" : oj.getClass().getName(), ":", null == oj ? "null" : oj.toString(), "]");
			}
			final InnerIndex ii = new InnerIndex(sb.indexOf("?"));
			final int bs = sb.indexOf("#");
			final String bf;
			if (bs != -1) {
				bf = sb.substring(bs);
				sb.delete(bs, sb.length());
			} else {
				bf = null;
			}
			// if ((null != oj) && (oj instanceof ClientSessionBean)) {
			// // 得到当前客户端信息
			// final ClientSessionBean cs = (ClientSessionBean) oj;
			// // sb.append(WebBaseConstants.CUSTOM_SESSION_TOKEN_KEY).append('=').append(cs.getToken());
			// this.assembleParam(sb, ii, WebBaseConstants.CUSTOM_SESSION_TOKEN_KEY, cs.getToken());
			// if (null != cs.getClientId()) {
			// // sb.append('&').append(WebBaseConstants.CUSTOM_SESSION_NAME_KEY).append('=').append(cs.getClientId());
			// this.assembleParam(sb, ii, WebBaseConstants.CUSTOM_SESSION_NAME_KEY, cs.getClientId());
			// }
			// }
			// 组合参数
			if (null != param) {
				for (final Entry<String, ? extends Object> ent : param.entrySet()) {
					if (null == ent.getValue()) {
						continue;
					}
					this.assembleParam(sb, ii, ent);
				}
			}
			if (null != bf) {
				sb.append(bf);
			}
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), new StringBuilder().append("Back To Outside >> ").append(sb.toString()).toString());
			}
			response.sendRedirect(sb.toString());
		}

		/**
		 * 针对读取一个指定文件内容
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-9-5 下午9:46:09
		 * @param back 返回的数据
		 * @param request 请求
		 * @param response 返回
		 * @throws IOException 抛
		 * @throws ServletException 抛
		 */
		public void executeResult(final BackFileBean back, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
			final BackLinkOperationInfo info = this.getBackResult(back.getTarget());
			// 文件名后半部分
			final String target;
			if (null != info) {
				if (null != back.getFormats()) {
					target = String.format(info.getTarget(), back.getFormats());
				} else {
					target = info.getTarget();
				}
			} else {
				target = back.getTarget();
			}
			// 得到完整文件名
			final String filePath = target.startsWith("file:") ? target.substring(7) : (Constants.INIT_CONFIG_PATH_BASE + target);
			// 读及输出文件内容
			final byte[] b = new byte[8192];
			final FileInputStream fis = new FileInputStream(filePath);
			final BufferedInputStream bis = new BufferedInputStream(fis);
			final ServletOutputStream out = response.getOutputStream();
			final BufferedOutputStream bos = new BufferedOutputStream(out);
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), new StringBuilder().append("Back File local path >> ").append(Constants.INIT_CONFIG_PATH_BASE).append(target).toString());
			}
			int len;
			// 逐段内容读入
			while ((len = bis.read(b)) >= 0) {
				bos.write(b, 0, len);
			}
			// 清理和关闭流文件
			bis.close();
			fis.close();
			bos.flush();
			bos.close();
			out.close();
		}

		/**
		 * 针对返回json结构的字串信息
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-7-6 下午12:31:49
		 * @param back 返回的数据
		 * @param request 请求
		 * @param response 返回
		 * @throws IOException 抛
		 * @throws ServletException 抛
		 */
		public void executeResult(final BackJsonBean back, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
			response.setContentType("application/json; charset=" + Constants.SYSTEM_CODE);
			response.setCharacterEncoding(Constants.SYSTEM_CODE);
			final PrintWriter pw = response.getWriter();
			final String bv;
			if (null == back.getSerializeFilter()) {
				bv = JSON.toJSONString(back.getAttributes());
			} else {
				bv = JSON.toJSONString(back.getAttributes(), back.getSerializeFilter());
			}
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), new StringBuilder().append("Back Json Data >> ").append(bv.length() > WebBaseConstants.BACK_MAX_LENGTH ? bv.substring(0, WebBaseConstants.BACK_MAX_LENGTH) : bv).toString());
			}
			pw.write(bv);
			pw.close();
		}

		/**
		 * 针对返回指定的字串信息
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-12-24 下午6:44:38
		 * @param back 返回的数据
		 * @param request 请求
		 * @param response 返回
		 * @throws IOException 抛
		 * @throws ServletException 抛
		 */
		public void executeResult(final BackStringBean back, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
			if (back.getType().indexOf(Constants.DIAGONAL_LINE) == -1) {
				response.setContentType("application/" + back.getType() + "; charset=" + Constants.SYSTEM_CODE);
			} else {
				response.setContentType(back.getType() + "; charset=" + Constants.SYSTEM_CODE);
			}
			response.setCharacterEncoding(Constants.SYSTEM_CODE);
			final PrintWriter pw = response.getWriter();
			final String bv = back.getValue();
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), new StringBuilder().append("Back String Data >> ").append(bv == null ? "null" : (bv.length() > WebBaseConstants.BACK_MAX_LENGTH ? bv.substring(0, WebBaseConstants.BACK_MAX_LENGTH) : bv)).toString());
			}
			pw.write(bv);
			pw.close();
		}

		/**
		 * 针对动态图片内容的放回
		 * 
		 * @author XuWeijie
		 * @datetime 2015年9月6日_下午5:30:08
		 * @param back 返回的数据
		 * @param request 请求
		 * @param response 返回
		 * @throws IOException 抛
		 * @throws ServletException 抛
		 */
		public void executeResult(final BackImgBean back, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
			// 设置响应的类型格式为图片格式
			response.setContentType(back.getImgType().getType());
			// 禁止图像缓存。
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			final ServletOutputStream out = response.getOutputStream();
			javax.imageio.ImageIO.write(back.getImg(), back.getImgType().getSufName(), out);
			out.close();
		}

		/**
		 * 返回简单加密字节串
		 * 
		 * @author tfzzh
		 * @dateTime 2023年10月15日 13:59:54
		 * @param back 返回的数据
		 * @param request 请求
		 * @param response 返回
		 * @throws IOException 抛
		 * @throws ServletException 抛
		 */
		public void executeResult(final BackSimpleEncryptionBean back, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
			// // 设置响应的类型格式为图片格式
			// response.setContentType("application/octet-stream;");
			// // 禁止图像缓存。
			// response.setHeader("Pragma", "no-cache");
			// response.setHeader("Cache-Control", "no-cache");
			// response.setDateHeader("Expires", 0);
			// final ServletOutputStream out = response.getOutputStream();
			// // 写入到out
			// out.write(back.getOutBytes());
			// out.close();
			OperateLinkInfo.executeEncryptionResult(back, request, response);
		}

		/**
		 * 返回简单加密字节串
		 * 
		 * @author tfzzh
		 * @dateTime 2024年6月4日 11:13:32
		 * @param back 返回的数据
		 * @param request 请求
		 * @param response 返回
		 * @throws IOException 抛
		 * @throws ServletException 抛
		 */
		public static void executeEncryptionResult(final BackSimpleEncryptionBean back, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
			// 设置响应的类型格式为图片格式
			response.setContentType("application/octet-stream;");
			// 禁止图像缓存。
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			final ServletOutputStream out = response.getOutputStream();
			// 写入到out
			out.write(back.getOutBytes());
			out.close();
		}

		/**
		 * 得到返回的操作信息
		 * 
		 * @author Weijie Xu
		 * @dateTime 2013-12-21 下午5:16:09
		 * @param target 目标Key
		 * @return 操作信息
		 */
		private BackLinkOperationInfo getBackResult(final String target) {
			if (null != this.targets) {
				final BackLinkOperationInfo b = this.targets.get(target);
				if (null == b) {
					return this.targetNode.getBackResult(target);
				} else {
					return b;
				}
			} else {
				return this.targetNode.getBackResult(target);
			}
		}
		// /**
		// * 验证访问权限
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-13 上午11:28:26
		// * @param request 请求的信息
		// * @param response 返回数据
		// * @param cs 客户端会话
		// * @return true，验证成功；<br />
		// * false，验证失败，但该情况一般多认为抛出了异常；<br />
		// */
		// public boolean validateAccessPermission(final HttpServletRequest request, final HttpServletResponse response, final ClientSessionBean cs) {
		// return AccessPermissionsControl.getInstance().validateAccessPermission(this.accessPermissions, request, response, cs);
		// }

		/**
		 * 得到连接中的对应Key内容<br />
		 * 为了统合request中ParameterMap<br />
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-8 下午2:49:43
		 * @param paths 路径信息，要求：首尾去掉“/”的
		 * @return 匹配后的参数信息，如果不包含参数，也是零长，不会null；<br />
		 *         null，不存在匹配；<br />
		 */
		protected abstract Map<String, List<String>> getKeyBack(final String[] paths);
		// /**
		// * 得到目标
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-16 下午12:31:21
		// * @return 目标值
		// * @see com.tfzzh.view.web.purview.AccessPermissionsInfo#getTarget()
		// */
		// @Override
		// public String getTarget() {
		// return this.reflectControlKey;
		// }
		// /**
		// * 得到目标节点连接
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-17 下午9:24:22
		// * @return 目标节点连接
		// * @see com.tfzzh.view.web.purview.AccessPermissionsInfo#getTargetNode()
		// */
		// @Override
		// public AccessPermissionsNodeInfo getTargetNode() {
		// return this.targetNode;
		// }
		// /**
		// * 是否节点
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-18 下午8:16:00
		// * @return true，是节点；<br />
		// * false，不是节点；<br />
		// * @see com.tfzzh.view.web.purview.AccessPermissionsInfo#isNode()
		// */
		// @Override
		// public boolean isNode() {
		// return false;
		// }
	}

	/**
	 * 正常模式的连接控制信息
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-2 上午10:38:48
	 */
	private class NormalOperateLinkInfo extends OperateLinkInfo {

		/**
		 * @author xuweijie
		 * @dateTime 2012-2-2 上午10:40:27
		 * @param reflectControlKey 反射控制键
		 * @param prefix 路径前缀
		 * @param targetNode 目标节点连接
		 * @param result 结果控制集合
		 * @param params 参数集合，有序的<br />
		 *           <参数对应key,参数的对象类型><br />
		 * @param readStreamJSON 是否读取stream相关json信息 add 2023-11-24
		 * @param canCrossDomain 是否可以跨域，true，可以跨域
		 * @param needToken 是否需要token验证
		 * @param description 说明
		 * @param ipRestr ip限制标识
		 */
		public NormalOperateLinkInfo(final String reflectControlKey, final String prefix, final OperateLinkNodeInfo targetNode, final String[] result, final Map<String, Class<?>> params, final boolean readStreamJSON, final boolean canCrossDomain, final boolean needToken, final String description, final String ipRestr) {
			// * @param accessPermissions 访问权限值 del 2023-11-24
			super(reflectControlKey, prefix, targetNode, result, params, readStreamJSON, canCrossDomain, needToken, description, ipRestr);
		}

		/**
		 * @author xuweijie
		 * @dateTime 2012-2-9 下午4:58:18
		 * @see com.tfzzh.view.web.link.OperateLink.OperateLinkInfo#getKeyBack(java.lang.String[])
		 */
		@Override
		protected Map<String, List<String>> getKeyBack(final String[] path) {
			return null;
		}
	}

	// /**
	// * @author Weijie Xu
	// * @dateTime 2017年4月10日 上午10:02:49
	// */
	// private class NewDeployOperateLinkInfo extends OperateLinkInfo {
	//
	// private final int tier;
	//
	// private final Map<Integer, String> keys = new TreeMap<>();
	//
	// private final Map<Integer, List<String>> paras = new TreeMap<>();
	//
	//
	//
	//
	// /**
	// * @author Weijie Xu
	// * @dateTime 2017年4月10日 上午10:02:53
	// * @param path 适配路径
	// * @param reflectControlKey 反射控制键
	// * @param prefix 路径前缀
	// * @param accessPermissions 访问权限值
	// * @param targetNode 目标节点连接
	// * @param result 结果控制集合
	// * @param params 参数集合，有序的<br />
	// * <参数对应key,参数的对象类型><br />
	// * @param description 说明
	// */
	// public NewDeployOperateLinkInfo(final String path, final String reflectControlKey, final String prefix, final int accessPermissions, final OperateLinkNodeInfo targetNode, final String[] result, final Map<String, Class<?>> params, final String description) {
	// super(reflectControlKey, prefix, accessPermissions, targetNode, result, params, description);
	//
	// // 分解path
	// final String[] paths = path.split("[/]");
	// int ind = 0;
	// int linkStart;
	// int linkEnd;
	// int linkTmp;
	// List<String> l;
	// for (final String pa : paths) {
	// if (pa.length() > 0) {
	// if (((linkStart = pa.indexOf("{")) != -1) && ((linkTmp = pa.indexOf("}", linkStart)) != -1)) {
	// // 参数判定位
	// this.paras.put(ind++, (l = new LinkedList<>()));
	// linkEnd = -1;
	// do {
	// l.add(pa.substring(linkEnd + 1, linkStart));
	// linkEnd = linkTmp;
	// l.add(pa.substring(linkStart + 1, linkEnd));
	// } while (((linkStart = pa.indexOf("{", linkEnd)) != -1) && ((linkTmp = pa.indexOf("}", linkStart)) != -1));
	// l.add(pa.substring(linkEnd + 1, pa.length()));
	// } else {
	// // 正常位
	// this.keys.put(ind++, pa);
	// }
	// }
	// }
	// this.tier = ind;
	//
	// }
	//
	// /**
	// * @author Weijie Xu
	// * @dateTime 2017年4月10日 上午10:03:28
	// * @param paths
	// * @return
	// * @see com.tfzzh.view.web.link.OperateLink.OperateLinkInfo#getKeyBack(java.lang.String[])
	// */
	// @Override
	// protected Map<String, List<String>> getKeyBack(String[] paths) {
	// // Auto-generated method stub
	// return null;
	// }
	//
	//
	//
	//
	// }
	/**
	 * 适配模式的连接控制信息
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-2 下午6:00:17
	 */
	private class DeployOperateLinkInfo extends OperateLinkInfo {

		/**
		 * 层级数量
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-7 下午5:30:24
		 */
		private final int tier;

		/**
		 * 关键字段列表<br />
		 * <所在索引位置,对应关键字段><br />
		 * 
		 * @author xuweijiek
		 * @dateTime 2012-2-7 上午10:33:18
		 */
		private final Map<Integer, String> keys = new TreeMap<>();

		/**
		 * 参数字段列表<br />
		 * <所在索引位置,对应参数名><br />
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-7 下午5:36:04
		 */
		private final Map<Integer, List<String>> paras = new TreeMap<>();

		/**
		 * @author xuweijie
		 * @dateTime 2012-2-2 下午6:00:07
		 * @param path 适配路径
		 * @param reflectControlKey 反射控制键
		 * @param prefix 路径前缀
		 * @param targetNode 目标节点连接
		 * @param result 结果控制集合
		 * @param params 参数集合，有序的<br />
		 *           <参数对应key,参数的对象类型><br />
		 * @param readStreamJSON 是否读取stream相关json信息 add 2023-11-24
		 * @param canCrossDomain 是否可以跨域，true，可以跨域
		 * @param needToken 是否需要token验证
		 * @param description 说明
		 * @param ipRestr ip限制标识
		 */
		public DeployOperateLinkInfo(final String path, final String reflectControlKey, final String prefix, final OperateLinkNodeInfo targetNode, final String[] result, final Map<String, Class<?>> params, final boolean readStreamJSON, final boolean canCrossDomain, final boolean needToken, final String description, final String ipRestr) {
			// * @param accessPermissions 访问权限值 del 2023-11-24
			super(reflectControlKey, prefix, targetNode, result, params, readStreamJSON, canCrossDomain, needToken, description, ipRestr);
			// 分解path
			final String[] paths = path.split("[/]");
			int effInd = 0;
			boolean eff = false;
			int linkStart;
			int linkEnd;
			int linkTmp;
			List<String> l;
			for (final String pa : paths) {
				if (pa.length() > 0) {
					if (((linkStart = pa.indexOf("{")) != -1) && ((linkTmp = pa.indexOf("}", linkStart)) != -1)) {
						if (!eff) {
							eff = true;
						}
						// 参数判定位
						this.paras.put(effInd++, (l = new LinkedList<>()));
						linkEnd = -1;
						do {
							l.add(pa.substring(linkEnd + 1, linkStart));
							linkEnd = linkTmp;
							l.add(pa.substring(linkStart + 1, linkEnd));
						} while (((linkStart = pa.indexOf("{", linkEnd)) != -1) && ((linkTmp = pa.indexOf("}", linkStart)) != -1));
						l.add(pa.substring(linkEnd + 1, pa.length()));
					} else {
						// 正常位
						if (eff) {
							this.keys.put(effInd++, pa);
						}
					}
				}
			}
			this.tier = effInd;
		}

		/**
		 * 进行相似性比较
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-7 上午10:26:44
		 * @param obj 目标对象
		 * @return true，相似；<br />
		 *         false，不相似；<br />
		 */
		@Override
		public boolean equals(final Object obj) {
			if (obj instanceof DeployOperateLinkInfo) {
				final DeployOperateLinkInfo other = (DeployOperateLinkInfo) obj;
				// 需要进行关键词的比较
				if (this.keys.size() != other.keys.size()) {
					return false;
				} else {
					String str;
					for (final Entry<Integer, String> ent : other.keys.entrySet()) {
						if (null == (str = this.keys.get(ent.getKey()))) {
							// 不存在
							return false;
						} else if (!str.equals(ent.getValue())) {
							// 不相同
							return false;
						}
					}
					// 通过验证的
					return true;
				}
			} else {
				return false;
			}
		}

		/**
		 * 验证路径是否匹配
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-7 下午5:39:45
		 * @param path 路径信息，要求：首尾去掉“/”的
		 * @return 匹配后的参数信息，如果不包含参数，也是零长，不会null；<br />
		 *         null，不匹配；<br />
		 */
		private boolean validatePath(final String path) {
			// 分解path
			final String[] paths = path.split("[/]");
			if (paths.length == this.tier) {
				for (final Entry<Integer, String> ent : this.keys.entrySet()) {
					if (!ent.getValue().equals(paths[ent.getKey()])) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}

		/**
		 * 得到连接中的对应Key内容
		 * 
		 * @author xuweijie
		 * @dateTime 2012-2-8 下午2:49:43
		 * @param paths 路径信息，要求：首尾去掉“/”的
		 * @return 匹配后的参数信息，如果不包含参数，也是零长，不会null；
		 * @see com.tfzzh.view.web.link.OperateLink.OperateLinkInfo#getKeyBack(java.lang.String[])
		 */
		@Override
		protected Map<String, List<String>> getKeyBack(final String paths[]) {
			final Map<String, List<String>> back = new HashMap<>(this.keys.size());
			final int gap = paths.length - this.tier;
			List<String> l;
			int li;
			int si;
			// 内容开始
			String cs;
			// 内容结束
			String ce;
			String line;
			// 内容信息
			List<String> cL;
			for (final Entry<Integer, List<String>> ent : this.paras.entrySet()) {
				line = paths[gap + ent.getKey()];
				l = ent.getValue();
				li = 0;
				cs = l.get(li);
				si = cs.length();
				while ((li + 2) < l.size()) {
					ce = l.get(li + 2);
					if (ce.equals("")) {
						// 一定是结尾
						back.put(l.get(li + 1), (cL = new LinkedList<>()));
						cL.add(line.substring(si, line.length()));
						break;
					} else {
						back.put(l.get(li + 1), (cL = new LinkedList<>()));
						cL.add(line.substring(si, (si = line.indexOf(ce, si))));
						si += ce.length();
					}
					li += 2;
				}
			}
			return back;
		}
	}

	/**
	 * 分流模式的连接控制信息
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-2 上午10:39:50
	 */
	private class BranchOperateLinkInfo extends OperateLinkInfo {

		/**
		 * @author xuweijie
		 * @dateTime 2012-2-2 上午10:40:31
		 * @param reflectControlKey 反射控制键
		 * @param prefix 路径前缀
		 *           // * @param accessPermissions 访问权限值
		 * @param targetNode 目标节点连接
		 * @param result 结果控制集合
		 * @param params 参数集合，有序的<br />
		 *           <参数对应key,参数的对象类型><br />
		 * @param readStreamJSON 是否读取stream相关json信息 add 2023-11-24
		 * @param canCrossDomain 是否可以跨域，true，可以跨域
		 * @param needToken 是否需要token验证
		 * @param description 说明
		 * @param ipRestr ip限制标识
		 */
		public BranchOperateLinkInfo(final String reflectControlKey, final String prefix, final OperateLinkNodeInfo targetNode, final String[] result, final Map<String, Class<?>> params, final boolean readStreamJSON, final boolean canCrossDomain, final boolean needToken, final String description, final String ipRestr) {
			// * @param accessPermissions 访问权限值 del 2023-11-24
			super(reflectControlKey, prefix, targetNode, result, params, readStreamJSON, canCrossDomain, needToken, description, ipRestr);
		}

		/**
		 * @author xuweijie
		 * @dateTime 2012-2-9 下午4:58:18
		 * @see com.tfzzh.view.web.link.OperateLink.OperateLinkInfo#getKeyBack(java.lang.String[])
		 */
		@Override
		protected Map<String, List<String>> getKeyBack(final String[] path) {
			return null;
		}
	}

	/**
	 * 控制连接节点
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-17 下午8:55:53
	 */
	public class OperateLinkNodeInfo {
		// /**
		// * 目标
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-17 下午9:12:49
		// */
		// private final String target;
		// /**
		// * 访问权限值
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-17 下午9:12:51
		// */
		// private final int value;
		// /**
		// * 说明
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-17 下午9:12:52
		// */
		// private final String description;
		// /**
		// * 目标节点连接
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-17 下午9:27:17
		// */
		// private final AccessPermissionsNodeInfo targetNode;
		// /**
		// * 访问权限值
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-17 下午9:12:53
		// */
		// private AccessPermissionsInfo accessPermissions = null;

		/**
		 * 节点目标集合
		 * 
		 * @author Weijie Xu
		 * @dateTime 2013-12-21 下午5:00:40
		 */
		private final Map<String, BackLinkOperationInfo> nodeTargets;

		/**
		 * @author Xu Weijie
		 * @dateTime 2012-7-17 下午9:14:01
		 * @param nodeResult 节点通用结果控制集合
		 */
		private OperateLinkNodeInfo(final String[] nodeResult) {
			// * @param target 目标
			// * @param value 访问权限值
			// * @param description 说明
			// * @param targetNode 目标节点连接
			// private OperateLinkNodeInfo(final String target, final int value, final String description, final AccessPermissionsNodeInfo targetNode, final String[] nodeResult) {
			// this.target = target;
			// this.value = value;
			// this.description = description;
			// this.targetNode = targetNode;
			this.nodeTargets = new TmpTool().getResultMap(nodeResult);
			// AccessPermissionsControl.getInstance().putAccessPermissions(this);
		}
		// /**
		// * 得到目标
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-17 下午9:06:18
		// * @return 目标值
		// * @see com.tfzzh.view.web.purview.AccessPermissionsInfo#getTarget()
		// */
		// @Override
		// public String getTarget() {
		// return this.target;
		// }
		// /**
		// * 得到访问权限值
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-17 下午9:06:18
		// * @return the accessPermission
		// * @see com.tfzzh.view.web.purview.AccessPermissionsInfo#getAccessPermissionsValue()
		// */
		// @Override
		// public int getAccessPermissionsValue() {
		// if (null == this.accessPermissions) {
		// return this.value;
		// } else {
		// return this.accessPermissions.getAccessPermissionsValue();
		// }
		// }
		// /**
		// * 得到说明
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-17 下午9:06:18
		// * @return the description
		// * @see com.tfzzh.view.web.purview.AccessPermissionsInfo#getDescription()
		// */
		// @Override
		// public String getDescription() {
		// return this.description;
		// }
		// /**
		// * 设置访问权限对象
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-17 下午9:06:18
		// * @param accessPermissions the accessPermissions to set
		// * @see com.tfzzh.view.web.purview.AccessPermissionsInfo#setAccessPermissions(com.tfzzh.view.web.purview.AccessPermissionsInfo)
		// */
		// @Override
		// public void setAccessPermissions(final AccessPermissionsInfo accessPermissions) {
		// if (null == this.accessPermissions) {
		// this.accessPermissions = accessPermissions;
		// }
		// }
		// /**
		// * 得到目标节点连接
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-17 下午9:27:52
		// * @return 目标节点连接
		// * @see com.tfzzh.view.web.purview.AccessPermissionsInfo#getTargetNode()
		// */
		// @Override
		// public AccessPermissionsNodeInfo getTargetNode() {
		// return this.targetNode;
		// }
		// /**
		// * 是否节点
		// *
		// * @author Xu Weijie
		// * @dateTime 2012-7-18 下午8:15:10
		// * @return true，是节点；<br />
		// * false，不是节点；<br />
		// * @see com.tfzzh.view.web.purview.AccessPermissionsInfo#isNode()
		// */
		// @Override
		// public boolean isNode() {
		// return true;
		// }

		/**
		 * 得到返回的操作信息<br />
		 * 此处不判定其是否为NULL，认为只要进入到此方法，一定会有存在的返回结果<br />
		 * 否则属于项目基础配置问题<br />
		 * 
		 * @author Weijie Xu
		 * @dateTime 2013-12-21 下午5:06:03
		 * @param target 目标Key
		 * @return 操作信息
		 */
		private BackLinkOperationInfo getBackResult(final String target) {
			if (null == this.nodeTargets) {
				return null;
			}
			return this.nodeTargets.get(target);
		}
	}

	/**
	 * 操作信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-5 上午11:13:41
	 */
	private class BackLinkOperationInfo {

		/**
		 * 目标
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-7-5 上午11:32:02
		 */
		private final String target;

		/**
		 * 作用到jsp的目标
		 * 
		 * @author tfzzh
		 * @dateTime 2016年8月30日 上午10:18:26
		 */
		private String jspTarget;

		/**
		 * Jsp前缀
		 * 
		 * @author tfzzh
		 * @dateTime 2016年8月29日 下午7:22:00
		 */
		private final String jspPrefix;

		/**
		 * Jsp后缀
		 * 
		 * @author tfzzh
		 * @dateTime 2016年8月29日 下午7:22:01
		 */
		private final String jspSuffix;

		/**
		 * @author tfzzh
		 * @dateTime 2016年8月30日 上午11:42:07
		 * @param target 目标
		 */
		private BackLinkOperationInfo(final String target) {
			this(target, null);
		}

		/**
		 * @author tfzzh
		 * @dateTime 2016年8月29日 下午7:28:59
		 * @param target 目标
		 * @param type 操作方式
		 */
		private BackLinkOperationInfo(final String target, final BackLinkOperationTypeEnum type) {
			this(target, type, WebBaseConstants.JSP_PREFIX, WebBaseConstants.JSP_SUFFIX);
		}

		/**
		 * @author Xu Weijie
		 * @dateTime 2012-7-5 上午11:33:40
		 * @param target 目标
		 * @param type 操作方式
		 * @param jspPrefix jsp前缀
		 * @param jspSuffix jsp后缀
		 */
		private BackLinkOperationInfo(final String target, final BackLinkOperationTypeEnum type, final String jspPrefix, final String jspSuffix) {
			this.target = StringTools.assemblyStringWhitInterval(target, false);
			this.jspPrefix = jspPrefix;
			this.jspSuffix = jspSuffix;
		}

		/**
		 * 得到目标JSP地址
		 * 
		 * @author tfzzh
		 * @dateTime 2016年8月29日 下午7:19:53
		 * @return 目标JSP地址
		 */
		private String getJspTarget() {
			if (null == this.jspTarget) {
				final StringBuilder sb = new StringBuilder(this.jspPrefix.length() + this.jspSuffix.length() + this.target.length());
				sb.append(this.jspPrefix).append(StringTools.splitStringWhitUpper(this.target)).append(this.jspSuffix);
				this.jspTarget = sb.toString();
			}
			return this.jspTarget;
		}

		/**
		 * 得到目标地址
		 * 
		 * @author tfzzh
		 * @dateTime 2016年8月29日 下午7:39:09
		 * @return 目标地址
		 */
		private String getTarget() {
			return this.target;
		}
	}

	/**
	 * 临时的工具处理
	 * 
	 * @author Weijie Xu
	 * @dateTime 2013-12-21 下午4:54:02
	 */
	private class TmpTool {

		/**
		 * 进行结果串的分析
		 * 
		 * @author Weijie Xu
		 * @dateTime 2013-12-21 下午4:54:10
		 * @param result 待分析的结果串
		 * @return 分析出的结果
		 */
		private Map<String, BackLinkOperationInfo> getResultMap(final String[] result) {
			String[] eles;
			final Map<String, BackLinkOperationInfo> targets = new HashMap<>(1, 1);
			StringBuilder value;
			String[] s;
			int ind;
			for (String str : result) {
				value = new StringBuilder();
				if ((str = str.trim()).length() > 0) {
					eles = str.split("[:]");
					if (eles.length >= 3) {
						if (eles[0].equalsIgnoreCase("d")) {
							// JSP合成情况
							// 方式，d开头，每“-”为一个路径间隔，每“_”为文件名称间隔
							s = eles[2].split("[-]");
							if ("d".equals(s[0].toLowerCase())) {
								// 标识头
								ind = 1;
							} else {
								ind = 0;
							}
							final String jspPrefix;
							if (eles.length >= 4) {
								jspPrefix = eles[3];
							} else {
								jspPrefix = WebBaseConstants.JSP_PREFIX;
							}
							// 路径的拼接
							for (final int n = s.length - 2; ind <= n; ind++) {
								value.append(s[ind]).append('/');
							}
							// if (s[s.length - 1].indexOf("_") == -1) {
							// // 需要将大写字符转换为
							// value.append(s[s.length - 1]);
							// } else {
							value.append(s[s.length - 1]);
							// }
							final String jspSuffix;
							if (eles.length >= 5) {
								jspSuffix = eles[4];
							} else {
								jspSuffix = WebBaseConstants.JSP_SUFFIX;
							}
							targets.put(eles[1], new BackLinkOperationInfo(value.toString(), BackLinkOperationTypeEnum.Dispatcher, jspPrefix, jspSuffix));
						} else if ("r".equals(eles[0].toLowerCase())) {
							// 服务器内跳转情况
							// 方式，r开头，每“_”为一个路径间隔
							s = eles[2].split("[_]");
							if ("r".equals(s[0])) {
								// 标识头
								ind = 1;
							} else {
								ind = 0;
							}
							// value = "";
							for (final int n = s.length - 2; ind <= n; ind++) {
								value.append(s[ind]).append('/');
							}
							value.append(s[ind]);
							targets.put(eles[1], new BackLinkOperationInfo(value.toString(), BackLinkOperationTypeEnum.Redirect));
						}
					} else if (eles.length == 2) {// 2016-08-29 new style
						targets.put(eles[0], new BackLinkOperationInfo(eles[1], null));
					}
				}
			}
			return targets.size() > 0 ? targets : null;
		}
	}
	// public static void main(String[] args) {
	// Map<String, Object> requestParas = new HashMap<>();
	// String boundary = "--------------------------9358da3a657bd0c6";
	// String cont = "--------------------------9358da3a657bd0c6\r\n" + "Content-Disposition: form-data; name=\"cc\"\r\n" + "\r\n" + "jzzq\r\n" + "--------------------------9358da3a657bd0c6\r\n" + "Content-Disposition: form-data; name=\"v\"\r\n" + "\r\n" + "202105\r\n" + "--------------------------9358da3a657bd0c6\r\n" + "Content-Disposition: form-data; name=\"usercode\"\r\n" + "\r\n" + "001MioGa1SrFkB0psuIa184BmK2MioGY\r\n" + "--------------------------9358da3a657bd0c6\r\n"
	// + "Content-Disposition: form-data; name=\"signature\"\r\n" + "\r\n" + "15c1e988ef9e5c0ae49f6b7590e283a60aa55231\r\n" + "--------------------------9358da3a657bd0c6\r\n" + "Content-Disposition: form-data; name=\"encryptedData\"\r\n" + "\r\n"
	// + "wbOdEDW QAiBBCZ6TnX6gJFZotH1ujGKpPBYHaiFkDbXOsWZ1wld2wCG9vAODheFGZsALwVsm6C6bt4E5otWhSy0UDkfyNvGSX2Q63HsuHqFGLGsvQ WTKfirxhucVkOIFEl4i2J3B2vypQ4t8RoDid4cCWznhmWclLrp6Z9s962o3IHUDbKdydfqN gWZQuRP7vFhhZUTxi3hRnZn5cpeJU43NGW9D4uoI R0eRjbnW1TEv UB9ocQh0zc CHoXj 6RoL8nGuBikhsJFpu7Sm/Ridfkc3AplvFKuKx3lY3e88LVHMNAqdd/GFlS2ILXs vtCqseYg2NBdSdi6xeCkLm7qzlqU7Pr90epk2kegMGGQRrSUavHtqeudfErGLzsb0BitYBxsYlyzN1kQZOAXrxH6aJu7Axjcpw4eSSjfulW642m69BG/aAgBQ8px3dlWtOxttSDOGiQhZ3apaKZA==\r\n"
	// + "--------------------------93";
	// List<String> fields = StringTools.splitToArray(cont, boundary);
	// int fns, fne, fvs, fve;
	// String key, val;
	// for (String fc : fields) {
	// if (StringTools.isNullOrEmpty(fc)) {
	// continue;
	// }
	// fc = fc.toLowerCase();
	// if ((fns = fc.indexOf(OperateLink.S_CONTENT_DISPOSITION)) == -1) {
	// // 非有效数据
	// continue;
	// }
	// fns += OperateLink.S_CONTENT_DISPOSITION.length();
	// fne = fc.indexOf(Constants.DOUBLE_QUOTATION, fns);
	// if (fne == -1) {
	// // 非有效数据
	// continue;
	// }
	// fvs = fc.indexOf(OperateLink.S_X_WWW_VAL_START, fne);
	// if (fvs == -1) {
	// // 非有效数据
	// continue;
	// }
	// fvs += OperateLink.S_X_WWW_VAL_START.length();
	// fve = fc.indexOf(OperateLink.S_X_WWW_VAL_END, fvs);
	// if (fve == -1) {
	// // 非有效数据
	// continue;
	// }
	// key = fc.substring(fns, fne);
	// val = fc.substring(fvs, fve);
	// requestParas.put(key, val);
	// }
	// System.out.println(requestParas);
	// }
	// public static void main(String[] args) {
	// String ct = "application/x-www-form-urlencoded; boundary=------------------------16c1b94e3716fd25;a";
	// int bdys = ct.indexOf(OperateLink.S_BOUNDARY);
	// int bdye = ct.indexOf(Constants.SEMICOLON_LINE, bdys);
	// final String bdy;
	// if (bdye == -1) {
	// bdy = ct.substring(bdys + OperateLink.S_BOUNDARY.length());
	// } else {
	// bdy = ct.substring(bdys + OperateLink.S_BOUNDARY.length(), bdye);
	// }
	// System.out.println(bdy);
	// }
}
