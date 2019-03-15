package com.usa.ri.gov.ies.admin.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usa.ri.gov.ies.admin.entity.CreatePlanEntity;

@Repository("ctrPlnRepository")
public interface CreatePlanRepository extends JpaRepository<CreatePlanEntity, Serializable> {

}
