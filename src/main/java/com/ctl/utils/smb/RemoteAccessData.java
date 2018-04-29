package com.ctl.utils.smb;

/**@author 		guolin
 *  @date    		2016-5-4下午6:25:50
 *  @package_name com.ctl.test
 *  @project_name   Aa权限
 *  @version 		version.1.0
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class RemoteAccessData {

	/**
	 * @param args
	 * @throws IOException
	 */

	// [share] #指定目录的共享名称
	// #path=/home/soft/share #指定共享share这个目录
	// path=/opt/IBM/WebSphere/AppServer/profiles
	// valid users=ctl,root,db2inst1
	// public= no #public=guest ok，设为yes允许匿名用户访问
	// #browseable= yes #让匿名用户可以看到共享内容
	// writable= yes #设置可以有写入的权限
	// #createmask = 0644 #设置新建文件的权限，表示属主具有读写权限，其他人都只有只读权限（rw-r--r--）
	// #directory mask = 0755 #设置新建目录的权限，表示属主具有读写执行权限，其他人没有写入权限（rwxr-xr-x）
	//需要在redhat 执行setenforce 0，切将139和445设置为可访问

	//smbclient -L 192.168.42.7 -U ctl
	//smbpasswd -a root
	//smbclient //192.168.42.7/share -U root
	//
	//遇到下面两个问题  关闭SELinux，先使用getenforce查看状态，如果是Enforcing，就使用setenforce 0关闭
	//问题一jcifs.smb.SmbAuthException: Access is denied
	//问题二Samba配置共享，出现NT_STATUS_ACCESS_DENIED listing \*

	// /etc/samba/smb.conf 配置内容如下
	//	[share]
	//	comment = Share Directories
	//	path = /share
	//			browseable = yes
	//	writeable = yes
	//	valid users=ctl,root
	//service smb start 启动服务
	//smbpasswd -a root 添加用户
	public static void main(String[] args) throws IOException {
		// smbGet1("smb://192.168.75.204/test/新建 文本文档.txt");
		// smbGet("smb://ctl:ctllin@192.168.42.19/share/product_StartMenu.log","e:/");
		smbPut("smb://root:ctllin@192.168.42.7/share/", "D:" + File.separator+ "1.txt");
	}

	/**
	 * 方法一：
	 *
	 * @param remoteUrl
	 *            远程路径 smb://192.168.75.204/test/新建 文本文档.txt
	 * @throws IOException
	 */
	public static void smbGet1(String remoteUrl) throws IOException {
		SmbFile smbFile = new SmbFile(remoteUrl);
		int length = smbFile.getContentLength();// 得到文件的大小
		byte buffer[] = new byte[length];
		SmbFileInputStream in = new SmbFileInputStream(smbFile);
		// 建立smb文件输入流
		while ((in.read(buffer)) != -1) {

			System.out.write(buffer);
			System.out.println(buffer.length);
		}
		in.close();
	}

	// 从共享目录下载文件
	/**
	 * 方法二： 路径格式：smb://192.168.75.204/test/新建 文本文档.txt
	 * smb://username:password@192.168.0.77/test
	 *
	 * @param remoteUrl
	 *            远程路径
	 * @param localDir
	 *            要写入的本地路径
	 */
	public static void smbGet(String remoteUrl, String localDir) {
		InputStream in = null;
		OutputStream out = null;
		try {
			SmbFile remoteFile = new SmbFile(remoteUrl);
			if (remoteFile == null) {
				System.out.println("共享文件不存在");
				return;
			}
			String fileName = remoteFile.getName();
			File localFile = new File(localDir + File.separator + fileName);
			in = new BufferedInputStream(new SmbFileInputStream(remoteFile));
			out = new BufferedOutputStream(new FileOutputStream(localFile));
			byte[] buffer = new byte[1024];
			while (in.read(buffer) != -1) {
				out.write(buffer);
				buffer = new byte[1024];
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 向共享目录上传文件
	public static void smbPut(String remoteUrl, String localFilePath) {
		InputStream in = null;
		OutputStream out = null;
		try {
			File localFile = new File(localFilePath);

			String fileName = localFile.getName();
			SmbFile remoteFile = new SmbFile(remoteUrl + "/" + fileName);
			in = new BufferedInputStream(new FileInputStream(localFile));
			SmbFile file=new SmbFile(remoteUrl+"/smb");
			if(file.isDirectory()&&file.exists()){
				System.out.println("file.exists()");
			}else{
				System.out.println("file.mkdirs()");
				file.mkdirs();
			}
			String filepath = remoteUrl + "/" + fileName;
			SmbFile smbFileOut = new SmbFile(filepath);
			if (!smbFileOut.exists())
				smbFileOut.createNewFile();

			out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));
			byte[] buffer = new byte[1024];
			while (in.read(buffer) != -1) {
				out.write(buffer);
				buffer = new byte[1024];
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 远程url smb://192.168.0.77/test
	// 如果需要用户名密码就这样：
	// smb://username:password@192.168.0.77/test

}
