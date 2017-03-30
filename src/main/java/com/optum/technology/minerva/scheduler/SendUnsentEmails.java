package com.optum.technology.minerva.scheduler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("myBean")
public class SendUnsentEmails {


	private final static Logger logger = LoggerFactory
			.getLogger(SendUnsentEmails.class);

	public void process() throws IOException {
		logger.debug("The unsent email processor was called. Hello.");
		System.out.println("The unsent email processor was called. Hello from the processor's side.");
	}

}
