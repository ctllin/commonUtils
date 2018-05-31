package com.ctl.utils.json;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

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
public class JsonDateValueProcessor2 implements JsonValueProcessor {
    // 定义转换日期类型的输出格式
    private String format = "yyyy-MM-dd HH:mm:ss";

    public JsonDateValueProcessor2() {

    }

    public JsonDateValueProcessor2(String format) {
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

    public static void main(String[] args) {
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
        jsonConfig.registerJsonValueProcessor(Timestamp.class, new JsonDateValueProcessor());
        Map<String,Object> jsonObjectData=new HashMap<>();
        jsonObjectData.put("registerTime",new Date());
        System.out.println(JSONObject.fromObject(jsonObjectData,jsonConfig));
        System.out.println(jsonObjectData.get("registerTime").getClass());
    }
}