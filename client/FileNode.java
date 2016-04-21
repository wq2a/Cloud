package cloud.client;

import java.util.Vector;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.tree.DefaultMutableTreeNode;

class FileNode extends DefaultMutableTreeNode{
private String name;
private String path;
private HashMap<String,FileNode> childNodes;
// 0 is directory,1 is file
private int type;
private boolean shared;
	
FileNode(String name){
    super(name);
		this.name = name;
    type = 1;
    shared = false;
    childNodes = new HashMap<String, FileNode>();
}

public void AddPath(String[] path,String p){
    
    FileNode node;

    if( childNodes.containsKey(path[0])){
  	  	node = childNodes.get(path[0]);
    		String[] newpath = Arrays.copyOfRange(path, 1, path.length);
    		if(newpath.length>0){
    			node.AddPath(newpath,p);
    		} else if(!p.isEmpty()&&(p.substring(p.length() - 1)).equals("/")){
            node.setFileType(0);
        }
    } else {
    	  node = new FileNode(path[0]);
        //System.out.println("add:"+path[0]);
    	  childNodes.put(path[0] , node); 

    	  this.add(node);

    	  String[] newpath = Arrays.copyOfRange(path, 1, path.length);
    	  if(newpath.length>0){
    		  node.AddPath(newpath,p);
    	  } else if(!p.isEmpty()&&(p.substring(p.length() - 1)).equals("/")){
            node.setFileType(0);
        }
    }
    setPath(p);
    
}

  public void setFileType(int fileType){
      this.type = fileType;
  }

  public void setPath(String p){
    setType(p);
    if(p.startsWith("public/")){
      this.shared = true;
    }
    this.path = p;
  }

  public String getP(){
    return path;
  }

  public void setType(String p){
    if(!p.isEmpty()&&(p.substring(p.length() - 1)).equals("/")){
        setFileType(0);
    }
  }

  public boolean isShared(){
    return shared;
  }

  public int getFileType(){
    return type;
  }
	
  public String getNodeName(){
    	return name;
  }

  public FileNode get(String childName){
    return childNodes.get(childName);
  }

  public void add(String childName, FileNode node){
    childNodes.put(childName, node);
  }

   /*@Override
    public String toString() {
        //String color = status.equals("Online") ? "Green" : "Red";
        return "<html><a>"+nodename+"</a></html>";
    }*/
	
	
	
}