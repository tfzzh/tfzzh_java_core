/**
 * @author Weijie Xu
 * @dateTime 2014年9月19日 上午11:02:50
 */
package com.tfzzh.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * 文件工具
 * 
 * @author Weijie Xu
 * @dateTime 2014年9月19日 上午11:02:50
 */
public class FileTools {

	/**
	 * 净化文件路径
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月24日_下午3:57:26
	 * @param path 原始路径
	 * @return 净化后的路径
	 */
	public static String purifyFilePath(String path) {
		boolean firstX = false;
		if (path.startsWith("/") || path.startsWith("\\\\")) {
			firstX = true;
		}
		// 替换统一字符
		path = path.replaceAll("\\\\", "/");
		int ind = path.indexOf("://");
		if (ind == -1) {
			if (path.startsWith("//")) {
				ind = 1;
			} else {
				ind = 0;
			}
		} else {
			ind += 2;
		}
		path = StringTools.replace(path, "//", "/", ind);
		// 主要针对win系统
		StringTools.replace(path, "%20", " ", 0);
		final List<String> ls = StringTools.splitToArray(ind == 0 ? path : path.substring(ind + 1), "/");
		String s;
		for (int i = 0; i < ls.size();) {
			s = ls.get(i);
			if ("..".equals(s)) {
				// 是需要向上的
				if (i == 0) {
					// 是第一个，这时跳出
					break;
				} else {
					ls.remove(i);
					ls.remove(--i);
					continue;
				}
			}
			i++;
		}
		if (ind > 0) {
			return path.substring(0, ind + 1) + StringTools.listToString(ls, "/");
		} else {
			if (ls.get(0).endsWith(":")) {
				// 是盘符路径
				return StringTools.listToString(ls, "/");
			} else {
				// 是一般路径
				return (firstX ? "/" : "") + StringTools.listToString(ls, "/");
			}
		}
	}

	/**
	 * 针对获取配置资源文件得到内容
	 * 
	 * @author 许纬杰
	 * @datetime 2016年2月4日_上午11:31:15
	 * @param bundleName 配置资源文件名
	 * @return 资源文件内容对象
	 */
	public static ResourceBundle getResourceBundle(final String bundleName) {
		return FileTools.getResourceBundle(bundleName, null, null);
	}

	/**
	 * 针对获取配置资源文件得到内容
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 下午12:00:43
	 * @param bundleName 配置资源文件名
	 * @param clz 所相关类对象
	 * @return 资源文件内容对象
	 */
	public static ResourceBundle getResourceBundle(final String bundleName, final Class<?> clz) {
		return FileTools.getResourceBundle(bundleName, null, clz);
	}

	/**
	 * 针对获取配置资源文件得到内容
	 * 
	 * @author 许纬杰
	 * @datetime 2016年2月4日_上午11:15:16
	 * @param bundleName 配置资源文件名
	 * @param folderName 文件夹名
	 * @return 资源文件内容对象
	 */
	public static ResourceBundle getResourceBundle(final String bundleName, final String folderName) {
		return FileTools.getResourceBundle(bundleName, folderName, null);
	}

	/**
	 * 针对获取配置资源文件得到内容
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月21日 上午11:57:19
	 * @param bundleName 配置资源文件名
	 * @param folderName 文件夹名
	 * @param clz 所相关类对象
	 * @return 资源文件内容对象
	 */
	public static ResourceBundle getResourceBundle(final String bundleName, final String folderName, final Class<?> clz) {
		if (Constants.class.getResource("/") != null) {
			if (null == clz) {
				return ResourceBundle.getBundle(bundleName);
			} else {
				return ResourceBundle.getBundle(bundleName, Locale.getDefault(), clz.getClassLoader());
			}
		} else {
			try {
				final File f = new File(FileTools.purifyFilePath(Constants.INIT_CONFIG_PATH_BASE + ((folderName == null) ? "" : "/../" + folderName + "/") + bundleName + (bundleName.endsWith(".properties") ? "" : ".properties")));
				final FileInputStream fis = new FileInputStream(f);
				final ResourceBundle rb = new PropertyResourceBundle(new InputStreamReader(new BufferedInputStream(fis), Constants.SYSTEM_CODE));
				return rb;
			} catch (final IOException e) {
				e.printStackTrace();
				System.exit(0);
				return null;
			}
		}
	}

	/**
	 * 获取配置资源文件对象内容
	 * 
	 * @author tfzzh
	 * @dateTime 2020年7月27日 下午1:25:57
	 * @param bundleFile 目标资源文件文件对象
	 * @return 资源文件内容对象
	 */
	public static ResourceBundle getResourceBundle(final File bundleFile) {
		try {
			return new PropertyResourceBundle(new InputStreamReader(new BufferedInputStream(new FileInputStream(bundleFile)), Constants.SYSTEM_CODE));
		} catch (final IOException e) {
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}

	/**
	 * 得到文件在项目中路径
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月14日_下午4:36:15
	 * @param filePath 文件相对路径
	 * @return 文件在项目中路径
	 */
	public static String getFileProjectPath(final String filePath) {
		return FileTools.purifyFilePath(Constants.INIT_CONFIG_PATH_BASE + "/" + filePath);
	}

	/**
	 * 得到文件比较用信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年9月19日 上午11:12:05
	 * @param file 文件信息
	 * @return 文件比较用信息
	 */
	public static FileCompareInfo getFileCompareInfo(final File file) {
		return new FileCompareInfo(file);
	}

	/**
	 * 进行两个文件的比较
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年9月19日 上午11:12:50
	 * @param f1 文件1
	 * @param f2 文件2
	 * @return true，两文件内容相同；<br />
	 *         false，两文件内容不同；<br />
	 */
	public static boolean compareFile(final File f1, final File f2) {
		final FileCompareInfo fc1 = new FileCompareInfo(f1);
		final FileCompareInfo fc2 = new FileCompareInfo(f2);
		return fc1.equals(fc2);
	}

	/**
	 * 删除目标文件夹下所有内容，包括所有文件与文件夹
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年12月10日 下午1:20:12
	 * @param tarPath 目标文件夹路径
	 */
	public static void deletefile(final String tarPath) {
		final File file = new File(tarPath);
		if (!file.exists()) {
			// 因为不存在而停止
			return;
		}
		// 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
		if (!file.isDirectory()) {
			file.delete();
		} else {
			FileTools.deleteFolder(file);
		}
	}

	/**
	 * 删除文件夹
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年12月10日 下午1:39:05
	 * @param tarFolder 目标文件夹信息
	 * @return true，被完全删除；<br />
	 *         false，未被完全删除；<br />
	 */
	private static boolean deleteFolder(final File tarFolder) {
		boolean delSelf = true;
		for (final File f : tarFolder.listFiles()) {
			if (!f.isDirectory()) {
				f.delete();
			} else {
				if (f.getName().equals(".svn")) {
					delSelf = false;
					continue;
				}
				if (delSelf) {
					delSelf = FileTools.deleteFolder(f);
				} else {
					FileTools.deleteFolder(f);
				}
			}
		}
		tarFolder.delete();
		return delSelf;
	}

	/**
	 * 文件比较用信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年9月19日 上午11:03:24
	 */
	public static class FileCompareInfo extends BaseBean {

		/**
		 * @author tfzzh
		 * @dateTime 2016年9月8日 下午5:03:04
		 */
		private static final long serialVersionUID = 658948260523790245L;

		/**
		 * 文件长度
		 * 
		 * @author Weijie Xu
		 * @dateTime 2014年9月19日 上午11:03:50
		 */
		private final long size;

		/**
		 * 文件对象
		 * 
		 * @author Weijie Xu
		 * @dateTime 2014年12月8日 下午4:59:44
		 */
		private final File file;

		/**
		 * @author Weijie Xu
		 * @dateTime 2014年9月19日 上午11:05:11
		 * @param file 处理的文件
		 */
		private FileCompareInfo(final File file) {
			this.size = file.length();
			this.file = file;
		}

		/**
		 * 得到文件长度
		 * 
		 * @author Weijie Xu
		 * @dateTime 2014年9月19日 上午11:05:36
		 * @return the size
		 */
		public long getSize() {
			return this.size;
		}

		/**
		 * 不多说，你懂得
		 * 
		 * @author Weijie Xu
		 * @dateTime 2014年9月19日 上午11:11:09
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(final Object obj) {
			if (obj instanceof FileCompareInfo) {
				final FileCompareInfo c = (FileCompareInfo) obj;
				if (this.size == c.size) {
					// 逐字节比较内容差异
					try (final FileInputStream is1 = new FileInputStream(this.file); final FileInputStream is2 = new FileInputStream(c.file);) {
						final byte[] bs1 = new byte[4096];
						final byte[] bs2 = new byte[4096];
						int g1 = 0, g2 = 0;
						int ind1 = 0;
						int ind2 = 0;
						while ((g1 = is1.read(bs1)) != -1) {
							while ((ind2 < g2) || ((g2 = is2.read(bs2)) != -1)) {
								for (; (ind1 < g1) && (ind2 < g2); ind1++, ind2++) {
									if (bs1[ind1] != bs2[ind2]) {
										// 错误的情况
										return false;
									}
								}
								if (ind1 == g1) {
									break;
								}
								ind2 = 0;
							}
							ind1 = 0;
						}
						// 成功的
						return true;
					} catch (final IOException e) {
						e.printStackTrace();
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}
}
