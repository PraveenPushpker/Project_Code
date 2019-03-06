package com.usa.ri.gov.ies.admin.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usa.ri.gov.ies.constants.AppConstants;
import com.usa.ri.gov.ies.properties.AppProperties;

@RestController
public class WelcomeRestController {

	private static Logger logger= LoggerFactory.getLogger(WelcomeRestController.class);

	public WelcomeRestController() {
		logger.info("WelcomeRestController::Intializied");
	}

	@Autowired(required = true)
	private AppProperties properties;

	@GetMapping("/welcome")
	public String welcome() 
	{
		logger.info("RestController::Welcome method Started");
		System.out.println(properties);
		String msg = properties.getProperties().get(AppConstants.MESSAGE);
		logger.info("RestController::Welcome method Ended");
		return msg;
	}

}
