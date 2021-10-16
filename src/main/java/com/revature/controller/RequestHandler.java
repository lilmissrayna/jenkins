package com.revature.controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class RequestHandler {

	public static boolean checkSession(Context ctx) {
		if(ctx.sessionAttribute("isLoggedIn") != null && (Boolean) ctx.sessionAttribute("isLoggedIn")) {
			return true;
		}
		
		return false;
	}
	
	public static void setUpEndpoints(Javalin app) {
		EmployeeController employeeController = new EmployeeController();
		FinanceManagerController managerController = new FinanceManagerController();
		final String BASE_URL = "localhost:9000/";
		
		employeeController.initializeFakeData();
		managerController.initializeFakeData();
		
		app.get("/", ctx -> ctx.req.getRequestDispatcher("index.html").forward(ctx.req, ctx.res));
		app.get("/login", ctx -> ctx.req.getRequestDispatcher("index.html").forward(ctx.req, ctx.res));
		
		app.get("/home", ctx -> ctx.req.getRequestDispatcher("dashboard.html").forward(ctx.req, ctx.res));
		
		app.post("/authenticate", ctx -> ctx.redirect(employeeController.authenticate(ctx)));
		
		app.get("/logout", ctx -> {
			ctx.consumeSessionAttribute("isLoggedIn");
			ctx.removeCookie("username");
			ctx.removeCookie("isFinanceManager");
			ctx.redirect(BASE_URL);
		});
		
//		app.get("/user", ctx -> {
//			if(checkSession(ctx)) {
//				ctx.json(ctx.sessionAttribute("user"));
//			} else {
//				ctx.redirect(BASE_URL);
//			}
//		});
		
		app.post("/user", ctx -> {
			employeeController.registerAccount(ctx);
		});
		
		app.put("/user", ctx -> {
			employeeController.changePassword(ctx);
		});
		
		app.post("/request", ctx -> {
			if(checkSession(ctx)) {
				if(employeeController.createNewRequest(ctx)) {
					ctx.res.sendRedirect("localhost:9000/home");
				}
			} else {
				ctx.redirect(BASE_URL);
			}
		});
		
		app.get("/pending", ctx -> {
			if(checkSession(ctx)) {
				ctx.json(employeeController.getUserPendingRequests(ctx));
			} else {
				ctx.redirect(BASE_URL);
			}
		});
		
		app.get("/past", ctx -> {
			if(checkSession(ctx)) {
				ctx.json(employeeController.getAllPastRequests(ctx));
			} else {
				ctx.redirect(BASE_URL);
			}
		});
		
		app.get("/all", ctx -> {
			if(checkSession(ctx)) {
				ctx.json(managerController.getAllReimbursements(ctx));
			} else {
				ctx.redirect(BASE_URL);
			}
		});
		
		app.put("/request", ctx -> {
			if(checkSession(ctx)) {
				if(managerController.updateRequestStatus(ctx)) {
					ctx.res.sendRedirect("localhost:9000/home");
				}
			} else {
				ctx.redirect(BASE_URL);
			}
		});
		
		
	}
}
