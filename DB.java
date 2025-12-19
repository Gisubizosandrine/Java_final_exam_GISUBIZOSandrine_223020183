package ecommerceform;


import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DB {

	 private static Connection connection = null;
	 
	 public static Connection getConnection() {
	     try {
	         if (connection == null || connection.isClosed()) {
	             
	             Properties props = new Properties();
	             try {
	                 props.load(new FileInputStream("config.properties"));
	             } catch (IOException e) {
	                                 System.out.println("Config file not found, using default values");
	             }
	             
	             String url = props.getProperty("db.url", "jdbc:mysql://localhost:3306/ecommerce_platform");
	             String username = props.getProperty("db.username", "root");
	             String password = props.getProperty("db.password", "");
	             
	             
	             Class.forName("com.mysql.jdbc.Driver");
	             
	             connection = DriverManager.getConnection(url, username, password);
	             System.out.println("Database connected successfully!");
	         }
	     } catch (ClassNotFoundException e) {
	         System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
	     } catch (SQLException e) {
	         System.out.println("Database connection failed: " + e.getMessage());
	     }
	     return connection;
	 }
	 
	 public static void closeConnection() {
	     try {
	         if (connection != null && !connection.isClosed()) {
	             connection.close();
	             System.out.println("Database connection closed.");
	         }
	     } catch (SQLException e) {
	         System.out.println("Error closing connection: " + e.getMessage());
	     }
	 }
	 
	 public static boolean testConnection() {
	     try {
	         Connection conn = getConnection();
	         return conn != null && !conn.isClosed();
	     } catch (SQLException e) {
	         return false;
	     }
	 }
	}