/**
 * @author TFZZH
 * @dateTime 2011-11-3 下午4:14:43
 */
package com.tfzzh.view.web.bean;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import com.tfzzh.view.web.tools.UploadBackCodeEnum;
import com.tfzzh.view.web.tools.UploadFileNameTypeEnum;

/**
 * 文件上传信息用Bean
 * 
 * @author TFZZH
 * @dateTime 2011-11-3 下午4:14:43
 */
public final class UploadFileBean extends BaseUploadFileBean {

	/**
	 * @author Xu Weijie
	 * @datetime 2017年9月28日_下午2:05:35
	 */
	private static final long serialVersionUID = 887119059821200587L;

	/**
	 * 文件信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-28 下午2:10:31
	 */
	private final FileItem fileInfo;

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-9-28 下午4:58:28
	 * @param fileInfo 文件信息
	 * @param addTimestamp 是否在文件名中增加时间戳
	 * @param folderPath 存储文件路径
	 * @param fileNameType 文件名类型
	 * @param targetFileName 目标文件名
	 * @param suffixs 后缀名限定
	 * @param paramMap 请求的参数集合
	 */
	public UploadFileBean(final FileItem fileInfo, final boolean addTimestamp, final String folderPath, final UploadFileNameTypeEnum fileNameType, final String targetFileName, final String suffixs, final Map<String, Object> paramMap) {
		super(fileInfo.getName(), addTimestamp, folderPath, fileNameType, targetFileName, suffixs, paramMap);
		this.fileInfo = fileInfo;
	}

	/**
	 * 得到文件大小
	 * 
	 * @author tfzzh
	 * @dateTime 2016年8月25日 下午6:12:30
	 * @return 文件大小
	 */
	@Override
	public long getSize() {
		return this.fileInfo.getSize();
	}

	/**
	 * 将文件存盘
	 * 
	 * @author Xu Weijie
	 * @dateTime 2012-9-28 下午1:54:24
	 * @return 被记录的文件信息；<br />
	 *         null，表示记录文件失败；<br />
	 */
	@Override
	public SaveFileBackBean fileToDisk() {
		// 转存文件到硬盘
		{ // 得到或创建目标文件夹
			final File folder = new File(this.getFolderPath());
			if (!folder.exists()) {
				folder.mkdirs();
			}
		}
		final File file = new File(this.getFolderPath() + "/" + this.getFileName());
		try {
			// 创建内容文件
			file.createNewFile();
			// 写入内容到文件
			this.fileInfo.write(file);
			return new SaveFileBackBean(UploadBackCodeEnum.OK, file);
		} catch (final IOException e) {
			return new SaveFileBackBean(UploadBackCodeEnum.IOException);
		} catch (final Exception e) {
			return new SaveFileBackBean(UploadBackCodeEnum.UnknowExceptioin);
		}
	}
}