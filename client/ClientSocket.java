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
    private InputStream in2;
    private StringBuffer response;

    private String hostName;
    private int portNumber;

    private String code;

    ClientSocket(){
        hostName = "localhost";//"52.87.188.170";
        portNumber = 9900;
        response = new StringBuffer();
        code = Utils.Random();
        connect();
    }

    public String getCode(){
        return code;
    }

    private void connect(){
        try{
            socket = new Socket(hostName,portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            in2 = socket.getInputStream();
            // System.out.println("established");
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

    public String getResponse(int requestID,String request,FileManager fm){
        if(socket == null)
            connect();
        try{
            String fromServer;
            response.setLength(0);
            
            out.println(request);

            if(fm != null){
                byte[] mybytearray = fm.getContent();
                OutputStream os = socket.getOutputStream();
                // wait until server is ready
                if(in.readLine() != null){
                    os.write(mybytearray, 0, mybytearray.length);
                }
            }

            while((fromServer = in.readLine()) != null){
                if(fromServer.isEmpty())
                    break;
                response.append(fromServer+CRLF);
            }

            if(!code.equals(instance.getCode())&&requestID!=Connection.GET_FILE){
                disconnect();
            }
        }catch(Exception e){
            System.err.println("Cannot establish connection. Server may not be up."+e.getMessage());
        }
        return response.toString();
    }

    public String getFile(int length) throws Exception{
        byte[] bytes = new byte[0];

        if(length > 0){
            bytes = new byte[length];
            int count = 0;
            out.println("ok");
            while((count+=in2.read(bytes)) < length){
            }
        }
        disconnect();
        return new String(bytes,"UTF-8");
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