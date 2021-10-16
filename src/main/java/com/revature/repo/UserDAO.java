package com.revature.repo;

import java.util.List;

import com.revature.models.User;

public interface UserDAO {
	User selectUser(String username);

	boolean updatePassword(String username, String newPassword);

	boolean deleteUser(String username);

	List<User> selectAllUsers();

	boolean insertUser(String username, String email, String password);
}