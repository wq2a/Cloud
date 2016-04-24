package cloud.client;
import java.sql.Timestamp;
import java.util.Date;

import java.math.*;
import java.security.MessageDigest;
import java.security.SecureRandom;

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

	public static String Random(){
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return String.format("%064x", new BigInteger(1, bytes));
    }

    public static String warning(String message){
        return "<html><font color=\"#c00000;\">"+message+"</font></html>";
    }

    public static String info(String message){
        return "<html><font color=\"#009933;\">"+message+"</font></html>";
    }
}