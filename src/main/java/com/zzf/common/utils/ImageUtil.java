package com.zzf.common.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;

import org.apache.commons.io.FileUtils;

import sun.awt.image.JPEGImageDecoder;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageUtil {

	/**
	 * 功能描述：缩放图像（按高度和宽度缩放）
	 * 
	 * @param srcImageFile 源图像文件地址
	 * @param result 缩放后的图像地址
	 * @param height 缩放后的高度
	 * @param width 缩放后的宽度
	 * @param flag 比例不对时是否需要补白：true为补白; false为不补白;
	 * 
	 */
	public void scale(String srcImageFile, String result, int width, int height, boolean flag) throws Exception {
		String imageType = srcImageFile.substring(srcImageFile.lastIndexOf(".") + 1);//得到图片类型
		if (imageType.equalsIgnoreCase("png")) {
			flag = true;
		}
		try {
			double widthRatio = 0.0; // 缩放比例
			double heightRatio = 0.0; // 缩放比例
			File f = new File(srcImageFile);
			BufferedImage bi = ImageIO.read(f);
			/* 创建此图像的缩放版本。返回一个新的 Image 对象，默认情况下，该对象按指定的 width 和 height 呈现图像。
			 * 即使已经完全加载了初始源图像，新的 Image 对象也可以被异步加载。 
			 * 如果 width 或 height 为负数，则替换该值以维持初始图像尺寸的高宽比。
			 * 如果 width 和 height 都为负，则使用初始图像尺寸。
			 */
			Image itemp = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);//SCALE_SMOOTH 选择图像平滑度比缩放速度具有更高优先级的图像缩放算法
			// 计算比例
			heightRatio = (new Integer(height)).doubleValue() / bi.getHeight();
			widthRatio = (new Integer(width)).doubleValue() / bi.getWidth();
			//返回表示缩放变换的变换。
			AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(widthRatio, heightRatio), null);
			/* 转换源 BufferedImage 并将结果存储在目标 BufferedImage 中。
			 * 如果两个图像的颜色模型不匹配，则将颜色模型转换成目标颜色模型。
			 * 如果目标图像为 null，则使用源 ColorModel 创建 BufferedImage。
			 */
			itemp = op.filter(bi, null);
			if (flag) {// 补白
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//构造一个类型为预定义图像类型之一的 BufferedImage。
				Graphics2D g = image.createGraphics();//创建一个 Graphics2D，可以将它绘制到此 BufferedImage 中
				g.setColor(Color.white);
				g.fillRect(0, 0, width, height);//填充指定的矩形
				if (width == itemp.getWidth(null))
					g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);//绘制指定图像中已缩放到适合指定矩形内部的图像。
				else
					g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);//绘制指定图像中已缩放到适合指定矩形内部的图像。
				g.dispose();//释放此图形的上下文以及它使用的所有系统资源。调用 dispose 之后，就不能再使用 Graphics 对象。 
				itemp = image;
			}
			//构造一个类型为预定义图像类型之一的 BufferedImage。
			BufferedImage bufImg = new BufferedImage(itemp.getWidth(null), itemp.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics g = bufImg.createGraphics();
			g.drawImage(itemp, 0, 0, null);//绘制指定图像中当前可用的图像。图像的左上角位于该图形上下文坐标空间的 (x, y)。
			g.dispose();//释放此图形的上下文以及它使用的所有系统资源。调用 dispose 之后，就不能再使用 Graphics 对象。 
			ImageIO.write(bufImg, imageType, new File(result));//使用支持给定格式的任意 ImageWriter 将一个图像写入 File
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/***
	 * 功能：压缩图片变成小尺寸***
	 * 参数1：oImage：原图；*
	 * 参数2：maxWidth：小尺寸宽度；*
	 * 参数3：maxHeight：小尺寸长度；*
	 * @throws IOException **
	 * */
	public Image compressImage(File oImage, int maxWidth, int maxHeight) throws IOException {
		BufferedImage srcImage = ImageIO.read(oImage);
		;
		int srcWidth = srcImage.getWidth(null);
		int srcHeight = srcImage.getHeight(null);
		if (srcWidth <= maxWidth && srcHeight <= maxHeight) {
			return srcImage;
		}
		Image scaledImage = srcImage.getScaledInstance(srcWidth, srcHeight, Image.SCALE_SMOOTH);
		double ratio = Math.min((double) maxWidth / srcWidth, (double) maxHeight / srcHeight);
		AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
		scaledImage = op.filter(srcImage, null);
		return scaledImage;
	}

	/** 
	 *  自己设置压缩质量来把图片压缩成byte[] 
	 * 
	 * @param image 压缩源图片 
	 * @param quality 压缩质量，在0-1之间， 
	 * @return 返回的字节数组 
	 * @throws IOException 
	 */
	public static byte[] bufferedImageTobytes(BufferedImage image, float quality) throws IOException {
		if (image == null) { // 如果图片空，返回空  
			return null;
		}
		// 得到指定Format图片的writer ,指定写图片的方式为 jpg
		Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");// 得到迭代器  
		ImageWriter writer = (ImageWriter) iter.next(); // 得到writer  
		// 得到指定writer的输出参数设置(ImageWriteParam )  
		ImageWriteParam iwp = writer.getDefaultWriteParam();
		// 设置可否压缩  ,要使用压缩，必须指定压缩方式为MODE_EXPLICIT
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		// 设置压缩质量参数
		iwp.setCompressionQuality(quality);
		//指定 writer 使用逐步模式写出图像，从而输出流将包含一系列质量递增的扫描。
		iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);

		ColorModel colorModel = ColorModel.getRGBdefault();
		// 指定压缩时使用的色彩模式  
		iwp.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));

		// 开始打包图片，写入byte[]  
		ByteArrayOutputStream out = new ByteArrayOutputStream(); // 取得内存输出流  
		IIOImage iIamge = new IIOImage(image, null, null);
		// 此处因为ImageWriter中用来接收write信息的output要求必须是ImageOutput  
		// 通过ImageIo中的静态方法，得到byteArrayOutputStream的ImageOutput  
		writer.setOutput(ImageIO.createImageOutputStream(out));
		writer.write(null, iIamge, iwp);
		return out.toByteArray();
	}

	/** 
	 * 通过 com.sun.image.codec.jpeg.JPEGCodec提供的编码器来实现图像压缩 
	 *  
	 * @param image 
	 * @param quality 
	 * @return 
	 * @throws IOException 
	 * @throws ImageFormatException 
	 */
	public byte[] newCompressImage(BufferedImage image, float quality) throws Exception {
		if (image == null) { // 如果图片空，返回空  
			return null;
		}
		// 开始开始，写入byte[]  
		ByteArrayOutputStream out = new ByteArrayOutputStream(); // 取得内存输出流  
		// 设置压缩参数  
		JPEGEncodeParam param = JPEGCodec.getDefaultJPEGEncodeParam(image);
		param.setQuality(quality, false);
		// 设置编码器  
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out, param);
		encoder.encode(image);
		return out.toByteArray();

	}

	/** 
	 * 测试把图片先压缩成JPEG，再用JPEG压缩成GIF 
	 * @throws IOException 
	 */
	public byte[] giftest(byte[] imagedata) throws IOException {
		BufferedImage image = null;
		ByteArrayInputStream input = new ByteArrayInputStream(imagedata);
		// 得到解码器  
		JPEGImageDecoder decoder = (JPEGImageDecoder) JPEGCodec.createJPEGDecoder(input);
		// 把JPEG 数据流解压缩  
		image = ((com.sun.image.codec.jpeg.JPEGImageDecoder) decoder).decodeAsBufferedImage();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "gif", out);
		return out.toByteArray();
	}

	/** 
	 * 把图片印刷到图片上 
	 * @param pressImg --水印文件 
	 * @param targetImg --目标文件 
	 * @param x --x坐标 
	 * @param y --y坐标 
	 */
	public static void addWaterMark(String watermark, String filePath, int x, int y) {
		ImageIcon imgIcon = new ImageIcon(filePath);
		Image theImg = imgIcon.getImage();
		int width = theImg.getWidth(null);
		int height = theImg.getHeight(null);
		ImageIcon waterIcon = new ImageIcon(watermark);
		Image waterImg = waterIcon.getImage();
		BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bimage.createGraphics();
		g.drawImage(theImg, 0, 0, null);
		g.drawImage(waterImg, x, y, null);
		g.dispose();
		try {
			FileOutputStream out = new FileOutputStream(filePath);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage);
			param.setQuality(70f, true);
			encoder.encode(bimage, param);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	 * 打印文字水印图片 
	 * @param pressText --文字 
	 * @param targetImg --目标图片 
	 * @param fontName --字体名 
	 * @param fontStyle --字体样式 
	 * @param color --字体颜色 
	 * @param fontSize --字体大小 
	 * @param x --偏移量 
	 * @param y 
	 */
	public static void pressText(String pressText, String targetImg, String fontName, int fontStyle, Color color, int fontSize, int x, int y) {
		try {
			File _file = new File(targetImg);
			Image src = ImageIO.read(_file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			g.setColor(color);//Color.RED
			g.setFont(new Font(fontName, fontStyle, fontSize));
			g.drawString(pressText, wideth - fontSize - x, height - fontSize / 2 - y);
			g.dispose();
			FileOutputStream out = new FileOutputStream(targetImg);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			out.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * 线性缩放指定图片
	 * @param photoFile 源图片
	 * @param photo_width 缩略图的宽度
	 * @param photo_height 缩略图的长度
	 * @param newFileName 缩略图的名称
	 * @throws IOException
	 */
	public static void breviaryPhoto(File photoFile, int photo_width, int photo_height, String newFileName) throws IOException {
		Image image = ImageIO.read(photoFile);
		BufferedImage bufferedImage = new BufferedImage(photo_width, photo_height, BufferedImage.TYPE_INT_RGB);
		bufferedImage.getGraphics().drawImage(image, 0, 0, photo_width, photo_height, null);
		FileOutputStream output = new FileOutputStream(newFileName);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
		encoder.encode(bufferedImage);
		output.close();
	}

	/** 
	 * 对图片裁剪，并把裁剪新图片保存 
	 * @param srcPath 读取源图片路径
	 * @param toPath	写入图片路径
	 * @param x 剪切起始点x坐标
	 * @param y 剪切起始点y坐标
	 * @param width 剪切宽度
	 * @param height	 剪切高度
	 * @throws IOException
	 */
	public void cutImage(String srcPath, String toPath, int x, int y, int width, int height) throws IOException {
		FileInputStream fis = null;
		ImageInputStream iis = null;
		try {
			//读取图片文件
			fis = new FileInputStream(srcPath);
			//获取图片流 
			iis = ImageIO.createImageInputStream(fis);
			Iterator<ImageReader> it = ImageIO.getImageReaders(iis);
			ImageReader reader = (ImageReader) it.next();
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			//定义一个矩形
			Rectangle rect = new Rectangle(x, y, width, height);
			//提供一个 BufferedImage，将其用作解码像素数据的目标。 
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			//保存新图片 
			ImageIO.write(bi, reader.getFormatName(), new File(toPath));
		} finally {
			if (fis != null)
				fis.close();
			if (iis != null)
				iis.close();
		}
	}

	/**
	 * 质量压缩
	 * compressQuality("d:\\1.JPG","d:\\2.jpg",(float) 0.2);
	 */
	public static void compressQuality(String srcPath, String toPath, float quality) throws Exception {
		File picFile = new File(toPath);//目标文件
		FileOutputStream out = FileUtils.openOutputStream(picFile);
		BufferedImage image = ImageIO.read(new File(srcPath));//源文件
		out.write(bufferedImageTobytes(image, quality));
		out.flush();
		out.close();
	}

	/**
	 * 大小压缩
	 * compressWH("d:\\1.JPG","d:\\3.jpg",415,626);
	 */
	public static void compressWH(String srcPath, String toPath, int weight, int height) throws Exception {
		// 构造Image对象  
		BufferedImage src = ImageIO.read(new File(srcPath));
		// 缩小边长 
		BufferedImage tag = new BufferedImage(weight, height, BufferedImage.TYPE_INT_RGB);
		// 绘制 缩小  后的图片 
		tag.getGraphics().drawImage(src, 0, 0, weight, height, null);
		FileOutputStream out = new FileOutputStream(toPath);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(tag);
		out.close();
	}

	public static void main(String[] args) throws Exception {
		//compressQuality("d:\\1.JPG","d:\\2.jpg",(float) 0.2);
		//compressWH("d:\\1.JPG","d:\\3.jpg",415,626);

		//ImageUtil image = new ImageUtil();
		//image.scale("e:\\tupian\\http_imgload.cgi.jpg", "e:\\false.jpg", 1500, 1600, false);
		//		ImageUtil.addWaterMark("e:\\watermark.png", "e:\\53802.jpg", 100, 100);
		//ImageUtil.pressText("我爱你", "e:\\1.png","", Font.BOLD, Color.RED, 12, 50,10);
	}

}
