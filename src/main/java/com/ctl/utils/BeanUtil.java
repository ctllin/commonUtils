package com.ctl.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import com.ctl.utils.bean.Person;
import com.ctl.utils.encryp.des.DesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BeanUtil {
	static Logger logger = LoggerFactory.getLogger(BeanUtil.class);

	/**
	 * 根据属性名获取属性值
	 * */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {  
            String firstLetter = fieldName.substring(0, 1).toUpperCase();  
            String getter = "get" + firstLetter + fieldName.substring(1);  
            Method method = o.getClass().getMethod(getter, new Class[] {});  
            Object value = method.invoke(o, new Object[] {});  
            return value;  
        } catch (Exception e) {
			logger.error(e.getMessage());
            return null;  
        }  
    } 
    
    /**
     * 获取属性名数组
     * */
    private static String[] getFiledName(Object o){
    	Field[] fields=o.getClass().getDeclaredFields();
       	String[] fieldNames=new String[fields.length];
    	for(int i=0;i<fields.length;i++){
    		System.out.println(fields[i].getType());
    		fieldNames[i]=fields[i].getName();
    	}
    	return fieldNames;
    }
    
    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getFiledsInfo(Object o){
    	Field[] fields=o.getClass().getDeclaredFields();
       	//String[] fieldNames=new String[fields.length];
       	List list = new ArrayList();
       	Map infoMap=null;
    	for(int i=0;i<fields.length;i++){
    		infoMap = new HashMap();
    		infoMap.put("type", fields[i].getType().toString());
    		infoMap.put("name", fields[i].getName());
    		infoMap.put("value", getFieldValueByName(fields[i].getName(), o));
    		list.add(infoMap);
    	}
    	return list;
    }
    
    /**
     * 获取对象的所有属性值，返回一个对象数组
     * */
    public static Object[] getFiledValues(Object o){
    	String[] fieldNames=getFiledName(o);
    	Object[] value=new Object[fieldNames.length];
    	for(int i=0;i<fieldNames.length;i++){
    		value[i]=getFieldValueByName(fieldNames[i], o);
    	}
    	return value;
    }

	public static void main(String[] args) throws Exception {
		String str= DateUtil.sdf.format(new Date());
		System.out.println("加密前："+str);
		System.out.println("加密后："+ DesUtils.encrypt(str));
		System.out.println("解密后："+DesUtils.decrypt(DesUtils.encrypt(str)));
		Person person=new Person();
		person.setId(1);
		person.setAge(27);
		person.setAddress("XX新乡");
		person.setFloatv(1.01f);
		person.setFloatVV(1.02f);
		person.setDoublev(2.01d);
		person.setDoubleVV(2.03d);
		person.setDatenow(new Date());
		logger.info(Arrays.deepToString(getFiledValues(person)));
		List filedsInfo = getFiledsInfo(person);
		logger.info(filedsInfo.toString());
		String fieldValue=(String)getFieldValueByName("address",person);
		logger.info(fieldValue);
	}

}
