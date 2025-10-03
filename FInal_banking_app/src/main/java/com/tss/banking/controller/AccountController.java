package com.tss.banking.controller;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tss.banking.dto.request.AccountCreationRequestDTO;
import com.tss.banking.dto.request.JointAccountCreationRequestDTO;
import com.tss.banking.dto.request.JointAccountHolderRequestDTO;
import com.tss.banking.dto.response.AccountHolderResponseDTO;
import com.tss.banking.dto.response.AccountResponseDTO;
import com.tss.banking.dto.response.AccountSummaryResponseDTO;
import com.tss.banking.dto.response.ApiResponseDTO;
import com.tss.banking.service.AccountService;
import com.tss.banking.util.AccessControlUtil;
import jakarta.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccessControlUtil accessControlUtil;
    @PostMapping
    public ResponseEntity<ApiResponseDTO<AccountResponseDTO>> createAccount(@RequestBody AccountCreationRequestDTO accountCreationRequest) {
        try {
            AccountResponseDTO account = accountService.createAccount(accountCreationRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Account created successfully", account));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Account creation failed: " + e.getMessage()));
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AccountResponseDTO>> getAccountById(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long accountOwnerId = accountService.getAccountOwnerId(id);
            accessControlUtil.validateAccountAccess(request, accountOwnerId);
            AccountResponseDTO account = accountService.getAccountById(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Account retrieved successfully", account));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve account: " + e.getMessage()));
        }
    }
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponseDTO<List<AccountResponseDTO>>> getAccountsByCustomerId(@PathVariable Long customerId, HttpServletRequest request) {
        try {
            accessControlUtil.validateCustomerAccess(request, customerId);
            List<AccountResponseDTO> accounts = accountService.getAccountsByCustomerId(customerId);
            return ResponseEntity.ok(ApiResponseDTO.success("Accounts retrieved successfully", accounts));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve accounts: " + e.getMessage()));
        }
    }
    @GetMapping("/{id}/balance")
    public ResponseEntity<ApiResponseDTO<BigDecimal>> getAccountBalance(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long accountOwnerId = accountService.getAccountOwnerId(id);
            accessControlUtil.validateAccountAccess(request, accountOwnerId);
            BigDecimal balance = accountService.getAccountBalance(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Balance retrieved successfully", balance));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve balance: " + e.getMessage()));
        }
    }
    @GetMapping("/{id}/summary")
    public ResponseEntity<ApiResponseDTO<AccountSummaryResponseDTO>> getAccountSummary(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long accountOwnerId = accountService.getAccountOwnerId(id);
            accessControlUtil.validateAccountAccess(request, accountOwnerId);
            AccountSummaryResponseDTO summary = accountService.getAccountSummary(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Account summary retrieved successfully", summary));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve account summary: " + e.getMessage()));
        }
    }
    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<AccountResponseDTO>>> getAllAccounts(Pageable pageable, HttpServletRequest request) {
        try {
            accessControlUtil.validateAdminOperationAccess(request, "VIEW_ACCOUNTS");
            Page<AccountResponseDTO> accounts = accountService.getAllAccounts(pageable);
            return ResponseEntity.ok(ApiResponseDTO.success("Accounts retrieved successfully", accounts));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve accounts: " + e.getMessage()));
        }
    }
    @PutMapping("/{id}/close")
    public ResponseEntity<ApiResponseDTO<String>> closeAccount(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long accountOwnerId = accountService.getAccountOwnerId(id);
            accessControlUtil.validateAccountAccess(request, accountOwnerId);
            accountService.closeAccount(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Account closed successfully", "OK"));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to close account: " + e.getMessage()));
        }
    }
    @PutMapping("/{id}/reopen")
    public ResponseEntity<ApiResponseDTO<AccountResponseDTO>> reopenAccount(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long accountOwnerId = accountService.getAccountOwnerId(id);
            accessControlUtil.validateAccountAccess(request, accountOwnerId);
            AccountResponseDTO account = accountService.reopenAccount(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Account reopened successfully", account));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to reopen account: " + e.getMessage()));
        }
    }
    @PostMapping("/joint")
    public ResponseEntity<ApiResponseDTO<AccountResponseDTO>> createJointAccount(
            @RequestBody JointAccountCreationRequestDTO jointAccountRequest, HttpServletRequest request) {
        try {
            accessControlUtil.validateCustomerAccess(request, jointAccountRequest.getPrimaryCustomerId());
            AccountResponseDTO account = accountService.createJointAccount(jointAccountRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Joint account created successfully", account));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Joint account creation failed: " + e.getMessage()));
        }
    }
    @PostMapping("/{id}/holders")
    public ResponseEntity<ApiResponseDTO<AccountResponseDTO>> addJointAccountHolder(
            @PathVariable Long id, @RequestBody JointAccountHolderRequestDTO jointHolderRequest, HttpServletRequest request) {
        try {
            Long accountOwnerId = accountService.getAccountOwnerId(id);
            accessControlUtil.validateAccountAccess(request, accountOwnerId);
            jointHolderRequest.setAccountId(id);
            AccountResponseDTO account = accountService.addJointAccountHolder(jointHolderRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Joint account holder added successfully", account));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to add joint account holder: " + e.getMessage()));
        }
    }
    @PostMapping("/joint/add-holder")
    public ResponseEntity<ApiResponseDTO<AccountResponseDTO>> addJointAccountHolderAlternative(
            @RequestBody JointAccountHolderRequestDTO jointHolderRequest, HttpServletRequest request) {
        try {
            Long accountOwnerId = accountService.getAccountOwnerId(jointHolderRequest.getAccountId());
            accessControlUtil.validateAccountAccess(request, accountOwnerId);
            AccountResponseDTO account = accountService.addJointAccountHolder(jointHolderRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Joint account holder added successfully", account));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to add joint account holder: " + e.getMessage()));
        }
    }
    @DeleteMapping("/{id}/holders/{customerId}")
    public ResponseEntity<ApiResponseDTO<AccountResponseDTO>> removeJointAccountHolder(
            @PathVariable Long id, @PathVariable Long customerId, HttpServletRequest request) {
        try {
            Long accountOwnerId = accountService.getAccountOwnerId(id);
            accessControlUtil.validateAccountAccess(request, accountOwnerId);
            JointAccountHolderRequestDTO jointHolderRequest = new JointAccountHolderRequestDTO();
            jointHolderRequest.setAccountId(id);
            jointHolderRequest.setCustomerId(customerId);
            AccountResponseDTO account = accountService.removeJointAccountHolder(jointHolderRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Joint account holder removed successfully", account));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to remove joint account holder: " + e.getMessage()));
        }
    }
    @DeleteMapping("/{id}/holders")
    public ResponseEntity<ApiResponseDTO<AccountResponseDTO>> removeJointAccountHolderWithBody(
            @PathVariable Long id, @RequestBody JointAccountHolderRequestDTO request, HttpServletRequest httpRequest) {
        try {
            request.setAccountId(id);
            Long accountOwnerId = accountService.getAccountOwnerId(id);
            accessControlUtil.validateAccountAccess(httpRequest, accountOwnerId);
            AccountResponseDTO account = accountService.removeJointAccountHolder(request);
            return ResponseEntity.ok(ApiResponseDTO.success("Joint account holder removed successfully", account));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to remove joint account holder: " + e.getMessage()));
        }
    }
    @GetMapping("/{id}/holders")
    public ResponseEntity<ApiResponseDTO<List<AccountHolderResponseDTO>>> getJointAccountHolders(
            @PathVariable Long id, HttpServletRequest request) {
        try {
            Long currentUserId = accessControlUtil.getCurrentUserId(request);
            if (currentUserId == null || !accountService.hasCustomerAccessToAccount(id, currentUserId)) {
                accessControlUtil.validateAdminOperationAccess(request, "VIEW_ACCOUNT_HOLDERS");
            }
            List<AccountHolderResponseDTO> holders = accountService.getJointAccountHolders(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Joint account holders retrieved successfully", holders));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve joint account holders: " + e.getMessage()));
        }
    }
    @GetMapping("/customer/{customerId}/accessible")
    public ResponseEntity<ApiResponseDTO<List<AccountResponseDTO>>> getAllAccessibleAccounts(
            @PathVariable Long customerId, HttpServletRequest request) {
        try {
            accessControlUtil.validateCustomerAccess(request, customerId);
            List<AccountResponseDTO> accounts = accountService.getAllAccessibleAccountsByCustomerId(customerId);
            return ResponseEntity.ok(ApiResponseDTO.success("Accessible accounts retrieved successfully", accounts));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve accessible accounts: " + e.getMessage()));
        }
    }
    @GetMapping("/joint")
    public ResponseEntity<ApiResponseDTO<Page<AccountResponseDTO>>> getAllJointAccounts(
            Pageable pageable, HttpServletRequest request) {
        try {
            accessControlUtil.validateAdminOperationAccess(request, "VIEW_JOINT_ACCOUNTS");
            Page<AccountResponseDTO> accounts = accountService.getAllJointAccounts(pageable);
            return ResponseEntity.ok(ApiResponseDTO.success("Joint accounts retrieved successfully", accounts));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve joint accounts: " + e.getMessage()));
        }
    }
    @PutMapping("/{id}/convert-to-joint")
    public ResponseEntity<ApiResponseDTO<AccountResponseDTO>> convertToJointAccount(
            @PathVariable Long id, @RequestBody List<Long> secondaryCustomerIds, HttpServletRequest request) {
        try {
            Long accountOwnerId = accountService.getAccountOwnerId(id);
            accessControlUtil.validateAccountAccess(request, accountOwnerId);
            AccountResponseDTO account = accountService.convertToJointAccount(id, secondaryCustomerIds);
            return ResponseEntity.ok(ApiResponseDTO.success("Account converted to joint account successfully", account));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to convert account to joint account: " + e.getMessage()));
        }
    }
}
