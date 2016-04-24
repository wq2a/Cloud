package cloud.client;
import java.util.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.tree.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;

import javax.swing.JTree;
import cloud.client.FileTreeCellRenderer;

public class MainActivity extends Base implements TreeSelectionListener, MouseListener{

    private JPanel main;
    private JLabel log;

    private JButton upload,save,editfile_btn,newfile_btn,newdir_btn,delfile_btn,logout,refreshbtn;
    private GridLayout gl;

    private JPanel myPanel;
    private JTextArea textArea;
    private JTree tree;
    private DefaultTreeModel treeModel ;
    private FileNode fo;
    private FileNode ff;
    private ArrayList<String> nodePaths;
    FileNode foinit;
    private String currentPath = "";
    private boolean file_get_locker = false;

    ImageIcon saveImage = createImageIcon("resources/save.png");
    ImageIcon logoutImage = createImageIcon("resources/close.png");
    ImageIcon deleteImage = createImageIcon("resources/delete.png");
    ImageIcon editImage = createImageIcon("resources/edit.png");
    
    

    private void setLayout(){   
        nodePaths = new ArrayList<String>();
        setTitle("Main Program");
        //setUndecorated(true);
        

        main = new JPanel(new BorderLayout());
        
        log = new JLabel("");
        upload = new JButton("Upload");
        upload.setActionCommand("upload");

        save = new JButton("Save");
        save.setActionCommand("save");
        
        logout = new JButton("logout");
        //logout.setIcon(logoutImage);
        logout.setActionCommand("logout");
        //logout.setBorder(BorderFactory.createEmptyBorder());
        //logout.setText("");
        
        editfile_btn = new JButton("Edit");
        editfile_btn.setActionCommand("editfile_btn");

        newfile_btn = new JButton("New File");
        newfile_btn.setActionCommand("newfile_btn");

        newdir_btn = new JButton("New Dir");
        newdir_btn.setActionCommand("newdir_btn");

        delfile_btn = new JButton("Delete");
        //delfile_btn.setIcon(deleteImage);
        delfile_btn.setActionCommand("delfile_btn");
        //delfile_btn.setBorder(BorderFactory.createEmptyBorder());
        //delfile_btn.setText("");

        refreshbtn = new JButton("Refresh");
        refreshbtn.setActionCommand("refreshbtn");
        
        myPanel = new JPanel();
        fo = new FileNode("root");
        treeModel = new DefaultTreeModel(fo);

        // init
        foinit = new FileNode("public");
        foinit.setPath("public/");

        nodePaths.add("public/");
        fo.add(foinit);
        fo.add("public",foinit);

        tree = new JTree(treeModel);
        tree.expandRow(0);
        tree.setRootVisible(false);
        tree.setCellRenderer(new FileTreeCellRenderer());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JScrollPane treePanel = new JScrollPane(tree);

        //treePanel.setPreferredSize(new Dimension(200, (int)(Config.SCREENDIM.height*0.6)));
        treePanel.setPreferredSize(new Dimension(200, (int)(Config.SCREENDIM.height*0.6)));
        treePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        myPanel.add(treePanel); 

        textArea = new JTextArea(30,35);
        //textArea.setPreferredSize(new Dimension(200, (int)(Config.SCREENDIM.height*0.6)));
        textArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        textArea.setEditable(false);

        textArea.append("");

        main.add(myPanel,BorderLayout.WEST);//BorderLayout.LINE_START
        main.add(new JScrollPane(textArea),BorderLayout.CENTER); 

        JPanel buttonpanel = new JPanel(new GridLayout(0, 10));
        buttonpanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        //buttonpanel.add(Box.createRigidArea(new Dimension(1,0)));
        //buttonpanel.add(Box.createRigidArea(new Dimension(1,0)));
        //JPanel buttonpanelsub = new JPanel(new GridLayout(0, 2));
        buttonpanel.add(delfile_btn);
        //buttonpanel.add(buttonpanelsub);

        //buttonpanel.add(Box.createRigidArea(new Dimension(1,0)));
        //buttonpanel.add(Box.createRigidArea(new Dimension(1,0)));
        //buttonpanel.add(Box.createRigidArea(new Dimension(1,0)));
        buttonpanel.add(newfile_btn);
        buttonpanel.add(newdir_btn);
        buttonpanel.add(refreshbtn);
        buttonpanel.add(upload);
        
        
        //buttonpanel.add(logout);
        
        buttonpanel.add(editfile_btn);
        buttonpanel.add(save);
        
 
        main.add(buttonpanel,BorderLayout.SOUTH);//BorderLayout.PAGE_END

        JPanel logpanel = new JPanel();
        
        logpanel.setLayout(new GridLayout(0, 5));
        //logpanel.setLayout(new BorderLayout());
        logpanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        //logpanel.add(newfile_btn,BorderLayout.WEST);
        //logpanel.add(newdir_btn,BorderLayout.CENTER);
        logpanel.add(Box.createRigidArea(new Dimension(1,0)));
        logpanel.add(Box.createRigidArea(new Dimension(1,0)));
        logpanel.add(log);
        logpanel.add(Box.createRigidArea(new Dimension(1,0)));
        JPanel logpanelsub = new JPanel(new GridLayout(0, 2));
        //logpanelsub.add(Box.createRigidArea(new Dimension(1,0)));
        //logpanelsub.add(Box.createRigidArea(new Dimension(1,0)));
        logpanelsub.add(Box.createRigidArea(new Dimension(1,0)));
        logpanelsub.add(logout);
        logpanel.add(logpanelsub);
        //logpanel.add(log,BorderLayout.CENTER);
        //logpanel.add(logout,BorderLayout.EAST);

        main.add(logpanel,BorderLayout.PAGE_START);

        setContentPane(main);
        pack();
        setSize((int)(Config.SCREENDIM.width*0.7),(int)(Config.SCREENDIM.height*0.7));
        setLocation(Config.SCREENDIM.width/2-getSize().width/2, Config.SCREENDIM.height/2-getSize().height/2);
        setVisible(true); 

        Connection cnn = new Connection();
        cnn.setRequestMethod(Connection.GET_PATH,"GET");
        request(cnn); 
    }
    
    // register event lisener
    private void registerLisener(){
        addWindowListener(this);
        upload.addActionListener(this);
        save.addActionListener(this);
        logout.addActionListener(this);
        newdir_btn.addActionListener(this);
        
        editfile_btn.addActionListener(this);
        newfile_btn.addActionListener(this);
        delfile_btn.addActionListener(this);
        refreshbtn.addActionListener(this);
        tree.addTreeSelectionListener(this);
        tree.addMouseListener(this);
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
                Connection cnn = new Connection();
                /* upload local file to server*/
                cnn.setRequestMethod(Connection.UPLOAD_FILE,"PUT");
                cnn.setRequestProperty("Auth",Auth.getInstance().toString());
                cnn.setRequestProperty("LocPath",fileChooser.getSelectedFile().getAbsolutePath());
                cnn.setRequestProperty("Path",currentPath.substring(0,currentPath.lastIndexOf("/"))
                    +"/"+fileChooser.getSelectedFile().getName());
                request(cnn);
              //treepanel.AddtoTextArea(selectedFile.getName());
              //treepanel.AddtoNewFile(selectedFile.getName());
            }

        } else if (e.getActionCommand() == "logout") {
            JFrame frame = new JFrame();
            int n = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sureadmin quit the program?",
                    "Logout Confirm",
                    JOptionPane.OK_CANCEL_OPTION);

            if(n==0){
                exit();
            }

        } else if (e.getActionCommand() == "save"){
            if(textArea.isEditable()){
                Connection cnn = new Connection();
                cnn.setRequestMethod(Connection.UPLOAD_FILE,"PUT");
                cnn.setRequestProperty("Auth",Auth.getInstance().toString());
                cnn.setRequestProperty("Content",textArea.getText());
                cnn.setRequestProperty("Path",currentPath);
                request(cnn);
                textArea.setEditable(false);
            }
            
        } else if (e.getActionCommand() == "newfile_btn"){
            if(currentPath.isEmpty()){
                log.setText(Utils.warning("Please select a directory."));
            }else{
                JFrame frame = new JFrame();
                String result = JOptionPane.showInputDialog(frame, "Enter New File Name:");  
                if(result!=null&&!result.isEmpty()){
                    //Random rand = new Random();
                    //int  n = rand.nextInt(500) + 1;
                    Connection cnn = new Connection();
                    cnn.setRequestMethod(Connection.UPLOAD_FILE,"PUT");
                    cnn.setRequestProperty("Auth",Auth.getInstance().toString());
                    cnn.setRequestProperty("Path",currentPath.substring(0,currentPath.lastIndexOf("/"))+"/"+result);
                    //cnn.setRequestProperty("Path",currentPath.substring(0,currentPath.lastIndexOf("/"))+"/"+n+"new_file.txt");
                    request(cnn);
                }
                
            }
        } else if (e.getActionCommand() == "newdir_btn"){
            if(currentPath.isEmpty()){
                log.setText(Utils.warning("Please select a directory."));
            }else{
                JFrame frame = new JFrame();
                String result = JOptionPane.showInputDialog(frame, "Enter New Directory Name:"); 
                if(result!=null&&!result.isEmpty()){
                    
                    //Random rand = new Random();
                    //int  n = rand.nextInt(500) + 1;
                    Connection cnn = new Connection();
                    cnn.setRequestMethod(Connection.UPLOAD_FILE,"PUT");
                    cnn.setRequestProperty("Auth",Auth.getInstance().toString());
                    cnn.setRequestProperty("Path",currentPath.substring(0,currentPath.lastIndexOf("/"))+"/"+result+"/");
                    //cnn.setRequestProperty("Path",currentPath.substring(0,currentPath.lastIndexOf("/"))+"/"+n+"new_dir/");
                    request(cnn);
                    log.setText(Utils.warning(""));
                    
                }
                
            }
           
        } else if (e.getActionCommand() == "delfile_btn"){
            if(currentPath.isEmpty()){
                log.setText(Utils.warning("Please select a file or a directory."));
            }else{
                JFrame frame = new JFrame();
                int n = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure delete "+currentPath+"?",
                    "Delete Confirm",
                    JOptionPane.OK_CANCEL_OPTION);
                if(n==0){
                    Connection cnn = new Connection();
                    cnn.setRequestMethod(Connection.DELETE_PATH,"DELETE");
                    cnn.setRequestProperty("Auth",Auth.getInstance().toString());
                    cnn.setRequestProperty("Path",currentPath);
                    request(cnn);
                }
            }
        } 
        else if (e.getActionCommand() == "editfile_btn"){
            // get lock
            if (currentPath != null) {
                if(!currentPath.isEmpty() && !(currentPath.substring(currentPath.length() - 1)).equals("/")){
                    Connection cnn = new Connection();
                    cnn.setRequestMethod(Connection.LOCK,"POST");
                    cnn.setRequestProperty("EditMode","");
                    System.out.println("Send lock");
                    cnn.setRequestProperty("Path",currentPath);
                    cnn.setRequestProperty("Auth",Auth.getInstance().toString());
                    request(cnn);
                    //textArea.setEditable(true);
                }else{
                    log.setText(Utils.warning("Please select a file."));
                }
            }
        } else if (e.getActionCommand() == "refreshbtn"){
            Connection cnn = new Connection();
            cnn.setRequestMethod(Connection.GET_PATH,"GET");
            cnn.setRequestProperty("Path","");
            cnn.setRequestProperty("Auth",Auth.getInstance().toString());
            request(cnn);
        }
        
    }

    public void valueChanged(TreeSelectionEvent e) {
        FileNode node = (FileNode) tree.getLastSelectedPathComponent();         
        if (node == null) {
            currentPath = "";
            return;
        } else {
            currentPath = node.getP();
        }

        if (node.isLeaf() && node.getFileType() == 1){
            //textArea.setText(node.getP());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
       // || e.getClickCount() == 2
        if(e.getClickCount() == 1){
            TreePath path = tree.getPathForLocation(e.getX(), e.getY());
            if (path != null) {
                //System.out.println(path.getLastPathComponent());
                FileNode node = (FileNode) tree.getLastSelectedPathComponent();
                if (node == null) {
                    currentPath = "";
                    return;
                } else {

                    currentPath = node.getP();
                
                    if(node.getFileType() == 1){
                        if(!file_get_locker){
                            file_get_locker = true;

                            textArea.setEditable(false);
                            Connection cnn = new Connection();
                            cnn.setRequestMethod(Connection.GET_FILE,"GET");
                            cnn.setRequestProperty("File","");
                            cnn.setRequestProperty("Path",currentPath);
                            cnn.setRequestProperty("Auth",Auth.getInstance().toString());
                            request(cnn);
                        }
                    }else{
                        textArea.setText("");
                    }
                    textArea.setEditable(false);
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    // receive data, still process in background thread, heavy work here
    public HashMap<String,String> preReceive(int requestID,int tag,HashMap<String,String> data){

        if(data != null){

            StringBuffer pp = new StringBuffer();
            if(data.get("private") != null){
                pp.append(data.get("private"));
            }
            if(data.get("public") != null){
                pp.append(";").append(data.get("public"));
            }
            data.put("paths",pp.toString());

        }
        return data;
    }
    private boolean ii = false;
    // receive data which need to display, in UI thread
    public void receive(int requestID,int tag,HashMap<String,String> data){
        file_get_locker = false;
        if(data != null){
            System.out.println("ID:"+requestID+" Tag:"+tag+" Return Code:"+data.get("Status"));
            if(data.get("Status").equals("200")){
                log.setText(Utils.info("Return Code: "+data.get("Status")));
            }else{
                log.setText(Utils.warning("Return Code: "+data.get("Status")));
            }
            
            if(requestID == Connection.GET_FILE){
                
                if(data.get("Content") != null){
                    log.setText(Utils.info("== Standard Mode =="));
                    textArea.setEditable(false);
                    textArea.setText(data.get("Content"));
                }
                
            }else if(requestID == Connection.LOCK){
                //System.out.println("Send lock"+data.get("Status"));
                if(data.get("Status").equals("423")){
                    // file locked
                    log.setText(Utils.warning("== File Locked =="));
                    textArea.setEditable(false);
                }else{
                    log.setText(Utils.info("== Edit Mode =="));
                    textArea.setEditable(true);
                }
            }else if(requestID == Connection.DELETE_PATH){
                if(data.get("Status").equals("200")){
                    textArea.setText("");
                }
            }

            if(data.get("paths")!=null && !((data.get("paths")).isEmpty())){

                //System.out.println(data.get("paths"));
                String[] ps = (data.get("paths")).split(";");
                ArrayList<String> nodePathsTemp = new ArrayList<String>();

                for(int i = 0 ; i<ps.length ; i++){
                    nodePathsTemp.add(ps[i]);
                }

                ArrayList<String> toDel = new ArrayList<String>();
                //del
                for(String temp:nodePaths){
                    if(!(nodePathsTemp.contains(temp))){
                        String[] nodeName = temp.split("/");
                        FileNode parentNode = null;
                        FileNode delNode = fo;
                        String delName = "";
                        
                        for(String name:nodeName){

                            if(delNode != null){
                                parentNode = delNode;
                                delNode = delNode.get(name);
                            }
                            delName = name;
                        }

                        if(delNode != null && delNode != fo){
                            treeModel.removeNodeFromParent(delNode);
                            toDel.add(temp);
                            //nodePaths.remove(temp);
                        }

                        if(parentNode != null && !delName.isEmpty()){
                            parentNode.del(delName);
                        }
                    }
                }

                for(String temp:toDel){
                    nodePaths.remove(temp);
                }

                // add
                for(String temp:nodePathsTemp){
                    if(!(nodePaths.contains(temp))){
                        String[] nodeName = temp.split("/");
                        FileNode parent = fo;
                        for(String name:nodeName){
                            FileNode ff = parent.get(name);
                            if(ff == null){
                                ff = new FileNode(name);
                                ff.setPath(temp);
                                treeModel.insertNodeInto(ff,parent,0);
                                parent.add(name,ff);
                            }
                            parent = ff;
                        }
                        nodePaths.add(temp);
                    }
                }
            }
            
        }
        
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = MainActivity.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            //System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}