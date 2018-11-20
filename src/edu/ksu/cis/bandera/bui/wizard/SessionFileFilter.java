package edu.ksu.cis.bandera.bui.wizard;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import org.apache.log4j.*;

/**
 * Insert the type's description here.
 * Creation date: (4/18/2002 9:20:34 PM)
 * @author: 
 */
public final class SessionFileFilter extends javax.swing.filechooser.FileFilter {
	private final static Category log = Category.getInstance(SessionFileFilter.class);
/**
 * SessionFileFilter constructor comment.
 */
public SessionFileFilter() {
	super();
}
	/**
	 * Only accept session files (.session extension) and directories.
	 *
	 * @param File file The file to check if it is acceptable to show in the JFileChooser.
	 * @pre file is not null.
	 */
public boolean accept(File file) {

	if(file == null) {
		return(false);
	}

	if(file.isDirectory()) {
		return(true);
	}

	if(file.getPath().endsWith(".session")) {
		return(true);
	}

	return(false);
}
	/**
	 * Filter out everything except session files (.session) and directories.
	 * @see FileView#getName
	 */
public String getDescription() {
	return("*.session");
}
}
