package cloud.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.lang.*;
import java.security.MessageDigest;
import java.io.IOException;
import cloud.client.Auth;

public class Client extends JPanel{

    public static void main(String[] args){
    	// generate auth info
    	Auth auth = Auth.getInstance();
    	
        System.out.println("***** first socket, without auth 401");

    	Connection cnn = new Connection();
    	cnn.setRequestMethod("GET");
    	cnn.setRequestProperty("User-agent","xxxx/1.0");
    	System.out.println(cnn.connect());

    	System.out.println("***** post with auth but wrong password 401");

    	cnn = new Connection();
    	cnn.setRequestMethod("POST");
    	cnn.setRequestProperty("User-agent1","xxxx/1.0");
        auth.setAccount("admin","wrongpassword");
    	cnn.setRequestProperty("Auth",auth.toString());
    	System.out.println(cnn.connect());

        System.out.println("***** bad post request but login with right password 400");

        cnn = new Connection();
        cnn.setRequestMethod("POSTo");
        cnn.setRequestProperty("User-agent1","xxxx/1.0");
        auth.setAccount("admin","password");
        cnn.setRequestProperty("Auth",auth.toString());
        System.out.println(cnn.connect());

        System.out.println("***** close socket 1 already login 200");

        cnn = new Connection();
        cnn.setRequestMethod("POST");
        cnn.setRequestProperty("User-agent1","xxxx/1.0");
        cnn.setRequestProperty("Connection","close");
        System.out.println(cnn.connect());

        System.out.println("***** reconnect socket and login with right password 200");

        cnn = new Connection();
        cnn.setRequestMethod("POST");
        cnn.setRequestProperty("User-agent1","xxxx/1.0");
        cnn.setRequestProperty("Auth",auth.toString());
        cnn.setRequestProperty("Path","/dir/l/");
        System.out.println(cnn.connect());

        System.out.println("***** PUT already login 200");

        cnn = new Connection();
        cnn.setRequestMethod("PUT");
        cnn.setRequestProperty("User-agent1","xxxx/1.0");
        cnn.setRequestProperty("Path","/dir/l/file.txt");
        System.out.println(cnn.connect());

        System.out.println("***** close socket 2");
    	ClientSocket.getInstance().disconnect();

        System.out.println("***** reconnect");

        cnn = new Connection();
        cnn.setRequestMethod("PUT");
        cnn.setRequestProperty("User-agent1","xxxx/1.0");
        cnn.setRequestProperty("Path","/dir/l/file.txt");
        System.out.println(cnn.connect());

        System.out.println("***** close socket 1");

        cnn = new Connection();
        cnn.setRequestMethod("POST");
        cnn.setRequestProperty("User-agent1","xxxx/1.0");
        cnn.setRequestProperty("Connection","close");
        //System.out.println(cnn.connect());

        JFrame f = new JFrame();
        final JLabel label = new JLabel(cnn.connect().toString());
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
    }
}
