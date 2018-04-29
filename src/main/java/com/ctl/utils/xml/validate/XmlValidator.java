package com.ctl.utils.xml.validate;

/**
 * @author 		 guolin
 * @tel			 18515287139
 * @date    	 2016-3-13\u4E0A\u534811:06:18
 * @package_name 
 * @project_name ctlUtils
 * @version 	 version.1.0
 * @description	 
 */

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URLDecoder;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

/**
 * @author wpan 2010-09-12 \u9A8C\u8BC1xml\u662F\u5426\u7B26\u5408\u683C\u5F0F
 * 
 */
public class XmlValidator {

	public final static String SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";

	/**
	 * @param xmlStr  \u5F85\u6821\u9A8Cxml(\u5B57\u7B26\u4E32)
	 * @param xsdStr  schema\u5185\u5BB9(\u5B57\u7B26\u4E32)
	 * @return XmlValidateResult \u901A\u8FC7Schema\u9A8C\u8BC1\u6307\u5B9A\u7684xml\u5B57\u7B26\u4E32\u662F\u5426\u7B26\u5408\u7ED3\u6784
	 * @throws Exception 
	 */
	public static XmlValidateResult validateByXsd(String xmlStr, String xsdStr) throws Exception {
		// \u67E5\u627E\u652F\u6301\u6307\u5B9A\u6A21\u5F0F\u8BED\u8A00\u7684 SchemaFactory \u7684\u5B9E\u73B0\u5E76\u8FD4\u56DE\u5B83
		SchemaFactory factory = SchemaFactory.newInstance(XmlValidator.SCHEMA_LANGUAGE);
		// \u5305\u88C5\u5F85\u9A8C\u8BC1\u7684xml\u5B57\u7B26\u4E32\u4E3AReader
		Reader xmlReader = new BufferedReader(new StringReader(xmlStr));
		// \u4FDD\u969CSchema xsd\u5B57\u7B26\u4E32\u4E3AReader
		Reader xsdReader = new BufferedReader(new StringReader(xsdStr));
		// \u521B\u5EFA\u8FD4\u56DE\u503C\u7C7B\uFF0C\u9ED8\u8BA4\u4E3A\u5931\u8D25
		XmlValidateResult vs = new XmlValidateResult();

		try {
			// \u6784\u9020Schema Source
			Source xsdSource = new StreamSource(xsdReader);
			// \u89E3\u6790\u4F5C\u4E3A\u6A21\u5F0F\u7684\u6307\u5B9A\u6E90\u5E76\u4EE5\u6A21\u5F0F\u5F62\u5F0F\u8FD4\u56DE\u5B83
			Schema schema = factory.newSchema(xsdSource);

			// \u6839\u636ESchema\u68C0\u67E5xml\u6587\u6863\u7684\u5904\u7406\u5668,\u521B\u5EFA\u6B64 Schema \u7684\u65B0 Validator
			Validator validator = schema.newValidator();

			// \u6784\u9020\u5F85\u9A8C\u8BC1xml Source
			Source xmlSource = new StreamSource(xmlReader);
			// \u6267\u884C\u9A8C\u8BC1
			validator.validate(xmlSource);
			// \u8BBE\u7F6E\u9A8C\u8BC1\u901A\u8FC7
			vs.setValidated(true);
			return vs;
		} catch (SAXException ex) {
			// \u8BBE\u7F6E\u9A8C\u8BC1\u5931\u8D25
			vs.setValidated(false);
			// \u8BBE\u7F6E\u9A8C\u8BC1\u5931\u8D25\u4FE1\u606F
			vs.setErrorMsg(ex.getMessage());
			//\u8BBE\u7F6E\u5F85\u6821\u9A8C\u5185\u5BB9
			vs.setXmlStr(xmlStr);
			return vs;
		} catch (IOException e) {
			// \u8BBE\u7F6E\u9A8C\u8BC1\u5931\u8D25
			vs.setValidated(false);
			// \u8BBE\u7F6E\u9A8C\u8BC1\u5931\u8D25\u4FE1\u606F
			vs.setErrorMsg(e.getMessage());
			//\u8BBE\u7F6E\u5F85\u6821\u9A8C\u5185\u5BB9
			vs.setXmlStr(xmlStr);
			return vs;
		}finally{
			if(xmlReader!=null){
				try {
					xmlReader.close();
				} catch (Exception e2) {
					throw e2;
				}
			}
			if(xsdReader!=null){
				try {
					xsdReader.close();
				} catch (Exception e2) {
					throw e2;
				}
			}
		}
	}
	
	public static XmlValidateResult validateByXsd2(String xmlStr, String xsdStr) throws Exception {
		// \u67E5\u627E\u652F\u6301\u6307\u5B9A\u6A21\u5F0F\u8BED\u8A00\u7684 SchemaFactory \u7684\u5B9E\u73B0\u5E76\u8FD4\u56DE\u5B83
		SchemaFactory factory = SchemaFactory.newInstance(XmlValidator.SCHEMA_LANGUAGE);
		XmlValidateResult vs = new XmlValidateResult();

		try {
			// \u6784\u9020Schema Source
			Source xsdSource = new StreamSource(new ByteArrayInputStream(xsdStr.getBytes()));
			// \u89E3\u6790\u4F5C\u4E3A\u6A21\u5F0F\u7684\u6307\u5B9A\u6E90\u5E76\u4EE5\u6A21\u5F0F\u5F62\u5F0F\u8FD4\u56DE\u5B83
			Schema schema = factory.newSchema(xsdSource);

			// \u6839\u636ESchema\u68C0\u67E5xml\u6587\u6863\u7684\u5904\u7406\u5668,\u521B\u5EFA\u6B64 Schema \u7684\u65B0 Validator
			Validator validator = schema.newValidator();

			// \u6784\u9020\u5F85\u9A8C\u8BC1xml Source
			Source xmlSource = new StreamSource(new ByteArrayInputStream(xmlStr.getBytes()));
			// \u6267\u884C\u9A8C\u8BC1
			validator.validate(xmlSource);
			// \u8BBE\u7F6E\u9A8C\u8BC1\u901A\u8FC7
			vs.setValidated(true);
			return vs;
		} catch (SAXException ex) {
			// \u8BBE\u7F6E\u9A8C\u8BC1\u5931\u8D25
			vs.setValidated(false);
			// \u8BBE\u7F6E\u9A8C\u8BC1\u5931\u8D25\u4FE1\u606F
			vs.setErrorMsg(ex.getMessage());
			//\u8BBE\u7F6E\u5F85\u6821\u9A8C\u5185\u5BB9
			vs.setXmlStr(xmlStr);
			return vs;
		} catch (IOException e) {
			// \u8BBE\u7F6E\u9A8C\u8BC1\u5931\u8D25
			vs.setValidated(false);
			// \u8BBE\u7F6E\u9A8C\u8BC1\u5931\u8D25\u4FE1\u606F
			vs.setErrorMsg(e.getMessage());
			//\u8BBE\u7F6E\u5F85\u6821\u9A8C\u5185\u5BB9
			vs.setXmlStr(xmlStr);
			return vs;
		}finally{}
	}
	
	/**
	 * @author 			guolin
	 * @date    		2016-3-13\u4E0A\u534811:50:16
	 * @package_name 	com.ctl.util.xml.validate
	 * @project_name   	ctlUtils
	 * @version 		version.1.0
	 * @description		
	 * @param xmlFile   \u5F85\u6821\u9A8Cxml\u6587\u4EF6
	 * @param xdsFile   schema\u5B9A\u4E49\u6587\u4EF6
	 * @return
	 * @throws Exception 
	 * @throws Exception
	 */
	public static XmlValidateResult validateByXsd(File xmlFile, File xdsFile) throws Exception  {
		BufferedReader xmlBuff;
		try {
			xmlBuff = new BufferedReader(new FileReader(xmlFile));
		} catch (FileNotFoundException e) {
			throw e;
		}
		BufferedReader xsdBuff;
		try {
			xsdBuff = new BufferedReader(new FileReader(xdsFile));
		} catch (FileNotFoundException e) {
			throw e;
		}

		StringBuffer xmlStr = new StringBuffer();
		String line = null;
		while ((line = xmlBuff.readLine()) != null) {
			xmlStr.append(line);
			xmlStr.append("\n");
		}
		if(xmlBuff!=null){
			try {
				xmlBuff.close();
			} catch (Exception e) {
				throw e;
			}
		}

		StringBuffer xsdStr = new StringBuffer();
		line = null;
		while ((line = xsdBuff.readLine()) != null) {
			xsdStr.append(line);
		}
		if(xsdBuff!=null){
			try {
				xsdBuff.close();
			} catch (Exception e) {
				throw e;
			}
		}
		return XmlValidator.validateByXsd2(xmlStr.toString(), xsdStr.toString());
	}

	/**
	 * @author 			guolin
	 * @date    		2016-3-13下午4:27:00
	 * @package_name 	com.ctl.util.xml.validate
	 * @project_name   	ctlUtils
	 * @version 		version.1.0
	 * @description		
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static XmlValidateResult validateByXsd(String xmlStr,File xdsFile) throws Exception{
		BufferedReader xsdBuff;
		try {
			xsdBuff = new BufferedReader(new FileReader(xdsFile));
		} catch (FileNotFoundException e) {
			throw e;
		}
		StringBuffer xsdStr = new StringBuffer();
		String line = null;
		while ((line = xsdBuff.readLine()) != null) {
			xsdStr.append(line);
		}
		if(xsdBuff!=null){
			try {
				xsdBuff.close();
			} catch (Exception e) {
				throw e;
			}
		}
		return XmlValidator.validateByXsd(xmlStr.toString(), xsdStr.toString());
	}
	
	/**
	 * @param args0
	 * @throws Exception 
	 */
	public static void main(String[] args0) throws Exception {
		File xml = new File(URLDecoder.decode(XmlValidator.class.getClassLoader().getResource("email.xml").getPath(),"utf-8"));
		File xsd = new File(URLDecoder.decode(XmlValidator.class.getClassLoader().getResource("email.xsd").getPath(),"utf-8"));
        

		XmlValidateResult vs = XmlValidator.validateByXsd(xml,xsd);
		if (vs.isValidated()) {
			System.out.println("\u9A8C\u8BC1\u6210\u529F\uFF01");
		} else {
			System.out.println("\u9A8C\u8BC1\u5931\u8D25\uFF01");
			System.out.println("\u5931\u8D25\u539F\u56E0\uFF1A" + vs.getErrorMsg());
			System.out.println("\u9A8C\u8BC1xml\uFF1A\n" + vs.getXmlStr());
		}

	}
}