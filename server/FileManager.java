package cloud.server;

import java.sql.*;
import java.io.*;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import cloud.server.User;
import cloud.server.Auth;

public class FileManager{

	private final static String ROOTDIR = "data";
	private final static String SP = "/";
	private StringBuffer home;
	private File fo;
	public FileManager(){
		home = new StringBuffer();
		home.append(ROOTDIR).append(SP);
	}

	public FileManager(Auth auth){
		home = new StringBuffer();
		home.append(ROOTDIR).append(SP).append(auth.getUser().getUsername());
		//System.out.println(auth.getUser().getUsername());
	}

	public FileManager(User user){
		home = new StringBuffer();
		home.append(ROOTDIR).append(SP).append(user.getUsername());
	}

	private String aPath(String relativePath){
		return home.toString()+SP+relativePath;
	}

	public boolean mkdir(String relativePath){
		fo = new File(aPath(relativePath));
		return fo.mkdirs();
	}

	public boolean mkdirAB(String path){
		fo = new File(path);
		return fo.mkdirs();
	}

	public boolean mkdirROOT(){
		fo = new File(ROOTDIR);
		return fo.mkdirs();
	}

	public boolean mkfile(String relativePath,String content) throws Exception{
		File targetFile = new File(aPath(relativePath));
		File parent = targetFile.getParentFile();
		if(!parent.exists()){
			parent.mkdirs();
    		//throw new IllegalStateException("Couldn't create dir: " + parent);
		}

		// if file doesnt exists, then create it
		if (!targetFile.exists()) {
			targetFile.createNewFile();
		}

		FileWriter fw = new FileWriter(targetFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		return true;
	}

	public boolean mkfileAB(String path,String content) throws Exception{
		File targetFile = new File(path);
		File parent = targetFile.getParentFile();
		if(!parent.exists()){
			parent.mkdirs();
    		//throw new IllegalStateException("Couldn't create dir: " + parent);
		}

		// if file doesnt exists, then create it
		if (!targetFile.exists()) {
			targetFile.createNewFile();
		}

		FileWriter fw = new FileWriter(targetFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		return true;
	}

	private void delete(File file) throws IOException{
		if(file.isDirectory()){
			if(file.list().length == 0){
				file.delete();
			}else{
				String[] fs = file.list();
				for(String temp:fs){
					File fd = new File(file,temp);
					delete(fd);
				}

				if(file.list().length == 0){
					file.delete();
				}
			}
		}else{
			file.delete();
		}
	}
	
	public boolean del(String relativePath){
		if(relativePath == null || relativePath.isEmpty()){
			return false;
		}
		File fo = new File(ROOTDIR+SP+relativePath);
		if(!fo.exists()){
			return false;
		}else{
			try{
				delete(fo);
			}catch(IOException e){
				return false;
			}
		}
		return true;
	}

	public boolean delROOT(){
		File fo = new File(ROOTDIR);
		if(!fo.exists()){
			return false;
		}else{
			try{
				delete(fo);
			} catch(IOException e){
				return false;
			}
		}
		return true;
	}

	public byte[] getContent(String path) {
		try{
			File fo = new File(home.toString()+path);
        	byte[] mybytearray = new byte[(int) fo.length()];
        	BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fo));
        	bis.read(mybytearray, 0, mybytearray.length);
        	return mybytearray;
		}catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}

	public byte[] getContentAB(String path) {
		try{
			File fo = new File(path);
        	byte[] mybytearray = new byte[(int) fo.length()];
        	BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fo));
        	bis.read(mybytearray, 0, mybytearray.length);
        	return mybytearray;
		}catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}

	public String getLength(String path){
		//File fo = new File(aPath(relativePath));
		File fo = new File(home.toString()+path);
		return ""+fo.length();
	}

}