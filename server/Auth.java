package cloud.server;

import java.math.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import cloud.server.User;
import cloud.server.DBManager;

public class Auth {
    final static String SP = " ";
    private User user;
    private boolean isAuthorized;

    Auth(){
        user = new User();
        isAuthorized = false;
    }

    private String SHA_256(String str){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes("UTF-8"));
            return String.format("%064x", new BigInteger(1, md.digest()));
        }catch(Exception e){
            return null;
        }
    }

    private String Random(){
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return String.format("%064x", new BigInteger(1, bytes));
    }

    private boolean checkValidate(){
        if(user.isEmpty())
            return false;
        return user.getH2().equals(SHA_256(user.getSalt()+user.getH1()));
    }

    // fetch user info from client
    public void setUser(User user){
        this.user = user;
    }
    public void searchUser(){
        user = DBManager.getInstance().searchUser(user);
    }
    // for fetch user info from database
    public User getUser(){
        return user;
    }
    // for new user
    public void insertUser(User user){
        setUser(user);
        user.setSalt(Random());
        user.setH2(SHA_256(user.getSalt()+user.getH1()));
        DBManager.getInstance().insertUser(user);
    }
    // admin for test
    public void insertAdmin(User user,String pass){
        setUser(user);
        user.setSalt(Random());
        user.setH2(SHA_256(user.getSalt()+SHA_256(user.getUsername()+pass)));
        DBManager.getInstance().insertUser(user);
    }

    public boolean isAuthorized(HashMap<String,String> requestMap){
        if(isAuthorized){
            return true;
        }
        
        if(requestMap.get("Auth") == null)
            return false;
        String[] property = requestMap.get("Auth").split(SP);
        if(property.length>1){
            user.setUsername(property[0]);
            user.setH1(property[1]);
            searchUser();
            isAuthorized = checkValidate();
        }
        return isAuthorized;
    }

    public String toString(){
        return user.getUsername()+"\n"+user.getH1()+"\n"+user.getSalt()+"\n"+user.getH2();
    }
}
