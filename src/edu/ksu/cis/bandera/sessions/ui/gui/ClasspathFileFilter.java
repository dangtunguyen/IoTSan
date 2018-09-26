package edu.ksu.cis.bandera.sessions.ui.gui;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import org.apache.log4j.Category;

/**
 * The ClasspathFileFilter will filter out all other files except
 * those that would make up a valid classpath:
 * <ul>
 * <li>Java archive files (.jar extension)</li>
 * <li>Java class files (.class extension)</li>
 * <li>Directories</li>
 * </ul>
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:42 $
 */
public final class ClasspathFileFilter extends FileFilter {
	
	/**
	  * The log we will write messages to.
	  */
	private static final Category log = Category.getInstance(ClasspathFileFilter.class);
	
    /**
     * ClasspathFileFilter constructor comment.
     */
    public ClasspathFileFilter() {
        super();
    }
    /**
     * Accept only jar files, class files, and directories.
     *
     * @return boolean True if this file is acceptable, False otherwise.
     * @param File file The file to test for acceptance.
     * @pre file is not null.
     */
    public boolean accept(File file) {
        if(file == null) {
	        return(false);
        }
        if(file.isDirectory()) {
	        return(true);
        }
        if(file.getPath().endsWith(".jar")) {
	        return(true);
        }
        if(file.getPath().endsWith(".class")) {
	        return(true);
        }
        return(false);
    }
    /**
     * Accept only Java archives (.jar extension), Java class files (.class),
     * and directories.
     *
     * @return String The description of this file filter.
     * @see FileView#getName
     */
    public String getDescription() {
        return("*.class, *.jar");
    }
}
