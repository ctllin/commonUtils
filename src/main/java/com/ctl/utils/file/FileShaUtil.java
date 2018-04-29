package com.ctl.utils.file;

import com.ctl.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class FileShaUtil {
	static Logger logger = LoggerFactory.getLogger(FileShaUtil.class); 

	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6','7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	public static MessageDigest messagedigest = null;
	public static void main(String[] args) throws OutOfMemoryError, NoSuchAlgorithmException, IOException {
		File file=new File("d:\\she.mp4");
		System.out.println(FileCRC32Util.getCRC32(file));
		System.out.println(FileMD5Util.getMd5ByFile(file));
		System.out.println(FileShaUtil.getSha1(file));
		
	}


	/***
	 * 计算SHA1码
	 * 
	 * @return String 适用于上G大的文件
	 * @throws NoSuchAlgorithmException
	 * */
	public static String getSha1(File file){
		long fileLength=file.length();
        logger.info("文件大小为:"+fileLength+"\t最后修改时间："+ DateUtil.sdfyyyy_MM_dd_HH_mm_ss.format(new Date(file.lastModified())));
		FileInputStream in=null;
		try {
			messagedigest = MessageDigest.getInstance("SHA-1");
			in= new FileInputStream(file);
			FileChannel ch = in.getChannel();
			MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,file.length());
			messagedigest.update(byteBuffer);
			return bufferToHex(messagedigest.digest()).toUpperCase();
		} catch (Exception e) {
			logger.error("计算SHA1码失败",e);
			return null;
		}finally{
			try {
				in.close();
			} catch (Exception e2) {
				logger.error("关闭Io流失败",e2);
			}
		}
	}


	public static String getMD5String(String s) {
		return getMD5String(s.getBytes());
	}

	public static String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

	/**
	 * @Description 计算二进制数据
	 * @return String
	 * */
	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}
}