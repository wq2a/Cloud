package cloud.server;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.*;
import java.io.IOException;
import cloud.server.DBManager;

public class ServerManager{
	private int portNumber;
	private Integer workerID = 0;
	ServerManager(){
		this.portNumber = 9900;
	}
	ServerManager(int portNumber){
		this.portNumber = portNumber;
	}
	public void start(boolean reset){
		if(reset){
			DBManager.getInstance().cleanCreate();
		}else{
			DBManager.getInstance().create();
		}
		
        try{
            ServerSocket serverSocket = 
                new ServerSocket(this.portNumber);
            while(true){
            	System.out.println("current workers:"+ClientWorker.getWorkers().size());
                ClientWorker c = new ClientWorker(serverSocket.accept(),this.workerID);
                Thread t = new Thread(c);
                t.start();
                this.workerID++;
            }
        } catch(IOException e){
            e.printStackTrace();
        }
	}
}