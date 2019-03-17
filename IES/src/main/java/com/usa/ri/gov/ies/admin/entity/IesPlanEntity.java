package com.usa.ri.gov.ies.admin.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;


@Entity
@Table(name = "IES_PLAN_DETAILS")
public class IesPlanEntity {
	
	@Id
	@GeneratedValue
	@Column(name = "PLN_ID")
	private Integer planId;
	
	@Column(name = "PLN_NAME", unique = true)
	private String planName;
	
	@Column(name = "PLN_DESC")
	private String planDesc;
	
	@Column(name = "START_DT")
	private Date startDate;

	@Column(name = "END_DT")
	private Date endDate;
	
	@CreationTimestamp
	@Column(name = "CREATE_DT")
	private Date createDate;

	@CreationTimestamp
	@Column(name = "UPDATE_DT")
	private Date updateDate;

	@Column(name = "ACTIVE_SW")
	private String activeSw;

	public Integer getPlanId() {
		return planId;
	}

	public void setPlanId(Integer planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getPlanDesc() {
		return planDesc;
	}

	public void setPlanDesc(String planDesc) {
		this.planDesc = planDesc;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getActiveSw() {
		return activeSw;
	}

	public void setActiveSw(String activeSw) {
		this.activeSw = activeSw;
	}


}
