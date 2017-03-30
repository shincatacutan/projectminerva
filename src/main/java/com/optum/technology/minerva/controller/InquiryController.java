package com.optum.technology.minerva.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.mail.EmailException;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.optum.technology.minerva.entity.Email;
import com.optum.technology.minerva.entity.EmailDetails;
import com.optum.technology.minerva.entity.Inquiry;
import com.optum.technology.minerva.entity.InquiryReply;
import com.optum.technology.minerva.entity.LineOfBusiness;
import com.optum.technology.minerva.entity.UserInfo;
import com.optum.technology.minerva.io.FileStreamGenerator;
import com.optum.technology.minerva.io.ReportGenerator;
import com.optum.technology.minerva.service.EmailService;
import com.optum.technology.minerva.service.InquiryService;
import com.optum.technology.minerva.service.LoBService;
import com.optum.technology.minerva.service.PropertiesService;
import com.optum.technology.minerva.service.UserService;
import com.optum.technology.minerva.util.EmailGenerator;
import com.optum.technology.minerva.util.GenericException;
import com.optum.technology.minerva.util.MinervaConstants;
import com.optum.technology.minerva.util.MinervaUtils;
import com.optum.technology.minerva.vo.InquiryVO;

@Controller
public class InquiryController extends CommonController {

	@Autowired
	public InquiryService inquiryService;

	@Autowired
	public PropertiesService propertiesService;

	@Autowired
	public LoBService lobService;

	@Autowired
	public UserService userService;

	@Autowired
	public EmailService emailService;

	@Autowired
	public ServletContext context;

	@Value("${minerva.folder.path}")
	public String momFolder;

	private final static Logger logger = LoggerFactory.getLogger(InquiryController.class);

	@ExceptionHandler(GenericException.class)
	public ModelAndView handleCustomException(GenericException ex) {

		ModelAndView model = new ModelAndView("error");
		model.addObject("errCode", ex.getErrCode());
		model.addObject("errMsg", ex.getErrMsg());

		return model;
	}

	@RequestMapping(value = "/addInquiry", method = RequestMethod.POST)
	public String addInquiry(@RequestParam String inqTitle, @RequestParam String inqBody, @RequestParam int lineOfBus,
			@RequestParam String fileName, @RequestParam MultipartFile selectedFile, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		logger.debug("[addInquiry] title: " + inqTitle + " body " + inqBody);
		Inquiry inquiry = new Inquiry();
		try {
			inquiry.setBody(inqBody);
			inquiry.setTitle(inqTitle);
			UserInfo employee = (UserInfo) request.getSession().getAttribute("employee");

			if (null == employee) {
				redirectAttributes.addAttribute("isLoggedin", false);
				return MinervaConstants.LOGIN_PAGE;
			}
			inquiry.setCreateUser(employee);
			inquiry.setCreateDate(new LocalDateTime());
			inquiry.setStatus((byte) 0);
			inquiry.setEnabled(true);
			inquiry.setUpdateDate(new LocalDateTime());
			inquiry.setUpdateUser(employee);
			LineOfBusiness lob = lobService.geLoB(lineOfBus);

			inquiry.setLineOfBusiness(lob);

			if (!selectedFile.isEmpty()) {

				String folderDir = momFolder + "//inquiries//";

				File inquiryPath = new File(folderDir);

				inquiryPath.mkdir();
				fileName = MinervaUtils.fileNameAppender(fileName, folderDir);
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(inquiryPath + "//" + fileName));
				FileCopyUtils.copy(selectedFile.getInputStream(), stream);
				stream.close();
				String fullPath = folderDir + fileName;

				inquiry.setPath(fullPath);

				inquiry.setFilename(fileName);
			}

			Inquiry newInquiry = inquiryService.addInquiry(inquiry);
			String htmlBody = inquiryHtmlBody(true, newInquiry);
			EmailDetails addInqEmail = getEmailDetails(propertiesService, false);
			String subject = "New Inquiry Post";
			addInqEmail.setSubject(subject);
			addInqEmail.setHtmlBody(htmlBody);
			try {
				new EmailGenerator().sendEmail(addInqEmail);

			} catch (EmailException e) {
				logger.error(e.getMessage(), e);
				e.printStackTrace();
				retrySendingEmail(subject, htmlBody, addInqEmail.getRecipient(), employee.getUsername(),
						new LocalDateTime());
			}

			// notify admins
			String msg = "New inquiry was posted";
			StringBuilder adminNotifEmail = adminNotifyHtml(msg, employee, inquiry);

			String adminRoles = userService.getEmailRecipients(ADMIN_ROLE);
			notifyAdminsMain(adminNotifEmail.toString(), addInqEmail, adminRoles);

			redirectAttributes.addFlashAttribute("message", "You successfully added an inquiry!");
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "Failed in saving the inquiry");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "Server error - IO Exception");
		}

		return "redirect:" + MinervaConstants.ADD_INQ_URL;
	}

	@RequestMapping(value = "/closeInquiry", method = RequestMethod.POST)
	public @ResponseBody String closeInquiry(@RequestParam int inquiryId, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		logger.debug("[closeInquiry] id: " + inquiryId);

		try {
			UserInfo employee = (UserInfo) request.getSession().getAttribute("employee");

			if (null == employee) {
				redirectAttributes.addAttribute("isLoggedin", false);
				return MinervaConstants.LOGIN_PAGE;
			}

			Inquiry inqToUpdate = inquiryService.getInquiry(inquiryId);
			inqToUpdate.setStatus((byte) 2);
			inqToUpdate.setUpdateDate(new LocalDateTime());
			inqToUpdate.setUpdateUser(employee);
			inquiryService.updateInquiry(inqToUpdate);

			String htmlBody = closeInquiryHtmlBody(inqToUpdate.getCreateUser(), inquiryId);
			EmailDetails closeInqEmail = getEmailDetails(propertiesService, false);
			String subject = "Inquiry has been Closed";
			closeInqEmail.setSubject(subject);
			closeInqEmail.setHtmlBody(htmlBody);
			closeInqEmail.setRecipient(inqToUpdate.getCreateUser().getEmail());
			try {
				new EmailGenerator().sendEmail(closeInqEmail);
			} catch (EmailException e) {
				logger.error(e.getMessage(), e);
				e.printStackTrace();
				retrySendingEmail(subject, htmlBody.toString(), closeInqEmail.getRecipient(), employee.getUsername(),
						new LocalDateTime());
			}

			// notify admins
			String msg = "An inquiry was closed";
			StringBuilder adminNotifEmail = adminNotifyHtml(msg, employee, inqToUpdate);

			String adminRoles = userService.getEmailRecipients(ADMIN_ROLE);
			notifyAdminsMain(adminNotifEmail.toString(), closeInqEmail, adminRoles);

		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "SUCCESS";
	}

	@RequestMapping(value = "/deleteInquiry", method = RequestMethod.POST)
	public @ResponseBody String deleteInquiry(@RequestParam int inquiryId, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		logger.debug("[delete] id: " + inquiryId);

		UserInfo user = (UserInfo) request.getSession().getAttribute("employee");

		if (null == user) {
			redirectAttributes.addAttribute("isLoggedin", false);
			return MinervaConstants.LOGIN_PAGE;
		}
		try {
			Inquiry todelete = inquiryService.getInquiry(inquiryId);

			inquiryService.deleteInquiry(todelete);

			EmailDetails deleteInqEmail = getEmailDetails(propertiesService, false);
			UserInfo employee = ((UserInfo) request.getSession().getAttribute("employee"));

			StringBuilder adminNotifEmail = adminNotifyHtml("An inquiry was deleted", employee, todelete);

			String adminRoles = userService.getEmailRecipients(ADMIN_ROLE);
			notifyAdminsMain(adminNotifEmail.toString(), deleteInqEmail, adminRoles);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "Delete inquiry encountered an error");
		}
		return "SUCCESS";
	}

	@RequestMapping(value = "/downloadRepAttach", method = RequestMethod.GET)
	public void downloadReplyAttach(HttpServletRequest request, HttpServletResponse response, @RequestParam int repId)
			throws IOException {
		String fullPath = "";

		try {

			fullPath = inquiryService.getInquiryReply(repId).getPath();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new GenericException("SQL Exception", "Error caught during an SQL Operation");
		}
		FileStreamGenerator.generate(response, fullPath, context);
	}

	@RequestMapping(value = "/downloadAttach", method = RequestMethod.GET)
	public void downloadAttach(HttpServletRequest request, HttpServletResponse response, @RequestParam int inqId)
			throws IOException {
		String fullPath = "";

		try {

			fullPath = inquiryService.getInquiry(inqId).getPath();
		} catch (NumberFormatException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new GenericException("Inquiry Exception", "Error in parsing inquiry id.");
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new GenericException("SQL Exception", "Error caught during an SQL Operation");
		}
		FileStreamGenerator.generate(response, fullPath, context);
	}

	@RequestMapping(value = "/getAllInquiry", method = RequestMethod.POST)
	public @ResponseBody List<InquiryVO> getAllInquiry(@RequestParam boolean getAll, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		UserInfo emp = (UserInfo) request.getSession().getAttribute("employee");
		if (null == emp) {
			response.sendRedirect(context.getContextPath() + "/login");
		}
		Set<Inquiry> inquiryList = new HashSet<Inquiry>();
		List<InquiryVO> returnList = new ArrayList<InquiryVO>();
		try {
			if (getAll) {
				inquiryList.addAll(inquiryService.viewAll());
			} else {
				inquiryList.addAll(inquiryService.getInquiry(emp));
			}

			for (Inquiry inquiry : inquiryList) {
				String sme = "";
				if (null != inquiry.getAssignedSME()) {
					sme = inquiry.getAssignedSME().getFullName();
				}
				returnList.add(new InquiryVO(String.valueOf(inquiry.getId()), inquiry.getTitle(), inquiry.getBody(),
						inquiry.getCreateDate().toString("MM/dd/YYYY hh:mm:ss"),
						inquiry.getCreateUser().getFirstName() + " " + inquiry.getCreateUser().getLastName(),
						String.valueOf(inquiry.getStatus()), MinervaUtils.getStatusEq(inquiry.getStatus()),
						String.valueOf(inquiry.isEnabled()), inquiry.getLineOfBusiness().getName(), inquiry.getPath(),
						sme));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new GenericException("SQL Exception", "Error caught during an SQL Operation");
		}
		return returnList;
	}

	@RequestMapping(value = "/addReply", method = RequestMethod.POST)
	public @ResponseBody String addReply(@RequestParam int inquiryId, @RequestParam String reply,
			@RequestParam String fileName, @RequestParam MultipartFile selectedFile, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		UserInfo emp = (UserInfo) request.getSession().getAttribute("employee");
		logger.debug("[addReply] inquiryId id :" + inquiryId);
		if (null == emp) {
			redirectAttributes.addAttribute("isLoggedin", false);
			return MinervaConstants.LOGIN_PAGE;
		}

		InquiryReply replyBody = new InquiryReply();
		try {

			if (null != replyBody && !"".equals(reply.trim())) {
				replyBody.setReplyBody(reply);
				replyBody.setCreateDate(new LocalDateTime());
				Inquiry inq = new Inquiry();
				inq.setId(inquiryId);
				replyBody.setInquiryId(inq);
				replyBody.setCreateUser(emp);

				if (!selectedFile.isEmpty()) {
					logger.debug("selected file: " + fileName);

					String folderDir = momFolder + "//inquiryReplies//";

					File inquiryPath = new File(folderDir);

					inquiryPath.mkdir();
					fileName = MinervaUtils.fileNameAppender(fileName, folderDir);
					BufferedOutputStream stream = new BufferedOutputStream(
							new FileOutputStream(inquiryPath + "//" + fileName));
					FileCopyUtils.copy(selectedFile.getInputStream(), stream);
					stream.close();
					String fullPath = folderDir + fileName;

					replyBody.setPath(fullPath);
					replyBody.setFilename(fileName);
				}
				inquiryService.addReply(replyBody);

				if (MinervaUtils.hasRole(emp, MinervaConstants.ROLE_INQUIRY_ADMIN)) {

					Inquiry inqToUpdate = inquiryService.getInquiry(inquiryId);
					inqToUpdate.setUpdateDate(new LocalDateTime());
					inqToUpdate.setUpdateUser(emp);
					inqToUpdate.setStatus((byte) 1);

					inquiryService.updateInquiry(inqToUpdate);

					String htmlBody = replyInquiryHtmlBody(inqToUpdate.getCreateUser(), inquiryId);
					EmailDetails addInqEmail = getEmailDetails(propertiesService, false);
					String subject = "New Reply To Inquiry";
					addInqEmail.setSubject(subject);
					addInqEmail.setHtmlBody(htmlBody);
					addInqEmail.setRecipient(inqToUpdate.getCreateUser().getEmail());

					try {
						new EmailGenerator().sendEmail(addInqEmail);

					} catch (EmailException e) {

						logger.error(e.getMessage(), e);
						e.printStackTrace();

						retrySendingEmail(subject, htmlBody, addInqEmail.getRecipient(), emp.getUsername(),
								new LocalDateTime());
					}

					// notify admins

					StringBuilder adminNotifEmail = adminNotifyHtml("A reply was added to inquiry.", emp, inqToUpdate);

					String adminRoles = userService.getEmailRecipients(ADMIN_ROLE);
					notifyAdminsMain(adminNotifEmail.toString(), addInqEmail, adminRoles);
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new GenericException("SQL Exception", "Error caught during an SQL Operation");
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "SUCCESS";
	}

	private String closeInquiryHtmlBody(UserInfo userInfo, int inquiryId) throws SQLException {
		String appName = propertiesService.getProperty(MinervaConstants.APPNAME).getValue();
		String hostname = propertiesService.getProperty(MinervaConstants.HOST_NAME).getValue();
		String serverPort = propertiesService.getProperty(MinervaConstants.SERVER_PORT).getValue();
		StringBuilder htmlBody = new StringBuilder();
		htmlBody.append("<p><strong>Dear " + userInfo.getFirstName());
		htmlBody.append(",</strong> <br/> <br/>");
		htmlBody.append("Your inquiry has been set to Closed status. <br/><br/>");
		htmlBody.append("<strong>Link</strong>: <a href='http://" + hostname + ":" + serverPort);
		htmlBody.append(context.getContextPath() + "/viewInquiry/" + inquiryId);
		htmlBody.append("'>http://" + hostname + ":" + serverPort + context.getContextPath());
		htmlBody.append("/viewInquiry/" + inquiryId + "</a><br/><br/>");
		htmlBody.append("Regards,<br/><span class='app-name'>" + appName + "</span></p>");
		return htmlBody.toString();
	}

	private String replyInquiryHtmlBody(UserInfo empInfo, int inqId) throws SQLException {
		String appName = propertiesService.getProperty(MinervaConstants.APPNAME).getValue();
		String hostname = propertiesService.getProperty(MinervaConstants.HOST_NAME).getValue();
		String serverPort = propertiesService.getProperty(MinervaConstants.SERVER_PORT).getValue();
		StringBuilder htmlBody = new StringBuilder();
		htmlBody.append("<p><strong>Dear " + empInfo.getFirstName());
		htmlBody.append(",</strong> <br/> <br/>");
		htmlBody.append("A reply was posted to your inquiry. <br/><br/>");
		htmlBody.append("<strong>Link</strong>: <a href='http://" + hostname + ":" + serverPort);
		htmlBody.append(context.getContextPath() + "/viewInquiry/" + inqId);
		htmlBody.append("'>http://" + hostname + ":" + serverPort + context.getContextPath());
		htmlBody.append("/viewInquiry/" + inqId + "</a><br/><br/>");
		htmlBody.append("Regards,<br/><span class='app-name'>" + appName + "</span></p>");
		return htmlBody.toString();
	}

	@RequestMapping(value = "/fetchReplies", method = RequestMethod.POST)
	public @ResponseBody List<InquiryReply> fetchReplies(@RequestParam int inquiryId, HttpServletRequest request) {
		logger.debug("[fetching reply for] id: " + inquiryId);

		List<InquiryReply> sortedList = new ArrayList<InquiryReply>();

		try {
			sortedList.addAll(inquiryService.getInquiry(inquiryId).getReplies());
			Collections.sort(sortedList, new Comparator<InquiryReply>() {
				@Override
				public int compare(InquiryReply o1, InquiryReply o2) {
					if (o1.getCreateDate().equals(o2.getCreateDate()))
						return 0;
					return o2.getCreateDate().compareTo(o1.getCreateDate());
				}
			});
		} catch (SQLException e) {
			throw new GenericException("SQL Exception", "Error caught during an SQL Operation");
		}
		return sortedList;
	}

	@RequestMapping(value = "/searchInquiry", method = RequestMethod.POST)
	public @ResponseBody List<InquiryVO> searchInquiry(@RequestParam String startDate, @RequestParam String endDate,
			@RequestParam String lineOfBus) {

		LineOfBusiness lob = new LineOfBusiness();

		if (!StringUtils.isEmpty(lineOfBus)) {
			lob.setId(Integer.parseInt(lineOfBus));
		}

		List<Inquiry> inquiryList = new ArrayList<Inquiry>();
		List<InquiryVO> returnList = new ArrayList<InquiryVO>();
		try {
			inquiryList = inquiryService.getInquiryByDates(startDate, endDate, lob);

			for (Inquiry inquiry : inquiryList) {
				String sme = "";
				if (null != inquiry.getAssignedSME()) {
					sme = inquiry.getAssignedSME().getFullName();
				}
				returnList.add(new InquiryVO(String.valueOf(inquiry.getId()), inquiry.getTitle(), inquiry.getBody(),
						inquiry.getCreateDate().toString("MM/dd/YYYY hh:mm:ss"),
						inquiry.getCreateUser().getFirstName() + " " + inquiry.getCreateUser().getLastName(),
						String.valueOf(inquiry.getStatus()), MinervaUtils.getStatusEq(inquiry.getStatus()),
						String.valueOf(inquiry.isEnabled()), inquiry.getLineOfBusiness().getName(), inquiry.getPath(),
						sme));
			}
		} catch (SQLException e) {

			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

		return returnList;
	}

	@RequestMapping(value = "/downloadReport", method = RequestMethod.POST)
	public void doDownload(HttpServletRequest request, HttpServletResponse response, @RequestParam String startDate,
			@RequestParam String endDate, @RequestParam String lineOfBus) throws IOException {
		List<Inquiry> inquiryList = new ArrayList<Inquiry>();
		List<InquiryReply> replyList = new ArrayList<InquiryReply>();
		try {
			String momPath = momFolder + "//logs//";
			LineOfBusiness lob = new LineOfBusiness();

			if (!StringUtils.isEmpty(lineOfBus)) {
				lob.setId(Integer.parseInt(lineOfBus));
			}

			inquiryList = inquiryService.getInquiryByDates(startDate, endDate, lob);
			replyList = inquiryService.getInquiryReplyByInq(startDate, endDate, lob);
			String fileName = new ReportGenerator().generate(inquiryList, replyList, momPath);
			FileStreamGenerator.generate(response, fileName, context);
		} catch (NumberFormatException e) {

			logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new GenericException("Inquiry Exception", "Error in parsing inquiry id.");
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new GenericException("SQL Exception", "Error caught during an SQL Operation.");
		}

	}

	@RequestMapping(value = "/viewInquiry/{inqId}", method = RequestMethod.GET)
	public String viewInquiry(@PathVariable("inqId") int inqId, ModelMap model, HttpServletRequest request) {
		try {

			Inquiry inq = inquiryService.getInquiry(inqId);

			if (null == inq) {
				throw new GenericException("Inquiry Exception", "Inquiry is not found.");
			}
			Set<InquiryReply> replies = new HashSet<InquiryReply>();
			replies.addAll(inquiryService.viewReplies(inqId));
			UserInfo emp = (UserInfo) request.getSession().getAttribute("employee");

			if (null == emp) {
				model.addAttribute("isLoggedin", false);
				return MinervaConstants.LOGIN_PAGE;
			}
			boolean isUploader = false;
			if (emp.getEmpid().equals(inq.getCreateUser().getEmpid())) {
				isUploader = true;
			}

			model.addAttribute("inquiry", inq);
			model.addAttribute("replies", replies);
			model.addAttribute("isAdmin", MinervaUtils.hasRole(emp, MinervaConstants.ROLE_ADMIN));
			model.addAttribute("isRoleUploader", MinervaUtils.hasRole(emp, MinervaConstants.ROLE_UPLOADER));
			model.addAttribute("isUploader", isUploader);
			model.addAttribute("isInquiryAdmin", MinervaUtils.hasRole(emp, MinervaConstants.ROLE_INQUIRY_ADMIN));
			model.addAttribute("isLoggedin", true);

		} catch (NumberFormatException | SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new GenericException("Inquiry Exception", "Error in parsing inquiry id.");
		}
		return MinervaConstants.VIEW_INQ_PAGE;
	}

	@RequestMapping(value = "/updateInqLOB", method = RequestMethod.POST)
	public String updateInqLOB(@RequestParam int inqToChange, @RequestParam String newLob, ModelMap model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {

		String referrer = request.getHeader("referer");
		UserInfo employee = (UserInfo) request.getSession().getAttribute("employee");

		if (null == employee) {
			redirectAttributes.addAttribute("isLoggedin", false);
			return MinervaConstants.LOGIN_PAGE;
		}
		try {
			Inquiry inq = inquiryService.getInquiry(inqToChange);

			if (null == inq) {
				throw new GenericException("Inquiry Exception", "Inquiry is not found.");
			}

			UserInfo sme = userService.getUser(newLob);
			inq.setAssignedSME(sme);
			inq.setUpdateDate(new LocalDateTime());
			inq.setUpdateUser(employee);
			inquiryService.updateInquiry(inq);

			String htmlBody = inquiryHtmlBody(false, inq);

			EmailDetails updateInqEmail = getEmailDetails(propertiesService, false);
			String subject = "Inquiry SME Assignment";
			updateInqEmail.setSubject(subject);
			updateInqEmail.setHtmlBody(htmlBody);
			updateInqEmail.setRecipient(sme.getEmail());
			// send to all
			try {
				new EmailGenerator().sendEmail(updateInqEmail);
			} catch (EmailException e) {
				logger.error(e.getMessage(), e);
				e.printStackTrace();
				retrySendingEmail(subject, htmlBody, updateInqEmail.getRecipient(), employee.getUsername(),
						new LocalDateTime());
			}

			// notify admin

			String adminSubject = "Inquiry has been assigned to an SME";
			StringBuilder adminNotifEmail = adminNotifyHtml(adminSubject, employee, inq);
			String recipients = userService.getEmailRecipients(ADMIN_ROLE);
			try {
				emailAdmins(updateInqEmail, adminNotifEmail.toString(), recipients);
			} catch (EmailException e) {
				logger.error(e.getMessage(), e);
				e.printStackTrace();
				retrySendingEmail(NOTIFY_ADMIN_SUBJECT, adminNotifEmail.toString(), recipients, "admin",
						new LocalDateTime());
			}

			redirectAttributes.addFlashAttribute("message", "SME assignment is successful");
		} catch (NumberFormatException | SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new GenericException("Inquiry Exception", "Error in parsing inquiry id.");
		}
		return "redirect:" + referrer;
	}

	private String inquiryHtmlBody(boolean isNew, Inquiry inq) throws SQLException {
		String appName = propertiesService.getProperty(MinervaConstants.APPNAME).getValue();
		String hostname = propertiesService.getProperty(MinervaConstants.HOST_NAME).getValue();
		String serverPort = propertiesService.getProperty(MinervaConstants.SERVER_PORT).getValue();

		String msg = "";
		if (isNew) {
			msg = "A new inquiry was posted in ";
		} else {
			msg = "An inquiry has been assigned to you in  ";
		}
		StringBuilder htmlBody = new StringBuilder();
		htmlBody.append("<p><strong>Dear SME,</strong> <br/> <br/>");
		htmlBody.append(msg);
		htmlBody.append("<span class='app-name'>" + appName + "</span>. <br/><br/>");
		htmlBody.append("<strong>Inquiry Title</strong>: " + inq.getTitle() + "<br/>");
		htmlBody.append("<strong>Line of Business</strong>: " + inq.getLineOfBusiness().getName() + "<br/>");
		if (!isNew) {
			htmlBody.append("<strong>Assigned SME</strong>: " + inq.getAssignedSME().getFullName() + "<br/>");
		}
		htmlBody.append("<strong>Sender</strong>: " + inq.getCreateUser().getFullName() + "</br>");
		htmlBody.append("<strong>Link</strong>: <a href='http://" + hostname + ":" + serverPort
				+ context.getContextPath() + "/viewInquiry/" + inq.getId() + "'>");
		htmlBody.append(
				"http://" + hostname + ":" + serverPort + context.getContextPath() + "/viewInquiry/" + inq.getId());
		htmlBody.append("</a><br/><br/>");
		htmlBody.append("Regards,<br/><span class='app-name'>" + appName + "</span></p>");
		return htmlBody.toString();
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
