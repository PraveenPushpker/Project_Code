package com.usa.ri.gov.ies.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.usa.ri.gov.ies.admin.model.AppAccount;
import com.usa.ri.gov.ies.admin.model.CreatePlan;
import com.usa.ri.gov.ies.admin.service.AdminService;
import com.usa.ri.gov.ies.constants.AppConstants;
import com.usa.ri.gov.ies.properties.AppProperties;

/**
 * This class is used to Handle Admin module related functionalities
 * 
 * @author admin
 *
 */
@Controller
public class AdminController {

	private static Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired(required = true)
	private AdminService adminService;

	@Autowired
	private AppProperties appProperties;

	/**
	 * This method is used to Display User Account reg form
	 * 
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/accReg", method = RequestMethod.GET)
	public String accRegForm(Model model) {
		logger.debug("AdminController::accRegForm() started");
		// Creating empty model object
		AppAccount accModel = new AppAccount();

		// add cwModel object to Model scope
		model.addAttribute("accModel", accModel);

		initForm(model);

		logger.debug("AdminController::accRegForm() ended");
		logger.info("Account Reg Form loaded Successfully");

		return "accReg";
	}

	/**
	 * This method is used to register user account with given values
	 * 
	 * @param appAccModel
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/accReg", method = RequestMethod.POST)
	public String accReg(@ModelAttribute("accModel") AppAccount appAccModel, Model model) {
		try {
			logger.debug("user account creation started");

			// call Service layer method
			boolean isSaved = adminService.registerAccount(appAccModel);

			Map<String, String> map = appProperties.getProperties();
			if (isSaved) {
				// Display success message
				model.addAttribute(AppConstants.SUCCESS, map.get(AppConstants.CW_REG_SUCCESS));
			} else {
				// Display failure message
				model.addAttribute(AppConstants.FAILURE, map.get(AppConstants.CW_REG_FAIL));
			}
			initForm(model);
			logger.debug("user account creation ended");
			logger.info("User Account creation completed successfully");
		} catch (Exception e) {
			logger.error("User Account Creation Failed :: " + e.getMessage());
		}
		return "accReg";
	}

	/**
	 * This method is used to load roles for application
	 * 
	 * @param model
	 */
	private void initForm(Model model) {
		List<String> rolesList = new ArrayList<>();
		rolesList.add("Case Worker");
		rolesList.add("Admin");
		model.addAttribute("rolesList", rolesList);

		List<String> gendersList = new ArrayList<>();
		gendersList.add("Male");
		gendersList.add("Fe-Male");
		model.addAttribute("gendersList", gendersList);
	}

	/**
	 * This method is used to validate email
	 * 
	 * @param req
	 * @param model
	 * @return String
	 */
	@RequestMapping("/accReg/validateEmail")
	public @ResponseBody String checkEmailValidity(HttpServletRequest req, Model model) {
		String emailId = req.getParameter("email");
		return adminService.findByEmail(emailId);
	}

	/**
	 * This method is used to display all app accounts in table
	 * 
	 * @return String
	 */
	@RequestMapping(value = "/viewAccounts")
	public String viewAccounts(Model model) {
		logger.debug("viewAccounts() method started");

		// calling service layer method
		List<AppAccount> accounts = adminService.findAllAppAccounts();

		// store accounts in model scope
		model.addAttribute(AppConstants.APP_ACCOUNTS, accounts);

		logger.debug("viewAccounts() method ended");
		return "viewAccounts"; // view name
	}

	/**
	 * This method is used to perform Soft Delete
	 * 
	 * @param req
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/delete")
	public String deleteAccount(HttpServletRequest req, Model model) {
		logger.info("*** Account Getting De-Activating ***");
		logger.debug("***deleteAccount() started***");

		// capture query param value
		String accId = req.getParameter("accId");

		// call service layer method
		boolean isDeleted = adminService.updateAccountSw(accId, AppConstants.IN_ACTIVE_SW);
		logger.debug("***deleteAccount() ended***");

		// calling service layer method
		List<AppAccount> accounts = adminService.findAllAppAccounts();

		// store accounts in model scope
		model.addAttribute(AppConstants.APP_ACCOUNTS, accounts);

		if (isDeleted) {
			String succMsg = appProperties.getProperties().get(AppConstants.ACC_DE_ACTIVATE_SUCC_MSG);
			model.addAttribute(AppConstants.SUCCESS, succMsg);
		} else {
			String errMsg = appProperties.getProperties().get(AppConstants.ACC_DE_ACTIVATE_ERR_MSG);
			model.addAttribute(AppConstants.FAILURE, errMsg);
		}
		return "viewAccounts";
	}

	/**
	 * This method is used to perform Soft Delete
	 * 
	 * @param req
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/activate")
	public String activateAccount(HttpServletRequest req, Model model) {
		logger.debug("***activateAccount() started***");
		logger.info("*** Account Getting Activating ***");
		// capture query param value
		String accId = req.getParameter("accId");

		// call service layer method
		boolean isActivated = adminService.updateAccountSw(accId, AppConstants.ACTIVE_SW);
		logger.debug("***activateAccount() ended***");

		// calling service layer method
		List<AppAccount> accounts = adminService.findAllAppAccounts();

		// store accounts in model scope
		model.addAttribute(AppConstants.APP_ACCOUNTS, accounts);

		if (isActivated) {
			String succMsg = appProperties.getProperties().get(AppConstants.ACC_ACTIVATE_SUCC_MSG);
			model.addAttribute(AppConstants.SUCCESS, succMsg);
		} else {
			String errMsg = appProperties.getProperties().get(AppConstants.ACC_ACTIVATE_ERR_MSG);
			model.addAttribute(AppConstants.FAILURE, errMsg);
		}
		return "viewAccounts";
	}
	
	
	@RequestMapping(value = "/crtPlan", method = RequestMethod.GET)
	public String plnDtlsForm(Model model) {
		logger.debug("AdminController::crtPlanForm() started");
		// Creating empty model object
		CreatePlan crtModel = new CreatePlan();

		// add cwModel object to Model scope
		model.addAttribute("crtModel", crtModel);

		initForm(model);

		logger.debug("AdminController::crtPlanForm() ended");
		logger.info("Create Plan Form loaded Successfully");

		return "crtPlan";
	}
	
	@RequestMapping(value = "/crtPlan", method = RequestMethod.POST)
	public String plnDtls(@ModelAttribute("crtModel") CreatePlan createPlanModal, Model model) {
		try {
			logger.debug("Create Plan creation started");

			// call Service layer method
			boolean isSaved = adminService.registerPlan(createPlanModal);

			Map<String, String> map = appProperties.getProperties();
			if (isSaved) {
				// Display success message
				model.addAttribute(AppConstants.SUCCESS, map.get(AppConstants.CW_REG_SUCCESS));
			} else {
				// Display failure message
				model.addAttribute(AppConstants.FAILURE, map.get(AppConstants.CW_REG_FAIL));
			}
			initForm(model);
			logger.debug("user account creation ended");
			logger.info("User Account creation completed successfully");
		} catch (Exception e) {
			logger.error("User Account Creation Failed :: " + e.getMessage());
		}
		return "crtPlan";
	}

}
