package cloud.client;

import java.io.*;
import java.io.File;
import java.nio.charset.Charset;


public class FileManager{
	
	private String root = "MyDocuments";
	private final static String SP = "/";
	private String p;
	private String content = null;
	private int length;

	public FileManager(){

	}

	public FileManager(String path){
		this.p = path;
	}

	public void setContent(String content){
		this.content = content;
	}

	public void setLength(int length){
		this.length = length;
	}

	public String getP(){
		return this.p;
	}

	public String getLength(String abPath){
		//File fo = new File(aPath(relativePath));
		File fo = new File(abPath);
		return ""+fo.length();
	}

	public String aPath(String relativePath){
		return root+SP+relativePath;
	}

	public boolean mk(String path){
		if(path == null || path.isEmpty()){
			return false;
		}
		File fo = new File(aPath(path));
		try{
			if((path.substring(path.length()-1)).equals(SP)){
				return fo.mkdirs();
			}else{
				return fo.createNewFile();
			}
		}catch(IOException e){
			return false;
		}
	}

	public byte[] getContent() throws Exception{
		
		if(content != null){
			//byte[] mybytearray = new byte[this.length];;
			byte[] mybytearray = this.content.getBytes(Charset.forName("UTF-8"));
			System.out.println(this.length+"==="+mybytearray.length);

			return mybytearray;
		}else{
			File fo = new File(getP());
            byte[] mybytearray = new byte[(int) fo.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fo));
            bis.read(mybytearray, 0, mybytearray.length);
            return mybytearray;
		}
		
	}
}