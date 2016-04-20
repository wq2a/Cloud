package cloud.client;
import java.util.HashMap;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

import javax.swing.JTree;

import cloud.client.FileTreeCellRenderer;

public class MainActivity extends Base {

    private JPanel main;
    private JLabel log;

    private JButton upload,logout;
    private GridLayout gl;
//    private JPanel treepanel;
    private Tree treepanel;
    private void setLayout(){   
        setTitle("Main Program");
        
        main = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        log = new JLabel("");
        upload = new JButton("upload");
        logout = new JButton("logout");
        treepanel = new Tree();
            //treepanel.setCellRenderer(new FileTreeCellRenderer());
        
        c.insets = new Insets(0, 0, 0, 0);
//        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
//        treepanel.setBorder(Config.BLACKBORDER);        
        main.add(treepanel, c);
        
        JPanel buttonpanel = new JPanel(new GridLayout(1,4));
        buttonpanel.add(Box.createRigidArea(new Dimension(1,0)));
        buttonpanel.add(Box.createRigidArea(new Dimension(1,0)));

        buttonpanel.add(upload);
        buttonpanel.add(logout);
 //       buttonpanel.setBorder(Config.BLACKBORDER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridheight = 1;
        c.gridwidth = 3;
        c.gridx = 1;
        c.gridy = 1;
        main.add(buttonpanel,c);

        JPanel logpanel = new JPanel();
        logpanel.add(log);
        c.gridx = 0;
        c.gridy = 2;
        main.add(logpanel,c);

        
//      main.add(Box.createRigidArea(new Dimension(1,0)));

        setContentPane(main);
        pack();
        setSize((int)(Config.SCREENDIM.width*0.7),(int)(Config.SCREENDIM.height*0.7));
        setLocation(Config.SCREENDIM.width/2-getSize().width/2, Config.SCREENDIM.height/2-getSize().height/2);
        setVisible(true);   
        
    }
    
    // register event lisener
    private void registerLisener(){
        addWindowListener(this);
        upload.addActionListener(this);
        logout.addActionListener(this);
    }

    // initialize the layout
    public void start(){
        setLayout();
        registerLisener();
    }

    /*
     * click event here
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "upload") {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
              File selectedFile = fileChooser.getSelectedFile();
              treepanel.AddtoTextArea(selectedFile.getName());
              treepanel.AddtoNewFile(selectedFile.getName());
            }
        } else if (e.getActionCommand() == "logout") {
            exit();
        }
    }

    

    // receive data, still process in background thread, heavy work here
    public HashMap<String,String> preReceive(int requestID,int tag,HashMap<String,String> data){
        if(requestID == Connection.GET_PATH){
            return null;
        }
        return data;
    }

    // receive data which need to display, in UI thread
    public void receive(int requestID,int tag,HashMap<String,String> data){

        if(data != null){
            System.out.println("ID:"+requestID+" Tag:"+tag+" Return Code:"+data.get("Status"));
        }
        
    }
}