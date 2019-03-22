package com.usa.ri.gov.ies.admin.service;

import java.util.List;
import java.util.Map;

import com.usa.ri.gov.ies.admin.model.AppAccount;
import com.usa.ri.gov.ies.admin.model.PlanDetails;

public interface AdminService {

	public boolean registerAccount(AppAccount appAccount);

	public String findByEmail(String emailId);
	
	public List<AppAccount> findAllAppAccounts();
	
	public boolean updateAccountSw(String accId,String activeSw);

	public AppAccount findByAccountId(String accId);
	
	boolean editAppAccount(AppAccount accModel);
	
	public boolean createPlan(PlanDetails planDetails);
	
	public String checkDuplicatePlan(String plan);
	
	public List<PlanDetails> findAllPlans();
	
	public boolean updatePlanActiveSw(String planId, String activeSw);
	
	public PlanDetails findPlanById(String planId);
	
	public boolean editPlan(PlanDetails planDetails);

	public Map<Boolean, String>  login(String email, String pwd);
	
	public String forgotPassword(String emailId);

	
}
