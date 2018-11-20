package edu.ksu.cis.bandera.sessions.ui.gui.tree.abstraction;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;

import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Component;

import ca.mcgill.sable.soot.SootClass;
import ca.mcgill.sable.soot.SootField;
import ca.mcgill.sable.soot.SootMethod;

/**
 * The SootClassTreeCellRenderer provides a way to render TreeNodes that hold objects of the following
 * type:
 * <ul>
 * <li>ca.mcgill.sable.soot.SootClass - class name</li>
 * <li>ca.mcgill.sable.soot.SootField - field name : field type -> abstraction</li>
 * <li>ca.mcgill.sable.soot.SootMethod - method signature</li>
 * </ul>
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:43 $
 */
public class SootClassTreeCellRenderer extends JLabel implements TreeCellRenderer {

    private boolean selected;

    public SootClassTreeCellRenderer() {
	super();
    }
    private String getAbstraction(Object field) {
	// query the abstraction from the Session?
	return("No Abstraction");
    }
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
						  boolean expanded, boolean leaf,
						  int row, boolean hasFocus) {

	DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
	Object o = node.getUserObject();
	if (o == null) {
	    setText("null");
	}
	else if (o instanceof SootClass) {
	    SootClass sc = (SootClass)o;
	    setText(sc.getName());
	    setForeground(Color.black);
	}
	else if (o instanceof SootField) {
	    SootField sf = (SootField)o;
	    setText(sf.getName() + " : " + sf.getType().toString() + " -> " + getAbstraction(sf));
	    setForeground(Color.red);
	}
	else if (o instanceof SootMethod) {
	    SootMethod sm = (SootMethod)o;
	    setText(sm.getDeclaration());
	    setForeground(Color.blue);
	}
	else {
	    setForeground(Color.darkGray);
	    setText(o.toString());
	}
	this.selected = selected;

	return(this);
    }
    public void paintComponent(Graphics g) {

	Icon icon = getIcon();
	int offset = 0;
	if((icon != null) && (getText() != null)) {
	    offset = (icon.getIconWidth() + getIconTextGap());
	}
	if(selected) {
	    g.setColor(Color.lightGray);
	    g.drawRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);
	    g.setColor(Color.yellow);
	}
	else {
	    g.setColor(Color.white);
	}
	g.fillRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);
	super.paintComponent(g);
    }
}
