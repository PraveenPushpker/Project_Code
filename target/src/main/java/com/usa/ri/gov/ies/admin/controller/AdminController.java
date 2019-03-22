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
import org.springframework.web.bind.support.SessionStatus;

import com.usa.ri.gov.ies.admin.model.AppAccount;
import com.usa.ri.gov.ies.admin.model.PlanDetails;
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
	 * This method is used for show login form
	 * @return String
	 */
	@RequestMapping(value="/")
	public String loginForm() {
		logger.info("****login form generated successfully****");
		return "login";
	}
	
	@RequestMapping(value="/login")
	public String login(HttpServletRequest req, Model model) {
		String email=req.getParameter("email");
		String pwd=req.getParameter("pwd");
		Map<Boolean, String> result = adminService.login(email, pwd);
		if(result.containsKey(true)) {
			return result.get(true);
		}else {
			model.addAttribute(AppConstants.FAILURE, appProperties.getProperties().get(result.get(false)));
		}
		return "login";
	}
	/**
	 * This method is used for displaying forgot password form
	 * @return String
	 */
	@RequestMapping(value="/forgotPwd")
	public String forgotPwdForm() {
		logger.info("****Forgot password form generated successfully****");
		return "forgotPwd";
	}

	/**
	 * This method is used to perform forgotPassword operation
	 * @return string
	 */
	@RequestMapping(value="/forgotPwd",method=RequestMethod.POST)
	public String forgotPassword(HttpServletRequest req,Model model) {
		logger.debug("forgotPassword(-) method is started ");
		String emailId = req.getParameter("email");
		String resultMsg = adminService.forgotPassword(emailId);
		if(resultMsg.contains("not"))
			model.addAttribute(AppConstants.FAILURE, resultMsg);
		else
			model.addAttribute(AppConstants.SUCCESS, resultMsg);
		return "forgotPwd";
	}

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

	@RequestMapping(value = "/editAcc")
	public String editAccountForm(HttpServletRequest req, Model model) {

		String accId = req.getParameter("accId");

		AppAccount accModel = adminService.findByAccountId(accId);

		model.addAttribute(AppConstants.APP_ACCOUNT, accModel);
		initForm(model);
		return "editAcc";
	}
	/**
	 * This method is used for Updating Account Details
	 * 
	 * @param planDTLS
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/editAcc", method = RequestMethod.POST)
	public String editAccount(@ModelAttribute("account") AppAccount accModel, Model model) {
		logger.debug("AdminController:: editAccount() method started");
		boolean status = false;
		// Invoke Service method
		status = adminService.editAppAccount(accModel);

		if (status) {
			// get SuccessMessage value
			String successMsg = appProperties.getProperties().get(AppConstants.EDIT_ACC_SUCCESS_MSG);
			model.addAttribute(AppConstants.SUCCESS, successMsg);
		} else {
			// get Failure Message
			String failureMsg = appProperties.getProperties().get(AppConstants.EDIT_ACC_ERR_MSG);
			model.addAttribute(AppConstants.FAILURE, failureMsg);
		}
		//add roles Details
		initForm(model);
		logger.debug("AdminController:: editAccount() method started");
		logger.info("editAccount  Method completed Successfully");

		return "editAcc";
	}
	
	
	/**
	 * This method is used for displaying plan registration form
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/planReg", method = RequestMethod.GET)
	public String planRegForm(Model model) {
		logger.debug("AdminController:: planRegForm() method started");
		PlanDetails planModel = new PlanDetails();

		model.addAttribute(AppConstants.PLANMODEL, planModel);
		logger.debug("AdminController:: planRegForm() method started");
		return "planReg";
	}

	/**
	 * This method is used for Plan Registration
	 * 
	 * @param planDTLS
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/planReg", method = RequestMethod.POST)
	public String planCreation(@ModelAttribute("planModel") PlanDetails planDTLS, Model model) {
		logger.debug("AdminController:: planCreation() method started");
		boolean status = false;
		// Invoke Service method
		status = adminService.createPlan(planDTLS);

		if (status) {
			// get SuccessMessage value
			String successMsg = appProperties.getProperties().get(AppConstants.PLAN_REG_SUCCESS_MSG);
			model.addAttribute(AppConstants.SUCCESS, successMsg);
		} else {
			// get Failure Message
			String failureMsg = appProperties.getProperties().get(AppConstants.PLAN_REG_ERR_MSG);
			model.addAttribute(AppConstants.FAILURE, failureMsg);
		}
		logger.debug("AdminController:: planCreation() method started");
		logger.info("Plan Creation Method executed Successfully");
		return "planReg";
	}

	/**
	 * This method is used to validate the plan
	 * 
	 * @param req
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = { "planReg/validatePlan", "editPlan/validatePlan" })
	public @ResponseBody String varifyPlan(HttpServletRequest req, Model model) {
		logger.debug("Plan Validation started");
		String plan = req.getParameter("plan");
		logger.debug("Plan Validation ended");
		return adminService.checkDuplicatePlan(plan);
	}

	/**
	 * This method is used for displaying all the plans
	 * 
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/viewPlans", method = RequestMethod.GET)
	public String findAllPlans(Model model) {
		logger.debug("AdminController ::findAllPlans() method started");
		// invoke Service method
		List<PlanDetails> modelsList = adminService.findAllPlans();

		// add data to model scope
		model.addAttribute(AppConstants.PlANS, modelsList);

		logger.debug("AdminController ::findAllPlans() method ended");
		logger.info("findAllPlans Method completed");
		return "viewPlans";
	}

	/**
	 * This method used to De-Activate the account
	 * 
	 * @param acc_id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/deletePlan", method = RequestMethod.GET)
	public String deletePlan(HttpServletRequest req, Model model) {
		logger.debug("AdminController:: deletePlan(); started");
		// call service method
		String planId = req.getParameter("planId");
		boolean isDelete = adminService.updatePlanActiveSw(planId, AppConstants.IN_ACTIVE_SW);

		// invoke Service method
		List<PlanDetails> modelsList = adminService.findAllPlans();

		// add data to model scope
		model.addAttribute(AppConstants.PlANS, modelsList);
		if (isDelete) {
			// add account De-Activation success Message
			String successMsg = appProperties.getProperties().get(AppConstants.PLAN_DE_ACTIVATE_SUCCESS_MSG);
			model.addAttribute(AppConstants.SUCCESS, successMsg);

			logger.debug("AdminController:: deletePlan(); started");
			logger.info("Plan Deactivated SuccessFully");
			return "viewPlans";
		} else {
			String failureMsg = appProperties.getProperties().get(AppConstants.PLAN_DE_ACTIVATE_ERR_MSG);
			model.addAttribute(AppConstants.FAILURE, failureMsg);

			return "viewPlans";
		}
	}

	/**
	 * This method used to De-Activate the account
	 * 
	 * @param acc_id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/activatePlan", method = RequestMethod.GET)
	public String activatePlan(HttpServletRequest req, Model model) {

		logger.debug("AdminController:: activatePlan(); started");

		// call service method
		boolean isDelete = adminService.updatePlanActiveSw(req.getParameter("planId"),
				AppConstants.ACTIVE_SW);

		// invoke Service method
		List<PlanDetails> modelsList = adminService.findAllPlans();

		// add data to model scope
		model.addAttribute(AppConstants.PlANS, modelsList);
		if (isDelete) {
			// add plan De-Activation success Message
			String successMsg = appProperties.getProperties().get(AppConstants.PLAN_ACTIVATE_SUCCESS_MSG);
			model.addAttribute(AppConstants.SUCCESS, successMsg);

			logger.debug("AdminController:: activatePlan(); started");
			logger.info("Plan Activated SuccessFully");

			return "viewPlans";
		} else {
			String failureMsg = appProperties.getProperties().get(AppConstants.PLAN_ACTIVATE_ERR_MSG);
			model.addAttribute(AppConstants.FAILURE, failureMsg);

			return "viewPlans";
		}
	}

	/**
	 * This method is used to show editPlan with initial Data
	 * 
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/editPlan", method = RequestMethod.GET)
	public String showEditPlan(HttpServletRequest req, Model model, SessionStatus status) {
		logger.debug("AdminController::showEditPlan( ); method started");
		// read plan id from request scope
		String planId = req.getParameter("planId");
		status.setComplete();
		// call service method to get initial model
		PlanDetails planDetails = adminService.findPlanById(planId);

		if (planDetails != null) {
			// put into model scope
			model.addAttribute(AppConstants.PLANMODEL, planDetails);
		}
		logger.debug("AdminController::showEditPlan( ); method ended");
		logger.info(" showEditPlan form completed Successfully");

		return "editPlan";
	}

	/**
	 * This method is used for Editing Plan Details
	 * 
	 * @param planDTLS
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/editPlan", method = RequestMethod.POST)
	public String editPlan(@ModelAttribute("planModel") PlanDetails planModel, Model model) {
		logger.debug("AdminController:: editPlan() method started");
		boolean status = false;
		// Invoke Service method
		status = adminService.editPlan(planModel);

		if (status) {
			// get SuccessMessage value
			String successMsg = appProperties.getProperties().get(AppConstants.EDIT_PLAN_SUCCESS_MSG);
			model.addAttribute(AppConstants.SUCCESS, successMsg);
		} else {
			// get Failure Message
			String failureMsg = appProperties.getProperties().get(AppConstants.EDIT_PLAN_ERR_MSG);
			model.addAttribute(AppConstants.FAILURE, failureMsg);
		}
		logger.debug("AdminController:: editPlan() method started");
		logger.info("editPlan  Method completed Successfully");

		return "editPlan";
	}


}
