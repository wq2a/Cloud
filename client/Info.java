package cloud.client;

public class Info{
	public static Info instance;
	private String os;
	private String language;
	private Info(){
		os = System.getProperty("os.name");
		language = System.getProperty("user.language");
	}

	public static Info getInstance(){
		if(instance == null){
			instance = new Info();
		}
		return instance;
	}

	public String getOS(){
		if(os == null)
			return "";
		return os;
	}

	public String getLanguage(){
		if(language == null)
			return "";
		return language;
	}
}