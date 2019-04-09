package com.tfzzh.socket.webservice;

/**
 * 4位操作码，定义有效负载数据，如果收到了一个未知的操作码，连接也必须断掉，以下是定义的操作码：<br />
 * %x0 表示连续消息片断<br />
 * %x1 表示文本消息片断<br />
 * %x2 表未二进制消息片断<br />
 * %x3-7 为将来的非控制消息片断保留的操作码<br />
 * %x8 表示连接关闭<br />
 * %x9 表示心跳检查的ping<br />
 * %xA 表示心跳检查的pong<br />
 * %xB-F 为将来的控制消息片断的保留操作码<br />
 */
public class WebsocketOpCode {

	/**
	 * 表示为连续消息中片段
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月8日_上午11:32:29
	 */
	public static final byte Fragment = 0;

	/**
	 * 表示文本消息片断
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年1月28日 下午12:01:46
	 */
	public static final byte Text = 1;

	/**
	 * 表未二进制消息片断
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年1月28日 下午12:01:47
	 */
	public static final byte Binary = 2;

	/**
	 * 表示连接关闭
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年1月28日 下午12:01:48
	 */
	public static final byte Close = 8;

	/**
	 * 表示心跳检查的ping
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年1月28日 下午12:01:49
	 */
	public static final byte Ping = 9;

	/**
	 * 表示心跳检查的pong
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年1月28日 下午12:01:45
	 */
	public static final byte Pong = 10;
}
