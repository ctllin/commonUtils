package com.ctl.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Date;

import com.ctl.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileMD5Util {
	 static Logger logger = LoggerFactory.getLogger(FileMD5Util.class); 
	 public static String getMd5ByFile(File file) throws FileNotFoundException {
         String value = null;
         long fileLength=file.length();
         logger.info("文件大小为:"+fileLength+"\t最后修改时间："+ DateUtil.sdfyyyy_MM_dd_HH_mm_ss.format(new Date(file.lastModified())));
         FileInputStream in = new FileInputStream(file);
         try {
        	 MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        	 MessageDigest md5 = MessageDigest.getInstance("MD5");
        	 md5.update(byteBuffer);
        	 BigInteger bi = new BigInteger(1, md5.digest());
        	 value = bi.toString(16);
         } catch (Exception e) {
        	 logger.error("获取文件md5值失败(16)",e);
         } finally {
        	 if(null != in) {
        		 try {
        			 in.close();
        		 } catch (IOException e) {
        			 logger.error("io输入流关闭失败",e);
        		 }
        	 }
         }
         return value.toUpperCase();
	 }
	 
	public static void main(String[] args) throws IOException {
		String path="d:\\影视帝国(bbs.cnxp.com).监守自盗.Inside.Job.2010.bluray.720p.rmvb";
		 path="d:\\she.mp4";
		String v = getMd5ByFile(new File(path));
		System.out.println("MD5:"+v);
	}

}

