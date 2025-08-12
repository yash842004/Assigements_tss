package com.tss.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tss.DBConnection.DBConnection;
import com.tss.model.User;

@WebServlet("/ShowUsers")
public class ShowUsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ShowUsersServlet() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<User> userList = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection()) {
			String sql = "SELECT username, password FROM user";

			try (PreparedStatement statement = connection.prepareStatement(sql);
					ResultSet result = statement.executeQuery()) {
				while (result.next()) {
					System.out.println();
					String username = result.getString("username");
					String password = result.getString("password");
					userList.add(new User(username, password));
				}

			}

		} catch (Exception e) {
			System.out.println(e);

		}
		request.setAttribute("userList", userList);
     	request.getRequestDispatcher("user.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// doGet(request, response);

	}

}
