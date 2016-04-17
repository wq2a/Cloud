package cloud.server;

import java.math.*;
import java.security.MessageDigest;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import cloud.server.Auth;

public class Protocol {
    final static String CRLF = "\r\n";
    final static String SP = " ";

    final static String METHOD = "Method";
    final static String GET = "GET";
    final static String PUT = "PUT";
    final static String DELETE = "DELETE";
    final static String POST = "POST";

    final static String STATUS = "Status";
    final static String OK = "200";
    final static String BADREQUEST = "400";
    final static String UNAUTHORIZED = "401";
    final static String NOTFOUND = "404";

    final static String CONNECTION = "Connection";
    final static String CLOSE = "close";

    //private Socket socket;
    private int length;
    private HashMap<String,String> requestMap;
    private HashMap<String,String> responseMap;
    private StringBuffer response;
    private boolean isClosed;

    byte[] bytes;
    private boolean writeToFile = false;

    private Auth auth;

    Protocol(){
        responseMap = new HashMap<String,String>();
        requestMap = new HashMap<String,String>();
        response = new StringBuffer();
        isClosed = false;
        auth = new Auth();
        bytes = new byte[0];
    }

    private String generator(){
        response.setLength(0);
        response.append(responseMap.get(STATUS)+CRLF);
        responseMap.remove(STATUS);
        
        for(Map.Entry entry:responseMap.entrySet()){
            response.append(entry.getKey()+":"+SP+entry.getValue()+CRLF);
        }
        return response.toString();
    }

    public String response(byte[] bytes){
        if(requestMap.isEmpty()||requestMap.get(METHOD)==null){
            responseMap.put(STATUS,BADREQUEST);
            return generator();
        }

        if(!auth.isAuthorized(requestMap) && !(requestMap.get("Register")!=null&&(requestMap.get(METHOD)).equals(POST))){
            responseMap.put(STATUS,UNAUTHORIZED);
            return generator();
        }
        
        responseMap.put(STATUS,OK);
        // 
        switch(requestMap.get(METHOD)){
            case GET:
                responseMap.put("type",GET);
                if(requestMap.get("Parent") != null){
                    responseMap.put("data", 
                        DBManager.getInstance().getPath("data/"+auth.getUser().getUsername()+"/"+requestMap.get("Parent")));
                }
                // get file from server
                // ...
                break;
            case PUT:
                this.bytes = bytes;
                if(null!=requestMap.get("Path")){
                    
                    if(DBManager.getInstance().insertPath("data/"+requestMap.get("Path")) == -1){
                        // error
                        responseMap.put(STATUS,NOTFOUND);
                    }else{
                        writeToFile = true;
                    }
                }
                break;
            case DELETE:
                // delete file on server
                // ...
                break;
            case POST:
                responseMap.put("type",POST);
                if(requestMap.get("Register")!=null){
                    auth.register(requestMap);
                }
                
                break;
            default:
                responseMap.put(STATUS,BADREQUEST);
        }
        if(requestMap.get(CONNECTION)!=null&&requestMap.get(CONNECTION).equals(CLOSE)){
            responseMap.put(CONNECTION,CLOSE);
            isClosed = true;
        }
        return generator();
    }

    public int process(String request){
        length = 0;
        requestMap.clear();
        responseMap.clear();
        if(!request.isEmpty()){
            String r[] = request.split("\\r?\\n");
            for(int i=0;i<r.length;i++){
                if(i == 0){
                    requestMap.put(METHOD,r[i]);
                }else{
                    String[] property = r[i].split(":");
                    requestMap.put(property[0],property[1].trim());
                    if(property[0].equals("Length")){
                        length = Integer.parseInt(property[1].trim());
                    }
                }
            }
        }
        return length;
        // return response();
    }

    public void backGround(){
        if(writeToFile){
            try{
                FileManager fm = new FileManager();
                if(requestMap.get("Path").endsWith("/")){
                    fm.mkdir(requestMap.get("Path"));
                }else{
                    fm.mkfile(requestMap.get("Path"),new String(bytes,"UTF-8"));
                }
                
            } catch(IOException e){
                System.err.println(e.getMessage());
            } catch(Exception e){
                System.err.println(e.getMessage());
            }
        }
    }

    public boolean isClosed(){
        return isClosed;
    }

}
