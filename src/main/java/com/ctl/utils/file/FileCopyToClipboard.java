package com.ctl.utils.file;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**@author 		guolin
 *  @date    		2015-8-14上午8:36:30
 *  @package_name com.ctl.test
 *  @project_name   Spring3.0企业应用�?��实战
 *  @version 		version.1.0
 */
public class FileCopyToClipboard {

	public static void main(String[] args) {

		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		Transferable contents = new Transferable() {
			DataFlavor[] dataFlavors = new DataFlavor[] { DataFlavor.javaFileListFlavor };

			@Override
			public Object getTransferData(DataFlavor flavor)throws UnsupportedFlavorException, IOException {
				java.util.List<File> files = new ArrayList<File>();
				String filePath=System.getProperty("user.dir")+File.separator+"src"+File.separator+"resources"+ File.separator+"config.properties";
				File file=new File(filePath);
				files.add(file);
				return files;
			}

			@Override
			public DataFlavor[] getTransferDataFlavors() {
				return dataFlavors;
			}

			@Override
			public boolean isDataFlavorSupported(DataFlavor flavor) {
				for (int i = 0; i < dataFlavors.length; i++) {
					if (dataFlavors[i].equals(flavor)) {
						return true;
					}
				}
				return false;
			}
		};

		clipboard.setContents(contents, null);

		//paste("c:\\\\123");
	}

	public static void paste(String dirPath) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable t = clipboard.getContents(null); // 获取粘贴板内数据传输对象
		DataFlavor dataFlavors = DataFlavor.javaFileListFlavor;// 数据对象类型
		if (t.isDataFlavorSupported(dataFlavors)) {// 类型是否匹配为文
			try {
				java.util.List<File> filelist = (	java.util.List<File>) t.getTransferData(dataFlavors);// 拿出粘贴板内文件对象列表
				for (int i = 0; i < filelist.size(); i++) { // 遍历文件列表并复�?
					File file = filelist.get(i);
					File tagFile = new File(dirPath + File.separator
							+ file.getName());
					if (file.getPath().equals(dirPath + file.getName()))
						return;// 判断粘贴板内文件与当前粘贴目录的文件是否存在是一个文�?
					if (tagFile.exists()) {// 文件存在�?
						if (JOptionPane.showConfirmDialog(null,"file exist,cover it?", "Note",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
							tagFile.delete();
							tagFile = new File(dirPath + File.separator+ file.getName());
							copyFolder(file.getAbsolutePath(),tagFile.getAbsolutePath());
						}
					} else {
						copyFolder(file.getAbsolutePath(),tagFile.getAbsolutePath());
					}
				}
			} catch (UnsupportedFlavorException ex) {
			} catch (IOException ex) {
			} catch (Exception e) {
			}
		}
	}


	public static void copyFolder(String sourceFolder, String destinationFolder) throws Exception {
		try {
			if(new File(sourceFolder).isFile()) {
				new File(destinationFolder).getParentFile().mkdirs();
			} else {
				new File(destinationFolder).mkdirs(); // 如果文件夹不存在 则建立新文件�?
			}
			File a = new File(sourceFolder);
			if (a.isFile()) {
				copy(destinationFolder, a,0);//0表示粘贴的是文件
				return;
			}
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (sourceFolder.endsWith(File.separator)) {
					temp = new File(sourceFolder + file[i]);
				} else {
					temp = new File(sourceFolder + File.separator + file[i]);
				}
				if (temp.isFile()) {
					copy(destinationFolder, temp,1);//1表示粘贴的是目录
				}
				if (temp.isDirectory()) {// 如果是子文件�?
					copyFolder(sourceFolder + "/" + file[i], destinationFolder + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			throw new Exception("", e);
		}
	}

	private static void copy(String destinationFolder, File temp,int flag)
			throws FileNotFoundException, IOException {
		FileInputStream input = null;
		FileOutputStream output = null;
		try {
			input = new FileInputStream(temp);
			output = null;
			if (flag == 0) {
				output = new FileOutputStream(destinationFolder);
			} else {
				output = new FileOutputStream(destinationFolder + "/" + (temp.getName()).toString());
			}
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = input.read(b)) != -1) {
				output.write(b, 0, len);
			}
			output.flush();
		} catch (Exception e) {
		} finally {
			output.close();
			input.close();
		}
	}
}
