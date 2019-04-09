/**
 * @author Weijie Xu
 * @dateTime 2017年3月23日 下午6:57:19
 */
package com.tfzzh.view.web.tools;

import com.tfzzh.core.annotation.PropertiesValue;
import com.tfzzh.tools.BaseConstants;
import com.tfzzh.tools.CoreConstantsPool;

/**
 * web工具常量
 * 
 * @author Weijie Xu
 * @dateTime 2017年3月23日 下午6:57:19
 */
public class WebBaseConstants extends BaseConstants {

	static {
		CoreConstantsPool.getInstance().getConstants(WebBaseConstants.class);
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 下午7:02:14
	 */
	public WebBaseConstants() {
		super("web_message");
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年3月23日 下午6:57:22
	 * @param bundleName 资源名
	 */
	public WebBaseConstants(final String bundleName) {
		super(bundleName);
	}

	/**
	 * 初始化：连接页面配置：DTD文件名称
	 * 
	 * @author TFZZH
	 * @dateTime 2011-2-21 上午10:33:15
	 */
	@PropertiesValue
	public static String INIT_LINK_PAGE_DTD;

	/**
	 * 已经登录到系统的用户，作为存储在Session的信息的key名称
	 * 
	 * @author xuweijie
	 * @dateTime 2012-2-28 上午11:17:23
	 */
	@PropertiesValue
	public static String SESSION_KEY_USER;

	/**
	 * 请求信息中所产生的问题数据集合<br />
	 * 在request中出去的非null内容一定是Map结构<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-10-11 上午11:32:45
	 */
	@PropertiesValue
	public static String REQUEST_KEY_ERROR_BACK;

	/**
	 * 与JSP合成时，jsp文件的路径前缀
	 * 
	 * @author TFZZH
	 * @dateTime 2011-5-6 下午11:38:55
	 */
	@PropertiesValue
	public static String JSP_PREFIX;

	/**
	 * 与JSP合成时，jsp文件的路径后缀
	 * 
	 * @author TFZZH
	 * @dateTime 2011-5-7 上午01:58:56
	 */
	@PropertiesValue
	public static String JSP_SUFFIX;

	/**
	 * URL后缀
	 * 
	 * @author TFZZH
	 * @dateTime 2011-5-4 下午03:53:06
	 */
	@PropertiesValue
	public static String URL_POS;

// /**
// * 私有的，连接名称，针对HttpRequest
// *
// * @author xuweijie
// * @dateTime 2012-2-13 下午12:07:22
// */
// @PropertiesValue
// public static String PRIVATE_LINK_NAME_REQUEST;
// /**
// * 私有的，连接名称，针对HttpResponse
// *
// * @author xuweijie
// * @dateTime 2012-2-13 下午12:07:24
// */
// @PropertiesValue
// public static String PRIVATE_LINK_NAME_RESPONSE;
// /**
// * 私有的，连接名称，针对HttpSession
// *
// * @author Xu Weijie
// * @dateTime 2012-7-6 下午4:09:04
// */
// @PropertiesValue
// public static String PRIVATE_LINK_NAME_SESSION;
// /**
// * 私有的，连接名称，针对请求的所有参数
// *
// * @author tfzzh
// * @dateTime 2016年10月10日 下午10:31:07
// */
// @PropertiesValue
// public static String PRIVATE_LINK_NAME_ALL_PARAM;
// /**
// * 私有的，连接名称，针对Bean集合
// *
// * @author xuweijie
// * @dateTime 2012-2-13 下午12:09:20
// */
// @PropertiesValue
// public static String PRIVATE_LINK_NAME_BEAN;
// /**
// * 私有的，连接名称，针对登陆帐号信息
// *
// * @author Weijie Xu
// * @dateTime 2012-7-17 上午1:54:28
// */
// @PropertiesValue
// public static String PRIVATE_LINK_NAME_ACCOUNT;
// /**
// * 私有的，连接名称，针对IP
// *
// * @author Xu Weijie
// * @dateTime 2012-7-6 下午4:13:42
// */
// @PropertiesValue
// public static String PRIVATE_LINK_NAME_IP;
// /**
// * 私有的，连接名称，来自当前请求的完整url
// *
// * @author XuWeijie
// * @datetime 2015年8月25日_下午8:17:46
// */
// @PropertiesValue
// public static String PRIVATE_LINK_NAME_URL;
// /**
// * 私有的，连接名称，针对请求后缀名
// *
// * @author Xu Weijie
// * @dateTime 2012-9-9 下午12:24:55
// */
// @PropertiesValue
// public static String PRIVATE_LINK_NAME_SUFFIX;
	/**
	 * 跳转请求参数Bean命名后缀
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-7-21 下午4:00:28
	 */
	@PropertiesValue
	public static String REDIRECT_SESSION_PARAM_BEAN_POS;

	/**
	 * 上传文件块大小设置
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-11 上午1:39:56
	 */
	@PropertiesValue
	public static int UPLOAD_FILE_SIZE_THRESHOLD;

	/**
	 * 自定session，保存host所相关cookie的host名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月13日 下午1:33:37
	 */
	@PropertiesValue
	public static String CUSTOM_SESSION_TOKEN_HOST_NAME;

	/**
	 * 自定session，对应tokenControl实现的对象命名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月13日 下午9:38:47
	 */
	@PropertiesValue
	public static String CUSTOM_SESSION_TOKEN_CONTROL_IMPL_NAME;

	/**
	 * 自定session，对应paramValidateControl实现的对象命名
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年9月28日_上午10:38:06
	 */
	@PropertiesValue
	public static String CUSTOM_VALIDATE_CONTROL_IMPL_NAME;

	/**
	 * 一些需要被记录进cookie中数据的，有效时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月12日 下午8:17:30
	 */
	@PropertiesValue
	public static int COOKIE_TIME;

	/**
	 * 自定义SESSION的名字参数命名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月7日 上午11:41:50
	 */
	@PropertiesValue
	public static String CUSTOM_SESSION_NAME_KEY;

	/**
	 * 自定义SESSION的SCOPE参数命名
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年12月6日_下午4:12:37
	 */
	@PropertiesValue
	public static String CUSTOM_SESSION_SCOPE_KEY;

	/**
	 * 自定义SESSION的TOKEN参数命名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月7日 上午11:41:51
	 */
	@PropertiesValue
	public static String CUSTOM_SESSION_TOKEN_KEY;

	/**
	 * 会话池时间任务时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月7日 上午10:39:34
	 */
	@PropertiesValue
	public static long SESSION_POOL_TASK_TIME;

	/**
	 * 会话池内容验证时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月7日 上午11:24:55
	 */
	@PropertiesValue
	public static long SESSION_POOL_VALIDATE_TIME;

	/**
	 * 与客户端会话标识的令牌字串长度
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月27日 下午7:10:43
	 */
	@PropertiesValue
	public static int SESSION_TOKEN_LENGTH;

	/**
	 * 与客户端会话标识的令牌时间串转换所在字串中位置
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月27日 下午7:12:33
	 */
	@PropertiesValue
	public static int SESSION_TOKEN_TIME_INDEX;

	/**
	 * 连接session过期时间（单位毫秒），非token过期时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月28日 上午10:04:33
	 */
	@PropertiesValue
	public static long SESSION_EXPIRATION_TIME;

	/**
	 * token有效时间（单位毫秒）
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月28日 上午10:05:13
	 */
	@PropertiesValue
	public static long TOKEN_EXPIRATION_TIME;

	/**
	 * 返回内容，最大输出长度
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年4月16日 下午12:36:22
	 */
	@PropertiesValue
	public static int BACK_MAX_LENGTH;
}
