package cloud.server;

import java.util.*;
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

			insertPath("data/"+user.getUsername()+"/");

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
      		e.printStackTrace();
   		}
		create();
	}

	public String getPath(String path){
		//String result = "";
		String prefix = "";
		StringBuffer result = new StringBuffer();
		try{
			int parent = -1;

			stmt = getConnection().createStatement();			
			String sql = "SELECT * FROM path WHERE path='"+path+"'";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				parent = rs.getInt("id");
				result.append(path.substring(5));
				prefix = ";";
			}

			sql = "SELECT * FROM path p "+
				"JOIN path_closure c "+
				"ON p.id=c.descendant "+
				"WHERE c.ancestor="+parent;
			rs = stmt.executeQuery(sql);

			PathManager pm = new PathManager();

			while(rs!=null && rs.next()){
				// format tree
				// System.out.println(rs.getInt("id")+","+rs.getString("name")+","+
					// rs.getString("path")+","+rs.getInt("ancestor")+","+rs.getInt("descendant")+","+rs.getInt("depth"));
				
				// pm.add(new Path(rs.getInt("id"),rs.getString("name"),rs.getInt("parent"),rs.getInt("depth"),rs.getInt("type")));
				result.append(prefix);
				if(rs.getString("path")!=null){
					result.append(rs.getString("path").substring(5));
				}
				//result.append(rs.getString("path").substring(5));//.append(";");
			}
			//result = pm.generateXML();

		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		e.printStackTrace();
   		}finally{
      		return result.toString();
   		}
	}

	public boolean delPath(String path){
		try{
			int id = -1;
			int parent = -1;
			System.out.println("="+path);
			stmt = getConnection().createStatement();			
			String sql = "SELECT * FROM path WHERE path='"+path+"'";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				parent = rs.getInt("parent");
				id = rs.getInt("id");

				sql = "DELETE from path" +
					" WHERE path like '"+ path +"%'";

				stmt.executeUpdate(sql);
				
				sql = "DELETE link from path_closure p, path_closure link, path_closure c" +
					" WHERE p.ancestor = link.ancestor and c.descendant = link.descendant" +
					" and (c.descendant=" + id +
					" or c.ancestor=" + id +
					")";
				if(stmt.executeUpdate(sql) == 0){
					return true;
				}
			}
		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		e.printStackTrace();
   		}finally{
      		return true;
   		}
	}

	public int insertPath(String path){
		int id = -1;
		int parent = -1;
		String name = "";
		int type = 0;
		String sql;
		ResultSet rs;
		try{
			stmt = getConnection().createStatement();
			sql = "SELECT * FROM path WHERE path='"+path+"'";
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				// aready exist;
				// System.out.println("Aready exit!"+rs.getInt("id"));
				
				return rs.getInt("id");
			}

			if(path.equals("data/")){
				parent = 0;
				name = "data";
			}else{
				if(!path.endsWith("/")){
					type = 1;
				}
				String r[] = path.split("\\/");
				int length = r.length;
				if(length != 0){
					name = r[length-1];
					int index = path.lastIndexOf(name);
					sql = "SELECT * FROM path WHERE path='"+path.substring(0,index)+"'";
					rs = stmt.executeQuery(sql);
					if(rs.next()){
						parent = rs.getInt("id");
					}else{
						// there's no parent dir
						System.out.println("No parent dir!");
						return id;
					}
				}
			}
			sql = "INSERT INTO path" +
			  	  "(name,parent,path,type)" +
			  	  "VALUES('"+name +
			  	  "'," + parent +
			      ",'" + path +
			      "'," + type +
			      ")";

			PreparedStatement stmt = getConnection().prepareStatement(
                           sql, Statement.RETURN_GENERATED_KEYS);

			if(stmt.executeUpdate()==0)
				return id;

			ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }

            Statement statement = getConnection().createStatement();
            
            sql = "INSERT INTO path_closure" +
			  	  "(ancestor,descendant,depth)" +
			  	  "VALUES('"+parent +
			  	  "'," + id +
			      ",'" + 1 +
			      "')";
			if(statement.executeUpdate(sql)==0)
				return id;
            
            sql = "INSERT INTO path_closure" +
			  	  "(ancestor,descendant,depth)" +
			  	  "SELECT p.ancestor, c.descendant,p.depth+c.depth" +
			  	  " from path_closure p, path_closure c" +
			  	  " where p.descendant=" + parent +
			  	  " and c.descendant=" + id;
            
			if(statement.executeUpdate(sql)==0)
				return id;

		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		e.printStackTrace();
   		}finally{
     		
   		}
   		return id;
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
			// User table
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

			// Path Closure Tables
			sql = "CREATE TABLE IF NOT EXISTS path" +
                  "(id INTEGER not NULL AUTO_INCREMENT, " +
                  " name VARCHAR(255) NOT NULL, " +
                  " parent INTEGER NOT NULL, " +
                  " path VARCHAR(255) NOT NULL, " +
                  " type INTEGER NOT NULL DEFAULT 0, " +
                  " PRIMARY KEY ( id ))";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS path_closure" +
                  "(ancestor INTEGER NOT NULL, " +
                  " descendant INTEGER NOT NULL, " +
                  " depth INTEGER NOT NULL DEFAULT 0, " +
                  " PRIMARY KEY ( ancestor,descendant ))";
			stmt.executeUpdate(sql);
			
			insertPath("data/");
			
			// create root directory
			FileManager fm = new FileManager();
			fm.mkdirROOT();

			// create an admin
			User user = new User();
			user.setUsername("admin");
			Auth auth = new Auth();
			auth.insertAdmin(user,"password");
			fm = new FileManager(user);
			fm.mkdir("");

			// create a shared location
			user = new User();
			user.setUsername("public");
			auth = new Auth();
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