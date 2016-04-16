package cloud.server;
import java.util.*;

class Path implements Comparator<Path>{
	private int id;
	private String name;
	private int parent;
	private int depth;
	private int type;
	Path(){

	}
	Path(int id, String name, int parent, int depth,int type){
		this.id = id;
		this.name = name;
		this.parent = parent;
		this.depth = depth;
		this.type = type;
	}
	public int getID(){
		return id;
	}
	public String getName(){
		return name;
	}
	public int getParent(){
		return parent;
	}
	public int getDepth(){
		return depth;
	}
	public int getType(){
		return type;
	}

	public int compare(Path p1, Path p2){
		return p1.depth - p2.depth;
	}
}