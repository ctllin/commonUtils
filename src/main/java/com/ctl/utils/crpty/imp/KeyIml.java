package com.ctl.utils.crpty.imp;


import java.io.File;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.ctl.utils.crpty.dao.KeyDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("static-access")
public class KeyIml implements KeyDao {
	public static Logger logger = LoggerFactory.getLogger(KeyIml.class);
	public String keyName;

	public KeyIml() {
	}

	public KeyIml(String keyName) {
		this.keyName = keyName;
	}


	public void createKey(String keyName) throws Exception {
		SecureRandom sr = new SecureRandom();
		KeyGenerator kg = KeyGenerator.getInstance("DES");
		kg.init(sr);
		SecretKey key = kg.generateKey();
		byte rawKeyData[] = key.getEncoded();
		FileOutputStream fo = new FileOutputStream(new File(keyName));
		fo.write(rawKeyData);
		//fo.write("6217855000008856817".getBytes());
		fo.close();
	}

	public  boolean checkcard(String banknumber) {
		// 取出最后一位
		String last = banknumber.substring(banknumber.length() - 1);
		// 前15或18位
		String front_last = banknumber.substring(0, banknumber.length() - 1);
		StringBuilder front_arr = new StringBuilder(front_last);
		// 前15或18位倒序存进数组
		front_arr.reverse();
		int sum1, sum2, sum3;
		sum1 = sum2 = sum3 = 0;
		for (int j = 0; j < front_arr.length(); j++) {
			if ((j + 1) % 2 == 1) {
				// 奇数数字和
				if (Integer.parseInt(new String().valueOf(front_arr.charAt(j))) * 2 < 9) {
					sum1 += Integer.parseInt(new String().valueOf(front_arr
							.charAt(j))) * 2;
				} else {
					int str = Integer.parseInt(new String().valueOf(front_arr
							.charAt(j))) * 2;
					int str1 = 1;
					int str2 = str % 10;
					sum2 += str1;
					sum2 += str2;
				}
			} else {
				// 偶数数字和
				sum3 += Integer.parseInt(new String().valueOf(front_arr.charAt(j)));
			}
		}
		int sum = sum1 + sum2 + sum3;
		int luhn = sum % 10 == 0 ? 0 : 10 - sum % 10;
		if (luhn == Integer.parseInt(last)) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String args[]) {
		try {
			new KeyIml().createKey(System.getProperty("user.dir")+ File.separator + "key.dat");
			logger.info(""+System.getProperty("user.dir"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(new KeyIml().checkcard("6217855000008856817"));
	}
}
