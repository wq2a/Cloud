//seashore
package cloud.client;

import java.math.*;
import java.security.MessageDigest;
import java.io.IOException;

public class Auth {

    final static String SP = " ";
    private static Auth instance = new Auth();
    private String username;
    private String h1;

    private Auth(){
        
    }

    private String SHA_256(String str){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes("UTF-8"));
            return String.format("%064x", new BigInteger(1, md.digest()));
        } catch(Exception e){
            return null;
        }
    }

    public static Auth getInstance(){
        return instance;
    }

    public void setAccount(String username,String password){
        reset();
        this.username = username;
        this.h1 = SHA_256(username+password);
    }

    public void reset(){
        this.username = "";
        this.h1 = "";
    }

    public String getUsername(){
        return username;
    }

    public String toString(){
        return username+SP+h1;
    }
}
