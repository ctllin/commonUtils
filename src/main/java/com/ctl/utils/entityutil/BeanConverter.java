package com.ctl.utils.entityutil;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

import com.ctl.utils.DateUtil;
import com.ctl.utils.bean.Person;
import com.ctl.utils.encryp.des.DesUtils;
import com.ctl.utils.json.JsonDateValueProcessor;
import com.ctl.utils.json.JsonNullValuePropertyFilter;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 转换器
 * 1:将JavaBean 转换成Map、JSONObject
 * 2:将JSONObject 转换成Map
 *
 * @author xxx
 */
public class BeanConverter {
    static Logger logger = LoggerFactory.getLogger(BeanConverter.class);

    static {
        try {
            new DesUtils();
        } catch (Exception e) {
            logger.error("初始化DesUtils失败：", e);
        }
    }

    public static void main(String[] args) throws Exception {
        String str = DateUtil.sdf.format(new Date());
        System.out.println("加密前：" + str);
        System.out.println("加密后：" + DesUtils.encrypt(str));
        System.out.println("解密后：" + DesUtils.decrypt(DesUtils.encrypt(str)));
        Person person = new Person();
        person.setId(1);
        person.setAge(27);
        person.setAddress("XX新乡");
        person.setFloatv(1.01f);
        person.setFloatVV(1.02f);
        person.setDoublev(2.01d);
        person.setDoubleVV(2.03d);
        person.setDatenow(new Date());
        Map<String, Object> map = new HashMap<String, Object>();
        transBean2Map(person, map);
        logger.info(map.toString());
        person = new Person();
        transMap2Bean(person, map);
        logger.info(person.toString());
//        精度
//        float和double的精度是由尾数的位数来决定的。浮点数在内存中是按科学计数法来存储的，其整数部分始终是一个隐含着的“1”，由于它是不变的，故不能对精度造成影响。
//        float：2^23 = 8388608，一共七位，由于最左为1的一位省略了，这意味着最多能表示8位数： 2*8388608 = 16777216 。有8位有效数字，但绝对能保证的为7位，也即float的精度为7~8位有效数字；
//        double：2^52 = 4503599627370496，一共16位，同理，double的精度为16~17位
        JsonConfig jsonConfig = new JsonConfig();
        Map<String, Object> classMap = new HashMap<String, Object>();
        jsonConfig.setClassMap(classMap);
        JsonDateValueProcessor jsonValueProcessor = new JsonDateValueProcessor();
        jsonConfig.registerJsonValueProcessor(Date.class, jsonValueProcessor);
        jsonConfig.registerJsonValueProcessor(java.sql.Date.class, jsonValueProcessor);
        jsonConfig.registerJsonValueProcessor(Timestamp.class, jsonValueProcessor);
        jsonConfig.setJsonPropertyFilter(new JsonNullValuePropertyFilter());
        person = new Person();
        person.setId(1);
        person.setJine(999.346734d);
        person.setJineMax(9.12354d);
        person.setMax(10);
        person.setName("ctl");
        person.setPrice(10.99647f);//不超过8位
        person.setPriceMax(1.0134646f);//不超过8位
        person.setResult(true);
        person.setSavetime(new Date());
        person.setSuccess(false);
        person.setContent(null);
        System.out.println(JSONObject.fromObject(person, jsonConfig));
    }

    /**
     * 将javaBean转换成Map(加密后)
     *
     * @param javaBean javaBean
     * @return Map对象
     */
    public static Map<String, String> toEncryptMap(Object javaBean) {
        Map<String, String> result = new HashMap<>();
        Method[] methods = javaBean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            try {
                if (method.getName().startsWith("get")) {
                    String field = method.getName();
                    String simpleName = method.getReturnType().getSimpleName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    Object value = method.invoke(javaBean, (Object[]) null);
                    if (null != value && !"".equals(value) && "String".equals(simpleName)) {
                        result.put(field, null == value ? "" : DesUtils.encrypt(value.toString()));
                    } else {
                        result.put(field, null == value ? "" : value.toString());
                    }
                }
            } catch (Exception e) {
                logger.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            }
        }
        return result;
    }

    /**
     * @param
     * @return
     * @desc 将javaBean转换成Map(解密后)
     */
    public static Map<String, String> toDecryptMap(Object javaBean) {
        Map<String, String> result = new HashMap<>();
        Method[] methods = javaBean.getClass().getDeclaredMethods();

        for (Method method : methods) {
            try {
                if (method.getName().startsWith("get")) {
                    String simpleName = method.getReturnType().getSimpleName();
                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);

                    Object value = method.invoke(javaBean, (Object[]) null);
                    if (null != value && !"".equals(value) && "String".equals(simpleName)) {
                        result.put(field, null == value ? "" : DesUtils.decrypt(value.toString()));
                    } else {
                        result.put(field, null == value ? "" : value.toString());
                    }
                }
            } catch (Exception e) {
                logger.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            }
        }
        return result;
    }

    /**
     * @param mapData 待加密map
     * @return
     */
    public static Map<String, String> toDecryptMap(Map<String, String> mapData) {
        Set<String> key = mapData.keySet();
        Iterator<String> iter = key.iterator();
        while (iter.hasNext()) {
            String keyStr = iter.next();
            if (!(mapData.get(keyStr) instanceof String)) {
                continue;
            }
            String valueStr = mapData.get(keyStr);
            try {
                valueStr = null == valueStr ? "" : DesUtils.decrypt(valueStr.toString());
            } catch (Exception e) {
                logger.error(keyStr + " 解密失败,解密前：" + valueStr, e);
            }
            mapData.put(keyStr, valueStr);
        }
        return mapData;
    }


    public static Map<String, Object> transBean2Map(Object obj, Map<String, Object> map) {
        if (obj == null) {
            return null;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (null != value && !"".equals(value))
                        map.put(key, value);
                }

            }
        } catch (Exception e) {
            logger.error("transBean2Map Error ", e);
        }
        return map;
    }


    public static Object transMap2Bean(Object javabean, Map<String, Object> data) {
        Method[] methods = javabean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            try {
                if (method.getName().startsWith("set")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("set") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    method.invoke(javabean, new Object[]{data.get(field)});
                }
            } catch (Exception e) {
                logger.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            }
        }
        return javabean;
    }
}
