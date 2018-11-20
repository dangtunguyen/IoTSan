package edu.ksu.cis.bandera.sessions.ui.gui;

import edu.ksu.cis.bandera.sessions.Session;
import javax.swing.*;
import edu.ksu.cis.bandera.sessions.*;
import java.net.URL;

/**
 * The SessionListCellRenderer provides a way to render Session's in a List.  This
 * will be used by the SessionManagerView to display a list of sessions and denote the
 * active session when one exists.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:42 $
 */
public final class SessionListCellRenderer extends JLabel implements ListCellRenderer {
	private SessionManager sessionManager;
	private static javax.swing.Icon activeIcon;
	private static javax.swing.Icon inactiveIcon;
/**
 * SessionListCellRenderer constructor comment.
 */
public SessionListCellRenderer() {
	this(null);
}
	/**
	 * Return a component that has been configured to display the specified
	 * value. That component's <code>paint</code> method is then called to
	 * "render" the cell.  If it is necessary to compute the dimensions
	 * of a list because the list cells do not have a fixed size, this method
	 * is called to generate a component on which <code>getPreferredSize</code>
	 * can be invoked.
	 *
	 * @param list The JList we're painting.
	 * @param value The value returned by list.getModel().getElementAt(index).
	 * @param index The cells index.
	 * @param isSelected True if the specified cell was selected.
	 * @param cellHasFocus True if the specified cell has the focus.
	 * @return A component whose paint() method will render the specified value.
	 *
	 * @see JList
	 * @see ListSelectionModel
	 * @see ListModel
	 */
public java.awt.Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	
	// ignore index
	// ignore cellHasFocus

	setOpaque(true);
	
	if (isSelected) {
		setBackground(list.getSelectionBackground());
		setForeground(list.getSelectionForeground());
	}
	else {
		setBackground(list.getBackground());
		setForeground(list.getForeground());
	}

	if((value != null) && (value instanceof Session)) {
		Session session = (Session)value;
		setText(session.getName());
		Session activeSession = sessionManager.getActiveSession();
		if((activeSession != null) && (activeSession.equals(session))) {
			setIcon(activeIcon);
		}
		else {
			setIcon(inactiveIcon);
		}
	}
	else if((value != null) && (value instanceof String)) {
		String sessionID = (String)value;
		Session session = sessionManager.getSession(sessionID);
		setText(sessionID);
		Session activeSession = sessionManager.getActiveSession();
		if((activeSession != null) && (activeSession.equals(session))) {
			setIcon(activeIcon);
		}
		else {
			setIcon(inactiveIcon);
		}
	}
	else {
		setText("Not a Session");
		setIcon(inactiveIcon);
	}
	
	return(this);
}
/**
 * Insert the method's description here.
 * Creation date: (8/11/2002 1:21:28 PM)
 * @return edu.ksu.cis.bandera.sessions.SessionManager
 */
public SessionManager getSessionManager() {
	return sessionManager;
}
/**
 * Insert the method's description here.
 * Creation date: (8/11/2002 1:21:28 PM)
 * @param newSessionManager edu.ksu.cis.bandera.sessions.SessionManager
 */
public void setSessionManager(SessionManager newSessionManager) {
	sessionManager = newSessionManager;
}

/**
 * SessionListCellRenderer constructor comment.
 */
public SessionListCellRenderer(SessionManager sessionManager) {
	
	super();

	if(sessionManager != null) {
		this.sessionManager = sessionManager;
	}
	else {
		// since there isn't a session manager given, we will grab the shared instance
		this.sessionManager = SessionManager.getInstance();
	}
	
	// init the active icon
	if(activeIcon == null) {
		try {
        	String activeIconFilename = "edu/ksu/cis/bandera/sessions/ui/gui/images/activeIcon.gif";
        	URL url = ClassLoader.getSystemClassLoader().getResource(activeIconFilename);
        	activeIcon = new ImageIcon(url);
		}
		catch(Exception e) {
			System.err.println("Exception while loading the actice icon: " + e.toString());
			activeIcon = new ImageIcon();
		}
	}
	
	// init the inactive icon
	if(inactiveIcon == null) {
		try {
        	String inactiveIconFilename = "edu/ksu/cis/bandera/sessions/ui/gui/images/inactiveIcon.gif";
        	URL url = ClassLoader.getSystemClassLoader().getResource(inactiveIconFilename);
        	inactiveIcon = new ImageIcon(url);
		}
		catch(Exception e) {
			System.err.println("Exception while loading the inactice icon: " + e.toString());
			inactiveIcon = new ImageIcon();
		}
	}
}
}
