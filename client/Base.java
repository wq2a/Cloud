package cloud.client;
import java.util.HashMap;
import java.util.Vector;
import java.lang.reflect.Method;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import cloud.client.FileManager;
import cloud.client.MyExecutor;
import cloud.client.Receiver;
import cloud.client.ClientSocket;
import cloud.client.Utils;

public abstract class Base extends JFrame implements ReceiverCallback,WindowListener,ActionListener{
    public static Vector<JFrame> layouts = new Vector<JFrame>();
    public abstract void start();
	public abstract HashMap<String,String> preReceive(int requestID,int tag,HashMap<String,String> data);
	public abstract void receive(int requestID,int tag,HashMap<String,String> data);

    Base(){
        layouts.add(this);
    }

    
    public void moveTo(Class<?> c){
        //Utils.wait(1);
        this.setVisible(false);
        try {
            Object obj = c.newInstance();
            Method method = c.getMethod("start", (Class<?>[]) null);
            method.invoke(obj, (Object[]) null);
        } catch (InstantiationException ex) {
            System.err.println(ex);
        } catch (IllegalAccessException ex) {
            System.err.println(ex);
        } catch(IllegalArgumentException ex){

        } catch(Exception ex){
            ex.printStackTrace();
        }
        //MyExecutor.getInstance().shutdown();
        this.dispose();
        System.out.println("moveTo");
    }

    public void exit(){
        //this.dispose();
        ClientSocket.getInstance().disconnect();
        MyExecutor.getInstance().shutdown();
        System.exit(0);
        /*
        for(JFrame j : layouts){
            j.dispose();
        }
        ClientSocket.getInstance().disconnect();
        System.exit(0);
        */
    }



	public void request(Connection connection){
		Connection cnn = new Connection(this,connection, new Callback(){
            @Override
            public void start(Connection c){
                //c.setRequestMethod("GET");
            }
            @Override
            public void response(int requestID,int tag,ReceiverCallback rc,HashMap<String,String> response){
                Receiver re = new Receiver(requestID,tag,response,rc);
                re.pre();
                SwingUtilities.invokeLater(re);               
            }
        });
        MyExecutor.getInstance().exec(cnn);
	}



	public void windowClosing(WindowEvent e) {
        //dispose();
        //exit();
    }

    public void windowOpened(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {
        //exit();
    }
    public abstract void actionPerformed(ActionEvent e);

}