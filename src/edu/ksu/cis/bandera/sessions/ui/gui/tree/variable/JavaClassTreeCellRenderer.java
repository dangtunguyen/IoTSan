package edu.ksu.cis.bandera.sessions.ui.gui.tree.variable;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;

import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;

import java.lang.Class;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Component;

/**
 * The ClassTreeCellRenderer provides a way to render TreeNodes that hold objects of the following
 * type:
 * <ul>
 * <li>java.lang.Class - class name</li>
 * <li>java.lang.reflect.Field - field name : field type -> abstraction</li>
 * <li>java.lang.reflect.Method - method signature</li>
 * </ul>
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:43 $
 */
public class JavaClassTreeCellRenderer extends JLabel implements TreeCellRenderer {

    private boolean selected;

    public JavaClassTreeCellRenderer() {
	super();
    }
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
						  boolean expanded, boolean leaf,
						  int row, boolean hasFocus) {

	DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
	Object o = node.getUserObject();
	if (o == null) {
	    setText("null");
	}
	else if (o instanceof Class) {
	    Class c = (Class)o;
	    setText(c.getName());
	    setForeground(Color.black);
	}
	else if (o instanceof Field) {
	    Field f = (Field)o;
	    setText(f.getName() + " : " + f.getType().getName());
	    setForeground(Color.red);
	}
	else if (o instanceof Method) {
	    Method m = (Method)o;

	    int modifiers = m.getModifiers();
	    StringBuffer temp = new StringBuffer();
	    if(java.lang.reflect.Modifier.isPrivate(modifiers)) {
		temp.append("private ");
	    }
	    else if(java.lang.reflect.Modifier.isProtected(modifiers)) {
		temp.append("protected ");
	    }
	    else {
		temp.append("public ");
	    }
	    if(Modifier.isStatic(modifiers)) {
		temp.append("static ");
	    }
	    if(Modifier.isFinal(modifiers)) {
		temp.append("final ");
	    }
	    if(Modifier.isAbstract(modifiers)) {
		temp.append("abstract ");
	    }
	    if(Modifier.isNative(modifiers)) {
		temp.append("native ");
	    }
	    if(Modifier.isSynchronized(modifiers)) {
		temp.append("synchronized ");
	    }

	    String modifierString = temp.toString().trim();
	    temp = new StringBuffer();
	    Class[] parameterTypes = m.getParameterTypes();
	    if((parameterTypes != null) && (parameterTypes.length > 0)) {
		for(int i = 0; i < parameterTypes.length; i++) {
		    temp.append(parameterTypes[i].getName() + " ");
		}
	    }
	    String parameterString = temp.toString().trim();
	    setText(modifierString + " " + m.getReturnType().getName() + " " + m.getName() + "(" + parameterString + ")");
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
