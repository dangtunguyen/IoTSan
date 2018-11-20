package edu.ksu.cis.bandera.bui.counterexample.objectdiagram;

import edu.ksu.cis.bandera.bui.counterexample.TraceManager;
//import edu.ksu.cis.bandera.bui.counterexample.JPFTraceManager;
import edu.ksu.cis.bandera.bui.counterexample.BIRCTraceManager;

//import edu.ksu.cis.bandera.bui.counterexample.objectdiagram.jpf.JPFObjectParser;

import edu.ksu.cis.bandera.bui.counterexample.objectdiagram.birc.BIRCObjectParser;


/**
 * The ObjectParserFactory provides a way to create an ObjectParser based
 * upon what type of TraceManager is being used in the counter-example.
 *
 * Creation date: (1/18/02 12:13:59 PM)
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 */
public class ObjectParserFactory {
    /**
     * ObjectParserFactory constructor comment.
     */
    public ObjectParserFactory() {
        super();
    }
/**
 * Get an instance of an object parser to use when constructing the object diagram.
 * The ObjectParser is based upon the type of trace manager that is being used.  So,
 * if a JPFTraceManager is given, a JPFObjectParser will be returned.  And if
 * a BIRCTraceManager is given, a BIRCObjectParser will be returned.  And if a
 * null value is given, a JavaObjectParser will be returned.  If something else
 * is given, a null value will be returned.
 *
 * Creation date: (1/18/02 12:15:17 PM)
 *
 * @return ObjectParser The object parser associated with the TraceManager given.
 * @param TraceManager traceManager The traceManager that will be used by the
 *        object parser.
 */
public static ObjectParser getInstance(TraceManager traceManager) {
	
    if (traceManager == null) {
        return (new JavaObjectParser());
    }
    
    /*if (traceManager instanceof JPFTraceManager) {
        return (new JPFObjectParser(traceManager));
    }*/

    if(traceManager instanceof BIRCTraceManager) {
	    return(new BIRCObjectParser(traceManager));
    }
    
    return (null);
}
}
