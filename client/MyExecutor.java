package cloud.client;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyExecutor{
	private final static int MAINPOOLSIZE = 3;
	private final static int POOLSIZE = 5;
	// main threads
	private ExecutorService ex_m = Executors.newFixedThreadPool(MAINPOOLSIZE);
	// other threads
	private ExecutorService ex_o = Executors.newFixedThreadPool(POOLSIZE);
	private static MyExecutor instance = new MyExecutor();
	
	public static MyExecutor getInstance(){
		return instance;
	}
	public void exec(Runnable r){
		if(((Connection)r).getMethod() == "PUT"){
			ex_o.execute(r);
		}else{
			ex_m.execute(r);
		}
	}

	public void shutdown(){
		ex_m.shutdown();
		ex_o.shutdown();

        try
        {
            ex_m.awaitTermination(30, TimeUnit.MINUTES);
            ex_o.awaitTermination(30, TimeUnit.MINUTES);
        } catch(InterruptedException ex1){
        }
	}
}