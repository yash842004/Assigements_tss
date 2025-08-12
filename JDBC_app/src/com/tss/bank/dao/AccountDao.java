package com.tss.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.tss.bank.Connection.DBConnection;

public class AccountDao {

	public double getBalance(int accountId) throws Exception {
		Connection connection = DBConnection.getConnection();
		PreparedStatement preparedStatement = connection
				.prepareStatement("SELECT account_balance FROM account WHERE account_id = ?");
		preparedStatement.setInt(1, accountId);
		ResultSet result = preparedStatement.executeQuery();
		if (result.next())
			return result.getDouble(1);
		throw new Exception("Account not found");
	}

	public void debit(int accountId, double amount, Connection connection) throws SQLException {
		PreparedStatement preparedStatement = connection
				.prepareStatement("UPDATE account SET account_balance = account_balance - ? WHERE account_id = ?");
		preparedStatement.setDouble(1, amount);
		preparedStatement.setInt(2, accountId);
		preparedStatement.executeUpdate();
	}

	public void credit(int accountId, double amount, Connection connection) throws SQLException {
		PreparedStatement preparedStatement = connection
				.prepareStatement("UPDATE account SET account_balance = account_balance + ? WHERE account_id = ?");
		preparedStatement.setDouble(1, amount);
		preparedStatement.setInt(2, accountId);
		preparedStatement.executeUpdate();
	}
}
