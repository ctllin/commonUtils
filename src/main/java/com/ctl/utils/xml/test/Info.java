package com.ctl.utils.xml.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Info {
	private String version;
	private String url;
	private String description;
	private String url_server;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl_server() {
		return url_server;
	}
	public void setUrl_server(String url_server) {
		this.url_server = url_server;
	}
	
	@Override
	public String toString() {
		return "Info [version=" + version + ", url=" + url + ", description="
				+ description + ", url_server=" + url_server + "]";
	}
	public static void main(String[] args) throws FileNotFoundException {
		    String xmlFilePath="src2/com/ctl/util/xmlRes/info.xml";
		//	readBookByJAXB(xmlFilePath,Articles.class);
			File xmlFile = new File(xmlFilePath);
			// ����JAXBContext�����Ķ���
			JAXBContext context;
			try {
				// ͨ��ָ��ӳ����ഴ��������
				context = JAXBContext.newInstance(Info.class);
				// ͨ�������Ĵ���xmlת��java�Ķ���Unmarshaller
				Unmarshaller u = context.createUnmarshaller();
				//ȡ�ø�Ԫ�ض�Ӧ��ʵ�������
				Info info = (Info) u.unmarshal(xmlFile);
				//content  ��ȡcontent
//				System.out.println("content:"+info.getVersion());
//				//get group
//				System.out.println("group ��Ϣ:"+articles.getGroups().getGroup().get(1).getPerson());
				//get article
				System.out.println(info);
				
				Marshaller  marshaller=context.createMarshaller();
				FileOutputStream output=new FileOutputStream(xmlFile);
				info.setVersion("3");
				marshaller.marshal(info, output);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
	}
}
