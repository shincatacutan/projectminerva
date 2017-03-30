package com.optum.technology.minerva.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.mail.EmailException;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
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
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.optum.technology.minerva.entity.Document;
import com.optum.technology.minerva.entity.Email;
import com.optum.technology.minerva.entity.EmailDetails;
import com.optum.technology.minerva.entity.LineOfBusiness;
import com.optum.technology.minerva.entity.UserInfo;
import com.optum.technology.minerva.io.FileStreamGenerator;
import com.optum.technology.minerva.io.ReportGenerator;
import com.optum.technology.minerva.service.CategoryService;
import com.optum.technology.minerva.service.DocumentService;
import com.optum.technology.minerva.service.EmailService;
import com.optum.technology.minerva.service.PropertiesService;
import com.optum.technology.minerva.service.UserService;
import com.optum.technology.minerva.util.EmailGenerator;
import com.optum.technology.minerva.util.GenericException;
import com.optum.technology.minerva.util.MinervaConstants;
import com.optum.technology.minerva.util.MinervaUtils;
import com.optum.technology.minerva.vo.DocumentVO;

@Controller
public class DocumentController extends CommonController {
	@Value("${minerva.folder.path}")
	public String momFolder;
	@Autowired
	public ServletContext context;

	@Autowired
	public DocumentService docService;

	@Autowired
	public CategoryService catService;

	@Autowired
	public UserService userService;

	@Autowired
	public EmailService emailService;

	@ExceptionHandler(GenericException.class)
	public ModelAndView handleCustomException(GenericException ex) {

		ModelAndView model = new ModelAndView("error");
		model.addObject("errCode", ex.getErrCode());
		model.addObject("errMsg", ex.getErrMsg());

		return model;
	}

	@Autowired
	private PropertiesService propertiesService;

	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	public String handleFileUpload(@RequestParam String title, @RequestParam String categoryName,
			@RequestParam String subCategory, @RequestParam String detailedInfo, @RequestParam String tags,
			@RequestParam String fileName, @RequestParam int lineOfBus, @RequestParam MultipartFile selectedFile,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		logger.debug("[upload] document title :" + title + " filename: " + fileName);
		if (!selectedFile.isEmpty()) {

			try {

				UserInfo employee = (UserInfo) request.getSession().getAttribute("employee");

				if (null == employee) {
					redirectAttributes.addAttribute("isLoggedin", false);
					return MinervaConstants.LOGIN_PAGE;
				}

				String folderDir = momFolder + "//" + categoryName + "//";

				if (!StringUtils.isEmpty(subCategory)) {
					folderDir = momFolder + "//" + categoryName + "//" + subCategory + "//";
				}
				File categoryPath = new File(folderDir);

				categoryPath.mkdir();

				fileName = MinervaUtils.fileNameAppender(fileName, folderDir);

				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(categoryPath + "//" + fileName));
				FileCopyUtils.copy(selectedFile.getInputStream(), stream);
				stream.close();
				String fullPath = folderDir + fileName;

				Document doc = new Document();
				doc.setTitle(title);
				doc.setFilename(fileName);
				doc.setCategory(catService.getCaterogy(categoryName));

				if (!StringUtils.isEmpty(subCategory)) {
					doc.setSubCategory(catService.getCaterogy(subCategory));
				}
				doc.setDetailedInfo(detailedInfo);
				doc.setPath(fullPath);
				doc.setTags(tags);
				doc.setCreateUser(employee);
				doc.setCreateDate(new LocalDateTime());
				doc.setUpdateUser(employee);
				doc.setUpdateDate(new LocalDateTime());
				doc.setEnabled(true);
				LineOfBusiness lob = new LineOfBusiness();
				lob.setId(lineOfBus);
				doc.setLineOfBusiness(lob);

				docService.addDocument(doc);
				String htmlBody = newDocHTMLBody(doc);

				EmailDetails newDocumentEmailDetails = getEmailDetails(propertiesService, true);
				String emailSubject = "New Document Upload";

				newDocumentEmailDetails.setSubject(emailSubject);
				newDocumentEmailDetails.setHtmlBody(htmlBody);
				try {
					new EmailGenerator().sendEmail(newDocumentEmailDetails);

				} catch (EmailException e) {
					logger.error(e.getMessage(), e);
					retrySendingEmail(emailSubject, htmlBody.toString(), newDocumentEmailDetails.getRecipient(),
							employee.getUsername(), new LocalDateTime());
				}
				// notify admins
				StringBuilder adminEmailBody = adminNotifyHtml("A new document was uploaded.", employee, doc);

				String adminRoles = userService.getEmailRecipients(ADMIN_ROLE);
				notifyAdminsMain(adminEmailBody.toString(), newDocumentEmailDetails, adminRoles);

				redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + title + "!");
			} catch (MaxUploadSizeExceededException e) {
				throw new GenericException("Document Exception", "Max upload limit is " + e.getMaxUploadSize() + ".");

			} catch (DataIntegrityViolationException e) {
				logger.error(e.getMessage(), e);
				e.printStackTrace();
			} catch (NumberFormatException e) {
				logger.error(e.getMessage(), e);
				e.printStackTrace();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("message", "SQL has encountered an error. Upload failed.");
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage(), e);
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("message",
						"Error was encountered... File is missing in the server. Upload failed.");
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("message", "Error was encountered in the server");
			}
		}

		return "redirect:" + MinervaConstants.ADD_URL;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getAllDocs")
	public @ResponseBody Set<DocumentVO> getAllDocuments(@RequestParam boolean isMainDoc) {
		Set<Document> resultSet = new HashSet<Document>();
		Set<DocumentVO> documentSet = new HashSet<DocumentVO>();

		try {
			resultSet.addAll(docService.getAllDocuments(isMainDoc));
			for (Document doc : resultSet) {
				documentSet.add(new DocumentVO(doc.getId(), doc.getCategory().getId(), doc.getCategory().getName(),
						doc.getFilename(), doc.getCreateDate().toString("MM/dd/YYYY hh:mm:ss"),
						doc.getCreateUser().getFirstName() + " " + doc.getCreateUser().getLastName(),
						doc.getDetailedInfo(), null == doc.getSubCategory() ? -1 : doc.getSubCategory().getId(),
						null == doc.getSubCategory() ? "" : doc.getSubCategory().getName(), doc.getTags(),
						doc.getTitle(), doc.getUpdateDate().toString("MM/dd/YYYY hh:mm:ss"),
						doc.getUpdateUser().getFirstName() + " " + doc.getUpdateUser().getLastName(),
						doc.getLineOfBusiness().getName(), doc.isEnabled()));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return documentSet;
	}

	@RequestMapping(value = "/doDownload", method = RequestMethod.GET)
	public void doDownload(HttpServletRequest request, HttpServletResponse response, @RequestParam String docId)
			throws IOException {
		Document doc;
		String fullPath = "";

		try {
			doc = docService.getDocument(Integer.parseInt(docId));
			fullPath = doc.getPath();
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		FileStreamGenerator.generate(response, fullPath, context);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update")
	public String updateDoc(@RequestParam String docId, @RequestParam String detailedInfo, @RequestParam String tags,
			@RequestParam String fileName, @RequestParam MultipartFile selectedFile, @RequestParam String pageId,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		UserInfo employee = (UserInfo) request.getSession().getAttribute("employee");

		if (null == employee) {
			redirectAttributes.addAttribute("isLoggedin", false);
			return MinervaConstants.LOGIN_PAGE;
		}

		if (MinervaUtils.hasRole(employee, MinervaConstants.ROLE_UPLOADER)) {
			try {
				Document docInfo;

				docInfo = docService.getDocument(Integer.parseInt(docId));

				docInfo.setDetailedInfo(detailedInfo);
				docInfo.setTags(tags);

				if (!selectedFile.isEmpty()) {

					String folderDir = momFolder + "//" + docInfo.getCategory().getDirectoryName() + "//";

					if (!StringUtils.isEmpty(docInfo.getSubCategory())) {
						folderDir = momFolder + "//" + docInfo.getCategory().getDirectoryName() + "//"
								+ docInfo.getSubCategory().getDirectoryName() + "//";
					}
					File fileToBeReplaced = new File(docInfo.getPath());
					if (fileToBeReplaced.delete()) {
						logger.debug(fileToBeReplaced.getName() + " is deleted!");
					} else {
						logger.debug("Delete operation is failed.");
					}

					File categoryPath = new File(folderDir);
					categoryPath.mkdir();

					BufferedOutputStream stream = new BufferedOutputStream(
							new FileOutputStream(categoryPath + "//" + fileName));
					FileCopyUtils.copy(selectedFile.getInputStream(), stream);
					stream.close();
					String fullPath = folderDir + fileName;
					docInfo.setFilename(fileName);
					docInfo.setPath(fullPath);

				}
				docInfo.setUpdateDate(new LocalDateTime());
				docInfo.setUpdateUser(employee);
				docService.updateDocument(docInfo);

				// send email notification to users
				String htmlBody = updateDocHtmlBody(docInfo);
				EmailDetails updateDocumentEmailDetails = getEmailDetails(propertiesService, true);
				String emailSubject = "Document Update";

				updateDocumentEmailDetails.setSubject(emailSubject);
				updateDocumentEmailDetails.setHtmlBody(htmlBody);

				try {
					new EmailGenerator().sendEmail(updateDocumentEmailDetails);
				} catch (EmailException e) {
					logger.error(e.getMessage());
					e.printStackTrace();
					retrySendingEmail(emailSubject, htmlBody.toString(), updateDocumentEmailDetails.getRecipient(),
							employee.getUsername(), new LocalDateTime());
				}

				// notify admins
				String msg = "A document was updated.";
				StringBuilder adminEmailBody = adminNotifyHtml(msg, employee, docInfo);
				String adminRoles = userService.getEmailRecipients(ADMIN_ROLE);
				notifyAdminsMain(adminEmailBody.toString(), updateDocumentEmailDetails, adminRoles);

				redirectAttributes.addFlashAttribute("message", "You successfully updated " + docInfo.getTitle() + "!");
			} catch (MaxUploadSizeExceededException e) {
				throw new GenericException("Document Exception", "Max upload limit is " + e.getMaxUploadSize() + ".");

			} catch (NumberFormatException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("message", "SQL has encountered an error. Upload failed.");
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("message",
						"Error was encountered... File is missing in the server. Upload failed.");
			} catch (IOException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("message", "Error was encountered in the server");
			}
		}

		if ("tools".equals(pageId)) {
			return "redirect:" + MinervaConstants.TOOLS_PAGE;
		}

		return "redirect:" + MinervaConstants.HOME_URL;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/delete")
	public String deleteDoc(@RequestParam String docId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		logger.debug("[deleteDoc] document to be deleted: " + docId);
		UserInfo employee = (UserInfo) request.getSession().getAttribute("employee");
		
		if (null == employee) {
			redirectAttributes.addAttribute("isLoggedin", false);
			return MinervaConstants.LOGIN_PAGE;
		}

		if (MinervaUtils.hasRole(employee, MinervaConstants.ROLE_UPLOADER)) {
			try {

				Document docInfo = docService.getDocument(Integer.parseInt(docId));

				docService.deleteDocument(docInfo);

				File fileToBeReplaced = new File(docInfo.getPath());
				if (fileToBeReplaced.delete()) {
					logger.debug(fileToBeReplaced.getName() + " is deleted!");
				} else {
					logger.debug("Delete operation is failed.");
				}

				EmailDetails emailDetails = getEmailDetails(propertiesService, true);

				StringBuilder adminEmailBody = adminNotifyHtml("A document was deleted.", employee, docInfo);
				String adminRoles = userService.getEmailRecipients(ADMIN_ROLE);
				notifyAdminsMain(adminEmailBody.toString(), emailDetails, adminRoles);
				redirectAttributes.addFlashAttribute("message", "You successfully deleted the document.");
			} catch (NumberFormatException | SQLException e) {
				return "FAILED";
			}
		}
		logger.debug("[deleteDoc] document id :" + docId + " SUCCESS");
		return "redirect:" + MinervaConstants.HOME_URL;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/archive")
	public String archiveDocument(@RequestParam int docId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		logger.debug("[archive] document id :" + docId);
		String emailSubject = "A document has been archived.";
		UserInfo employee = (UserInfo) request.getSession().getAttribute("employee");
		String referrer = request.getHeader("referer");
		Document doc = new Document();
		if (null == employee) {
			redirectAttributes.addAttribute("isLoggedin", false);
			return MinervaConstants.LOGIN_PAGE;
		}

		try {
			doc = docService.getDocument(docId);
			doc.setEnabled(false);
			doc.setUpdateUser(employee);
			doc.setUpdateDate(new LocalDateTime());

			docService.updateDocument(doc);
			EmailDetails emailDetails = getEmailDetails(propertiesService, true);

			// notifyAdmins
			StringBuilder htmlBody = adminNotifyHtml(emailSubject, employee, doc);
			String adminRoles = userService.getEmailRecipients(ADMIN_ROLE);
			notifyAdminsMain(htmlBody.toString(), emailDetails, adminRoles);
			redirectAttributes.addFlashAttribute("message", "You successfully archived the document.");
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

		logger.debug("[archive] document id :" + docId + " SUCCESS");
		return "redirect:" + referrer;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/undoArchive")
	public String undoArchive(@RequestParam int docId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		logger.debug("[undoArchive] document id :" + docId);

		UserInfo employee = (UserInfo) request.getSession().getAttribute("employee");

		if (null == employee) {
			redirectAttributes.addAttribute("isLoggedin", false);
			return MinervaConstants.LOGIN_PAGE;
		}

		String referrer = request.getHeader("referer");
		Document doc = new Document();

		try {
			doc = docService.getDocument(docId);
			doc.setEnabled(true);
			doc.setUpdateUser(employee);
			doc.setUpdateDate(new LocalDateTime());

			docService.updateDocument(doc);

			EmailDetails emailDetails = getEmailDetails(propertiesService, true);
			String emailSubject = "A document was unarchived.";

			// notifyAdmins
			StringBuilder htmlBody = adminNotifyHtml(emailSubject, employee, doc);
			String adminRoles = userService.getEmailRecipients(ADMIN_ROLE);
			notifyAdminsMain(htmlBody.toString(), emailDetails, adminRoles);
			redirectAttributes.addFlashAttribute("message", "You successfully unarchived the document.");
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		logger.debug("[undoArchive] document id :" + docId + " SUCCESS");
		return "redirect:" + referrer;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/searchDocument")
	public @ResponseBody Set<DocumentVO> searchDocument(@RequestParam boolean isMainDoc, @RequestParam String title,
			@RequestParam String firstName, @RequestParam String lastName, @RequestParam String tags,
			@RequestParam String dateCascaded, @RequestParam String mainCat, @RequestParam String subCat,
			@RequestParam String lineOfBus, @RequestParam boolean isArchived) {

		Set<Document> resultSet = new HashSet<Document>();
		Set<DocumentVO> documentSet = new HashSet<DocumentVO>();

		try {
			resultSet.addAll(docService.searchDocument(isMainDoc, title, firstName, lastName, tags, dateCascaded,
					mainCat, subCat, lineOfBus, isArchived));
			for (Document doc : resultSet) {
				// Category cat = catService.getCaterogy(doc.getCategory())
				documentSet.add(new DocumentVO(doc.getId(), doc.getCategory().getId(), doc.getCategory().getName(),
						doc.getFilename(), doc.getCreateDate().toString("MM/dd/YYYY hh:mm:ss"),
						doc.getCreateUser().getFirstName() + " " + doc.getCreateUser().getLastName(),
						doc.getDetailedInfo(), null == doc.getSubCategory() ? -1 : doc.getSubCategory().getId(),
						null == doc.getSubCategory() ? "" : doc.getSubCategory().getName(), doc.getTags(),
						doc.getTitle(), doc.getUpdateDate().toString("MM/dd/YYYY hh:mm:ss"),
						doc.getUpdateUser().getFirstName() + " " + doc.getUpdateUser().getLastName(),
						doc.getLineOfBusiness().getName(), doc.isEnabled()));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return documentSet;
	}

	@RequestMapping(value = "/downloadDocReport", method = RequestMethod.POST)
	public void doDownloadDocReport(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String startDate, @RequestParam String endDate, @RequestParam String lineOfBus)
			throws IOException {
		List<Document> docList = new ArrayList<Document>();

		try {
			String momPath = momFolder + "//logs//";
			LineOfBusiness lob = new LineOfBusiness();

			if (!StringUtils.isEmpty(lineOfBus)) {
				lob.setId(Integer.parseInt(lineOfBus));
			}

			docList = docService.getDocumentsByDates(MinervaUtils.parseDate(startDate), MinervaUtils.parseDate(endDate),
					lob);
			String fileName = new ReportGenerator().generateDoc(docList, momPath);
			FileStreamGenerator.generate(response, fileName, context);
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/viewDocument/{docId}", method = RequestMethod.GET)
	public String viewDocument(@PathVariable("docId") String docId, ModelMap model, HttpServletRequest request) {

		try {
			Document doc = docService.getDocument(Integer.parseInt(docId));
			UserInfo emp = (UserInfo) request.getSession().getAttribute("employee");

			if (null == emp) {
				model.addAttribute("isLoggedin", false);
				return MinervaConstants.LOGIN_PAGE;
			}
			if (null == doc) {
				throw new GenericException("Document Exception", "Document is not found.");
			}
			model.addAttribute("document", doc);
			model.addAttribute("isAdmin", MinervaUtils.hasRole(emp, MinervaConstants.ROLE_ADMIN));
			model.addAttribute("isRoleUploader", MinervaUtils.hasRole(emp, MinervaConstants.ROLE_UPLOADER));
			model.addAttribute("isInquiryAdmin", MinervaUtils.hasRole(emp, MinervaConstants.ROLE_INQUIRY_ADMIN));
			model.addAttribute("isLoggedin", true);

		} catch (NumberFormatException | SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			throw new GenericException("Document Exception", "Document id was not found.");
		}
		return MinervaConstants.VIEW_DOCUMENT_PAGE;
	}

	private String newDocHTMLBody(Document docInfo) throws SQLException {

		String appName = propertiesService.getProperty(MinervaConstants.APPNAME).getValue();
		String hostname = propertiesService.getProperty(MinervaConstants.HOST_NAME).getValue();
		String serverPort = propertiesService.getProperty(MinervaConstants.SERVER_PORT).getValue();

		StringBuilder htmlBody = new StringBuilder();
		htmlBody.append("<p><strong>Dear Users,</strong> <br/> <br/>");
		htmlBody.append(
				"A new document has been uploaded to <span class='app-name'>" + appName + "</span>.<br/><br/> ");
		htmlBody.append("<strong>Uploaded by</strong>: " + docInfo.getCreateUser().getFirstName());
		htmlBody.append(" " + docInfo.getCreateUser().getLastName() + "<br/>");
		htmlBody.append("<strong>Title</strong>: " + docInfo.getTitle() + "<br/>");
		htmlBody.append("<strong>Category</strong>: " + docInfo.getCategory().getName() + "<br/>");
		htmlBody.append("<strong>Link</strong>: <a href='http://" + hostname + ":" + serverPort
				+ context.getContextPath() + "/viewDocument/" + docInfo.getId() + "'>");
		htmlBody.append("http://" + hostname + ":" + serverPort + context.getContextPath() + "/viewDocument/"
				+ docInfo.getId());
		htmlBody.append("</a><br/><br/>");
		htmlBody.append("Regards,<br/><span class='app-name'>" + appName + "</span></p>");
		return htmlBody.toString();
	}

	private String updateDocHtmlBody(Document docInfo) throws SQLException {

		String appName = propertiesService.getProperty(MinervaConstants.APPNAME).getValue();
		String hostname = propertiesService.getProperty(MinervaConstants.HOST_NAME).getValue();
		String serverPort = propertiesService.getProperty(MinervaConstants.SERVER_PORT).getValue();

		StringBuilder htmlBody = new StringBuilder();
		htmlBody.append("<p><strong>Dear Users,</strong> <br/> <br/>");
		htmlBody.append("A document has been updated in <span class='app-name'>" + appName + "</span>.<br/><br/> ");
		htmlBody.append("<strong>Updated by</strong>: " + docInfo.getUpdateUser().getFirstName());
		htmlBody.append(" " + docInfo.getUpdateUser().getLastName() + "<br/>");
		htmlBody.append("<strong>Title</strong>: " + docInfo.getTitle() + "<br/>");
		htmlBody.append("<strong>Category</strong>: " + docInfo.getCategory().getName() + "<br/>");
		htmlBody.append("<strong>Link</strong>: <a href='http://" + hostname + ":" + serverPort
				+ context.getContextPath() + "/viewDocument/" + docInfo.getId() + "'>");
		htmlBody.append("http://" + hostname + ":" + serverPort + context.getContextPath() + "/viewDocument/"
				+ docInfo.getId());
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
