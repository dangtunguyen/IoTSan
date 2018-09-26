package edu.ksu.cis.bandera.sessions.ui.gui;

import javax.swing.filechooser.FileFilter;

import java.io.File;

import org.apache.log4j.Category;

/**
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:42 $
 */
public final class SessionFileFilter extends FileFilter {
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
