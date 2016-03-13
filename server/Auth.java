package cloud.server;

import java.math.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.io.IOException;

public class Auth {

    private String username;
    private String h1;
    private String h2;
    private String salt;
    private boolean isAuthorized;

    private Auth(){
        username = "";
        h1 = "";
        h2 = "";
        salt = "";
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
        if(this.username.isEmpty()||this.h1.isEmpty()||this.salt.isEmpty())
            return false;
        this.h2 = SHA_256(this.salt+h1);
        System.out.println(toString());
        return true;
    }

    public void setAccount(String username,String h1){
        this.username = username;
        this.h1 = h1;
        this.salt = Random();
    }

    public boolean isAuthorized(){
        if(isAuthorized)
            return true;
        return checkValidate();
    }

    public String toString(){
        return username+"\n"+h1+"\n"+salt+"\n"+h2;
    }
}
