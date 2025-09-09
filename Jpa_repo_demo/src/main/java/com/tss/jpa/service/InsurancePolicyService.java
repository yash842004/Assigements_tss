package com.tss.jpa.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tss.jpa.entity.InsurancePolicy;

public interface InsurancePolicyService {
	
	InsurancePolicy createPolicy(InsurancePolicy policy);

    Page<InsurancePolicy> getAllPolicies(Pageable pageable);

    InsurancePolicy getPolicyById(Long id);

    Page<InsurancePolicy> getPoliciesByHolderName(String name, Pageable pageable);

    Page<InsurancePolicy> getPoliciesWithDurationLessThan(int years, Pageable pageable);

    void deletePolicyByNumber(String number);

}
