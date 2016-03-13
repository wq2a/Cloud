package cloud.server;

public class User{
	private int id;
	private String username;
	private String h1;
	private String h2;
	private String firstname;
	private String lastname;
	private String email;
	private String salt;

	User(){
		id = -1;
		username = "";
		h1 = "";
		h2 = "";
		firstname = "";
		lastname = "";
		email = "";
		salt = "";
	}
	public void setUsername(String username){
		this.username = username;
	}
	public void setH1(String h1){
		this.h1 = h1;
	}
	public void setH2(String h2){
		this.h2 = h2;
	}
	public void setSalt(String salt){
		this.salt = salt;
	}
	public String getUsername(){
		return username;
	}
	public String getH1(){
		return h1;
	}
	public String getH2(){
		return h2;
	}
	public String getSalt(){
		return salt;
	}
	public String getFirstname(){
		return firstname;
	}
	public String getLastname(){
		return lastname;
	}
	public String getEmail(){
		return email;
	}
	public boolean isEmpty(){
		return getUsername().isEmpty() || 
			   getH1().isEmpty()||
			   getSalt().isEmpty()||
			   getH2().isEmpty();
	}
}