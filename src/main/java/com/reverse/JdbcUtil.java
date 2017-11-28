package com.reverse;

import java.util.List;
import java.util.Properties;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcUtil {
	
	public static Connection getConnection(){
		
		InputStream in = null; 
    	URL url = JdbcUtil.class.getClassLoader().getResource("dbconfig.properties");
        try {
			in = url.openStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        
    	Properties config = new Properties();
    	try {
			config.load(new InputStreamReader(in, "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	
    	String URL = config.getProperty("url");
    	String username = config.getProperty("username");
    	String driverClassName = config.getProperty("driverClassName");
    	String password = config.getProperty("password");
    	
		Connection conn = null;
	
	    try {
	        Class.forName(driverClassName); 
	        conn = (Connection) DriverManager.getConnection(URL, username, password);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return conn;
	   
	}
	
	public static <T> void initParameter(PreparedStatement pstmt, List<T> params) throws SQLException{
		
		for(int i = 0; i < params.size(); i++) {
			pstmt.setObject(i+1, params.get(i));	
		}
		
	}
	
	public static void getMetaData(Connection conn){
		
	}
	
}
