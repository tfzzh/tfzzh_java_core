/**
 * @author Xu Weijie
 * @dateTime 2012-9-5 下午9:33:50
 */
package com.tfzzh.view.web.link;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfzzh.view.web.link.OperateLink.OperateLinkInfo;
import com.tfzzh.view.web.tools.SuffixTypeEnum;

/**
 * 进行图片内容的读取
 * 
 * @author XuWeijie
 * @datetime 2015年9月6日_下午5:26:43
 */
public class BackImgBean extends BaseBackOperationBean {

	/**
	 * 图片流
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:28:30
	 */
	private final BufferedImage bi;

	/**
	 * 文件后缀名
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:32:21
	 */
	private final SuffixTypeEnum imgType;

	/**
	 * 进入到默认的目标<br />
	 * 默认目标值：“t”<br />
	 * 默认可跨域<br />
	 * 
	 * @author Xu Weijie
	 * @datetime 2015年9月6日_下午5:26:43
	 * @param bi 图片流
	 * @param suf 文件后缀名
	 */
	public BackImgBean(final BufferedImage bi, final String suf) {
		super("t", null);
		this.bi = bi;
		this.imgType = SuffixTypeEnum.getType(suf);
	}

	/**
	 * 连接到
	 * 
	 * @author Xu Weijie
	 * @datetime 2017年8月25日_上午10:00:10
	 * @param link 控制连接
	 * @param request 请求数据
	 * @param response 返回数据
	 * @throws IOException 抛
	 * @throws ServletException 抛
	 * @see com.tfzzh.view.web.link.BaseBackOperationBean#linkTo(com.tfzzh.view.web.link.OperateLink.OperateLinkInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void linkTo(final OperateLinkInfo link, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		link.executeResult(this, request, response);
	}

	/**
	 * 得到图片流
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:29:06
	 * @return the bi
	 */
	public BufferedImage getImg() {
		return this.bi;
	}

	/**
	 * 得到文件后缀名
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:33:07
	 * @return the suf
	 */
	public SuffixTypeEnum getImgType() {
		return this.imgType;
	}
}
