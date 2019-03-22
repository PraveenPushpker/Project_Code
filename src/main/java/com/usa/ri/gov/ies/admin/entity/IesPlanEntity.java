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
	private String startDate;

	@Column(name = "END_DT")
	private String endDate;
	
	@CreationTimestamp
	@Column(name = "CREATE_DT")
	private Date createDate;

	@CreationTimestamp
	@Column(name = "UPDATE_DT")
	private Date updateDate;

	@Column(name = "ACTIVE_SW")
	private String activeSw;

	/**
	 * @return the planId
	 */
	public Integer getPlanId() {
		return planId;
	}

	/**
	 * @param planId the planId to set
	 */
	public void setPlanId(Integer planId) {
		this.planId = planId;
	}

	/**
	 * @return the planName
	 */
	public String getPlanName() {
		return planName;
	}

	/**
	 * @param planName the planName to set
	 */
	public void setPlanName(String planName) {
		this.planName = planName;
	}

	/**
	 * @return the planDesc
	 */
	public String getPlanDesc() {
		return planDesc;
	}

	/**
	 * @param planDesc the planDesc to set
	 */
	public void setPlanDesc(String planDesc) {
		this.planDesc = planDesc;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the activeSw
	 */
	public String getActiveSw() {
		return activeSw;
	}

	/**
	 * @param activeSw the activeSw to set
	 */
	public void setActiveSw(String activeSw) {
		this.activeSw = activeSw;
	}

	

}
