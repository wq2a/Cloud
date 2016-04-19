package cloud.client;
import java.util.HashMap;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;

public class RegisterActivity extends Base {

    /* add more Request ID here 
	   ...

    */

    // views
    private JPanel main;
    private JLabel log,id,pw;
    private JTextField username;
    private JPasswordField password,cfpassword;
    private JButton registerbtn,cancelbtn;
    private GridLayout gl, gl2;

    // init the view layout here
    private void setLayout(){
		
		gl = new GridLayout(5, 0);
		gl.setVgap(3);		

		gl2 = new GridLayout(0,2);
		gl2.setHgap(20);
		setTitle(" Create New Account ");
		main = new JPanel(gl);

        JPanel idpanel = new JPanel(gl2);
        id = new JLabel("New Username", JLabel.CENTER);
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
        
        JPanel cfpwpanel = new JPanel(gl2);
        JLabel cfpwlabel = new JLabel("Confirm Password", JLabel.CENTER);
        cfpassword = new JPasswordField("",JPasswordField.CENTER);
        cfpwpanel.add(cfpwlabel);
        cfpwpanel.add(cfpassword);
        cfpwpanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        main.add(cfpwpanel);

        registerbtn = new JButton("Register");
        cancelbtn = new JButton("Cancel");
    	registerbtn.addActionListener(this);
        cancelbtn.addActionListener(this);

        JPanel button = new JPanel(gl2);
        button.add(registerbtn);
        button.add(cancelbtn);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        main.add(button);
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));              

        JPanel logpanel = new JPanel(new GridLayout(1,1));
        log = new JLabel("log label");
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
        cancelbtn.addActionListener(this);
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
        log.setText(e.getActionCommand());
        if(e.getActionCommand() == "Register") {
        	test();

        }else if (e.getActionCommand() == "Cancel"){
        	moveTo(LoginActivity.class);
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
        if(tag == 106){
            //moveTo(LoginActivity.class);
        }
	}

    // test
    private void test() {
    	System.out.println("show test case");
    
    
    }
}