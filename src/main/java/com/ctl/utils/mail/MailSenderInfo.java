package com.ctl.utils.mail;

/**   
* \u53D1\u9001\u90AE\u4EF6\u9700\u8981\u4F7F\u7528\u7684\u57FA\u672C\u4FE1\u606F 
*author by wangfun
http://www.5a520.cn \u5C0F\u8BF4520  
*/    
import java.util.Properties;    
public class MailSenderInfo {    
    // \u53D1\u9001\u90AE\u4EF6\u7684\u670D\u52A1\u5668\u7684IP\u548C\u7AEF\u53E3    
    private String mailServerHost;    
    private String mailServerPort = "25";    
    // \u90AE\u4EF6\u53D1\u9001\u8005\u7684\u5730\u5740    
    private String fromAddress;    
    // \u90AE\u4EF6\u63A5\u6536\u8005\u7684\u5730\u5740    
    private String toAddress;    
    // \u767B\u9646\u90AE\u4EF6\u53D1\u9001\u670D\u52A1\u5668\u7684\u7528\u6237\u540D\u548C\u5BC6\u7801    
    private String userName;    
    private String password;    
    // \u662F\u5426\u9700\u8981\u8EAB\u4EFD\u9A8C\u8BC1    
    private boolean validate = false;    
    // \u90AE\u4EF6\u4E3B\u9898    
    private String subject;    
    // \u90AE\u4EF6\u7684\u6587\u672C\u5185\u5BB9    
    private String content;    
    // \u90AE\u4EF6\u9644\u4EF6\u7684\u6587\u4EF6\u540D    
    private String[] attachFileNames;      
    /**   
      * \u83B7\u5F97\u90AE\u4EF6\u4F1A\u8BDD\u5C5E\u6027   
      */    
    public Properties getProperties(){    
      Properties p = new Properties();    
      p.put("mail.smtp.host", this.mailServerHost);    
      p.put("mail.smtp.port", this.mailServerPort);    
      p.put("mail.smtp.auth", validate ? "true" : "false");    
      return p;    
    }    
    public String getMailServerHost() {    
      return mailServerHost;    
    }    
    public void setMailServerHost(String mailServerHost) {    
      this.mailServerHost = mailServerHost;    
    }   
    public String getMailServerPort() {    
      return mailServerPort;    
    }   
    public void setMailServerPort(String mailServerPort) {    
      this.mailServerPort = mailServerPort;    
    }   
    public boolean isValidate() {    
      return validate;    
    }   
    public void setValidate(boolean validate) {    
      this.validate = validate;    
    }   
    public String[] getAttachFileNames() {    
      return attachFileNames;    
    }   
    public void setAttachFileNames(String[] fileNames) {    
      this.attachFileNames = fileNames;    
    }   
    public String getFromAddress() {    
      return fromAddress;    
    }    
    public void setFromAddress(String fromAddress) {    
      this.fromAddress = fromAddress;    
    }   
    public String getPassword() {    
      return password;    
    }   
    public void setPassword(String password) {    
      this.password = password;    
    }   
    public String getToAddress() {    
      return toAddress;    
    }    
    public void setToAddress(String toAddress) {    
      this.toAddress = toAddress;    
    }    
    public String getUserName() {    
      return userName;    
    }   
    public void setUserName(String userName) {    
      this.userName = userName;    
    }   
    public String getSubject() {    
      return subject;    
    }   
    public void setSubject(String subject) {    
      this.subject = subject;    
    }   
    public String getContent() {    
      return content;    
    }   
    public void setContent(String textContent) {    
      this.content = textContent;    
    }    
}   