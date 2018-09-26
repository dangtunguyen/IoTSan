package edu.ksu.cis.bandera.bui.counterexample;

import edu.ksu.cis.bandera.bui.counterexample.BipartiteGraph;

import org.apache.log4j.Category;

/**
  * The BipartiteGraphFactory will provide an easy way to create a new
  * instance of a BipartiteGraph.  This should make it easy to change
  * which implementation will be used.  
  *
  * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
  */
public class BipartiteGraphFactory {

    /**
     * The log to write messages to.
     */
    private static Category log = Category.getInstance(BipartiteGraphFactory.class);

    /**
      * A collection of BipartiteGraph class names used by the factory;
      */
    private static final String[] bipartiteGraphTypes = {
	    "edu.ksu.cis.bandera.bui.counterexample.OpenJGraphBipartiteGraph",
	    "edu.ksu.cis.bandera.bui.counterexample.JGraphBipartiteGraph",
	    "edu.ksu.cis.bandera.bui.counterexample.GrappaBipartiteGraph",
	    "edu.ksu.cis.bandera.bui.counterexample.JGoBipartiteGraph"
	    };

    /**
      * This is the attribute to use if you want to create a graph of type OpenJGraphBipartiteGraph.  It
      * uses the Open Source (LGPL) graph library OpenJGraph.  [add URL to project]
      */
    public static final int OPENJGRAPH = 0;

    /**
      * This is the attribute to use if you want to create a graph of type OpenJGraphBipartiteGraph.  It
      * uses the Open Source (LGPL) graph library JGraph.  [add URL to project]
      */
    public static final int JGRAPH = 1;

    /**
      * This is the attribute to use if you want to create a graph of type GrappaBipartiteGraph.  It
      * uses the Open Source (LGPL) graph library Grappa.  [add URL to project]
      */
    public static final int GRAPPA = 2;

    /**
      * This is the attribute to use if you want to create a graph of type JGoBipartiteGraph.  It
      * uses the proprietary graphing library from Northwoods. [add URL to company]
      */
    public static final int JGO = 3;

    /**
      * This is the default class to use.  We have it set to the OpenJGraph version.
      */
    private static final int DEFAULT_CLASS = OPENJGRAPH;


    /**
      * Create a new instance of a BipartiteGraph of the default type.
      *
      * @return BipartiteGraph A new blank graph.  If an error occurs, null will be returned.
      */
    public static BipartiteGraph getInstance() {

        BipartiteGraph graph = getInstance(DEFAULT_CLASS);

        return(graph);
    }
    /**
      * Create a new instance of a BipartiteGraph of the type specified.
      *
      * @return BipartiteGraph A new blank graph.  If an error occurs, null will be returned.
      */
    public static BipartiteGraph getInstance(int type) {

        BipartiteGraph graph = null;

        switch(type) {

            // list all valid types here. -todd
            case OPENJGRAPH:
            case JGRAPH:
            case GRAPPA:
            case JGO:
                // ok, we have been given a valid type!  create it and return it!
                try {
                    //Object bipartiteGraphObject = bipartiteGraphs[type].newInstance();
                    Object bipartiteGraphObject = Class.forName(bipartiteGraphTypes[type]).newInstance();
                    if(bipartiteGraphObject instanceof BipartiteGraph) {
                        graph = (BipartiteGraph)bipartiteGraphObject;
                    }
                    else {
                        log.error("This is really bad.  The factory is broken.  The class created is " +
                                           "not of type BipartiteGraph.  Instead it is a " + bipartiteGraphObject.getClass().getName() +
                                           ".  The factory must be fixed or it will not work properly.");
                        graph = null;

                    }
                }
                catch(Exception e) {
                    log.error("This is really bad.  The factory is broken.  An exception was caught.", e);
                    graph = null;
                }
                break;

            default:
                // this is the catch for all invalid types given.  -todd
                graph = null;
                log.error("Invalid type given (" + type + ").  Must be a valid type to instantiate.");
        }

        return(graph);
    }
}
