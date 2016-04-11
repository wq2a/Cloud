package cloud.client;

import java.math.*;
import java.security.MessageDigest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import cloud.client.Info;

public class Connection implements Runnable{
    final static String SP = " ";
    final static String CRLF = "\r\n";

    final static String GET = "GET";
    final static String PUT = "PUT";
    final static String DELETE = "DELETE";
    final static String POST = "POST";

    final static String STATUS = "Status";
    final static String OK = "200";
    final static String BADREQUEST = "400";
    final static String UNAUTHORIZED = "401";
    final static String NOTFOUND = "404";
    
    final static String AGENT = "User-agent";
    final static String LANGUAGE = "Language";
    final static String CONNECTION = "Connection";
    final static String CLOSE = "close";

    public final static int LOGIN = 0;
    public final static int REGISTER = 1;
    public final static int UPLOAD_FILE = 2;
    public final static int GET_PATH = 3;

    private int requestID;
    private int tag;
    private String method;
    private StringBuffer requestPropertys;
    private HashMap<String,String> response;
    private FileManager fm;

    Callback callback;
    ReceiverCallback rcallback;

    Connection(){
        requestPropertys = new StringBuffer();
        response = new HashMap<String,String>();
        fm = null;
    }
    // Copy Constructor
    Connection(Connection cnn){
        this();
        method = cnn.getMethod();
        requestPropertys = cnn.getRequest();
        tag = cnn.getTag();
        requestID = cnn.getRequestID();
        fm = cnn.getFm();
    }

    Connection(Callback c){
        this();
        callback = c;
        callback.start(this);
    }

    Connection(Connection cnn, Callback c){
        this(cnn);
        callback = c;
        callback.start(this);
    }

    Connection(ReceiverCallback rc,Connection cnn, Callback c){ 
        this(cnn,c);
        this.rcallback = rc;
    }
    public ReceiverCallback getRCallback(){
        return rcallback;
    }
    public String getMethod(){
        return method;
    }
    public StringBuffer getRequest(){
        return requestPropertys;
    }
    public FileManager getFm(){
        return fm;
    }
    public void setRequestMethod(int requestID,String method){
        this.requestID = requestID;
        this.method = method+CRLF;
        /*if(method.equals(PUT)){
            setRequestProperty("Auth",Auth.getInstance().toString());
            setRequestProperty("Connection","close");
        }*/
        setRequestProperty(AGENT,Info.getInstance().getOS());
        setRequestProperty(LANGUAGE,Info.getInstance().getLanguage());
    }
    public void setTag(int tag){
        this.tag = tag;
    }
    public int getTag(){
        return tag;
    }
    public int getRequestID(){
        return requestID;
    }
    public void setRequestProperty(String field,String value){
        if(field.equals("Path") && value.length()>0 
                && !(value.substring(value.length()-1)).equals("/")){
            fm = new FileManager(value);
            setRequestProperty("Length",fm.getLength(value));

            setRequestProperty("Connection","close");
            requestPropertys.append(field+":"+SP+Auth.getInstance().getUsername()+"/"+value+CRLF);
        }else{
            requestPropertys.append(field+":"+SP+value+CRLF);
        }
        
    }

    public void run(){
        connect();
    }

    public void connect(){
        response.clear();
        String temp;
        ClientSocket client;

        if(method.equals(PUT+CRLF)){
            client = new ClientSocket();
        }else{
            client = ClientSocket.getInstance();
        }

        temp = client.getResponse(toString(),fm);
        
        if(!temp.isEmpty()){
            String r[] = temp.split("\\r?\\n");
            for(int i=0;i<r.length;i++){
                if(i == 0){
                    response.put(STATUS,r[i]);
                }else{
                    String[] property = r[i].split(":");
                    if(property.length<2){
                        response.put(property[0],"");
                    }else{
                        response.put(property[0],property[1].trim());
                    }
                }
            }
        }

        if(response.get(CONNECTION)!=null && response.get(CONNECTION).equals(CLOSE)){
            client.disconnect();
        }
        callback.response(requestID,tag,rcallback,response);
    }
    
    public String toString(){
        return method+requestPropertys.toString();
    }

}
