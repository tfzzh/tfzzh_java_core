/**
 * @author Xu Weijie
 * @dateTime 2012-9-16 下午12:06:48
 */
package com.tfzzh.view.web.tools;

import java.util.List;
import java.util.Map;

import com.tfzzh.tools.DateFormat;
import com.tfzzh.tools.Spelling;

/**
 * 上传文件后缀名类型
 * 
 * @author Xu Weijie
 * @dateTime 2012-9-16 下午12:06:48
 */
public enum UploadFileNameTypeEnum {

	/**
	 * 与名称相同
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-16 下午4:10:51
	 */
	Same {

		@Override
		public String getFileSaveName(final String fileName, final boolean addTimestamp, final String fileNameSuffix, final String targetFileName, final Map<String, Object> paramMap) {
			final StringBuilder sb = new StringBuilder();
			sb.append(fileName);
			if (addTimestamp) {
				sb.append('_').append(DateFormat.getLongDate());
			}
			sb.append('.').append(fileNameSuffix);
			return sb.toString();
		}
	},
	/**
	 * 中文转拼音，完全
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-16 下午4:10:53
	 */
	SpellingAll {

		@Override
		public String getFileSaveName(final String fileName, final boolean addTimestamp, final String fileNameSuffix, final String targetFileName, final Map<String, Object> paramMap) {
			final StringBuilder sb = new StringBuilder();
			sb.append(Spelling.getInstance().getSpellingWithString(fileName));
			if (addTimestamp) {
				sb.append("_" + DateFormat.getLongDate());
			}
			sb.append('.').append(fileNameSuffix);
			return sb.toString();
		}
	},
	/**
	 * 中文转拼音，仅单字首字母
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-18 上午11:51:58
	 */
	SpellingHead {

		@Override
		public String getFileSaveName(final String fileName, final boolean addTimestamp, final String fileNameSuffix, final String targetFileName, final Map<String, Object> paramMap) {
			final StringBuilder sb = new StringBuilder();
			sb.append(Spelling.getInstance().getHeadSpellingWithString(fileName));
			if (addTimestamp) {
				sb.append("_" + DateFormat.getLongDate());
			}
			sb.append('.').append(fileNameSuffix);
			return sb.toString();
		}
	},
	/**
	 * 指定名称
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-16 下午4:12:20
	 */
	Designation {

		@Override
		public String getFileSaveName(final String fileName, final boolean addTimestamp, final String fileNameSuffix, final String targetFileName, final Map<String, Object> paramMap) {
			final StringBuilder sb = new StringBuilder();
			sb.append(targetFileName);
			if (addTimestamp) {
				sb.append("_" + DateFormat.getLongDate());
			}
			sb.append('.').append(fileNameSuffix);
			return sb.toString();
		}
	},
	/**
	 * 根据指定字段内容
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-17 下午3:07:36
	 */
	Field {

		@SuppressWarnings("unchecked")
		@Override
		public String getFileSaveName(final String fileName, final boolean addTimestamp, final String fileNameSuffix, final String targetFileName, final Map<String, Object> paramMap) {
			final StringBuilder sb = new StringBuilder();
			sb.append(((List<String>) paramMap.get(targetFileName)).get(0));
			if (addTimestamp) {
				sb.append("_" + DateFormat.getLongDate());
			}
			sb.append('.').append(fileNameSuffix);
			return sb.toString();
		}
	};

	/**
	 * 得到文件记录用名
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-26 下午3:23:44
	 * @param fileName 文件名称——前缀
	 * @param addTimestamp 是否在文件名中增加时间戳
	 * @param fileNameSuffix 文件后缀名
	 * @param targetFileName 目标的文件名称
	 * @param paramMap 参数列表：对有效键的对应值必须存在，且类型为List<String>
	 * @return 被记录的文件名
	 */
	public abstract String getFileSaveName(String fileName, boolean addTimestamp, String fileNameSuffix, String targetFileName, Map<String, Object> paramMap);
}
