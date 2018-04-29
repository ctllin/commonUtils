package com.ctl.utils.mail;

import java.util.Date;    
import java.util.Properties;   
import javax.mail.Address;    
import javax.mail.BodyPart;    
import javax.mail.Message;    
import javax.mail.MessagingException;    
import javax.mail.Multipart;    
import javax.mail.Session;    
import javax.mail.Transport;    
import javax.mail.internet.InternetAddress;    
import javax.mail.internet.MimeBodyPart;    
import javax.mail.internet.MimeMessage;    
import javax.mail.internet.MimeMultipart;    
  
/**   
* \u7B80\u5355\u90AE\u4EF6\uFF08\u4E0D\u5E26\u9644\u4EF6\u7684\u90AE\u4EF6\uFF09\u53D1\u9001\u5668   
http://www.bt285.cn BT\u4E0B\u8F7D
*/    
public class SimpleMailSender  {    
/**   
  * \u4EE5\u6587\u672C\u683C\u5F0F\u53D1\u9001\u90AE\u4EF6   
  * @param mailInfo \u5F85\u53D1\u9001\u7684\u90AE\u4EF6\u7684\u4FE1\u606F   
  */    
    public boolean sendTextMail(MailSenderInfo mailInfo) {    
      // \u5224\u65AD\u662F\u5426\u9700\u8981\u8EAB\u4EFD\u8BA4\u8BC1    
      MyAuthenticator authenticator = null;    
      Properties pro = mailInfo.getProperties();
      //如果采用ssl必须有下面
      pro.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
      pro.setProperty("mail.smtp.socketFactory.fallback", "false");
      pro.setProperty("mail.smtp.socketFactory.port", mailInfo.getMailServerPort());
      if (mailInfo.isValidate()) {    
      // \u5982\u679C\u9700\u8981\u8EAB\u4EFD\u8BA4\u8BC1\uFF0C\u5219\u521B\u5EFA\u4E00\u4E2A\u5BC6\u7801\u9A8C\u8BC1\u5668    
        authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());    
      }   
      // \u6839\u636E\u90AE\u4EF6\u4F1A\u8BDD\u5C5E\u6027\u548C\u5BC6\u7801\u9A8C\u8BC1\u5668\u6784\u9020\u4E00\u4E2A\u53D1\u9001\u90AE\u4EF6\u7684session    
      Session sendMailSession = Session.getDefaultInstance(pro,authenticator);    
      try {
      // \u6839\u636Esession\u521B\u5EFA\u4E00\u4E2A\u90AE\u4EF6\u6D88\u606F
      Message mailMessage = new MimeMessage(sendMailSession);    
      // \u521B\u5EFA\u90AE\u4EF6\u53D1\u9001\u8005\u5730\u5740    
      Address from = new InternetAddress(mailInfo.getFromAddress());    
      // \u8BBE\u7F6E\u90AE\u4EF6\u6D88\u606F\u7684\u53D1\u9001\u8005    
      mailMessage.setFrom(from);    
      // \u521B\u5EFA\u90AE\u4EF6\u7684\u63A5\u6536\u8005\u5730\u5740\uFF0C\u5E76\u8BBE\u7F6E\u5230\u90AE\u4EF6\u6D88\u606F\u4E2D    
      Address to = new InternetAddress(mailInfo.getToAddress());    
      mailMessage.setRecipient(Message.RecipientType.TO,to);    
      // \u8BBE\u7F6E\u90AE\u4EF6\u6D88\u606F\u7684\u4E3B\u9898    
      mailMessage.setSubject(mailInfo.getSubject());    
      // \u8BBE\u7F6E\u90AE\u4EF6\u6D88\u606F\u53D1\u9001\u7684\u65F6\u95F4    
      mailMessage.setSentDate(new Date());    
      // \u8BBE\u7F6E\u90AE\u4EF6\u6D88\u606F\u7684\u4E3B\u8981\u5185\u5BB9    
      String mailContent = mailInfo.getContent();    
      mailMessage.setText(mailContent);    
      // \u53D1\u9001\u90AE\u4EF6    
      Transport.send(mailMessage);   
      return true;    
      } catch (MessagingException ex) {    
          ex.printStackTrace();    
      }    
      return false;    
    }    
       
    /**   
      * \u4EE5HTML\u683C\u5F0F\u53D1\u9001\u90AE\u4EF6   
      * @param mailInfo \u5F85\u53D1\u9001\u7684\u90AE\u4EF6\u4FE1\u606F   
      */    
    public boolean sendHtmlMail(MailSenderInfo mailInfo){    
      // \u5224\u65AD\u662F\u5426\u9700\u8981\u8EAB\u4EFD\u8BA4\u8BC1    
      MyAuthenticator authenticator = null;   
      Properties pro = mailInfo.getProperties();   
      //\u5982\u679C\u9700\u8981\u8EAB\u4EFD\u8BA4\u8BC1\uFF0C\u5219\u521B\u5EFA\u4E00\u4E2A\u5BC6\u7801\u9A8C\u8BC1\u5668     
      if (mailInfo.isValidate()) {    
        authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());   
      }    
      // \u6839\u636E\u90AE\u4EF6\u4F1A\u8BDD\u5C5E\u6027\u548C\u5BC6\u7801\u9A8C\u8BC1\u5668\u6784\u9020\u4E00\u4E2A\u53D1\u9001\u90AE\u4EF6\u7684session    
      Session sendMailSession = Session.getDefaultInstance(pro,authenticator);    
      try {    
      // \u6839\u636Esession\u521B\u5EFA\u4E00\u4E2A\u90AE\u4EF6\u6D88\u606F    
      Message mailMessage = new MimeMessage(sendMailSession);    
      // \u521B\u5EFA\u90AE\u4EF6\u53D1\u9001\u8005\u5730\u5740    
      Address from = new InternetAddress(mailInfo.getFromAddress());    
      // \u8BBE\u7F6E\u90AE\u4EF6\u6D88\u606F\u7684\u53D1\u9001\u8005    
      mailMessage.setFrom(from);    
      // \u521B\u5EFA\u90AE\u4EF6\u7684\u63A5\u6536\u8005\u5730\u5740\uFF0C\u5E76\u8BBE\u7F6E\u5230\u90AE\u4EF6\u6D88\u606F\u4E2D    
      Address to = new InternetAddress(mailInfo.getToAddress());    
      // Message.RecipientType.TO\u5C5E\u6027\u8868\u793A\u63A5\u6536\u8005\u7684\u7C7B\u578B\u4E3ATO    
      mailMessage.setRecipient(Message.RecipientType.TO,to);    
      // \u8BBE\u7F6E\u90AE\u4EF6\u6D88\u606F\u7684\u4E3B\u9898    
      mailMessage.setSubject(mailInfo.getSubject());    
      // \u8BBE\u7F6E\u90AE\u4EF6\u6D88\u606F\u53D1\u9001\u7684\u65F6\u95F4    
      mailMessage.setSentDate(new Date());    
      // MiniMultipart\u7C7B\u662F\u4E00\u4E2A\u5BB9\u5668\u7C7B\uFF0C\u5305\u542BMimeBodyPart\u7C7B\u578B\u7684\u5BF9\u8C61    
      Multipart mainPart = new MimeMultipart();    
      // \u521B\u5EFA\u4E00\u4E2A\u5305\u542BHTML\u5185\u5BB9\u7684MimeBodyPart    
      BodyPart html = new MimeBodyPart();    
      // \u8BBE\u7F6EHTML\u5185\u5BB9    
      html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");    
      mainPart.addBodyPart(html);    
      // \u5C06MiniMultipart\u5BF9\u8C61\u8BBE\u7F6E\u4E3A\u90AE\u4EF6\u5185\u5BB9    
      mailMessage.setContent(mainPart);    
      // \u53D1\u9001\u90AE\u4EF6    
      Transport.send(mailMessage);    
      return true;    
      } catch (MessagingException ex) {    
          ex.printStackTrace();    
      }    
      return false;    
    }    
}   

