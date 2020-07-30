/**
 * @author 许纬杰
 * @datetime 2016年3月25日_上午11:34:02
 */
package com.tfzzh.tools;

/**
 * 过滤字处理“DFA模型？”
 * 
 * @author 许纬杰
 * @datetime 2016年3月25日_上午11:34:02
 */
public class FilterWordDFA {

	/**
	 * 起始节点
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月25日_下午12:09:13
	 */
	private final DFANode mainNode;

	/**
	 * 对象唯一实例
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月25日_上午11:35:34
	 */
	private final static FilterWordDFA dfa = new FilterWordDFA();

	/**
	 * @author 许纬杰
	 * @datetime 2016年3月25日_上午11:35:33
	 */
	private FilterWordDFA() {
		this.mainNode = new DFANode(null, false);
	}

	/**
	 * 得到对象实例
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月25日_上午11:35:31
	 * @return 对象唯一实例
	 */
	public static FilterWordDFA getInstance() {
		return FilterWordDFA.dfa;
	}

	/**
	 * 放入过滤词
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月25日_下午1:35:30
	 * @param word 目标字串
	 */
	public void putFilterWord(final String word) {
		char c;
		DFANode n = this.mainNode;
		for (int i = 0, s = word.length() - 1; i <= s; i++) {
			c = word.charAt(i);
			n = n.getOrCreate(new Character(c), i == s);
		}
	}

	/**
	 * 验证是否存在屏蔽词
	 * 
	 * @author 许纬杰
	 * @datetime 2016年7月14日_下午5:53:04
	 * @param context 文本内容
	 * @return true，安全的不存在屏蔽词；<br />
	 *         false，存在屏蔽词；<br />
	 */
	public boolean hasFilterWord(final String context) {
		char c;
		DFANode dn;
		for (int i = 0; i < context.length(); i++) {
			c = context.charAt(i);
			dn = this.mainNode.getNode(Character.valueOf(c));
			if (null == dn) {
				// 不存在对应的目标数据
				continue;
			}
			if (0 != this.filterFlow(context, i, dn)) {
				// 是成功情况
				return false;
			}
		}
		return true;
	}

	/**
	 * 进行对目标内容的过滤
	 * 
	 * @author 许纬杰
	 * @datetime 2016年3月25日_下午2:11:10
	 * @param context 目标内容
	 * @param replace 替换符
	 * @return 过滤后的内容
	 */
	public String filterWord(final String context, final String replace) {
		final StringBuilder sb = new StringBuilder();
		char c;
		DFANode dn;
		for (int i = 0, j; i < context.length(); i++) {
			c = context.charAt(i);
			dn = this.mainNode.getNode(Character.valueOf(c));
			if (null == dn) {
				// 不存在对应的目标数据
				sb.append(c);
				continue;
			}
			if (0 != (j = this.filterFlow(context, i, dn))) {
				// 是成功情况
				sb.append(replace);
				i = j;
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * @author 许纬杰
	 * @datetime 2016年3月28日_下午4:14:35
	 * @param context 文本内容
	 * @param currInd 当前索引
	 * @param node 相关节点
	 * @return 替换的节点位置
	 */
	private int filterFlow(final String context, final int currInd, final DFANode node) {
		final int currIndA = currInd + 1;
		if (currIndA < context.length()) {
			final char c = context.charAt(currIndA);
			final DFANode nn = node.getNode(Character.valueOf(c));
			if (null != nn) {
				int bak;
				if (0 != (bak = this.filterFlow(context, currIndA, nn))) {
					// 是成功情况
					return bak;
				}
			}
		}
		if (node.isOver()) {
			return currInd;
		} else {
			return 0;
		}
	}
	// public static void main(String[] args) {
	// FilterWordDFA fw = FilterWordDFA.getInstance();
	// fw.putFilterWord("发的说法");
	// fw.putFilterWord("1");
	// fw.putFilterWord("2");
	// fw.putFilterWord("3");
	// fw.putFilterWord("324");
	// String con = "\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"this. .setPositionX(this.presentLabelTitle.getPositionX()+num);this.presentLabelTitle.setPositionY(this.presentLabelTitle.getPositionY()+ Math.abs(fx));this.presentLabelTitle.setPosition(this.presentLabelBg.getPositionX() + 6, this.presentLabelBg.getPositionY() -
	// rowDifference);this.presentLabelType.setPosition(this.presentLabelTitle.getPositionX(), this.presentLabelTitle.getPositionY() - rowDifference);this.goodLabelPresent_1.setPosition(this.presentLabelType.getPositionX(), this.presentLabelType.getPositionY() - ((natureLine + 1) * rowDifference)); for (var i = 0; i < this.haveAllGoods.length; i++) { if (data.itemType[data.goods[this.haveAllGoods[i].good_id].itid].itpos > 3) {}}\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"this.
	// .setPositionX(this.presentLabelTitle.getPositionX()+num);this.presentLabelTitle.setPositionY(this.presentLabelTitle.getPositionY()+ Math.abs(fx));this.presentLabelTitle.setPosition(this.presentLabelBg.getPositionX() + 6, this.presentLabelBg.getPositionY() - rowDifference);this.presentLabelType.setPosition(this.presentLabelTitle.getPositionX(), this.presentLabelTitle.getPositionY() - rowDifference);this.goodLabelPresent_1.setPosition(this.presentLabelType.getPositionX(),
	// this.presentLabelType.getPositionY() - ((natureLine + 1) * rowDifference)); for (var i = 0; i < this.haveAllGoods.length; i++) { if (data.itemType[data.goods[this.haveAllGoods[i].good_id].itid].itpos > 3) {}}\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"this. .setPositionX(this.presentLabelTitle.getPositionX()+num);this.presentLabelTitle.setPositionY(this.presentLabelTitle.getPositionY()+ Math.abs(fx));this.presentLabelTitle.setPosition(this.presentLabelBg.getPositionX() + 6, this.presentLabelBg.getPositionY() -
	// rowDifference);this.presentLabelType.setPosition(this.presentLabelTitle.getPositionX(), this.presentLabelTitle.getPositionY() - rowDifference);this.goodLabelPresent_1.setPosition(this.presentLabelType.getPositionX(), this.presentLabelType.getPositionY() - ((natureLine + 1) * rowDifference)); for (var i = 0; i < this.haveAllGoods.length; i++) { if (data.itemType[data.goods[this.haveAllGoods[i].good_id].itid].itpos > 3) {}}\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"this.
	// .setPositionX(this.presentLabelTitle.getPositionX()+num);this.presentLabelTitle.setPositionY(this.presentLabelTitle.getPositionY()+ Math.abs(fx));this.presentLabelTitle.setPosition(this.presentLabelBg.getPositionX() + 6, this.presentLabelBg.getPositionY() - rowDifference);this.presentLabelType.setPosition(this.presentLabelTitle.getPositionX(), this.presentLabelTitle.getPositionY() - rowDifference);this.goodLabelPresent_1.setPosition(this.presentLabelType.getPositionX(),
	// this.presentLabelType.getPositionY() - ((natureLine + 1) * rowDifference)); for (var i = 0; i < this.haveAllGoods.length; i++) { if (data.itemType[data.goods[this.haveAllGoods[i].good_id].itid].itpos > 3) {}}\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"this. .setPositionX(this.presentLabelTitle.getPositionX()+num);this.presentLabelTitle.setPositionY(this.presentLabelTitle.getPositionY()+ Math.abs(fx));this.presentLabelTitle.setPosition(this.presentLabelBg.getPositionX() + 6, this.presentLabelBg.getPositionY() -
	// rowDifference);this.presentLabelType.setPosition(this.presentLabelTitle.getPositionX(), this.presentLabelTitle.getPositionY() - rowDifference);this.goodLabelPresent_1.setPosition(this.presentLabelType.getPositionX(), this.presentLabelType.getPositionY() - ((natureLine + 1) * rowDifference)); for (var i = 0; i < this.haveAllGoods.length; i++) { if (data.itemType[data.goods[this.haveAllGoods[i].good_id].itid].itpos > 3) {}}\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"1324234357&162&240&165\", \"clip_zone2\":\"205&139&175&163\"2342 \"clip_zone2\":\"229&166&104&138\"234234 \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"229&166&104&138\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\",
	// \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"21312314 \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", 发的说法\"clip_zone\":\"157&162&240&165\",
	// 12312\"clip_zone\":\"157&162&240&165\", 213\"clip_zone\":\"1是否&162&240&165\", 324\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"234 \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", 32423\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"\"clip_zone\":\"157&162&240&165\", \"clip_zone2\":\"205&139&175&163\"";
	// long l1 = System.currentTimeMillis();
	// for (int i = 10000; i >= 0; i--) {
	// fw.filterWord(con, "*");
	// }
	// System.out.println("use[" + (System.currentTimeMillis() - l1) + "] >> " + fw.filterWord(con, "*"));
	// }
}