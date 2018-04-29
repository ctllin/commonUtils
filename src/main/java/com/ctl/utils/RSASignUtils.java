package com.ctl.utils;


import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class RSASignUtils {

    /**
     * TODO: 证书中读取的私钥，需要替换成商户自己证书中的私钥对应的base64编码
     */
    public static final String keybase64 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIlN3v6j181d7RNGjjT4uJIJZz88fAIXjs0tz6slRVMKuloqVaXJOqf/cD5cvV6HI/lnQ8iqiTITEbphs6XL4rltr1QiShxDkYJ/ZWWRcjpOTRtUsjQyO1i0uNAVtHv6pOhVHsMMdM+ffUX4UkUQH1J3YmUkUAw+KsqFpW2lSmb/AgMBAAECgYADCJ1ssuIJv5GFY4Gw2e36DAsj8ooq4apeDi0QcvpdI1eKtnS9/Pw2ygYI2rO0avf4greUr2/ok1NI8u+tMhwIRGNZJ9t2yfBJzelAXR96PJ66ESijrBOn4mM1+5FaAHQ+JQJl7mhjxwRPZrVFO+jyIh7gL1BQuNFxpFVPyPJMAQJBAN7vXOyK9q1PetpgtNF9OBGbnFMyzTYgBYGvDMd07eAc/l3G/itRNxpuAQoIPEspseMj9G8BICCu2NNlSneATkECQQCdqy83wjcEvdULugi6U+JliiBDfoxWvrDks2vUuwjViZN39D5fUHgYI1mc2DAps3+BrcR50QmdzvTrApd29+U/AkEAnAD6kNjJuAvnV4vW3MX4h3HSWfIx4k9UKyT5F1Z+45bGdY8768ogelFCTufmcV6TCfTuJwOtnNFeD8hQ7QEPQQJAOFE6B2+e2Vm5LHwhu/RUjLJevd0I4b8xTi9sV8sK58NBYnpigQcsLmsKCkUKRwrvg3F/GYf6KF0RDJSbZ4o6XwJAJ7rmMkjWjRPldF7v93JgviAGaP9pjvsgE3cr3iWk640eSpRAiOe2qUvXWrrxhfLE5Q5/b8xuP3k0KnT7gBIMoA==";

    private static final String HEX_CHARS = "0123456789abcdef";

    private static final String ALGNAME = "SHA1withRSA";

    private static byte[] base64DecodeChars = new byte[]
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53,
                    54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
                    12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29,
                    30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1,
                    -1, -1, -1};

    public static String  getSignMsg(String orderNumber){
    	return genSignByRSA(new StringBuilder("version=2.6&serialID=").append(orderNumber).append("&mode=1&type=1&orderID=").append(orderNumber).append("&beginTime=&endTime=&partnerID=11000000473&remark=1&charset=1&signType=1").toString());
    }
    
	public static void main(String[] args) {
		   String str=genSignByRSA("version=2.6&serialID=DK201612161707549717600CB6D1CF23&mode=1&type=1&orderID=DK201612161707549717600CB6D1CF23&beginTime=&endTime=&partnerID=11000000473&remark=1&charset=1&signType=1");
		   str=getSignMsg("DK201612161707549717600CB6D1CF23");
		   System.out.println(str);
	}
    /**
     * 使用该方法，对需要进行rsa签名的字符串进行签名操作
     *
     * @param rsaMsg �?��签名的字符串
     * @return
     */
    public static String genSignByRSA(String rsaMsg) {
        String signMsg = "";
        try {
            PrivateKey pkey = loadPrivateKeyPem(keybase64);
            rsaMsg = String.valueOf(PJWHash(rsaMsg));
            signMsg = genSignature(rsaMsg.getBytes("utf-8"), pkey);
        } catch (Exception e) {
            System.out.println("RSA genSignByRSA Exception" + e.getMessage());
        }
        return signMsg;
    }

    /**
     * 从私钥中创建PrivateKey对象，供签名使用
     *
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    private static PrivateKey loadPrivateKeyPem(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    private static int PJWHash(String str) {
        byte bitsInUnsignedInt = 32;
        int threeQuarters = bitsInUnsignedInt * 3 / 4;
        int oneEighth = bitsInUnsignedInt / 8;
        int highBits = -1 << bitsInUnsignedInt - oneEighth;
        int hash = 0;
        for (int i = 0; i < str.length(); ++i) {
            hash = (hash << oneEighth) + str.charAt(i);
            int var8;
            if ((var8 = hash & highBits) != 0) {
                hash = (hash ^ var8 >> threeQuarters) & ~highBits;
            }
        }

        return hash & 2147483647;
    }


    /**
     * 使用私钥进行签名
     *
     * @param src
     * @param usekey
     * @return
     * @throws Exception
     */
    private static String genSignature(byte[] src, PrivateKey usekey) throws Exception {
        Signature sig = Signature.getInstance(ALGNAME);
        sig.initSign(usekey);
        sig.update(src);
        return toHexString(sig.sign());
    }


    /**
     * 解密base64后的私钥�?     *
     * @param str
     * @return
     */
    private static byte[] decode(String str) {
        try {
            return decodePrivate(str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    private static byte[] decodePrivate(String str) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        byte[] data = null;
        data = str.getBytes("US-ASCII");
        int len = data.length;
        int i = 0;
        int b1, b2, b3, b4;
        while (i < len) {

            do {
                b1 = base64DecodeChars[data[i++]];
            } while (i < len && b1 == -1);
            if (b1 == -1)
                break;

            do {
                b2 = base64DecodeChars[data[i++]];
            } while (i < len && b2 == -1);
            if (b2 == -1)
                break;
            sb.append((char) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

            do {
                b3 = data[i++];
                if (b3 == 61)
                    return sb.toString().getBytes("iso8859-1");
                b3 = base64DecodeChars[b3];
            } while (i < len && b3 == -1);
            if (b3 == -1)
                break;
            sb.append((char) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

            do {
                b4 = data[i++];
                if (b4 == 61)
                    return sb.toString().getBytes("iso8859-1");
                b4 = base64DecodeChars[b4];
            } while (i < len && b4 == -1);
            if (b4 == -1)
                break;
            sb.append((char) (((b3 & 0x03) << 6) | b4));
        }
        return sb.toString().getBytes("iso8859-1");
    }


    /**
     * �?6进制字符�?     *
     * @param b
     * @return
     */
    private static String toHexString(byte[] b) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < b.length; ++i) {
            sb.append(HEX_CHARS.charAt(b[i] >>> 4 & 15));
            sb.append(HEX_CHARS.charAt(b[i] & 15));
        }

        return sb.toString();
    }

}
