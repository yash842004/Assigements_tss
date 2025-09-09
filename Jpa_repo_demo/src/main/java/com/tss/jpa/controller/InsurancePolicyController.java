package com.tss.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tss.jpa.entity.InsurancePolicy;
import com.tss.jpa.service.InsurancePolicyService;

import jakarta.validation.Valid;				

@RestController
@RequestMapping("/api/policies")
public class InsurancePolicyController {
	
	@Autowired
    private InsurancePolicyService service;

    @PostMapping
    public ResponseEntity<InsurancePolicy> createPolicy(@Valid @RequestBody InsurancePolicy policy) {
        InsurancePolicy created = service.createPolicy(policy);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<Page<InsurancePolicy>> getAllPolicies(Pageable pageable) {
        Page<InsurancePolicy> policies = service.getAllPolicies(pageable);
        return ResponseEntity.ok(policies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsurancePolicy> getPolicyById(@PathVariable Long id) {
        InsurancePolicy policy = service.getPolicyById(id);
        if (policy == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(policy);
    }

    @GetMapping("/holder/{name}")
    public ResponseEntity<Page<InsurancePolicy>> getPoliciesByHolderName(@PathVariable String name, Pageable pageable) {
        Page<InsurancePolicy> policies = service.getPoliciesByHolderName(name, pageable);
        return ResponseEntity.ok(policies);
    }

    @GetMapping("/duration-less-than/{years}")
    public ResponseEntity<Page<InsurancePolicy>> getPoliciesWithDurationLessThan(@PathVariable int years, Pageable pageable) {
        Page<InsurancePolicy> policies = service.getPoliciesWithDurationLessThan(years, pageable);
        return ResponseEntity.ok(policies);
    }

    @DeleteMapping("/{policyNumber}")
    public ResponseEntity<Void> deletePolicyByNumber(@PathVariable String policyNumber) {
        service.deletePolicyByNumber(policyNumber);
        return ResponseEntity.noContent().build();
    }

}
