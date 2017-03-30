package com.optum.technology.minerva.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.mail.EmailException;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.optum.technology.minerva.entity.Email;
import com.optum.technology.minerva.entity.EmailDetails;
import com.optum.technology.minerva.entity.UserInfo;
import com.optum.technology.minerva.service.EmailService;
import com.optum.technology.minerva.service.PropertiesService;
import com.optum.technology.minerva.util.EmailGenerator;
import com.optum.technology.minerva.util.MinervaConstants;

@Controller
public class UnsentEmailController extends CommonController{

	@Autowired
	public EmailService emailService;
	
	@Autowired
	public ServletContext context;
	
	@Autowired
	private PropertiesService propertiesService;
	
	private final static Logger logger = LoggerFactory.getLogger(UnsentEmailController.class);
	
	@RequestMapping(value = "/getUnsentEmail", method = RequestMethod.POST)
	public @ResponseBody List<Email> getUnsentEmail( HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		UserInfo emp = (UserInfo) request.getSession().getAttribute("employee");
		if (null == emp) {
			response.sendRedirect(context.getContextPath() + "/login");
		}
		List<Email> unsentEmails = new ArrayList<>();
		try {
			unsentEmails = emailService.getUnsentEmails();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
		return unsentEmails;
	}
	

	@RequestMapping(value = "/getUnsentEmailByUser", method = RequestMethod.POST)
	public @ResponseBody List<Email> getUnsentEmailByEmail( HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		UserInfo emp = (UserInfo) request.getSession().getAttribute("employee");
		if (null == emp) {
			response.sendRedirect(context.getContextPath() + "/login");
		}
		logger.debug("[getUnsentEmailByUser] "+emp.getUsername());
		List<Email> unsentEmails = new ArrayList<>();
		try {
			unsentEmails = emailService.getUnsentByUsername(emp.getUsername());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
		return unsentEmails;
	}
	
	@RequestMapping(value = "/sendFailedEmail", method = RequestMethod.POST)
	public @ResponseBody String sendFailedEmail(@RequestParam int emailId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		logger.debug("[sendFailedEmail] id: " + emailId);
		
		try {
			UserInfo employee = (UserInfo) request.getSession().getAttribute("employee");

			if (null == employee) {
				redirectAttributes.addAttribute("isLoggedin", false);
				return MinervaConstants.LOGIN_PAGE;
			}
			Email emailDetail = emailService.getEmail(emailId);
		
			String htmlBody = emailDetail.getBody();
			EmailDetails emailToSend = getEmailDetails(propertiesService, false);
			emailToSend.setSubject(emailDetail.getTitle());
			emailToSend.setHtmlBody(htmlBody);
			emailToSend.setRecipient(emailDetail.getRecipient());
			new EmailGenerator().sendEmail(emailToSend);

			emailService.deleteUnsentEmail(emailDetail);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (EmailException e) {
			logger.error(e.getMessage(), e);
			logger.debug("Email was not sent. Email will be on the unsent queue.");
			return "FAILED";
		}
		return "SUCCESS";
	}

	@Override
	protected void retrySendingEmail(String emailSubject, String htmlBody, String recipients, String sender,
			LocalDateTime sendDate) {
		try {
			Email email = new Email();
			email.setBody(htmlBody);
			email.setRecipient(recipients);
			email.setStatus("FAILED");
			email.setTitle(emailSubject);
			email.setSender(sender);
			email.setTimeSent(sendDate);
			emailService.addUnsentEmail(email);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
