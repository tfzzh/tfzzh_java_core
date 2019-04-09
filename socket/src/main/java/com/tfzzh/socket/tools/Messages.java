/**
 * @author 许纬杰
 * @datetime 2016年3月8日_下午2:12:41
 */
package com.tfzzh.socket.tools;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.tfzzh.tools.FileTools;

/**
 * @author 许纬杰
 * @datetime 2016年3月8日_下午2:12:41
 */
public class Messages {

	/**
	 * @author 许纬杰
	 * @datetime 2016年3月8日_下午2:20:16
	 */
	private static final String BUNDLE_NAME = "messages_socket";

	/**
	 * @author 许纬杰
	 * @datetime 2016年3月8日_下午2:16:19
	 */
	private static final ResourceBundle RESOURCE_BUNDLE = FileTools.getResourceBundle(Messages.BUNDLE_NAME);

	/**
	 * @author 许纬杰
	 * @datetime 2016年3月8日_下午2:16:20
	 */
	private Messages() {
	}

	/**
	 * @author 许纬杰
	 * @datetime 2016年3月8日_下午2:16:21
	 * @param key 消息键
	 * @return 目标值
	 */
	public static String getString(final String key) {
		try {
			return Messages.RESOURCE_BUNDLE.getString(key);
		} catch (final MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
