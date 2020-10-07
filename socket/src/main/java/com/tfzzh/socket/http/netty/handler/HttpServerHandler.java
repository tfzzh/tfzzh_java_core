/**
 * @author 许纬杰
 * @datetime 2016年5月7日_下午4:07:16
 */
package com.tfzzh.socket.http.netty.handler;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tfzzh.socket.RequestSession;
import com.tfzzh.socket.http.netty.tools.InputHttpMessageBean;
import com.tfzzh.socket.netty.tools.ChannelPool;
import com.tfzzh.socket.netty.tools.RequestNettySession;
import com.tfzzh.socket.tools.RequestPool;
import com.tfzzh.socket.tools.SessionControl;
import com.tfzzh.tools.StringTools;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;

/**
 * Http消息处理器
 * 
 * @author 许纬杰
 * @datetime 2016年5月7日_下午4:07:16
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

	/**
	 * @author 许纬杰
	 * @datetime 2016年4月27日_上午9:37:53
	 */
	private final Logger log = LogManager.getLogger(this.getClass());

	/**
	 * 请求消息
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月9日_上午10:37:14
	 */
	private HttpRequest request;

	/**
	 * 暂不明其意
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月9日_上午10:37:14
	 */
	private boolean readingChunks;

	/**
	 * @author 许纬杰
	 * @datetime 2016年5月9日_上午10:37:15
	 */
	private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); // Disk if size exceed

	/**
	 * post消息解码
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月9日_上午10:37:16
	 */
	private HttpPostRequestDecoder decoder;

	/**
	 * 相关URI
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月7日_上午11:13:33
	 */
	private String uri = null;

	/**
	 * 请求的消息码
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月7日_上午11:13:34
	 */
	private int code;

	// /**
	// * 参数集合
	// *
	// * @author 许纬杰
	// * @datetime 2016年5月7日_上午11:13:35
	// */
	// private final Map<String, String> params = new HashMap<>();
	/**
	 * Session控制
	 * 
	 * @author 许纬杰
	 * @datetime 2016年4月28日_下午4:09:04
	 */
	private final SessionControl sc;

	/**
	 * 所在空间名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年10月31日 下午7:10:10
	 */
	private final RequestPool spaceRequestPool;
	// 这里暂时不明
	// static {
	// //
	// DiskFileUpload.deleteOnExitTemporaryFile = true; // should delete file
	// // on exit (in normal
	// // exit)
	// DiskFileUpload.baseDirectory = null; // system temp directory
	// DiskAttribute.deleteOnExitTemporaryFile = true; // should delete file on
	// // exit (in normal exit)
	// DiskAttribute.baseDirectory = null; // system temp directory
	// }

	/**
	 * @author 许纬杰
	 * @datetime 2016年5月7日_下午4:30:39
	 * @param sc Session控制
	 * @param spaceRequestPool 所在空间名
	 */
	public HttpServerHandler(final SessionControl sc, final RequestPool spaceRequestPool) {
		this.sc = sc;
		this.spaceRequestPool = spaceRequestPool;
	}

	/**
	 * 频道的注册
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月9日_上午10:39:49
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRegistered(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
		final RequestSession rs = ChannelPool.getInstance().putChannel(ctx.channel());
		ctx.fireChannelRegistered();
		if (null != this.sc) {
			this.sc.open(rs);
		}
	}

	/**
	 * 频道的注销
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月9日_上午10:40:23
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelUnregistered(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
		if (null != this.sc) {
			final RequestNettySession rs = ChannelPool.getInstance().getChannelSession(ctx.channel());
			this.sc.closed(rs);
		}
		ChannelPool.getInstance().clearChannelSession(ctx.channel());
		if (this.decoder != null) {
			this.decoder.cleanFiles();
		}
	}

	/**
	 * @author 许纬杰
	 * @datetime 2016年5月9日_上午10:40:45
	 * @see io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead0(final ChannelHandlerContext ctx, final HttpObject msg) throws Exception {
		final long startTime = System.currentTimeMillis();
		final Map<String, String> params = new HashMap<>();
		if (msg instanceof HttpRequest) {
			final HttpRequest request = this.request = (HttpRequest) msg;
			new URI(request.uri());
			params.clear();
			this.uri = this.request.uri();
			final int end = this.uri.indexOf('?');
			String codeStr;
			if (end == -1) {
				codeStr = this.uri.substring(1, this.uri.length());
			} else {
				final String paramStr = this.uri.substring(end + 1);
				codeStr = this.uri.substring(1, end);
				this.putContentToParams(params, paramStr);
			}
			if (codeStr.length() == 0) {
				// 无实际消息
				return;
			}
			// 进行路径分析
			final List<String> pps = StringTools.splitToArray(codeStr, "/");
			codeStr = pps.get(0);
			// 注意这里的位置关系
			int eInd;
			String pp;
			for (int i = pps.size() - 1; i > 0; i--) {
				pp = pps.get(i);
				eInd = pp.indexOf('_');
				if ((eInd != -1) && (eInd < (pp.length() - 1))) {
					params.put(pp.substring(0, eInd), pp.substring(eInd + 1));
				}
			}
			try {
				this.code = Integer.parseInt(codeStr);
			} catch (final Exception e) {
				return;
			}
			if (request.method().equals(HttpMethod.GET)) {
				return;
			}
			try {
				this.decoder = new HttpPostRequestDecoder(HttpServerHandler.factory, request);
			} catch (final ErrorDataDecoderException e1) {
				e1.printStackTrace();
				this.exec(ctx.channel(), this.code, params, startTime);
				ctx.channel().close();
				return;
			}
			this.readingChunks = HttpUtil.isTransferEncodingChunked(request);
			if (this.readingChunks) {
				// Chunk version
				this.readingChunks = true;
			}
		}
		// check if the decoder was constructed before
		// if not it handles the form get
		if (this.decoder != null) {
			if (msg instanceof HttpContent) {
				// New chunk is received
				final HttpContent chunk = (HttpContent) msg;
				try {
					this.decoder.offer(chunk);
				} catch (final ErrorDataDecoderException e1) {
					e1.printStackTrace();
					// writeResponse(ctx.channel());
					this.exec(ctx.channel(), this.code, params, startTime);
					ctx.channel().close();
					return;
				}
				// example of reading chunk by chunk (minimize memory usage due to
				// Factory)
				this.readHttpDataChunkByChunk(params);
				// example of reading only if at the end
				if (chunk instanceof LastHttpContent) {
					// writeResponse(ctx.channel());
					this.exec(ctx.channel(), this.code, params, startTime);
					this.readingChunks = false;
					this.reset();
				}
			}
		} else {
			// writeResponse(ctx.channel());
			this.exec(ctx.channel(), this.code, params, startTime);
		}
	}

	/**
	 * 重置一些消息，这里可能未完全
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月9日_上午10:41:46
	 */
	private void reset() {
		this.request = null;
		// destroy the decoder to release all resources
		this.decoder.destroy();
		this.decoder = null;
	}

	/**
	 * Example of reading request by chunk and getting values from chunk to chunk
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月9日_上午10:42:12
	 * @param params 参数集合
	 */
	private void readHttpDataChunkByChunk(final Map<String, String> params) {
		try {
			while (this.decoder.hasNext()) {
				final InterfaceHttpData data = this.decoder.next();
				if (data != null) {
					try {
						// new value
						this.writeHttpData(data, params);
					} finally {
						data.release();
					}
				}
			}
		} catch (final EndOfDataDecoderException e1) {
			// end
		}
	}

	/**
	 * 读取数据内容
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月9日_上午10:42:27
	 * @param data 目标文具
	 * @param params 参数集合
	 */
	private void writeHttpData(final InterfaceHttpData data, final Map<String, String> params) {
		if (data.getHttpDataType() == HttpDataType.Attribute) {
			final Attribute attribute = (Attribute) data;
			try {
				params.put(attribute.getName(), attribute.getValue());
			} catch (final IOException e1) {
				// Error while reading data from File, only print name and error
				e1.printStackTrace();
				return;
			}
		} else {
		}
	}

	/**
	 * 执行具体的操作，并结束当前流程
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月9日_上午10:42:49
	 * @param cha 对应频道
	 * @param code 消息码
	 * @param params 参数集合
	 * @param startTime 开始时间
	 * @throws Exception 异常的抛
	 */
	private void exec(final Channel cha, final int code, final Map<String, String> params, final long startTime) throws Exception {
		final InputHttpMessageBean imb = new InputHttpMessageBean(this.spaceRequestPool, code, params, startTime);
		final RequestNettySession rs = ChannelPool.getInstance().getChannelSession(cha);
		// System.out.println("Uri:" + this.uri + " >> " + code + " >> " + params);
		imb.transpondExec(rs);
		if (this.log.isDebugEnabled()) {
			if (!imb.getRequestInfo().isKeep()) {
				this.log.debug(new StringBuilder(100).append("http msg use[").append(imb.getRunningTime()).append("] [").append(cha.localAddress()).append("] Receive [").append(cha.remoteAddress()).append("] ").append(imb.showLinkInfo()).append(" ok msg(").append(") >> ").append(imb.getMsg()));
			}
		}
		// System.out.println("cod [" + code + "] is Over...");
	}

	/**
	 * 将内容放入到参数消息中
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月3日_下午6:36:30
	 * @param params 参数消息
	 * @param cont 内容
	 */
	private void putContentToParams(final Map<String, String> params, final String cont) {
		final List<String> ls = StringTools.splitToArray(cont, "&");
		List<String> lp;
		for (final String s : ls) {
			lp = StringTools.splitToArray(s, "=");
			switch (lp.size()) {
			case 0:
				continue;
			case 1:
				params.put(lp.get(0), null);
				continue;
			case 2:
			default:
				params.put(lp.get(0), lp.get(1));
				continue;
			}
		}
	}

	/**
	 * 消息读取完成
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月9日_上午10:43:22
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelReadComplete(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	/**
	 * 异常处理
	 * 
	 * @author 许纬杰
	 * @datetime 2016年5月9日_上午10:43:24
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#exceptionCaught(io.netty.channel.ChannelHandlerContext, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
		this.log.error(cause.getMessage());
		ctx.channel().close();
	}
}
