package com.ctl.utils.xml.test;

import java.io.*;
import java.util.*;
import javax.xml.bind.*;
import org.w3c.dom.Node;

import com.ctl.utils.xml.test1.TXTest1;
import com.ctl.utils.xml.validate.XmlValidateResult;
import com.ctl.utils.xml.validate.XmlValidator;


public class JAXBDemoReadUtil {

	static int[] weight = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 }; // \u5341\u4E03\u4F4D\u6570\u5B57\u672C\u4F53\u7801\u6743\u91CD
	static char[] validate = { '1', '0', 'X', '9', '8', '7', '6', '5', '4',
			'3', '2' }; // mod11,\u5BF9\u5E94\u6821\u9A8C\u7801\u5B57\u7B26\u503C

	public static char getValidateCode(String id17) {
		int sum = 0;
		int mode = 0;
		for (int i = 0; i < id17.length(); i++) {
			sum = sum + Integer.parseInt(String.valueOf(id17.charAt(i)))
					* weight[i];
		}
		mode = sum % 11;
		return validate[mode];
	}

	static String str1 = "<?xml version='1.0' encoding='UTF-8'?>"
			+ "<TX>"
			+ "<TX_HEADER>"
			+ "<SYS_HDR_LEN>100</SYS_HDR_LEN>"
			+ "<SYS_PKG_VRSN>01</SYS_PKG_VRSN>"
			+ "<SYS_TTL_LEN>400</SYS_TTL_LEN>"
			+ "<SYS_REQ_SEC_ID>0123456789</SYS_REQ_SEC_ID>"
			+ "<SYS_SND_SEC_ID>9876543210</SYS_SND_SEC_ID>"
			+ "<SYS_TX_CODE>BiolServices</SYS_TX_CODE>"
			+ "<SYS_TX_VRSN>01</SYS_TX_VRSN>"
			+ "<SYS_TX_TYPE>00000</SYS_TX_TYPE>"
			+ "<SYS_RESERVED>AA</SYS_RESERVED>"
			+ "<SYS_EVT_TRACE_ID>yyyyMMddHHmmssms</SYS_EVT_TRACE_ID>"
			+ "<SYS_SND_SERIAL_NO>0000000023</SYS_SND_SERIAL_NO>"
			+ "<SYS_PKG_TYPE>1</SYS_PKG_TYPE>"
			+ "<SYS_MSG_LEN>100</SYS_MSG_LEN>"
			+ "<SYS_IS_ENCRYPTED>0</SYS_IS_ENCRYPTED>"
			+ "<SYS_ENCRYPT_TYPE>3</SYS_ENCRYPT_TYPE>"
			+ "<SYS_COMPRESS_TYPE>0</SYS_COMPRESS_TYPE>"
			+ "<SYS_EMB_MSG_LEN>0</SYS_EMB_MSG_LEN>"
			+ "<SYS_REQ_TIME>yyyyMMddHHmmssms</SYS_REQ_TIME>"
			+ "<SYS_TIME_LEFT>HHmmssms</SYS_TIME_LEFT>"
			+ "<SYS_PKG_STS_TYPE>00</SYS_PKG_STS_TYPE>"
			+ "</TX_HEADER>"
			+ "<TX_BODY>"
			+ "<COMMON>"
			+ "<COM1>"
			+ "<TXN_INSID>440000000</TXN_INSID>"
			+ "<TXN_ITT_CHNL_ID>0130</TXN_ITT_CHNL_ID>"
			+ "<TXN_ITT_CHNL_CGY_CODE>0001</TXN_ITT_CHNL_CGY_CODE>"
			+ "<TXN_DT>yyyyMMdd</TXN_DT>"
			+ "<TXN_TM>HHmmss</TXN_TM>"
			+ "<TXN_STFF_ID>999999</TXN_STFF_ID>"
			+ "<MULTI_TENANCY_ID>CN000</MULTI_TENANCY_ID>"
			+ "<LNG_ID>zh-cn</LNG_ID>"
			+ "</COM1>"
			+ "</COMMON>"
			+ "<ENTITY>"
			+ "<AUTH_TYPE>1</AUTH_TYPE>"
			+ "<AUTH_MODE>1</AUTH_MODE>"
			+
			// "<PICTURE1>12313</PICTURE1>" +
			// "<PICTURE2>4235345435</PICTURE2>" +
			"<CUST_NAME>xzdong</CUST_NAME>"
			+ "<ID_NO>431281198907225616</ID_NO>"
			+ "<CERT_NO>431281198907225618</CERT_NO>"
			+ "<HITS>1</HITS>"
			+ "<THRESHOLD>0.88</THRESHOLD>"
			+ "<FEATURES>89821</FEATURES>"
			+ "<PICTURE></PICTURE>"
			+ "</ENTITY>" + "</TX_BODY>" + "<TX_EMB>" + "</TX_EMB>" + "</TX>";

	static String str2= "<?xml version='1.0' encoding='UTF-8'?>"
			+ "<TX>"
			+ "<TX_HEADER>"
			+ "<SYS_HDR_LEN>100</SYS_HDR_LEN>"
			+ "<SYS_PKG_VRSN>01</SYS_PKG_VRSN>"
			+ "<SYS_TTL_LEN>400</SYS_TTL_LEN>"
			+ "<SYS_REQ_SEC_ID>0123456789</SYS_REQ_SEC_ID>"
			+ "<SYS_SND_SEC_ID>9876543210</SYS_SND_SEC_ID>"
			+ "<SYS_TX_CODE>BiolServices</SYS_TX_CODE>"
			+ "<SYS_TX_VRSN>01</SYS_TX_VRSN>"
			+ "<SYS_TX_TYPE>00000</SYS_TX_TYPE>"
			+ "<SYS_RESERVED>AA</SYS_RESERVED>"
			+ "<SYS_EVT_TRACE_ID>yyyyMMddHHmmssms</SYS_EVT_TRACE_ID>"
			+ "<SYS_SND_SERIAL_NO>0000000023</SYS_SND_SERIAL_NO>"
			+ "<SYS_PKG_TYPE>1</SYS_PKG_TYPE>"
			+ "<SYS_MSG_LEN>100</SYS_MSG_LEN>"
			+ "<SYS_IS_ENCRYPTED>0</SYS_IS_ENCRYPTED>"
			+ "<SYS_ENCRYPT_TYPE>3</SYS_ENCRYPT_TYPE>"
			+ "<SYS_COMPRESS_TYPE>0</SYS_COMPRESS_TYPE>"
			+ "<SYS_EMB_MSG_LEN>0</SYS_EMB_MSG_LEN>"
			+ "<SYS_REQ_TIME>yyyyMMddHHmmssms</SYS_REQ_TIME>"
			+ "<SYS_TIME_LEFT>HHmmssms</SYS_TIME_LEFT>"
			+ "<SYS_PKG_STS_TYPE>00</SYS_PKG_STS_TYPE>"
			+ "</TX_HEADER>"
			+ "<TX_BODY>"
			+ "<COMMON>"
			+ "<COM1>"
			+ "<TXN_INSID>440000000</TXN_INSID>"
			+ "<TXN_ITT_CHNL_ID>0130</TXN_ITT_CHNL_ID>"
			+ "<TXN_ITT_CHNL_CGY_CODE>0001</TXN_ITT_CHNL_CGY_CODE>"
			+ "<TXN_DT>yyyyMMdd</TXN_DT>"
			+ "<TXN_TM>HHmmss</TXN_TM>"
			+ "<TXN_STFF_ID>999999</TXN_STFF_ID>"
			+ "<MULTI_TENANCY_ID>CN000</MULTI_TENANCY_ID>"
			+ "<LNG_ID>zh-cn</LNG_ID>"
			+ "</COM1>"
			+ "</COMMON>"
			+ "<ENTITY>"
			+ "<AUTH_TYPE>1</AUTH_TYPE>"
			+ "<AUTH_MODE>2</AUTH_MODE>"
			+ "<CUST_NAME>xzdong</CUST_NAME>"
			+ "<ID_NO>431281198907225619</ID_NO>"
			+ "<HITS>1</HITS>"
			+ "<THRESHOLD>0.88</THRESHOLD>"
			+ "<FEATURES>89821</FEATURES>"
			+ "<PICTURE></PICTURE>"
			+ "</ENTITY>" + "</TX_BODY>" + "<TX_EMB>" + "</TX_EMB>"
			+ "</TX>";
			static String str3="<?xml version='1.0' encoding='UTF-8'?>"
			+ "<TX>"
			+ "<TX_HEADER>"
			+ "<SYS_HDR_LEN>100</SYS_HDR_LEN>"
			+ "<SYS_PKG_VRSN>01</SYS_PKG_VRSN>"
			+ "<SYS_TTL_LEN>400</SYS_TTL_LEN>"
			+ "<SYS_REQ_SEC_ID>0123456789</SYS_REQ_SEC_ID>"
			+ "<SYS_SND_SEC_ID>9876543210</SYS_SND_SEC_ID>"
			+ "<SYS_TX_CODE>BiolServices</SYS_TX_CODE>"
			+ "<SYS_TX_VRSN>01</SYS_TX_VRSN>"
			+ "<SYS_TX_TYPE>00000</SYS_TX_TYPE>"
			+ "<SYS_RESERVED>AA</SYS_RESERVED>"
			+ "<SYS_EVT_TRACE_ID>yyyyMMddHHmmssms</SYS_EVT_TRACE_ID>"
			+ "<SYS_SND_SERIAL_NO>0000000023</SYS_SND_SERIAL_NO>"
			+ "<SYS_PKG_TYPE>1</SYS_PKG_TYPE>"
			+ "<SYS_MSG_LEN>100</SYS_MSG_LEN>"
			+ "<SYS_IS_ENCRYPTED>0</SYS_IS_ENCRYPTED>"
			+ "<SYS_ENCRYPT_TYPE>3</SYS_ENCRYPT_TYPE>"
			+ "<SYS_COMPRESS_TYPE>0</SYS_COMPRESS_TYPE>"
			+ "<SYS_EMB_MSG_LEN>0</SYS_EMB_MSG_LEN>"
			+ "<SYS_REQ_TIME>yyyyMMddHHmmssms</SYS_REQ_TIME>"
			+ "<SYS_TIME_LEFT>HHmmssms</SYS_TIME_LEFT>"
			+ "<SYS_PKG_STS_TYPE>00</SYS_PKG_STS_TYPE>"
			+ "</TX_HEADER>"
			+ "<TX_BODY>"
			+ "<COMMON>"
			+ "<COM1>"
			+ "<TXN_INSID>440000000</TXN_INSID>"
			+ "<TXN_ITT_CHNL_ID>0130</TXN_ITT_CHNL_ID>"
			+ "<TXN_ITT_CHNL_CGY_CODE>0001</TXN_ITT_CHNL_CGY_CODE>"
			+ "<TXN_DT>yyyyMMdd</TXN_DT>"
			+ "<TXN_TM>HHmmss</TXN_TM>"
			+ "<TXN_STFF_ID>999999</TXN_STFF_ID>"
			+ "<MULTI_TENANCY_ID>CN000</MULTI_TENANCY_ID>"
			+ "<LNG_ID>zh-cn</LNG_ID>"
			+ "</COM1>"
			+ "</COMMON>"
			+ "<ENTITY>"
			+ "<AUTH_TYPE>1</AUTH_TYPE>"
			+ "<AUTH_MODE>3</AUTH_MODE>"
			+
			// "<PICTURE1>12313</PICTURE1>" +
			// "<PICTURE2>4235345435</PICTURE2>" +
			"<CUST_NAME>xzdong</CUST_NAME>"
			+ "<ID_NO>431281198907225619</ID_NO>"
			+ "<HITS>3</HITS>"
			+ "<THRESHOLD>0.08</THRESHOLD>"
			+ "<FEATURES>89821</FEATURES>"
			+ "<PICTURE></PICTURE>"
			+ "</ENTITY>" + "</TX_BODY>" + "<TX_EMB>" + "</TX_EMB>"
			+ "</TX>";
			static String str4="<?xml version='1.0' encoding='UTF-8'?>"
					+ "<TX>"
					+ "<TX_HEADER>"
					+ "<SYS_HDR_LEN>100</SYS_HDR_LEN>"
					+ "<SYS_PKG_VRSN>01</SYS_PKG_VRSN>"
					+ "<SYS_TTL_LEN>400</SYS_TTL_LEN>"
					+ "<SYS_REQ_SEC_ID>0123456789</SYS_REQ_SEC_ID>"
					+ "<SYS_SND_SEC_ID>9876543210</SYS_SND_SEC_ID>"
					+ "<SYS_TX_CODE>BiolServices</SYS_TX_CODE>"
					+ "<SYS_TX_VRSN>01</SYS_TX_VRSN>"
					+ "<SYS_TX_TYPE>00000</SYS_TX_TYPE>"
					+ "<SYS_RESERVED>AA</SYS_RESERVED>"
					+ "<SYS_EVT_TRACE_ID>yyyyMMddHHmmssms</SYS_EVT_TRACE_ID>"
					+ "<SYS_SND_SERIAL_NO>0000000023</SYS_SND_SERIAL_NO>"
					+ "<SYS_PKG_TYPE>1</SYS_PKG_TYPE>"
					+ "<SYS_MSG_LEN>100</SYS_MSG_LEN>"
					+ "<SYS_IS_ENCRYPTED>0</SYS_IS_ENCRYPTED>"
					+ "<SYS_ENCRYPT_TYPE>3</SYS_ENCRYPT_TYPE>"
					+ "<SYS_COMPRESS_TYPE>0</SYS_COMPRESS_TYPE>"
					+ "<SYS_EMB_MSG_LEN>0</SYS_EMB_MSG_LEN>"
					+ "<SYS_REQ_TIME>yyyyMMddHHmmssms</SYS_REQ_TIME>"
					+ "<SYS_TIME_LEFT>HHmmssms</SYS_TIME_LEFT>"
					+ "<SYS_PKG_STS_TYPE>00</SYS_PKG_STS_TYPE>"
					+ "</TX_HEADER>"
					+ "<TX_BODY>"
					+ "<COMMON>"
					+ "<COM1>"
					+ "<TXN_INSID>440000000</TXN_INSID>"
					+ "<TXN_ITT_CHNL_ID>0130</TXN_ITT_CHNL_ID>"
					+ "<TXN_ITT_CHNL_CGY_CODE>0001</TXN_ITT_CHNL_CGY_CODE>"
					+ "<TXN_DT>yyyyMMdd</TXN_DT>"
					+ "<TXN_TM>HHmmss</TXN_TM>"
					+ "<TXN_STFF_ID>999999</TXN_STFF_ID>"
					+ "<MULTI_TENANCY_ID>CN000</MULTI_TENANCY_ID>"
					+ "<LNG_ID>zh-cn</LNG_ID>"
					+ "</COM1>"
					+ "</COMMON>"
					+ "<ENTITY>"
					+ "<PICTURE1></PICTURE1>"
					+ "<PICTURE2></PICTURE2>"
					+ "</ENTITY></TX_BODY><TX_EMB></TX_EMB>"
					+ "</TX>";
	/**
	 * @author guolin
	 * @date 2016-3-12\u4E0B\u53487:31:08
	 * @package_name test
	 * @project_name ctlUtils
	 * @version version.1.0
	 * @description \u5224\u65AD\u8EAB\u4EFD\u8BC1\u53F7\u662F\u5426\u5408\u6CD5
	 * @param strXml  strXml\u4E3A\u5B8C\u6210\u7684\u62A5\u6587
	 * @return
	 */
	public static boolean isCardIDRex(String strXml) {
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(JAXBDemoReadUtil.class);
			Unmarshaller u = context.createUnmarshaller();
			TXTest1 com = (TXTest1) u.unmarshal(new ByteArrayInputStream(strXml.getBytes()));
			String card_id = com.getTX_BODY().getENTITY().getID_NO();
			if (card_id != null && card_id.length() == 18) {
				char jy = getValidateCode(card_id.substring(0, 17));
				char jyorg = card_id.charAt(17);
				return jy == jyorg ? true : false;
			}
		} catch (JAXBException e) {
			return false;
		}
		return false;
	}

	/**
	 * @author guolin
	 * @date 2016-3-12\u4E0B\u53486:46:12
	 * @package_name test
	 * @project_name ctlUtils
	 * @version version.1.0
	 * @description
	 * @param xmlStr
	 *            xml\u5B57\u7B26\u4E32
	 * @param set
	 *            set\u53C2\u6570\u4ECE\u5BF9\u5E94\u7684\u9759\u6001\u7C7B\u83B7\u53D6\u4F8B\u5982Test1NodeSet.getNodeSet()\uFF0C
	 *            \u5982\u679C\u62A5\u6587\u4E2D\u7684xml\u8282\u70B9\u53D1\u751F\u53D8\u5316\u9700\u8981\u9700\u8981\u5728Test*NodeSet\u7684\u4E2D\u7684set\u9700\u8981\u53D8\u5316
	 * @return
	 */
	/*
	public static boolean XMLRex(String xmlStr, Set<String> set) {
		DocumentBuilder builder = null;
		// \u58F0\u660E\u4E00\u4E2A DocumentBuilderFactory\u5BF9\u8C61. \u901A\u8FC7\u5355\u4F8B\u6A21\u5F0F\u521B\u5EFA
		DocumentBuilderFactory builderFactory = null;
		builderFactory = DocumentBuilderFactory.newInstance();
		try {
			builderFactory.setValidating(true);
			builder = builderFactory.newDocumentBuilder();
			// \u89E3\u6790\u6587\u4EF6
			Document document = builder.parse(new ByteArrayInputStream(xmlStr
					.getBytes()));
			// \u83B7\u5F97\u6839\u5143\u7D20
			Element root = document.getDocumentElement();
			Set<String> setResult = new TreeSet<String>();
			set.add(root.getNodeName());
			System.out.println(root.getNodeName());
			NodeSet(root, setResult);
			setResult.remove("#text");
			// System.err.println("----");
			// System.out.println(set);
			// System.out.println(setResult);
			//
			// System.err.println("----");
			return set.equals(setResult) ? true : false;
		} catch (Exception e) {
			return false;
		}
	}
*/
	public static XmlValidateResult XMLRex(String xmlStr, int xsd) throws Exception {
		StringBuilder xsdStr=new StringBuilder(2048);
		Scanner scan=null;
		InputStream  input=null;
		try {
			input=JAXBDemoReadUtil.class.getResourceAsStream("/test"+xsd+".xsd");
			if(input!=null){
				scan=new Scanner(input);
			}
			if(scan!=null){
				while (scan.hasNext()) {
					xsdStr.append(scan.nextLine());
				}
			}
		} catch (Exception e) {
			try {
				throw e;
			} catch (Exception e1) {
			}
		}finally{
			if(scan!=null){
				scan.close();
			}
			if(input!=null){
				input.close();
			}
		}
		return XmlValidator.validateByXsd(xmlStr, xsdStr.toString());
	}
	
	public static Set<String> NodeSet(Node node, Set<String> set) {
		if (node == null) {
			return set;
		} else if (node != null) {
			set.add(node.getNodeName());
		}
		if (node.hasChildNodes()) {
			for (int i = 0; i < node.getChildNodes().getLength(); i++) {
				NodeSet(node.getChildNodes().item(i), set);
			}
		}
		return set;
	}
	
	
	public static void main(String[] args) throws Exception {
		for(int i=1;i<=4;i++){
				System.out.println(XMLRex(str1, i).isValidated());
		}
		for(int i=1;i<=4;i++){
			System.out.println(XMLRex(str2, i).isValidated());
		}
		for(int i=1;i<=4;i++){
			System.out.println(XMLRex(str3, i).isValidated());
		}
		for(int i=1;i<=4;i++){
			System.out.println(XMLRex(str4, i).isValidated());
		}
		
	}
}
