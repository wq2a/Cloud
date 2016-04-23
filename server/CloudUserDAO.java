package cloud.server;
import java.sql.*;


public class CloudUserDAO implements UserDAO {

	private Statement stmt = null;
	private String sql = "";

	public CloudUserDAO() {
		try{
			stmt = CloudDAOFactory.createConnection().createStatement();
		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		e.printStackTrace();
   		}
   	}

	public boolean insertUser(User user) {
		try{
			if(contains(user)){
				return false;
			}

			sql = "INSERT INTO user" +
			  	  "(id,username,h2,salt,firstname,lastname,email)" +
			  	  "VALUES(" + user.getUsername().hashCode() +
			  	  ",'"+user.getUsername() +
			  	  "','" + user.getH2() +
			      "','" + user.getSalt() +
			      "','" + user.getFirstname() +
			      "','" + user.getLastname() +
			      "','" + user.getEmail() +
			      "')";

			if(stmt.executeUpdate(sql)==0)
				return false;

			//insertPath("data/"+user.getUsername()+"/");

			//FileManager fm = new FileManager(user);
			//fm.mkdir("");

		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		e.printStackTrace();
   		}
     	return true;
   		
	}

	public User findUser(User user) {
		try{
			sql = "SELECT * FROM user WHERE username='"+user.getUsername()+"'";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				user.setH2(rs.getString("h2"));
				user.setSalt(rs.getString("salt"));
			}
		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		e.printStackTrace();
   		}
     	return user;
   		
	}

	public boolean contains(User user) {
		try{
			sql = "SELECT * FROM user WHERE username='"+user.getUsername()+"'";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				// user already exist
				return true;
			}
		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		e.printStackTrace();
   		}
     	return false;
	}
}