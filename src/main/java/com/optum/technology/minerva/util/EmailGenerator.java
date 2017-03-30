package com.optum.technology.minerva.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.optum.technology.minerva.entity.EmailDetails;

public class EmailGenerator {
	
	protected final static Logger logger = LoggerFactory.getLogger(EmailGenerator.class);

	public void sendEmail(EmailDetails emailDetails) throws EmailException {

		String body = composeHTML(emailDetails.getHtmlBody()).toString();

		HtmlEmail email = new HtmlEmail();

		email.setHostName("mailo2.uhc.com");
		email.setSmtpPort(25);
		String recipientList[] = emailDetails.getRecipient().split(",");

		for (String r : recipientList) {
			email.addBcc(r);
//			email.addTo(r);
		}

		email.setFrom(emailDetails.getEmailSender(), emailDetails.getEmailIdentity());
		email.setSubject(emailDetails.getSubject());
		email.setHtmlMsg(body);

		email.setTextMsg("Your email client does not support HTML messages");

		// send
		String mimeID = email.send();

		logger.debug("Email sent to " + emailDetails.getRecipient() + ". Message ID: " + mimeID);

	}

	private StringBuilder composeHTML(String htmlBody) {
		StringBuilder builder = new StringBuilder();

		builder.append("<html>");
		builder.append(
				"<style> body{font-family: Consolas, sans-serif; font-size:12px;} "
				+ ".app-name{color:#DD4814; font-weight:bold;}</style>");
		builder.append("<body>");
		builder.append(htmlBody);

		builder.append("</body></html>");
		return builder;
	}

}
