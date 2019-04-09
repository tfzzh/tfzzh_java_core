/**
 * @author Weijie Xu
 * @dateTime 2015年2月7日 上午9:54:26
 */
package com.tfzzh.socket.netty.coder;

import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tfzzh.socket.RequestSession;
import com.tfzzh.socket.bean.RequestInfoBean;
import com.tfzzh.socket.netty.tools.RequestNettySession;
import com.tfzzh.socket.tools.RequestPool;
import com.tfzzh.socket.tools.ValidateControl;
import com.tfzzh.socket.tools.VerificationTypeEnum;
import com.tfzzh.socket.webservice.DefaultHandshake;
import com.tfzzh.socket.webservice.FlashHandshake;
import com.tfzzh.socket.webservice.Handshake;
import com.tfzzh.socket.webservice.WebSocketHandshake;
import com.tfzzh.tools.Constants;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 针对netty的基础解码适配器
 * 
 * @author Weijie Xu
 * @dateTime 2015年2月7日 上午9:54:26
 */
public class BaseNettyDecoderAdapter extends ChannelInboundHandlerAdapter {

	/**
	 * @author Weijie Xu
	 * @dateTime 2014年12月5日 下午8:55:47
	 */
	protected final Logger log = LogManager.getLogger(this.getClass());

	/**
	 * 所在空间名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月31日 下午7:10:10
	 */
	protected final RequestPool spaceRequestPool;

	/**
	 * 验证控制器
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月26日_下午12:10:28
	 */
	protected final ValidateControl validateControl;

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 上午9:55:46
	 * @param spaceRequestPool 所在空间名
	 * @param validateControl 验证控制器
	 */
	public BaseNettyDecoderAdapter(final RequestPool spaceRequestPool, final ValidateControl validateControl) {
		this.spaceRequestPool = spaceRequestPool;
		this.validateControl = validateControl;
	}

	/**
	 * 进行安全验证
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月27日 下午5:26:43
	 * @param rs 客户端会话
	 * @param msg 请求来的消息
	 * @return 状态值
	 */
	protected int isSecurityRequest(final RequestNettySession rs, final ByteBuf msg) {
		// 得到Session安全验证情况
		if (rs.isPass()) {
			// 已经通过了
			return 0;
		}
		// 进行消息分析
		final String request;
		Handshake hs;
		final ByteBuf cb = msg.copy();
		if (null != (request = this.getRequestInfo(cb))) {
			// 进行前位比较
			if (null != (hs = this.validateFlash(request))) {
				// 是falsh的安全信息头，需pass掉当前内容
				rs.setHandshakeInfo(hs);
				// final int i = msg.readableBytes();
				// msg.skipBytes(i);
				msg.release();
				msg.clear();
				return 2;
			} else if (null != (hs = this.validateWebsocket(request))) {
				// 是websocket的消息头，需pass掉当前内容
				rs.setHandshakeInfo(hs);
				// final int i = msg.readableBytes();
				// msg.skipBytes(i);
				msg.release();
				msg.clear();
				return 2;
			} else {
				hs = new DefaultHandshake();
				// 正常的通过，一般的客户端
				rs.setHandshakeInfo(hs);
				// 因为该方法中存在对一个属性的移除操作
				rs.getHandshakeBackMsg();
				// 认为是已经通过了的
				return 0;
			}
		} else {
			// 一定错误
			return 1;
		}
	}

	/**
	 * 得到请求信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月27日 下午5:21:43
	 * @param bb 字节流信息
	 * @return 相关的字符串内容
	 */
	private String getRequestInfo(final ByteBuf bb) {
		// 指定长度字节
		final byte[] bytes = new byte[bb.readableBytes()];
		// 内容读取
		bb.getBytes(bb.readerIndex(), bytes);
		try {
			// 按系统默认编码进行转换
			return new String(bytes, Constants.SYSTEM_CODE);
		} catch (final UnsupportedEncodingException e) {
			return null;
		} finally {
			bb.release();
			bb.clear();
		}
	}

	/**
	 * 是否为flash安全验证信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 上午11:22:17
	 * @param txt 待验证文本内容
	 * @return true，是flash的；<br />
	 *         false，不是flash的；<br />
	 */
	private Handshake validateFlash(final String txt) {
		return FlashHandshake.toHandshake(txt);
	}

	/**
	 * 是否为websocket安全验证信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年2月7日 下午5:38:09
	 * @param txt 待验证文本内容
	 * @return true，是flash的；<br />
	 *         false，不是flash的；<br />
	 */
	private Handshake validateWebsocket(final String txt) {
		return WebSocketHandshake.toHandshake(txt);
	}

	/**
	 * 在解析param前验证消息
	 * 
	 * @author XuWeijie
	 * @datetime 2015年12月26日_下午12:12:10
	 * @param rs 请求的会话
	 * @param requestInfo 被请求的消息信息
	 * @return 验证的结果
	 */
	protected VerificationTypeEnum validateBeforeParams(final RequestSession rs, final RequestInfoBean requestInfo) {
		if (null == this.validateControl) {
			return VerificationTypeEnum.Pass;
		}
		return this.validateControl.validateBeforeParams(rs, requestInfo);
	}

	/**
	 * 得到int值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年1月26日 下午4:45:26
	 * @param bs 字节组
	 * @param startIndex 读取起始索引位
	 * @return 得到的int值
	 */
	protected int readInt(final byte[] bs, final int startIndex) {
		return (bs[startIndex + 3] & 0xff) | ((bs[startIndex + 2] & 0xff) << 8) | ((bs[startIndex + 1] & 0xff) << 16) | ((bs[startIndex] & 0xff) << 24);
	}
}
