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
    FileObject fo;
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
            tree.setCellRenderer(new FileTreeCellRenderer());
            tree.expandRow(0);
            tree.setRootVisible(false);
            tree.addTreeSelectionListener(this);
            setViewportView(tree);
        }
        public void setTreeSelectionListener(TreeSelectionListener tslistener){
            tree.addTreeSelectionListener(tslistener);
        }
        private TreeNode createNodes() {
            fo = new FileObject("");
        	String database = "admin/;admin/;admin/;admin/data_new.txt;admin/data2.txt;public/aaa/";
            String[] paths = database.split(";");
            for(int i = 0 ; i<paths.length ; i++){

          	  	fo.AddPath(paths[i].split("/"),paths[i]);
            }
      	  	return fo;
        }
        /*private void addNewFile(String newnodename){
        	FileObject node = (FileObject) tree.getLastSelectedPathComponent();        	
        	String path[] = { newnodename };
        	node.AddPath(path);
        }*/


        
        // Tree Selection Event
        
        public void valueChanged(TreeSelectionEvent e) {
        	FileObject node = (FileObject) tree.getLastSelectedPathComponent();        	
        	 if (node == null) return;
        	 else if(node.isLeaf() && node.getFileType()==1){

        		 textArea.setText(node.getNodeName());        		 
        	 }
        }
    }

    public void AddtoTextArea(String contents){
    	textArea.setText(contents);    	
    }
    
    public void AddtoNewFile(String newnodename){
    	//TreeArea.addNewFile(newnodename);
    }
}