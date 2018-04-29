package com.ctl.utils;
import java.io.*;
import java.util.Enumeration;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.ZipOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  解压Zip文件工具
 * @author zhangyongbo
 *
 */
public class ZipUtil {
	private static final ReentrantLock lock = new ReentrantLock();
    public static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	private static final int buffer = 2048; 
//	public static void main(String[] args){
//		String str="D:\\sy_2018-01-26033438_48_f.zip";
//        unZip(str);
//	}
	/**
     * 解压Zip文件
     * @param path 文件目录
     */
	public static void unZip(String path){
    	 lock.lock();
    	 int count = -1;
         String savepath = null;
         File file = null;
         InputStream is = null;
         FileOutputStream fos = null;
         BufferedOutputStream bos = null;
         savepath = path.substring(0, path.lastIndexOf(".")) + File.separator; //保存解压文件目录
         new File(savepath).mkdir(); //创建保存目录
         ZipFile zipFile = null;
         try
         {
         	 zipFile = new ZipFile(path,"gbk"); //解决中文乱码问题
             Enumeration<?> entries = zipFile.getEntries();

             while(entries.hasMoreElements())
             {
                 byte buf[] = new byte[buffer];
                 ZipEntry entry = (ZipEntry)entries.nextElement();

                 String filename = entry.getName();
                 boolean ismkdir = false;
                 if(filename.lastIndexOf("/") != -1){ //�?��此文件是否带有文件夹
                 	ismkdir = true;
                 }
                 filename = savepath + filename;
                 if(entry.isDirectory()){ //如果是文件夹先创�?
                 	file = new File(filename);
                 	file.mkdirs();
                 	 continue;
                 }
                 file = new File(filename);
                 if(!file.exists()){ //如果是目录先创建
                 	if(ismkdir){
                 	new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs(); //目录先创
                 	}
                 }
                 file.createNewFile(); //创建文件

                 is = zipFile.getInputStream(entry);
                 fos = new FileOutputStream(file);
                 bos = new BufferedOutputStream(fos, buffer);

                 while((count = is.read(buf)) > -1)
                 {
                     bos.write(buf, 0, count);
                 }
                 
                 bos.flush();
                 bos.close();
                 fos.close();

                 is.close();
                 if(filename.endsWith(".zip")){//如果解压文件包含zip文件那么进入深度解压
                 	 unZip(filename);
                 }
             }
             zipFile.close();
             logger.info(path+" 解压成功");
         }catch(IOException ioe){
        	 logger.error(path+" 解压失败",ioe);
         }finally{
            	try{
            	if(bos != null){
            		bos.close();
            	}
            	if(fos != null) {
            		fos.close();
            	}
            	if(is != null){
            		is.close();
            	}
            	if(zipFile != null){
            		zipFile.close();
            	}
            	}catch(Exception e) {
            		 logger.error("IO Stream close fail:",e);
            	}
            }
         	lock.unlock();
        }
    public static void main(String args[]) throws Exception{
	    // 所有异常抛出
        File file = new File("D:\\upload") ;	// 定义要压缩的文件夹
       // file=new File("d:\\ZipUtil.java");
        File zipFile = new File("D:" + File.separator + "upload.zip") ;	// 定义压缩文件名称
        zip(file,zipFile);

    }
    public static void zip(File file,File zipFile) {
        try {
            logger.info("执行压缩开始");
            InputStream input = null;    // 定义文件输入流
            ZipOutputStream zipOut = null;    // 声明压缩流对象
            zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            zipOut.setComment("http://blog.csdn.net/ctllin");    // 设置注释
            byte readbytes[] = new byte[2048];
            if (file.isDirectory()) {    // 判断是否是文件夹
                File lists[] = file.listFiles();    // 列出全部文件
                int listsLength = lists.length;
                for (int i = 0; i < listsLength; i++) {
                    if (lists[i].isDirectory()) {
                        logger.debug("进入目录" + lists[i]);
                        addZipEntry(zipOut, input, lists[i], file.getName());
                    } else {
                        input = new FileInputStream(lists[i]);    // 定义文件的输入流
                        zipOut.putNextEntry(new ZipEntry(file.getName() + File.separator + lists[i].getName()));    // 设置ZipEntry对象
                        while (input.read(readbytes) != -1) {    // 读取内容
                            zipOut.write(readbytes, 0, readbytes.length);    // 压缩输出
                        }
                        input.close();    // 关闭输入流
                        logger.debug(file.getName() + File.separator + lists[i].getName()+"\t完成压缩");
                    }
                }
            } else if (file.isFile()) {
                input = new FileInputStream(file);    // 定义文件的输入流
                zipOut.putNextEntry(new ZipEntry(file.getName()));
                while (input.read(readbytes) != -1) {    // 读取内容
                    zipOut.write(readbytes, 0, readbytes.length);    // 压缩输出
                }
                input.close();    // 关闭输入流
                logger.debug(file.getName()+"\t完成压缩");
            }
            zipOut.close();    // 关闭输出流
        } catch (Exception e) {
            logger.error("压缩文件失败",e);
        } finally {
            logger.info("执行压缩结束");
        }
    }
    private static void addZipEntry(ZipOutputStream zipOut,InputStream input,File file,String fileParentName) throws Exception {
        if (file.isDirectory()) {
            File lists[] = file.listFiles();    // 列出全部文件
            byte readbytes[] = new byte[2048];
            for (int i = 0; i < lists.length; i++) {
                if (lists[i].isDirectory()) {
                    logger.debug("进入目录" + lists[i]);
                    addZipEntry(zipOut, input, lists[i], fileParentName + File.separator + file.getName());
                } else {
                    input = new FileInputStream(lists[i]);    // 定义文件的输入流
                    zipOut.putNextEntry(new ZipEntry(fileParentName + File.separator + file.getName() + File.separator + lists[i].getName()));    // 设置ZipEntry对象
                    while (input.read(readbytes) != -1) {    // 读取内容
                        zipOut.write(readbytes, 0, readbytes.length);    // 压缩输出
                    }
                    input.close();    // 关闭输入流
                    logger.debug(fileParentName + File.separator + file.getName() + File.separator + lists[i].getName()+"\t完成压缩");
                }
            }
        }
    }
}