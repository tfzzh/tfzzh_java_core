/**
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午3:19:20
 */
package com.tfzzh.model.dao.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tfzzh.model.dao.annotation.DataField;
import com.tfzzh.model.dao.annotation.IdField;

/**
 * 数据字段类型
 * 
 * @author Weijie Xu
 * @dateTime 2015年4月20日 下午3:19:20
 */
public interface FieldType {

	/**
	 * 该类型的名称<br />
	 * 这里因为目标对应枚举，所以直接使用了枚举对应的该方法<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月5日 下午7:54:10
	 * @return 该类型的名称
	 */
	String name();

	/**
	 * 得到真实的默认值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午8:19:25
	 * @param dv 默认值，字符串模型
	 * @return 真实的默认值
	 */
	Object getDefaultValue(String dv);

	/**
	 * 制作alter内容
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年4月21日 上午10:50:27
	 * @param sb 组合用字串，会直接将增加的内容，放入进去
	 * @param df 数据字段信息
	 * @param idf Id类字段信息
	 * @return 同sb
	 */
	StringBuilder makeAlter(StringBuilder sb, DataField df, IdField idf);

	/**
	 * 是否为可以设置自增的类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月5日 下午7:10:57
	 * @return true，可以为自增；<br />
	 *         false，不可以为自增；<br />
	 */
	boolean canIncrement();

	/**
	 * 是否可以设置为UUID的类型
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午1:43:36
	 * @return true，可以为UUID；<br />
	 *         false，不可以为UUID；<br />
	 */
	boolean canUuid();

	/**
	 * 是否相同的类型<br />
	 * 针对更新数据库结构时，当前主要结构型数据库<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月5日 下午5:20:15
	 * @param type 类型名，请保证内容均为大写
	 * @return true，是相同的；<br />
	 *         false，是不同的；<br />
	 */
	boolean sameType(String type);

	/**
	 * 验证字段数据是否相同<br />
	 * 针对更新数据库结构时，当前主要结构型数据库<br />
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月5日 下午6:33:14
	 * @param df 数据字段信息
	 * @param idf Id类字段信息
	 * @param length 字段长度
	 * @param canNull 是否可null
	 * @param unsigned 是否无符号，针对数值
	 * @param isIncrement 是否为自增
	 * @param scale 小数点位数
	 * @param defValue 默认值
	 * @param desc 说明内容
	 * @return true，是相同的；<br />
	 *         false，是不同的；<br />
	 */
	boolean validateData(DataField df, IdField idf, int length, boolean canNull, boolean unsigned, boolean isIncrement, int scale, String defValue, String desc);

	/**
	 * 放入值到声明对象
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月6日 下午3:50:49
	 * @param ps 声明对象
	 * @param val 目标值
	 * @param ind 目标位置，在ps中
	 */
	void putValueToPs(PreparedStatement ps, Object val, int ind);

	/**
	 * 向对象中设置结果集中的对应值
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月9日 下午4:57:08
	 * @param rs jdbc反馈的结果集
	 * @param obj 目标对象
	 * @param setM 对应的目标方法
	 * @param ind 在rs的索引位
	 * @throws IllegalAccessException 抛
	 * @throws IllegalArgumentException 抛
	 * @throws InvocationTargetException 抛
	 * @throws SQLException 抛
	 */
	void setValueWithResult(ResultSet rs, Object obj, Method setM, int ind) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException;

	/**
	 * 放入内容到mongo条件
	 * 
	 * @author tfzzh
	 * @dateTime 2017年1月9日 下午4:35:48
	 * @param mongo mongo条件串
	 * @param val 目标值
	 */
	void putValueToDBObject(StringBuilder mongo, Object val);

	/**
	 * 将对象按照目标类型进行可明文显示处理
	 * 
	 * @author Weijie Xu
	 * @dateTime 2015年5月8日 下午7:10:39
	 * @param obj 目标对象
	 * @return 可被明文显示的内容
	 */
	Object toShow(Object obj);
}
