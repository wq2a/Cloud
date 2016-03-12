package cloud.client;

import java.math.*;
import java.security.MessageDigest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Connection {
    final static String SP = "\b";
    final static String CRLF = "\r\n";

    final static String STATUS = "Status";
    final static String OK = "200";
    final static String BADREQUEST = "400";
    final static String UNAUTHORIZED = "401";
    final static String NOTFOUND = "404";
    
    final static String CONNECTION = "Connection";
    final static String CLOSE = "close";

    private String method;
    private StringBuffer requestPropertys;
    private HashMap<String,String> response;
    Connection(){
        requestPropertys = new StringBuffer();
        response = new HashMap<String,String>();
    }
    public void setRequestMethod(String method){
        this.method = method+CRLF;
    }
    public void setRequestProperty(String field,String value){
        requestPropertys.append(field+":"+SP+value+CRLF);
    }
    public HashMap<String,String> connect(){
        response.clear();
        String temp = ClientSocket.getInstance().getResponse(toString());
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
            ClientSocket.getInstance().disconnect();
        }
        return response;
    }
    public String toString(){
        return method+requestPropertys.toString();
    }

}
