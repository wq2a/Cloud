package cloud.client;
import java.util.HashMap;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;

public class MyClient extends Base {

	// request id
    private final static int GET_PATH = 0;
    private final static int PUT_FILE = 1;
    private final static int POST = 2;
    /* add more Request ID here 
	   ...

    */

    // views
    private Dimension screenDim;
    private JFrame f;
    private JPanel main;
    private JLabel log;
    private JTextField username,password;
    private JButton loginbtn,registerbtn;
    private CompoundBorder blackBorder;
    private CompoundBorder redBorder;
    private GridLayout gl;

    // init the view layout here
    private void setLayout(){
    	f = new JFrame();
		screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		gl = new GridLayout(10, 0);
		gl.setVgap(6);
		
		f.setTitle(" Cloud Login");
		f.setSize(300,500);
		f.setLocation(screenDim.width/2-f.getSize().width/2, screenDim.height/2-f.getSize().height/2);

		main = new JPanel(gl);
        log = new JLabel("");
        username = new JTextField("",JTextField.CENTER);
        password = new JTextField("",JTextField.CENTER);
        loginbtn = new JButton("login");
        registerbtn = new JButton("register");

        blackBorder = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black), 
        	BorderFactory.createEmptyBorder(10, 10, 10, 10));
        redBorder = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.red), 
        	BorderFactory.createEmptyBorder(10, 10, 10, 10));
        log.setBorder(redBorder);
      	username.setBorder(blackBorder);
      	password.setBorder(blackBorder);
        loginbtn.setBorder(blackBorder);
        registerbtn.setBorder(blackBorder);
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        main.add(Box.createRigidArea(new Dimension(1,0)));
        main.add(log);        
        main.add(username);
        main.add(password);
        main.add(loginbtn);
		main.add(registerbtn);

        main.addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent event){
                Component c = (Component)event.getSource();
                Dimension dim = c.getSize();
                //label.setText(dim.width+","+dim.height);
                // System.out.println(dim.width+","+dim.height);
            }
        });

    	f.setContentPane(main);
        f.setVisible(true);
    }

    // register event lisener
    private void registerLisener(){
    	f.addWindowListener(this);
    	registerbtn.addActionListener(this);
        loginbtn.addActionListener(this);
    }

	// initialize the layout
	public void start(){
		setLayout();
		registerLisener();

		/*
		Connection cnn = new Connection();
		cnn.setRequestMethod(GET_PATH,"GET");
		request(cnn);
		for(int index=0;index<10;index++){
			cnn = new Connection();
			cnn.setRequestMethod(index,"PUT");
			request(cnn);
		}*/
	}

	/*
	 * click event here
	 */
	public void actionPerformed(ActionEvent e) {
        log.setText(e.getActionCommand());
        if(e.getActionCommand() == "login") {
        	Auth auth = Auth.getInstance();
        	Connection cnn = new Connection();
        	// get username and password and check
        	// admin, password
        	auth.setAccount(username.getText(),password.getText());
        	cnn.setRequestProperty("Auth",auth.toString());
			cnn.setRequestMethod(POST,"POST");
			request(cnn);
        }
    }

	// receive data still process in background thread, heavy work here
	public HashMap<String,String> preReceive(int requestID,int tag,HashMap<String,String> data){
		if(requestID == GET_PATH){
			return null;
		}
		return data;
	}

	// receive data which need to display, in UI thread
	public void receive(int requestID,int tag,HashMap<String,String> data){
		if(data != null){
			log.setText("ID:"+requestID+" Return Code:"+data.get("Status"));
		}
	}
}