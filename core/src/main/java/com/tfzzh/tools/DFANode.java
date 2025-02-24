/**
 * @author 许纬杰
 * @datetime 2016年3月25日_上午11:36:59
 */
package com.tfzzh.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据基础类
 * 
 * @author 许纬杰
 * @datetime 2016年3月25日_上午11:36:59
 */
public class DFANode {

	/**
	 * 自己所表示的字符
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月25日_上午11:38:46
	 */
	private final Character c;

	/**
	 * 其下所相关节点列表
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月25日_上午11:38:47
	 */
	private final Map<Character, DFANode> nodes;

	/**
	 * 是否为结束字符
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月25日_下午1:29:09
	 */
	private final boolean isOver;

	/**
	 * @author 许纬杰
	 * @datetime 2016年3月25日_上午11:39:26
	 * @param c 所相关字节
	 * @param isOver 是否为结束字符
	 */
	protected DFANode(final Character c, final boolean isOver) {
		this.c = c;
		this.nodes = new HashMap<>();
		this.isOver = isOver;
	}

	/**
	 * 放入一个节点
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月25日_上午11:40:48
	 * @param node 节点
	 */
	protected void putNode(final DFANode node) {
		this.nodes.put(node.c, node);
	}

	/**
	 * 得到或创建目标节点
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月25日_下午2:07:26
	 * @param c 目标字符
	 * @param isOver 是否字串的结束字符
	 * @return 目标节点
	 */
	protected DFANode getOrCreate(final Character c, final boolean isOver) {
		DFANode node = this.nodes.get(c);
		if (null == node) {
			node = new DFANode(c, isOver);
			this.nodes.put(c, node);
		}
		return node;
	}

	/**
	 * 得到所相关节点 k
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月25日_上午11:47:47
	 * @param c 目标字符
	 * @return 所相关的节点
	 */
	protected DFANode getNode(final Character c) {
		return this.nodes.get(c);
	}

	/**
	 * 是否为结束字符</br>
	 * true，为结束字符</br>
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月25日_下午1:29:53
	 * @return the isOver
	 */
	protected boolean isOver() {
		return this.isOver;
	}
}
