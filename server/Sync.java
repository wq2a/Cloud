package cloud.server;

public class Sync {
	private String path;
	private int type;
	private int sync_type;
	private int user_id;
	private int length;
	Sync(String path,int length,int type,int sync_type,int user_id){
		this.path = path;
		this.length = length;
		this.type = type;
		this.sync_type = sync_type;
		this.user_id = user_id;
	}

	public String getPath(){
		return path;
	}

	public int getLength(){
		return length;
	}

	public int getType(){
		return type;
	}

	public int getSync_type(){
		return sync_type;
	}

	public int getUser_id(){
		return user_id;
	}
}