package cloud.server;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Synchronization implements Runnable{
	private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private InputStream in2;
    private StringBuffer request;

    private String hostName;
    private int portNumber;
    DAOFactory cloudFactory;
    SyncDAO syncDAO;
    ModeDAO modeDAO;
    ArrayList<Sync> syncs;
    String modes;
    private final static int SLEEPTIME = 5000;

	public Synchronization() {
		cloudFactory = DAOFactory.getDAOFactory(DAOFactory.DAOCLOUD);
		syncDAO = cloudFactory.getSyncDAO();
        modeDAO = cloudFactory.getModeDAO();
		hostName = "52.87.188.170";//"54.165.222.100";
        portNumber = 9901;
        request = new StringBuffer();
	}

	public void run() {
		while(!connect()){
			try{
                	Thread.sleep(SLEEPTIME);
        		}catch(InterruptedException ex){
        			System.err.println(ex.getMessage());
        		}
		}
		while(true){
            modes = modeDAO.getModes();
            out.println(10+";"+0+";"+modes);

			syncs = syncDAO.selectSync();
			if(!syncs.isEmpty()){
				for(Sync sync:syncs){
					//System.out.println(sync.getPath());
					out.println(sync.getType()+";"+sync.getLength()+";"+sync.getPath());

					if(sync.getType()==2 && sync.getLength()>0){
						try{
							//wait for the server ready
						FileManager fm = new FileManager();
						byte[] mybytearray = fm.getContentAB(sync.getPath());
                		OutputStream os = socket.getOutputStream();
                		if(in.readLine() != null){
                    		os.write(mybytearray, 0, mybytearray.length);
                		}
						}catch(Exception e){

						}
						
					}
				}
			}else{
				try{
                	Thread.sleep(SLEEPTIME);
        		} catch(InterruptedException ex){
        			System.err.println(ex.getMessage());
        		}
			}
		}
	}

	private boolean connect(){
        try{
            socket = new Socket(hostName,portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            in2 = socket.getInputStream();
            // System.out.println("established");
        } catch (UnknownHostException e) {
            //System.err.println("Host unknown. Cannot establish connection");
            return false;
        } catch (IOException e) {
            //System.err.println("Cannot establish connection. Server may not be up."+e.getMessage());
            return false;
        }
        return true;
    }

}