package com.optum.technology.minerva.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.optum.technology.minerva.entity.Email;
import com.optum.technology.minerva.entity.UserInfo;
import com.optum.technology.minerva.service.EmailService;
import com.optum.technology.minerva.service.UserService;
import com.optum.technology.minerva.util.MinervaConstants;

@Controller
public class PageController extends CommonController {
	@Autowired
	public UserService userService;
	
	@Autowired
	public EmailService emailService;
	
	private final static Logger logger = LoggerFactory.getLogger(PageController.class);

	@RequestMapping(value = "/myaccount", method = RequestMethod.GET)
	public String viewAccount(ModelMap model, HttpServletRequest request) {
		return checkSession(model, request, MinervaConstants.ACCT_PAGE);
	}

	@RequestMapping(value = "/inquiry/view", method = RequestMethod.GET)
	public String viewInquiries(ModelMap model, HttpServletRequest request) {
		return checkSession(model, request, MinervaConstants.INQUIRY_VIEW);
	}

	@RequestMapping(value = "/inquiry/add", method = RequestMethod.GET)
	public String addInquiries(ModelMap model, HttpServletRequest request) {
		return checkSession(model, request, MinervaConstants.INQUIRY_ADD);
	}

	@RequestMapping(value = "/inquiry/report", method = RequestMethod.GET)
	public String reportInquiry(ModelMap model, HttpServletRequest request) {
		return checkSession(model, request, MinervaConstants.INQUIRY_REPORT);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String defaultHome(ModelMap model, HttpServletRequest request) {
		return checkSession(model, request, MinervaConstants.VIEW_INDEX);

	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String viewAdmin(ModelMap model, HttpServletRequest request) {
		return checkSession(model, request, MinervaConstants.ADMIN_PAGE);
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String viewLogin(ModelMap model, HttpServletRequest request) {
		return checkSession(model, request, MinervaConstants.LOGIN_PAGE);
	}

	@RequestMapping(value = "/library/search-tools", method = RequestMethod.GET)
	public String searchTools(ModelMap model, HttpServletRequest request) {
		return checkSession(model, request, MinervaConstants.TOOLS_PAGE);
	}

	@RequestMapping(value = "/library/add", method = RequestMethod.GET)
	public String addEntity(ModelMap model, HttpServletRequest request) {
		return checkSession(model, request, MinervaConstants.ADD_PAGE);
	}

	@RequestMapping(value = "/library/search", method = RequestMethod.GET)
	public String searchLibrary(ModelMap model, HttpServletRequest request) {
		return checkSession(model, request, MinervaConstants.VIEW_INDEX);
	}

	@RequestMapping(value = "/document/report", method = RequestMethod.GET)
	public String reportDocument(ModelMap model, HttpServletRequest request) {
		return checkSession(model, request, MinervaConstants.DOCUMENT_REPORT);
	}

	@RequestMapping(value = "/myunsent", method = RequestMethod.GET)
	public String viewUnsent(ModelMap model, HttpServletRequest request) {
		return checkSession(model, request, MinervaConstants.UNSENT_PAGE);
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String doLogout(ModelMap model, HttpServletRequest request) {
		request.setAttribute("employee", null);
		request.getSession().invalidate();
		model.addAttribute("message", "Logout successful");
		return MinervaConstants.LOGIN_PAGE;
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public String welcomeName(@PathVariable String name, ModelMap model, HttpServletRequest request) {
		return checkSession(model, request, MinervaConstants.VIEW_INDEX);
	}

	@RequestMapping(value = "/getAllUsers", method = RequestMethod.POST)
	public @ResponseBody List<UserInfo> getAllUsers() {

		logger.debug("[getAllUsers]");
		List<UserInfo> users = new ArrayList<UserInfo>();

		try {
			users = userService.getAllUsers();

		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

		return users;
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
			e.printStackTrace();
		}
	}

}
