package cloud.client;

import java.math.*;
import java.security.MessageDigest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import cloud.client.Info;

public class Connection {
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


    private String method;
    private StringBuffer requestPropertys;
    private HashMap<String,String> response;
    private FileManager fm;
    Connection(){
        requestPropertys = new StringBuffer();
        response = new HashMap<String,String>();
        fm = null;
    }
    public void setRequestMethod(String method){
        this.method = method+CRLF;
        if(method.equals(PUT)){
            setRequestProperty("Auth",Auth.getInstance().toString());
            setRequestProperty("Connection","close");
        }
        setRequestProperty(AGENT,Info.getInstance().getOS());
        setRequestProperty(LANGUAGE,Info.getInstance().getLanguage());
    }
    public void setRequestProperty(String field,String value){
        if(field.equals("Path") && value.length()>0 
                && !(value.substring(value.length()-1)).equals("/")){
            
            fm = new FileManager(value);
            setRequestProperty("Length",fm.getLength(value));

            setRequestProperty("Connection","close");
        }
        requestPropertys.append(field+":"+SP+value+CRLF);
    }
    public HashMap<String,String> connect(){
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
                    response.put(property[0],property[1].trim());
                }
            }
        }

        if(response.get(CONNECTION)!=null&&response.get(CONNECTION).equals(CLOSE)){
            client.disconnect();
        }
        return response;
    }
    public String toString(){
        return method+requestPropertys.toString();
    }

}
