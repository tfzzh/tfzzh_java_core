/**
 * @author XuWeijie
 * @dateTime Aug 11, 2010 2:56:46 PM
 */
package com.tfzzh.tools;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author XuWeijie
 * @dateTime Aug 11, 2010 2:56:46 PM
 * @model
 */
public class Messages {

	/**
	 * @author XuWeijie
	 * @dateTime Aug 11, 2010 2:57:11 PM
	 */
	private static final ResourceBundle RESOURCE_BUNDLE = FileTools.getResourceBundle("/messages_core");

	/**
	 * @author XuWeijie
	 * @dateTime Aug 11, 2010 2:57:12 PM
	 */
	private Messages() {
	}

	/**
	 * @author XuWeijie
	 * @dateTime Aug 11, 2010 2:57:13 PM
	 * @param key 键值名称
	 * @return 内容
	 */
	public static String getString(final String key) {
		try {
			return Messages.RESOURCE_BUNDLE.getString(key);
		} catch (final MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
