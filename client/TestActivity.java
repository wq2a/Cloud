package cloud.client;

import java.util.HashMap;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;

/**
 *  Test
 *
 */
public class TestActivity extends Base {

    // views
    private JPanel main;
    private JLabel log;
    private JButton upload,logout,getpath;

    private GridLayout gl;

    // init the view layout here
    private void setLayout() {
		
		gl = new GridLayout(10, 0);
		gl.setVgap(6);
		
		setTitle("This activity is used for test");
        
		main = new JPanel(gl);
        log = new JLabel("");
        upload = new JButton("upload");
        logout = new JButton("logout");
        getpath = new JButton("getpath");

        log.setBorder(Config.REDBORDER);
        upload.setBorder(Config.BLACKBORDER);
        logout.setBorder(Config.BLACKBORDER);
        getpath.setBorder(Config.BLACKBORDER);
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        main.add(Box.createRigidArea(new Dimension(1,0)));
        main.add(log);        
        main.add(upload);
        main.add(logout);
        main.add(getpath);
        
        setContentPane(main);
        pack();
        setSize((int)(Config.SCREENDIM.width*0.7),(int)(Config.SCREENDIM.height*0.7));
        setLocation(Config.SCREENDIM.width/2-getSize().width/2, Config.SCREENDIM.height/2-getSize().height/2);
        setVisible(true);
    }

    // register event lisener
    private void registerLisener() {
    	addWindowListener(this);
    	upload.addActionListener(this);
        logout.addActionListener(this);
        getpath.addActionListener(this);
    }

	// initialize the layout
	public void start() {
		setLayout();
		registerLisener();
	}

	/*
	 * click event here
	 */
	public void actionPerformed(ActionEvent e) {
        log.setText(e.getActionCommand());
        if(e.getActionCommand() == "upload") {
        	test();
        } else if (e.getActionCommand() == "logout"){
            exit();
        } else if (e.getActionCommand() == "getpath"){
            // get path example
            Connection cnn = new Connection();
            cnn.setRequestMethod(Connection.GET_PATH,"GET");
            cnn.setRequestProperty("tt","root");
            request(cnn);
        }
    }

	// receive data, still process in background thread, heavy work here
	public HashMap<String,String> preReceive(int requestID,int tag,HashMap<String,String> data){
		
		return data;
	}

	// receive data which need to display, in UI thread
	public void receive(int requestID,int tag,HashMap<String,String> data){

		if(data != null){
            System.out.println("ID:"+requestID+" Tag:"+tag+" Return Code:"+data.get("Status"));
            System.out.println("Data:"+data.get("data"));
		}
        if(tag == 106){
            //moveTo(LoginActivity.class);
        }
	}

    // test
    private void test() {
        FileManager fm = new FileManager();
        fm.mk("wo/");
        fm.mk("wo/data.txt");

        Connection cnn = new Connection();
        cnn.setRequestMethod(Connection.UPLOAD_FILE,"PUT");
        cnn.setRequestProperty("Auth",Auth.getInstance().toString());
        cnn.setRequestProperty("Connection","close");
        cnn.setRequestProperty("Path","wo/data.txt");
        request(cnn);
    }
}