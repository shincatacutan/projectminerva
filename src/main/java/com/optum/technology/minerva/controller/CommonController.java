package com.optum.technology.minerva.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.EmailException;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;

import com.optum.technology.minerva.entity.Category;
import com.optum.technology.minerva.entity.Document;
import com.optum.technology.minerva.entity.EmailDetails;
import com.optum.technology.minerva.entity.Inquiry;
import com.optum.technology.minerva.entity.UserInfo;
import com.optum.technology.minerva.service.PropertiesService;
import com.optum.technology.minerva.util.EmailGenerator;
import com.optum.technology.minerva.util.MinervaConstants;
import com.optum.technology.minerva.util.MinervaUtils;

public abstract class CommonController {

	protected final String ADMIN_ROLE = "ROLE_ADMIN";
	protected final String INQUIRY_ADMIN_ROLE = "ROLE_INQUIRY_ADMIN";
	protected final String NOTIFY_ADMIN_SUBJECT = "Notify Admin";
	protected final static Logger logger = LoggerFactory.getLogger(CommonController.class);

	protected String checkSession(ModelMap model, HttpServletRequest request, String page) {
		UserInfo emp = (UserInfo) request.getSession().getAttribute("employee");

		if (null == emp) {
			model.addAttribute("isLoggedin", false);
			return MinervaConstants.LOGIN_PAGE;
		}
		model.addAttribute("isAdmin", MinervaUtils.hasRole(emp, MinervaConstants.ROLE_ADMIN));
		model.addAttribute("isRoleUploader", MinervaUtils.hasRole(emp, MinervaConstants.ROLE_UPLOADER));
		model.addAttribute("isInquiryAdmin", MinervaUtils.hasRole(emp, MinervaConstants.ROLE_INQUIRY_ADMIN));
		model.addAttribute("isLoggedin", true);
		return page;
	}

	public EmailDetails getEmailDetails(PropertiesService service, boolean isDocument) throws SQLException {
		EmailDetails emailDetails = new EmailDetails();
		emailDetails.setEmailIdentity(service.getProperty(MinervaConstants.EMAIL_IDENTITY).getValue());
		emailDetails.setEmailSender(service.getProperty(MinervaConstants.EMAIL_SENDER).getValue());
		if (isDocument) {
			emailDetails.setRecipient(service.getProperty(MinervaConstants.DOCUMENT_EMAIL_RECIPIENT).getValue());
		} else {
			emailDetails.setRecipient(service.getProperty(MinervaConstants.INQUIRY_EMAIL_RECIPIENT).getValue());
		}

		return emailDetails;
	}

	protected void emailAdmins(EmailDetails emailDetails, String htmlBody, String recipients)
			throws SQLException, EmailException {

		emailDetails.setSubject(NOTIFY_ADMIN_SUBJECT);
		emailDetails.setHtmlBody(htmlBody);
		emailDetails.setRecipient(recipients);
		new EmailGenerator().sendEmail(emailDetails);
	}

	protected void notifyAdminsMain(String html, EmailDetails addInqEmail, String adminRoles)
			throws SQLException {

		
		try {
			emailAdmins(addInqEmail, html, adminRoles);
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			retrySendingEmail(NOTIFY_ADMIN_SUBJECT, html, adminRoles, "admin", new LocalDateTime());
		}
	}

	protected StringBuilder adminNotifyHtml(String msg, UserInfo employee, Document doc) {
		StringBuilder htmlBody = new StringBuilder();
		htmlBody.append("Admins -- " + msg + "<br>Document Id: " + doc.getId());
		htmlBody.append("<br/> Document Title: " + doc.getTitle());
		htmlBody.append("<br/> Action Date: " + new LocalDateTime());
		htmlBody.append("<br/> Performed By: " + employee.getFullName());
		return htmlBody;
	}

	protected StringBuilder adminNotifyHtml(String msg, UserInfo employee, Inquiry inq) {
		StringBuilder htmlBody = new StringBuilder();
		htmlBody.append("Admins -- " + msg + "<br>Inquiry Id: " + inq.getId());
		htmlBody.append("<br/> Inquiry Title: " + inq.getTitle());
		htmlBody.append("<br/> Action Date: " + new LocalDateTime());
		htmlBody.append("<br/> Performed By: " + employee.getFullName());
		return htmlBody;
	}

	protected StringBuilder adminNotifyHtml(String msg, UserInfo employee, UserInfo affectedUser) {
		StringBuilder htmlBody = new StringBuilder();
		htmlBody.append("Admins -- " + msg + "<br>Employee Id: " + affectedUser.getEmpid());
		htmlBody.append("<br/> Employee name: " + affectedUser.getFullName());
		htmlBody.append("<br/> Action Date: " + new LocalDateTime());
		htmlBody.append("<br/> Performed By: " + employee.getFullName());
		return htmlBody;
	}

	protected StringBuilder adminNotifyHtml(String msg, UserInfo employee, Category cat) {
		StringBuilder htmlBody = new StringBuilder();
		htmlBody.append("Admins -- " + msg + "<br>Category Id: " + cat.getId());
		htmlBody.append("<br/> Category name: " + cat.getName());
		htmlBody.append("<br/> Action Date: " + new LocalDateTime());
		htmlBody.append("<br/> Performed By: " + employee.getFullName());
		return htmlBody;
	}

	protected abstract void retrySendingEmail(String emailSubject, String htmlBody, String recipients, String sender,
			LocalDateTime sendDate);

}
