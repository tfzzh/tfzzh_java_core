/**
 * @author Weijie Xu
 * @dateTime 2017年3月22日 下午4:39:08
 */
package com.tfzzh.tools;

import com.tfzzh.core.annotation.PropertiesValue;

/**
 * 基础工具常量
 * 
 * @author Weijie Xu
 * @dateTime 2017年3月22日 下午4:39:08
 */
public class BaseToolsConstants extends BaseConstants {

	static {
		CoreConstantsPool.getInstance().getConstants(BaseToolsConstants.class);
	}

	/**
	 * 通过默认名称文件
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午4:45:47
	 */
	public BaseToolsConstants() {
		this("base_tools_message");
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午4:41:35
	 * @param bundleName 资源名
	 */
	public BaseToolsConstants(final String bundleName) {
		super(bundleName);
	}

	/**
	 * 项目长codo，限定最小长度，不要小于20
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午5:17:39
	 */
	@PropertiesValue
	public static int LONG_CODE_LIMIT_MIN;

	/**
	 * 项目长codo，限定最大长度
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午5:17:39
	 */
	@PropertiesValue
	public static int LONG_CODE_LIMIT_MAX;

	/**
	 * 项目长code，针对时间字串，所字符串转换替换用字符集
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午7:19:57
	 */
	@PropertiesValue
	public static String[] LONG_CODE_TIME_RANDOM_CODE;
}
