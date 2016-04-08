package cloud.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyExecutor{
	private ExecutorService ex = Executors.newFixedThreadPool(5);
	private static MyExecutor instance = new MyExecutor();
	
	private MyExecutor(){

	}
	public static MyExecutor getInstance(){
		return instance;
	}
	public void exec(Runnable r){
		ex.execute(r);
	}
}