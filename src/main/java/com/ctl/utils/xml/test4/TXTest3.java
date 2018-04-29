package com.ctl.utils.xml.test4;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author guolin
 * @tel 18515287139
 * @date 2016-3-12下午3:57:35
 * @package_name
 * @project_name ctlUtils
 * @version version.1.0
 * @description
 */
@XmlRootElement(name = "TX")
public class TXTest3 {
	TXHeader TX_HEADER;
	TXEmb TX_EMB;
	TXBody TX_BODY;

	public TXHeader getTX_HEADER() {
		return TX_HEADER;
	}

	public void setTX_HEADER(TXHeader tX_HEADER) {
		TX_HEADER = tX_HEADER;
	}

	public TXEmb getTX_EMB() {
		return TX_EMB;
	}

	public void setTX_EMB(TXEmb tX_EMB) {
		TX_EMB = tX_EMB;
	}

	public TXBody getTX_BODY() {
		return TX_BODY;
	}

	public void setTX_BODY(TXBody tX_BODY) {
		TX_BODY = tX_BODY;
	}

}
