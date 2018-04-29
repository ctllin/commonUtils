package com.ctl.utils.mail;

public class MailMain {
	public static void main(String[] args) {
		for(int i=0;i<2;i++){
			SendMail();
		}
	}

	public static void SendMail1() {
		// \u8FD9\u4E2A\u7C7B\u4E3B\u8981\u662F\u8BBE\u7F6E\u90AE\u4EF6
		MailSenderInfo mailInfoGrgbanking = new MailSenderInfo();
		mailInfoGrgbanking.setMailServerHost("pop.qq.com");//\u53D1\u9001\u65B9\u7684\u670D\u52A1\u5668
		mailInfoGrgbanking.setMailServerPort("995");//\u53D1\u9001\u65B9\u7684\u670D\u52A1\u5668\u7AEF\u53E3
		mailInfoGrgbanking.setValidate(true);//\u662F\u5426\u9A8C\u8BC1\u5BC6\u7801
		mailInfoGrgbanking.setUserName("1083539025@qq.com");//\u53D1\u9001\u8005\u7684\u90AE\u7BB1
		mailInfoGrgbanking.setPassword("xingLin8913ctl");// \u53D1\u9001\u8005\u7684\u90AE\u7BB1\u5BC6\u7801
		mailInfoGrgbanking.setFromAddress("1083539025@qq.com");//\u53D1\u9001\u8005\u7684\u90AE\u7BB1\u5730\u5740
	  //  mailInfoGrgbanking.setToAddress("925109040@qq.com");
	    mailInfoGrgbanking.setToAddress("1603681398@qq.com");//\u63A5\u6536\u8005\u7684\u90AE\u7BB1\u5730\u5740
		mailInfoGrgbanking.setSubject("\u6D4B\u8BD5");//\u4E3B\u9898
		mailInfoGrgbanking.setContent("Ich liebe who!");//\u5185\u5BB9
		//mailInfoGrgbanking.setAttachFileNames(new String[]{"D:\\E盘\\Picture\\壁纸\\酷屏资源\\image\\174675.jpg"});
		// \u8FD9\u4E2A\u7C7B\u4E3B\u8981\u6765\u53D1\u9001\u90AE\u4EF6
		SimpleMailSender smsGrgbanking = new SimpleMailSender();
		smsGrgbanking.sendTextMail(mailInfoGrgbanking);// \u53D1\u9001\u6587\u4F53\u683C\u5F0F
		//smsGrgbanking.sendHtmlMail(mailInfoGrgbanking);// \u53D1\u9001html\u683C\u5F0F
		System.out.println("Grgbaking over");
		// \u8FD9\u4E2A\u7C7B\u4E3B\u8981\u662F\u8BBE\u7F6E\u90AE\u4EF6
	}
	
	public static void SendMail() {
		// \u8FD9\u4E2A\u7C7B\u4E3B\u8981\u662F\u8BBE\u7F6E\u90AE\u4EF6
		MailSenderInfo mailInfoGrgbanking = new MailSenderInfo();
		mailInfoGrgbanking.setMailServerHost("smtp.163.com");//\u53D1\u9001\u65B9\u7684\u670D\u52A1\u5668
		//public static String smtpPort = "465";//587 和 465都可以 因为开启了ssl所以使用这两个端口，否则是25
		mailInfoGrgbanking.setMailServerPort("465");//\u53D1\u9001\u65B9\u7684\u670D\u52A1\u5668\u7AEF\u53E3
		mailInfoGrgbanking.setValidate(true);//\u662F\u5426\u9A8C\u8BC1\u5BC6\u7801
		mailInfoGrgbanking.setUserName("guolinit@163.com");//\u53D1\u9001\u8005\u7684\u90AE\u7BB1
		//    # 授权码
		//    # 授权码是用于登录第三方邮件客户端的专用密码。
		//    # 适用于登录以下服务: POP3/IMAP/SMTP/Exchange/CardDAV/CalDAV服务。
		//    # Password = "liebe518"  # 口令 因为此处设置了授权码所以不使用登录密码使用授权码
		mailInfoGrgbanking.setPassword("liebe518");// \u53D1\u9001\u8005\u7684\u90AE\u7BB1\u5BC6\u7801
		mailInfoGrgbanking.setFromAddress("guolinit@163.com");//\u53D1\u9001\u8005\u7684\u90AE\u7BB1\u5730\u5740
	  //  mailInfoGrgbanking.setToAddress("925109040@qq.com");
	    mailInfoGrgbanking.setToAddress("guolin@shareco.cn");//\u63A5\u6536\u8005\u7684\u90AE\u7BB1\u5730\u5740
		mailInfoGrgbanking.setSubject("\u6D4B\u8BD5");//\u4E3B\u9898
		mailInfoGrgbanking.setContent("Ich liebe who!");//\u5185\u5BB9
		//mailInfoGrgbanking.setAttachFileNames(new String[]{"D:\\E盘\\Picture\\壁纸\\酷屏资源\\image\\174675.jpg"});
		// \u8FD9\u4E2A\u7C7B\u4E3B\u8981\u6765\u53D1\u9001\u90AE\u4EF6
		SimpleMailSender smsGrgbanking = new SimpleMailSender();
		smsGrgbanking.sendTextMail(mailInfoGrgbanking);// \u53D1\u9001\u6587\u4F53\u683C\u5F0F
		//smsGrgbanking.sendHtmlMail(mailInfoGrgbanking);// \u53D1\u9001html\u683C\u5F0F
		System.out.println("Grgbaking over");
		// \u8FD9\u4E2A\u7C7B\u4E3B\u8981\u662F\u8BBE\u7F6E\u90AE\u4EF6
	}
	public static void SendMail2() {
		// \u8FD9\u4E2A\u7C7B\u4E3B\u8981\u662F\u8BBE\u7F6E\u90AE\u4EF6
		MailSenderInfo mailInfoGrgbanking = new MailSenderInfo();
		mailInfoGrgbanking.setMailServerHost("hwimap.qiye.163.com");//\u53D1\u9001\u65B9\u7684\u670D\u52A1\u5668
		mailInfoGrgbanking.setMailServerPort("993");//\u53D1\u9001\u65B9\u7684\u670D\u52A1\u5668\u7AEF\u53E3
		
		
//		mailInfoGrgbanking.setMailServerHost("hwsmtp.qiye.163.com");//\u53D1\u9001\u65B9\u7684\u670D\u52A1\u5668
//		mailInfoGrgbanking.setMailServerPort("994");//\u53D1\u9001\u65B9\u7684\u670D\u52A1\u5668\u7AEF\u53E3
		
		mailInfoGrgbanking.setValidate(false);//\u662F\u5426\u9A8C\u8BC1\u5BC6\u7801
		mailInfoGrgbanking.setUserName("service@shareco.cn");//\u53D1\u9001\u8005\u7684\u90AE\u7BB1
		mailInfoGrgbanking.setPassword("xile99hangRoot");// \u53D1\u9001\u8005\u7684\u90AE\u7BB1\u5BC6\u7801
		mailInfoGrgbanking.setFromAddress("service@shareco.cn");//\u53D1\u9001\u8005\u7684\u90AE\u7BB1\u5730\u5740
	  //  mailInfoGrgbanking.setToAddress("925109040@qq.com");
	    mailInfoGrgbanking.setToAddress("guolin@shareco.cn");//\u63A5\u6536\u8005\u7684\u90AE\u7BB1\u5730\u5740
		mailInfoGrgbanking.setSubject("\u6D4B\u8BD5");//\u4E3B\u9898
		mailInfoGrgbanking.setContent("\u6D4B\u8BD5!");//\u5185\u5BB9
		//mailInfoGrgbanking.setAttachFileNames(new String[]{"D:\\E盘\\Picture\\壁纸\\酷屏资源\\image\\174675.jpg"});
		// \u8FD9\u4E2A\u7C7B\u4E3B\u8981\u6765\u53D1\u9001\u90AE\u4EF6
		SimpleMailSender smsGrgbanking = new SimpleMailSender();
		smsGrgbanking.sendTextMail(mailInfoGrgbanking);// \u53D1\u9001\u6587\u4F53\u683C\u5F0F
		//smsGrgbanking.sendHtmlMail(mailInfoGrgbanking);// \u53D1\u9001html\u683C\u5F0F
		System.out.println("Grgbaking over");
		// \u8FD9\u4E2A\u7C7B\u4E3B\u8981\u662F\u8BBE\u7F6E\u90AE\u4EF6
	}
	
}
