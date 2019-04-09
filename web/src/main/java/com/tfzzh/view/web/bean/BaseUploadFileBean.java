/**
 * @author TFZZH
 * @dateTime 2011-11-3 下午4:14:43
 */
package com.tfzzh.view.web.bean;

import java.io.File;
import java.util.Map;

import com.tfzzh.tools.BaseBean;
import com.tfzzh.tools.Constants;
import com.tfzzh.tools.FileTools;
import com.tfzzh.tools.PropertiesTools;
import com.tfzzh.view.web.tools.UploadBackCodeEnum;
import com.tfzzh.view.web.tools.UploadFileNameTypeEnum;

/**
 * 文件上传信息用Bean
 * 
 * @author TFZZH
 * @dateTime 2016年10月14日 下午4:40:29
 */
public abstract class BaseUploadFileBean extends BaseBean {

	/**
	 * @author Xu Weijie
	 * @datetime 2017年9月28日_下午2:05:44
	 */
	private static final long serialVersionUID = -1253737186866367940L;

	/**
	 * 预存放本地路径——相对路径地址
	 * 
	 * @author Xu Weijie
	 * @dateTime 2016年10月14日 下午4:40:29
	 */
	private String relativeFolderPath;

	/**
	 * 预存放本地路径——绝对路径地址
	 * 
	 * @author Xu Weijie
	 * @dateTime 2016年10月14日 下午4:40:29
	 */
	private String folderPath;

	/**
	 * 文件本地名
	 * 
	 * @author tfzzh
	 * @dateTime 2016年10月14日 下午4:40:29
	 */
	private final String fileOldName;

	/**
	 * 文件名称
	 * 
	 * @author Xu Weijie
	 * @dateTime 2016年10月14日 下午4:40:29
	 */
	private final String fileName;

	/**
	 * 后缀名
	 * 
	 * @author tfzzh
	 * @dateTime 2016年10月14日 下午4:40:29
	 */
	private String posName;

	/**
	 * @author Xu Weijie
	 * @dateTime 2016年10月14日 下午4:40:29
	 * @param fileName 文件名称
	 * @param addTimestamp 是否在文件名中增加时间戳
	 * @param folderPath 存储文件路径
	 * @param fileNameType 文件名类型
	 * @param targetFileName 目标文件名
	 * @param suffixs 后缀名限定
	 * @param paramMap 请求的参数集合
	 */
	public BaseUploadFileBean(final String fileName, final boolean addTimestamp, final String folderPath, final UploadFileNameTypeEnum fileNameType, final String targetFileName, final String suffixs, final Map<String, Object> paramMap) {
		this.setFolderPath(folderPath);
		this.fileOldName = fileName;
		this.fileName = this.getTrueFileName(fileName, addTimestamp, fileNameType, targetFileName, "|" + suffixs + "|", paramMap);
	}

	/**
	 * 设置文件预存储路径
	 * 
	 * @author Xu Weijie
	 * @dateTime 2016年10月14日 下午4:40:29
	 * @param folderPath 路徑首位为“-”：接续当前应用所在硬盘路径；<br />
	 *           首位无“-”：全新的路径；<br />
	 */
	public void setFolderPath(final String folderPath) {
		if (folderPath.startsWith("-")) {
			// 是当前运行应用相对路径
			this.relativeFolderPath = folderPath.substring(1);
			this.folderPath = FileTools.purifyFilePath(Constants.INIT_CONFIG_FILE_PATH_BASE + File.separator + this.relativeFolderPath);
		} else {
			// 针对指定目标的绝对路径
			this.relativeFolderPath = null;
			this.folderPath = folderPath;
		}
	}

	/**
	 * 设置文件路径<br />
	 * url为关联数据处理<br />
	 * 磁盘为单一数据处理<br />
	 * 
	 * @author tfzzh
	 * @dateTime 2016年10月14日 下午4:40:29
	 * @param mainUrlPath 主URL地址
	 * @param urlPrefix url辅路径前缀
	 * @param diskPath 硬盘路径
	 */
	public void setFolderPath(final String mainUrlPath, String urlPrefix, String diskPath) {
		urlPrefix = PropertiesTools.getConstantsValue(urlPrefix);
		diskPath = PropertiesTools.getConstantsValue(diskPath);
		this.relativeFolderPath = FileTools.purifyFilePath(mainUrlPath + File.separator + urlPrefix);
		if (diskPath.startsWith("-")) {
			this.folderPath = FileTools.purifyFilePath(Constants.INIT_CONFIG_FILE_PATH_BASE + File.separator + diskPath.substring(1));
		} else {
			this.folderPath = FileTools.purifyFilePath(diskPath);
		}
	}

	/**
	 * 设置相关url及磁盘位置路径
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月10日_下午4:46:53
	 * @param urlPrefix url前缀
	 * @param diskPrefix 磁盘路径前缀
	 * @param path 路径
	 */
	public void setBothFolderPath(final String urlPrefix, final String diskPrefix, final String path) {
		this.relativeFolderPath = PropertiesTools.getConstantsValue(FileTools.purifyFilePath(urlPrefix + File.separator + path));
		if (diskPrefix.startsWith("-")) {
			this.folderPath = PropertiesTools.getConstantsValue(FileTools.purifyFilePath(Constants.INIT_CONFIG_FILE_PATH_BASE + File.separator + diskPrefix.substring(1) + File.separator + path));
		} else {
			this.folderPath = PropertiesTools.getConstantsValue(FileTools.purifyFilePath(diskPrefix + File.separator + path));
		}
	}

	/**
	 * 得到真实的文件名称
	 * 
	 * @author Xu Weijie
	 * @dateTime 2016年10月14日 下午4:40:29
	 * @param localName 文件名
	 * @param addTimestamp 是否在文件名中增加时间戳
	 * @param fileNameType 文件名类型
	 * @param targetFileName 目标文件名
	 * @param suffixs 允许后缀
	 * @param paramMap 请求的参数集合
	 * @return 真实的文件名称
	 */
	private String getTrueFileName(final String localName, final boolean addTimestamp, final UploadFileNameTypeEnum fileNameType, final String targetFileName, final String suffixs, final Map<String, Object> paramMap) {
		String pos = null;
		final String fileNamePrefix;
		// 得到后缀名
		final int posInd = localName.lastIndexOf(".");
		if (posInd != -1) {
// if (!"||".equals(suffixs) && !"|*|".equals(suffixs)) {
// pos = "|" + localName.substring(posInd + 1) + "|";
// if (null != suffixs) { // 分析后缀名
// // 存在后缀名情况
// if (!suffixs.equals("*")) {
// // 需要指定文件类型
// if (suffixs.indexOf(pos) == -1) {
// // 不在指定的后缀名之列
// error.put("fileError", "Dno't support the file type.");
// return null;
// }
// } else {
// // 不存在后缀
// error.put("fileError", "文件不存在后缀");
// return null;
// }
// }
// }
			fileNamePrefix = localName.substring(0, posInd);
			pos = localName.substring(posInd + 1);
		} else {
			// 不需要判断后缀名情况
			fileNamePrefix = localName;
			pos = "";
		}
		this.posName = pos;
		// 定义文件名
		return fileNameType.getFileSaveName(fileNamePrefix, addTimestamp, pos, targetFileName, paramMap);
	}

	/**
	 * 得到预存放本地路径——绝对路径地址
	 * 
	 * @author tfzzh
	 * @dateTime 2016年10月14日 下午4:48:08
	 * @return the folderPath
	 */
	protected String getFolderPath() {
		return this.folderPath;
	}

	/**
	 * 得到文件名称
	 * 
	 * @author tfzzh
	 * @dateTime 2016年10月14日 下午4:48:08
	 * @return the fileName
	 */
	protected String getFileName() {
		return this.fileName;
	}

	/**
	 * 得到后缀名
	 * 
	 * @author tfzzh
	 * @dateTime 2016年10月14日 下午4:40:29
	 * @return 后缀名
	 */
	public String getPosName() {
		return this.posName;
	}

	/**
	 * 得到文件大小
	 * 
	 * @author tfzzh
	 * @dateTime 2016年10月14日 下午4:40:29
	 * @return 文件大小
	 */
	public abstract long getSize();

	/**
	 * 将文件存盘
	 * 
	 * @author Xu Weijie
	 * @dateTime 2016年10月14日 下午4:40:29
	 * @return 被记录的文件信息；<br />
	 *         null，表示记录文件失败；<br />
	 */
	public abstract SaveFileBackBean fileToDisk();

	/**
	 * 记录数据存档返回信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2016年10月14日 下午4:40:29
	 */
	public class SaveFileBackBean extends BaseBean {

		/**
		 * @author tfzzh
		 * @dateTime 2016年10月14日 下午4:52:47
		 */
		private static final long serialVersionUID = -7140496656351389388L;

		/**
		 * 返回码
		 * 
		 * @author TFZZH
		 * @dateTime 2016年10月14日 下午4:40:29
		 */
		private final UploadBackCodeEnum back;

		/**
		 * 所相关的文件
		 * 
		 * @author tfzzh
		 * @dateTime 2016年10月14日 下午4:40:29
		 */
		private final File file;

		/**
		 * 针对错误情况
		 * 
		 * @author tfzzh
		 * @dateTime 2016年10月14日 下午4:40:29
		 * @param back 返回的信息
		 */
		protected SaveFileBackBean(final UploadBackCodeEnum back) {
			this(back, null);
		}

		/**
		 * @author Xu Weijie
		 * @dateTime 2016年10月14日 下午4:40:29
		 * @param back 返回的信息
		 * @param file 所相关的文件
		 */
		protected SaveFileBackBean(final UploadBackCodeEnum back, final File file) {
			this.back = back;
			this.file = file;
		}

		/**
		 * 得到返回码
		 * 
		 * @author TFZZH
		 * @dateTime 2011-11-3 下午4:24:17
		 * @return the back
		 */
		public UploadBackCodeEnum getBack() {
			return this.back;
		}

		/**
		 * 得到所相关的文件
		 * 
		 * @author tfzzh
		 * @dateTime 2016年8月25日 下午6:55:15
		 * @return the file
		 */
		public File getFile() {
			return this.file;
		}

		/**
		 * 得到文件大小
		 * 
		 * @author tfzzh
		 * @dateTime 2016年10月12日 下午8:11:32
		 * @return 文件大小
		 */
		public long getFileSize() {
			return BaseUploadFileBean.this.getSize();
		}

		/**
		 * 得到文件本地名
		 * 
		 * @author tfzzh
		 * @dateTime 2016年8月25日 下午6:45:55
		 * @return the fileOldName
		 */
		public String getFileOldName() {
			return BaseUploadFileBean.this.fileOldName;
		}

		/**
		 * 得到文件名
		 * 
		 * @author Xu Weijie
		 * @dateTime 2016年8月25日 下午6:45:55
		 * @return the fileName
		 */
		public String getFileName() {
			return BaseUploadFileBean.this.fileName;
		}

		/**
		 * 得到对应当前运行服务所在地址的相对路径
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-10-9 下午3:42:40
		 * @return the relativePath
		 */
		public String getRelativePath() {
			return BaseUploadFileBean.this.relativeFolderPath;
		}

		/**
		 * 得到硬盘存储路径
		 * 
		 * @author Xu Weijie
		 * @dateTime 2012-9-26 下午12:32:30
		 * @return the diskPath
		 */
		public String getDiskPath() {
			return BaseUploadFileBean.this.folderPath;
		}
	}
}
