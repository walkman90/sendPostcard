package com.appspot.sendmail.server;

import com.appspot.sendmail.client.SendMassageService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMassageImpl extends RemoteServiceServlet implements SendMassageService {

	/**
	 * Sending email (JavaMail)
	 */
	@Override
	public boolean sendMsg(String toEmail, String msgBody, String postcard) throws UnsupportedEncodingException {
		 Properties props = new Properties();
	       Session session = Session.getDefaultInstance(props);

	        try {
	            Message msg = new MimeMessage(session);
	            msg.setFrom(new InternetAddress("send.postcard1@gmail.com",
	            		"send.postcard1@gmail.com"));
	            msg.addRecipient(Message.RecipientType.TO,
	                             new InternetAddress(toEmail));
	            msg.setSubject("Greeting");
	            MimeBodyPart mbp1 = new MimeBodyPart();
	            mbp1.setText(msgBody);
	            mbp1.setContent(msgBody + 
	            		"<br /><br /><img src='http://sendpostcard1.appspot.com/images/"+ 
	            		postcard + "'/>", "text/html");
	            Multipart multipart = new MimeMultipart();
	            multipart.addBodyPart(mbp1);
	            msg.setContent(multipart); 
	            Transport.send(msg);
	    
	        } catch (AddressException e) {
	            System.out.println(e.getMessage());
	            return false;
	        } catch (MessagingException e) {
	        	 System.out.println(e.getMessage());
	        	 return false;
	        }
		return true;
	}
}
