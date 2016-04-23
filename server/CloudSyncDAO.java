package cloud.server;
import java.sql.*;
import java.util.ArrayList;



public class CloudSyncDAO implements SyncDAO {
	private Statement stmt = null;
	private String sql = "";
	private static int sync_id = -1;

	public CloudSyncDAO() {
		try{
			stmt = CloudDAOFactory.createConnection().createStatement();
		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		e.printStackTrace();
   		}
	}

	public ArrayList<Sync> selectSync() {
		ResultSet rs;
		ArrayList<Sync> syncs = new ArrayList<Sync>();
		
		try{
			sql = "SELECT * FROM file_sync where sync_id>"+sync_id +" order by sync_id";
			rs = stmt.executeQuery(sql);
			while(rs!=null && rs.next()){
				Sync sync = new Sync(rs.getString("path"),
						rs.getInt("length"),rs.getInt("type"),
						rs.getInt("sync_type"),rs.getInt("user_id"));
				syncs.add(sync);
				sync_id = rs.getInt("sync_id");
			}
			
		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		e.printStackTrace();
   		}
      	return syncs;
	}

	public boolean insertSync(Sync sync) {
		try{
			deleteSync(sync);

			sql = "INSERT INTO file_sync" +
			  	  "(path,length,type,sync_type,user_id)" +
			  	  "VALUES('" + sync.getPath() +
			  	  "',"+sync.getLength() +
			  	  ","+sync.getType() +
			  	  "," + sync.getSync_type() +
			      "," + sync.getUser_id() +
			      ")";

			if(stmt.executeUpdate(sql)==0)
				return false;

		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		e.printStackTrace();
   		}
     	return true;
	}

	public void deleteSync(Sync sync) {
		try{
			
			sql = "DELETE from file_sync" +
				" WHERE path like '"+ sync.getPath() +"%'";
			
			stmt.executeUpdate(sql);

		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		e.printStackTrace();
   		}
	}


}