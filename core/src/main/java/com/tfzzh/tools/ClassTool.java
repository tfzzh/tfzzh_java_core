/**
 * @author Weijie Xu
 * @dateTime 2014-3-22 下午2:55:28
 */
package com.tfzzh.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类工具得到包下所有类文件<br />
 * 代码来源于网络<br />
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-22 下午2:55:28
 */
public class ClassTool {

	/**
	 * 得到包下类文件，不向下查找
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-22 下午3:08:09
	 * @param pack 包路径
	 * @return 类文件列表
	 */
	public static Set<Class<?>> getClasses(final String pack) {
		return ClassTool.getClasses(pack, false);
	}

	/**
	 * 从包package中获取所有的Class
	 * 
	 * @param pack 包路径
	 * @param recursive 是否循环迭代
	 * @return 类文件列表
	 */
	public static Set<Class<?>> getClasses(String pack, final boolean recursive) {
		// 第一个class类的集合
		final Set<Class<?>> classes = new LinkedHashSet<>();
		// 获取包的名字 并进行替换
		String packageDirName = pack.replace('.', '/');
		if (packageDirName.endsWith("/")) {
			// 去掉之后的分隔符
			packageDirName = packageDirName.substring(0, packageDirName.length() - 1);
		}
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			// 循环迭代下去
			// final ClassLoader cl = ClassLoader.getSystemClassLoader();
			final ClassLoader cl = ClassTool.class.getClassLoader();
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				final URL url = dirs.nextElement();
				// System.out.println("\t\t\t > url : " + url.getFile() + " > " + url.getProtocol() + " > " + packageDirName);
				// 得到协议的名称
				final String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					// System.err.println("file类型的扫描");
					// 获取包的物理路径
					final String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					ClassTool.findAndAddClassesInPackageByFile(pack, filePath, recursive, classes);
				} else if ("jar".equals(protocol)) {
					// 如果是jar包文件
					// 定义一个JarFile
					// System.err.println("jar类型的扫描");
					JarFile jar;
					try {
						// 获取jar
						jar = ((JarURLConnection) url.openConnection()).getJarFile();
						// 从此jar包 得到一个枚举类
						final Enumeration<JarEntry> entries = jar.entries();
						// System.out.println();
						// 同样的进行循环迭代
						while (entries.hasMoreElements()) {
							// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
							final JarEntry entry = entries.nextElement();
							String name = entry.getName();
							// 如果是以/开头的
							if (name.charAt(0) == '/') {
								// 获取后面的字符串
								name = name.substring(1);
							}
							// System.out.println("\t\t\t\tentry >> " + name + " >>> " + packageDirName);
							// 如果前半部分和定义的包名相同
							if (name.startsWith(packageDirName)) {
								final int idx = name.lastIndexOf('/');
								// 如果以"/"结尾 是一个包
								if (idx != -1) {
									// 获取包名 把"/"替换成"."
									pack = name.substring(0, idx).replace('/', '.');
								}
								// 如果可以迭代下去 并且是一个包
								if (idx != -1) {
									// System.out.println(">>\t\t\t\t length : " + idx + "--" + packageDirName.length());
									if ((idx != packageDirName.length()) && !recursive) {
										continue;
									}
									// 如果是一个.class文件 而且不是目录
									if (name.endsWith(".class") && !entry.isDirectory()) {
										// 去掉后面的".class" 获取真正的类名
										final String className = name.substring(pack.length() + 1, name.length() - 6);
										try {
											// 添加到classes
											classes.add(cl.loadClass(pack + '.' + className));
										} catch (final ClassNotFoundException e) {
											// log
											// .error("添加用户自定义视图类错误 找不到此类的.class文件");
											e.printStackTrace();
										}
									}
								}
							}
						}
					} catch (final IOException e) {
						// log.error("在扫描用户定义视图时从jar包获取文件出错");
						e.printStackTrace();
					}
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return classes;
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 * 
	 * @param packageName 包名
	 * @param packagePath 实际路径
	 * @param recursive 是否循环迭代
	 * @param classes 存储集合
	 */
	private static void findAndAddClassesInPackageByFile(final String packageName, final String packagePath, final boolean recursive, final Set<Class<?>> classes) {
		// 获取此包的目录 建立一个File
		final File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			// log.warn("用户定义包名 " + packageName + " 下没有任何文件");
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		final File[] dirfiles = dir.listFiles(new FileFilter() {

			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			@Override
			public boolean accept(final File file) {
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});
		final ClassLoader cl = ClassTool.class.getClassLoader();
		// 循环所有文件
		for (final File file : dirfiles) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				ClassTool.findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				final String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					// 添加到集合中去
					// classes.add(Class.forName(packageName + '.' + className));
					// 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
					classes.add(cl.loadClass(packageName + '.' + className));
				} catch (final ClassNotFoundException e) {
					// log.error("添加用户自定义视图类错误 找不到此类的.class文件");
					// e.printStackTrace();
					try {
						classes.add(Class.forName(packageName + '.' + className, false, cl));
					} catch (final ClassNotFoundException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 得到当前所属方法名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月21日 下午1:16:01
	 * @return 类名:方法名
	 */
	public static String getCurrentMethodName() {
		// 0-java.lang.Thread:getStackTrace
		// 1-com.tfzzh.tools.ClassTool:getCurrentMethodName
		// final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		return new StringBuilder().append(Thread.currentThread().getStackTrace()[2]).toString();
	}

	/**
	 * 得到当前所在方法的前一个调用方法名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月17日 下午4:56:43
	 * @return 类名:方法名<br />
	 *         如果没有前一个方法，则显示当前的<br />
	 */
	public static String getBeforeMethodName() {
		final StackTraceElement[] es = Thread.currentThread().getStackTrace();
		if (es.length > 3) {
			// final StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
			return new StringBuilder().append(Thread.currentThread().getStackTrace()[3]).toString();
		} else {
			// final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
			return new StringBuilder().append("Havn't BeforeMethod. The Current Method : ").append(Thread.currentThread().getStackTrace()[2]).toString();
		}
	}

	/**
	 * 得到当前所在方法的上层被调用路径
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月17日 下午4:56:45
	 * @return 类名:方法名集合
	 */
	public static String getFullMethodName() {
		return ClassTool.getFullMethodName(18);
	}

	/**
	 * 得到当前所在方法的上层被调用路径
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月18日 上午10:36:26
	 * @param maxLevel 最大层级
	 * @return 类名:方法名集合
	 */
	public static String getFullMethodName(final int maxLevel) {
		return ClassTool.getFullMethodName(maxLevel, null);
	}

	/**
	 * 得到当前所在方法的上层被调用路径，给回的是否之前已经存在相同路径
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月18日 上午11:09:24
	 * @param history 历史路径列表
	 * @param maxLevel 最大层级
	 * @return 类名:方法名集合；如果是null，则认为该路径已经存在
	 */
	public static String getFullMethodName(final Set<String> history, final int maxLevel) {
		return ClassTool.getFullMethodName(maxLevel, null);
	}

	/**
	 * 得到当前所在方法的上层被调用路径
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月18日 上午10:36:25
	 * @param except 如果走到类名的此的地方则跳出
	 * @return 类名:方法名集合
	 */
	public static String getFullMethodName(final String except) {
		return ClassTool.getFullMethodName(18, except);
	}

	/**
	 * 得到当前所在方法的上层被调用路径，给回的是否之前已经存在相同路径
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月18日 上午11:09:26
	 * @param history 历史路径列表
	 * @param except 如果走到类名的此的地方则跳出
	 * @return 类名:方法名集合；如果是null，则认为该路径已经存在
	 */
	public static String getFullMethodName(final Set<String> history, final String except) {
		return ClassTool.getFullMethodName(18, except);
	}

	/**
	 * 得到当前所在方法的上层被调用路径
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月18日 上午10:09:57
	 * @param maxLevel 最大层级
	 * @param except 如果走到类名的此的地方则跳出
	 * @return 类名:方法名集合
	 */
	public static String getFullMethodName(final int maxLevel, final String except) {
		final StackTraceElement[] es = Thread.currentThread().getStackTrace();
		final StringBuilder sb = new StringBuilder((es.length - 2) * 30);
		boolean isFirst = true;
		final boolean hasExcept;
		if ((null == except) || (except.length() == 0)) {
			hasExcept = false;
		} else {
			hasExcept = true;
		}
		for (int i = 2; (i < es.length) && (i < maxLevel); i++) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append('\n').append("\tat ");
			}
			sb.append(es[i]);
			if (hasExcept && es[i].getClassName().startsWith(except)) {
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * 得到当前所在方法的上层被调用路径，给回的是否之前已经存在相同路径
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月18日 上午10:55:41
	 * @param history 历史路径列表
	 * @param maxLevel 最大层级
	 * @param except 如果走到类名的此的地方则跳出
	 * @return 类名:方法名集合；如果是null，则认为该路径已经存在
	 */
	public static String getFullMethodName(final Set<String> history, final int maxLevel, final String except) {
		final String path = ClassTool.getFullMethodName(maxLevel, except);
		if (history.add(path)) {
			return path;
		} else {
			return null;
		}
	}

	/**
	 * 得到当前方法名<br />
	 * 当时第一个看到的方法，感觉思路很有意思，虽然开销确实不小<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年6月21日 下午1:02:31
	 * @return 当前方法名
	 */
	public static String getCurrentMethodName1() {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintWriter pw = new PrintWriter(baos);
		(new Throwable()).printStackTrace(pw);
		pw.flush();
		final String stackTrace = baos.toString();
		pw.close();
		StringTokenizer tok = new StringTokenizer(stackTrace, "\n");
		tok.nextToken(); // 'java.lang.Throwable'
		// System.out.println("1 > " + l);
		tok.nextToken(); // 'at ...getCurrentMethodName'
		// System.out.println("2 > " + l);
		// // tok.nextToken(); // 'at ...<caller to getCurrentRoutine>'
		// System.out.println("3 > " + l);
		// Parse line 3
		tok = new StringTokenizer(tok.nextToken().trim(), " <("); // 'at ...<caller to getCurrentRoutine>'
		tok.nextToken(); // 'at'
		// System.out.println("4 > " + l);
		// // String t = tok.nextToken(); // '...<caller to getCurrentRoutine>'
		// System.out.println("5 > " + t);
		return tok.nextToken(); // '...<caller to getCurrentRoutine>'
	}
}
