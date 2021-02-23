/**
 * @author XuWeijie
 * @datetime 2015年9月6日_下午3:24:05
 */
package com.tfzzh.view.web.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.tfzzh.tools.RandomCode;
import com.tfzzh.tools.TfzzhRandom;

/**
 * 验证码工具
 * 
 * @author XuWeijie
 * @datetime 2015年9月6日_下午3:24:05
 */
public class SeccodeFactory {

	/**
	 * 得到随机验证码（类型：大写+数字；长度：4）
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午3:46:58
	 * @param width 宽
	 * @param height 高
	 * @return 验证码信息
	 */
	public static SeccodeInfoBean getSeccode(final int width, final int height) {
		return SeccodeFactory.getSeccode(4, width, height);
	}

	/**
	 * 得到随机验证码（类型：大写+数字）
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午3:46:54
	 * @param length 验证码长度
	 * @param width 宽
	 * @param height 高
	 * @return 验证码信息
	 */
	public static SeccodeInfoBean getSeccode(final int length, final int width, final int height) {
		return SeccodeFactory.getSeccode(RandomCode.CODE_TYPE_UPPERCASE_DIGITAL, 4, width, height);
	}

	/**
	 * 得到随机验证码
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午3:49:46
	 * @param type 验证码类型，同{@See RandomCode}
	 * @param length 验证码长度
	 * @param width 宽
	 * @param height 高
	 * @return 验证码信息
	 */
	public static SeccodeInfoBean getSeccode(final int type, int length, final int width, final int height) {
		final RandomCode rc = RandomCode.getInstance(type);
		// 进行长度限制
		if ((length < 2) || (length > 10)) {
			length = 4;
		}
		// 得到码
		final String code = rc.getRandomCode(length);
		final BufferedImage bi = SeccodeFactory.getImage(code, width, height);
		return new SeccodeInfoBean(code, bi);
	}

	/**
	 * 字体列表
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午4:48:32
	 */
	private static final String[] FONTS = new String[] { "Helvetica", "Garamond", "Frutiger", "Bodoni", "Futura", "Times", "Arial" };

	/**
	 * 得到验证码图形内容
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午3:51:39
	 * @param code 目标码内容
	 * @param width 宽
	 * @param height 高
	 * @return 图形内容
	 */
	private static BufferedImage getImage(final String code, int width, int height) {
		if (height < 14) {
			height = 14;
		} else if (height > 220) {
			height = 220;
		}
		final int min = (code.length() * 7) + 2;
		final int max = (code.length() * 110) + 10;
		if (width < min) {
			width = min;
		} else if (width > max) {
			width = max;
		}
		// 字体高
		final int hfs = (height * 6) / 7;
		final int wfs = (width * 12) / code.length() / 7;
		final int fs = Math.min(hfs, wfs);
		final int fsw = (fs / 2) + (fs & 1);
		final int codeY = height - ((fs * 8) / 9);
		final int codeX = (width - (fsw * code.length())) / (code.length() + 1);
		// 定义图像buffer
		final BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g = buffImg.createGraphics();
		// 将图像填充为白色
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		final TfzzhRandom tr = TfzzhRandom.getInstance();
		// 创建字体，字体的大小应该根据图片的高度来定。
		final Font font = new Font(SeccodeFactory.FONTS[tr.nextInt(SeccodeFactory.FONTS.length)], Font.PLAIN, fs);
		// 设置字体。
		g.setFont(font);
		g.drawRect(0, 0, width - 1, height - 1);
		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		char c;
		int red, green, blue;
		// 随机产生codeCount数字的验证码。
		int x = tr.nextInt(3 * codeX);
		float angle = 0;
		for (int i = 0; i < code.length(); i++) {
			c = code.charAt(i);
			// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
			red = tr.nextInt(255);
			green = tr.nextInt(255);
			blue = tr.nextInt(255);
			// 用随机产生的颜色将验证码绘制到图像中。
			g.setColor(new Color(red, green, blue));
			angle = (float) (((tr.nextInt(41) - 20f) * Math.PI) / 180f);
			// g.rotate(angle);
			// g.drawString(String.valueOf(c), x + i * fsw, tr.nextInt(codeY) + fs);
			// g.rotate(-angle);
			SeccodeFactory.rotateString(String.valueOf(c), x + (i * fsw), (tr.nextInt(codeY * 2) - codeY) + fs, g, angle);
			x += tr.nextInt(((codeX * (i + 3)) - x));
		}
		return buffImg;
	}

	/**
	 * 字体旋转
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午6:49:14
	 * @param s 内容
	 * @param x x轴起点
	 * @param y y轴起点
	 * @param g 所在范围
	 * @param degree 角度
	 */
	private static void rotateString(final String s, final int x, final int y, final Graphics2D g, final float degree) {
		final Graphics2D g2d = (Graphics2D) g.create();
		// 平移原点到图形环境的中心 ,这个方法的作用实际上就是将字符串移动到某一个位置
		g2d.translate(x, y);
		// 旋转文本
		g2d.rotate(degree);
		// 特别需要注意的是,这里的画笔已经具有了上次指定的一个位置,所以这里指定的其实是一个相对位置
		g2d.drawString(s, 0, 0);
	}

	/**
	 * 验证码信息
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午3:25:14
	 */
	public static class SeccodeInfoBean {

		/**
		 * 验证码内容
		 * 
		 * @author XuWeijie
		 * @datetime 2015年9月6日_下午3:31:40
		 */
		private final String code;

		/**
		 * 验证码图片
		 * 
		 * @author XuWeijie
		 * @datetime 2015年9月6日_下午3:31:53
		 */
		private final BufferedImage img;

		/**
		 * @author XuWeijie
		 * @datetime 2015年9月6日_下午3:32:43
		 * @param code 验证码内容
		 * @param img 验证码图片
		 */
		public SeccodeInfoBean(final String code, final BufferedImage img) {
			this.code = code;
			this.img = img;
		}

		/**
		 * 得到验证码内容
		 * 
		 * @author XuWeijie
		 * @datetime 2015年9月6日_下午3:32:36
		 * @return the code
		 */
		public String getCode() {
			return this.code;
		}

		/**
		 * 得到验证码图片
		 * 
		 * @author XuWeijie
		 * @datetime 2015年9月6日_下午3:32:36
		 * @return the img
		 */
		public BufferedImage getImg() {
			return this.img;
		}
	}
}
