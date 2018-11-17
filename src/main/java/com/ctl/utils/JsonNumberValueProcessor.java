package com.ctl.utils;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.io.Serializable;

/**
 * <p>Title: JsonIntegerValueProcessor</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-08-07 16:15
 */
public class JsonNumberValueProcessor implements JsonValueProcessor,Serializable {
    @Override
    public Object processArrayValue(Object o, JsonConfig jsonConfig) {
        return null;
    }

    @Override
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
        if (value instanceof Integer) {
            String str = String.valueOf(value);
            return str;
        }
        if (value instanceof Byte) {
            String str = String.valueOf(value);
            return str;
        }
        if (value instanceof Long) {
            String str = String.valueOf(value);
            return str;
        }
        if (value instanceof Float) {
            String str = String.valueOf(value);
            return str;
        }
        if (value instanceof Double) {
            String str = String.valueOf(value);
            return str;
        }
        if (null != value) {
            return value.toString();
        }
        return "";
    }
}
