/**
 * @author TFZZH
 * @dateTime 2011-11-3 下午4:14:43
 */
package com.tfzzh.view.web.bean;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import com.tfzzh.tools.Constants;
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
	 * 得到文件原始名
	 * 
	 * @author tfzzh
	 * @dateTime 2021年2月5日 下午3:26:05
	 * @return 文件原始名
	 */
	public String getOriginalName() {
		return this.fileInfo.getName();
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
				if (!Constants.OS_WIN) {
					try {
						Runtime.getRuntime().exec(new String[] { "chmod 777 -R " + folder.getPath() });
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		final File file = new File(this.getFolderPath() + Constants.DIAGONAL_LINE + this.getFileName());
		try {
			// 创建内容文件
			// file.createNewFile();
			// 写入内容到文件
			file.setReadable(true, false);
			file.setWritable(true, false);
			file.setExecutable(true, false);
			this.fileInfo.write(file);
			if (!Constants.OS_WIN) {
				try {
					Runtime.getRuntime().exec(new String[] { "chmod 777 -R " + file.getPath() });
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
			return new SaveFileBackBean(UploadBackCodeEnum.OK, file);
		} catch (final IOException e) {
			e.printStackTrace();
			return new SaveFileBackBean(UploadBackCodeEnum.IOException);
		} catch (final Exception e) {
			e.printStackTrace();
			return new SaveFileBackBean(UploadBackCodeEnum.UnknowExceptioin);
		}
	}
}
