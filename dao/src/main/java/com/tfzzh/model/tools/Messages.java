/**
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午4:09:00
 */
package com.tfzzh.model.tools;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.tfzzh.tools.FileTools;

/**
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午4:09:00
 */
public class Messages {

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午4:10:14
	 */
	private static final String BUNDLE_NAME = "dao_messages";

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午4:10:13
	 */
	private static final ResourceBundle RESOURCE_BUNDLE = FileTools.getResourceBundle(Messages.BUNDLE_NAME);

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午4:10:12
	 */
	private Messages() {
	}

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月20日 下午4:10:15
	 * @param key 目标键
	 * @return 对应的值
	 */
	public static String getString(final String key) {
		try {
			return Messages.RESOURCE_BUNDLE.getString(key);
		} catch (final MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
