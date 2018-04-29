package com.ctl.utils.json;

import java.sql.Timestamp;
import java.util.Date;

import com.ctl.utils.DateUtil;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
/**
 * 自定义JsonValueProcessor
 * 比如我们要控制JSON序列化过程中的Date对象的格式化，以及数值的格式化，JsonValueProcessor是最好的选择。
 * @author bitaotao
 *
 */
public class JsonDateValueProcessor implements JsonValueProcessor {
	public Object processArrayValue(Object value, JsonConfig config) {
		return process(value);
	}
	public Object processObjectValue(String key, Object value, JsonConfig config) {
		return process(value);
	}
	private Object process(Object value){
		if(value instanceof Date){
			return DateUtil.sdfyyyy_MM_dd_HH_mm_ss.format(value);
		}else if(value instanceof Timestamp){
			return DateUtil.sdfyyyy_MM_dd_HH_mm_ss.format(value);
		}
		return value == null ? "" : value.toString();
	}
}