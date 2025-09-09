package com.tss.jpa.Repositary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tss.jpa.entity.InsurancePolicy;

public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long> {

	Page<InsurancePolicy> findByHolderNameContaining(String name, Pageable pageable);

    @Query(value = "SELECT * FROM insurance_policy WHERE DATEDIFF(DAY, start_date, end_date) / 365 < ?1", nativeQuery = true)
    Page<InsurancePolicy> findByDurationLessThan(int years, Pageable pageable);

    void deleteByPolicyNumber(String policyNumber);
}
