package com.ctl.utils.crpty.imp;

import java.io.*;
import java.security.SecureRandom;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;

import com.ctl.utils.crpty.dao.CryptDao;
import com.ctl.utils.file.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CrptyImp implements CryptDao {
	public static Logger logger = LoggerFactory.getLogger(CrptyImp.class);
	//是否已经加密标识，放在最后
	private static final String CRPTYFLAG = "crpty1851528713900";
	//加密标识长度
	private static final int CRPTYFLAGLENGTH = CRPTYFLAG.length();
	//加密密匙所在路径
	private static final String KEYPATH = System.getProperty("user.dir")
			+ File.separator + "key.dat";

	/**
	 * 不加密过滤条件
	 *
	 * @param filePath
	 *            文件路径 例如D:\org\ctl\test\Test.class
	 * @return
	 */
	public boolean unCrptyFilter(String filePath) {
		if (       filePath.endsWith("Dao.class")
				|| filePath.endsWith(".xml")
				|| filePath.endsWith(".dat")
				|| filePath.endsWith(".txt")
				|| filePath.endsWith(".properties")
				|| filePath.endsWith("Bean.class")
				|| filePath.endsWith("Test.class")
				|| filePath.endsWith("Utils.class")
				|| filePath.endsWith("CrptyImp.class")
				|| filePath.endsWith("DecryptImp.class")
				|| filePath.endsWith("MyClassLoaderImp.class")) {
			return true;
		}
		return false;
	}

	/**
	 * 加密当前项目除了满足UnCrptyFilter条件的所有文件
	 */
	public void crypt() {
		String classRootPath =System.getProperty("user.dir");
		List<String> allClassPath = new ArrayList<String>();
		allClassPath = FileUtil.listAllFlies(classRootPath, true, allClassPath);
		SecureRandom sr = null;
		FileInputStream fi = null;
		byte rawKeyData[] = null;
		try {
			sr = new SecureRandom();
			fi = new FileInputStream(KEYPATH);
			rawKeyData = new byte[fi.available()];
			fi.read(rawKeyData);
			fi.close();

		} catch (Exception e) {
			logger.info("读取密匙失败！" + Arrays.deepToString(e.getStackTrace()));
		}
		for (int i = 0; i < allClassPath.size(); i++) {
			try {
				if (unCrptyFilter(allClassPath.get(i))) {
					//	logger.info("过滤文件:"+allClassPath.get(i));
					continue;
				}
				DESKeySpec dks = new DESKeySpec(rawKeyData);
				SecretKey key = SecretKeyFactory.getInstance("DES")
						.generateSecret(dks);
				Cipher cipher = Cipher.getInstance("DES");
				cipher.init(Cipher.ENCRYPT_MODE, key, sr);
				FileInputStream fi2 = new FileInputStream(new File(
						allClassPath.get(i)));
				byte data[] = new byte[fi2.available()];
				fi2.read(data);
				fi2.close();
				if (new String(data, data.length - CRPTYFLAGLENGTH,
						CRPTYFLAGLENGTH).equals(CRPTYFLAG)) {
					logger.info(allClassPath.get(i)+"已经加密过了");
					continue;
				}
				byte encryptedData[] = cipher.doFinal(data);
				FileOutputStream fo = new FileOutputStream(new File(
						allClassPath.get(i)));
				fo.write(encryptedData);
				fo.write(CRPTYFLAG.getBytes());
				fo.close();
				logger.info(allClassPath.get(i)+"加密成功!");
			} catch (Exception e) {
				logger.info("加密【"+allClassPath.get(i)+"】文件失败！"+Arrays.deepToString(e.getStackTrace()));
			}
		}
	}

	public void cryptFilePath(String filePath) {
		if (unCrptyFilter(filePath)) {
			return;
		}
		SecureRandom sr = null;
		FileInputStream fi = null;
		byte rawKeyData[] = null;
		try {
			sr = new SecureRandom();
			fi = new FileInputStream(new File(KEYPATH));
			rawKeyData = new byte[fi.available()];
			fi.read(rawKeyData);
			fi.close();
			DESKeySpec dks = new DESKeySpec(rawKeyData);
			SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(
					dks);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key, sr);
			FileInputStream fi2 = new FileInputStream(new File(filePath));
			byte data[] = new byte[fi2.available()];
			fi2.read(data);
			fi2.close();
			if (new String(data, data.length - CRPTYFLAGLENGTH, CRPTYFLAGLENGTH)
					.equals(CRPTYFLAG)) {
				logger.info(filePath+"已经加密过了");
				return;
			}
			byte encryptedData[] = cipher.doFinal(data);
			FileOutputStream fo = new FileOutputStream(new File(filePath));
			fo.write(encryptedData);
			fo.write(CRPTYFLAG.getBytes());
			fo.close();
		} catch (Exception e) {
			logger.info("加密【"+filePath+"】文件失败！"+Arrays.deepToString(e.getStackTrace()));
		}
	}

	public void cryptDircotryPath(String dirPath) {

		String classRootPath = dirPath;
		List<String> allClassPath = new ArrayList<String>();
		allClassPath = FileUtil.listAllFlies(classRootPath, true, allClassPath);
		logger.info("class root dirctory is:" + classRootPath);
		SecureRandom sr = null;
		FileInputStream fi = null;
		byte rawKeyData[] = null;
		try {
			sr = new SecureRandom();
			fi = new FileInputStream(new File(KEYPATH));
			rawKeyData = new byte[fi.available()];
			fi.read(rawKeyData);
			fi.close();

		} catch (Exception e) {
			logger.info("读取密匙失败！"+Arrays.deepToString(e.getStackTrace()));
			return;
		}

		for (int i = 0; i < allClassPath.size(); i++) {
			try {
				if (unCrptyFilter(allClassPath.get(i))) {
					//logger.info("过滤文件:"+allClassPath.get(i));
					continue;
				}
				DESKeySpec dks = new DESKeySpec(rawKeyData);
				SecretKey key = SecretKeyFactory.getInstance("DES")
						.generateSecret(dks);
				Cipher cipher = Cipher.getInstance("DES");
				cipher.init(Cipher.ENCRYPT_MODE, key, sr);
				FileInputStream fi2 = new FileInputStream(new File(
						allClassPath.get(i)));
				byte data[] = new byte[fi2.available()];
				fi2.read(data);
				fi2.close();
				if (new String(data, data.length - CRPTYFLAGLENGTH,
						CRPTYFLAGLENGTH).equals(CRPTYFLAG)) {
					logger.info(allClassPath.get(i)+"已经加密过了");
					continue;
				}
				byte encryptedData[] = cipher.doFinal(data);
				FileOutputStream fo = new FileOutputStream(new File(
						allClassPath.get(i)));
				fo.write(encryptedData);
				fo.write(CRPTYFLAG.getBytes());
				fo.close();
			} catch (Exception e) {
				logger.info("加密【"+allClassPath.get(i)+"】文件失败！"+Arrays.deepToString(e.getStackTrace()));
			}
		}
	}

	public static void main(String[] args) {
		new CrptyImp().crypt();
	}
}
