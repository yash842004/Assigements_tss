package com.tss.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tss.dao.AccountDAO;
import com.tss.dao.TransactionDAO;
import com.tss.model.Account;

@WebServlet("/firstDeposit")
public class firstDeposit extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    public firstDeposit() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accountNumber = request.getParameter("accountNumber");
        String amountStr = request.getParameter("amount");

        if (accountNumber == null || accountNumber.trim().isEmpty() || 
            amountStr == null || amountStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Account number and amount are required.");
            request.getRequestDispatcher("/deposit.jsp?accountNumber=" + accountNumber).forward(request, response);
            return;
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountStr).setScale(2, RoundingMode.HALF_UP);
            if (amount.compareTo(BigDecimal.ZERO) <= 0)
                throw new NumberFormatException("Amount must be positive");
        } catch (NumberFormatException ex) {
            request.setAttribute("errorMessage", "Enter a valid amount greater than 0.");
            request.getRequestDispatcher("/deposit.jsp?accountNumber=" + accountNumber).forward(request, response);
            return;
        }

        try {
            // get account
            Account account = accountDAO.getAccountByNumber(accountNumber);
            if (account == null) {
                request.setAttribute("errorMessage", "Account not found: " + accountNumber);
                request.getRequestDispatcher("/deposit.jsp?accountNumber=" + accountNumber).forward(request, response);
                return;
            }

            // calculate new balance
            BigDecimal newBalance = account.getBalance().add(amount);

            // update balance
            accountDAO.updateBalance(account.getAccountId(), newBalance);

            // save transaction
            transactionDAO.saveTransaction(account.getAccountId(), "DEPOSIT", amount, "Initial deposit");

            // redirect to dashboard / success page
            response.sendRedirect(request.getContextPath() + "/index.jsp?message=Deposit+successful");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred during deposit. Try again.");
            request.getRequestDispatcher("/deposit.jsp?accountNumber=" + accountNumber).forward(request, response);
        }
    }
}
