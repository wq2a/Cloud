package cloud.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.lang.*;
import java.io.IOException;
import cloud.server.ClientWorker;

public class Server {
    public static void main(String[] args){
        DBManager.getInstance().create();
        try{
            ServerSocket serverSocket = 
                new ServerSocket(Integer.parseInt(args[0]));
            while(true){
                ClientWorker c = new ClientWorker(serverSocket.accept());
                Thread t = new Thread(c);
                t.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
