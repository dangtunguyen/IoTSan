package edu.ksu.cis.bandera.bui.counterexample.objectdiagram;

import edu.ksu.cis.bandera.bui.counterexample.TraceManager;

import edu.ksu.cis.bandera.bui.counterexample.objectdiagram.ObjectData;
import edu.ksu.cis.bandera.bui.counterexample.objectdiagram.ObjectParser;
import edu.ksu.cis.bandera.bui.counterexample.objectdiagram.ObjectDiagram;

import edu.ksu.cis.bandera.bui.counterexample.objectdiagram.gef.GEFObjectDiagram;

import edu.ksu.cis.bandera.ui.common.layout.LayoutEngine;
import edu.ksu.cis.bandera.ui.common.layout.LayoutManager;

import edu.ksu.cis.bandera.ui.common.layout.grid.gef.GEFRootedGridLayoutEngine;
import edu.ksu.cis.bandera.ui.common.layout.grid.gef.GEFRootedGridLayoutManager;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import java.util.*;

import org.apache.log4j.Category;

/**
 * This class will manage a rooted diagram.  A rooted diagram is one in which
 * a single object is at the root and the diagram is based upon the objects that
 * are reachable from the root.  This class will take care of generating the
 * diagram based upon the given root, the maximum depth requested, and the type
 * upon which it should be filtered.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.2 $ - $Date: 2003/04/30 19:33:18 $
 */
public class RootedObjectDiagramManager implements ObjectDiagramManager {

    /**
     * The ObjectDiagram in which we will be drawing.
     */
    private ObjectDiagram objectDiagram;

    /**
     * The root from which to base the diagram.
     */
    private Object rootObject;

    /**
     * The ObjectParser to use when parsing objects into ObjectData objects.
     */
    private ObjectParser objectParser;

    /**
     * This represents how much of the object diagram we will be drawing.  This will
     * match the depth of the tree created by drawing the diagram.  If it is 0, only
     * the root node will be drawn (meaning 0 references after the root node).  If it
     * is 1, all nodes that are referenced from the root node will be drawn but no
     * farther (meaning 1 reference after the root node).
     */
    private int depth;
    private ObjectData rootObjectData;
    private Set objectDataStore;
    private List excludeTypeList;
    private List includeTypeList;

    private static Category log = Category.getInstance(RootedObjectDiagramManager.class);
	
    /**
     * @param traceManager edu.ksu.cis.bandera.bui.counterexample.TraceManager
     * @param rootObject java.lang.Object
     */
    public RootedObjectDiagramManager(TraceManager traceManager, Object rootObject) {
	
	includeTypeList = new ArrayList();
	excludeTypeList = new ArrayList();

	objectParser = ObjectParserFactory.getInstance(traceManager);
	
	depth = -1;
	objectDataStore = new HashSet();
	objectDiagram = new GEFObjectDiagram();
	//LayoutEngine layoutEngine = new GEFRootedGridLayoutEngine();
	//objectDiagram.setLayoutEngine(layoutEngine, true);
	LayoutManager layoutManager = new GEFRootedGridLayoutManager();
	log.debug("Creating the layoutManager: " + layoutManager);
	objectDiagram.setLayoutManager(layoutManager, true);
	
	setRootObject(rootObject);
	
    }
    /**
     * @param rootObject java.lang.Object
     */
    public RootedObjectDiagramManager(Object rootObject) {

	this(null, rootObject);
		
    }
    /**
     * Add the references associated with the given object at the current depth
     * given.  If the currentDepth is equal to the maximum depth to show, then
     * don't show these references.  Otherwise, show these references and call
     * this method recursively to continue showing references.
     *
     * @pre objectData is not null.
     * @pre objectData is already present in the objectDiagram
     * @pre currentDepth <= depth
     * @pre objectDataStore is not null.
     * @pre objectDiagram is not null.
     * @pre includeTypeList is not null.
     * @pre excludeTypeList is not null.
     * @pre if includeTypeList.size() > 0 => excludeTypeList.size() == 0
     * @pre if excludeTypeList.size() > 0 => includeTypeList.size() == 0
     */
    private void addReferences(ObjectData objectData, int currentDepth) {

	if(objectDataStore == null) {
	    log.error("objectDataStore is null.  Cannot continue adding references.");
	    return;
	}

	if(objectData == null) {
	    log.error("objectData is null.  Cannot continue adding references.");
	    return;
	}

	if(objectDiagram == null) {
	    log.error("objectDiagram is null.  Cannot continue adding references.");
	    return;
	}

	if((excludeTypeList == null) || (includeTypeList == null)) {
	    log.error("excludeTypeList or includeTypeList are null.  This should never happen.");
	    return;
	}

        if (currentDepth == depth) {
            return;
        }

        // check to see if this object has references
        if (objectData.hasReferences()) {

            Iterator iterator = objectData.getReferences();
            int field = 0;
            while (iterator.hasNext()) {
                ObjectData currentObjectData = (ObjectData) iterator.next();

                if(currentObjectData == null) {
		    continue;
                }

                String currentClass = ((ObjectData)(currentObjectData)).getType();
                if(currentClass == null) {
		    continue;
                }

                /*
                 * Note: This should be re-worked to provide better efficiency. -tcw
                 */
                if(excludeTypeList.size() > 0) {
		    if(excludeTypeList.contains(currentClass)) {
			//System.out.println("skipping current node (type: " + currentClass.getName() + ") as the type is in the exclude list.");
			continue;
		    }
		    else {
			// type not in the exclude list, draw this node
			//System.out.println("drawing node because it is not in the exclude list.  type = " + currentClass.getName());
		    }
                }
                else {
		    if(includeTypeList.size() > 0) {
			if(includeTypeList.contains(currentClass)) {
			    // type in the include list, draw this node
			    //System.out.println("drawing node because it is in the include list.  type = " + currentClass.getName());
			}
			else {
			    //System.out.println("skipping current node (type: " + currentClass.getName() + ") as the type is not in the include list.");
			    continue;
			}
		    }
		    else {
			// include list and exclude list are empty, draw all nodes
			//System.out.println("drawing node because both lists are empty.");
		    }
                }

                if (objectDataStore.contains(currentObjectData)) {
                    // skip this one, we just need the reference
                }
                else {
                    // add this node to the diagram
                    objectDataStore.add(currentObjectData);
                    objectDiagram.addNode(currentObjectData);

                    addReferences(currentObjectData, currentDepth + 1);
                }

                objectDiagram.addReference(objectData, field, currentObjectData);
                field++;
            }

        }

    }

    /**
     * Close the current diagram and clean up objects in the system.  This might also
     * be referred to as dispose.  Not sure which name is more appropriate? -tcw
     */
    public void close() {

	// close the object diagram
	objectDiagram.close();

	// empty the exclude list
	excludeTypeList.clear();
	
	// empty the include list
	includeTypeList.clear();
	
	// empty the object data store
	objectDataStore.clear();
	
	// delete the object parser
	objectParser = null;
	
	// delete the root object
	rootObject = null;
	
	// delete the root object data
	rootObjectData = null;
    }

    /**
     * Draw the diagram based upon the root object, the depth, and the type filter.
     *
     * @pre objectDiagram is not null.
     * @pre objectParser is not null.
     * @pre objectDataStore is not null.
     */
    private void drawDiagram() {

        // clear the diagram
        objectDiagram.clear();

        // clear the local store of nodes
        objectDataStore.clear();

        // add this node to our collection
        objectDataStore.add(rootObjectData);

        // add this to the diagram
        objectDiagram.addNode(rootObjectData);

        // now add all the references for this object
        addReferences(rootObjectData, 0);

        if(objectDiagram instanceof GEFObjectDiagram) {
	    log.debug("Calling layout for the GEFObjectDiagram ...");
	    ((GEFObjectDiagram)objectDiagram).layout();
        }

    }

    /**
     *
     */
    public void emptyExcludeTypeList() {
	
	if(excludeTypeList != null) {
	    excludeTypeList.clear();
	}
	
    }

    /**
     *
     */
    public void emptyIncludeTypeList() {
	
	if(includeTypeList != null) {
	    includeTypeList.clear();
	}
	
    }

    /**
     * Get the maximum depth which should be shown in the diagram.
     */
    public int getDepth() {
        return (depth);
    }

    /**
     * @return java.util.List
     */
    public List getExcludeTypeList() {
	return excludeTypeList;
    }

    /**
     * @return java.util.List
     */
    public java.util.List getIncludeTypeList() {
	return includeTypeList;
    }

    /**
     * Get the object parse defined for this object.  This uses lazy initialization
     * so the first call to this will create a new one.
     *
     * @return ObjectParser The ObjectParser defined for this object.
     */
    private ObjectParser getObjectParser() {

	if(objectParser == null) {
	    // create a new ObjectParser using the ObjectParserFactory
	    objectParser = ObjectParserFactory.getInstance(null);
	}
	
	return(objectParser);
    }

    /**
     * Get the root object for this rooted object diagram.
     *
     * @return Object The root object for this diagram.
     */
    public Object getRootObject() {
	return(rootObject);
    }

    /**
     *
     */
    public void hide() {
	show(false);
    }

    /**
     * Set the maximum depth to show in the diagram.
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * @param newExcludeTypeList java.util.List
     */
    public void setExcludeTypeList(List newExcludeTypeList) {
	excludeTypeList = newExcludeTypeList;
	emptyIncludeTypeList();
    }

    /**
     * @param newIncludeTypeList java.util.List
     */
    public void setIncludeTypeList(java.util.List newIncludeTypeList) {
	includeTypeList = newIncludeTypeList;
	emptyExcludeTypeList();
    }

    /**
     * @param newRootObject java.lang.Object
     */
    public void setRootObject(java.lang.Object newRootObject) {
	rootObject = newRootObject;
	
	ObjectParser objectParser = getObjectParser();
	if(objectParser == null) {
	    log.error("objectParser is null.  Cannot parse the value of the rootObject into rootObjectData.");
	    rootObjectData = null;
	}
	else {
	    rootObjectData = objectParser.parse(rootObject);
	}

    }

    /**
     *
     */
    public void show() {
	show(true);
    }

    /**
     * @param showIt boolean
     */
    public void show(boolean showIt) {
	
	if(objectDiagram != null) {
	    objectDiagram.show(showIt);
	}
	
    }

    /**
     */
    public void update() {

	// update the value of the rootObject -> rootObjectData	
	ObjectParser objectParser = getObjectParser();
	if(objectParser == null) {
	    log.error("objectParser is null.  Cannot update the value of the rootObject.  Using the old value instead.");

	    // should we return here or just use the old value?
	}
	else {
	    rootObjectData = objectParser.parse(rootObject);
	}

	if(rootObjectData == null) {
	    log.error("rootObjectData is null.  Cannot update the diagram with a null value.");
	}
	else {
	    drawDiagram();
	}
    }
}
