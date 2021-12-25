/**
 * @author TFZZH
 * @dateTime 2016年10月14日 下午4:49:33
 */
package com.tfzzh.view.web.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.tfzzh.tools.Constants;
import com.tfzzh.view.web.tools.UploadBackCodeEnum;
import com.tfzzh.view.web.tools.UploadFileNameTypeEnum;

/**
 * 文件上传信息用Bean
 * 
 * @author TFZZH
 * @dateTime 2016年10月14日 下午4:49:33
 */
public final class UploadByteFileBean extends BaseUploadFileBean {

	/**
	 * @author Xu Weijie
	 * @datetime 2017年9月28日_下午2:05:30
	 */
	private static final long serialVersionUID = -4575899019628895251L;

	/**
	 * 文件信息
	 * 
	 * @author Xu Weijie
	 * @dateTime 2016年10月14日 下午4:49:33
	 */
	private final String fileByte;

	/**
	 * @author Xu Weijie
	 * @dateTime 2016年10月14日 下午4:49:33
	 * @param fileByte 文件字节信息
	 * @param fileName 文件名
	 * @param addTimestamp 是否在文件名中增加时间戳
	 * @param folderPath 存储文件路径
	 * @param fileNameType 文件名类型
	 * @param targetFileName 目标文件名
	 * @param suffixs 后缀名限定
	 * @param paramMap 请求的参数集合
	 */
	public UploadByteFileBean(final String fileByte, final String fileName, final boolean addTimestamp, final String folderPath, final UploadFileNameTypeEnum fileNameType, final String targetFileName, final String suffixs, final Map<String, Object> paramMap) {
		super(fileName, addTimestamp, folderPath, fileNameType, targetFileName, suffixs, paramMap);
		this.fileByte = fileByte;
	}

	/**
	 * 得到文件大小
	 * 
	 * @author tfzzh
	 * @dateTime 2016年10月14日 下午4:49:33
	 * @return 文件大小
	 */
	@Override
	public long getSize() {
		return this.fileByte.length();
	}

	/**
	 * 将文件存盘
	 * 
	 * @author Xu Weijie
	 * @dateTime 2016年10月14日 下午4:49:33
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
		// final Base64 decoder = new Base64();
		final int ind = this.fileByte.indexOf(Constants.COMMA);
		final String tfileByte = this.fileByte.substring(ind + 1);
		try {
			final byte[] db = Base64.decodeBase64(tfileByte);
			// final byte[] db = decoder.decodeBuffer(tfileByte);
			final FileOutputStream out = new FileOutputStream(this.getFolderPath() + Constants.DIAGONAL_LINE + this.getFileName());
			out.write(db);
			out.close();
			final File file = new File(this.getFolderPath() + Constants.DIAGONAL_LINE + this.getFileName());
			// 写入内容到文件
			file.setReadable(true, false);
			file.setWritable(true, false);
			file.setExecutable(true, false);
			if (!Constants.OS_WIN) {
				try {
					Runtime.getRuntime().exec("chmod 777 -R " + file.getPath());
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
			return new SaveFileBackBean(UploadBackCodeEnum.OK, file);
		} catch (final IOException e) {
			return new SaveFileBackBean(UploadBackCodeEnum.IOException);
		} catch (final Exception e) {
			return new SaveFileBackBean(UploadBackCodeEnum.UnknowExceptioin);
		}
	}
}
