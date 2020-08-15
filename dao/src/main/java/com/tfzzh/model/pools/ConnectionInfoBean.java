/**
 * @author Weijie Xu
 * @dateTime 2015年4月21日 下午5:03:21
 */
package com.tfzzh.model.pools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.tfzzh.model.tools.DaoConstants;

/**
 * 连接信息
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月21日 下午5:03:21
 */
public class ConnectionInfoBean {

	/**
	 * 连接的名字
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午8:56:35
	 */
	private final String name;

	/**
	 * 连接用驱动
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:25:12
	 */
	private final String driver;

	/**
	 * 连接用地址
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:25:10
	 */
	private final String url;

	/**
	 * 连接用帐号名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:25:10
	 */
	private final String user;

	/**
	 * 连接用密码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:25:10
	 */
	private final String pass;

	/**
	 * 连接的说明
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:25:09
	 */
	private final String description;

	/**
	 * 是否使用unicode格式
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:25:09
	 */
	private final boolean useUnicode;

	/**
	 * 连接用字符编码类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:25:08
	 */
	private final String characterEncoding;

	/**
	 * 是否只读操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:25:07
	 */
	private final boolean readOnly;

	/**
	 * 最大超时时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:25:06
	 */
	private final long timeOut;

	/**
	 * 连接后空闲超时时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:25:05
	 */
	private final long idelTime;

	/**
	 * 超时安全时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:25:05
	 */
	private final long safetyOut;

	/**
	 * 最小连接数
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:25:04
	 */
	private final int min;

	/**
	 * 最大连接数
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:25:03
	 */
	private final int max;

	/**
	 * 其他属性对应键值对
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:25:03
	 */
	private final Map<String, String> others;

	/**
	 * 被实际创建连接对象的索引值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午8:35:42
	 */
	private int ind = 0;

	/**
	 * 是否运行中
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 下午8:31:23
	 */
	private boolean isRun = false;

	/**
	 * 空闲的连接列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:25:02
	 */
	private final List<ConnectionBean> freeCons;

	/**
	 * 被使用的连接列表
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:25:01
	 */
	private final List<ConnectionBean> useCons;

	/**
	 * 所存在于的连接池 changeable
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午4:35:15
	 */
	private final Set<String> inPools = new HashSet<>();

	/**
	 * 锁，针对连接信息处理
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午1:04:43
	 */
	private final Lock conLock = new ReentrantLock();

	/**
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 上午10:19:48
	 * @param name 该连接信息套件的名字
	 * @param driver 连接用驱动
	 * @param url 连接用地址
	 * @param user 连接用帐号名
	 * @param pass 连接用密码
	 * @param description 连接的说明
	 * @param useUnicode 是否使用unicode格式
	 * @param characterEncoding 连接用字符编码类型
	 * @param readOnly 是否只读操作
	 * @param timeOut 连接后空闲超时时间
	 * @param min 最小连接数
	 * @param max 最大连接数
	 * @param others 其他属性对应键值对，需为键与值成对出现，1为键名，2为键值，以此类推
	 */
	public ConnectionInfoBean(final String name, final String driver, final String url, final String user, final String pass, final String description, final boolean useUnicode, final String characterEncoding, final boolean readOnly, final long timeOut, final int min, final int max, final String... others) {
		this.name = name;
		this.driver = driver;
		if (url.indexOf("?") == -1) {
			this.url = url + "?createDatabaseIfNotExist=true&useUnicode=" + useUnicode + "&characterEncoding=" + characterEncoding + "&useSSL=false";
		} else {
			this.url = url + "&createDatabaseIfNotExist=true&useUnicode=" + useUnicode + "&characterEncoding=" + characterEncoding + "&useSSL=false";
		}
		this.user = user;
		this.pass = pass;
		this.description = description;
		this.useUnicode = useUnicode;
		this.characterEncoding = characterEncoding;
		this.readOnly = readOnly;
		// 超时时间5分钟，暂定，不可配置
		this.idelTime = DaoConstants.BASE_CONNECTION_IDLE_TIME_OUT;
		if (timeOut < (this.idelTime * 2)) {
			this.timeOut = this.idelTime * 2;
			this.min = 1;
			this.max = 3;
		} else {
			this.timeOut = timeOut;
			this.min = min;
			this.max = max;
		}
		// 认为比较有效的安全连接时间是10s，暂定，不可配置
		this.safetyOut = 10000;
		final int s = others.length / 2;
		this.others = new HashMap<>(s);
		for (int i = 0; i < s; i++) {
			this.others.put(others[i * 2], others[(i * 2) + 1]);
		}
		this.freeCons = new LinkedList<>();
		this.useCons = new LinkedList<>();
	}

	/**
	 * 得到连接的名字
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午8:40:00
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 得到连接用驱动
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:22:52
	 * @return the driver
	 */
	public String getDriver() {
		return this.driver;
	}

	/**
	 * 得到连接用地址
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:22:52
	 * @return the url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * 得到连接用帐号名
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:22:52
	 * @return the user
	 */
	public String getUser() {
		return this.user;
	}

	/**
	 * 得到连接用密码
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:22:52
	 * @return the pass
	 */
	public String getPass() {
		return this.pass;
	}

	/**
	 * 得到连接的说明
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:22:52
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * 得到是否使用unicode格式
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:22:52
	 * @return the useUnicode
	 */
	public boolean isUseUnicode() {
		return this.useUnicode;
	}

	/**
	 * 得到连接用字符编码类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:22:52
	 * @return the characterEncoding
	 */
	public String getCharacterEncoding() {
		return this.characterEncoding;
	}

	/**
	 * 得到是否只读操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:22:52
	 * @return the readOnly
	 */
	public boolean isReadOnly() {
		return this.readOnly;
	}

	/**
	 * 得到最大超时时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:22:52
	 * @return the timeOut
	 */
	public long getTimeOut() {
		return this.timeOut;
	}

	/**
	 * 得到连接后空闲超时时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:22:52
	 * @return the idelTime
	 */
	public long getIdelTime() {
		return this.idelTime;
	}

	/**
	 * 得到超时安全时间
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:22:52
	 * @return the safetyOut
	 */
	public long getSafetyOut() {
		return this.safetyOut;
	}

	/**
	 * 得到最小连接数
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:22:52
	 * @return the min
	 */
	public int getMin() {
		return this.min;
	}

	/**
	 * 得到最大连接数
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:22:52
	 * @return the max
	 */
	public int getMax() {
		return this.max;
	}

	/**
	 * 得到其他属性对应键值对
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 下午7:22:52
	 * @return the others
	 */
	public Map<String, String> getOthers() {
		return this.others;
	}

	/**
	 * 得到被实际创建连接对象的索引值<br />
	 * 每次调用此方法，值+1<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午8:38:21
	 * @return 被实际创建连接对象的索引值
	 */
	protected int getConnectionIndex() {
		return this.ind++;
	}

	/**
	 * 放入新的被使用中的连接对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 上午10:53:21
	 * @param cb 连接信息
	 */
	protected void addUseCons(final ConnectionBean cb) {
		this.conLock.lock();
		try {
			this.useCons.add(cb);
		} finally {
			this.conLock.unlock();
		}
	}

	/**
	 * 释放使用过后的连接对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午4:47:50
	 * @param cb 目标连接对象
	 */
	protected void releaseConn(final ConnectionBean cb) {
		this.conLock.lock();
		try {
			if (this.useCons.remove(cb)) {
				if (this.isRun) {
					if (this.max >= this.freeCons.size()) {
						// 在有效数量内
						cb.refreshUseTime();
						this.freeCons.add(cb);
					} else {
						// 为应急创建等情况，销毁
						cb.closeWithTooMany();
					}
				} else {
					// 因为信息已经失宠，而直接关闭
					cb.closeWithFall();
				}
			} else {
				// 不存在的直接放弃
				cb.closeWithFall();
			}
		} finally {
			this.conLock.unlock();
		}
	}

	/**
	 * 关闭连接
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 下午8:41:36
	 */
	private void clearCons() {
		while (this.freeCons.size() > 0) {
			this.freeCons.remove(0).closeWithFall();
		}
	}

	/**
	 * 因系统关闭而关闭连接
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午2:14:31
	 */
	private void shutdownCons() {
		while (this.freeCons.size() > 0) {
			this.freeCons.remove(0).closeWithShutdown();
		}
	}

	/**
	 * 验证内部存在连接的有效性<br />
	 * 只处理空闲队列中的连接<br />
	 * 会存在关闭必须要的连接的操作<br />
	 * 因为这里主要是锁空闲队列，而同时有与空闲队列操作的方法，也都会有该锁，所以不会有连接对象的同步问题<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月23日 下午2:33:16
	 */
	protected void validateCons() {
		this.conLock.lock();
		try {
			ConnectionBean cb;
			for (int s = this.freeCons.size() - 1; s >= 0; s--) {
				cb = this.freeCons.get(s);
				if (!cb.validateConn()) {
					// 因为验证失败而移除
					this.freeCons.remove(s);
				}
			}
		} finally {
			this.conLock.unlock();
		}
	}

	/**
	 * 得到一个连接对象，根据是否必须，来决定是根据队列情况是等待有空闲出现，还是直接创建新的
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 上午11:06:14
	 * @return 连接对象
	 */
	protected ConnectionBean getConnection() {
		// 这里不做更多的运行情况判定，因为本质上是不可能出现的，如果有一定是代码问题
		this.conLock.lock();
		try {
			while (this.freeCons.size() > 0) {
				final ConnectionBean cb = this.freeCons.remove(0);
				if (cb.canUse()) {
					this.useCons.add(cb);
				} else {
					// add 2019-02-29
					continue;
				}
				return cb;
			}
			return null;
		} finally {
			this.conLock.unlock();
		}
	}

	/**
	 * 得到当前连接数总量
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 上午10:37:21
	 * @return 当前连接数总量
	 */
	public int getConnectionCount() {
		// 因为是瞬态，所以就不锁了
		return this.freeCons.size() + this.useCons.size();
	}

	/**
	 * 注册连接池信息<br />
	 * 如果是第一个开始的，则需要同时创建一个监听线程<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午8:55:25
	 * @param pool 相关的池
	 */
	protected void reguestPool(final ConnectionPool pool) {
		this.inPools.add(pool.getName());
		if (!this.isRun) {
			this.isRun = true;
		}
	}

	/**
	 * 注销连接池信息<br />
	 * 如果没有其他相关的池，则销毁自身相关的连接信息，并向监听线程发送一条数据信息关闭消息<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月22日 下午8:55:27
	 * @param pool 相关的池
	 */
	protected void unreguestPool(final ConnectionPool pool) {
		this.inPools.remove(pool.getName());
		if (this.inPools.size() == 0) {
			// 因为没有外在调用了，所以销毁掉其中的所有连接，并修改运行状态
			this.isRun = false;
			// 清理掉所有空闲中的连接
			this.clearCons();
		}
	}

	/**
	 * 因系统关闭而注销连接池信息
	 * 
	 * @author tfzzh
	 * @dateTime 2016年11月18日 下午2:15:16
	 * @param pool 相关的池
	 */
	protected void shutdownPool(final ConnectionPool pool) {
		this.inPools.remove(pool.getName());
		if (this.inPools.size() == 0) {
			// 因为没有外在调用了，所以销毁掉其中的所有连接，并修改运行状态
			this.isRun = false;
			// 清理掉所有空闲中的连接
			this.shutdownCons();
		}
	}
}
