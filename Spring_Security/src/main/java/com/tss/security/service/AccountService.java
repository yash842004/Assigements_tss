package com.tss.security.service;

import com.tss.security.dto.AccountRequestDto;
import com.tss.security.dto.AccountResponseDto;
import com.tss.security.entity.Account;
import com.tss.security.exception.ResourceNotFoundException;
import com.tss.security.repositary.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID; // Import UUID
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountResponseDto createAccount(AccountRequestDto request) {
        Account account = new Account();
        account.setHolderName(request.getHolderName());
        account.setBalance(request.getInitialBalance()); // Assumes DTO now uses BigDecimal

        // **FIX:** Generate and set a unique account number
        String uniqueAccountNumber = UUID.randomUUID().toString();
        account.setAccountNumber(uniqueAccountNumber);

        // **FIX:** Explicitly set isEnabled, though now handled by entity default
        account.setEnabled(true);

        Account savedAccount = accountRepository.save(account);
        return new AccountResponseDto(savedAccount);
    }

    public List<AccountResponseDto> readAllAccounts() {
        return accountRepository.findAll().stream()
                .map(AccountResponseDto::new)
                .collect(Collectors.toList());
    }

    public AccountResponseDto readAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        return new AccountResponseDto(account);
    }

    @Transactional
    public void disableAccount(Long id) {
        // This check correctly throws an exception if the account doesn't exist.
        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account not found with id: " + id);
        }
        accountRepository.disableAccount(id);
    }
}