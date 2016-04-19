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
        // generate auth info
        JFrame f = new JFrame();
        final JLabel label = new JLabel("");
        f.add(label);
        f.setSize(500,500);
        f.addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent event){
                Component c = (Component)event.getSource();
                Dimension dim = c.getSize();
                label.setText(dim.width+","+dim.height);
                // System.out.println(dim.width+","+dim.height);
            }
        });
        f.setVisible(true);


    	Auth auth = Auth.getInstance();

        
    	
        System.out.println("***** first socket, without auth 401");
        Connection cnn = new Connection(new Callback(){
            @Override
            public void start(Connection c){
                c.setRequestMethod("GET");
            }
            @Override
            public void response(HashMap<String,String> response){
                try{
                    Thread.sleep(5000);
                    System.out.println("1");

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run()
                        {
                            // Update UI
                            label.setText("cccccc");
                        }
                    });
                }catch(InterruptedException e){

                }                 
            }
        });
        MyExecutor.getInstance().exec(cnn);

        cnn = new Connection(new Callback(){
            @Override
            public void start(Connection c){
                c.setRequestMethod("PUT");
            }
            @Override
            public void response(HashMap<String,String> response){
                try{
                    Thread.sleep(1000);
                    System.out.println("1");
                }catch(InterruptedException e){

                }                 
            }
        });
        cnn.setRequestMethod("PUT1");
        MyExecutor.getInstance().exec(cnn);
*/

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
