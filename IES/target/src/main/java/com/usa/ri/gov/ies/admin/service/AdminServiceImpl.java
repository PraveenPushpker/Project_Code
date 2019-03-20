package com.usa.ri.gov.ies.admin.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usa.ri.gov.ies.admin.entity.AppAccountEntity;
import com.usa.ri.gov.ies.admin.entity.PlanDetailsEntity;
import com.usa.ri.gov.ies.admin.model.AppAccount;
import com.usa.ri.gov.ies.admin.model.PlanDetails;
import com.usa.ri.gov.ies.admin.repository.AppAccountRepository;
import com.usa.ri.gov.ies.admin.repository.PlanDetailsRepository;
import com.usa.ri.gov.ies.constants.AppConstants;
import com.usa.ri.gov.ies.properties.AppProperties;
import com.usa.ri.gov.ies.util.EmailUtils;
import com.usa.ri.gov.ies.util.PasswordUtils;

/**
 * This class is used to handler all business operations related to admin module
 * 
 * @author admin
 *
 */
@Service("adminService")
public class AdminServiceImpl implements AdminService {

	private static Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Autowired(required = true)
	private AppAccountRepository appAccRepository;

	@Autowired(required = true)
	private PlanDetailsRepository planRepository;

	@Autowired(required = true)
	private EmailUtils emailUtils;

	@Autowired(required = true)
	private AppProperties appProperties;

	/**
	 * This method is used for login
	 * 
	 * @param email
	 * @param pwd
	 * @return Map of result msg and flag
	 */
	@Override
	public Map<Boolean, String> login(String email, String pwd) {
		logger.debug("**AdminService: login() method started**");
		Map<Boolean, String> result = new HashMap<>();
		// calling findByEmail() method to get Entity obj
		AppAccountEntity entity = appAccRepository.findByEmail(email);

		// checking entity whether with that email any record found or not
		if (entity != null) {
			// check password by decrypting it
			String decrptPass = PasswordUtils.decrypt(entity.getPassword());

			if (pwd.equals(decrptPass)) {
				if (entity.getActiveSw().equals(AppConstants.ACTIVE_SW)) {
					if (entity.getRole().equals(AppConstants.ADMIN)) {
						// returning login flag as true and result message as admin
						result.put(true, AppConstants.ADMIN);
						logger.debug("**AdminService: login() method ended**");
						logger.info("**AdminService: login success with admin");
						return result;
					} else {
						// returning login flag as true and result message as caseWorker
						result.put(true, AppConstants.CASE_WORKER);
						logger.debug("**AdminService: login() method ended**");
						logger.info("**AdminService: login success with caseworker");
						return result;
					}
				} else {
					// returning login flag as false and result message as Account is not Active, For Query Contact IES Admin
					result.put(false, AppConstants.ACCOUNT_DE_ACTIVATE_MSG);
					logger.debug("**AdminService: login() method ended**");
					logger.info("**AdminService: login failure account deactive");
					return result;
				}
			}else {
				// returning login flag as false and result message as Invalid Credentials
				result.put(false, AppConstants.INVALID_CREDENTIALS);
				logger.debug("**AdminService: login() method ended**");
				logger.info("**AdminService: login failure invalid credential");
				return result;
			}
		} else {
			// returning login flag as false and result message as Invalid Credentials
			result.put(false, AppConstants.INVALID_CREDENTIALS);
			logger.debug("**AdminService: login() method ended**");
			logger.info("**AdminService: login failure invalid credential");
			return result;
		}
	}

	/**
	 * This method is used for forgot password
	 */
	@Override
	public String forgotPassword(String emailId) {
		logger.debug("***AdminServiceImpl::forgotPassword() is started***");
		AppAccountEntity entity=appAccRepository.findByEmail(emailId);
		if(entity==null) {
			logger.debug(emailId+" email is not registered!!");
			return appProperties.getProperties().get(AppConstants.EMAIL_NOT_REG);
		}else {
			if(entity.getActiveSw().equals("N")) {
				logger.debug(emailId+"  account not in active");
				return appProperties.getProperties().get(AppConstants.ACCOUNT_DE_ACTIVATE_MSG);
			}else {
				try {
					AppAccount appAccountModel=new AppAccount();
					BeanUtils.copyProperties(entity, appAccountModel);
					appAccountModel.setPassword(PasswordUtils.decrypt(appAccountModel.getPassword()));
					logger.debug("***Password is decryted***");
					String mailBody= getEmailBodyContent(appAccountModel, appProperties.getProperties().get(AppConstants.FORGOT_PASSWORD_EMAIL_FILE_NAME));
					emailUtils.sendEmail(appAccountModel.getEmail(), appProperties.getProperties().get(AppConstants.FORGOT_PASSWORD_EMAIL_SUB), mailBody);
					logger.debug("password is sent  to "+emailId+" succussfully");
					logger.info("AdminServiceImpl::forgotPassword() is completed");
					return appProperties.getProperties().get(AppConstants.PWD_EMAIL_SENT_SUCC_MSG);
				}catch (Exception e) {
					logger.error("password is failed to sent to "+emailId);
					return appProperties.getProperties().get(AppConstants.PWD_EMAIL_SENT_FAIL_MSG);
				}
				
			}//else
			
		}//top else
		
	}//forgotPwd

	/**
	 * This method is used to Register cw/admin
	 */
	@Override
	public boolean registerAccount(AppAccount appAcc) {
		logger.debug("User Registration started");

		// Convert model data to Entity data
		AppAccountEntity entity = new AppAccountEntity();
		BeanUtils.copyProperties(appAcc, entity);

		// Encrypt Password
		String encryptedPwd = PasswordUtils.encrypt(appAcc.getPassword());

		// Set Encrypted password to Entity obj
		entity.setPassword(encryptedPwd);

		// set Status as Active
		entity.setActiveSw(AppConstants.ACTIVE_SW);

		// Call Repository method
		entity = appAccRepository.save(entity);

		// sending Registration Email
		try {
			String fileName = appProperties.getProperties().get(AppConstants.REG_EMAIL_FILE_NAME);
			String mailSub = appProperties.getProperties().get(AppConstants.REG_EMAIL_SUBJECT);
			String mailBody = getEmailBodyContent(appAcc, fileName);
			emailUtils.sendEmail(entity.getEmail(), mailSub, mailBody);
		} catch (Exception e) {
			logger.error("User Registration failed : " + e.getMessage());
		}
		logger.debug("User Registration completed");
		logger.info("AdminServiceImpl::registerAccount() completed");
		return (entity.getAccId() !=null) ? true : false;
	}

	/**
	 * This method is used to reg email body from a file
	 * 
	 * @param accModel
	 * @return String
	 * @throws Exception
	 */
	public String getEmailBodyContent(AppAccount accModel, String fileName) throws Exception {
		logger.debug("Reading Reg Email content started");
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		StringBuffer body = new StringBuffer();
		String line = br.readLine();
		while (line != null) {
			if (line != null && !"".equals(line) && !"<br/>".equals(line)) {
				// process
				if (line.contains("USER_NAME")) {
					line = line.replace("USER_NAME", accModel.getFirstName() + " " + accModel.getLastName());
				}
				if (line.contains("APP_URL")) {
					line = line.replace("APP_URL", "<a href='http://localhost:7070/IES/'>IES URL</a>");
				}
				if (line.contains("APP_USER_EMAIL")) {
					line = line.replace("APP_USER_EMAIL", accModel.getEmail());
				}
				if (line.contains("APP_USER_PWD")) {
					line = line.replace("APP_USER_PWD", accModel.getPassword());
				}
				// Adding processed line to SB body
				body.append(line);
			}
			// read next line
			line = br.readLine();
		}
		// closing br
		br.close();
		logger.debug("Reading Reg Email content Ended");
		logger.info("Reg Email body parsing completed");
		return body.toString();
	}

	/**
	 * This method is used to check unique email
	 */
	@Override
	public String findByEmail(String emailId) {
		AppAccountEntity entity = appAccRepository.findByEmail(emailId);
		return (entity == null) ? "Unique" : "Duplicate";
	}

	/**
	 * This method is used to return All accounts details
	 */
	@Override
	public List<AppAccount> findAllAppAccounts() {
		logger.debug("findAllAppAccounts() method started");
		List<AppAccount> models = new ArrayList<AppAccount>();
		try {
			// call Repository method
			List<AppAccountEntity> entities = appAccRepository.findAll();

			if (entities.isEmpty()) {
				logger.warn("***No Accounts found in Application****");
			} else {
				// convert Entities to models
				for (AppAccountEntity entity : entities) {
					AppAccount model = new AppAccount();
					BeanUtils.copyProperties(entity, model);
					models.add(model);
				}
				logger.info("All Accounts details loaded successfully");
			}
		} catch (Exception e) {
			logger.error("Exception occured in findAllAppAccounts()::" + e.getMessage());
		}
		logger.debug("findAllAppAccounts() method ended");
		return models;
	}

	/**
	 * This method is used to update Account Active Switch
	 */
	@Override
	public boolean updateAccountSw(String accId, String activeSw) {
		logger.debug("*** updateAccountSw() method started");
		String fileName = null, mailSub = null, mailBody = null, password = null;
		try {
			// load existing record using accId
			AppAccountEntity entity = appAccRepository.findById(accId).get();
			if (entity != null) {
				// Setting Account Active Sw (Y|N)
				entity.setActiveSw(activeSw);
				// Updating Account
				appAccRepository.save(entity);
				logger.debug("*** updateAccountSw() method ended");

				AppAccount accModel = new AppAccount();
				BeanUtils.copyProperties(entity, accModel);

				// TODO:Need to complete email functionality
				if (activeSw.equals("Y")) {
					// send Email saying account activated
					try {
						// get file name
						fileName = appProperties.getProperties().get(AppConstants.ACTIVATE_EMAIL_FILE);
						// get mail subject
						mailSub = appProperties.getProperties().get(AppConstants.ACTIVATE_EMAIL_SUB);
						// decrypt the password
						password = PasswordUtils.decrypt(accModel.getPassword());
						// set decrypted password to accModel object password field
						accModel.setPassword(password);
						// get email body
						mailBody = getEmailBodyContent(accModel, fileName);
						// send email to activate registered cw/admin
						emailUtils.sendEmail(entity.getEmail(), mailSub, mailBody);
					} catch (Exception e) {
						logger.error("Email Sending is failed : " + e.getMessage());
					}
					return true;
				} else {

					try {
						// send Email saying account de-activated
						// send account de-activation mail to registered case worker/admin
						// get file name
						fileName = appProperties.getProperties().get(AppConstants.DE_ACTIVATE_EMAIL_FILE);
						// get email subject
						mailSub = appProperties.getProperties().get(AppConstants.DE_ACTIVATE_EMAIL_SUB);
						// get email body content
						mailBody = getDeActivateAccountEmailBodyContent(fileName, accModel);
						// send email to registered cw/admin
						emailUtils.sendEmail(entity.getEmail(), mailSub, mailBody);
					} catch (Exception e) {
						logger.error("Email Sending is failed : " + e.getMessage());
					}
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("Exception occured in updateAccountSw() method : " + e.getMessage());
		}
		return false;
	}// method

	/**
	 * This method is used to get inactive account email body content from file
	 * 
	 * @param fileName
	 * @param cwModel
	 * @return body
	 * @throws IOException
	 */
	public String getDeActivateAccountEmailBodyContent(String fileName, AppAccount accModel) throws IOException {
		// create BufferReader object
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		// create StringBuffer object
		StringBuffer body = new StringBuffer();
		// read the line from the text file
		String line = br.readLine();
		while (line != null) {
			if (line != null && !"".equals(line) && !"<br/>".equals(line)) {
				// process the line
				if (line.contains("USER_NAME")) {
					line = line.replace("USER_NAME", accModel.getFirstName() + " " + accModel.getLastName());
				}
				// Adding processed line to StringBuffer body
				body.append(line);
			}
			// read next line
			line = br.readLine();
		}
		// close/release br object
		br.close();
		return body.toString();
	}// method

	@Override
	public AppAccount findByAccountId(String accId) {
		AppAccountEntity entity = appAccRepository.findById(accId).get();

		AppAccount model = new AppAccount();

		BeanUtils.copyProperties(entity, model);

		String decryptedPwd = PasswordUtils.decrypt(model.getPassword());

		model.setPassword(decryptedPwd);

		return model;
	}

	/**
	 * This method is used edit application account details
	 *
	 * @Param accModel
	 * @return boolean
	 */
	@Override
	public boolean editAppAccount(AppAccount accModel) {
		logger.debug(" editAppAccount() started");
		String encryptedPwd = null;
		// create AppAccount Entity object
		AppAccountEntity entity = new AppAccountEntity();

		// convert model object to entity
		BeanUtils.copyProperties(accModel, entity);

		// convert the password to encrypted password
		encryptedPwd = PasswordUtils.encrypt(entity.getPassword());
		entity.setPassword(encryptedPwd);
		try {

			// call repository method
			AppAccountEntity appAccEntity = appAccRepository.save(entity);

			String fileName = appProperties.getProperties().get(AppConstants.UPDATE_ACC_EMAIL_FILE_NAME);
			String mailSub = appProperties.getProperties().get(AppConstants.ACCOUNT_UPDATE_SUBJECT);
			String mailBody = getEmailBodyContent(accModel, fileName);
			// sending confirmation mail
			emailUtils.sendEmail(accModel.getEmail(), mailSub, mailBody);
			logger.debug("editAppAccount() ended");

			if (appAccEntity.getAccId() !=null)
				return true;
		} catch (Exception e) {
			logger.error("editAppAccount() failed" + e.getMessage());
		}
		logger.info("editAppAccount completed Successfull");
		return false;
	}

	/**
	 * This method is used to register plan
	 * 
	 * @param planDetails
	 * @return boolean
	 */
	@Override
	public boolean createPlan(PlanDetails planModel) {
		logger.debug("AdminServiceImpl createPlan method started");
		try {
			// convert model to entity
			PlanDetailsEntity planEntity = new PlanDetailsEntity();

			BeanUtils.copyProperties(planModel, planEntity);
			// set Active Switch to "Y"
			planEntity.setActiveSw(AppConstants.ACTIVE_SW);
			// set Created And Updated By
			planEntity.setCreatedBy("Admin");
			planEntity.setUpdatedBy("Admin");
			// call repository method
			PlanDetailsEntity repoPlanEntity = planRepository.save(planEntity);

			logger.debug("AdminServiceImpl registerPlan method started");
			logger.info("Plan created Successfully");
			if (repoPlanEntity.getPlanId() !=null)
				return true;

		} catch (Exception e) {
			logger.error("Plan Creation Failed");
		}
		return false;
	}

	/**
	 * This method is used form checking plan name Uniqueness
	 * 
	 * @param plan
	 * @return String
	 */
	@Override
	public String checkDuplicatePlan(String plan) {
		logger.debug("CW Service:: checkDuplicatePlan method started");

		// call Repository method
		PlanDetailsEntity planEntity = planRepository.findByPlanName(plan);

		logger.debug("CW Service:: checkDuplicatePlan method started");

		return planEntity == null ? "Unique" : "Duplicate";

	}

	/**
	 * This method is used to get all application Accounts
	 * 
	 * @return List
	 */

	@Override
	public List<PlanDetails> findAllPlans() {
		logger.debug("AdminServiceImpl:: findAllPlans method started");
		List<PlanDetails> models = new ArrayList<PlanDetails>();

		try {
			List<PlanDetailsEntity> entities = planRepository.findAll();

			// checking for emptiness
			if (!entities.isEmpty()) {
				// convert PlanDetail entities into models
				for (PlanDetailsEntity entity : entities) {
					PlanDetails model = new PlanDetails();
					BeanUtils.copyProperties(entity, model);
					models.add(model);
				}
			}
		} catch (Exception e) {
			logger.error("Finding of Accounts method executio failed " + e.getMessage());
		}
		logger.debug("AdminServiceImpl:: findAllPlans method started");
		logger.info("findAllPlans completed ");

		return models;
	}

	/**
	 * This method used to perform soft delete of a Plan
	 * 
	 * @param id
	 * @return String
	 */
	@Override
	public boolean updatePlanActiveSw(String planId, String activeSw) {
		logger.debug(" AdminServiceImpl ::updatePlanActiveSw  started ");
		try {
			// load the record from database table
			PlanDetailsEntity entity = planRepository.findById(planId).get();
			// set active status to Y|N
			entity.setActiveSw(activeSw);

			// update The record in database table
			PlanDetailsEntity planEntity = planRepository.save(entity);

			logger.debug(" AdminServiceImpl ::updatePlanActiveSw ended ");
			logger.info("updatePlanActiveSw Successfully completed");
			// send Account Activation Email
			if (planEntity != null) {
				return true;
			}

		} catch (Exception e) {
			logger.error("Record Updation failed" + e.getMessage());
		}
		return false;
	}

	/**
	 * This method is used to find plan by planId
	 * 
	 * @param planId
	 * @return PlanDetails
	 */
	@Override
	public PlanDetails findPlanById(String planId) {
		logger.debug("AdminServiceImpl::findPlanById() started");
		PlanDetailsEntity planEntity = planRepository.findById(planId).get();
		try {
			if (planEntity != null) {
				// create Model
				PlanDetails planModel = new PlanDetails();
				// convert entity to model
				BeanUtils.copyProperties(planEntity, planModel);

				logger.debug("AdminServiceImpl::findPlanById() ended");
				logger.info("findPlanById() completed Successfully");

				return planModel;
			}
		} catch (Exception e) {
			logger.error("Plan Not Exist in database Table " + e.getMessage());
		}
		return null;
	}

	/**
	 * This method is used to edit plan Details
	 * 
	 * @param planModel
	 * @return boolean
	 */
	@Override
	public boolean editPlan(PlanDetails planModel) {
		logger.debug("AdminServiceImpl editPlan method started");
		try {
			// convert model to entity
			PlanDetailsEntity planEntity = new PlanDetailsEntity();

			BeanUtils.copyProperties(planModel, planEntity);
			// set Active Switch to "Y"
			// planEntity.setActiveSw(Constants.ACTIVE_SW);
			// set Created And Updated By
			// planEntity.setCreatedBy("Admin");

			// edit planUpdated By
			planEntity.setUpdatedBy("Admin");
			// call repository method
			PlanDetailsEntity updatedPlanEntity = planRepository.save(planEntity);

			logger.debug("AdminServiceImpl editPlan method started");
			logger.info("Plan Edited Successfully");
			if (updatedPlanEntity != null)
				return true;

		} catch (Exception e) {
			logger.error("Plan Creation Failed");
		}
		return false;
	}

}// class
