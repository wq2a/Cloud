package cloud.server;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class SynchronizationR implements Runnable{
	private Socket socket;
    public static String modes = "";
    private int portNumber;
    DAOFactory cloudFactory;
    SyncDAO syncDAO;
    ArrayList<Sync> syncs;

	public SynchronizationR() {
		cloudFactory = DAOFactory.getDAOFactory(DAOFactory.DAOCLOUD);
		syncDAO = cloudFactory.getSyncDAO();
        portNumber = 9901;
        
	}

	public void run() {
		 try{
            ServerSocket serverSocket = new ServerSocket(this.portNumber);
            socket = serverSocket.accept();
            String temp;
            try(BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            InputStream in2 = socket.getInputStream();
            OutputStream os = socket.getOutputStream();){

            while(true){
                temp=in.readLine();
                String[] r = temp.split(";");
                if((r[0]).equals("10")){
                    if(r.length == 3){
                        modes = r[2];
                    }else{
                        modes = "";
                    }
                    
                }else if((r[0]).equals("1")){
                    PathDAO pathDAO = cloudFactory.getPathDAO();
                    pathDAO.deletePath(r[2]);
                    FileManager fm = new FileManager();
                    fm.del(r[2]);
                }else if((r[0]).equals("2")){
                    byte[] bytes = new byte[0];
                    int length = Integer.parseInt(r[1]);
                    if(length>0){
                        bytes = new byte[length];
                        int count = 0;
                        out.println("ok");
                        while((count+=in2.read(bytes)) < length){
                        }
                    }
                    PathDAO pathDAO = cloudFactory.getPathDAO();
                    if(pathDAO.insertPath(r[2]) != -1){
                        try{
                            FileManager fm = new FileManager();
                            if(r[2].endsWith("/")){
                                fm.mkdirAB(r[2]);
                            }else{
                                fm.mkfileAB(r[2],new String(bytes,"UTF-8"));
                            }
                
                        } catch(IOException e){
                            System.err.println(e.getMessage());
                        } catch(Exception e){
                            System.err.println(e.getMessage());
                        }
                    }
                }
            }

            }catch(IOException e){
                e.printStackTrace();
            }
            
        } catch(IOException e){
            e.printStackTrace();
        }
	}

}