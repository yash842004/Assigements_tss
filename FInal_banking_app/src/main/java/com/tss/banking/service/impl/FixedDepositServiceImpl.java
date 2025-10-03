package com.tss.banking.service.impl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tss.banking.dto.request.FDClosureRequestDTO;
import com.tss.banking.dto.request.FDCreateRequestDTO;
import com.tss.banking.dto.response.FDResponseDTO;
import com.tss.banking.dto.response.PagedResponseDTO;
import com.tss.banking.entity.Account;
import com.tss.banking.entity.Customer;
import com.tss.banking.entity.FixedDeposit;
import com.tss.banking.entity.Transaction;
import com.tss.banking.entity.eums.FDStatus;
import com.tss.banking.entity.eums.TransactionType;
import com.tss.banking.exception.AccountNotFoundException;
import com.tss.banking.exception.CustomerNotFoundException;
import com.tss.banking.exception.FixedDepositNotFoundException;
import com.tss.banking.exception.InsufficientFundsException;
import com.tss.banking.repository.AccountRepository;
import com.tss.banking.repository.CustomerRepository;
import com.tss.banking.repository.FixedDepositRepository;
import com.tss.banking.repository.TransactionRepository;
import com.tss.banking.service.EmailService;
import com.tss.banking.service.FixedDepositService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FixedDepositServiceImpl implements FixedDepositService {
	@Autowired
	private FixedDepositRepository fdRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private EmailService emailService;
	@Override
	public FDResponseDTO createFD(FDCreateRequestDTO request) {
		log.info("Creating FD for customer ID: {} with amount: {}", request.getCustomerId(),
				request.getPrincipalAmount());
		Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(
				() -> new CustomerNotFoundException("Customer not found with ID: " + request.getCustomerId()));
		Account account = accountRepository.findById(request.getAccountId()).orElseThrow(
				() -> new AccountNotFoundException("Account not found with ID: " + request.getAccountId()));
		if (!account.getCustomer().getId().equals(request.getCustomerId())) {
			throw new IllegalStateException("Account does not belong to the specified customer");
		}
		if (account.getBalance().compareTo(request.getPrincipalAmount()) < 0) {
			throw new InsufficientFundsException(account.getBalance(), request.getPrincipalAmount());
		}
		BigDecimal maturityAmount = calculateMaturityAmount(request.getPrincipalAmount(), request.getInterestRate(),
				request.getTenureMonths());
		LocalDate maturityDate = LocalDate.now().plusMonths(request.getTenureMonths());
		String fdNumber = generateFDNumber();
		BigDecimal newBalance = account.getBalance().subtract(request.getPrincipalAmount());
		account.setBalance(newBalance);
		accountRepository.save(account);
		FixedDeposit fd = FixedDeposit.builder().fdNumber(fdNumber).customer(customer)
				.principalAmount(request.getPrincipalAmount()).interestRate(request.getInterestRate())
				.tenureMonths(request.getTenureMonths()).maturityAmount(maturityAmount).openingDate(LocalDate.now())
				.maturityDate(maturityDate).status(FDStatus.ACTIVE).autoRenewal(request.getAutoRenewal())
				.prematureWithdrawalAllowed(request.getPrematureWithdrawalAllowed())
				.nomineeName(request.getNomineeName()).nomineeRelationship(request.getNomineeRelationship()).build();
		FixedDeposit savedFD = fdRepository.save(fd);
		Transaction transaction = Transaction.builder().account(account).transactionType(TransactionType.WITHDRAWAL)
				.amount(request.getPrincipalAmount()).description("Fixed Deposit Creation - FD Number: " + fdNumber)
				.referenceId(fdNumber).balanceAfter(newBalance).transactionDate(LocalDateTime.now()).build();
		transactionRepository.save(transaction);
		emailService.sendFDCreationNotification(customer.getEmail(),
				customer.getFirstName() + " " + customer.getLastName(), request.getPrincipalAmount(),
				account.getAccountNumber(), account.getBalance());
		log.info("FD created successfully with FD Number: {}, debited amount: {} from account: {}", fdNumber,
				request.getPrincipalAmount(), account.getAccountNumber());
		return mapToResponseDTO(savedFD);
	}
	@Override
	@Transactional(readOnly = true)
	public FDResponseDTO getFDById(Long fdId) {
		log.debug("Fetching FD with ID: {}", fdId);
		FixedDeposit fd = fdRepository.findById(fdId)
				.orElseThrow(() -> new FixedDepositNotFoundException("FD not found with ID: " + fdId));
		return mapToResponseDTO(fd);
	}
	@Override
	@Transactional(readOnly = true)
	public FDResponseDTO getFDByFDNumber(String fdNumber) {
		log.debug("Fetching FD with FD Number: {}", fdNumber);
		FixedDeposit fd = fdRepository.findByFdNumber(fdNumber)
				.orElseThrow(() -> new FixedDepositNotFoundException("FD not found with FD Number: " + fdNumber));
		return mapToResponseDTO(fd);
	}
	@Override
	@Transactional(readOnly = true)
	public PagedResponseDTO<FDResponseDTO> getFDsByCustomerId(Long customerId, int page, int size, String sortBy,
			String sortDirection) {
		log.debug("Fetching FDs for customer ID: {} with pagination", customerId);
		customerRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));
		Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<FixedDeposit> fdPage = fdRepository.findByCustomerId(customerId, pageable);
		List<FDResponseDTO> fdDTOs = fdPage.getContent().stream().map(this::mapToResponseDTO)
				.collect(Collectors.toList());
		return PagedResponseDTO.<FDResponseDTO>builder().content(fdDTOs).page(fdPage.getNumber()).size(fdPage.getSize())
				.totalElements(fdPage.getTotalElements()).totalPages(fdPage.getTotalPages()).last(fdPage.isLast())
				.build();
	}
	@Override
	@Transactional(readOnly = true)
	public PagedResponseDTO<FDResponseDTO> getAllFDs(int page, int size, String sortBy, String sortDirection) {
		log.debug("Fetching all FDs with pagination");
		Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<FixedDeposit> fdPage = fdRepository.findAll(pageable);
		List<FDResponseDTO> fdDTOs = fdPage.getContent().stream().map(this::mapToResponseDTO)
				.collect(Collectors.toList());
		return PagedResponseDTO.<FDResponseDTO>builder().content(fdDTOs).page(fdPage.getNumber()).size(fdPage.getSize())
				.totalElements(fdPage.getTotalElements()).totalPages(fdPage.getTotalPages()).last(fdPage.isLast())
				.build();
	}
	@Override
	public FDResponseDTO closeFD(FDClosureRequestDTO request) {
		log.info("Closing FD with ID: {}", request.getFdId());
		FixedDeposit fd = fdRepository.findById(request.getFdId())
				.orElseThrow(() -> new FixedDepositNotFoundException("FD not found with ID: " + request.getFdId()));
		if (fd.getStatus() != FDStatus.ACTIVE) {
			throw new IllegalStateException("FD is not in active state");
		}
		Account account = accountRepository.findById(request.getAccountId()).orElseThrow(
				() -> new AccountNotFoundException("Account not found with ID: " + request.getAccountId()));
		if (!account.getCustomer().getId().equals(fd.getCustomer().getId())) {
			throw new IllegalStateException("Account does not belong to the FD holder");
		}
		Customer customer = account.getCustomer();
		LocalDate now = LocalDate.now();
		LocalDate maturityDate = fd.getMaturityDate();
		boolean isPrematureClosure = now.isBefore(maturityDate);
		BigDecimal penalty = BigDecimal.ZERO;
		FDStatus newStatus = FDStatus.CLOSED;
		BigDecimal creditAmount;
		if (isPrematureClosure) {
			if (!fd.getPrematureWithdrawalAllowed()) {
				throw new IllegalStateException("Premature withdrawal is not allowed for this FD");
			}
			penalty = fd.getPrincipalAmount().multiply(new BigDecimal("0.01"));
			newStatus = FDStatus.PREMATURE_CLOSED;
			creditAmount = fd.getPrincipalAmount().subtract(penalty);
			log.info("Premature closure penalty: {} for FD: {}", penalty, fd.getFdNumber());
		} else {
			BigDecimal interest = fd.getPrincipalAmount()
				.multiply(fd.getInterestRate().divide(new BigDecimal("100")))
				.multiply(new BigDecimal(fd.getTenureMonths()))
				.divide(new BigDecimal("12"), 2, java.math.RoundingMode.HALF_UP);
			creditAmount = fd.getPrincipalAmount().add(interest);
		}
		BigDecimal newBalance = account.getBalance().add(creditAmount);
		account.setBalance(newBalance);
		accountRepository.save(account);
		fd.setStatus(newStatus);
		fd.setPenaltyAmount(penalty);
		fd.setClosedAt(LocalDateTime.now());
		FixedDeposit closedFD = fdRepository.save(fd);
		String description = newStatus == FDStatus.PREMATURE_CLOSED
				? "FD Premature Closure - FD Number: " + fd.getFdNumber()
						+ (penalty.compareTo(BigDecimal.ZERO) > 0 ? " (Penalty: â‚¹" + penalty + ")" : "")
				: "FD Maturity Closure - FD Number: " + fd.getFdNumber();
		Transaction transaction = Transaction.builder().account(account).transactionType(TransactionType.DEPOSIT)
				.amount(creditAmount).description(description).referenceId(fd.getFdNumber()).balanceAfter(newBalance)
				.transactionDate(LocalDateTime.now()).build();
		transactionRepository.save(transaction);
		try {
			emailService.sendFDMaturityNotification(
				customer.getEmail(),
				customer.getFirstName() + " " + customer.getLastName(),
				creditAmount,
				account.getAccountNumber()
			);
		} catch (Exception e) {
			log.error("Failed to send FD closure email: {}", e.getMessage());
		}
		log.info("FD closed successfully: {}, credited amount: {} to account: {}", fd.getFdNumber(), creditAmount,
				account.getAccountNumber());
		return mapToResponseDTO(closedFD);
	}
	@Override
	public FDResponseDTO renewFD(Long fdId) {
		log.info("Renewing FD with ID: {}", fdId);
		FixedDeposit fd = fdRepository.findById(fdId)
				.orElseThrow(() -> new FixedDepositNotFoundException("FD not found with ID: " + fdId));
		if (!fd.isMatured()) {
			throw new IllegalStateException("FD has not matured yet");
		}
		if (fd.getStatus() != FDStatus.ACTIVE && fd.getStatus() != FDStatus.MATURED) {
			throw new IllegalStateException("FD cannot be renewed in current state");
		}
		String newFdNumber = generateFDNumber();
		LocalDate newMaturityDate = LocalDate.now().plusMonths(fd.getTenureMonths());
		BigDecimal newMaturityAmount = calculateMaturityAmount(fd.getMaturityAmount(), fd.getInterestRate(),
				fd.getTenureMonths());
		FixedDeposit renewedFD = FixedDeposit.builder().fdNumber(newFdNumber).customer(fd.getCustomer())
				.principalAmount(fd.getMaturityAmount())
				.interestRate(fd.getInterestRate()).tenureMonths(fd.getTenureMonths()).maturityAmount(newMaturityAmount)
				.openingDate(LocalDate.now()).maturityDate(newMaturityDate).status(FDStatus.ACTIVE)
				.autoRenewal(fd.getAutoRenewal()).prematureWithdrawalAllowed(fd.getPrematureWithdrawalAllowed())
				.nomineeName(fd.getNomineeName()).nomineeRelationship(fd.getNomineeRelationship()).build();
		fd.setStatus(FDStatus.RENEWED);
		fdRepository.save(fd);
		FixedDeposit savedRenewedFD = fdRepository.save(renewedFD);
		log.info("FD renewed successfully. Old FD: {}, New FD: {}", fd.getFdNumber(), newFdNumber);
		return mapToResponseDTO(savedRenewedFD);
	}
	@Override
	@Transactional(readOnly = true)
	public List<FDResponseDTO> getMaturedFDs() {
		log.debug("Fetching all matured FDs");
		List<FixedDeposit> maturedFDs = fdRepository.findMaturedFDs(LocalDate.now());
		return maturedFDs.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
	}
	@Override
	@Transactional(readOnly = true)
	public List<FDResponseDTO> getFDsMaturingInDays(int days) {
		log.debug("Fetching FDs maturing in {} days", days);
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = startDate.plusDays(days);
		List<FixedDeposit> fds = fdRepository.findFDsMaturingBetween(startDate, endDate);
		return fds.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
	}
	@Override
	public BigDecimal calculateMaturityAmount(BigDecimal principalAmount, BigDecimal interestRate, int tenureMonths) {
		BigDecimal rate = interestRate.divide(BigDecimal.valueOf(100), 6, java.math.RoundingMode.HALF_UP);
		BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 6, java.math.RoundingMode.HALF_UP);
		BigDecimal compoundFactor = BigDecimal.ONE.add(monthlyRate);
		BigDecimal result = principalAmount;
		for (int i = 0; i < tenureMonths; i++) {
			result = result.multiply(compoundFactor);
		}
		return result.setScale(2, java.math.RoundingMode.HALF_UP);
	}
	@Override
	@Transactional(readOnly = true)
	public BigDecimal calculateInterestEarned(Long fdId) {
		FixedDeposit fd = fdRepository.findById(fdId)
				.orElseThrow(() -> new FixedDepositNotFoundException("FD not found with ID: " + fdId));
		return fd.calculateInterestEarned();
	}
	@Override
	public void processAutoRenewals() {
		log.info("Processing auto-renewal FDs");
		List<FixedDeposit> autoRenewalFDs = fdRepository.findAutoRenewalFDsDueForRenewal(LocalDate.now());
		for (FixedDeposit fd : autoRenewalFDs) {
			try {
				renewFD(fd.getId());
				log.info("Auto-renewed FD: {}", fd.getFdNumber());
			} catch (Exception e) {
				log.error("Failed to auto-renew FD: {}", fd.getFdNumber(), e);
			}
		}
	}
	@Override
	public void markMaturedFDs() {
		log.info("Marking matured FDs");
		List<FixedDeposit> maturedFDs = fdRepository.findMaturedFDs(LocalDate.now());
		for (FixedDeposit fd : maturedFDs) {
			if (fd.getStatus() == FDStatus.ACTIVE) {
				fd.setStatus(FDStatus.MATURED);
				fdRepository.save(fd);
				log.debug("Marked FD as matured: {}", fd.getFdNumber());
			}
		}
	}
	@Override
	@Transactional(readOnly = true)
	public PagedResponseDTO<FDResponseDTO> getFDsByStatus(String status, int page, int size, String sortBy,
			String sortDirection) {
		log.debug("Fetching FDs with status: {}", status);
		FDStatus fdStatus = FDStatus.valueOf(status.toUpperCase());
		Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<FixedDeposit> fdPage = fdRepository.findByStatus(fdStatus, pageable);
		List<FDResponseDTO> fdDTOs = fdPage.getContent().stream().map(this::mapToResponseDTO)
				.collect(Collectors.toList());
		return PagedResponseDTO.<FDResponseDTO>builder().content(fdDTOs).page(fdPage.getNumber()).size(fdPage.getSize())
				.totalElements(fdPage.getTotalElements()).totalPages(fdPage.getTotalPages()).last(fdPage.isLast())
				.build();
	}
	@Override
	@Transactional(readOnly = true)
	public BigDecimal getTotalActiveFDAmount() {
		BigDecimal total = fdRepository.getTotalActiveFDAmount();
		return total != null ? total : BigDecimal.ZERO;
	}
	@Override
	@Transactional(readOnly = true)
	public Long getActiveFDCount() {
		return fdRepository.getActiveFDCount();
	}
	private String generateFDNumber() {
		String prefix = "FD";
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		String suffix = String.valueOf((int) (Math.random() * 1000));
		return prefix + timestamp + suffix;
	}
	private FDResponseDTO mapToResponseDTO(FixedDeposit fd) {
		long daysToMaturity = fd.isMatured() ? 0 : ChronoUnit.DAYS.between(LocalDate.now(), fd.getMaturityDate());
		return FDResponseDTO.builder().id(fd.getId()).fdNumber(fd.getFdNumber()).customerId(fd.getCustomer().getId())
				.customerName(fd.getCustomer().getFirstName() + " " + fd.getCustomer().getLastName())
				.principalAmount(fd.getPrincipalAmount()).interestRate(fd.getInterestRate())
				.tenureMonths(fd.getTenureMonths()).maturityAmount(fd.getMaturityAmount())
				.openingDate(fd.getOpeningDate()).maturityDate(fd.getMaturityDate()).status(fd.getStatus())
				.autoRenewal(fd.getAutoRenewal()).prematureWithdrawalAllowed(fd.getPrematureWithdrawalAllowed())
				.nomineeName(fd.getNomineeName()).nomineeRelationship(fd.getNomineeRelationship())
				.interestEarned(fd.calculateInterestEarned()).penaltyAmount(fd.getPenaltyAmount())
				.createdAt(fd.getCreatedAt()).updatedAt(fd.getUpdatedAt()).isMatured(fd.isMatured())
				.daysToMaturity(daysToMaturity).build();
	}
}
