package com.usa.ri.gov.ies.admin.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.usa.ri.gov.ies.admin.entity.AppAccountEntity;

@Repository("appAccRepository")
public interface AppAccountRepository extends JpaRepository<AppAccountEntity, Serializable> {

	@Query(name = "from AppAccountEntity where email=:emailId")
	public AppAccountEntity findByEmail(String emailId);

}
