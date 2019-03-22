package com.usa.ri.gov.ies.admin.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;




public class PlanDetails {
	
	private String planId;
	
	private String planName;
	
	private String planDescription;
	
	//@DateTimeFormat(pattern="MM-dd-yyyy")
	private String planStartDate;
	
	//@DateTimeFormat(pattern="MM-dd-yyyy")
	private String planEndDate;
	
	private String activeSw;
	
	private String createdBy;
	
	private String updatedBy;
	
	//@DateTimeFormat(pattern = "dd-MM-yyyy")
	//@Temporal(TemporalType.DATE)
	private Timestamp createDate;
	
	//@DateTimeFormat(pattern = "dd-MM-yyyy")
	//@Temporal(TemporalType.DATE)
	private Timestamp updateDate;

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getPlanDescription() {
		return planDescription;
	}

	public void setPlanDescription(String planDescription) {
		this.planDescription = planDescription;
	}

	public String getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(String planStartDate) {
		this.planStartDate = planStartDate;
	}

	public String getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(String planEndDate) {
		this.planEndDate = planEndDate;
	}

	public String getActiveSw() {
		return activeSw;
	}

	public void setActiveSw(String activeSw) {
		this.activeSw = activeSw;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	
}
