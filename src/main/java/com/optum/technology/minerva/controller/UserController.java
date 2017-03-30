package com.optum.technology.minerva.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.optum.technology.minerva.entity.Email;
import com.optum.technology.minerva.entity.EmailDetails;
import com.optum.technology.minerva.entity.LoginTries;
import com.optum.technology.minerva.entity.UserInfo;
import com.optum.technology.minerva.entity.UserRole;
import com.optum.technology.minerva.service.EmailService;
import com.optum.technology.minerva.service.PropertiesService;
import com.optum.technology.minerva.service.UserService;
import com.optum.technology.minerva.util.GenericException;
import com.optum.technology.minerva.util.MinervaConstants;
import com.optum.technology.minerva.util.MinervaUtils;
import com.optum.technology.minerva.util.PasswordEncryptionService;

@Controller
public class UserController extends CommonController {

	@Autowired
	public UserService userService;

	@Autowired
	public EmailService emailService;

	@Autowired
	public PropertiesService propertiesService;

	private final static Logger logger = LoggerFactory.getLogger(UserController.class);

	private PasswordEncryptionService encryptionService = new PasswordEncryptionService();

	@ExceptionHandler(GenericException.class)
	public ModelAndView handleCustomException(GenericException ex) {

		ModelAndView model = new ModelAndView("error");
		model.addObject("errCode", ex.getErrCode());
		model.addObject("errMsg", ex.getErrMsg());

		return model;
	}

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public String addUser(@RequestParam String username, @RequestParam String empID, @RequestParam String lastname,
			@RequestParam String firstname, @RequestParam String email, @RequestParam String[] roles,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("employee");

		if (null == user) {
			redirectAttributes.addAttribute("isLoggedin", false);
			return MinervaConstants.LOGIN_PAGE;
		}
		try {
			UserInfo info = userService.getUser(username);
			String defaultPassword = propertiesService.getProperty(MinervaConstants.DEFAULT_PASSWORD).getValue();
			if (null != info) {
				redirectAttributes.addFlashAttribute("message", "Username already exists.");
				return "redirect:" + MinervaConstants.ADMIN_PAGE;
			}
			UserInfo employee = new UserInfo();

			employee.setEmail(email);
			employee.setEmpid(empID);
			employee.setEnabled(true);
			employee.setFirstName(firstname);
			employee.setLastName(lastname);
			byte[] saltBytes = encryptionService.generateSalt();
			employee.setSalt(saltBytes);
			employee.setPassword(encryptionService.getEncryptedPassword(defaultPassword, saltBytes));
			employee.setUsername(username);

			List<UserRole> roleNames = new ArrayList<UserRole>();
			for (String role : roles) {
				roleNames.add(new UserRole(employee.getUsername(), role));
			}

			userService.addUser(employee, roleNames);

			UserInfo updater = ((UserInfo) request.getSession().getAttribute("employee"));
			EmailDetails emailDetails = getEmailDetails(propertiesService, true);
			StringBuilder adminEmailBody = adminNotifyHtml("A new user was added", updater, employee);
			String adminRoles = userService.getEmailRecipients(ADMIN_ROLE);
			notifyAdminsMain(adminEmailBody.toString(), emailDetails, adminRoles);

			redirectAttributes.addFlashAttribute("message", "User successfully added");
		} catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			redirectAttributes.addFlashAttribute("message", "Failed in adding user.");
		}

		return "redirect:" + MinervaConstants.ADMIN_PAGE;
	}

	@RequestMapping(value = "/getAllUsernames", method = RequestMethod.POST)
	public @ResponseBody Set<String> getAllUsernames() {
		Set<String> usernames = new HashSet<String>();
		try {
			for (UserInfo info : userService.getAllUsers()) {
				usernames.add(info.getUsername());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new GenericException("SQL Exception", "Error caught during an SQL Operation.");
		}
		return usernames;
	}

	@RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
	public @ResponseBody UserInfo getUserInfo(@RequestParam String username) {
		UserInfo info = new UserInfo();
		try {
			info = userService.getUser(username);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new GenericException("SQL Exception", "Error caught during an SQL Operation.");
		}
		return info;
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public String updateUser(@RequestParam String username, @RequestParam String empID, @RequestParam String lastname,
			@RequestParam String firstname, @RequestParam String email, @RequestParam String[] roles,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("employee");

		if (null == user) {
			redirectAttributes.addAttribute("isLoggedin", false);
			return MinervaConstants.LOGIN_PAGE;
		}
		try {
			UserInfo employee = userService.getUser(username);

			employee.setEmail(email);

			employee.setEnabled(true);
			employee.setFirstName(firstname);
			employee.setLastName(lastname);

			List<UserRole> roleNames = new ArrayList<UserRole>();
			for (String role : roles) {
				roleNames.add(new UserRole(employee.getUsername(), role));
			}

			userService.updateUser(employee, roleNames);

			EmailDetails emailDetails = getEmailDetails(propertiesService, true);

			UserInfo updater = ((UserInfo) request.getSession().getAttribute("employee"));
			StringBuilder adminEmailBody = adminNotifyHtml("A user was updated.", updater, employee);
			String adminRoles = userService.getEmailRecipients(ADMIN_ROLE);
			notifyAdminsMain(adminEmailBody.toString(), emailDetails, adminRoles);

			redirectAttributes.addFlashAttribute("message", "User (" + username + ") successfully updated");
		} catch (SQLException e) {
			redirectAttributes.addFlashAttribute("message", "Failed in updating user (" + username + ")");
		}

		return "redirect:" + MinervaConstants.ADMIN_PAGE;
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	public String deleteUser(@RequestParam String username, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		UserInfo user = (UserInfo) request.getSession().getAttribute("employee");

		if (null == user) {
			redirectAttributes.addAttribute("isLoggedin", false);
			return MinervaConstants.LOGIN_PAGE;
		}

		try {
			UserInfo employee = userService.getUser(username);

			userService.deleteUser(employee);

			EmailDetails emailDetails = getEmailDetails(propertiesService, true);

			UserInfo updater = ((UserInfo) request.getSession().getAttribute("employee"));
			StringBuilder adminEmailBody = adminNotifyHtml("A user was deleted.", updater, employee);
			String adminRoles = userService.getEmailRecipients(ADMIN_ROLE);
			notifyAdminsMain(adminEmailBody.toString(), emailDetails, adminRoles);

			redirectAttributes.addFlashAttribute("message", "User (" + username + ") successfully deleted");
		} catch (SQLException e) {
			redirectAttributes.addFlashAttribute("message", "Failed in deleting user (" + username + ")");
		}

		return "redirect:" + MinervaConstants.ADMIN_PAGE;
	}

	@RequestMapping(value = "/deactivateUser", method = RequestMethod.POST)
	public String deactivateUser(@RequestParam String username, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("employee");

		if (null == user) {
			redirectAttributes.addAttribute("isLoggedin", false);
			return MinervaConstants.LOGIN_PAGE;
		}
		try {
			UserInfo employee = userService.getUser(username);

			userService.deactivateUser(employee);

			EmailDetails emailDetails = getEmailDetails(propertiesService, true);

			UserInfo updater = ((UserInfo) request.getSession().getAttribute("employee"));
			StringBuilder adminEmailBody = adminNotifyHtml("A user was deactivated.", updater, employee);
			String adminRoles = userService.getEmailRecipients(ADMIN_ROLE);
			notifyAdminsMain(adminEmailBody.toString(), emailDetails, adminRoles);

			redirectAttributes.addFlashAttribute("message", "User (" + username + ") successfully deactivated");
		} catch (SQLException e) {
			redirectAttributes.addFlashAttribute("message", "Failed in deactivating user (" + username + ")");
		}

		return "redirect:" + MinervaConstants.ADMIN_PAGE;
	}

	@RequestMapping(value = "/unlockUser", method = RequestMethod.POST)
	public String unlockUser(@RequestParam String username, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("employee");

		if (null == user) {
			redirectAttributes.addAttribute("isLoggedin", false);
			return MinervaConstants.LOGIN_PAGE;
		}
		try {
			UserInfo employee = userService.getUser(username);
			String defaultPassword = propertiesService.getProperty(MinervaConstants.DEFAULT_PASSWORD).getValue();
			employee.setPassword(encryptionService.getEncryptedPassword(defaultPassword, employee.getSalt()));
			employee.setEnabled(true);
			userService.updateUser(employee);

			EmailDetails emailDetails = getEmailDetails(propertiesService, true);

			UserInfo updater = ((UserInfo) request.getSession().getAttribute("employee"));

			StringBuilder adminEmailBody = adminNotifyHtml("Deactivated user is now active.", updater, employee);
			String adminRoles = userService.getEmailRecipients(ADMIN_ROLE);
			notifyAdminsMain(adminEmailBody.toString(), emailDetails, adminRoles);

			redirectAttributes.addFlashAttribute("message", "User (" + username + ") successfully unlocked");
		} catch (SQLException e) {
			redirectAttributes.addFlashAttribute("message", "Failed in unlocking user (" + username + ")");
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "redirect:" + MinervaConstants.ADMIN_PAGE;
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public String changePassword(@RequestParam String newPassString, @RequestParam String repeatPass,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("employee");

		if (null == user) {
			redirectAttributes.addAttribute("isLoggedin", false);
			return MinervaConstants.LOGIN_PAGE;
		}
		try {
			UserInfo emp = (UserInfo) request.getSession().getAttribute("employee");

			if (null == emp) {
				redirectAttributes.addAttribute("isLoggedin", false);
				return MinervaConstants.LOGIN_PAGE;
			}
			if (!newPassString.equals(repeatPass)) {
				redirectAttributes.addFlashAttribute("message", "Passwords don't match");
				return "redirect:" + MinervaConstants.ACCT_PAGE;
			}

			byte[] newPassword = encryptionService.getEncryptedPassword(newPassString, emp.getSalt());
			emp.setPassword(newPassword);
			userService.updateUser(emp);
			redirectAttributes.addFlashAttribute("message", "Change password is successful");
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "Password change is not successful.");
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

		return "redirect:" + MinervaConstants.ACCT_PAGE;
	}

	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public String getUser(@RequestParam String username, @RequestParam String password,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {

		if (username.isEmpty() && password.isEmpty()) {
			redirectAttributes.addFlashAttribute("isLoggedin", false);
			return MinervaConstants.LOGIN_PAGE;
		}

		logger.debug(" [attemping to auth] == username: " + username);
		UserInfo emp;
		try {
			emp = userService.getUser(username);

			if (null == emp) {
				redirectAttributes.addFlashAttribute("isLoggedin", false);
				redirectAttributes.addFlashAttribute("message", "Username or password is invalid");
				return "redirect:" + MinervaConstants.LOGIN_PAGE;
			}

			PasswordEncryptionService encryptionService = new PasswordEncryptionService();

			boolean isAuthenticated = encryptionService.authenticate(password, emp.getPassword(), emp.getSalt());

			LoginTries tries = userService.getLoginTries(emp.getUsername());
			if (null == tries) {
				tries = new LoginTries(emp.getUsername(), 1);
			}

			if (emp.isEnabled()) {
				if (!isAuthenticated) {
					logger.debug(username + " is authenticated");
					String addtnlMsg = checkIfLockedOut(emp, tries);

					redirectAttributes.addFlashAttribute("isLoggedin", false);
					redirectAttributes.addFlashAttribute("message", "Username or password is invalid. " + addtnlMsg);

					return "redirect:" + MinervaConstants.LOGIN_PAGE;
				} else {
					tries.setTries(0);
					userService.addLoginTries(tries);
				}
			} else {
				redirectAttributes.addFlashAttribute("isLoggedin", false);
				redirectAttributes.addFlashAttribute("message",
						"User account is locked. Please contact administrator.");

				return "redirect:" + MinervaConstants.LOGIN_PAGE;
			}

			redirectAttributes.addFlashAttribute("isLoggedin", true);
			redirectAttributes.addFlashAttribute("isAdmin", MinervaUtils.hasRole(emp, MinervaConstants.ROLE_ADMIN));
			redirectAttributes.addFlashAttribute("isRoleUploader",
					MinervaUtils.hasRole(emp, MinervaConstants.ROLE_UPLOADER));
			redirectAttributes.addFlashAttribute("isInquiryAdmin",
					MinervaUtils.hasRole(emp, MinervaConstants.ROLE_INQUIRY_ADMIN));
			HttpSession session = request.getSession();
			session.setAttribute("employee", emp);

		} catch (SQLException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

		String referrer = request.getHeader("referer");
		logger.debug("Referrer: " + referrer);

		if (referrer.contains(MinervaConstants.LOGOUT_URL) || referrer.contains(MinervaConstants.LOGIN_URL)) {
			return "redirect:" + MinervaConstants.HOME_URL;
		} else {
			return "redirect:" + referrer;
		}

	}

	private String checkIfLockedOut(UserInfo emp, LoginTries tries) throws SQLException {
		String addtnlMsg = "";

		if (tries.getTries() > 4) {
			emp.setEnabled(false);
			addtnlMsg = "Maximum tries exceeded. User account is locked.";
			userService.updateUser(emp);
		} else {
			tries.setTries(tries.getTries() + 1);
			userService.addLoginTries(tries);
		}

		return addtnlMsg;
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
