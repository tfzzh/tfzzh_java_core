/**
 * @author XuWeijie
 * @dateTime May 19, 2010 10:26:09 AM
 */
package com.tfzzh.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 抄自——spring中相对应类
 * 
 * @author XuWeijie
 * @dateTime May 19, 2010 10:26:09 AM
 * @model
 */
public abstract class NestedExceptionUtils {

	/**
	 * 创建消息
	 * 
	 * @author XuWeijie
	 * @dateTime May 19, 2010 10:26:25 AM
	 * @param message 基础消息
	 * @param cause 抛出对象
	 * @return 消息内容
	 */
	public static String buildMessage(final String message, final Throwable cause) {
		if (cause != null) {
			// 存在其他的异常相关
			final StringBuilder buf = new StringBuilder();
			if (message != null) {
				buf.append(message).append("; ");
			}
			buf.append("nested exception is ").append(cause);
			return buf.toString();
		} else {
			// 只属于自身的异常内容
			return message;
		}
	}

	/**
	 * 进行消息组合
	 * 
	 * @author XuWeijie
	 * @dateTime Aug 2, 2010 5:29:26 PM
	 * @param messages 消息集合
	 * @return 组合好的串，中间使用“|”分割
	 */
	public static String messageCombination(final String... messages) {
		if (messages == null) {
			// 因不存在，返回零长串
			return "";
		} else {
			final StringBuilder sb = new StringBuilder();
			for (int i = 0, s = messages.length - 1; i <= s; i++) {
				sb.append(messages[i]);
				if (i != s) {
					// 最后一个不加该分割符
					sb.append("|");
				}
			}
			return sb.toString();
		}
	}

	/**
	 * 进行消息拆分
	 * 
	 * @author XuWeijie
	 * @dateTime Aug 2, 2010 7:43:51 PM
	 * @param message 消息集合串
	 * @return 被拆分好的集合
	 */
	public static List<String> messageSplit(final String message) {
		if (message == null) {
			return new ArrayList<>();
		} else {
			final String[] strs = message.trim().split("[|]");
			final List<String> list = new ArrayList<>(strs.length);
			for (final String str : strs) {
				list.add(str);
			}
			return list;
		}
	}
}
