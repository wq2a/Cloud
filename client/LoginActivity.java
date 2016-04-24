package cloud.client;
import java.util.HashMap;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;

public class LoginActivity extends Base {

    // views
    private JPanel main;
    private JLabel log,pw,id;
    private JTextField username;
    private JPasswordField password;
    private JButton loginbtn,registerbtn;

    private String[] server = {"Server 1", "Server 2"};
    private JPanel serverpanel;
    private JLabel serverlabel;
    private JComboBox<String> serverList;
    private GridLayout gl,gl2;

    ImageIcon loginImage = createImageIcon("resources/login.png");
    ImageIcon registerImage = createImageIcon("resources/register.png");

    // init the view layout here
    private void setLayout(){
		gl = new GridLayout(5, 0);
        gl.setVgap(3);      
        gl2 = new GridLayout(0,2);
        gl2.setHgap(20);
        setTitle(" Cloud Login");
        main = new JPanel(gl);

        serverpanel = new JPanel(new GridLayout(1,2));
        serverlabel = new JLabel("Server Name", JLabel.CENTER);
        serverList = new JComboBox<>(server);
        serverList.setSelectedIndex(0);
        serverpanel.add(serverlabel);
        serverpanel.add(serverList);
        main.add(serverpanel);

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
        
        loginbtn = new JButton("login");
        //loginbtn.setIcon(loginImage);
        loginbtn.setActionCommand("login");
        //loginbtn.setBorder(BorderFactory.createEmptyBorder());
        //loginbtn.setText("");

        registerbtn = new JButton("register");
        //registerbtn.setIcon(registerImage);
        registerbtn.setActionCommand("register");
        //registerbtn.setBorder(BorderFactory.createEmptyBorder());
        //registerbtn.setText("");

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
        serverList.addActionListener(this);
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
        //log.setText(e.getActionCommand());
        if(e.getActionCommand() == "login") {
            if(username.getText().isEmpty() || String.valueOf(password.getPassword()).isEmpty()){
                log.setText(Utils.warning("Sorry, we don't recognize this account."));
            }else{
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
        } else if(e.getActionCommand() == "register"){
            moveTo(RegisterActivity.class);
        } else if("comboBoxChanged".equals(e.getActionCommand())){
            String serverName = (String)((JComboBox)(e.getSource())).getSelectedItem();
            if(serverName.equals("Server 1")){
                ClientSocket.hostName = "52.87.188.170";
            }else{
                ClientSocket.hostName = "54.165.222.100";
            }
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
            System.out.println("ID:"+requestID+" Tag:"+tag+" Return Code:"+data.get("Status"));
            if(tag == 106 && data.get("Status").equals(Connection.OK)){
                moveTo(MainActivity.class);
            }else if(!data.get("Status").equals(Connection.OK)){
                log.setText(Utils.warning("Wrong username or password."));
            }
		}
        
	}

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = LoginActivity.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            //System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}