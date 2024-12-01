package com.tfzzh.view.web.servlet;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServlet;

import com.tfzzh.core.control.tools.NewManagerMapTool;
import com.tfzzh.log.CoreLog;
import com.tfzzh.tools.Constants;
import com.tfzzh.tools.InstanceFactory;
import com.tfzzh.view.web.annotation.SingletonHideField;
import com.tfzzh.view.web.purview.AccessPermissionsControl;
import com.tfzzh.view.web.tools.InfoControl;
import com.tfzzh.view.web.tools.LinkPageAnnotationAnalysis;

/**
 * 初始化系统消息<br />
 * 因为古老，不再定义为初始化必要项目 2024-06-18<br />
 * 
 * @author TFZZH
 * @dateTime 2011-2-15 上午11:35:10
 */
// @WebServlet(loadOnStartup = 1)
public class InitServlet extends HttpServlet {

	/**
	 * @author Xu Weijie
	 * @dateTime 2012-12-8 下午8:56:01
	 */
	private static final long serialVersionUID = -2924042540797620498L;

	/**
	 * 初始化数据
	 * 
	 * @author TFZZH
	 * @dateTime 2011-2-15 上午11:34:58
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() {
		System.out.println(" in InitServlet init ... ");
		this.handle();
	}

	/**
	 * 进行数据加载控制
	 * 
	 * @author TFZZH
	 * @dateTime 2011-2-15 上午11:34:55
	 */
	private void handle() {
		long l1 = 0, l2 = 0;
		if (CoreLog.getInstance().debugEnabled(this.getClass())) {
			l1 = System.currentTimeMillis();
		}
		// 加载存在先后顺序
		String basePath;
		if ((basePath = this.getInitParameter("basePath")) == null) {
			basePath = Constants.INIT_CONFIG_PATH_BASE;
		}
		final NewManagerMapTool nmmt = new NewManagerMapTool();
		if (null != this.getInitParameter("manager")) {
			// 1初始化控制层实体对象控制
			// ManagerMap.getInstance().readerAmountXML(basePath, Constants.INIT_MANAGER_DTD, this.getInitParameter("manager"));
			nmmt.readerAmountXML(basePath, Constants.INIT_MANAGER_DTD, this.getInitParameter("manager"));
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), "ManagerMap over. run: ", Long.toString((l2 = System.currentTimeMillis()) - l1));
			}
		}
		if (null != this.getInitParameter("managerPrefix")) {
			// 是路径下文件的情况
			nmmt.readerPrefixAmountXML(basePath, Constants.INIT_MANAGER_DTD, this.getInitParameter("managerPrefix"));
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), "ManagerMap over. run: ", Long.toString((l2 = System.currentTimeMillis()) - l1));
			}
		}
		// { // 暂时不支持对XML文件的读取
		// if ((null != this.getInitParameter("linkpage")) && (null == this.getInitParameter("linkpage"))) {
		// // 2初始化页面对象控制
		// new LinkPageXmlAnalysis().readerAmountXML(basePath, WebConstants.INIT_LINK_PAGE_DTD,
		// this.getInitParameter("linkpage"));
		// if (this.log.isDebugEnabled()) {
		// this.log.debug("LinkPageMap over. run: " + ((l1 = System.currentTimeMillis()) - l2));
		// }
		// }
		// }
		if (null != this.getInitParameter("linkAnnotation")) {
			// 2初始化页面对象控制
			new LinkPageAnnotationAnalysis(nmmt).readerAnnotationPath(this.getInitParameter("linkAnnotation"));
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), "LinkPageAnnotation over. run: ", Long.toString((l2 = System.currentTimeMillis()) - l1));
			}
		}
		{
			// 2初始化页面对象控制
			// 初始化信息控制
			final InfoControl info = InfoControl.getInstantce();
			SingletonHideField shf;
			String pName;
			// 检查属性字段
			for (final Field f : info.getClass().getDeclaredFields()) {
				if (null != (shf = f.getAnnotation(SingletonHideField.class))) {
					// 存在目标注解
					pName = shf.value();
					if (null != this.getInitParameter(pName)) {
						f.setAccessible(true);
						try {
							f.set(info, InstanceFactory.classInstance(this.getInitParameter(pName)));
						} catch (final Exception e) {
							e.printStackTrace();
						}
						f.setAccessible(false);
					}
				}
			}
			final AccessPermissionsControl upc = AccessPermissionsControl.getInstance();
			for (final Field f : upc.getClass().getDeclaredFields()) {
				if (null != (shf = f.getAnnotation(SingletonHideField.class))) {
					// 存在目标注解
					pName = shf.value();
					if (null != this.getInitParameter(pName)) {
						f.setAccessible(true);
						try {
							f.set(upc, InstanceFactory.classInstance(this.getInitParameter(pName)));
						} catch (final Exception e) {
							e.printStackTrace();
						}
						f.setAccessible(false);
					}
				}
			}
			// 初始化数据
			upc.initData();
			if (CoreLog.getInstance().debugEnabled(this.getClass())) {
				CoreLog.getInstance().debug(this.getClass(), "Singleton over. run: ", Long.toString((l1 = System.currentTimeMillis()) - l2));
			}
		}
	}
}
