package com.tss.banking.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import com.tss.banking.entity.eums.CustomerStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long customerId;  

	    @Column(nullable = false, length = 120)
	    private String fullName;

	    @Column(nullable = false, length = 180, unique = true)
	    private String email;

	    @Column(nullable = false, length = 255)
	    private String password;

	    @Column(length = 255)
	    private String address;

	    @Column(length = 30)
	    private String phone;

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false, length = 20)
	    private CustomerStatus status = CustomerStatus.ACTIVE;

	    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	    private Set<Account> accounts = new LinkedHashSet<>();

	    
	    public void addAccount(Account account) {
	        accounts.add(account);
	        account.setCustomer(this);
	    }

	    public void removeAccount(Account account) {
	        accounts.remove(account);
	        account.setCustomer(null);
	    }


}
