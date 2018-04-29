package com.ctl.utils.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
/**
 * @author \u90ED\u6797(ctl)
 * @category \u6587\u4EF6\u64CD\u4F5C
 */
public class FileUtil {
	static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	// byte\u6570\u7EC4\u7684\u957F\u5EA6\uFF0C\u6BCF\u6B21\u8BFB\u5199\u7684\u4F4D\u6570
	private static int len = 1024;
	/**
	 * @param pathToCopy
	 *        \u88AB\u62F7\u8D1D\u7684\u6587\u4EF6\u8DEF\u5F84\u548C\u6587\u4EF6\u540D
	 *        \u4F8B\u5982H:%%temp%%test1.txt
	 * @param pathCopyTo \u5C06\u6587\u4EF6\u590D\u5236\u5230\u54EA\u91CC\u7684
	 * 
	 */

	/**
	 * @deprecated \u590D\u5236\u6587\u4EF6
	 * @param pathToCopy
	 * @param pathCopyTo
	 * @throws IOException
	 */
	public static void copyFile(String pathToCopy, String pathCopyTo) throws IOException {
		File toCopy = new File(pathToCopy);
		File copyTo = new File(pathCopyTo);
		boolean b_toCopy = toCopy.isFile();
		boolean b_copyTo = copyTo.isFile();
		if (!b_toCopy) {
			logger.info("copyFile \u8981\u590D\u5236\u7684\u6E90\u6587\u4EF6\u4E0D\u5B58\u5728");
			return;
		}
		if (!b_copyTo) {
			logger.info("pathCopyTo \u76EE\u6807\u6587\u4EF6\u4E0D\u5B58\u5728");
			logger.info("pathCopyTocreate \u76EE\u6807\u6587\u4EF6");
			File file = new File(copyTo.getParent());
			file.mkdirs();
			file = new File(pathCopyTo);
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		int length = (int) toCopy.length();
		byte[] buf = new byte[len];
		try {
			InputStream in = new FileInputStream(toCopy);
			BufferedInputStream inBuf = new BufferedInputStream(in);

			OutputStream out = new FileOutputStream(copyTo, true);
			BufferedOutputStream outBuf = new BufferedOutputStream(out);
			int read = 1;
			int offset = 0;
			System.out.println(new String(buf));
			while (read > 0) {
				try {
					read = inBuf.read(buf);
					outBuf.write(buf);
					outBuf.flush();
					read = in.read(buf);
					System.out.println(new String(buf));
					System.out.println(read);
				} catch (Exception e) {
				}
			}
			try {
				in.close();
				out.flush();
				out.close();
				inBuf.close();
				out.flush();
				outBuf.close();
			} catch (Exception e) {
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println(toCopy.isFile());
		System.out.println(copyTo.isFile());
	}


	/**
	 * @deprecated 
	 *             \u8BE5\u65B9\u6CD5\u7528\u6765\u5411\u6307\u5B9A\u7684\u6587\u4EF6\u6309\u67D0\u79CD\u7F16\u7801\u5199\u5165
	 * @param filePath \u8981\u5199\u5165\u7684\u6587\u4EF6\u8DEF\u5F84
	 * @param code \u8981\u91C7\u53D6\u54EA\u79CD\u7F16\u7801\u5199\u5165
	 */
	public static void writeToFile(String strToWrite, String filePath,String code) {
		File file = new File(filePath);
		File f = new File(file.getParent());
		if (!f.isDirectory()) {
			System.out
					.println("\u8DEF\u5F84\u4E0D\u5B58\u5728\u521B\u5EFA\u76EE\u5F55");
			f.mkdirs();
			try {
				System.out
						.println("\u8DEF\u5F84\u4E0D\u5B58\u5728\u521B\u5EFA\u6587\u4EF6");
				file.createNewFile();
			} catch (IOException e) {
				System.err
						.println("\u6587\u4EF6\u4E0D\u5B58\u5728\uFF0C\u521B\u5EFA\u6587\u4EF6\u5931\u8D25");
				e.printStackTrace();
			}
		} else {
			if (!file.exists()) {

				logger.info("writeToFile \u6587\u4EF6\u4E0D\u5B58\u5728\u521B\u5EFA\u6587\u4EF6");
				try {
					file.createNewFile();
				} catch (IOException e) {
					logger.info("writeToFile \u6587\u4EF6\u4E0D\u5B58\u5728\u521B\u5EFA\u6587\u4EF6\u5931\u8D25");
					e.printStackTrace();
				}
			}
		}
		try {
			OutputStream out = new FileOutputStream(filePath, true);
			byte buf[] = new byte[strToWrite.length()];
			try {
				buf = strToWrite.getBytes(code);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			try {
				out.write(buf);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @deprecated \u6309\u67D0\u79CD\u7F16\u7801\u8BFB\u53D6\u6587\u4EF6
	 * @param filePath \u8981\u8BFB\u53D6\u7684\u6587\u4EF6\u8DEF\u5F84
	 * @param code
	 *        \u8981\u4F7F\u7528\u54EA\u79CD\u7F16\u7801\u8FDB\u884C\u8BFB\u53D6
	 */
	public static void readFromFile(String filePath, String code) {
		File file = new File(filePath);
		File f = new File(file.getParent());
		if (!f.isDirectory()) {
			System.out.println("\u8DEF\u5F84\u4E0D\u5B58");
			return;
		}
		if (!file.exists()) {
			logger.info("ReadFromFile\u6587\u4EF6\u4E0D\u5B58\u5728");
			return;
		}
		try {
			Reader reader = null;
			reader = new InputStreamReader(new FileInputStream(file), code); // \u5C06\u5B57\u8282\u6D41\u53D8\u4E3A\u5B57\u7B26\u6D41
			char c[] = new char[1024];
			int len = reader.read(c); // \u8BFB\u53D6
			while (len != -1) {
				// System.out.println("\u3010"+len+"\u3011");
				System.out.println(new String(c, 0, len));
				len = reader.read(c);
			}
			reader.close(); // \u5173\u95ED
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * @deprecated \u6309\u67D0\u79CD\u7F16\u7801\u8BFB\u53D6\u6587\u4EF6
	 * @param filePath \u8981\u8BFB\u53D6\u7684\u6587\u4EF6\u8DEF\u5F84
	 * @param code
	 *        \u8981\u4F7F\u7528\u54EA\u79CD\u7F16\u7801\u8FDB\u884C\u8BFB\u53D6
	 */
	public static void readFromFile2(String filePath, String code) {
		File file = new File(filePath);
		File f = new File(file.getParent());
		if (!f.isDirectory()) {
			System.out.println("\u8DEF\u5F84\u4E0D\u5B58");
			return;
		}
		if (!file.exists()) {
			logger.info("readFromFile2\u6587\u4EF6\u4E0D\u5B58\u5728");
			return;
		}
		try {
			InputStream in = new FileInputStream(file);
			int len = 0;
			byte buffer[] = new byte[1024];
			while ((len = in.read(buffer)) != -1) {
				System.out.println(new String(buffer, code).trim());
			}
			in.close(); // \u5173\u95ED
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	// /**
	// *
	// * @param path \u8981\u5220\u9664\u6587\u4EF6\u7684\u8DEF\u5F84
	// * @param endWithStr \u6587\u4EF6\u4EE5\u4EC0\u4E48\u7ED3\u5C3E
	// * @param childDirDelete
	// \u662F\u5426\u5220\u9664\u5B50\u6587\u4EF6\u4E2D\u7684\u6587\u4EF6
	// * @category for example "H:/ctl.bak", false
	// */
	// public static void deleteFileEndWith(String filePath, String endWithStr,
	// boolean childDirDelete) {
	// LogUtil log = new LogUtil();
	// File file = new File(filePath);
	// // File f = new File(file.getParent());
	// if (!file.isDirectory()) {
	// System.out.println("\u8DEF\u5F84\u4E0D\u5B58");
	// return;
	// }
	// String[] fileList = file.list();
	// for (String s : fileList) {
	// System.out.println(s);
	// if (s.endsWith(endWithStr)) {
	// String file2 = new File(filePath).getAbsolutePath()
	// + File.separator + s;
	// deleteFile(file2);
	// }
	// }
	// }
	//

	/**
	 * 
	 * @param path
	 * @param fileName
	 */
	public static void deleteFile2(String path, String... fileName) {
		// System.out.println(path + fileName[0]);
		for (int i = 0; i < fileName.length; i++) {
			String filePath = path + File.separator + fileName[i];
			// System.out.println(filePath);
			File file = new File(filePath);
			File f = new File(file.getParent());
			if (!f.isDirectory()) {
				return;
			} else {
				if (!file.exists()) {
					return;
				} else {
					file.delete();
				}
			}
		}
	}

	/**
	 * @category 
	 *           \u904D\u5386filePath\u76EE\u5F55\u4E0B\u7684\u6240\u6709\u6587\u4EF6
	 * @param filePath
	 * @param childDir \u662F\u5426\u904D\u5386\u5B50\u76EE\u5F55
	 * @param listAllFlies
	 *        \u9700\u8981\u8C03\u7528\u5904\u521B\u5EFA\u65B0\u7684new
	 *        ArrayList \u7136\u540E\u4F20\u5165
	 */
	public static List<String> listAllFlies(String filePath, boolean childDir,
			List<String> listAllFlies) {
		File file = new File(filePath);
		// File f = new File(file.getParent());
		// System.out.println(file);
		if (!file.isDirectory()) {
			return listAllFlies;
		} else {
			String fileList[] = file.list();
			for (String s : fileList) {
				// System.out.println(s);
				File file1 = new File(file + File.separator + s);
				if (file1.isDirectory() && childDir) {
					listAllFlies(file + File.separator + s, childDir,
							listAllFlies);
				} else {
					listAllFlies.add(file + File.separator + s);
					// System.out.println(file + File.separator + s);
				}
			}
		}
		return listAllFlies;
	}

	/**
	 * @description \u8FD4\u56DEmap \u53EF\u4EE5\u662F\u6587\u4EF6\u540D
	 *              value\u662F\u8DEF\u5F84
	 * @param filePath \u6587\u4EF6\u8DEF\u5F84
	 * @param childDir \u662F\u5426\u5305\u542B\u5B57\u6587\u4EF6\u5939
	 * @return
	 */
	public static Map<String, String> getAllFileNameAndFilePathMap(
			String filePath, boolean childDir,
			Map<String, String> mapAllFileNameAndFilePath) {
		File file = new File(filePath);
		// File f = new File(file.getParent());
		// System.out.println(file);
		if (!file.isDirectory()) {
			return mapAllFileNameAndFilePath;
		} else {
			String fileList[] = file.list();
			for (String s : fileList) {
				// System.out.println(s);
				File file1 = new File(file + File.separator + s);
				if (file1.isDirectory() && childDir) {
					getAllFileNameAndFilePathMap(file + File.separator + s,
							childDir, mapAllFileNameAndFilePath);
				} else {
					// System.out.println(file);
					mapAllFileNameAndFilePath.put(s, file.toString());
				}
			}
		}
		return mapAllFileNameAndFilePath;
	}

	/**
	 * 
	 * @param filePath
	 * @param childDirDelete
	 *        \u662F\u5426\u5220\u9664\u5B50\u6587\u4EF6\u4E2D\u7684\u6587\u4EF6
	 * @param fileName FileUtil.deleteFile2("H:/ctl",false,
	 *        "tcp.properties","build.xml");
	 */
	public static void deleteFile2(String filePath, boolean childDirDelete,
			String... fileName) {
		File file = new File(filePath);
		if (!file.isDirectory()) {
			return;
		} else {
			String fileList[] = file.list();
			for (String s : fileList) {
				File file1 = new File(file + File.separator + s);
				if (file1.isDirectory() && childDirDelete) {
					deleteFile2(file + File.separator + s, childDirDelete,
							fileName);
				} else {
					String str = file + File.separator + s;
					File file2 = new File(file + File.separator + s);
					for (int i = 0; i < fileName.length; i++) {
						if (str.endsWith(fileName[i])) {
							file2.delete();
						}
					}
				}
			}
		}

	}

	// /**
	// *
	// * @param path
	// * \u8981\u5220\u9664\u6587\u4EF6\u7684\u8DEF\u5F84
	// * @param endWithStr
	// * \u6587\u4EF6\u4EE5\u4EC0\u4E48\u7ED3\u5C3E
	// * @param childDirDelete
	// * \u662F\u5426\u5220\u9664\u5B50\u6587\u4EF6\u4E2D\u7684\u6587\u4EF6
	// * @category example "H:/ctl.bak", false
	// */
	// public static void deleteFileEndWith(String filePath, String endWithStr,
	// boolean childDirDelete) {
	// LogUtil log = new LogUtil();
	// File file = new File(filePath);
	// // File f = new File(file.getParent());
	// if (!file.isDirectory()) {
	// System.out.println("\u8DEF\u5F84\u4E0D\u5B58");
	// return;
	// }
	// String[] fileList = file.list();
	// for (String s : fileList) {
	// System.out.println(s);
	// if (s.endsWith(endWithStr)) {
	// String file2 = new File(filePath).getAbsolutePath()
	// + File.separator + s;
	// deleteFile(file2);
	// }
	// }
	// }

	/**
	 * 
	 * @param filePath for example "C:/test.txt"
	 */
	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		File f = new File(file.getParent());
		if (!f.isDirectory()) {
			return;
		} else {
			if (!file.exists()) {
				return;
			} else {
				file.delete();
			}
		}
	}

	/**
	 * @category \u5220\u9664\u540E\u7F00\u4E3AendWithStr\u7684\u6587\u4EF6
	 * @param filePath
	 * @param childDirDelete \u662F\u5426\u5220\u9664\u5B57\u5B50\u76EE\u5F55
	 * @param endWithStr
	 */
	public static void deleteFileEndWith(String filePath,
			boolean childDirDelete, String... endWithStr) {
		File file = new File(filePath);
		if (!file.isDirectory()) {
			return;
		} else {
			String fileList[] = file.list();
			for (String s : fileList) {
				File file1 = new File(file + File.separator + s);

				if (file1.isDirectory() && childDirDelete) {
					deleteFileEndWith(file + File.separator + s,
							childDirDelete, endWithStr);

				} else {
					String str = file + File.separator + s;
					File file2 = new File(file + File.separator + s);
					for (int i = 0; i < endWithStr.length; i++) {
						if (str.endsWith(endWithStr[i])) {
							file2.delete();
						}
					}
				}
			}
		}

	}

	/**
	 * @category param path for exemple E:%JavaWeb%TCPClientUtils%src1
	 */
	public static void deleteDir(String filePath) {
		File file = new File(filePath);
		if (!file.isDirectory()) {
			return;
		} else {
			String fileList[] = file.list();
			for (String s : fileList) {
				String strPath = file + File.separator + s;
				File file1 = new File(strPath);
				if (file1.isDirectory()) {
					deleteDir(strPath);
					file1.delete();
				} else {
					String str = strPath;
					File file2 = new File(strPath);
					file2.delete();
				}
			}
			file.delete();
		}
	}

	public static void listAllFliesAndDir(String filePath, boolean childDir) {
		File file = new File(filePath);
		// File f = new File(file.getParent());
		// System.out.println(file);
		if (!file.isDirectory()) {
			return;
		} else {
			String fileList[] = file.list();
			for (String s : fileList) {
				// System.out.println(file + File.separator + s);
				File file1 = new File(file + File.separator + s);
				if (file1.isFile()) {
					System.out.println("\u6587\u4EF6:" + file + File.separator
							+ s);
					continue;
				}
				if (file1.isDirectory() && childDir && file1.list().length > 0) {
					System.out.println("\u76EE\u5F55:" + file + File.separator
							+ s);
					listAllFliesAndDir(file + File.separator + s, childDir);
				}
				if (file1.isDirectory() && childDir && file1.list().length == 0) {
					System.out.println("\u76EE\u5F55:" + file + File.separator
							+ s);
				}
				// if(file1.isDirectory()&&file1.list().length==0){
				// System.out.println(file + File.separator + s);
				// }
			}
		}
	}

	/**
	 * \u6309\u5B57\u8282\u8BFB\u53D6\u6587\u4EF6\u7528\u4E8E\u52A0\u5BC6\u6587\u4EF6
	 * 
	 * @param file
	 */
	public static void readFile(String file) {
		FileReader fr=null;
		try {
			fr= new FileReader(file);
			int ch = 0;
			while ((ch = fr.read()) != -1) {
				System.out.print((char) (ch - 0630));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fr!=null){
				try {
					fr.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * \u5224\u65AD\u6587\u4EF6\u7684\u7F16\u7801\u683C\u5F0F
	 * \u652F\u6301UTF-8,Unicode,UTF-16BE,ANSI|ASCII,GBK
	 * 
	 * @param fileName :file
	 * @return \u6587\u4EF6\u7F16\u7801\u683C\u5F0F
	 * @throws Exception
	 */
	public static String getEncodeString(String fileName) throws Exception {
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(
				fileName));
		int p = (bin.read() << 8) + bin.read();
		String code = null;
		System.out.println(p);
		// \u5176\u4E2D\u7684
		// 0xefbb\u30010xfffe\u30010xfeff\u30010x5c75\u8FD9\u4E9B\u90FD\u662F\u8FD9\u4E2A\u6587\u4EF6\u7684\u524D\u9762\u4E24\u4E2A\u5B57\u8282\u768416\u8FDB\u5236\u6570
		switch (p) {
		case 28769:
			code = "UTF-8";
			break;
		case 0xefbb:
			code = "UTF-8";
			break;
		case 8420:
			code = "UTF-8";
			break;
		case 59778:
			code = "UTF-8";
			break;
		case 0xfffe:
			code = "Unicode";
			break;
		case 0xfeff:
			code = "UTF-16BE";
			break;
		case 0x5c75:
			code = "ANSI|ASCII";
			break;
		case 8205:
			code = "GBK";
			break;
		case 8381:
			code = "ANSI";
			break;
		default:
			code = "GBK";
		}
	//	System.out.println("銆�+p+"銆�"+code + "  " + fileName);
		bin.close();
		return code;
	}



	public static void findLineContainStr(String filePath,String str) {
		try {
			FileReader fr = new FileReader( filePath);
			BufferedReader br = new BufferedReader(fr);
			String readStr;
			while ((readStr = br.readLine()) != null) {
				if(readStr.contains(str)||readStr.contains(str.toLowerCase())||readStr.toLowerCase().contains(str.toLowerCase()))
				logger.info(readStr);
			}
			fr.close();
			fr.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		List<String> listAllFlies =new ArrayList<String>();
		listAllFlies(".",false,listAllFlies);
		for (int i = 0; i < listAllFlies.size(); i++) {
			System.out.println(listAllFlies.get(i));
		}
		// listAllFlie'sAndDir(".", true);
		// System.out.println(getFilePath(".pro.properties", true));
		// readFile("H:/test.java");
		//getEncodeString("E:/JavaWeb/Tarena/src2/demo20130610/test.txt");
	}
}
