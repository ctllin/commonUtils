package com.ctl.utils.io;

import java.io.*;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

/**
 * @author guolin
 * @date 2015-11-4下午3:31:53
 * @package_name NetFox
 * @project_name ctlUtils
 * @version version.1.0
 */
public class TestMethod {

	public TestMethod() { // /xx/weblogic60b2_win.exe
		try {
			SiteInfoBean bean = new SiteInfoBean("http://localhost/Download/download/data.exp", "d://temp","data.exp", 2);
			//bean=new SiteInfoBean("d://ctl.txt","D://FileLoc","ctl",4);
			// SiteInfoBean bean = new
			// SiteInfoBean(" http://localhost:8080/down.zip";;,"L://temp","weblogic60b2_win.exe",5);
			SiteFileFetch fileFetch = new SiteFileFetch(bean);
			fileFetch.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		new TestMethod();
		//createFile();
	}

	private static void createFile() throws IOException {
		Writer writer=new FileWriter(new File("D://ctl.txt"));
		//Wirter in=new FileInputStream(new File("D:////ctl.txt"));
		for(int i=0;i<=1000000;i++){
			writer.append(StringUtils.leftPad(String.valueOf(i), 8,"0"));
			writer.append("        ##");
			writer.append(UUID.randomUUID().toString());
			writer.append("\n");
			if(i%100==0){
				writer.flush();
			}
		}
		writer.close();
	}
}