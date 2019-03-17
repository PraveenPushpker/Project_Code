package com.usa.ri.gov.ies.admin.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.usa.ri.gov.ies.admin.entity.IesPlanEntity;

@Repository("iesPlnRepository")
public interface IesPlanRepository extends JpaRepository<IesPlanEntity, Serializable> {
	
	@Query(name = "from IesPlanEntity where planName=:planId")
	public IesPlanEntity findByPlanName(String planId);

}
