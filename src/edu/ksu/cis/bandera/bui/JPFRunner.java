package edu.ksu.cis.bandera.bui;

/*import gov.nasa.arc.ase.jpf.jvm.Main;

import gov.nasa.arc.ase.jpf.iVirtualMachine;
import gov.nasa.arc.ase.jpf.Engine;
import gov.nasa.arc.ase.jpf.JPFOptions;
import gov.nasa.arc.ase.jpf.ErrorList;*/

//import edu.ksu.cis.bandera.bui.counterexample.JPFTraceManager;
import edu.ksu.cis.bandera.bui.counterexample.CounterExample;
import edu.ksu.cis.bandera.bui.counterexample.Trail;

import edu.ksu.cis.bandera.abstraction.typeinference.TypeTable;

/**
 * The JPFRunner provides a way to run JPF on an application
 * given the set of options, the type table, and a trail file.  This
 * logic was nested inside the Driver but a problem occurs when using
 * a new version of JDK since it tries to pre-load classes.  When JPF
 * isn't installed for a user, it will not be able to preload the JPF
 * classes.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/06/19 15:32:18 $
 */
public class JPFRunner {

    /**
     * Run JPF on the current application using the given options, type table, and trail.  If
     * the application verifies, this method will return null.  If an error occurs, an exception
     * will be thrown.
     *
     * @param String[] options The command line options to JPF.
     * @param TypeTable typeTable
     * @param Trail trail
     * @return CounterExample The counter example geneated from JPF or null if it was
     *         verified.
     * @throws Exception Throws an exception when an error occurs.
     */
    public CounterExample runJPF(String[] options, TypeTable typeTable, Trail trail) throws Exception {

	/*CounterExample ce = null;

	Main.main(options);
	iVirtualMachine vm = Engine.getJPF().vm;
	ErrorList errors = null;
	if (trail == null) {
	    errors = Engine.getJPF().search.getErrors();
	}
	
	if (trail != null || (errors != null && errors.size() > 0)) {
	    ((JPFOptions) Engine.options).debug = true;
	    JPFTraceManager traceManager = new JPFTraceManager(vm, typeTable, trail);
	    ce = new CounterExample(traceManager);
	}
	else {
	    ce = null;
	}*/

	return(null);
    }

}
