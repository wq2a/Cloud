package cloud.server;

import java.sql.*;
import java.io.*;
import cloud.server.User;
import cloud.server.FileManager;


public class DBManager{

	private static DBManager instance;

	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/cloud?useUnicode=true&characterEncoding=utf-8&useSSL=false";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	private Connection conn = null;
	private Statement stmt = null;

	private DBManager(){}
	private Connection getConnection(){
		if (conn == null){
			try{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL,USER,PASSWORD);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return conn;
	}

	public static DBManager getInstance(){
		if (instance == null)
			instance = new DBManager();
		return instance;
	}

	public User searchUser(User user){
		try{
			stmt = getConnection().createStatement();
			String sql = "SELECT * FROM user WHERE username='"+user.getUsername()+"'";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				user.setH2(rs.getString("h2"));
				user.setSalt(rs.getString("salt"));
			}
			return user;
		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		//Handle errors for Class.forName
      		e.printStackTrace();
   		}finally{
     		return user;
   		}
	}

	public boolean insertUser(User user){
		try{
			stmt = getConnection().createStatement();
			String sql = "SELECT * FROM user WHERE username='"+user.getUsername()+"'";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				// user already exist
				return false;
			}
			sql = "INSERT INTO user" +
			  	  "(username,h2,salt,firstname,lastname,email)" +
			  	  "VALUES('"+user.getUsername() +
			  	  "','" + user.getH2() +
			      "','" + user.getSalt() +
			      "','" + user.getFirstname() +
			      "','" + user.getLastname() +
			      "','" + user.getEmail() +
			      "')";
			if(stmt.executeUpdate(sql)==0)
				return false;
			
			FileManager fm = new FileManager(user);
			fm.mkdir("");

		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		//Handle errors for Class.forName
      		e.printStackTrace();
   		}finally{
      		//finally block used to close resources
      		/*
      		try{
         		if(stmt!=null)
            		conn.close();
      		}catch(SQLException se){
      		}
      		try{
         		if(conn!=null)
            		conn.close();
      		}catch(SQLException se){
         		se.printStackTrace();
     		}
     		conn = null;*/
     		return true;
   		}
   		
	}

	public void cleanCreate(){
		try{
			conn = DriverManager.getConnection("jdbc:mysql://localhost/?useSSL=false",USER,PASSWORD);
			stmt = conn.createStatement();
			String sql = "DROP DATABASE IF EXISTS cloud";
			stmt.executeUpdate(sql);
			conn.close();
			conn = null;
			// delete data 
			FileManager fm = new FileManager();
			fm.delROOT();
		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		//Handle errors for Class.forName
      		e.printStackTrace();
   		}
		create();
	}

	public void create(){
		try{
			conn = DriverManager.getConnection("jdbc:mysql://localhost/?useSSL=false",USER,PASSWORD);
			stmt = conn.createStatement();
			String sql = "CREATE DATABASE IF NOT EXISTS cloud";
			stmt.executeUpdate(sql);
			conn.close();
			conn = null;
			stmt = getConnection().createStatement();
			sql = "CREATE TABLE IF NOT EXISTS user" +
                  "(id INTEGER not NULL AUTO_INCREMENT, " +
                  " username VARCHAR(255), " +
                  " h2 VARCHAR(255), " +
                  " salt VARCHAR(255), " +
                  " firstname VARCHAR(255), " +
                  " lastname VARCHAR(255), " +
                  " email VARCHAR(255), " +
                  " PRIMARY KEY ( id ))";
			stmt.executeUpdate(sql);

			FileManager fm = new FileManager();
			fm.mkdirROOT();

			User user = new User();
			user.setUsername("admin");
			Auth auth = new Auth();
			auth.insertAdmin(user,"password");
			fm = new FileManager(user);
			fm.mkdir("");

		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		//Handle errors for Class.forName
      		e.printStackTrace();
   		}
	}
}