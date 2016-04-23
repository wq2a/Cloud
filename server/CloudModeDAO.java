package cloud.server;
import java.sql.*;


public class CloudModeDAO implements ModeDAO {
	private Statement stmt = null;
	private String sql = "";

	public CloudModeDAO() {
		try{
			stmt = CloudDAOFactory.createConnection().createStatement();
		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		e.printStackTrace();
   		}
	}

	public String getModes() {
		ResultSet rs;
		StringBuffer result = new StringBuffer();
		try{
			sql = "SELECT * FROM mode";
			rs = stmt.executeQuery(sql);
			String prefix = "";
			while(rs.next()){
				result.append(rs.getString("path")).append(prefix);
				prefix = ":";
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public boolean insertMode(Mode mode) {
		try{
			sql = "INSERT INTO mode" +
			  	  "(path,user_id)" +
			  	  "VALUES('" + mode.getPath() +
			  	  "',"+ mode.getUser_id() +
			  	  ")";
			stmt.executeUpdate(sql);
		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		e.printStackTrace();
   		}
     	return true;
	}

	public boolean deleteMode(Mode mode){
		try{
			if(mode.getUser_id()!=0){
				sql = "DELETE from mode WHERE user_id="+ mode.getUser_id();
				stmt.executeUpdate(sql);
			}else{
				sql = "DELETE from mode WHERE path='"+ mode.getPath()+"'";
				stmt.executeUpdate(sql);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
		
	}

	public boolean contains(Mode mode){
		ResultSet rs;
		try{
			if(!mode.getPath().isEmpty()){
				sql = "SELECT * FROM mode WHERE path like '"+mode.getPath()+"%'";
				rs = stmt.executeQuery(sql);
				if(rs.next()){
					return true;
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
}