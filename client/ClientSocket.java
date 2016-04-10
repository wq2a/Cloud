package cloud.client;
import java.io.*;
import java.net.*;
import cloud.client.FileManager;
 
public class ClientSocket{
    final static String CRLF = "\r\n";
    private static ClientSocket instance;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private StringBuffer response;

    private String hostName;
    private int portNumber;

    ClientSocket(){
        hostName = "localhost";
        portNumber = 9900;
        response = new StringBuffer();
        connect();
    }

    private void connect(){
        try{
            socket = new Socket(hostName,portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //System.out.println("established");
        } catch (UnknownHostException e) {
            System.err.println("Host unknown. Cannot establish connection");
        } catch (IOException e) {
            System.err.println("Cannot establish connection. Server may not be up."+e.getMessage());
        }
    }

    public static ClientSocket getInstance(){
        if(instance == null)
            instance = new ClientSocket();
        return instance;
    }

    public String getResponse(String request,FileManager fm){
        if(socket == null)
            connect();
        try{
            String fromServer;
            response.setLength(0);
            
            out.println(request);

            if(fm != null){
                File fo = new File(fm.aPath(fm.getP()));
                byte[] mybytearray = new byte[(int) fo.length()];
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fo));
                bis.read(mybytearray, 0, mybytearray.length);
                OutputStream os = socket.getOutputStream();
                os.write(mybytearray, 0, mybytearray.length);
            }

            while((fromServer = in.readLine()) != null){
                if(fromServer.isEmpty())
                    break;
                response.append(fromServer+CRLF);
            }
        }catch(Exception e){
            System.err.println("Cannot establish connection. Server may not be up."+e.getMessage());
        }
        return response.toString();
    }

    public void disconnect(){
        try{
            in.close();
            out.close();
            socket.close();
            socket = null;
        }catch(Exception e){

        }
    }
}