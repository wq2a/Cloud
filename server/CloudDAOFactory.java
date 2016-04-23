package cloud.server;
import java.sql.*;

public class CloudDAOFactory extends DAOFactory{

	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/cloud?useUnicode=true&characterEncoding=utf-8&useSSL=false";
	private static final String USER = "root";
	private static final String PASSWORD = "";
	private static Connection conn = null;

	public static Connection createConnection() {
		//return ConnectionPool.getConnection();
		if (conn == null) {
			try{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL,USER,PASSWORD);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return conn;
	}

	public UserDAO getUserDAO() {
		return new CloudUserDAO();
	}

	public PathDAO getPathDAO() {
		return new CloudPathDAO();
	}

	public SyncDAO getSyncDAO() {
		return new CloudSyncDAO();
	}
}