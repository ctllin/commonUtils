package com.ctl.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;


/**
 * 编码工具
 * 1.将byte[]转为各种进制的字符串
 * 2.base 64 encode
 * 3.base 64 decode
 * 4.获取byte[]的md5
 * 5.获取字符串md5
 * 6.结合base64实现md5加密
 * 7.AES加密
 * 8.AES加密为base 64 code
 * 9.AES解密
 * 10.将base 64 code AES解密
 * @author uikoo9
 * @version 0.0.7.20140601
 */
public class AEncodeUtil {
	final static byte[] bs ={120,108,104,90,32,42,45,33,64,35,94,55,38,43,45,61,46,44,50,64,33,97,122,48,57};
	public static void main(String[] args) throws Exception {
		String content = "xlh2014";
		System.out.println("加密前：" + content);
		String key = "518";
		System.out.println("加密密钥和解密密钥：" + key);
		
		String encrypt = aesEncrypt(content, key);
		System.out.println("加密后：" + encrypt);
		
		String decrypt = aesDecrypt(encrypt, key);
		System.out.println("解密后：" + decrypt);	
	}
	
	/**
	 * 将byte[]转为各种进制的字符串
	 * @param bytes byte[]
	 * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
	 * @return 转换后的字符
	 */
	public static String binary(byte[] bytes, int radix){
		return new BigInteger(1, bytes).toString(radix);// 这里代表正数
	}
	
	/**
	 * base 64 encode
	 * @param bytes 待编码的byte[]
	 * @return 编码后的base 64 code
	 */
	private static String base64Encode(byte[] bytes){
		//return new BASE64Encoder().encode(bytes);
		return Base64.getEncoder().encodeToString(bytes);
	}
	
	/**
	 * base 64 decode
	 * @param base64Code 待解码的base 64 code
	 * @return 解码后的byte[]
	 * @throws Exception
	 */
	private static byte[] base64Decode(String base64Code) throws Exception{
		if(null ==base64Code || "".equals(base64Code.trim())){
			return null;
		}
		//return new BASE64Decoder().decodeBuffer(base64Code);
		return Base64.getDecoder().decode(base64Code);
	}
	
	/**
	 * 获取byte[]的md5
	 * @param bytes byte[]
	 * @return md5
	 * @throws Exception
	 */
	private static byte[] md5(byte[] bytes) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(bytes);
		
		return md.digest();
	}
	
	/**
	 * 获取字符串md5
	 * @param msg 
	 * @return md5
	 * @throws Exception
	 */
	private static byte[] md5(String msg) throws Exception {
		if(null ==msg || "".equals(msg.trim())){
			return null;
		}
		return  md5(msg.getBytes());
	}
	
	/**
	 * 结合base64实现md5加密
	 * @param msg 待加密字符串
	 * @return 获取md5后转为base64
	 * @throws Exception
	 */
	public static String md5Encrypt(String msg) throws Exception{
		if(null ==msg || "".equals(msg.trim())){
			return null;
		}
		return  base64Encode(md5(msg));
	}
	
	/**
	 * AES加密
	 * @param content 待加密的内容
	 * @param encryptKey 加密密钥
	 * @return 加密后的byte[]
	 * @throws Exception
	 */
	private static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, new SecureRandom(encryptKey.getBytes()));

		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
		
		return cipher.doFinal(content.getBytes("utf-8"));
	}
	
	/**
	 * AES加密为base 64 code
	 * @param content 待加密的内容
	 * @param encryptKey 加密密钥
	 * @return 加密后的base 64 code
	 * @throws Exception
	 */
	public static String aesEncrypt(String content, String encryptKey) throws Exception {
		byte[] bs ={120,108,104,90,32,42,45,33,64,35,94,55,38,43,45,61,46,44,50,64,33,97,122,48,57};
		if(null==encryptKey || "".equals(encryptKey.trim())){
			encryptKey = new String(bs);
		}
		return base64Encode(aesEncryptToBytes(content, encryptKey));
	}
	
	/**
	 * AES解密
	 * @param encryptBytes 待解密的byte[]
	 * @param decryptKey 解密密钥
	 * @return 解密后的String
	 * @throws Exception
	 */
	private static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
		byte[] bs ={120,108,104,90,32,42,45,33,64,35,94,55,38,43,45,61,46,44,50,64,33,97,122,48,57};
		if(null==decryptKey || "".equals(decryptKey.trim())){
			decryptKey = new String(bs);
		}
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, new SecureRandom(decryptKey.getBytes()));

		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
		byte[] decryptBytes = cipher.doFinal(encryptBytes);
		
		return new String(decryptBytes);
	}
	
	/**
	 * 将base 64 code AES解密
	 * @param encryptStr 待解密的base 64 code
	 * @param decryptKey 解密密钥
	 * @return 解密后的string
	 * @throws Exception
	 */
	public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
		if(null ==encryptStr || "".equals(encryptStr)){
			return null;
		}
		return  aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
	}

}
