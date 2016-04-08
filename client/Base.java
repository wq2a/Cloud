package cloud.client;
import java.util.HashMap;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import cloud.client.Auth;
import cloud.client.FileManager;
import cloud.client.MyExecutor;
import cloud.client.Receiver;
import cloud.client.ClientSocket;

public abstract class Base extends JPanel implements ReceiverCallback,WindowListener,ActionListener{
	public abstract HashMap<String,String> preReceive(int requestID,int tag,HashMap<String,String> data);
	public abstract void receive(int requestID,int tag,HashMap<String,String> data);

	public void request(Connection connection){
		Connection cnn = new Connection(this,connection, new Callback(){
            @Override
            public void start(Connection c){
                //c.setRequestMethod("GET");
            }
            @Override
            public void response(int requestID,int tag,ReceiverCallback rc,HashMap<String,String> response){
                try{
                    Thread.sleep(requestID%2*1000);
                    Receiver re = new Receiver(requestID,tag,response,rc);
                    re.pre();
                    SwingUtilities.invokeLater(re);
                } catch(InterruptedException e){

                }                
            }
        });
        MyExecutor.getInstance().exec(cnn);
	}

	public void windowClosing(WindowEvent e) {
        //dispose();
        System.out.println("windowClosing");
        ClientSocket.getInstance().disconnect();
    	System.exit(0);
    }

    public void windowOpened(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {
    	System.out.println("windowClosed");
    }
    public abstract void actionPerformed(ActionEvent e);

}