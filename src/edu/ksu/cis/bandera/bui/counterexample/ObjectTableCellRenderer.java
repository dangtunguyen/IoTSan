package edu.ksu.cis.bandera.bui.counterexample;

import javax.swing.table.*;
import java.awt.Color;
import javax.swing.JButton;

/**
 * This class will provide formatting for table cell's in the VariableWatchWindow
 * table.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 */
public class ObjectTableCellRenderer extends DefaultTableCellRenderer {
	
/**
 * Set the value of this table cell.  It will handle the formatting of the table cell's
 * text.  If it is null it will print it in red letters.  If the value is an object of
 * type String, Boolean, or Number, it will just print the value.  Otherwise, it will
 * print the type of object that it is.
 *
 * Note: This will be changed to print the type and then a button to expand it into
 * an object diagram.
 *
 * @param Object value The value to place in this table cell.
 */
public void setValue(Object value) {

    if ((value == null) || (value.toString().equals("null"))) {
        setForeground(Color.red);
        setText("<null>");
    }
    else {
        setForeground(Color.black);
        if ((value instanceof String)
            || (value instanceof Character)
            || (value instanceof Boolean)
            || (value instanceof Number)) {
            setText(value.toString());
        }
        else {
	    // just use the toString value temporarily and take out the "\n" and replace with ", "
            setText("Object of type " + value.getClass().getName());
        }
    }
}
}
