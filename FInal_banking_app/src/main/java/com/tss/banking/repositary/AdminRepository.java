package com.tss.banking.repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tss.banking.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long>{

}
