package com.optum.technology.minerva.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.PropertyValueException;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.optum.technology.minerva.entity.Category;
import com.optum.technology.minerva.entity.Email;
import com.optum.technology.minerva.entity.EmailDetails;
import com.optum.technology.minerva.entity.LineOfBusiness;
import com.optum.technology.minerva.entity.MinervaProperties;
import com.optum.technology.minerva.entity.UserInfo;
import com.optum.technology.minerva.entity.UserRole;
import com.optum.technology.minerva.service.CategoryService;
import com.optum.technology.minerva.service.EmailService;
import com.optum.technology.minerva.service.LoBService;
import com.optum.technology.minerva.service.PropertiesService;
import com.optum.technology.minerva.service.UserService;
import com.optum.technology.minerva.util.MinervaConstants;

@Controller
public class DropDownController extends CommonController {
	private final static Logger logger = LoggerFactory.getLogger(DropDownController.class);
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private LoBService lobService;

	@Autowired
	private PropertiesService propertiesService;

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@Value("${minerva.folder.path}")
	private String momFolder;

	@RequestMapping(value = "/getCategories", method = RequestMethod.POST)
	public @ResponseBody List<Category> getAllInquiry() {
		List<Category> subCategories = new ArrayList<Category>();
		try {
			subCategories = categoryService.getAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return subCategories;
	}

	@RequestMapping(value = "/getSubCategories", method = RequestMethod.POST)
	public @ResponseBody List<Category> getSubCategory(@RequestParam String cat) {

		List<Category> subCategories = new ArrayList<Category>();
		try {
			subCategories = categoryService.getSubCategory(cat);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return subCategories;
	}

	@RequestMapping(value = "/getProperties", method = RequestMethod.POST)
	public @ResponseBody List<MinervaProperties> getProperties() {
		List<MinervaProperties> props = new ArrayList<MinervaProperties>();
		try {
			props = propertiesService.getProperties();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return props;
	}

	@RequestMapping(value = "/updateProperties", method = RequestMethod.POST)
	public String updateProperties(@RequestParam String defaultPassword, @RequestParam String inquiryRecipients,
			@RequestParam String newDocRecipients, @RequestParam String appName, @RequestParam String hostName,
			@RequestParam String serverPort, @RequestParam String emailSender, @RequestParam String emailIdentity,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		logger.debug("[updateProperties]");
		UserInfo user = (UserInfo) request.getSession().getAttribute("employee");

		if (null == user) {
			redirectAttributes.addAttribute("isLoggedin", false);
			return MinervaConstants.LOGIN_PAGE;
		}
		try {
			propertiesService
					.updateProperties(new MinervaProperties(MinervaConstants.DEFAULT_PASSWORD, defaultPassword));
			propertiesService.updateProperties(
					new MinervaProperties(MinervaConstants.INQUIRY_EMAIL_RECIPIENT, inquiryRecipients));
			propertiesService.updateProperties(
					new MinervaProperties(MinervaConstants.DOCUMENT_EMAIL_RECIPIENT, newDocRecipients));
			propertiesService.updateProperties(new MinervaProperties(MinervaConstants.APPNAME, appName));

			propertiesService.updateProperties(new MinervaProperties(MinervaConstants.HOST_NAME, hostName));

			propertiesService.updateProperties(new MinervaProperties(MinervaConstants.SERVER_PORT, serverPort));

			propertiesService.updateProperties(new MinervaProperties(MinervaConstants.EMAIL_SENDER, emailSender));

			propertiesService.updateProperties(new MinervaProperties(MinervaConstants.EMAIL_IDENTITY, emailIdentity));

			UserInfo updater = ((UserInfo) request.getSession().getAttribute("employee"));
			StringBuilder htmlBody = new StringBuilder();
			htmlBody.append("Admins -- Cerebra properties were updated.");
			htmlBody.append("<br/> Action Date: " + new LocalDateTime());
			htmlBody.append("<br/> Performed By: " + updater.getFullName());
			EmailDetails emailDetails = getEmailDetails(propertiesService, false);
			String adminRoles = userService.getEmailRecipients(ADMIN_ROLE);
			notifyAdminsMain(htmlBody.toString(), emailDetails, adminRoles);

			redirectAttributes.addFlashAttribute("message", "You successfully updated the application properties.");
		} catch (SQLException e) {
			redirectAttributes.addFlashAttribute("message", "Update of application properties failed.");
		}
		return "redirect:" + MinervaConstants.ADMIN_PAGE;
	}

	@RequestMapping(value = "/deleteCategory", method = RequestMethod.POST)
	public String deleteCategory(@RequestParam int parentCat, @RequestParam int subCat, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("employee");

		if (null == user) {
			redirectAttributes.addAttribute("isLoggedin", false);
			return MinervaConstants.LOGIN_PAGE;
		}
		int id;
		if (parentCat == -2) {
			redirectAttributes.addFlashAttribute("message", "Please select a category.");
			return "redirect:" + MinervaConstants.ADMIN_PAGE;
		}
		try {
			if (subCat == -2) {
				id = parentCat;
			} else {
				id = subCat;
			}
			Category cat = categoryService.getCaterogy(id);
			categoryService.deleteCategory(cat);

			UserInfo updater = ((UserInfo) request.getSession().getAttribute("employee"));
			EmailDetails emailDetails = getEmailDetails(propertiesService, true);

			StringBuilder adminEmailBody = adminNotifyHtml("A new category was deleted", updater, cat);
			
			String adminRoles = userService.getEmailRecipients(ADMIN_ROLE);
			notifyAdminsMain(adminEmailBody.toString(), emailDetails, adminRoles);

			redirectAttributes.addFlashAttribute("message", "Category was deleted successfully.");
		} catch (DataIntegrityViolationException e) {
			redirectAttributes.addFlashAttribute("message",
					"Selected category has associated documents. Delete operation is denied." + e.getMessage());
		} catch (PropertyValueException pve) {
			redirectAttributes.addFlashAttribute("message",
					"Selected category has associated documents. Delete operation is denied." + pve.getMessage());
		} catch (SQLException e) {
			redirectAttributes.addFlashAttribute("message", "Delete operation is denied.");
		}
		return "redirect:" + MinervaConstants.ADMIN_PAGE;
	}

	@RequestMapping(value = "/addCategory", method = RequestMethod.POST)
	public String addCategory(@RequestParam String type, @RequestParam String name, @RequestParam String directoryName,
			@RequestParam String parentCat, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("employee");

		if (null == user) {
			redirectAttributes.addAttribute("isLoggedin", false);
			return MinervaConstants.LOGIN_PAGE;
		}
		try {
			Category cat = new Category();
			cat.setName(name);
			cat.setDirectoryName(directoryName);
			String folderDir = momFolder + "//" + directoryName + "//";
			if ("doc_cat".equals(type)) {
				File categoryPath = new File(folderDir);
				categoryPath.mkdir();
				cat.setParentCat("-1");
			} else if (("tools_ref").equals(type)) {
				File categoryPath = new File(folderDir);
				categoryPath.mkdir();
				cat.setParentCat("0");
			} else {
				cat.setParentCat(parentCat);
			}

			categoryService.addCategory(cat);

			UserInfo updater = ((UserInfo) request.getSession().getAttribute("employee"));
			EmailDetails emailDetails = getEmailDetails(propertiesService, true);

			StringBuilder adminEmailBody = adminNotifyHtml("A new category was added", updater, cat);
			
			String adminRoles = userService.getEmailRecipients(ADMIN_ROLE);
			notifyAdminsMain(adminEmailBody.toString(), emailDetails, adminRoles);

			redirectAttributes.addFlashAttribute("message", "Category (" + name + ") successfully added");

		} catch (SQLException e) {
			redirectAttributes.addFlashAttribute("message", "Failed in adding category (" + name + ")");
		}

		return "redirect:" + MinervaConstants.ADMIN_PAGE;
	}

	@RequestMapping(value = "/getInquirySMEs", method = RequestMethod.POST)
	public @ResponseBody List<UserInfo> getInquirySMEs() {
		List<UserInfo> smes = new ArrayList<UserInfo>();
		try {
			UserRole role = new UserRole();
			role.setRole(INQUIRY_ADMIN_ROLE);
			smes = userService.getUsersByRole(role);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return smes;
	}
	
	@RequestMapping(value = "/getLoBList", method = RequestMethod.POST)
	public @ResponseBody List<LineOfBusiness> getLoBList() {
		List<LineOfBusiness> lobList = new ArrayList<LineOfBusiness>();
		try {
			lobList = lobService.getAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lobList;
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