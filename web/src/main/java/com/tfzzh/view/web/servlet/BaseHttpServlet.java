package com.tfzzh.view.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import com.tfzzh.tools.ObjectBack;
import com.tfzzh.tools.StringTools;

/**
 * 基础Servlet
 * 
 * @author tfzzh
 * @createDate 2008-10-30 下午02:30:20
 */
public abstract class BaseHttpServlet extends HttpServlet {

	/**
	 * @author tfzzh
	 * @dateTime 2024年6月11日 16:28:44
	 */
	private static final long serialVersionUID = 2967337876636669893L;

	/**
	 * 进行消息返回
	 * 
	 * @author tfzzh
	 * @dateTime 2024年6月7日 12:28:07
	 * @param response 输出对象
	 * @param cont 待输出的文本提示内容
	 */
	protected void bakEmpty(final HttpServletResponse response, final String cont) {
		if (StringTools.isNullOrEmpty(cont)) {
			return;
		}
		try (PrintWriter pw = response.getWriter()) {
			pw.write(cont);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析url
	 * 
	 * @author tfzzh
	 * @dateTime 2024年1月22日 12:01:41
	 * @param oldPath 老path
	 * @param tka 加密token
	 * @param reqLets 请求解码基础子串
	 * @return 新的path 与请求版本号
	 */
	protected ObjectBack<String, Integer> analysisUrl(final String oldPath, final String tka, final String reqLets) {
		// final String reqLets = ProjectConstantsPool.getInstance().getProjCons().REQUEST_DECRYPT_LETTERS;
		String url = oldPath;
		final String l1 = String.valueOf(tka.charAt(tka.length() - 1));
		final int li1 = reqLets.indexOf(l1);
		final String l2 = String.valueOf(tka.charAt(tka.length() - 2));
		final int li2 = reqLets.indexOf(l2);
		final String l3 = String.valueOf(tka.charAt(tka.length() - 3));
		final int li3 = reqLets.indexOf(l3);
		// System.out.println(" analysisUrl oldPath[" + oldPath + "] tka[" + tka + "] ... ");
		// System.out.println(" analysisUrl l1[" + l1 + "][" + li1 + "] l2[" + l2 + "][" + li2 + "] l3[" + l3 + "][" + li3 + "] ... ");
		final int uLen1 = url.length() - 3;
		final int uLen2 = url.length() - 2;
		final int uLen3 = url.length() - 1;
		int ind1 = li1;
		while (ind1 >= uLen1) {
			ind1 -= uLen1;
		}
		int ind2 = ind1 + li2;
		while (ind2 >= uLen2) {
			ind2 -= uLen2;
		}
		int ind3 = ind2 + li3;
		while (ind3 >= uLen3) {
			ind3 -= uLen3;
		}
		final String v3 = String.valueOf(url.charAt(ind3));
		// url = url.substring(0, ind3 - 1) + url.substring(ind3);
		url = url.substring(0, ind3) + url.substring(ind3 + 1);
		final int vi3 = reqLets.indexOf(v3) % 10;
		// System.out.println(" url 3 [" + ind3 + "] -> [" + v3 + "] [" + vi3 + "] [" + url + "] ... ");
		final String v2 = String.valueOf(url.charAt(ind2));
		// url = url.substring(0, ind2 - 1) + url.substring(ind2);
		url = url.substring(0, ind2) + url.substring(ind2 + 1);
		final int vi2 = reqLets.indexOf(v2) % 10;
		// System.out.println(" url 2 [" + ind2 + "] -> [" + v2 + "] [" + vi2 + "] [" + url + "] ... ");
		final String v1 = String.valueOf(url.charAt(ind1));
		// url = url.substring(0, ind1 - 1) + url.substring(ind1);
		url = url.substring(0, ind1) + url.substring(ind1 + 1);
		final int vi1 = reqLets.indexOf(v1) % 10;
		// System.out.println(" url 1 [" + ind1 + "] -> [" + v1 + "] [" + vi1 + "] [" + url + "] ... ");
		final int var = ((vi3 * 100) + (vi2 * 10) + vi1);
		return new ObjectBack<>(url, var);
	}
}
