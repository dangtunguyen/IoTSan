package edu.ksu.cis.bandera.bui.wizard.jwf;


import java.util.*;
public class Stage extends Wizard {
	private java.lang.String name;
	private Set wizardPanelClassSet;
/**
 * Stage constructor comment.
 */
public Stage() {
	super();
	
	wizardPanelClassSet = new HashSet();
	
}
/**
 * Insert the method's description here.
 * Creation date: (5/28/2002 10:55:17 AM)
 * @param wizardPanelClass java.lang.Class
 */
public void add(Class wizardPanelClass) {
	wizardPanelClassSet.add(wizardPanelClass);
}
/**
 * Insert the method's description here.
 * Creation date: (5/28/2002 10:55:44 AM)
 * @return boolean
 * @param wizardPanel edu.ksu.cis.bandera.bui.wizard.jwf.WizardPanel
 */
public boolean contains(WizardPanel wizardPanel) {

	Class wizardPanelClass = wizardPanel.getClass();
	if(wizardPanelClassSet.contains(wizardPanelClass)) {
		return(true);
	}
	else {
		return(false);
	}
	
}
/**
 * Insert the method's description here.
 * Creation date: (5/28/2002 10:54:42 AM)
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
/**
 * Insert the method's description here.
 * Creation date: (5/28/2002 10:54:42 AM)
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	name = newName;
}
}
