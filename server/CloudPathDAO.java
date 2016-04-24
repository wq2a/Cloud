package cloud.server;
import java.sql.*;


public class CloudPathDAO implements PathDAO {
	private Statement stmt = null;
	private String sql = "";

	public CloudPathDAO() {
		try{
			stmt = CloudDAOFactory.createConnection().createStatement();
		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		e.printStackTrace();
   		}
	}

	public int insertPath(String path) {
		int id = -1;
		int parent = -1;
		String name = "";
		int type = 0;
		String sql,subPath;
		ResultSet rs;

		try{
			if(contains(path)){
				// already exist
				return path.hashCode();
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
					subPath = path.substring(0,path.lastIndexOf(name));

					if(contains(subPath)){
						parent = subPath.hashCode();
						
					}else{
						// no parent dir
						return id;
					}
				}
			}

			sql = "INSERT INTO path" +
			  	  "(id,name,parent,path,type)" +
			  	  "VALUES(" + path.hashCode() +
			  	  ",'"+ name +
			  	  "'," + parent +
			      ",'" + path +
			      "'," + type +
			      ")";

			if(stmt.executeUpdate(sql) != 0) {
				id = path.hashCode();
            
            	sql = "INSERT INTO path_closure" +
			  	  	"(ancestor,descendant,depth)" +
			  	  	"VALUES('"+parent +
			  	  	"'," + id +
			      	",'" + 1 +
			      	"')";
				if(stmt.executeUpdate(sql)==0)
					return id;

            	sql = "INSERT INTO path_closure" +
			  	  	"(ancestor,descendant,depth)" +
			  	  	"SELECT p.ancestor, c.descendant,p.depth+c.depth" +
			  	  	" from path_closure p, path_closure c" +
			  	  	" where p.descendant=" + parent +
			  	  	" and c.descendant=" + id;
            
				stmt.executeUpdate(sql);
			}
		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		e.printStackTrace();
   		}
     	return id;
   		
	}

	public boolean deletePath(String path) {
		try{
			if(contains(path)){
				sql = "DELETE from path" +
					" WHERE path like '"+ path +"%'";
				stmt.executeUpdate(sql);
				
				sql = "DELETE link from path_closure p, path_closure link, path_closure c" +
					" WHERE p.ancestor = link.ancestor and c.descendant = link.descendant" +
					" and (c.descendant=" + path.hashCode() +
					" or c.ancestor=" + path.hashCode() +
					")";
				stmt.executeUpdate(sql);
			}
		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		e.printStackTrace();
   		}
      	return true;
   		
	}

	public String getPaths(String path) {
		ResultSet rs;
		String prefix = "";
		StringBuffer result = new StringBuffer();
		try{
			if(contains(path)){
				result.append(path.substring(5));
				prefix = ";";
				sql = "SELECT * FROM path p "+
					"JOIN path_closure c "+
					"ON p.id=c.descendant "+
					"WHERE c.ancestor="+path.hashCode();
				rs = stmt.executeQuery(sql);

				while(rs!=null && rs.next()){
					result.append(prefix);
					if(rs.getString("path") != null){
						result.append(rs.getString("path").substring(5));
					}
				}
			}
		}catch(SQLException se){
      		se.printStackTrace();
   		}catch(Exception e){
      		e.printStackTrace();
   		}
      	return result.toString();
	}

	public boolean contains(String path) {
		ResultSet rs;
		try{
			if(path==null || path.isEmpty()){
				return false;
			}
			sql = "SELECT * FROM path WHERE id="+path.hashCode();
			rs = stmt.executeQuery(sql);
			if(rs.next()){

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