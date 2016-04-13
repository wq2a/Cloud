package cloud.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.lang.*;
import java.security.MessageDigest;
import java.io.IOException;
import java.util.HashMap;


import cloud.client.Auth;
import cloud.client.FileManager;
import cloud.client.MyExecutor;
import cloud.client.Utils;


public class Client {

    public static void main(String[] args){
        Config.getInstance();
        LoginActivity client = new LoginActivity();
        client.start();
    }
}

/*

        FileManager fm = new FileManager();
        fm.mk("wo/");
        fm.mk("wo/data.txt");

        System.out.println("***** open socket 2");

        cnn = new Connection();
        cnn.setRequestMethod("PUT");
        cnn.setRequestProperty("Length","123456789");
        cnn.setRequestProperty("Path","wo/data.txt");
        System.out.println(cnn.connect());

        System.out.println("***** open socket 3");

        cnn = new Connection();
        cnn.setRequestMethod("PUT");
        System.out.println(cnn.connect());

        System.out.println("***** reconnect socket and login with right password 200");

        cnn = new Connection();
        cnn.setRequestMethod("POST");
        cnn.setRequestProperty("Auth",auth.toString());
        cnn.setRequestProperty("Path","/dir/l/");
        System.out.println(cnn.connect());

        System.out.println("***** PUT already login 200");

        cnn = new Connection();
        cnn.setRequestMethod("PUT");
        cnn.setRequestProperty("Path","/dir/l/file.txt");
        System.out.println(cnn.connect());

        //System.out.println("***** close socket 2");
    	//ClientSocket.getInstance().disconnect();

        System.out.println("***** reconnect");

        cnn = new Connection();
        cnn.setRequestMethod("PUT");
        cnn.setRequestProperty("Path","/dir/l/file.txt");
        System.out.println(cnn.connect());

        System.out.println("***** close socket 1");
        */
