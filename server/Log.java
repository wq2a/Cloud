package cloud.server;

public class Log{

	public static Log instance;
	private Log(){

	}
	public static Log getInstance(){
		if(instance == null)
			instance = new Log();
		return instance;
	}
	public void print(User user){

	}
	public void print(Object o){
		
		System.out.println(o);
	}
}