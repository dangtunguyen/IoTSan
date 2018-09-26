package edu.ksu.cis.bandera.bui.counterexample;

import javax.swing.table.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Insert the type's description here.
 * Creation date: (2/12/2002 11:23:51 AM)
 * @author: 
 */
public class ButtonTableCellRenderer implements TableCellRenderer {
/**
 *  This method is sent to the renderer by the drawing table to
 *  configure the renderer appropriately before drawing.  Return
 *  the Component used for drawing.
 *
 * @param	table		the JTable that is asking the renderer to draw.
 *				This parameter can be null.
 * @param	value		the value of the cell to be rendered.  It is
 *				up to the specific renderer to interpret
 *				and draw the value.  eg. if value is the
 *				String "true", it could be rendered as a
 *				string or it could be rendered as a check
 *				box that is checked.  null is a valid value.
 * @param	isSelected	true is the cell is to be renderer with
 *				selection highlighting
 * @param	row	        the row index of the cell being drawn.  When
 *				drawing the header the rowIndex is -1.
 * @param	column	        the column index of the cell being drawn
 */
public java.awt.Component getTableCellRendererComponent(
    javax.swing.JTable table,
    Object value,
    boolean isSelected,
    boolean hasFocus,
    int row,
    int column) {

    if (value == null) {
        JLabel label = new JLabel("<null/>");
        label.setForeground(Color.red);
        return (label);
    }
    else {
        if ((value instanceof String)
            || (value instanceof Boolean)
            || (value instanceof Number)) {
            JLabel label = new JLabel(value.toString());
            label.setForeground(Color.black);
            return (label);
        }
        else {
	        JPanel panel = new JPanel();
	        panel.setLayout(new FlowLayout());
	        
            JLabel label = new JLabel("Object of type " + value.getClass().getName());
            panel.add(label);
            label.setForeground(Color.black);
            
            JButton button = new JButton("Open Object Diagram");
            button.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
		            System.out.println("button pressed.");
	            }
            });
            panel.add(button);
            
            return (panel);
        }
    }

}
}
