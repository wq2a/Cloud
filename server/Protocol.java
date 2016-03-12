package cloud.server;

import java.math.*;
import java.security.MessageDigest;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Protocol {
    final static String CRLF = "\r\n";
    final static String SP = "\b";
    final static String METHOD = "Method";
    final static String GET = "GET";
    final static String PUT = "PUT";
    final static String DELETE = "DELETE";
    final static String POST = "POST";
    final static String STATUS = "status";
    private static Protocol instance;
    private HashMap<String,String> requestMap;
    private HashMap<String,String> responseMap;
    private StringBuffer response;

    private Protocol(){
        responseMap = new HashMap<String,String>();
        requestMap = new HashMap<String,String>();
        response = new StringBuffer();
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

    private String response(){
        if(requestMap.isEmpty()||requestMap.get(METHOD)==null){
            responseMap.put(STATUS,"400");
            return generator();
        }
        responseMap.put(STATUS,"200");
        // 
        switch(requestMap.get(METHOD)){
            case GET:
                responseMap.put("type",GET);
                break;
            case PUT:
                break;
            case DELETE:
                break;
            case POST:
                responseMap.put("type",POST);
                break;
            default:
                responseMap.put(STATUS,"400");
        }
        return generator();
    }

    public static Protocol getInstance(){
        if (instance == null)
            instance = new Protocol();
        return instance;
    }

    public String process(String request){
        requestMap.clear();
        responseMap.clear();
        if(!request.isEmpty()){
            String r[] = request.split("\\r?\\n");
            for(int i=0;i<r.length;i++){
                if(i == 0){
                    requestMap.put(METHOD,r[i]);
                }else{
                    String[] property = r[i].split(SP);
                    requestMap.put(property[0],property[1]);
                }
            }
        }
        return response();
    }

}
