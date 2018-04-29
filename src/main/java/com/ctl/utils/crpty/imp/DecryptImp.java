package com.ctl.utils.crpty.imp;


import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.security.SecureRandom;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.ctl.utils.crpty.dao.DecryptDao;
import com.ctl.utils.crpty.dao.TypeValueBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DecryptImp implements DecryptDao {

    public static Logger logger = LoggerFactory.getLogger(DecryptImp.class);
    private static final String KEYPATH = System.getProperty("user.dir") + File.separator + "key.dat";
    private static final String CRPTYFLAG = "crpty1851528713900";
    private static final int CRPTYFLAGLENGTH = CRPTYFLAG.length();

    /**
     * @param classPackageAndClassName for exemple: com.ctl.util.CopyUtil
     * @param flag                     if flag=0,method is static ,else method is not static
     * @param args                     method args
     * @return
     * @throws Exception
     */
    public Class getClassObject(String classPackageAndClassName)
            throws Exception {

        String classRootPath = URLDecoder.decode(
                DecryptImp.class.getClassLoader().getResource(".").toString(),
                "utf-8").substring(6);
        SecureRandom sr = new SecureRandom();
        FileInputStream fi = new FileInputStream(new File(KEYPATH));
        byte rawKeyData[] = new byte[fi.available()];
        fi.read(rawKeyData);
        fi.close();
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key, sr);
        FileInputStream fi2 = new FileInputStream(new File(classRootPath
                + classPackageAndClassName.replace(".", File.separator)
                + ".class"));
        byte encryptedData[] = new byte[fi2.available()];
        fi2.read(encryptedData);
        fi2.close();

        if (!new String(encryptedData, encryptedData.length - CRPTYFLAGLENGTH, CRPTYFLAGLENGTH).equals(CRPTYFLAG)) {
            logger.info("没有进行加密");
            return Class.forName(classPackageAndClassName);
        }

        byte decryptedData[] = cipher.doFinal(encryptedData, 0, encryptedData.length - CRPTYFLAGLENGTH);
        MyClassLoaderImp mcl = new MyClassLoaderImp(classRootPath);

        Class clazz = mcl.loadClass(decryptedData, classPackageAndClassName);
//		// 下面的是把class还原
//		new File("D:/Test1.class").createNewFile();
//		OutputStream out = new FileOutputStream(new File("D:/Test1.class"));
//		out.write(decryptedData);
//		out.close();
        return clazz;
    }

    /**
     * @param Class for exemple: com.ctl.util.CopyUtil.class
     *              注意该class必须从getClassObject获取
     * @param flag  if flag=0,method is static ,else method is not static
     * @param args  method args
     * @return
     * @throws Exception
     */


    public Object execMethod(Class clazz, int flag, String methodName,
                             List<TypeValueBean> typeValue) {
        Class types[] = null;
        Object values[] = null;
        if (typeValue != null && typeValue.size() > 0) {
            types = new Class[typeValue.size()];
            values = new Object[typeValue.size()];
            for (int i = 0; i < typeValue.size(); i++) {
                types[i] = typeValue.get(i).getType();
                values[i] = typeValue.get(i).getValue();
            }
        }
        Method mainMethod = null;
        Object objReturn = null;
        try {
            if (flag == 0 && types != null) {
                mainMethod = clazz.getMethod(methodName, types);
                objReturn = mainMethod.invoke(null, values);// not staic method
            } else if (flag == 0 && types == null) {
                mainMethod = clazz.getMethod(methodName);
                objReturn = mainMethod.invoke(null);// not staic method
            }
            if (flag == 1 && types != null) {
                Object obj = clazz.newInstance();
                mainMethod = clazz.getMethod(methodName, types);
                objReturn = mainMethod.invoke(obj, values);// not staic method
            } else if (flag == 1 && types == null) {
                Object obj = clazz.newInstance();
                mainMethod = clazz.getMethod(methodName);
                objReturn = mainMethod.invoke(obj);// not staic method
            }
        } catch (Exception e) {
            logger.info("method invoke fail!");
        }
        return objReturn;
    }
}
