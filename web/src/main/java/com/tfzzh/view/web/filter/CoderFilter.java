package com.tfzzh.view.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfzzh.tools.Constants;

/**
 * 字符编码过滤器
 */
@WebFilter(value = "/*", initParams = { @WebInitParam(name = "code", value = "UTF-8") })
public class CoderFilter implements Filter {

	/**
	 * @author TFZZH
	 * @dateTime 2011-2-15 下午05:01:59
	 */
	private String coder;

	/**
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy() {
		// 无失效时的操作
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		// 进行编码设置
		((HttpServletRequest) request).setCharacterEncoding(this.coder);
		((HttpServletResponse) response).setCharacterEncoding(this.coder);
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(final FilterConfig fConfig) throws ServletException {
		// 得到配置所设置的编码
		final String code = fConfig.getInitParameter("code");
		if (code == null) {
			// 因为不存在，记录为系统默认编码
			this.coder = Constants.SYSTEM_CODE;
		} else {
			// 设置的编码
			this.coder = code;
		}
	}
}
