package edu.ksu.cis.bandera.sessions;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observer;
import java.util.Observable;
import java.util.StringTokenizer;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import edu.ksu.cis.bandera.sessions.ResourceBounds;
import edu.ksu.cis.bandera.sessions.BIROptions;

import org.apache.log4j.Category;

/**
 * The Session object is a holder of configuration information.  Most of the information is
 * in the form of properties.  Some examples of the properties that will probably exist:
 * - checker.name
 * - jpf.options
 * - spin.options
 * - hsf-spin.options
 * - dspin.options
 * - smv.options
 * - and many, many more
 *
 * Each property will be returned as a String and will expect the caller to interpret the data.
 *
 * Each session will have a name and a description.  The name is required to be unique within
 * the session file.  Each session will also have a main class file as well as a set of other
 * class files that make up the application.  It will also rely upon a classpath to take care
 * of external libraries.  Each session will also have a location in which temporary and dumped
 * files will be placed; this is the working directory.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:28 $
 */
public final class Session extends Observable implements Cloneable, Observer {

	/**
	 * The log to write messages to.
	 */
	private static final Category log = Category.getInstance(Session.class.getName());

	/**
	 *
	 */
	public static final String CHECKER_OPTIONS_PROPERTY = "checker.options";

	/**
	 *
	 */
	public static final String CHECKER_NAME_PROPERTY = "checker.name";

	/**
	 *
	 */
	public static final String JPF_OPTIONS_PROPERTY = "jpf.options";

	/**
	 *
	 */
	public static final String SPIN_OPTIONS_PROPERTY = "spin.options";

	/**
	 *
	 */
	public static final String DSPIN_OPTIONS_PROPERTY = "dspin.options";

	/**
	 *
	 */
	public static final String SMV_OPTIONS_PROPERTY = "smv.options";

	/**
	 *
	 */
	public static final String HSF_SPIN_OPTIONS_PROPERTY = "hsf-spin.options";

	/**
	 *
	 */
	public static final String JPF_CHECKER_NAME_PROPERTY = "JPF";

	/**
	 *
	 */
	public static final String SPIN_CHECKER_NAME_PROPERTY = "Spin";

	/**
	 *
	 */
	public static final String DSPIN_CHECKER_NAME_PROPERTY = "DSpin";

	/**
	 *
	 */
	public static final String SMV_CHECKER_NAME_PROPERTY = "SMV";

	/**
	 *
	 */
	public static final String HSF_SPIN_CHECKER_NAME_PROPERTY = "HSF-Spin";

	/**
	 * The name of the Session.
	 */
	private String name;

	/**
	 * A description of what this session does (checks).
	 */
	private String description;

	/**
	 * The name to use to generate the temporary output directory.
	 */
	private String outputName;

	/**
	 * The location in which to put the temporary output.
	 */
	private File workingDirectory;

	/**
	 * The file that holds the main class.
	 */
	private File mainClassFile;

	/**
	 * A List of resources (String) that make up the classpath.  This
	 * should be a list of jar files and directories.
	 */
	private List classpath;

	/**
	 * 
	 */
	private List includes;

	/**
	 * The abstraction description that will be used in this session.
	 */
	private Abstraction abstraction;

	/**
	 * To allow for an incremental change from the old session file to the new one, we
	 * put the abstraction file name in the session.  [This field should be removed! -tcw]
	 */
	private String abstractionFilename;

	/**
	 * The specification that will be used in this session.
	 */
	private Specification specification;

	/**
	 * To allow for an incremental change from the old session file to the new one, we
	 * put the specification file name in the session.  [This field should be removed! -tcw]
	 */
	private String specificationFilename;

	/**
	 * The counter example configuration information to use when a counter example is
	 * generated.
	 */
	private CounterExampleDescription counterExampleDescription;

	/**
	 * A flag to determine if this session will use abstraction (slabs).
	 */
	private boolean abstractionEnabled;

	/**
	 * A flag to determine if this session will use slicing.
	 */
	private boolean slicingEnabled;

	/**
	 * A flag to determine if this session will use a model checker.
	 */
	private boolean checkerEnabled;

	/**
	 * A map of property names to values.
	 */
	private Map propertyMap;

	/**
	 * The BIR-based model checker options.
	 */
	private BIROptions birOptions;


	public Session() {
		birOptions = new BIROptions();
		checkerEnabled = false;
		slicingEnabled = false;
		abstractionEnabled = false;
		propertyMap = new HashMap();
	}

	/**
	 *
	 */
	public void addClasspathResource(String resource) {
		if(classpath == null) {
			classpath = new ArrayList();
		}
		classpath.add(resource);
	}

	/**
	 *
	 */
	public void addInclude(String include) {
		if(includes == null) {
			includes = new ArrayList();
		}
		includes.add(include);
	}

	/**
	 * Clone this session to create a new session based upon the values in this session.
	 */
	public Object clone() {
		Session session = new Session();
		session.setName(name);
		session.setDescription(description);
		session.setOutputName(outputName);
		session.setMainClassFile(mainClassFile);
		session.setWorkingDirectory(workingDirectory);
		session.setAbstractionEnabled(abstractionEnabled);
		session.setSlicingEnabled(slicingEnabled);
		session.setModelCheckingEnabled(checkerEnabled);

		// classpath
		if(classpath != null) {
			for(int i = 0; i < classpath.size(); i++) {
				session.addClasspathResource((String)classpath.get(i));
			}
		}

		// properties
		if(propertyMap != null) {
			Iterator propertyMapIterator = propertyMap.keySet().iterator();
			while(propertyMapIterator.hasNext()) {
				String key = (String)propertyMapIterator.next();
				String value = (String)propertyMap.get(key);
				session.setProperty(key, value);
			}
		}

		// abstraction
		if(abstraction != null) {
			Abstraction a = (Abstraction)abstraction.clone();
			session.setAbstraction(a);
		}

		// specification
		if(specification != null) {
			Specification s = (Specification)specification.clone();
			session.setSpecification(s);
		}

		// counter example
		if(counterExampleDescription != null) {
			CounterExampleDescription c = (CounterExampleDescription)counterExampleDescription.clone();
			session.setCounterExampleDescription(c);
		}

		// includes
		if(includes != null) {
			for(int i = 0; i < includes.size(); i++) {
				session.addInclude((String)includes.get(i));
			}
		}

		// bir options
		if(birOptions != null) {
			BIROptions bo = (BIROptions)birOptions.clone();
			session.setBIROptions(bo);
		}

		// observers? 

		return(session);
	}

	/**
	 * Disable the use of abstraction (slabs) in this session.
	 */
	public void disableAbstraction() {
		setAbstractionEnabled(false);
	}

	/**
	 * Disable the use of a model checker in this session.
	 */
	public void disableModelChecking() {
		setModelCheckingEnabled(false);
	}

	/**
	 * Disable the use of slicing in this session.
	 */
	public void disableSlicing() {
		setSlicingEnabled(false);
	}

	/**
	 * Enable the use of abstraction (slabs) in this session.
	 */
	public void enableAbstraction() {
		setAbstractionEnabled(true);
	}

	/**
	 * Enable the use of a model checker in this session.
	 */
	public void enableModelChecking() {
		setModelCheckingEnabled(true);
	}

	/**
	 * Enable the use of slicing in this session.
	 */
	public void enableSlicing() {
		setSlicingEnabled(true);
	}

	/**
	 * Test the equality of this object with another.  Equality in this
	 * case means of the same type (Session) and contain the same name.
	 *
	 * @param Object object The object to test equality with.
	 * @return boolean True if this session and the given object are equal.
	 */
	public boolean equals(Object object) {

		boolean equal = false;

		if(object == null) {
			equal = false;
		}
		else if(object == this) {
			equal = true;
		}
		else if(object instanceof Session) {
			Session session = (Session)object;
			if(((name == null) && (session.name == null)) ||
					((name != null) && (name.equals(session.name)))) {
				equal = true;
			}
		}

		return(equal);
	}

	/**
	 * Get the abstraction defined for this session.
	 *
	 * @return Abstraction The abstraction that has been defined for thsi session
	 *         or null if none has been specified.
	 */
	public Abstraction getAbstraction() {
		return(abstraction);
	}

	public String getAbstractionFilename() {
		return(abstractionFilename);
	}

	/**
	 * Get the BIR Options for this session.
	 *
	 * @return BIROptions The options to use for BIR for this session.
	 */
	public BIROptions getBIROptions() {
		return birOptions;
	}

	/**
	 *
	 */
	public List getClasspath() {
		return(classpath);
	}

	public String getClasspathString() {

		if(classpath == null) {
			return("");
		}

		StringBuffer sb = new StringBuffer();

		for(int i = 0; i < classpath.size(); i++) {
			sb.append(classpath.get(i).toString() + File.pathSeparator);
		}

		return(sb.toString().substring(0, sb.length() - 1));
	}

	public void setClasspathString(String classpathString) {
		// parse the string into the list
		if(classpath != null) {
			classpath.clear();
		}
		else {
			classpath = new ArrayList();
		}

		if(classpathString == null) {
			return;
		}

		StringTokenizer st = new StringTokenizer(classpathString, File.pathSeparator);
		while(st.hasMoreTokens()) {
			classpath.add(st.nextToken());
		}
	}

	public CounterExampleDescription getCounterExampleDescription() {
		return(counterExampleDescription);
	}

	/**
	 * Get the session description.
	 *
	 * @return String A description of this session.
	 */
	public String getDescription() {
		return(description);
	}

	/**
	 *
	 */
	public List getIncludes() {
		return(includes);
	}

	public String[] getIncludesArray() {
		String[] includesArray = new String[0];
		if((includes != null) && (includes.size() > 0)) {
			includesArray = new String[includes.size()];
			for(int i = 0; i < includes.size(); i++) {
				includesArray[i] = (String)includes.get(i);
			}
		}
		return(includesArray);
	}

	/**
	 * Get the main class file for this session's application.
	 *
	 * @return File The File in which the Java source code main method will be found.
	 */
	public File getMainClassFile() {
		return(mainClassFile);
	}

	/**
	 * Get the name of this session.
	 *
	 * @return String The name of this session.
	 */
	public String getName() {
		return(name);
	}

	/**
	 * Get the output name of this session.
	 *
	 * @return String The output name of this session.
	 */
	public String getOutputName() {
		return(outputName);
	}

	/**
	 * This will retrieve a named property based upon the key given.
	 *
	 * @param String key The key to the property in which to grab.
	 * @return String The property associated with the key given or null if
	 *         a matching value cannot be found.
	 */
	public String getProperty(String key) {
		if(propertyMap == null) {
			return(null);
		}
		String value = (String)propertyMap.get(key);
		if(value == null) {
			value = "";
		}
		return(value);
	}

	/**
	 * Get the specification defined for this session.
	 *
	 * @return Specification The specification that has been defined for this session
	 *         or null if none has been specified.
	 */
	public Specification getSpecification() {
		return(specification);
	}

	/* ****************************************************** */
	/* This method is for backward compatibility ... this should be removed! */
	public String getSpecificationFilename() {
		return(specificationFilename);
	}

	/**
	 * Get the directory in which we will place temporary files.
	 *
	 * @return File The directory to place temporary files in.
	 */
	public File getWorkingDirectory() {
		return(workingDirectory);
	}

	/**
	 * Is abstraction enabled for this session?
	 *
	 * @param boolean true if this session is set to use abstraction, false otherwise.
	 */
	public boolean isAbstractionEnabled() {
		return(abstractionEnabled);
	}

	/**
	 * Is model checking enabled for this session?
	 *
	 * @param boolean true if this session is set to use model checking, false otherwise.
	 */
	public boolean isModelCheckingEnabled() {
		return(checkerEnabled);
	}

	/**
	 * Is slicing enabled for this session?
	 *
	 * @param boolean true if this session is set to use slicing, false otherwise.
	 */
	public boolean isSlicingEnabled() {
		return(slicingEnabled);
	}

	/**
	 *
	 */
	public void removeClasspathResource(String resource) {
		if(classpath == null) {
			return;
		}
		boolean changed = classpath.remove(resource);
		if(changed) {
			setChanged();
			notifyObservers();
		}
	}

	/**
	 *
	 */
	public void removeInclude(String include) {
		if(includes == null) {
			return;
		}
		boolean changed = includes.remove(include);
		if(changed) {
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * Set the abstraction to use with this session's application.
	 */
	public void setAbstraction(Abstraction abstraction) {
		this.abstraction = abstraction;
		if(this.abstraction != null ) {
			this.abstraction.addObserver(this);
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * Set the use abstraction flag.  If true, abstraction will be used
	 * in this session.
	 */
	public void setAbstractionEnabled(boolean enabled) {
		if(abstractionEnabled != enabled) {
			abstractionEnabled = enabled;
			setChanged();
			notifyObservers();
		}
	}

	public void setAbstractionFilename(String abstractionFilename) {
		this.abstractionFilename = abstractionFilename;
		setChanged();
		notifyObservers();
	}

	/**
	 * Set the BIR options for this session.
	 *
	 * @param BIROptions birOptions The new options to use.
	 */
	public void setBIROptions(BIROptions birOptions) {
		this.birOptions = birOptions;
		if(this.birOptions != null) {
			this.birOptions.addObserver(this);
		}
		setChanged();
		notifyObservers();
	}

	public void setClasspath(List classpath) {
		this.classpath = classpath;
		setChanged();
		notifyObservers();
	}

	public void setCounterExampleDescription(CounterExampleDescription counterExampleDescription) {
		this.counterExampleDescription = counterExampleDescription;
		if(this.counterExampleDescription != null ){
			this.counterExampleDescription.addObserver(this);
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * Set the description of this session.
	 *
	 * @param String description A description of this session.
	 */
	public void setDescription(String description) {
		this.description = description;
		setChanged();
		notifyObservers();
	}

	public void setIncludes(List includes) {
		this.includes = includes;
		setChanged();
		notifyObservers();
	}

	public void setMainClassFile(File file) {
		mainClassFile = file;
		setChanged();
		notifyObservers();
	}

	public void setMainClassFile(String filename) {
		if(filename == null) {
			return;
		}
		File file = new File(filename);
		setMainClassFile(file);
	}

	/**
	 * Set the use model checking flag.  If true, model checking will be
	 * used in this session.
	 */
	public void setModelCheckingEnabled(boolean enabled) {
		if(checkerEnabled != enabled) {
			checkerEnabled = enabled;
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * Set the name of this session.
	 *
	 * @param String name The name of this session.
	 */
	public void setName(String name) {
		this.name = name;
		setChanged();
		notifyObservers();
	}

	/**
	 * Set the output name for this session.
	 *
	 * @param String outputName The name of the output.
	 */
	public void setOutputName(String outputName) {
		this.outputName = outputName;
		setChanged();
		notifyObservers();
	}

	/**
	 * Set a property for this session.
	 *
	 * @param String key The key to the property.
	 * @param String value The property value to match the given key.
	 * @pre propertyMap != null
	 */
	public void setProperty(String key, Object value) {
		if(propertyMap == null) {
			propertyMap = new HashMap();
		}
		propertyMap.put(key, value);

		// if we are setting the generic checker options property, we should
		//  duplicate that setting for the JPF, Spin, DSpin, etc. and vice-versa
		if(key.equals(CHECKER_OPTIONS_PROPERTY)) {
			String checkerName = (String)propertyMap.get(CHECKER_NAME_PROPERTY);
			if(checkerName == null) {
			}
			else if(checkerName.equals(JPF_CHECKER_NAME_PROPERTY)) {
				propertyMap.put(JPF_OPTIONS_PROPERTY, value);
			}
			else if(checkerName.equals(SPIN_CHECKER_NAME_PROPERTY)) {
				propertyMap.put(SPIN_OPTIONS_PROPERTY, value);
			}
			else if(checkerName.equals(DSPIN_CHECKER_NAME_PROPERTY)) {
				propertyMap.put(SPIN_OPTIONS_PROPERTY, value);
			}
			else if(checkerName.equals(HSF_SPIN_CHECKER_NAME_PROPERTY)) {
				propertyMap.put(HSF_SPIN_OPTIONS_PROPERTY, value);
			}
			else if(checkerName.equals(SMV_CHECKER_NAME_PROPERTY)) {
				propertyMap.put(SMV_OPTIONS_PROPERTY, value);
			}
			else {
				// i guess do nothing then!
			}
		}
		if(key.equals(JPF_OPTIONS_PROPERTY)) {
			propertyMap.put(CHECKER_OPTIONS_PROPERTY, value);
		}
		if(key.equals(SPIN_OPTIONS_PROPERTY)) {
			propertyMap.put(CHECKER_OPTIONS_PROPERTY, value);
		}
		if(key.equals(DSPIN_OPTIONS_PROPERTY)) {
			propertyMap.put(CHECKER_OPTIONS_PROPERTY, value);
		}
		if(key.equals(SMV_OPTIONS_PROPERTY)) {
			propertyMap.put(CHECKER_OPTIONS_PROPERTY, value);
		}
		if(key.equals(HSF_SPIN_OPTIONS_PROPERTY)) {
			propertyMap.put(CHECKER_OPTIONS_PROPERTY, value);
		}

		setChanged();
		notifyObservers();
	}

	/**
	 * Set the use slicing enabled flag.  If true, slicing will be used
	 * in this session.
	 */
	public void setSlicingEnabled(boolean enabled) {
		if(slicingEnabled != enabled) {
			slicingEnabled = enabled;
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * Set the specification to use to model check this session's application.
	 */
	public void setSpecification(Specification specification) {
		this.specification = specification;
		if(this.specification != null) {
			this.specification.addObserver(this);
		}
		setChanged();
		notifyObservers();
	}

	public void setSpecificationFilename(String specificationFilename) {
		this.specificationFilename = specificationFilename;
		setChanged();
		notifyObservers();
	}

	/**
	 * Set the working directory for this session.
	 *
	 * @param File workingDirectory A directory.
	 * @pre workingDirectory != null
	 * @pre workingDirectory must be a directory
	 * @pre workingDirectory must be readable
	 * @pre workingDirectory must be writable
	 * @pre workingDirectory must exist
	 */
	public void setWorkingDirectory(File workingDirectory) {
		this.workingDirectory = workingDirectory;
		setChanged();
		notifyObservers();
	}

	/**
	 * Set the working directory for this session based upon this directory name.
	 *
	 * @param String workingDirectoryName The name of a directory.
	 * @pre workingDirectoryName != null
	 * @pre workingDirectoryName must be a directory
	 * @pre workingDirectoryName must be readable
	 * @pre workingDirectoryName must be writable
	 * @pre workingDirectoryName must exist
	 */
	public void setWorkingDirectory(String workingDirectoryName) {

		if(workingDirectoryName == null) {
			return;
		}

		File workingDirectory = new File(workingDirectoryName);

		setWorkingDirectory(workingDirectory);
	}

	/**
	 * Create a String representation of this Session.
	 *
	 * @return String The String representation of this session.
	 */
	public String toString() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		pw.println("Session: " + name);
		pw.println("description = " + description);
		pw.println("output = " + outputName + " into " + workingDirectory);
		pw.println("mainClassFile = " + mainClassFile);
		if(abstractionEnabled) {
			pw.println("Abstraction Enabled.");
		}
		else {
			pw.println("Abstraction Disabled.");
		}

		if(slicingEnabled) {
			pw.println("Slicing Enabled.");
		}
		else {
			pw.println("Slicing Disabled.");
		}

		if(checkerEnabled) {
			pw.println("Model Checking Enabled.");
		}
		else {
			pw.println("Model Checking Disabled.");
		}

		if((classpath != null) && (classpath.size() > 0)) {
			pw.println("Classpath:");
			for(int i = 0; i < classpath.size(); i++) {
				pw.println(classpath.get(i));
			}
		}

		if((includes != null) && (includes.size() > 0)) {
			pw.println("Included Types and/or Packages:");
			for(int i = 0; i < includes.size(); i++) {
				pw.println(includes.get(i));
			}
		}

		if(birOptions != null) {
			pw.println(birOptions.toString());
		}

		if((propertyMap != null) && (propertyMap.size() > 0)) {
			pw.println("Properties:");
			Iterator propertyIterator = propertyMap.keySet().iterator();
			while(propertyIterator.hasNext()) {
				Object key = propertyIterator.next();
				Object value = propertyMap.get(key);
				pw.println(key + " = " + value);
			}
		}

		if(abstraction != null) {
			pw.println(abstraction.toString());
		}
		if(specification != null) {
			pw.println(specification.toString());
		}

		return(sw.toString());
	}

	public int hashCode() {
		int hash = 0;

		if(name != null) {
			hash += name.hashCode();
		}

		return(hash);
	}

	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers();
	}
}
