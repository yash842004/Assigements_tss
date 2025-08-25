package com.tss.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import com.tss.dao.AccountDAO;
import com.tss.dao.TransactionDAO;
import com.tss.model.Account;
import com.tss.util.DBConnection;

public class AccountService {
	private final AccountDAO accountDAO;
	private final TransactionDAO transactionDAO;

	public AccountService() {
		this.accountDAO = new AccountDAO();
		this.transactionDAO = new TransactionDAO();
	}

	public boolean deposit(int accountId, BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			System.out.println("AccountService: Cannot deposit zero or negative amount");
			return false; 
		}
		
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			
			Account account = accountDAO.getAccountById(accountId, conn);
			if (account == null) {
				System.out.println("AccountService: Account not found for ID: " + accountId);
				conn.rollback();
				return false;
			}
			
			System.out.println("AccountService: Processing deposit of $" + amount + " to account " + accountId);
			System.out.println("AccountService: Current balance: $" + account.getBalance());
			
			BigDecimal newBalance = account.getBalance().add(amount);
			account.setBalance(newBalance);
			accountDAO.updateBalance(account, conn);

			String description = "Cash Deposit";
			transactionDAO.addTransaction(accountId, "DEPOSIT", amount, description, conn);
			
			conn.commit(); 
			System.out.println("AccountService: Deposit successful. New balance: $" + newBalance);
			return true;
			
		} catch (SQLException e) {
			System.err.println("AccountService: Error during deposit: " + e.getMessage());
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			return false;
		} catch (Exception e) {
			System.err.println("AccountService: Unexpected error during deposit: " + e.getMessage());
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback(); 
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			return false;
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean withdraw(int accountId, BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			System.out.println("AccountService: Cannot withdraw zero or negative amount");
			return false;
		}
		
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false); 
			
			Account account = accountDAO.getAccountById(accountId, conn);
			if (account == null) {
				System.out.println("AccountService: Account not found for ID: " + accountId);
				conn.rollback();
				return false;
			}
			
			if (account.getBalance().compareTo(amount) < 0) {
				System.out.println("AccountService: Insufficient funds. Balance: $" + account.getBalance() + ", Requested: $" + amount);
				conn.rollback();
				return false; 
			}
			
			System.out.println("AccountService: Processing withdrawal of $" + amount + " from account " + accountId);
			System.out.println("AccountService: Current balance: $" + account.getBalance());
			
			BigDecimal newBalance = account.getBalance().subtract(amount);
			account.setBalance(newBalance);
			accountDAO.updateBalance(account, conn);

			
			String description = "Cash Withdrawal";
			transactionDAO.addTransaction(accountId, "WITHDRAWAL", amount, description, conn);
			
			conn.commit(); 
			System.out.println("AccountService: Withdrawal successful. New balance: $" + newBalance);
			return true;
			
		} catch (SQLException e) {
			System.err.println("AccountService: Error during withdrawal: " + e.getMessage());
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			return false;
		} catch (Exception e) {
			System.err.println("AccountService: Unexpected error during withdrawal: " + e.getMessage());
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback(); 
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			return false;
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true); 
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String transferMoney(int fromAccountId, String toAccountNumber, BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			return "Transfer amount must be positive.";
		}

		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false); 

			Account fromAccount = accountDAO.getAccountById(fromAccountId, conn);
			Account toAccount = accountDAO.getAccountByNumber(toAccountNumber); 

			if (fromAccount == null) {
				conn.rollback();
				return "Sender account not found.";
			}
			if (toAccount == null) {
				conn.rollback();
				return "Recipient account not found.";
			}
			if (fromAccount.getAccountId() == toAccount.getAccountId()) {
				conn.rollback();
				return "Cannot transfer money to the same account.";
			}

			if (fromAccount.getBalance().compareTo(amount) < 0) {
				conn.rollback();
				return "Insufficient funds for the transfer.";
			}

			System.out.println("AccountService: Processing transfer of $" + amount + " from account " + fromAccountId + " to " + toAccountNumber);
			System.out.println("AccountService: From account balance: $" + fromAccount.getBalance());
			System.out.println("AccountService: To account balance: $" + toAccount.getBalance());

			fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
			accountDAO.updateBalance(fromAccount, conn);

			toAccount.setBalance(toAccount.getBalance().add(amount));
			accountDAO.updateBalance(toAccount, conn);

			String fromDesc = "Transfer to " + toAccount.getAccountNumber();
			transactionDAO.addTransaction(fromAccountId, "TRANSFER_OUT", amount, fromDesc, conn);

			String toDesc = "Transfer from " + fromAccount.getAccountNumber();
			transactionDAO.addTransaction(toAccount.getAccountId(), "TRANSFER_IN", amount, toDesc, conn);

			conn.commit(); 
			System.out.println("AccountService: Transfer successful");
			System.out.println("AccountService: From account new balance: $" + fromAccount.getBalance());
			System.out.println("AccountService: To account new balance: $" + toAccount.getBalance());
			return "Transfer successful!";

		} catch (SQLException e) {
			System.err.println("AccountService: Error during transfer: " + e.getMessage());
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback(); 
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			return "An error occurred during the transfer. The transaction has been rolled back.";
		} catch (Exception e) {
			System.err.println("AccountService: Unexpected error during transfer: " + e.getMessage());
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback(); 
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			return "An unexpected error occurred during the transfer. The transaction has been rolled back.";
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true); 
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}