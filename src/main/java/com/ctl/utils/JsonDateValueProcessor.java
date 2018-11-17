package com.ctl.utils;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: JsonDateValueProcessor</p>
 * <p>Description: json日期处理类</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-05-30 11:36
 */
public class JsonDateValueProcessor implements JsonValueProcessor ,Serializable {
    private static final long serialVersionUID = 8145326926153914645L;

    public static JsonConfig jsonConfig = initJsonConfig();

    private static JsonConfig initJsonConfig() {
        if (jsonConfig == null) {
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
            jsonConfig.registerJsonValueProcessor(Timestamp.class, new JsonDateValueProcessor());
            return jsonConfig;
        } else {
            return jsonConfig;
        }
    }
    // 定义转换日期类型的输出格式
    private String format = "yyyy-MM-dd HH:mm:ss";

    public JsonDateValueProcessor() {

    }

    public JsonDateValueProcessor(String format) {
        this.format = format;
    }

    @Override
    public Object processArrayValue(Object arg0, JsonConfig arg1) {
        return process(arg0);
    }

    private Object process(Object arg0) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(arg0);
    }

    @Override
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
        if (value instanceof Date) {
            String str = new SimpleDateFormat(format).format((Date) value);
            return str;
        }
        if (value instanceof Timestamp) {
            String str = new SimpleDateFormat(format).format((Timestamp) value);
            return str;
        }
        if (null != value) {
            return value.toString();
        }
        return "";
    }

    public static void main(String[] args) throws Exception{
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
        jsonConfig.registerJsonValueProcessor(Timestamp.class, new JsonDateValueProcessor());
        jsonConfig.registerJsonValueProcessor(Integer.class, new JsonNumberValueProcessor());
        jsonConfig.registerJsonValueProcessor(Long.class, new JsonNumberValueProcessor());
        jsonConfig.registerJsonValueProcessor(Byte.class, new JsonNumberValueProcessor());
        jsonConfig.registerJsonValueProcessor(Float.class, new JsonNumberValueProcessor());
        jsonConfig.registerJsonValueProcessor(Double.class, new JsonNumberValueProcessor());
        Map<String,Object> jsonObjectData=new HashMap<>();
        jsonObjectData.put("registerTime",DateUtil.sdfyyyy_MM_dd_HH_mm_ss.parse("1991-06-30 12:00:00"));
        System.out.println(JSONObject.fromObject(jsonObjectData,jsonConfig));
        System.out.println(jsonObjectData.get("registerTime").getClass());
    }
}