package cloud.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.lang.*;
import java.util.HashMap;
import java.util.ArrayList;
import cloud.server.Protocol;
import cloud.server.Log;

public class ClientWorker implements Runnable{
    private static HashMap<Integer,ClientWorker> workers = new HashMap<Integer,ClientWorker>();
    final static String CRLF = "\r\n";
    private Socket socket;
    private Integer workerID;

    Protocol protocol;

    ClientWorker(Socket socket,Integer workerID){
        this.socket = socket;
        this.workerID = workerID;
        workers.put(workerID,this);
    }

    public void run(){
        //Log.getInstance().print("["+workerID+"]");
        process();
    }

    public static HashMap<Integer,ClientWorker> getWorkers(){
        return workers;
    }

    private void process(){
        protocol = new Protocol();
        StringBuffer requestStr = new StringBuffer();
        
        StringBuffer responseStr = new StringBuffer();
        String temp;
        int length;
        try(BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);){
            while((temp=in.readLine()) != null){
                if(temp.isEmpty()){
                    length = protocol.process(requestStr.toString());

                    if(length > 0){
                        /*
                        datafile.setLength(0);
                        for(int i=0;i<length;i++){
                            datafile.append(in.read());
                        }*/
                        InputStream in2 = socket.getInputStream();
                        byte[] bytes = new byte[length];
                        int count = 0;
                        while((count+=in2.read(bytes)) < length){

                        }
                        //String datafile = new String(bytes,"UTF-8");

                        protocol.processFile(new String(bytes,"UTF-8"));
                    }

                    out.println(protocol.response());
                    
                    if(protocol.isClosed())
                        break;
                    requestStr.setLength(0);
                }else{
                    requestStr.append(temp+CRLF);
                }
            }
            Log.getInstance().print("["+workerID+"]"+"closed");
            workers.remove(workerID);
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
