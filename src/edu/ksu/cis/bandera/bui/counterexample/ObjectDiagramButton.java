package edu.ksu.cis.bandera.bui.counterexample;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
/**
 * Insert the type's description here.
 * Creation date: (2/25/2002 10:27:11 AM)
 * @author: 
 */
public class ObjectDiagramButton extends JButton implements ActionListener {
	private ValueNode valueNode;
	private CounterExample counterExample;
/**
 * Insert the method's description here.
 * Creation date: (2/25/2002 10:28:51 AM)
 * @param valueNode edu.ksu.cis.bandera.bui.counterexample.ValueNode
 */
public ObjectDiagramButton(CounterExample counterExample, ValueNode valueNode) {
	super("Open Object Diagram");
	
	this.valueNode = valueNode;
	this.counterExample = counterExample;
	this.addActionListener(this);
}

/**
 * Insert the method's description here.
 * Creation date: (2/25/2002 10:32:41 AM)
 * @param actionEvent java.awt.event.ActionEvent
 */
public void actionPerformed(ActionEvent actionEvent) {
	
	if(counterExample == null) {
		return;
	}
	if(valueNode == null) {
		return;
	}
	
	counterExample.showObjectDiagram(valueNode);
}
}
