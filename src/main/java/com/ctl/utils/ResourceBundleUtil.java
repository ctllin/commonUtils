package com.ctl.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

public class ResourceBundleUtil {
    static Logger logger = LoggerFactory.getLogger(ResourceBundleUtil.class);

    private static ResourceBundle config = ResourceBundle.getBundle("config");

    public static String getConfig(String key) {
        return (config.getString(key)==null) ? "" : config.getString(key).trim();
    }
    public static void main(String args[]){
       logger.info( getConfig("name"));
    }
}
