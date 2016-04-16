package cloud.server;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyExecutor{
	private final static int POOLSIZE = 50;
	private ExecutorService ex = Executors.newFixedThreadPool(POOLSIZE);
	private static MyExecutor instance = new MyExecutor();
	
	public static MyExecutor getInstance(){
		return instance;
	}
	public void exec(Runnable r){
		ex.execute(r);
	}

	public void shutdown(){
		ex.shutdown();
		
        try
        {
            ex.awaitTermination(30, TimeUnit.MINUTES);
        } catch(InterruptedException ex1){
        }
	}
}