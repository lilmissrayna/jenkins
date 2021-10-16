package com.revature.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.ReimbursementStatus;
import com.revature.models.ReimbursementType;
import com.revature.models.Request;
import com.revature.models.User;
import com.revature.repo.RequestDAO;
import com.revature.repo.RequestDAOImpl;
import com.revature.repo.UserDAO;
import com.revature.repo.UserDAOImpl;
import com.revature.service.EmployeeService;
import com.revature.service.EmployeeServiceImpl;

import io.javalin.http.Context;

public class EmployeeController {
	
	private final UserDAO uDao = new UserDAOImpl();
	private final RequestDAO rDao = new RequestDAOImpl();
	private List<Request> requestList = new ArrayList<>();
	private EmployeeService eService = new EmployeeServiceImpl(uDao, rDao);

	public void initializeFakeData() {
		requestList.add(new Request(1, "Someone", ReimbursementType.OTHER, 400, "test", Timestamp.valueOf(LocalDateTime.now()), ReimbursementStatus.PENDING));
		requestList.add(new Request(2, "Me", ReimbursementType.FOOD, 600, null, Timestamp.valueOf(LocalDateTime.now()), ReimbursementStatus.APPROVED));
		requestList.add(new Request(3, "Gulliver", ReimbursementType.TRAVEL, 900, null, Timestamp.valueOf(LocalDateTime.now()), ReimbursementStatus.PENDING));
		requestList.add(new Request(4, "Nessie", ReimbursementType.OTHER, 3.50, "I need about tree fiddy", Timestamp.valueOf(LocalDateTime.now()), ReimbursementStatus.REJECTED));
		requestList.add(new Request(5, "Someone Else", ReimbursementType.LODGING, 400, null, Timestamp.valueOf(LocalDateTime.now()), ReimbursementStatus.APPROVED));
	}
	
	public String authenticate(Context ctx) throws IOException {
		String username = ctx.formParam("username");
		String password = ctx.formParam("password");
		
		if(eService.checkIfUsernameExists(username)) {
			User user = eService.login(username);
//			if(username.equals("User") && password.equals("password")) {
			if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
				ctx.res.setStatus(200);
				Boolean isFinanceManager = user.isFinanceManager();
//				ctx.sessionAttribute("isLoggedIn", true);
				ctx.cookie("username", username);
				ctx.cookie("isFinanceManager", isFinanceManager.toString());
//				ctx.cookie("isFinanceManager", "true");

				ctx.res.sendRedirect("http://localhost:9000/home");
			}
		}
			return "/login";
	}
	
	public boolean registerAccount(Context ctx) {
		
		String username = ctx.formParam("username");
		String email = ctx.formParam("email");
		
		if(!eService.checkIfUsernameExists(username)) {
			if(eService.createNewAccount(username, email)) {
				ctx.res.setStatus(200);
				return true;
			} else {
				ctx.res.setStatus(500);
				return false;
			}
		} else {
			ctx.res.setStatus(500);
			return false;
		}
	}
	
	public boolean changePassword(Context ctx) {
		//TODO:implement
		return false;
	}
	
	public boolean createNewRequest(Context ctx) {
		String username = ctx.cookieMap().get("username");
		ReimbursementType type = ReimbursementType.valueOf(ctx.formParam("type"));
		double amount = Double.parseDouble(ctx.formParam("amount"));
		String description = ctx.formParam("description");
		
		if(eService.requestReimbursment(username, type, amount, description)) {
			ctx.res.setStatus(200);
//			System.out.println(username);
//			System.out.println(type.toString());
//			System.out.println(amount);
//			System.out.println(description);
			
			return true;
		}
		
		ctx.res.setStatus(500);
		return false;
	}

	public List<Request> getUserPendingRequests(Context ctx) {
		String username = ctx.cookieMap().get("username");
		
		requestList = eService.viewPendingRequests(username);
		
		return requestList;
	}
	
	public List<Request> getAllPastRequests(Context ctx) {
		String username = ctx.cookieMap().get("username");
		
		requestList = eService.viewPastRequests(username);
		
		return requestList;
	}

}
