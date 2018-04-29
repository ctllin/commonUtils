package com.ctl.utils.xml.test2;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author guolin
 * @tel 18515287139
 * @date 2016-3-12下午5:53:09
 * @package_name test1
 * @project_name ctlUtils
 * @version version.1.0
 * @description
 */

public class Test2NodeSet {
	private static Set<String> set = new TreeSet<String>();
	//当test2.html中xmlstr节点发生变化那么该static中的set对应的需要修改，增加，删除
	static {
		set.add("AUTH_MODE");
		set.add("AUTH_TYPE");
		//set.add("CERT_NO");
		set.add("COM1");
		set.add("COMMON");
		set.add("CUST_NAME");
		set.add("ENTITY");
		set.add("FEATURES");
		set.add("HITS");
		set.add("ID_NO");
		set.add("LNG_ID");
		set.add("MULTI_TENANCY_ID");
		set.add("PICTURE");
		set.add("SYS_COMPRESS_TYPE");
		set.add("SYS_EMB_MSG_LEN");
		set.add("SYS_ENCRYPT_TYPE");
		set.add("SYS_EVT_TRACE_ID");
		set.add("SYS_HDR_LEN");
		set.add("SYS_IS_ENCRYPTED");
		set.add("SYS_MSG_LEN");
		set.add("SYS_PKG_STS_TYPE");
		set.add("SYS_PKG_TYPE");
		set.add("SYS_PKG_VRSN");
		set.add("SYS_REQ_SEC_ID");
		set.add("SYS_REQ_TIME");
		set.add("SYS_RESERVED");
		set.add("SYS_SND_SEC_ID");
		set.add("SYS_SND_SERIAL_NO");
		set.add("SYS_TIME_LEFT");
		set.add("SYS_TTL_LEN");
		set.add("SYS_TX_CODE");
		set.add("SYS_TX_TYPE");
		set.add("SYS_TX_VRSN");
		set.add("THRESHOLD");
		set.add("TX");
		set.add("TXN_DT");
		set.add("TXN_INSID");
		set.add("TXN_ITT_CHNL_CGY_CODE");
		set.add("TXN_ITT_CHNL_ID");
		set.add("TXN_STFF_ID");
		set.add("TXN_TM");
		set.add("TX_BODY");
		set.add("TX_EMB");
		set.add("TX_HEADER");
	}
	public static Set<String> getNodeSet() {
		return set;
	}
}
