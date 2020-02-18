package com.account.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {

	public String sendEmail(String host, String fromEmail, String password, String toEmail, String port, String subject,
			String body) {
		Properties props = new Properties();
		props.put("mail.smtp.socketFactory.port", port); // SSL Port
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // SSL
		props.put("mail.smtp.auth", "true"); // Enabling SMTP Authentication
		props.put("mail.smtp.port", port); // SMTP Port

		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host); // SMTP Host

		Authenticator auth = new Authenticator() {
			// override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getInstance(props, auth);
		try {
			MimeMessage msg = new MimeMessage(session);
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			msg.setFrom(new InternetAddress(fromEmail));
			msg.setSubject(subject, "UTF-8");
			msg.setText(body, "UTF-8", "html");
			msg.setSentDate(new Date());
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			Transport.send(msg);
			return Constant.STATUS_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.STATUS_FAILURE;
		}
	}
}