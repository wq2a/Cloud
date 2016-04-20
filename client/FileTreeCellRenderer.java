package cloud.client;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

class FileTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,hasFocus);
        if (value instanceof DefaultMutableTreeNode) {
            FileObject node = (FileObject) value;

            if (node.getFileType()==0) {
                setIcon(UIManager.getIcon("FileView.directoryIcon"));
            }else{
                setIcon(UIManager.getIcon("FileView.fileIcon"));
            }
        }
        return this;
    }
}