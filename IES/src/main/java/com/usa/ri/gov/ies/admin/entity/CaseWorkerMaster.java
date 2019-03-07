package com.usa.ri.gov.ies.admin.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.Getter;

@Data()
@Entity
@Table(name = "CW_MASTER")
public class CaseWorkerMaster {
	
	@Id
	@GeneratedValue
	@Column(name = "CW_ID")
	private Integer caseWorkerId;
	
	@Column(name = "CW_FNAME")
	private String firstName;
	
	@Column(name = "CW_LNAME")
	private String lastName;

	@Column(name = "CW_EMAIL", unique = true)
	private String email;
	
	@Column(name = "CW_PWD")
	private String password;
	
	@Column(name = "CW_DOB")
	private Date dob;
	
	@Column(name = "CW_PHNO")
	private String phno;
	
	@Column(name = "CW_SNN")
	private Long ssn;

	@Column(name = "ACTIVE_SW")
	private String activeSw;

	@CreationTimestamp
	@Column(name = "CREATE_DT")
	private Date createDate;
	
	@UpdateTimestamp
	@Column(name = "UPDATE_DT")
	private Date updateDate;

}
