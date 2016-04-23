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
    final static String LOCKED = "423";

    final static String CONNECTION = "Connection";
    final static String CLOSE = "close";

    //private Socket socket;
    private int length;
    private HashMap<String,String> requestMap;
    private HashMap<String,String> responseMap;
    private String filePath = "";
    private int fileLength = 0;
    private StringBuffer response;
    private boolean isClosed;

    byte[] bytes;
    private boolean writeToFile = false;

    private Auth auth;

    DAOFactory cloudFactory;

    Protocol(){
        responseMap = new HashMap<String,String>();
        requestMap = new HashMap<String,String>();
        response = new StringBuffer();
        isClosed = false;
        auth = new Auth();
        bytes = new byte[0];
        cloudFactory = DAOFactory.getDAOFactory(DAOFactory.DAOCLOUD);
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

        switch(requestMap.get(METHOD)){
            case GET:
                
                if(null!=requestMap.get("File")&&null!=requestMap.get("Path")){
                    FileManager fm = new FileManager();
                    
                    responseMap.put("File",requestMap.get("Path"));
                    filePath = requestMap.get("Path");
                    fileLength = Integer.parseInt(fm.getLength(requestMap.get("Path")));
                    responseMap.put("Length",fileLength+"");
                   
                }
                break;
            case PUT:
                this.bytes = bytes;
                if(null!=requestMap.get("Path")){

                    PathDAO pathDAO = cloudFactory.getPathDAO();
                    if(pathDAO.insertPath("data/"+requestMap.get("Path")) == -1){
                        //if(DBManager.getInstance().insertPath("data/"+requestMap.get("Path")) == -1){
                        // error
                        responseMap.put(STATUS,NOTFOUND);
                    }else{
                        writeToFile = true;
                        Sync sync = new Sync("data/"+requestMap.get("Path"),bytes.length,2,1,(auth.getUser().getUsername()).hashCode());
                        SyncDAO syncDAO = cloudFactory.getSyncDAO();
                        syncDAO.insertSync(sync);
                        
                    }
                }
                break;
            case DELETE:
                // delete file on server
                if(null!=requestMap.get("Path") && !(requestMap.get("Path")).isEmpty()){
                    Mode mode = new Mode("data/"+requestMap.get("Path"),auth.getUser().getUsername().hashCode());
                    ModeDAO modeDAO = cloudFactory.getModeDAO();
                    if(modeDAO.contains(mode) || SynchronizationR.modes.contains(requestMap.get("Path"))){
                        responseMap.put(STATUS,LOCKED);
                    }else{
                        PathDAO pathDAO = cloudFactory.getPathDAO();
                        pathDAO.deletePath("data/"+requestMap.get("Path"));
                        //DBManager.getInstance().delPath("data/"+requestMap.get("Path"));
                        FileManager fm = new FileManager(auth);
                        fm.del(requestMap.get("Path"));
                        Sync sync = new Sync("data/"+requestMap.get("Path"),0,1,1,(auth.getUser().getUsername()).hashCode());
                        SyncDAO syncDAO = cloudFactory.getSyncDAO();
                        syncDAO.insertSync(sync);
                    }
                }else{
                    // path is empty
                    responseMap.put(STATUS,NOTFOUND);
                }
                break;
            case POST:
                // register new user
                if(requestMap.get("Register")!=null){
                    auth.register(requestMap);
                }

                break;
            default:
                responseMap.put(STATUS,BADREQUEST);
        }

        if(requestMap.get("Path")!=null){
            Mode mode = new Mode("data/"+requestMap.get("Path"),auth.getUser().getUsername().hashCode());
            ModeDAO modeDAO = cloudFactory.getModeDAO();
            if(requestMap.get("EditMode")!=null){
                if(modeDAO.contains(mode) || SynchronizationR.modes.contains(requestMap.get("Path"))){
                    responseMap.put(STATUS,LOCKED);
                }else{
                    System.out.println("insert");
                    modeDAO.insertMode(mode);
                }
            } else {
                System.out.println("remove");
                modeDAO.deleteMode(mode);
            }
        }

        if(requestMap.get(METHOD).equals(GET)||requestMap.get(METHOD).equals(PUT)||requestMap.get(METHOD).equals(DELETE)){
            PathDAO pathDAO = cloudFactory.getPathDAO();
            responseMap.put("private", pathDAO.getPaths("data/"+auth.getUser().getUsername()+"/"));
            responseMap.put("public", pathDAO.getPaths("data/public/"));
            //responseMap.put("private", DBManager.getInstance().getPath("data/"+auth.getUser().getUsername()+"/"));
            //responseMap.put("public", DBManager.getInstance().getPath("data/public/"));
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

    public String getFilePath(){
        return filePath;
    }

    public int getFileLength(){
        return fileLength;
    }

    public boolean isClosed(){
        return isClosed;
    }

}
