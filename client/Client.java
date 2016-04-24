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