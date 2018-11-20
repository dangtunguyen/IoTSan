package edu.ksu.cis.bandera.bui.counterexample;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.table.*;
/**
 * Insert the type's description here.
 * Creation date: (2/25/2002 10:11:12 AM)
 * @author: 
 */
public class ButtonCellRenderer implements TableCellRenderer {
	private CounterExample counterExample;
/**
 * ButtonCellRenderer constructor comment.
 */
public ButtonCellRenderer(CounterExample counterExample) {
	super();
	this.counterExample = counterExample;
}
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
public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

	if((counterExample != null) && (value != null) && (value instanceof ValueNode)) {
		// return a JButton that when pressed will open up the ObjectDiagram for this ValueNode
		ObjectDiagramButton button = new ObjectDiagramButton(counterExample, (ValueNode)value);
		return(button);
	}

	return(new JLabel(""));
}
}
