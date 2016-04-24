package cloud.client;
import java.util.HashMap;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;

public class LoginActivity extends Base {

    // views
    private JPanel main;
    private JLabel log,pw,id;
    private JTextField username;
    private JPasswordField password;
    private JButton loginbtn,registerbtn,test_loginbtn;
    private GridLayout gl,gl2;

    
    // drop-down select server
    /*public class ComboBoxes extends JPanel{ 
       public ComboBoxes(){
             super(new GridLayout(1,2));         
                String[] serverStrings = { "Server 1", "Server 2"};
                JLabel serverlabel = new JLabel("Server Name", JLabel.CENTER);
                JComboBox serverList = new JComboBox(serverStrings);
                serverList.setSelectedIndex(1);
                add(serverlabel);
                add(serverList);
       }
    }*/
    // End drop-list

    // init the view layout here
    private void setLayout(){
		gl = new GridLayout(4, 0);
        gl.setVgap(3);      
        gl2 = new GridLayout(0,2);
        gl2.setHgap(20);
        setTitle(" Cloud Login");
        main = new JPanel(gl);
        
        /*JPanel boxpanel = new JPanel();
        JPanel cbbox = new ComboBoxes();
        boxpanel.add(cbbox);
        main.add(boxpanel);*/

        JPanel idpanel = new JPanel(gl2);
        id = new JLabel("ID", JLabel.CENTER);
        username = new JTextField("",JTextField.CENTER);
        idpanel.add(id);
        idpanel.add(username);
        idpanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        main.add(idpanel);    
        
        JPanel pwpanel = new JPanel(gl2);
        JLabel pwlabel = new JLabel("Password", JLabel.CENTER);
        password = new JPasswordField("",JPasswordField.CENTER);

        pwpanel.add(pwlabel);
        pwpanel.add(password);
        pwpanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        main.add(pwpanel);

        
        //insert login, register icon
        ImageIcon login_icn = createImageIcon("resources/Login.png");
        ImageIcon register_icn = createImageIcon("resources/Register.png");
        
        loginbtn = new JButton(login_icn);
        loginbtn.setOpaque(false);
        loginbtn.setContentAreaFilled(false);
        loginbtn.setBorderPainted(false);
        loginbtn.setFocusPainted(false);

        registerbtn = new JButton(register_icn);
        registerbtn.setOpaque(false);
        registerbtn.setContentAreaFilled(false);
        registerbtn.setBorderPainted(false);
        registerbtn.setFocusPainted(false);
        //
        /* default button
        loginbtn = new JButton("Login");
        registerbtn = new JButton("Register);

        */
        JPanel button = new JPanel(gl2);
        button.add(loginbtn);
        button.add(registerbtn);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        main.add(button);
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));              

        JPanel logpanel = new JPanel(new GridLayout(1,1));
        log = new JLabel("");
        logpanel.add(log);
        logpanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));              

        main.add(logpanel);        

        setContentPane(main);
        pack();
        setSize(300,300);
        setLocation(Config.SCREENDIM.width/2-getSize().width/2, Config.SCREENDIM.height/2-getSize().height/2);
        setVisible(true);
    }

    // register event lisener
    private void registerLisener(){
    	addWindowListener(this);
    	registerbtn.addActionListener(this);
        loginbtn.addActionListener(this);
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
		if(e.getSource() == loginbtn) {
			if(username.getText().equals("")){
        		log.setText("Invalid Username!!");
        	}
        	else{
        	Auth auth = Auth.getInstance();
        	Connection cnn = new Connection();
        	// get username and password and check. 
            // username:admin password:password
        	auth.setAccount(username.getText(),String.valueOf(password.getPassword()));
        	cnn.setRequestProperty("Auth",auth.toString());
			cnn.setRequestMethod(Connection.LOGIN,"POST");
            cnn.setTag(106);
			request(cnn);
        	}
        }else if(e.getActionCommand() == "testlogin"){
            Auth auth = Auth.getInstance();
            Connection cnn = new Connection();
            // get username and password and check. 
            // username:admin password:password
            auth.setAccount("admin","password");
            cnn.setRequestProperty("Auth",auth.toString());
            cnn.setRequestMethod(Connection.LOGIN,"POST");
            cnn.setTag(106);
            request(cnn);
        }else if(e.getSource() == registerbtn){
            moveTo(RegisterActivity.class);
        }
    }

	// receive data, still process in background thread, heavy work here
	public HashMap<String,String> preReceive(int requestID,int tag,HashMap<String,String> data){
		/* heavy work here*/
        // required to return data at the end
		return data;
	}

	// receive data which need to display, in UI thread
	public void receive(int requestID,int tag,HashMap<String,String> data){

		if(data != null){
			log.setText("ID:"+requestID+" Tag:"+tag+" Return Code:"+data.get("Status"));
            System.out.println("ID:"+requestID+" Tag:"+tag+" Return Code:"+data.get("Status"));

            if(tag == 106 && data.get("Status").equals(Connection.OK)){
                moveTo(MainActivity.class);
            }

            if(requestID == Connection.REGISTER && data.get("Status").equals(Connection.OK)){
                moveTo(TestActivity.class);
            }
		}
        
	}
	
	
	 /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = LoginActivity.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
	
	
	
}