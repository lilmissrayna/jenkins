package com.revature.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesParser {
	
	
	static Map<String,String> dbDetails = new HashMap<>();
	
	public static void getProperties(){
		System.out.println("Parsing properties");
		try {
			FileReader reader = new FileReader("db.properties");
			Properties dbProperites = new Properties();
			
			dbProperites.load(reader);
			
			dbDetails.put("username", dbProperites.getProperty("username"));
			dbDetails.put("password", dbProperites.getProperty("password"));
			dbDetails.put("url", dbProperites.getProperty("url"));
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	

}
