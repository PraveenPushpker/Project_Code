package com.usa.ri.gov.ies.admin.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.usa.ri.gov.ies.admin.entity.PlanDetailsEntity;

@Repository("planDetailsRepository")
public interface PlanDetailsRepository extends JpaRepository<PlanDetailsEntity, Serializable> {

	@Query(name="from PlanDetailsEntity where planName=:plnName")
	public PlanDetailsEntity findByPlanName(String plnName);
}