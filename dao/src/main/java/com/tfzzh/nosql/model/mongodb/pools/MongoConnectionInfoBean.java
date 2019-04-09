/**
 * @author tfzzh
 * @dateTime 2016年11月18日 下午2:56:22
 */
package com.tfzzh.nosql.model.mongodb.pools;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.tfzzh.tools.StringTools;

/**
 * Mongo连接信息
 * 
 * @author tfzzh
 * @dateTime 2016年11月18日 下午2:56:22
 */
public class MongoConnectionInfoBean {

	/**
	 * 连接名
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午2:57:41
	 */
	private final String name;

	/**
	 * 数据库连接信息列表
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午2:57:42
	 */
	private final List<ServerAddress> hostInfo;

	/**
	 * 所相关的数据库名
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午6:03:20
	 */
	private final String dbName;

	/**
	 * 连接用信息
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午3:26:02
	 */
	private final List<MongoCredential> credential;

	/**
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午4:01:38
	 * @param name 连接信息名
	 * @param url 地址
	 * @param dbName 目标库
	 * @param userName 用户名
	 * @param password 密码
	 */
	public MongoConnectionInfoBean(final String name, final String url, final String dbName, final String userName, final String password) {
		this.name = name;
		this.hostInfo = this.initServerAddress(url);
		this.dbName = dbName;
		this.credential = this.initCredential(dbName, userName, password);
	}

	/**
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午4:24:13
	 * @param name 连接信息名
	 * @param url 地址
	 * @param post 端口
	 * @param dbName 目标库
	 * @param userName 用户名
	 * @param password 密码
	 */
	public MongoConnectionInfoBean(final String name, final String url, final int post, final String dbName, final String userName, final String password) {
		this.name = name;
		this.hostInfo = this.initServerAddress(url, post);
		this.dbName = dbName;
		this.credential = this.initCredential(dbName, userName, password);
	}

	/**
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午4:01:17
	 * @param name 连接信息名
	 * @param urlList 地址列表
	 * @param dbName 目标库
	 * @param userName 用户名
	 * @param password 密码
	 */
	public MongoConnectionInfoBean(final String name, final List<String> urlList, final String dbName, final String userName, final String password) {
		this.name = name;
		this.hostInfo = this.initServerAddress(urlList);
		this.dbName = dbName;
		this.credential = this.initCredential(dbName, userName, password);
	}

	/**
	 * 初始化服务器地址列表
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午5:06:23
	 * @param url url，可以支持存在的port，用“:”分割
	 * @return 服务器地址列表
	 */
	private List<ServerAddress> initServerAddress(final String url) {
		final List<ServerAddress> list = new ArrayList<>();
		final ServerAddress sa;
		final List<String> us = StringTools.splitToArray(url, ":");
		switch (us.size()) {
		case 1:
			sa = new ServerAddress(us.get(0));
			break;
		default:
			sa = new ServerAddress(us.get(0), Integer.parseInt(us.get(1)));
			break;
		}
		list.add(sa);
		return list;
	}

	/**
	 * 初始化服务器地址列表
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午5:06:24
	 * @param url 单纯的地址
	 * @param post 请求的端口
	 * @return 服务器地址列表
	 */
	private List<ServerAddress> initServerAddress(final String url, final int post) {
		final List<ServerAddress> list = new ArrayList<>();
		final ServerAddress sa = new ServerAddress(url, post);
		list.add(sa);
		return list;
	}

	/**
	 * 初始化服务器地址列表
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午5:06:26
	 * @param urlList 多个地址，host与post用“:”分割
	 * @return 服务器地址列表
	 */
	private List<ServerAddress> initServerAddress(final List<String> urlList) {
		final List<ServerAddress> list = new ArrayList<>();
		ServerAddress sa;
		List<String> us;
		for (final String url : urlList) {
			us = StringTools.splitToArray(url, ":");
			switch (us.size()) {
			case 0: // 错误的数据
				continue;
			case 1: // 只有url
				sa = new ServerAddress(url);
				break;
			default: // 大于1的情况
				sa = new ServerAddress(us.get(0), Integer.parseInt(us.get(1)));
				break;
			}
			list.add(sa);
		}
		return list;
	}

	/**
	 * 初始化凭证列表
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午5:41:48
	 * @param dbName 目标库
	 * @param userName 用户名
	 * @param password 密码
	 * @return 凭证列表
	 */
	private List<MongoCredential> initCredential(final String dbName, final String userName, final String password) {
		if (null == userName) {
			return null;
		}
		final MongoCredential mc = MongoCredential.createCredential(userName, dbName, password.toCharArray());
		final List<MongoCredential> list = new ArrayList<>();
		list.add(mc);
		return list;
	}

	/**
	 * 得到连接名
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午5:19:33
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 得到数据库连接信息列表
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午5:19:33
	 * @return the hostInfo
	 */
	public List<ServerAddress> getHostInfo() {
		return this.hostInfo;
	}

	/**
	 * 得到所相关的数据库名
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午6:05:14
	 * @return the dbName
	 */
	public String getDbName() {
		return this.dbName;
	}

	/**
	 * 得到连接用信息
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午5:19:33
	 * @return the credential
	 */
	public List<MongoCredential> getCredential() {
		return this.credential;
	}

	/**
	 * 得到连接
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午5:47:51
	 * @return 连接
	 */
	public MongoClient getClient() {
		if (null == this.credential) {
			return new MongoClient(this.hostInfo);
		} else {
			return new MongoClient(this.hostInfo, this.credential);
		}
	}
}
