package com.optum.technology.minerva.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExceptionController {

	@ExceptionHandler({SQLException.class,DataAccessException.class})
	  public String databaseError() {
	    // Nothing to do.  Returns the logical view name of an error page, passed to
	    // the view-resolver(s) in usual way.
	    // Note that the exception is _not_ available to this view (it is not added to
	    // the model) but see "Extending ExceptionHandlerExceptionResolver" below.
	    return "error";
	  }
	
	@ExceptionHandler(Exception.class)
	  public ModelAndView handleError(HttpServletRequest req, Exception exception) {

	    ModelAndView mav = new ModelAndView();
	    mav.addObject("exception", exception);
	    mav.addObject("url", req.getRequestURL());
	    mav.setViewName("error");
	    return mav;
	  }
}
