package cloud.client;

import java.math.*;
import java.security.MessageDigest;
import java.io.IOException;

public class Connection {
    final static String CRLF = "\r\n";
    private String method;
    private StringBuffer requestPropertys;
    private String response;
    Connection(){
        requestPropertys = new StringBuffer();
    }
    public void setRequestMethod(String method){
        this.method = method+CRLF;
    }
    public void setRequestProperty(String field,String value){
        requestPropertys.append(field+": "+value+CRLF);
    }
    public String connect(){
        return ClientSocket.getInstance().getResponse(toString());
    }
    public String toString(){
        return method+requestPropertys.toString();
    }

}
