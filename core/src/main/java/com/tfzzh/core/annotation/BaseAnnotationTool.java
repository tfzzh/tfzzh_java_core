/**
 * @author Weijie Xu
 * @dateTime 2014年4月11日 下午10:43:32
 */
package com.tfzzh.core.annotation;

/**
 * 注解解析基础类
 * 
 * @author Weijie Xu
 * @dateTime 2014年4月11日 下午10:43:32
 */
public abstract class BaseAnnotationTool {

	/**
	 * 读取注解路径
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月11日 下午10:44:35
	 * @param analysePath 解析路径
	 */
	public void readerAnnotationPath(final String analysePath) {
		final String[] paths = analysePath.split("[,]");
		for (String path : paths) {
			// System.out.println("\t read path >> " + path);
			if ((path = path.trim()).length() > 0) {
				path = path.replaceAll("[/]", ".");
				this.readerPathFiles(path);
			}
		}
	}

	/**
	 * 读取路径下所有文件
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月11日 下午10:44:33
	 * @param path 目标路径
	 */
	protected abstract void readerPathFiles(String path);
}
