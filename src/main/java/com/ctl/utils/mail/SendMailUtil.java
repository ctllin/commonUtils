package com.ctl.utils.mail;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class SendMailUtil {
    static Logger logger = LoggerFactory.getLogger(SendMailUtil.class);
    // 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
    // PS: 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）, 
    //     对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）。
    // public static String myEmailAccount = "service@shareco.cn";
    //public static String myEmailPassword = "xile99hangRoot";
    public static String myEmailAccount = "guolinit@163.com";
    //    # 授权码
    //    # 授权码是用于登录第三方邮件客户端的专用密码。
    //    # 适用于登录以下服务: POP3/IMAP/SMTP/Exchange/CardDAV/CalDAV服务。
    //    # Password = "liebe518"  # 口令 因为此处设置了授权码所以不使用登录密码使用授权码
    public static String myEmailPassword = "liebe518";
    public static String encode = "utf-8";
    // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
    // 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
    //public static String myEmailSMTPHost = "hwsmtp.qiye.163.com";
    // public static String smtpPort="994";
    public static String myEmailSMTPHost = "smtp.163.com";
    public static String smtpPort = "465";//587 和 465都可以 因为开启了ssl所以使用这两个端口，否则是25
    // 收件人邮箱（替换为自己知道的有效邮箱）
    public static String receiveMailAccount = "guolinit@163.com";

    public static void main(String[] args) throws Exception {
        SendMailUtil sendMailUtil = new SendMailUtil();
        sendMailUtil.sendMail(myEmailAccount, "JavaMail测试邮件", "test", new String[]{receiveMailAccount});
    }

    public void sendMail(String myEmailAccount, String subject, String content, String receiveMails[]) throws NoSuchProviderException, MessagingException, Exception {
        sendMail(myEmailAccount, subject, content, receiveMails, null, null);
    }

    public void sendMail(String mailAccount, String subject, String content, String receiveMails[], String ccMails[], String bccMails[]) throws Exception, NoSuchProviderException, MessagingException {

        if (StringUtils.isEmpty(mailAccount)) {
            logger.error("发件人为空，退出");
            return;
        }
        if (StringUtils.isEmpty(subject)) {
            logger.error("主题为空，退出");
            return;
        }
        if (StringUtils.isEmpty(mailAccount)) {
            logger.error("内容为空，退出");
            return;
        }
        if (null == receiveMails || receiveMails.length == 0) {
            logger.error("收件人为空，退出");
            return;
        }

        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        props.setProperty("mail.smtp.port", smtpPort);
        // PS: 某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证 (为了提高安全性, 邮箱支持SSL连接, 也可以自己开启),
        //     如果无法连接邮件服务器, 仔细查看控制台打印的 log, 如果有有类似 “连接失败, 要求 SSL 安全连接” 等错误,
        //     打开下面 /* ... */ 之间的注释代码, 开启 SSL 安全连接。
        /*
        // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
        //                  需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
        //                  QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
        final String smtpPort = "465";
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);
        */

        //没有下面三行报 could not connect to host "hwsmtp.qiye.163.com", port: 994, response: -1
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);

        //logger.info(myEmailAccount+":"+myEmailPassword);
        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        MyAuthenticator myauth = new MyAuthenticator(myEmailAccount, myEmailPassword);
        Session session = Session.getDefaultInstance(props, myauth);
        session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log

        // 3. 创建一封邮件
        MimeMessage message = createMimeMessage(session, subject, content, myEmailAccount, receiveMails, ccMails, bccMails);

        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();

        // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
        // 
        //    PS_01: 成败的判断关键在此一句, 如果连接服务器失败, 都会在控制台输出相应失败原因的 log,
        //           仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接, 根据给出的错误
        //           类型到对应邮件服务器的帮助网站上查看具体失败原因。
        //
        //    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
        //           (1) 邮箱没有开启 SMTP 服务;
        //           (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
        //           (3) 邮箱服务器要求必须要使用 SSL 安全连接;
        //           (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
        //           (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
        //
        //    PS_03: 仔细看log, 认真看log, 看懂log, 错误原因都在log已说明。
        transport.connect(myEmailAccount, myEmailPassword);

        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());

        // 7. 关闭连接
        transport.close();
    }

    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session     和服务器交互的会话
     * @param sendMail    发件人邮箱
     * @param receiveMail 收件人邮箱
     * @return
     * @throws Exception
     */
    public MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, "喜乐航服务中心", encode));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, receiveMail, encode));
        InternetAddress[] cc = new InternetAddress[1];
        cc[0] = new InternetAddress("guolin@shareco.cn", "guolin", encode);
        message.setRecipients(MimeMessage.RecipientType.CC, cc);//CC 英文全称是 Carbon Copy(抄送);
        InternetAddress[] bcc = new InternetAddress[1];
        bcc[0] = new InternetAddress("1083539025@qq.com", "1083539025", encode);
        message.setRecipients(MimeMessage.RecipientType.BCC, bcc);//BCC英文全称是 Blind CarbonCopy(暗抄送)。 
        // 4. Subject: 邮件主题
        message.setSubject("test", encode);

        // 5. Content: 邮件正文（可以使用html标签）
        message.setContent("用户你好, 测试邮件", "text/html;charset=" + encode);

        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存设置
        message.saveChanges();

        return message;
    }

    public MimeMessage createMimeMessage(Session session, String subject, String content, String sendMail, String receiveMails[]) throws Exception {
        return createMimeMessage(session, subject, content, sendMail, receiveMails, null, null);
    }

    public MimeMessage createMimeMessage(Session session, String subject, String content, String sendMail, String receiveMails[], String ccMails[]) throws Exception {
        return createMimeMessage(session, subject, content, sendMail, receiveMails, ccMails, null);
    }

    /**
     * @param session
     * @param subject      邮件主题
     * @param content:     邮件正文（可以使用html标签）
     * @param sendMail     邮件发送人
     * @param receiveMails 邮件接收人
     * @param ccMails      邮件抄送人
     * @param bccMails     邮件密送人
     * @return
     * @throws Exception
     */
    public MimeMessage createMimeMessage(Session session, String subject, String content, String sendMail, String receiveMails[], String ccMails[], String bccMails[]) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, sendMail, encode));
        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        InternetAddress[] to = new InternetAddress[receiveMails.length];
        for (int i = 0; i < to.length; i++) {
            to[i] = new InternetAddress(receiveMails[i], receiveMails[i], encode);
        }
        logger.info("message = " + message + "to = " + to);

        message.setRecipients(MimeMessage.RecipientType.TO, to);
        //抄送人
        if (ccMails != null && ccMails.length >= 1) {
            InternetAddress[] cc = new InternetAddress[ccMails.length];
            for (int i = 0; i < cc.length; i++) {
                cc[i] = new InternetAddress(ccMails[i], ccMails[i], encode);
            }
            message.setRecipients(MimeMessage.RecipientType.CC, cc);//CC 英文全称是 Carbon Copy(抄送);
        }

        //密送人
        if (bccMails != null && bccMails.length >= 1) {
            InternetAddress[] bcc = new InternetAddress[bccMails.length];
            for (int i = 0; i < bcc.length; i++) {
                bcc[i] = new InternetAddress(bccMails[i], bccMails[i], encode);
            }
            message.setRecipients(MimeMessage.RecipientType.BCC, bcc);//BCC英文全称是 Blind CarbonCopy(暗抄送)。
        }
        // 4. Subject: 邮件主题
        message.setSubject(subject, encode);
        // 5. Content: 邮件正文（可以使用html标签）
        message.setContent(content, "text/html;charset=UTF-8");
        //   message.setFileName("D:/E盘/document_shareco/永乐/永乐.txt");
        // 6. 设置发件时间
        message.setSentDate(new Date());
        // 7. 保存设置
        message.saveChanges();
        return message;
    }

    class MyAuthenticator extends javax.mail.Authenticator {
        private String strUser;

        private String strPwd;

        public MyAuthenticator(String user, String password) {
            this.strUser = user;
            this.strPwd = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(strUser, strPwd);
        }
    }

}