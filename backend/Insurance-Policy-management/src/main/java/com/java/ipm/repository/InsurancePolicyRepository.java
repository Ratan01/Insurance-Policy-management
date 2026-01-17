package com.java.ipm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.ipm.entity.InsurancePolicy;

public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long>{
	List<InsurancePolicy> findByUserId(Long userId);
}
