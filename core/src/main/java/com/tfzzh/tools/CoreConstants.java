/**
 * @author Weijie Xu
 * @dateTime 2017年3月22日 下午5:24:01
 */
package com.tfzzh.tools;

import com.tfzzh.core.annotation.PropertiesValue;

/**
 * 核心常量
 * 
 * @author Weijie Xu
 * @dateTime 2017年3月22日 下午5:24:01
 */
public class CoreConstants extends BaseConstants {

	static {
		CoreConstantsPool.getInstance().getConstants(CoreConstants.class);
	}

	/**
	 * 通过默认名称文件
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午5:25:28
	 */
	public CoreConstants() {
		super("core_message");
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午5:24:23
	 * @param bundleName 资源名
	 */
	public CoreConstants(final String bundleName) {
		super(bundleName);
	}

	/**
	 * 系统编码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午5:27:03
	 */
	@PropertiesValue
	public static String SYSTEM_CODE = "UTF-8";

	/**
	 * 是否Windows系统
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午5:41:39
	 */
	public static boolean OS_WIN = System.getProperty("os.name").toLowerCase().startsWith("win");

	/**
	 * 初始化：配置文件路径：基础路径；</br>
	 * 用于类对象，或者配置文件路径的操作；</br>
	 * 最后会有“/”作为结束符号；</br>
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午5:45:12
	 */
	public static String INIT_CONFIG_PATH_BASE = FileTools.purifyFilePath((Constants.class.getResource("/") != null) ? (Constants.OS_WIN ? Constants.class.getResource("/").getPath().substring(1) : Constants.class.getResource("/").getPath()) : (Constants.OS_WIN ? Constants.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1) + "/../config/" : Constants.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/../config/"));

	/**
	 * 初始化：应用文件路径：基础路径；</br>
	 * 用于纯文件相关的路径的操作；</br>
	 * 最后会有“/”作为结束符号；</br>
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午5:45:13
	 */
	public static String INIT_CONFIG_FILE_PATH_BASE = FileTools.purifyFilePath((Constants.INIT_CONFIG_PATH_BASE.indexOf(".jar") == -1) ? (Constants.INIT_CONFIG_PATH_BASE + "../../") : ((Constants.INIT_CONFIG_PATH_BASE.indexOf(".jar") == -1) ? (Constants.INIT_CONFIG_PATH_BASE + "../../") : (Constants.INIT_CONFIG_PATH_BASE.substring(0, Constants.INIT_CONFIG_PATH_BASE.substring(0, Constants.INIT_CONFIG_PATH_BASE.indexOf(".jar")).lastIndexOf("/")) + "/../")));

	/**
	 * 初始化：管理配置：DTD文件名称
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午5:45:14
	 */
	@PropertiesValue
	public static String INIT_MANAGER_DTD;

	/**
	 * 分页默认类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午5:45:16
	 */
	@PropertiesValue
	public static int PAGERANK_DEFAULT_TYPE;

	/**
	 * 数据：一天的毫秒数
	 * 
	 * @author Weijie Xu
	 * @dateTime 2017年3月22日 下午5:45:17
	 */
	@PropertiesValue
	public static long DATA_ONE_DAY_SECOND;
}
