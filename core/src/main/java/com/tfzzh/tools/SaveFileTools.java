/**
 * @author Xu Weijie
 * @datetime 2017年8月10日_下午3:45:55
 */
package com.tfzzh.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;

/**
 * 保存文件工具
 * 
 * @author Xu Weijie
 * @datetime 2017年8月10日_下午3:45:55
 */
public class SaveFileTools {

	/**
	 * 记录内容到文件
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月10日_下午3:45:55
	 * @param cont 目标内容
	 * @param path 文件路径：<br />
	 *           带“-”为当前项目执行的相对路径；<br />
	 *           不带为绝对路径；<br />
	 * @param fileName 文件名
	 * @return true，保存成功
	 */
	public static boolean saveContToFile(final String cont, final String path, final String fileName) {
		final String diskPath;
		if (path.startsWith("-")) {
			// 是当前运行应用相对路径
			diskPath = FileTools.purifyFilePath(Constants.INIT_CONFIG_FILE_PATH_BASE + File.separatorChar + path.substring(1));
		} else {
			// 针对指定目标的绝对路径
			diskPath = path;
		}
		// 处理文件
		final File fd = new File(diskPath);
		if (!fd.exists()) {
			fd.mkdirs();
		}
		final File f = new File(diskPath + File.separatorChar + fileName);
		try {
			if (!f.exists()) {
				// 因为不存在而创建
				// f.createNewFile();
				Files.createFile(f.toPath(), FileTools.FILE_ATTR);
			} else {
				Files.setPosixFilePermissions(f.toPath(), FileTools.FILE_PERMISSION);
			}
			final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), Constants.SYSTEM_CODE));
			out.write(cont);
			out.flush();
			out.close();
			return true;
		} catch (final IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
