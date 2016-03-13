package cloud.server;

import java.sql.*;
import java.io.*;

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
				//Class.forName("myDriver.ClassName");
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
	public void create(){
		try{
			stmt = getConnection().createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS user" +
                   		"(id INTEGER not NULL, " +
                   		" username VARCHAR(255), " + 
                   		" h2 VARCHAR(255), " + 
                   		" salt VARCHAR(255), " + 
                   		" PRIMARY KEY ( id ))";
			stmt.executeUpdate(sql);
		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		//Handle errors for Class.forName
      		e.printStackTrace();
   		}finally{
      		//finally block used to close resources
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
   		}
	}
}