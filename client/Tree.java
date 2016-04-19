package cloud.client;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
public class Tree extends JPanel{
    TreeArea TreeArea;
    JTextArea textArea;
    final static String newline = "\n";
    
    public Tree() {
        super(new GridLayout(1,2,1,2));
        TreeArea = new TreeArea();
        add(TreeArea);
        textArea = new JTextArea(30,35);
        textArea.setEditable(false);
        textArea.append("Printout Contents");
        add(new JScrollPane(textArea)); 
    }
    class TreeArea extends JScrollPane implements TreeSelectionListener{
        JTree tree;     
        public TreeArea() {
            TreeNode NodeTree = createNodes();
//            TreeNode NodeTree = AddNode("Root");
            tree = new JTree(NodeTree);
            tree.addTreeSelectionListener(this);
            setViewportView(tree);
        }
        private TreeNode createNodes() {
            FileObject test = new FileObject("Root");
        	String database ="first/second/third;first/second_2/third/gg;first_2/second/third;first/second_2/third/uu";
            String[] paths = database.split(";");
            for(int i = 0 ; i<paths.length ; i++){
          	  	test.AddPath(paths[i].split("/"));
            }
      	  	return test;
        }
        private void addNewFile(String newnodename){
        	FileObject node = (FileObject) tree.getLastSelectedPathComponent();        	
        	String path[] = { newnodename };
        	node.AddPath(path);
        }
        
        // Tree Selection Event
        public void valueChanged(TreeSelectionEvent e) {
        	FileObject node = (FileObject) tree.getLastSelectedPathComponent();        	
        	 if (node == null) return;
        	 else if(node.isLeaf()){
        		 textArea.setText(node.getNodeName());        		 
        	 }
        }        
    }

    public void AddtoTextArea(String contents){
    	textArea.setText(contents);    	
    }
    
    public void AddtoNewFile(String newnodename){
    	TreeArea.addNewFile(newnodename);
    }
}
