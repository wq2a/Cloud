package cloud.client;
import java.sql.Timestamp;
import java.util.Date;

public class Utils{
	public static void showTime(){
		Date date = new Date();
		System.out.println(new Timestamp(date.getTime()));
	}

	public static void wait(int sec){
		try{
                Thread.sleep(2000);
        }catch(InterruptedException ex){
        	System.err.println(ex.getMessage());
        }
	}
}