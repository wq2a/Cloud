package cloud.server;

import cloud.server.ServerManager;

public class Server {
    public static void main(String[] args){
        ServerManager server;
        switch(args.length){
            case 0:
                break;
            case 1:
                server = new ServerManager(Integer.parseInt(args[0]));
                server.start(false);
                break;
            case 2:
                server = new ServerManager(Integer.parseInt(args[0]));
                server.start(true);
                break;
        }
        
    }
}