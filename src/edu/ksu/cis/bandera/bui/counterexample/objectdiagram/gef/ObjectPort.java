package edu.ksu.cis.bandera.bui.counterexample.objectdiagram.gef;

import org.tigris.gef.graph.*;
import org.tigris.gef.graph.presentation.*;
import java.io.Serializable;

import org.apache.log4j.Category;


public class ObjectPort extends NetPort implements Serializable {

	private static Category log = Category.getInstance(ObjectPort.class);

    public ObjectPort(NetNode parent) {
        super(parent);
        if(!(parent instanceof ObjectNode)) {
            log.error("ObjectPort objects are only to be used on ObjectNodes.");
        }
    }
    public boolean canConnectTo(GraphModel gm, Object anotherPort) {
	    
	    if(anotherPort == null) {
		    return(false);
	    }
	    
	    if(super.canConnectTo(gm, anotherPort)) {
		    
		    if(this.getClass() == anotherPort.getClass()) {
			    return(true);
		    }
		    
			if(this.getClass().isInstance(anotherPort.getClass())) {
				return(true);
			}
	    }
	    
	    return(false);
    }
    protected Class defaultEdgeClass(NetPort otherPort) {
           return(ObjectEdge.class);
    }
}
