package com.optum.technology.minerva.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ExceptionResolverImpl implements HandlerExceptionResolver {
	private final static Logger logger = LoggerFactory.getLogger(ExceptionResolverImpl.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object obj,
			Exception ex) {

		if (ex instanceof MaxUploadSizeExceededException) {
			response.setContentType("text/html");
			response.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());

			ModelAndView model = new ModelAndView("error");
			model.addObject("errCode", "Document Upload Error");
			model.addObject("errMsg",
					"Max upload limit is " + ((MaxUploadSizeExceededException) ex).getMaxUploadSize() + ".");
			logger.debug("MaxUploadSizeExceededException was invoked");
			return model;

		}

		
		if (ex instanceof DataIntegrityViolationException) {

			ModelAndView model = new ModelAndView("error");
			model.addObject("errCode", "Server Encountered Error");
			model.addObject("errMsg", ex.getMessage());
			logger.debug("DataIntegrityViolationException was invoked",ex);
			ex.printStackTrace();
			return model;

		}

		// for default behaviour
		return null;
	}
}