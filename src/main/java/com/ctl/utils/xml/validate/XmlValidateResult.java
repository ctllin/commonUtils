package com.ctl.utils.xml.validate;

/**
 * @author 		 guolin
 * @tel			 18515287139
 * @date    	 2016-3-13\u4E0A\u534811:07:08
 * @package_name 
 * @project_name ctlUtils
 * @version 	 version.1.0
 * @description	 
 */


/**
 * @author wpan Xml\u9A8C\u8BC1\u7ED3\u679C
 */
public class XmlValidateResult {
	// \u662F\u5426\u901A\u8FC7\u9A8C\u8BC1
	private boolean validated;

	// \u9519\u8BEF\u4FE1\u606F
	private String errorMsg;

	//\u5F85\u6821\u9A8Cxml\u5185\u5BB9
	private String xmlStr;
	// \u6784\u9020\u51FD\u6570\uFF0C\u9ED8\u8BA4\u4E3A\u4E0D\u901A\u8FC7\uFF0C\u9519\u8BEF\u539F\u56E0\u4E3A\u7A7A\u5B57\u7B26\u4E32
	XmlValidateResult() {
		validated = false;
		errorMsg = "";
		xmlStr="";
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public String getXmlStr() {
		return xmlStr;
	}

	public void setXmlStr(String xmlStr) {
		this.xmlStr = xmlStr;
	}

}