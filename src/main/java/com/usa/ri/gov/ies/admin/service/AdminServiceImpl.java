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
import com.usa.ri.gov.ies.admin.entity.IesPlanEntity;
import com.usa.ri.gov.ies.admin.model.AppAccount;
import com.usa.ri.gov.ies.admin.model.IesPlan;
import com.usa.ri.gov.ies.admin.repository.AppAccountRepository;
import com.usa.ri.gov.ies.admin.repository.IesPlanRepository;
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
	private IesPlanRepository iesPlnRepository;

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
					// returning login flag as false and result message as Account is not Active,
					// For Query Contact IES Admin
					result.put(false, AppConstants.ACCOUNT_DE_ACTIVATE_MSG);
					logger.debug("**AdminService: login() method ended**");
					logger.info("**AdminService: login failure account deactive");
					return result;
				}
			} else {
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
		AppAccountEntity entity = appAccRepository.findByEmail(emailId);
		if (entity == null) {
			logger.debug(emailId + " email is not registered!!");
			return appProperties.getProperties().get(AppConstants.EMAIL_NOT_REG);
		} else {
			if (entity.getActiveSw().equals("N")) {
				logger.debug(emailId + "  account not in active");
				return appProperties.getProperties().get(AppConstants.ACCOUNT_DE_ACTIVATE_MSG);
			} else {
				try {
					AppAccount appAccountModel = new AppAccount();
					BeanUtils.copyProperties(entity, appAccountModel);
					appAccountModel.setPassword(PasswordUtils.decrypt(appAccountModel.getPassword()));
					logger.debug("***Password is decryted***");
					String mailBody = getEmailBodyContent(appAccountModel,
							appProperties.getProperties().get(AppConstants.FORGOT_PASSWORD_EMAIL_FILE_NAME));
					emailUtils.sendEmail(appAccountModel.getEmail(),
							appProperties.getProperties().get(AppConstants.FORGOT_PASSWORD_EMAIL_SUB), mailBody);
					logger.debug("password is sent  to " + emailId + " succussfully");
					logger.info("AdminServiceImpl::forgotPassword() is completed");
					return appProperties.getProperties().get(AppConstants.PWD_EMAIL_SENT_SUCC_MSG);
				} catch (Exception e) {
					logger.error("password is failed to sent to " + emailId);
					return appProperties.getProperties().get(AppConstants.PWD_EMAIL_SENT_FAIL_MSG);
				}

			} // else

		} // top else

	}// forgotPwd

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
		return (entity.getAccId() > 0) ? true : false;
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
			AppAccountEntity entity = appAccRepository.findById(Integer.parseInt(accId)).get();
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
	}

	@Override
	public boolean registerPlan(IesPlan createPlan) {
		logger.debug("Create Plan started");

		// Convert model data to Entity data
		IesPlanEntity entity = new IesPlanEntity();
		BeanUtils.copyProperties(createPlan, entity);

		// set Status as Active
		entity.setActiveSw(AppConstants.ACTIVE_SW);

		// Call Repository method
		entity = iesPlnRepository.save(entity);

		logger.debug("User Registration completed");
		logger.info("AdminServiceImpl::registerAccount() completed");
		return (entity.getPlanId() > 0) ? true : false;
	}// method

	@Override
	public String findByPlanName(String planId) {
		IesPlanEntity entity = iesPlnRepository.findByPlanName(planId);
		return (entity == null) ? "Unique" : "Duplicate";
	}

	@Override
	public List<IesPlan> findAllIesPlans() {
		logger.debug("findAllIesPlans() method started");
		List<IesPlan> models = new ArrayList<IesPlan>();
		try {
			// call Repository method
			List<IesPlanEntity> entities = iesPlnRepository.findAll();

			if (entities.isEmpty()) {
				logger.warn("***No Plans found in Application****");
			} else {
				// convert Entities to models
				for (IesPlanEntity entity : entities) {
					IesPlan model = new IesPlan();
					BeanUtils.copyProperties(entity, model);
					models.add(model);
				}
				logger.info("All Plans details loaded successfully");
			}
		} catch (Exception e) {
			logger.error("Exception occured in findAllIesPlans()::" + e.getMessage());
		}
		logger.debug("findAllIesPlans() method ended");
		return models;
	}

	@Override
	public boolean updatePlanSw(String planId, String activeSw) {
		logger.debug("*** updatePlanSw() method started");
		try {
			// load existing record using accId
			IesPlanEntity entity = iesPlnRepository.findById(Integer.parseInt(planId)).get();
			if (entity != null) {
				// Setting Account Active Sw (Y|N)
				entity.setActiveSw(activeSw);
				// Updating Account
				iesPlnRepository.save(entity);
				logger.debug("*** updatePlanSw() method ended");

				IesPlan plnModel = new IesPlan();
				BeanUtils.copyProperties(entity, plnModel);

			}
			return true;
		} catch (Exception e) {
			logger.error("Exception occured in updatePlanSw() method : " + e.getMessage());
		}
		return false;
	}// method

	/**
	 * This method is used to get Single plan record by ID
	 */
	@Override
	public IesPlan findByPlanId(Integer planId) {
		logger.debug("findPlanById() method started");

		IesPlan model = new IesPlan();

		try {
			// Loading PlanDetails Entity
			IesPlanEntity entity = iesPlnRepository.findById(planId).get();
			// Setting PlanDetails Entity to Model
			BeanUtils.copyProperties(entity, model);
			logger.info("Plan loaded Successfully");
		} catch (Exception e) {
			logger.error("Exception Occured in Loading Plan: " + e.getMessage());
		}

		logger.debug("findPlanById() method ended");
		return model;
	}

	/**
	 * This method is used to update plan
	 */
	@Override
	public boolean updatePlan(IesPlan iesPlan) {
		logger.debug("updatePlan() method started");

		try {
			// Loading PlanDetails Entity
			IesPlanEntity entity = iesPlnRepository.findById(iesPlan.getPlanId()).get();

			// Setting model object to Entity
			entity.setPlanName(iesPlan.getPlanName());
			entity.setPlanDesc(iesPlan.getPlanDesc());
			entity.setStartDate(iesPlan.getStartDate());
			entity.setEndDate(iesPlan.getEndDate());

			// calling save method
			iesPlnRepository.save(entity);
			return true;
		} catch (Exception e) {
			logger.error("Exception Occured in updatePlan(): " + e.getMessage());
		}

		logger.debug("updatePlan() method ended");
		return false;
	}

	@Override
	public AppAccount findByAccountId(Integer accId) {
		logger.debug("findPlanById() method started");

		AppAccount model = new AppAccount();

		try {
			// Loading PlanDetails Entity
			AppAccountEntity entity = appAccRepository.findById(accId).get();
			// Setting PlanDetails Entity to Model
			BeanUtils.copyProperties(entity, model);
			logger.info("Plan loaded Successfully");

			String decryptedPwd = PasswordUtils.decrypt(model.getPassword());

			model.setPassword(decryptedPwd);
		} catch (Exception e) {
			logger.error("Exception Occured in Loading Plan: " + e.getMessage());
		}

		logger.debug("findPlanById() method ended");
		return model;
	}

	@Override
	public boolean updateAccount(AppAccount accModel) {
		logger.debug("updatePlan() method started");
		String encryptedPwd = null;
		try {
			// Loading PlanDetails Entity
			AppAccountEntity entity = appAccRepository.findById(accModel.getAccId()).get();
			
			// Setting model object to Entity
			entity.setAccId(accModel.getAccId());
			entity.setFirstName(accModel.getFirstName());
			entity.setLastName(accModel.getLastName());
			entity.setEmail(accModel.getEmail());
			entity.setPassword(accModel.getPassword());
			entity.setDob(accModel.getDob());
			entity.setGender(accModel.getGender());
			entity.setSsn(accModel.getSsn());
			entity.setPhno(accModel.getPhno());
			entity.setRole(accModel.getRole());
			
			
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
		} catch (Exception e) {
			logger.error("Exception Occured in updatePlan(): "+e.getMessage());
		}
				
		logger.debug("updatePlan() method ended");
		return false;
	}




}// class
