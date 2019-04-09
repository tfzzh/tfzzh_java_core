/**
 * @author Weijie Xu
 * @dateTime 2014年12月13日 下午2:16:55
 */
package com.tfzzh.socket.tools;

import java.io.IOException;
import java.util.Map;

import com.tfzzh.model.tools.iface.DataFieldOperationBean;
import com.tfzzh.model.tools.iface.PutDataFieldOperation;

/**
 * 消息字段的操作
 * 
 * @author Weijie Xu
 * @dateTime 2014年12月13日 下午2:16:55
 * @param <M> 消息实例对象
 */
public interface MessageFieldOperation<M extends MessageFieldOperationBean> {

	/**
	 * 读取数据消息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年12月13日 下午2:35:07
	 * @param m 目标对象
	 * @param dis 被输入数据流
	 * @param problemMsg 问题消息列表
	 * @throws IOException 抛
	 */
	void read(M m, TfzzhDataInputStream dis, Map<String, String> problemMsg) throws IOException;

	/**
	 * 写入数据消息
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年12月13日 下午2:35:08
	 * @param m 目标对象
	 * @param dos 预写出数据流
	 * @throws IOException 抛
	 */
	void write(M m, TfzzhDataOutputStream dos) throws IOException;

	/**
	 * 关联的数字字段
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年12月15日 上午10:18:36
	 * @param dataField 数据字段控制信息
	 */
	void relatedDataFields(PutDataFieldOperation dataField);

	/**
	 * 进行变更数据字段的操作
	 * 
	 * @author Weijie Xu
	 * @dateTime 2014年12月15日 下午2:52:52
	 * @param data 数据对象
	 * @param msg 消息对象
	 */
	void changeDataField(DataFieldOperationBean data, M msg);
}
