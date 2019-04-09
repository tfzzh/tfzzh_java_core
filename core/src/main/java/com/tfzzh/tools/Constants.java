/**
 * @author XuWeijie
 * @dateTime Apr 25, 2010 4:48:21 PM
 */
package com.tfzzh.tools;

/**
 * 系统级常用常量参数
 * 
 * @author XuWeijie
 * @dateTime Apr 25, 2010 4:48:21 PM
 * @model
 */
public interface Constants {

	/**
	 * 系统级编码设置<br />
	 * 先从getenv()的“LANG”中获取<br />
	 * 默认：UTF-8<br />
	 * 
	 * @author XuWeijie
	 * @dateTime Apr 25, 2010 4:49:03 PM
	 */
	String SYSTEM_CODE = "UTF-8";

	// String SYSTEM_CODE = Messages.getString("Constants.SystemCode");
	// String SYSTEM_CODE = System.getenv("LANG") == null ? "UTF-8" : System.getenv("LANG").substring(System.getenv("LANG").indexOf(".") + 1);
	/**
	 * 是否Windows系统
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-5 下午9:59:58
	 */
	boolean OS_WIN = System.getProperty("os.name").toLowerCase().indexOf("win") != -1;

	/**
	 * 初始化：配置文件路径：基础路径；<br />
	 * 用于类对象，或者配置文件路径的操作；<br />
	 * 最后会有“/”作为结束符号；<br />
	 * 
	 * @author TFZZH
	 * @dateTime 2011-2-21 下午04:34:57
	 */
	String INIT_CONFIG_PATH_BASE = FileTools.purifyFilePath((Constants.class.getResource("/") != null) ? (Constants.OS_WIN ? Constants.class.getResource("/").getPath().substring(1) : Constants.class.getResource("/").getPath()) : (Constants.OS_WIN ? Constants.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1) + "/../config/" : Constants.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/../config/")) + "/";

	/**
	 * 初始化：应用文件路径：基础路径；<br />
	 * 用于纯文件相关的路径的操作；<br />
	 * 最后会有“/”作为结束符号；<br />
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-10-8 下午5:39:44
	 */
	// String INIT_CONFIG_FILE_PATH_BASE = FileTools.purifyFilePath((Constants.INIT_CONFIG_PATH_BASE.indexOf(".jar") == -1) ? (Constants.INIT_CONFIG_PATH_BASE + "../../") : ((Constants.INIT_CONFIG_PATH_BASE.indexOf(".jar") == -1) ? (Constants.INIT_CONFIG_PATH_BASE + "../../") : (Constants.INIT_CONFIG_PATH_BASE.substring(0, Constants.INIT_CONFIG_PATH_BASE.substring(0, Constants.INIT_CONFIG_PATH_BASE.indexOf(".jar")).lastIndexOf("/")) + "/../")));
	String INIT_CONFIG_FILE_PATH_BASE = FileTools.purifyFilePath((Constants.INIT_CONFIG_PATH_BASE.indexOf(".jar") == -1) ? (Constants.INIT_CONFIG_PATH_BASE + "/../../") : (Constants.INIT_CONFIG_PATH_BASE.substring(0, Constants.INIT_CONFIG_PATH_BASE.substring(0, Constants.INIT_CONFIG_PATH_BASE.indexOf(".jar")).lastIndexOf("/")) + "/../")) + "/";

	/**
	 * 初始化：管理配置：DTD文件名称
	 * 
	 * @author TFZZH
	 * @dateTime 2011-2-21 上午10:33:11
	 */
	String INIT_MANAGER_DTD = "dtd/ControllerDTD.dtd";

	/**
	 * 分页默认类型
	 * 
	 * @author TFZZH
	 * @dateTime 2011-5-7 下午03:09:53
	 */
	int PAGERANK_DEFAULT_TYPE = Integer.parseInt("1");

	/**
	 * 数据：一天的毫秒数
	 * 
	 * @author xuweijie
	 * @dateTime 2012-3-22 下午5:08:41
	 */
	long DATA_ONE_DAY_SECOND = 1000l * 60l * 60l * 24l;
}
