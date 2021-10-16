package com.revature;

import com.revature.controller.RequestHandler;

import io.javalin.Javalin;

public class MainDriver {

	public static void main(String[] args) {
		Javalin app = Javalin.create(config -> { 
			config.addStaticFiles(staticFiles ->{staticFiles.directory = "/public";}); 
			config.enableCorsForAllOrigins();
			}).start(9000);
		
		
		RequestHandler.setUpEndpoints(app);
	}

}
