package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

//		private final static String URL = PropertiesParser.dbDetails.get("url");
//		private final static String USERNAME = PropertiesParser.dbDetails.get("username");;
//		private final static String PASSWORD = PropertiesParser.dbDetails.get("password");;
	private final static String URL = "jdbc:postgresql://expense-reimbursement.c9sv39gxxxk6.us-east-2.rds.amazonaws.com/postgres";
	private final static String USERNAME = "postgres";
	private final static String PASSWORD ="P4ssw0rd123!";

	public static Connection getConnection() throws SQLException {

		return DriverManager.getConnection(URL, USERNAME, PASSWORD);

	}
}
