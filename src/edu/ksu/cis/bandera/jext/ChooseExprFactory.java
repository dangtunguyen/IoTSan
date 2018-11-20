package edu.ksu.cis.bandera.jext;

import edu.ksu.cis.bandera.jext.ChooseExpr;
import edu.ksu.cis.bandera.jext.IntChooseExpr;
import edu.ksu.cis.bandera.jext.BooleanChooseExpr;
import edu.ksu.cis.bandera.jext.ExternalIntChooseExpr;
import edu.ksu.cis.bandera.jext.ExternalBooleanChooseExpr;

import ca.mcgill.sable.soot.jimple.IntConstant;

import java.util.List;

import org.apache.log4j.Category;

/**
 * The ChooseExprFactory makes it easy to find ChooseExpr to replace
 * method calls to the given class, method, and arguments.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:23 $
 */
public final class ChooseExprFactory {

    private static final Category log = Category.getInstance(ChooseExprFactory.class);

    /**
     *
     *
     * @param String className The name of the class the method is in.
     * @param String methodName The name of the method that will be replaced.
     * @param List args A list of arguments to the method given.
     * @return A ChooseExpr that matches the class, method, and args given.  If an error occurs
     *         null will be returned.
     */
    public static ChooseExpr getChooseExpr(String className, String methodName, List args) {

	if((className == null) || (className.equals(""))) {
	    log.warn("Cannot create a ChooseExpr given an empty class name.");
	    return(null);
	}

	if((methodName == null) || (methodName.equals(""))) {
	    log.warn("Cannot create a ChooseExpr given an empty method name.");
	    return(null);
	}

	if((className.equals("Bandera")) && (methodName.equals("randomInt"))) {
	    if((args == null) || (args.size() != 1)) {
		log.warn("The Bandera.randomInt call needs a single, int parameter.  This was not supplied.");
		return(null);
	    }

	    Object arg = args.get(0);
	    int max = 0;
	    if(arg == null) {
		log.warn("The parameter to Bandera.randomInt was null.  It expects an Integer or IntConstant.");
		return(null);
	    }
	    else if(arg instanceof Integer) {
		Integer maxInteger = (Integer)arg;
		max = maxInteger.intValue();
	    }
	    else if(arg instanceof IntConstant) {
		IntConstant intConstant = (IntConstant)arg;
		max = intConstant.value;
	    }
	    else {
		log.warn("The parameter to Bandera.randomInt was a " + arg.getClass().getName() +
			 ".  It expects an Integer or IntConstant.");
		return(null);
	    }

	    return(new IntChooseExpr(max));
	}

	if((className.equals("Bandera")) && ((methodName.equals("randomBool")) || (methodName.equals("choose")))) {
	    return(new BooleanChooseExpr());
	}

	if((className.equals("Bandera")) && (methodName.equals("randomIntExtern"))) {
	    if((args == null) || (args.size() != 1)) {
		log.warn("The Bandera.randomIntExtern call needs a single, int parameter.  This was not supplied.");
		return(null);
	    }

	    Object arg = args.get(0);
	    int max = 0;
	    if(arg == null) {
		log.warn("The parameter to Bandera.randomIntExtern was null.  It expects an Integer or IntConstant.");
		return(null);
	    }
	    else if(arg instanceof Integer) {
		Integer maxInteger = (Integer)arg;
		max = maxInteger.intValue();
	    }
	    else if(arg instanceof IntConstant) {
		IntConstant intConstant = (IntConstant)arg;
		max = intConstant.value;
	    }
	    else {
		log.warn("The parameter to Bandera.randomIntExtern was a " + arg.getClass().getName() +
			 ".  It expects an Integer or IntConstant.");
		return(null);
	    }

	    return(new ExternalIntChooseExpr(max));
	}

	if((className.equals("Bandera")) && (methodName.equals("randomBoolExtern"))) {
	    return(new ExternalBooleanChooseExpr());
	}

	// this is a catch all for the other calls in Bandera that are not supported yet! -tcw
	if((className.equals("Bandera")) && ((methodName.equals("randomClass")) || (methodName.equals("randomReachable")) ||
	   (methodName.equals("randomClassExtern")) || (methodName.equals("randomReachableExtern")))) {
	    log.warn("The factory currently doesn't support the randomClass," +
		     " randomClassExtern, randomReachable, or randomReachableExtern choose expressions.");
	    return(null);
	}

	log.warn("The factory could not find an appropriate ChooseExpr for the" +
		 " supplied class name, method name, and argument list.");
	return(null);
    }
}
