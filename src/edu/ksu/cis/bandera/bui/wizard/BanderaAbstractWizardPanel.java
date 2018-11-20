package edu.ksu.cis.bandera.bui.wizard;

import edu.ksu.cis.bandera.bui.wizard.jwf.*;
import java.util.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import org.apache.log4j.*;
/**
 * The BanderaAbstractWizardPanel provides a base class that all other WizardPanel's in this
 * wizard can extend.  This provides the common functionality of them all and provides
 * default implementations of many of the methods.  This makes the extended classes much
 * smaller and efficient.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 */
public abstract class BanderaAbstractWizardPanel extends WizardPanel {
	private WizardController wizardController;
	private java.lang.String message;
	private final static Category log = Category.getInstance(BanderaAbstractWizardPanel.class);
/**
 * BanderaAbstractWizardPanel constructor comment.
 */
public BanderaAbstractWizardPanel() {
	super();
}
/**
 * This method determines if the Wizard can complete at the end of this WizardPanel.  This
 * default implemention returns false (meaning we cannot complete the wizard process from this
 * step).
 *
 * @return boolean True if the wizard can complete from this WizardPanel, False otherwise.
 */
public boolean canFinish() {
	return(false);
}
/**
 * This method will provide a way to perform actions before displaying the UI to the user.  This
 * implementation provides no action and relies upon the Wizard to handle any such actions that
 * should occur.
 */
public void display() {}
/**
 * This method will provide a way to finish up the wizard activities.  This
 * implementation provides no action upon completion of the task and relies upon
 * the Wizard to handle the completion tasks.
 */
public void finish() {}
/**
 * Insert the method's description here.
 * Creation date: (6/3/2002 11:22:58 AM)
 * @return java.lang.String
 */
protected java.lang.String getMessage() {
	return message;
}
/**
 * Since all the wizard panels will contain some form of text, we will share
 * the ability to generate the same type of JComponent based upon a URL given
 * that will contain the message to be displayed.  This will create a GUI
 * component that can be added to the panel.
 *
 * @return javax.swing.JComponent
 */
protected JComponent getMessagePane() {

	JEditorPane editorPane = new JEditorPane();
	editorPane.setEditable(false);
	editorPane.setText(message);
	return(editorPane);
	
}
/**
 * Insert the method's description here.
 * Creation date: (6/3/2002 11:08:25 AM)
 * @return edu.ksu.cis.bandera.bui.wizard.WizardController
 */
public WizardController getWizardController() {
	return wizardController;
}
/**
 * Check to see if there is another step in the process.  This will default to
 * true but will probably be over-ridden by the extension to the WizardPanel.
 *
 * @return boolean True if another WizardPanel follows this WizardPanel, False otherwise.
 */
public boolean hasNext() {
	return(true);
}
/**
 * Starts the application.
 * @param args an array of command-line arguments
 */
public static void main(java.lang.String[] args) {
	// Insert code to start the application here.
}
/**
 * Get the next WizardPanel in this Wizard process.  This will make use of
 * the wizard controller to make this decision.
 *
 * @return edu.ksu.cis.bandera.bui.wizard.jwf.WizardPanel
 */
public WizardPanel next() {
	
	if(wizardController != null) {
		return(wizardController.getNext(this));
	}
	else {
		return(null);
	}
	
}
/**
 * Insert the method's description here.
 * Creation date: (6/3/2002 11:22:58 AM)
 * @param newMessage java.lang.String
 */
protected void setMessage(java.lang.String newMessage) {
	message = newMessage;
}
/**
 * Set the URL from which to read the message for this WizardPanel.  This will
 * read the contents of the URL and set it as the message for this WizardPanel.
 *
 * @param messageURL java.net.URL
 * @pre messageURL != null
 */
public void setMessageURL(URL messageURL) {

    if (messageURL == null) {
        return;
    }

    // read the url into the message attribute
    StringBuffer b = new StringBuffer();
    InputStream is = null;
    InputStreamReader isr = null;
    BufferedReader br = null;
    try {
        is = messageURL.openStream();
        isr = new InputStreamReader(is);
        br = new BufferedReader(isr);
        while (br.ready()) {
            b.append(br.readLine() + "\n");
        }
        message = b.toString();
    }
    catch (Exception e) {
        log.error("Exception while reading the URL.", e);
    }
    finally {
        try {
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (is != null) {
                is.close();
            }
        }
        catch (Exception e) {
            // ignore this. -tcw
        }
    }
}
/**
 * Insert the method's description here.
 * Creation date: (6/3/2002 11:08:25 AM)
 * @param newWizardController edu.ksu.cis.bandera.bui.wizard.WizardController
 */
public void setWizardController(WizardController newWizardController) {
	wizardController = newWizardController;
}
/**
 * Validate this completion of the Wizard process and report errors that are caught using the
 * list provided.  This default implementation will just return true and not modify
 * the list of errors.
 *
 * @return boolean
 * @param list java.util.List
 */
public boolean validateFinish(List list) {
	return(true);
}
/**
 * Validate this step in the Wizard process and report errors that are caught using the
 * list provided.  This default implementation will just return true and not modify
 * the list of errors.
 *
 * @return boolean
 * @param list java.util.List
 */
public boolean validateNext(List list) {
	return(true);
}
}
