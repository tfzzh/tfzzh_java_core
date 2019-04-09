/**
 * @author Weijie Xu
 * @dateTime 2014-3-19 上午11:29:24
 */
package com.tfzzh.socket.netty.tools;

import java.io.UnsupportedEncodingException;
import java.nio.channels.ClosedChannelException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tfzzh.exception.InitializeException;
import com.tfzzh.socket.RequestSession;
import com.tfzzh.socket.action.OutputMessageBean;
import com.tfzzh.socket.bean.OutputSocketMessageBean;
import com.tfzzh.socket.http.netty.bean.OutputHttpMessageBean;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * 基于Netty请求来的会话
 * 
 * @author Weijie Xu
 * @dateTime 2014-3-19 上午11:29:24
 */
public class RequestNettySession extends RequestSession {

	/**
	 * @author 许纬杰
	 * @datetime 2016年7月19日_下午5:47:09
	 */
	private static final long serialVersionUID = 4619605690470629482L;

	/**
	 * 日志消息
	 * 
	 * @author 许纬杰
	 * @datetime 2016年7月19日_下午5:43:50
	 */
	private final static Logger log = LogManager.getLogger(RequestNettySession.class);

	/**
	 * 相关的频道信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-21 下午7:07:21
	 */
	private final Channel channel;

	/**
	 * 会话IP
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月10日 下午6:41:45
	 */
	private final String ip;

	/**
	 * 会话端口
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月10日 下午6:41:46
	 */
	private final int port;

	/**
	 * hashCode码<br />
	 * 此为缓存，因为所有东西为固定不可变的<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月27日 下午3:29:21
	 */
	private final int hashCode;

	/**
	 * 字节流
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午7:03:16
	 */
	private ByteBuf bb = null;

	/**
	 * @author Weijie Xu
	 * @dateTime 2014-3-19 下午12:55:45
	 * @param channel 相关的频道
	 */
	public RequestNettySession(final Channel channel) {
		this.channel = channel;
		try {
			final String addr = this.channel.remoteAddress().toString();
			final Pattern p = Pattern.compile("/([\\d.]*):([\\d]*)");
			final Matcher m = p.matcher(addr);
			m.matches();
			this.ip = m.group(1);
			this.port = Integer.parseInt(m.group(2));
		} catch (final Exception e) {
			throw new InitializeException("Unknow Channel han't Remote Info!!");
		}
		// 得到实际的hashCode
		this.hashCode = super.hashCode();
	}

	/**
	 * 得到相关的频道信息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-21 下午7:08:12
	 * @return 相关频道
	 */
	public Channel getChannel() {
		return this.channel;
	}

	/**
	 * 放入字节流
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午7:04:48
	 * @param bb 字节流
	 * @return 新的字节流
	 */
	public ByteBuf putByteBuf(final ByteBuf bb) {
		if (null == this.bb) {
			this.bb = bb.copy();
			bb.release();
			bb.clear();
		} else {
			if (this.bb.readableBytes() == 0) {
				// 之前的已经被清空，则放入到新的
				// this.bb.release();
				this.bb.release();
				this.bb = bb.copy();
				bb.release();
				bb.clear();
			} else {
				this.bb.writeBytes(bb);
				// bb.release();
				bb.release();
				bb.clear();
			}
		}
		return this.bb;
	}

	/**
	 * 清理字节流
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-20 下午7:05:44
	 */
	public void clearByteBuf() {
		this.bb.clear();
	}

	/**
	 * 向目标发送消息，及时的
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月4日 下午7:43:31
	 * @param msg 准备发送的消息
	 * @see com.tfzzh.socket.RequestSession#sendMessage(com.tfzzh.socket.action.OutputMessageBean)
	 */
	@Override
	public boolean sendMessage(final OutputMessageBean msg) {
		if (!this.channel.isActive()) {
			return false;
		}
		if (msg instanceof OutputSocketMessageBean) {
			this.channel.writeAndFlush(msg);
		} else if (msg instanceof OutputHttpMessageBean) {
			final OutputHttpMessageBean ohm = (OutputHttpMessageBean) msg;
			final StringBuilder sb = new StringBuilder();
			ohm.getMsg().writeContent(sb);
			try {
				final FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(sb.toString().getBytes("UTF-8")));
				response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/json");
				response.headers().set(HttpHeaderNames.ACCEPT_CHARSET, "UTF-8");
				response.headers().set(HttpHeaderNames.ACCEPT_ENCODING, "UTF-8");
				response.headers().set(HttpHeaderNames.CONTENT_ENCODING, "UTF-8");
				// 直接允许跨域
				response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
				response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
				this.channel.write(response);
			} catch (final UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (RequestNettySession.log.isDebugEnabled()) {
				RequestNettySession.log.debug(" Back Http MsgBean [" + this.channel.remoteAddress().toString() + "] <" + ohm.getCode() + "> msg(" + sb.length() + ") >> " + (sb.length() > 500 ? sb.substring(0, 500) : sb.toString()));
			}
			this.channel.flush();
			// 关闭
			this.channel.close();
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 向目标发送消息，会在一个小时段内被集合为一个队列后，然后统一在一起发送<br />
	 * 优化时效性不高的传输时用<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年11月19日 下午2:55:24
	 * @param msg 准备发送的消息
	 * @see com.tfzzh.socket.RequestSession#sendMessageForTask(com.tfzzh.socket.action.OutputMessageBean)
	 */
	@Override
	public boolean sendMessageForTask(final OutputMessageBean msg) {
		final ChannelFuture cf = this.channel.write(msg);
		final Throwable ta = cf.cause();
		if (null != ta) {
			if (ta instanceof ClosedChannelException) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 进行关闭，以及进行一些清理操作<br />
	 * 清理掉所有属性<br />
	 * 正常情况不会调用<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014-3-19 下午1:27:25
	 */
	@Override
	public void close() {
		if (null != this.bb) {
			this.bb.release();
			this.bb.clear();
			this.bb = null;
		}
		if (null != this.channel) {
			this.channel.close();
		}
		super.close();
	}

	/**
	 * 得到会话IP
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月10日 下午6:42:49
	 * @see com.tfzzh.socket.RequestSession#getIp()
	 */
	@Override
	public String getIp() {
		return this.ip;
	}

	/**
	 * 得到会话端口
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年4月10日 下午6:42:49
	 * @see com.tfzzh.socket.RequestSession#getPort()
	 */
	@Override
	public int getPort() {
		return this.port;
	}

	/**
	 * 你懂的，不解释
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月27日 下午2:43:26
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.hashCode;
	}
}
