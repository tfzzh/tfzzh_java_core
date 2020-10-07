/**
 * @author xuweijie
 * @dateTime 2012-1-20 下午4:22:09
 */
package com.tfzzh.socket.tools;

import java.util.Set;

import com.tfzzh.core.annotation.BaseAnnotationTool;
import com.tfzzh.socket.annotation.InitiativeOperation;
import com.tfzzh.socket.initiative.OperationAction;
import com.tfzzh.tools.ClassTool;

/**
 * 操作行为注解解析
 * 
 * @author xuweijie
 * @dateTime 2012-1-20 下午4:22:09
 */
public class OperationAnnotationTool extends BaseAnnotationTool {

	/**
	 * 读取路径下所有文件
	 * 
	 * @author xuweijie
	 * @dateTime 2012-1-29 上午11:48:48
	 * @param path 目标路径
	 * @see com.tfzzh.core.annotation.BaseAnnotationTool#readerPathFiles(java.lang.String)
	 */
	@Override
	protected void readerPathFiles(final String path) {
		final Set<Class<?>> clzs = ClassTool.getClasses(path);
		InitiativeOperation as;
		Class<?>[] ifs;
		OperationAction oa;
		for (final Class<?> clz : clzs) {
			ifs = clz.getInterfaces();
			for (final Class<?> cl : ifs) {
				if (cl == OperationAction.class) {
					// 是目标类型
					as = clz.getAnnotation(InitiativeOperation.class);
					if (null != as) {
						try {
							oa = (OperationAction) clz.getDeclaredConstructor().newInstance();
							// 将对象放入到池中
							OperationPool.getInstance().putOperationAction(as.value(), oa);
						} catch (final Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
