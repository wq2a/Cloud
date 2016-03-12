package cloud.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.lang.*;

public class ClientWorker implements Runnable{
    final static String CRLF = "\r\n";
    private Socket socket;
    ClientWorker(Socket socket){
        this.socket = socket;
    }
    public void run(){
        process();
    }

    private void process(){
        StringBuffer requestStr = new StringBuffer();
        StringBuffer responseStr = new StringBuffer();
        String temp;
        try{
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            int index=0;
            while((temp=in.readLine()) != null){
                if(temp.isEmpty()){
                    out.println(requestStr.toString());
                    System.out.println(requestStr.toString());
                    requestStr.setLength(0);
                }else{
                    requestStr.append(temp+CRLF);
                }                  
                index++;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
