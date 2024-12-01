/**
 * @author tfzzh
 * @dateTime 2023年11月8日 18:41:02
 */
package com.tfzzh.view.web.iface;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.tfzzh.log.CoreLog;
import com.tfzzh.tools.Constants;
import com.tfzzh.view.web.tools.WebBaseConstants;

/**
 * 请求入口
 * 
 * @author tfzzh
 * @dateTime 2023年11月8日 18:41:02
 */
public abstract class RequestEntry {

	/**
	 * 入口处理方法
	 * 
	 * @author tfzzh
	 * @dateTime 2023年11月8日 19:09:19
	 * @param request 请求数据
	 * @return 返回的对象
	 */
	public abstract RequestEntryResult entry(HttpServletRequest request);

	/**
	 * 执行返回JSON数据
	 * 
	 * @author tfzzh
	 * @dateTime 2023年11月12日 16:43:17
	 * @param back 返回的json数据
	 * @param request 请求数据
	 * @param response 返回数据
	 * @throws IOException 抛
	 */
	public void executeResult(final JSONObject back, final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		response.setContentType("application/json; charset=" + Constants.SYSTEM_CODE);
		response.setCharacterEncoding(Constants.SYSTEM_CODE);
		final PrintWriter pw = response.getWriter();
		final String bv = JSON.toJSONString(back);
		if (CoreLog.getInstance().debugEnabled(this.getClass())) {
			CoreLog.getInstance().debug(this.getClass(), new StringBuilder().append("Back Json Data >> ").append(bv.length() > WebBaseConstants.BACK_MAX_LENGTH ? bv.substring(0, WebBaseConstants.BACK_MAX_LENGTH) : bv).toString());
		}
		pw.write(bv);
		pw.close();
	}

	/**
	 * 执行返回字串数据
	 * 
	 * @author tfzzh
	 * @dateTime 2023年11月12日 16:43:16
	 * @param back 返回的字串数据
	 * @param request 请求数据
	 * @param response 返回数据
	 * @throws IOException 抛
	 */
	public void executeResult(final String back, final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=" + Constants.SYSTEM_CODE);
		response.setCharacterEncoding(Constants.SYSTEM_CODE);
		final PrintWriter pw = response.getWriter();
		if (CoreLog.getInstance().debugEnabled(this.getClass())) {
			CoreLog.getInstance().debug(this.getClass(), new StringBuilder().append("Back String Data >> ").append(back == null ? "null" : (back.length() > WebBaseConstants.BACK_MAX_LENGTH ? back.substring(0, WebBaseConstants.BACK_MAX_LENGTH) : back)).toString());
		}
		pw.write(back);
		pw.close();
	}
	// public void executeResult(final BackOutsideBean back, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
	// final StringBuilder sb = new StringBuilder(back.getTarget());
	// final Map<String, ? extends Object> param = back.getAttributes();
	// if (CoreLog.getInstance().debugEnabled(this.getClass())) {
	// final Object oj = RunThreadLocal.getInstance().getObject(WebBaseConstants.TL_KEY);
	// CoreLog.getInstance().debug(this.getClass(), "executeResult .. [", null == oj ? "null" : oj.getClass().getName(), ":", null == oj ? "null" : oj.toString(), "]");
	// }
	// final InnerIndex ii = new InnerIndex(sb.indexOf("?"));
	// final int bs = sb.indexOf("#");
	// final String bf;
	// if (bs != -1) {
	// bf = sb.substring(bs);
	// sb.delete(bs, sb.length());
	// } else {
	// bf = null;
	// }
	// // 组合参数
	// if (null != param) {
	// for (final Entry<String, ? extends Object> ent : param.entrySet()) {
	// if (null == ent.getValue()) {
	// continue;
	// }
	// this.assembleParam(sb, ii, ent);
	// }
	// }
	// if (null != bf) {
	// sb.append(bf);
	// }
	// if (CoreLog.getInstance().debugEnabled(this.getClass())) {
	// CoreLog.getInstance().debug(this.getClass(), new StringBuilder().append("Back To Outside >> ").append(sb.toString()).toString());
	// }
	// response.sendRedirect(sb.toString());
	// }
	// private void assembleParam(final StringBuilder sb, final InnerIndex ii, final Entry<String, ? extends Object> ent) throws UnsupportedEncodingException {
	// this.assembleParam(sb, ii, ent.getKey(), ent.getValue());
	// }
	// private void assembleParam(final StringBuilder sb, final InnerIndex ii, final String key, final Object val) throws UnsupportedEncodingException {
	// if (ii.add() > 0) {
	// sb.append('&');
	// } else {
	// sb.append('?');
	// }
	// sb.append(key).append('=');
	// if (val instanceof String) {
	// // 字符串才转
	// try {
	// sb.append(URLEncoder.encode(URLEncoder.encode(val.toString(), Constants.SYSTEM_CODE), Constants.SYSTEM_CODE));
	// } catch (final Exception e) {
	// // 某些系统在获取系统编码时，可能存在一些问题，具体还未深入去看，先做个简单处理
	// sb.append(URLEncoder.encode(URLEncoder.encode(val.toString(), "UTF-8"), "UTF-8"));
	// }
	// } else {
	// sb.append(val);
	// }
	// }
}
