package com.zzf.common.email;

import java.io.InputStream;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;


/**
 * 发送邮件
 */
public class EmailUtil {

//  protected static Logger logger = Logger.getLogger(EmailUtil.class);

	public static final String EMAIL_SEND_TYPE = "imap";//
	public static final String EMAIL_SEND_HOSTNAME = "smtp.qq.com";//发件服务器
	public static final String EMAIL_SEND = "25462123@qq.com";//发送人邮件地址
	public static final String EMAIL_SEND_USERNAME = "25462123@qq.com";//发送人用户名
	public static final String EMAIL_SEND_PASSWORD = "";//发送人邮件密码

	public static final String EMAIL_RECEIVE_TYPE = "imap";//
	public static final String EMAIL_RECEIVE_HOSTNAME = "imap.126.com";//收件服务器
	public static final String EMAIL_RECEIVE = "boyte2632@126.com";//接收人邮件
	public static final String EMAIL_RECEIVE_USERNAME = "boyte2632@126.com";//接收人用户名
	public static final String EMAIL_RECEIVE_PASSWORD = "";//接收人邮件密码

	//读取邮件模板中的内容，参数替换为实际的值
	private String getEmailContent() {
		try {
			InputStream in = EmailUtil.class.getResourceAsStream("email_template.html");
			return IOUtils.toString(in, "utf-8").replaceAll("param_code", "123456");
		} catch (Exception e) {
			return "";
		}
	}

	public void sendEmail(String toEmail) {
		try {
			//            logger.debug("---------------------------send email start----------------------------------------------------------------");
			Email email = new SimpleEmail();
			email.setCharset("utf-8");
			//            email.setSSL(true);
			//            email.setSslSmtpPort("465");
			email.setHostName(EMAIL_SEND_HOSTNAME);//
			email.setAuthenticator(new DefaultAuthenticator(EMAIL_SEND_USERNAME, EMAIL_SEND_PASSWORD));//pop3的不用后面的@yi-ma.com.cn作为用户名
			email.setFrom(EMAIL_SEND, "发件人");
			email.setSubject("邮件标题");
			email.setContent(getEmailContent(), "text/html; charset=utf-8");
			email.addTo(toEmail);
			email.send();
			//            logger.debug("---------------------------send email end----------------------------------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void receiveEmail() {
		try {
			//            logger.debug("---------------------------receive email start----------------------------------------------------------------");
			Properties props = System.getProperties();
			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore(EMAIL_RECEIVE_TYPE);
			store.connect(EMAIL_RECEIVE_HOSTNAME, EMAIL_RECEIVE_USERNAME, EMAIL_RECEIVE_PASSWORD);
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			javax.mail.Message[] msgs = folder.getMessages();
			for (int i = 0; i < msgs.length; i++) {
				//                logger.debug("---------------------------第"+(i+1)+"封邮件内容  start----------------------------------------------------------------");
				//                logger.debug(msgs[i].getContent());
				System.out.println(msgs[i].getContent());
				//                logger.debug("---------------------------第"+(i+1)+"封邮件内容  end----------------------------------------------------------------");
			}
			//            logger.debug("---------------------------send email end----------------------------------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		EmailUtil emailUtil = new EmailUtil();
		emailUtil.sendEmail(EMAIL_RECEIVE);
		//    	emailUtil.receiveEmail();
	}
}
