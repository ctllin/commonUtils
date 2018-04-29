package com.ctl.utils.crpty.dao;

@SuppressWarnings("rawtypes")
public class TypeValueBean {

	public Class type;// 参数类型
	public Object value;// 参数值

	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public TypeValueBean(Class type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}

}
