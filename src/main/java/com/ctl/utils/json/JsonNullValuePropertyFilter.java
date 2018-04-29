package com.ctl.utils.json;

import java.util.List;

import net.sf.json.util.PropertyFilter;

public class JsonNullValuePropertyFilter implements PropertyFilter {

	@SuppressWarnings("unchecked")
	public boolean apply(Object object, String fieldName, Object fieldValue) {
		if (fieldValue instanceof List) {
			List<Object> list = (List<Object>) fieldValue;
			if (list.size() == 0) {
				return true;
			}
		}
		//return null == fieldValue || "".equals(fieldValue);//为空则不导出
		return false;//为空也导出
	}

}
