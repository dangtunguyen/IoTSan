package edu.ksu.cis.bandera.checker;

import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Category;

import edu.ksu.cis.bandera.checker.Options;
import edu.ksu.cis.bandera.checker.OptionsView;

/**
 * The CheckerOptionsFactory provides factory methods for creating the checker
 * options model and view.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:22 $
 */
public class OptionsFactory {

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(OptionsFactory.class.getName());

    /**
     * A Map from checker name to the class name to use for the model.  The key for
     * this map will be the name of the checker and the value will be a String that
     * represents the full class name to instantiate to create the Options model.
     */
    private static Map optionsNameMap;

    /**
     * A Map from checker name to the class to use for the model.  The key for
     * this map will be the name of the checker and the value will be the class
     * to use.  This is done to provide faster performance for creating Options
     * model.
     */
    private static Map optionsMap;

    /**
     * A Map from checker name to the class name to use for the view.  The key for
     * this map will be the name of the checker and the value will be a String that
     * represents the full class name to instantiate to create the Options view.
     */
    private static Map optionsViewNameMap;

    /**
     * A Map from checker name to the class to use for the view.  The key for this
     * map will the name of the checker and the value will be the class to use.  This
     * is done to provide faster performance for creating OptionsView view.
     */
    private static Map optionsViewMap;

    static {
	init();
    }

    /**
     * Initialize the Factory.  To do this, we will init all the mappings.
     */
    private static void init() {

	/*
	 * To enhance this, we should be able to define this in a text/xml file and read
	 * it at runtime! -tcw
	 */

	optionsNameMap = new HashMap();
	optionsMap = new HashMap();
	optionsViewNameMap = new HashMap();
	optionsViewMap = new HashMap();

	// setup defaults just in case we cannot find the requested model and view
	optionsNameMap.put("", "edu.ksu.cis.bandera.checker.DefaultOptions");
	optionsViewNameMap.put("", "edu.ksu.cis.bandera.checker.DefaultOptionsView");
	optionsNameMap.put("Default", "edu.ksu.cis.bandera.checker.DefaultOptions");
	optionsViewNameMap.put("Default", "edu.ksu.cis.bandera.checker.DefaultOptionsView");
	
	optionsNameMap.put("JPF", "edu.ksu.cis.bandera.checker.jpf.JPFOptions");
	optionsViewNameMap.put("JPF", "edu.ksu.cis.bandera.checker.jpf.JPFOptionsView");

	optionsNameMap.put("Spin", "edu.ksu.cis.bandera.checker.spin.SpinOptions");
	optionsViewNameMap.put("Spin", "edu.ksu.cis.bandera.checker.spin.SpinOptionsView");

	optionsNameMap.put("DSpin", "edu.ksu.cis.bandera.checker.dspin.DSpinOptions");
	optionsViewNameMap.put("DSpin", "edu.ksu.cis.bandera.checker.dspin.DSpinOptionsView");

	optionsNameMap.put("SMV", "edu.ksu.cis.bandera.checker.DefaultOptions");
	optionsViewNameMap.put("SMV", "edu.ksu.cis.bandera.checker.DefaultOptionsView");
	
	optionsNameMap.put("HSF-Spin", "edu.ksu.cis.bandera.checker.DefaultOptions");
	optionsViewNameMap.put("HSF-Spin", "edu.ksu.cis.bandera.checker.DefaultOptionsView");
    }

    /**
     * Create a new instance of Options that matches the checker given.
     *
     * @param String name The name of the checker for which we need the Options model.
     * @return Options The checker options model or null if an error occurs.
     */
    public static Options createOptionsInstance(String name) {

	Options options = null;

	Class optionsClass = (Class)optionsMap.get(name);
	if(optionsClass == null) {
	    String optionsClassName = (String)optionsNameMap.get(name);
	    if((optionsClassName != null) && (optionsClassName.length() > 0)) {
		try {
		    optionsClass = Class.forName(optionsClassName);
		    optionsMap.put(name, optionsClass);
		}
		catch(ClassNotFoundException cnfe) {
		    log.error("The class name mapped to this checker name, " + name + ", was not found.");
		    options = null;
		}
	    }
	    else {
		log.error("There is no mapping from this checker name, " + name + ", to a class name of type Options.");
		options = null;
	    }
	}
	else {
	    log.debug("The class was already loaded [name = " + name + ", class name = " + optionsClass.getName() + "].");
	}

	if(optionsClass != null) {
	    try {
		Object optionsObject = optionsClass.newInstance();
		options = (Options)optionsObject;
	    }
	    catch(ClassCastException cce) {
		log.error("The class mapped from this checker name, " + name + ", was not of type Options.");
		options = null;
	    }
	    catch(Exception e) {
		log.error("An error occured while creating a new Options instance of type " + optionsClass.getName() + ".");
		options = null;
	    }
	}

	return(options);
    }

    /**
     *
     *
     * @pre optionsViewMap != null
     * @pre optionsViewNameMap != null
     */
    public static OptionsView createOptionsViewInstance(String name) {

	OptionsView optionsView = null;

	Class optionsViewClass = (Class)optionsViewMap.get(name);
	if(optionsViewClass == null) {
	    String optionsViewClassName = (String)optionsViewNameMap.get(name);
	    if((optionsViewClassName != null) && (optionsViewClassName.length() > 0)) {
		try {
		    optionsViewClass = Class.forName(optionsViewClassName);
		    optionsViewMap.put(name, optionsViewClass);
		}
		catch(ClassNotFoundException cnfe) {
		    log.error("The class name mapped to this checker name, " + name + ", was not found.");
		    optionsView = null;
		}
	    }
	    else {
		log.error("There is no mapping from this checker name, " + name + ", to a class name of type OptionsView.");
		optionsView = null;
	    }
	}
	else {
	    log.debug("The class was already loaded [name = " + name + ", class name = " + optionsViewClass.getName() + "].");
	}

	if(optionsViewClass != null) {
	    try {
		Object optionsViewObject = optionsViewClass.newInstance();
		optionsView = (OptionsView)optionsViewObject;
	    }
	    catch(ClassCastException cce) {
		log.error("The class mapped from this checker name, " + name + ", was not of type OptionsView.");
		optionsView = null;
	    }
	    catch(Exception e) {
		log.error("An error occured while creating a new OptionsView instance of type " + optionsViewClass.getName() + ".");
		optionsView = null;
	    }
	}

	return(optionsView);
    }
}
