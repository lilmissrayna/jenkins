package com.revature.service;

import java.util.List;
import com.revature.models.ReimbursementStatus;
import com.revature.models.ReimbursementType;
import com.revature.models.Request;
import com.revature.models.User;
import com.revature.repo.RequestDAO;
import com.revature.repo.UserDAO;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.security.SecureRandom;
import java.util.Base64;

public class EmployeeServiceImpl implements EmployeeService {

	private RequestDAO rDao;
	private UserDAO uDao;

	public EmployeeServiceImpl(UserDAO uDao, RequestDAO rDao) {
		this.uDao = uDao;
		this.rDao = rDao;
	}

	@Override
	public User login(String username) {
		return uDao.selectUser(username);
	}

	@Override
	public boolean authenticate(String username, String password) {
		boolean authenticated = false;
		
		User current = login(username);
		
		if(current != null) {
			String decryptedPass = decryptPassword(current.getPassword());
			if(password.equals(decryptedPass)) {
				authenticated = true;
			}
			
		}
		
		
		return authenticated;
	}

	@Override
	public boolean checkIfUsernameExists(String username) {

		boolean usernameExists = false;

		List<User> allUsers = uDao.selectAllUsers();

		for (User user : allUsers) {
			if (user.getUsername().equals(username)) {
				usernameExists = true;
				break;
			}
		}

		return usernameExists;
	}

	@Override
	public boolean requestReimbursment(String username, ReimbursementType type, double amount, String description) {
		return rDao.insertRequest(username, type, amount, description);
	}

	@Override
	public List<Request> viewPendingRequests(String username) {
		return rDao.selectRequestByUsernameAndStatus(username, ReimbursementStatus.PENDING);
	}

	@Override
	public List<Request> viewPastRequests(String username) {
		return rDao.selectPastRequestByUsername(username);
	}

	@Override
	public boolean createNewAccount(String username, String email) {
		String password = generateRandomPassword();
		String encryptedPass = encryptPassword(password);

		if (!checkIfUsernameExists(username)) {

			sendEmail(email, username, password);
		}

		return uDao.insertUser(username, email, encryptedPass);
	}

	@Override
	public boolean changePassword(String username, String newPassword) {
		String encryptedPassword = encryptPassword(newPassword);
		return uDao.updatePassword(username, encryptedPassword);
	}

	private String encryptPassword(String password) {
		return Base64.getEncoder().encode(password.getBytes()).toString();
	}
	
	private String decryptPassword(String password) {
		return Base64.getDecoder().decode(password.getBytes()).toString();
	}

	private static String generateRandomPassword() {
		final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 15; i++) {
			int randomIndex = random.nextInt(chars.length());
			sb.append(chars.charAt(randomIndex));
		}

		return sb.toString();
	}

	private void sendEmail(String email, String username, String password) {

		String to = email;
		String from = "expensereimbursementapp@gmail.com";
		String host = "smtp.gmail.com";
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, "P4ssw0rd!");
			}
		});

		session.setDebug(true);

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("Welcome to Expense-Reimbursement");
			message.setContent("<h1>Thank's for Registering " + username + "!</h1>" + "<h3>Your temporary password is "
					+ password + "</h3><h3>Don't forget to update it next time you login!</h3>", "text/html");
			Transport.send(message);
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

	}
}
