/**
 * @author tfzzh
 * @dateTime 2024年8月28日 09:49:34
 */
package com.tfzzh.tools;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * 图片相关工具
 * 
 * @author tfzzh
 * @dateTime 2024年8月28日 09:49:34
 */
public class ImageUtil {
	// public static void main(String[] args) {
	// String imgPath = "E:/Document/小游戏/";
	// String imgName = "tst_long_img";
	// String imgSuf = "jpg";
	// int tarHei = 700;
	// try {
	// List<String> imgPaths = ImageUtil.splitImage(imgPath + imgName + "." + imgSuf, imgPath, imgName, imgSuf, tarHei);
	// System.out.println(" imgPaths ... [" + imgPaths + "]");
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 分割图片
	 * 
	 * @author tfzzh
	 * @dateTime 2024年10月18日 16:44:53
	 * @param srcPath 原图片地址
	 * @param imgPath 分割完图片地址
	 * @param imgName 图片基础名
	 * @param imgSuf 图片后缀
	 * @return 处理好的文件名集合
	 * @throws IOException 抛
	 */
	public static List<String> splitImage(final String srcPath, final String imgPath, final String imgName, final String imgSuf) throws IOException {
		return ImageUtil.splitImage(srcPath, imgPath, imgName, imgSuf, 1500, 750);
	}

	/**
	 * 分割图片
	 * 
	 * @author tfzzh
	 * @dateTime 2024年10月18日 16:44:52
	 * @param srcPath 原图片地址
	 * @param imgPath 分割完图片地址
	 * @param imgName 图片基础名
	 * @param imgSuf 图片后缀
	 * @param chunkHeight 分割图片目标高
	 * @return 处理好的文件名集合
	 * @throws IOException 抛
	 */
	public static List<String> splitImage(final String srcPath, final String imgPath, final String imgName, final String imgSuf, final int chunkHeight) throws IOException {
		return ImageUtil.splitImage(srcPath, imgPath, imgName, imgSuf, chunkHeight, 750);
	}

	/**
	 * 分割图片
	 * 
	 * @author tfzzh
	 * @dateTime 2024年10月18日 16:52:51
	 * @param srcPath 原图片地址
	 * @param imgPath 分割完图片地址
	 * @param imgName 图片基础名
	 * @param imgSuf 图片后缀
	 * @param chunkHeight 分割图片目标高
	 * @param maxWidth 最大宽度限制，超过先宽度压缩
	 * @return 处理好的文件名集合
	 * @throws IOException 抛
	 */
	public static List<String> splitImage(final String srcPath, final String imgPath, final String imgName, final String imgSuf, int chunkHeight, final int maxWidth) throws IOException {
		final File inputFile = new File(srcPath);
		BufferedImage inpImg = ImageIO.read(inputFile);
		int imgWidth = inpImg.getWidth();
		// ImageTypeSpecifier type = ImageTypeSpecifier.createFromRenderedImage(inpImg);
		final String imgType = ImageUtil.getImgType(imgSuf);
		if (imgWidth > maxWidth) {
			inpImg = ImageUtil.reduceImage(inpImg, maxWidth, -1);
			imgWidth = inpImg.getWidth();
			final String redPath = imgName + "_redu." + imgSuf;
			final File outFile = new File(imgPath, redPath);
			ImageIO.write(inpImg, imgType, outFile);
			try {
				Files.setPosixFilePermissions(outFile.toPath(), FileTools.FILE_PERMISSION);
			} catch (final Exception e) {
			}
		}
		// return;
		final int imgHeight = inpImg.getHeight();
		int chunks = imgHeight / chunkHeight;
		if ((imgHeight - (chunkHeight * chunks)) > (chunkHeight / 4)) {
			chunks += 1;
		}
		chunkHeight = (imgHeight / chunks) + 1;
		chunkHeight = (chunkHeight / 20) * 20; // 十位取整
		int sHei = 0;
		int ind = 1;
		final List<String> fileNames = new ArrayList<>(chunks);
		while (sHei < imgHeight) {
			final int iHei = ((sHei + chunkHeight) < imgHeight) ? chunkHeight : (imgHeight - sHei);
			final BufferedImage chunkImage = inpImg.getSubimage(0, sHei, imgWidth, iHei);
			final String chunkFileName = imgName + "_ck_" + ind + "." + imgSuf;
			final File outputFile = new File(imgPath, chunkFileName);
			fileNames.add(chunkFileName);
			ImageIO.write(chunkImage, imgType, outputFile);
			try {
				Files.setPosixFilePermissions(outputFile.toPath(), FileTools.FILE_PERMISSION);
			} catch (final Exception e) {
			}
			sHei += chunkHeight;
			ind++;
		}
		return fileNames;
	}

	/**
	 * 得到图片类型
	 * 
	 * @author tfzzh
	 * @dateTime 2025年1月16日 16:29:43
	 * @param suf 后缀名
	 * @return 目标类型
	 */
	private static String getImgType(String suf) {
		suf = suf.toLowerCase();
		return switch (suf) {
		case "jpg", "jpeg" -> "jpeg";
		case "png" -> "png";
		case "gif" -> "gif";
		case "bmp" -> "bmp";
		case "tif", "tiff" -> "tiff";
		case "wbmp" -> "wbmp";
		default -> "png";
		};
	}

	/**
	 * 压缩图片
	 * 
	 * @author tfzzh
	 * @dateTime 2025年1月10日 15:16:57
	 * @param srcDiskPath 原始文件地址
	 * @param diskPath 新图片地址
	 * @param diskName 新图片名
	 * @param nameSuf 图片后缀
	 * @param tarWid 目标最大宽
	 * @param maxHei 最大高，压缩后超过后做切割
	 * @return 处理后的文件路径
	 * @throws IOException 抛
	 */
	public static String reduceImage(final String srcDiskPath, final String diskPath, final String diskName, final String nameSuf, final int tarWid, final int maxHei) throws IOException {
		final File inputFile = new File(srcDiskPath);
		final BufferedImage inpImg = ImageIO.read(inputFile);
		final BufferedImage bi = ImageUtil.reduceImage(inpImg, tarWid, maxHei);
		final String redName = diskName + Constants.SPOT + nameSuf;
		final File outFolder = new File(diskPath);
		if (!outFolder.exists()) {
			outFolder.mkdirs();
		}
		final File outFile = new File(diskPath, redName);
		if (!outFile.exists()) {
			try {
				Files.createFile(outFile.toPath(), FileTools.FILE_ATTR);
			} catch (final UnsupportedOperationException uoe) {
				Files.createFile(outFile.toPath());
			}
		}
		final String imgType = ImageUtil.getImgType(nameSuf);
		ImageIO.write(bi, imgType, outFile);
		try {
			Files.setPosixFilePermissions(outFile.toPath(), FileTools.FILE_PERMISSION);
		} catch (final Exception e) {
		}
		return diskPath + redName;
	}

	/**
	 * 压缩图片
	 * 
	 * @author tfzzh
	 * @dateTime 2025年1月10日 14:11:17
	 * @param inpImg 入口图片文件
	 * @param tarWid 目标宽
	 * @param maxHei 最大高度限制
	 * @return 新的图片字节流
	 */
	private static BufferedImage reduceImage(final BufferedImage inpImg, int tarWid, final int maxHei) {
		final int imgWidth = inpImg.getWidth();
		int imgHeight = inpImg.getHeight();
		int tarHei = imgHeight;
		if (imgWidth > tarWid) {
			tarHei = (imgHeight * tarWid) / imgWidth;
			if (maxHei > 0) {
				if (tarHei > maxHei) {
					imgHeight = tarHei;
					tarHei = maxHei;
				} else {
					imgHeight = tarHei;
				}
			} else {
				imgHeight = tarHei;
			}
		} else {
			tarWid = imgWidth;
			if (maxHei > 0) {
				tarHei = (imgHeight * tarWid) / imgWidth;
				if (tarHei > maxHei) {
					tarHei = maxHei;
				}
			}
		}
		final Image resImg = inpImg.getScaledInstance(tarWid, imgHeight, Image.SCALE_SMOOTH);
		final BufferedImage outImg = new BufferedImage(tarWid, tarHei, BufferedImage.TYPE_INT_RGB);
		// System.out.println(" tarWid[" + tarWid + "] tarHei[" + tarHei + "] imgHeight[" + imgHeight + "] ... ");
		final Graphics2D g2d = outImg.createGraphics();
		g2d.drawImage(resImg, 0, 0, null);
		g2d.dispose();
		return outImg;
	}
}
