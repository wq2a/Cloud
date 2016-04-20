package cloud.client;

import java.util.Vector;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.tree.DefaultMutableTreeNode;

class FileObject extends DefaultMutableTreeNode{
	private String nodename;
  // 0 is directory,1 is file
  private int fileType;
	HashMap<String, FileObject> childmap;
	
	FileObject(String name){
		super(name);

		nodename=name;
		childmap = new HashMap<String,FileObject>();
    fileType = 1;
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
	public void AddPath(String[] path,String p){
    
      FileObject node;

      if( childmap.containsKey(path[0])){
    	  	node = childmap.get(path[0]);
      		String[] newpath = Arrays.copyOfRange(path, 1, path.length);
      		if(newpath.length>0){
      			node.AddPath(newpath,p);	
      		}
      } else{
    	  node = new FileObject(path[0]);
    	  childmap.put(path[0] , node);                	
    	  this.add(node);
    	  String[] newpath = Arrays.copyOfRange(path, 1, path.length);
    	  if(newpath.length>0){
    		  node.AddPath(newpath,p);
    	  }
      }

      if(!p.isEmpty()&&(p.substring(p.length() - 1)).equals("/")){
        node.setFileType(0);
      }
	}

  public void setFileType(int fileType){
      this.fileType = fileType;
  }

  public int getFileType(){
    return fileType;
  }
	
  public String getNodeName(){
    	return nodename;
  }

   /*@Override
    public String toString() {
        //String color = status.equals("Online") ? "Green" : "Red";
        return "<html><a>"+nodename+"</a></html>";
    }*/
	
	
	
}