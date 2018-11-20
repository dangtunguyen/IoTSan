package edu.ksu.cis.bandera.bui.counterexample.objectdiagram.gef;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.presentation.*;

import java.util.Hashtable;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;

import edu.ksu.cis.bandera.bui.counterexample.objectdiagram.ObjectData;

import org.apache.log4j.Category;

public class ObjectNode extends NetNode implements Serializable {

	private static Category log = Category.getInstance(ObjectNode.class);

    private Object object;

    private TableModel tableModel;

    private String type;
    private String name;

    private FigObjectNode figObjectNode;
    public ObjectNode(Object object) {
        tableModel = null;
        ObjectPort objectPort = new ObjectPort(this);
        addPort(objectPort);
        setObject(object);
    }
    /**
     * Insert the method's description here.
     * Creation date: (1/23/02 3:06:43 PM)
     * @return edu.ksu.cis.bandera.bui.counterexample.objectdiagram.gef.FigObjectNode
     */
    public FigObjectNode getFigObjectNode() {
        if (figObjectNode == null) {
            figObjectNode = new FigObjectNode(this);
        }
        return (figObjectNode);
    }
    public String getId() {
        return (name);
    }
    public String getName() {
        return (name);
    }
    public Object getObject() {
        return (object);
    }
    public TableModel getTableModel() {
        return (tableModel);
    }
    public String getType() {
        return (type);
    }
    public void initialize(Hashtable h) {

    }
    /** Sample event handler: prints a message to the console. */
    public void keyPressed(KeyEvent e) {
        //    System.out.println("sample node got keyDown");
    }
    /** Sample event handler: prints a message to the console. */
    public void keyReleased(KeyEvent e) {
        //    System.out.println("sample node got keyUp");
    }
    /** Sample event handler: prints a message to the console. */
    public void keyTyped(KeyEvent e) {
        //    System.out.println("sample node got keyUp");
    }
    public FigNode makePresentation(Layer lay) {
        FigNode figNode = getFigObjectNode();
        return (figNode);
    }
    /** Sample event handler: prints a message to the console. */
    public void mouseClicked(MouseEvent e) {
        //    System.out.println("sample node got mouseDown");
    }
    /** Sample event handler: prints a message to the console. */
    public void mouseDragged(MouseEvent e) {
        //    System.out.println("sample node got mouseDrag");
    }
    /** Sample event handler: prints a message to the console. */
    public void mouseEntered(MouseEvent e) {
        //    System.out.println("sample node got mouseEnter");
    }
    /** Sample event handler: prints a message to the console. */
    public void mouseExited(MouseEvent e) {
        //    System.out.println("sample node got mouseExit");
    }
    /** Sample event handler: prints a message to the console. */
    public void mouseMoved(MouseEvent e) {
        //    System.out.println("sample node got mouseMove");
    }
    /** Sample event handler: prints a message to the console. */
    public void mousePressed(MouseEvent e) {
        //    System.out.println("sample node got mouseDown");
    }
    /** Sample event handler: prints a message to the console. */
    public void mouseReleased(MouseEvent e) {
        //    System.out.println("sample node got mouseUp");
    }
    /**
     * Insert the method's description here.
     * Creation date: (1/23/02 3:06:43 PM)
     * @param newFigObjectNode edu.ksu.cis.bandera.bui.counterexample.objectdiagram.gef.FigObjectNode
     */
    private void setFigObjectNode(FigObjectNode newFigObjectNode) {
        figObjectNode = newFigObjectNode;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setObject(Object object) {

        this.object = object;

        if (object == null) {
            log.error("object is null.  This might make the system unstable.");
            return;
        }

        // if this is ObjectData then all the hard-work is done already.  just grab it and go.
        if (object instanceof ObjectData) {

            ObjectData objectData = (ObjectData) object;
            setName(objectData.getName());
            //setType(objectData.getType().getName());
            setType(objectData.getType());
            tableModel = objectData.getFields();

            // walk the table model and create the ports for each reference
            if (tableModel != null) {
                int lastColumn = tableModel.getColumnCount() - 1;
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    Object value = tableModel.getValueAt(i, lastColumn);
                    if (value instanceof ObjectData) {
	                    
                        //log.info("found a reference (ObjectData) ... creating the ObjectPort for object.getName() = " + ((ObjectData)value).getName());
                        
                        ObjectPort objectPort = new ObjectPort(this);
                        addPort(objectPort);
                        tableModel.setValueAt(objectPort, i, lastColumn);
                    }
                }
            }

            return;
        }

        log.error("************************************");
        log.error("GOT HERE.  THIS SHOULD NEVER HAPPEN.");
        log.error("************************************");

        // now parse it to create this node -tcw
        Class thisClass = object.getClass();
        setName("" + object.hashCode());
        setType(thisClass.getName());

        try {
            java.lang.reflect.Field[] fieldArray = thisClass.getFields();
            tableModel = new DefaultTableModel(fieldArray.length + 1, 3);

            tableModel.setValueAt("-- Name --", 0, 0);
            tableModel.setValueAt("-- Type --", 0, 1);
            tableModel.setValueAt("-- Value --", 0, 2);

            for (int i = 0; i < fieldArray.length; i++) {
                try {
                    tableModel.setValueAt(fieldArray[i].getName(), i + 1, 0);
                    tableModel.setValueAt(fieldArray[i].getType().getName(), i + 1, 1);

                    Object value = null;
                    Object temp = fieldArray[i].get(object);
                    if (temp == null) {
                        value = "null";
                    }
                    else {
                        if ((temp instanceof String) || (temp instanceof Number)) {
                            value = temp.toString();
                        }
                        else {
                            // create a port!
                            ObjectPort objectPort = new ObjectPort(this);
                            addPort(objectPort);
                            value = objectPort;

                            //value = "object";
                        }
                    }

                    tableModel.setValueAt(value, i + 1, 2);
                }
                catch (Exception e) {
                    log.error(
                        "Exception caught while getting a field.  Exception = " + e.toString());
                }
            }
        }
        catch (SecurityException se) {
            log.error(
                "SecurityException caught while getting the fields.  Exception = "
                    + se.toString());
            tableModel = null;
        }

    }
    public void setType(String type) {
        this.type = type;
    }
}