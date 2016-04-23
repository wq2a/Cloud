package cloud.server;

public class Mode {
	private int id;
	private String path;
	private int user_id = 0;
	public Mode(String path, int user_id){
		this.path = path;
		this.user_id = user_id;
	}

	public String getPath(){
		return path;
	}

	public int getUser_id(){
		return user_id;
	}

}