package com.tss.jpa.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tss.jpa.Repositary.InsurancePolicyRepository;
import com.tss.jpa.entity.InsurancePolicy;

import jakarta.transaction.Transactional;

@Service
public class InsurancePolicyServiceImpl implements InsurancePolicyService {

	@Autowired
	private InsurancePolicyRepository repository;

	@Override
	public InsurancePolicy createPolicy(InsurancePolicy policy) {
		policy.setPolicyNumber(UUID.randomUUID().toString());
		return repository.save(policy);
	}

	@Override
	public Page<InsurancePolicy> getAllPolicies(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public InsurancePolicy getPolicyById(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Page<InsurancePolicy> getPoliciesByHolderName(String name, Pageable pageable) {
		return repository.findByHolderNameContaining(name, pageable);
	}

	@Override
	public Page<InsurancePolicy> getPoliciesWithDurationLessThan(int years, Pageable pageable) {
		return repository.findByDurationLessThan(years, pageable);
	}

	@Override
	@Transactional   
	public void deletePolicyByNumber(String number) {
		repository.deleteByPolicyNumber(number);
	}

}
