package com.ctl.utils;

@SuppressWarnings("rawtypes")
public class FactoryUtil {
    public static Object getInstance(String type) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class c = null;
        Object obj = null;
        String className = ConfigUtils.getType(type);
        c = Class.forName(className);
        obj = c.newInstance();
        return obj;
    }
}
