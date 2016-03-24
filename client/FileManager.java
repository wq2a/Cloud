package cloud.client;

import java.io.*;
import java.io.File;

public class FileManager{
	private String root = "MyDocuments";
	private final static String SP = "/";
	private String p;
	public FileManager(){

	}

	public FileManager(String path){
		this.p = path;
	}

	public String getP(){
		return this.p;
	}

	public String getLength(String relativePath){
		File fo = new File(aPath(relativePath));
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
			if((path.substring(path.length()-1)).equals("/")){
				return fo.mkdirs();
			}else{
				return fo.createNewFile();
			}
		}catch(IOException e){
			return false;
		}
		
	}
}