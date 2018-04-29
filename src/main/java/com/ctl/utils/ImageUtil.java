package com.ctl.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.util.Scanner;

public class ImageUtil {
	static BASE64Encoder encoder = new BASE64Encoder();
	static BASE64Decoder decoder = new BASE64Decoder();
	static Logger logger = LoggerFactory.getLogger(ImageUtil.class);  
	public static void main(String[] args) throws FileNotFoundException, IOException {

		// 测试从图片文件转换为Base64编码
		//System.out.println(GetImageStr("d:\\wangyc.jpg"));
	//	base64StringToImage("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");
	  Scanner scanner=new Scanner(new File("D:\\1"));
	  while(scanner.hasNext()){
		  base64StringToImage(scanner.nextLine());
	  }
	  scanner.close();
	}

	
	public static void base64StringToImage(String imageBase64Str) {
		 base64StringToImage(imageBase64Str,"jpg","D:\\out.jpg");
	}
	/**
	 * 
	 * @param imageBase64Str  base64 字符串
	 * @param suffix  图片格式  jgp  png
	 * @param fileCreatePath  图片生成路径
	 */
	public static void base64StringToImage(String imageBase64Str,String suffix,String fileCreatePath) {
		try {
			BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(imageBase64Str)));
			if (null == bufImg) {
				return;
			} else {
				ImageIO.write(bufImg, suffix, new File(fileCreatePath));
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String imageToBase64String(String filePath) throws FileNotFoundException, IOException{
		InputStream input=new BufferedInputStream(new FileInputStream(new File(filePath)));
		byte bytes[]=new byte[input.available()];
		input.read(bytes);
		input.close();
		return new String(new BASE64Encoder().encode(bytes));
	}
	public static String encodeImgageToBase64(File imageFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		ByteArrayOutputStream outputStream = null;
		try {
			BufferedImage bufferedImage = ImageIO.read(imageFile);
			outputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "jpg", outputStream);
		} catch (MalformedURLException e1) {
			logger.error("error",e1);
		} catch (IOException e) {
			logger.error("error",e);
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(outputStream.toByteArray());// 返回Base64编码过的字节数组字符串
	}


}