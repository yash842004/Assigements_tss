package com.tss.banking.service.impl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tss.banking.dto.request.EMIPaymentRequestDTO;
import com.tss.banking.dto.request.LoanApplicationRequestDTO;
import com.tss.banking.dto.request.LoanApprovalRequestDTO;
import com.tss.banking.dto.request.LoanPaymentRequestDTO;
import com.tss.banking.dto.request.LoanRejectionRequestDTO;
import com.tss.banking.dto.request.TransactionRequestDTO;
import com.tss.banking.dto.response.LoanResponseDTO;
import com.tss.banking.dto.response.TransactionResponseDTO;
import com.tss.banking.entity.Account;
import com.tss.banking.entity.Admin;
import com.tss.banking.entity.Customer;
import com.tss.banking.entity.Loan;
import com.tss.banking.entity.eums.AccountStatus;
import com.tss.banking.entity.eums.CustomerStatus;
import com.tss.banking.entity.eums.LoanStatus;
import com.tss.banking.entity.eums.LoanType;
import com.tss.banking.entity.eums.TransactionType;
import com.tss.banking.exception.AccountNotFoundException;
import com.tss.banking.exception.AdminNotFoundException;
import com.tss.banking.exception.BusinessRuleViolationException;
import com.tss.banking.exception.CustomerNotFoundException;
import com.tss.banking.exception.InsufficientBalanceException;
import com.tss.banking.exception.LoanNotFoundException;
import com.tss.banking.exception.ValidationException;
import com.tss.banking.repository.AccountRepository;
import com.tss.banking.repository.AdminRepository;
import com.tss.banking.repository.CustomerRepository;
import com.tss.banking.repository.LoanRepository;
import com.tss.banking.service.EmailService;
import com.tss.banking.service.LoanService;
import com.tss.banking.service.TransactionService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@Transactional
public class LoanServiceImpl implements LoanService {
	@Autowired
	private LoanRepository loanRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private Validator validator;
	@Autowired
	private EmailService emailService;
	private final Random random = new Random();
	@Override
	public LoanResponseDTO applyForLoan(LoanApplicationRequestDTO request) {
		log.info("Processing loan application for account ID: {}", request.getAccountId());
		validateRequest(request);
		Account account = accountRepository.findById(request.getAccountId()).orElseThrow(
				() -> new AccountNotFoundException("Account not found with ID: " + request.getAccountId()));
		Customer customer = account.getCustomer();
		if (customer.getStatus() != CustomerStatus.ACTIVE) {
			throw new BusinessRuleViolationException("Loan applications are only allowed for active customers");
		}
		if (account.getStatus() != AccountStatus.ACTIVE) {
			throw new BusinessRuleViolationException("Loan applications are only allowed for active accounts");
		}
		BigDecimal monthlyPayment = calculateMonthlyPayment(request.getPrincipalAmount(), request.getInterestRate(),
				request.getTermMonths());
		LocalDate maturityDate = LocalDate.now().plusMonths(request.getTermMonths());
		LocalDate nextPaymentDate = LocalDate.now().plusMonths(1);
		String loanNumber = generateLoanNumber();
		Loan loan = Loan.builder().loanNumber(loanNumber).loanType(request.getLoanType())
				.principalAmount(request.getPrincipalAmount()).outstandingAmount(request.getPrincipalAmount())
				.interestRate(request.getInterestRate()).termMonths(request.getTermMonths())
				.monthlyPayment(monthlyPayment).status(LoanStatus.PENDING).applicationDate(LocalDateTime.now())
				.maturityDate(maturityDate).nextPaymentDate(nextPaymentDate).purpose(request.getPurpose())
				.customer(customer).account(account).build();
		Loan savedLoan = loanRepository.save(loan);
		log.info("Loan application created successfully with ID: {} and loan number: {}", savedLoan.getId(),
				savedLoan.getLoanNumber());
		return mapToResponseDTO(savedLoan);
	}
	@Override
	@Transactional(readOnly = true)
	public LoanResponseDTO getLoanById(Long loanId) {
		log.debug("Fetching loan with ID: {}", loanId);
		Loan loan = findLoanById(loanId);
		return mapToResponseDTO(loan);
	}
	@Override
	@Transactional(readOnly = true)
	public LoanResponseDTO getLoanByNumber(String loanNumber) {
		log.debug("Fetching loan with number: {}", loanNumber);
		Loan loan = loanRepository.findByLoanNumber(loanNumber)
				.orElseThrow(() -> new LoanNotFoundException("Loan not found with number: " + loanNumber));
		return mapToResponseDTO(loan);
	}
	@Override
	@Transactional(readOnly = true)
	public List<LoanResponseDTO> getLoansByCustomerId(Long customerId) {
		log.debug("Fetching loans for customer ID: {}", customerId);
		if (!customerRepository.existsById(customerId)) {
			throw new CustomerNotFoundException("Customer not found with ID: " + customerId);
		}
		List<Loan> loans = loanRepository.findByCustomerId(customerId);
		return loans.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
	}
	@Override
	@Transactional(readOnly = true)
	public Page<LoanResponseDTO> getLoansByCustomerId(Long customerId, Pageable pageable) {
		log.debug("Fetching loans for customer ID: {} with pagination", customerId);
		if (!customerRepository.existsById(customerId)) {
			throw new CustomerNotFoundException("Customer not found with ID: " + customerId);
		}
		Page<Loan> loanPage = loanRepository.findByCustomerIdOrderByApplicationDateDesc(customerId, pageable);
		return loanPage.map(this::mapToResponseDTO);
	}
	@Override
	@Transactional(readOnly = true)
	public List<LoanResponseDTO> getLoansByAccountId(Long accountId) {
		log.debug("Fetching loans for account ID: {}", accountId);
		if (!accountRepository.existsById(accountId)) {
			throw new AccountNotFoundException("Account not found with ID: " + accountId);
		}
		List<Loan> loans = loanRepository.findByAccountId(accountId);
		return loans.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
	}
	@Override
	@Transactional(readOnly = true)
	public List<LoanResponseDTO> getLoansByStatus(LoanStatus status) {
		log.debug("Fetching loans with status: {}", status);
		List<Loan> loans = loanRepository.findByStatus(status);
		return loans.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
	}
	@Override
	@Transactional(readOnly = true)
	public Page<LoanResponseDTO> getLoansByStatus(LoanStatus status, Pageable pageable) {
		log.debug("Fetching loans with status: {} with pagination", status);
		Page<Loan> loanPage = loanRepository.findByStatusOrderByApplicationDateDesc(status, pageable);
		return loanPage.map(this::mapToResponseDTO);
	}
	@Override
	@Transactional(readOnly = true)
	public List<LoanResponseDTO> getLoansByType(LoanType loanType) {
		log.debug("Fetching loans with type: {}", loanType);
		List<Loan> loans = loanRepository.findByLoanType(loanType);
		return loans.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
	}
	@Override
	@Transactional(readOnly = true)
	public List<LoanResponseDTO> getPendingLoanApplications() {
		log.debug("Fetching pending loan applications");
		List<Loan> loans = loanRepository.findPendingLoanApplications();
		return loans.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
	}
	@Override
	@Transactional(readOnly = true)
	public Page<LoanResponseDTO> getPendingLoanApplications(Pageable pageable) {
		log.debug("Fetching pending loan applications with pagination");
		Page<Loan> loanPage = loanRepository.findPendingLoanApplications(pageable);
		return loanPage.map(this::mapToResponseDTO);
	}
	@Override
	public LoanResponseDTO approveLoan(Long loanId, Long adminId, LoanApprovalRequestDTO request) {
		log.info("Approving loan with ID: {} by admin ID: {}", loanId, adminId);
		Loan loan = findLoanById(loanId);
		Admin admin = adminRepository.findById(adminId)
				.orElseThrow(() -> new AdminNotFoundException("Admin not found with ID: " + adminId));
		if (loan.getStatus() != LoanStatus.PENDING) {
			throw new BusinessRuleViolationException("Only pending loans can be approved");
		}
		loan.setStatus(LoanStatus.ACTIVE);
		loan.setApprovalDate(LocalDateTime.now());
		loan.setDisbursementDate(LocalDateTime.now());
		loan.setApprovedBy(admin);
		loan.setApprovalNotes(request.getApprovalNotes());
		loan.setLastUpdated(LocalDateTime.now());
		Account account = loan.getAccount();
		BigDecimal newBalance = account.getBalance().add(loan.getPrincipalAmount());
		account.setBalance(newBalance);
		accountRepository.save(account);
		TransactionRequestDTO transactionRequest = TransactionRequestDTO.builder().accountId(loan.getAccount().getId())
				.transactionType(TransactionType.LOAN_DISBURSEMENT).amount(loan.getPrincipalAmount())
				.description("Loan approved and disbursed - Loan Number: " + loan.getLoanNumber()).build();
		transactionService.processTransaction(transactionRequest);
		log.info("Loan approved and disbursed successfully with ID: {}, amount: {} credited to account: {}", loanId,
				loan.getPrincipalAmount(), account.getAccountNumber());
		Customer customer = loan.getCustomer();
		emailService.sendLoanDisbursementNotification(
	                customer.getEmail(),
	                customer.getFirstName() + " " + customer.getLastName(),
	                loan.getPrincipalAmount(),
	                account.getAccountNumber()
	            );
		return mapToResponseDTO(loan);
	}
	@Override
	public LoanResponseDTO rejectLoan(Long loanId, Long adminId, LoanRejectionRequestDTO request) {
		log.info("Rejecting loan with ID: {} by admin ID: {}", loanId, adminId);
		Loan loan = findLoanById(loanId);
		Admin admin = adminRepository.findById(adminId)
				.orElseThrow(() -> new AdminNotFoundException("Admin not found with ID: " + adminId));
		if (loan.getStatus() != LoanStatus.PENDING) {
			throw new BusinessRuleViolationException("Only pending loans can be rejected");
		}
		loan.setStatus(LoanStatus.REJECTED);
		loan.setApprovalDate(LocalDateTime.now());
		loan.setApprovedBy(admin);
		loan.setRejectionReason(request.getRejectionReason());
		loan.setLastUpdated(LocalDateTime.now());
		Loan savedLoan = loanRepository.save(loan);
		log.info("Loan rejected successfully with ID: {}", loanId);
		return mapToResponseDTO(savedLoan);
	}
	@Override
	public LoanResponseDTO disburseLoan(Long loanId) {
		log.info("Disbursing loan with ID: {}", loanId);
		Loan loan = findLoanById(loanId);
		if (loan.getStatus() != LoanStatus.APPROVED) {
			throw new BusinessRuleViolationException("Only approved loans can be disbursed");
		}
		TransactionRequestDTO transactionRequest = TransactionRequestDTO.builder().accountId(loan.getAccount().getId())
				.transactionType(TransactionType.LOAN_DISBURSEMENT).amount(loan.getPrincipalAmount())
				.description("Loan disbursement for loan " + loan.getLoanNumber()).build();
		transactionService.processTransaction(transactionRequest);
		loan.setStatus(LoanStatus.ACTIVE);
		loan.setDisbursementDate(LocalDateTime.now());
		loan.setLastUpdated(LocalDateTime.now());
		Loan savedLoan = loanRepository.save(loan);
		log.info("Loan disbursed successfully with ID: {}", loanId);
		return mapToResponseDTO(savedLoan);
	}
	@Override
	public TransactionResponseDTO makeLoanPayment(LoanPaymentRequestDTO request) {
		log.info("Processing loan payment for loan ID: {}, amount: {}", request.getLoanId(), request.getAmount());
		Loan loan = findLoanById(request.getLoanId());
		if (loan.getStatus() != LoanStatus.ACTIVE) {
			throw new BusinessRuleViolationException("Payments can only be made for active loans");
		}
		if (request.getAmount().compareTo(loan.getOutstandingAmount()) > 0) {
			throw new BusinessRuleViolationException("Payment amount cannot exceed outstanding amount");
		}
		Account account = loan.getAccount();
		if (account.getBalance().compareTo(request.getAmount()) < 0) {
			throw new InsufficientBalanceException("Insufficient balance for loan payment");
		}
		TransactionRequestDTO transactionRequest = TransactionRequestDTO.builder().accountId(account.getId())
				.transactionType(TransactionType.LOAN_PAYMENT).amount(request.getAmount())
				.description(request.getDescription() != null ? request.getDescription()
						: "Loan payment for loan " + loan.getLoanNumber())
				.build();
		TransactionResponseDTO transaction = transactionService.processTransaction(transactionRequest);
		BigDecimal newOutstanding = loan.getOutstandingAmount().subtract(request.getAmount());
		loan.setOutstandingAmount(newOutstanding);
		if (newOutstanding.compareTo(BigDecimal.ZERO) > 0) {
			loan.setNextPaymentDate(loan.getNextPaymentDate().plusMonths(1));
		} else {
			loan.setStatus(LoanStatus.PAID_OFF);
			loan.setNextPaymentDate(null);
		}
		loan.setLastUpdated(LocalDateTime.now());
		loanRepository.save(loan);
		log.info("Loan payment processed successfully for loan ID: {}", request.getLoanId());
		return transaction;
	}
	@Override
	public BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal interestRate, Integer termMonths) {
		if (principal == null || interestRate == null || termMonths == null || termMonths <= 0) {
			throw new IllegalArgumentException("Invalid parameters for monthly payment calculation");
		}
		if (interestRate.compareTo(BigDecimal.ZERO) == 0) {
			return principal.divide(BigDecimal.valueOf(termMonths), 2, RoundingMode.HALF_UP);
		}
		BigDecimal monthlyRate = interestRate.divide(BigDecimal.valueOf(100 * 12), 10, RoundingMode.HALF_UP);
		BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
		BigDecimal powerFactor = BigDecimal.ONE;
		for (int i = 0; i < termMonths; i++) {
			powerFactor = powerFactor.multiply(onePlusRate);
		}
		BigDecimal numerator = principal.multiply(monthlyRate).multiply(powerFactor);
		BigDecimal denominator = powerFactor.subtract(BigDecimal.ONE);
		return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
	}
	@Override
	@Transactional(readOnly = true)
	public BigDecimal getTotalOutstandingAmount(Long customerId) {
		log.debug("Calculating total outstanding amount for customer ID: {}", customerId);
		if (!customerRepository.existsById(customerId)) {
			throw new CustomerNotFoundException("Customer not found with ID: " + customerId);
		}
		BigDecimal totalOutstanding = loanRepository.getTotalOutstandingAmountByCustomerId(customerId);
		return totalOutstanding != null ? totalOutstanding : BigDecimal.ZERO;
	}
	@Override
	@Transactional(readOnly = true)
	public List<LoanResponseDTO> getLoansWithDuePayments() {
		log.debug("Fetching loans with due payments");
		List<Loan> loans = loanRepository.findLoansWithDuePayments();
		return loans.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
	}
	@Override
	public LoanResponseDTO cancelLoanApplication(Long loanId) {
		log.info("Cancelling loan application with ID: {}", loanId);
		Loan loan = findLoanById(loanId);
		if (loan.getStatus() != LoanStatus.PENDING) {
			throw new BusinessRuleViolationException("Only pending loan applications can be cancelled");
		}
		loan.setStatus(LoanStatus.CANCELLED);
		loan.setLastUpdated(LocalDateTime.now());
		Loan savedLoan = loanRepository.save(loan);
		log.info("Loan application cancelled successfully with ID: {}", loanId);
		return mapToResponseDTO(savedLoan);
	}
	@Override
	@Transactional(readOnly = true)
	public long getLoanCountByCustomer(Long customerId) {
		log.debug("Getting loan count for customer ID: {}", customerId);
		if (!customerRepository.existsById(customerId)) {
			throw new CustomerNotFoundException("Customer not found with ID: " + customerId);
		}
		return loanRepository.countByCustomerId(customerId);
	}
	@Override
	@Transactional(readOnly = true)
	public Loan getLoanEntityById(Long loanId) {
		return findLoanById(loanId);
	}
	@Override
	@Transactional(readOnly = true)
	public Long getLoanApplicantId(Long loanId) {
		Loan loan = findLoanById(loanId);
		return loan.getCustomer().getId();
	}
	public String generateLoanNumber() {
		String loanNumber;
		do {
			loanNumber = "LN" + String.format("%010d", Math.abs(random.nextLong() % 10000000000L));
		} while (loanRepository.existsByLoanNumber(loanNumber));
		return loanNumber;
	}
	private Loan findLoanById(Long loanId) {
		return loanRepository.findById(loanId)
				.orElseThrow(() -> new LoanNotFoundException("Loan not found with ID: " + loanId));
	}
	private LoanResponseDTO mapToResponseDTO(Loan loan) {
		return LoanResponseDTO.builder().id(loan.getId()).loanNumber(loan.getLoanNumber()).loanType(loan.getLoanType())
				.principalAmount(loan.getPrincipalAmount()).outstandingAmount(loan.getOutstandingAmount())
				.interestRate(loan.getInterestRate()).termMonths(loan.getTermMonths())
				.monthlyPayment(loan.getMonthlyPayment()).status(loan.getStatus())
				.applicationDate(loan.getApplicationDate()).approvalDate(loan.getApprovalDate())
				.disbursementDate(loan.getDisbursementDate()).maturityDate(loan.getMaturityDate())
				.nextPaymentDate(loan.getNextPaymentDate()).purpose(loan.getPurpose())
				.approvalNotes(loan.getApprovalNotes()).rejectionReason(loan.getRejectionReason())
				.createdDate(loan.getCreatedDate()).lastUpdated(loan.getLastUpdated())
				.customerId(loan.getCustomer().getId())
				.customerName(loan.getCustomer().getFirstName() + " " + loan.getCustomer().getLastName())
				.accountId(loan.getAccount().getId()).accountNumber(loan.getAccount().getAccountNumber())
				.missedPayments(loan.getMissedPayments()).lateFeeAmount(loan.getLateFeeAmount())
				.totalLateFees(loan.getTotalLateFees()).isOverdue(loan.getIsOverdue())
				.overdueDays(loan.getOverdueDays())
				.approvedBy(loan.getApprovedBy() != null ? loan.getApprovedBy().getId() : null)
				.approvedByName(loan.getApprovedBy() != null
						? loan.getApprovedBy().getFirstName() + " " + loan.getApprovedBy().getLastName()
						: null)
				.build();
	}
	private <T> void validateRequest(T request) {
		var violations = validator.validate(request);
		if (!violations.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<T> violation : violations) {
				sb.append(violation.getMessage()).append("; ");
			}
			throw new ValidationException("Validation failed: " + sb.toString());
		}
	}
	@Override
	public TransactionResponseDTO processEMIPayment(EMIPaymentRequestDTO request) {
		log.info("Processing EMI payment for loan ID: {}, amount: {}", request.getLoanId(), request.getAmount());
		Loan loan = findLoanById(request.getLoanId());
		if (loan.getStatus() != LoanStatus.ACTIVE) {
			throw new BusinessRuleViolationException("EMI payments can only be made for active loans");
		}
		Account account = loan.getAccount();
		if (request.getAmount().compareTo(loan.getMonthlyPayment()) < 0
				&& request.getAmount().compareTo(loan.getOutstandingAmount()) < 0) {
			throw new BusinessRuleViolationException(
					"Payment amount should be at least the monthly EMI amount or the remaining outstanding amount");
		}
		if (request.getIsAutomaticDeduction() && account.getBalance().compareTo(request.getAmount()) < 0) {
			BigDecimal lateFee = calculateLateFee(loan.getId());
			loan.setLateFeeAmount(loan.getLateFeeAmount().add(lateFee));
			loan.setTotalLateFees(loan.getTotalLateFees().add(lateFee));
			loan.setMissedPayments(loan.getMissedPayments() + 1);
			loan.setIsOverdue(true);
			loanRepository.save(loan);
			throw new InsufficientBalanceException(
					"Insufficient balance for EMI payment. Late fee of â‚¹" + lateFee + " applied.");
		}
		if (!request.getIsAutomaticDeduction() && account.getBalance().compareTo(request.getAmount()) < 0) {
			throw new InsufficientBalanceException("Insufficient balance for loan payment");
		}
		TransactionRequestDTO transactionRequest = TransactionRequestDTO.builder().accountId(account.getId())
				.transactionType(TransactionType.LOAN_PAYMENT).amount(request.getAmount())
				.description(request.getDescription() != null ? request.getDescription()
						: "EMI Payment for loan " + loan.getLoanNumber())
				.build();
		TransactionResponseDTO transaction = transactionService.processTransaction(transactionRequest);
		BigDecimal newOutstanding = loan.getOutstandingAmount().subtract(request.getAmount());
		loan.setOutstandingAmount(newOutstanding);
		if (loan.getIsOverdue()) {
			loan.setIsOverdue(false);
			loan.setOverdueDays(0);
		}
		if (newOutstanding.compareTo(BigDecimal.ZERO) > 0) {
			loan.setNextPaymentDate(loan.getNextPaymentDate().plusMonths(1));
		} else {
			loan.setStatus(LoanStatus.PAID_OFF);
			loan.setNextPaymentDate(null);
		}
		loan.setLastUpdated(LocalDateTime.now());
		loan = loanRepository.save(loan);
		log.info("EMI payment processed successfully for loan ID: {}", request.getLoanId());
		Customer customer = loan.getCustomer();
		emailService.sendEMIDeductionNotification(customer.getEmail(),
				customer.getFirstName() + " " + customer.getLastName(), request.getAmount(),
				loan.getOutstandingAmount());
		return transaction;
	}
	@Override
	public void processAutomaticEMIDeduction(Long loanId) {
		log.info("Processing automatic EMI deduction for loan ID: {}", loanId);
		Loan loan = findLoanById(loanId);
		if (loan.getStatus() != LoanStatus.ACTIVE) {
			return;
		}
		EMIPaymentRequestDTO emiRequest = EMIPaymentRequestDTO.builder().loanId(loanId).amount(loan.getMonthlyPayment())
				.description("Automatic EMI deduction for loan " + loan.getLoanNumber()).isAutomaticDeduction(true)
				.build();
		try {
			processEMIPayment(emiRequest);
		} catch (Exception e) {
			log.error("Failed to process automatic EMI deduction for loan ID: {}", loanId, e);
		}
	}
	@Override
	public void processOverdueLoans() {
		log.info("Processing overdue loans for late fees");
		LocalDate today = LocalDate.now();
		List<Loan> overdueLoans = loanRepository.findActiveLoansWithOverduePayments(today);
		for (Loan loan : overdueLoans) {
			try {
				long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(loan.getNextPaymentDate(), today);
				if (overdueDays > 0) {
					loan.setIsOverdue(true);
					loan.setOverdueDays((int) overdueDays);
					if (overdueDays % 30 == 0) {
						BigDecimal lateFee = calculateLateFee(loan.getId());
						loan.setLateFeeAmount(loan.getLateFeeAmount().add(lateFee));
						loan.setTotalLateFees(loan.getTotalLateFees().add(lateFee));
						loan.setOutstandingAmount(loan.getOutstandingAmount().add(lateFee));
						log.info("Applied late fee of â‚¹{} to loan ID: {} (overdue by {} days)", lateFee, loan.getId(),
								overdueDays);
					}
					loanRepository.save(loan);
				}
			} catch (Exception e) {
				log.error("Failed to process overdue loan ID: {}", loan.getId(), e);
			}
		}
	}
	@Override
	@Transactional(readOnly = true)
	public List<LoanResponseDTO> getOverdueLoans() {
		log.debug("Fetching overdue loans");
		List<Loan> overdueLoans = loanRepository.findOverdueLoans();
		return overdueLoans.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
	}
	@Override
	public BigDecimal calculateLateFee(Long loanId) {
		Loan loan = findLoanById(loanId);
		BigDecimal lateFeeRate = new BigDecimal("0.02");
		BigDecimal calculatedFee = loan.getMonthlyPayment().multiply(lateFeeRate);
		BigDecimal minimumFee = new BigDecimal("100.00");
		return calculatedFee.compareTo(minimumFee) > 0 ? calculatedFee : minimumFee;
	}
}
