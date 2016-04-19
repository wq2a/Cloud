package cloud.client;

import java.util.Vector;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.tree.DefaultMutableTreeNode;

class FileObject extends DefaultMutableTreeNode{
	private String nodename;
	HashMap<String, FileObject> childmap;
	
	FileObject(String name){
		super(name);
		nodename=name;
		childmap = new HashMap();
	}
/*
	public void AddNode(String name){
		FileObject NewNode;
        NewNode = new FileObject(name);
        if( childmap.containsKey(name)){
        	FileObject nextnode = childmap.get(name);
        	nextnode.AddNode(sp[2]);
        }
        else{
            childmap.put(name , NewNode);                	
            this.add(NewNode);
        }
       
	}
	*/
	public void AddPath(String[] path){
      if( childmap.containsKey(path[0])){
    	  	FileObject nextnode = childmap.get(path[0]);
      		String[] newpath = Arrays.copyOfRange(path, 1, path.length);
      		if(newpath.length>0){
      			nextnode.AddPath(newpath);	
      		}
      }
      else{
    	  FileObject NewNode;
    	  NewNode = new FileObject(path[0]);
    	  childmap.put(path[0] , NewNode);                	
    	  this.add(NewNode);
    	  String[] newpath = Arrays.copyOfRange(path, 1, path.length);
    	  if(newpath.length>0){
    		  NewNode.AddPath(newpath);
    	  }
      }
	}
	
    public String getNodeName(){
    	return nodename;
    }
	
	
	
}