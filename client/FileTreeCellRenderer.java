package cloud.client;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

class FileTreeCellRenderer extends DefaultTreeCellRenderer {

    ImageIcon sharedFolder = createImageIcon("resources/sharedfolder.png");
    ImageIcon folder = createImageIcon("resources/folder.png");
    ImageIcon gdoc = createImageIcon("resources/file.png");

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,hasFocus);
        
        if (value instanceof DefaultMutableTreeNode) {
            FileNode node = (FileNode) value;

            if (node.getFileType()==0) {

                //setIcon(UIManager.getIcon("FileView.directoryIcon"));
                if(node.isShared()){
                    setIcon(sharedFolder);
                }else{
                    setIcon(folder);
                }
                
            }else{

                //setIcon(UIManager.getIcon("FileView.fileIcon"));
                setIcon(gdoc);
            }
        }
        return this;
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = FileTreeCellRenderer.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            //System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}