package edu.ksu.cis.bandera.checker;

import java.io.*;
import java.util.*;

import edu.ksu.cis.bandera.bir.*;
import edu.ksu.cis.bandera.birc.*;
import edu.ksu.cis.bandera.smv.*;

import edu.ksu.cis.bandera.spin.SpinTrans;
import edu.ksu.cis.bandera.checker.spin.SpinOptions;

import edu.ksu.cis.bandera.dspin.DSpinTrans;
import edu.ksu.cis.bandera.checker.dspin.DSpinOptions;

/**
 * Checker Factory pattern to generate Checker classes.
 *
 * @author Roby Joehanes
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.2 $ - $Date: 2002/12/10 22:39:49 $
 */
public class CheckerFactory {
    private static void write(String fn, String f) {
	try {
	    PrintWriter pw = new PrintWriter(new FileWriter(fn));
	    pw.print(f);
	    pw.close();
	}
	catch (Exception e) {
	    throw new RuntimeException(e.getMessage());
	}
    }

    public static Checker getChecker(String name, TransSystem sys, String option, String formula, String outputDir) {
	edu.ksu.cis.bandera.bir.Type.booleanType = new edu.ksu.cis.bandera.bir.Bool(); // need to reset this
	if ("spin".equals(name)) {
	    SpinOptions options = (SpinOptions)OptionsFactory.createOptionsInstance("Spin");
	    options.init(option);
	    if (formula != null) {
		write(sys.getName()+".ltl", formula);
		options.setApplyNeverClaim(true);
		options.setAcceptanceCycles(true);
		options.setSafety(false);
	    }
	    else {
		options.setApplyNeverClaim(false);
		options.setAcceptanceCycles(false);
		options.setSafety(true);
	    }
	    return SpinTrans.translate(sys, options, outputDir);
	}
	else if ("dspin".equals(name)) {
	    DSpinOptions options = (DSpinOptions)OptionsFactory.createOptionsInstance("DSpin");
	    options.init(option);
	    return DSpinTrans.translate(sys, options);
	}
	else if ("smv".equals(name)) {
	    return SmvTrans.translate(sys, new SmvOptions(option));
	}
	return null;
    }
}
