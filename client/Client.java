package cloud.client;

import java.net.ServerSocket;
import java.net.Socket;
import java.lang.*;
import java.security.MessageDigest;
import java.io.IOException;
import cloud.client.Auth;


public class Client {

    public static void main(String[] args){
    	
    	Auth auth = Auth.getInstance();
    	auth.setAccount("wanjiang","qian111");
    	/*
    	auth.setAccount("wan","qian111");
    	System.out.println(auth.toString());

    	auth.setAccount("wanjiang","qian111");
    	System.out.println(auth.toString());
    	*/
    	Connection cnn = new Connection();
    	cnn.setRequestMethod("GET");
    	cnn.setRequestProperty("User-agent","xxxx/1.0");
    	System.out.println(cnn.connect());

    	System.out.println("*****");

    	cnn = new Connection();
    	cnn.setRequestMethod("POST");
    	cnn.setRequestProperty("User-agent1","xxxx/1.0");
    	cnn.setRequestProperty("Auth",auth.toString());
    	System.out.println(cnn.connect());

        System.out.println("*****Bad");

        cnn = new Connection();
        cnn.setRequestMethod("POSTo");
        cnn.setRequestProperty("User-agent1","xxxx/1.0");
        cnn.setRequestProperty("Auth",auth.toString());
        System.out.println(cnn.connect());

        System.out.println("*****Bad");

        cnn = new Connection();
        cnn.setRequestMethod("");
        cnn.setRequestProperty("User-agent1","xxxx/1.0");
        cnn.setRequestProperty("Auth",auth.toString());
        System.out.println(cnn.connect());

    	ClientSocket.getInstance().disconnect();
    }
}
