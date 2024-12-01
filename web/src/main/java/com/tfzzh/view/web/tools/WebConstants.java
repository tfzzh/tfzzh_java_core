/**
 * @author TFZZH
 * @dateTime 2011-3-12 上午10:18:23
 */
package com.tfzzh.view.web.tools;

/**
 * @author TFZZH
 * @dateTime 2011-3-12 上午10:18:23
 */
public interface WebConstants {

	// /**
	// * 初始化：连接页面配置：DTD文件名称
	// *
	// * @author TFZZH
	// * @dateTime 2011-2-21 上午10:33:15
	// */
	// String INIT_LINK_PAGE_DTD = "dtd/LinkPageDTD.dtd";
	// /**
	// * 已经登录到系统的用户，作为存储在Session的信息的key名称
	// *
	// * @author xuweijie
	// * @dateTime 2012-2-28 上午11:17:23
	// */
	// String SESSION_KEY_USER = "la";
	// /**
	// * 请求信息中所产生的问题数据集合<br />
	// * 在request中出去的非null内容一定是Map结构<br />
	// *
	// * @author Xu Weijie
	// * @dateTime 2012-10-11 上午11:32:45
	// */
	// String REQUEST_KEY_ERROR_BACK = "err_bak";
	// /**
	// * 与JSP合成时，jsp文件的路径前缀
	// *
	// * @author TFZZH
	// * @dateTime 2011-5-6 下午11:38:55
	// */
	// String JSP_PREFIX = "/WEB-INF/jsp/";
	// /**
	// * 与JSP合成时，jsp文件的路径后缀
	// *
	// * @author TFZZH
	// * @dateTime 2011-5-7 上午01:58:56
	// */
	// String JSP_SUFFIX = ".jsp";
	// /**
	// * URL后缀
	// *
	// * @author TFZZH
	// * @dateTime 2011-5-4 下午03:53:06
	// */
	// String URL_POS = ".shtml";
	//////////////////////
	/**
	 * 请求：文件类提交类型
	 *
	 * @author tfzzh
	 * @dateTime 2024年9月2日 10:31:39
	 */
	String REQUEST_MULTIPART = "multipart/";

	/**
	 * 请求：post模式
	 *
	 * @author tfzzh
	 * @dateTime 2024年9月2日 10:37:39
	 */
	String REQUEST_POST = "post";

	/**
	 * 私有的，连接名称，针对HttpRequest
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-13 下午12:07:22
	 */
	String PRIVATE_LINK_NAME_REQUEST = "request";

	/**
	 * 私有的，连接名称，针对HttpResponse
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-13 下午12:07:24
	 */
	String PRIVATE_LINK_NAME_RESPONSE = "response";

	/**
	 * 私有的，连接名称，针对HttpSession
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-6 下午4:09:04
	 */
	String PRIVATE_LINK_NAME_SESSION = "session";

	/**
	 * 私有的，连接名称，针对自定义Session
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月7日 下午3:18:02
	 */
	String PRIVATE_LINK_NAME_CUSTOM_SESSION = "custom_session";

	/**
	 * 私有的，连接名称，针对请求的所有参数
	 * 
	 * @author tfzzh
	 * @dateTime 2016年10月10日 下午10:31:07
	 */
	String PRIVATE_LINK_NAME_ALL_PARAM = "param";

	/**
	 * 私有的，连接名称，针对Bean集合
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-13 下午12:09:20
	 */
	String PRIVATE_LINK_NAME_BEAN = "bean";

	/**
	 * 私有的，连接名称，针对登陆帐号信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2012-7-17 上午1:54:28
	 */
	String PRIVATE_LINK_NAME_ACCOUNT = "account";

	/**
	 * 私有的，连接名称，针对IP
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-6 下午4:13:42
	 */
	String PRIVATE_LINK_NAME_IP = "ip";

	/**
	 * 私有的，连接名称，针对首位IP
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年10月17日_下午3:27:32
	 */
	String PRIVATE_LINK_NAME_FIRST_IP = "fip";

	/**
	 * 私有的，连接名称，来自当前请求的完整url
	 * 
	 * @author XuWeijie
	 * @datetime 2015年8月25日_下午8:17:46
	 */
	String PRIVATE_LINK_NAME_URL = "url";

	/**
	 * 私有的，连接名称，来自当前请求的user-agent
	 * 
	 * @author tfzzh
	 * @dateTime 2021年12月25日 上午10:51:19
	 */
	String PRIVATE_LINK_NAME_USER_AGENT = "user-agent";

	/**
	 * 私有的，连接名称，针对请求后缀名
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-9 下午12:24:55
	 */
	String PRIVATE_LINK_NAME_SUFFIX = "suffix";

	/**
	 * 私有的，有问题参数
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月28日_下午6:01:05
	 */
	String PRIVATE_PARAM_ERROR = "param_err";

	/**
	 * KEY，内跳相关到request.attribute
	 * 
	 * @author tfzzh
	 * @dateTime 2023年11月25日 11:24:42
	 */
	String KEY_REQUEST_ATTRIBUTE = "req_attr_";

	/**
	 * KEY，加密POST stream 请求，解密后数据到request.attribute
	 * 
	 * @author tfzzh
	 * @dateTime 2023年11月25日 11:20:26
	 */
	String KEY_REQUEST_DECRYPT_STREAM_ATTRIBUTE = "req_dec_sr_attr";
	// /**
	// * 跳转请求参数Bean命名后缀
	// *
	// * @author Xu Weijie
	// * @dateTime 2012-7-21 下午4:00:28
	// */
	// String REDIRECT_SESSION_PARAM_BEAN_POS = "-ParamBean";
	//
	// /**
	// * 上传文件块大小设置
	// *
	// * @author Xu Weijie
	// * @dateTime 2012-9-11 上午1:39:56
	// */
	// int UPLOAD_FILE_SIZE_THRESHOLD = 1024 * 2;
	//
	// /**
	// * 默认的跨域情况，true，为可跨域
	// *
	// * @author XuWeijie
	// * @datetime 2015年9月21日_下午5:17:17
	// */
	// boolean DEFAULT_CROSS_DOMAIN = Boolean.parseBoolean("true");
}
