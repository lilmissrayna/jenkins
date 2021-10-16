package com.revature.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.revature.models.User;
import com.revature.util.ConnectionFactory;

public class UserDAOImpl implements UserDAO {

	@Override
	public List<User> selectAllUsers() {
		String sql = "SELECT * FROM users;";
		List<User> allUsers = new ArrayList<User>();
		try (Connection conn = ConnectionFactory.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				allUsers.add(new User(rs.getString("user_username"), rs.getString("user_password"),
						rs.getString("user_email"), rs.getBoolean("is_finance_manager")));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allUsers;
	}

	@Override
	public User selectUser(String username) {
		String sql = "SELECT * FROM users WHERE user_username = ?;";
		User selectedUser = new User();
		try (Connection conn = ConnectionFactory.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				selectedUser = new User(rs.getString("user_username"), rs.getString("user_password"),
						rs.getString("user_email"), rs.getBoolean("is_finance_manager"));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return selectedUser;
	}

	@Override
	public boolean updatePassword(String username, String newPassword) {
		boolean success = false;
		String sql = "UPDATE users SET user_password = ? WHERE user_username = ?;";
		try (Connection conn = ConnectionFactory.getConnection()) {
			PreparedStatement updateStatus = conn.prepareStatement(sql);
			updateStatus.setString(1, newPassword);
			updateStatus.setString(2, username);
			updateStatus.execute();

			success = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	@Override
	public boolean insertUser(String username, String email, String password) {
		boolean success = false;
		String sql = "INSERT INTO users(user_username,user_email,user_password,is_finance_manager) "
				+ "VALUES( ?, ?, ?, ?);";
		try (Connection conn = ConnectionFactory.getConnection()) {
			PreparedStatement insertStatement = conn.prepareStatement(sql);
			insertStatement.setString(1, username);
			insertStatement.setString(2, email);
			insertStatement.setString(3, password);
			insertStatement.setBoolean(4, false);
			insertStatement.execute();

			sql = "SELECT * FROM users WHERE user_username = ?;";
			PreparedStatement confirmInsert = conn.prepareStatement(sql);
			confirmInsert.setString(1, username);
			success = confirmInsert.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	@Override
	public boolean deleteUser(String username) {

		boolean success = false;
		String sql = "DELETE FROM users WHERE user_username = ?;";
		try (Connection conn = ConnectionFactory.getConnection()) {
			PreparedStatement deleteStatement = conn.prepareStatement(sql);
			deleteStatement.setString(1, username);
			deleteStatement.execute();

			sql = "SELECT * FROM users WHERE user_username = ?;";
			PreparedStatement confirmDelete = conn.prepareStatement(sql);
			confirmDelete.setString(1, username);
			success = !(confirmDelete.execute());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

}